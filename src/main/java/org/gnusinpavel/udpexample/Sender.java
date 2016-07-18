package org.gnusinpavel.udpexample;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

class Sender implements Runnable {
    private static final Logger logger = Logger.getLogger(Sender.class.getName());
    static final byte[] BROADCAST = new byte[]{(byte) 192, (byte) 168, 1, (byte) 255};
    static final int BUFFER_SIZE = 4;

    private final DatagramSocket socket;

    Sender(DatagramSocket socket) {
        this.socket = socket;
    }

    public void run() {
        Thread thread = Thread.currentThread();
        try {
            byte[] buffer = "Аргентина манит негра. Аж два целых раза!\u0000".getBytes(Charset.forName("UTF-8"));
            while (!thread.isInterrupted()) {
                int offset = 0;
                DatagramPacket packet = new DatagramPacket(
                        buffer,
                        buffer.length,
                        InetAddress.getByAddress(BROADCAST),
                        Main.PORT);
                while (offset < buffer.length) {
                    packet.setData(Arrays.copyOfRange(buffer, offset, offset + BUFFER_SIZE));
                    socket.send(packet);
                    offset += BUFFER_SIZE;
                }

                Thread.sleep(500);
            }
        } catch (IOException e) {
            logger.log(Level.INFO, e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.info("Поток был прерван.");
        }
        logger.info("Поток отправителя завершается.");
    }
}
