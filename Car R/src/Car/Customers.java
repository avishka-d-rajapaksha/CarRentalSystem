package Car;


import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.text.*;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

 

  

public class Customers extends javax.swing.JFrame {

    PreparedStatement insert;
    Connection con;
    PreparedStatement pst;
    ResultSet rs;

    public Customers() {
        initComponents();
        installSidebarUniversal();
        table_update();
         
    ((AbstractDocument) txtName.getDocument()).setDocumentFilter(new UppercaseDocumentFilter());// Apply to NAME field
    ((AbstractDocument) txtAddress.getDocument()).setDocumentFilter(new UppercaseDocumentFilter());// Apply to ADDRESS field
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
        lblNic = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        editBtn = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        txtNic = new javax.swing.JTextField();
        lblName = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        lblAddress = new javax.swing.JLabel();
        lblContact = new javax.swing.JLabel();
        txtContact = new javax.swing.JTextField();
        saveBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        resetBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1300, 750));
        setPreferredSize(new java.awt.Dimension(1368, 780));

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1366, 750));

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

        jButton3.setBackground(new java.awt.Color(204, 204, 255));
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
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        jLabel1.setText("Customer Management\n");

        lblNic.setFont(lblNic.getFont().deriveFont(lblNic.getFont().getStyle() | java.awt.Font.BOLD, lblNic.getFont().getSize()+6));
        lblNic.setForeground(new java.awt.Color(0, 0, 51));
        lblNic.setText("NIC");

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setBackground(new java.awt.Color(204, 204, 204));
        jTable1.setFont(jTable1.getFont().deriveFont(jTable1.getFont().getStyle() & ~java.awt.Font.BOLD, 16));
        jTable1.setForeground(new java.awt.Color(0, 0, 204));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
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
                "NIC", "NAME", "ADDRESS", "CONTACT"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        editBtn.setBackground(new java.awt.Color(0, 0, 51));
        editBtn.setFont(editBtn.getFont().deriveFont(editBtn.getFont().getStyle() | java.awt.Font.BOLD, editBtn.getFont().getSize()+6));
        editBtn.setForeground(new java.awt.Color(255, 255, 255));
        editBtn.setText("EDIT");
        editBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                editBtnMouseClicked(evt);
            }
        });
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
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

        lblName.setFont(lblName.getFont().deriveFont(lblName.getFont().getStyle() | java.awt.Font.BOLD, lblName.getFont().getSize()+6));
        lblName.setForeground(new java.awt.Color(0, 0, 51));
        lblName.setText("Name");

        lblAddress.setFont(lblAddress.getFont().deriveFont(lblAddress.getFont().getStyle() | java.awt.Font.BOLD, lblAddress.getFont().getSize()+6));
        lblAddress.setForeground(new java.awt.Color(0, 0, 51));
        lblAddress.setText("Address");

        lblContact.setFont(lblContact.getFont().deriveFont(lblContact.getFont().getStyle() | java.awt.Font.BOLD, lblContact.getFont().getSize()+6));
        lblContact.setForeground(new java.awt.Color(0, 0, 51));
        lblContact.setText("Contact");

        txtContact.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContactActionPerformed(evt);
            }
        });

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1043, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lblNic)
                                .addGap(203, 203, 203)
                                .addComponent(txtNic, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblName)
                                    .addComponent(lblAddress)
                                    .addComponent(lblContact))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtContact, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 253, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deleteBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(resetBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(165, 165, 165))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(428, 428, 428))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(389, 389, 389))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 1121, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblNic, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtNic))
                        .addGap(16, 16, 16)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblName)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(4, 4, 4)
                                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAddress)
                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblContact)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtContact, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(1, 1, 1))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(saveBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(editBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(resetBtn)
                        .addGap(13, 13, 13)
                        .addComponent(deleteBtn)))
                .addGap(32, 32, 32)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 463, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(333, 333, 333)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addContainerGap(99, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

       public void table_update() {
    try {
        int c;
        Class.forName("com.mysql.cj.jdbc.Driver");
       con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental","root","");

        insert = con.prepareStatement("SELECT * FROM custregestration");
        ResultSet rs = insert.executeQuery();
        ResultSetMetaData rsd = rs.getMetaData();
        c = rsd.getColumnCount();

        DefaultTableModel d = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Makes table read-only
            }
        };

        d.setColumnIdentifiers(new String[] { "NIC", "Name", "Address", "Contact" });
        jTable1.setModel(d);

        while (rs.next()) {
            Vector v2 = new Vector();
            for (int i = 1; i <= c; i++) {
                v2.add(rs.getString(i));
            }
            d.addRow(v2);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Availability c  = new Availability();
        c.setVisible(true);
        this.setVisible(false);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Rental c  = new Rental ();
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Customers c  = new Customers ();
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
         Cars c  = new Cars ();
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        // TODO add your handling code here:
       DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
int selectedIndex = jTable1.getSelectedRow();

if (selectedIndex == -1) {
    JOptionPane.showMessageDialog(this, "Please select a row in the table first.");
    return;
}

String originalNic = model.getValueAt(selectedIndex, 0).toString(); // old NIC

String nic = txtNic.getText().trim();
String name = txtName.getText().trim();
String address = txtAddress.getText().trim();
String contact = txtContact.getText().trim();

if (nic.isEmpty() || name.isEmpty() || address.isEmpty() || contact.isEmpty()) {
    JOptionPane.showMessageDialog(this, "All fields are required.");
    return;
}

try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental","root","");


    String sql = "UPDATE custregestration SET NIC = ?, Name = ?, Address = ?, Contact = ? WHERE NIC = ?";
    PreparedStatement pst = con.prepareStatement(sql);

    pst.setString(1, nic);          // new NIC
    pst.setString(2, name);
    pst.setString(3, address);
    pst.setString(4, contact);
    pst.setString(5, originalNic);  // match old NIC

    int result = pst.executeUpdate();

    if (result > 0) {
        JOptionPane.showMessageDialog(null, "Record updated successfully.");
        table_update(); // refresh the table
    } else {
        JOptionPane.showMessageDialog(null, "No record found with NIC: " + originalNic);
    }

    pst.close();
    con.close();

} catch (Exception e) {
    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
}


    }//GEN-LAST:event_editBtnActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
        // TODO add your handling code here:
          try {   
            String custnic =txtNic.getText();
            String name =txtName.getText();
            String address =txtAddress.getText();  
            String contact =txtContact.getText();  
       
            
            Class.forName("com.mysql.cj.jdbc.Driver");
con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental","root","");

            pst = con.prepareStatement("insert into custregestration (NIC,Name,Address,Contact)values(?,?,?,?)");
            pst.setString(1,custnic);
            pst.setString(2,name);
            pst.setString(3,address); 
            pst.setString(4,contact); 
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sucsessfully Saved");
             
             txtNic.setText("");
             txtName.setText("");
             txtAddress.setText("");
             txtContact.setText("");
          table_update();
            
          } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error loading table: " + e.getMessage());
    

          }
       
        
    }//GEN-LAST:event_saveBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:   DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
