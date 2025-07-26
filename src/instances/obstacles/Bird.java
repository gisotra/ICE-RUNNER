package instances.obstacles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import loop.GCanvas;
import utilz.AnimationType;
import utilz.LinearInterp;
import utilz.Screen;
import utilz.Sprite;
import utilz.SpriteData;
import utilz.SpriteLoader;
import utilz.Universal;

public class Bird extends Obstacles{ //extends Obstacles{
    /*------------ ATRIBUTOS ------------*/
    private Sprite<BirdAnimation> birdSprite;
    private BufferedImage birdSpriteSheet;
    private BirdAnimation birdAction = BirdAnimation.FLYING;
    private float factorSpeed = 1.9f;
    private float time;
    private float verticalspeed = 1.0f;
    private float MaxHeight = 5 * Universal.TILES_SIZE;
    private float MinHeight = 7 * Universal.TILES_SIZE;
    /*------------ CONSTRUTOR ------------*/
    public Bird(Screen screen, GCanvas gc) {
        super(screen, gc);
        setY(Universal.BIRD_SPAWN_Y);
        setX(Universal.OBST_SPAWN_X); //fora da tela na direita
        initSprite();
        initObstHitbox();
        setIsActive(false);
    }
    
    public void initSprite() {
        SpriteData birdData = SpriteLoader.spriteDataLoader().get("bird");
        
        try {
            birdSpriteSheet = ImageIO.read(getClass().getResource(birdData.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //altura e largura do meu passarinho
        setWidth(32); //largura em px do FRAME ORIGINAL 
        setHeight(32); //altura em px do FRAME ORIGINAL
        birdSprite = new Sprite<>(birdSpriteSheet, this.heightO, this.widthO, BirdAnimation.class, 10);
    }
    
    @Override
    protected void drawObstHitbox(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.drawRect((int) obs_hitbox.x, (int) obs_hitbox.y, (int) obs_hitbox.width, (int) obs_hitbox.height);
    }
    
    @Override
    public void initObstHitbox() {
        this.obs_hitbox = new Rectangle2D.Float(getX(), getY(), Universal.BIRD_HITBOX_WIDTH, Universal.BIRD_HITBOX_HEIGHT); //metade do tamanho
    }
    
    @Override
    public void render(Graphics2D g2d) {
        
        birdSprite.setAction(birdAction);
        birdSprite.update();
        birdSprite.render(g2d, (int) getX() - 5, (int) getY() - 16);
        if (Universal.showGrid) {
            drawObstHitbox(g2d);
        }
    }
    
    @Override
    public void update(float deltatime){
        time += deltatime;
        
        float t = (float)((Math.sin(time * Math.PI) + 0.5) / 2.0);
        float linearY = LinearInterp.lerp(MinHeight, MaxHeight, t);
        setY(linearY * 1f);
        
        setX(getX()  + Universal.BASE_SPEED * factorSpeed * deltatime); //desloca para a esquerda
        updateObstHitbox();
    
    }
    
    /*========== Classe interna Para os Sprites ==========*/ 
    public enum BirdAnimation implements AnimationType{
        FLYING(0, 2);
        
        private final int index;
        private final int frameCount;

        BirdAnimation(int index, int frameCount){
            this.index = index;
            this.frameCount = frameCount;
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public int getFrameCount() {
            return frameCount;
        }
        
    }
}
