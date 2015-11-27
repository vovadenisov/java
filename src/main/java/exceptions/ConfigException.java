package exceptions;

/**
 * Created by alla on 27.11.15.
 */
public class ConfigException extends  Exception{

    public ConfigException(){}

    @Override
    public String getMessage(){
        return "Error in the configuration file";
    }
}
