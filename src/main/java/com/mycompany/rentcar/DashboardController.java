package com.mycompany.rentcar;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Arrays;
import java.util.Locale;

import com.mycompany.rentcar.util.DBConnection;

public class DashboardController {

    @FXML private Button reportBtn; // bottom "Report" button
    @FXML private Button availabilityBtn;
    @FXML private Button customerBtn;
    @FXML private Button rentalBtn;
    @FXML private Button returnBtn;
    @FXML private Button vehiclesBtn;
    @FXML private Button exitBtn;

    @FXML private Label totalCarsLabel;
    @FXML private Label totalBookingLabel;
    @FXML private Label totalPaymentLabel;

    @FXML private PieChart pieChart;
    @FXML private LineChart<String, Number> lineChart;
    @FXML private AreaChart<String, Number> areaChart;

    @FXML private AnchorPane rootPane;

    @FXML
    public void initialize() {
        loadDashboardData();
        loadPieChart();
        loadLineChart();
        loadAreaChart();
        setupShortcuts();
        setupTopNavKeyTraversal();

        if (reportBtn != null) {
            reportBtn.setOnAction(this::handleReport);
        }
    }

    @FXML
    private void handleReport(ActionEvent event) {
        openWindow("Report.fxml", "Reports");
    }

    private void loadDashboardData() {
        try (Connection conn = DBConnection.getConnection()) {
            ResultSet rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM vehicles");
            if (rs.next()) totalCarsLabel.setText(rs.getString(1));

            rs = conn.createStatement().executeQuery("SELECT COUNT(*) FROM bookings");
            if (rs.next()) totalBookingLabel.setText(rs.getString(1));

            rs = conn.createStatement().executeQuery("SELECT COALESCE(SUM(amount_cents)/100,0) FROM payments WHERE status='CAPTURED'");
            if (rs.next()) totalPaymentLabel.setText("Rs. " + rs.getString(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadPieChart() {
        pieChart.getData().clear();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT status, COUNT(*) FROM vehicles GROUP BY status";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pieChart.getData().add(new PieChart.Data(rs.getString(1), rs.getInt(2)));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLineChart() {
        lineChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Bookings per Month");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT MONTH(FROM_UNIXTIME(pickup_ts)) AS m, COUNT(*) " +
                         "FROM bookings GROUP BY MONTH(FROM_UNIXTIME(pickup_ts))";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int monthNum = rs.getInt(1);
                series.getData().add(new XYChart.Data<>(
                        Month.of(monthNum).getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                        rs.getInt(2)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        lineChart.getData().add(series);
    }

    private void loadAreaChart() {
        areaChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Payments per Month");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT MONTH(FROM_UNIXTIME(paid_at)) AS m, SUM(amount_cents)/100 " +
                         "FROM payments WHERE status='CAPTURED' GROUP BY MONTH(FROM_UNIXTIME(paid_at))";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int monthNum = rs.getInt(1);
                series.getData().add(new XYChart.Data<>(
                        Month.of(monthNum).getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                        rs.getDouble(2)
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        areaChart.getData().add(series);
    }

    private void setupShortcuts() {
        rootPane.setFocusTraversable(true);

        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                handleExit(null);
            } else if (event.getCode() == KeyCode.F1) {
                openWindow("Availability.fxml", "Availability");
            } else if (event.getCode() == KeyCode.F2) {
                openWindow("Customer.fxml", "Customer");
            } else if (event.getCode() == KeyCode.F3) {
                openWindow("Rental.fxml", "Rental");
            } else if (event.getCode() == KeyCode.F4) {
                openWindow("Return.fxml", "Return");
            } else if (event.getCode() == KeyCode.F5) {
                openWindowCandidates(new String[]{"Vehicle.fxml", "Vehical.fxml"}, "Vehicles");
            } else if (event.getCode() == KeyCode.F7) {
                openWindow("Report.fxml", "Reports");
            }
        });
    }

    private void setupTopNavKeyTraversal() {
        Button[] navButtons = { availabilityBtn, customerBtn, rentalBtn, returnBtn, vehiclesBtn };
        for (int i = 0; i < navButtons.length; i++) {
            final int index = i;
            navButtons[i].setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.RIGHT) {
                    int next = (index + 1) % navButtons.length;
                    navButtons[next].requestFocus();
                } else if (event.getCode() == KeyCode.LEFT) {
                    int prev = (index - 1 + navButtons.length) % navButtons.length;
                    navButtons[prev].requestFocus();
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.SPACE) {
                    navButtons[index].fire();
                }
            });
        }
    }

    private void openWindow(String fxmlFile, String title) {
        openWindowCandidates(new String[]{fxmlFile}, title);
    }

    private void openWindowCandidates(String[] candidates, String title) {
        try {
            String base = "/com/mycompany/rentcar/";
            URL found = null;
            for (String f : candidates) {
                URL url = getClass().getResource(base + f);
                if (url != null) { found = url; break; }
            }
            if (found == null) {
                showAlert(title + " Failed",
                        "FXML not found.\nTried: " + Arrays.toString(candidates) +
                        "\nMake sure the file exists under src/main/resources" + base);
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
            if (current != null) current.hide();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(title + " Failed", "Cannot open " + title + " window.\n" + e.getMessage());
        }
    }

    @FXML private void handleAvailabilityBtn(ActionEvent event) { openWindow("Availability.fxml", "Availability"); }
    @FXML private void handleCustomerBtn(ActionEvent event)     { openWindow("Customer.fxml", "Customer"); }
    @FXML private void handleRentalBtn(ActionEvent event)       { openWindow("Rental.fxml", "Rental"); }
    @FXML private void handleReturnBtn(ActionEvent event)       { openWindow("Return.fxml", "Return"); }
    @FXML private void handleVehiclesBtn(ActionEvent event)     { openWindowCandidates(new String[]{"Vehicle.fxml","Vehical.fxml"}, "Vehicles"); }
    @FXML private void handleExit(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/rentcar/Login.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.setResizable(false);
            stage.show();

            Stage currentStage = (Stage) rootPane.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Login Failed", "Cannot return to Login window.");
        }
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}