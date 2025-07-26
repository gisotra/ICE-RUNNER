package effects;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;
import utilz.SpriteData;
import utilz.SpriteLoader;
import utilz.Universal;

public class SnowParticle {
    private Random r = new Random();
    private float x;
    private float y;
    private float verticalSpeed;
    private float horizontalSpeed;
    private BufferedImage snowSprite;
    private BufferedImage spriteEscalonado;
    private int altura = 7;
    private int largura = 7;
    private float rotation = 0;
    private float rotationSpeed;
    
    public SnowParticle() { //nao preciso passar nada no construtor pq vai ser tudo aleatorio
        this.x = r.nextFloat() * Universal.GAME_WIDTH + 2 * Universal.TILES_SIZE;
        this.y = r.nextFloat() * (-150f) - 70f;
        this.horizontalSpeed = -190f;
        this.verticalSpeed = r.nextFloat() * 190.0f + 165f;

        // Define uma velocidade de rotação alta, entre 3 e 10 rad/s (pode ajustar)
        float minRotationSpeed = 3f;
        float maxRotationSpeed = 10f;
        float randomSpeed = minRotationSpeed + r.nextFloat() * (maxRotationSpeed - minRotationSpeed);
        float direction = r.nextBoolean() ? 1f : -1f;

        this.rotationSpeed = randomSpeed * direction;
        initSprite();
    }
    
    public void initSprite(){
        int randomParticle = r.nextInt(5) + 1;

        SpriteData snowData = SpriteLoader.spriteDataLoader().get("SnowParticle" + String.valueOf(randomParticle));
        
        if (snowData == null) {
            throw new RuntimeException("Erro: snowData retornou null. Verifique se todos os 'snowParticleX' estão carregados corretamente no SpriteLoader.");
        }
        
        try {
            snowSprite = ImageIO.read(getClass().getResource(snowData.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        int escalaAleatoria = r.nextInt(2) + 1;
        int larguraRandom = largura * escalaAleatoria;
        int alturaRandom = altura * escalaAleatoria;
        
        /*Inicialização do sprite escalonado*/
        spriteEscalonado = new BufferedImage(larguraRandom, alturaRandom, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = spriteEscalonado.createGraphics();
        g2d.drawImage(this.snowSprite, 0, 0, larguraRandom, alturaRandom, null);
        g2d.dispose();
    }
    
    /*métodos*/
    public void update(float deltaTime){
        this.rotation += rotationSpeed * deltaTime;
        setX(getX() + horizontalSpeed * deltaTime );
        setY(getY() + verticalSpeed * deltaTime);
        //cada um vai ter uma velocidade aleatoria
        
    }
    
    public void render(Graphics2D g2d){
        int drawX = (int) x; //converter para int 
        int drawY = (int) y;
        int centerX = drawX + spriteEscalonado.getWidth() / 2; //aplico que a rotação será feita no centro da minha sprite
        int centerY = drawY + spriteEscalonado.getHeight() / 2;

        // Salva o estado atual da transformação
        g2d.translate(centerX, centerY);
        g2d.rotate(rotation); // Aplica a rotação
        g2d.drawImage(spriteEscalonado, -spriteEscalonado.getWidth() / 2, -spriteEscalonado.getHeight() / 2, null);
        g2d.rotate(-rotation); // Desfaz a rotação
        g2d.translate(-centerX, -centerY); // Volta ao estado original
    }
    
    /*--------------- Getters e Setters ---------------*/
    
    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getSpeed() {
        return verticalSpeed;
    }

    public void setSpeed(float speed) {
        this.verticalSpeed = speed;
    }

    public Random getR() {
        return r;
    }

    public void setR(Random r) {
        this.r = r;
    }

    
    
}
