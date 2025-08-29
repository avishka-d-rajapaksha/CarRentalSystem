package Car;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;


public class Return extends javax.swing.JFrame {

    PreparedStatement insert;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    int dailyRate = 0;
    
    
    
    public Return() {
        initComponents();
        startTotalAutoUpdate();
        installSidebarUniversal();
        loadVehicleNumbersToCombo();
        loadOnRentTable();
        calculateDelayAndTotalFee();
        loadReturnedCarsTable();
        
        
        actualReturnDateChooser.addPropertyChangeListener("date", new java.beans.PropertyChangeListener() {
    public void propertyChange(java.beans.PropertyChangeEvent evt) {
        calculateDelayAndTotalFee();
    }
});
     
editBtn.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent evt) {
        editReturnedCar();
    }
});

deleteBtn.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent evt) {
        deleteReturnedCar();
    }
});

resetBtn.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent evt) {
        resetFields();
    }
});
        
    }
    
    // ===== UNIVERSAL SIDEBAR — PASTE THIS INSIDE EACH JFrame CLASS (above the last }) =====
// After pasting, call installSidebarUniversal(); right after initComponents() in your constructor.

private void installSidebarUniversal() {
    try { __sb_installSidebar(); } catch (Throwable t) { t.printStackTrace(); }
}

