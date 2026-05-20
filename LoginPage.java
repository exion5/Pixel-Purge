import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;
import java.io.PrintWriter;
import java.util.Scanner;

public class LoginPage extends JDialog{
    private boolean loggedIn = false;
    public LoginPage(Frame owner) {
        super(owner, "Register Page", true);
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
        setSize(580, 510);
        setLocationRelativeTo(null);
        
        CardLayout cl = new CardLayout();
        JPanel showing = new JPanel(cl);
        
        //register page
        JPanel registerPage = new JPanel(new BorderLayout());
        
        JLabel titleR = new JLabel("Registor Page", SwingConstants.CENTER);
        titleR.setBorder(BorderFactory.createEmptyBorder(60, 0, 20, 0));
        registerPage.add(titleR, BorderLayout.NORTH);
        
        JPanel registerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField usernameR = new JTextField();
        registerPanel.add(usernameR, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        registerPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPasswordField passwordR = new JPasswordField();
        registerPanel.add(passwordR, gbc);
        
        registerPage.add(registerPanel, BorderLayout.CENTER);
        
        JPanel reg = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
        JButton regButton = new JButton("Register");
        regButton.setPreferredSize(new Dimension(100, 35));
        reg.add(regButton);
        JButton loginR = new JButton("Login");
        loginR.setPreferredSize(new Dimension(100, 35));
        reg.add(loginR);
        reg.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        registerPage.add(reg, BorderLayout.SOUTH);
        
        regButton.addActionListener(e -> {
            String user = usernameR.getText();
            String pass = new String(passwordR.getPassword());
        
            if (!user.isEmpty() && !pass.isEmpty()){
                try (PrintWriter fileOut = prompt.getPrintWriter()) { 
                    if (fileOut != null) {
                        // Write the user and pass on new lines
                        fileOut.println(user);
                        fileOut.println(pass);
                        
                        UIManager.put("OptionPane.border", new LineBorder(Color.BLACK, 1));
                        UIManager.put("OptionPane.messageAreaBorder", BorderFactory.createEmptyBorder(10, 10, 10, 10));
                        JOptionPane.showMessageDialog(showing, "Registration Successful!");
                        
                        usernameR.setText("");
                        passwordR.setText("");
                        cl.show(showing, "login");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                UIManager.put("OptionPane.border", new LineBorder(Color.BLACK, 1));
                UIManager.put("OptionPane.messageAreaBorder", BorderFactory.createEmptyBorder(10, 10, 10, 10));
                JOptionPane.showMessageDialog(showing, "Please enter both fields");
            }
        });
        
        
        //login page
        JPanel loginPage = new JPanel(new BorderLayout());
        
        JLabel title = new JLabel("Login Page", SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(60, 0, 20, 0));
        loginPage.add(title, BorderLayout.NORTH);
        JPanel loginPanel = new JPanel(new GridBagLayout());
        
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JTextField username = new JTextField();
        loginPanel.add(username, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;
        loginPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPasswordField password = new JPasswordField();
        loginPanel.add(password, gbc);
        
        loginPage.add(loginPanel, BorderLayout.CENTER);
        
        JPanel button = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
        JButton b = new JButton("Login");
        JButton create  = new JButton("Register");
        b.setPreferredSize(new Dimension(100, 35));
        create.setPreferredSize(new Dimension(100, 35));
        button.add(b);
        button.add(create);
        button.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        loginPage.add(button, BorderLayout.SOUTH);
        
        
        
        
        //entrance page
        JPanel entered = new JPanel(new BorderLayout());
        JLabel success = new JLabel("Login Successful");
        success.setHorizontalAlignment(SwingConstants.CENTER);
        entered.add(success, BorderLayout.NORTH);
        JPanel backButtonPanel = new JPanel(); 
        JButton back = new JButton("Back");
        back.setPreferredSize(new Dimension(100, 35)); 
        backButtonPanel.add(back);
        entered.add(backButtonPanel, BorderLayout.CENTER);
        back.addActionListener(e -> {
            cl.show(showing, "login");
        });
        
        create.addActionListener(e -> {
            username.setText("");
            password.setText("");
            cl.show(showing, "register");
        });
        
        //goes to entered page
        b.addActionListener(e -> {
            boolean shown = false;
            try (Scanner scanner = prompt.getInputScanner()) {
                while (scanner.hasNextLine()) {
                    String userS = scanner.nextLine();
                    if (scanner.hasNextLine()) {
                        String passS = scanner.nextLine();
                        if (username.getText().equals(userS) && password.getText().equals(passS)){
                            loggedIn = true;
                            username.setText("");
                            password.setText("");
                            shown = true;
                            break; 
                        }
                    }
                }
            }
            
            if (shown) {
                setVisible(false);
                dispose(); 
            } else {
                UIManager.put("OptionPane.border", new LineBorder(Color.BLACK, 1));
                UIManager.put("OptionPane.messageAreaBorder", BorderFactory.createEmptyBorder(10, 10, 10, 10));
                JOptionPane.showMessageDialog(showing, "Invalid credentials");
                username.setText("");
                password.setText("");
            }
        });
        
        loginR.addActionListener(e -> {
            usernameR.setText("");
            passwordR.setText("");
            cl.show(showing, "login");
        });
        
        
        showing.add(loginPage, "login");
        showing.add(entered, "entered");
        showing.add(registerPage, "register");
        
        add(showing);
    }
    
    public boolean getLoggedIn(){
        return loggedIn;
    }
}