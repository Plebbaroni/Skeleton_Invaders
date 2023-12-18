package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

import entity.Arrow;
import entity.Player;
import entity.PrimaryAttack;
import tile.TileManager;
import main.CollisionChecker;
import main.KeyHandler;
import entity.Player;
import tile.TileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable{
    
    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 5;

    public final int tileSize = originalTileSize * scale; // 80x80 tile 
    public final int maxScreenCol = 24; // original = 24
    public final int maxScreenRow = 13; // original = 13
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;
    public int playerScreenX;
    public int playerScreenY;

    
    // WORLD SETTINGS
    public final int maxWorldCol = 24;
    public final int maxWorldRow = 13;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    // FPS
    int FPS = 60;
    
    TileManager tileM = new TileManager(this);
    KeyHandler keyHandler = new KeyHandler();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyHandler);
    public PrimaryAttack pa = new PrimaryAttack(this);
    private final AtomicBoolean canShoot = new AtomicBoolean(true);
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        // Set the initial camera position to the center of the game world
        this.playerScreenX = screenWidth / 2 - (tileSize / 2);
        this.playerScreenY = screenHeight / 2 - (tileSize / 2);

        player.screenX = player.worldX;
        player.screenY = player.worldY;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
//    PLAYER LOCATION
    public int getPlayerX() {
        return player.worldX;
    }

    public int getPlayerY() {
        return player.worldY;
    }
    
    public int getPlayerScreenX() {
        return player.screenX;
    }

    public int getPlayerScreenY() {
        return player.screenY;
    }
    
    
// TIME SHIT TO SET FPS
    public void run() {
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >= 1){
                // Call update here instead of in the constructor
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if(timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                System.out.println("Current Character Location: " + getPlayerX() + "x" + getPlayerY());
                drawCount = 0;
                timer = 0;
            }
        }
    }
 
// UPDATES AND DRAWS CHARACTER
    public void update(){
        player.update();
        pa.tick();

        if(keyHandler.spacePressed){
        	pa.addArrow(new Arrow(player.worldX+32, player.worldY, this)); 
        	keyHandler.spacePressed = false;
        }
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        // Draw tiles without adjusting for player's position
        tileM.draw(g2);
        pa.draw(g2);

        // Draw player using its screen coordinates directly
        player.draw(g2);
        g2.dispose();
    }
}
