package com.elisapolystar.codetest.server.socket;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ServerWorkerTest {

    private ServerSocket serverSocket;
    private int port;
    private final String testFilePath = "src/test/resources/example.txt";

    @BeforeEach
    void setup() throws Exception {
        serverSocket = new ServerSocket(0); // dynamic port
        port = serverSocket.getLocalPort();
    }

    @AfterEach
    void cleanup() throws Exception {
        serverSocket.close();
    }

    @Test
    void run_shouldSendFileContentsToClient() throws Exception {
        // Setup
        Thread serverThread = new Thread(() -> {
            try {
                Socket clientSocket = serverSocket.accept();
                ServerWorker worker = new ServerWorker(clientSocket, testFilePath);
                worker.run();
            } catch (IOException e) {
                fail("Failed to accept socket: " + e.getMessage());
            }
        });
        serverThread.start();

        // Simulate client
        try (Socket socket = new Socket("localhost", port);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            List<String> lines = new ArrayList<>();
            String line;
            while ((line = in.readLine()) != null) {
                lines.add(line);
            }

            assertEquals(
                    List.of("I guarantee you there's no problem, I guarantee",
                            "You either have it or you don't"), lines);
        }
    }

    @Test
    void run_shouldNotCrashIfFileDoesNotExist() throws Exception {
        String invalidPath = "src/test/resources/missing.txt";

        Thread serverThread = new Thread(() -> {
            try {
                Socket clientSocket = serverSocket.accept();
                ServerWorker worker = new ServerWorker(clientSocket, invalidPath);
                worker.run();
            } catch (IOException e) {
                fail("Failed to accept socket: " + e.getMessage());
            }
        });
        serverThread.start();

        try (Socket socket = new Socket("localhost", port)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // The server should not send anything since the file is missing
            assertNull(reader.readLine());
        }
    }
}