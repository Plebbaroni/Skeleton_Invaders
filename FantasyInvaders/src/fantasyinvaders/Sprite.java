/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fantasyinvaders;

/**
 *
 * @author Marlex
 */
public class Sprite {
    private boolean alive;
    private Image img;
    private boolean dying;
    private int x;
    private int y;
    
    public Sprite(){
        alive = true;
    }
    public void dead(){
        alive = false;
    }
    public boolean isAlive(){
        return alive;
    }
    public Image getImg(){
        return img;
    }
    
    public void setImg(Image img){
        this.img = img;
    }
    public int getX(){
        return x;
    }
    public void setX(int x){
        this.x = x;
    }
    public int getY(){
        return y;
    }
    public void setY(int y){
        this.y = y;
    }
    public void setDying(boolean dying) {

        this.dying = dying;
    }
    public boolean isDying() {

        return this.dying;
    }
}
