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
import utilz.Sprite;
import utilz.Universal;

public class Saw extends Obstacles{ //extends Obstacles, que extende objects
    /*------------ ATRIBUTOS ------------*/
    private BufferedImage sawSpriteSheet;
    private Sprite<SawAnimation> sawSprite;
    private SawAnimation sawAction = SawAnimation.STATIC;
    private Rectangle2D.Float hitbox2, hitbox3;
    
    /*------------ CONSTRUTOR ------------*/
    public Saw(Screen screen, GCanvas gc) {
        super(screen, gc);
        setY(Universal.SAW_SPAWN_Y);
        setX(Universal.OBST_SPAWN_X); //fora da tela na direita
        initSprite();
        initObstHitbox();
        setIsActive(false);
    }
    
    public void initSprite() {
        SpriteData sawData = SpriteLoader.spriteDataLoader().get("saw");
        try {
            sawSpriteSheet = ImageIO.read(getClass().getResource(sawData.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        setWidth(sawSpriteSheet.getWidth()/3); //largura em px do FRAME ORIGINAL 
        setHeight(43); //altura em px do FRAME ORIGINAL
        sawSprite = new Sprite<>(sawSpriteSheet, this.heightO, this.widthO, SawAnimation.class, 2);
        
    }
    
    @Override
    protected void drawObstHitbox(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.drawRect((int) obs_hitbox.x, (int) obs_hitbox.y, (int) obs_hitbox.width, (int) obs_hitbox.height);
        g2d.drawRect((int) hitbox2.x, (int) hitbox2.y, (int) hitbox2.width, (int) hitbox2.height);
        g2d.drawRect((int) hitbox3.x, (int) hitbox3.y, (int) hitbox3.width, (int) hitbox3.height);
    }
    
    @Override
    public void initObstHitbox() { //x, y, largura, altura
        this.obs_hitbox = new Rectangle2D.Float(getX(), getY() + 29 * Universal.SCALE, 80 * Universal.SCALE, 14 * Universal.SCALE); //metade do tamanho
        this.hitbox2 = new Rectangle2D.Float(getX() + 8 * Universal.SCALE, getY() + 16 * Universal.SCALE, 64 * Universal.SCALE, 13 * Universal.SCALE);
        this.hitbox3 = new Rectangle2D.Float(getX() + 70 * Universal.SCALE, getY(), 30 * Universal.SCALE, 12 * Universal.SCALE);
    }
    
    @Override 
    public void updateObstHitbox() {
        obs_hitbox.x = getX(); //atualizo a posição horizontal
        obs_hitbox.y = getY() + 29 * Universal.SCALE;
        hitbox2.x = getX() + 8 * Universal.SCALE;
        hitbox2.y = getY() + 16 * Universal.SCALE;
        hitbox3.x = getX() + 75;
        hitbox3.y = getY() + 12;
    }
    
    @Override
    public void render(Graphics2D g2d){
        sawSprite.setAction(sawAction);
        sawSprite.update();
        sawSprite.render(g2d, (int) getX(), (int) getY());
            if(Universal.showGrid){
                drawObstHitbox(g2d);
            }
    }
    
    /*========== Classe interna Para os Sprites ==========*/ 
    public enum SawAnimation implements AnimationType{
        STATIC(0, 3);
        
        private final int index;
        private final int frameCount;

        SawAnimation(int 
        index,
        int frameCount

        
            ){
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

    public Rectangle2D.Float getHitbox2(){
        return hitbox2;
    }
    public Rectangle2D.Float getHitbox3(){
        return hitbox3;
    }
}