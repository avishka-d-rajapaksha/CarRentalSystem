package com.mycompany.rentcar;

import com.mycompany.rentcar.dao.VehicleDAO;
import com.mycompany.rentcar.model.Vehicle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class AvailabilityController {

    // Link this to the main AnchorPane in FXML
    @FXML
    private AnchorPane rootPane;  // <- add this
    
    // Table & Columns
    @FXML private TableView<Vehicle> availabilityTable;
    @FXML private TableColumn<Vehicle, Integer> colId;
    @FXML private TableColumn<Vehicle, String> colMake;
    @FXML private TableColumn<Vehicle, String> colModel;
    @FXML private TableColumn<Vehicle, Integer> colYear;
    @FXML private TableColumn<Vehicle, String> colLicensePlate;
    @FXML private TableColumn<Vehicle, String> colColor;
    @FXML private TableColumn<Vehicle, Double> colDailyRate;
    @FXML private TableColumn<Vehicle, String> colStatus;
    @FXML private TableColumn<Vehicle, Integer> colMileage;
    @FXML private TableColumn<Vehicle, String> colFuelType;
    @FXML private TableColumn<Vehicle, String> colTransmission;
    @FXML private TableColumn<Vehicle, Integer> colSeatingCapacity;

    // Header Buttons
    @FXML private Button availabilityBtn;
    @FXML private Button customerBtn;
    @FXML private Button rentalBtn;
    @FXML private Button returnBtn;
    @FXML private Button vehiclesBtn;

    // Search & Filter
    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusFilter;
    @FXML private Button searchBtn;
    @FXML private Button resetBtn;

    // Action Buttons
    @FXML private Button bookBtn;
    @FXML private Button refreshBtn;
    @FXML private Button exitBtn;



    private ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();

@FXML
public void initialize() {
    setupTable();
    loadVehicles();
    setupFilters();
    setupShortcuts();

    // Book button: single click
    bookBtn.setOnAction(event -> handleBook());

    // Table row: double-click
    availabilityTable.setRowFactory(tv -> {
        TableRow<Vehicle> row = new TableRow<>();
        row.setOnMouseClicked(event -> {
            if (!row.isEmpty() && event.getClickCount() == 2) handleBook();
        });
        return row;
    });

    // Header buttons: mouse click + Enter key
    setupHeaderButton(availabilityBtn, "Availability.fxml", "Availability");
    setupHeaderButton(customerBtn, "Customer.fxml", "Customer");
    setupHeaderButton(rentalBtn, "Rental.fxml", "Rental");
    setupHeaderButton(returnBtn, "Return.fxml", "Return");
    setupHeaderButton(vehiclesBtn, "Vehical.fxml", "Vehicles");
}

// Helper method
private void setupHeaderButton(Button btn, String fxml, String title) {
    btn.setOnAction(e -> openWindow(fxml, title));  // mouse click
    btn.setFocusTraversable(true);                 // allow keyboard focus
    btn.setOnKeyPressed(event -> {
        if (event.getCode() == KeyCode.ENTER) {
            openWindow(fxml, title);               // Enter key
        }
    });
}

    private void setupTable() {
        colId.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getId()).asObject());
        colMake.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getMake()));
        colModel.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getModel()));
        colYear.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getYear()).asObject());
        colLicensePlate.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getLicensePlate()));
        colColor.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getColor()));
        colDailyRate.setCellValueFactory(cell -> new javafx.beans.property.SimpleDoubleProperty(cell.getValue().getDailyRate()).asObject());
        colStatus.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getStatus()));
        colMileage.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getMileage()).asObject());
        colFuelType.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getFuelType()));
        colTransmission.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getTransmission()));
        colSeatingCapacity.setCellValueFactory(cell -> new javafx.beans.property.SimpleIntegerProperty(cell.getValue().getSeatingCapacity()).asObject());
    }

    private void loadVehicles() {
        List<Vehicle> vehicles = VehicleDAO.getAllVehicles();
        vehicleList.setAll(vehicles);
        availabilityTable.setItems(vehicleList);
    }

    private void setupFilters() {
        statusFilter.getItems().addAll("All", "Available", "Rented", "Maintenance");
        statusFilter.setValue("All");
    }

    // ================== BUTTON ACTIONS ==================
    @FXML
    private void handleSearch() {
        String keyword = searchField.getText().toLowerCase();
        String status = statusFilter.getValue();

        List<Vehicle> filtered = vehicleList.stream()
            .filter(v -> v.getModel().toLowerCase().contains(keyword) || v.getLicensePlate().toLowerCase().contains(keyword))
            .filter(v -> status.equals("All") || v.getStatus().equalsIgnoreCase(status))
            .collect(Collectors.toList());

        availabilityTable.setItems(FXCollections.observableArrayList(filtered));
    }

    @FXML
    private void handleReset() {
        searchField.clear();
        statusFilter.setValue("All");
        loadVehicles();
    }

    @FXML
    private void handleBook() {
        Vehicle selected = availabilityTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("No Selection", "Please select a vehicle to book.");
            return;
        }
        if (!"Available".equalsIgnoreCase(selected.getStatus())) {
            showAlert("Unavailable", "Selected vehicle is not available.");
            return;
        }

        // ✅ Instead of just marking rented → open Rental.fxml
        openWindow("Rental.fxml", "Rental");
    }

    @FXML
    private void handleRefresh() {
        loadVehicles();
    }

    @FXML
    private void handleExit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/rentcar/Main.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dash Bord");
            stage.setMaximized(true); // full screen effect
            stage.show();
            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
            showAlert("Login Failed", "Cannot return to Login window.");
        }
    }

    // ================== SHORTCUTS ==================
    private void setupShortcuts() {
        rootPane.setFocusTraversable(true);

        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) handleExit(null);         // Exit → Login
            else if (event.getCode() == KeyCode.F1) openWindow("Availability.fxml", "Availability");
            else if (event.getCode() == KeyCode.F2) openWindow("Customer.fxml", "Customer");
            else if (event.getCode() == KeyCode.F3) openWindow("Rental.fxml", "Rental");
            else if (event.getCode() == KeyCode.F4) openWindow("Return.fxml", "Return");
            else if (event.getCode() == KeyCode.F5) openWindow("Vehical.fxml", "Vehicles");
            else if (event.getCode() == KeyCode.F6) handleRefresh();
            else if (event.isControlDown() && event.getCode() == KeyCode.R) handleReset();
            else if (event.isControlDown() && event.getCode() == KeyCode.B) handleBook();
            else if (event.getCode() == KeyCode.ENTER) handleSearch();
        });
    }

    // ================== NAVIGATION (LIKE DASHBOARD) ==================
     private void openWindow(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/rentcar/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);

            // Open full screen
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setMaximized(true); // full screen effect
            stage.show();

            // Hide current dashboard
            rootPane.getScene().getWindow().hide();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(title + " Failed", "Cannot open " + title + " window.");
        }
    }


    // ================== HEADER BUTTONS ==================
    @FXML private void handleAvailabilityBtn(ActionEvent event) { openWindow("Availability.fxml", "Availability"); }
    @FXML private void handleCustomerBtn(ActionEvent event) { openWindow("Customer.fxml", "Customer"); }
    @FXML private void handleRentalBtn(ActionEvent event) { openWindow("Rental.fxml", "Rental"); }
    @FXML private void handleReturnBtn(ActionEvent event) { openWindow("Return.fxml", "Return"); }
    @FXML private void handleVehiclesBtn(ActionEvent event) { openWindow("Vehical.fxml", "Vehicles"); }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}