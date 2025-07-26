package instances.entities;

import instances.Objects;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import loop.GCanvas;
import utilz.Screen;

public abstract class Entities extends Objects{
    
    /*------------ ATRIBUTOS ------------*/
    protected Rectangle2D.Float hitbox;
    
    public Entities(Screen screen, GCanvas gc){
        super(screen, gc);
        initHitbox(getX(), getY(), 45, 78);
    }
    /*------------ MÉTODOS ------------*/
    protected void drawHitbox(Graphics g) {
        //para testar a hitbox
        g.setColor(Color.BLACK);
        g.drawRect((int) hitbox.x, (int) hitbox.y, (int) hitbox.width, (int) hitbox.height);
    }
    
    protected void initHitbox(float x, float y, float width, float height) {
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }
    
    protected void updateHitbox(){
        hitbox.x = getX(); //atualizo a posição horizontal
        hitbox.y = getY(); //atualizo a posição vertical
    }

    public Rectangle2D.Float getHitbox(){
        return hitbox;
    }

    
    @Override
    public abstract void update(float deltaTime);

    @Override
    public abstract void render(Graphics2D g2d);
    
    public float getHitboxHeight() {
        return (float) hitbox.height;
    }
    
    public float getHitboxWidth() {
        return (float) hitbox.height;
    }

}
