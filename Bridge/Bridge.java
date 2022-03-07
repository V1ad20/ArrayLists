package Bridge;

import java.io.*;
import java.util.*;

public class Bridge {
    private ArrayList<String> deck;
    private String[][] playerCards;
    private int[] scores;

    public Bridge() throws IOException {
        deck = new ArrayList<String>();
        playerCards = new String[4][13];
        scores = new int[4];

        Scanner sc = new Scanner(new File("Bridge/cards.txt"));
        while (sc.hasNext()) {
            deck.add(sc.nextLine());
        }
        Collections.shuffle(deck);
    }

    public void dealCards() {
        int i = 0;
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 13; k++) {
                playerCards[j][k] = deck.get(i);
                if (k == 12) {
                    scores[j] = determineScore(playerCards[j]);
                }
                i++;
            }
        }
    }

    private int determineScore(String[] cards) {
        int score = 0;
        int[] suitNums = new int[4];
        for (String card : cards) {
            char type = card.charAt(0);
            char suit = card.charAt(1);

            switch (type) {
                case 'A':
                    score += 4;
                    break;
                case 'K':
                    score += 3;
                    break;
                case 'Q':
                    score += 2;
                    break;
                case 'J':
                    score++;
                    break;
                default:
            }

            switch (suit) {
                case 'S':
                    suitNums[0]++;
                    break;
                case 'H':
                    suitNums[1]++;
                    break;
                case 'C':
                    suitNums[2]++;
                    break;
                default:
                    suitNums[3]++;
            }
        }

        for (int suitNum : suitNums) {
            switch (suitNum) {
                case 2:
                    score++;
                    break;
                case 1:
                    score += 2;
                    break;
                case 0:
                    score += 3;
            }
        }
        return score;
    }

    private String displayCards(String[] cards) {
        String cardString = "";
        for (String card : cards) {
            cardString += card + " ";
        }
        return cardString;
    }

    public String displayResults() {
        int maxScore = 0;
        String winners = "";
        String info = "";
        for (int i = 0; i < 4; i++) {
            info += "Player" + (i + 1) + ": " +
                    displayCards(playerCards[i]) + "(" + scores[i] + " points)\n";
            if (scores[i] > maxScore || maxScore == 0) {
                maxScore = scores[i];
                winners = "Player" + (i + 1);
            } else if (scores[i] == maxScore) {
                winners += " & Player" + (i + 1);
            }
        }

        return info + "Winner(s): " + winners;
    }

    public static void main(String[] args) throws IOException {
        Bridge bridge = new Bridge();
        bridge.dealCards();
        System.out.println(bridge.displayResults());
    }
}