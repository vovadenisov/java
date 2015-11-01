package parser;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by alla on 26.10.15.
 */
public class XmlParser extends DefaultHandler{
        private String thisElement = "";
        private Map<String, String> fields;
        private static final String[] keys = {"param1", "param2", "param3"};
        public Map<String, String> getParam(){
            return fields;
        }
        @Override
        public void startDocument() throws SAXException
        {
            fields = new HashMap<>();
        }
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
        {
            thisElement = qName;
        }
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException
        {
            if (thisElement.equals("param1")) {
                fields.put("param1", thisElement);
            }
            if (thisElement.equals("param2")) {
                fields.put("param2", thisElement);
            }
            if (thisElement.equals("param3")) {
                fields.put("param3", thisElement);
            }
        }
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException
        {
            thisElement = "";
        }
        @Override
        public void endDocument() throws SAXException
        {
        }
    }

