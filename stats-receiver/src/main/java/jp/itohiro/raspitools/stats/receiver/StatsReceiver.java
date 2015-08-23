package jp.itohiro.raspitools.stats.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class StatsReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatsReceiver.class);
    private static final ReceiverProperties RECEIVER_PROPERTIES = new ReceiverProperties();
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);

    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket();
            server.setReuseAddress(true);
            server.bind(new InetSocketAddress(RECEIVER_PROPERTIES.getReceiverPort()));
            LOGGER.info("Ready to receive request...");
            while(true){
                Socket socket = server.accept();
                LOGGER.info("Connection established.");
                EXECUTOR_SERVICE.execute(new InputHandler(socket, RECEIVER_PROPERTIES));
            }

        } catch (IOException e) {
            LOGGER.error("Failsed to listen to port: " + RECEIVER_PROPERTIES.getReceiverPort(), e);
        }
    }
}
