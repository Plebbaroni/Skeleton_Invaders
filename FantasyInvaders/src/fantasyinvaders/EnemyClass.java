/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fantasyinvaders;

/**
 *
 * @author Marlex
 */
public class EnemyClass extends Sprite{
    private int health;
    private Arrow arrow;
    
    public EnemyClass(int x, int y){
        initEnemy(x, y);
    }
    
    public int getHealth(){
        return health;
    }
    
    public void setHealth(int health){
        this.health = health;
    }
    
    public void initEnemy(int x, int y){
        setX(x);
        setY(y);
        this.arrow = new Arrow(x, y);
        /* I need images */
    }
    
    // Fires an arrow
    public Arrow getArrow(){
        return arrow;
    }
    
    // Positions an enemy in horizontal direction
    public void move(int direction){
        this.x += direction;
    }
    
    public class Arrow extends Sprite{
        private boolean isDestroyed;

        public Arrow(int x, int y){
            initArrow(x, y);
        }
        
        public boolean getDestroyed(){
            return isDestroyed;
        }
        public void setDestroyed(boolean destroyed){
            this.isDestroyed = destroyed;
        }
        public  void initArrow(int x, int y){
            setDestroyed(true);
            setX(x);
            setY(y);
            /* I need images */
        }
        
    }
}

    
