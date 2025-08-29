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
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author User
 */
public class Rental extends javax.swing.JFrame {

    PreparedStatement insert;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;
    
  public Rental() {
    initComponents();

installSidebarUniversal();
    loadCustomerNICs();
    loadVehicleNumbers(); 
    loadRentalTable();
    loadAvailableCarsToTable();
 calculateTotalFee();
    txtbrand.setEditable(false);
    txtbrand.setFocusable(false);

    txtFee.setEditable(false);
    txtFee.setFocusable(false);
    
          // Clear selection
    txtVehicleNumber.setSelectedIndex(-1);  // Clear selection
txtCustNIC.setText("");
  

    // Add ActionListener here — this is the correct place
    txtVehicleNumber.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            String regNo = (String) txtVehicleNumber.getSelectedItem();
            if (regNo != null && !regNo.isEmpty()) {
                loadVehicleBrand(regNo); // call the brand loading method
            }
        }
    });
    
    
    txtVehicleNumber.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        String regNo = (String) txtVehicleNumber.getSelectedItem();
        if (regNo != null && !regNo.isEmpty()) {
            loadVehicleDetails(regNo); // this loads brand AND fee
        }
    }
});
    
    returnDateChooser.addPropertyChangeListener("date", evt -> {
    calculateTotalFee();
});
    
    
}
// ===== UNIVERSAL SIDEBAR — PASTE THIS INSIDE EACH JFrame CLASS (above the last }) =====
// After pasting, call installSidebarUniversal(); right after initComponents() in your constructor.

private void installSidebarUniversal() {
    try { __sb_installSidebar(); } catch (Throwable t) { t.printStackTrace(); }
}

private void __sb_installSidebar() {
    javax.swing.JPanel sidebar = __sb_findSidebarPanel();
    if (sidebar == null) return;

    // Keep your color/design
    final java.awt.Color PANEL_BG = new java.awt.Color(0,0,51);
    sidebar.setBackground(PANEL_BG);

    // Use any existing nav button to clone style
    javax.swing.JButton template = __sb_findAnyNavButton(sidebar);

    // Ensure buttons exist (clone style if created)
    javax.swing.JButton btnAvail  = __sb_ensureButton(sidebar, "AVAILABILITY", template);
    javax.swing.JButton btnCust   = __sb_ensureButton(sidebar, "CUSTOMERS",   template);
    javax.swing.JButton btnRent   = __sb_ensureButton(sidebar, "RENT",        template);
    javax.swing.JButton btnReturn = __sb_ensureButton(sidebar, "RETURN",      template);
    javax.swing.JButton btnCars   = __sb_ensureButton(sidebar, "CARS",        template);
    javax.swing.JButton btnReport = __sb_ensureButton(sidebar, "REPORTS",     template); // must be under CARS

    // Labels
    javax.swing.JLabel logo       = __sb_findLogo(sidebar);
    javax.swing.JLabel lblAccount = __sb_ensureLabel(sidebar, "ACCOUNT");
    javax.swing.JLabel lblLogout  = __sb_ensureLabel(sidebar, "LOGOUT");

    // Wire actions via reflection (no package coupling)
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

    // Active page: disable and highlight
    String active = __sb_detectActive();
    __sb_setActive(btnAvail,  "AVAILABILITY".equals(active));
    __sb_setActive(btnCust,   "CUSTOMERS".equals(active));
    __sb_setActive(btnRent,   "RENT".equals(active));
    __sb_setActive(btnReturn, "RETURN".equals(active));
    __sb_setActive(btnCars,   "CARS".equals(active));
    __sb_setActive(btnReport, "REPORTS".equals(active));

    // Rebuild layout to keep order and spacing (REPORTS under CARS)
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
    v.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE);
    v.addComponent(lblAccount).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED);
    v.addComponent(lblLogout).addGap(40);
    gl.setVerticalGroup(gl.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(v));

    sidebar.revalidate(); sidebar.repaint();
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
    
   public void loadCustomerNICs() {
       
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");
        pst = con.prepareStatement("SELECT nic FROM custregestration");
        rs = pst.executeQuery();

        txtCustNIC.setText(""); // Clear existing items if any

        while (rs.next()) {
            String nic = rs.getString("nic");
            
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Failed to load NICs: " + e.getMessage());
    }
}
   
   
   
