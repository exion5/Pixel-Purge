import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Player extends GameObject {
    int speed = 6;
    boolean movingLeft = false;
    boolean movingRight = false;

    Player(int x, int y) {
        super(x, y, 40, 20);
    }

    @Override
    void update() {
        if (movingLeft) x -= speed;
        if (movingRight) x += speed;
    }

    @Override
    void draw(Graphics2D g) {
        g.setColor(Color.CYAN);
        g.fillRect(x, y, width, height);
    }
}