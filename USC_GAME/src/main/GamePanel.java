package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import entity.Player;
import entity.Player.PlayerAttack;
import tile.TileManager;
import entity.Enemy;
import entity.Enemy.ArmoredSkeleton;
import entity.Enemy.Skeleton;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

	// SCREEN SETTINGS
	private final int originalTileSize = 16; // 16x16 tile
	private final int scale = 3;
	private final int tileSize = originalTileSize * scale; // 80x80 tile
	private final int maxScreenCol = 24; // original = 24
	private final int maxScreenRow = 13; // original = 13
	private final int screenWidth = tileSize * maxScreenCol;
	private final int screenHeight = tileSize * maxScreenRow;

	// WORLD SETTINGS
	private final int maxWorldCol = 24;
	private final int maxWorldRow = 13;
	private final int worldWidth = tileSize * maxWorldCol;
	private final int worldHeight = tileSize * maxWorldRow;
	// FPS
	private int FPS = 60;

	private TileManager tileM = new TileManager(this);
	private KeyHandler keyHandler = new KeyHandler();
	private Thread gameThread;
	private Player player;
	private PlayerAttack attack;
	private List<Enemy> enemies;
	private int enemyFireInterval;
	private int enemyKillCount;
	private boolean hasInvaded;

	public int getWorldWidth() {
		return worldWidth;
	}

	public int getWorldHeight() {
		return worldHeight;
	}

	public int getTileSize() {
		return tileSize;
	}
	
	public int getScale() {
		return scale;
	}
	
	public int getOriginalTileSize() {
		return originalTileSize;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public boolean hasInvaded() {
		return hasInvaded;
	}
	
	public TileManager getTileM() {
		return tileM;
	}

	public void setTileM(TileManager tileM) {
		this.tileM = tileM;
	}

	public KeyHandler getKeyHandler() {
		return keyHandler;
	}

	public void setKeyHandler(KeyHandler keyHandler) {
		this.keyHandler = keyHandler;
	}

	public Thread getGameThread() {
		return gameThread;
	}

	public void setGameThread(Thread gameThread) {
		this.gameThread = gameThread;
	}

	public void setInvaded(boolean invaded) {
		this.hasInvaded = invaded;
	}

	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.addKeyListener(keyHandler);
		this.setFocusable(true);
		// Set the initial camera position to the center of the game world
		initGame();

	}

	public void initGame() {
		enemies = new ArrayList<>();
		enemyKillCount = 0;
		enemyFireInterval = 2500;
		hasInvaded = false;
		for (int y = 0; y <= 5; y++) {
			for (int x = 6; x <= 15; x++) {
				var enemy = (y < 4) ? new Skeleton(this.tileSize * x, this.tileSize * y)
						: new ArmoredSkeleton(this.tileSize * x, this.tileSize * y);
				enemies.add(enemy);
			}
		}

		player = new Player(this, keyHandler);
		attack = new PlayerAttack();
		player.setScreenX(player.getX());
		player.setScreenY(player.getY());
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

// TIME SHIFT TO SET FPS
	public void run() {
		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		while (gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				// Call update here instead of in the constructor
				update();
				repaint();
				delta--;
				drawCount++;
			}

			if (timer >= 1000000000) {
				System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}
	}

	// UPDATES GAME EVENTS
	public void update() {
		// Player
		if (player.getLives() <= 0 || hasInvaded == true) {
			player.setDirection("dead");
			attack.die();
			for(Enemy enemy : enemies) {
				if(!enemy.attack().isDestroyed()) {
					enemy.attack().setDestroyed(true);
				}
				
				enemy.moveLose(player, screenWidth, tileSize);
			}
		} else {
			player.act();
			// Attack
			if (player.getKeyH().spacePressed == true && !attack.isVisible()) {
				attack = new PlayerAttack(player.getX(), player.getY());
			}
			if (attack.isVisible()) {
				int atkX = attack.getX();
				int atkY = attack.getY();
				for (Enemy enemy : enemies) {
					int enemyX = enemy.getX();
					int enemyY = enemy.getY();

					if (enemy.isVisible() && attack.isVisible()) {
						if (atkX >= enemyX && atkX <= (enemyX + tileSize / 2) && atkY >= enemyY
								&& atkY <= (enemyY + tileSize)) {
							attack.die();
							enemy.setHealth(enemy.getHealth() - 1);
							if (enemy.getHealth() == 1) {
								try {
									enemy.setImage(enemy.getSpriteNum() == 1 || enemy.getSpriteNum() == 3
											? ImageIO.read(getClass().getResourceAsStream("/enemy/Skeleton2.png"))
											: ImageIO.read(getClass().getResourceAsStream("/enemy/Skeleton3.png")));
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if (enemy.getHealth() <= 0) {
								enemy.setDying(true);
								enemyKillCount++;
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
			// Enemy
			for (Enemy enemy : enemies) {
				if (enemy.getX() >= screenWidth - tileSize && enemy.getDirection() == "right") {
					Iterator<Enemy> i1 = enemies.iterator();
					while (i1.hasNext()) {
						Enemy e1 = i1.next();
						e1.setY(e1.getY() + tileSize);
						e1.setDirection("left");
					}

				}
				if (enemy.getX() <= 0 && enemy.getDirection() == "left") {
					Iterator<Enemy> i2 = enemies.iterator();
					while (i2.hasNext()) {
						Enemy e2 = i2.next();
						e2.setY(e2.getY() + tileSize);
						e2.setDirection("right");
					}

				}
			}

			Iterator<Enemy> it = enemies.iterator();

			while (it.hasNext()) {
				Enemy enemy = it.next();
				if (enemy.isVisible()) {
					int y = enemy.getY();
					if (y >= player.getY() - tileSize / 2) {
						hasInvaded = true;
					}
					enemy.move();
				}
			}

			// Enemy Attack
			var generator = new Random();
			for (Enemy enemy : enemies) {
				int shoot = generator.nextInt(enemyFireInterval);
				Enemy.EnemyAttack attack = enemy.attack();
				if (shoot == enemyFireInterval - 3 && enemy.isVisible() && attack.isDestroyed()) {
					attack.setDestroyed(false);
					attack.setX(enemy.getX());
					attack.setY(enemy.getY());
				}
				int atkX = attack.getX();
				int atkY = attack.getY();
				int playerX = player.getX();
				int playerY = player.getY();
				if (player.isVisible() && !attack.isDestroyed()) {
					if (atkX >= playerX && atkX <= (playerX + tileSize) && atkY >= playerY
							&& atkY <= (playerY + tileSize * 2)) {
						player.setLives(player.getLives() - 1);
						player.setDirection("hit");
						attack.setDestroyed(true);
					}
				}

				if (!attack.isDestroyed()) {
					attack.setY(attack.getY() + 3);
					if (attack.getY() >= player.getY() + tileSize) {
						attack.setDestroyed(true);
					}
				}
			}
		}
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getMaxWorldCol() {
		return maxWorldCol;
	}

	public int getMaxWorldRow() {
		return maxWorldRow;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		doDrawing(g2);
		String text = "You Win";
		String guide = "Press SPACE to shoot";
		String guide2 = "Press LEFT or RIGHT arrow keys to move";
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, tileSize / 2));
		if (player.getLives() <= 0) {
			text = "You lost by dying";
		}

		if (hasInvaded == true) {
			text = "You lost by invasion";
		}
		if (player.getLives() <= 0 || enemies.size() == enemyKillCount || hasInvaded == true) {
			g2.setColor(new Color(0, 0, 0, 150));
			g2.fillRect(0, 0, screenWidth, screenHeight);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, tileSize));
			g2.setColor(Color.white);
			g2.drawString(text, screenHeight / 2, screenHeight / 2);
		} else {
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, tileSize / 3));
			g2.setColor(Color.black);
			g2.drawString(guide, 0, screenHeight - tileSize / 3);
			g2.drawString(guide2, 0, screenHeight);
		}
		if(enemies.size() == enemyKillCount) {
			gameThread = null;
		}
		g2.dispose();
	}

	public void doDrawing(Graphics2D g2) {
		tileM.draw(g2);
		player.animate(g2);
		drawEnemy(g2);
		drawAttack(g2);
		drawEnemyAttack(g2);
		drawLives(g2);
	}

	public void drawEnemy(Graphics2D g2) {
		for (Enemy enemy : enemies) {
			if (enemy.isVisible()) {
				g2.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), tileSize, tileSize * 2, this);
			}

			if (enemy.isDying()) {
				enemy.die();
			}
		}
	}

	public void drawLives(Graphics2D g2) {
		try {
			Image img = ImageIO.read(getClass().getResourceAsStream("/player/heart.png"));
			for (int lives = 0; lives < player.getLives(); lives++) {
				g2.drawImage(img, lives * 50, lives, tileSize, tileSize, this);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void drawAttack(Graphics2D g2) {

		if (attack.isVisible()) {
			g2.drawImage(attack.getImage(), attack.getX(), attack.getY(), tileSize / 2, tileSize, this);
		}

	}

	public void drawEnemyAttack(Graphics2D g2) {
		for (Enemy enemy : enemies) {
			Enemy.EnemyAttack e = enemy.attack();
			if (!e.isDestroyed()) {
				g2.drawImage(e.getImage(), e.getX(), e.getY(), tileSize / 2, tileSize, this);
			}
		}
	}
}
