
package instances.entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import utilz.AnimationType;
import utilz.Sprite;
import utilz.SpriteData;
import utilz.SpriteLoader;
import utilz.Universal;

public class PowerUps {
    /*
    x, y, sprite, spriteScale, index, horizontalSpeed, verticalSpeed
    */
    private float x, y;
    private BufferedImage sprite;
    private int index;
    private float horizontalSpeed = Universal.BASE_SPEED;
    private float verticalSpeed;
    private Sprite<PowerUpAnimation> powerUpSprite;
    private Rectangle2D.Float collisionArea;
    private float collAreaWidth;
    private float collAreaHeight;

    public PowerUps(int index, int y) {
        this.index = index;
        this.x = Universal.OBST_SPAWN_X;
        this.y = y;
        initProperties();
        
    }
    
    public void initProperties(){
        switch(index){
            case 1:{//sword
                initSprites();
            }break;
            case 2:{//shield
                initSprites();
            }break;
            case 3:{//cap
                initSprites();
            }break;
        }
    }
    
    public void initSprites(){
        
        SpriteData powerData = SpriteLoader.spriteDataLoader().get("powerUp" + String.valueOf(this.index));
        
        try{
            sprite= ImageIO.read(getClass().getResource(powerData.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        powerUpSprite = new Sprite(sprite, sprite.getHeight(), sprite.getWidth(), PowerUpAnimation.class, 1);
    }
    
    public void initCollisionArea(){
        collAreaWidth = powerUpSprite.getLarguraFrameEscalonado() / 2;
        collAreaHeight = powerUpSprite.getAlturaFrameEscalonado() / 2;
        
        this.collisionArea = new Rectangle2D.Float(x, y, collAreaWidth, collAreaHeight);
        
    }
    public void drawCollisionArea(Graphics2D g2d){
        g2d.setColor(Color.WHITE);
        g2d.drawRect((int) collisionArea.x, (int) collisionArea.y, (int) collisionArea.width, (int) collisionArea.height); 
    }
    
    
    public void update(float deltaTime){
        setX(getX() + horizontalSpeed * deltaTime);
    }
    
    public void render(Graphics2D g2d, float x, float y){
        int drawX = (int) (x - powerUpSprite.getLarguraFrameEscalonado() / 2);
        int drawY = (int) (y - powerUpSprite.getAlturaFrameEscalonado() / 2) ;
        powerUpSprite.render(g2d, drawX, drawY);
        if(Universal.showGrid){
            drawCollisionArea(g2d);
        }
    }
 
    /*========== Classe interna Para os Sprites ==========*/
    public enum PowerUpAnimation implements AnimationType{
        STATIC;
        
        @Override
        public int getIndex(){
            return 0;
        }
        
        @Override
        public int getFrameCount(){
            return 1;
        }
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
    
    
}
