package Car;


import Car.UppercaseDocumentFilter;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.AbstractDocument;

public class Cars extends javax.swing.JFrame {
    
    
    PreparedStatement insert;
    Connection con;
    
    public Cars() {
        initComponents();
    installSidebarUniversal();
    __sb_fitSidebarToWindow();
    installSideBar();
    __sb_restoreSidebarColors();
        table_update();
        ((AbstractDocument) txtregno.getDocument()).setDocumentFilter(new UppercaseDocumentFilter());
        ((AbstractDocument) txtbrand.getDocument()).setDocumentFilter(new UppercaseDocumentFilter());
        ((AbstractDocument) txtmodel.getDocument()).setDocumentFilter(new UppercaseDocumentFilter());
      

        
        jTable1.setDefaultEditor(Object.class, null);
        
        setPlaceholderWithTyping(txtregno, "  ABC-1234  ");
    }
    // === EASY PATCH: keep ACCOUNT / LOGOUT visible by fitting sidebar height ===
private void __sb_fitSidebarToWindow() {
    // Ensure labels exist (no style change)
    try {
        javax.swing.JPanel side = __sb_findSidebarPanel();
        if (side == null) return;
        __sb_ensureLabel(side, "ACCOUNT");
        __sb_ensureLabel(side, "LOGOUT");

        final java.awt.Container parent = side.getParent();
        if (parent == null) return;

        final Runnable apply = new Runnable() {
            public void run() {
                try {
                    int h = parent.getHeight();
                    if (h <= 0) h = getHeight();
                    if (h <= 0) h = 768; // fallback
                    int w = side.getWidth() > 0 ? side.getWidth() : 290;

                    if (parent.getLayout() == null) {
                        // Absolute (null) layout: force bounds (overrides 1515 height)
                        side.setBounds(side.getX(), side.getY(), w, h);
                    } else {
                        // Managed layout: hint with preferred size
                        side.setPreferredSize(new java.awt.Dimension(w, h));
                        parent.revalidate();
                    }
                    side.revalidate();
                    side.repaint();
                } catch (Throwable ignore) {}
            }
        };

        // Run now, on resize, and after window opens
        javax.swing.SwingUtilities.invokeLater(apply);
        parent.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override public void componentResized(java.awt.event.ComponentEvent e) { apply.run(); }
        });
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowOpened(java.awt.event.WindowEvent e) { apply.run(); }
        });
    } catch (Throwable t) {
        t.printStackTrace();
    }
}
    // === Sidebar Color Restore + Back-compat adapter (paste once per JFrame) ===

// 1) If some old code calls installSideBar(...), route it to your universal method.
private void installSideBar() {
    try { installSidebarUniversal(); } catch (Throwable t) { t.printStackTrace(); }
}
private void installSideBar(String activePage) {
    installSideBar(); // active page is auto-detected by your universal code
}
private void installSideBar(javax.swing.JPanel sidePanel, javax.swing.JFrame owner, String activePage) {
    installSideBar(); // ignore args; universal code already handles everything
}

