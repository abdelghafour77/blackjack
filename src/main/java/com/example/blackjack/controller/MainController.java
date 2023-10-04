package com.example.blackjack.controller;


import com.example.blackjack.service.MainService;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import java.util.Arrays;
import java.util.Objects;


public class MainController {

    @FXML
    private StackPane cards;

    @FXML StackPane dealerCards;

    @FXML StackPane playerCards;


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

    public void dealFirstCards(){

        PauseTransition pause = new PauseTransition(Duration.seconds(6));
        pause.setOnFinished(event -> {
            int[][] deck = MainService.createDeck();

            deck= MainService.shuffleDeck(deck);

            Object[] card = MainService.drawFirstCards(deck, 2);
            int[][] dealerCards = (int[][]) card[0];

            deck = (int[][]) card[1];
            card = MainService.drawFirstCards(deck, 2);
            int[][] playerCards = (int[][]) card[0];

            System.out.println("The dealer's cards: ");
            Arrays.stream(dealerCards).map(Arrays::toString).forEach(System.out::println);
            System.out.println("The player's cards: ");
            Arrays.stream(playerCards).map(Arrays::toString).forEach(System.out::println);
            for (int i = 0; i < dealerCards.length; i++) {
                try {
                    setCardsToTable(dealerCards[i][0] + "-" + dealerCards[i][1], 100 + i * 20,25, true);
                    setCardsToTable(playerCards[i][0] + "-" + playerCards[i][1], 100 + i * 20,0, false);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
        pause.play();
    }

    @FXML
    private void setCardsToTable(String cardName, int x,int y, Boolean is_dealer) throws FileNotFoundException {

        ImageView card = getImageView(cardName);
        card.setTranslateX(x);
        card.setTranslateY(y);
        card.setEffect(new DropShadow());
        if (is_dealer) {
            dealerCards.getChildren().add(card);
        } else {
            playerCards.getChildren().add(card);
        }

    }

}