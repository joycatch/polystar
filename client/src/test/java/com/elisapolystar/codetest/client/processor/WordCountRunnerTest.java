package com.elisapolystar.codetest.client.processor;

import com.elisapolystar.codetest.client.client.RemoteFileFetcher;
import com.elisapolystar.codetest.client.config.ServerConfig;
import com.elisapolystar.codetest.client.config.ServerListProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class WordCountRunnerTest {

    private final RemoteFileFetcher fetcher = mock(RemoteFileFetcher.class);
    private final WordCounter wordCounter = mock(WordCounter.class);
    private WordCountRunner runner;

    private final ByteArrayOutputStream outputCapture = new ByteArrayOutputStream();

    @BeforeEach
    void setup() {
        // Mock configuration
        List<ServerConfig> servers = List.of(
                new ServerConfig("localhost", 9001),
                new ServerConfig("localhost", 9002)
        );
        ServerListProperties config = new ServerListProperties(servers);

        // System.out capturing
        System.setOut(new PrintStream(outputCapture));

        runner = new WordCountRunner(fetcher, wordCounter, config);
    }

    @Test
    void run_shouldPrintMergedTop5Words() throws Exception {
        // Setup
        Map<String, Integer> wordCount1 = Map.of("one", 3, "two", 2);
        Map<String, Integer> wordCount2 = Map.of("one", 1, "three", 4);
        Map<String, Integer> merged = Map.of("one", 4, "two", 2, "three", 4);
        Map<String, Integer> top5 = Map.of("one", 4, "three", 4, "two", 2);

        when(fetcher.fetchAndCountWords("localhost", 9001)).thenReturn(wordCount1);
        when(fetcher.fetchAndCountWords("localhost", 9002)).thenReturn(wordCount2);
        when(wordCounter.merge(any())).thenReturn(merged);
        when(wordCounter.topN(merged, 5)).thenReturn(top5);

        runner.run();

        String output = outputCapture.toString();
        System.setOut(System.out); // reset System.out

        assertTrue(output.contains("The 5 most common words"));
        assertTrue(output.contains("Word"));
        assertTrue(output.contains("Occurrences"));
        assertTrue(output.contains("one"));
        assertTrue(output.contains("two"));
        assertTrue(output.contains("three"));
        verify(fetcher, times(2)).fetchAndCountWords(anyString(), anyInt());
        verify(wordCounter).merge(any());
        verify(wordCounter).topN(any(), eq(5));
    }
}