package com.ustadmobile.xmlpullparserkmp

/**
 * Expect / actual that allows setting XmlPullProcessor input from a String. On JVM/Android this will use the
 * StringReader class. On Javascript it will directly parse the contents
 */
expect fun XmlPullParser.setInputString(xmlString: String)
