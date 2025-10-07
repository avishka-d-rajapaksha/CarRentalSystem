package com.mycompany.rentcar;

import com.mycompany.rentcar.dao.CustomerDAO;
import com.mycompany.rentcar.model.Customer;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AddCustomerController {

    @FXML private TextField fullNameField;
    @FXML private TextField licenseNoField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextArea addressField;
    @FXML private CheckBox blacklistCheckBox;
    @FXML private TextArea notesField;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;

    private CustomerDAO customerDAO;
    private Customer customer; // null = Add, non-null = Edit

    @FXML
    public void initialize() {
        try {
            customerDAO = new CustomerDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Cannot connect to database.");
            btnSave.setDisable(true);
        }

        // Add listener to wait until the scene is ready
        btnSave.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                // Enter = Save
                newScene.getAccelerators().put(
                        KeyCombination.keyCombination("ENTER"),
                        this::handleSave
                );
                // Esc = Cancel
                newScene.getAccelerators().put(
                        KeyCombination.keyCombination("ESCAPE"),
                        this::handleCancel
                );
            }
        });
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null) { // Edit mode
            fullNameField.setText(customer.getFullName());
            licenseNoField.setText(customer.getLicenseNo());
            phoneField.setText(customer.getPhone());
            emailField.setText(customer.getEmail());
            addressField.setText(customer.getAddress());
            blacklistCheckBox.setSelected(customer.isBlacklisted());
            notesField.setText(customer.getNotes());
        }
    }

    @FXML
    private void handleSave() {
        String fullName = fullNameField.getText() == null ? "" : fullNameField.getText().trim();
        String licenseNo = licenseNoField.getText() == null ? "" : licenseNoField.getText().trim();
        String phone = phoneField.getText() == null ? "" : phoneField.getText().trim();
        String email = emailField.getText() == null ? "" : emailField.getText().trim();
        String address = addressField.getText() == null ? "" : addressField.getText().trim();
        String notes = notesField.getText() == null ? "" : notesField.getText().trim();
        boolean isBlacklisted = blacklistCheckBox.isSelected();

        if (fullName.isEmpty() || licenseNo.isEmpty()) {
            showAlert("Validation Error", "Full Name and License No are required.");
            return;
        }

        try {
            if (customer == null) {
                Customer newCustomer = new Customer();
                newCustomer.setFullName(fullName);
                newCustomer.setLicenseNo(licenseNo);
                newCustomer.setPhone(phone);
                newCustomer.setEmail(email);
                newCustomer.setAddress(address);
                newCustomer.setBlacklisted(isBlacklisted);
                newCustomer.setNotes(notes);
                newCustomer.setCreatedAt(System.currentTimeMillis());
                customerDAO.addCustomer(newCustomer);
            } else {
                customer.setFullName(fullName);
                customer.setLicenseNo(licenseNo);
                customer.setPhone(phone);
                customer.setEmail(email);
                customer.setAddress(address);
                customer.setBlacklisted(isBlacklisted);
                customer.setNotes(notes);
                customerDAO.updateCustomer(customer);
            }
            closeForm();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to save customer. Please try again.");
        }
    }

    @FXML
    private void handleCancel() {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Optional: Enter in TextAreas should NOT close form, Shift+Enter for newline
    @FXML
    private void handleTextAreaEnter(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER && !event.isShiftDown()) {
            event.consume(); // prevent newline
            handleSave();
        }
    }
}