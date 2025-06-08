package github.PanheadGG.SuperMarioBros.core;

import github.PanheadGG.SuperMarioBros.core.scene.GameEngine;
import github.PanheadGG.SuperMarioBros.core.scene.GameMenu;
import github.PanheadGG.SuperMarioBros.core.scene.GameOverUI;
import github.PanheadGG.SuperMarioBros.manager.UIManager;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {
    private static UIManager ui;
    private static GameMenu menu;
    private static GameEngine engine;
    private static Thread engineThread;
    private static GameOverUI gameOverUI;
    private static int cameraWidth = 15;
    private static int cameraHeight = 14;
    private static int pixelPerUnit = 80;
    private static int fps = 165;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ui = new UIManager(pixelPerUnit * cameraWidth, pixelPerUnit * cameraHeight, fps);
            new Thread(ui).start();



            menu = new GameMenu(pixelPerUnit);
            gameOverUI = new GameOverUI(pixelPerUnit);
            runMenu();
        });


    }

    public static void runMenu() {
        System.gc();
        ui.setDraw(menu);
        ui.setBackground(menu.getBackgroundColor());
        ui.setKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    runGame();
                }
            }
        });
    }

    public static void runGame() {
        engine = new GameEngine();
        engine.setUI(ui);
        engine.loadMap();
        ui.setKeyListener(engine.getKeyListener());
        ui.setDraw(engine);
        ui.setBackground(engine.getBackgroundColor());

        engineThread = new Thread(engine);
        engineThread.start();

        engine.setGameOverCallback((reason, score) -> {
            gameOverUI.set(reason, score);
            ui.setDraw(gameOverUI);
            ui.setBackground(gameOverUI.getBackgroundColor());
            engineThread.interrupt();
            ui.setKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        runMenu();
                    }
                }
            });
        });
    }

}