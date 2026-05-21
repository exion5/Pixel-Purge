import java.awt.*;

abstract class GameObject { // creates the restrictions for any object in the game, such as the player, bullets, shields, and powerups
    int x, y, width, height;
    boolean alive = true;

    GameObject(int x, int y, int w, int h) { // position of game object and its dimensions
        this.x = x; this.y = y;
        this.width = w; this.height = h;
    }

    abstract void update(); // forces all objects to have both an update and draw method
    abstract void draw(Graphics2D g);

    Rectangle getBounds() { // creates boundaries of the object for collision detection
        return new Rectangle(x, y, width, height);
    }
}