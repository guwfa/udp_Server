package org.example;


//import org.apache.commons.cli.*;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;
import java.util.Random;

/**
  HOST PORT
 **/

public class Server {

    private static final int AVERAGE_DELAY = 1000;  // Задержка получения и отправки запросов
    public  static int SERVER_PORT = 50001;
    private static int BUFFER_SIZE = 1024;
    public  static int CLIENT_PORT = 0;

    public static void main(String[] args) throws Exception  {

        //Буферы для отправлемых и принимаемых данных
        byte[] receivingDataBuffer = new byte[BUFFER_SIZE];
       /* CommandLineParser parser = new DefaultParser();
        Options options = listOptions();

        try {
            CommandLine cmd = parser.parse(options, args);

            if(cmd.hasOption("p"))      SERVER_PORT = Integer.parseInt(cmd.getOptionValue("p"));
            if(cmd.hasOption("size"))   BUFFER_SIZE = Math.min(Integer.parseInt(cmd.getOptionValue("size")), 1024);

        }catch (ParseException e) {
            log4Me("Error: " + e);
        }*/

        startServer(receivingDataBuffer);

        System.out.println(
                "File received successfully." +
                        "File: " + receivingDataBuffer.length +
                        "\n{ " + new String(receivingDataBuffer) + " }"
        );

    }

    private static void startServer(byte[] receivingDataBuffer) {
        StringBuilder stringBuilder = new StringBuilder();
        String sendingDataBuffer;
        try {
            log4Me("Server start...");

            DatagramSocket socket = new DatagramSocket(SERVER_PORT);

            System.out.println("Host = " + socket.getInetAddress() + " Port = " + socket.getPort());

            int i = 0;
            while (true) {
               Thread.sleep(AVERAGE_DELAY);

               DatagramPacket packet = new DatagramPacket(receivingDataBuffer, receivingDataBuffer.length);

               log4Me("Waiting for a client to connect...");
               socket.receive(packet);

               InetAddress inetAddress = packet.getAddress();
               CLIENT_PORT = packet.getPort();

               String receivedData  = new String(packet.getData());
               log4Me("Sent from the client: " + receivedData );

               sendingDataBuffer = ("Got package " + i);

               DatagramPacket outputPacket = new DatagramPacket(
                       sendingDataBuffer.getBytes(), sendingDataBuffer.length(), inetAddress, CLIENT_PORT
               );
               socket.send(outputPacket);
               i++;

                if(packet.getLength() == 0) {
                    log4Me("Got the last package");
                    break;
                }else {
                    stringBuilder.append(receivedData);
                }

           }

            socket.close();
        }catch (Exception e) {
            log4Me("Error: " + e);
        }
    }

    /*private static Options listOptions() {
        Options options = new Options();

        options.addOption("p", "ServerPort", true, "Порт сервера");
        options.addOption("size", "SizePackage", true, "Размер пакета, НЕ БОЛЕЕ 1024");

        return options;
    }*/
    private static void log4Me(String log) {
        System.out.println(log);
    }
}