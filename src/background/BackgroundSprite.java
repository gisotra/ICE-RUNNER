package background;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import utilz.Universal;

public class BackgroundSprite {
    {/*
    com essa classe, eu vou destrinchar os meu cenários em tiles de 64 pixels cada
    */
    
    /*
    como vai ser a estrutura:
    > classe abstrata Environment | feita
    > subclasses das camadas: Chão, arvores, montanhas, etc. | em produção 
    Cada subclasse vai ter em sua composição um atributo BackgroundSprite, que 
    vai ser iniciado no seu construtor;
    > O Background sprite vai ter em sua composição um List<> do tipo Tiles, 
    e vai dividir a BufferedImage das subclasses em Tiles menores, através
    do método SliceImage(), que retorna um sprite próprio
    > A classe Tiles vai ter um método update (deltaTime, speed)
    > A classe backgroundSprite vai ser um método update com os mesmos parâmetros.
    > As subclasses terão o método update (deltaTime) herdado de Objects, e em seu
    desenvolvimento usarão um atributo próprio "speed", que será chamado no parâmetro 
    do update do seu background image, que por cascata será passado pro método update
    dos Tiles 
    
    resultado:
    Vou dividir essas camadas de cenário gigantescos em tiles de tamanho fixo, renderizando
    um por um e os movendo da direita pra esquerda. Como é um movimento meramente horizontal, 
    não é necessária uma malha bidimensional, um vetor unidimensional possui o mesmo funcionamento
    de maneira mais leve e de fácil execução 
    
    anotações: deixar pra inicializar imagem, sprites e tiles SOMENTE 1 VEZ no construtor, e nunca
    no desenvolver do loop do jogo 
    */}
    private BufferedImage spriteOriginal; 
    private BufferedImage spriteEscalonado;
    private List<Tiles> tiles;
    private float x, y;
    private int comprimentoOriginal;
    private int alturaOriginal;
    private int compriPerTile;
    private int alturaEscalonada;
    private int comprimentoEscalonado;

    public BackgroundSprite(BufferedImage original, float x, float y, int comprimentoOriginal, int alturaOriginal, float escala){
        this.tiles = new ArrayList<>();
        this.spriteOriginal = original;
        this.x = x;
        this.y = y;
        this.comprimentoOriginal = comprimentoOriginal;
        this.alturaOriginal = alturaOriginal;
        this.comprimentoEscalonado = (int) (comprimentoOriginal * escala);
        this.alturaEscalonada = (int) (alturaOriginal * escala);
        this.spriteEscalonado = initScaleImage(this.comprimentoEscalonado, this.alturaEscalonada);
        initTiles();
    }

    public BufferedImage initScaleImage(int comprimentoEscala, int alturaEscala){
        BufferedImage spriteAuxScaled = new BufferedImage(comprimentoEscala, alturaEscala, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = spriteAuxScaled.createGraphics();
        g2d.drawImage(this.spriteOriginal, 0, 0, comprimentoEscala, alturaEscala, null);
        //g2d.drawImage(this.spriteOriginal, 0, 0, comprimentoEscala, alturaEscala, (int)x, (int)y, comprimentoEscala + (int)x, alturaEscala + (int)y, null);
        g2d.dispose();
        return spriteAuxScaled;
    }
    
    public Tiles sliceImage(BufferedImage imageSpace, int x, int y, int sliceComprimento, int sliceAltura){
        int larguraExtra = (x + sliceComprimento < imageSpace.getWidth()) ? 1 : 0;
        BufferedImage newSlicedTile = new BufferedImage(sliceComprimento + larguraExtra, sliceAltura, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = newSlicedTile.createGraphics();
        g2d.drawImage(imageSpace, 0, 0, sliceComprimento + larguraExtra, sliceAltura, x, y, sliceComprimento + x + larguraExtra, sliceAltura + y, null);
        g2d.dispose();
        Tiles t = new Tiles(newSlicedTile, x, y, sliceAltura);
        return t;
    }
    
    
    public void initTiles(){
        for(int i = 0; i < Universal.numHorizontalTiles; i++){
            Tiles t = (sliceImage(this.spriteEscalonado,
                    i * Universal.spriteEnviroWidthSLICEDSCALED, 
                        0, 
                            Universal.spriteEnviroWidthSLICEDSCALED, 
                                this.alturaEscalonada));
            t.setX(i * Universal.spriteEnviroWidthSLICEDSCALED);
            t.setY(this.y);
            tiles.add(t);
        }
    }

    public void update(float deltaTime, float speed){
        for(Tiles t : tiles){
            if (t.getX() > -2*Universal.GAME_WIDTH && t.getX() < 2 * Universal.GAME_WIDTH) {
                t.update(deltaTime, speed);
            }
            
        }
        if(tiles.get(3).getX() <= -Universal.spriteEnviroWidthSLICEDSCALED){
            resetSpriteCoordenates();
        }
        
    }
    
    public void render(Graphics2D g2d){
        for(Tiles t : tiles){
            if (t.getX() > -Universal.TILES_SIZE * 4 && t.getX() < Universal.GAME_WIDTH) {
                t.render(g2d);
            }
        }
    }
    
    public void resetSpriteCoordenates(){
        for(int i = 0; i < Universal.numHorizontalTiles; i++){
            tiles.get(i).setX(i * Universal.spriteEnviroWidthSLICEDSCALED);
        }
    }
}

