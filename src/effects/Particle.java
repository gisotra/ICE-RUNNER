package effects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import utilz.Sprite;

public class Particle {
    /*
    Seria uma classe de partículas o mais modularizada possível
    x, y, recebe uma spritesheet, recebe uma speed para passar a animação, só vai desenhar a sprite uma vez
    a spritesheet nao terá mais de um índice, apenas um conjunto de frames linear, para diminuir a complexidade
    */
    private float x, y;
    private BufferedImage spritesheetOriginal;
    private BufferedImage spritesheetOriginalSCALED;
    private int numberOfFrames;
    
    public Particle(float x, float y, BufferedImage spritesheetOriginal, int numberOfFrames){
        this.x = x;
        this.y = y;
        this.spritesheetOriginal = spritesheetOriginal;
        this.numberOfFrames = numberOfFrames;
    }
    
    public void render(Graphics2D g2d){
        
    }
    
}
