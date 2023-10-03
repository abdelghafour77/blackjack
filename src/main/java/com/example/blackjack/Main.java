package com.example.blackjack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("fxml/start-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
//        stage.setFullScreen(true);
//        stage.setResizable(false);
        stage.setTitle("BlackJack");
       // stage.getIcons().add(new Image("com/example/blackjack/images/icon.png"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}