import org.w3c.dom.Node

class ParserEvent {

    var eventNode: Node? = null

    var eventNodeDepth: Int = -1


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
    }
}