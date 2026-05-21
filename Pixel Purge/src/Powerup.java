import java.awt.*;

public class Powerup extends GameObject { // creates the powerups that fall down in the game, either a shield or a heal
    String type;
    
    Powerup(int x, int y, String type){
        super(x, y, 20, 20);
        this.type = type;
    }
    
    @Override
    void update(){ // moves the powerup down and checks if it goes off screen
        y+= 5;
        if (y > 600){
            alive = false;
        }
    }
    
    @Override
    void draw(Graphics2D a){
        if (type.equals("Shield")) { // shield is blue
            a.setColor(Color.BLUE);
        } else {
            a.setColor(Color.RED); // heal is red
        }
        a.fillOval(x, y, width, height);
    }
}