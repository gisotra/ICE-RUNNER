package loop;

import gamestates.Gamestate;
import static gamestates.Gamestate.*;
import utilz.Universal;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import utilz.AnimationType;
import utilz.AudioPlayer;
import utilz.Screen;
import utilz.Sprite;
import utilz.SpriteData;
import utilz.SpriteLoader;

public class GCanvas extends Canvas {

    /*------------ ATRIBUTOS ------------*/
    private GRoom room;
    private Thread loop; 
    private Font chickenFont;
    private Font fontInGame;
    private Screen screen = new Screen(this);
    private Point mousePoint;
    private Cursor cursor;
    private BufferedImage cursorMouse;
    private Sprite<CursorAnimation> spriteMouse;
    
    /*------------ CONSTRUTOR ------------*/
    public GCanvas() {
        setPreferredSize(new Dimension(Universal.GAME_WIDTH, Universal.GAME_HEIGHT));
        setFocusable(true);
        requestFocus();
        initMouseSprites();
        addKeyListener(new KeyInputs(this, Screen.objectsOnScreen)); 
        addMouseListener(new MouseInputs(this));
        
        AudioPlayer.initSounds();
        
        try {
            InputStream is = getClass().getResourceAsStream("/assets/font/Chicken Font.ttf");
            chickenFont = Font.createFont(Font.TRUETYPE_FONT, is);
            fontInGame = chickenFont.deriveFont(Font.PLAIN, 25f); //aplico as mudanças de tamanho
        } catch (FontFormatException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        
        }

    /*------------ O LENDÁRIO MÉTODO RENDER ------------*/
    public void render() {
        //instancia o que é necessário para desenhar 
        BufferStrategy bs = getBufferStrategy(); 
        if (bs == null) {
            return; // ainda não foi criado
        }
        Graphics2D g2D = (Graphics2D) bs.getDrawGraphics();
        try {
            // Limpa o fundo para evitar artefatos de frames anteriores
            g2D.setColor(new Color(80, 48, 179));
            g2D.fillRect(0, 0, getWidth(), getHeight());
            if (Universal.showGrid) {
                drawGrid(g2D);
            }
            screen.renderAll(g2D);
            mousePoint = getMousePosition();
            if(mousePoint != null){
                spriteMouse.render(g2D, (int) mousePoint.getX(), (int)mousePoint.getY());            
            }
            if(Gamestate.state == PLAYING){
            g2D.setFont(fontInGame);
            g2D.setColor(Color.BLACK);
            g2D.drawString("SCORE:   " + String.valueOf(Universal.SCORE), Universal.GAME_WIDTH/2 - 120, 40);
            }
            if(Gamestate.state == MULTIPLAYER_MENU){
            g2D.setFont(fontInGame);
            g2D.setColor(Color.WHITE);
            g2D.drawString("Criar Servidor", 3*Universal.TILES_SIZE - 65, 5*Universal.TILES_SIZE + 25);
            g2D.drawString("Jogar como Cliente", 7*Universal.TILES_SIZE - 65, 5*Universal.TILES_SIZE + 25);
            g2D.drawString("Local", 12*Universal.TILES_SIZE - 25, 5*Universal.TILES_SIZE + 25);
            }
            if(Gamestate.state == GAME_OVER){
                g2D.setFont(fontInGame);
                g2D.setColor(Color.WHITE);    
                g2D.drawString("SCORE:", 6 * Universal.TILES_SIZE + 40, 6 * Universal.TILES_SIZE + 45);
                g2D.drawString(String.valueOf(Universal.SCORE), 8 * Universal.TILES_SIZE + 15, 6 * Universal.TILES_SIZE + 45);
            }
            if(Gamestate.state == HOSTING){
                g2D.setFont(fontInGame);
                g2D.setColor(Color.WHITE);
                g2D.drawString("SEU", Universal.GAME_WIDTH / 2 - 75, 200);
                g2D.drawString("IP:", Universal.GAME_WIDTH / 2 + 10, 200);
                g2D.drawString(Universal.IPString, Universal.GAME_WIDTH / 2 - 140, 250);
                g2D.drawString("CRIAR", Universal.GAME_WIDTH / 2 - 50, 500);
                g2D.drawString("SERVIDOR", Universal.GAME_WIDTH / 2 -90, 540);
            }
            if(Gamestate.state == WAITING){
                g2D.setFont(fontInGame);
                g2D.setColor(Color.WHITE);
                g2D.drawString("DIGITE", Universal.GAME_WIDTH / 2 - 120, 400);
                g2D.drawString("O", Universal.GAME_WIDTH / 2 + 20, 400);
                g2D.drawString("IP:", Universal.GAME_WIDTH / 2 + 60, 400);

            }
            
            
        } finally {
            // Garante que o objeto Graphics será liberado mesmo que dê erro
            
            g2D.dispose();
        }

        // Exibe o buffer completo na tela
        bs.show();

        // Sincroniza a atualização gráfica com o hardware para reduzir tearing
        Toolkit.getDefaultToolkit().sync();
    }
    
    /*------------ MÉTODO QUE RETORNA A SALA PARA USO DO KEYINPUTS ------------*/
    public GRoom getGame() {
        return room;
    }
    
    public Screen getScreen() {
        return screen;
    }
    
    public void update(float dT) {
        screen.updateAll(dT);
    }
    
    public void initCanvas() {
        createBufferStrategy(3); // você pode usar 2 ou 3 buffers
    }
    
    public void drawGrid(Graphics2D g2D) {
        g2D.setColor(Color.LIGHT_GRAY); // Cor preta para o grid
        for (int x = 0; x < Universal.GAME_WIDTH; x += Universal.TILES_SIZE) {
            for (int y = 0; y < Universal.GAME_HEIGHT; y += Universal.TILES_SIZE) {
                // Desenha o contorno de cada tile
                g2D.drawRect(x, y, Universal.TILES_SIZE, Universal.TILES_SIZE);
            }
        }
        
    }
    
    //mudar o sprite do meu cursor
    public void initMouseSprites(){
        
        SpriteData mouseData = SpriteLoader.spriteDataLoader().get("cursor");
        try {
            cursorMouse = ImageIO.read(getClass().getResource(mouseData.getPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //inicio as propriedades do meu sprite player
        this.spriteMouse = new Sprite<>(cursorMouse, 32, 32, CursorAnimation.class, 1);
        
        cursor = Toolkit.getDefaultToolkit().createCustomCursor(
        new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), "blank cursor");
        setCursor(cursor);

    }
    
    
    public void initGame(){
        Universal.BASE_SPEED = -1.8f * Universal.SCALE;
        this.room = new GRoom(this);
        this.loop = new Thread(room);
        this.loop.start();
    }
    
    public void sleepGame(){
        this.room.sleepEngine();
    }
    
    /*========== Classe interna Para os Sprites ==========*/ 
    public enum CursorAnimation implements AnimationType{
        
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