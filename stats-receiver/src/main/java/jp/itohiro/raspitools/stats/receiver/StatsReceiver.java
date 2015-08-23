package jp.itohiro.raspitools.stats.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class StatsReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatsReceiver.class);
    private static final ReceiverProperties RECEIVER_PROPERTIES = new ReceiverProperties();

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket();
            server.setReuseAddress(true);
            server.bind(new InetSocketAddress(RECEIVER_PROPERTIES.getReceiverPort()));
            LOGGER.info("Ready to receive request...");
            while(true){
                Socket socket = server.accept();
                LOGGER.info("Connection established.");
                new Thread(new InputHandler(socket, RECEIVER_PROPERTIES)).start();
            }

        } catch (IOException e) {
            LOGGER.error("Failsed to listen to port: " + RECEIVER_PROPERTIES.getReceiverPort(), e);
        }
    }
}
