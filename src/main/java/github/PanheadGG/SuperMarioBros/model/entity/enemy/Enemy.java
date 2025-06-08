package github.PanheadGG.SuperMarioBros.model.entity.enemy;

import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.model.item.Air;
import github.PanheadGG.SuperMarioBros.model.GameObject;
import github.PanheadGG.SuperMarioBros.model.entity.Entity;

import java.awt.image.BufferedImage;

public abstract class Enemy extends Entity {
    protected boolean dead;
    protected int clearTime=1;

    public static class Action {
        public static final String LEFT = "left";
        public static final String RIGHT = "right";
        public static final String SQUASHED = "squashed";
        public static final String UPSIDE_DOWN = "upside_down";
    }

    public void move(String action){};

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public Enemy(int pixelPerUnit) {
        super(pixelPerUnit);
    }

    public Enemy(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
    }

    public Enemy(DynamicImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }

    public Enemy(BufferedImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }

    public void clearTime(){
        if (clearTime <= 0) needClear = true;
        clearTime--;
    }

    public GameObject getUpsideDownObj(){
        return new Air();
    }
}
