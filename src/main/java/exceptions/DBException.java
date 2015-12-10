package exceptions;

/**
 * Created by alla on 27.11.15.
 */
public class DBException extends Exception {

    private String message = "Error in the database";

    public DBException(){}

    public DBException(String message){

        this.message = this.message + '\n' + message;
    }

    @Override
    public String getMessage(){

        return message;
    }
}
