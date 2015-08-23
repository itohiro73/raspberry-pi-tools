package jp.itohiro.raspitools.stats.receiver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReceiverProperties {
    private Properties properties = new Properties();

    public ReceiverProperties() {
        try(InputStream inputStream = getClass().getResourceAsStream("/receiver.properties");){
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load receiver.properties file", e);
        }
    }

    public int getReceiverPort(){
        return Integer.valueOf(properties.getProperty("receiver.port"));
    }

    public String getTemperatureFilePath(){
        return properties.getProperty("stats.file.path");
    }
}
