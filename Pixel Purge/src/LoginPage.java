import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;
import java.io.PrintWriter;
import java.util.Scanner;
 
public class LoginPage extends JDialog { // dialog is chosen because the login page is meant to be temporary
    private boolean loggedIn = false;
    private String user = "";
 
    public LoginPage(Frame owner) {
        super(owner, "Pixel Purge - Login", true);
        setModal(true);
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) { 
                System.exit(0); 
            } 
        });
        setSize(580, 510);
        setLocationRelativeTo(null);
        setResizable(false);
 
        CardLayout cl = new CardLayout();
        JPanel showing = new JPanel(cl);
        showing.setBackground(Color.BLACK);
 
        Font monoFont   = new Font("Monospaced", Font.BOLD, 12); // font creation
        Font titleFont;
        try {
            titleFont = Font.createFont(Font.TRUETYPE_FONT,
                new java.io.File("PressStart2P-Regular.ttf")).deriveFont(22f);
        } catch (Exception e) {
            titleFont = new Font("Monospaced", Font.BOLD, 22);
        }
 
        Color accent    = new Color(0, 255, 100);
        Color accentAlt = new Color(100, 200, 255);
        Color bg        = Color.BLACK;
 
        JPanel loginPage = buildPagePanel(bg); // login page
 
        JLabel loginTitle = makeTitle("LOGIN", titleFont, accent);
        JTextField username = makeField(monoFont, accent);
        JPasswordField password = makePassField(monoFont, accent);
 
        JPanel loginForm = buildForm(
            new String[]{"USERNAME", "PASSWORD"},
            new JComponent[]{username, password},
            monoFont, accent, bg
        );
 
        JButton lBtn  = makeRetroButton("▶  LOGIN",    accent);
        JButton toRBtn  = makeRetroButton("  REGISTER", accentAlt);
 
        JPanel lBtns = new JPanel();
        lBtns.setOpaque(false);
        lBtns.setLayout(new BoxLayout(lBtns, BoxLayout.Y_AXIS));
        lBtns.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        lBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        toRBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        lBtns.add(lBtn);
        lBtns.add(Box.createVerticalStrut(12));
        lBtns.add(toRBtn);
 
        loginPage.add(Box.createVerticalStrut(50));
        loginPage.add(loginTitle);
        loginPage.add(Box.createVerticalStrut(8));
        loginPage.add(makeDivider(accent));
        loginPage.add(Box.createVerticalStrut(30));
        loginPage.add(loginForm);
        loginPage.add(lBtns);
 
        JPanel registerPage = buildPagePanel(bg); // registration page, same layout as login
 
        JLabel regTitle = makeTitle("REGISTER", titleFont, accentAlt);
        JTextField usernameR  = makeField(monoFont, accentAlt);
        JPasswordField passwordR = makePassField(monoFont, accentAlt);
 
        JPanel regForm = buildForm(
            new String[]{"USERNAME", "PASSWORD"},
            new JComponent[]{usernameR, passwordR},
            monoFont, accentAlt, bg
        );
 
        JButton rBtn    = makeRetroButton("▶  CREATE ACCOUNT", accentAlt);
        JButton tolBtn = makeRetroButton("  BACK TO LOGIN",   accent);
 
        JPanel rBtns = new JPanel();
        rBtns.setOpaque(false);
        rBtns.setLayout(new BoxLayout(rBtns, BoxLayout.Y_AXIS));
        rBtns.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        rBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        tolBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        rBtns.add(rBtn);
        rBtns.add(Box.createVerticalStrut(12));
        rBtns.add(tolBtn);
 
        registerPage.add(Box.createVerticalStrut(50));
        registerPage.add(regTitle);
        registerPage.add(Box.createVerticalStrut(8));
        registerPage.add(makeDivider(accentAlt));
        registerPage.add(Box.createVerticalStrut(30));
        registerPage.add(regForm);
        registerPage.add(rBtns);
 
        lBtn.addActionListener(e -> { // creates the page functionality
            try (Scanner scanner = Prompt.getInputScanner()) {
                while (scanner.hasNextLine()) {
                    String userS = scanner.nextLine();
                    String passS = scanner.nextLine();
                    String score = scanner.nextLine();
                    if (username.getText().equals(userS) && new String(password.getPassword()).equals(passS)) {
                        loggedIn = true;
                        user = username.getText();
                        username.setText("");
                        password.setText("");
                        setVisible(false);
                        dispose();
                        return;
                    }
                }
            }
            showRetroDialog(showing, "INVALID CREDENTIALS", new Color(255, 80, 80));
            username.setText("");
            password.setText("");
        });
 
        toRBtn.addActionListener(e -> {
            username.setText(""); password.setText("");
            cl.show(showing, "register");
        });
 
        rBtn.addActionListener(e -> {
            String user = usernameR.getText();
            String pass = new String(passwordR.getPassword());
            if (!user.isEmpty() && !pass.isEmpty()) {
                try (PrintWriter fileOut = Prompt.getPrintWriter()) {
                    if (fileOut != null) {
                        fileOut.println(user);
                        fileOut.println(pass);
                        fileOut.println(0);
                        showRetroDialog(showing, "ACCOUNT CREATED!", accent);
                        usernameR.setText(""); passwordR.setText("");
                        cl.show(showing, "login");
                    }
                } catch (Exception ex) { ex.printStackTrace(); }
            } else {
                showRetroDialog(showing, "FILL ALL FIELDS", new Color(255, 220, 0));
            }
        });
 
        tolBtn.addActionListener(e -> {
            usernameR.setText(""); passwordR.setText("");
            cl.show(showing, "login");
        });
 
        showing.add(loginPage,    "login");
        showing.add(registerPage, "register");
 
        JPanel root = new JPanel(new BorderLayout()) { // creates the GUI for the login page
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(new Color(255, 255, 255, 60));
            }
        };
        root.setBackground(Color.BLACK);
        root.add(showing, BorderLayout.CENTER);
        add(root);
    }
 
    private JPanel buildPagePanel(Color bg) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(bg);
        p.setOpaque(false);
        return p;
    }
 
    private JLabel makeTitle(String text, Font f, Color c) {
        JLabel l = new JLabel(text, SwingConstants.CENTER);
        l.setFont(f);
        l.setForeground(c);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }
 
    private JSeparator makeDivider(Color c) {
        JSeparator sep = new JSeparator();
        sep.setForeground(c);
        sep.setBackground(c);
        sep.setMaximumSize(new Dimension(320, 2));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);
        return sep;
    }
 
    private JTextField makeField(Font f, Color accent) {
        JTextField tf = new JTextField(18);
        styleInputField(tf, f, accent);
        return tf;
    }
 
    private JPasswordField makePassField(Font f, Color accent) {
        JPasswordField pf = new JPasswordField(18);
        styleInputField(pf, f, accent);
        return pf;
    }
 
    private void styleInputField(JTextField tf, Font f, Color accent) {
        tf.setFont(f);
        tf.setForeground(accent);
        tf.setBackground(new Color(15, 15, 15));
        tf.setCaretColor(accent);
        tf.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(accent, 1),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));
        tf.setMaximumSize(new Dimension(300, 38));
    }
 
    private JPanel buildForm(String[] labels, JComponent[] fields,
                             Font f, Color accent, Color bg) {
        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setOpaque(false);
        form.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(f.deriveFont(10f));
            lbl.setForeground(new Color(160, 160, 160));
            lbl.setAlignmentX(Component.CENTER_ALIGNMENT);
            form.add(lbl);
            form.add(Box.createVerticalStrut(4));
            fields[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            form.add(fields[i]);
            form.add(Box.createVerticalStrut(16));
        }
        return form;
    }
 
    private JButton makeRetroButton(String text, Color accent) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(accent.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 35));
                } else {
                    g2.setColor(Color.BLACK);
                }
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(accent);
                g2.setStroke(new BasicStroke(2));
                g2.drawRect(1, 1, getWidth() - 3, getHeight() - 3);
                g2.setColor(getModel().isRollover() ? accent : Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int tx = (getWidth() - fm.stringWidth(getText())) / 2;
                int ty = (getHeight() + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(getText(), tx, ty);
            }
        };
        btn.setFont(new Font("Monospaced", Font.BOLD, 12));
        btn.setPreferredSize(new Dimension(260, 42));
        btn.setMaximumSize(new Dimension(260, 42));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
 
    private void showRetroDialog(Component parent, String message, Color accent) {
        JDialog dlg = new JDialog();
        dlg.setUndecorated(true);
        dlg.setModal(true);
        dlg.setSize(320, 120);
        dlg.setLocationRelativeTo(parent);
 
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.BLACK);
        p.setBorder(new LineBorder(accent, 2));
 
        JLabel msg = new JLabel(message, SwingConstants.CENTER);
        msg.setFont(new Font("Monospaced", Font.BOLD, 13));
        msg.setForeground(accent);
        p.add(msg, BorderLayout.CENTER);
 
        JButton ok = new JButton("OK");
        ok.setFont(new Font("Monospaced", Font.BOLD, 11));
        ok.setForeground(Color.WHITE);
        ok.setBackground(new Color(30, 30, 30));
        ok.setBorder(new LineBorder(accent, 1));
        ok.setFocusPainted(false);
        ok.addActionListener(e -> dlg.dispose());
 
        JPanel btnP = new JPanel();
        btnP.setBackground(Color.BLACK);
        btnP.add(ok);
        p.add(btnP, BorderLayout.SOUTH);
        dlg.add(p);
        dlg.setVisible(true);
    }
 
    public boolean getLoggedIn() {
        return loggedIn;
    }

    public String getUser() {
        return user;
    }
}