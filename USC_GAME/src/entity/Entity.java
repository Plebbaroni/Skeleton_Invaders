package entity;

import java.awt.Rectangle;
import java.awt.Image;

public class Entity {
	private int worldX;
	private int worldY;
	private int dirX;
	private int speed;
	private int originalSpeed;
	private Image image;
	private String direction;
	private int spriteCounter;
	private int spriteNum;
	private Rectangle solidArea;
	private boolean collisionOn;
	private boolean visible;
	private boolean dying;

	public Entity() {
		visible = true;
	}

	public void die() {
		visible = false;
	}

	public boolean isVisible() {

		return visible;
	}

	public void setVisible(boolean visible) {

		this.visible = visible;
	}

	public void setDying(boolean dying) {

		this.dying = dying;
	}

	public boolean isDying() {

		return this.dying;
	}

	public int getX() {
		return worldX;
	}

	public void setX(int x) {
		this.worldX = x;
	}

	public int getDirX() {
		return dirX;
	}

	public void setDirX(int x) {
		this.dirX = x;
	}

	public int getY() {
		return worldY;
	}

	public void setY(int y) {
		this.worldY = y;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getOriginalSpeed() {
		return originalSpeed;
	}

	public void setOriginalSpeed(int originalSpeed) {
		this.originalSpeed = originalSpeed;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public int getSpriteCounter() {
		return spriteCounter;
	}

	public void setSpriteCounter(int spriteCounter) {
		this.spriteCounter = spriteCounter;
	}

	public int getSpriteNum() {
		return spriteNum;
	}

	public void setSpriteNum(int spriteNum) {
		this.spriteNum = spriteNum;
	}

	public Rectangle getSolidArea() {
		return solidArea;
	}

	public void setSolidArea(Rectangle solidArea) {
		this.solidArea = solidArea;
	}

	public boolean isCollisionOn() {
		return collisionOn;
	}

	public void setCollisionOn(boolean collisionOn) {
		this.collisionOn = collisionOn;
	}
}