public void loadVehicleNumbers() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");
        
        pst = con.prepareStatement("SELECT reg_no FROM carregistration WHERE Available = 'Yes'");
        rs = pst.executeQuery();

        txtVehicleNumber.removeAllItems();

        while (rs.next()) {
            String vehicleNo = rs.getString("reg_no");
            txtVehicleNumber.addItem(vehicleNo);
        }

        rs.close();
        pst.close();
        con.close();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading vehicle numbers: " + e.getMessage());
    }
}

  
  
  
public void loadVehicleBrand(String regNo) {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");

        pst = con.prepareStatement("SELECT brand FROM carregistration WHERE reg_no = ?");
        pst.setString(1, regNo);
        rs = pst.executeQuery();

        if (rs.next()) {
            txtbrand.setText(rs.getString("brand")); // Auto-fill brand field
        } else {
            txtbrand.setText(""); // Clear if not found
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading vehicle brand: " + e.getMessage());
    }
}


public void loadVehicleDetails(String regNo) {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");

        pst = con.prepareStatement("SELECT brand, rental FROM carregistration WHERE reg_no = ?");
        pst.setString(1, regNo);
        rs = pst.executeQuery();

        if (rs.next()) {
            txtbrand.setText(rs.getString("brand"));

            int rental = rs.getInt("rental");
            NumberFormat formatter = NumberFormat.getInstance();
            formatter.setMaximumFractionDigits(0);
            txtFee.setText("LKR " + formatter.format(rental));
        } else {
            txtbrand.setText("");
            txtFee.setText("");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading vehicle details: " + e.getMessage());
    }
}
// Drop-in: self-wires for auto-calc, allows same-day as 1 day, shows per-day until both dates picked
public void calculateTotalFee() {
    try {
        // Wire once so fee auto-updates when dates change
        if (!Boolean.TRUE.equals(txtFee.getClientProperty("autoCalcWired"))) {
            java.beans.PropertyChangeListener pl = evt -> {
                if ("date".equals(evt.getPropertyName())) calculateTotalFee();
            };
            try { rentDateChooser.addPropertyChangeListener("date", pl); } catch (Exception ignore) {}
            try { returnDateChooser.addPropertyChangeListener("date", pl); } catch (Exception ignore) {}
            try { rentDateChooser.addActionListener(e -> calculateTotalFee()); } catch (Exception ignore) {}
            try { returnDateChooser.addActionListener(e -> calculateTotalFee()); } catch (Exception ignore) {}
            txtFee.putClientProperty("autoCalcWired", Boolean.TRUE);
        }

        Date rentDate = rentDateChooser.getDate();
        Date returnDate = returnDateChooser.getDate();

        // Keep/learn per-day rate (cache so we don't re-multiply totals)
        Long perDay = null;
        Object cached = txtFee.getClientProperty("perDayRate");
        if (cached instanceof Number) perDay = ((Number) cached).longValue();
        if (perDay == null || perDay <= 0) {
            String digits = (txtFee.getText() == null) ? "" : txtFee.getText().replaceAll("[^0-9]", "");
            if (!digits.isEmpty()) {
                try { perDay = Long.parseLong(digits); txtFee.putClientProperty("perDayRate", perDay); }
                catch (NumberFormatException ignore) {}
            }
        }

        // If one/both dates not chosen yet: show daily rate if known
        if (rentDate == null || returnDate == null) {
            if (perDay != null && perDay > 0) {
                NumberFormat nf = NumberFormat.getInstance(new java.util.Locale("en","LK"));
                nf.setMaximumFractionDigits(0);
                txtFee.setText("LKR " + nf.format(perDay));
            } else {
                txtFee.setText("");
            }
            return;
        }

        LocalDate start = new java.sql.Date(rentDate.getTime()).toLocalDate();
        LocalDate end   = new java.sql.Date(returnDate.getTime()).toLocalDate();

        if (end.isBefore(start)) {
            JOptionPane.showMessageDialog(this, "Return date cannot be before rent date.");
            if (perDay != null && perDay > 0) {
                NumberFormat nf = NumberFormat.getInstance(new java.util.Locale("en","LK"));
                nf.setMaximumFractionDigits(0);
                txtFee.setText("LKR " + nf.format(perDay));
            } else {
                txtFee.setText("");
            }
            return;
        }

        long days = java.time.temporal.ChronoUnit.DAYS.between(start, end);
        if (days == 0) days = 1; // same-day rental counts as 1 day

        if (perDay == null || perDay <= 0) {
            // Try deducing if current text is a total (divisible by days)
            String digits = (txtFee.getText() == null) ? "" : txtFee.getText().replaceAll("[^0-9]", "");
            if (!digits.isEmpty()) {
                try {
                    long raw = Long.parseLong(digits);
                    long deduced = (raw > 0 && days > 0 && raw % days == 0) ? (raw / days) : raw;
                    perDay = Math.max(0, deduced);
                    txtFee.putClientProperty("perDayRate", perDay);
                } catch (NumberFormatException ignore) {}
            }
        }

        if (perDay == null || perDay <= 0) {
            txtFee.setText("");
            return;
        }

        long total = perDay * days;
        NumberFormat formatter = NumberFormat.getInstance(new java.util.Locale("en","LK"));
        formatter.setMaximumFractionDigits(0);
        txtFee.setText("LKR " + formatter.format(total));
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error calculating fee: " + e.getMessage());
    }
}

