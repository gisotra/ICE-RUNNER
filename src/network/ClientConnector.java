package network;

import gamestates.Gamestate;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javax.imageio.ImageIO;
import loop.GWindow;
import ui.Buttons;
import ui.MultiplayerMenu;
import ui.ScreenStates;
import utilz.AnimationType;
import utilz.Screen;
import utilz.Sprite;
import utilz.SpriteData;
import utilz.SpriteLoader;
import utilz.Universal;

public class ClientConnector implements ScreenStates{
    /*Parte gráfica*/
    private BufferedImage multMenuFundo;
    private Sprite<MultiplayerMenu.MultiplayerMenuAnimation> multMenuSprite;
    //adicionar o sprite pro loading 
    private Buttons[] botoesMenu = new Buttons[2];
    private BufferedImage botaoEnviar;
    private BufferedImage botaoExit;
    public static boolean waitingConnection = false;

    /*Sockets*/
    private int porta = 8888;
    private Socket clientSocket;
    private String IPdoServidorhost;

    public ClientConnector() {
        initSpriteMenu();
        botoesMenu[0] = new Buttons(20, 20, 48, 48, botaoExit, Gamestate.MULTIPLAYER_MENU);
        botoesMenu[1] = new Buttons(Universal.GAME_WIDTH / 2 - 60, Universal.GAME_HEIGHT / 2 +50, 40, 22, botaoEnviar, null);
   }

    public void connectToServer() {
        // captura o texto do campo de input
        IPdoServidorhost = GWindow.getInputIPField().getText().trim();
        GWindow.getInputIPField().setText("");

        if (IPdoServidorhost.isEmpty()) {
            System.out.println("Campo de IP está vazio.");
            return;
        }

        new Thread(() -> {
            try {
                waitingConnection = true;
                clientSocket = new Socket(IPdoServidorhost, porta);

                // conectado com sucesso
                System.out.println("Conectado ao servidor com sucesso.");
                PlayerNetworkReceiver receiver = new PlayerNetworkReceiver(clientSocket, 1, Screen.spawner); // client recebe dummy1
                new Thread(receiver).start();

                PlayerNetworkSender sender = new PlayerNetworkSender(clientSocket, 2); // client envia player2
                new Thread(sender).start();
                
                waitingConnection = false;
                Universal.resetBooleans();
                Universal.youAreAClient = true;
                
                // oculta o campo e troca o estado do jogo
                GWindow.getInputIPField().setVisible(false);
                Screen.resetCoordenates();
                Screen.startCoordenates();
                Gamestate.state = Gamestate.PLAYING;
            } catch (IOException e) {
                System.out.println("Erro ao conectar com o servidor: " + e.getMessage());
                waitingConnection = false;
            }
        }).start();
    }
    
    public void initSpriteMenu() {
        SpriteData menuData = SpriteLoader.spriteDataLoader().get("fundoMenu");
        SpriteData exitData = SpriteLoader.spriteDataLoader().get("exitbutton");
        SpriteData submitData = SpriteLoader.spriteDataLoader().get("submit");

        try {

            multMenuFundo = ImageIO.read(getClass().getResource(menuData.getPath()));
            botaoExit = ImageIO.read(getClass().getResource(exitData.getPath()));
            botaoEnviar = ImageIO.read(getClass().getResource(submitData.getPath()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //inicio as propriedades do meu sprite player
        this.multMenuSprite = new Sprite<>(multMenuFundo, 288, 512, MultiplayerMenu.MultiplayerMenuAnimation.class, 1);
    }

    /*-------------- MÉTODOS HERDADOS --------------*/
    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g2D) {
        multMenuSprite.render(g2D, 0, 0);
        for (Buttons but : botoesMenu) {
            but.render(g2D);
        }
        if (waitingConnection) {
            g2D.setColor(Color.WHITE);
            g2D.drawString("ESPERANDO", Universal.GAME_WIDTH / 2 - 75, 600);
            g2D.drawString("CONEXAO", Universal.GAME_WIDTH / 2 + 15, 600);
            GWindow.getInputIPField().setVisible(true);
        }
    }

    @Override
    public boolean isIn(MouseEvent e, Buttons mb) {
        return mb.getDimensoes().contains(e.getX(), e.getY());
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (Buttons but : botoesMenu) {
            if (isIn(e, but)) {
                but.setCursorPressed(true);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (Buttons but : botoesMenu) {
            if (isIn(e, but)) {
                if (but.isCursorPressed()) {
                    if (but.getState() == null) {
                        //aplico o que foi escrito pra minha variável String local no meu IPServidorHost
                        connectToServer();
                        continue;
                    } 
                    else if (but.getState() == Gamestate.MULTIPLAYER_MENU){ //voltar
                        waitingConnection = false;
                        GWindow.getInputIPField().setVisible(false);
                        but.applyGamestate();
                    }
                    
                    else {
                        Universal.bothPlayingLocal = true;
                        but.applyGamestate();
                    }
                }
            }
        }

        resetButtons();
    }

    private void resetButtons() {
        for (Buttons but : botoesMenu) {
            but.resetBooleans();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (Buttons but : botoesMenu) {
            but.setCursorOver(false);
        }

        for (Buttons but : botoesMenu) {
            if (isIn(e, but)) {
                but.setCursorOver(true);
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    /*========== Classe interna Para o Sprite ==========*/
    public enum HostingAnimation implements AnimationType {
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

    public boolean isWaitingConnection() {
        return waitingConnection;
    }

    public void setWaitingConnection(boolean waitingConnection) {
        this.waitingConnection = waitingConnection;
    }

}
