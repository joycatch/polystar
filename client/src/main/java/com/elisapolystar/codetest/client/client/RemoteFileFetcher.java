package com.elisapolystar.codetest.client.client;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

@Service
public class RemoteFileFetcher {

    public Map<String, Integer> fetchAndCountWords(String host, int port) {
        Map<String, Integer> wordCount = new HashMap<>();

        try {
            Socket socket = new Socket(host, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.toLowerCase().split("\\W+");
                for (String word : words) {
                    if (!word.isBlank()) {
                        wordCount.merge(word, 1, Integer::sum);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to read from server " + host + ":" + port, e);
        }

        return wordCount;
    }
}