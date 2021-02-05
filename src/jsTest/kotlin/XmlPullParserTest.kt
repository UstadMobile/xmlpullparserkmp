import kotlin.test.Test
import kotlin.test.assertEquals

class XmlPullParserTest {

    private val XML_CONTENT = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<package xmlns=\"http://www.idpf.org/2007/opf\" version=\"3.0\" xml:lang=\"en\" unique-identifier=\"uid\" prefix=\"cc: http://creativecommons.org/ns#\">\n" +
            "  <metadata xmlns:dc=\"http://purl.org/dc/elements/1.1/\">\n" +
            "    <dc:title id=\"title\">Creative Commons - A Shared Culture</dc:title>\n" +
            "    <dc:creator>Jesse Dylan</dc:creator>\n" +
            "    <dc:identifier id=\"uid\">code.google.com.epub-samples.cc-shared-culture</dc:identifier>\n" +
            "    <dc:language>en-US</dc:language>\n" +
            "    <meta property=\"dcterms:modified\">2012-01-20T12:47:00Z</meta>\n" +
            "    <dc:publisher>Creative Commons</dc:publisher>  \n" +
            "    <dc:contributor>mgylling</dc:contributor>\n" +
            "    <dc:description>Multiple video tests (see Navigation Document (toc) for details)</dc:description>\n" +
            "    <dc:rights>This work is licensed under a Creative Commons Attribution-Noncommercial-Share Alike (CC BY-NC-SA) license.</dc:rights>               \n" +
            "  </metadata>\n" +
            "  <manifest>\n" +
            "    <item id=\"font1\" href=\"fonts/Quicksand_Light.otf\" media-type=\"application/vnd.ms-opentype\"/>\n" +
            "    <item id=\"font2\" href=\"fonts/Quicksand_Bold_Oblique.otf\" media-type=\"application/vnd.ms-opentype\"/>                       \n" +
            "  </manifest>\n" +
            "  <spine>\n" +
            "    <itemref idref=\"cover\" linear=\"no\"/>\n" +
            "    <itemref idref=\"toc\"/>\n" +
            "  </spine>\n" +
            "</package>\n"

    private val XHTML_CONTENT = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:epub=\"http://www.idpf.org/2007/ops\" xml:lang=\"en\" lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"utf-8\"/>\n" +
            "</head>\n" +
            "<body>\n" +
            "<nav epub:type=\"toc\" id=\"toc\">\n" +
            "    <ol>\n" +
            "        <li><a href=\"Page_1.xhtml\">Page 1</a></li>\n" +
            "        <li><a href=\"Page_2.xhtml\">Page 2</a></li>\n" +
            "        <li><a href=\"Page_3.xhtml\">Page 3</a></li>\n" +
            "        <li><a href=\"Page_4.xhtml\">Page 4</a></li>\n" +
            "        <li><a href=\"Page_5.xhtml\">Page 5</a></li>\n" +
            "        <li><a href=\"Page_6.xhtml\">Page 6</a></li>\n" +
            "        <li><a href=\"Acknowledgements.xhtml\">Acknowledgements</a></li>\n" +
            "    </ol>\n" +
            "</nav>\n" +
            "</body>\n" +
            "</html>"

    @Test
    fun givenParsedXmlContent_whenFirstNextIsCalled_ShouldBeDocumentRead(){
        val xmlPullParser = UmXmlPullParser()
        xmlPullParser.setInput(XML_CONTENT)
        assertEquals(ParserEvent.START_DOCUMENT,xmlPullParser.next())
    }

    @Test
    fun givenParsedXhtmlContent_whenFirstNextIsCalled_ShouldBeDocumentRead(){
        val xmlPullParser = UmXmlPullParser()
        xmlPullParser.setInput(XHTML_CONTENT)
        assertEquals(ParserEvent.START_DOCUMENT,xmlPullParser.next())
    }

    @Test
    fun givenParsedXmlContentAfterDocumentRead_whenNextIsCalled_ShouldBeTagRead(){
        val xmlPullParser = UmXmlPullParser()
        xmlPullParser.setInput(XML_CONTENT)
        //read document
        xmlPullParser.next()
        assertEquals(ParserEvent.START_TAG,xmlPullParser.next())
    }

    @Test
    fun givenParsedXhtmlContentAfterDocumentRead_whenNextIsCalled_ShouldBeTagRead(){
        val xmlPullParser = UmXmlPullParser()
        xmlPullParser.setInput(XHTML_CONTENT)
        //read document
        xmlPullParser.next()
        assertEquals(ParserEvent.START_TAG,xmlPullParser.next())
    }

}