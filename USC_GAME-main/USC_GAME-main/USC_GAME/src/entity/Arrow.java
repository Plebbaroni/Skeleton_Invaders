package entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;

public class Arrow {
	public double x;
	public double y;
	
	BufferedImage image;
	
	public Arrow(double x, double y, GamePanel game) {
		this.x =x;
		this.y =y;
		
		getArrowImage();
	}
	
	public void tick(){
		y -=10;
	}
	
	public void draw(Graphics g) {
		g.drawImage(image, (int) x, (int) y,null);
	}
	
public void getArrowImage(){
        
        try {
            
            System.out.println("Image loading started");
         // load sprite images....
            image = ImageIO.read(getClass().getResourceAsStream("/player/arrow.png"));
       
            
        }catch(IOException e){
            e.printStackTrace();
            
        }
    }
}
