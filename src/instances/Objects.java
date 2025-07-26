package instances;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import loop.GCanvas;
import utilz.Screen;

/*Objects vai ser uma classe que todos os elementos do meu jogo vão herdar, tanto
o player1, quantos os obstáculos quanto o chão.*/

public abstract class Objects { 
    /*------------ ATRIBUTOS ------------*/
    public Screen screen;
    public GCanvas gc;
    public float x, y;
    public int widthO, heightO;
    public boolean isActive = false;
    
    public Objects(Screen screen, GCanvas gc){
        this.screen = screen;
        this.gc = gc;
    }
    
    /*------------ GETTERS ------------*/
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
    
    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {
        return widthO;
    }

    public int getHeight() {
        return heightO;
    }
    
    public void setWidth(int width) {
        this.widthO = width;
    }
    
    public void setHeight(int height) {
        this.heightO = height;
    }
    
    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    /*------------ MÉTODOS ABSTRATOS ------------*/
    public abstract void update(float deltaTime);

    public abstract void render(Graphics2D g2d);
}
