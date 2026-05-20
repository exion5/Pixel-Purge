import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

abstract class GameObject {
    int x, y, width, height;
    boolean alive = true;

    GameObject(int x, int y, int w, int h) {
        this.x = x; this.y = y;
        this.width = w; this.height = h;
    }

    abstract void update();
    abstract void draw(Graphics2D g);

    Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}