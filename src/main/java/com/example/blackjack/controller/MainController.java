package com.example.blackjack.controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class MainController {

    @FXML
    private JFXButton card;

    @FXML
    public void gameView(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/blackjack/fxml/game-view.fxml"));
        Parent gameViewRoot = fxmlLoader.load();
        Scene gameViewScene = new Scene(gameViewRoot);

        // Access the primary stage from the event and set the new scene
        Stage primaryStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        primaryStage.setScene(gameViewScene);


        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(3));
        transition.setNode(card);
        transition.setToX(-200);
        transition.play();
        System.out.println("Game View");

    }



}