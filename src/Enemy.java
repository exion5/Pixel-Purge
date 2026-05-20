import java.awt.*;
 
class Enemy extends GameObject { // creates the enemies in the game, which are pixelated invaders that move left and right across the screen
    int dx = 2;
    int row = 0; // used for color/shape variation
 
    Enemy(int x, int y) {
        super(x, y, 30, 20);
    }
 
    @Override
    void update() {
        x += dx;
    }
 
    @Override
    void draw(Graphics2D g) { // there are 3 variations of the invaders determined based on positioning on the grid
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
 
        // Row-based pixel invader shapes & colors
        switch (row % 3) {
            case 0:
                drawCrabInvader(g, new Color(255, 80, 80));   // Red top rows
                break;
            case 1:
                drawSquidInvader(g, new Color(255, 180, 0));  // Yellow middle
                break;
            case 2:
                drawOctopusInvader(g, new Color(100, 180, 255)); // Blue bottom
                break;
        }
    }
 
    // Classic crab-style invader
    private void drawCrabInvader(Graphics2D g, Color c) {
        g.setColor(c);
        int[][] shape = { // matrix to create pixelated look of the invader
            {0,0,1,0,0,0,1,0,0},
            {0,0,0,1,1,1,0,0,0},
            {0,0,1,1,1,1,1,0,0},
            {0,1,1,0,1,0,1,1,0},
            {0,1,1,1,1,1,1,1,0},
            {0,0,1,0,0,0,1,0,0},
            {0,1,0,0,0,0,0,1,0},
        };
        int ps = 3;
        for (int r = 0; r < shape.length; r++)
            for (int cc = 0; cc < shape[r].length; cc++)
                if (shape[r][cc] == 1)
                    g.fillRect(x + cc * ps, y + r * ps, ps, ps);
        // Glow
        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 40));
        g.fillOval(x - 2, y - 2, 34, 24);
    }
 
    // Squid-style invader
    private void drawSquidInvader(Graphics2D g, Color c) {
        g.setColor(c);
        int[][] shape = { // matrix to create pixelated look of the invader
            {0,0,0,1,1,0,0,0},
            {0,0,1,1,1,1,0,0},
            {0,1,1,0,0,1,1,0},
            {0,1,1,1,1,1,1,0},
            {0,0,1,0,0,1,0,0},
            {0,1,0,1,1,0,1,0},
            {0,0,1,0,0,1,0,0},
        };
        int ps = 3;
        for (int r = 0; r < shape.length; r++)
            for (int cc = 0; cc < shape[r].length; cc++)
                if (shape[r][cc] == 1)
                    g.fillRect(x + cc * ps + 3, y + r * ps, ps, ps);
        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 40));
        g.fillOval(x - 2, y - 2, 34, 24);
    }
 
    // Octopus-style invader
    private void drawOctopusInvader(Graphics2D g, Color c) {
        g.setColor(c);
        int[][] shape = { // matrix to create pixelated look of the invader
            {0,0,1,1,1,1,0,0},
            {0,1,1,1,1,1,1,0},
            {1,1,0,1,1,0,1,1},
            {1,1,1,1,1,1,1,1},
            {0,0,1,0,0,1,0,0},
            {0,1,0,1,1,0,1,0},
            {1,0,1,0,0,1,0,1},
        };
        int ps = 3;
        for (int r = 0; r < shape.length; r++)
            for (int cc = 0; cc < shape[r].length; cc++)
                if (shape[r][cc] == 1)
                    g.fillRect(x + cc * ps, y + r * ps, ps, ps);
        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 40));
        g.fillOval(x - 2, y - 2, 34, 24);
    }
}