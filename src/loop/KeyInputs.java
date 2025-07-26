package loop;

import gamestates.Gamestate;
import static gamestates.Gamestate.*;
import instances.Objects;
import instances.entities.Player;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import utilz.Screen;
import utilz.Universal;

public class KeyInputs implements KeyListener {

    private GCanvas gameCanvas;
    private List<Objects> listOfObjects;

    public KeyInputs(GCanvas gameCanvas, List<Objects> listOfObjects) {
        this.gameCanvas = gameCanvas;
        this.listOfObjects = listOfObjects;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Ignorar por enquanto
    }

    @Override
    public void keyReleased(KeyEvent e) {
        Player p1 = getPlayerById(1);
        Player p2 = getPlayerById(2);

        switch (e.getKeyCode()) {
            /*player 1*/
            case KeyEvent.VK_W:
                p1.up = false;
                break;
            case KeyEvent.VK_A:
                p1.left = false;
                break;
            case KeyEvent.VK_S:
                p1.down = false;
                break;
            case KeyEvent.VK_D:
                p1.right = false;
                break;
            case KeyEvent.VK_SPACE: //pulo
                p1.jump = false;
                p1.getMovement().setJumpButtonReleased(true);
                break;
            case KeyEvent.VK_SHIFT:
                p1.dash = false;
                break;

            /*player 2*/
            case KeyEvent.VK_UP:
                p2.up = false;
                break;
            case KeyEvent.VK_LEFT:
                p2.left = false;
                break;
            case KeyEvent.VK_DOWN:
                p2.down = false;
                break;
            case KeyEvent.VK_RIGHT:
                p2.right = false;
                break;
            case KeyEvent.VK_NUMPAD1: { //pulo
                p2.jump = false;
                p2.getMovement().setJumpButtonReleased(true);
                break;
            }
            case KeyEvent.VK_NUMPAD2: {
                p2.dash = false;
                break;
            }

            /*Geral*/
            case KeyEvent.VK_P:
                //em breve um pause button
                break;
            /*DEBUG*/
            case KeyEvent.VK_U:
                //Toggle Grid 
                Universal.showGrid = !Universal.showGrid;
                break; 
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Player p1 = getPlayerById(1);
        Player p2 = getPlayerById(2);
        
        switch (e.getKeyCode()) {
            /*player 1*/
            case KeyEvent.VK_W:
                p1.up = true;
                break;
            case KeyEvent.VK_A:
                p1.left = true;
                break;
            case KeyEvent.VK_S:
                p1.down = true;
                break;
            case KeyEvent.VK_D:
                p1.right = true;
                break;
            case KeyEvent.VK_SPACE:
                p1.jump = true;
                p1.getMovement().setJumpButtonReleased(false);
                break;
            case KeyEvent.VK_SHIFT:
                p1.dash = true;
                break;

            /*player 2*/
            case KeyEvent.VK_UP:
                p2.up = true;
                break;
            case KeyEvent.VK_LEFT:
                p2.left = true;
                break;
            case KeyEvent.VK_DOWN:
                p2.down = true;
                break;
            case KeyEvent.VK_RIGHT:
                p2.right = true;
                break;
            case KeyEvent.VK_NUMPAD1: {
                p2.jump = true;
                p2.getMovement().setJumpButtonReleased(false);
                break;
            }
            case KeyEvent.VK_NUMPAD2: {
                p2.dash = true;
                break;
            }
            /*DEBUG*/
            case KeyEvent.VK_U:
                //Toggle Grid 
                break;
            case KeyEvent.VK_ESCAPE:
                System.exit(0);
                break;

        }
    }
    
    public Player getPlayerById(int Index){
        for(Objects obj : listOfObjects){
            if(obj instanceof Player){
                if(((Player)obj).getPlayerIndex() == Index){
                Player p = (Player)obj;
                return p;                    
                }
            }
        }
        return null;
    }
}
