package com.mycompany.rentcar;

import com.mycompany.rentcar.dao.CustomerDAO;
import com.mycompany.rentcar.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class CustomerController {

    @FXML private AnchorPane rootPane;  // AnchorPane for shortcut handling
    @FXML private TextField searchField;
    @FXML private ComboBox<String> blacklistFilter;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, Integer> colId;
    @FXML private TableColumn<Customer, String> colFullName, colLicenseNo, colPhone, colEmail, colAddress, colNotes;
    @FXML private TableColumn<Customer, Boolean> colBlacklisted;
    @FXML private Button searchBtn, resetBtn, addBtn, editBtn, deleteBtn, refreshBtn, exitBtn;
    @FXML private Button availabilityBtn, customerBtn, rentalBtn, returnBtn, vehiclesBtn;

    private CustomerDAO customerDAO;
    private ObservableList<Customer> customerList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        try {
            customerDAO = new CustomerDAO();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("DB Error", "Cannot connect to database.");
        }

        setupTable();
        setupFilters();
        loadCustomers();
        setupRowDoubleClick();
        setupHeaderButtons();
        setupMainButtons();

        // Add key listener for shortcuts
        rootPane.setOnKeyPressed(this::handleKeyboardShortcuts);
    }

    // ------------------- Table Setup -------------------
    private void setupTable() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());
        colFullName.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFullName()));
        colLicenseNo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLicenseNo()));
        colPhone.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPhone()));
        colEmail.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        colAddress.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAddress()));
        colBlacklisted.setCellValueFactory(c -> new javafx.beans.property.SimpleBooleanProperty(c.getValue().isBlacklisted()).asObject());
        colNotes.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNotes()));

        customerTable.setItems(customerList);
    }

    private void setupFilters() {
        blacklistFilter.getItems().addAll("All", "Blacklisted", "Active");
        blacklistFilter.setValue("All");
    }

    private void loadCustomers() {
        try {
            customerList.clear();
            List<Customer> data = customerDAO.searchByLicenseOrName("");
            customerList.addAll(data);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Load Error", "Failed to load customers.");
        }
    }

    // ------------------- Button Actions -------------------
    @FXML private void handleSearch(ActionEvent event) {
        String searchInput = searchField.getText().trim();
        String filter = blacklistFilter.getValue();
        try {
            List<Customer> data = customerDAO.searchByLicenseOrName(searchInput);
            if ("Blacklisted".equals(filter)) data.removeIf(c -> !c.isBlacklisted());
            else if ("Active".equals(filter)) data.removeIf(Customer::isBlacklisted);
            customerList.setAll(data);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Search Error", "Failed to search customers.");
        }
    }

    @FXML private void handleReset(ActionEvent event) {
        searchField.clear();
        blacklistFilter.setValue("All");
        loadCustomers();
    }

    @FXML private void handleAdd(ActionEvent event) { openAddEditForm(null); }

    @FXML private void handleEdit(ActionEvent event) {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("No Selection", "Please select a customer to edit."); return; }
        openAddEditForm(selected);
    }

    @FXML private void handleDelete(ActionEvent event) {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) { showAlert("No Selection", "Please select a customer to delete."); return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete customer: " + selected.getFullName() + "?", ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(res -> {
            if (res == ButtonType.YES) {
                try {
                    customerDAO.deleteCustomer(selected.getId());
                    loadCustomers();
                } catch (SQLException e) {
                    e.printStackTrace();
                    showAlert("Delete Error", "Failed to delete customer.");
                }
            }
        });
    }

    @FXML private void handleRefresh(ActionEvent event) { loadCustomers(); }

    @FXML private void handleExit(ActionEvent event) { openModuleFullScreen("Availability.fxml", "Dash Board"); }

    // ------------------- Add/Edit Form -------------------
    private void openAddEditForm(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/rentcar/addCustomer.fxml"));
            Parent root = loader.load();
            if (customer != null) {
                AddCustomerController ctrl = loader.getController();
                ctrl.setCustomer(customer);
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(customer == null ? "Add Customer" : "Edit Customer");
            stage.showAndWait();
            loadCustomers();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Form Error", "Cannot open Add/Edit Customer form.");
        }
    }

    // ------------------- Row Double Click -------------------
    private void setupRowDoubleClick() {
        customerTable.setRowFactory(tv -> {
            TableRow<Customer> row = new TableRow<>();
            row.setOnMouseClicked(ev -> {
                if (ev.getClickCount() == 2 && !row.isEmpty()) openAddEditForm(row.getItem());
            });
            return row;
        });
    }

    // ------------------- Header Buttons -------------------
    private void setupHeaderButtons() {
        setupHeaderButton(rootPane, availabilityBtn, "Availability.fxml", "Availability", KeyCode.F1);
        setupHeaderButton(rootPane, customerBtn, "Customer.fxml", "Customer", KeyCode.F2);
        setupHeaderButton(rootPane, rentalBtn, "Rental.fxml", "Rental", KeyCode.F3);
        setupHeaderButton(rootPane, returnBtn, "Return.fxml", "Return", KeyCode.F4);
        setupHeaderButton(rootPane, vehiclesBtn, "Vehical.fxml", "Vehicles", KeyCode.F5);
    }

    private void setupMainButtons() {
        setupButton(searchBtn, () -> handleSearch(null), KeyCode.S);    // Ctrl+S
        setupButton(resetBtn, () -> handleReset(null), KeyCode.R);      // Ctrl+R
        setupButton(addBtn, () -> handleAdd(null), KeyCode.A);          // Ctrl+A
        setupButton(editBtn, () -> handleEdit(null), KeyCode.E);        // Ctrl+E
        setupButton(deleteBtn, () -> handleDelete(null), KeyCode.D);    // Ctrl+D
        setupButton(refreshBtn, () -> handleRefresh(null), KeyCode.F6); // F6
        setupButton(exitBtn, () -> handleExit(null), KeyCode.ESCAPE);   // ESC
    }

    // ------------------- Keyboard Shortcuts -------------------
    private void handleKeyboardShortcuts(KeyEvent event) {
        KeyCode code = event.getCode();

        if (code == KeyCode.ESCAPE) handleExit(null);
        else if (code == KeyCode.F1) openModuleFullScreen("Availability.fxml", "Availability");
        else if (code == KeyCode.F2) openModuleFullScreen("Customer.fxml", "Customer");
        else if (code == KeyCode.F3) openModuleFullScreen("Rental.fxml", "Rental");
        else if (code == KeyCode.F4) openModuleFullScreen("Return.fxml", "Return");
        else if (code == KeyCode.F5) openModuleFullScreen("Vehical.fxml", "Vehicles");
        else if (code == KeyCode.F6) handleRefresh(null);
        else if (event.isControlDown() && code == KeyCode.R) handleReset(null);
        else if (code == KeyCode.ENTER) handleSearch(null);
    }

    // ------------------- Button Helpers -------------------
    private void setupButton(Button btn, Runnable action, KeyCode shortcut) {
        btn.setOnAction(e -> action.run());
        btn.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) action.run(); });
    }

    private void setupHeaderButton(AnchorPane root, Button btn, String fxmlFile, String title, KeyCode shortcut) {
        btn.setOnAction(ev -> openModuleFullScreen(fxmlFile, title));
        btn.setFocusTraversable(true);
        btn.setOnKeyPressed(ev -> { if (ev.getCode() == KeyCode.ENTER) openModuleFullScreen(fxmlFile, title); });
    }

    private void openModuleFullScreen(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/rentcar/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setMaximized(true);
            stage.show();

            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Cannot open " + title + " window.");
        }
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.show();
    }

}