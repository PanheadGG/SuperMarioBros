package github.PanheadGG.SuperMarioBros.model.item;

import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.model.GameObject;

import java.awt.image.BufferedImage;

public abstract class Item extends GameObject {
    public Item(BufferedImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }

    public Item(DynamicImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }

    public Item(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
    }

    public Item(int pixelPerUnit) {
        super(pixelPerUnit);
    }
}
