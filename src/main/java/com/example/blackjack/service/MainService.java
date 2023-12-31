package com.example.blackjack.service;

import java.util.Arrays;
import java.util.HashMap;

public class MainService {
    private static final int[] RANKS = {1, 2, 3, 4, 5, 6};
    private static final int[] SUITS = {1, 2, 3, 4}; // Hearts, Diamonds, Clubs, Spades

    public static void main(String[] args) {
        int[][] deck = createDeck();

        // Testing: Print the deck to verify it's created correctly
        System.out.println("The deck created:");
        Arrays.stream(deck).map(Arrays::toString).forEach(System.out::println);
        System.out.println("Number of cards in deck: " + deck.length);

        // Testing: Print the deck to verify it's shuffled correctly and no cards are missing with number of cards in deck
        System.out.println("The deck shuffled:");
        Arrays.stream(shuffleDeck(deck)).map(Arrays::toString).forEach(System.out::println);
        System.out.println("Number of cards in deck: " + deck.length);

        // Testing: Print the deck sorted by rank
        System.out.println("The deck sorted by rank:");
        Arrays.stream(sortDeckByRank(deck)).map(Arrays::toString).forEach(System.out::println);
        System.out.println("Number of cards in deck: " + deck.length);
    }

    public static int[][] createDeck() {
        int[][] deck = new int[RANKS.length * SUITS.length][2];
        int cardIndex = 0;

        for (int suit : SUITS) {
            for (int rank : RANKS) {
                deck[cardIndex][0] = rank;
                deck[cardIndex][1] = suit;
                cardIndex++;
            }
        }

        return deck;
        // return IntStream.range(0, RANKS.length * SUITS.length)// 0 to 52
        //         .mapToObj(index -> new int[]{RANKS[index % RANKS.length], SUITS[index / RANKS.length]})
        //         .toArray(int[][]::new);
    }

    public static Object[] extractCardByIndex(int[][] deck, int index) {
        if (index < 0 || index >= deck.length) {
            // Handle invalid index here
            // ...
            return null;
        }

        Object[] newDeck = new Object[2];
        newDeck[0] = deck[index];

        int[][] newDeck2 = new int[deck.length - 1][deck[0].length];
        for (int i = 0, newIndex = 0; i < deck.length; i++) {
            if (i != index) {
                newDeck2[newIndex++] = deck[i];
            }
        }
        newDeck[1] = newDeck2;
        return newDeck;
    }


    public static Object[] extractRandomCard(int[][] deck) {
        int index = (int) (Math.random() * deck.length);
        return extractCardByIndex(deck, index);
    }

    public static int[][] shuffleDeck(int[][] deck) {
        int[][] shuffledDeck = new int[deck.length][deck[0].length];
        int[][] tmpDeck = deck.clone();
        for (int i = 0; i < deck.length; i++) {
            Object[] card = extractRandomCard(tmpDeck);
            shuffledDeck[i] = (int[]) card[0];
            tmpDeck = (int[][]) card[1];
        }
        return shuffledDeck;
    }

    public static int[][] sortDeckByRank(int[][] deck) {
        int[][] sortedDeck = new int[deck.length][deck[0].length];
        int[][] tmpDeck = deck.clone();

        // Implement a sorting algorithm based on the rank
        for (int i = 0; i < deck.length; i++) {
            int minIndex = findMinRankCardIndex(tmpDeck);
            Object[] card = extractCardByIndex(tmpDeck, minIndex);
            assert card != null;
            sortedDeck[i] = (int[]) card[0];
            tmpDeck = (int[][]) card[1];
        }

        return sortedDeck;
    }

    private static int findMinRankCardIndex(int[][] deck) {
        int minIndex = 0;
        int minRank = deck[0][0]; // Initialize with the rank of the first card

        for (int i = 1; i < deck.length; i++) {
            int currentRank = deck[i][0];
            if (currentRank < minRank) {
                minRank = currentRank;
                minIndex = i;
            }
        }
        return minIndex;
    }

// TODO: change type of return value to HashMap

    public static HashMap<String, int[][]> drawFirstCards(int[][] deck, int number) {
        HashMap<String, int[][]> result = new HashMap<>();

        int[][] drawnCards = new int[number][deck[0].length];
        int[][] newDeck = deck.clone();

        for (int i = 0; i < number; i++) {
            Object[] card = extractRandomCard(newDeck);
            drawnCards[i] = (int[]) card[0];
            newDeck = (int[][]) card[1];
        }

        result.put("cards", drawnCards);
        result.put("deck", newDeck);

        return result;
    }

}
