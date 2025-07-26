package instances.entities;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
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

public class Player extends Entities{
    
    //https://www.youtube.com/watch?v=rTVoyWu8r6g
    /*------------ ATRIBUTOS ------------*/
    private int playerIndex;
    private boolean isDummy = false;
    private Movement movement;
    private Collider collider;
    private BufferedImage playerSpriteSheet;
    private BufferedImage shadow;
    private BufferedImage floormark;
    
    /*Controle de Dash*/
    private long lastDash = 0;
    private long dashCooldown = 250; //evita spam
    
    /*Flags booleanas de Movimento*/
    public boolean right = false;
    public boolean left = false;
    public boolean up = false;
    public boolean down = false;
    public boolean dead = false;
    public boolean dash = false;
    public boolean jump = false;
    
    /*Sprites*/
    private Sprite<PlayerAnimation> playerSprite;
    private Sprite<ShadowAnimation> shadowSprite;
    private Sprite<MarkAnimation> markSprite;
    private Sprite<ChargeAnimation> chargeSprite;
    private Sprite<SwordAnimation> swordSprite;
    public PlayerAnimation playerAction = PlayerAnimation.IDLE;
    public ChargeAnimation chargeAction = ChargeAnimation.STATIC;
    private ScarfRope scarf1, scarf2;
    private BufferedImage scarfSegV1;
    private BufferedImage scarfSegV2;
    
    /*Power Ups Icons*/
    public static boolean isPowered = false; //os dois players ficam com power up ao mesmo tempo
    private boolean sword = false;
    private boolean marioCap = false;
    private boolean shielded = false;
    
    private boolean attacking = false;
    
    /*Power Ups sprites*/
    private BufferedImage mariocapimage;
    private BufferedImage mariocapimageSCALED;
    private BufferedImage chargeimage;
    private BufferedImage swordimage;
    private SpriteData capData;
    private SpriteData playerData;
    private SpriteData scarfV1Data;
    private SpriteData scarfV2Data;
    private SpriteData chargeData;
    private SpriteData shadowData;
    private SpriteData markData;
    private SpriteData swordData;

    //pra fazer o ghost do dash do personagem, eu teria que pegar o frame atual e só repintar ele na posição registrada
    
    
    
    public Player(Screen screen, GCanvas gc, int playerCode, boolean isDummy){
        super(screen, gc);
        this.playerIndex = playerCode;
        setPlayerIndex(playerCode);
        setIsDummy(isDummy);
        movement = new Movement(this);
        collider = new Collider(this);
        initSprite();
        setX(120 * playerCode);
        setY(360);
        movement.setIsJumping(true); //para ele cair logo de primeira
        scarf1 = new ScarfRope(this, 1.2f * Universal.SCALE, scarfSegV1, 7, 23, 3, 9);
        scarf2 = new ScarfRope(this, 0.8f * Universal.SCALE, scarfSegV2, 3, 17, 2f, 12);
        setIsActive(true);
    }     
   
