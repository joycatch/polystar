package com.elisapolystar.codetest.server.socket;

import com.elisapolystar.codetest.server.socket.FileSocketServer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {
        "server.port=9988",
        "server.filePath=src/test/resources/example.txt"
})
class FileSocketServerIntegrationTest {

    @Autowired
    private FileSocketServer server;

    @Test
    void serverShouldStreamFileToClient() throws Exception {
        // Give the server a moment to bind
        Thread.sleep(500);

        try (Socket socket = new Socket("localhost", 9988);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            assertEquals(
                    List.of("I guarantee you there's no problem, I guarantee",
                            "You either have it or you don't"), lines);
        }
    }
}