public void loadRentalTable() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");

        pst = con.prepareStatement("SELECT * FROM rental_records");
        rs = pst.executeQuery();

        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) onRentTbl.getModel();
        model.setRowCount(0); // Clear existing data

        while (rs.next()) {
            Object[] row = {
                rs.getInt("id"),
                rs.getString("cust_nic"),
                rs.getString("vehicle_no"),
                rs.getString("brand"),
                rs.getDate("rent_date"),
                rs.getDate("return_date"),
                rs.getInt("total_fee")
            };
            model.addRow(row);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading table data: " + e.getMessage());
    }
}


   public void loadAvailableCarsToTable() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");

        String query = "SELECT reg_no, brand, model, rental, Available FROM carregistration WHERE Available = 'Yes'";
        pst = con.prepareStatement(query);
        rs = pst.executeQuery();

        DefaultTableModel model = (DefaultTableModel) CarsTable.getModel();
        model.setRowCount(0); // clear existing rows

        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getString("reg_no"),
                rs.getString("brand"),
                rs.getString("model"),
                rs.getInt("rental"),
                rs.getString("Available")
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error loading available cars: " + e.getMessage());
    }
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
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        CarsTable = new javax.swing.JTable();
        btnEdit = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtbrand = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        saveBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        resetBtn = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        onRentTbl = new javax.swing.JTable();
        txtVehicleNumber = new javax.swing.JComboBox<>();
        txtFee = new javax.swing.JTextField();
        rentDateChooser = new de.wannawork.jcalendar.JCalendarComboBox();
        returnDateChooser = new de.wannawork.jcalendar.JCalendarComboBox();
        txtCustNIC = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1300, 750));

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1366, 768));

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

        jButton2.setBackground(new java.awt.Color(204, 204, 255));
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

        jLabel9.setFont(jLabel9.getFont().deriveFont(jLabel9.getFont().getStyle() | java.awt.Font.BOLD, jLabel9.getFont().getSize()+6));
        jLabel9.setForeground(new java.awt.Color(102, 102, 255));
        jLabel9.setText("ACCOUNT");
        jLabel9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel9MouseClicked(evt);
            }
        });

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
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(57, 57, 57))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Rental Management");
        jLabel1.setMaximumSize(new java.awt.Dimension(1368, 780));

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() | java.awt.Font.BOLD, jLabel5.getFont().getSize()+6));
        jLabel5.setForeground(new java.awt.Color(0, 0, 51));
        jLabel5.setText("Vehicle Number");

        CarsTable.setAutoCreateRowSorter(true);
        CarsTable.setBackground(new java.awt.Color(204, 204, 204));
        CarsTable.setFont(CarsTable.getFont().deriveFont(CarsTable.getFont().getStyle() | java.awt.Font.BOLD, 12));
        CarsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Vehicle No", "Brand", "Model", "Price"
            }
        ));
        jScrollPane1.setViewportView(CarsTable);

        btnEdit.setBackground(new java.awt.Color(0, 0, 51));
        btnEdit.setFont(btnEdit.getFont().deriveFont(btnEdit.getFont().getStyle() | java.awt.Font.BOLD, btnEdit.getFont().getSize()+6));
        btnEdit.setForeground(new java.awt.Color(255, 255, 255));
        btnEdit.setText("EDIT");
        btnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(0, 0, 51));
        jButton7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(230, 0, 52));
        jButton7.setText("Reset");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel6.setFont(jLabel6.getFont().deriveFont(jLabel6.getFont().getStyle() | java.awt.Font.BOLD, jLabel6.getFont().getSize()+6));
        jLabel6.setForeground(new java.awt.Color(0, 0, 51));
        jLabel6.setText("Customer NIC");

        txtbrand.setBackground(new java.awt.Color(204, 204, 204));

        jLabel7.setFont(jLabel7.getFont().deriveFont(jLabel7.getFont().getStyle() | java.awt.Font.BOLD, jLabel7.getFont().getSize()+6));
        jLabel7.setForeground(new java.awt.Color(0, 0, 51));
        jLabel7.setText("Brand");

        jLabel8.setFont(jLabel8.getFont().deriveFont(jLabel8.getFont().getStyle() | java.awt.Font.BOLD, jLabel8.getFont().getSize()+6));
        jLabel8.setForeground(new java.awt.Color(0, 0, 51));
        jLabel8.setText("Rent Date");

        saveBtn.setBackground(new java.awt.Color(0, 0, 51));
        saveBtn.setFont(saveBtn.getFont().deriveFont(saveBtn.getFont().getStyle() | java.awt.Font.BOLD, saveBtn.getFont().getSize()+6));
        saveBtn.setForeground(new java.awt.Color(255, 255, 255));
        saveBtn.setText("SAVE");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        deleteBtn.setBackground(new java.awt.Color(0, 0, 51));
        deleteBtn.setFont(deleteBtn.getFont().deriveFont(deleteBtn.getFont().getStyle() | java.awt.Font.BOLD, deleteBtn.getFont().getSize()+6));
        deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteBtn.setText("DELETE");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        resetBtn.setBackground(new java.awt.Color(0, 0, 51));
        resetBtn.setFont(resetBtn.getFont().deriveFont(resetBtn.getFont().getStyle() | java.awt.Font.BOLD, resetBtn.getFont().getSize()+6));
        resetBtn.setForeground(new java.awt.Color(255, 255, 255));
        resetBtn.setText("RESET");
        resetBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetBtnActionPerformed(evt);
            }
        });

        jLabel11.setFont(jLabel11.getFont().deriveFont(jLabel11.getFont().getStyle() | java.awt.Font.BOLD, jLabel11.getFont().getSize()+6));
        jLabel11.setForeground(new java.awt.Color(0, 0, 51));
        jLabel11.setText("Renturn Date");

        jLabel12.setFont(jLabel12.getFont().deriveFont(jLabel12.getFont().getStyle() | java.awt.Font.BOLD, jLabel12.getFont().getSize()+6));
        jLabel12.setForeground(new java.awt.Color(0, 0, 51));
        jLabel12.setText("Fee");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 255));
        jLabel14.setText("Available For Rent");

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 255));
        jLabel15.setText("On Rent");

        onRentTbl.setAutoCreateRowSorter(true);
        onRentTbl.setBackground(new java.awt.Color(204, 204, 204));
        onRentTbl.setFont(onRentTbl.getFont().deriveFont(onRentTbl.getFont().getStyle() | java.awt.Font.BOLD, 13));
        onRentTbl.setForeground(javax.swing.UIManager.getDefaults().getColor("Actions.Blue"));
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
                "Rental ID", "NIC", "Vehicle Number", "Brand", "Rent Date", "Renturn Date", "Fee"
            }
        ));
        onRentTbl.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onRentTblMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(onRentTbl);

        txtVehicleNumber.setFont(txtVehicleNumber.getFont());
        txtVehicleNumber.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtVehicleNumberActionPerformed(evt);
            }
        });

        txtFee.setBackground(new java.awt.Color(204, 204, 204));
        txtFee.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        txtFee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFeeActionPerformed(evt);
            }
        });

        txtCustNIC.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtCustNIC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCustNICActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(428, 428, 428))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1038, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(413, 413, 413)
                                        .addComponent(jLabel14))
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1038, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(452, 452, 452)
                                        .addComponent(jLabel15))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(396, 396, 396)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12))
                                .addGap(159, 159, 159)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(rentDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtbrand)
                                    .addComponent(txtVehicleNumber, 0, 232, Short.MAX_VALUE)
                                    .addComponent(txtFee, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(returnDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtCustNIC))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(resetBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(179, 179, 179)))
                        .addContainerGap(20, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1541, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(resetBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(16, 16, 16))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(txtVehicleNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(txtCustNIC)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtbrand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addComponent(rentDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jLabel11))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(returnDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(txtFee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(46, 46, 46)
                .addComponent(jLabel14)
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(733, 733, 733)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
  
    
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Customers c  = new Customers();
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
      
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        Return c  = new Return();
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO add your handling code here:
        int selectedIndex = onRentTbl.getSelectedRow();

if (selectedIndex == -1) {
    JOptionPane.showMessageDialog(this, "Please select a row to update.");
    return;
}

javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) onRentTbl.getModel();
int id = Integer.parseInt(model.getValueAt(selectedIndex, 0).toString()); // Primary key

try {
    String nic =txtCustNIC.getText().trim();
    String vehicleNo = (String) txtVehicleNumber.getSelectedItem();
    String brand = txtbrand.getText();
    Date rentDate = rentDateChooser.getDate();
    Date returnDate = returnDateChooser.getDate();
    String feeText = txtFee.getText().replace("LKR", "").replace(",", "").trim();
    int totalFee = Integer.parseInt(feeText);

    // Validation
    if (nic == null || vehicleNo == null || rentDate == null || returnDate == null || brand.isEmpty() || feeText.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields before updating.");
        return;
    }

    Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");

    pst = con.prepareStatement("UPDATE rental_records SET cust_nic=?, vehicle_no=?, brand=?, rent_date=?, return_date=?, total_fee=? WHERE id=?");

    pst.setString(1, nic);
    pst.setString(2, vehicleNo);
    pst.setString(3, brand);
    pst.setDate(4, new java.sql.Date(rentDate.getTime()));
    pst.setDate(5, new java.sql.Date(returnDate.getTime()));
    pst.setInt(6, totalFee);
    pst.setInt(7, id); // WHERE condition

    int rows = pst.executeUpdate();

    if (rows > 0) {
        JOptionPane.showMessageDialog(this, "Rental updated successfully!");
        loadRentalTable(); // Refresh table
        
    txtCustNIC.setText(""); 
    txtVehicleNumber.setSelectedIndex(-1);
    txtbrand.setText("");
    txtFee.setText("");
    rentDateChooser.setDate(null);
    returnDateChooser.setDate(null);// Clear fields
    
    } else {
        JOptionPane.showMessageDialog(this, "Update failed.");
    }

} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Error updating rental: " + e.getMessage());
}

    }//GEN-LAST:event_btnEditActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
    // TODO add your handling code here:
