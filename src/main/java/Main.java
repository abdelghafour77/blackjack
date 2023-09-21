import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        int[][] deck = createDeck();

        for (int[] card : deck) {
            System.out.println(Arrays.toString(card));
        }
    }

    public static int[][] createDeck() {
        int[][] deck = new int[52][2]; 

        int cardIndex = 0;

        for (int rank = 1; rank <= 13; rank++) {
            for (int suit = 1; suit <= 4; suit++) {
                deck[cardIndex][0] = rank;
                deck[cardIndex][1] = suit;
                cardIndex++;
            }
        }

        return deck;
    }
}
