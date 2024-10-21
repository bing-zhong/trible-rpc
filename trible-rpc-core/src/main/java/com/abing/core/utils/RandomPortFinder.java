package com.abing.core.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

/**
 * @Author CaptainBing
 * @Date 2024/10/21 9:42
 * @Description tcp服务随机寻找一个端口
 */
public class RandomPortFinder {


    private static final int MIN_PORT = 1024;
    private static final int MAX_PORT = 65535;
    private static final Random RANDOM = new Random();

    private static int randomPort = -1;

    public static int genRandomAvailablePort() throws IOException {
        int port;
        ServerSocket serverSocket = null;
        try {
            do {
                port = MIN_PORT + RANDOM.nextInt(MAX_PORT - MIN_PORT + 1);
                serverSocket = new ServerSocket(port);
            } while (serverSocket.getLocalPort() != port); // Ensure the port we got is the one we set
        } catch (IOException e) {
            // Port is already in use, retry with a new port
            serverSocket = null;
            port = -1; // Mark as invalid
        }

        // If we still don't have a valid port, recursively find one
        if (port == -1) {
            return genRandomAvailablePort();
        }

        // Close the server socket as we are just checking for availability
        serverSocket.close();
        return port;
    }

    public static int getRandomAvailablePort() throws IOException {
        if (randomPort == -1) {
            randomPort = genRandomAvailablePort();
            return randomPort;
        }
        return randomPort;
    }

}
