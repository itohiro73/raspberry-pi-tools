package jp.itohiro.raspitools.stats.collector;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CollectorProperties {
    private Properties properties = new Properties();

    public CollectorProperties() {
        try(InputStream inputStream = getClass().getResourceAsStream("/collector.properties");){
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load collector.properties file", e);
        }
    }

    public String getReceiverHost(){
        return properties.getProperty("receiver.host");
    }

    public int getReceiverPort(){
        return Integer.valueOf(properties.getProperty("receiver.port"));
    }

    public String getTemperatureFilePath(){
        return properties.getProperty("temp.file.path");
    }
}
