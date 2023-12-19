package entity;

import java.awt.Rectangle;
import java.awt.Image;

public class Entity {
	private int x;
	private int y;
	private int speed;
	private Image image;
	private String direction;
	private int spriteCounter;
	private int spriteNum;
	private Rectangle solidArea;
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
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
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

}
