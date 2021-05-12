package com.ustadmobile.xmlpullparserkmp

import com.ustadmobile.xmlpullparserkmp.XmlPullParserConstants.END_DOCUMENT
import com.ustadmobile.xmlpullparserkmp.XmlPullParserConstants.END_TAG
import com.ustadmobile.xmlpullparserkmp.XmlPullParserConstants.PROPERTY_STANDALONE
import com.ustadmobile.xmlpullparserkmp.XmlPullParserConstants.PROPERTY_VERSION
import com.ustadmobile.xmlpullparserkmp.XmlPullParserConstants.START_DOCUMENT
import com.ustadmobile.xmlpullparserkmp.XmlPullParserConstants.START_TAG
import com.ustadmobile.xmlpullparserkmp.XmlPullParserConstants.TEXT
import kotlinx.browser.document
import org.w3c.dom.*
import org.w3c.dom.parsing.DOMParser

class XmlPullParserJsImpl: XmlPullParser {

    private lateinit var treeWalker: TreeWalker

    private lateinit var document: Document

    private val eventsStack = mutableListOf<ParserEvent>()

    private val parentNodesStack = mutableListOf<Node>()

    private var nextNode: Node? = null

    private var currentEvent: ParserEvent? = null

    private var lastParentNode: Node? = null

    private var processNsp:Boolean = false

    private var relaxed: Boolean = true

    override fun setInput(content: String) {
        document = DOMParser().parseFromString(content,
            "text/${if(content.startsWith("<?xml")) "xml" else "html"}")
        treeWalker = document.createTreeWalker(document, NodeFilter.SHOW_ALL) { NodeFilter.FILTER_ACCEPT }
        logParserEvents()
    }

    private fun isProp(ns1: String, prop: Boolean, ns2: String): Boolean {
        if (!ns1.startsWith("http://xmlpull.org/v1/doc/"))
            return false
        return if (prop)
            ns1.substring(42) == ns2
        else
            ns1.substring(40) == ns2
    }

    private fun logParserEvents(){
        while ({ nextNode = treeWalker.nextNode(); nextNode }() != null){

            if(nextNode?.nodeType == Node.TEXT_NODE){
                eventsStack.add(ParserEvent().apply {
                    eventNode = nextNode
                    eventType = TEXT
                    eventNodeDepth = parentNodesStack.size + 1
                })
            }

            eventsStack.add(ParserEvent().apply {
                eventNode = nextNode
                eventType = START_TAG
                eventNodeDepth = parentNodesStack.size + 1
            })

            if(nextNode?.hasChildNodes() == false){
                eventsStack.add(ParserEvent().apply {
                    eventNode = nextNode
                    eventType = END_TAG
                    eventNodeDepth = parentNodesStack.size + 1
                })
            }

            if(parentNodesStack.isEmpty() || nextNode?.hasChildNodes() == true){
                nextNode?.let { parentNodesStack.add(it) }
            }

            val currentParentNode = parentNodesStack.last()
            if(currentParentNode.lastChild == nextNode){
                eventsStack.add(ParserEvent().apply {
                    eventNode = currentParentNode
                    eventType = END_TAG
                    eventNodeDepth = parentNodesStack.indexOf(currentParentNode) + 1
                })
                parentNodesStack.removeLastOrNull()

                if(currentParentNode.parentNode?.nextSibling != null
                    && currentParentNode.parentNode?.lastChild == currentParentNode){
                    eventsStack.add(ParserEvent().apply {
                        eventNode = currentParentNode.parentNode
                        eventType = END_TAG
                        eventNodeDepth = parentNodesStack.indexOf(currentParentNode.parentNode) + 1
                    })
                    parentNodesStack.removeLastOrNull()
                }
            }
        }

        while ({ lastParentNode = parentNodesStack.removeLastOrNull(); lastParentNode }() != null){
            eventsStack.add(ParserEvent().apply {
                eventNode = lastParentNode
                eventType = END_TAG
                eventNodeDepth = parentNodesStack.size + 1
            })
        }

        eventsStack.add(0, ParserEvent().apply {
            eventNode = treeWalker.root
            eventType = START_DOCUMENT
        })

        eventsStack.add(ParserEvent().apply {
            eventNode = treeWalker.root
            eventType = END_DOCUMENT
        })
        eventsStack.reverse()
    }

    private fun getAttributes(): List<Attr>{
        return getCurrentEventElement()?.attributes?.asList()?: listOf()
    }

    private fun getCurrentEventElement(event: ParserEvent? = null): Element?{
        val mElement = event ?: currentEvent
        mElement?.eventNode?.appendChild(document.createTextNode("text"))
        return mElement?.eventNode?.lastChild?.parentElement
    }

    private fun isStartOrEndTag():Boolean{
        return  (currentEvent?.eventType == START_TAG
                || currentEvent?.eventType == END_TAG)
    }

    private fun isNsEnabledAndStartOrEndTag(): Boolean {
        return processNsp && isStartOrEndTag()
    }


    override fun getDepth(): Int {
        return currentEvent?.eventNodeDepth ?: -1
    }

    override fun isWhitespace(): Boolean {
        if(currentEvent?.eventType == TEXT){
            return getText().isNullOrEmpty()
        }
        return false
    }

    override fun getText(): String? {
        return if(currentEvent?.eventNode?.nodeType == Node.TEXT_NODE){
            currentEvent?.eventNode?.textContent
        } else null
    }

