package instances.entities;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ScarfRope {
    //vai receber como parâmetro: o numero de segmentos a serem adicionados, o espaçamento de cada um
    //na hora de renderizar, considerar a ancora como o centro do frame 
    //vai possuir um arrayList de tamanho condizente com o passado no construtor
    private Player player; //a partir disso vai saber se é o P1 ou P2 atraves do playerIndex
    private ScarfSegment[] scarf;
    private float distanceX;
    private float distanceY;
    private float offsetX;
    private float offsetY;
    private float scale;
    private int length;
    
    public ScarfRope(Player player, float distanceBetween, BufferedImage scarfsprite, float offsetX, float offsetY, float scale, int length) {
        this.player = player;
        this.length = length;
        this.scarf = new ScarfSegment[length];
        this.distanceX = distanceBetween;
        this.distanceY = distanceBetween;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.scale = scale;
        initScarf(scarfsprite);
    }
    
    public void initScarf(BufferedImage scarfsprite){
        scarf[0] = new ScarfSegment(player.getX() + offsetX, player.getY() + offsetY, this.player, this.scale, scarfsprite);
        for (int i = 1; i < scarf.length ; i++) {
            scarf[i] = new ScarfSegment(scarf[i - 1].getX(), scarf[i - 1].getY() + distanceY, this.player, this.scale, scarfsprite);
        }
       
    }
    
    public void update(float deltaTime) {
        // âncora segue o jogador
        scarf[0].setX(player.getX() + offsetX);
        scarf[0].setY(player.getY() + offsetY);

        // atualiza todos os outros segmentos
        //for (int i = 1; i < scarf.length; i++) {
        //    scarf[i].update(deltaTime);
        //}

        // aplica múltiplas iterações de constraint
        //int constraintIterations = 4;
        //for (int j = 0; j < constraintIterations; j++) {
            for (int i = 1; i < scarf.length; i++) {
                scarf[i].applyConstraint(scarf[i - 1], distanceX, deltaTime);
            }
        //}
    }
    
    public void render(Graphics2D g2d){
        for(int i = 0; i < scarf.length; i++){
            scarf[i].render(g2d);
        }
    }
    
    
}
