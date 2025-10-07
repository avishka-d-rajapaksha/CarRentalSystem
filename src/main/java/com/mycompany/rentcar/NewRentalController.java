package com.mycompany.rentcar;

import com.mycompany.rentcar.dao.BookingDAO;
import com.mycompany.rentcar.dao.CustomerDAO;
import com.mycompany.rentcar.dao.VehicleDAO;
import com.mycompany.rentcar.model.Booking;
import com.mycompany.rentcar.model.Customer;
import com.mycompany.rentcar.model.Vehicle;
import com.mycompany.rentcar.util.DBConnection;
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
import java.sql.Connection;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class NewRentalController {

    @FXML private AnchorPane rootPane;

    // Customer Section
    @FXML private TextField searchCustomerField;
    @FXML private Button searchCustomerBtn;
    @FXML private TextField customerNameField;
    @FXML private TextField customerLicenseField;
    @FXML private TextField customerPhoneField;
    @FXML private TextField customerEmailField;

    // Vehicle Section
    @FXML private TextField searchVehicleField;
    @FXML private Button searchVehicleBtn;
    @FXML private TextField vehicleRegNoField;
    @FXML private TextField vehicleModelField;
    @FXML private TextField vehicleRateField;
    @FXML private TextField vehicleStatusField;

    // Rental Details
    @FXML private DatePicker pickupDatePicker;
    @FXML private DatePicker returnDatePicker;
    @FXML private TextField daysField;
    @FXML private TextField totalField;
    @FXML private TextArea notesArea;

    // Payment
    @FXML private ComboBox<String> paymentMethodCombo;
    @FXML private TextField advanceField;
    @FXML private TextField balanceField;

    // Action Buttons
    @FXML private Button confirmBtn;
    @FXML private Button printBtn;
    @FXML private Button cancelBtn;

    // Header Buttons
    @FXML private Button availabilityBtn;
    @FXML private Button customerBtn;
    @FXML private Button rentalBtn;
    @FXML private Button returnBtn;
    @FXML private Button vehiclesBtn;

    // Selected Customer & Vehicle
    private Customer selectedCustomer;
    private Vehicle selectedVehicle;

    // DAOs
    private final CustomerDAO customerDAO;
    private final VehicleDAO vehicleDAO;
    private final BookingDAO bookingDAO;

    public NewRentalController() {
        try {
            Connection conn = DBConnection.getConnection();
            customerDAO = new CustomerDAO();
            vehicleDAO = new VehicleDAO();
            bookingDAO = new BookingDAO();
        } catch (Exception e) {
            throw new RuntimeException("DAO init failed", e);
        }
    }

    @FXML
    public void initialize() {
        paymentMethodCombo.getItems().addAll("Cash", "Card", "Bank Transfer");
        setupActionButtons();
        setupHeaderButtons();
        setupShortcuts();
        setupArrowKeyNavigation();
        setupEnterKeyOnButtons();

        pickupDatePicker.valueProperty().addListener((obs, oldV, newV) -> updateRentalTotals());
        returnDatePicker.valueProperty().addListener((obs, oldV, newV) -> updateRentalTotals());
        advanceField.textProperty().addListener((obs, oldV, newV) -> updateBalance());
    }

    // ================== ACTION BUTTONS ==================
    private void setupActionButtons() {
        confirmBtn.setOnAction(e -> handleConfirm());
        cancelBtn.setOnAction(e -> handleCancel());
        searchCustomerBtn.setOnAction(e -> handleSearchCustomer());
        searchVehicleBtn.setOnAction(e -> handleSearchVehicle());
    }

    // ================== HEADER BUTTONS ==================
    private void setupHeaderButtons() {
        setupHeaderButton(availabilityBtn, "Availability.fxml", "Availability");
        setupHeaderButton(customerBtn, "Customer.fxml", "Customer");
        setupHeaderButton(rentalBtn, "Rental.fxml", "Rental");
        setupHeaderButton(returnBtn, "Return.fxml", "Return");
        setupHeaderButton(vehiclesBtn, "Vehical.fxml", "Vehicles");
    }

    private void setupHeaderButton(Button btn, String fxml, String title) {
        btn.setOnAction(e -> openWindowFullScreen(fxml, title));
        btn.setFocusTraversable(true);
        btn.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) openWindowFullScreen(fxml, title);
        });
    }

    private void openWindowFullScreen(String fxml, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/rentcar/" + fxml));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setMaximized(true);
            stage.show();
            rootPane.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(title + " Failed", "Cannot open " + title + " window.");
        }
    }

    // ================== SHORTCUTS ==================
    private void setupShortcuts() {
        rootPane.setFocusTraversable(true);
        rootPane.setOnKeyPressed(this::handleKeyShortcuts);
    }

    private void handleKeyShortcuts(KeyEvent event) {
        KeyCode code = event.getCode();
        if (code == KeyCode.ESCAPE) handleExit(null);
        else if (code == KeyCode.F1) openWindowFullScreen("Availability.fxml", "Availability");
        else if (code == KeyCode.F2) openWindowFullScreen("Customer.fxml", "Customer");
        else if (code == KeyCode.F3) openWindowFullScreen("Rental.fxml", "Rental");
        else if (code == KeyCode.F4) openWindowFullScreen("Return.fxml", "Return");
        else if (code == KeyCode.F5) openWindowFullScreen("Vehical.fxml", "Vehicles");
        else if (code == KeyCode.F6) handleRefresh();
        else if (event.isControlDown() && code == KeyCode.R) handleReset();
        else if (event.isControlDown() && code == KeyCode.B) handleBook();
        else if (code == KeyCode.ENTER) handleSearch();
    }

    // ================== ENTER KEY ON BUTTONS ==================
    private void setupEnterKeyOnButtons() {
        confirmBtn.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) handleConfirm(); });
        cancelBtn.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) handleCancel(); });
        searchCustomerBtn.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) handleSearchCustomer(); });
        searchVehicleBtn.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) handleSearchVehicle(); });
    }

    // ================== ARROW NAVIGATION ==================
    private void setupArrowKeyNavigation() {
        rootPane.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP, LEFT -> traverseFocus(-1);
                case DOWN, RIGHT -> traverseFocus(1);
            }
        });
    }

    private void traverseFocus(int direction) {
        var nodes = rootPane.getChildrenUnmodifiable().filtered(node -> node.isFocusTraversable());
        if (nodes.isEmpty()) return;
        var focused = rootPane.getScene().getFocusOwner();
        int idx = nodes.indexOf(focused);
        if (idx == -1) idx = 0;
        int nextIdx = (idx + direction + nodes.size()) % nodes.size();
        nodes.get(nextIdx).requestFocus();
    }

    // ================== CUSTOMER SEARCH ==================
    private void handleSearchCustomer() {
        String query = searchCustomerField.getText().trim();
        if (query.isEmpty()) return;
        try {
            List<Customer> customers = customerDAO.searchByLicenseOrName(query);
            if (!customers.isEmpty()) {
                selectedCustomer = customers.get(0);
                customerNameField.setText(selectedCustomer.getFullName());
                customerLicenseField.setText(selectedCustomer.getLicenseNo());
                customerPhoneField.setText(selectedCustomer.getPhone());
                customerEmailField.setText(selectedCustomer.getEmail());
            } else {
                showAlert("Customer not found", "No customer matches your search.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to search customer: " + e.getMessage());
        }
    }

    // ================== VEHICLE SEARCH ==================
    private void handleSearchVehicle() {
        String query = searchVehicleField.getText().trim();
        if (query.isEmpty()) return;
        try {
            List<Vehicle> vehicles = vehicleDAO.searchByRegNoOrName(query);
            if (!vehicles.isEmpty()) {
                selectedVehicle = vehicles.get(0);
                vehicleRegNoField.setText(selectedVehicle.getLicensePlate());
                vehicleModelField.setText(selectedVehicle.getFullName());
                vehicleRateField.setText(String.valueOf(selectedVehicle.getDailyRate()));
                vehicleStatusField.setText(selectedVehicle.isAvailable() ? "Available" : "Booked");
                updateRentalTotals();
            } else {
                showAlert("Vehicle not found", "No vehicle matches your search.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to search vehicle: " + e.getMessage());
        }
    }

    // ================== RENTAL CALCULATIONS ==================
    private void updateRentalTotals() {
        if (pickupDatePicker.getValue() != null && returnDatePicker.getValue() != null && selectedVehicle != null) {
            LocalDate start = pickupDatePicker.getValue();
            LocalDate end = returnDatePicker.getValue();
            long days = ChronoUnit.DAYS.between(start, end);
            if (days <= 0) days = 1;

            daysField.setText(String.valueOf(days));
            double total = days * selectedVehicle.getDailyRate();
            totalField.setText(String.format("%.2f", total));
            updateBalance();
        }
    }

    private void updateBalance() {
        try {
            double total = Double.parseDouble(totalField.getText().isEmpty() ? "0" : totalField.getText());
            double advance = Double.parseDouble(advanceField.getText().isEmpty() ? "0" : advanceField.getText());
            double balance = total - advance;
            if (balance < 0) balance = 0;
            balanceField.setText(String.format("%.2f", balance));
        } catch (NumberFormatException e) {
            balanceField.setText(totalField.getText());
        }
    }

    // ================== CONFIRM RENTAL ==================
    private void handleConfirm() {
        if (selectedCustomer == null || selectedVehicle == null) {
            showAlert("Missing Data", "Select both customer and vehicle before confirming.");
            return;
        }
        try {
            Booking booking = new Booking();
            booking.setCustomerId(selectedCustomer.getId());
            booking.setVehicleId(selectedVehicle.getId());

            long pickupEpoch = pickupDatePicker.getValue().atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC);
            long returnEpoch = returnDatePicker.getValue().atStartOfDay().toEpochSecond(java.time.ZoneOffset.UTC);

            booking.setPickupTs(pickupEpoch);
            booking.setReturnTs(returnEpoch);

            long days = ChronoUnit.DAYS.between(pickupDatePicker.getValue(), returnDatePicker.getValue());
            if (days <= 0) days = 1;

            double dailyRate = selectedVehicle.getDailyRate();
            int totalCents = (int) Math.round(dailyRate * days * 100);

            double advance = Double.parseDouble(advanceField.getText().isEmpty() ? "0" : advanceField.getText());
            int advanceCents = (int) Math.round(advance * 100);

            booking.setRateCents(totalCents);
            booking.setDepositCents(advanceCents);
            booking.setTaxRateBp(0);
            booking.setDiscountCents(0);
            booking.setPickupOdometer(null);
            booking.setReturnOdometer(null);
            booking.setLateFeeCents(0);
            booking.setStatus("RESERVED");
            booking.setNotes(notesArea.getText());
            booking.setCreatedBy(1); // TODO: Logged-in user ID
            booking.setCreatedAt(System.currentTimeMillis() / 1000L);

            bookingDAO.addBooking(booking);

            selectedVehicle.setAvailable(false);
            vehicleDAO.updateVehicle(selectedVehicle);

            showAlert("Booking Confirmed", "Rental has been successfully saved!");
            clearForm();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to confirm booking: " + e.getMessage());
        }
    }

    // ================== CANCEL ==================
    private void handleCancel() {
        rootPane.getScene().getWindow().hide();
    }

    // ================== EXIT TO DASHBOARD ==================
    @FXML
    private void handleExit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/rentcar/Customer.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Dash Board");
            stage.setMaximized(true);
            stage.show();

            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Navigation Failed", "Cannot return to main window.");
        }
    }

    // ================== UTILITIES ==================
    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void clearForm() {
        selectedCustomer = null;
        selectedVehicle = null;

        customerNameField.clear();
        customerLicenseField.clear();
        customerPhoneField.clear();
        customerEmailField.clear();

        vehicleRegNoField.clear();
        vehicleModelField.clear();
        vehicleRateField.clear();
        vehicleStatusField.clear();

        pickupDatePicker.setValue(null);
        returnDatePicker.setValue(null);
        daysField.clear();
        totalField.clear();
        notesArea.clear();
        advanceField.clear();
        balanceField.clear();
    }

    private void handleReset() { clearForm(); }
    private void handleRefresh() { /* reload tables if needed */ }
    private void handleBook() { handleConfirm(); }
    private void handleSearch() {
        handleSearchCustomer();
        handleSearchVehicle();
    }
}