    override fun getNamespace(): String? {
        return  if(isNsEnabledAndStartOrEndTag())
            getCurrentEventElement()?.namespaceURI
        else if (!processNsp) ""  else null
    }

    override fun getNamespace(prefix: String?): String? {
        return  getCurrentEventElement()?.lookupNamespaceURI(prefix)
    }

    override fun getName(): String? {
        return currentEvent?.eventNode?.nodeName?.toLowerCase()
    }

    override fun getPrefix(): String? {
        return if(isNsEnabledAndStartOrEndTag())
            currentEvent?.eventNode?.lookupPrefix(null) else null
    }

    override fun getAttributeCount(): Int {
        val currentNode = currentEvent
        return if(currentNode != null && currentNode.eventType == START_TAG &&
            currentNode.eventNode?.nodeType == Node.ELEMENT_NODE){
            getAttributes().size
        } else  -1
    }

    override fun getEventType(): Int {
        return currentEvent?.eventType ?: -1
    }

    override fun setFeature(name: String, state: Boolean) {
        when {
            XmlPullParserConstants.FEATURE_PROCESS_NAMESPACES == name -> processNsp = state
            isProp(name, false, "relaxed") -> relaxed = state
            else -> throw Exception("unsupported feature: $name")
        }
    }

    override fun getFeature(name: String): Boolean {
        return when {
            XmlPullParserConstants.FEATURE_PROCESS_NAMESPACES == name -> processNsp
            isProp(name, false, "relaxed") -> relaxed
            else -> false
        }
    }


    override fun getNamespaceCount(depth: Int): Int {
        val namespaceSet = mutableSetOf<String>()
        val mDepth = depth + if(currentEvent?.eventType == END_TAG) 1 else 0
        eventsStack.filter { it.eventNodeDepth == mDepth}.forEach {
            val namespace = getCurrentEventElement(it)?.namespaceURI
            if(namespace != null){
                namespaceSet.add(namespace)
            }
        }
        return if(processNsp) namespaceSet.size else 0
    }

    override fun getNamespacePrefix(pos: Int): String? {
        val attributes = getAttributes()
        return attributes[pos].prefix
    }

    override fun getNamespaceUri(pos: Int): String {
        val attributes = getAttributes()
        return if(attributes.size < pos) throw XmlPullParserException("")  else attributes[pos].namespaceURI?:""
    }

    override fun getAttributeNamespace(index: Int): String {
        val ns = getNamespaceUri(index)
        return if(!processNsp || ns == null) "" else if(currentEvent?.eventType != START_TAG)
            throw IndexOutOfBoundsException() else ns
    }

    override fun getAttributeName(index: Int): String {
        val attributes = getAttributes()
        return if(currentEvent?.eventType != START_TAG || attributes.size < index) throw IndexOutOfBoundsException()
        else if(processNsp) attributes[index].name else attributes[index].localName
    }

    override fun getAttributePrefix(index: Int): String? {
        val attributes = getAttributes()
        return if(currentEvent?.eventType != START_TAG) throw IndexOutOfBoundsException()
        else if(attributes.isNotEmpty() && processNsp) attributes[index].prefix else null
    }

    override fun getAttributeValue(index: Int): String {
        val attributes = getAttributes()
        return when {
            currentEvent?.eventType != START_TAG || attributes.size < index -> throw IndexOutOfBoundsException()
            else -> attributes[index].value
        }
    }

    override fun getAttributeValue(namespace: String?, name: String): String? {
        return when {
            currentEvent?.eventType != START_TAG -> throw IndexOutOfBoundsException()
            else -> getAttributes().firstOrNull {(it.name == name || it.name.contains(name))
                    && (it.namespaceURI == null || it.namespaceURI == namespace)}?.value
        }
    }

    override fun next(): Int {
        currentEvent = eventsStack.removeLastOrNull()
        return currentEvent?.eventType ?: -1
    }

    override fun nextToken(): Int = next()

    override fun nextTag(): Int {
        next()

        if(currentEvent?.eventType == TEXT && currentEvent?.eventNode?.textContent?.isBlank() == true)
            next()

        if(currentEvent?.let { it.eventType == START_TAG || it.eventType == END_TAG } != true)
            throw XmlPullParserException("nextTag: expected START_TAG or END_TAG")

        return currentEvent?.eventType ?: -1
    }

    override fun nextText(): String? {
        //Implemented in accordance with the original kXML
        if(currentEvent?.eventType != START_TAG)
            throw XmlPullParserException("nextText: precondition: current event must be start_tag")

        next()

        val result: String?
        if(currentEvent?.eventType == TEXT) {
            result = getText()
            next()
            /*
            TreeWalker parsing is different, for every text node the inner content is treated as text node too,
            so to make sure we maintain the logic we have to come out of that inner text node by calling next()
            again, otherwise it will always throw an exception since the next event wont be END_TAG
            */
            next()
        }else {
            result = ""
        }
        if(currentEvent?.eventType != END_TAG){
            throw XmlPullParserException("nextText: END_TAG expected")
        }

        return result
    }

    override fun getProperty(name: String): Any? {
        return when(name) {
            PROPERTY_VERSION -> "1.0"
            PROPERTY_STANDALONE -> {
                (document as? XMLDocument)?.asDynamic()?.xmlStandalone
            }
            else -> null
        }
    }

    override fun getInputEncoding() = document.inputEncoding

    override fun getNode(): Node? {
        return currentEvent?.eventNode
    }
}