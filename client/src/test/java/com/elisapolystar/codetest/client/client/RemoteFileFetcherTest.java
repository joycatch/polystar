package com.elisapolystar.codetest.client.client;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class RemoteFileFetcherTest {

    private ServerSocket serverSocket;
    private ExecutorService executor;
    private int port;

    private final RemoteFileFetcher remoteFileFetcher = new RemoteFileFetcher();

    @BeforeEach
    void setup() throws Exception {
        // Create dynamic port and mock server
        serverSocket = new ServerSocket(0); // 0 = dynamically assigned port
        port = serverSocket.getLocalPort();
        executor = Executors.newSingleThreadExecutor();

        // Start server in background
        executor.submit(() -> {
            try (Socket clientSocket = serverSocket.accept();
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

                out.println("Hello world!");
                out.println("This is a test, test test.");
                out.println("HELLO... again?");

            } catch (Exception e) {
                // Log or ignore for test
            }
        });
    }

    @AfterEach
    void tearDown() throws Exception {
        serverSocket.close();
        executor.shutdownNow();
    }

    @Test
    void fetchAndCountWords_shouldCountCorrectlyFromMockServer() {
        Map<String, Integer> result = remoteFileFetcher.fetchAndCountWords("localhost", port);

        assertEquals(2, result.get("hello"));
        assertEquals(1, result.get("world"));
        assertEquals(3, result.get("test"));
        assertEquals(1, result.get("this"));
        assertEquals(1, result.get("is"));
        assertEquals(1, result.get("a"));
        assertEquals(1, result.get("again"));
    }
}