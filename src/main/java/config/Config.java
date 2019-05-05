package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    static Properties properties = new Properties();

    static {
        try {
            InputStream inputStream = Config.class.getResourceAsStream("/config.properties");
            properties.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getProp(String key) {
        return getProp(key, null);
    }

    public static String getProp(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
}
