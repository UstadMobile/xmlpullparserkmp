package com.ustadmobile.xmlpullparserkmp

expect interface XmlSerializer {

    fun startDocument (encoding: String, standalone: Boolean)

    fun setPrefix(prefix: String, namespace: String)

    fun getPrefix(namespace: String?, generatePrefix: Boolean): String?

    fun startTag(namespace: String?, name: String): XmlSerializer

    fun attribute(namespace: String?, name: String, value: String): XmlSerializer

    fun text(text: String): XmlSerializer

    fun endTag(namespace: String?, name: String): XmlSerializer

    fun endDocument()
}