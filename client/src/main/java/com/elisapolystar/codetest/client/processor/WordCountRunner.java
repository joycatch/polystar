package com.elisapolystar.codetest.client.processor;

import com.elisapolystar.codetest.client.client.RemoteFileFetcher;
import com.elisapolystar.codetest.client.config.ServerListProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Component
public class WordCountRunner {

    private final RemoteFileFetcher clientService;
    private final WordCounter wordCounter;
    private final ServerListProperties serverListProperties;

    public WordCountRunner(RemoteFileFetcher clientService,
                           WordCounter wordCounter,
                           ServerListProperties serverListProperties) {
        this.clientService = clientService;
        this.wordCounter = wordCounter;
        this.serverListProperties = serverListProperties;
    }

    @PostConstruct
    public void run() {
        ExecutorService executorService = Executors.newFixedThreadPool(serverListProperties.servers().size());

        List<Callable<Map<String, Integer>>> tasks = serverListProperties.servers().stream()
                .map(server -> (Callable<Map<String, Integer>>) () ->
                        clientService.fetchAndCountWords(server.host(), server.port()))
                .toList();

        try {
            List<Future<Map<String, Integer>>> futures = executorService.invokeAll(tasks);

            List<Map<String, Integer>> results = new ArrayList<>();
            for (Future<Map<String, Integer>> future : futures) {
                results.add(future.get());
            }

            Map<String, Integer> merged = wordCounter.merge(results);
            Map<String, Integer> topWords = wordCounter.topN(merged, 5);

            System.out.println("The 5 most common words");
            System.out.println("------------------------");
            System.out.println("Word\t\tOccurrences");
            System.out.println("------------------------");
            topWords.forEach((word, count) -> System.out.printf("%s\t\t\t%d\n", word, count));

        } catch (Exception e) {
            log.error("Failed to fetch and count words", e);
        }
        executorService.shutdown();
    }
}