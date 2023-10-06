package com.example.blackjack.controller;


import com.example.blackjack.service.MainService;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;


public class MainController {

    @FXML
    private StackPane cards;

    @FXML
    StackPane dealerCards;

    @FXML
    StackPane playerCards;

    @FXML
    Text dealerScore;

    @FXML
    Text playerScore;

    @FXML
    Button hitButton;

    @FXML
    Button standButton;

    private int[][] deck = new int[0][0];
    private int[][] dealerCardsArray = new int[0][0];
    private int[][] playerCardsArray = new int[0][0];

    @FXML
    public void gameView(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/blackjack/fxml/game-view.fxml"));
        fxmlLoader.setController(this);
        Parent gameViewRoot = fxmlLoader.load();
        Scene gameViewScene = new Scene(gameViewRoot);

        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        primaryStage.setScene(gameViewScene);

        gameViewScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/blackjack/css/style.css")).toExternalForm());
        primaryStage.show();
        disableButtons(true);
        animationShufflingCards();


    }

    @FXML
    public void animationShufflingCards() throws FileNotFoundException {
        System.out.println("Shuffling cards");

        ImageView card = getImageView("card_back");
        ImageView card1 = getImageView("card_back");
        ImageView card2 = getImageView("card_back");
        ImageView card3 = getImageView("card_back");

        card1.setTranslateX(5);
        card2.setTranslateX(10);
        card3.setTranslateX(15);
        card.setEffect(new DropShadow());
        card1.setEffect(new DropShadow());
        card2.setEffect(new DropShadow());
        card3.setEffect(new DropShadow());

        cards.getChildren().add(card);
        cards.getChildren().add(card1);
        cards.getChildren().add(card2);
        cards.getChildren().add(card3);
        TranslateTransition transition = new TranslateTransition();

        transition.setDuration(Duration.seconds(0.3));
        transition.setNode(cards.getChildren().get(2));

        transition.setToX(-65);
        transition.setAutoReverse(true);
        transition.setCycleCount(10);
        Timeline toBackTimeline = new Timeline(new KeyFrame(Duration.seconds(0.3), event -> {
            cards.getChildren().get(0).toBack();
            cards.getChildren().get(2).toFront();
        }));
        toBackTimeline.setCycleCount(10);
        toBackTimeline.play();


        transition.play();
        dealFirstCards();
    }


    private ImageView getImageView(String file) throws FileNotFoundException {
        FileInputStream stream = new FileInputStream("src/main/resources/com/example/blackjack/images/cards/" + file + ".png");
        Image image = new Image(stream);
        ImageView card = new ImageView(image);
        card.setFitHeight(130);
        card.setFitWidth(90);
        return card;
    }