try {
    // Get values from form
    String nic =  txtCustNIC.getText().trim();
    String vehicleNo = (String) txtVehicleNumber.getSelectedItem();
    String brand = txtbrand.getText();
    Date rentDate = rentDateChooser.getDate();
    Date returnDate = returnDateChooser.getDate();

    String feeText = txtFee.getText().replace("LKR", "").replace(",", "").trim();
    int totalFee = Integer.parseInt(feeText);

    // Insert into your rental DB
    Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");

    pst = con.prepareStatement("INSERT INTO rental_records (cust_nic, vehicle_no, brand, rent_date, return_date, total_fee) VALUES (?, ?, ?, ?, ?, ?)");
    pst.setString(1, nic);
    pst.setString(2, vehicleNo);
    pst.setString(3, brand);
    pst.setDate(4, new java.sql.Date(rentDate.getTime()));
    pst.setDate(5, new java.sql.Date(returnDate.getTime()));
    pst.setInt(6, totalFee);
    pst.executeUpdate();

    // ✅ Also insert into on_rentrecords
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");
  PreparedStatement pst2 = con.prepareStatement("INSERT INTO on_rentrecords (cust_nic, vehicle_no, brand, rent_date, return_date, total_fee) VALUES (?, ?, ?, ?, ?, ?)");
    pst2.setString(1, nic);
    pst2.setString(2, vehicleNo);
    pst2.setString(3, brand);
    pst2.setDate(4, new java.sql.Date(rentDate.getTime()));
    pst2.setDate(5, new java.sql.Date(returnDate.getTime()));
    pst2.setInt(6, totalFee);
    pst2.executeUpdate();

    JOptionPane.showMessageDialog(this, "Rental saved and added to On-Rent records!");

} catch (Exception e) {
    JOptionPane.showMessageDialog(this, "Error saving rental: " + e.getMessage());
}

   

    }//GEN-LAST:event_saveBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
       DefaultTableModel model = (DefaultTableModel) onRentTbl.getModel();
