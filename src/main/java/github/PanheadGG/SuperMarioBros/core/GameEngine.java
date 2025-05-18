package github.PanheadGG.SuperMarioBros.core;

import github.PanheadGG.SuperMarioBros.map.GameMap;
import github.PanheadGG.SuperMarioBros.map.Map2;
import github.PanheadGG.SuperMarioBros.map.MapLoader;
import github.PanheadGG.SuperMarioBros.timer.MicroTimer;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;

public class GameEngine implements Runnable {
    private int gameTickRate = 200;
    private int refreshRate = 165;
    private int pixelPerUnit = 80;
    private int mapWidth;
    private int mapHeight;
    private final GameUI ui;
    private GameMap map;

    public GameEngine() {
//        map = Map1.getInstance(this);
        MapLoader loader = new MapLoader();
        loader.setGroundMap(this);
        loader.load(GameEngine.class.getResource("/assets/map/level_1.json"));
//        map = new Map2(this);
        map = loader.getGroundMap();
        mapWidth = map.getMapWidth();
        mapHeight = map.getMapHeight();
        pixelPerUnit = map.getPixelPerUnit();
        ui = new GameUI(this, pixelPerUnit * mapWidth, pixelPerUnit * mapHeight, refreshRate);
        new Thread(ui).start();
    }

    public void addKeyListener(KeyListener keyListener) {
        ui.addKeyListener(keyListener);
    }

    public void drawFrame(Graphics g) {
        map.drawFrame(g);
    }

    @Override
    public void run() {
        map.afterCreateUI();
        double tps = 0;
        long start = System.currentTimeMillis();
        long end;
        int tick = 0;
        while (true) {
            tick++;
            end = System.currentTimeMillis();
            if (tick >= gameTickRate) {
                tick = 0;
                tps = gameTickRate * 1000 / (double) (end - start);
//                System.out.println("TPS: "+tps);
                start = System.currentTimeMillis();
            }

            map.update();
            MicroTimer.sleep(1000000 / gameTickRate);
        }
    }

    public int getGameTickRate() {
        return gameTickRate;
    }

    public int getPixelPerUnit() {
        return pixelPerUnit;
    }

    public GameUI getUi() {
        return ui;
    }

    public void onWindowSizeChanged(ComponentEvent e){

    }
    public static void main(String[] args) {
        new GameEngine().run();
    }
}
