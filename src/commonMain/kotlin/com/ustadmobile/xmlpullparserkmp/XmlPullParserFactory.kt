package com.ustadmobile.xmlpullparserkmp

expect open class XmlPullParserFactory {

    open fun setFeature(name: String, state: Boolean)

    open fun getFeature(name: String): Boolean

    open fun setNamespaceAware(awareness: Boolean)

    open fun isNamespaceAware(): Boolean

    open fun setValidating(validating: Boolean)

    open fun isValidating(): Boolean

    open fun newPullParser(): XmlPullParser

    open fun newSerializer(): XmlSerializer

}