package entity;

import java.awt.Graphics2D;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import java.awt.Rectangle;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

	private GamePanel gp;
	private KeyHandler keyH;
	private int lives;
	private int screenX;
	private int screenY;

	public Player(GamePanel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		Rectangle area = new Rectangle();
		area.x = 20;
		area.y = 135;
		area.width = 40;
		area.height = 12;
		setSolidArea(area);
		setDefaultValues();
		getPlayerImage();
	}

	// SET DEFAULT VALUES OF PLAYER HERE
	public void setDefaultValues() {
		setX(gp.getTileSize() * 9);
		setY(gp.getTileSize() * 10);
		lives = 3;
		setSpeed(0);
		setDirection("right");
	}

	public GamePanel getGp() {
		return gp;
	}

	public void setGp(GamePanel gp) {
		this.gp = gp;
	}

	public KeyHandler getKeyH() {
		return keyH;
	}

	public void setKeyH(KeyHandler keyH) {
		this.keyH = keyH;
	}

	public int getScreenX() {
		return screenX;
	}

	public void setScreenX(int screenX) {
		this.screenX = screenX;
	}

	public int getScreenY() {
		return screenY;
	}

	public void setScreenY(int screenY) {
		this.screenY = screenY;
	}

	public void getPlayerImage() {
		var ii = new ImageIcon("/player/newPlayer1.png");
		setImage(ii.getImage());
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public void act() {
		int imageCycler = 0;
		if (keyH.leftPressed == false && keyH.rightPressed == false) {
			setSpriteNum(1);
		} else {
			if (keyH.leftPressed) {
				setDirection("left");
				setX(getX() - getSpeed());
			}

			if (keyH.rightPressed) {
				setDirection("right");
				setX(getX() + getSpeed());
			}

			// CHECK TILE COLLISION
			// IF COLLISION IS TRUE; PLAYER CANNOT MOVE
			if(getX() <= 0) {
				setX(0);
			}
			if(getX() >= gp.getScreenWidth() - gp.getTileSize()) {
				setX(gp.getScreenWidth() - gp.getTileSize());
			}

			setSpriteCounter(getSpriteCounter() + 1);
			if (getSpriteCounter() > imageCycler) {
				switch (getSpriteNum()) {
				case 1:
					setSpriteNum(2);
					break;
				case 2:
					setSpriteNum(3);
					break;
				case 3:
					setSpriteNum(4);
					break;

				case 4:
					setSpriteNum(1);
					break;
				}
				setSpriteCounter(0);
			}
			screenX = (int)getX();
			screenY = (int)getY();
		}

		if (keyH.shiftPressed == false) {
			setSpeed(6);
			imageCycler = 4;
		} else {
			setSpeed(12);
			imageCycler = 2;
		}
	}

	public void animate(Graphics2D g2) {
		switch (getDirection()) {
		// LEFT DIRECTION
		case "left":
			if (getSpriteNum() == 1) {
				try {
					setImage(ImageIO.read(getClass().getResourceAsStream("/player/newPlayer1.png")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (getSpriteNum() == 2) {
				try {
					setImage(ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (getSpriteNum() == 3) {
				try {
					setImage(ImageIO.read(getClass().getResourceAsStream("/player/newPlayer2.png")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;

		// RIGHT DIRECTION
		case "right":
			if (getSpriteNum() == 1) {
				try {
					setImage(ImageIO.read(getClass().getResourceAsStream("/player/newPlayer1.png")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (getSpriteNum() == 2) {
				try {
					setImage(ImageIO.read(getClass().getResourceAsStream("/player/newPlayer2.png")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (getSpriteNum() == 3) {
				try {
					setImage(ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case "hit":
			try {
				setImage(ImageIO.read(getClass().getResourceAsStream("/player/newPlayer4.png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "dead":
			try {
				setImage(ImageIO.read(getClass().getResourceAsStream("")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}

		g2.drawImage(getImage(), screenX, screenY, gp.getTileSize(), gp.getTileSize() * 2, null);
	}

	public static class PlayerAttack extends Entity {

		public PlayerAttack() {

		}

		public PlayerAttack(float x, float y) {
			attack(x, y);
		}

		private void attack(float x, float y) {
			try {
				setImage(ImageIO.read(getClass().getResourceAsStream("/player/arrowSprite.png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setX(x + 17);
			setY(y - 1);
		}
	}
}
