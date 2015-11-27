package exceptions;

/**
 * Created by alla on 27.11.15.
 */
public class XMLReaderException extends Exception {
     public XMLReaderException(){}

    @Override
    public String getMessage(){
        return "Error in the xml file";
    }
}
