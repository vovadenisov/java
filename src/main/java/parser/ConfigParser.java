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
    private static Integer port;

    public ConfigParser() throws IOException{
        loadConfig();
    }

    public static void loadConfig() throws IOException{
        try {
            FileInputStream fis = new FileInputStream(CONFIG_FILE);
            Properties properties = new Properties();
            properties.load(fis);
            fis.close();
            port = Integer.valueOf(properties.getProperty("port"));
            host = properties.getProperty("host");
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static void setHost(String host) {
        ConfigParser.host = host;
    }

    public static void setPort(Integer port) {
        ConfigParser.port = port;
    }

    public Integer getPort(){ return port;}
    public String getHost() {return host; }
}