// === DROP-IN REPLACEMENT FOR YOUR __sb_installSidebar() + HELPERS ===
private void __sb_installSidebar() {
    javax.swing.JPanel sidebar = __sb_findSidebarPanel();
    if (sidebar == null) return;

    final java.awt.Color PANEL_BG = new java.awt.Color(0,0,51);
    sidebar.setBackground(PANEL_BG);

    javax.swing.JButton template = __sb_findAnyNavButton(sidebar);

    javax.swing.JButton btnAvail  = __sb_ensureButton(sidebar, "AVAILABILITY", template);
    javax.swing.JButton btnCust   = __sb_ensureButton(sidebar, "CUSTOMERS",   template);
    javax.swing.JButton btnRent   = __sb_ensureButton(sidebar, "RENT",        template);
    javax.swing.JButton btnReturn = __sb_ensureButton(sidebar, "RETURN",      template);
    javax.swing.JButton btnCars   = __sb_ensureButton(sidebar, "CARS",        template);
    javax.swing.JButton btnReport = __sb_ensureButton(sidebar, "REPORTS",     template); // under CARS

    javax.swing.JLabel logo       = __sb_findLogo(sidebar);
    javax.swing.JLabel lblAccount = __sb_ensureLabel(sidebar, "ACCOUNT");
    javax.swing.JLabel lblLogout  = __sb_ensureLabel(sidebar, "LOGOUT");

    // Normalize & force visible
    lblAccount.setText("ACCOUNT");
    lblLogout.setText("LOGOUT");
    lblAccount.setVisible(true);
    lblLogout.setVisible(true);

    // Remove any duplicates from GUI builder (e.g., "LOG OUT")
    __sb_removeDupLabelsNormalized(sidebar, "ACCOUNT", lblAccount);
    __sb_removeDupLabelsNormalized(sidebar, "LOGOUT",  lblLogout);

    // Wire actions
    __sb_rewire(btnAvail,  new Runnable(){ public void run(){ __sb_open("Availability","Available","AvailabilityForm"); }});
    __sb_rewire(btnCust,   new Runnable(){ public void run(){ __sb_open("Customers","Customer","CustomerList"); }});
    __sb_rewire(btnRent,   new Runnable(){ public void run(){ __sb_open("Rental","Rent","RentCar","Rentals"); }});
    __sb_rewire(btnReturn, new Runnable(){ public void run(){ __sb_open("Return","Returns","ReturnCar","CarReturn"); }});
    __sb_rewire(btnCars,   new Runnable(){ public void run(){ __sb_open("Cars","Car","CarList","ModifyTb"); }});
    __sb_rewire(btnReport, new Runnable(){ public void run(){ __sb_open("Report01","Reports","Report","Report1","Report_01"); }});

    __sb_installLabelAction(lblAccount, new Runnable(){ public void run(){ __sb_open("Account","UserAccount","Profile"); }});
    __sb_installLabelAction(lblLogout,  new Runnable(){ public void run(){
        int opt = javax.swing.JOptionPane.showConfirmDialog(
            (java.awt.Component)null, "Do you want to logout?", "Logout", javax.swing.JOptionPane.YES_NO_OPTION
        );
        if (opt == javax.swing.JOptionPane.YES_OPTION) __sb_open("Login","SignIn","Sign_In");
    }});

    // Active page
    String active = __sb_detectActive();
    __sb_setActive(btnAvail,  "AVAILABILITY".equals(active));
    __sb_setActive(btnCust,   "CUSTOMERS".equals(active));
    __sb_setActive(btnRent,   "RENT".equals(active));
    __sb_setActive(btnReturn, "RETURN".equals(active));
    __sb_setActive(btnCars,   "CARS".equals(active));
    __sb_setActive(btnReport, "REPORTS".equals(active));

    // Rebuild layout — keep labels near buttons (not off-screen)
    javax.swing.GroupLayout gl = new javax.swing.GroupLayout(sidebar);
    sidebar.setLayout(gl);

    javax.swing.GroupLayout.ParallelGroup h = gl.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addComponent(__sb_ensureInPanel(sidebar, logo), 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(__sb_ensureInPanel(sidebar, btnAvail), 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(__sb_ensureInPanel(sidebar, btnCust), 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(__sb_ensureInPanel(sidebar, btnRent), 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(__sb_ensureInPanel(sidebar, btnReturn), 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(__sb_ensureInPanel(sidebar, btnCars), 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(__sb_ensureInPanel(sidebar, btnReport), 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(__sb_ensureInPanel(sidebar, lblAccount), 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        .addComponent(__sb_ensureInPanel(sidebar, lblLogout), 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE);

    gl.setHorizontalGroup(gl.createSequentialGroup().addContainerGap().addGroup(h).addContainerGap());

    javax.swing.GroupLayout.SequentialGroup v = gl.createSequentialGroup().addContainerGap();
    if (logo != null) v.addComponent(logo, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE);
    v.addGap(46);
    __sb_vAdd(v, btnAvail); __sb_vAdd(v, btnCust); __sb_vAdd(v, btnRent); __sb_vAdd(v, btnReturn); __sb_vAdd(v, btnCars); __sb_vAdd(v, btnReport);
    // Keep labels visible just under the buttons
    v.addGap(20);
    v.addComponent(lblAccount).addGap(8);
    v.addComponent(lblLogout).addGap(20);

    gl.setVerticalGroup(gl.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(v));

    sidebar.revalidate();
    sidebar.repaint();
}

// Remove duplicate labels by text (treats "LOG OUT" == "LOGOUT")
private void __sb_removeDupLabelsNormalized(java.awt.Container root, String text, javax.swing.JLabel keep) {
    String target = __sb_norm(text);
    java.util.List<java.awt.Component> toRemove = new java.util.ArrayList<>();
    java.util.List<java.awt.Container> parents = new java.util.ArrayList<>();
    __sb_collectDupLabels(root, target, keep, toRemove, parents);
    for (int i = 0; i < toRemove.size(); i++) {
        java.awt.Container p = parents.get(i);
        if (p != null) p.remove(toRemove.get(i));
    }
    root.revalidate();
    root.repaint();
}
private String __sb_norm(String s) {
    return (s == null) ? "" : s.replaceAll("\\s+", "").trim().toUpperCase();
}
private void __sb_collectDupLabels(java.awt.Container root, String normalizedTarget, javax.swing.JLabel keep,
                                   java.util.List<java.awt.Component> out, java.util.List<java.awt.Container> parents) {
    if (root == null) return;
    for (java.awt.Component c : root.getComponents()) {
        if (c instanceof javax.swing.JLabel) {
            javax.swing.JLabel l = (javax.swing.JLabel) c;
            if (l != keep && __sb_norm(l.getText()).equals(normalizedTarget)) {
                out.add(l);
                parents.add(root);
            }
        }
        if (c instanceof java.awt.Container) {
            __sb_collectDupLabels((java.awt.Container) c, normalizedTarget, keep, out, parents);
        }
    }
}
// ---- helpers (do not modify) ----
private void __sb_vAdd(javax.swing.GroupLayout.SequentialGroup v, java.awt.Component c) {
    if (c != null) {
        v.addComponent(c, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE);
        v.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
    }
}

private javax.swing.JButton __sb_findAnyNavButton(java.awt.Container p) {
    for (String s : new String[]{"CARS","AVAILABILITY","CUSTOMERS","RENT","RETURN","REPORTS"}) {
        javax.swing.JButton b = __sb_findButton(p, s);
        if (b != null) return b;
    }
    return null;
}

private javax.swing.JButton __sb_ensureButton(javax.swing.JPanel p, String text, javax.swing.JButton template) {
    javax.swing.JButton b = __sb_findButton(p, text);
    if (b == null) {
        b = new javax.swing.JButton(text);
        __sb_applyButtonStyle(b, template);
        __sb_ensureInPanel(p, b);
    } else {
        __sb_applyButtonStyle(b, template); // normalize style
    }
    return b;
}

private void __sb_applyButtonStyle(javax.swing.JButton b, javax.swing.JButton template) {
    final java.awt.Color BTN_BG = new java.awt.Color(102,102,255);
    try {
        if (template != null) {
            b.setBackground(template.getBackground());
            b.setForeground(template.getForeground());
            b.setFont(template.getFont());
            b.setBorder(template.getBorder());
            b.setCursor(template.getCursor());
            b.setFocusPainted(false);
            b.setOpaque(true);
            try { b.setPreferredSize(template.getPreferredSize()); } catch (Exception ignore) {}
            try { b.setMaximumSize(template.getMaximumSize()); } catch (Exception ignore) {}
            try { b.setMinimumSize(template.getMinimumSize()); } catch (Exception ignore) {}
        } else {
            b.setBackground(BTN_BG);
            b.setForeground(java.awt.Color.WHITE);
            java.awt.Font f = b.getFont();
            b.setFont(f.deriveFont(java.awt.Font.BOLD, f.getSize()+8f));
            b.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            b.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
            b.setPreferredSize(new java.awt.Dimension(260, 67));
        }
    } catch (Exception ignore) {}
}

private javax.swing.JLabel __sb_ensureLabel(javax.swing.JPanel p, String text) {
    javax.swing.JLabel l = __sb_findLabel(p, text);
    if (l == null) {
        l = new javax.swing.JLabel(text, javax.swing.SwingConstants.CENTER);
        l.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 18));
        l.setForeground(java.awt.Color.WHITE);
        __sb_ensureInPanel(p, l);
    } else {
        l.setFont(l.getFont().deriveFont(java.awt.Font.BOLD, 18f));
        l.setForeground(java.awt.Color.WHITE);
        l.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    }
    return l;
}

private javax.swing.JButton __sb_findButton(java.awt.Container root, String text) {
    if (root == null || text == null) return null;
    String target = text.trim().toUpperCase();
    for (java.awt.Component c : root.getComponents()) {
        if (c instanceof javax.swing.JButton) {
            String t = ((javax.swing.JButton) c).getText();
            if (t != null && t.trim().toUpperCase().equals(target)) return (javax.swing.JButton) c;
        } else if (c instanceof java.awt.Container) {
            javax.swing.JButton r = __sb_findButton((java.awt.Container) c, text);
            if (r != null) return r;
        }
    }
    return null;
}

private javax.swing.JLabel __sb_findLabel(java.awt.Container root, String text) {
    if (root == null || text == null) return null;
    String target = text.trim().toUpperCase();
    for (java.awt.Component c : root.getComponents()) {
        if (c instanceof javax.swing.JLabel) {
            String t = ((javax.swing.JLabel) c).getText();
            if (t != null && t.trim().toUpperCase().equals(target)) return (javax.swing.JLabel) c;
        } else if (c instanceof java.awt.Container) {
            javax.swing.JLabel r = __sb_findLabel((java.awt.Container) c, text);
            if (r != null) return r;
        }
    }
    return null;
}

private javax.swing.JLabel __sb_findLogo(java.awt.Container root) {
    if (root == null) return null;
    for (java.awt.Component c : root.getComponents()) {
        if (c instanceof javax.swing.JLabel) {
            javax.swing.JLabel l = (javax.swing.JLabel) c;
            if (l.getIcon() != null) return l;
        } else if (c instanceof java.awt.Container) {
            javax.swing.JLabel r = __sb_findLogo((java.awt.Container) c);
            if (r != null) return r;
        }
    }
    return null;
}

private <T extends java.awt.Component> T __sb_ensureInPanel(javax.swing.JPanel p, T c) {
    if (c != null && c.getParent() != p) p.add(c);
    return c;
}

private void __sb_rewire(javax.swing.JButton b, final Runnable r) {
    if (b == null || r == null) return;
    for (java.awt.event.ActionListener al : b.getActionListeners()) b.removeActionListener(al);
    b.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
            try { r.run(); } catch (Throwable t) { t.printStackTrace(); }
        }
    });
}

private void __sb_installLabelAction(javax.swing.JLabel l, final Runnable r) {
    if (l == null || r == null) return;
    for (java.awt.event.MouseListener ml : l.getMouseListeners()) l.removeMouseListener(ml);
    l.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
    l.addMouseListener(new java.awt.event.MouseAdapter() {
        public void mouseClicked(java.awt.event.MouseEvent e) { r.run(); }
    });
}

private void __sb_setActive(javax.swing.JButton b, boolean active) {
    if (b == null) return;
    if (active) {
        b.setEnabled(false);
        java.awt.Color base = b.getBackground(); 
        if (base == null) base = new java.awt.Color(102,102,255);
        java.awt.Color target = new java.awt.Color(204,204,255);
        b.setBackground(new java.awt.Color(
            (int)Math.round(base.getRed()*0.4 + target.getRed()*0.6),
            (int)Math.round(base.getGreen()*0.4 + target.getGreen()*0.6),
            (int)Math.round(base.getBlue()*0.4 + target.getBlue()*0.6)
        ));
        b.setForeground(java.awt.Color.WHITE);
    } else {
        b.setEnabled(true);
    }
}

private String __sb_detectActive() {
    String n = getClass().getSimpleName().toUpperCase();
    if (n.contains("REPORT")) return "REPORTS";
    if (n.contains("AVAIL")) return "AVAILABILITY";
    if (n.contains("CUSTOM")) return "CUSTOMERS";
    if (n.equals("RENT") || n.contains("RENTAL")) return "RENT";
    if (n.contains("RETURN")) return "RETURN";
    if (n.equals("CARS") || (n.contains("CAR") && !n.contains("ACCOUNT"))) return "CARS";
    return "";
}

private void __sb_open(String... simpleNames) {
    java.util.LinkedHashSet<String> pkgs = new java.util.LinkedHashSet<String>();
    try {
        Package p = getClass().getPackage();
        String pkg = (p != null ? p.getName() : "");
        if (pkg != null && !pkg.isEmpty()) {
            pkgs.add(pkg);
            int idx = pkg.lastIndexOf('.');
            while (idx > 0) { pkgs.add(pkg.substring(0, idx)); idx = pkg.lastIndexOf('.', idx - 1); }
        }
        pkgs.add("Car"); // common
        pkgs.add("car"); // case variant
        pkgs.add("");    // default package
    } catch (Exception ignore) {}

    java.util.List<String> tried = new java.util.ArrayList<String>();
    for (String pack : pkgs) {
        for (String s : simpleNames) {
            if (s == null || s.isEmpty()) continue;
            String fq = (pack == null || pack.isEmpty()) ? s : (pack + "." + s);
            try {
                Class<?> cl = Class.forName(fq);
                if (javax.swing.JFrame.class.isAssignableFrom(cl)) {
                    javax.swing.JFrame next = (javax.swing.JFrame) cl.getDeclaredConstructor().newInstance();
                    next.setVisible(true);
                    this.dispose();
                    return;
                }
            } catch (Throwable t) { tried.add(fq); }
        }
    }
    javax.swing.JOptionPane.showMessageDialog(this, "Screen not found. Tried:\n" + java.lang.String.join("\n", tried), "Navigation", javax.swing.JOptionPane.WARNING_MESSAGE);
}

private javax.swing.JPanel __sb_findSidebarPanel() {
    // 1) Try fields by common names (case-insensitive), including typos you mentioned (Jpanel / japnel)
    try {
        for (java.lang.reflect.Field f : getClass().getDeclaredFields()) {
            if (javax.swing.JPanel.class.isAssignableFrom(f.getType())) {
                String nm = f.getName();
                if (nm.equalsIgnoreCase("jPanel2") || nm.equalsIgnoreCase("JPanel2") ||
                    nm.equalsIgnoreCase("jpanel2") || nm.equalsIgnoreCase("Jpanel2") ||
                    nm.equalsIgnoreCase("jPanel")  || nm.equalsIgnoreCase("JPanel")  ||
                    nm.equalsIgnoreCase("jpanel")  || nm.equalsIgnoreCase("Jpanel")  ||
                    nm.equalsIgnoreCase("japnel")  || nm.equalsIgnoreCase("Japnel")  ||
                    nm.equalsIgnoreCase("sidePanel") || nm.equalsIgnoreCase("sidebar") ||
                    nm.equalsIgnoreCase("leftPanel") || nm.equalsIgnoreCase("menuPanel")) {
                    f.setAccessible(true);
                    Object o = f.get(this);
                    if (o instanceof javax.swing.JPanel) return (javax.swing.JPanel) o;
                }
            }
        }
    } catch (Throwable ignore) {}

    // 2) Score-based search on content
    java.util.List<javax.swing.JPanel> all = new java.util.ArrayList<javax.swing.JPanel>();
    __sb_collectPanels(getContentPane(), all);

    javax.swing.JPanel best = null;
    int bestScore = Integer.MIN_VALUE;
    java.awt.Color target = new java.awt.Color(0,0,51);

    for (javax.swing.JPanel p : all) {
        int s = 0;
        java.awt.Color bg = p.getBackground();
        if (bg != null) {
            int d = Math.abs(bg.getRed()-target.getRed()) + Math.abs(bg.getGreen()-target.getGreen()) + Math.abs(bg.getBlue()-target.getBlue());
            if (d < 40) s += 8; // close to sidebar color
        }
        java.awt.Dimension pref = p.getPreferredSize();
        if (pref != null && pref.width >= 250) s += 4;
        if (__sb_findButton(p, "CARS") != null) s += 6;
        if (__sb_findButton(p, "AVAILABILITY") != null) s += 4;
        if (__sb_findButton(p, "CUSTOMERS") != null) s += 2;
        if (p.getX() <= 10) s += 2;
        if (s > bestScore) { bestScore = s; best = p; }
    }
    return best;
}

private void __sb_collectPanels(java.awt.Container root, java.util.List<javax.swing.JPanel> out) {
    if (root == null) return;
    for (java.awt.Component c : root.getComponents()) {
        if (c instanceof javax.swing.JPanel) {
            out.add((javax.swing.JPanel) c);
            __sb_collectPanels((java.awt.Container) c, out);
        } else if (c instanceof java.awt.Container) {
            __sb_collectPanels((java.awt.Container) c, out);
        }
    }
}
// ===== END UNIVERSAL SIDEBAR =====
/* === AUTO FULL-SCREEN + RESPONSIVE SCALING (paste inside Cars class) === */
private boolean __autoFS_inited = false;

@Override
public void addNotify() {
    super.addNotify();
    if (__autoFS_inited) return;
    __autoFS_inited = true;
    __installFullScreenAndScaling();
}

private void __installFullScreenAndScaling() {
    // 1) Let jPanel1 fill the entire frame
    try {
        java.awt.Container root = getContentPane();
        java.awt.Component center = jPanel1; // keep your existing main panel
        root.removeAll();
        root.setLayout(new java.awt.BorderLayout());
        root.add(center, java.awt.BorderLayout.CENTER);
        root.validate();
    } catch (Exception ignore) {}

    // 2) Prepare proportional scaling for components inside jPanel1 (null layout)
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

    // 3) Maximize to screen when the window opens (stable across OSes)
    this.addWindowListener(new java.awt.event.WindowAdapter() {
        @Override public void windowOpened(java.awt.event.WindowEvent e) {
            try {
                // Loosen size caps set by the GUI builder
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
/* === END AUTO FULL-SCREEN + SCALING === */    
    
public final void loadOnRentTable() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");

        String sql = "SELECT id, vehicle_no, cust_nic, brand, rent_date, return_date, total_fee FROM on_rentrecords";
        pst = con.prepareStatement(sql);
        rs = pst.executeQuery();

        DefaultTableModel model = (DefaultTableModel) onRentTbl.getModel();
        model.setRowCount(0); // clear table

        while (rs.next()) {
            Vector v = new Vector();
            v.add(rs.getString("id"));
            v.add(rs.getString("vehicle_no"));
            v.add(rs.getString("cust_nic"));
            v.add(rs.getString("brand"));
            v.add(rs.getDate("rent_date"));
            v.add(rs.getDate("return_date"));
            v.add("LKR " + rs.getInt("total_fee"));
            model.addRow(v);
        }

        // 🔎 DEBUG PRINT - to see if any value is null
        for (int i = 0; i < model.getRowCount(); i++) {
            for (int j = 0; j < model.getColumnCount(); j++) {
                Object value = model.getValueAt(i, j);
                if (value != null) {
                    System.out.println("Row " + i + ", Col " + j + ": " + value.toString());
                } else {
                    System.out.println("❗ NULL at Row " + i + ", Col " + j);
                }
            }
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading on-rent records: " + e.getMessage());
    }
}


public void loadVehicleNumbersToCombo() {
    try {
        // Connect to your DB
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");

        // Query to get all vehicle numbers
        pst = con.prepareStatement("SELECT vehicle_no FROM on_rentrecords");
        rs = pst.executeQuery();

        txtVehicleNumber.removeAllItems(); // Clear previous items

        while (rs.next()) {
            String vehicleNo = rs.getString("vehicle_no");
            txtVehicleNumber.addItem(vehicleNo); // Add to ComboBox
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error loading vehicle numbers: " + e.getMessage());
    }
}

public void calculateDelayAndTotalFee() {
    try {
        Date rentDate = actualReturnDateChooser.getDate();
        Date actualReturnDate = actualReturnDateChooser.getDate();

        if (rentDate != null && actualReturnDate != null) {

            DefaultTableModel model = (DefaultTableModel) onRentTbl.getModel();
            int selectedIndex = onRentTbl.getSelectedRow();
            if (selectedIndex == -1) {
              
                return;
            }

            // Get dates
            String expectedReturnDateStr = model.getValueAt(selectedIndex, 5).toString();
            LocalDate rent = new java.sql.Date(rentDate.getTime()).toLocalDate();
            LocalDate expectedReturn = LocalDate.parse(expectedReturnDateStr);
            LocalDate actualReturn = new java.sql.Date(actualReturnDate.getTime()).toLocalDate();

            // Get base fee
            String feeStr = model.getValueAt(selectedIndex, 6).toString().replace("LKR", "").replace(",", "").trim();
            int baseFee = Integer.parseInt(feeStr);

            // Calculate days rented (original period)
            long originalDays = ChronoUnit.DAYS.between(rent, expectedReturn);
            if (originalDays <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid rental period.");
                return;
            }

            // Calculate daily rate from base fee
            int dailyRate = (int) (baseFee / originalDays);

            // Calculate delay
            long delay = ChronoUnit.DAYS.between(expectedReturn, actualReturn);
            if (delay < 0) delay = 0;
            txtDelay.setText(Long.toString(delay));

            // New total
            int totalFee = baseFee + (int) delay * dailyRate;

            // Show total
            NumberFormat formatter = NumberFormat.getInstance();
            String formattedFee = "LKR " + formatter.format(totalFee);
            txtFee.setText(formattedFee);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error calculating total fee: " + e.getMessage());
    }
}

public void loadReturnedCarsTable() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");

        String sql = "SELECT * FROM returned_cars";
        pst = con.prepareStatement(sql);
        rs = pst.executeQuery();

        DefaultTableModel model = (DefaultTableModel) returnedCarsTbl.getModel();
        model.setRowCount(0); // clear old data

        while (rs.next()) {
            Vector v = new Vector();
            v.add(rs.getString("id"));
            v.add(rs.getString("vehicle_no"));
            v.add(rs.getString("cust_nic"));
            v.add(rs.getString("rent_date"));
            v.add(rs.getString("expected_return_date"));
            v.add(rs.getString("actual_return_date"));
            v.add(rs.getInt("delay_days"));
            v.add("LKR " + rs.getInt("final_fee"));
            v.add(rs.getString("status"));
            model.addRow(v);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading returned cars: " + e.getMessage());
    }
}


public void editReturnedCar() {
    try {
        int selectedRow = returnedCarsTbl.getSelectedRow();
        if (selectedRow == -1) {
            
            return;
        }

        String id = returnedCarsTbl.getValueAt(selectedRow, 0).toString();

        String vehicleNo = txtVehicleNumber.getSelectedItem().toString();
        String custNic = txtCustNIC.getText();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String rentDate = sdf.format(actualReturnDateChooser.getDate());
        String expectedReturnDate = sdf.format(actualReturnDateChooser.getDate());
        String actualReturnDate = sdf.format(actualReturnDateChooser.getDate());
        int delayDays = Integer.parseInt(txtDelay.getText());
        int finalFee = Integer.parseInt(txtFee.getText().replace("LKR", "").replace(",", "").trim());
        String status = "Returned";

        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/returned", "root", "");

        String sql = "UPDATE returned_cars SET vehicle_no=?, cust_nic=?, rent_date=?, expected_return_date=?, actual_return_date=?, delay_days=?, final_fee=?, status=? WHERE id=?";
        pst = con.prepareStatement(sql);
        pst.setString(1, vehicleNo);
        pst.setString(2, custNic);
        pst.setString(3, rentDate);
        pst.setString(4, expectedReturnDate);
        pst.setString(5, actualReturnDate);
        pst.setInt(6, delayDays);
        pst.setInt(7, finalFee);
        pst.setString(8, status);
        pst.setString(9, id);

        pst.executeUpdate();
        JOptionPane.showMessageDialog(this, "Record updated successfully!");

        loadReturnedCarsTable(); // refresh table

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error updating record: " + e.getMessage());
    }
}


public void deleteReturnedCar() {
    try {
        int selectedRow = returnedCarsTbl.getSelectedRow();
        if (selectedRow == -1) {
            
            return;
        }

        String id = returnedCarsTbl.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this record?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/returned", "root", "");

        String sql = "DELETE FROM returned_cars WHERE id=?";
        pst = con.prepareStatement(sql);
        pst.setString(1, id);
        pst.executeUpdate();

        JOptionPane.showMessageDialog(this, "Record deleted successfully!");
        loadReturnedCarsTable(); // refresh

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error deleting record: " + e.getMessage());
    }
}

public void resetFields() {
    txtVehicleNumber.setSelectedIndex(-1);
    txtCustNIC.setText("");
    actualReturnDateChooser.setDate(null);
    actualReturnDateChooser.setDate(null);
    txtDelay.setText("");
    txtFee.setText("");
}






    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        onRentTbl = new javax.swing.JTable();
        editBtn = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        txtCustNIC = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnSave = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        resetBtn = new javax.swing.JButton();
        txtVehicleNumber = new javax.swing.JComboBox<>();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtFee = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        returnedCarsTbl = new javax.swing.JTable();
        jLabel13 = new javax.swing.JLabel();
        txtDelay = new javax.swing.JTextField();
        jCalendarComboBox1 = new de.wannawork.jcalendar.JCalendarComboBox();
        actualReturnDateChooser = new de.wannawork.jcalendar.JCalendarComboBox();
        txtFee1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1368, 780));
        setMinimumSize(new java.awt.Dimension(1300, 750));
        setPreferredSize(new java.awt.Dimension(1368, 780));
        getContentPane().setLayout(null);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(1378, 780));
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

        jButton4.setBackground(new java.awt.Color(102, 102, 255));
        jButton4.setFont(jButton4.getFont().deriveFont(jButton4.getFont().getStyle() | java.awt.Font.BOLD, jButton4.getFont().getSize()+8));
        jButton4.setForeground(new java.awt.Color(255, 255, 255));
        jButton4.setText("CARS");
        jButton4.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(230, 0, 52));
        jLabel2.setText("LOGOUT");

        jLabel3.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("ACCOUNT");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImageR/Side Bar Logo.png"))); // NOI18N
        jLabel4.setPreferredSize(new java.awt.Dimension(1368, 780));

        jLabel9.setFont(jLabel9.getFont().deriveFont(jLabel9.getFont().getStyle() | java.awt.Font.BOLD, jLabel9.getFont().getSize()+6));
        jLabel9.setForeground(new java.awt.Color(102, 102, 255));
        jLabel9.setText("ACCOUNT");

        jLabel10.setFont(jLabel10.getFont().deriveFont(jLabel10.getFont().getStyle() | java.awt.Font.BOLD, jLabel10.getFont().getSize()+6));
        jLabel10.setForeground(new java.awt.Color(204, 204, 204));
        jLabel10.setText("LOGOUT");

        jButton11.setBackground(new java.awt.Color(204, 204, 255));
        jButton11.setFont(jButton11.getFont().deriveFont(jButton11.getFont().getStyle() | java.awt.Font.BOLD, jButton11.getFont().getSize()+8));
        jButton11.setForeground(new java.awt.Color(255, 255, 255));
        jButton11.setText("RETURN");
        jButton11.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(102, 102, 255));
        jLabel8.setText("ACCOUNT");
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("LOG OUT");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
                .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 527, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(57, 57, 57))
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 290, 1399);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Return Management");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(698, 6, 233, 32);

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() | java.awt.Font.BOLD, jLabel5.getFont().getSize()+6));
        jLabel5.setForeground(new java.awt.Color(0, 0, 51));
        jLabel5.setText("Customer NIC");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(440, 90, 180, 25);

        onRentTbl.setAutoCreateRowSorter(true);
        onRentTbl.setBackground(new java.awt.Color(204, 204, 204));
        onRentTbl.setFont(onRentTbl.getFont().deriveFont(onRentTbl.getFont().getStyle() | java.awt.Font.BOLD, 12));
        onRentTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Rental ID", "Vehical Number", "Customer NIC", "Brand", "Reent Date", "Return Date", "Rent Fee"
            }
        ));
        onRentTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onRentTblMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(onRentTbl);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(310, 350, 1038, 150);

        editBtn.setBackground(new java.awt.Color(0, 0, 51));
        editBtn.setFont(editBtn.getFont().deriveFont(editBtn.getFont().getStyle() | java.awt.Font.BOLD, editBtn.getFont().getSize()+6));
        editBtn.setForeground(new java.awt.Color(255, 255, 255));
        editBtn.setText("EDIT");
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });
        jPanel1.add(editBtn);
        editBtn.setBounds(1110, 140, 145, 39);

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
        jButton7.setBounds(793, 1335, 145, 58);

        txtCustNIC.setBackground(new java.awt.Color(204, 204, 204));
        jPanel1.add(txtCustNIC);
        txtCustNIC.setBounds(760, 90, 232, 22);

        jLabel6.setFont(jLabel6.getFont().deriveFont(jLabel6.getFont().getStyle() | java.awt.Font.BOLD, jLabel6.getFont().getSize()+6));
        jLabel6.setForeground(new java.awt.Color(0, 0, 51));
        jLabel6.setText("Vehicle Number");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(440, 50, 190, 25);

        jLabel7.setFont(jLabel7.getFont().deriveFont(jLabel7.getFont().getStyle() | java.awt.Font.BOLD, jLabel7.getFont().getSize()+6));
        jLabel7.setForeground(new java.awt.Color(0, 0, 51));
        jLabel7.setText("Rent Date");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(440, 140, 190, 25);

        btnSave.setBackground(new java.awt.Color(0, 0, 51));
        btnSave.setFont(btnSave.getFont().deriveFont(btnSave.getFont().getStyle() | java.awt.Font.BOLD, btnSave.getFont().getSize()+6));
        btnSave.setForeground(new java.awt.Color(255, 255, 255));
        btnSave.setText("SAVE");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        jPanel1.add(btnSave);
        btnSave.setBounds(1110, 60, 145, 57);

        deleteBtn.setBackground(new java.awt.Color(0, 0, 51));
        deleteBtn.setFont(deleteBtn.getFont().deriveFont(deleteBtn.getFont().getStyle() | java.awt.Font.BOLD, deleteBtn.getFont().getSize()+6));
        deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteBtn.setText("DELETE");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });
        jPanel1.add(deleteBtn);
        deleteBtn.setBounds(1110, 260, 145, 39);

        resetBtn.setBackground(new java.awt.Color(0, 0, 51));
        resetBtn.setFont(resetBtn.getFont().deriveFont(resetBtn.getFont().getStyle() | java.awt.Font.BOLD, resetBtn.getFont().getSize()+6));
        resetBtn.setForeground(new java.awt.Color(255, 255, 255));
        resetBtn.setText("RESET");
        resetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBtnActionPerformed(evt);
            }
        });
        jPanel1.add(resetBtn);
        resetBtn.setBounds(1110, 200, 145, 39);

        txtVehicleNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVehicleNumberActionPerformed(evt);
            }
        });
        jPanel1.add(txtVehicleNumber);
        txtVehicleNumber.setBounds(760, 50, 232, 22);

        jLabel11.setFont(jLabel11.getFont().deriveFont(jLabel11.getFont().getStyle() | java.awt.Font.BOLD, jLabel11.getFont().getSize()+6));
        jLabel11.setForeground(new java.awt.Color(0, 0, 51));
        jLabel11.setText("Return Date");
        jPanel1.add(jLabel11);
        jLabel11.setBounds(440, 180, 220, 25);

        jLabel12.setFont(jLabel12.getFont().deriveFont(jLabel12.getFont().getStyle() | java.awt.Font.BOLD, jLabel12.getFont().getSize()+6));
        jLabel12.setForeground(new java.awt.Color(0, 0, 51));
        jLabel12.setText("Fee");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(440, 255, 120, 30);

        txtFee.setForeground(new java.awt.Color(255, 0, 0));
        txtFee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFeeActionPerformed(evt);
            }
        });
        jPanel1.add(txtFee);
        txtFee.setBounds(760, 242, 232, 30);

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 255));
        jLabel14.setText("On Rent");
        jPanel1.add(jLabel14);
        jLabel14.setBounds(790, 320, 70, 25);

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 255));
        jLabel15.setText("Retuned Cars");
        jLabel15.setMinimumSize(new java.awt.Dimension(1300, 750));
        jPanel1.add(jLabel15);
        jLabel15.setBounds(770, 500, 113, 20);

        returnedCarsTbl.setAutoCreateRowSorter(true);
        returnedCarsTbl.setBackground(new java.awt.Color(204, 204, 204));
        returnedCarsTbl.setFont(returnedCarsTbl.getFont().deriveFont(returnedCarsTbl.getFont().getStyle() | java.awt.Font.BOLD, 12));
        returnedCarsTbl.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Return ID", "Vehical Number", "NIC", "Rent Date", "Renturn Date", "Delay", "Total", "Status"
            }
        ));
        returnedCarsTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                returnedCarsTblMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(returnedCarsTbl);

        jPanel1.add(jScrollPane2);
        jScrollPane2.setBounds(310, 530, 1038, 150);

        jLabel13.setFont(jLabel13.getFont().deriveFont(jLabel13.getFont().getStyle() | java.awt.Font.BOLD, jLabel13.getFont().getSize()+6));
        jLabel13.setForeground(new java.awt.Color(0, 0, 51));
        jLabel13.setText("Delay");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(440, 220, 150, 25);
        jPanel1.add(txtDelay);
        txtDelay.setBounds(760, 202, 232, 30);
        jPanel1.add(jCalendarComboBox1);
        jCalendarComboBox1.setBounds(760, 170, 230, 22);
        jPanel1.add(actualReturnDateChooser);
        actualReturnDateChooser.setBounds(760, 130, 230, 22);

        txtFee1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFee1ActionPerformed(evt);
            }
        });
        jPanel1.add(txtFee1);
        txtFee1.setBounds(760, 282, 232, 30);

        jLabel17.setFont(jLabel17.getFont().deriveFont(jLabel17.getFont().getStyle() | java.awt.Font.BOLD, jLabel17.getFont().getSize()+6));
        jLabel17.setForeground(new java.awt.Color(0, 0, 51));
        jLabel17.setText("Total");
        jPanel1.add(jLabel17);
        jLabel17.setBounds(440, 290, 120, 25);

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 10, 1366, 780);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Availability c  = new Availability();
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
         Rental c  = new Rental();
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Customers c  = new Customers();
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
          Cars c  = new Cars();
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton11ActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        // TODO add your handling code here:
           editReturnedCar();



    }//GEN-LAST:event_editBtnActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
                                                                                    
    try {
        DefaultTableModel model = (DefaultTableModel) onRentTbl.getModel();
        int selectedIndex = onRentTbl.getSelectedRow();

        
        

        // From table
        String vehicleNo = model.getValueAt(selectedIndex, 1).toString();
        String nic = model.getValueAt(selectedIndex, 2).toString();
        String rentDateStr = model.getValueAt(selectedIndex, 4).toString();
        String expectedReturnDateStr = model.getValueAt(selectedIndex, 5).toString();

        // From form
        String actualReturnDateStr = new SimpleDateFormat("yyyy-MM-dd").format(actualReturnDateChooser.getDate());
        int delay = Integer.parseInt(txtDelay.getText());
        String feeStr = txtFee.getText().replace("LKR", "").replace(",", "").trim();
        int totalFee = Integer.parseInt(feeStr);

        // ✅ Connect to the new DB: 'returned'
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");

        // ✅ Insert into 'returned_cars'
        String sql = "INSERT INTO returned_cars (vehicle_no, cust_nic, rent_date, expected_return_date, actual_return_date, delay_days, final_fee, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        pst = con.prepareStatement(sql);
        pst.setString(1, vehicleNo);
        pst.setString(2, nic);
        pst.setString(3, rentDateStr);
        pst.setString(4, expectedReturnDateStr);
        pst.setString(5, actualReturnDateStr);
        pst.setInt(6, delay);
        pst.setInt(7, totalFee);
        pst.setString(8, "Returned");

        pst.executeUpdate();

        // ✅ Optionally: remove from on_rentrecords (must connect to on_rent DB for this)
     // ✅ Optionally: remove from on_rentrecords (must connect to on_rent DB for this)
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");
        String delSql = "DELETE FROM on_rentrecords WHERE id = ?";
        PreparedStatement delPst = con.prepareStatement(delSql);
        delPst.setString(1, model.getValueAt(selectedIndex, 0).toString());
        delPst.executeUpdate();

        JOptionPane.showMessageDialog(this, "Vehicle return recorded successfully!");

        loadOnRentTable();
        txtVehicleNumber.setSelectedIndex(-1);
        txtCustNIC.setText("");
        txtFee.setText("");
        txtDelay.setText("");
        actualReturnDateChooser.setDate(null);
        actualReturnDateChooser.setDate(null);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error saving returned car: " + e.getMessage());
    }




    }//GEN-LAST:event_btnSaveActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:

            deleteReturnedCar();


    }//GEN-LAST:event_deleteBtnActionPerformed

    private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
        // TODO add your handling code here:
            resetFields();

    }//GEN-LAST:event_resetBtnActionPerformed

    private void txtFeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFeeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFeeActionPerformed

    private void txtVehicleNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVehicleNumberActionPerformed
        // TODO add your handling code here:
      
    String selectedVehicleNo = (String) txtVehicleNumber.getSelectedItem();
