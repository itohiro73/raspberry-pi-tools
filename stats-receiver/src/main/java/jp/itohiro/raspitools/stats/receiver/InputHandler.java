package jp.itohiro.raspitools.stats.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class InputHandler implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(StatsReceiver.class);
    private final Socket socket;
    private final ReceiverProperties properties;

    public InputHandler(Socket socket, ReceiverProperties properties) {
        this.socket = socket;
        this.properties = properties;
    }

    @Override
    public void run() {
        try (InputStream in = socket.getInputStream()){
            readLineAndWriteToFile(in);
        } catch (IOException e) {
            LOGGER.error("Failed to read line!", e);
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                LOGGER.error("Failed to close socket!", e);
            }
        }
    }

    private void readLineAndWriteToFile(InputStream in) throws IOException {
        DataInputStream dio = new DataInputStream(in);
        String hostname = dio.readUTF();
        int ip1 = dio.readByte() & 0xFF;
        int ip2 = dio.readByte() & 0xFF;
        int ip3 = dio.readByte() & 0xFF;
        int ip4 = dio.readByte() & 0xFF;
        long currentTimeMillis = dio.readLong();
        int temperature = dio.readInt();
        LocalDateTime time = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTimeMillis), ZoneId.systemDefault());
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
        String line = hostname + "," + ip1 + "." + ip2 + "." + ip3 + "." + ip4 + "," + dateTimeFormatter.format(time) + "," + temperature + "\n";

        LOGGER.info("Writing: " + line);
        Path testFile = Paths.get(properties.getTemperatureFilePath());
        if(Files.notExists(testFile)) Files.createFile(testFile);
        BufferedWriter bufferedWriter = Files.newBufferedWriter(testFile, StandardOpenOption.APPEND);
        bufferedWriter.write(line);
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}
