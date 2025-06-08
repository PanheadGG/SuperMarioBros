package github.PanheadGG.SuperMarioBros.model.item;

import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.model.GameObject;

import java.awt.image.BufferedImage;

public class Air extends GameObject {
    public Air(){super(0);}

    public Air(int pixelPerUnit) {
        super(pixelPerUnit);
    }

    public Air(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
    }

    public Air(DynamicImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }

    public Air(BufferedImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }
}