DefaultTableModel model = (DefaultTableModel) onRentTbl.getModel();

for (int i = 0; i < model.getRowCount(); i++) {
    Object objVehicleNo = model.getValueAt(i, 1);
    String tableVehicleNo = objVehicleNo != null ? objVehicleNo.toString() : "";

    if (selectedVehicleNo.equals(tableVehicleNo)) {
        onRentTbl.setRowSelectionInterval(i, i);

        Object objNIC = model.getValueAt(i, 2);
        txtCustNIC.setText(objNIC != null ? objNIC.toString() : "");

        try {
            Object objRentDate = model.getValueAt(i, 4);
            if (objRentDate != null) {
                String rentDateStr = objRentDate.toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date rentDate = sdf.parse(rentDateStr);
                actualReturnDateChooser.setDate(rentDate);
            }

            Object objReturnDate = model.getValueAt(i, 5);
            if (objReturnDate != null) {
                String returnDateStr = objReturnDate.toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date returnDate = sdf.parse(returnDateStr);
                actualReturnDateChooser.setDate(returnDate);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Date parse error: " + e.getMessage());
        }

        Object objFee = model.getValueAt(i, 6);
        String fee = objFee != null ? objFee.toString().replace("LKR", "").trim() : "";
        txtFee.setText(fee);

        break;
    }
}



    }//GEN-LAST:event_txtVehicleNumberActionPerformed

    private void onRentTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onRentTblMouseClicked
        // TODO add your handling code here:
  
    DefaultTableModel model = (DefaultTableModel) onRentTbl.getModel();
