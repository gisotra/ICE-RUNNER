package instances.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import utilz.LinearInterp;
import utilz.SpriteData;
import utilz.SpriteLoader;
import utilz.Universal;

public class ScarfSegment {
     //vai ter X, Y, as sprites, e os valores prévios de X e Y
    private Player player;
    private BufferedImage scarfSegSprite;
    private BufferedImage scarfSegSpriteSCALED;
    private int largura = 7;
    private int altura = 7;
    private float x;
    private float y;
    private float lastXPosition;
    private float lastYPosition;
    private float gravity = 0.5f;
    private float wind = -2.0f;
    private float time;
    private float scale;
    

    public ScarfSegment(float x, float y, Player player, float scale, BufferedImage scarfSegSprite) {
        //verlet integration
        this.x = x;
        this.y = y;
        lastXPosition = x;
        lastYPosition = y;
        this.scale = scale;
        
        this.player = player;
        this.scarfSegSprite = scarfSegSprite;
        initSprite();
    }
    
    public void initSprite() {
        int larguraEscalonada = largura * (int)scale;
        int alturaEscalonada = altura * (int)scale;

        scarfSegSpriteSCALED = new BufferedImage(larguraEscalonada, alturaEscalonada, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scarfSegSpriteSCALED.createGraphics();
        g2d.drawImage(this.scarfSegSprite, 0, 0, larguraEscalonada, alturaEscalonada, null);
        g2d.dispose();
    }
    
    public void render(Graphics2D g2d){
        int drawX = (int) x; //converter para int 
        int drawY = (int) y;
        //implementar centerX :/
        int centerX = drawX + scarfSegSpriteSCALED.getWidth() / 2; //aplico que a ancora será no centro da minha sprite
        int centerY = drawY + scarfSegSpriteSCALED.getHeight() / 2;
        g2d.drawImage(scarfSegSpriteSCALED, centerX-scarfSegSpriteSCALED.getWidth() / 2, centerY-scarfSegSpriteSCALED.getHeight() / 2, null);
    }
    
    public void update(float deltaTime) {
        float currentX = x;
        float currentY = y;

        // calcula a velocidade
        float velocityX = (x - lastXPosition);
        float velocityY = (y - lastYPosition); 

        // atualiza posição com base na nova velocidade
        x += velocityX;
        y += velocityY;

        // armazena a posição anterior
        lastXPosition = currentX;
        lastYPosition = currentY;
    }
    
    public void applyConstraint(ScarfSegment segmentoAncora, float distanciaIDEALEntreSegmentos, float deltatime){ //limita os meus segmentos de forma que eles pareçam conectados entre si
        time += deltatime;
        float t = (float) ((Math.sin(time * Math.PI) + 0.5) / 2.0);
        float linearY = LinearInterp.lerp(-0.7f, 0.7f, t);
        gravity = linearY + 0.5f;
        
        float deltaX = x - segmentoAncora.getX(); 
        float deltaY = y - segmentoAncora.getY();
        
        //aplico teorema de pitágoras para pegar a distância entre esses dois pontos
        // D = √(x² + y²)
        float dist = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        
        float diferenca = distanciaIDEALEntreSegmentos - dist;
        
        float percent = diferenca / dist / 2;
        //calcula o tanto que a gente precisa mudar a posição dos dois pontos para se aproximar da distancia ideal
        //se estiver muito perto, a gente vai afastar metade da distancia cada um 
        //se estiver muito longe, a gente vai juntar e por aí vai
        
        float OffSetX = deltaX * percent;
        float OffSetY = deltaY * percent;
        
        setX(getX() + OffSetX + wind);
        setY(getY() + OffSetY + gravity);
        //segmentoAncora.setX(getX() - OffSetX); //fazia o cachecol estourar
        //segmentoAncora.setY(getY() - OffSetY); //fazia o cachecol estourar
    }
    
    //lerp ->  

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

    public float getLastXPosition() {
        return lastXPosition;
    }

    public void setLastXPosition(int lastXPosition) {
        this.lastXPosition = lastXPosition;
    }

    public float getLastYPosition() {
        return lastYPosition;
    }

    public void setLastYPosition(int lastYPosition) {
        this.lastYPosition = lastYPosition;
    }
    
    
    
    
    
    
}
