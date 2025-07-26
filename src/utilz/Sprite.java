package utilz;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Sprite<T extends Enum<T> & AnimationType> {  //restringe o tipo do meu genérico T 
    private T currentState; 
    private BufferedImage sprite;
    private BufferedImage[][] spritesEscalonados;
    private int frameAtual;
    private int contadorDeFrames;
    private int trocaDeFrames; //velocidade da animação, basicamente
    private int alturaFrame, larguraFrame;
    private int alturaFrameEscalonado, larguraFrameEscalonado;
    
    public Sprite(BufferedImage sprite, int alturaFrame, int larguraFrame, Class<T> enumClass, int trocaDeFrames){
        this.alturaFrame = alturaFrame;
        this.larguraFrame = larguraFrame;
        this.trocaDeFrames = trocaDeFrames;
        
        alturaFrameEscalonado = alturaFrame * (int)Universal.SCALE;
        larguraFrameEscalonado = larguraFrame * (int)Universal.SCALE;

        T[] actions = enumClass.getEnumConstants();
        spritesEscalonados = new BufferedImage[actions.length][];
        
        /*preencher a minha malha*/
        for(T action : actions){
            int row = action.getIndex();
            int frameCount = action.getFrameCount();
            spritesEscalonados[row] = new BufferedImage[frameCount];
             
            for(int i = 0; i < frameCount; i++){
                /*Recorto o frame original*/
                BufferedImage frame = sprite.getSubimage(
                    i * larguraFrame,
                    row * alturaFrame,
                    larguraFrame,
                    alturaFrame 
                );
             
            /*agora eu vou ESCALONAR esse frame*/
            BufferedImage frameEscalonado = new BufferedImage(larguraFrameEscalonado, alturaFrameEscalonado, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = frameEscalonado.createGraphics();
            g2d.drawImage(frame, 0, 0, larguraFrameEscalonado, alturaFrameEscalonado, null);
            g2d.dispose();
            
            spritesEscalonados[row][i] = frameEscalonado;
            }
        }
        // Inicializa com a primeira ação por padrão
        currentState = actions[0];
        frameAtual = 0;
        contadorDeFrames = 0;
    }
    
    
    public void setAction(T novaAcao){
        if(novaAcao != currentState){
            currentState = novaAcao;
            frameAtual = 0;
            contadorDeFrames = 0;
        }
    }
    
    public void update(){
        contadorDeFrames++;
        if(contadorDeFrames >= trocaDeFrames){
            frameAtual = (frameAtual + 1) % currentState.getFrameCount();
            contadorDeFrames = 0;
        }
    }
    
    public void resetAction(){
        frameAtual = 0;
        contadorDeFrames = 0;
    }
    
    public void render(Graphics2D g2d, int x, int y){
        g2d.drawImage(spritesEscalonados[currentState.getIndex()][frameAtual],
                x, y,
                null
        );
        //g2d.drawImage(spritesEscalonados[currentState.getIndex()][frameAtual], transparency, x, y);
    }
    
    public BufferedImage getFrameAtual() {
        return spritesEscalonados[currentState.getIndex()][frameAtual];
    }

    public int getAlturaFrameEscalonado() {
        return alturaFrameEscalonado;
    }

    public void setAlturaFrameEscalonado(int alturaFrameEscalonado) {
        this.alturaFrameEscalonado = alturaFrameEscalonado;
    }

    public int getLarguraFrameEscalonado() {
        return larguraFrameEscalonado;
    }

    public void setLarguraFrameEscalonado(int larguraFrameEscalonado) {
        this.larguraFrameEscalonado = larguraFrameEscalonado;
    }
    
    
    
}
