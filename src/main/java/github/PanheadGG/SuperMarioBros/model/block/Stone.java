package github.PanheadGG.SuperMarioBros.model.block;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.assets.DynamicImage;

import java.awt.image.BufferedImage;

public class Stone extends Block{
    public Stone(int pixelPerUnit, double x, double y){
        super(pixelPerUnit);
        init();
        setX(x);
        setY(y);
    }

    public Stone(int pixelPerUnit) {
        super(pixelPerUnit);
        init();
    }

    public Stone(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
        init();
    }

    public Stone(DynamicImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
        init();
    }

    public Stone(BufferedImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
        init();
    }
    private void init(){
        setTexture(Assets.getImageByKey("texture.block.stone"));
        setWidth(1);
        setHeight(1);
    }
}