int selectedIndex = onRentTbl.getSelectedRow();

if (selectedIndex != -1) {
    // Other values...
    String vehicleNo = model.getValueAt(selectedIndex, 1).toString();
    String nic = model.getValueAt(selectedIndex, 2).toString();
    String rentDateStr = model.getValueAt(selectedIndex, 4).toString();
    String feeText = model.getValueAt(selectedIndex, 6).toString().replace("LKR", "").trim();

    // ✅ Store daily rate here:
    dailyRate = Integer.parseInt(feeText);

    // ✅ Optional: show it in txtFee as clean number if you want:
    txtFee.setText(feeText);

    // Existing rent date parsing:
    try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date rentDate = sdf.parse(rentDateStr);
        actualReturnDateChooser.setDate(rentDate);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error parsing rent date: " + e.getMessage());
    }
}



    }//GEN-LAST:event_onRentTblMouseClicked

    private void returnedCarsTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_returnedCarsTblMouseClicked
        // TODO add your handling code here:
                                                
     DefaultTableModel model = (DefaultTableModel) returnedCarsTbl.getModel();
    int selectedIndex = returnedCarsTbl.getSelectedRow();

    if (selectedIndex != -1) {
        String vehicleNo = model.getValueAt(selectedIndex, 1).toString();
        String nic = model.getValueAt(selectedIndex, 2).toString();
        String rentDateStr = model.getValueAt(selectedIndex, 3).toString();
        String expectedReturnDateStr = model.getValueAt(selectedIndex, 4).toString();
        String actualReturnDateStr = model.getValueAt(selectedIndex, 5).toString();
        String delay = model.getValueAt(selectedIndex, 6).toString();
        String fee = model.getValueAt(selectedIndex, 7).toString();

        //  Make sure the selected vehicle is present in the ComboBox
        boolean found = false;
        for (int i = 0; i < txtVehicleNumber.getItemCount(); i++) {
            if (txtVehicleNumber.getItemAt(i).equals(vehicleNo)) {
                found = true;
                break;
            }
        }
        if (!found) {
            txtVehicleNumber.addItem(vehicleNo);
        }
        txtVehicleNumber.setSelectedItem(vehicleNo);

        txtCustNIC.setText(nic);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date rentDate = sdf.parse(rentDateStr);
            Date actualReturnDate = sdf.parse(actualReturnDateStr);

            actualReturnDateChooser.setDate(rentDate);
            actualReturnDateChooser.setDate(actualReturnDate);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Date parse error: " + e.getMessage());
        }

        txtDelay.setText(delay);
        txtFee.setText(fee);
    }





                                           

    }//GEN-LAST:event_returnedCarsTblMouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        // TODO add your handling code here:
        new Account().setVisible(true);

    // Close the current window (optional)
    this.dispose();
    }//GEN-LAST:event_jLabel8MouseClicked
