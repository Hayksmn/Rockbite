package org.example;

import java.util.HashMap;

public class Main {
    public static void main(String[] args) {

        int N = 10000;

        long startTime = System.nanoTime();
        String result = QuestRewardGenerator.generateQuestRewards(N);
        long endTime = System.nanoTime();

        long duration = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        System.out.println("Result: " + result);
        printCount(result);
        System.out.println("Time taken: " + duration + " ms");
    }

    private static void printCount(String result) {
        HashMap<Character, Integer> resultCount = new HashMap<>();
        for(char c : result.toCharArray()) {
            if(resultCount.containsKey(c)) {
                resultCount.put(c, resultCount.get(c) + 1);
            } else {
                resultCount.put(c, 1);
            }
        }
        System.out.println(resultCount);
    }
}