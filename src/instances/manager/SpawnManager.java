package instances.manager;
import instances.Objects;
import instances.entities.PowerUps;
import instances.obstacles.Bird;
import instances.obstacles.FallBlock;
import instances.obstacles.Saw;
import instances.obstacles.Wall;
import java.util.Random;
import utilz.Screen;
import utilz.Universal;

public class SpawnManager {
    {/*
    lógica que será usada para criação do player2
    > Classe Objects (x, y, width, height)
    > Classe Obstacles + Wall, Saw, Bird
    > Classe SpawnManager vai ser uma classe auxiliar q vai abrigar a queue estática da Screen para adicionar os obstáculos
    > Classe Player2, vai ter o spawnManager em sua composição e em seus métodos vai implementar mecanica de cooldown 
    
    Cada obstáculo vai ter um método diferente, porque cada um vai ter um cooldown próprio
    */}
    public void spawnWall(){
        for(Objects obj : Screen.objectsOnScreen) {
            if(!obj.getIsActive() && obj instanceof Wall ){
                obj.setX(Universal.OBST_SPAWN_X);
                obj.setY(Universal.WALL_SPAWN_Y);
                obj.setIsActive(true);
                return; //acontece 1 única vez
            }
        }
    }
    public void spawnBird(){
        for(Objects obj : Screen.objectsOnScreen) {
            if(!obj.getIsActive() && obj instanceof Bird ){
                obj.setX(Universal.OBST_SPAWN_X);
                obj.setY(Universal.BIRD_SPAWN_Y);
                obj.setIsActive(true);
                return; //acontece 1 única vez
            }
        }
    }
    public void spawnSaw(){
        for(Objects obj : Screen.objectsOnScreen) {
            if(!obj.getIsActive() && obj instanceof Saw ){
                obj.setX(Universal.OBST_SPAWN_X);
                obj.setY(Universal.SAW_SPAWN_Y);
                obj.setIsActive(true);
                return; //acontece 1 única vez
            }
        }
    }

    public void spawnBlock(){
        for(Objects obj : Screen.objectsOnScreen) {
            if(!obj.getIsActive() && obj instanceof FallBlock ){
                obj.setX(Universal.OBST_SPAWN_X);
                obj.setY(Universal.BLOCK_SKY_LEVEL);
                obj.setIsActive(true);
                return; //acontece 1 única vez
            }
        }
    }
    
    public void spawnPowerUp(){
        Random r = new Random();
        int pIndex;
        pIndex = r.nextInt(3); //de 0 a 2
        
        if(!Screen.powerUpArray.get(pIndex).isActive()){
            Screen.powerUpArray.get(pIndex).setIsActive(true);
            Screen.powerUpArray.get(pIndex).setX(Universal.OBST_SPAWN_X);
            Screen.powerUpArray.get(pIndex).setY(Universal.BIRD_SPAWN_Y - 32);
        }
    }
    /*
    Lógica usada:
    Se eu encontrar na minha Queue um objeto do tipo correspondente do obstáculo
    que eu quero e o mesmo for INATIVO, eu pego sua coordenada X, coloco do lado
    direito da tela, setto seu Y corretamente e o defino como Ativo.
    
    */
    
    
    
}
