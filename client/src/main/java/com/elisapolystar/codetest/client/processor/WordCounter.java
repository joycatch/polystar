package com.elisapolystar.codetest.client.processor;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class WordCounter {

    public Map<String, Integer> merge(List<Map<String, Integer>> maps) {
        if (maps == null) {
            return Collections.emptyMap();
        }

        return maps.stream()
                .flatMap(map -> map.entrySet().stream())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum
                ));
    }

    public Map<String, Integer> topN(Map<String, Integer> wordCount, int n) {
        if (wordCount == null) {
            return Collections.emptyMap();
        }

        return wordCount.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(n)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }
}