/*
    private void txtFee1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFee1ActionPerformed
        // TODO add your handling code here:
                          try {
        // 1. Get delay days
        double delayDays = Double.parseDouble(txtDelay.getText().trim());

        // 2. Get base fee (remove currency and commas if present)
        double baseFee = Double.parseDouble(
            txtFee.getText()
                 .replace("LKR", "")
                 .replace(",", "")
                 .trim()
        );

        // 3. Calculate total (example: base fee + (delay days × base fee))
        double total = delayDays + baseFee;

        // 4. Display result in txtFee1
        txtFee1.setText(String.valueOf(total));
        
    } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(
            this,
            "Please enter valid numbers in Fee and Delay fields.",
            "Invalid Input",
            JOptionPane.ERROR_MESSAGE
        );
    }
            
        
            
    }//GEN-LAST:event_txtFee1ActionPerformed
*/
    // === Auto total while typing (txtDelay + txtFee) ===
// Call once in constructor after initComponents():  startTotalAutoUpdate();

private void startTotalAutoUpdate() {
    javax.swing.event.DocumentListener dl = new javax.swing.event.DocumentListener() {
        public void insertUpdate(javax.swing.event.DocumentEvent e) { __updateTotal(); }
        public void removeUpdate(javax.swing.event.DocumentEvent e) { __updateTotal(); }
        public void changedUpdate(javax.swing.event.DocumentEvent e) { __updateTotal(); }
    };
    try { txtDelay.getDocument().addDocumentListener(dl); } catch (Throwable ignore) {}
    try { txtFee.getDocument().addDocumentListener(dl); } catch (Throwable ignore) {}

    // Also update once at start
    __updateTotal();
}

