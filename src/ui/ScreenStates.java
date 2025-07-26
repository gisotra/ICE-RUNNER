package ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public interface ScreenStates {
    public void update();

    public void render(Graphics2D g2d);

    public void mouseClicked(MouseEvent e);

    public void mousePressed(MouseEvent e);

    public void mouseReleased(MouseEvent e);

    public void mouseMoved(MouseEvent e);

    public void keyPressed(KeyEvent e);

    public void keyReleased(KeyEvent e);

    public boolean isIn(MouseEvent e, Buttons mb);
    // a ser implementado em cada gamestate
    //public boolean isIn(MouseEvent e, MenuButton mb) {
        //return mb.getBounds().contains(e.getX(), e.getY());
    //}
}
