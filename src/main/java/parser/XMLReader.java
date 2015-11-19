package parser;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

/**
 * Created by alla on 01.11.15.
 */
public class XMLReader {
    public  Object readXML(String fileName) throws IOException, SAXException, ParserConfigurationException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XmlParser xmlparser = new XmlParser();
            parser.parse(fileName, xmlparser);
            return xmlparser.getObject();
        }
        catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
