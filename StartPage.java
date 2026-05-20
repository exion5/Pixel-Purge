import javax.swing.*;
import java.awt.*;
import javax.swing.border.LineBorder;
import java.io.PrintWriter;
import java.util.Scanner;

public class StartPage extends JDialog{
    private boolean start = false;
    public StartPage(Frame owner){
        super(owner, "Pixel Purge", true);
        setModal(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); 
        setSize(580, 510);
        setLocationRelativeTo(null);
        CardLayout cl = new CardLayout();
        JPanel showing = new JPanel(cl);
        
        
        add(showing);
    }
    
    public boolean getStart(){
        return start;
    }
}