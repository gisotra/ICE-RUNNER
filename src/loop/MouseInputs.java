package loop;

import gamestates.Gamestate;
import static gamestates.Gamestate.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class MouseInputs implements MouseListener, MouseMotionListener {
     
    private GCanvas gc;

    public MouseInputs(GCanvas gc) {
        this.gc = gc;
    }
    
    @Override
    public void mouseDragged(MouseEvent e){
    
    }

    @Override
    public void mouseMoved(MouseEvent e){
        switch(Gamestate.state){
            case MENU:{
                gc.getScreen().getMenu().mouseMoved(e);
            }
                break;
            case PLAYING:{
                
            }
                break;
            case GAME_OVER:{
                gc.getScreen().getGameOver().mouseMoved(e);
            }
                break;
            case MULTIPLAYER_MENU: {
                gc.getScreen().getMultMenu().mouseMoved(e);
                break;
            }
            case HOSTING: {
                gc.getScreen().getHosting().mouseMoved(e);
                break;
            }
            case WAITING: {
                gc.getScreen().getConnector().mouseMoved(e);
                break;
            }
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e){
        switch(Gamestate.state){
            case MENU:{
                gc.getScreen().getMenu().mouseClicked(e);            
            }
                break;
            case PLAYING:{
            //
            }
                break;
            case GAME_OVER:{
                gc.getScreen().getGameOver().mouseClicked(e);
            }
                break;            
            case MULTIPLAYER_MENU: {
                gc.getScreen().getMultMenu().mouseClicked(e);
                break;
            }
            
            case HOSTING: {
                gc.getScreen().getHosting().mouseClicked(e);
                break;
            }
            case WAITING: {
                gc.getScreen().getConnector().mouseClicked(e);
                break;
            }
            
        }
    }
    
    
    @Override
    public void mousePressed(MouseEvent e){
        switch (Gamestate.state) {
            case MENU: {
                gc.getScreen().getMenu().mousePressed(e);
            }
            break;
            case PLAYING: {
                //
            }
            break;
            case GAME_OVER: {
                gc.getScreen().getGameOver().mousePressed(e);
            }
            break;
            case MULTIPLAYER_MENU: {
                gc.getScreen().getMultMenu().mousePressed(e);
                break;
            }
            case HOSTING: {
                gc.getScreen().getHosting().mousePressed(e);
                break;
            }
            case WAITING: {
                gc.getScreen().getConnector().mousePressed(e);
                break;
            }
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e){
        switch (Gamestate.state) {
            case MENU: {
                gc.getScreen().getMenu().mouseReleased(e);
            }
            break;
            case PLAYING: {

            }
            break;
            case GAME_OVER: {
                gc.getScreen().getGameOver().mouseReleased(e);
            }
            break;
            case MULTIPLAYER_MENU: {
                gc.getScreen().getMultMenu().mouseReleased(e);
            break;
            }
            case HOSTING: {
                gc.getScreen().getHosting().mouseReleased(e);
            break;
            }
            case WAITING: {
                gc.getScreen().getConnector().mouseReleased(e);
                break;
            }
        }
    }
    
    @Override
    public void mouseEntered(MouseEvent e){
        //TODO
    }
    
    @Override
    public void mouseExited(MouseEvent e){
        //TODO
    }
    
    
}
