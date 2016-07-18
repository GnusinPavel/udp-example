package org.gnusinpavel.udpexample;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger anonymousLogger = Logger.getAnonymousLogger();
    static final int PORT = 9001;

    public static void main(String[] args) {
        final DatagramSocket socket;
        try {
            socket = new DatagramSocket(PORT);
        } catch (IOException e) {
            anonymousLogger.log(Level.SEVERE, e.getMessage(), e);
            return;
        }

        final Thread senderThread = new Thread(new Sender(socket));
        final Thread receiverThread = new Thread(new Receiver(socket));

        senderThread.start();
        receiverThread.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                anonymousLogger.info("Попали в хук.");
                socket.close();

                senderThread.interrupt();
                receiverThread.interrupt();

                try {
                    receiverThread.join(5000);
                    senderThread.join(5000);
                } catch (InterruptedException e) {
                    anonymousLogger.info("Поток завершения был прерван.");
                }
            }
        });
    }
}
