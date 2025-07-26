package utilz;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class AudioPlayer {
    private static HashMap<String, Clip> soundMap = new HashMap<>();
    
    public static void loadSound(String name, String path, boolean loop) {
        try {
            URL soundURL = AudioPlayer.class.getResource(path);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            soundMap.put(name, clip);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Erro ao carregar o som: " + path);
            e.printStackTrace();
        }
    }

    public static void playSound(String name) {
        Clip clip = soundMap.get(name);
        if (clip != null) {
            if (clip.isRunning()) clip.stop(); // reinicia
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public static void stopSound(String name) {
        Clip clip = soundMap.get(name);
        if (clip != null) {
            clip.stop();
        }
    }
    
    public static void initSounds() {
        //loadSound("bg_music", "/sounds/bg_music.wav", true);
        loadSound("jump", "/sounds/jump.wav", false);
        loadSound("land", "/sounds/land.wav", false);
        loadSound("start", "/sounds/start.wav", false);
        loadSound("collision", "/sounds/collision.wav", false);
        loadSound("milestone", "/sounds/milestone.wav", false);
    }
}
