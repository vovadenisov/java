package parser;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
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
    public  Object readXML(String fileName) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XmlParser xmlparser = new XmlParser();
            parser.parse(fileName, xmlparser);
            return xmlparser.getObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
