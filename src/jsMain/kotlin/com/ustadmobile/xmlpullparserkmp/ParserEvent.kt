package com.ustadmobile.xmlpullparserkmp

import org.w3c.dom.Node

open class ParserEvent {

    var eventNode: Node? = null

    var eventNodeDepth: Int = -1

    var eventType: Int = -1

}