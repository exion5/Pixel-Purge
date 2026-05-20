import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class Powerup extends GameObject {
    String type;
    
    Powerup(int x, int y, String type){
        super(x, y, 20, 20);
        this.type = type;
    }
    
    @Override
    void update(){
        y+= 5;
        if (y > 600){
            alive = false;
        }
    }
    
    @Override
    void draw(Graphics2D a){
        if (type.equals("Shield")) {
            a.setColor(Color.BLUE);
        } else {
            a.setColor(Color.RED);
        }
        a.fillOval(x, y, width, height);
    }
}