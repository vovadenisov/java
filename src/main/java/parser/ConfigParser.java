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

    public String getPort(){ return port;}
    public String getHost() {return host; }
}
