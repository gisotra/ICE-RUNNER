package ui;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import utilz.AnimationType;
import utilz.SpriteData;
import utilz.SpriteLoader;
import utilz.Sprite;
import utilz.Universal;


public class Playing implements ScreenStates {
    private BufferedImage fundoLoop;
    private Sprite<PlayingScreenAnimation> fundoSprite;
    
    public Playing(){
        initSpriteMenu();
    }
    
    public void initSpriteMenu(){
        SpriteData menuData = SpriteLoader.spriteDataLoader().get("layer0");
        
        try {
            fundoLoop = ImageIO.read(getClass().getResource(menuData.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //inicio as propriedades do meu sprite player
        this.fundoSprite = new Sprite<>(fundoLoop, 224, 512, PlayingScreenAnimation.class, 1); 
    }

    /*-------------- MÉTODOS HERDADOS --------------*/
    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2D) {
        fundoSprite.render(g2D, 0, 0);
    }

    @Override
    public boolean isIn(MouseEvent e, Buttons mb) {
        return mb.getDimensoes().contains(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //
    }

    @Override
    public void mousePressed(MouseEvent e) {
    
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    
    }

    private void resetButtons() {
    
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    
    /*========== Classe interna Para o Sprite ==========*/ 
    public enum PlayingScreenAnimation implements AnimationType{
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
}
