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

public class FallBlock extends Obstacles{ //extends Obstacles
    /*------------ ATRIBUTOS ------------*/
    /*Movimento*/
    private boolean shouldFall;
    private float block_speed = 0;
    private float block_gravity = 0.095f * Universal.SCALE;
    private float block_levitate = -0.85f * Universal.SCALE;
    private float block_heightGY; //usado para achar a posição Y em que o bloco tá "no chão"
    private float groundLvl;
    
    /*Animação*/
    private BufferedImage BlockSpriteSheet;
    private Sprite<BlockAnimation> blockSprite;
    private BufferedImage blockShadow;
    private Sprite<ShadowAnimation> shadowSprite;
    private BlockAnimation blockAction = BlockAnimation.FALLING;
    
    /*------------ CONSTRUTOR ------------*/
    public FallBlock(Screen screen, GCanvas gc) {
        super(screen, gc);
        setY(Universal.WALL_SPAWN_Y);
        setX(Universal.OBST_SPAWN_X); //fora da tela na direita
        this.shouldFall = false;
        initSprite();
        initObstHitbox();
        block_heightGY = getHitboxHeight();
        groundLvl = Universal.groundY - block_heightGY + 60; // 
        setIsActive(false);
    }
    
    public void initSprite() {
        SpriteData blockData = SpriteLoader.spriteDataLoader().get("fallblock");
        SpriteData shadowData = SpriteLoader.spriteDataLoader().get("blockshadow");
        try {
            BlockSpriteSheet = ImageIO.read(getClass().getResource(blockData.getPath()));
            blockShadow = ImageIO.read(getClass().getResource(shadowData.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        setWidth(64); //largura em px do FRAME ORIGINAL 
        setHeight(48); //altura em px do FRAME ORIGINAL
        blockSprite = new Sprite<>(BlockSpriteSheet, this.heightO, this.widthO, BlockAnimation.class, 1);
        shadowSprite = new Sprite<>(blockShadow, 32, 64, ShadowAnimation.class, 1);
    }
    
    @Override
    protected void drawObstHitbox(Graphics2D g2d) {
        g2d.setColor(Color.WHITE);
        g2d.drawRect((int) obs_hitbox.x, (int) obs_hitbox.y, (int) obs_hitbox.width, (int) obs_hitbox.height);
    }
    
    @Override
    public void initObstHitbox() {
        this.obs_hitbox = new Rectangle2D.Float(getX() - (Universal.TILES_SIZE), getY(), Universal.BLOCK_HITBOX_WIDTH, Universal.BLOCK_HITBOX_HEIGHT); 
    }
    
    @Override
    public void render(Graphics2D g2d) {
        /*Altero o estado da animação*/
        blockSprite.setAction(blockAction);
        blockSprite.update();
        
        shadowSprite.render(g2d, (int) getX() - 45, (int) Universal.groundY - (Universal.TILES_SIZE / 6) + 25) ;
        blockSprite.render(g2d, (int) getX() - 32, (int) getY() - 21);
        if (Universal.showGrid) {
            drawObstHitbox(g2d);
        }
    }
    
    @Override
    public void update(float deltaTime) {
        //movimentação constante pra esquerda
        if (this.isActive) { // se estiver ativo
            this.setX(this.getX() + Universal.BASE_SPEED * deltaTime); //atualizo a speed
            updateY();
            updateObstHitbox();
        }
        
    }
    //comportamento diferente dos outros obstáculos
    public void updateY(){
        if(shouldFall){
            //se ele não está no chão, a gravidade exponencial o afeta
            blockAction = BlockAnimation.FALLING;
            block_speed -= block_gravity;
            setY(getY() - block_speed);
            
            if(getY() >= groundLvl){ //chegou no chão
                setY(groundLvl);
                block_speed = 0;
                /*
                insiro um timer aqui de nao sei quantos segundos ou milissegundos
                */
                shouldFall = false;
            }
        }
        
        if(!shouldFall){
            block_speed = block_levitate;
            blockAction = BlockAnimation.LEVITATING;
            setY(getY() + block_speed);
            if(getY() <= (Universal.BLOCK_SKY_LEVEL)){
                setY(Universal.BLOCK_SKY_LEVEL);
                block_speed = 0;
                shouldFall = true;
            }
        }
    }
    
    @Override
    public void updateObstHitbox() { //pode tirar e definir no obstacles
        obs_hitbox.x =  getX(); //atualizo a posição horizontal
        obs_hitbox.y =  getY(); //atualizo a posição vertical
    }
    
    public float getHitboxHeight() {
        return (float) obs_hitbox.height;
    }

    public float getHitboxWidth() {
        return (float) obs_hitbox.height;
    }
    
    /*========== Classe interna Para os Sprites ==========*/ 
    public enum BlockAnimation implements AnimationType{
        FALLING(0, 1),
        LEVITATING(1, 1);
        
        public int index;
        public int frameCount;
        
        BlockAnimation(int index, int frameCount){
            this.index = index;
            this.frameCount = frameCount;
        }
        
        @Override
        public int getIndex(){
            return index;
        }
        
        @Override
        public int getFrameCount(){
            return frameCount;
        }
    }
    
    public enum ShadowAnimation implements AnimationType{
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