// 2) Restore your exact colors: make all non-active buttons blue (102,102,255);
//    then re-apply the active highlight only to the current page.
private void __sb_restoreSidebarColors() {
    try {
        javax.swing.JPanel sidebar = __sb_findSidebarPanel();
        if (sidebar == null) return;

        final java.awt.Color BTN_BG = new java.awt.Color(102, 102, 255); // your original blue
        final String[] names = { "AVAILABILITY", "CUSTOMERS", "RENT", "RETURN", "CARS", "REPORTS" };

        java.util.Map<String, javax.swing.JButton> map = new java.util.LinkedHashMap<>();
        for (String n : names) {
            javax.swing.JButton b = __sb_findButton(sidebar, n);
            if (b != null) {
                // normalize to your design
                b.setOpaque(true);
                b.setBackground(BTN_BG);
                b.setForeground(java.awt.Color.WHITE);
                b.setFocusPainted(false);
                if (b.getBorder() == null || !(b.getBorder() instanceof javax.swing.border.BevelBorder)) {
                    b.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
                }
                map.put(n, b);
                b.setEnabled(true); // re-enable; active page will be handled below
            }
        }

        // Lighten only the active page (keeps your existing __sb_setActive behavior)
        String active = __sb_detectActive();
        for (java.util.Map.Entry<String, javax.swing.JButton> e : map.entrySet()) {
            __sb_setActive(e.getValue(), e.getKey().equals(active));
        }

        // Make sure the panel uses your sidebar background too
        sidebar.setBackground(new java.awt.Color(0, 0, 51));
        sidebar.revalidate();
        sidebar.repaint();
    } catch (Throwable t) {
        t.printStackTrace();
    }
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
// ===== END: Add REPORTS under CARS (keep exact sidebar design) =====
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
        ModifyTb = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        EditBtn = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtmodel = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        SaveBtn = new javax.swing.JButton();
        DeleteBtn = new javax.swing.JButton();
        ResetBtn = new javax.swing.JButton();
        txtbrand = new javax.swing.JTextField();
        txtregno = new javax.swing.JTextField();
        txtrental = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        availability = new javax.swing.JComboBox<>();

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

        ModifyTb.setBackground(new java.awt.Color(204, 204, 255));
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
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(ModifyTb, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel9)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabel10)))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addGap(6, 6, 6)
                                    .addComponent(jLabel2))))
                        .addGap(99, 99, 99))))
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
                .addGap(18, 18, 18)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 759, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(57, 57, 57))
        );

        jPanel1.add(jPanel2);
        jPanel2.setBounds(0, 0, 290, 1515);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Cars Management");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(700, 0, 204, 32);

        jLabel5.setFont(jLabel5.getFont().deriveFont(jLabel5.getFont().getStyle() | java.awt.Font.BOLD, jLabel5.getFont().getSize()+6));
        jLabel5.setForeground(new java.awt.Color(0, 0, 51));
        jLabel5.setText("Brand");
        jPanel1.add(jLabel5);
        jLabel5.setBounds(330, 130, 230, 25);

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setBackground(new java.awt.Color(204, 204, 204));
        jTable1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 16)); // NOI18N
        jTable1.setForeground(new java.awt.Color(0, 0, 204));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Reg. Number", "Brand", "Model", "Rental (24h)", "Availability"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(308, 351, 1034, 392);

        EditBtn.setBackground(new java.awt.Color(0, 0, 51));
        EditBtn.setFont(EditBtn.getFont().deriveFont(EditBtn.getFont().getStyle() | java.awt.Font.BOLD, EditBtn.getFont().getSize()+6));
        EditBtn.setForeground(new java.awt.Color(255, 255, 255));
        EditBtn.setText("EDIT");
        EditBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditBtnActionPerformed(evt);
            }
        });
        jPanel1.add(EditBtn);
        EditBtn.setBounds(1110, 150, 145, 39);

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
        jLabel6.setText("Reg. Number");
        jPanel1.add(jLabel6);
        jLabel6.setBounds(330, 90, 230, 30);

        txtmodel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtmodelKeyPressed(evt);
            }
        });
        jPanel1.add(txtmodel);
        txtmodel.setBounds(630, 170, 232, 30);

        jLabel7.setFont(jLabel7.getFont().deriveFont(jLabel7.getFont().getStyle() | java.awt.Font.BOLD, jLabel7.getFont().getSize()+6));
        jLabel7.setForeground(new java.awt.Color(0, 0, 51));
        jLabel7.setText("Model");
        jPanel1.add(jLabel7);
        jLabel7.setBounds(330, 170, 230, 25);

        SaveBtn.setBackground(new java.awt.Color(0, 0, 51));
        SaveBtn.setFont(SaveBtn.getFont().deriveFont(SaveBtn.getFont().getStyle() | java.awt.Font.BOLD, SaveBtn.getFont().getSize()+6));
        SaveBtn.setForeground(new java.awt.Color(255, 255, 255));
        SaveBtn.setText("SAVE");
        SaveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveBtnActionPerformed(evt);
            }
        });
        jPanel1.add(SaveBtn);
        SaveBtn.setBounds(1110, 80, 145, 50);

        DeleteBtn.setBackground(new java.awt.Color(0, 0, 51));
        DeleteBtn.setFont(DeleteBtn.getFont().deriveFont(DeleteBtn.getFont().getStyle() | java.awt.Font.BOLD, DeleteBtn.getFont().getSize()+6));
        DeleteBtn.setForeground(new java.awt.Color(255, 255, 255));
        DeleteBtn.setText("DELETE");
        DeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeleteBtnActionPerformed(evt);
            }
        });
        jPanel1.add(DeleteBtn);
        DeleteBtn.setBounds(1110, 270, 145, 39);

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
        ResetBtn.setBounds(1110, 210, 145, 39);

        txtbrand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtbrandActionPerformed(evt);
            }
        });
        txtbrand.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtbrandKeyPressed(evt);
            }
        });
        jPanel1.add(txtbrand);
        txtbrand.setBounds(630, 130, 232, 30);

        txtregno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtregnoKeyPressed(evt);
            }
        });
        jPanel1.add(txtregno);
        txtregno.setBounds(630, 90, 232, 30);

        txtrental.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtrentalActionPerformed(evt);
            }
        });
        txtrental.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtrentalKeyPressed(evt);
            }
        });
        jPanel1.add(txtrental);
        txtrental.setBounds(630, 210, 232, 30);

        jLabel13.setFont(jLabel13.getFont().deriveFont(jLabel13.getFont().getStyle() | java.awt.Font.BOLD, jLabel13.getFont().getSize()+6));
        jLabel13.setForeground(new java.awt.Color(0, 0, 51));
        jLabel13.setText("One day Rental");
        jPanel1.add(jLabel13);
        jLabel13.setBounds(330, 210, 230, 25);

        jLabel12.setFont(jLabel12.getFont().deriveFont(jLabel12.getFont().getStyle() | java.awt.Font.BOLD, jLabel12.getFont().getSize()+6));
        jLabel12.setForeground(new java.awt.Color(0, 0, 51));
        jLabel12.setText("Available");
        jPanel1.add(jLabel12);
        jLabel12.setBounds(330, 250, 110, 25);

        availability.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Yes", "No" }));
        jPanel1.add(availability);
        availability.setBounds(630, 250, 120, 30);

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
                .addGap(254, 254, 254)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
