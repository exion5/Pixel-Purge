# Pixel Purge

Pixel Purge is a retro style Space Invaders game built in Java Swing for the Unit 4 Game Project in the ICS4U course. You control a spaceship at the bottom of the screen and must destroy waves of alien invaders before they reach you. The game features a login system, high score tracking, shield mechanics, powerups, and background music.

---

## What the Program Does

- **Login & Registration** — Players create an account or log in before playing. Credentials and high scores are saved to `Registration.txt`.
- **Gameplay** — Move left/right and shoot upward to destroy enemies arranged in a grid. Enemies fire back and move down over time.
- **Levels** — There are 5 levels. Each level adds more enemy rows and increases their speed.
- **Shields** — Destructible shield barriers protect the player. They can get hit 3 times before breaking.
- **Powerups** — Killing enemies has a 5% chance to drop either a shield powerup (repairs all shields) or an extra life (only if below max).
- **Top Display** — Displays score, current level, and remaining lives shown as pixel hearts.
- **High Score** — Your best score is saved per account and displayed on the start screen.
- **Music & SFX** — Background music playlist with volume control and skip button. Sound effects for shooting, explosions, powerups, and more.

---

## How to Setup & Run

You must turn the folder into a Java Project through VSCode. Ensure you have the Java Expansion Pack. Then click the play button in the top right corner on a .java file.

---

## Controls

`←` / `→` to Move left / right
`↑` or Mouse Click to Shoot
`R` to Restart after game over

---

## Project Goals

- Apply object-oriented programming principles (inheritance, abstraction, polymorphism) through a real, interactive project
- Practice Java Swing for GUI development
- Implement file I/O for persistent user data (login credentials and high scores)
- Create an engaging, polished game with original artwork, sound, and UI design

---

## Project Structure

`PixelPurge.java` - Manages flow between login, start, and game screens
`GamePanel.java` - Handles the main game loop, rendering, inputs keys, and collision detection
`GameFrame.java` - JFrame wrapper for the game panel
`GameObject.java` - Abstract base class for all game objects
`Player.java` - Player ship that the user controls
`Enemy.java` - Three enemy types with pixel art
`Bullet.java` - Bullets fired by both player and enemies
`ShieldBlock.java` - Destructible shield blocks with 3 health
`Powerup.java` - Falling powerups (Shield / Life)
`LoginPage.java` - Login and registration
`StartPage.java` - Start screen with high score, controls, and music player
`Sound.java` - Background music and sound effect management
`Prompt.java` - File I/O utility for reading and writing to `Registration.txt`

---

## References

- [Java Swing Documentation](https://docs.oracle.com/javase/8/docs/api/javax/swing/package-summary.html)
- [Java Sound API](https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/package-summary.html)
- [PressStart2P Font — Google Fonts](https://fonts.google.com/specimen/Press+Start+2P)

---

*ICS4U Game Project - Ethan Xiong*