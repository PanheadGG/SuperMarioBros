package github.PanheadGG.SuperMarioBros.model.item;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.utils.ImageUtil;

public class UpsideDownKoopa extends Item {

    public UpsideDownKoopa(int gameTickRate, int pixelPerUnit, double x, double y, double velX) {
        super(gameTickRate, pixelPerUnit);
        setPosition(x, y);
        this.velX = velX;
        velY = -10;
        verticalAcc = 50;
        init();
    }

    private void init() {
        setTexture(ImageUtil.mirror(Assets.getImageByKey("texture.enemy.koopa.shell"), ImageUtil.FlipType.VERTICAL));
        setWidth(1);
        setHeight(1);
    }
}
