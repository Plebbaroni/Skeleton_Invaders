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
	}
	
  public void setDefaultValues(){
        setSpeed(5);
 
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
	
	public void move(int direction) {
		setX(getX() + direction);
	}
	
	public void act() {
        setX((getDirection()=="right")?getX() + getSpeed(): getX() - getSpeed());
        
        // CHECK TILE COLLISION
        setCollisionOn(false);
        gp.cChecker.checkTile(this);
        // IF COLLISION IS TRUE; PLAYER CANNOT MOVE
	}
	
	public void changeDirection(String direction){
		int tempWorldX = getX();
        int tempWorldY = getY();
        setX(tempWorldX);
        setY(tempWorldY);
        
        switch(direction) { 
        case "right":        
            setDirection("left");
            setY(getY() + getSpeed());
            setCollisionOn(false);
        	break;
        	
        case "left":  
            setDirection("right");
            setY(getY() + getSpeed());
            setCollisionOn(false);
        	break;
        }
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
