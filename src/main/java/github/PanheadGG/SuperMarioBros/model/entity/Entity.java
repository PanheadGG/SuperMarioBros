package github.PanheadGG.SuperMarioBros.model.entity;

import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.model.GameObject;

import java.awt.image.BufferedImage;

public abstract class Entity extends GameObject {
    protected Status horizontalStatus = Status.STOP;
    public enum Status {
        STOP,
        LEFT,
        RIGHT
    }

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

    public Status getHorizontalStatus() {
        return horizontalStatus;
    }

    public void setHorizontalStatus(Status horizontalStatus) {
        this.horizontalStatus = horizontalStatus;
    }
}
