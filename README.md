## XmlPullParser For Kotlin Multiplatform (JS/Android/JVM)

This provides a basic working implementation of the XmlPullParser API for Kotlin multiplatform 
(JS, Android, and JVM).

On Android/JVM: It just uses expect/actual and links to kXML implementation

On JS: It uses the browser's native Xml parsing and the TreeWalker API

Sorry, no iOS or native implementation (yet). Pull requests welcome.

