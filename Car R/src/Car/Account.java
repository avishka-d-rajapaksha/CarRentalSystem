package Car;

import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;

public class Account extends javax.swing.JFrame {

    /**
     * Creates new form Account
     */
    public Account() {
        initComponents();
        installSidebarUniversal();

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
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
        jButton7 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        AddNew = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        id = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        password1 = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        password2 = new javax.swing.JTextField();
        Clear = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));
        jPanel1.setMaximumSize(new java.awt.Dimension(1366, 750));
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

        jLabel9.setBackground(java.awt.SystemColor.activeCaption);
        jLabel9.setFont(jLabel9.getFont().deriveFont(jLabel9.getFont().getStyle() | java.awt.Font.BOLD, jLabel9.getFont().getSize()+6));
        jLabel9.setForeground(java.awt.SystemColor.activeCaption);
        jLabel9.setText("ACCOUNT");

        jLabel10.setFont(jLabel10.getFont().deriveFont(jLabel10.getFont().getStyle() | java.awt.Font.BOLD, jLabel10.getFont().getSize()+6));
        jLabel10.setForeground(new java.awt.Color(204, 204, 204));
        jLabel10.setText("LOGOUT");
        jLabel10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel10MouseClicked(evt);
            }
        });

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
                            .addComponent(jButton4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 204));
        jLabel1.setText("Accounts Setting");

        jButton7.setBackground(new java.awt.Color(0, 0, 51));
        jButton7.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jButton7.setForeground(new java.awt.Color(230, 0, 52));
        jButton7.setText("Reset");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        AddNew.setBackground(new java.awt.Color(0, 0, 51));
        AddNew.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        AddNew.setForeground(new java.awt.Color(255, 255, 255));
        AddNew.setText("Add New");
        AddNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddNewActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("New user id");

        id.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                idKeyPressed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Password");

        password1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                password1ActionPerformed(evt);
            }
        });
        password1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                password1KeyPressed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Confirm Password");

        password2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                password2KeyPressed(evt);
            }
        });

        Clear.setBackground(new java.awt.Color(0, 0, 51));
        Clear.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Clear.setForeground(new java.awt.Color(255, 255, 255));
        Clear.setText("Clear");
        Clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ClearActionPerformed(evt);
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 503, Short.MAX_VALUE)
                        .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(428, 428, 428))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(279, 279, 279)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(id)
                            .addComponent(password1)
                            .addComponent(password2))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(180, 180, 180)
                                .addComponent(AddNew, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(Clear, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel1)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 2246, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(56, 56, 56)
                .addComponent(jLabel5)
                .addGap(22, 22, 22)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(id, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(password1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(password2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AddNew, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Clear, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1730, 1730, 1730)
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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
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
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Customers c  = new Customers();
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
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
         Cars c  = new Cars();
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        Return c  = new Return();
        c.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton7ActionPerformed

    private void AddNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddNewActionPerformed
      // Add new user
      String Ids, Pword1, Pword2, query;
String SUrl, SUser, Spass;

SUrl = "jdbc:mysql://localhost:3306/car_rental"; // lowercase 'mysql' and correct DB name
SUser = "root";
Spass = "";


try {
    Class.forName("com.mysql.cj.jdbc.Driver");
    Connection con = DriverManager.getConnection(SUrl, SUser, Spass);
    Statement st = con.createStatement();

    if ("".equals(id.getText())) {
        JOptionPane.showMessageDialog(new JFrame(), "User id is required", "Error", JOptionPane.ERROR_MESSAGE);
    } else if ("".equals(password1.getText())) {
        JOptionPane.showMessageDialog(new JFrame(), "Password is required", "Error", JOptionPane.ERROR_MESSAGE);
    } else if ("".equals(password2.getText())) {
        JOptionPane.showMessageDialog(new JFrame(), "Confirm Password is required", "Error", JOptionPane.ERROR_MESSAGE);
    } else {
        Ids = id.getText();
        Pword1 = password1.getText();
        Pword2 = password2.getText();

        if (!Pword1.equals(Pword2)) {
            JOptionPane.showMessageDialog(new JFrame(), "Passwords do not match", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // Passwords match, insert into database
            query = "INSERT INTO user(ID, Password) VALUES('" + Ids + "','" + Pword1 + "')";
            st.execute(query);

            // Clear input fields
            id.setText("");
            password1.setText("");
            password2.setText("");

            JOptionPane.showMessageDialog(null, "New user account has been created successfully!");
        }
    }

} catch (Exception e) {
    System.out.println("Error! " + e.getMessage());
}
    }//GEN-LAST:event_AddNewActionPerformed

    private void password1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_password1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_password1ActionPerformed

    private void idKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idKeyPressed
        // TODO add your handling code here:
           int key=evt.getKeyCode();
        if (key==10){
            password1.requestFocus();
        }
   
    }//GEN-LAST:event_idKeyPressed

    private void password1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_password1KeyPressed
           int key=evt.getKeyCode();
        if (key==10){
            password2.requestFocus();
        }
        
    }//GEN-LAST:event_password1KeyPressed

    private void password2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_password2KeyPressed
        // TODO add your handling code here:                                    
    int key = evt.getKeyCode();
    if (key == KeyEvent.VK_ENTER) {
        AddNew.doClick(); // Simulates pressing the Add New button
            }

    }//GEN-LAST:event_password2KeyPressed

    private void ClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ClearActionPerformed
        // TODO add your handling code here:
    id.setText("");
    password1.setText("");
    password2.setText("");


    }//GEN-LAST:event_ClearActionPerformed

    private void jLabel10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel10MouseClicked
        // TODO add your handling code here:
         new Login().setVisible(true);

    // Close the current window (optional)
    this.dispose();
    }//GEN-LAST:event_jLabel10MouseClicked

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
            java.util.logging.Logger.getLogger(Account.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Account.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Account.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Account.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Account().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddNew;
    private javax.swing.JButton Clear;
    private javax.swing.JTextField id;
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
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField password1;
    private javax.swing.JTextField password2;
    // End of variables declaration//GEN-END:variables
}
