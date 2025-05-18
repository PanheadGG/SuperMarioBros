package github.PanheadGG.SuperMarioBros.model.block;

import github.PanheadGG.SuperMarioBros.assets.Asset;
import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.utils.ImageUtil;

import java.awt.image.BufferedImage;

public class Brick extends Block implements Breakable{
    public Brick(int pixelPerUnit, double x,double y){
        super(pixelPerUnit);
        init();
        setX(x);
        setY(y);
    }

    public Brick(int pixelPerUnit) {
        super(pixelPerUnit);
        init();
    }

    public Brick(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
        init();
    }

    public Brick(DynamicImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
        init();
    }

    public Brick(BufferedImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
        init();
    }
    private void init(){
        BufferedImage image = Asset.getImage(Asset.TILES_IMAGE);
        BufferedImage image1 = ImageUtil.getSubImage(image,0, 0, 16, 16);
        setTexture(image1);
        setWidth(1);
        setHeight(1);
    }
}