    public void dealFirstCards() {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            disableButtons(false);
            deck = MainService.createDeck();
            deck = MainService.shuffleDeck(deck);

            HashMap<String, int[][]> dealerDraw = MainService.drawFirstCards(deck, 2);
            deck = dealerDraw.get("deck");
            dealerCardsArray = dealerDraw.get("cards");

            HashMap<String, int[][]> playerDraw = MainService.drawFirstCards(deck, 2);
            deck = playerDraw.get("deck");
            playerCardsArray = playerDraw.get("cards");
            try {
                setCardsToTable(dealerCardsArray[0][0] + "-" + dealerCardsArray[0][1], 50, 25, true);
                setCardsToTable("card_back", 50, 25, true);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            for (int[] ints : playerCardsArray) {
                try {
                    setCardsToTable(ints[0] + "-" + ints[1], 50, 0, false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            playerScore.setText("Your score : " + String.valueOf(getHandValue(playerCardsArray)));
            int scoreOfDealer = dealerCardsArray[0][0];
            if (scoreOfDealer > 10) {
                scoreOfDealer = 10;
            }
            dealerScore.setText("Score of dealer : " + scoreOfDealer);
        });
        pause.play();
    }


    @FXML
    private void setCardsToTable(String cardName, int x, int y, Boolean is_dealer) throws FileNotFoundException {

        ImageView card = getImageView(cardName);

        card.setTranslateY(y);
        card.setEffect(new DropShadow());
        if (is_dealer) {
            card.setTranslateX(x * dealerCards.getChildren().stream().mapToInt(node -> 1).sum());
            dealerCards.getChildren().add(card);
        } else {
            card.setTranslateX(x * playerCards.getChildren().stream().mapToInt(node -> 1).sum());
            playerCards.getChildren().add(card);
        }

    }

    // TODO - Still working on this
    @FXML
    public void hit(MouseEvent event) {
        System.out.println("Hit");
        try {
            HashMap<String, int[][]> playerDraw = MainService.drawFirstCards(deck, 1);
            deck = playerDraw.get("deck");
            int[][] newPlayerCardsArray = new int[playerCardsArray.length + 1][2];
            System.arraycopy(playerCardsArray, 0, newPlayerCardsArray, 0, playerCardsArray.length);
            newPlayerCardsArray[newPlayerCardsArray.length - 1] = playerDraw.get("cards")[0];
            playerCardsArray = newPlayerCardsArray;
            setCardsToTable(playerCardsArray[playerCardsArray.length - 1][0] + "-" + playerCardsArray[playerCardsArray.length - 1][1], 50, 0, false);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // print playerCardsArray
        System.out.println("Player cards:");
        for (int[] ints : playerCardsArray) {
            System.out.println(ints[0] + "-" + ints[1]);
        }
        System.out.println("size of array : " + playerCardsArray.length);

        System.out.println("Player hand value: " + getHandValue(playerCardsArray));

//        change dealerScore and playerScore
        int playerScoreTotal = getHandValue(playerCardsArray);
        if (playerScoreTotal > 21) {
            playerScore.setText("Your score : " + String.valueOf(playerScoreTotal) + " - You lose");
            stand(event);
        } else {
            playerScore.setText("Your score : " + String.valueOf(playerScoreTotal));
        }

    }

    @FXML
    public void stand(MouseEvent event) {
        disableButtons(true);
        // Clear dealerCards
        dealerCards.getChildren().clear();
        for (int[] ints : dealerCardsArray) {
            try {
                setCardsToTable(ints[0] + "-" + ints[1], 50, 25, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        int dealerScoreTotal = getHandValue(dealerCardsArray);
        if (dealerScoreTotal > 21) {
            dealerScore.setText("Score of dealer : " + String.valueOf(dealerScoreTotal) + " - dealer lose");
        } else {
            dealerScore.setText("Score of dealer : " + String.valueOf(dealerScoreTotal));
            // Create a PauseTransition with a 2-second duration
//            PauseTransition pause = new PauseTransition(Duration.millis(1500));
//            while(dealerScoreTotal < 17) {
//
//                dealerScoreTotal = getHandValue(dealerCardsArray);
//                pause.setOnFinished(e -> {
                    drawDealerCard(); // Call a method to draw a dealer card after the pause
//                });
//                pause.play();
//            }
        }
    }

    private void drawDealerCard() {

//        int dealerScoreTotal = getHandValue(dealerCardsArray);
        PauseTransition pause = new PauseTransition(Duration.millis(1500));
        while(getHandValue(dealerCardsArray) < 17)  {

            int dealerScoreTotal= getHandValue(dealerCardsArray);
            HashMap<String, int[][]> dealerDraw = MainService.drawFirstCards(deck, 1);
            deck = dealerDraw.get("deck");
            int[][] newDealerCardsArray = new int[dealerCardsArray.length + 1][2];
            System.arraycopy(dealerCardsArray, 0, newDealerCardsArray, 0, dealerCardsArray.length);
            newDealerCardsArray[newDealerCardsArray.length - 1] = dealerDraw.get("cards")[0];
            dealerCardsArray = newDealerCardsArray;

            try {
                setCardsToTable(dealerCardsArray[dealerCardsArray.length - 1][0] + "-" + dealerCardsArray[dealerCardsArray.length - 1][1], 50, 25, true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            dealerScoreTotal = getHandValue(dealerCardsArray);
            int playerScoreTotal = getHandValue(playerCardsArray);

            if (dealerScoreTotal == playerScoreTotal) {
                dealerScore.setText("Score of dealer : " + String.valueOf(dealerScoreTotal));
                playerScore.setText("Your score : " + String.valueOf(playerScoreTotal));
            } else if (dealerScoreTotal > 21) {
                dealerScore.setText("Score of dealer : " + String.valueOf(dealerScoreTotal) + " - dealer lose");
            } else {
                if (dealerScoreTotal > playerScoreTotal) {
                    dealerScore.setText("Score of dealer : " + String.valueOf(dealerScoreTotal) + " - dealer win");
                } else {
                    dealerScore.setText("Score of dealer : " + String.valueOf(dealerScoreTotal));
                }
            }
        }
    }


    public int getHandValue(int[][] hand) {
        int handValue = 0;
        int aceCount = 0;
        for (int[] ints : hand) {
            int rank = ints[0];
            if (rank == 1) {
                aceCount++;
                handValue += 11;
            } else if (rank > 10) {
                handValue += 10;
            } else {
                handValue += rank;
            }
        }
        while (handValue > 21 && aceCount > 0) {
            handValue -= 10;
            aceCount--;
        }
        return handValue;
    }

    public void disableButtons(boolean disable) {
        hitButton.setDisable(disable);
        standButton.setDisable(disable);
    }

}