private void __updateTotal() {
    try {
        double delayDays = __parseNumber(txtDelay.getText());
        double baseFee   = __parseNumber(txtFee.getText());

        // Your current rule: total = baseFee + delayDays
        // If you need per-day fee use: baseFee + (delayDays * perDay)
        double total = baseFee + delayDays;

        boolean showCurrency = txtFee.getText() != null && txtFee.getText().toUpperCase().contains("LKR");
        txtFee1.setText(__formatNumber(total, showCurrency));
    } catch (Exception ex) {
        // While typing invalid char, just clear total
        txtFee1.setText("");
    }
}

private double __parseNumber(String s) {
    if (s == null) return 0;
    s = s.replace("LKR", "").replace(",", "").trim();
    s = s.replaceAll("[^0-9.\\-]", ""); // keep digits, dot, minus
    if (s.isEmpty() || s.equals(".") || s.equals("-")) return 0;
    try { return Double.parseDouble(s); } catch (NumberFormatException e) { return 0; }
}

private String __formatNumber(double v, boolean withCurrency) {
    java.text.DecimalFormat df = new java.text.DecimalFormat("#,##0.00");
    return (withCurrency ? "LKR " : "") + df.format(v);
}

// Replace your existing handler body with this one-liner
private void txtFee1ActionPerformed(java.awt.event.ActionEvent evt) {
    __updateTotal();
}
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Return.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Return.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Return.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Return.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Return().setVisible(true);
            }
            
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private de.wannawork.jcalendar.JCalendarComboBox actualReturnDateChooser;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private de.wannawork.jcalendar.JCalendarComboBox jCalendarComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable onRentTbl;
    private javax.swing.JButton resetBtn;
    private javax.swing.JTable returnedCarsTbl;
    private javax.swing.JTextField txtCustNIC;
    private javax.swing.JTextField txtDelay;
    private javax.swing.JTextField txtFee;
    private javax.swing.JTextField txtFee1;
    private javax.swing.JComboBox<String> txtVehicleNumber;
    // End of variables declaration//GEN-END:variables
}
