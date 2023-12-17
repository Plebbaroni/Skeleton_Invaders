package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import main.GamePanel;

public class PrimaryAttack {
	public LinkedList<Arrow> a = new LinkedList<Arrow>();
	
	
	GamePanel game;
	
	public PrimaryAttack(GamePanel game) {
		this.game = game;
	}
	Arrow TempArrow;
	
	public void tick() {

		for(int i=0; i<a.size(); i++) {
			TempArrow = a.get(i);
			
			TempArrow.tick();
		}
	}
	
	public void draw(Graphics g) {
		for(int i=0; i<a.size(); i++) {
			TempArrow = a.get(i);
			
			TempArrow.draw(g);
		}
	}
	
	public void addArrow(Arrow block) {
		a.add(block);
	}
	public void removeArrow(Arrow block) {
		a.remove(block);
	}

	public double getX() {
		// TODO Auto-generated method stub
		return 0;
	}
}
