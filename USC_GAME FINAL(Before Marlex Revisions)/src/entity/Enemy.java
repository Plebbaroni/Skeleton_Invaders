package entity;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import main.GamePanel;

public class Enemy extends Entity{
	private int health;
	private EnemyAttack attack;
	public GamePanel gp;
	private int enemySprite;
	private int moveCounter;
	private int animator;
	public Enemy(GamePanel gp, int x, int y) {
		this.gp = gp;
		initEnemy(x, y);
		Rectangle area = new Rectangle();
        area.x = 20;
        area.y = 135;
        area.width = 40;
        area.height = 12;
        setSolidArea(area);
        setDefaultValues();
        enemySprite = 1;
        moveCounter = 0;
        animator = 20;
	}
	
	public int getEnemySprite() {
		return enemySprite;
	}
	
	public void setEnemySprite(int num) {
		this.enemySprite = num;
	}
	
  public void setDefaultValues(){
        setSpeed(1);
 
        setDirection("right");
    }
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public void initEnemy(int x, int y) {
		setX(x);
		setY(y);
		this.attack = new EnemyAttack(x, y);
	}
	
	public EnemyAttack attack() {
		return attack;
	}
	
	public void move() {
		setX((getDirection()=="right")?getX() + getSpeed(): getX() - getSpeed());
		moveCounter++;
		if(moveCounter > animator) {
			if(enemySprite == 1 || enemySprite == 3) {
				try {
					setImage(getHealth() == 2 ?
		                    ImageIO.read(getClass().getResourceAsStream("/enemy/ArmoredSkeleton2.png")) :
		                    ImageIO.read(getClass().getResourceAsStream("/enemy/Skeleton2.png")));
					enemySprite = 2;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if (enemySprite == 2) {
				try {
					setImage(getHealth() == 2 ?
		                    ImageIO.read(getClass().getResourceAsStream("/enemy/ArmoredSkeleton3.png")) :
		                    ImageIO.read(getClass().getResourceAsStream("/enemy/Skeleton3.png")));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				enemySprite = 3;
			}
			moveCounter = 0;
		}
	}
	
	public class EnemyAttack extends Entity{
		private boolean isDestroyed;
		
		public EnemyAttack(int x, int y) {
			initAttack(x, y);
		}
		
		public boolean isDestroyed(){
            return isDestroyed;
        }
        public void setDestroyed(boolean destroyed){
            this.isDestroyed = destroyed;
        }
        public  void initAttack(int x, int y){
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
	
	public static class Skeleton extends Enemy{
		public Skeleton(GamePanel gp, int x, int y) {
			super(gp ,x, y);
			try {
				setImage(ImageIO.read(getClass().getResourceAsStream("/enemy/Skeleton1.png")));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			setHealth(1);
		}
	}
	
	public static class ArmoredSkeleton extends Skeleton{
		public ArmoredSkeleton(GamePanel gp, int x, int y) {
			super(gp, x, y);
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
