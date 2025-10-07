package com.mycompany.rentcar;

import com.mycompany.rentcar.util.DBConnection;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ReportController {

    @FXML private AnchorPane rootPane;
    @FXML private Button availabilityBtn, customerBtn, rentalBtn, returnBtn, vehiclesBtn;
    @FXML private ComboBox<String> reportTypeCombo;
    @FXML private DatePicker fromDatePicker, toDatePicker;
    @FXML private ComboBox<String> statusFilterCombo;
    @FXML private Button generateBtn, clearBtn;
    @FXML private Label totalRecordsLabel, totalRevenueLabel, activeBookingsLabel, totalCustomersLabel;
    @FXML private TableView<Row> reportTable;
    @FXML private TableColumn<Row, Integer> colId;
    @FXML private TableColumn<Row, String> colDate, colReference, colCustomer, colVehicle, colStatus, colAmount, colDetails;
    @FXML private TextArea reportNotesArea;
    @FXML private Button exportPdfBtn, exportExcelBtn, printBtn, refreshBtn, exitBtn;

    private final ObservableList<Row> rows = FXCollections.observableArrayList();
    private final NumberFormat lkr = NumberFormat.getNumberInstance(new Locale("en","LK"));

    @FXML
    public void initialize() {
        lkr.setMinimumFractionDigits(2);
        lkr.setMaximumFractionDigits(2);

        setupParams();
        setupTable();
        setupActions();
        setupNav();
        setupShortcuts();

        toDatePicker.setValue(LocalDate.now());
        fromDatePicker.setValue(LocalDate.now().minusDays(30));
        reportTypeCombo.setValue("Revenue Summary");
        statusFilterCombo.setValue("All");

        generateReport();
    }

    private void setupParams() {
        // Extended report types
        reportTypeCombo.getItems().setAll(
            "Revenue Summary",
            "Payments",
            "Bookings", 
            "Invoices",
            "Customer Activity",
            "Vehicle Utilization",
            "Maintenance History",
            "Overdue Returns",
            "Top Customers",
            "Vehicle Performance",
            "User Activity",
            "Daily Revenue",
            "Monthly Revenue",
            "Cancellation Report"
        );
        statusFilterCombo.getItems().setAll("All", "RESERVED", "RENTED", "CANCELLED", "COMPLETED", "NO_SHOW");
    }

    private void setupTable() {
        colId.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().id()).asObject());
        colDate.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().date()));
        colReference.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().reference()));
        colCustomer.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().customer()));
        colVehicle.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().vehicle()));
        colStatus.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().status()));
        colAmount.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().amount()));
        colDetails.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().details()));
        reportTable.setItems(rows);
    }

    private void setupActions() {
        generateBtn.setOnAction(e -> generateReport());
        clearBtn.setOnAction(e -> clearFilters());
        refreshBtn.setOnAction(e -> generateReport());

        exportPdfBtn.setOnAction(e -> exportToPdf());
        printBtn.setOnAction(e -> exportToPdf());
        exportExcelBtn.setOnAction(e -> exportCsv());

        exitBtn.setOnAction(e -> openModuleCandidates(new String[]{"Main.fxml","Availability.fxml"}, "Dashboard"));

        fromDatePicker.setOnAction(e -> generateReport());
        toDatePicker.setOnAction(e -> generateReport());
        reportTypeCombo.setOnAction(e -> generateReport());
        statusFilterCombo.setOnAction(e -> generateReport());
    }

    private void setupNav() {
        availabilityBtn.setOnAction(e -> openModule("Availability.fxml","Availability"));
        customerBtn.setOnAction(e -> openModule("Customer.fxml","Customer"));
        rentalBtn.setOnAction(e -> openModule("Rental.fxml","Rental"));
        returnBtn.setOnAction(e -> openModule("Return.fxml","Return"));
        vehiclesBtn.setOnAction(e -> openModuleCandidates(new String[]{"Vehicle.fxml","Vehical.fxml"},"Vehicles"));
    }

    private void setupShortcuts() {
        Platform.runLater(() -> {
            if (rootPane == null) return;
            Scene scene = rootPane.getScene();
            if (scene == null) {
                rootPane.sceneProperty().addListener((o, ov, nv) -> { if (nv != null) addAccelerators(nv); });
            } else {
                addAccelerators(scene);
            }
            rootPane.setFocusTraversable(true);
            rootPane.setOnKeyPressed(this::handleKeys);
        });
    }

    private void addAccelerators(Scene scene) {
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F1), () -> openModule("Availability.fxml","Availability"));
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F2), () -> openModule("Customer.fxml","Customer"));
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F3), () -> openModule("Rental.fxml","Rental"));
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F4), () -> openModule("Return.fxml","Return"));
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F5), () -> openModuleCandidates(new String[]{"Vehicle.fxml","Vehical.fxml"},"Vehicles"));
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.F7), this::generateReport);
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN), this::clearFilters);
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN), this::exportToPdf);
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN), this::exportCsv);
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.ESCAPE), () -> openModuleCandidates(new String[]{"Main.fxml","Availability.fxml"},"Dashboard"));
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), this::generateReport);
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.SPACE), this::generateReport);
    }

    private void handleKeys(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER || e.getCode() == KeyCode.SPACE) generateReport();
    }

    private void generateReport() {
        rows.clear();
        String type = reportTypeCombo.getValue() == null ? "Revenue Summary" : reportTypeCombo.getValue();
        String status = statusFilterCombo.getValue();
        LocalDate from = fromDatePicker.getValue();
        LocalDate to = toDatePicker.getValue();

        switch (type) {
            case "Revenue Summary":
                loadRevenueSummaryReport(from, to);
                break;
            case "Payments":
                loadPaymentsReport(from, to, status);
                break;
            case "Bookings":
                loadBookingsReport(from, to, status);
                break;
            case "Invoices":
                loadInvoicesReport(from, to, status);
                break;
            case "Customer Activity":
                loadCustomerActivityReport(from, to);
                break;
            case "Vehicle Utilization":
                loadVehicleUtilizationReport(from, to);
                break;
            case "Maintenance History":
                loadMaintenanceReport(from, to);
                break;
            case "Overdue Returns":
                loadOverdueReturnsReport();
                break;
            case "Top Customers":
                loadTopCustomersReport(from, to);
                break;
            case "Vehicle Performance":
                loadVehiclePerformanceReport(from, to);
                break;
            case "User Activity":
                loadUserActivityReport(from, to);
                break;
            case "Daily Revenue":
                loadDailyRevenueReport(from, to);
                break;
            case "Monthly Revenue":
                loadMonthlyRevenueReport(from, to);
                break;
            case "Cancellation Report":
                loadCancellationReport(from, to);
                break;
            default:
                loadRevenueSummaryReport(from, to);
        }

        updateStatistics();
        updateReportNotes(type, from, to, status);
    }

    private void loadRevenueSummaryReport(LocalDate from, LocalDate to) {
        String sql = """
            SELECT 
                DATE(FROM_UNIXTIME(p.paid_at)) as report_date,
                COUNT(DISTINCT b.id) as booking_count,
                COUNT(DISTINCT b.customer_id) as customer_count,
                SUM(p.amount_cents) as total_revenue,
                p.category,
                p.method
            FROM payments p
            JOIN bookings b ON p.booking_id = b.id
            WHERE p.status = 'CAPTURED' 
                AND p.paid_at BETWEEN ? AND ?
            GROUP BY DATE(FROM_UNIXTIME(p.paid_at)), p.category, p.method
            ORDER BY report_date DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, atStart(from));
            ps.setLong(2, atEnd(to));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Row(
                        0,
                        rs.getString("report_date"),
                        String.valueOf(rs.getInt("booking_count")),
                        "Customers: " + rs.getInt("customer_count"),
                        rs.getString("category"),
                        rs.getString("method"),
                        fmt(rs.getInt("total_revenue")),
                        "Revenue Summary"
                    ));
                }
            }
        } catch (Exception ex) {
            showError("Revenue Summary Report", ex.getMessage());
        }
    }

    private void loadCustomerActivityReport(LocalDate from, LocalDate to) {
        String sql = """
            SELECT 
                c.id,
                c.full_name,
                c.phone,
                c.email,
                COUNT(b.id) as total_bookings,
                SUM(CASE WHEN b.status = 'COMPLETED' THEN 1 ELSE 0 END) as completed_bookings,
                SUM(CASE WHEN b.status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelled_bookings,
                COALESCE(SUM(p.amount_cents), 0) as total_spent
            FROM customers c
            LEFT JOIN bookings b ON c.id = b.customer_id 
                AND b.created_at BETWEEN ? AND ?
            LEFT JOIN payments p ON b.id = p.booking_id 
                AND p.status = 'CAPTURED'
            GROUP BY c.id, c.full_name, c.phone, c.email
            HAVING total_bookings > 0
            ORDER BY total_spent DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, atStart(from));
            ps.setLong(2, atEnd(to));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Row(
                        rs.getInt("id"),
                        "-",
                        safe(rs.getString("phone")),
                        rs.getString("full_name"),
                        safe(rs.getString("email")),
                        "Bookings: " + rs.getInt("total_bookings"),
                        fmt(rs.getInt("total_spent")),
                        "Completed: " + rs.getInt("completed_bookings") + 
                        ", Cancelled: " + rs.getInt("cancelled_bookings")
                    ));
                }
            }
        } catch (Exception ex) {
            showError("Customer Activity Report", ex.getMessage());
        }
    }

    private void loadVehicleUtilizationReport(LocalDate from, LocalDate to) {
        String sql = """
            SELECT 
                v.id,
                v.plate_no,
                v.make,
                v.model,
                v.status as current_status,
                COUNT(b.id) as total_bookings,
                SUM(CASE WHEN b.status = 'COMPLETED' THEN 
                    DATEDIFF(FROM_UNIXTIME(b.return_ts), FROM_UNIXTIME(b.pickup_ts)) 
                    ELSE 0 END) as days_rented,
                COALESCE(SUM(p.amount_cents), 0) as revenue_generated,
                v.daily_rate_cents
            FROM vehicles v
            LEFT JOIN bookings b ON v.id = b.vehicle_id 
                AND b.pickup_ts BETWEEN ? AND ?
            LEFT JOIN payments p ON b.id = p.booking_id 
                AND p.status = 'CAPTURED' AND p.category = 'RENTAL'
            GROUP BY v.id
            ORDER BY revenue_generated DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, atStart(from));
            ps.setLong(2, atEnd(to));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    double utilizationRate = 0;
                    if (from != null && to != null) {
                        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(from, to) + 1;
                        utilizationRate = (rs.getDouble("days_rented") / totalDays) * 100;
                    }
                    
                    rows.add(new Row(
                        rs.getInt("id"),
                        rs.getString("plate_no"),
                        "Bookings: " + rs.getInt("total_bookings"),
                        rs.getString("make") + " " + rs.getString("model"),
                        "Days Rented: " + rs.getInt("days_rented"),
                        rs.getString("current_status"),
                        fmt(rs.getInt("revenue_generated")),
                        String.format("Utilization: %.1f%%", utilizationRate)
                    ));
                }
            }
        } catch (Exception ex) {
            showError("Vehicle Utilization Report", ex.getMessage());
        }
    }

    private void loadMaintenanceReport(LocalDate from, LocalDate to) {
        String sql = """
            SELECT 
                vm.id,
                DATE(FROM_UNIXTIME(vm.service_date)) as service_date,
                v.plate_no,
                v.make,
                v.model,
                vm.description,
                vm.cost_cents,
                vm.odometer,
                DATE(FROM_UNIXTIME(vm.next_due_date)) as next_due
            FROM vehicle_maintenance vm
            JOIN vehicles v ON vm.vehicle_id = v.id
            WHERE vm.service_date BETWEEN ? AND ?
            ORDER BY vm.service_date DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, atStart(from));
            ps.setLong(2, atEnd(to));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Row(
                        rs.getInt("id"),
                        rs.getString("service_date"),
                        rs.getString("plate_no"),
                        "-",
                        rs.getString("make") + " " + rs.getString("model"),
                        "MAINTENANCE",
                        fmt(rs.getInt("cost_cents")),
                        safe(rs.getString("description")) + " | Next: " + safe(rs.getString("next_due"))
                    ));
                }
            }
        } catch (Exception ex) {
            showError("Maintenance Report", ex.getMessage());
        }
    }

    private void loadOverdueReturnsReport() {
        String sql = """
            SELECT 
                b.id,
                DATE(FROM_UNIXTIME(b.return_ts)) as expected_return,
                c.full_name,
                c.phone,
                v.plate_no,
                v.make,
                v.model,
                b.status,
                DATEDIFF(NOW(), FROM_UNIXTIME(b.return_ts)) as days_overdue,
                b.late_fee_cents
            FROM bookings b
            JOIN customers c ON b.customer_id = c.id
            JOIN vehicles v ON b.vehicle_id = v.id
            WHERE b.status = 'RENTED' 
                AND b.return_ts < UNIX_TIMESTAMP(NOW())
            ORDER BY days_overdue DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                rows.add(new Row(
                    rs.getInt("id"),
                    rs.getString("expected_return"),
                    rs.getString("phone"),
                    rs.getString("full_name"),
                    rs.getString("plate_no") + " - " + rs.getString("make") + " " + rs.getString("model"),
                    "OVERDUE",
                    fmt(rs.getInt("late_fee_cents")),
                    "Days Overdue: " + rs.getInt("days_overdue")
                ));
            }
        } catch (Exception ex) {
            showError("Overdue Returns Report", ex.getMessage());
        }
    }

    private void loadTopCustomersReport(LocalDate from, LocalDate to) {
        String sql = """
            SELECT 
                c.id,
                c.full_name,
                c.phone,
                c.email,
                COUNT(DISTINCT b.id) as booking_count,
                SUM(p.amount_cents) as total_revenue,
                AVG(p.amount_cents) as avg_payment,
                MAX(b.created_at) as last_booking
            FROM customers c
            JOIN bookings b ON c.id = b.customer_id
            JOIN payments p ON b.id = p.booking_id
            WHERE p.status = 'CAPTURED' 
                AND p.paid_at BETWEEN ? AND ?
            GROUP BY c.id
            ORDER BY total_revenue DESC
            LIMIT 20
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, atStart(from));
            ps.setLong(2, atEnd(to));
            
            try (ResultSet rs = ps.executeQuery()) {
                int rank = 1;
                while (rs.next()) {
                    rows.add(new Row(
                        rank++,
                        dt(rs.getLong("last_booking")),
                        safe(rs.getString("phone")),
                        rs.getString("full_name"),
                        safe(rs.getString("email")),
                        "Bookings: " + rs.getInt("booking_count"),
                        fmt(rs.getInt("total_revenue")),
                        "Avg: Rs. " + fmt(rs.getInt("avg_payment"))
                    ));
                }
            }
        } catch (Exception ex) {
            showError("Top Customers Report", ex.getMessage());
        }
    }

    private void loadVehiclePerformanceReport(LocalDate from, LocalDate to) {
        String sql = """
            SELECT 
                v.id,
                v.plate_no,
                v.make,
                v.model,
                v.year,
                COUNT(b.id) as total_bookings,
                SUM(CASE WHEN b.status = 'COMPLETED' THEN 1 ELSE 0 END) as completed_bookings,
                SUM(CASE WHEN b.status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelled_bookings,
                COALESCE(SUM(p.amount_cents), 0) as total_revenue,
                COALESCE(SUM(vm.cost_cents), 0) as maintenance_cost,
                (COALESCE(SUM(p.amount_cents), 0) - COALESCE(SUM(vm.cost_cents), 0)) as net_revenue
            FROM vehicles v
            LEFT JOIN bookings b ON v.id = b.vehicle_id 
                AND b.created_at BETWEEN ? AND ?
            LEFT JOIN payments p ON b.id = p.booking_id 
                AND p.status = 'CAPTURED'
            LEFT JOIN vehicle_maintenance vm ON v.id = vm.vehicle_id 
                AND vm.service_date BETWEEN ? AND ?
            GROUP BY v.id
            ORDER BY net_revenue DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, atStart(from));
            ps.setLong(2, atEnd(to));
            ps.setLong(3, atStart(from));
            ps.setLong(4, atEnd(to));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Row(
                        rs.getInt("id"),
                        rs.getString("plate_no"),
                        "Total: " + rs.getInt("total_bookings"),
                        rs.getString("make") + " " + rs.getString("model") + " (" + rs.getString("year") + ")",
                        "Revenue: " + fmt(rs.getInt("total_revenue")),
                        "Maint: " + fmt(rs.getInt("maintenance_cost")),
                        fmt(rs.getInt("net_revenue")),
                        "Net Revenue"
                    ));
                }
            }
        } catch (Exception ex) {
            showError("Vehicle Performance Report", ex.getMessage());
        }
    }

    private void loadUserActivityReport(LocalDate from, LocalDate to) {
        String sql = """
            SELECT 
                u.id,
                u.full_name,
                u.username,
                u.role,
                COUNT(DISTINCT al.id) as actions_count,
                COUNT(DISTINCT b.id) as bookings_created,
                COUNT(DISTINCT p.id) as payments_processed,
                DATE(FROM_UNIXTIME(u.last_login_at)) as last_login
            FROM users u
            LEFT JOIN audit_log al ON u.id = al.user_id 
                AND al.created_at BETWEEN ? AND ?
            LEFT JOIN bookings b ON u.id = b.created_by 
                AND b.created_at BETWEEN ? AND ?
            LEFT JOIN payments p ON u.id = p.created_by 
                AND p.paid_at BETWEEN ? AND ?
            WHERE u.is_active = 1
            GROUP BY u.id
            ORDER BY actions_count DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, atStart(from));
            ps.setLong(2, atEnd(to));
            ps.setLong(3, atStart(from));
            ps.setLong(4, atEnd(to));
            ps.setLong(5, atStart(from));
            ps.setLong(6, atEnd(to));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Row(
                        rs.getInt("id"),
                        safe(rs.getString("last_login")),
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getString("role"),
                        "Actions: " + rs.getInt("actions_count"),
                        "Bookings: " + rs.getInt("bookings_created"),
                        "Payments: " + rs.getInt("payments_processed")
                    ));
                }
            }
        } catch (Exception ex) {
            showError("User Activity Report", ex.getMessage());
        }
    }

    private void loadDailyRevenueReport(LocalDate from, LocalDate to) {
        String sql = """
            SELECT 
                DATE(FROM_UNIXTIME(p.paid_at)) as revenue_date,
                COUNT(DISTINCT p.id) as payment_count,
                COUNT(DISTINCT b.id) as booking_count,
                SUM(CASE WHEN p.category = 'DEPOSIT' THEN p.amount_cents ELSE 0 END) as deposit_total,
                SUM(CASE WHEN p.category = 'RENTAL' THEN p.amount_cents ELSE 0 END) as rental_total,
                SUM(CASE WHEN p.category = 'LATE_FEE' THEN p.amount_cents ELSE 0 END) as late_fee_total,
                SUM(p.amount_cents) as daily_total
            FROM payments p
            JOIN bookings b ON p.booking_id = b.id
            WHERE p.status = 'CAPTURED' 
                AND p.paid_at BETWEEN ? AND ?
            GROUP BY DATE(FROM_UNIXTIME(p.paid_at))
            ORDER BY revenue_date DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, atStart(from));
            ps.setLong(2, atEnd(to));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Row(
                        0,
                        rs.getString("revenue_date"),
                        "Payments: " + rs.getInt("payment_count"),
                        "Bookings: " + rs.getInt("booking_count"),
                        "Deposit: " + fmt(rs.getInt("deposit_total")),
                        "Rental: " + fmt(rs.getInt("rental_total")),
                        fmt(rs.getInt("daily_total")),
                        "Late Fees: " + fmt(rs.getInt("late_fee_total"))
                    ));
                }
            }
        } catch (Exception ex) {
            showError("Daily Revenue Report", ex.getMessage());
        }
    }

    private void loadMonthlyRevenueReport(LocalDate from, LocalDate to) {
        String sql = """
            SELECT 
                DATE_FORMAT(FROM_UNIXTIME(p.paid_at), '%Y-%m') as month,
                COUNT(DISTINCT b.customer_id) as unique_customers,
                COUNT(DISTINCT b.id) as total_bookings,
                COUNT(DISTINCT p.id) as total_payments,
                SUM(p.amount_cents) as monthly_revenue,
                AVG(p.amount_cents) as avg_payment
            FROM payments p
            JOIN bookings b ON p.booking_id = b.id
            WHERE p.status = 'CAPTURED' 
                AND p.paid_at BETWEEN ? AND ?
            GROUP BY DATE_FORMAT(FROM_UNIXTIME(p.paid_at), '%Y-%m')
            ORDER BY month DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, atStart(from));
            ps.setLong(2, atEnd(to));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Row(
                        0,
                        rs.getString("month"),
                        "Customers: " + rs.getInt("unique_customers"),
                        "Bookings: " + rs.getInt("total_bookings"),
                        "Payments: " + rs.getInt("total_payments"),
                        "Avg: " + fmt(rs.getInt("avg_payment")),
                        fmt(rs.getInt("monthly_revenue")),
                        "Monthly Total"
                    ));
                }
            }
        } catch (Exception ex) {
            showError("Monthly Revenue Report", ex.getMessage());
        }
    }

    private void loadCancellationReport(LocalDate from, LocalDate to) {
        String sql = """
            SELECT 
                b.id,
                DATE(FROM_UNIXTIME(b.updated_at)) as cancel_date,
                c.full_name,
                c.phone,
                v.plate_no,
                v.make,
                v.model,
                b.rate_cents,
                b.notes,
                DATEDIFF(FROM_UNIXTIME(b.return_ts), FROM_UNIXTIME(b.pickup_ts)) as planned_days
            FROM bookings b
            JOIN customers c ON b.customer_id = c.id
            JOIN vehicles v ON b.vehicle_id = v.id
            WHERE b.status = 'CANCELLED' 
                AND b.updated_at BETWEEN ? AND ?
            ORDER BY b.updated_at DESC
            """;
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, atStart(from));
            ps.setLong(2, atEnd(to));
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Row(
                        rs.getInt("id"),
                        rs.getString("cancel_date"),
                        safe(rs.getString("phone")),
                        rs.getString("full_name"),
                        rs.getString("plate_no") + " - " + rs.getString("make") + " " + rs.getString("model"),
                        "CANCELLED",
                        fmt(rs.getInt("rate_cents") * rs.getInt("planned_days")),
                        "Lost Revenue (" + rs.getInt("planned_days") + " days)"
                    ));
                }
            }
        } catch (Exception ex) {
            showError("Cancellation Report", ex.getMessage());
        }
    }

    private void loadPaymentsReport(LocalDate from, LocalDate to, String status) {
        String sql = """
            SELECT p.id, p.paid_at, p.reference, p.amount_cents, p.category, p.method,
                   b.status, c.full_name, v.make, v.model, v.plate_no
            FROM payments p
            JOIN bookings b ON p.booking_id = b.id
            JOIN customers c ON b.customer_id = c.id
            JOIN vehicles  v ON b.vehicle_id  = v.id
            WHERE p.status='CAPTURED' AND p.paid_at BETWEEN ? AND ?
            """ + (status != null && !"All".equalsIgnoreCase(status) ? " AND b.status=? " : "") +
            " ORDER BY p.paid_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int i=1;
            ps.setLong(i++, atStart(from));
            ps.setLong(i++, atEnd(to));
            if (status != null && !"All".equalsIgnoreCase(status)) ps.setString(i++, status);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rows.add(new Row(
                            rs.getInt("id"),
                            dt(rs.getLong("paid_at")),
                            safe(rs.getString("reference")),
                            rs.getString("full_name"),
                            rs.getString("make") + " " + rs.getString("model") + " [" + rs.getString("plate_no") + "]",
                            rs.getString("status"),
                            fmt(rs.getInt("amount_cents")),
                            rs.getString("category") + " / " + rs.getString("method")
                    ));
                }
            }
        } catch (Exception ex) {
            showError("Payments Report", ex.getMessage());
        }
    }

    private void loadBookingsReport(LocalDate from, LocalDate to, String status) {
        String sql = """
            SELECT b.id, b.pickup_ts, b.return_ts, b.status, b.rate_cents, 
                   c.full_name, v.make, v.model, v.plate_no,
                   DATEDIFF(FROM_UNIXTIME(b.return_ts), FROM_UNIXTIME(b.pickup_ts)) as days
            FROM bookings b
            JOIN customers c ON b.customer_id = c.id
            JOIN vehicles  v ON b.vehicle_id  = v.id
            WHERE b.pickup_ts BETWEEN ? AND ?
            """ + (status != null && !"All".equalsIgnoreCase(status) ? " AND b.status=? " : "") +
            " ORDER BY b.id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int i=1;
            ps.setLong(i++, atStart(from));
            ps.setLong(i++, atEnd(to));
            if (status != null && !"All".equalsIgnoreCase(status)) ps.setString(i++, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int days = rs.getInt("days");
                    int rateCents = rs.getInt("rate_cents");
                    rows.add(new Row(
                            rs.getInt("id"),
                            dt(rs.getLong("pickup_ts")),
                            dt(rs.getLong("return_ts")),
                            rs.getString("full_name"),
                            rs.getString("make") + " " + rs.getString("model") + " [" + rs.getString("plate_no") + "]",
                            rs.getString("status"),
                            fmt(rateCents * days),
                            days + " days @ " + fmt(rateCents) + "/day"
                    ));
                }
            }
        } catch (Exception ex) {
            showError("Bookings Report", ex.getMessage());
        }
    }

    private void loadInvoicesReport(LocalDate from, LocalDate to, String statusIgnored) {
        String sql = """
            SELECT i.id, i.issued_at, i.invoice_no, i.total_cents, 
                   i.balance_cents, c.full_name, v.make, v.model, v.plate_no
            FROM invoices i
            JOIN bookings b ON i.booking_id = b.id
            JOIN customers c ON b.customer_id = c.id
            JOIN vehicles  v ON b.vehicle_id  = v.id
            WHERE i.issued_at BETWEEN ? AND ?
            ORDER BY i.id DESC
            """;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, atStart(from));
            ps.setLong(2, atEnd(to));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String status = rs.getInt("balance_cents") > 0 ? "PENDING" : "PAID";
                    rows.add(new Row(
                            rs.getInt("id"),
                            dt(rs.getLong("issued_at")),
                            rs.getString("invoice_no"),
                            rs.getString("full_name"),
                            rs.getString("make") + " " + rs.getString("model") + " [" + rs.getString("plate_no") + "]",
                            status,
                            fmt(rs.getInt("total_cents")),
                            "Balance: " + fmt(rs.getInt("balance_cents"))
                    ));
                }
            }
        } catch (Exception ex) {
            showError("Invoices Report", ex.getMessage());
        }
    }

    private void updateStatistics() {
        totalRecordsLabel.setText(String.valueOf(rows.size()));
        totalRevenueLabel.setText("Rs. " + sumAmount(rows));
        activeBookingsLabel.setText(String.valueOf(countActiveBookings()));
        totalCustomersLabel.setText(String.valueOf(countCustomers()));
    }

    private void updateReportNotes(String type, LocalDate from, LocalDate to, String status) {
        String notes = "Report Type: " + type + "\n" +
                      "Period: " + from + " to " + to + "\n" +
                      (status == null || "All".equalsIgnoreCase(status) ? "Status: All" : "Status: " + status) + "\n" +
                      "Generated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "\n" +
                      "Total Records: " + rows.size();
        reportNotesArea.setText(notes);
    }

    private void exportToPdf() {
        if (rows.isEmpty()) {
            showWarn("Export PDF", "No data to export. Please generate a report first.");
            return;
        }

        try {
            String html = generateReportHtml();
            showPdfPreview(html);
        } catch (Exception ex) {
            showError("Export PDF", "Failed to generate report: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private String generateReportHtml() throws IOException {
        // Try to load template
        InputStream is = getClass().getResourceAsStream("/com/mycompany/rentcar/templates/report_template.html");
        if (is == null) {
            return generateEmbeddedTemplate();
        }
        
        String template = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        is.close();

        // Replace placeholders
        String type = reportTypeCombo.getValue();
        LocalDate from = fromDatePicker.getValue();
        LocalDate to = toDatePicker.getValue();
        String status = statusFilterCombo.getValue();

        template = template.replace("{{reportType}}", type);
        template = template.replace("{{fromDate}}", from.toString());
        template = template.replace("{{toDate}}", to.toString());
        template = template.replace("{{generatedDate}}", LocalDate.now().toString());
        template = template.replace("{{reportId}}", "RPT" + System.currentTimeMillis());
        template = template.replace("{{generatedDateTime}}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        template = template.replace("{{notes}}", reportNotesArea.getText());

        // Build table rows
        StringBuilder tableRows = new StringBuilder();
        for (Row row : rows) {
            tableRows.append("<tr>\n");
            tableRows.append("  <td>").append(escapeHtml(row.date())).append("</td>\n");
            tableRows.append("  <td>").append(escapeHtml(row.reference())).append("</td>\n");
            tableRows.append("  <td>").append(escapeHtml(row.customer())).append("</td>\n");
            tableRows.append("  <td>").append(escapeHtml(row.vehicle())).append("</td>\n");
            tableRows.append("  <td class=\"text-center\"><span class=\"status-badge\">").append(escapeHtml(row.status())).append("</span></td>\n");
            tableRows.append("  <td class=\"text-right font-bold\">Rs. ").append(escapeHtml(row.amount())).append("</td>\n");
            tableRows.append("</tr>\n");
        }

        // Calculate totals
        String totalRev = sumAmount(rows);
        template = template.replace("{{subtotal}}", totalRev);
        template = template.replace("{{taxPercent}}", "0");
        template = template.replace("{{tax}}", "0.00");
        template = template.replace("{{grandTotal}}", totalRev);

        // Replace table data
        int insertPos = template.indexOf("</tbody>");
        if (insertPos > 0) {
            template = template.substring(0, insertPos) + tableRows.toString() + template.substring(insertPos);
        }

        return template;
    }

    private String generateEmbeddedTemplate() {
        StringBuilder html = new StringBuilder();
        html.append("""
            <!DOCTYPE html>
            <html><head><meta charset="UTF-8"><title>Report</title>
            <style>
              @page { size: A4; margin: 12mm; }
              body { font-family: "Segoe UI", Arial, sans-serif; margin: 0; padding: 20px; color: #333; }
              .header { text-align: center; margin-bottom: 30px; border-bottom: 3px solid #2196F3; padding-bottom: 15px; }
              .brand { font-size: 28px; font-weight: 700; color: #1976D2; margin-bottom: 5px; }
              .subtitle { font-size: 18px; color: #666; }
              .meta-info { background: #f5f5f5; padding: 15px; border-radius: 5px; margin-bottom: 20px; }
              .meta-info p { margin: 5px 0; }
              table { width: 100%; border-collapse: collapse; margin-top: 20px; box-shadow: 0 2px 5px rgba(0,0,0,0.1); }
              th { background: #2196F3; color: white; padding: 12px; text-align: left; font-weight: 600; }
              td { padding: 10px; text-align: left; border-bottom: 1px solid #e0e0e0; }
              tr:nth-child(even) { background: #f9f9f9; }
              tr:hover { background: #f0f0f0; }
              .totals { margin-top: 30px; text-align: right; padding: 20px; background: #e8f5e9; border-radius: 5px; }
              .totals h3 { color: #4CAF50; font-size: 24px; margin: 0; }
              .footer { margin-top: 30px; text-align: center; color: #999; font-size: 12px; }
            </style></head><body>
            <div class="header">
              <div class="brand">Car Rental System</div>
              <div class="subtitle">""");
        
        html.append(reportTypeCombo.getValue()).append(" Report</div></div>");
        
        html.append("<div class=\"meta-info\">");
        html.append("<p><strong>Report Type:</strong> ").append(reportTypeCombo.getValue()).append("</p>");
        html.append("<p><strong>Period:</strong> ").append(fromDatePicker.getValue()).append(" to ").append(toDatePicker.getValue()).append("</p>");
        html.append("<p><strong>Generated:</strong> ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("</p>");
        html.append("<p><strong>Total Records:</strong> ").append(rows.size()).append("</p>");
        html.append("</div>");
        
        html.append("<table><thead><tr>");
        html.append("<th>Date</th><th>Reference</th><th>Customer</th><th>Vehicle</th><th>Status</th><th>Amount</th><th>Details</th>");
        html.append("</tr></thead><tbody>");
        
        for (Row r : rows) {
            html.append("<tr>");
            html.append("<td>").append(escapeHtml(r.date())).append("</td>");
            html.append("<td>").append(escapeHtml(r.reference())).append("</td>");
            html.append("<td>").append(escapeHtml(r.customer())).append("</td>");
            html.append("<td>").append(escapeHtml(r.vehicle())).append("</td>");
            html.append("<td>").append(escapeHtml(r.status())).append("</td>");
            html.append("<td>Rs. ").append(escapeHtml(r.amount())).append("</td>");
            html.append("<td>").append(escapeHtml(r.details())).append("</td>");
            html.append("</tr>");
        }
        
        html.append("</tbody></table>");
        html.append("<div class=\"totals\"><h3>Total Revenue: Rs. ").append(sumAmount(rows)).append("</h3></div>");
        html.append("<div class=\"footer\">Generated by Car Rental System &copy; 2024</div>");
        html.append("</body></html>");
        
        return html.toString();
    }

    private void showPdfPreview(String html) {
        Stage previewStage = new Stage();
        previewStage.initModality(Modality.APPLICATION_MODAL);
        previewStage.setTitle("Report Preview");

        WebView webView = new WebView();
        webView.getEngine().loadContent(html);

        Button saveBtn = new Button("Save HTML (Ctrl+S)");
        Button printBtn = new Button("Print (Ctrl+P)");
        Button closeBtn = new Button("Close (Esc)");

        saveBtn.setOnAction(e -> savePdf(html, previewStage));
        printBtn.setOnAction(e -> printWebView(webView));
        closeBtn.setOnAction(e -> previewStage.close());

        javafx.scene.layout.HBox buttonBar = new javafx.scene.layout.HBox(10, saveBtn, printBtn, closeBtn);
        buttonBar.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBar.setPadding(new javafx.geometry.Insets(10));

        javafx.scene.layout.VBox root = new javafx.scene.layout.VBox(webView, buttonBar);
        javafx.scene.layout.VBox.setVgrow(webView, javafx.scene.layout.Priority.ALWAYS);

        Scene scene = new Scene(root, 900, 700);
        
        scene.setOnKeyPressed(e -> {
            if (new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN).match(e)) {
                savePdf(html, previewStage);
            } else if (new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN).match(e)) {
                printWebView(webView);
            } else if (e.getCode() == KeyCode.ESCAPE) {
                previewStage.close();
            }
        });

        previewStage.setScene(scene);
        previewStage.show();
    }

    private void savePdf(String html, Stage stage) {
        FileChooser fc = new FileChooser();
        fc.setTitle("Save Report as HTML");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML Files", "*.html"));
        fc.setInitialFileName("report_" + reportTypeCombo.getValue().replace(" ", "_") + "_" + System.currentTimeMillis() + ".html");
        File file = fc.showSaveDialog(stage);
        
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                writer.write(html);
                showInfo("Export", "Report saved successfully!\n\nOpen in browser and use Print > Save as PDF to create PDF.");
            } catch (Exception ex) {
                showError("Export", "Failed to save: " + ex.getMessage());
            }
        }
    }

    private void printWebView(WebView webView) {
        javafx.print.PrinterJob job = javafx.print.PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(webView.getScene().getWindow())) {
            webView.getEngine().print(job);
            job.endJob();
        }
    }

    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }

    private String sumAmount(ObservableList<Row> data) {
        long cents = 0;
        for (Row r : data) {
            try {
                String s = r.amount().replace(",", "").trim();
                long c = Math.round(Double.parseDouble(s) * 100.0);
                cents += c;
            } catch (Exception ignored) {}
        }
        return lkr.format(cents / 100.0);
    }

    private int countActiveBookings() {
        String sql = "SELECT COUNT(*) FROM bookings WHERE status='RENTED'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception ignored) {}
        return 0;
    }

    private int countCustomers() {
        String sql = "SELECT COUNT(DISTINCT customer_id) FROM bookings WHERE created_at >= ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, atStart(fromDatePicker.getValue()));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception ignored) {}
        return 0;
    }

    private void exportCsv() {
        try {
            FileChooser fc = new FileChooser();
            fc.setTitle("Export Report CSV");
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            fc.setInitialFileName("report_" + reportTypeCombo.getValue().replace(" ", "_") + "_" + System.currentTimeMillis() + ".csv");
            File f = fc.showSaveDialog(rootPane.getScene().getWindow());
            if (f == null) return;

            try (BufferedWriter bw = new BufferedWriter(new FileWriter(f))) {
                bw.write("ID,Date,Reference,Customer,Vehicle,Status,Amount,Details\n");
                for (Row r : rows) {
                    bw.write(String.join(",",
                            s(r.id()), q(r.date()), q(r.reference()), q(r.customer()),
                            q(r.vehicle()), q(r.status()), q(r.amount()), q(r.details())));
                    bw.write("\n");
                }
            }
            showInfo("Export", "CSV exported successfully to:\n" + f.getAbsolutePath());
        } catch (Exception ex) {
            showError("Export", ex.getMessage());
        }
    }

    private void clearFilters() {
        reportTypeCombo.setValue("Revenue Summary");
        fromDatePicker.setValue(LocalDate.now().minusDays(30));
        toDatePicker.setValue(LocalDate.now());
        statusFilterCombo.setValue("All");
        rows.clear();
        generateReport();
    }

    // Helper methods
    private String fmt(int cents) {
        double v = cents / 100.0;
        synchronized (lkr) { return lkr.format(v); }
    }
    
    private long atStart(LocalDate d) {
        if (d == null) d = LocalDate.now();
        return d.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }
    
    private long atEnd(LocalDate d) {
        if (d == null) d = LocalDate.now();
        return d.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toEpochSecond() - 1;
    }
    
    private String dt(long epoch) {
        return Instant.ofEpochSecond(epoch).atZone(ZoneId.systemDefault()).toLocalDate().toString();
    }
    
    private String q(String s) { return "\"" + (s == null ? "" : s.replace("\"","\"\"")) + "\""; }
    private String s(int v) { return String.valueOf(v); }
    private String safe(String s) { return s == null ? "" : s; }

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
            showError("Navigation Error", e.getMessage());
        }
    }

    private void showInfo(String h, String m) { 
        Alert a = new Alert(Alert.AlertType.INFORMATION, m, ButtonType.OK); 
        a.setHeaderText(h); 
        a.showAndWait(); 
    }
    
    private void showWarn(String h, String m) { 
        Alert a = new Alert(Alert.AlertType.WARNING, m, ButtonType.OK); 
        a.setHeaderText(h); 
        a.showAndWait(); 
    }
    
    private void showError(String h, String m){ 
        Alert a = new Alert(Alert.AlertType.ERROR, m, ButtonType.OK); 
        a.setHeaderText(h); 
        a.showAndWait(); 
    }

    public static class Row {
        private final int id;
        private final String date, reference, customer, vehicle, status, amount, details;
        
        public Row(int id, String date, String reference, String customer, String vehicle, 
                   String status, String amount, String details) {
            this.id = id; 
            this.date = date; 
            this.reference = reference; 
            this.customer = customer; 
            this.vehicle = vehicle; 
            this.status = status; 
            this.amount = amount; 
            this.details = details;
        }
        
        public int id() { return id; }
        public String date() { return date; }
        public String reference() { return reference; }
        public String customer() { return customer; }
        public String vehicle() { return vehicle; }
        public String status() { return status; }
        public String amount() { return amount; }
        public String details() { return details; }
    }
}