    public synchronized void updateNetworkState(float x, float y, PlayerAnimation anim) {
        this.x = x;
        this.y = y;
        this.playerAction = anim;
    }
    /*Inicialização de Sprites via XML*/
    public void initSprite(){
        if(playerIndex == 1){
            playerData = SpriteLoader.spriteDataLoader().get("player1");
            scarfV1Data = SpriteLoader.spriteDataLoader().get("scarf1.1");
            scarfV2Data = SpriteLoader.spriteDataLoader().get("scarf1.2");
            capData = SpriteLoader.spriteDataLoader().get("mario");
            swordData = SpriteLoader.spriteDataLoader().get("sword1");
        } else if (playerIndex == 2){
            playerData = SpriteLoader.spriteDataLoader().get("player2");    
            scarfV1Data = SpriteLoader.spriteDataLoader().get("scarf2.1");
            scarfV2Data = SpriteLoader.spriteDataLoader().get("scarf2.2");        
            capData = SpriteLoader.spriteDataLoader().get("luigi");        
            swordData = SpriteLoader.spriteDataLoader().get("sword2");
        }
            /*em comum*/
            chargeData = SpriteLoader.spriteDataLoader().get("charge");
            shadowData = SpriteLoader.spriteDataLoader().get("shadow");
            markData = SpriteLoader.spriteDataLoader().get("mark");

        try {
            playerSpriteSheet = ImageIO.read(getClass().getResource(playerData.getPath()));
            shadow = ImageIO.read(getClass().getResource(shadowData.getPath()));
            floormark = ImageIO.read(getClass().getResource(markData.getPath()));
            scarfSegV1 = ImageIO.read(getClass().getResource(scarfV1Data.getPath()));
            scarfSegV2 = ImageIO.read(getClass().getResource(scarfV2Data.getPath()));
            mariocapimage = ImageIO.read(getClass().getResource(capData.getPath()));
            chargeimage = ImageIO.read(getClass().getResource(chargeData.getPath()));
            swordimage = ImageIO.read(getClass().getResource(swordData.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //inicio as propriedades do meu sprite player
        setWidth(32);
        setHeight(32);
        /*Inicio as Sprites*/
        playerSprite = new Sprite<>(playerSpriteSheet, this.heightO, this.widthO, PlayerAnimation.class, 15);
        shadowSprite = new Sprite<>(shadow, 32, 32, ShadowAnimation.class, 1);
        markSprite = new Sprite<>(floormark, 32, 32, MarkAnimation.class, 1);
        chargeSprite = new Sprite<>(chargeimage, 13, 13, ChargeAnimation.class, 72);
        swordSprite = new Sprite<>(swordimage, 52, 27, SwordAnimation.class, 1);
        /*scale de imagens estáticas*/ 
        
        /*mario cap*/
        mariocapimageSCALED = new BufferedImage(mariocapimage.getWidth()* (int)Universal.SCALE, mariocapimage.getHeight() * (int)Universal.SCALE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = mariocapimageSCALED.createGraphics();
        g2d.drawImage(this.mariocapimage, 0, 0, mariocapimage.getWidth()* (int)Universal.SCALE, mariocapimage.getHeight() * (int)Universal.SCALE, null);
        g2d.dispose();
    }
    
    @Override
    public synchronized void update(float deltaTime){
        long currentTime = System.currentTimeMillis();
        if(dash 
                && movement.isCanDash() 
                    && !movement.isIsDashing() 
                        && currentTime - lastDash >= dashCooldown){
            movement.Dash();
            dash = false;
            lastDash = currentTime;
        }
        movement.updateMovement(deltaTime);
        collider.updateCollisionArea();
        
        if(collider.verifyNearby()){
            collider.verifyCollission();
        }
        if(!dead){    
            collider.verifyPowerUpCollision();
        }
        scarf2.update(deltaTime);
        scarf1.update(deltaTime);
        updateHitbox();
    }

    @Override
    public synchronized void render(Graphics2D g2d){
        Composite original = g2d.getComposite();

        playerSprite.setAction(playerAction);
        playerSprite.update(); //altero o state da minha animacao
        
        if (marioCap) { //mario
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); /*Aplico transparencia no pincel*/
            scarf2.render(g2d);
            playerSprite.render(g2d, (int) getX() - 12, (int) getY());
            g2d.drawImage(mariocapimageSCALED, (int)getX() - 25, (int)getY() - 39, null);
            scarf1.render(g2d);
            g2d.setComposite(original);
        } else if (sword) { //sword
            swordSprite.render(g2d, (int) getX() + 20, (int) getY() - 100);
            scarf2.render(g2d);
            playerSprite.render(g2d, (int) getX() - 12, (int) getY());
            scarf1.render(g2d);
            
        } else {
            scarf2.render(g2d);
            playerSprite.render(g2d, (int) getX() - 12, (int) getY());
            scarf1.render(g2d);
        }
        
        if (Player.isPowered) {
            chargeSprite.setAction(chargeAction);
            chargeSprite.update();
            chargeSprite.render(g2d, (int) getX() - 40, (int) getY() - 35);
        }
        
        
        
        if(Universal.showGrid){
            drawHitbox(g2d);
            collider.drawCollisionArea(g2d);
            markSprite.render(g2d, (int) getX() - 21, (int) Universal.groundY - (Universal.TILES_SIZE / 6) + 40);
        }
    }
    
    public void renderShadow(Graphics2D g2d){
        shadowSprite.render(g2d, (int) getX() - 21, (int) Universal.groundY - (Universal.TILES_SIZE / 6) + 40);
    }
    
    /*ChargeBar + Duração do powerUp*/
    public void startPowerUpCounter(){
        //chargeBar é uma sprite de 15 frames que passam com speed de duração 1,25 segundos cada
        Player.isPowered = true;
        this.chargeAction = ChargeAnimation.STATIC;
        
        Thread t = new Thread(() -> {
            try {
                Thread.sleep(15000); // Dorme exatamente 15 segundos (duração do power up)
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

            
            this.sword = false;
            this.marioCap = false;
            this.shielded = false;
            isPowered = false;
            this.chargeAction = null;
            chargeSprite.resetAction();
            return;
        });

        t.start();
        
    }

    /*------------ GETTERS AND SETTERS ------------*/
    public synchronized float getX() {
        return x;
    }
    public synchronized float getY() {
        return y;
    }
    public synchronized void setX(float x) {
        this.x = x;
    }
    public synchronized void setY(float y) {
        this.y = y;
    }
    public synchronized boolean isDead() {
        return dead;
    }
    public synchronized void setDead(boolean dead) {
        this.dead = dead;
    }
    public synchronized int getHeight() {
        return heightO;
    }
    public synchronized int getPlayerIndex(){
        return playerIndex;
    }
    public synchronized void setPlayerIndex(int playerIndex){
        this.playerIndex = playerIndex;
    }
    public synchronized Movement getMovement(){
        return movement;
    }
    public boolean isMarioCap() {
        return marioCap;
    }
    public void setMarioCap(boolean marioCap) {
        this.marioCap = marioCap;
    }
    public boolean isSword() {
        return sword;
    }
    public void setSword(boolean sword) {
        this.sword = sword;
    }
    public boolean isShielded() {
        return shielded;
    }

    public void setShielded(boolean shielded) {
        this.shielded = shielded;
    }
    
    public boolean isDummy(){
        return isDummy;
    }
    public void setIsDummy(boolean isDummy){
        this.isDummy = isDummy;
    }
    
    
    /*========== Classes internas Para os Sprites ==========*/ 
    /*Player*/
    public enum PlayerAnimation implements AnimationType{
        IDLE(0, 2),
        RUNNING(0, 2),
        JUMP(1, 3),
        FALLING(2, 1),
        DEAD(3, 1);
        //DASH(4, 2);

        private final int index;
        private final int frameCount;
    
        PlayerAnimation(int index, int frameCount){
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
    
    /*Sombra*/
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
    
    /*FloorMark*/
    public enum MarkAnimation implements AnimationType{
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
    
    /*ChargeBar*/
    public enum ChargeAnimation implements AnimationType{
        STATIC(0, 13); //13 frames
        
        private final int index;
        private final int frameCount;

        ChargeAnimation(int index, int frameCount){
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
    
    /*sword*/
    public enum SwordAnimation implements AnimationType {
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