public void setPlaceholderWithTyping(JTextField textField, String placeholder) {
    textField.setText(placeholder);
    textField.setForeground(Color.GRAY);

    textField.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent e) {
            if (textField.getText().equals(placeholder)) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent e) {
            if (textField.getText().isEmpty()) {
                textField.setText(placeholder);
                textField.setForeground(Color.GRAY);
            }
        }
    });

    textField.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyTyped(java.awt.event.KeyEvent e) {
            if (textField.getForeground() == Color.GRAY) {
                textField.setText("");
                textField.setForeground(Color.BLACK);
            }
        }
    });
}



   private void table_update() {
    int CC;
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");
        insert = con.prepareStatement("SELECT * FROM carregistration");
        ResultSet Rs = insert.executeQuery();

        ResultSetMetaData RSMD = Rs.getMetaData();
        CC = RSMD.getColumnCount();
        DefaultTableModel DFT = (DefaultTableModel) jTable1.getModel();
        DFT.setRowCount(0); // Clear existing rows

        while (Rs.next()) {
            Vector v2 = new Vector();

            // Add each column only ONCE, not in a loop
            v2.add(Rs.getString("reg_no"));
            v2.add(Rs.getString("brand"));
            v2.add(Rs.getString("model"));
            v2.add(Rs.getString("rental"));
            v2.add(Rs.getString("Available"));

            DFT.addRow(v2);
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error loading table: " + e.getMessage());
        }
    

}private void clearFields() {
    txtregno.setText("");
    txtbrand.setText("");
    txtmodel.setText("");
    txtrental.setText("");
    availability.setSelectedIndex(0);
    }

    private void ResetBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ResetBtnActionPerformed
        // TODO add your handling code here:
    txtregno.setText("");
    txtbrand.setText("");
    txtmodel.setText("");
    txtrental.setText("");
   
    }//GEN-LAST:event_ResetBtnActionPerformed

    private void DeleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeleteBtnActionPerformed
        // TODO add your handling code here:
       DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
int selectedIndex = jTable1.getSelectedRow();

if (selectedIndex == -1) {
    JOptionPane.showMessageDialog(this, "Please select a record to delete.");
    return;
}

String confirmCode = JOptionPane.showInputDialog(this, "Enter delete confirmation code:");

