package Car;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.text.MessageFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class Report01 extends javax.swing.JFrame {

    // ---- DB CONFIG ----
    private static final String DB_URL =
            "jdbc:mysql://localhost:3306/car_rental?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    // Map of status IDs (from your schema)
    private final Map<Integer, String> statusMap = new LinkedHashMap<Integer, String>() {{
        put(12, "Available");
        put(13, "Rented");
        put(14, "Maintenance");
        put(15, "Reserved");
    }};

    public Report01() {
        initComponents();
        populateReportTypes();
        ensureReportsButtonInSidebar();
        toggleDatePickers();
    }

    // ===== BEGIN: Add REPORTS under CARS (keep exact sidebar design, works in every class) =====
    private void ensureReportsButtonInSidebar() {
        if (jPanel2 == null) return;

        // Skip if already present
        if (hasReportsButton(jPanel2)) return;

        // Resolve standard components by field name; fallback to find-by-text
        javax.swing.JLabel logo = getLabelByFieldOrNull("jLabel4");
        if (logo == null) logo = findLogoLabel(jPanel2);

        javax.swing.JButton btnAvail = getButtonByFieldOrNull("jButton1");
        if (btnAvail == null) btnAvail = findButtonByText(jPanel2, "AVAILABILITY");

        javax.swing.JButton btnCust  = getButtonByFieldOrNull("jButton3");
        if (btnCust == null) btnCust = findButtonByText(jPanel2, "CUSTOMERS");

        javax.swing.JButton btnRent  = getButtonByFieldOrNull("jButton2");
        if (btnRent == null) btnRent = findButtonByText(jPanel2, "RENT");

        javax.swing.JButton btnReturn = getButtonByFieldOrNull("jButton11");
        if (btnReturn == null) btnReturn = findButtonByText(jPanel2, "RETURN");

        // CARS button may be "ModifyTb" or "jButton4"
        javax.swing.JButton btnCars  = getButtonByFieldOrNull("ModifyTb");
        if (btnCars == null) btnCars = getButtonByFieldOrNull("jButton4");
        if (btnCars == null) btnCars = findButtonByText(jPanel2, "CARS");
        if (btnCars == null) return; // cannot anchor without CARS

        // Clone style from CARS and create REPORTS
        javax.swing.JButton jButtonReports = new javax.swing.JButton("REPORTS");
        cloneButtonStyle(btnCars, jButtonReports);
        for (java.awt.event.ActionListener al : jButtonReports.getActionListeners()) jButtonReports.removeActionListener(al);
        jButtonReports.addActionListener(evt -> {
            try { new Report01().setVisible(true); } catch (Exception ex) { ex.printStackTrace(); }
            dispose();
        });

        // Optional small labels if present (kept to preserve layout)
        javax.swing.JLabel smallAcc = getLabelByFieldOrNull("jLabel9");
        if (smallAcc == null) smallAcc = findLabelByText(jPanel2, "ACCOUNT");
        javax.swing.JLabel smallOut = getLabelByFieldOrNull("jLabel10");
        if (smallOut == null) smallOut = findLabelByText(jPanel2, "LOGOUT");

        // Bottom labels (large)
        javax.swing.JLabel lblAccount = getLabelByFieldOrNull("jLabel3");
        if (lblAccount == null) lblAccount = findLabelByExactText(jPanel2, "ACCOUNT");
        javax.swing.JLabel lblLogout  = getLabelByFieldOrNull("jLabel2");
        if (lblLogout == null) lblLogout = findLabelByExactText(jPanel2, "LOGOUT");

        // Rewire nav buttons safely (only if present)
        rewireNavButton(btnAvail,  () -> { new Availability().setVisible(true); dispose(); });
        rewireNavButton(btnCust,   () -> { new Customers().setVisible(true);   dispose(); });
        rewireNavButton(btnRent,   () -> { new Rental().setVisible(true);      dispose(); });
        rewireNavButton(btnReturn, () -> { new Return().setVisible(true);      dispose(); });
        // current page is CARS; disable its click without changing look
        if (btnCars != null) {
            for (java.awt.event.ActionListener al : btnCars.getActionListeners()) btnCars.removeActionListener(al);
            btnCars.setEnabled(false);
        }

        // Rebuild sidebar as GroupLayout with the same order + REPORTS under CARS
        javax.swing.GroupLayout gl = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(gl);

        javax.swing.GroupLayout.ParallelGroup hGroup = gl.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING);
        addIfNotNull(hGroup, logo);
        addIfNotNull(hGroup, btnAvail);
        addIfNotNull(hGroup, btnCust);
        addIfNotNull(hGroup, btnRent);
        addIfNotNull(hGroup, btnReturn);
        addIfNotNull(hGroup, btnCars);
        addIfNotNull(hGroup, jButtonReports);
        addIfNotNull(hGroup, smallAcc);
        addIfNotNull(hGroup, smallOut);
        addIfNotNull(hGroup, lblAccount);
        addIfNotNull(hGroup, lblLogout);

        gl.setHorizontalGroup(
            gl.createSequentialGroup()
              .addContainerGap()
              .addGroup(hGroup)
              .addContainerGap()
        );

        javax.swing.GroupLayout.SequentialGroup vGroup = gl.createSequentialGroup()
            .addContainerGap();

        if (logo != null) vGroup.addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE);
        vGroup.addGap(46);

        addRow(vGroup, btnAvail);
        addRow(vGroup, btnCust);
        addRow(vGroup, btnRent);
        addRow(vGroup, btnReturn);
        addRow(vGroup, btnCars);
        addRow(vGroup, jButtonReports);

        vGroup.addGap(18);
        if (smallAcc != null) vGroup.addComponent(smallAcc);
        vGroup.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED);
        if (smallOut != null) vGroup.addComponent(smallOut);

        vGroup.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 759, Short.MAX_VALUE);
        if (lblAccount != null) vGroup.addComponent(lblAccount);
        vGroup.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
        if (lblLogout != null) vGroup.addComponent(lblLogout);
        vGroup.addGap(57);

        gl.setVerticalGroup(
            gl.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(vGroup)
        );

        jPanel2.revalidate();
        jPanel2.repaint();
    }

    // Helpers (generic and safe)
    private void addRow(javax.swing.GroupLayout.SequentialGroup v, java.awt.Component c) {
        if (c == null) return;
        v.addComponent(c, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE);
        v.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
    }

    private void addIfNotNull(javax.swing.GroupLayout.ParallelGroup h, java.awt.Component c) {
        if (c != null) h.addComponent(c, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);
    }

    private void cloneButtonStyle(javax.swing.JButton src, javax.swing.JButton dst) {
        if (src != null) {
            dst.setBackground(src.getBackground());
            dst.setForeground(src.getForeground());
            dst.setFont(src.getFont());
            dst.setBorder(src.getBorder());
            dst.setCursor(src.getCursor());
            dst.setFocusPainted(false);
            dst.setOpaque(true);
            try { dst.setPreferredSize(src.getPreferredSize()); } catch (Exception ignore) {}
            try { dst.setMaximumSize(src.getMaximumSize()); } catch (Exception ignore) {}
        } else {
            dst.setBackground(new java.awt.Color(102,102,255));
            dst.setForeground(java.awt.Color.WHITE);
            java.awt.Font f = dst.getFont();
            dst.setFont(f.deriveFont(java.awt.Font.BOLD, f.getSize()+8f));
            dst.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            dst.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
            dst.setPreferredSize(new java.awt.Dimension(260, 67));
        }
    }

    private void rewireNavButton(javax.swing.JButton btn, Runnable open) {
        if (btn == null) return;
        for (java.awt.event.ActionListener al : btn.getActionListeners()) btn.removeActionListener(al);
        btn.addActionListener(e -> { try { open.run(); } catch (Exception ex) { ex.printStackTrace(); } });
    }

    private boolean hasReportsButton(java.awt.Container root) {
        if (root == null) return false;
        for (java.awt.Component c : root.getComponents()) {
            if (c instanceof javax.swing.JButton) {
                String t = ((javax.swing.JButton) c).getText();
                if (t != null && t.trim().equalsIgnoreCase("REPORTS")) return true;
            } else if (c instanceof java.awt.Container) {
                if (hasReportsButton((java.awt.Container) c)) return true;
            }
        }
        return false;
    }

    private javax.swing.JButton findButtonByText(java.awt.Container root, String text) {
        if (root == null || text == null) return null;
        String target = text.trim().toUpperCase();
        for (java.awt.Component c : root.getComponents()) {
            if (c instanceof javax.swing.JButton) {
                String t = ((javax.swing.JButton) c).getText();
                if (t != null && t.trim().toUpperCase().equals(target)) return (javax.swing.JButton) c;
            } else if (c instanceof java.awt.Container) {
                javax.swing.JButton r = findButtonByText((java.awt.Container) c, text);
                if (r != null) return r;
            }
        }
        return null;
    }

    private javax.swing.JLabel findLabelByText(java.awt.Container root, String text) {
        if (root == null || text == null) return null;
        String target = text.trim().toUpperCase();
        for (java.awt.Component c : root.getComponents()) {
            if (c instanceof javax.swing.JLabel) {
                String t = ((javax.swing.JLabel) c).getText();
                if (t != null && t.trim().toUpperCase().equals(target)) return (javax.swing.JLabel) c;
            } else if (c instanceof java.awt.Container) {
                javax.swing.JLabel r = findLabelByText((java.awt.Container) c, text);
                if (r != null) return r;
            }
        }
        return null;
    }

    private javax.swing.JLabel findLabelByExactText(java.awt.Container root, String text) {
        if (root == null || text == null) return null;
        for (java.awt.Component c : root.getComponents()) {
            if (c instanceof javax.swing.JLabel) {
                String t = ((javax.swing.JLabel) c).getText();
                if (t != null && t.equals(text)) return (javax.swing.JLabel) c;
            } else if (c instanceof java.awt.Container) {
                javax.swing.JLabel r = findLabelByExactText((java.awt.Container) c, text);
                if (r != null) return r;
            }
        }
        return null;
    }

    private javax.swing.JLabel findLogoLabel(java.awt.Container root) {
        if (root == null) return null;
        for (java.awt.Component c : root.getComponents()) {
            if (c instanceof javax.swing.JLabel) {
                javax.swing.JLabel l = (javax.swing.JLabel) c;
                if (l.getIcon() != null) return l;
            } else if (c instanceof java.awt.Container) {
                javax.swing.JLabel r = findLogoLabel((java.awt.Container) c);
                if (r != null) return r;
            }
        }
        return null;
    }

    // Reflection helpers (avoid compile-time dependency on field names)
    private javax.swing.JButton getButtonByFieldOrNull(String fieldName) {
        try {
            java.lang.reflect.Field f = getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            Object o = f.get(this);
            return (o instanceof javax.swing.JButton) ? (javax.swing.JButton)o : null;
        } catch (Exception ignore) { return null; }
    }
    private javax.swing.JLabel getLabelByFieldOrNull(String fieldName) {
        try {
            java.lang.reflect.Field f = getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            Object o = f.get(this);
            return (o instanceof javax.swing.JLabel) ? (javax.swing.JLabel)o : null;
        } catch (Exception ignore) { return null; }
    }
    // ===== END: Add REPORTS under CARS (keep exact sidebar design, works in every class) =====

    private void populateReportTypes() {
        jComboBox1.removeAllItems();
        // Cars
        jComboBox1.addItem("Cars - All");
        jComboBox1.addItem("Cars - Available (status_id=12)");
        jComboBox1.addItem("Cars - Rented (status_id=13)");
        jComboBox1.addItem("Cars - Maintenance (status_id=14)");
        jComboBox1.addItem("Cars - Reserved (status_id=15)");
        // Rentals by date
        jComboBox1.addItem("Rentals - Rent Date (From–To)");
        jComboBox1.addItem("Rentals - Return Date (From–To)");
        jComboBox1.setSelectedIndex(0);
    }

    private boolean isDateReportSelected() {
        String sel = (String) jComboBox1.getSelectedItem();
        return sel != null && sel.startsWith("Rentals - ");
    }

    private void toggleDatePickers() {
        boolean enable = isDateReportSelected();
        dateFromPicker.setEnabled(enable);
        dateToPicker.setEnabled(enable);
    }

    private void onGenerate(ActionEvent evt) {
        String report = (String) jComboBox1.getSelectedItem();

        Date from = dateFromPicker.getDate();
        Date to = dateToPicker.getDate();

        try {
            if (isDateReportSelected()) {
                if (from == null || to == null) {
                    JOptionPane.showMessageDialog(this, "Please select both From and To dates.", "Missing Dates", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (from.after(to)) {
                    Date t = from; from = to; to = t;
                    dateFromPicker.setDate(from);
                    dateToPicker.setDate(to);
                }
                runRentalDateReport(report, from, to);
            } else {
                runCarReport(report);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error generating report: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void onPrint(ActionEvent evt) {
        try {
            MessageFormat header = new MessageFormat("CRMS Report - " + jComboBox1.getSelectedItem());
            MessageFormat footer = new MessageFormat("Page {0}");
            resultsTable.print(JTable.PrintMode.FIT_WIDTH, header, footer);
        } catch (PrinterException e) {
            JOptionPane.showMessageDialog(this, "Printer error: " + e.getMessage(),
                    "Print Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onExport(ActionEvent evt) {
        DefaultTableModel model = (DefaultTableModel) resultsTable.getModel();
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No data to export.", "Export CSV", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Save report as CSV");
        fc.setSelectedFile(new java.io.File("report.csv"));
        if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try (FileWriter fw = new FileWriter(fc.getSelectedFile())) {
                // header
                for (int c = 0; c < model.getColumnCount(); c++) {
                    fw.write(escapeCsv(model.getColumnName(c)));
                    if (c < model.getColumnCount() - 1) fw.write(",");
                }
                fw.write("\n");
                // rows
                for (int r = 0; r < model.getRowCount(); r++) {
                    for (int c = 0; c < model.getColumnCount(); c++) {
                        Object val = model.getValueAt(r, c);
                        fw.write(escapeCsv(val == null ? "" : val.toString()));
                        if (c < model.getColumnCount() - 1) fw.write(",");
                    }
                    fw.write("\n");
                }
                JOptionPane.showMessageDialog(this, "Exported successfully.", "Export CSV", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Error writing CSV: " + e.getMessage(),
                        "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private String escapeCsv(String s) {
        String q = "\"";
        if (s.contains(",") || s.contains("\n") || s.contains(q)) {
            s = q + s.replace(q, q + q) + q;
        }
        return s;
    }

    private void onReset(ActionEvent evt) {
        jComboBox1.setSelectedIndex(0);
        dateFromPicker.setDate(null);
        dateToPicker.setDate(null);
        ((DefaultTableModel) resultsTable.getModel()).setRowCount(0);
    }

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    // --- Reports: CARS ---
    private void runCarReport(String report) throws Exception {
        DefaultTableModel model = new DefaultTableModel(new Object[][]{},
                new String[]{"Reg No", "Model ID", "Rental", "Status ID", "Status"}) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
            @Override public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 1: case 2: case 3: return Integer.class;
                    default: return String.class;
                }
            }
        };
        resultsTable.setModel(model);
        resultsTable.setRowHeight(24);

        String base = "SELECT reg_no, model_id, rental, status_id FROM car";
        String sql;
        switch (report) {
            case "Cars - Available (status_id=12)":
                sql = base + " WHERE status_id = 12 ORDER BY reg_no";
                break;
            case "Cars - Rented (status_id=13)":
                sql = base + " WHERE status_id = 13 ORDER BY reg_no";
                break;
            case "Cars - Maintenance (status_id=14)":
                sql = base + " WHERE status_id = 14 ORDER BY reg_no";
                break;
            case "Cars - Reserved (status_id=15)":
                sql = base + " WHERE status_id = 15 ORDER BY reg_no";
                break;
            default: // Cars - All
                sql = base + " ORDER BY reg_no";
        }

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String reg = rs.getString("reg_no");
                int modelId = rs.getInt("model_id");
                int rental = rs.getInt("rental");
                int statusId = rs.getInt("status_id");
                model.addRow(new Object[]{reg, modelId, rental, statusId, statusMap.getOrDefault(statusId, "Status #" + statusId)});
            }
        }
    }

    // --- Reports: RENTALS by date (rental_records) ---
    private void runRentalDateReport(String report, Date from, Date to) throws Exception {
        String[] cols = new String[]{"Rental ID", "NIC", "Vehicle Number", "Brand", "Rent Date", "Return Date", "Fee"};
        DefaultTableModel model = new DefaultTableModel(new Object[][]{}, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        resultsTable.setModel(model);
        resultsTable.setRowHeight(24);

        String dateCol = report.contains("Return Date") ? "return_date" : "rent_date";
        String sql = "SELECT id, cust_nic, vehicle_no, brand, rent_date, return_date, total_fee " +
                     "FROM rental_records WHERE " + dateCol + " BETWEEN ? AND ? ORDER BY " + dateCol + " DESC";

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(from.getTime()));
            ps.setDate(2, new java.sql.Date(to.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                            rs.getInt("id"),
                            rs.getString("cust_nic"),
                            rs.getString("vehicle_no"),
                            rs.getString("brand"),
                            rs.getDate("rent_date"),
                            rs.getDate("return_date"),
                            rs.getInt("total_fee")
                    });
                }
            }
        }
    }

    // ===== UI =====
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();

        // Sidebar buttons
        jButtonAvailability = new javax.swing.JButton();
        jButtonCustomers = new javax.swing.JButton();
        jButtonRent = new javax.swing.JButton();
        jButtonReturn = new javax.swing.JButton();
        jButtonCars = new javax.swing.JButton();
        jButtonReports = new javax.swing.JButton(); // NEW: REPORTS (active)

        // Sidebar labels
        jLabelLogo = new javax.swing.JLabel();
        jLabelAccount = new javax.swing.JLabel();
        jLabelLogout = new javax.swing.JLabel();

        // Top and content
        jLabelTitle = new javax.swing.JLabel();
        jLabelReport = new javax.swing.JLabel();
        jLabelFrom = new javax.swing.JLabel();
        jLabelTo = new javax.swing.JLabel();
        jLabelResults = new javax.swing.JLabel();

        jComboBox1 = new javax.swing.JComboBox<>();
        dateFromPicker = new de.wannawork.jcalendar.JCalendarComboBox();
        dateToPicker = new de.wannawork.jcalendar.JCalendarComboBox();

        GenerateBtn = new javax.swing.JButton();
        DeleteBtn = new javax.swing.JButton(); // PRINT
        ExportBtn = new javax.swing.JButton(); // EXPORT CSV
        ResetBtn = new javax.swing.JButton();  // RESET
        AnalysisBtn = new javax.swing.JButton(); // NEW: ANALYSIS

        jScrollPane1 = new javax.swing.JScrollPane();
        resultsTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1300, 750));

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1366, 768));

        // ---- Sidebar (Left) ----
        jPanel2.setBackground(new java.awt.Color(0, 0, 51));
        jPanel2.setPreferredSize(new java.awt.Dimension(290, 768));

        // Buttons common style
        Color BTN_BG = new Color(102,102,255);
        Color BTN_ACTIVE_BG = new Color(204,204,255);

        jButtonAvailability.setBackground(BTN_BG);
        jButtonAvailability.setFont(jButtonAvailability.getFont().deriveFont(jButtonAvailability.getFont().getStyle() | java.awt.Font.BOLD, jButtonAvailability.getFont().getSize()+8));
        jButtonAvailability.setForeground(Color.WHITE);
        jButtonAvailability.setText("AVAILABILITY");
        jButtonAvailability.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonAvailability.addActionListener(evt -> {
            try { new Availability().setVisible(true); } catch (Exception ignore) {}
            this.setVisible(false);
        });

        jButtonCustomers.setBackground(BTN_BG);
        jButtonCustomers.setFont(jButtonCustomers.getFont().deriveFont(jButtonCustomers.getFont().getStyle() | java.awt.Font.BOLD, jButtonCustomers.getFont().getSize()+8));
        jButtonCustomers.setForeground(Color.WHITE);
        jButtonCustomers.setText("CUSTOMERS");
        jButtonCustomers.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonCustomers.addActionListener(evt -> {
            try { new Customers().setVisible(true); } catch (Exception ignore) {}
            this.setVisible(false);
        });

        jButtonRent.setBackground(BTN_BG);
        jButtonRent.setFont(jButtonRent.getFont().deriveFont(jButtonRent.getFont().getStyle() | java.awt.Font.BOLD, jButtonRent.getFont().getSize()+8));
        jButtonRent.setForeground(Color.WHITE);
        jButtonRent.setText("RENT");
        jButtonRent.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonRent.addActionListener(evt -> {
            try { new Rental().setVisible(true); } catch (Exception ignore) {}
            this.setVisible(false);
        });

        jButtonReturn.setBackground(BTN_BG);
        jButtonReturn.setFont(jButtonReturn.getFont().deriveFont(jButtonReturn.getFont().getStyle() | java.awt.Font.BOLD, jButtonReturn.getFont().getSize()+8));
        jButtonReturn.setForeground(Color.WHITE);
        jButtonReturn.setText("RETURN");
        jButtonReturn.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonReturn.addActionListener(evt -> {
            try { new Return().setVisible(true); } catch (Exception ignore) {}
            this.setVisible(false);
        });

        jButtonCars.setBackground(BTN_BG);
        jButtonCars.setFont(jButtonCars.getFont().deriveFont(jButtonCars.getFont().getStyle() | java.awt.Font.BOLD, jButtonCars.getFont().getSize()+8));
        jButtonCars.setForeground(Color.WHITE);
        jButtonCars.setText("CARS");
        jButtonCars.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonCars.addActionListener(evt -> {
            try { new Cars().setVisible(true); } catch (Exception ignore) {}
            this.setVisible(false);
        });

        // NEW: REPORTS button — active on this page
        jButtonReports.setBackground(BTN_ACTIVE_BG);
        jButtonReports.setFont(jButtonReports.getFont().deriveFont(jButtonReports.getFont().getStyle() | java.awt.Font.BOLD, jButtonReports.getFont().getSize()+8));
        jButtonReports.setForeground(Color.WHITE);
        jButtonReports.setText("REPORTS");
        jButtonReports.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButtonReports.setEnabled(false); // show active state (disabled on current page)

        jLabelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImageR/Side Bar Logo.png"))); // NOI18N

        // ACCOUNT (centered, white, clickable)
        jLabelAccount.setFont(new java.awt.Font("Arial", 1, 18));
        jLabelAccount.setForeground(Color.WHITE);
        jLabelAccount.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelAccount.setText("ACCOUNT");
        jLabelAccount.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabelAccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try { new Account().setVisible(true); } catch (Exception ignore) {}
                Report01.this.dispose();
            }
        });

        // LOGOUT (centered, white)
        jLabelLogout.setFont(new java.awt.Font("Arial", 1, 18));
        jLabelLogout.setForeground(Color.WHITE);
        jLabelLogout.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelLogout.setText("LOGOUT");
        jLabelLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        jLabelLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int opt = JOptionPane.showConfirmDialog(Report01.this, "Do you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.YES_OPTION) {
                    try { new Login().setVisible(true); } catch (Exception ignore) {}
                    Report01.this.dispose();
                }
            }
        });

        // Sidebar layout (GroupLayout)
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabelLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonAvailability, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonCustomers, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonRent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonReturn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonCars, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButtonReports, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelAccount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabelLogout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(46, 46, 46)
                    .addComponent(jButtonAvailability, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButtonCustomers, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButtonRent, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButtonReturn, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButtonCars, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButtonReports, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                    .addComponent(jLabelAccount)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabelLogout)
                    .addGap(40, 40, 40))
        );

        // ---- Header + filters ----
        jLabelTitle = new javax.swing.JLabel();
        jLabelTitle.setFont(new java.awt.Font("Segoe UI", 1, 24));
        jLabelTitle.setForeground(new java.awt.Color(0, 0, 255));
        jLabelTitle.setText("Reports");

        jLabelReport.setFont(jLabelReport.getFont().deriveFont(jLabelReport.getFont().getStyle() | java.awt.Font.BOLD, jLabelReport.getFont().getSize()+6));
        jLabelReport.setForeground(new java.awt.Color(0, 0, 51));
        jLabelReport.setText("Report");

        jLabelFrom.setFont(jLabelFrom.getFont().deriveFont(jLabelFrom.getFont().getStyle() | java.awt.Font.BOLD, jLabelFrom.getFont().getSize()+6));
        jLabelFrom.setForeground(new java.awt.Color(0, 0, 51));
        jLabelFrom.setText("From");

        jLabelTo.setFont(jLabelTo.getFont().deriveFont(jLabelTo.getFont().getStyle() | java.awt.Font.BOLD, jLabelTo.getFont().getSize()+6));
        jLabelTo.setForeground(new java.awt.Color(0, 0, 51));
        jLabelTo.setText("To");

        jLabelResults.setFont(new java.awt.Font("Segoe UI", 1, 18));
        jLabelResults.setForeground(new java.awt.Color(0, 0, 255));
        jLabelResults.setText("Results");

        GenerateBtn.setBackground(new java.awt.Color(0, 0, 51));
        GenerateBtn.setFont(GenerateBtn.getFont().deriveFont(GenerateBtn.getFont().getStyle() | java.awt.Font.BOLD, GenerateBtn.getFont().getSize()+6));
        GenerateBtn.setForeground(Color.WHITE);
        GenerateBtn.setText("GENERATE");
        GenerateBtn.addActionListener(this::onGenerate);

        DeleteBtn.setBackground(new java.awt.Color(0, 0, 153));
        DeleteBtn.setFont(DeleteBtn.getFont().deriveFont(DeleteBtn.getFont().getStyle() | java.awt.Font.BOLD, DeleteBtn.getFont().getSize()+6));
        DeleteBtn.setForeground(Color.WHITE);
        DeleteBtn.setText("PRINT");
        DeleteBtn.addActionListener(this::onPrint);

        ExportBtn.setBackground(new java.awt.Color(0, 0, 51));
        ExportBtn.setFont(ExportBtn.getFont().deriveFont(ExportBtn.getFont().getStyle() | java.awt.Font.BOLD, ExportBtn.getFont().getSize()+6));
        ExportBtn.setForeground(Color.WHITE);
        ExportBtn.setText("EXPORT CSV");
        ExportBtn.setPreferredSize(new java.awt.Dimension(170, 39));
        ExportBtn.addActionListener(this::onExport);

        ResetBtn.setBackground(new java.awt.Color(0, 0, 51));
        ResetBtn.setFont(ResetBtn.getFont().deriveFont(ResetBtn.getFont().getStyle() | java.awt.Font.BOLD, ResetBtn.getFont().getSize()+6));
        ResetBtn.setForeground(Color.WHITE);
        ResetBtn.setText("RESET");
        ResetBtn.addActionListener(this::onReset);

        // ANALYSIS button (new)
        AnalysisBtn.setBackground(new java.awt.Color(0, 0, 51));
        AnalysisBtn.setFont(AnalysisBtn.getFont().deriveFont(AnalysisBtn.getFont().getStyle() | java.awt.Font.BOLD, AnalysisBtn.getFont().getSize()+6));
        AnalysisBtn.setForeground(Color.WHITE);
        AnalysisBtn.setText("ANALYSIS");
        AnalysisBtn.setPreferredSize(new java.awt.Dimension(145, 39));
        AnalysisBtn.addActionListener(e -> showAnalysisDialog());

        resultsTable.setAutoCreateRowSorter(true);
        resultsTable.setBackground(new java.awt.Color(204, 204, 204));
        resultsTable.setFont(resultsTable.getFont().deriveFont(resultsTable.getFont().getStyle() | java.awt.Font.BOLD, 12));
        resultsTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {},
                new String [] { "Reg No", "Model ID", "Rental", "Status ID", "Status" }
        ) {
            public boolean isCellEditable(int row, int column) { return false; }
        });
        resultsTable.setRowHeight(24);
        jScrollPane1.setViewportView(resultsTable);

        // Main content layout (right of sidebar)
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(370, 370, 370)
                            .addComponent(jLabelTitle)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1038, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabelReport)
                                    .addGap(18, 18, 18)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(40, 40, 40)
                                    .addComponent(jLabelFrom)
                                    .addGap(10, 10, 10)
                                    .addComponent(dateFromPicker, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(20, 20, 20)
                                    .addComponent(jLabelTo)
                                    .addGap(10, 10, 10)
                                    .addComponent(dateToPicker, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(20, 20, 20)
                                    .addComponent(GenerateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(jLabelResults)
                                    .addGap(600, 600, 600)
                                    .addComponent(ExportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(DeleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(ResetBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(AnalysisBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 900, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabelTitle)
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabelReport)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelFrom)
                        .addComponent(dateFromPicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabelTo)
                        .addComponent(dateToPicker, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(GenerateBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabelResults)
                        .addComponent(ExportBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(DeleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ResetBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(AnalysisBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
        );

        pack();

        // Hook up combo action after components exist
        jComboBox1.addActionListener(e -> toggleDatePickers());
    }// </editor-fold>

    // ===== Auto full-screen + responsive scaling (your helper) =====
    private boolean __autoFS_inited = false;
    @Override
    public void addNotify() {
        super.addNotify();
        if (__autoFS_inited) return;
        __autoFS_inited = true;
        __installFullScreenAndScaling();
    }
    private void __installFullScreenAndScaling() {
        try {
            java.awt.Container root = getContentPane();
            java.awt.Component center = jPanel1;
            root.removeAll();
            root.setLayout(new java.awt.BorderLayout());
            root.add(center, java.awt.BorderLayout.CENTER);
            root.validate();
        } catch (Exception ignore) {}

        final java.awt.Dimension design = new java.awt.Dimension(
            Math.max(1, jPanel1.getPreferredSize().width > 0 ? jPanel1.getPreferredSize().width : 1366),
            Math.max(1, jPanel1.getPreferredSize().height > 0 ? jPanel1.getPreferredSize().height : 768)
        );

        __storeOriginalBoundsDirectChildren(jPanel1);
        jPanel1.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentResized(java.awt.event.ComponentEvent e) {
                __scaleDirectChildren(jPanel1, design);
            }
        });

        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowOpened(java.awt.event.WindowEvent e) {
                try {
                    setMinimumSize(new java.awt.Dimension(200, 200));
                    setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
                } catch (Exception ignore) {}
                try {
                    setResizable(true);
                    setExtendedState(getExtendedState() | javax.swing.JFrame.MAXIMIZED_BOTH);
                } catch (Exception ignore) {}
            }
        });
    }
    private void __storeOriginalBoundsDirectChildren(java.awt.Container root) {
        for (java.awt.Component child : root.getComponents()) {
            if (child instanceof javax.swing.JComponent) {
                ((javax.swing.JComponent) child).putClientProperty("origBounds", child.getBounds());
            }
        }
    }

    private void __scaleDirectChildren(java.awt.Container root, java.awt.Dimension design) {
        if (design.width <= 0 || design.height <= 0) return;
        double sx = root.getWidth()  / (double) design.width;
        double sy = root.getHeight() / (double) design.height;
        for (java.awt.Component child : root.getComponents()) {
            if (child instanceof javax.swing.JComponent) {
                Object prop = ((javax.swing.JComponent) child).getClientProperty("origBounds");
                if (prop instanceof java.awt.Rectangle) {
                    java.awt.Rectangle r = (java.awt.Rectangle) prop;
                    child.setBounds(
                        (int) Math.round(r.x * sx),
                        (int) Math.round(r.y * sy),
                        (int) Math.round(r.width * sx),
                        (int) Math.round(r.height * sy)
                    );
                }
            }
        }
        root.validate();
        root.repaint();
    }
    // ===== END auto full-screen helper =====

    // ===== IMPROVED ANALYSIS (with filters, KPIs, advanced charts) =====
    private void showAnalysisDialog() {
        javax.swing.JDialog dlg = new javax.swing.JDialog(this, "Analysis", true);
        dlg.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        dlg.setSize(1100, 720);
        dlg.setLocationRelativeTo(this);
        dlg.setLayout(new java.awt.BorderLayout());

        // Filters (top)
        javax.swing.JPanel north = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        javax.swing.JLabel lbFrom = new javax.swing.JLabel("From:");
        javax.swing.JLabel lbTo   = new javax.swing.JLabel("To:");
        final de.wannawork.jcalendar.JCalendarComboBox adFrom = new de.wannawork.jcalendar.JCalendarComboBox();
        final de.wannawork.jcalendar.JCalendarComboBox adTo   = new de.wannawork.jcalendar.JCalendarComboBox();
        adTo.setDate(new java.util.Date());
        adFrom.setDate(new java.util.Date(System.currentTimeMillis() - 90L*24*60*60*1000)); // last 90 days
        javax.swing.JButton btnRefresh = new javax.swing.JButton("Refresh");
        north.add(lbFrom); north.add(adFrom);
        north.add(lbTo);   north.add(adTo);
        north.add(btnRefresh);
        dlg.add(north, java.awt.BorderLayout.NORTH);

        // Tabs (center)
        final javax.swing.JTabbedPane tabs = new javax.swing.JTabbedPane();
        dlg.add(tabs, java.awt.BorderLayout.CENTER);

        // First load
        Runnable doRefresh = () -> {
            java.util.Date from = adFrom.getDate();
            java.util.Date to   = adTo.getDate();
            if (from == null || to == null) {
                javax.swing.JOptionPane.showMessageDialog(dlg, "Please select both From and To dates.", "Missing Dates", javax.swing.JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (from.after(to)) { java.util.Date t = from; from = to; to = t; adFrom.setDate(from); adTo.setDate(to); }
            updateAnalysisTabs(tabs, from, to);
        };
        btnRefresh.addActionListener(e -> doRefresh.run());
        doRefresh.run();

        // Bottom
        javax.swing.JPanel south = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        javax.swing.JButton btnClose = new javax.swing.JButton("Close");
        btnClose.addActionListener(e -> dlg.dispose());
        south.add(btnClose);
        dlg.add(south, java.awt.BorderLayout.SOUTH);

        dlg.setVisible(true);
    }

    private void updateAnalysisTabs(javax.swing.JTabbedPane tabs, java.util.Date from, java.util.Date to) {
        try {
            java.awt.Color theme = new java.awt.Color(102,102,255);

            javax.swing.JComponent overview     = wrapWithPadding(buildOverviewPanel(from, to));
            javax.swing.JComponent carsByStatus = wrapWithPadding(new SimplePieChartPanel("Cars by Status", fetchCarStatusCounts()));
            javax.swing.JComponent bookByStatus = wrapWithPadding(new SimpleBarChartPanel("Bookings by Status (in range)", fetchBookingStatusCounts(from, to), theme));
            javax.swing.JComponent rentMonthly  = wrapWithPadding(new SimpleBarChartPanel("Rentals per Month (in range)", fetchMonthlyCountsRange("rental_records", "rent_date", from, to), theme));
            javax.swing.JComponent bookMonthly  = wrapWithPadding(new SimpleBarChartPanel("Bookings per Month (in range)", fetchMonthlyCountsRange("bookings", "start_date", from, to), theme));
            javax.swing.JComponent revenueMonth = wrapWithPadding(new SimpleBarChartPanel("Revenue per Month (in range, k LKR)", fetchRevenueMonthlyK(from, to), theme, "k"));
            javax.swing.JComponent utilMonthly  = wrapWithPadding(new SimpleBarChartPanel("Utilization per Month (last 12, %)", fetchUtilizationMonthly(12), theme, "%"));
            javax.swing.JComponent leadTimeDist = wrapWithPadding(new SimpleBarChartPanel("Booking Lead Time (days)", fetchLeadTimeBuckets(from, to), theme));

            tabs.removeAll();
            tabs.addTab("Overview",             overview);
            tabs.addTab("Cars by Status",       carsByStatus);
            tabs.addTab("Bookings by Status",   bookByStatus);
            tabs.addTab("Rentals / Month",      rentMonthly);
            tabs.addTab("Bookings / Month",     bookMonthly);
            tabs.addTab("Revenue / Month",      revenueMonth);
            tabs.addTab("Utilization / Month",  utilMonthly);
            tabs.addTab("Lead Time",            leadTimeDist);

            tabs.revalidate(); tabs.repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, ex.getMessage(), "Analysis Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private javax.swing.JComponent buildOverviewPanel(java.util.Date from, java.util.Date to) throws Exception {
        int carsTotal      = fetchTotalCars();
        java.util.Map<String,Integer> carCounts = fetchCarStatusCounters();
        int carsAvail      = carCounts.getOrDefault("Available", 0);
        int carsRented     = carCounts.getOrDefault("Rented", 0);
        int carsMaint      = carCounts.getOrDefault("Maintenance", 0);
        int carsReserved   = carCounts.getOrDefault("Reserved", 0);
        int activeBookings = fetchActiveBookingsInRange(from, to);
        long revenueRange  = fetchRevenueInRange(from, to);
        int util30         = fetchUtilizationPercentLastNDays(30);

        javax.swing.JPanel panel = new javax.swing.JPanel(new java.awt.GridLayout(2, 3, 16, 16));
        panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(10,10,10,10));
        panel.setBackground(java.awt.Color.WHITE);

        panel.add(makeKpiCard("Cars (Total)", fmtInt(carsTotal), new java.awt.Color(0,102,204)));
        panel.add(makeKpiCard("Available",    fmtInt(carsAvail), new java.awt.Color(0,153,102)));
        panel.add(makeKpiCard("Rented Now",   fmtInt(carsRented), new java.awt.Color(255,102,0)));
        panel.add(makeKpiCard("Bookings (Range)", fmtInt(activeBookings), new java.awt.Color(102,102,255)));
        panel.add(makeKpiCard("Revenue (Range)", "LKR " + fmtInt(revenueRange), new java.awt.Color(0,0,153)));
        panel.add(makeKpiCard("Utilization (30d)", util30 + "%", new java.awt.Color(153,0,153)));

        javax.swing.JLabel foot = new javax.swing.JLabel(
            "Reserved: " + fmtInt(carsReserved) + "    Maintenance: " + fmtInt(carsMaint)
        );
        foot.setForeground(new java.awt.Color(80,80,80));
        javax.swing.JPanel wrap = new javax.swing.JPanel(new java.awt.BorderLayout());
        wrap.setBackground(java.awt.Color.WHITE);
        wrap.add(panel, java.awt.BorderLayout.CENTER);
        javax.swing.JPanel south = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));
        south.setBackground(java.awt.Color.WHITE);
        south.add(foot);
        wrap.add(south, java.awt.BorderLayout.SOUTH);

        return wrap;
    }

    private javax.swing.JComponent makeKpiCard(String title, String value, java.awt.Color color) {
        javax.swing.JPanel p = new javax.swing.JPanel();
        p.setBackground(java.awt.Color.WHITE);
        p.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(230,230,230)),
            javax.swing.BorderFactory.createEmptyBorder(14,14,14,14)
        ));
        p.setLayout(new java.awt.BorderLayout());
        javax.swing.JLabel v = new javax.swing.JLabel(value);
        v.setFont(v.getFont().deriveFont(java.awt.Font.BOLD, 26f));
        v.setForeground(color);
        javax.swing.JLabel t = new javax.swing.JLabel(title);
        t.setFont(t.getFont().deriveFont(java.awt.Font.PLAIN, 13f));
        t.setForeground(new java.awt.Color(60,60,60));
        p.add(v, java.awt.BorderLayout.CENTER);
        p.add(t, java.awt.BorderLayout.SOUTH);
        return p;
    }

    private javax.swing.JComponent wrapWithPadding(javax.swing.JComponent c) {
        javax.swing.JPanel p = new javax.swing.JPanel(new java.awt.BorderLayout());
        p.setBorder(javax.swing.BorderFactory.createEmptyBorder(10,10,10,10));
        p.add(c, java.awt.BorderLayout.CENTER);
        return p;
    }

    private String fmtInt(long v) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0");
        return df.format(v);
    }

    // ---- Data fetchers (enhanced) ----
    private java.util.LinkedHashMap<String, Integer> fetchCarStatusCounts() throws Exception {
        java.util.LinkedHashMap<String, Integer> map = new java.util.LinkedHashMap<>();
        String sql = "SELECT status_id, COUNT(*) cnt FROM car GROUP BY status_id ORDER BY status_id";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("status_id");
                String name = statusMap.getOrDefault(id, "Status #" + id);
                map.put(name, rs.getInt("cnt"));
            }
        }
        return map;
    }

    private java.util.Map<String,Integer> fetchCarStatusCounters() throws Exception {
        java.util.Map<String,Integer> m = new java.util.HashMap<>();
        String sql = "SELECT status_id, COUNT(*) cnt FROM car GROUP BY status_id";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("status_id");
                String name = statusMap.getOrDefault(id, "Status #" + id);
                m.put(name, rs.getInt("cnt"));
            }
        }
        if (!m.containsKey("Available")) m.put("Available", 0);
        if (!m.containsKey("Rented")) m.put("Rented", 0);
        if (!m.containsKey("Reserved")) m.put("Reserved", 0);
        if (!m.containsKey("Maintenance")) m.put("Maintenance", 0);
        return m;
    }

    private int fetchTotalCars() throws Exception {
        String sql = "SELECT COUNT(*) c FROM car";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private int fetchActiveBookingsInRange(java.util.Date from, java.util.Date to) throws Exception {
        String sql = "SELECT COUNT(*) c FROM bookings WHERE start_date <= ? AND end_date >= ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(to.getTime()));
            ps.setDate(2, new java.sql.Date(from.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    private long fetchRevenueInRange(java.util.Date from, java.util.Date to) throws Exception {
        String sql = "SELECT COALESCE(SUM(total_fee),0) s FROM rental_records WHERE rent_date BETWEEN ? AND ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(from.getTime()));
            ps.setDate(2, new java.sql.Date(to.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0L;
            }
        }
    }

    private int fetchUtilizationPercentLastNDays(int days) throws Exception {
        int totalCars = Math.max(1, fetchTotalCars());
        java.util.Date to = new java.util.Date();
        java.util.Date from = new java.util.Date(System.currentTimeMillis() - (long)(days - 1) * 24L*60L*60L*1000L);
        long usedDays = sumUsedDays(from, to);
        long denom = (long) totalCars * days;
        if (denom <= 0) return 0;
        long pct = Math.round((usedDays * 100.0) / denom);
        if (pct < 0) pct = 0;
        if (pct > 100) pct = 100;
        return (int)pct;
    }

    private long sumUsedDays(java.util.Date from, java.util.Date to) throws Exception {
        // Sum of overlapped days across all rentals in range [from,to], handle open rentals (NULL return_date)
        String sql = "SELECT SUM(DATEDIFF(LEAST(?, COALESCE(return_date, CURRENT_DATE())), GREATEST(?, rent_date)) + 1) AS days " +
                     "FROM rental_records WHERE rent_date <= ? AND COALESCE(return_date, CURRENT_DATE()) >= ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            java.sql.Date f = new java.sql.Date(from.getTime());
            java.sql.Date t = new java.sql.Date(to.getTime());
            ps.setDate(1, t);
            ps.setDate(2, f);
            ps.setDate(3, t);
            ps.setDate(4, f);
            try (ResultSet rs = ps.executeQuery()) {
                long v = 0;
                if (rs.next()) v = rs.getLong(1);
                return Math.max(0, v);
            }
        }
    }

    private java.util.LinkedHashMap<String, Integer> fetchBookingStatusCounts(java.util.Date from, java.util.Date to) throws Exception {
        java.util.LinkedHashMap<String, Integer> map = new java.util.LinkedHashMap<>();
        map.put("Booked", 0); map.put("Collected", 0); map.put("Completed", 0); map.put("Cancelled", 0);
        String sql = "SELECT status, COUNT(*) cnt FROM bookings WHERE start_date <= ? AND end_date >= ? GROUP BY status";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(to.getTime()));
            ps.setDate(2, new java.sql.Date(from.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) map.put(rs.getString("status"), rs.getInt("cnt"));
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return map;
    }

    private java.util.LinkedHashMap<String, Integer> fetchMonthlyCountsRange(String table, String dateCol, java.util.Date from, java.util.Date to) throws Exception {
        java.time.YearMonth start = java.time.YearMonth.from(
            java.time.LocalDate.ofInstant(from.toInstant(), java.time.ZoneId.systemDefault()).withDayOfMonth(1)
        );
        java.time.YearMonth end = java.time.YearMonth.from(
            java.time.LocalDate.ofInstant(to.toInstant(), java.time.ZoneId.systemDefault()).withDayOfMonth(1)
        );
        java.util.LinkedHashMap<String, Integer> map = new java.util.LinkedHashMap<>();
        java.time.YearMonth ym = start;
        while (!ym.isAfter(end)) {
            map.put(ym.toString(), 0);
            ym = ym.plusMonths(1);
        }

        String sql = "SELECT DATE_FORMAT(" + dateCol + ", '%Y-%m') ym, COUNT(*) cnt " +
                     "FROM " + table + " WHERE " + dateCol + " BETWEEN ? AND ? GROUP BY ym ORDER BY ym";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(from.getTime()));
            ps.setDate(2, new java.sql.Date(to.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String yms = rs.getString("ym");
                    int cnt = rs.getInt("cnt");
                    if (map.containsKey(yms)) map.put(yms, cnt);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return map;
    }

    private java.util.LinkedHashMap<String, Integer> fetchRevenueMonthlyK(java.util.Date from, java.util.Date to) throws Exception {
        // Revenue per month in thousands of LKR
        java.time.YearMonth start = java.time.YearMonth.from(
            java.time.LocalDate.ofInstant(from.toInstant(), java.time.ZoneId.systemDefault()).withDayOfMonth(1)
        );
        java.time.YearMonth end = java.time.YearMonth.from(
            java.time.LocalDate.ofInstant(to.toInstant(), java.time.ZoneId.systemDefault()).withDayOfMonth(1)
        );
        java.util.LinkedHashMap<String, Integer> map = new java.util.LinkedHashMap<>();
        java.time.YearMonth ym = start;
        while (!ym.isAfter(end)) {
            map.put(ym.toString(), 0);
            ym = ym.plusMonths(1);
        }

        String sql = "SELECT DATE_FORMAT(rent_date, '%Y-%m') ym, COALESCE(SUM(total_fee),0) fee " +
                     "FROM rental_records WHERE rent_date BETWEEN ? AND ? GROUP BY ym ORDER BY ym";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(from.getTime()));
            ps.setDate(2, new java.sql.Date(to.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String yms = rs.getString("ym");
                    double fee = rs.getDouble("fee");
                    int k = (int)Math.round(fee / 1000.0);
                    if (map.containsKey(yms)) map.put(yms, k);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return map;
    }

    private java.util.LinkedHashMap<String, Integer> fetchUtilizationMonthly(int months) throws Exception {
        java.util.LinkedHashMap<String, Integer> map = new java.util.LinkedHashMap<>();
        int totalCars = Math.max(1, fetchTotalCars());
        java.time.YearMonth now = java.time.YearMonth.now();
        for (int i = months - 1; i >= 0; i--) {
            java.time.YearMonth ym = now.minusMonths(i);
            java.time.LocalDate start = ym.atDay(1);
            java.time.LocalDate end   = ym.atEndOfMonth();
            java.util.Date f = java.sql.Date.valueOf(start);
            java.util.Date t = java.sql.Date.valueOf(end);
            long usedDays = sumUsedDays(f, t);
            int daysInMonth = end.getDayOfMonth();
            int pct = (int)Math.round((usedDays * 100.0) / (Math.max(1, totalCars) * daysInMonth));
            if (pct < 0) pct = 0; if (pct > 100) pct = 100;
            map.put(ym.toString(), pct);
        }
        return map;
    }

    private java.util.LinkedHashMap<String, Integer> fetchLeadTimeBuckets(java.util.Date from, java.util.Date to) throws Exception {
        // Buckets: 0-1d, 2-3d, 4-7d, 8-14d, 15-30d, 31+d
        java.util.LinkedHashMap<String, Integer> map = new java.util.LinkedHashMap<>();
        String[] keys = {"0-1d","2-3d","4-7d","8-14d","15-30d","31+d"};
        for (String k : keys) map.put(k, 0);

        String sql = "SELECT DATEDIFF(start_date, DATE(created_at)) AS diff FROM bookings WHERE start_date BETWEEN ? AND ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(from.getTime()));
            ps.setDate(2, new java.sql.Date(to.getTime()));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int d = Math.max(0, rs.getInt("diff")); // clamp negatives to 0
                    String bucket;
                    if (d <= 1) bucket = "0-1d";
                    else if (d <= 3) bucket = "2-3d";
                    else if (d <= 7) bucket = "4-7d";
                    else if (d <= 14) bucket = "8-14d";
                    else if (d <= 30) bucket = "15-30d";
                    else bucket = "31+d";
                    map.put(bucket, map.get(bucket) + 1);
                }
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
        return map;
    }

    // --- Simple charts (Java2D, no external libs) ---
    private static class SimpleBarChartPanel extends javax.swing.JPanel {
        private final String title;
        private final java.util.LinkedHashMap<String, Integer> data;
        private final java.awt.Color barColor;
        private final String valueSuffix;

        SimpleBarChartPanel(String title, java.util.LinkedHashMap<String, Integer> data, java.awt.Color barColor) {
            this(title, data, barColor, "");
        }
        SimpleBarChartPanel(String title, java.util.LinkedHashMap<String, Integer> data, java.awt.Color barColor, String valueSuffix) {
            this.title = title;
            this.data = data != null ? data : new java.util.LinkedHashMap<>();
            this.barColor = barColor != null ? barColor : new java.awt.Color(102,102,255);
            this.valueSuffix = valueSuffix == null ? "" : valueSuffix;
            setPreferredSize(new java.awt.Dimension(980, 540));
            setBackground(java.awt.Color.WHITE);
        }

        @Override protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int top = 60, bottom = 70, left = 80, right = 30;

            // Title
            g2.setColor(new java.awt.Color(0,0,51));
            java.awt.Font tf = getFont().deriveFont(java.awt.Font.BOLD, 18f);
            g2.setFont(tf);
            java.awt.FontMetrics tm = g2.getFontMetrics();
            g2.drawString(title, Math.max(10, (w - tm.stringWidth(title))/2), top - 25);

            // Data
            int n = data.size();
            int maxVal = 0;
            for (Integer v : data.values()) maxVal = Math.max(maxVal, v == null ? 0 : v);
            if (maxVal == 0) maxVal = 1;

            // Axes
            g2.setColor(new java.awt.Color(180,180,180));
            g2.drawLine(left, h-bottom, w-right, h-bottom); // x
            g2.drawLine(left, top, left, h-bottom);         // y

            // Plot area
            int plotW = w - left - right;
            int plotH = h - top - bottom;
            int gap = Math.max(8, (int)Math.round(plotW * 0.01));
            int barW = n > 0 ? Math.max(10, (plotW - (gap*(n+1))) / n) : 10;

            // y ticks (5)
            g2.setFont(getFont().deriveFont(12f));
            g2.setColor(new java.awt.Color(120,120,120));
            for (int i=0;i<=5;i++) {
                double ratio = i/5.0;
                int y = (int)(h - bottom - ratio*plotH);
                g2.drawLine(left-5, y, w-right, y);
                int val = (int)Math.round(ratio*maxVal);
                String s = String.valueOf(val);
                int sw = g2.getFontMetrics().stringWidth(s);
                g2.drawString(s, left - 10 - sw, y + 4);
            }

            // Bars
            int x = left + gap;
            g2.setFont(getFont().deriveFont(12f));
            for (java.util.Map.Entry<String,Integer> e : data.entrySet()) {
                int v = e.getValue() == null ? 0 : e.getValue();
                int barH = (int)Math.round((v/(double)maxVal) * plotH);
                int y = h - bottom - barH;

                // bar
                g2.setColor(barColor);
                g2.fillRoundRect(x, y, barW, barH, 6,6);
                g2.setColor(barColor.darker());
                g2.drawRoundRect(x, y, barW, barH, 6,6);

                // value label
                String vs = String.valueOf(v) + valueSuffix;
                int vsw = g2.getFontMetrics().stringWidth(vs);
                g2.setColor(new java.awt.Color(0,0,0));
                g2.drawString(vs, x + (barW - vsw)/2, y - 4);

                // x label (wrap)
                String lab = e.getKey();
                g2.setColor(new java.awt.Color(60,60,60));
                drawWrappedCentered(g2, lab, x, h-bottom+6, barW, 38, 12f);

                x += barW + gap;
            }
            g2.dispose();
        }

        private void drawWrappedCentered(java.awt.Graphics2D g2, String text, int x, int y, int width, int maxH, float fontSize) {
            if (text == null) return;
            java.util.List<String> lines = new java.util.ArrayList<>();
            String[] parts = text.split("\\s+|_");
            String line = "";
            java.awt.Font f = getFont().deriveFont(fontSize);
            java.awt.FontMetrics fm = g2.getFontMetrics(f);
            for (String p : parts) {
                String t = line.isEmpty() ? p : line + " " + p;
                if (fm.stringWidth(t) <= width) line = t;
                else {
                    if (!line.isEmpty()) lines.add(line);
                    line = p;
                }
            }
            if (!line.isEmpty()) lines.add(line);
            int lh = fm.getHeight();
            int total = Math.min(lines.size(), Math.max(1, maxH / lh));
            int yy = y + lh;
            g2.setFont(f);
            for (int i=0; i<total; i++) {
                String s = lines.get(i);
                int sw = fm.stringWidth(s);
                g2.drawString(s, x + (width - sw)/2, yy);
                yy += lh;
            }
        }
    }

    private static class SimplePieChartPanel extends javax.swing.JPanel {
        private final String title;
        private final java.util.LinkedHashMap<String, Integer> data;
        private final java.awt.Color[] palette = new java.awt.Color[] {
            new java.awt.Color(102,102,255), new java.awt.Color(0,153,204), new java.awt.Color(0,204,153),
            new java.awt.Color(255,153,0), new java.awt.Color(220,20,60), new java.awt.Color(128,0,128)
        };

        SimplePieChartPanel(String title, java.util.LinkedHashMap<String, Integer> data) {
            this.title = title;
            this.data = data != null ? data : new java.util.LinkedHashMap<>();
            setPreferredSize(new java.awt.Dimension(980, 540));
            setBackground(java.awt.Color.WHITE);
        }

        @Override protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING, java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int top = 60, left = 20, right = 20, bottom = 20;
            int legendW = 300;

            int maxPieSize = Math.min(w - left - right - legendW - 20, h - top - bottom);
            int size = Math.max(200, maxPieSize);

            // Title
            g2.setColor(new java.awt.Color(0,0,51));
            g2.setFont(getFont().deriveFont(java.awt.Font.BOLD, 18f));
            java.awt.FontMetrics tm = g2.getFontMetrics();
            g2.drawString(title, Math.max(10, (w - tm.stringWidth(title))/2), top - 25);

            // total
            double sum = 0;
            for (Integer v : data.values()) sum += (v == null ? 0 : v);
            if (sum <= 0) sum = 1;

            // pie
            int pieX = left + 20;
            int pieY = top;
            int start = 0;
            int i = 0;

            for (java.util.Map.Entry<String,Integer> e : data.entrySet()) {
                double val = e.getValue() == null ? 0 : e.getValue();
                int angle = (int)Math.round(360.0 * (val / sum));
                g2.setColor(palette[i % palette.length]);
                g2.fillArc(pieX, pieY, size, size, start, angle);
                g2.setColor(java.awt.Color.WHITE);
                g2.drawArc(pieX, pieY, size, size, start, angle);
                start += angle;
                i++;
            }

            // Legend
            int lx = left + size + 40;
            int ly = top + 10;
            g2.setFont(getFont().deriveFont(13f));
            java.text.DecimalFormat pct = new java.text.DecimalFormat("0.0");
            i = 0;
            for (java.util.Map.Entry<String,Integer> e : data.entrySet()) {
                double val = e.getValue() == null ? 0 : e.getValue();
                double p = (100.0 * val) / sum;
                g2.setColor(palette[i % palette.length]);
                g2.fillRoundRect(lx, ly, 16, 16, 4,4);
                g2.setColor(java.awt.Color.DARK_GRAY);
                String text = e.getKey() + " — " + (int)val + " (" + pct.format(p) + "%)";
                g2.drawString(text, lx + 24, ly + 13);
                ly += 26;
                i++;
            }

            g2.dispose();
        }
    }
    // ===== END IMPROVED ANALYSIS =====

    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) { UIManager.setLookAndFeel(info.getClassName()); break; }
            }
        } catch (Exception ignore) {}
        java.awt.EventQueue.invokeLater(() -> new Report01().setVisible(true));
    }

    // ===== Variables declaration =====
    private javax.swing.JButton DeleteBtn;
    private javax.swing.JButton ExportBtn;
    private javax.swing.JButton GenerateBtn;
    private javax.swing.JButton ResetBtn;
    private javax.swing.JButton AnalysisBtn; // NEW

    private javax.swing.JButton jButtonAvailability;
    private javax.swing.JButton jButtonCustomers;
    private javax.swing.JButton jButtonRent;
    private javax.swing.JButton jButtonReturn;
    private javax.swing.JButton jButtonCars;
    private javax.swing.JButton jButtonReports;

    private javax.swing.JComboBox<String> jComboBox1;

    private javax.swing.JLabel jLabelTitle;
    private javax.swing.JLabel jLabelReport;
    private javax.swing.JLabel jLabelFrom;
    private javax.swing.JLabel jLabelTo;
    private javax.swing.JLabel jLabelResults;

    private javax.swing.JLabel jLabelLogo;
    private javax.swing.JLabel jLabelAccount;
    private javax.swing.JLabel jLabelLogout;

    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable resultsTable;
    private de.wannawork.jcalendar.JCalendarComboBox dateFromPicker;
    private de.wannawork.jcalendar.JCalendarComboBox dateToPicker;
    // ===== End of variables declaration =====
}
/*
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;




public class Report01 extends javax.swing.JFrame {
    
   // --- IMPORTANT: CONFIGURE YOUR DATABASE CONNECTION DETAILS HERE FOR MYSQL ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/customer_add"; // Your customer_add DB
    private static final String DB_USER = "root"; // Your MySQL username
    private static final String DB_PASSWORD = ""; // Your MySQL password (empty string if no password)

    
    public Report01() {
        initComponents();
       
  /*   
       
    }
    /* === AUTO FULL-SCREEN + RESPONSIVE SCALING (paste inside Cars class) === */


    @SuppressWarnings("unchecked")
  /*
   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        ModifyTb = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        DeleteBtn = new javax.swing.JButton();
        ResetBtn = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1300, 750));

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1366, 768));
        jPanel1.setLayout(null);

        jPanel2.setBackground(new java.awt.Color(0, 0, 51));
        jPanel2.setPreferredSize(new java.awt.Dimension(400, 768));

        jButton1.setBackground(new java.awt.Color(102, 102, 255));
        jButton1.setFont(jButton1.getFont().deriveFont(jButton1.getFont().getStyle() | java.awt.Font.BOLD, jButton1.getFont().getSize()+8));
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("AVAILABILITY");
        jButton1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(102, 102, 255));
        jButton2.setFont(jButton2.getFont().deriveFont(jButton2.getFont().getStyle() | java.awt.Font.BOLD, jButton2.getFont().getSize()+8));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setText("RENT");
        jButton2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setBackground(new java.awt.Color(102, 102, 255));
        jButton3.setFont(jButton3.getFont().deriveFont(jButton3.getFont().getStyle() | java.awt.Font.BOLD, jButton3.getFont().getSize()+8));
        jButton3.setForeground(new java.awt.Color(255, 255, 255));
        jButton3.setText("CUSTOMERS");
        jButton3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        ModifyTb.setBackground(new java.awt.Color(102, 102, 255));
        ModifyTb.setFont(ModifyTb.getFont().deriveFont(ModifyTb.getFont().getStyle() | java.awt.Font.BOLD, ModifyTb.getFont().getSize()+8));
        ModifyTb.setForeground(new java.awt.Color(255, 255, 255));
        ModifyTb.setText("CARS");
        ModifyTb.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        ModifyTb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModifyTbActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(230, 0, 52));
        jLabel2.setText("LOGOUT");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ACCOUNT");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImageR/Side Bar Logo.png"))); // NOI18N

        jLabel9.setFont(jLabel9.getFont().deriveFont(jLabel9.getFont().getStyle() | java.awt.Font.BOLD, jLabel9.getFont().getSize()+6));
        jLabel9.setForeground(new java.awt.Color(102, 102, 255));
        jLabel9.setText("ACCOUNT");

        jLabel10.setFont(jLabel10.getFont().deriveFont(jLabel10.getFont().getStyle() | java.awt.Font.BOLD, jLabel10.getFont().getSize()+6));
        jLabel10.setForeground(new java.awt.Color(204, 204, 204));
        jLabel10.setText("LOGOUT");

        jButton11.setBackground(new java.awt.Color(102, 102, 255));
        jButton11.setFont(jButton11.getFont().deriveFont(jButton11.getFont().getStyle() | java.awt.Font.BOLD, jButton11.getFont().getSize()+8));
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("RETURN");
        jButton11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel9)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel10))))
                        .addGap(99, 99, 99))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ModifyTb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ModifyTb, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 686, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(57, 57, 57))
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 290, 1515);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Reports");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(810, 0, 150, 32);

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() | java.awt.Font.BOLD, jLabel5.getFont().getSize()+6));
        jLabel5.setForeground(new java.awt.Color(0, 0, 51));
        jLabel5.setText("From");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(330, 130, 230, 25);

        jButton7.setBackground(new java.awt.Color(0, 0, 51));
        jButton7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(230, 0, 52));
        jButton7.setText("Reset");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton7);
        jButton7.setBounds(793, 1451, 145, 58);

        jLabel6.setFont(jLabel6.getFont().deriveFont(jLabel6.getFont().getStyle() | java.awt.Font.BOLD, jLabel6.getFont().getSize()+6));
        jLabel6.setForeground(new java.awt.Color(0, 0, 51));
        jLabel6.setText("Report");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(330, 90, 230, 30);

        jLabel7.setFont(jLabel7.getFont().deriveFont(jLabel7.getFont().getStyle() | java.awt.Font.BOLD, jLabel7.getFont().getSize()+6));
        jLabel7.setForeground(new java.awt.Color(0, 0, 51));
        jLabel7.setText("To");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(330, 170, 230, 25);

        DeleteBtn.setBackground(new java.awt.Color(0, 0, 153));
        DeleteBtn.setFont(DeleteBtn.getFont().deriveFont(DeleteBtn.getFont().getStyle() | java.awt.Font.BOLD, DeleteBtn.getFont().getSize()+6));
        DeleteBtn.setForeground(new java.awt.Color(255, 255, 255));
        DeleteBtn.setText("PRINT");
        DeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteBtnActionPerformed(evt);
            }
        });
        jPanel1.add(DeleteBtn);
        DeleteBtn.setBounds(1060, 149, 145, 60);

        ResetBtn.setBackground(new java.awt.Color(0, 0, 51));
        ResetBtn.setFont(ResetBtn.getFont().deriveFont(ResetBtn.getFont().getStyle() | java.awt.Font.BOLD, ResetBtn.getFont().getSize()+6));
        ResetBtn.setForeground(new java.awt.Color(255, 255, 255));
        ResetBtn.setText("RESET");
        ResetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ResetBtnActionPerformed(evt);
            }
        });
        jPanel1.add(ResetBtn);
        ResetBtn.setBounds(1060, 90, 145, 39);

        jPanel1.add(jComboBox1);
        jComboBox1.setBounds(630, 90, 230, 30);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ResetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetBtnActionPerformed
        // TODO add your handling code here:
   

    }//GEN-LAST:event_ResetBtnActionPerformed

    private void DeleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteBtnActionPerformed
        // TODO add your handling code here:
     
    }//GEN-LAST:event_DeleteBtnActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void ModifyTbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifyTbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ModifyTbActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed
*/
   
/*
    /**
     * @param args the command line arguments
     */
  /*  public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Report01.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Report01.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Report01.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Report01.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
 /*       java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Report01().setVisible(true);
            }
        });
    }*/
/*
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DeleteBtn;
    private javax.swing.JButton ModifyTb;
    private javax.swing.JButton ResetBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton7;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
*/