package instances.manager;

import instances.entities.Player;
import java.util.Random;
import utilz.Universal;

public class Spawner{
    /*
    O que vai ser o player 2:
    Ele vai ter cooldowns próprios para cada obstáculo.
    */
    private SpawnManager spm = new SpawnManager();
    private Random r = new Random();
    private int spawnpoint;
    private long lastPowerUpSpawn = 0;
    private long nextPowerUpSpawn = 10000; //35000 = 35s
    
    // Cooldown global entre spawns (anti-spam)
    private long lastGlobalSpawn = 0;
    
    /*Método que será chamado somente quando o player estier jogando alone OU for um host*/
    public void play(){ //(currentTime - lastSpawn) >= SpawnWall )
        if(Universal.SCORE < 150){
            return;
        }
        long currentTime = System.currentTimeMillis();
        
        if(currentTime - lastGlobalSpawn < Universal.globalCooldown){ //evita spam 
            return;
        }
        spawnpoint = r.nextInt(9);
        switch(spawnpoint){
            case 1:
                Universal.wall = true;
                break;
            case 2:
                Universal.saw = true;
                break;
            case 3:
                Universal.bird = true;
                break;
            case 4:
                Universal.block = true;
                break;
            default:
                //spm.spawnPowerUp();
                break;
        }
        
        if(Universal.wall){
            spm.spawnWall();
            
            lastGlobalSpawn = currentTime;
            Universal.wall = false;
            Universal.obstSpawnIndex = 1;
            return;
        }
        
        if(Universal.saw){
            spm.spawnSaw();
            
            lastGlobalSpawn = currentTime;
            Universal.saw = false;
            Universal.obstSpawnIndex = 2;
            return;
        }
        if(Universal.bird){
            spm.spawnBird();
            
            lastGlobalSpawn = currentTime;
            Universal.bird = false;
            Universal.obstSpawnIndex = 3;
            return;
        }
        
        if(Universal.block){
            spm.spawnBlock();
            
            lastGlobalSpawn = currentTime;
            Universal.block = false;
            Universal.obstSpawnIndex = 4;
            return;
        }

        /*if(!Player.isPowered){
            if(currentTime - lastPowerUpSpawn >= nextPowerUpSpawn){
                spm.spawnPowerUp();   
                lastPowerUpSpawn = currentTime;
            }
        }*/
    }
    
    /*Método que será chamado quando você for um cliente*/
    public void hear(int obstIndex){
        switch(obstIndex){
            case 1: { //muro
              spm.spawnWall();   //eu spawno o obstáculo de acordo
              Universal.wall = false;
            }break;
            case 2: { //saw
              spm.spawnSaw();
              Universal.saw = false;
            }break;
            case 3: { //bird
              spm.spawnBird();
              Universal.bird = false;
            }break;
            case 4: { //block
              spm.spawnBlock();
              Universal.saw = false;
            }break;
            case 0:{
                break;
            }
        }
    }

}
