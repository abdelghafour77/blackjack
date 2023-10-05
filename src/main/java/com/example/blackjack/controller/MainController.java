package com.example.blackjack.controller;


import com.example.blackjack.service.MainService;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
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
        transition.setCycleCount(20);
        Timeline toBackTimeline = new Timeline(new KeyFrame(Duration.seconds(0.3), event -> {
            cards.getChildren().get(0).toBack();
            cards.getChildren().get(2).toFront();
        }));
        toBackTimeline.setCycleCount(20);
        toBackTimeline.play();


        transition.play();
        dealFirstCards();
    }


    private ImageView getImageView(String file) throws FileNotFoundException {
        FileInputStream stream = new FileInputStream("src/main/resources/com/example/blackjack/images/cards/" + file + ".png");
        Image image = new Image(stream);
        ImageView card = new ImageView(image);
        card.setFitHeight(150);
        card.setFitWidth(100);
        return card;
    }

    public void dealFirstCards() {
        PauseTransition pause = new PauseTransition(Duration.seconds(6));
        pause.setOnFinished(event -> {
            deck = MainService.createDeck();
            deck = MainService.shuffleDeck(deck);

            HashMap<String, int[][]> dealerDraw = MainService.drawFirstCards(deck, 2);
            deck = dealerDraw.get("deck");
            dealerCardsArray = dealerDraw.get("cards");

            HashMap<String, int[][]> playerDraw = MainService.drawFirstCards(deck, 2);
            deck = playerDraw.get("deck");
            playerCardsArray = playerDraw.get("cards");


            for (int i = 0; i < dealerCardsArray.length; i++) {
                try {
                    setCardsToTable(dealerCardsArray[i][0] + "-" + dealerCardsArray[i][1], 50, 25, true);
                    setCardsToTable(playerCardsArray[i][0] + "-" + playerCardsArray[i][1], 50, 0, false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
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


    }

    @FXML
    public void stand(MouseEvent event) {
        System.out.println("Stand");

    }



}