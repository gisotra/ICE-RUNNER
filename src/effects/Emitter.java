package effects;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import utilz.Universal;

public class Emitter {
    public static List<SnowParticle> snow;
    private Random r = new Random(); //usado para respawnar os flocos horizontalmente de forma aleatoria no topo da tela

    public Emitter(int particles) {
        this.snow = new ArrayList<>();
        createSnow(particles);
    }
    
    private void createSnow(int numberOfParticles){
        for(int i = 0; i < numberOfParticles; i++){
            snow.add(new SnowParticle());
        }
    }
    
    public void update(float deltaTime){
        for(SnowParticle snowP : snow){
            if(snowP.getX() <= 0 || snowP.getY() > Universal.GAME_HEIGHT){
                snowP.setY(-30); //pooling 
                snowP.setX(r.nextFloat() * Universal.GAME_WIDTH + 2 * Universal.TILES_SIZE);
            }
            snowP.update(deltaTime);
        }
    }
    
    public void render(Graphics2D g2d){
        for(SnowParticle snowP : snow){
            if(snowP.getY() > 0 && snowP.getY() < Universal.GAME_HEIGHT && snowP.getX() > 0 && snowP.getX() < Universal.GAME_WIDTH){
                snowP.render(g2d);
            }
        }
    }
    
    public void resetSnow(){
        for (SnowParticle snowP : snow) {
            snowP.setY(r.nextFloat() * (-150f) - 70f);
            snowP.setX(r.nextFloat() * Universal.GAME_WIDTH + 2 * Universal.TILES_SIZE);
        }
    }
}
