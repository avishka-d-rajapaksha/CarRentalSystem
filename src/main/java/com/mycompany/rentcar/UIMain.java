package com.mycompany.rentcar;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UIMain extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/mycompany/rentcar/login.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Car Rental System - Login");
        stage.setScene(scene);
        stage.setResizable(false); // keep small login window
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
