package com.ustadmobile.xmlpullparserkmp

expect interface XmlSerializer {

    fun docdecl(dd: String)

    fun startDocument (encoding: String, standalone: Boolean)

    fun setPrefix(prefix: String, namespace: String)

    fun getPrefix(namespace: String?, generatePrefix: Boolean): String?

    fun startTag(namespace: String?, name: String): XmlSerializer

    fun attribute(namespace: String?, name: String, value: String): XmlSerializer

    fun text(text: String): XmlSerializer

    fun cdsect(text: String)

    fun entityRef(text: String)

    fun endTag(namespace: String?, name: String): XmlSerializer

    fun endDocument()

    fun flush()
}