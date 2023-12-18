package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;
import java.awt.Color;

public class TileManager {

	GamePanel gp;
	public Tile[] tile;
	public int mapTileNum[][];
	
	
	public TileManager(GamePanel gp) {
		
		this.gp = gp;
		
		tile = new Tile[70];
		mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];
		
		getTileImage();
		loadMap();
	}
	
	public void getTileImage() {
		try {
			System.out.println("Resource path: " + getClass().getResource("/tiles/Outside.png"));
			System.out.println("Classpath: " + System.getProperty("java.class.path"));
			
			tile[0] = new Tile();
	        tile[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Outside.png"));
	        
//	        LOOP FOR TILES 1 - 55
	        /*for(int i=1; i<51; i++) {
	        	System.out.println("Resource path: " + "/tiles/Outside.png");
	        	 tile[i] = new Tile();
	        	 tile[i].image = ImageIO.read(getClass().getResourceAsStream("/tiles/Outside" + i + ".png"));
	        	 tile[i].collision = true;
	        }*/
			
	        for(int i=1; i<56; i++) {
	        	 tile[i] = new Tile();
	        	 tile[i].image = ImageIO.read(getClass().getResourceAsStream("/tiles/joegrass.png"));
	        	 if (i == 34 || (i >= 62 && i <= 67)) {
	                    tile[i].collision = true;
	                }
	        }
	        
	        for (int i = 56; i <= 67; i++) {
	            tile[i] = new Tile();
	            try {
	                String imageName = "/tiles/fort" + i + ".png";
	                tile[i].image = ImageIO.read(getClass().getResourceAsStream(imageName));
	                if ((i >= 62 && i <= 67)) {
	                    tile[i].collision = true;
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	                System.out.println("Failed to load image at path: /tiles/fort" + (i - 55) + ".png");
	            }
	        }
	        tile[1].collision = true;
	        tile[2].collision = true;
	        tile[68] = new Tile();
	        tile[69] = new Tile();
	        tile[68].image = ImageIO.read(getClass().getResourceAsStream("/tiles/fort56.png"));
	        tile[68].collision = true;
	        tile[69].image = ImageIO.read(getClass().getResourceAsStream("/tiles/fort61.png"));
	        tile[69].collision = true;
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadMap() {
		try {
			InputStream is = getClass().getResourceAsStream("/maps/world01.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			
			while(col < gp.maxWorldCol && row < gp.maxWorldRow) {
				
				String line = br.readLine();
				
				while(col <gp.maxWorldCol) {
					
					String numbers[] = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					
					mapTileNum[col][row] = num;
					col++;
				}
				if(col==gp.maxWorldCol) {
					col = 0;
					row++;
				}
			}
			br.close();
			
		}catch(Exception e) {
			
		}
	}
	
	public void draw(Graphics2D g2) {
	    int worldCol = 0;
	    int worldRow = 0;

	    while (worldCol < gp.maxWorldCol && worldRow < gp.maxWorldRow) {
	        int tileNum = mapTileNum[worldCol][worldRow];

	        int worldX = worldCol * gp.tileSize;
	        int worldY = worldRow * gp.tileSize;
	        int screenX = worldX - gp.player.getX() + gp.player.screenX;
	        int screenY = worldY - gp.player.getY() + gp.player.screenY;

	        // Draw the tile image
	        g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);

	        // Draw the corresponding number on top of the tile
	        g2.setColor(Color.WHITE);
	     
	        worldCol++;

	        if (worldCol == gp.maxWorldCol) {
	            worldCol = 0;
	            worldRow++;
	        }
	    }
	}
	
	
}
