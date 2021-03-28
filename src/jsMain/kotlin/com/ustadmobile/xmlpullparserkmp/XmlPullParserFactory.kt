package com.ustadmobile.xmlpullparserkmp

actual open class XmlPullParserFactory {

    private val features = mutableMapOf<String, Boolean>()

    actual open fun setFeature(name: String, state: Boolean) {
        features[name] = state
    }

    actual open fun getFeature(name: String): Boolean = features[name] ?: false

    actual open fun setNamespaceAware(awareness: Boolean) {
        setFeature(FEATURE_NAMESPACE, awareness)
    }

    actual open fun isNamespaceAware(): Boolean = getFeature(FEATURE_NAMESPACE)

    actual open fun setValidating(validating: Boolean) {
        setFeature(FEATURE_VALIDATING, validating)
    }

    actual open fun isValidating(): Boolean = getFeature(FEATURE_VALIDATING)

    actual open fun newPullParser(): XmlPullParser {
        val xpp = XmlPullParserJsImpl()
        features.forEach {
            xpp.setFeature(it.key, it.value)
        }

        return xpp
    }

    actual open fun newSerializer(): XmlSerializer {
        throw IllegalStateException("Not available on Javascript yet")
    }

    companion object {

        fun newInstance() = XmlPullParserFactory()

        const val FEATURE_VALIDATING = "http://xmlpull.org/v1/doc/features.html#validation"

        const val FEATURE_NAMESPACE = "http://xmlpull.org/v1/doc/features.html#process-namespaces"
    }


}