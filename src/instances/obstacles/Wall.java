package instances.obstacles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import loop.GCanvas;
import utilz.AnimationType;
import utilz.Screen;
import utilz.Sprite;
import utilz.SpriteData;
import utilz.SpriteLoader;
import utilz.Universal;

public class Wall extends Obstacles{ //extends Obstacles
    /*------------ ATRIBUTOS ------------*/
    private BufferedImage wallSpriteSheet;
    private Sprite<WallAnimation> wallSprite;
    
    /*------------ CONSTRUTOR ------------*/
    public Wall(Screen screen, GCanvas gc) {
        super(screen, gc);
        setY(Universal.WALL_SPAWN_Y);
        setX(Universal.OBST_SPAWN_X); //fora da tela na direita
        initSprite();
        initObstHitbox();
        setIsActive(false);
    }
    
    public void initSprite() {
        SpriteData wallData = SpriteLoader.spriteDataLoader().get("wall");
        try {
            wallSpriteSheet = ImageIO.read(getClass().getResource(wallData.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        setWidth(64); //largura em px do FRAME ORIGINAL 
        setHeight(96); //altura em px do FRAME ORIGINAL
        wallSprite = new Sprite<>(wallSpriteSheet, this.heightO, this.widthO, WallAnimation.class, 1);
    }
    
    @Override
    protected void drawObstHitbox(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.drawRect((int) obs_hitbox.x, (int) obs_hitbox.y, (int) obs_hitbox.width, (int) obs_hitbox.height);
    }
    
    @Override
    public void initObstHitbox() {
        this.obs_hitbox = new Rectangle2D.Float(getX(), getY(), Universal.WALL_HITBOX_WIDTH, Universal.WALL_HITBOX_HEIGHT); //metade do tamanho
    }
    
    @Override
    public void render(Graphics2D g2d) {
        wallSprite.render(g2d, (int) getX() - 60, (int) getY() - 120);
        if (Universal.showGrid) {
            drawObstHitbox(g2d);
        }
    }
    
    /*========== Classe interna Para os Sprites ==========*/ 
    public enum WallAnimation implements AnimationType{
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