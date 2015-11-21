package parser;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by alla on 26.10.15.
 */
public class ConfigParser {
    private static final String CONFIG_FILE = "cfg/server.properties";
    private static String host;
    private static String port;
    private static String DBUser;
    private static String DBPassword;
    private static String DBName;

    public ConfigParser() throws IOException{
        loadConfig();
    }

    public static void loadConfig() throws IOException{
        try {
            FileInputStream fis = new FileInputStream(CONFIG_FILE);
            Properties properties = new Properties();
            properties.load(fis);
            fis.close();
            port = properties.getProperty("port");
            host = properties.getProperty("host");
            DBPassword = properties.getProperty("DBPassword");
            DBUser = properties.getProperty("DBUser");
            DBName = properties.getProperty("DBName");
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void setHost(String host) {
        ConfigParser.host = host;
    }

    public static void setPort(String port) {
        ConfigParser.port = port;
    }

    public static void setDBUser(String user){
        ConfigParser.DBUser = user;
    }

    public static void setDBPassword(String password){
        ConfigParser.DBPassword = password;
    }


    public static void setDBName(String name){
        ConfigParser.DBName = name;
    }

    public String getDBName(){return DBName;}

    public String getDBUser(){return DBUser;}

    public String getDBPassword(){return DBPassword;}

    public String getPort(){ return port;}

    public String getHost() {return host; }
}