int selectedIndex = onRentTbl.getSelectedRow();

if (selectedIndex == -1) {
    JOptionPane.showMessageDialog(this, "Please select a row first.");
    return;
}

// Ask for confirmation code
String confirmCode = JOptionPane.showInputDialog(this, "Enter delete confirmation code:");

if (confirmCode != null && confirmCode.equals("0000")) {
    int dialogResult = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this rental record?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);

    if (dialogResult == JOptionPane.YES_OPTION) {
        try {
            String nic = model.getValueAt(selectedIndex, 1).toString(); // assuming NIC is in column 0
            String vehicleNo = model.getValueAt(selectedIndex, 2).toString(); // assuming Vehicle No is in column 1

            // 1. Delete rental record
            Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");

            pst = con.prepareStatement("DELETE FROM rental_records WHERE cust_nic = ? AND vehicle_no = ?");
            pst.setString(1, nic);
            pst.setString(2, vehicleNo);

            int rowsDeleted = pst.executeUpdate();

            if (rowsDeleted > 0) {
                // 2. Update car availability to 'Yes'
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");
                PreparedStatement pst2 = con.prepareStatement("UPDATE carregistration SET Available = 'Yes' WHERE reg_no = ?");
                pst2.setString(1, vehicleNo);
                pst2.executeUpdate();

                JOptionPane.showMessageDialog(this, "Rental deleted and vehicle recovered successfully.");

                // Refresh table and vehicle list
                loadRentalTable();
                loadVehicleNumbers();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete rental.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error during deletion: " + e.getMessage());
        }
    }
} else {
    JOptionPane.showMessageDialog(this, "Invalid or cancelled confirmation code.");
}


    }//GEN-LAST:event_deleteBtnActionPerformed

    private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
        // TODO add your handling code here:
    txtCustNIC.setText("");       // Deselect NIC
    txtVehicleNumber.setSelectedIndex(-1); // Deselect vehicle
    txtbrand.setText("");                  // Clear brand
    txtFee.setText("");                    // Clear fee
    rentDateChooser.setDate(null);         // Clear rent date
    returnDateChooser.setDate(null);       // Clear return date
    }//GEN-LAST:event_resetBtnActionPerformed

    private void txtVehicleNumberActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtVehicleNumberActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtVehicleNumberActionPerformed

    private void txtFeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFeeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFeeActionPerformed

    private void onRentTblMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onRentTblMouseClicked
        // TODO add your handling code here:
        
        int selectedIndex = onRentTbl.getSelectedRow();

