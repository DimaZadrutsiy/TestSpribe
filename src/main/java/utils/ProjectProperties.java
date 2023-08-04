package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProjectProperties {
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = ProjectProperties.class.getResourceAsStream("/config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String propertyName) {
        String propertyValue = properties.getProperty(propertyName);
        if (propertyValue == null) {
            throw new RuntimeException("Property \"" + propertyName + "\" isn't defined");
        }
        return propertyValue;
    }
}
