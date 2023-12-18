package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
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
import java.util.Iterator;
import java.util.List;
import java.util.Random;
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
        for(int y = 0; y <= 5; y++) {
        	for(int x = 6; x <= 16; x++) {
    			var enemy = (y < 4)? new Skeleton(this, this.tileSize * x,this.tileSize * y) : new ArmoredSkeleton(this, this.tileSize * x,this.tileSize * y);
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
        for(Enemy enemy: enemies) {
        	enemy.setCollisionOn(false);
            this.cChecker.checkTile(enemy);
            if (enemy.isCollisionOn() && enemy.getDirection()=="right") {
                Iterator<Enemy> i1 = enemies.iterator();
                while(i1.hasNext()) {
                	Enemy e1 = i1.next();
                	e1.setY(e1.getY() + 10);
                	e1.setDirection("left");
                	e1.setCollisionOn(false);
                }
                
            }
            if (enemy.isCollisionOn() && enemy.getDirection()=="left") {
                Iterator<Enemy> i2 = enemies.iterator();
                while(i2.hasNext()) {
                	Enemy e2 = i2.next();
                	e2.setY(e2.getY() + 10);
                	e2.setCollisionOn(false);
                	e2.setDirection("right");
                }
                
            }
        }
        
        Iterator<Enemy> it = enemies.iterator();
        
        while(it.hasNext()) {
        	Enemy enemy = it.next();
        	if(enemy.isVisible()) {
        		int y = enemy.getY();
        		if(y > 130) {
//        			System.out.println(y);
        		}
        		enemy.move();
        	}
        }
        
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
                   if(atkX >= enemyX && atkX <= (enemyX + tileSize/2) && atkY >= enemyY && atkY <= (enemyY + tileSize)) {
                	   attack.die();
                	   enemy.setHealth(enemy.getHealth() - 1);
                	   if(enemy.getHealth() == 1) {
                		   try {
	               				enemy.setImage(ImageIO.read(getClass().getResourceAsStream("/enemy/Skeleton1.png")));
	               			} catch (IOException e) {
	               				// TODO Auto-generated catch block
	               				e.printStackTrace();
	               			}
                	   }
                	   if(enemy.getHealth() <= 0) {
                		   enemy.setDying(true);
                	   }
                   }
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
        var generator = new Random();
        for(Enemy enemy : enemies) {
        	int shoot = generator.nextInt(5000);
        	Enemy.EnemyAttack attack = enemy.attack();
        	if(shoot == 500 && enemy.isVisible() && attack.isDestroyed()) {
        		attack.setDestroyed(false);
        		attack.setX(enemy.getX());
        		attack.setY(enemy.getY());
        	}
        	
        	int atkX = attack.getX();
        	int atkY = attack.getY();
        	int playerX = player.getX();
        	int playerY = player.getY();
        	if(player.isVisible() && !attack.isDestroyed()) {
        		if(atkX >= playerX && atkX <= (playerX + tileSize) && atkY >= playerY && atkY <= (playerY + tileSize*2)) { // needs improvement
        			player.setLives(player.getLives() - 1);
        			System.out.println(player.getLives() + "left");
        			if(player.getLives() <= 0) {
        				
        			}
        			attack.setDestroyed(true);
        			
        		}
        	}
        	
        	if(!attack.isDestroyed()) {
        		attack.setY(attack.getY() + 3);
        		if(attack.getY() >= player.getY()) {
        			attack.setDestroyed(true);
        		}
        	}
        }
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
    	drawEnemyAttack(g2);
    }
    
    public void drawEnemy(Graphics2D g2) {
    	for(Enemy enemy : enemies) {
    		if(enemy.isVisible()) {
    			g2.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), tileSize, tileSize*2, this);
    		}
    		
    		if(enemy.isDying()) {
    			enemy.die();
    		}
    	}
    }
    
    public void drawAttack(Graphics2D g2) {
  
    	if(attack.isVisible()) {
    		g2.drawImage(attack.getImage(), attack.getX(), attack.getY(), tileSize/2, tileSize, this);
    	}
    	
    }
    
    public void drawEnemyAttack(Graphics2D g2) {
    	for(Enemy enemy : enemies) {
    		Enemy.EnemyAttack e = enemy.attack();
    		if(!e.isDestroyed()) {
    			g2.drawImage(e.getImage(), e.getX(), e.getY(), tileSize/2, tileSize, this);
    		}
    	}
    }
   
}
