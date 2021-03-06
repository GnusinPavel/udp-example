package org.gnusinpavel.udpexample;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

class Receiver implements Runnable {
    private static final Logger logger = Logger.getLogger(Receiver.class.getName());
    private final byte[] buffer = new byte[Sender.BUFFER_SIZE];
    private final DatagramSocket socket;

    Receiver(DatagramSocket socket) {
        this.socket = socket;
    }

    public void run() {
        Thread thread = Thread.currentThread();
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            while (!thread.isInterrupted()) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                byteOut.write(buffer, packet.getOffset(), packet.getLength());
                if (packet.getLength() > 0 && buffer[packet.getLength() - 1] == 0x00) {
                    byte[] bytes = byteOut.toByteArray();
                    logger.info("Получено сообщение: " + new String(Arrays.copyOf(bytes, bytes.length - 1)));
                    byteOut = new ByteArrayOutputStream();
                }
            }
        } catch (IOException e) {
            logger.log(Level.INFO, e.getMessage(), e);
        }
        logger.info("Поток получателя завершается.");
    }
}
