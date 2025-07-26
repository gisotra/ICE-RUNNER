package instances.entities;

import instances.Objects;
import instances.obstacles.Obstacles;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import instances.obstacles.Saw;
import utilz.Screen;
import utilz.Universal;

public class Collider {
    Player player;
    private Rectangle2D.Float collisionArea;
    private float collAreaWidth;
    private float collAreaHeight;
    private float areaXOffset;
    private float areaYOffset;
    
    public Collider(Player player1) {
        this.player = player1;
        this.collAreaWidth = player1.getHitboxWidth() * 1.2f;
        this.collAreaHeight = player1.getHitboxHeight() * 1.4f;
        this.areaXOffset = Universal.TILES_SIZE * 0.23f;
        this.areaYOffset = Universal.TILES_SIZE * 0.15f;
        collisionArea = new Rectangle2D.Float(player1.x, player1.y, collAreaWidth, collAreaHeight);
    }
    
    public void drawCollisionArea(Graphics g2d){
        /*------------ MÉTODOS ------------*/
        //para testar a hitbox
        g2d.setColor(Color.ORANGE);
        g2d.drawRect((int) collisionArea.x, (int) collisionArea.y, (int) collisionArea.width, (int) collisionArea.height);
    }
    
    public void initCollisionArea(float x, float y, float width, float height) {
        collisionArea = new Rectangle2D.Float(player.x - this.areaXOffset, player.y - this.areaYOffset, collAreaWidth, collAreaHeight);
    }
    
    public void updateCollisionArea() {
        collisionArea.x = player.x - this.areaXOffset; //atualizo a posição horizontal
        collisionArea.y = player.y - this.areaYOffset; //atualizo a posição vertical
    }
        public boolean verifyNearby(){
        /*
        Se algum obstáculo estiver dentro da minha área de colisão (que NÃO É
        minha hitbox, é só um "campo de observação", eu passo a verificar a colisão
        com o obstáculo, poupando memória
        */
            for(Objects obj : Screen.objectsOnScreen){
                if(obj instanceof Obstacles){
                    Obstacles obstacle = (Obstacles) obj;

                    if (obstacle instanceof Saw) {
                        Saw s = (Saw) obstacle;
                        if (collisionArea.intersects(s.getObstHitbox()) ||
                                collisionArea.intersects(s.getHitbox2()) ||
                                collisionArea.intersects(s.getHitbox3())) {
                            return true;
                        }
                        continue;
                    }

                    if(collisionArea.intersects(obstacle.getObstHitbox())){
                        return true;
                    }
                }
            }
            return false;
        }

    public void verifyCollission(){
        for (Objects obj : Screen.objectsOnScreen) {
            if (!(obj instanceof Obstacles)) continue;
            Obstacles obstacle = (Obstacles) obj;
            if (obstacle instanceof Saw) {
                Saw s = (Saw) obstacle;
                if (player.getHitbox().intersects(s.getObstHitbox()) ||
                        player.getHitbox().intersects(s.getHitbox2()) ||
                        player.getHitbox().intersects(s.getHitbox3())) {
                    if (!player.isMarioCap()) {
                        player.dead = true;
                    }
                    continue;
                }
            }

            // Trata os outros obstáculos normalmente
            if (player.getHitbox().intersects(obstacle.getObstHitbox())) {
                if (!player.isMarioCap()) {
                    player.dead = true;
                }
            }
        }
    }
    public void verifyPowerUpCollision(){
        for (PowerUps p : Screen.powerUpArray) {
            PowerUps poder = p;
            if (player.getHitbox().intersects(p.getCollisionArea())) {
                if(p.getIndex() == 1){ //sword
                    player.setSword(true);
                    player.startPowerUpCounter();
                    p.setIsActive(false);
                    return;
                } else if(p.getIndex() == 2){ //shield
                    player.setShielded(true);
                    player.startPowerUpCounter();
                    p.setIsActive(false);
                    return;
                } else if(p.getIndex() == 3){ //marioCap
                    player.setMarioCap(true);
                    player.startPowerUpCounter();
                    p.setIsActive(false);
                    return;
                }
                /*Criar um método que inicia uma thread própria que dura 15 segundos, quando chega no fim, remove o 
                isPowered e setta os booleans de power up falsos*/
                
            }
        }
    }
    
    
}
