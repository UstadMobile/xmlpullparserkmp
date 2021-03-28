package com.ustadmobile.xmlpullparserkmp

import java.io.StringReader

actual fun XmlPullParser.setInputString(xmlString: String) {
    setInput(StringReader(xmlString))
}
