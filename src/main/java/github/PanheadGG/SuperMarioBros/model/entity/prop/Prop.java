package github.PanheadGG.SuperMarioBros.model.entity.prop;

import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.model.entity.Entity;

import java.awt.image.BufferedImage;

public abstract class Prop extends Entity {
    public Prop(int pixelPerUnit) {
        super(pixelPerUnit);
    }

    public Prop(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
    }

    public Prop(DynamicImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }

    public Prop(BufferedImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }
}
