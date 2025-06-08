package github.PanheadGG.SuperMarioBros.model.item;

import github.PanheadGG.SuperMarioBros.assets.Assets;

public class Boom extends Item {
    private int clearTime = 1;

    public Boom(int gameTickRate, int pixelPerUnit, double x, double y) {
        super(gameTickRate, pixelPerUnit);
        setPosition(x, y);
        init();
    }

    private void init() {
        setTexture(Assets.getDynamicImageByKey("texture.item.boom"));
        setWidth(1);
        setHeight(1);
        clearTime = gameTickRate/2;
    }

    @Override
    public void update() {
        if(clearTime-- <=0){
            needClear = true;
        }
    }
}
