package ui;

import gamestates.Gamestate;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import network.ClientConnector;
import utilz.AnimationType;
import utilz.Screen;
import utilz.Sprite;
import utilz.SpriteData;
import utilz.SpriteLoader;
import utilz.Universal;

public class MultiplayerMenu implements ScreenStates {
    private BufferedImage multMenuFundo;
    private Sprite<MultiplayerMenuAnimation> multMenuSprite;
    private Buttons[] botoesMenu = new Buttons[4];
    private BufferedImage botaoAsServer;
    private BufferedImage botaoAsClient;
    private BufferedImage botaoLocal;
    private BufferedImage botaoExit;
    
    public MultiplayerMenu(){
        initSpriteMenu();
        botoesMenu[0] = new Buttons(3*Universal.TILES_SIZE, 3*Universal.TILES_SIZE + (Universal.TILES_SIZE/4), 48, 48, botaoAsServer, Gamestate.HOSTING); //servidor
        botoesMenu[1] = new Buttons(7*Universal.TILES_SIZE + (Universal.TILES_SIZE/4)  , 3*Universal.TILES_SIZE + (Universal.TILES_SIZE/4), 48, 48, botaoAsClient, Gamestate.WAITING); //cliente
        botoesMenu[2] = new Buttons(11*Universal.TILES_SIZE + (Universal.TILES_SIZE/2)  , 3*Universal.TILES_SIZE + (Universal.TILES_SIZE/4), 48, 48, botaoLocal, Gamestate.PLAYING); //multiplayer local
        botoesMenu[3] = new Buttons(20, 20, 48, 48, botaoExit, Gamestate.MENU); //voltar
    }
    
    public void initSpriteMenu(){
        SpriteData menuData = SpriteLoader.spriteDataLoader().get("fundoMenu");
        SpriteData asClientData = SpriteLoader.spriteDataLoader().get("asServerButton");
        SpriteData asServerData = SpriteLoader.spriteDataLoader().get("asClientButton");
        SpriteData localData = SpriteLoader.spriteDataLoader().get("localButton");
        SpriteData exitData = SpriteLoader.spriteDataLoader().get("exitbutton");
        
        try {
            multMenuFundo = ImageIO.read(getClass().getResource(menuData.getPath()));
            botaoAsServer = ImageIO.read(getClass().getResource(asClientData.getPath()));
            botaoAsClient = ImageIO.read(getClass().getResource(asServerData.getPath()));
            botaoLocal = ImageIO.read(getClass().getResource(localData.getPath()));
            botaoExit = ImageIO.read(getClass().getResource(exitData.getPath()));
            
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //inicio as propriedades do meu sprite player
        this.multMenuSprite = new Sprite<>(multMenuFundo, 288, 512, MultiplayerMenuAnimation.class, 1); 
    }

    /*-------------- MÉTODOS HERDADOS --------------*/
    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2D) {
        multMenuSprite.render(g2D, 0, 0);
        for (Buttons but : botoesMenu) {
            but.render(g2D);
        }
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
        for (Buttons but : botoesMenu) {
            if (isIn(e, but)) {
                but.setCursorPressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (Buttons but : botoesMenu) {
            if (isIn(e, but)) {
                if (but.isCursorPressed()) {
                    if (but.getState() == Gamestate.PLAYING) { 
                        but.applyGamestate();
                        Universal.bothPlayingLocal = true;
                        Universal.youAreAClient = false;
                        Universal.youAreAHost = false;
                        Screen.resetCoordenates();
                        Screen.startCoordenates();
                    } else if (but.getState() == Gamestate.WAITING){
                        ClientConnector.waitingConnection = true;
                        but.applyGamestate();
                    } else if (but.getState() == Gamestate.HOSTING) {
                        Universal.bothPlayingLocal = false;
                        Universal.youAreAClient = false;
                        Universal.youAreAHost = true;
                        but.applyGamestate();
                    } 
                    
                    else {
                        but.applyGamestate();
                    }
                }
            }
        }

        resetButtons();
    }

    private void resetButtons() {
        for (Buttons but : botoesMenu) {
            but.resetBooleans();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (Buttons but : botoesMenu) {
            but.setCursorOver(false);
        }

        for (Buttons but : botoesMenu) {
            if (isIn(e, but)) {
                but.setCursorOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
    
    /*========== Classe interna Para o Sprite ==========*/
    public enum MultiplayerMenuAnimation implements AnimationType {
        STATIC;

        @Override
        public int getIndex() {
            return 0;
        }

        @Override
        public int getFrameCount() {
            return 1;
        }
    }
}
