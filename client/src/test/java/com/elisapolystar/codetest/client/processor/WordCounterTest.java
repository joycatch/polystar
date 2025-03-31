package com.elisapolystar.codetest.client.processor;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class WordCounterTest {

    private final WordCounter counter = new WordCounter();

    @Test
    void merge_shouldReturnEmptyMap_whenInputIsNull() {
        Map<String, Integer> result = counter.merge(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void merge_shouldReturnEmptyMapForEmptyInput() {
        Map<String, Integer> merged = counter.merge(Collections.emptyList());
        assertTrue(merged.isEmpty());
    }

    @Test
    void topN_shouldReturnEmptyMap_whenInputIsNull() {
        Map<String, Integer> result = counter.topN(null, 3);
        assertTrue(result.isEmpty());
    }

    @Test
    void topN_shouldReturnEmptyMapIfInputIsEmpty() {
        Map<String, Integer> result = counter.topN(Collections.emptyMap(), 3);
        assertTrue(result.isEmpty());
    }

    @Test
    void merge_shouldCombineMultipleMapsCorrectly() {
        Map<String, Integer> map1 = Map.of("apple", 2, "banana", 1);
        Map<String, Integer> map2 = Map.of("banana", 3, "cherry", 1);
        Map<String, Integer> map3 = Map.of("apple", 1, "date", 2);

        Map<String, Integer> merged = counter.merge(List.of(map1, map2, map3));

        assertEquals(3, merged.get("apple"));
        assertEquals(4, merged.get("banana"));
        assertEquals(1, merged.get("cherry"));
        assertEquals(2, merged.get("date"));
    }

    @Test
    void topN_shouldReturnTopNEntriesInDescendingOrder() {
        Map<String, Integer> wordCount = Map.of(
                "the", 10,
                "apple", 5,
                "banana", 7,
                "zebra", 8,
                "cat", 3
        );

        Map<String, Integer> top3 = counter.topN(wordCount, 3);

        assertEquals(List.of("the", "zebra", "banana"), new ArrayList<>(top3.keySet()));
    }

    @Test
    void topN_shouldReturnAllEntriesIfNIsLargerThanSize() {
        Map<String, Integer> wordCount = Map.of(
                "a", 1,
                "b", 2
        );

        Map<String, Integer> result = counter.topN(wordCount, 5);

        assertEquals(2, result.size());
        assertEquals(List.of("b", "a"), new ArrayList<>(result.keySet()));
    }
}