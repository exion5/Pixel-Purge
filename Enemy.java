import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

class Enemy extends GameObject {
    int dx = 2;

    Enemy(int x, int y) {
        super(x, y, 30, 20);
    }

    @Override
    void update() {
        x += dx;
    }

    @Override
    void draw(Graphics2D g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, width, height);
    }
}