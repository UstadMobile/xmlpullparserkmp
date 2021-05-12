package com.ustadmobile.xmlpullparserkmp

actual interface XmlSerializer {

    actual fun docdecl(dd: String)

    actual fun startDocument(encoding: String, standalone: Boolean)

    actual fun setPrefix(prefix: String, namespace: String)

    actual fun getPrefix(namespace: String?, generatePrefix: Boolean): String?

    actual fun startTag(namespace: String?, name: String): XmlSerializer

    actual fun attribute(namespace: String?, name: String, value: String): XmlSerializer

    actual fun text(text: String): XmlSerializer

    actual fun cdsect(text: String)

    actual fun endTag(namespace: String?, name: String): XmlSerializer

    actual fun endDocument()

    actual fun entityRef(text: String)

    actual fun flush()
}