if (confirmCode != null && confirmCode.equals("0000")) {
    try {
        String id = model.getValueAt(selectedIndex, 0).toString(); // reg_no assumed in column 0

        int dialogResult = JOptionPane.showConfirmDialog(
            null, "Are you sure you want to delete this record?", "Warning", JOptionPane.YES_NO_OPTION);

        if (dialogResult == JOptionPane.YES_OPTION) {
            Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");
            insert = con.prepareStatement("DELETE FROM carregistration WHERE reg_no = ?");
            insert.setString(1, id);
            insert.executeUpdate();

            JOptionPane.showMessageDialog(this, "Record deleted successfully.");
            table_update(); // refresh table
        }

    } catch (ClassNotFoundException | SQLException ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error occurred while deleting the record.");
    }

    } else {
         JOptionPane.showMessageDialog(this, "Wrong code. Delete cancelled.");
    }
    }//GEN-LAST:event_DeleteBtnActionPerformed

    private void SaveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveBtnActionPerformed
        // TODO add your handling code here:
     try {   
            String regno =txtregno.getText();
            String brand =txtbrand.getText();
            String model =txtmodel.getText();
            String rental =txtrental.getText();        
            String available = availability.getSelectedItem().toString();   
            
            Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");
            insert = con.prepareStatement("insert into carregistration(reg_no,brand,model,rental,Available)values(?,?,?,?,?)");
            insert.setString(1,regno);
            insert.setString(2,brand);
            insert.setString(3,model);
            insert.setString(4,rental);
            insert.setString(5,available);
            
            insert.executeUpdate();
            JOptionPane.showMessageDialog(this, "Sucsessfully Saved");
            
     
             txtregno.setText("");
             txtbrand.setText("");
             txtmodel.setText("");
             txtrental.setText("");
             availability.getSelectedItem().toString(); 
             table_update(); 
           
                
                
             }catch (Exception e) {
            System.out.println("oky" + e.getMessage());
 }

    }//GEN-LAST:event_SaveBtnActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void EditBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditBtnActionPerformed
        // TODO add your handling code here:
       
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    int selectedIndex = jTable1.getSelectedRow();

    if (selectedIndex == -1) {
        JOptionPane.showMessageDialog(this, "Please select a row in the table first.");
        return;
    }

    String originalRegNo = model.getValueAt(selectedIndex, 0).toString(); // original reg_no from table

    String regNo = txtregno.getText().trim();
    String brand = txtbrand.getText().trim();
    String modelCar = txtmodel.getText().trim();
    String rental = txtrental.getText().trim();
    String available = availability.getSelectedItem().toString();

    if (regNo.isEmpty() || brand.isEmpty() || modelCar.isEmpty() || rental.isEmpty()) {
        JOptionPane.showMessageDialog(this, "All fields are required.");
        return;
    }

    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/car_rental", "root", "");

        String sql = "UPDATE carregistration SET reg_no = ?, brand = ?, model = ?, rental = ?, available = ? WHERE reg_no = ?";
        PreparedStatement pst = con.prepareStatement(sql);

        pst.setString(1, regNo);
        pst.setString(2, brand);
        pst.setString(3, modelCar);
        pst.setString(4, rental);
        pst.setString(5, available);
        pst.setString(6, originalRegNo); // use original reg_no in WHERE

        int result = pst.executeUpdate();

        if (result > 0) {
            JOptionPane.showMessageDialog(this, "Car details updated successfully.");
            table_update();  // refresh table
            clearFields();   // reset fields
        } else {
            JOptionPane.showMessageDialog(this, "Update failed.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
    }

    }//GEN-LAST:event_EditBtnActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        Return c  = new Return();
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void ModifyTbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModifyTbActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ModifyTbActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Customers c  = new Customers();
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Rental c  = new Rental();
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Availability c  = new Availability();
        c.setVisible(true);
        this.setVisible(false);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtbrandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtbrandActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtbrandActionPerformed

    private void txtrentalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtrentalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtrentalActionPerformed

    private void txtregnoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtregnoKeyPressed
        // TODO add your handling code here:
        int key=evt.getKeyCode();
        if (key==10){
            txtbrand.requestFocus();
        }
    }//GEN-LAST:event_txtregnoKeyPressed

    private void txtbrandKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbrandKeyPressed
        // TODO add your handling code here:
        int key=evt.getKeyCode();
        if (key==10){
            txtmodel.requestFocus();
        }
    }//GEN-LAST:event_txtbrandKeyPressed

    private void txtmodelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtmodelKeyPressed
        // TODO add your handling code here:
        int key=evt.getKeyCode();
        if (key==10){
            txtrental.requestFocus();
        }
    }//GEN-LAST:event_txtmodelKeyPressed

    private void txtrentalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtrentalKeyPressed
        // TODO add your handling code here:
        int key=evt.getKeyCode();
        if (key==10){
            
        }
        
    }//GEN-LAST:event_txtrentalKeyPressed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
  
    DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
    int selectedIndex = jTable1.getSelectedRow();

    txtregno.setText(model.getValueAt(selectedIndex, 0).toString());
    txtbrand.setText(model.getValueAt(selectedIndex, 1).toString());
    txtmodel.setText(model.getValueAt(selectedIndex, 2).toString());
    txtrental.setText(model.getValueAt(selectedIndex, 3).toString());
    availability.setSelectedItem(model.getValueAt(selectedIndex, 4).toString());


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
            java.util.logging.Logger.getLogger(Cars.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cars.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cars.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cars.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cars().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton DeleteBtn;
    private javax.swing.JButton EditBtn;
    private javax.swing.JButton ModifyTb;
    private javax.swing.JButton ResetBtn;
    private javax.swing.JButton SaveBtn;
    private javax.swing.JComboBox<String> availability;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField txtbrand;
    private javax.swing.JTextField txtmodel;
    private javax.swing.JTextField txtregno;
    private javax.swing.JTextField txtrental;
    // End of variables declaration//GEN-END:variables
}
