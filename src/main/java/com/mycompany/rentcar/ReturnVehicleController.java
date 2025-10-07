package com.mycompany.rentcar;

import com.mycompany.rentcar.dao.BookingDAO;
import com.mycompany.rentcar.dao.CustomerDAO;
import com.mycompany.rentcar.dao.VehicleDAO;
import com.mycompany.rentcar.dao.PaymentDAO;
import com.mycompany.rentcar.model.Booking;
import com.mycompany.rentcar.model.Customer;
import com.mycompany.rentcar.model.Vehicle;
import com.mycompany.rentcar.util.DBConnection;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ReturnVehicleController implements Initializable {

    // Root
    @FXML private AnchorPane rootPane;

    // Search
    @FXML private TextField searchField;
    @FXML private Button searchBtn;
    @FXML private Button resetBtn;

    // Reservation & Customer Info
    @FXML private TextField reservationIdField;
    @FXML private TextField customerNameField;
    @FXML private TextField customerLicenseField;
    @FXML private TextField customerPhoneField;
    @FXML private TextField vehicleRegNoField;
    @FXML private TextField vehicleModelField;
    @FXML private TextField pickupDateField;
    @FXML private TextField dueDateField;
    @FXML private TextField statusField;

    // Return Details
    @FXML private DatePicker actualReturnDatePicker;
    @FXML private TextField plannedDaysField;
    @FXML private TextField actualDaysField;
    @FXML private TextField lateDaysField;
    @FXML private TextField lateFeeField;
    @FXML private TextField totalCostField;
    @FXML private TextField paidAmountField;
    @FXML private TextField balanceRefundField;
    @FXML private TextArea notesArea;

    // Actions
    @FXML private Button confirmBtn;
    @FXML private Button printBtn;
    @FXML private Button cancelBtn;

    // Nav
    @FXML private Button availabilityBtn;
    @FXML private Button customerBtn;
    @FXML private Button rentalBtn;
    @FXML private Button returnBtn;
    @FXML private Button vehiclesBtn;

    // DAOs
    private CustomerDAO customerDAO;
    private BookingDAO bookingDAO;
    private VehicleDAO vehicleDAO;
    private PaymentDAO paymentDAO;

    // State
    private Booking currentBooking;
    private Customer currentCustomer;
    private Vehicle currentVehicle;

    // Configs
    private static final String STATUS_COMPLETED = "COMPLETED";
    private static final double LATE_FEE_MULTIPLIER = 1.0;

    // LKR formatting
    private static final Locale LK_LOCALE = new Locale("en", "LK");
    private final NumberFormat lkrFmt = NumberFormat.getNumberInstance(LK_LOCALE);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            customerDAO = new CustomerDAO();
        } catch (SQLException e) {
            showError("DB Error", "Failed to initialize CustomerDAO", e);
        }
        bookingDAO = new BookingDAO();
        vehicleDAO = new VehicleDAO();
        paymentDAO = new PaymentDAO();

        lkrFmt.setMinimumFractionDigits(2);
        lkrFmt.setMaximumFractionDigits(2);

        clearForm();
        actualReturnDatePicker.setValue(LocalDate.now());
        actualReturnDatePicker.valueProperty().addListener((obs, oldV, newV) -> updateCalculatedFields());

        searchBtn.setDefaultButton(true);
        cancelBtn.setCancelButton(true);
        searchField.setOnAction(e -> handleSearch((ActionEvent) null));

        Platform.runLater(() -> {
            Scene scene = rootPane.getScene();
            if (scene != null) addAccelerators(scene);
            else rootPane.sceneProperty().addListener((o, ov, nv) -> { if (nv != null) addAccelerators(nv); });
        });

        setupShortcuts();
        Platform.runLater(() -> rootPane.requestFocus());
    }

    private void addAccelerators(Scene scene) {
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN), () -> handleConfirmReturn(null));
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN), () -> handlePrintReceipt(null));
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN), this::handleReset);
    }

    // ================== SHORTCUTS ==================
    private void setupShortcuts() {
        rootPane.setFocusTraversable(true);
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) handleExit(null);
            else if (event.getCode() == KeyCode.F1) openWindow("Availability.fxml", "Availability");
            else if (event.getCode() == KeyCode.F2) openWindow("Customer.fxml", "Customer");
            else if (event.getCode() == KeyCode.F3) openWindow("Rental.fxml", "Rental");
            else if (event.getCode() == KeyCode.F4) openWindow("Return.fxml", "Return");
            else if (event.getCode() == KeyCode.F5) openWindow("Vehical.fxml", "Vehicles");
            else if (event.getCode() == KeyCode.F6) handleRefresh();
            else if (event.isControlDown() && event.getCode() == KeyCode.R) handleReset();
            else if (event.isControlDown() && event.getCode() == KeyCode.B) handleBook();
            else if (event.getCode() == KeyCode.ENTER) handleSearch(); // no-arg overload
        });
    }

    // ------------------- Event handlers -------------------

    @FXML
    private void handleSearch(ActionEvent event) {
        String q = (searchField.getText() == null) ? "" : searchField.getText().trim();
        if (q.isEmpty()) {
            showWarn("Search", "Type a Booking ID, Customer Name, or Vehicle Reg No.");
            return;
        }

        Booking booking = null;

        if (q.matches("\\d+")) booking = bookingDAO.getBookingById(Integer.parseInt(q));
        if (booking == null) booking = bookingDAO.getBookingByVehicleReg(q);

        if (booking == null) {
            try {
                List<Customer> matches = customerDAO.searchByLicenseOrName(q);
                if (matches.isEmpty()) { showInfo("Search", "No results for: " + q); return; }
                if (matches.size() > 1) { showWarn("Search", "Multiple customers matched. Please refine your search."); return; }
                Customer c = matches.get(0);
                booking = findLatestOpenBookingByCustomerId(c.getId());
                if (booking == null) { showWarn("Search", "No active/open booking found for " + c.getFullName()); return; }
            } catch (SQLException e) {
                showError("DB Error", "Failed searching by customer", e);
                return;
            }
        }

        loadBooking(booking);
    }

    // no-arg overload for ENTER shortcut
    private void handleSearch() { handleSearch((ActionEvent) null); }

    @FXML
    private void handleReset(ActionEvent event) {
        handleReset();
    }

    private void handleReset() {
        clearForm();
        currentBooking = null;
        currentCustomer = null;
        currentVehicle = null;
        searchField.clear();
        actualReturnDatePicker.setValue(LocalDate.now());
        searchField.requestFocus();
    }

    @FXML
    private void handleConfirmReturn(ActionEvent event) {
        if (!validateBeforeConfirm()) return;

        LocalDate pickupDate = epochToLocalDate(currentBooking.getPickupTs());
        LocalDate plannedReturnDate = epochToLocalDate(currentBooking.getReturnTs());
        LocalDate actualReturnDate = actualReturnDatePicker.getValue();

        int plannedDays = safeDaysBetweenInclusive(pickupDate, plannedReturnDate);
        int actualDays = safeDaysBetweenInclusive(pickupDate, actualReturnDate);
        int lateDays = Math.max(0, actualDays - plannedDays);

        int rateCents = currentBooking.getRateCents();
        int discountCents = currentBooking.getDiscountCents();
        int taxRateBp = currentBooking.getTaxRateBp();

        int lateFeeCents = (int) Math.round(lateDays * rateCents * LATE_FEE_MULTIPLIER);
        int rentalBaseCents = (plannedDays * rateCents) + lateFeeCents - discountCents;
        if (rentalBaseCents < 0) rentalBaseCents = 0;
        int taxCents = (int) Math.round((rentalBaseCents * (taxRateBp / 10000.0)));
        int totalCostCents = rentalBaseCents + taxCents;

        int paidCents = paymentDAO.getTotalCapturedAmountCentsByBooking(currentBooking.getId());
        int balanceCents = totalCostCents - paidCents;

        String msg = "Confirm return?\n\n" +
                "Booking: #" + currentBooking.getId() + "\n" +
                "Customer: " + currentCustomer.getFullName() + "\n" +
                "Vehicle: " + currentVehicle.getLicensePlate() + " (" + currentVehicle.getMake() + " " + currentVehicle.getModel() + ")\n\n" +
                "Planned Days: " + plannedDays + "\n" +
                "Actual Days: " + actualDays + "\n" +
                "Late Days: " + lateDays + "\n" +
                "Late Fee: " + formatCents(lateFeeCents) + "\n" +
                "Total: " + formatCents(totalCostCents) + "\n" +
                "Paid: " + formatCents(paidCents) + "\n" +
                "Balance/Refund: " + (balanceCents >= 0 ? formatCents(balanceCents) : ("REFUND " + formatCents(-balanceCents)));
        if (!confirm("Return Vehicle", msg)) return;

        try {
            long actualReturnTs = localDateToEpochSeconds(actualReturnDate);
            Integer returnOdo = currentBooking.getReturnOdometer();
            int odo = (returnOdo == null) ? 0 : returnOdo;

            boolean ok1 = bookingDAO.closeBooking(
                    currentBooking.getId(),
                    actualReturnTs,
                    odo,
                    lateFeeCents,
                    STATUS_COMPLETED
            );

            String addedNotes = (notesArea.getText() == null) ? "" : notesArea.getText().trim();
            if (!addedNotes.isEmpty()) appendBookingNotes(currentBooking.getId(), addedNotes);

            boolean ok2 = updateVehicleStatus(currentBooking.getVehicleId(), "AVAILABLE");

            if (ok1 && ok2) {
                showInfo("Success", "Vehicle returned and booking marked as COMPLETED.");
                statusField.setText(STATUS_COMPLETED);
                setText(plannedDaysField, String.valueOf(plannedDays));
                setText(actualDaysField, String.valueOf(actualDays));
                setText(lateDaysField, String.valueOf(lateDays));
                setText(lateFeeField, formatCents(lateFeeCents));
                setText(totalCostField, formatCents(totalCostCents));
                setText(paidAmountField, formatCents(paidCents));
                setText(balanceRefundField, balanceCents >= 0 ? formatCents(balanceCents) : ("REFUND " + formatCents(-balanceCents)));
            } else {
                showError("Error", "Failed to update booking/vehicle.", null);
            }
        } catch (Exception e) {
            showError("Error", "Failed to confirm return.", e);
        }
    }

    // ================== PREVIEW + PRINT RECEIPT ==================
    @FXML
    private void handlePrintReceipt(ActionEvent event) { openReceiptPreview(); }

    private void openReceiptPreview() {
        if (currentBooking == null || currentCustomer == null || currentVehicle == null) {
            showWarn("Print", "Please search and load a booking before printing.");
            return;
        }
        String html = buildReceiptHtml();

        WebView webView = new WebView();
        WebEngine engine = webView.getEngine();
        engine.loadContent(html, "text/html");

        Button btnPrint = new Button("Print (Ctrl+P)");
        btnPrint.setOnAction(e -> {
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null && job.showPrintDialog(rootPane.getScene().getWindow())) {
                Printer printer = job.getPrinter();
                PageLayout layout = printer.createPageLayout(Paper.A4, javafx.print.PageOrientation.PORTRAIT, Printer.MarginType.DEFAULT);
                engine.print(job);
                job.endJob();
            }
        });

        Button btnClose = new Button("Close");
        btnClose.setOnAction(e -> ((Stage) btnClose.getScene().getWindow()).close());

        HBox toolbar = new HBox(10, btnPrint, btnClose);
        toolbar.setStyle("-fx-padding: 10; -fx-background-color: #f3f4f6; -fx-border-color: #e5e7eb; -fx-border-width: 0 0 1 0;");

        BorderPane root = new BorderPane();
        root.setTop(toolbar);
        root.setCenter(webView);

        Stage stage = new Stage();
        stage.setTitle("Receipt Preview - Booking #" + currentBooking.getId());
        Scene scene = new Scene(root, 900, 1000);
        stage.setScene(scene);
        scene.getAccelerators().put(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN), btnPrint::fire);
        stage.show();

        engine.getLoadWorker().stateProperty().addListener((obs, old, st) -> {
            if (st == Worker.State.SUCCEEDED) {
                // ready
            }
        });
    }

    private String buildReceiptHtml() {
        String tpl = loadTemplateOrDefault();

        // Replace any absolute Windows path for the logo with embedded data URL from classpath
        String dataLogo = dataUrlForLogo();
        String absPath = "C:\\\\Users\\\\LENOVO\\\\OneDrive\\\\Desktop\\\\rentaCar\\\\src\\\\main\\\\resources\\\\templates\\\\Side Bar Logo.png";
        tpl = tpl.replace(absPath, dataLogo);
        tpl = tpl.replaceAll("src\\s*=\\s*\"[^\"]*Side\\s*Bar\\s*Logo\\.png\"", "src=\"" + dataLogo + "\"");
        tpl = tpl.replaceAll("src\\s*=\\s*'[^']*Side\\s*Bar\\s*Logo\\.png'", "src='" + dataLogo + "'");

        // Figures
        LocalDate pickup = epochToLocalDate(currentBooking.getPickupTs());
        LocalDate plannedRet = epochToLocalDate(currentBooking.getReturnTs());
        LocalDate actualRet = actualReturnDatePicker.getValue() != null ? actualReturnDatePicker.getValue() : plannedRet;

        int plannedDays = safeDaysBetweenInclusive(pickup, plannedRet);
        int actualDays = safeDaysBetweenInclusive(pickup, actualRet);
        int lateDays = Math.max(0, actualDays - plannedDays);

        int rateCents = currentBooking.getRateCents();
        int discountCents = currentBooking.getDiscountCents();
        int taxRateBp = (currentBooking.getTaxRateBp() > 0) ? currentBooking.getTaxRateBp() : getDefaultTaxRateBp();

        int plannedAmountCents = plannedDays * rateCents;
        int lateFeeCents = (int) Math.round(lateDays * rateCents * LATE_FEE_MULTIPLIER);
        int subtotalCents = Math.max(0, plannedAmountCents + lateFeeCents - discountCents);
        int taxCents = (int) Math.round(subtotalCents * (taxRateBp / 10000.0));
        int totalCents = subtotalCents + taxCents;
        int paidCents = paymentDAO.getTotalCapturedAmountCentsByBooking(currentBooking.getId());
        int balCents = totalCents - paidCents;
        String balanceLabel = (balCents >= 0) ? "Balance" : "Refund";

        String html = tpl
                .replace("{{reservationId}}", String.valueOf(currentBooking.getId()))
                .replace("{{customerName}}", safe(currentCustomer.getFullName()))
                .replace("{{license}}", safe(currentCustomer.getLicenseNo()))
                .replace("{{phone}}", safe(currentCustomer.getPhone()))
                .replace("{{vehicleRegNo}}", currentVehicle.getLicensePlate())
                .replace("{{vehicle}}", currentVehicle.getMake() + " " + currentVehicle.getModel())
                .replace("{{pickup}}", pickup.toString())
                .replace("{{due}}", plannedRet.toString())
                .replace("{{actualReturn}}", actualRet.toString())
                .replace("{{status}}", safe(statusField.getText()))
                .replace("{{plannedDays}}", String.valueOf(plannedDays))
                .replace("{{actualDays}}", String.valueOf(actualDays))
                .replace("{{lateDays}}", String.valueOf(lateDays))
                .replace("{{dailyRate}}", centsToPlain(rateCents))
                .replace("{{plannedAmount}}", centsToPlain(plannedAmountCents))
                .replace("{{lateFee}}", centsToPlain(lateFeeCents))
                .replace("{{discount}}", centsToPlain(discountCents))
                .replace("{{taxPercent}}", String.valueOf(Math.round(taxRateBp / 100.0)))
                .replace("{{tax}}", centsToPlain(taxCents))
                .replace("{{subtotal}}", centsToPlain(subtotalCents))
                .replace("{{total}}", centsToPlain(totalCents))
                .replace("{{paid}}", centsToPlain(paidCents))
                .replace("{{balanceLabel}}", balanceLabel)
                .replace("{{balance}}", centsToPlain(Math.abs(balCents)))
                .replace("{{invoiceNo}}", makeInvoiceNo(currentBooking))
                .replace("{{date}}", LocalDate.now().toString());

        html = applyNotesConditional(html, (notesArea.getText() == null) ? "" : notesArea.getText().trim());
        return html;
    }

    private String loadTemplateOrDefault() {
        try (InputStream is = getClass().getResourceAsStream("/templates/return-receipt.html")) {
            if (is != null) return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception ignored) {}
        return "<html><body><h2>Receipt</h2><div>No template found.</div></body></html>";
    }

    private String applyNotesConditional(String tpl, String notes) {
        String pattern = "(?s)\\{\\{#if\\s+notes\\}\\}(.*?)\\{\\{\\/if\\}\\}";
        if (notes == null || notes.isBlank()) {
            return Pattern.compile(pattern).matcher(tpl).replaceAll("");
        } else {
            String keepMarkersRemoved = tpl.replace("{{#if notes}}", "").replace("{{/if}}", "");
            return keepMarkersRemoved.replace("{{notes}}", escapeHtml(notes));
        }
    }

    private String dataUrlForLogo() {
        String[] candidates = {
                "/com/mycompany/rentcar/Side Bar Logo.png",
                "/com/mycompany/rentcar/Logo 300200.png"
        };
        for (String path : candidates) {
            try (InputStream is = getClass().getResourceAsStream(path)) {
                if (is != null) {
                    byte[] bytes = is.readAllBytes();
                    return "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes);
                }
            } catch (Exception ignored) {}
        }
        return "";
    }

    // ------------------- Navigation -------------------
    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) rootPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleExit(ActionEvent e) {
        if (!openWindow("Rental.fxml", "Rental")) {
            handleCancel(null);
        }
    }

    private void handleRefresh() {
        try {
            if (currentBooking != null) {
                Booking fresh = bookingDAO.getBookingById(currentBooking.getId());
                if (fresh != null) {
                    loadBooking(fresh);
                } else {
                    clearForm();
                }
            } else {
                clearForm();
            }
        } catch (Exception ex) {
            showError("Refresh", "Failed to refresh data.", ex);
        }
    }

    private void handleBook() { openWindow("Rental.fxml", "Rental"); }

    @FXML private void handleAvailability(ActionEvent e) { openWindow("Availability.fxml", "Availability"); }
    @FXML private void handleCustomer(ActionEvent e) { openWindow("Customer.fxml", "Customer"); }
    @FXML private void handleRental(ActionEvent e) { openWindow("Rental.fxml", "Rental"); }
    @FXML private void handleReturn(ActionEvent e) { openWindow("Return.fxml", "Return"); }
    @FXML private void handleVehicles(ActionEvent e) { openWindow("Vehical.fxml", "Vehicles"); }

    private boolean openWindow(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/mycompany/rentcar/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setMaximized(true);
            stage.show();

            Stage current = (Stage) rootPane.getScene().getWindow();
            if (current != null) current.hide();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(title + " Failed", "Cannot open " + title + " window.");
            return false;
        }
    }

    // ------------------- Helpers -------------------

    private void clearForm() {
        setText(reservationIdField, "");
        setText(customerNameField, "");
        setText(customerLicenseField, "");
        setText(customerPhoneField, "");
        setText(vehicleRegNoField, "");
        setText(vehicleModelField, "");
        setText(pickupDateField, "");
        setText(dueDateField, "");
        setText(statusField, "");

        setText(plannedDaysField, "");
        setText(actualDaysField, "");
        setText(lateDaysField, "");
        setText(lateFeeField, "");
        setText(totalCostField, "");
        setText(paidAmountField, "");
        setText(balanceRefundField, "");
        if (notesArea != null) notesArea.clear();
    }

    private void loadBooking(Booking booking) {
        try {
            currentBooking = booking;
            currentCustomer = customerDAO.getCustomerById(booking.getCustomerId());
            currentVehicle = getVehicleById(booking.getVehicleId());

            if (currentCustomer == null || currentVehicle == null) {
                showError("Data Missing", "Customer or Vehicle not found for booking.", null);
                return;
            }

            setText(reservationIdField, String.valueOf(booking.getId()));
            setText(customerNameField, currentCustomer.getFullName());
            setText(customerLicenseField, currentCustomer.getLicenseNo());
            setText(customerPhoneField, currentCustomer.getPhone());
            setText(vehicleRegNoField, currentVehicle.getLicensePlate());
            setText(vehicleModelField, currentVehicle.getMake() + " / " + currentVehicle.getModel());

            LocalDate pickup = epochToLocalDate(booking.getPickupTs());
            LocalDate plannedReturn = epochToLocalDate(booking.getReturnTs());
            setText(pickupDateField, pickup.toString());
            setText(dueDateField, plannedReturn.toString());
            setText(statusField, booking.getStatus());

            actualReturnDatePicker.setValue(LocalDate.now());
            updateCalculatedFields();

        } catch (Exception e) {
            showError("Load Booking", "Failed to load booking details.", e);
        }
    }

    private void updateCalculatedFields() {
        if (currentBooking == null) return;

        LocalDate pickupDate = epochToLocalDate(currentBooking.getPickupTs());
        LocalDate plannedReturnDate = epochToLocalDate(currentBooking.getReturnTs());
        LocalDate actualReturnDate = actualReturnDatePicker.getValue();

        int plannedDays = safeDaysBetweenInclusive(pickupDate, plannedReturnDate);
        int actualDays = safeDaysBetweenInclusive(pickupDate, actualReturnDate);
        int lateDays = Math.max(0, actualDays - plannedDays);

        int rateCents = currentBooking.getRateCents();
        int discountCents = currentBooking.getDiscountCents();
        int taxRateBp = currentBooking.getTaxRateBp();

        int lateFeeCents = (int) Math.round(lateDays * rateCents * LATE_FEE_MULTIPLIER);
        int rentalBaseCents = (plannedDays * rateCents) + lateFeeCents - discountCents;
        if (rentalBaseCents < 0) rentalBaseCents = 0;
        int taxCents = (int) Math.round(rentalBaseCents * (taxRateBp / 10000.0));
        int totalCents = rentalBaseCents + taxCents;

        int paidCents = paymentDAO.getTotalCapturedAmountCentsByBooking(currentBooking.getId());
        int balanceCents = totalCents - paidCents;

        setText(plannedDaysField, String.valueOf(plannedDays));
        setText(actualDaysField, String.valueOf(actualDays));
        setText(lateDaysField, String.valueOf(lateDays));
        setText(lateFeeField, formatCents(lateFeeCents));
        setText(totalCostField, formatCents(totalCents));
        setText(paidAmountField, formatCents(paidCents));
        setText(balanceRefundField, balanceCents >= 0 ? formatCents(balanceCents)
                                                     : ("REFUND " + formatCents(-balanceCents)));
    }

    private boolean validateBeforeConfirm() {
        if (currentBooking == null) { showWarn("Return", "Search and select a reservation first."); return false; }
        if (STATUS_COMPLETED.equalsIgnoreCase(currentBooking.getStatus())) { showWarn("Return", "This booking is already completed."); return false; }
        if (actualReturnDatePicker.getValue() == null) { showWarn("Return", "Pick an actual return date."); return false; }
        LocalDate pickupDate = epochToLocalDate(currentBooking.getPickupTs());
        if (actualReturnDatePicker.getValue().isBefore(pickupDate)) { showWarn("Return", "Actual return date cannot be before pickup date."); return false; }
        return true;
    }

    // ------------------- DB utilities -------------------

    private Vehicle getVehicleById(int vehicleId) throws SQLException {
        String sql = "SELECT * FROM vehicles WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, vehicleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vehicle v = new Vehicle();
                    v.setId(rs.getInt("id"));
                    v.setMake(rs.getString("make"));
                    v.setModel(rs.getString("model"));
                    v.setYear(rs.getInt("year"));
                    v.setLicensePlate(rs.getString("plate_no"));
                    v.setColor(rs.getString("color"));
                    v.setDailyRate(rs.getInt("daily_rate_cents") / 100.0);
                    v.setStatus(rs.getString("status"));
                    v.setMileage(rs.getInt("odometer"));
                    return v;
                }
            }
        }
        return null;
    }

    private boolean updateVehicleStatus(int vehicleId, String status) throws SQLException {
        String sql = "UPDATE vehicles SET status=?, updated_at=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setLong(2, System.currentTimeMillis() / 1000L);
            ps.setInt(3, vehicleId);
            return ps.executeUpdate() > 0;
        }
    }

    private Booking findLatestOpenBookingByCustomerId(int customerId) throws SQLException {
        String sql = "SELECT * FROM bookings WHERE customer_id = ? AND status NOT IN ('COMPLETED', 'CANCELLED') ORDER BY id DESC LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, customerId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) return mapResultSetToBooking(rs);
            }
        }
        return null;
    }

    private void appendBookingNotes(int bookingId, String extraNotes) throws SQLException {
        String sql = "UPDATE bookings SET notes = CONCAT(COALESCE(notes,''), ?) WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String prefix = "\n[Return] ";
            ps.setString(1, prefix + extraNotes);
            ps.setInt(2, bookingId);
            ps.executeUpdate();
        }
    }

    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setId(rs.getInt("id"));
        b.setCustomerId(rs.getInt("customer_id"));
        b.setVehicleId(rs.getInt("vehicle_id"));
        b.setPickupTs(rs.getLong("pickup_ts"));
        b.setReturnTs(rs.getLong("return_ts"));
        b.setStatus(rs.getString("status"));
        b.setRateCents(rs.getInt("rate_cents"));
        b.setDepositCents(rs.getInt("deposit_cents"));
        b.setTaxRateBp(rs.getInt("tax_rate_bp"));
        b.setDiscountCents(rs.getInt("discount_cents"));
        int pOdo = rs.getInt("pickup_odometer");
        b.setPickupOdometer(rs.wasNull() ? null : pOdo);
        int rOdo = rs.getInt("return_odometer");
        b.setReturnOdometer(rs.wasNull() ? null : rOdo);
        b.setLateFeeCents(rs.getInt("late_fee_cents"));
        b.setNotes(rs.getString("notes"));
        b.setCreatedBy(rs.getInt("created_by"));
        b.setCreatedAt(rs.getLong("created_at"));
        long upd = rs.getLong("updated_at");
        b.setUpdatedAt(rs.wasNull() ? null : upd);
        return b;
    }

    private int getDefaultTaxRateBp() {
        String sql = "SELECT value FROM settings WHERE `key`='tax.rate_bp'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                String v = rs.getString(1);
                return Integer.parseInt(v.trim());
            }
        } catch (Exception ignored) {}
        return 0;
    }

    // ------------------- Util methods -------------------

    private LocalDate epochToLocalDate(long epochSeconds) {
        return Instant.ofEpochSecond(epochSeconds).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private long localDateToEpochSeconds(LocalDate date) {
        return date.atStartOfDay(ZoneId.systemDefault()).toEpochSecond();
    }

    private int safeDaysBetweenInclusive(LocalDate start, LocalDate end) {
        if (start == null || end == null) return 0;
        long days = ChronoUnit.DAYS.between(start, end) + 1;
        return (int) Math.max(days, 0);
    }

    private String formatCents(int cents) {
        double amount = cents / 100.0;
        synchronized (lkrFmt) {
            return "LKR " + lkrFmt.format(amount);
        }
    }

    private String centsToPlain(int cents) {
        double amount = cents / 100.0;
        synchronized (lkrFmt) {
            return lkrFmt.format(amount);
        }
    }

    private String makeInvoiceNo(Booking b) {
        return "INV-" + LocalDate.now().getYear() + "-" + b.getId();
    }

    private String escapeHtml(String s) {
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }

    private String safe(String s) { return s == null ? "" : s; }

    private void setText(TextField tf, String text) {
        if (tf != null) tf.setText(text == null ? "" : text);
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

    private void showError(String title, String msg, Throwable t) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setHeaderText(title);
        a.setContentText(msg + (t != null ? ("\n\n" + t.getMessage()) : ""));
        a.showAndWait();
        if (t != null) t.printStackTrace();
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText(title);
        a.showAndWait();
    }

    private boolean confirm(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION, msg, ButtonType.OK, ButtonType.CANCEL);
        a.setHeaderText(title);
        Optional<ButtonType> res = a.showAndWait();
        return res.isPresent() && res.get() == ButtonType.OK;
    }
}