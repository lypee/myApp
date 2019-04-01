package ly.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;


import java.io.InputStreamReader;
import java.util.Properties;


public class PropertiesUtil {
    private static Logger logger =
            LoggerFactory.getLogger(PropertiesUtil.class);
    private static Properties properties ;

    static{
        String fileName="application.properties";
        properties=new Properties();
        try {
            properties.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "UTF-8"));
        } catch (Exception e) {
            logger.error("配置文件error " ,e);
        }
    }

    public static String getProperty(String key) {
        String value = properties.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return null ;
        }
        return value.trim() ;
    }

    public static String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key.trim());
        if (StringUtils.isBlank(key)) {
            value = defaultValue ;
        }
        return value.trim();
    }
}
