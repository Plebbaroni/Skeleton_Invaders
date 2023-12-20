package entity;

import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy extends Entity {
	private int health;
	private EnemyAttack attack;

	public Enemy(float x, float y) {
		setX(x);
		setY(y);
		attack = new EnemyAttack(x, y);
		setSpeed(1);
		setDirection("left");
		setSpriteNum(1);
		setSpriteCounter(19);
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public EnemyAttack attack() {
		return attack;
	}

	public void move() {
		setX((getDirection() == "right") ? getX() + getSpeed() : getX() - getSpeed());
		setSpriteCounter(getSpriteCounter() + 1);
		if (getSpriteCounter() > 20) {
			if (getSpriteNum() == 1 || getSpriteNum() == 3) {
				try {
					setImage(getHealth() == 2
							? ImageIO.read(getClass().getResourceAsStream("/enemy/ArmoredSkeleton2.png"))
							: ImageIO.read(getClass().getResourceAsStream("/enemy/Skeleton2.png")));
					setSpriteNum(2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					setImage(getHealth() == 2
							? ImageIO.read(getClass().getResourceAsStream("/enemy/ArmoredSkeleton3.png"))
							: ImageIO.read(getClass().getResourceAsStream("/enemy/Skeleton3.png")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setSpriteNum(3);

			}
			setSpriteCounter(0);
		}

	}

	public void moveLose(Player player, int screenWidth, int tileSize) {
		if (getY() < player.getY()) {
			setY(getY() + 5);
		} else if (getX() < (screenWidth - tileSize) / 2 - 5) {
			setX(getX() + 5);
		} else if (getX() > (screenWidth - tileSize) / 2 + 5) {
			setX(getX() - 5);
		} else {
			die();
		}
		setSpriteCounter(getSpriteCounter() + 1);
		if (getSpriteCounter() > 15) {
			if (getSpriteNum() == 1 || getSpriteNum() == 3) {
				try {
					setImage(getHealth() == 2
							? ImageIO.read(getClass().getResourceAsStream("/enemy/ArmoredSkeleton2.png"))
							: ImageIO.read(getClass().getResourceAsStream("/enemy/Skeleton2.png")));
					setSpriteNum(2);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				try {
					setImage(getHealth() == 2
							? ImageIO.read(getClass().getResourceAsStream("/enemy/ArmoredSkeleton3.png"))
							: ImageIO.read(getClass().getResourceAsStream("/enemy/Skeleton3.png")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setSpriteNum(3);

			}
			setSpriteCounter(0);
		}
	}

	public class EnemyAttack extends Entity {
		private boolean isDestroyed;

		public EnemyAttack(float x, float y) {
			initAttack(x, y);
		}

		public boolean isDestroyed() {
			return isDestroyed;
		}

		public void setDestroyed(boolean destroyed) {
			this.isDestroyed = destroyed;
		}

		public void initAttack(float x, float y) {
			setDestroyed(true);
			setX(x);
			setY(y);
			try {
				setImage(ImageIO.read(getClass().getResourceAsStream("/enemy/ArrowSprite.png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static class Skeleton extends Enemy {
		public Skeleton(float x, float y) {
			super(x, y);
			try {
				setImage(ImageIO.read(getClass().getResourceAsStream("/enemy/Skeleton1.png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setHealth(1);
		}
	}

	public static class ArmoredSkeleton extends Skeleton {
		public ArmoredSkeleton(float x, float y) {
			super(x, y);
			try {
				setImage(ImageIO.read(getClass().getResourceAsStream("/enemy/ArmoredSkeleton1.png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setHealth(2);
		}
	}
}
