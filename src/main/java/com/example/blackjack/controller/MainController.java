package com.example.blackjack.controller;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public class MainController {

//    @FXML
//    private ImageView back_card;

    @FXML
    private StackPane cards;


    @FXML
    public void gameView(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/blackjack/fxml/game-view.fxml"));
        fxmlLoader.setController(this);
        Parent gameViewRoot = fxmlLoader.load();
        Scene gameViewScene = new Scene(gameViewRoot);


        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        primaryStage.setScene(gameViewScene);
//        primaryStage.setFullScreen(true);

//        TranslateTransition transition = new TranslateTransition();
//        transition.setDuration(Duration.seconds(0.1));



        gameViewScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/example/blackjack/css/style.css")).toExternalForm());

        primaryStage.show();
        animationShufflingCards();
//        transition.setToX(-20);
//        transition.setAutoReverse(true);
//        transition.setCycleCount(TranslateTransition.INDEFINITE);
//        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1));
//        pauseTransition.setOnFinished(even -> {
//            back_card.getStyleClass().add("rotation");
//            transition.play();
//        });
//        pauseTransition.play();
    }

    @FXML
    public void animationShufflingCards(){
        System.out.println("Shuffling cards");

        ImageView card = new ImageView(new Image(String.valueOf(getClass().getResource("/com/example/blackjack/images/cards/card_back.png"))));
        card.setFitHeight(150);
        card.setFitWidth(100);
        card.getStyleClass().add("rotation");
        cards.getChildren().add(card);




    }


}