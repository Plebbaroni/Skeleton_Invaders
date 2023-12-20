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

	// FINAL SCREEN SETTINGS
	private final int ORIGINAL_TILE_SIZE = 16; // 16x16 tile
	private final int SCALE = 4;
	private final int TILE_SIZE = ORIGINAL_TILE_SIZE * SCALE; // 80x80 tile
	private final int MAX_SCREEN_COL = 24; // original = 24
	private final int MAX_SCREEN_ROW = 13; // original = 13
	private final int SCREEN_WIDTH = TILE_SIZE * MAX_SCREEN_COL;
	private final int SCREEN_HEIGHT = TILE_SIZE * MAX_SCREEN_ROW;

	// FPS
	private int FPS = 60;

	private TileManager tileM = new TileManager(this);
	private KeyHandler keyHandler = new KeyHandler();
	private Thread gameThread;
	private Player player;
	private PlayerAttack attack;
	private List<Enemy> enemies;
	private int enemyFireInterval;
	private float originalFireInterval;
	private int enemyKillCount;
	private boolean hasInvaded;

	public int getTileSize() {
		return TILE_SIZE;
	}

	public int getScale() {
		return SCALE;
	}

	public int getOriginalTileSize() {
		return ORIGINAL_TILE_SIZE;
	}

	public int getMaxScreenCol() {
		return MAX_SCREEN_COL;
	}

	public int getMaxScreenRow() {
		return MAX_SCREEN_ROW;
	}

	public int getScreenWidth() {
		return SCREEN_WIDTH;
	}

	public int getScreenHeight() {
		return SCREEN_HEIGHT;
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
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
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
		originalFireInterval = enemyFireInterval;
		hasInvaded = false;
		for (int y = 0; y <= 5; y++) {
			for (int x = 5; x <= 18; x++) {
				var enemy = (y < 4) ? new Skeleton(this.TILE_SIZE * x, this.TILE_SIZE * y)
						: new ArmoredSkeleton(this.TILE_SIZE * x, this.TILE_SIZE * y);
				enemies.add(enemy);
			}
		}

		player = new Player(this, keyHandler);
		attack = new PlayerAttack();
		player.setScreenX((int) player.getX());
		player.setScreenY((int) player.getY());
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
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
		// Game end
		if (player.getLives() <= 0 || hasInvaded == true || enemyKillCount == enemies.size()) {
			if (player.getLives() <= 0 || hasInvaded == true) {
				player.die();
				for (Enemy enemy : enemies) {
					enemy.moveLose(player, SCREEN_WIDTH, TILE_SIZE);
				}
			} else {
				gameThread = null;
			}
			attack.die();
			for (Enemy enemy : enemies) {
				if (!enemy.attack().isDestroyed()) {
					enemy.attack().setDestroyed(true);
				}
			}
		} else {
			// Player
			player.act();

			// Attack
			if (player.getKeyH().spacePressed == true && !attack.isVisible()) {
				attack = new PlayerAttack(player.getX(), player.getY());
			}
			if (attack.isVisible()) {
				int atkX = (int) attack.getX();
				int atkY = (int) attack.getY();
				for (Enemy enemy : enemies) {
					int enemyX = (int) enemy.getX();
					int enemyY = (int) enemy.getY();

					if (enemy.isVisible() && attack.isVisible()) {
						if (atkX >= enemyX && atkX <= (enemyX + TILE_SIZE / 2) && atkY >= enemyY
								&& atkY <= (enemyY + TILE_SIZE)) {
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
								enemy.die();
								enemyKillCount++;
							}
						}
					}
				}
				int y = (int) attack.getY();
				y -= 10;

				if (y < 0) {
					attack.die();
				} else {
					attack.setY(y);
				}
			}
			// Enemy
			for (Enemy enemy : enemies) {
				if (enemy.getX() >= SCREEN_WIDTH - TILE_SIZE && enemy.getDirection() == "right") {
					Iterator<Enemy> i1 = enemies.iterator();
					while (i1.hasNext()) {
						Enemy e1 = i1.next();
						e1.setY(e1.getY() + TILE_SIZE / 2);
						e1.setDirection("left");
					}

				}
				if (enemy.getX() <= 0 && enemy.getDirection() == "left") {
					Iterator<Enemy> i2 = enemies.iterator();
					while (i2.hasNext()) {
						Enemy e2 = i2.next();
						e2.setY(e2.getY() + TILE_SIZE / 2);
						e2.setDirection("right");
					}

				}
			}

			Iterator<Enemy> it = enemies.iterator();

			while (it.hasNext()) {
				Enemy enemy = it.next();
				if (enemy.isVisible()) {
					int y = (int) enemy.getY();
					if (y >= player.getY() - TILE_SIZE / 2) {
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
				int atkX = (int) attack.getX();
				int atkY = (int) attack.getY();
				int playerX = (int) player.getX();
				int playerY = (int) player.getY();
				if (player.isVisible() && !attack.isDestroyed()) {
					attack.setY(attack.getY() + 5);
					if (atkY >= playerY + TILE_SIZE) {
						attack.setDestroyed(true);
					}
					if (atkX >= playerX && atkX <= (playerX + TILE_SIZE / 2) && atkY >= playerY
							&& atkY <= (playerY + TILE_SIZE)) {
						player.setLives(player.getLives() - 1);
						player.setDirection("hit");
						attack.setDestroyed(true);
					}
				}
			}

			// Dynamic Difficulty
			for (Enemy enemy : enemies) {
				if (enemyKillCount >= Math.floor(enemies.size() * 0.99)) {
					enemy.setSpeed(9.0f);
					enemyFireInterval = (int) (originalFireInterval * 0.04f);
				} else if (enemyKillCount >= Math.floor(enemies.size() * 0.96)) {
					enemy.setSpeed(4.5f);
					enemyFireInterval = (int) (originalFireInterval * 0.08f);
				} else if (enemyKillCount >= Math.floor(enemies.size() * 0.88)) {
					enemy.setSpeed(4.1f);
					enemyFireInterval = (int) (originalFireInterval * 0.16f);
				} else if (enemyKillCount >= Math.floor(enemies.size() * 0.80)) {
					enemy.setSpeed(3.7f);
					enemyFireInterval = (int) (originalFireInterval * 0.24f);
				} else if (enemyKillCount >= Math.floor(enemies.size() * 0.72)) {
					enemy.setSpeed(3.3f);
					enemyFireInterval = (int) (originalFireInterval * 0.32f);
				} else if (enemyKillCount >= Math.floor(enemies.size() * 0.64)) {
					enemy.setSpeed(2.9f);
					enemyFireInterval = (int) (originalFireInterval * 0.38f);
				} else if (enemyKillCount >= Math.floor(enemies.size() * 0.56)) {
					enemy.setSpeed(2.6f);
					enemyFireInterval = (int) (originalFireInterval * 0.44f);
				} else if (enemyKillCount >= Math.floor(enemies.size() * 0.48)) {
					enemy.setSpeed(2.3f);
					enemyFireInterval = (int) (originalFireInterval * 0.52f);
				} else if (enemyKillCount >= Math.floor(enemies.size() * 0.40)) {
					enemy.setSpeed(2.0f);
					enemyFireInterval = (int) (originalFireInterval * 0.60f);
				} else if (enemyKillCount >= Math.floor(enemies.size() * 0.32)) {
					enemy.setSpeed(1.8f);
					enemyFireInterval = (int) (originalFireInterval * 0.68f);
				} else if (enemyKillCount >= Math.floor(enemies.size() * 0.24)) {
					enemy.setSpeed(1.6f);
					enemyFireInterval = (int) (originalFireInterval * 0.76f);
				} else if (enemyKillCount >= Math.floor(enemies.size() * 0.16)) {
					enemy.setSpeed(1.4f);
					enemyFireInterval = (int) (originalFireInterval * 0.84f);
				} else if (enemyKillCount >= Math.floor(enemies.size() * 0.08)) {
					enemy.setSpeed(1.2f);
					enemyFireInterval = (int) (originalFireInterval * 0.92f);
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

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		doDrawing(g2);
		String text = "You Win";
		String warning = "Warning: Invasion Imminent";
		String guide = "Press SPACE to shoot";
		String guide2 = "Press LEFT or RIGHT arrow keys to move";
		g2.setFont(g2.getFont().deriveFont(Font.BOLD, TILE_SIZE / 2));
		if (player.getLives() <= 0) {
			text = "You lost by dying";
		} else if (hasInvaded == true) {
			text = "You lost by invasion";
		}

		if (player.getLives() <= 0 || enemies.size() == enemyKillCount || hasInvaded == true) {
			g2.setColor(new Color(0, 0, 0, 150));
			g2.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, TILE_SIZE));
			g2.setColor(Color.white);
			g2.drawString(text, SCREEN_HEIGHT / 2, SCREEN_HEIGHT / 2);
		} else {
			g2.setFont(g2.getFont().deriveFont(Font.BOLD, TILE_SIZE / 3));
			g2.setColor(Color.black);
			g2.drawString(guide, 0, SCREEN_HEIGHT - TILE_SIZE / 3);
			g2.drawString(guide2, 0, SCREEN_HEIGHT);
			// Warns if invaders are getting closer
			for (Enemy enemy : enemies) {
				if (enemy.isVisible() && enemy.getY() >= player.getY() - (TILE_SIZE * 2)) {
					g2.setFont(g2.getFont().deriveFont(Font.BOLD, TILE_SIZE / 3));
					g2.setColor(Color.red);
					g2.drawString(warning, SCREEN_HEIGHT / 2, TILE_SIZE / 2);
				}
			}
		}
		g2.dispose();
	}

	public void doDrawing(Graphics2D g2) {
		tileM.draw(g2);
		drawPlayer(g2);
		drawEnemy(g2);
		drawAttack(g2);
		drawEnemyAttack(g2);
		drawLives(g2);
	}

	public void drawPlayer(Graphics2D g2) {
		if (player.isVisible()) {
			g2.drawImage(player.getImage(), player.getScreenX(), player.getScreenY(), TILE_SIZE, TILE_SIZE * 2, null);
		}
	}

	public void drawEnemy(Graphics2D g2) {
		for (Enemy enemy : enemies) {
			if (enemy.isVisible()) {
				g2.drawImage(enemy.getImage(), (int) enemy.getX(), (int) enemy.getY(), TILE_SIZE, TILE_SIZE * 2, this);
			}
		}
	}

	public void drawLives(Graphics2D g2) {
		try {
			Image img = ImageIO.read(getClass().getResourceAsStream("/player/heart.png"));
			for (int lives = 0; lives < player.getLives(); lives++) {
				g2.drawImage(img, lives * 50, lives, TILE_SIZE, TILE_SIZE, this);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void drawAttack(Graphics2D g2) {
		if (attack.isVisible()) {
			g2.drawImage(attack.getImage(), (int) attack.getX(), (int) attack.getY(), TILE_SIZE / 2, TILE_SIZE, this);
		}

	}

	public void drawEnemyAttack(Graphics2D g2) {
		for (Enemy enemy : enemies) {
			Enemy.EnemyAttack e = enemy.attack();
			if (!e.isDestroyed()) {
				g2.drawImage(e.getImage(), (int) e.getX(), (int) e.getY(), TILE_SIZE / 2, TILE_SIZE, this);
			}
		}
	}
}
