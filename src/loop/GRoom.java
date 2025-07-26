package loop;

import gamestates.Gamestate;
import static gamestates.Gamestate.*;
import utilz.Universal;

public class GRoom implements Runnable {

    /*------------ ATRIBUTOS ------------*/

    GCanvas gc;
    private final double tempoPorFrame = 1_000_000_000.0 / Universal.FPS_SET;
    private double ultimoTempo = System.nanoTime();
    private long proximoFrame = System.nanoTime() + (long) tempoPorFrame;
    private double threadSleep;
    private long threadSleepMS;
    private int threadSleepNano;

    private long timer = System.currentTimeMillis();
    private int frames = 0;
    private int updates = 0;

    /*------------ CONSTRUTOR ------------*/
    public GRoom(GCanvas gc) {
        this.gc = gc;
    }

    /*------------ MÉTODO RUN DA THREAD ------------*/
    @Override
    public void run() {
        final float fixedStep = 1.0f / 60.0f;
        float accumulator = 0.0f;

        while (true) {

            double agora = System.nanoTime();
            double frameTime = (agora - ultimoTempo) / 1_000_000_000.0;
            ultimoTempo = agora;

            // Evita explosão de updates em travadas longas
            if (frameTime > 0.25) {
                frameTime = 0.25;
            }

            accumulator += (float) frameTime;

            // Substeps: atualiza várias vezes se necessário
            while (accumulator >= fixedStep) {
                update(fixedStep);
                updates++;
                accumulator -= fixedStep;

                if (Gamestate.state == PLAYING) {
                    Universal.SCORE += (int) (100 * fixedStep);

                    if (Universal.SCORE - Universal.lastSpeedUpScore >= 1000) {
                        Universal.lastSpeedUpScore = (Universal.SCORE / 1000) * 1000;

                        if (Universal.SCORE > 2000) {
                            Universal.increaseAllSpeed();
                            if (Universal.SCORE >= 4000 && Universal.globalCooldown > 100) {
                                Universal.globalCooldown -= 150;
                                if (Universal.globalCooldown < 500) {
                                    Universal.globalCooldown = 500;
                                }
                            }
                        }
                    }
                }
            }

            // Renderiza uma vez por frame
            render();
            frames++;

            // Controla o tempo de sono com base na lógica de FPS
            threadSleep = (proximoFrame - System.nanoTime()) / 1_000_000.0;
            if (threadSleep < 0) {
                threadSleep = 0;
            }

            threadSleepMS = (long) threadSleep;
            threadSleepNano = (int) ((threadSleep - threadSleepMS) * 1_000_000);

            try {
                Thread.sleep(threadSleepMS, threadSleepNano);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            proximoFrame += (long) tempoPorFrame;

            // FPS / UPS debug
            if (System.currentTimeMillis() - timer >= 1000) {
                //System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
                timer += 1000;
            }
        }
    }

    /*------------ MÉTODO UPDATE ------------*/
    public void update(float dT) {
        gc.update(dT);
    }

    /*------------ MÉTODO RENDER ------------*/
    public void render() {
        gc.render();
    }

    public void sleepEngine() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        proximoFrame = System.nanoTime() + (long) tempoPorFrame;
        ultimoTempo = System.nanoTime();
    }
}