package com.mycompany.rentcar;

import com.mycompany.rentcar.dao.UserDAO;
import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class LoginController {

    // Login fields
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    // Sign-up fields
    @FXML
    private TextField usernameField1; // Full name
    @FXML
    private PasswordField passwordField1; // Confirm password
    @FXML
    private Button signUpButton;

    @FXML
    public void initialize() {
        // Move focus from username → password on Enter
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                passwordField.requestFocus();
            }
        });

        // Trigger login on Enter in password field
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLogin();
            }
        });

        // Esc closes login safely after scene is attached
        Platform.runLater(() -> {
            Scene scene = usernameField.getScene(); // now scene is available
            if (scene != null) {
                scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                    if (event.getCode() == KeyCode.ESCAPE) {
                        handleCancel();
                    }
                });
            }
        });
    }

    // Login action
    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        User user = UserDAO.login(username, password);

        if (user != null) {
            try {
                // Close login window
                Stage loginStage = (Stage) usernameField.getScene().getWindow();
                loginStage.close();

                // Load main UI
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/rentcar/Main.fxml"));
                Parent root = loader.load();

                Stage mainStage = new Stage();
                mainStage.setTitle("Car Rental System - Main");
                mainStage.setScene(new Scene(root));
                mainStage.setMaximized(true);
                mainStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Failed to open Main window.");
            }
        } else {
            showAlert("Login Failed", "Invalid username or password.");
        }
    }

    // Cancel action
    @FXML
    private void handleCancel() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    // Sign-up button action
    @FXML
    private void handleSignUpButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/rentcar/Signup.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Sign Up");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Sign Up Failed", "Cannot open Sign Up window.");
        }
    }

    // Utility for showing alerts
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
