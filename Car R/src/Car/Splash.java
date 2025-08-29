
package Car;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Splash extends JFrame {

    // Image paths (classpath preferred; absolute is fallback)
    private static final String CLASSPATH_IMAGE = "/ImageR/car.jpg";
    private static final String ABSOLUTE_IMAGE_PATH =
            "C:\\Users\\LENOVO\\OneDrive\\Desktop\\CRMS-Group 09\\Car R\\src\\ImageR\\car.jpg";

    // Durations (ms)
    private static final int TITLE_MS     = 900;
    private static final int PROGRESS_MS  = 3800;

    // Colors (modern, high-contrast)
    private static final Color TITLE_COLOR   = new Color(236, 242, 255); // near-white
    private static final Color TITLE_SHADOW  = new Color(0, 0, 0, 160);  // soft shadow
    private static final Color TRACK_BG      = new Color(14, 14, 16);    // bar track
    private static final Color TRACK_BORDER  = new Color(40, 40, 44);    // track border
    private static final Color FILL_START    = new Color(243, 156, 18);  // amber
    private static final Color FILL_END      = new Color(46, 204, 113);  // green
    private static final Color TEXT_COLOR    = new Color(245, 248, 255);

    // UI
    private BackgroundPanel bgPanel;   // static background image
    private JLabel titleLabel;
    private JLabel titleShadow;
    private FancyProgressBar progressBar;

    // Layout numbers
    private int targetTitleY;
    private int startTitleY;

    public Splash() {
        initUI();
    }

    private void initUI() {
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(null);

        Dimension scr = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, scr.width, scr.height);

        // Static background image (no animation)
        Image bg = loadSplashImage();
        bgPanel = new BackgroundPanel(bg);
        bgPanel.setBounds(0, 0, scr.width, scr.height);
        add(bgPanel);

        // Title
        int titleFontSize = Math.max(42, scr.height / 12);
        int titleW = Math.min((int) (scr.width * 0.8), 900);
        int titleH = titleFontSize + 10;
        int titleX = (scr.width - titleW) / 2;
        targetTitleY = Math.max(20, scr.height / 10);
        startTitleY = -titleH;

        titleLabel = new JLabel("CRMS", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, titleFontSize));
        titleLabel.setForeground(new Color(TITLE_COLOR.getRed(), TITLE_COLOR.getGreen(), TITLE_COLOR.getBlue(), 0));
        titleLabel.setOpaque(false);
        titleLabel.setBounds(titleX, startTitleY, titleW, titleH);
        add(titleLabel);

        titleShadow = new JLabel("CRMS", SwingConstants.CENTER);
        titleShadow.setFont(titleLabel.getFont());
        titleShadow.setForeground(new Color(TITLE_SHADOW.getRed(), TITLE_SHADOW.getGreen(), TITLE_SHADOW.getBlue(), 0));
        titleShadow.setOpaque(false);
        titleShadow.setBounds(titleX + 2, startTitleY + 3, titleW, titleH);
        add(titleShadow);

        // Fancy progress bar
        int barW = Math.min((int) (scr.width * 0.7), 900);
        int barH = Math.max(10, scr.height / 90);
        int barX = (scr.width - barW) / 2;
        int barY = scr.height - barH - Math.max(24, scr.height / 40);

        progressBar = new FancyProgressBar(0, 100);
        progressBar.setBounds(barX, barY, barW, barH);
        add(progressBar);

        // Keep background behind everything
        Container root = getContentPane();
        root.setComponentZOrder(bgPanel, root.getComponentCount() - 1);
    }

    private Image loadSplashImage() {
        // Try classpath first
        try {
            java.net.URL url = getClass().getResource(CLASSPATH_IMAGE);
            if (url != null) return new ImageIcon(url).getImage();
        } catch (Exception ignore) {}

        // Fallback to absolute path
        File f = new File(ABSOLUTE_IMAGE_PATH);
        if (f.isFile()) return new ImageIcon(ABSOLUTE_IMAGE_PATH).getImage();

        // Fallback placeholder if image is missing
        BufferedImage img = new BufferedImage(1280, 720, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        g2.setPaint(new GradientPaint(0, 0, new Color(20, 20, 30), 0, 720, new Color(5, 5, 10)));
        g2.fillRect(0, 0, 1280, 720);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("SansSerif", Font.BOLD, 48));
        g2.drawString("Missing: car.jpg", 50, 100);
        g2.dispose();
        return img;
    }

    public void startAnimations() {
        // Show immediately so the photo is visible from the first frame
        setVisible(true);

        animateTitle();

        // Start the progress bar's visual animation (stripes/glow)
        progressBar.startVisuals();

        // Simulated loading to 100% (if you have real loading, update progressBar.setValue())
        final long start = System.currentTimeMillis();
        final Timer fillTimer = new Timer(30, null);
        fillTimer.addActionListener(e -> {
            long elapsed = System.currentTimeMillis() - start;
            float t = Math.min(1f, elapsed / (float) PROGRESS_MS);
            int value = Math.min(100, Math.round(100 * t));
            progressBar.setValue(value);
            progressBar.setText("Loading... " + value + "%");

            if (value >= 100) {
                fillTimer.stop();
                // brief pause to let the bar settle
                new Timer(200, ee -> {
                    ((Timer) ee.getSource()).stop();
                    progressBar.stopVisuals();
                    try { new Login().setVisible(true); } catch (Throwable ignore) {}
                    dispose();
                }).start();
            }
        });
        fillTimer.start();
    }

    private void animateTitle() {
        final long start = System.currentTimeMillis();
        Timer timer = new Timer(16, null);
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - start;
                float t = Math.min(1f, elapsed / (float) TITLE_MS);
                float s = easeOutBack(t);

                int y = (int) Math.round(startTitleY + (targetTitleY - startTitleY) * s);
                titleLabel.setLocation(titleLabel.getX(), y);
                titleShadow.setLocation(titleShadow.getX(), y + 3);

                int alpha = (int) (255 * t);
                titleLabel.setForeground(new Color(TITLE_COLOR.getRed(), TITLE_COLOR.getGreen(), TITLE_COLOR.getBlue(), alpha));
                titleShadow.setForeground(new Color(TITLE_SHADOW.getRed(), TITLE_SHADOW.getGreen(), TITLE_SHADOW.getBlue(), alpha));

                if (t >= 1f) timer.stop();
            }
        });
        timer.start();
    }

    // Easing
    private static float easeOutBack(float t) {
        float c1 = 1.70158f, c3 = c1 + 1;
        return 1 + c3 * (float) Math.pow(t - 1, 3) + c1 * (float) Math.pow(t - 1, 2);
    }

    // Static background renderer (no animation)
    private static final class BackgroundPanel extends JComponent {
        private final Image image;

        BackgroundPanel(Image image) {
            this.image = image;
            setDoubleBuffered(true);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, w, h);

            if (image != null) {
                int iw = image.getWidth(this);
                int ih = image.getHeight(this);
                if (iw > 0 && ih > 0) {
                    double scale = Math.max(w / (double) iw, h / (double) ih); // cover
                    int sw = (int) Math.round(iw * scale);
                    int sh = (int) Math.round(ih * scale);
                    int x = (w - sw) / 2;
                    int y = (h - sh) / 2;
                    g2.drawImage(image, x, y, sw, sh, this);
                }
            }
            g2.dispose();
        }
    }

    // Custom, highly-styled progress bar (rounded, gradient, stripes, glow)
    private static final class FancyProgressBar extends JProgressBar {
        private String text = "";
        private Timer animTimer;
        private float stripeOffset = 0f;  // moves the diagonal stripes
        private float glowPhase   = 0f;   // pulses the glow

        FancyProgressBar(int min, int max) {
            super(min, max);
            setOpaque(false);
        }

        void setText(String s) {
            this.text = s != null ? s : "";
            repaint();
        }

        void startVisuals() {
            if (animTimer != null && animTimer.isRunning()) return;
            animTimer = new Timer(16, e -> {
                stripeOffset += 1.4f;     // speed of stripe movement
                if (stripeOffset > 1000f) stripeOffset = 0f;
                glowPhase += 0.04f;       // speed of glow pulse
                repaint();
            });
            animTimer.start();
        }

        void stopVisuals() {
            if (animTimer != null) animTimer.stop();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int arc = h;                 // capsule shape
            int pad = 2;                 // inner padding for border
            int x = 0, y = 0;

            // Track shadow (soft)
            g2.setColor(new Color(0, 0, 0, 90));
            g2.fillRoundRect(x, y + 2, w, h, arc, arc);

            // Track background
            g2.setColor(TRACK_BG);
            g2.fillRoundRect(x, y, w, h, arc, arc);

            // Track border
            g2.setStroke(new BasicStroke(1f));
            g2.setColor(TRACK_BORDER);
            g2.drawRoundRect(x, y, w - 1, h - 1, arc, arc);

            // Progress fraction
            float f = getMaximum() > getMinimum()
                    ? (getValue() - getMinimum()) / (float) (getMaximum() - getMinimum())
                    : 0f;
            f = Math.max(0f, Math.min(1f, f));

            // Fill width (ensure nice rounded cap when very small)
            int fillW = Math.max(0, Math.round((w - pad * 2) * f));
            int fillX = x + pad;
            int fillY = y + pad;
            int fillH = h - pad * 2;
            int fillArc = fillH;

            if (fillW > 0) {
                // Gradient color across the fill
                Color base = lerpColor(FILL_START, FILL_END, f);
                Color top  = lighten(base, 0.18f);
                Color mid  = base;
                Color bot  = darken(base, 0.16f);

                // Vertical gradient for fill
                Paint vert = new LinearGradientPaint(
                        fillX, fillY, fillX, fillY + fillH,
                        new float[]{0f, 0.5f, 1f},
                        new Color[]{top, mid, bot}
                );

                // Clip to rounded fill shape
                Shape fillShape = new RoundRectangle2D.Float(fillX, fillY, Math.max(fillW, Math.min(fillH, fillW)), fillH, fillArc, fillArc);
                Area fillArea = new Area(fillShape);
                fillArea.intersect(new Area(new Rectangle2D.Float(fillX, fillY, fillW, fillH)));

                g2.setPaint(vert);
                g2.fill(fillArea);

                // Moving diagonal stripes (subtle)
                Shape oldClip = g2.getClip();
                g2.setClip(fillArea);
                g2.setComposite(AlphaComposite.SrcOver.derive(0.15f));
                g2.setColor(Color.WHITE);
                int stripeW = 10;
                int stripeG = 14;
                float off = stripeOffset % (stripeW + stripeG);
                for (int sx = -h; sx < fillX + fillW + h; sx += (stripeW + stripeG)) {
                    float x0 = sx + off;
                    Path2D.Float p = new Path2D.Float();
                    p.moveTo(x0, fillY);
                    p.lineTo(x0 + stripeW, fillY);
                    p.lineTo(x0 + stripeW - fillH, fillY + fillH);
                    p.lineTo(x0 - fillH, fillY + fillH);
                    p.closePath();
                    g2.fill(p);
                }
                g2.setClip(oldClip);
                g2.setComposite(AlphaComposite.SrcOver);

                // Glossy highlight on the top half
                Paint gloss = new LinearGradientPaint(
                        fillX, fillY, fillX, fillY + fillH / 2f,
                        new float[]{0f, 1f},
                        new Color[]{new Color(255, 255, 255, 110), new Color(255, 255, 255, 0)}
                );
                g2.setPaint(gloss);
                Area topHalf = new Area(new RoundRectangle2D.Float(fillX, fillY, fillW, fillH / 2f, fillArc, fillArc));
                g2.fill(topHalf);

                // Soft inner glow pulse
                float glow = 0.25f + 0.25f * (float) Math.sin(glowPhase);
                g2.setComposite(AlphaComposite.SrcOver.derive(glow));
                g2.setColor(lighten(base, 0.35f));
                g2.fill(new RoundRectangle2D.Float(fillX, fillY + 1, fillW, fillH - 2, fillArc, fillArc));
                g2.setComposite(AlphaComposite.SrcOver);
            }

            // Ticks every 10%
            g2.setColor(new Color(255, 255, 255, 40));
            for (int i = 1; i < 10; i++) {
                int tx = x + Math.round(w * (i / 10f));
                g2.drawLine(tx, y + 3, tx, y + h - 3);
            }

            // Progress text
            g2.setFont(getFont() != null ? getFont() : new Font("Segoe UI", Font.PLAIN, Math.max(12, h - 2)));
            FontMetrics fm = g2.getFontMetrics();
            int tw = fm.stringWidth(text);
            int th = fm.getAscent();
            int tx = x + (w - tw) / 2;
            int ty = y + (h + th) / 2 - 1;

            // Text shadow
            g2.setColor(new Color(0, 0, 0, 150));
            g2.drawString(text, tx + 1, ty + 1);
            // Text
            g2.setColor(TEXT_COLOR);
            g2.drawString(text, tx, ty);

            g2.dispose();
        }

        private static Color lerpColor(Color a, Color b, float t) {
            t = Math.max(0f, Math.min(1f, t));
            int r = (int) (a.getRed()   + (b.getRed()   - a.getRed())   * t);
            int g = (int) (a.getGreen() + (b.getGreen()) - a.getGreen() * t);
            g = (int) (a.getGreen() + (b.getGreen() - a.getGreen()) * t);
            int bl= (int) (a.getBlue()  + (b.getBlue())  - a.getBlue()  * t);
            bl = (int) (a.getBlue() + (b.getBlue() - a.getBlue()) * t);
            return new Color(r, g, bl);
        }

        private static Color lighten(Color c, float f) {
            f = Math.max(0f, f);
            int r = (int) Math.min(255, c.getRed()   + (255 - c.getRed())   * f);
            int g = (int) Math.min(255, c.getGreen() + (255 - c.getGreen()) * f);
            int b = (int) Math.min(255, c.getBlue()  + (255 - c.getBlue())  * f);
            return new Color(r, g, b);
        }

        private static Color darken(Color c, float f) {
            f = Math.max(0f, f);
            int r = (int) Math.max(0, c.getRed()   * (1 - f));
            int g = (int) Math.max(0, c.getGreen() * (1 - f));
            int b = (int) Math.max(0, c.getBlue()  * (1 - f));
            return new Color(r, g, b);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            // IMPORTANT: No Look & Feel changes here (prevents other windows' button colors from changing)
            Splash s = new Splash();
            s.startAnimations();
        });
    }
}


/*package Car;
public class Splash extends javax.swing.JFrame {

   
    public Splash() {
        
        initComponents();
    }
/*
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel2 = new javax.swing.JLabel();
        jProgressBar1 = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();

        setBounds(new java.awt.Rectangle(0, 0, 0, 0));
        setLocation(new java.awt.Point(1000, 2000));
        setMinimumSize(new java.awt.Dimension(500, 297));
        setUndecorated(true);
        setSize(new java.awt.Dimension(500, 297));
        getContentPane().setLayout(null);

        jLabel2.setFont(new java.awt.Font("Arial Black", 1, 36)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 51));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("CRMS");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(200, 40, 121, 40);
        getContentPane().add(jProgressBar1);
        jProgressBar1.setBounds(10, 252, 480, 4);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ImageR/Logo 300200.png"))); // NOI18N
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, 0, 500, 300);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    
    
/*
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JProgressBar jProgressBar1;
    // End of variables declaration//GEN-END:variables
}
*/