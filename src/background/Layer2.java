package background;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import loop.GCanvas;
import utilz.Screen;
import utilz.SpriteData;
import utilz.SpriteLoader;
import utilz.Universal;

public class Layer2 extends Environment {
    private float speedOffset = 0.3f;
    private BufferedImage groundSpriteSheet;
    private BackgroundSprite bgSprite;
    
    public Layer2(Screen screen, GCanvas gc) {
        super(screen, gc);
        this.y = Universal.layer2YOffset - 300;
        this.x = 0;
        initSprite();
        setIsActive(true);
        bgSprite = new BackgroundSprite(groundSpriteSheet, x, y, widthO, heightO, Universal.SCALE);
    }

    public void initSprite() {
        SpriteData groundData = SpriteLoader.spriteDataLoader().get("layer2");
        try {
            groundSpriteSheet = ImageIO.read(getClass().getResource(groundData.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setWidth(Universal.spriteEnviroWidth); //largura em px do FRAME ORIGINAL 28 tiles
        setHeight(153); //altura em px do FRAME ORIGINAL 8 tiles 
    }
    
    @Override
    public void update(float deltaTime) {
        bgSprite.update(deltaTime, speedOffset);
    }

    @Override
    public void render(Graphics2D g2d) {
        bgSprite.render(g2d);
    }
}
    

