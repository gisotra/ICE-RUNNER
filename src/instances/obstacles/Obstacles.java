package instances.obstacles;

import instances.Objects;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import loop.GCanvas;
import utilz.Screen;
import utilz.Sprite;
import utilz.Universal;

public abstract class Obstacles extends Objects{ //muito similiar a classe Entities, porém direcionada unicamente aos obstáculos
    /*------------ ATRIBUTOS ------------*/
    protected float speed; //ele vai sempre vir pra esquerda
    protected Rectangle2D.Float obs_hitbox; //desenvolver na criação dos objetos wall, saw, bird, etc

    /*------------ CONSTRUTOR ------------*/
    public Obstacles(Screen screen, GCanvas gc) {
        super(screen, gc);
    }

    /*------------ MÉTODOS HERDADOS ------------*/
    protected abstract void drawObstHitbox(Graphics2D g2d); // método para debug
    
    /*transformar esse método em um abstrato, e modificar ele pra cada classe subsequente*/
    protected abstract void initObstHitbox();
    
    public void updateObstHitbox(){ //pode tirar e definir no obstacles
        obs_hitbox.x = getX(); //atualizo a posição horizontal
        obs_hitbox.y = getY();
    }
    
    @Override
    public void update(float deltaTime) {
        if(this.isActive){ 
        this.setX(this.getX() + Universal.BASE_SPEED * deltaTime); //atualizo a speed
        updateObstHitbox();
        }
    }
    
    @Override
    public void render(Graphics2D g2d){
        //vai ser elaborado para cada obstáculo
    }
    
    public Rectangle2D.Float getObstHitbox(){
        return obs_hitbox;
    }
}

