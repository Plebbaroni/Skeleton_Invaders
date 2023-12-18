package entity;

import java.awt.Graphics2D;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import main.GamePanel;

public class Enemy extends Entity{
	private int health;
	private EnemyAttack attack;
	
	public Enemy(int x, int y) {
		initEnemy(x, y);
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
	
	public void move(int direction) {
		setX(getX() + direction);
	}
	
	public class EnemyAttack extends Entity{
		private boolean isDestroyed;
		
		public EnemyAttack(int x, int y) {
			initAttack(x, y);
		}
		
		public boolean getDestroyed(){
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
		public Skeleton(int x, int y) {
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
	
	public static class ArmoredSkeleton extends Skeleton{
		public ArmoredSkeleton(int x, int y) {
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
