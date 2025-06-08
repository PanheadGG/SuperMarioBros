package github.PanheadGG.SuperMarioBros.model.block;

import github.PanheadGG.SuperMarioBros.assets.Assets;

public class StageBlock extends Block{
    public StageBlock(int pixelPerUnit,double x,double y) {
        super(pixelPerUnit);
        setPosition(x,y);
        init();
    }
    public void init() {
        setTexture(Assets.getImageByKey("texture.block.stage"));
        setWidth(1);
        setHeight(1);
    }
}
