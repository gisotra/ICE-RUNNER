package background;

import instances.Objects;
import java.awt.Graphics2D;
import loop.GCanvas;
import utilz.Screen;

public abstract class Environment extends Objects{
    
    public Environment(Screen screen, GCanvas gc) {
        super(screen, gc);
    }
    @Override
    public abstract void update(float deltaTime);

    @Override
    public abstract void render(Graphics2D g2d);
}
