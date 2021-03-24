package com.ustadmobile.xmlpullparserkmp

/**
 * These are normally static flags on XmlPullParser. This however does not work with Kotlin expect/actual. These
 * constants have therefor been copied here for use in Kotlin Multiplatform.
 */
object XmlPullParserConstants {

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

    const val CDSECT = 5

    const val DOCDECL = 10

    val FEATURE_PROCESS_NAMESPACES = "http://xmlpull.org/v1/doc/features.html#process-namespaces"
}