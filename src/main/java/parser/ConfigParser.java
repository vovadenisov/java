package parser;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import exceptions.ConfigException;

/**
 * Created by alla on 26.10.15.
 */
public class ConfigParser {
    private static final String CONFIG_FILE = "cfg/server.properties";
    private static final String BD_CONFIG_FILE = "cfg/db.properties";
    private static String host;
    private static Integer port;
    private static String DBUser;
    private static String DBPassword;
    private static String DBName;
    private static String DBdialect;
    private static String DBdriver;
    private static String DBinitialization;

    public ConfigParser() throws ConfigException{
        loadConfig();
    }

    public static void loadConfig() throws ConfigException{
        try {
            FileInputStream fis = new FileInputStream(CONFIG_FILE);
            Properties properties = new Properties();
            properties.load(fis);
            fis.close();
            port = Integer.valueOf(properties.getProperty("port"));
            host = properties.getProperty("host");
        } catch (IOException |  NullPointerException| NumberFormatException e) {
            throw new ConfigException();
        }
    }

    public static void loadBDConfig() throws ConfigException{
        try {
            FileInputStream fis = new FileInputStream(BD_CONFIG_FILE);
            Properties properties = new Properties();
            properties.load(fis);
            fis.close();
            DBPassword = properties.getProperty("DBPassword");
            DBUser = properties.getProperty("DBUser");
            DBName = properties.getProperty("DBName");
            DBdialect = properties.getProperty("DBdialect");
            DBdriver = properties.getProperty("DBdriver");
            DBinitialization = properties.getProperty("hbm2ddlAuto");
        } catch (IOException |  NullPointerException| NumberFormatException e) {
            throw new ConfigException();
        }
    }

    public static void setHost(String host) {
        ConfigParser.host = host;
    }

    public static void setPort(Integer port) {
        ConfigParser.port = port;
    }

    public static void setDBUser(String user){
        ConfigParser.DBUser = user;
    }

    public static void setDBdriver(String DBdriver) {ConfigParser.DBdriver = DBdriver; }

    public static void setDBPassword(String password){
        ConfigParser.DBPassword = password;
    }

    public static void setDBdialect(String DBdialect){
        ConfigParser.DBdialect = DBdialect;
    }

    public static void setDBName(String name){
        ConfigParser.DBName = name;
    }

    public static void setDBinitialization(String DBinitialization){
        ConfigParser.DBinitialization = DBinitialization;
    }

    public String getDBName(){return DBName;}

    public String getDBUser(){return DBUser;}

    public String getDBPassword(){return DBPassword;}

    public String getDBdriver(){return DBdriver;}

    public Integer getPort(){return port;}
    public String getHost() {return host; }

    public String getDBdialect(){return DBdialect; }

    public String getDBinitialization(){return DBinitialization;}
}