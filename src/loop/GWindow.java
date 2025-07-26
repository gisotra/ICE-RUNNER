package loop;

import javax.swing.*;

import utilz.Universal;

public class GWindow {

    private JFrame janela;
    private GCanvas gc;
    private JProgressBar bar;
    public static JTextField inputIPField;

    public GWindow() {
        gc = new GCanvas();
        janela = new JFrame();
        janela.setUndecorated(true);
        janela.setLayout(null);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setSize(Universal.GAME_WIDTH, Universal.GAME_HEIGHT);
        janela.setResizable(true);
        gc.setBounds(0, 0, Universal.GAME_WIDTH, Universal.GAME_HEIGHT);
        inputIPField = new JTextField();
        inputIPField.setBounds(Universal.GAME_WIDTH / 2 - 150, Universal.GAME_HEIGHT / 2 - 15, 300, 30);
        inputIPField.setVisible(false);
        janela.add(inputIPField);
        janela.add(gc);

        janela.setLocationRelativeTo(null); // Centraliza a janela
        janela.setVisible(true); // Mostra tudo

        gc.initCanvas();
        gc.initGame();
    }
    
    public static JTextField getInputIPField(){
        return inputIPField;
    }
}