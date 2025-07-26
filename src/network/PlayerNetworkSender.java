package network;

import instances.Objects;
import instances.entities.Player;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import utilz.Screen;
import utilz.Universal;

public class PlayerNetworkSender implements Runnable{
    private Socket socket;
    private boolean running = true;
    private int realPlayerIndex;
    public static volatile int shouldRetry = 0; //10.105.65.75
    
    public PlayerNetworkSender(Socket socket, int realIndex){
        this.socket = socket;
        this.realPlayerIndex = realIndex;
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
        try{
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            Player player = getRealPlayerFromScreen();
        
            if(player == null){
                System.out.println("Player real eh nulo");
                
                return;
            }
            
            while(running){
                dos.writeFloat(player.getX());
                dos.writeFloat(player.getY());
                dos.writeByte((byte) player.playerAction.ordinal());
                dos.writeInt(Universal.obstSpawnIndex);
                Universal.obstSpawnIndex = 0;
                dos.writeInt(shouldRetry);
                dos.flush();
                
                shouldRetry = 0;
                
                try{
                    Thread.sleep(16);
                } catch(InterruptedException ex){
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } catch(IOException e){
            if(running){
                System.err.println("Erro no envio de dados: ");
                e.printStackTrace();
            }
        }
    }
    
    private Player getRealPlayerFromScreen(){
        for(Objects obj : Screen.objectsOnScreen){
            if(obj instanceof Player){
                Player p = (Player)obj;
                if(!p.isDummy() && p.getPlayerIndex() == realPlayerIndex){
                    return p;
                }
            }
        }
        return null;
    }
    
    
    
}
