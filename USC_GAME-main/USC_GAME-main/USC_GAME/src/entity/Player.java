package entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import java.awt.Rectangle;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
    
    GamePanel gp;
    KeyHandler keyH;
    
    PrimaryAttack pa = new PrimaryAttack(gp);
    public int screenX;
    public int screenY;
    
    public Player(GamePanel gp, KeyHandler keyH) {
        this.gp = gp;
        this.keyH = keyH;

        
        solidArea = new Rectangle();
        solidArea.x = 20;
        solidArea.y = 135;
        solidArea.width = 40;
        solidArea.height = 12;
        
        setDefaultValues();
        getPlayerImage();
    }
// SET DEFAULT VALUES OF PLAYER HERE
    public void setDefaultValues(){
        worldX = gp.tileSize * 12;
        worldY = gp.tileSize * 10;
        speed = 4;
        direction = "right";
        }
    
    public void getPlayerImage(){
        
        try {
            
            System.out.println("Image loading started");
         // load sprite images....
            up1 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            up2 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            up3 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            up4 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            down1 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            down3 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            down4 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            left1 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            left2 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            left3 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            left4 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            right1 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            right2 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            right3 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            right4 = ImageIO.read(getClass().getResourceAsStream("/player/newPlayer3.png"));
            System.out.println("Image loading ended");
            
        }catch(IOException e){
            e.printStackTrace();
            
        }
    }

    public void update() {
        int imageCycler = 0;

        if (keyH.upPressed == false && keyH.downPressed == false &&
                keyH.leftPressed == false && keyH.rightPressed == false) {
            spriteNum = 1;
        }

        if (keyH.shiftPressed == false) {
            speed = 4;
            imageCycler = 9;
        } else {
            speed = 7;
            imageCycler = 5;
        }
        
        int tempWorldX = worldX;
        int tempWorldY = worldY;

        if (keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed || keyH.spacePressed) {

            /*if (keyH.upPressed) {
                direction = "up";
                worldY -= speed;
            }

            if (keyH.downPressed) {
                direction = "down";
                worldY += speed;
            }*/

            if (keyH.leftPressed) {
                direction = "left";
                worldX -= speed;
            }

            if (keyH.rightPressed) {
                direction = "right";
                worldX += speed;
            }
            
            if(keyH.spacePressed) {
            	pa.addArrow(new Arrow(worldX, worldY, gp));           	
            }

            // Normalize diagonal movement
           /* if ((keyH.upPressed || keyH.downPressed) && (keyH.leftPressed || keyH.rightPressed)) {
                worldX = tempWorldX + (int) (speed / Math.sqrt(2) * (keyH.leftPressed ? -1 : 1));
                worldY = tempWorldY + (int) (speed / Math.sqrt(2) * (keyH.upPressed ? -1 : 1));
            }*/

            // CHECK TILE COLLISION
            collisionOn = false;
            gp.cChecker.checkTile(this);
            // IF COLLISION IS TRUE; PLAYER CANNOT MOVE
            if (collisionOn) {
                worldX = tempWorldX;
                worldY = tempWorldY;
            }
            
            spriteCounter++;
            if (spriteCounter > imageCycler) {
                switch (spriteNum) {
                    case 1:
                        spriteNum = 2;
                        break;
                    case 2:
                        spriteNum = 3;
                        break;
                    case 3:
                        spriteNum = 4;
                        break;

                    case 4:
                        spriteNum = 1;
                        break;
                }
                spriteCounter = 0;
            }
            screenX = worldX;
            screenY = worldY;
        }
    }
    	
    public void draw(Graphics2D g2) {

        BufferedImage image = null;

        switch (direction) {
            // UP DIRECTION
            case "up":
                if (spriteNum == 1) {
                    image = up1;
                }
                if (spriteNum == 2) {
                    image = up2;
                }
                if (spriteNum == 3) {
                    image = up3;
                }
                if (spriteNum == 4) {
                    image = up4;
                }
                break;

            // DOWN DIRECTION
            case "down":
                if (spriteNum == 1) {
                    image = down1;
                }
                if (spriteNum == 2) {
                    image = down2;
                }
                if (spriteNum == 3) {
                    image = down3;
                }
                if (spriteNum == 4) {
                    image = down4;
                }
                break;

            // LEFT DIRECTION
            case "left":
                if (spriteNum == 1) {
                    image = left1;
                }
                if (spriteNum == 2) {
                    image = left2;
                }
                if (spriteNum == 3) {
                    image = left3;
                }
                if (spriteNum == 4) {
                    image = left4;
                }
                break;

            // RIGHT DIRECTION
            case "right":
                if (spriteNum == 1) {
                    image = right1;
                }
                if (spriteNum == 2) {
                    image = right2;
                }
                if (spriteNum == 3) {
                    image = right3;
                }
                if (spriteNum == 4) {
                    image = right4;
                }
                break;
        }

        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize * 2, null);
    }
}

