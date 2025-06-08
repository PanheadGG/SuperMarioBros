package github.PanheadGG.SuperMarioBros.core;

import github.PanheadGG.SuperMarioBros.core.scene.GameEngine;
import github.PanheadGG.SuperMarioBros.model.entity.player.Player;

public class Camera {
    private Player player;
    private Camera instance;
    private double x = 0;
    private double y = 0;
    private int pixelPerUnit;
    private Camera getInstance(GameEngine engine){
        if(instance == null)  {
            instance = new Camera(engine);
        }
        return instance;
    }
    public Camera(GameEngine engine){
        pixelPerUnit = engine.getPixelPerUnit();
    }

    public double getX() {
        return x;
    }

    public int getPixelX() {
        return (int) (x * pixelPerUnit);
    }

    public int getPixelY() {
        return (int) (y * pixelPerUnit);
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
    public void set(double x,double y){
        this.x = x;
        this.y = y;
    }
    public void move(double x,double y){
        this.x+=x;
        this.y+=y;
    }
    public void moveX(double x){
        this.x+=x;
    }
    public void moveY(double y){
        this.y+=y;
    }
    public void bindPlayer(Player player){
        this.player = player;
    }
}
