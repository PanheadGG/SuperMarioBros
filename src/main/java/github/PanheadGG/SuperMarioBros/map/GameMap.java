package github.PanheadGG.SuperMarioBros.map;

import github.PanheadGG.SuperMarioBros.core.Camera;
import github.PanheadGG.SuperMarioBros.core.scene.GameEngine;
import github.PanheadGG.SuperMarioBros.listener.GameKeyListener;
import github.PanheadGG.SuperMarioBros.model.entity.player.Player;

import java.awt.*;

public abstract class GameMap {
    protected int pixelPerUnit;
    protected int mapWidth = 15;
    protected int mapHeight = 14;
    protected GameEngine engine;
    protected int gameTickRate=0;
    protected Camera camera;
    protected Player player;
    protected GameKeyListener key;
    protected Color backgroundColor = new Color(98,173,255);
    protected String mapName = "";
    public GameMap(GameEngine engine){
        this.engine = engine;
        gameTickRate = engine.getGameTickRate();
        camera = new Camera(engine);
        key = new GameKeyListener();
        pixelPerUnit = engine.getPixelPerUnit();
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setKeyListener(GameKeyListener gameKeyListener) {
        this.key = gameKeyListener;
    }

    public GameKeyListener getKeyListener() {
        return key;
    }

    public Camera getCamera() {
        return camera;
    }

    public void bindCamera(Camera camera) {
        this.camera = camera;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void update(){}
    public void drawFrame(Graphics g) {
    }

    public int getPixelPerUnit() {
        return pixelPerUnit;
    }

    public void setPixelPerUnit(int pixelPerUnit) {
        this.pixelPerUnit = pixelPerUnit;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }
}