int selectedIndex = jTable1.getSelectedRow();

if (selectedIndex == -1) {
    JOptionPane.showMessageDialog(this, "Please select a record to delete.");
    return;
}

// Assuming NIC is in column 0
String nic = jTable1.getValueAt(selectedIndex, 0).toString();

String confirmCode = JOptionPane.showInputDialog(this, "Enter delete confirmation code:");

if (confirmCode != null && confirmCode.equals("0000")) {
    try {
        int dialogResult = JOptionPane.showConfirmDialog(
            this, "Are you sure you want to delete this record?", "Warning", JOptionPane.YES_NO_OPTION);

        if (dialogResult == JOptionPane.YES_OPTION) {
            Class.forName("com.mysql.cj.jdbc.Driver");
       con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental","root","");

            insert = con.prepareStatement("DELETE FROM custregestration WHERE nic = ?");
            insert.setString(1, nic); // Bind NIC to query
            insert.executeUpdate();

            JOptionPane.showMessageDialog(this, "Record deleted successfully.");
            table_update(); // Refresh table after deletion
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error occurred while deleting the record:\n" + e.getMessage());
    }
} else {
    JOptionPane.showMessageDialog(this, "Wrong code. Delete cancelled.");
}

    }//GEN-LAST:event_deleteBtnActionPerformed

    private void resetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_resetBtnActionPerformed
        // TODO add your handling code here:
    txtNic.setText("");
    txtName.setText("");
    txtAddress.setText("");
    txtContact.setText("");
    }//GEN-LAST:event_resetBtnActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
         Return c  = new Return ();
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void txtContactActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContactActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContactActionPerformed

    private void editBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_editBtnMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_editBtnMouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    int selectedIndex = jTable1.getSelectedRow();

    txtNic.setText(model.getValueAt(selectedIndex, 0).toString());
    txtName.setText(model.getValueAt(selectedIndex, 1).toString());
    txtAddress.setText(model.getValueAt(selectedIndex, 2).toString());
    txtContact.setText(model.getValueAt(selectedIndex, 3).toString());
   


    }//GEN-LAST:event_jTable1MouseClicked

    private void jLabel9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel9MouseClicked
        // TODO add your handling code here:
        new Account().setVisible(true);

    // Close the current window (optional)
    this.dispose();
    }//GEN-LAST:event_jLabel9MouseClicked

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
            java.util.logging.Logger.getLogger(Customers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Customers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Customers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Customers.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Customers().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblContact;
    private javax.swing.JLabel lblName;
    private javax.swing.JLabel lblNic;
    private javax.swing.JButton resetBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtContact;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtNic;
    // End of variables declaration//GEN-END:variables
}
