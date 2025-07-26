package instances.entities;

import instances.entities.Player.PlayerAnimation;
import utilz.AudioPlayer;
import utilz.Universal;

public class Movement {
    
    /*Classe utilizada unicamente para implementação dos movimentos verticais*/
    
    Player player;

    /*horizontal*/
    private float speed = 140f*Universal.SCALE;
    private float MAX_SPEED = 120f*Universal.SCALE;
    private float horizontalSpeed;
    private float atrito = 25.0f*Universal.SCALE;

    /*vertical*/
    private boolean isJumping = false;
    private float verticalSpeed = 0f; //Y
    private float gravity = 0.08f * Universal.SCALE;
    private float jumpPower = -5.2f * Universal.SCALE; // Força do meu salto
    private boolean inAir = false;
    private float heightGY; //usado para achar a posição Y em que o player tá "no chão"
    private float groundLvl;
    private boolean jumpButtonReleased = false;

    /*dash*/
    private boolean isDashing = false;
    private boolean canDash = true; //caso dashTimeCounter > 0  && !hasDashed
    private boolean hasDashed = false;
    private float dashSpeed = 1.9f;
    private float dashDuration = .2f;
    private float dashLength = .001f;
    private float dashTimeCounter = 0f;
    private float dashSpamLimiter = .5f;
    private int horizontalDirection; //vai assumir 3 valores possíveis: -1, 1 ou 0 
    private int verticalDirection; //vai assumir 3 valores possíveis: -1, 1 ou 0 
    
    /*spritesheet smoke1, smoke2, smoke3*/

    /*morte*/
    private boolean deathJump = false; //usei isso aqui pra animação de morte 
    private float deathJumpPower = -1.8f * Universal.SCALE;
    private int cont = 0;
    
    public Movement(Player player1){
        this.player = player1;
        heightGY = player1.getHitboxHeight();
        groundLvl = Universal.groundY - heightGY + 40; // 5 Tiles - 1 = 4 tiles
    }
    
    public void updateMovement(float deltaTime){
        
        if(!player.dead){
            deathJump = false;
            
            if(!isDashing){
                gravity = 0.25f * Universal.SCALE;
            
                
                // ================ movimentação VERTICAL ================
                if (player.jump && isGrounded()) {
                    AudioPlayer.playSound("jump");
                    player.playerAction = PlayerAnimation.JUMP;
                    verticalSpeed = jumpPower;
                    isJumping = true;
                }
                
                if(jumpButtonReleased && verticalSpeed < 0){
                    verticalSpeed = 0;
                    player.setY(player.getY() + verticalSpeed);
                }
                
                if (isJumping) { //caso eu esteja pulando, eu continuamente somo a gravidade na airSpeed
                    verticalSpeed += gravity;
                    player.setY(player.getY() + verticalSpeed); //altero o Y do player
                    if (verticalSpeed > 0) { //estou caindo
                        player.playerAction = PlayerAnimation.FALLING;
                    }

                    //cheguei no chão, então preciso resetar o pulo
                    if (player.getY() >= groundLvl) {
                        player.setY(groundLvl);
                        verticalSpeed = 0f;
                        isJumping = false;
                        hasDashed = false;
                        canDash = true;
                        player.playerAction = PlayerAnimation.IDLE;
                    }
                    
                } else if (!isGrounded()){ //cai de uma plataforma ou qualquer evento alternativo
                    verticalSpeed += gravity;
                    player.setY(player.getY() + verticalSpeed);
                    player.playerAction = PlayerAnimation.FALLING;
                }
                
                // ================ movimentação HORIZONTAL ================
                
                if (player.right && !player.dead) {

                    horizontalSpeed = (float) speed * deltaTime;

                } else if (player.left && !player.dead) {

                    horizontalSpeed = (float) -speed * deltaTime;

                } else if (player.dead) {
                    horizontalSpeed = 0;
                } else {

                    //não estou apertando tecla alguma E estou no chão 
                    if (horizontalSpeed > 0) { //freio ele na direita
                        horizontalSpeed -= atrito * deltaTime;
                        if (horizontalSpeed < 0) {
                            horizontalSpeed = 0; //paro
                        }
                    } else if (horizontalSpeed < 0) {
                        horizontalSpeed += atrito * deltaTime;
                        if (horizontalSpeed > 0) {
                            horizontalSpeed = 0; //paro
                        }
                    }
                }
            
                if (horizontalSpeed > MAX_SPEED) {
                    horizontalSpeed = MAX_SPEED;
                }
                if (horizontalSpeed < -MAX_SPEED) {
                    horizontalSpeed = -MAX_SPEED;
                }

                //aplico a mudança no player
                player.setX(player.getX() + horizontalSpeed);
                if (player.getX() < 0) {
                    player.setX(0);
                } else if (player.getX() >= Universal.GAME_WIDTH - (Universal.TILES_SIZE) / 2) {
                    player.setX((Universal.GAME_WIDTH - (Universal.TILES_SIZE) / 2));
                }
                
            } else { //DASH
                dashTimeCounter += deltaTime;
                gravity = 0;
                hasDashed = true;
                canDash = false;
                isDashing = true;
                float dashVelocity = 260f * Universal.SCALE;
                float dx = horizontalDirection * dashVelocity * deltaTime;
                float dy = verticalDirection * dashVelocity * deltaTime;

                player.setX(player.getX() + dx);
                player.setY(player.getY() + dy);
                
                //verifico se ele não perfura o chão
                if (player.getY() >= groundLvl) {
                    player.setY(groundLvl);
                    verticalSpeed = 0f;
                    isJumping = false;
                    hasDashed = false;
                    canDash = true;
                    player.playerAction = PlayerAnimation.IDLE;
                } 

                //impeço ele de atravessar as bordas do cenário
                if (player.getX() < 0) {
                    player.setX(0);
                } else if (player.getX() >= Universal.GAME_WIDTH - (Universal.TILES_SIZE) / 2) {
                    player.setX((Universal.GAME_WIDTH - (Universal.TILES_SIZE) / 2));
                }
                
                if (dashTimeCounter >= dashDuration) {
                    //acaba meu dash
                    isDashing = false;
                    dashTimeCounter = 0;
                    horizontalSpeed = 0;
                    verticalSpeed = 0;
                    
                    if(!isGrounded()){
                        isJumping = true;
                        verticalSpeed = 0;
                    } else {
                        player.setY(groundLvl);
                        verticalSpeed = 0f;
                        isJumping = false;
                        hasDashed = false;
                        canDash = true;
                        player.playerAction = PlayerAnimation.IDLE;
                    }
                }
                return;
            }
        } else {
            //player morreu 
            //faço ele pular e corto o limitador vertical do chão
            //quando ele ultrapassar o tamanho da tela, eu setto o state como gameover 
            if(!deathJump || (!deathJump && isDashing)){
                gravity = 0.08f * Universal.SCALE;
            verticalSpeed = deathJumpPower; //jogo pra cima 
            deathJump = true;
            isJumping = true; //true 
            player.playerAction = PlayerAnimation.DEAD; //muda a animação
            }
            
            if (isJumping) {
                verticalSpeed += gravity;
                player.setY(player.getY() + verticalSpeed);
            }
        }
    }
    
