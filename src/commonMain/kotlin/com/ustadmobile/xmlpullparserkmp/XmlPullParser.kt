package com.ustadmobile.xmlpullparserkmp

/**
 * Interface responsible for defining all the parsing functionality
 *
 */
expect interface XmlPullParser {

    /**
     * Returns the current depth of the element.
     */
    fun getDepth(): Int

    /**
     * Check if current TEXT event contains only whitespace characters.
     */
    fun isWhitespace(): Boolean

    /**
     * Read text content of the current event as String.
     */
    fun getText(): String?

    /**
     * Returns the namespace URI of the current element.
     */
    fun getNamespace(): String?


    /**
     * Returns the name of the current element
     */
    fun getName(): String?

    /**
     * Returns the prefix of the current element or null if element has no prefix
     */
    fun getPrefix(): String?

    /**
     * Returns the number of attributes on the current element;
     * -1 if the current event is not START_TAG
     */
    fun getAttributeCount(): Int

    /**
     * Returns the type of the current event (START_TAG, END_TAG, TEXT, etc.)
     */
    fun getEventType(): Int

    /**
     * Use this call to change the general behaviour of the parser, such as namespace processing
     */
    fun setFeature(name: String, state: Boolean)

    /**
     * Return the current value of the feature with given name.
     */
    fun getFeature(name: String): Boolean

    /**
     * Return position in stack of first namespace slot for element at passed depth.
     * If namespaces are not enabled it returns always 0.
     */
    fun getNamespaceCount(depth: Int): Int

    /**
     * Return namespace prefixes for position pos in namespace stack
     */
    fun getNamespacePrefix(pos: Int): String?

    /**
     * Return namespace URIs for position pos in namespace stack
     * If pos is out of range it throw exception.
     */
    fun getNamespaceUri(pos: Int): String

    /**
     * Return uri for the given prefix.
     */
    fun getNamespace(prefix: String?): String?

    /**
     * Returns the namespace URI of the specified attribute
     * number index (starts from 0).
     */
    fun getAttributeNamespace(index: Int): String

    /**
     * Returns the local name of the specified attribute
     */
    fun getAttributeName(index: Int): String

    /**
     * Returns the prefix of the specified attribute
     */
    fun getAttributePrefix(index: Int): String?

    /**
     * Returns the given attributes value
     */
    fun getAttributeValue(index: Int): String

    /**
     * Returns the attributes value identified by namespace URI and namespace localName.
     */
    fun getAttributeValue(namespace: String?, name: String): String?

    /**
     * Net next parsing event
     */
    operator fun next(): Int

    fun nextToken(): Int

    fun nextText(): String?

    fun nextTag(): Int
}
