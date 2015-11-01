package parser;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
/**
 * Created by alla on 01.11.15.
 */
public class XMLReader {
    public  Map<String, String> readXML(String fileName) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XmlParser xmlparser = new XmlParser();
            parser.parse(fileName, xmlparser);
            Map A = xmlparser.getParam();
            return A;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
