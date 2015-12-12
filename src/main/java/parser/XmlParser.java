package parser;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import reflection.Reflection;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by alla on 26.10.15.
 */
public class XmlParser extends DefaultHandler{
        private String thisElement = "";
        private ArrayList<Object> fields;
        private Object object;
        public ArrayList<Object> getParam(){
            return fields;
        }
        public Object getObject(){ return object;}

        @Override
        public void startDocument() throws SAXException
        {
            fields = new ArrayList<>();
        }
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException
        {
            if(qName.equals("class")){
                String className = attributes.getValue(0);
                object = Reflection.createObject(className);
            } else {
                thisElement = qName;
            }
        }
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException
        {
            if (!Objects.equals(thisElement, "")){
                String value = new String(ch, start, length);
                Reflection.setFieldValue(object, thisElement, value);
            }
        }
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException
        {
            fields.add(object);
            thisElement = "";
        }
        @Override
        public void endDocument() throws SAXException
        {
        }
    }

