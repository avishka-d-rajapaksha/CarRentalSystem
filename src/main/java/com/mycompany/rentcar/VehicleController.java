package com.mycompany.rentcar;

import com.mycompany.rentcar.dao.VehicleDAO;
import com.mycompany.rentcar.model.Vehicle;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class VehicleController {

    // Root
    @FXML private AnchorPane rootPane;

    // Header nav buttons
    @FXML private Button availabilityBtn, customerBtn, rentalBtn, returnBtn, vehiclesBtn;

    // List pane
    @FXML private VBox vehicleListPane;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> statusCombo;
    @FXML private ComboBox<String> colorCombo;
    @FXML private TextField yearFromField, yearToField;
    @FXML private Button searchBtn, resetBtn, addVehicleBtn, deleteVehicleBtn, refreshBtn, exitBtn;

    // Table
    @FXML private TableView<Vehicle> vehicleTable;
    @FXML private TableColumn<Vehicle, Integer> colId;
    @FXML private TableColumn<Vehicle, String> colPlateNo, colVin, colMakeModel, colYear, colColor, colOdometer, colDailyRate, colStatus, colNotes;

    // Form pane (embedded)
    @FXML private VBox vehicleFormPane;
    @FXML private Label formTitle;
    @FXML private TextField plateNoField, vinField, makeField, modelField, yearField, odometerField, dailyRateField;
    @FXML private ComboBox<String> colorFormCombo, statusFormCombo;
    @FXML private TextArea notesArea;
    @FXML private Button saveBtn, deleteBtn, cancelBtn;

    private final ObservableList<Vehicle> masterList = FXCollections.observableArrayList();
    private final ObservableList<Vehicle> viewList   = FXCollections.observableArrayList();

    private Vehicle editingVehicle = null; // null = add-mode; non-null = edit-mode

    private final NumberFormat rateFmt = NumberFormat.getNumberInstance(new Locale("en", "LK"));

    @FXML
    public void initialize() {
        rateFmt.setMinimumFractionDigits(2);
        rateFmt.setMaximumFractionDigits(2);

        setupTable();
        setupFilters();
        setupButtonsAndShortcuts();

        reloadVehicles();
        showListPane();
    }

    // ------------------- Setup -------------------

    private void setupTable() {
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getId()).asObject());
        colPlateNo.setCellValueFactory(c -> new SimpleStringProperty(safe(c.getValue().getLicensePlate())));
        colVin.setCellValueFactory(c -> new SimpleStringProperty("-")); // VIN not persisted in current model/DAO
        colMakeModel.setCellValueFactory(c -> new SimpleStringProperty(safe(c.getValue().getMake()) + " / " + safe(c.getValue().getModel())));
        colYear.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getYear() > 0 ? String.valueOf(c.getValue().getYear()) : "-"));
        colColor.setCellValueFactory(c -> new SimpleStringProperty(safe(c.getValue().getColor())));
        colOdometer.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getMileage() > 0 ? String.valueOf(c.getValue().getMileage()) : "-"));
        colDailyRate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDailyRate() > 0 ? ("LKR " + rateFmt.format(c.getValue().getDailyRate())) : "LKR 0.00"));
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(safe(c.getValue().getStatus())));
        colNotes.setCellValueFactory(c -> new SimpleStringProperty("-")); // not persisted

        vehicleTable.setItems(viewList);

        // Double click -> edit
        vehicleTable.setRowFactory(tv -> {
            TableRow<Vehicle> row = new TableRow<>();
            row.setOnMouseClicked(ev -> {
                if (ev.getClickCount() == 2 && !row.isEmpty()) openEdit(row.getItem());
            });
            return row;
        });

        // Keyboard on table
        vehicleTable.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.DELETE) {
                handleDeleteFromList();
            } else if (ev.getCode() == KeyCode.ENTER) {
                Vehicle sel = vehicleTable.getSelectionModel().getSelectedItem();
                if (sel != null) openEdit(sel);
            }
        });
    }

    private void setupFilters() {
        statusCombo.getItems().setAll("All", "AVAILABLE", "RENTED", "MAINTENANCE", "RETIRED");
        statusCombo.setValue("All");

        colorCombo.getItems().setAll("All Colors", "White", "Black", "Silver", "Blue", "Red", "Grey");
        colorCombo.setValue("All Colors");

        colorFormCombo.getItems().setAll("White", "Black", "Silver", "Blue", "Red", "Grey", "Green", "Yellow");
        statusFormCombo.getItems().setAll("AVAILABLE", "RENTED", "MAINTENANCE", "RETIRED");
    }

    private void setupButtonsAndShortcuts() {
        // List actions
        searchBtn.setOnAction(e -> applyFilters());
        resetBtn.setOnAction(e -> resetFilters());
        addVehicleBtn.setOnAction(e -> openAdd());
        deleteVehicleBtn.setOnAction(e -> handleDeleteFromList());
        refreshBtn.setOnAction(e -> reloadVehicles());
        exitBtn.setOnAction(e -> openModuleCandidates(new String[]{"Main.fxml", "Availability.fxml"}, "Dashboard"));

        // Header nav
        availabilityBtn.setOnAction(e -> openModule("Availability.fxml", "Availability"));
        customerBtn.setOnAction(e -> openModule("Customer.fxml", "Customer"));
        rentalBtn.setOnAction(e -> openModule("Rental.fxml", "Rental"));
        returnBtn.setOnAction(e -> openModule("Return.fxml", "Return"));
        vehiclesBtn.setOnAction(e -> reloadVehicles()); // current page

        // Form actions
        saveBtn.setOnAction(e -> handleSave());
        deleteBtn.setOnAction(e -> handleDeleteFromForm());
        cancelBtn.setOnAction(e -> {
            if (vehicleFormPane.isVisible()) showListPane();
            else openModuleCandidates(new String[]{"Main.fxml", "Availability.fxml"}, "Dashboard");
        });

        // Enter on search
        searchField.setOnAction(e -> applyFilters());

        // Scene accelerators
        Platform.runLater(() -> {
            if (rootPane == null) return;
            Scene scene = rootPane.getScene();
            if (scene == null) {
                rootPane.sceneProperty().addListener((o, ov, nv) -> { if (nv != null) wireAccelerators(nv); });
            } else {
                wireAccelerators(scene);
            }
        });

        // Root-level key handling
        if (rootPane != null) {
            rootPane.setFocusTraversable(true);
            rootPane.setOnKeyPressed(this::handleKeyboardShortcuts);
        }
    }

    private void wireAccelerators(Scene scene) {
        // Navigation
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F1), () -> openModule("Availability.fxml", "Availability"));
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F2), () -> openModule("Customer.fxml", "Customer"));
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F3), () -> openModule("Rental.fxml", "Rental"));
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F4), () -> openModule("Return.fxml", "Return"));
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F5), () -> openModuleCandidates(new String[]{"Vehicle.fxml", "Vehical.fxml"}, "Vehicles"));
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F6), this::reloadVehicles);

        // Actions
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN), this::resetFilters);
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.N, KeyCombination.ALT_DOWN), this::openAdd);

        // Save / Delete behave based on visible pane
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), () -> {
            if (vehicleFormPane.isVisible()) handleSave();
            else applyFilters();
        });
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN), () -> {
            if (vehicleFormPane.isVisible()) handleDeleteFromForm();
            else handleDeleteFromList();
        });

        // Esc: close form or go back
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.ESCAPE), () -> {
            if (vehicleFormPane.isVisible()) showListPane();
            else openModuleCandidates(new String[]{"Vehical.fxml"}, "Dashboard");
        });
    }

    // ------------------- Data load & filters -------------------

    private void reloadVehicles() {
        Task<List<Vehicle>> task = new Task<>() {
            @Override protected List<Vehicle> call() { return VehicleDAO.getAllVehicles(); }
        };
        task.setOnSucceeded(e -> {
            masterList.setAll(task.getValue());
            populateColorsFromData();
            applyFilters();
        });
        task.setOnFailed(e -> {
            task.getException().printStackTrace();
            showError("Load Error", "Failed to load vehicles.");
        });
        new Thread(task, "load-vehicles").start();
    }

    private void populateColorsFromData() {
        Set<String> colors = masterList.stream()
                .map(v -> safe(v.getColor()))
                .filter(s -> !s.isBlank())
                .collect(Collectors.toCollection(TreeSet::new));
        List<String> listColors = new ArrayList<>();
        listColors.add("All Colors");
        listColors.addAll(colors);
        colorCombo.getItems().setAll(listColors);
        if (!colorCombo.getItems().contains(colorCombo.getValue())) colorCombo.setValue("All Colors");
    }

    private void applyFilters() {
        String q = safe(searchField.getText()).toLowerCase(Locale.ROOT).trim();
        String status = statusCombo.getValue();
        String color = colorCombo.getValue();
        Integer yFrom = parseIntOrNull(yearFromField.getText());
        Integer yTo   = parseIntOrNull(yearToField.getText());

        List<Vehicle> filtered = masterList.stream()
                .filter(v -> q.isBlank() || (safe(v.getLicensePlate()) + " " + safe(v.getMake()) + " " + safe(v.getModel())).toLowerCase(Locale.ROOT).contains(q))
                .filter(v -> status == null || "All".equalsIgnoreCase(status) || status.equalsIgnoreCase(safe(v.getStatus())))
                .filter(v -> color == null || "All Colors".equalsIgnoreCase(color) || color.equalsIgnoreCase(safe(v.getColor())))
                .filter(v -> (yFrom == null || v.getYear() == 0 || v.getYear() >= yFrom) && (yTo == null || v.getYear() == 0 || v.getYear() <= yTo))
                .collect(Collectors.toList());

        viewList.setAll(filtered);
        if (!filtered.isEmpty()) vehicleTable.getSelectionModel().select(0);
    }

    private void resetFilters() {
        searchField.clear();
        statusCombo.setValue("All");
        colorCombo.setValue("All Colors");
        yearFromField.clear();
        yearToField.clear();
        applyFilters();
    }

    // ------------------- List actions -------------------

    private void handleDeleteFromList() {
        Vehicle sel = vehicleTable.getSelectionModel().getSelectedItem();
        if (sel == null) { showWarn("Delete", "Select a vehicle to delete."); return; }
        if (!confirm("Delete Vehicle", "Delete vehicle " + safe(sel.getLicensePlate()) + "?")) return;

        boolean ok = VehicleDAO.deleteVehicle(sel.getId());
        if (ok) {
            masterList.remove(sel);
            viewList.remove(sel);
            showInfo("Deleted", "Vehicle removed.");
        } else {
            showError("Delete Error", "Failed to delete vehicle.");
        }
    }

    // ------------------- Form actions (embedded) -------------------

    private void openAdd() {
        editingVehicle = null;
        formTitle.setText("Add New Vehicle");
        deleteBtn.setVisible(false);

        plateNoField.clear();
        vinField.clear(); // not persisted
        makeField.clear();
        modelField.clear();
        yearField.clear();
        colorFormCombo.setValue(null);
        odometerField.clear();
        dailyRateField.clear();
        statusFormCombo.setValue("AVAILABLE");
        notesArea.clear(); // not persisted

        showFormPane();
        Platform.runLater(plateNoField::requestFocus);
    }

    private void openEdit(Vehicle v) {
        editingVehicle = v;
        formTitle.setText("Edit Vehicle - " + safe(v.getLicensePlate()));
        deleteBtn.setVisible(true);

        plateNoField.setText(safe(v.getLicensePlate()));
        vinField.setText("");
        makeField.setText(safe(v.getMake()));
        modelField.setText(safe(v.getModel()));
        yearField.setText(v.getYear() > 0 ? String.valueOf(v.getYear()) : "");
        colorFormCombo.setValue(safe(v.getColor()).isBlank() ? null : v.getColor());
        odometerField.setText(v.getMileage() > 0 ? String.valueOf(v.getMileage()) : "");
        dailyRateField.setText(v.getDailyRate() > 0 ? rateFmt.format(v.getDailyRate()) : "");
        statusFormCombo.setValue(safe(v.getStatus()).isBlank() ? "AVAILABLE" : v.getStatus());
        notesArea.setText("");

        showFormPane();
        Platform.runLater(plateNoField::requestFocus);
    }

    private void handleSave() {
        String plate = safe(plateNoField.getText()).trim();
        String make  = safe(makeField.getText()).trim();
        String model = safe(modelField.getText()).trim();
        Integer year = parseIntOrNull(yearField.getText());
        String color = colorFormCombo.getValue();
        Integer odometer = parseIntOrNull(odometerField.getText());
        Double dailyRate = parseDoubleOrNull(dailyRateField.getText());
        String status = statusFormCombo.getValue();

        if (plate.isBlank() || make.isBlank() || model.isBlank() || year == null || dailyRate == null || status == null) {
            showWarn("Validation", "Please fill Plate, Make, Model, Year, Daily Rate, Status.");
            return;
        }

        if (editingVehicle == null) {
            Vehicle nv = new Vehicle();
            nv.setLicensePlate(plate);
            nv.setMake(make);
            nv.setModel(model);
            nv.setYear(year);
            nv.setColor(color == null ? "" : color);
            nv.setMileage(odometer == null ? 0 : odometer);
            nv.setDailyRate(dailyRate);
            nv.setStatus(status);

            boolean ok = VehicleDAO.addVehicle(nv);
            if (ok) {
                showInfo("Saved", "Vehicle added.");
                reloadVehicles();
                showListPane();
            } else {
                showError("Save Error", "Failed to add vehicle.");
            }
        } else {
            editingVehicle.setLicensePlate(plate);
            editingVehicle.setMake(make);
            editingVehicle.setModel(model);
            editingVehicle.setYear(year);
            editingVehicle.setColor(color == null ? "" : color);
            editingVehicle.setMileage(odometer == null ? 0 : odometer);
            editingVehicle.setDailyRate(dailyRate);
            editingVehicle.setStatus(status);

            boolean ok = VehicleDAO.updateVehicle(editingVehicle);
            if (ok) {
                showInfo("Updated", "Vehicle updated.");
                reloadVehicles();
                showListPane();
            } else {
                showError("Update Error", "Failed to update vehicle.");
            }
        }
    }

    private void handleDeleteFromForm() {
        if (editingVehicle == null) return;
        if (!confirm("Delete Vehicle", "Delete vehicle " + safe(editingVehicle.getLicensePlate()) + "?")) return;
        boolean ok = VehicleDAO.deleteVehicle(editingVehicle.getId());
        if (ok) {
            showInfo("Deleted", "Vehicle removed.");
            reloadVehicles();
            showListPane();
        } else {
            showError("Delete Error", "Failed to delete vehicle.");
        }
    }

    // ------------------- Pane switching -------------------

    private void showListPane() {
        vehicleListPane.setVisible(true);
        vehicleListPane.setManaged(true);
        vehicleFormPane.setVisible(false);
        vehicleFormPane.setManaged(false);
        editingVehicle = null;
        Platform.runLater(() -> searchField.requestFocus());
    }

    private void showFormPane() {
        vehicleListPane.setVisible(false);
        vehicleListPane.setManaged(false);
        vehicleFormPane.setVisible(true);
        vehicleFormPane.setManaged(true);
    }

    // ------------------- Keyboard handling -------------------

    private void handleKeyboardShortcuts(KeyEvent event) {
        KeyCode code = event.getCode();
        if (code == KeyCode.ESCAPE) {
            if (vehicleFormPane.isVisible()) showListPane();
            else openModuleCandidates(new String[]{"Main.fxml", "Availability.fxml"}, "Dashboard");
        } else if (code == KeyCode.F1) openModule("Availability.fxml", "Availability");
        else if (code == KeyCode.F2) openModule("Customer.fxml", "Customer");
        else if (code == KeyCode.F3) openModule("Rental.fxml", "Rental");
        else if (code == KeyCode.F4) openModule("Return.fxml", "Return");
        else if (code == KeyCode.F5) openModuleCandidates(new String[]{"Vehicle.fxml", "Vehical.fxml"}, "Vehicles");
        else if (code == KeyCode.F6) reloadVehicles();
        else if (event.isControlDown() && code == KeyCode.R) resetFilters();
        else if (event.isAltDown() && code == KeyCode.N) openAdd();
        else if (code == KeyCode.ENTER) {
            if (vehicleFormPane.isVisible()) handleSave();
            else applyFilters();
        }
    }

    // ------------------- Navigation helpers -------------------

    private void openModule(String fxml, String title) {
        openModuleCandidates(new String[]{fxml}, title);
    }

    private void openModuleCandidates(String[] candidates, String title) {
        try {
            String base = "/com/mycompany/rentcar/";
            URL found = null;
            for (String name : candidates) {
                URL url = getClass().getResource(base + name);
                if (url != null) { found = url; break; }
            }
            if (found == null) {
                showError("Navigation Error", "FXML not found. Tried: " + Arrays.toString(candidates));
                return;
            }
            FXMLLoader loader = new FXMLLoader(found);
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.setMaximized(true);
            stage.show();

            Stage current = (Stage) rootPane.getScene().getWindow();
            if (current != null) current.close();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Navigation Error", "Cannot open " + title + " window.\n" + e.getMessage());
        }
    }

    // ------------------- Utils -------------------

    private String safe(String s) { return s == null ? "" : s; }

    private Integer parseIntOrNull(String s) {
        try {
            if (s == null) return null;
            String t = s.trim();
            if (t.isEmpty()) return null;
            return Integer.parseInt(t);
        } catch (Exception ignored) { return null; }
    }

    private Double parseDoubleOrNull(String s) {
        try {
            if (s == null) return null;
            String t = s.trim().replace(",", "");
            if (t.isEmpty()) return null;
            return Double.parseDouble(t);
        } catch (Exception ignored) { return null; }
    }

    private void showInfo(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }

    private void showWarn(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }

    private void showError(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }

    private boolean confirm(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.YES, ButtonType.NO);
        a.setHeaderText(title);
        Optional<ButtonType> res = a.showAndWait();
        return res.isPresent() && res.get() == ButtonType.YES;
    }
}