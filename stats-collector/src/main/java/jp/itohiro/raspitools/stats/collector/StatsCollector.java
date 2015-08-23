package jp.itohiro.raspitools.stats.collector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class StatsCollector {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatsCollector.class);
    private static final CollectorProperties COLLECTOR_PROPERTIES = new CollectorProperties();

    public static void main(String[] args) {
        InetAddress address;
        try {
            address = InetAddress.getByName(COLLECTOR_PROPERTIES.getReceiverHost());
        } catch (UnknownHostException e) {
            throw new RuntimeException("Couldn't get address for " + COLLECTOR_PROPERTIES.getReceiverHost(), e);
        }
        try (Socket socket = new Socket(address, COLLECTOR_PROPERTIES.getReceiverPort());){
            LOGGER.info("Connected to host: " + COLLECTOR_PROPERTIES.getReceiverHost() + ":" + COLLECTOR_PROPERTIES.getReceiverPort());
            List<String> lines = Files.readAllLines(Paths.get(COLLECTOR_PROPERTIES.getTemperatureFilePath()));
            InetAddress localHostAddress = Inet4Address.getLocalHost();

            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            dos.write(localHostAddress.getAddress());
            dos.writeLong(System.currentTimeMillis());
            dos.writeInt(Integer.valueOf(lines.get(0)));
            dos.close();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to send information ", ex);
        }
    }
}