if (selectedIndex != -1) {
    javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) onRentTbl.getModel();

    String nic = model.getValueAt(selectedIndex, 1).toString();
    String vehicleNo = model.getValueAt(selectedIndex, 2).toString();
    String brand = model.getValueAt(selectedIndex, 3).toString();
    java.sql.Date rentDate = (java.sql.Date) model.getValueAt(selectedIndex, 4);
    java.sql.Date returnDate = (java.sql.Date) model.getValueAt(selectedIndex, 5);
    int fee = Integer.parseInt(model.getValueAt(selectedIndex, 6).toString());

txtCustNIC.setText(nic);
txtVehicleNumber.setSelectedItem(vehicleNo);
    txtbrand.setText(brand);
    rentDateChooser.setDate(rentDate);
    returnDateChooser.setDate(returnDate);

    // Format fee back to "LKR xxx"
    txtFee.setText("LKR " + fee);
}

    }//GEN-LAST:event_onRentTblMouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        // TODO add your handling code here:
        new Account().setVisible(true);

    // Close the current window (optional)
    this.dispose();
    }//GEN-LAST:event_jLabel9MouseClicked

    private void txtCustNICActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCustNICActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCustNICActionPerformed

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
            java.util.logging.Logger.getLogger(Rental.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Rental.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Rental.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Rental.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Rental().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable CarsTable;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
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
    private de.wannawork.jcalendar.JCalendarComboBox rentDateChooser;
    private javax.swing.JButton resetBtn;
    private de.wannawork.jcalendar.JCalendarComboBox returnDateChooser;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField txtCustNIC;
    private javax.swing.JTextField txtFee;
    private javax.swing.JComboBox<String> txtVehicleNumber;
    private javax.swing.JTextField txtbrand;
    // End of variables declaration//GEN-END:variables
}
