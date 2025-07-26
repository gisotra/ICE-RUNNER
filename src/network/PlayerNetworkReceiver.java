package network;

import gamestates.Gamestate;
import static gamestates.Gamestate.PLAYING;
import instances.Objects;
import instances.entities.Player;
import instances.manager.Spawner;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import utilz.Screen;
import utilz.Universal;

public class PlayerNetworkReceiver implements Runnable{
    
    private Socket socket;
    private boolean running = false;
    private int dummyIndex; // 1 ou 2 
    private Spawner spawner; 
     
    public PlayerNetworkReceiver(Socket socket, int dummyIndex, Spawner spawner){
        this.socket = socket;
        this.dummyIndex = dummyIndex;
        this.spawner = spawner;
    }
    
    public void stop(){
        running = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run(){
        running = true;
        try{
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            Player dummy = getDummyFromScreen();
            
            if(dummy == null){
                System.out.println("Erro: O dummy eh nulo");
                return;
            }
            
            while(running){
                float x = dis.readFloat();
                float y = dis.readFloat();
                byte animIndex = dis.readByte();
                int obstSpawnIndex = dis.readInt();
                int shouldRetry = dis.readInt();
                
                /*
                dummy.setX(x);
                dummy.setY(y);
                dummy.playerAction = Player.PlayerAnimation.values()[animIndex];*/
                dummy.updateNetworkState(x, y, Player.PlayerAnimation.values()[animIndex]);
                if(Universal.youAreAClient){
                    spawner.hear(obstSpawnIndex);
                }
                if(shouldRetry == 1){
                    Screen.resetCoordenates();
                    Screen.startCoordenates();
                    Gamestate.state = PLAYING;
                }
                shouldRetry = 0;
            }
        } catch(IOException e){
            if(running){
                System.err.println("Erro na recepção de dados:");
                e.printStackTrace();
            }
        }
    }
    
    private Player getDummyFromScreen(){
        for(Objects obj : Screen.objectsOnScreen){
            if(obj instanceof Player){
                Player p = (Player)obj;
                if(p.isDummy() && p.getPlayerIndex() == dummyIndex){
                    return p;
                }
            }
        }
        return null;
    }

}
