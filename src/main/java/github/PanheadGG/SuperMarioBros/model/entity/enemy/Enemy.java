package github.PanheadGG.SuperMarioBros.model.entity.enemy;

import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.model.entity.Entity;

import java.awt.image.BufferedImage;

public abstract class Enemy extends Entity {
    protected boolean dead;
    protected int clearTime=1;

    public enum Action {
        LEFT, RIGHT, SQUASHED, UPSIDE_DOWN;
    }

    public abstract void move(Action action);

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
}
