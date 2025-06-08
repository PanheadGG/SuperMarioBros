package github.PanheadGG.SuperMarioBros.model.entity.prop;

import github.PanheadGG.SuperMarioBros.assets.Assets;

public class FireBall extends Prop {

    public FireBall(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
        init();
    }

    private void init() {
        setTexture(Assets.getDynamicImageByKey("texture.prop.fire_ball"));
        setWidth(0.5);
        setHeight(0.5);
        verticalAcc = 50;
    }

    public void bounce(){
        velY = -10;
        verticalAcc = 50;
    }
}