    public void resetMovement() {
        horizontalSpeed = 0;
        verticalSpeed = 0;
        inAir = false;
        isDashing = false;
        canDash = true;
        hasDashed = false;
        dashTimeCounter = 0f;
        deathJump = false;
        cont = 0;
    }

    public boolean isGrounded() {
        if (player.getY() >= groundLvl) {
            return true;
        } else {
            return false;
        }
    }

    
    public int getDirection(){
        if (player.up && player.left) {
            return 5;
        } else if (player.up && player.right) {
            return 6;
        } else if (player.down && player.left) {
            return 7;
        } else if (player.down && player.right) {
            return 8;
        } else if (player.up) {
            return 1;
        } else if (player.down) {
            return 2;
        } else if (player.right) {
            return 3;
        } else if (player.left) {
            return 4;
        }
        //caso nenhuma direção seja passada
        return 0;
    }
    
    public void Dash(){
        //usar para ativar o boolean do dash
        //usar para pegar as direções
        //mas onde chamar esse método? 
        //> updateMovement
        //> update dentro da classe player1 
        dashTimeCounter = 0f; // reset
        int direction = getDirection();
        isDashing = true;
        
        switch(direction){
            case 1:{ // UP
            horizontalDirection = 0;
            verticalDirection = -1;
            }break;
            case 2:{ //DOWN
            horizontalDirection = 0;
            verticalDirection = 1;                
            }break;
            case 3:{ //RIGHT
            horizontalDirection = 1;
            verticalDirection = 0;    
            }break;
            case 4:{ //LEFT
            horizontalDirection = -1;
            verticalDirection = 0;    
            }break;
            case 5:{ //UPPER LEFT
            horizontalDirection = -1;
            verticalDirection = -1;                    
            }break;
            case 6:{ //UPPER RIGHT
            horizontalDirection = 1;
            verticalDirection = -1;                    
            }break;
            case 7:{ //LOWER LEFT
            horizontalDirection = -1;
            verticalDirection = 1;                    
            }break;
            case 8:{ //LOWER RIGHT
            horizontalDirection = 1;
            verticalDirection = 1;                
            }break;
            case 0:{ //DEFAULT PARADO
            horizontalDirection = 1;
            verticalDirection = 0;
            break;
            }
        }
    }

    public boolean isIsJumping() {
        return isJumping;
    }

    public void setIsJumping(boolean isJumping) {
        this.isJumping = isJumping;
    }

    public boolean isCanDash() {
        return canDash;
    }

    public void setCanDash(boolean canDash) {
        this.canDash = canDash;
    }

    public boolean isIsDashing() {
        return isDashing;
    }

    public void setIsDashing(boolean isDashing) {
        this.isDashing = isDashing;
    }

    public boolean isJumpButtonReleased() {
        return jumpButtonReleased;
    }

    public void setJumpButtonReleased(boolean jumpButtonReleased) {
        this.jumpButtonReleased = jumpButtonReleased;
    }
}
