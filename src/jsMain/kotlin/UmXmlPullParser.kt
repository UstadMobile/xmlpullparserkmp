import ParserEvent.Companion.END_DOCUMENT
import ParserEvent.Companion.END_TAG
import ParserEvent.Companion.START_DOCUMENT
import ParserEvent.Companion.START_TAG
import kotlinx.browser.document
import org.w3c.dom.Node
import org.w3c.dom.NodeFilter
import org.w3c.dom.TreeWalker
import org.w3c.dom.parsing.DOMParser

class UmXmlPullParser: XmlPullParser {

    lateinit var treeWalker: TreeWalker

    private val eventsStack = mutableListOf<Pair<Int, ParserEvent>>()

    private val parentNodesStack = mutableListOf<Node>()

    private var nextNode: Node? = null

    private var currentEvent: Pair<Int, ParserEvent>? = null

    private var lastParentNode: Node? = null

    override fun setInput(content: String) {
        val domParser = DOMParser()
        treeWalker = document.createTreeWalker(domParser.parseFromString(content,
            "text/${if(content.startsWith("<?xml")) "xml" else "html"}"),
            NodeFilter.SHOW_ALL) { NodeFilter.FILTER_ACCEPT }
        logParserEvents()
    }

    private fun logParserEvents(){
        while ({ nextNode = treeWalker.nextNode(); nextNode }() != null){
            eventsStack.add(START_TAG to ParserEvent().apply {
                eventNode = nextNode
                eventNodeDepth = parentNodesStack.size + 1
            })

            if(nextNode?.hasChildNodes() == false){
                eventsStack.add(END_TAG to ParserEvent().apply {
                    eventNode = nextNode
                    eventNodeDepth = parentNodesStack.size + 1
                })
            }

            if(parentNodesStack.isEmpty() || nextNode?.hasChildNodes() == true){
                nextNode?.let { parentNodesStack.add(it) }
            }

            val currentParentNode = parentNodesStack.last()
            if(currentParentNode.lastChild == nextNode){
                eventsStack.add(END_TAG to ParserEvent().apply {
                    eventNode = currentParentNode
                    eventNodeDepth = parentNodesStack.indexOf(currentParentNode) + 1
                })
                parentNodesStack.removeLastOrNull()

                if(currentParentNode.parentNode?.nextSibling != null
                    && currentParentNode.parentNode?.lastChild == currentParentNode){
                    eventsStack.add(END_TAG to ParserEvent().apply {
                        eventNode = currentParentNode.parentNode
                        eventNodeDepth = parentNodesStack.indexOf(currentParentNode.parentNode) + 1
                    })
                    parentNodesStack.removeLastOrNull()
                }
            }
        }

        while ({ lastParentNode = parentNodesStack.removeLastOrNull(); lastParentNode }() != null){
            eventsStack.add(END_TAG to ParserEvent().apply {
                eventNode = lastParentNode
                eventNodeDepth = parentNodesStack.size + 1
            })
        }

        eventsStack.add(0, START_DOCUMENT to ParserEvent().apply {
            eventNode = treeWalker.root
        })

        eventsStack.add(END_DOCUMENT to ParserEvent().apply {
            eventNode = treeWalker.root
        })
        eventsStack.reverse()
    }


    override fun getDepth(): Int {
        TODO("Not yet implemented")
    }

    override fun isWhitespace(): Boolean {
        TODO("Not yet implemented")
    }

    override fun getText(): String? {
        TODO("Not yet implemented")
    }

    override fun getNamespace(): String? {
        TODO("Not yet implemented")
    }

    override fun getNamespace(prefix: String?): String? {
        TODO("Not yet implemented")
    }

    override fun getName(): String? {
        TODO("Not yet implemented")
    }

    override fun getPrefix(): String? {
        TODO("Not yet implemented")
    }

    override fun getAttributeCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getEventType(): Int {
        TODO("Not yet implemented")
    }

    override fun getProperty(name: String): Any? {
        TODO("Not yet implemented")
    }

    override fun getNamespaceCount(depth: Int): Int {
        TODO("Not yet implemented")
    }

    override fun getNamespacePrefix(pos: Int): String? {
        TODO("Not yet implemented")
    }

    override fun getNamespaceUri(pos: Int): String? {
        TODO("Not yet implemented")
    }

    override fun getAttributeNamespace(index: Int): String? {
        TODO("Not yet implemented")
    }

    override fun getAttributeName(index: Int): String? {
        TODO("Not yet implemented")
    }

    override fun getAttributePrefix(index: Int): String? {
        TODO("Not yet implemented")
    }

    override fun getAttributeValue(index: Int): String? {
        TODO("Not yet implemented")
    }

    override fun getAttributeValue(namespace: String?, name: String): String? {
        TODO("Not yet implemented")
    }

    override fun next(): Int {
        currentEvent = eventsStack.removeLastOrNull()
        return currentEvent?.first ?: -1
    }
}