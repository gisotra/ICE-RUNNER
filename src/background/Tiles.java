package background;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import utilz.Universal;

public class Tiles {
    private float x, y;
    private BufferedImage tileImage;
    private int altura;
    private int largura = Universal.spriteEnviroWidthSLICED;
    
    public Tiles(BufferedImage spriteOriginal, float x, float y, int altura){
        this.tileImage = spriteOriginal;
        this.x = x;
        this.y = y;
        this.altura = altura;
    }
    
    /*------------- métodos do loop -------------*/
    
    public void update(float deltaTime, float speed){
        this.setX(this.getX() + Universal.BASE_SPEED * speed * deltaTime);
    }
    
    public void render(Graphics2D g2d){
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g2d.drawImage(tileImage,(int)x,(int)y, null);

        if(Universal.showGrid){
            g2d.drawRect(Math.round(x), Math.round(y), tileImage.getWidth(), tileImage.getHeight());

        }
    }
   
    /*------------- Getters e Setters -------------*/

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
    
    public int getWidth() {
        return tileImage.getWidth();
    }
    
}
