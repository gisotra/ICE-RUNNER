package ui;

import gamestates.Gamestate;
import java.awt.*;
import java.awt.image.BufferedImage;
import utilz.AnimationType;
import utilz.Sprite;
import utilz.Universal;

public class Buttons {
    
    /*---------- ATRIBUTOS ----------*/
    private int x, y, width, height;
    private Rectangle dimensoes; //vou usar isso pra inserir o click do mouse
    private BufferedImage spritesheetButton;
    private Sprite<ButtonAnimation> buttonSprite;
    private boolean cursorOver, cursorPressed;
    private Gamestate state;
    
    /*---------- CONSTRUTOR ----------*/
    public Buttons(int x, int y, int width, int height, BufferedImage image, Gamestate state) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.spritesheetButton = image;
        this.state = state;
        initRectangle();
        initSpritesheet(image);
        
    }
    
    /*---------- métodos próprios ----------*/
    public void initRectangle(){
            dimensoes = new Rectangle(this.x, this.y, this.width * (int) Universal.SCALE, this.height * (int)Universal.SCALE);
    }
    
    public void initSpritesheet(BufferedImage image){
        this.buttonSprite = new Sprite<>(spritesheetButton, image.getHeight(), image.getWidth(), ButtonAnimation.class, 1);
    }

    public void render(Graphics2D g2d){
        this.buttonSprite.render(g2d, this.x, this.y);
    }

    /*------------- GETTERS & SETTERS -------------*/
    
    public Rectangle getDimensoes(){
        return dimensoes;
    }

    public boolean isCursorOver() {
        return cursorOver;
    }

    public void setCursorOver(boolean cursorOver) {
        this.cursorOver = cursorOver;
    }

    public boolean isCursorPressed() {
        return cursorPressed;
    }

    public void setCursorPressed(boolean cursorPressed) {
        this.cursorPressed = cursorPressed;
    }
    
    public Gamestate getState(){
        return state;
    }
    
    public void applyGamestate() {
        Gamestate.state = state;
    }
    
    public void resetBooleans(){
        this.cursorOver = false;
        this.cursorPressed = false;
    }
    
    /*========== Classe interna Para o Sprite ==========*/ 
    public enum ButtonAnimation implements AnimationType{
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
