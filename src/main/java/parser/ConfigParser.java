package parser;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by alla on 26.10.15.
 */
public class ConfigParser {
    private static final String CONFIG_FILE = "cfg/server.properties";
    public static String  host;
    public static String port;
    private static FileInputStream fis;

    public ConfigParser() throws IOException{
        loadConfig();
    }

    public static void loadConfig() throws IOException{
        try {
            fis = new FileInputStream(CONFIG_FILE);
            Properties properties = new Properties();
            properties.load(fis);
            fis.close();
            port = properties.getProperty("port");
            host = properties.getProperty("host");
        } catch (IOException e){
            /*
                printStackTrace () - чтобы печатать информацию относительно исключения,
                как оно произошло и какой строке кода.
             */
            e.printStackTrace();
        }
    }
    public String getPort(){ return port;}
    public String getHost() {return host; }
}
