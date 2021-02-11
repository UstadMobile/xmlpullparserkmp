/**
 * Interface responsible for defining all the parsing functionality
 *
 */
actual interface XmlPullParserKmp {
    /**
     * Returns the current depth of the element.
     */
    actual fun getDepth(): Int

    /**
     * Check if current TEXT event contains only whitespace characters.
     */
    actual fun isWhitespace(): Boolean

    /**
     * Read text content of the current event as String.
     */
    actual fun getText(): String?

    /**
     * Returns the namespace URI of the current element.
     */
    actual fun getNamespace(): String?

    /**
     * Returns the name of the current element
     */
    actual fun getName(): String?

    /**
     * Returns the prefix of the current element or null if element has no prefix
     */
    actual fun getPrefix(): String?

    /**
     * Returns the number of attributes on the current element;
     * -1 if the current event is not START_TAG
     */
    actual fun getAttributeCount(): Int

    /**
     * Returns the type of the current event (START_TAG, END_TAG, TEXT, etc.)
     */
    actual fun getEventType(): Int

    /**
     * Use this call to change the general behaviour of the parser, such as namespace processing
     */
    actual fun setFeature(name: String, state: Boolean)

    /**
     * Return the current value of the feature with given name.
     */
    actual fun getFeature(name: String): Boolean

    /**
     * Return position in stack of first namespace slot for element at passed depth.
     * If namespaces are not enabled it returns always 0.
     */
    actual fun getNamespaceCount(depth: Int): Int

    /**
     * Return namespace prefixes for position pos in namespace stack
     */
    actual fun getNamespacePrefix(pos: Int): String?

    /**
     * Return namespace URIs for position pos in namespace stack
     * If pos is out of range it throw exception.
     */
    actual fun getNamespaceUri(pos: Int): String?

    /**
     * Return uri for the given prefix.
     */
    actual fun getNamespace(prefix: String?): String?

    /**
     * Returns the namespace URI of the specified attribute
     * number index (starts from 0).
     */
    actual fun getAttributeNamespace(index: Int): String?

    /**
     * Returns the local name of the specified attribute
     */
    actual fun getAttributeName(index: Int): String?

    /**
     * Returns the prefix of the specified attribute
     */
    actual fun getAttributePrefix(index: Int): String?

    /**
     * Returns the given attributes value
     */
    actual fun getAttributeValue(index: Int): String?

    /**
     * Returns the attributes value identified by namespace URI and namespace localName.
     */
    actual fun getAttributeValue(namespace: String?, name: String): String?

    /**
     * Net next parsing event
     */
    actual operator fun next(): Int

    /**
     * Set source content to be parsed
     */
    fun setInput(content: String)


    companion object {

        /**
         * Event fired when document is being read
         */
        const val START_DOCUMENT = 0

        /**
         * Event fired when document read is completed
         */
        const val END_DOCUMENT = 1

        /**
         * Event fired when node is being read
         */
        const val START_TAG = 2

        /**
         * Event fired when node read is completed
         */
        const val END_TAG = 3

        /**
         * Event fired when text node is being read
         */
        const val TEXT = 4

        val FEATURE_PROCESS_NAMESPACES = "http://xmlpull.org/v1/doc/features.html#process-namespaces"
    }
}