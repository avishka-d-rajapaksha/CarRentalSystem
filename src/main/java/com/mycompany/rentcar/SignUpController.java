package com.mycompany.rentcar;

import com.mycompany.rentcar.dao.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField; // added confirm password field

    @FXML
    private TextField fullNameField;

    // Triggered when user clicks "Sign Up" button
    @FXML
    private void handleSignUp() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();
        String fullName = fullNameField.getText().trim();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || fullName.isEmpty()) {
            showAlert("Error", "Please fill all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Error", "Passwords do not match");
            return;
        }

        // Create new user object using no-arg constructor
        User user = new User();
        user.setUsername(username);
        user.setPasswordHash(password); // assuming User class has setPasswordHash()
        user.setFullName(fullName);
        user.setRole("USER");            // default role
        user.setActive(true);            // is_active

        // Save user using DAO
        UserDAO userDAO = new UserDAO();
        boolean success = userDAO.signUp(user);

        if (success) {
            showAlert("Success", "User signed up successfully");
            closeWindow();
        } else {
            showAlert("Error", "Sign up failed. Username may already exist.");
        }
    }

    // Close Sign Up window
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }

    // Utility method for alerts
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
