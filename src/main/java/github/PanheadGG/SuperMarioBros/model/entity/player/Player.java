package github.PanheadGG.SuperMarioBros.model.entity.player;

import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.model.entity.Entity;

import java.awt.image.BufferedImage;

public abstract class Player extends Entity {

    public enum Action {
        LEFT, RIGHT, STOP, JUMP
    }

    public abstract void move(Action action);

    public Player(int pixelPerUnit) {
        super(pixelPerUnit);
    }

    public Player(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
    }

    public Player(DynamicImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }

    public Player(BufferedImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }
}
