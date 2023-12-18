package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JPanel;

import entity.Player;
import entity.Player.PlayerAttack;
import tile.TileManager;
import main.CollisionChecker;
import main.KeyHandler;
import entity.Player;
import entity.Enemy;
import entity.Enemy.ArmoredSkeleton;
import entity.Enemy.Skeleton;
import tile.TileManager;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.List;
public class GamePanel extends JPanel implements Runnable{
    
    // SCREEN SETTINGS
    public final int originalTileSize = 16; // 16x16 tile
    public final int scale = 5;

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
    public Player player;
    private PlayerAttack attack;
    private List<Enemy> enemies;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);

        // Set the initial camera position to the center of the game world
        this.playerScreenX = screenWidth / 2 - (tileSize / 2);
        this.playerScreenY = screenHeight / 2 - (tileSize / 2);
        initGame();

    }
    
    public void initGame() {
    	enemies = new ArrayList<>();
        for(int y = 0; y <= 4; y++) {
        	for(int x = 6; x <= 12; x++) {
    			var enemy = (y != 4)? new Skeleton(this.tileSize * x,this.tileSize * y) : new ArmoredSkeleton(this.tileSize * x,this.tileSize * y);
    			enemies.add(enemy);
    			System.out.println("Enemy Added");
        	}
        }
        
        player = new Player(this, keyHandler);
        attack = new PlayerAttack();
        player.screenX = player.getX();
        player.screenY = player.getY();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    
//    PLAYER LOCATION
    public int getPlayerX() {
        return player.getX();
    }

    public int getPlayerY() {
        return player.getY();
    }
    
    public int getPlayerScreenX() {
        return player.screenX;
    }

    public int getPlayerScreenY() {
        return player.screenY;
    }

    
// TIME SHIFT TO SET FPS
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

    // UPDATES GAME EVENTS
    public void update(){
    	//Player
        player.act();
        
        //Enemy
        
        //Attack
        if(player.keyH.spacePressed == true && !attack.isVisible()) {
        	attack = new PlayerAttack(player.getX(), player.getY());
        }
        if(attack.isVisible()) {
        	int atkX = attack.getX();
        	int atkY = attack.getY();
        	for(Enemy enemy : enemies) {
        		int enemyX = enemy.getX();
                int enemyY = enemy.getY();

                if (enemy.isVisible() && attack.isVisible()) {
                   
                }
        	}
        	int y = attack.getY();
	        y -= 10;
	
	        if (y < 0) {
	            attack.die();
	        } else {
	            attack.setY(y);
	        }
        }
	        
        //Enemy Attack
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        doDrawing(g2);
        g2.dispose();
    }
    
    public void doDrawing(Graphics2D g2) {
    	tileM.draw(g2);
    	player.animate(g2);
    	drawEnemy(g2);
    	drawAttack(g2);
    }
    
    public void drawEnemy(Graphics2D g2) {
    	for(Enemy enemy : enemies) {
    		g2.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
    	}
    }
    
    public void drawAttack(Graphics2D g2) {
    	if(attack.isVisible()) {
    		g2.drawImage(attack.getImage(), attack.getX(), attack.getY(), this);
    	}
    	
    }
    
   
}
