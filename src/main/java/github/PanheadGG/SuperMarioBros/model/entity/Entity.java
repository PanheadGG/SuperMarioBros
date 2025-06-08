package github.PanheadGG.SuperMarioBros.model.entity;

import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.model.GameObject;

import java.awt.image.BufferedImage;

public abstract class Entity extends GameObject {

    final public static class Status {
        public static final String LEFT = "left";
        public static final String RIGHT = "right";
        public static final String STOP = "stop";
    }

    public static class Action {
        public static final String LEFT = "left";
        public static final String RIGHT = "right";
        public static final String STOP = "stop";
        public static final String JUMP = "jump";
    }

    protected String horizontalStatus = Status.STOP;

    public void move(String action){};

    public Entity(int pixelPerUnit) {
        super(pixelPerUnit);
    }

    public Entity(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
    }

    public Entity(DynamicImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }

    public Entity(BufferedImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }

    public String getHorizontalStatus() {
        return horizontalStatus;
    }

    public void setHorizontalStatus(String horizontalStatus) {
        this.horizontalStatus = horizontalStatus;
    }
}
