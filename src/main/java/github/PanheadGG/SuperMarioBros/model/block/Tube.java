package github.PanheadGG.SuperMarioBros.model.block;

import github.PanheadGG.SuperMarioBros.assets.Assets;

public class Tube extends Block{
    public static class Texture{
        public static final String UPPER = "upper";
        public static final String LOWER = "lower";
    }

    private String texture;

    private Tube(int pixelPerUnit) {
        super(pixelPerUnit);
    }

    public Tube(int pixelPerUnit, double x,double y,String texture){
        super(pixelPerUnit);
        setPosition(x,y);
        this.texture = texture;
        init();
    }

    private void init() {
        if (Texture.UPPER.equals(texture)) {
            setTexture(Assets.getImageByKey("texture.tube.upper"));
        }
        if (Texture.LOWER.equals(texture)) {
            setTexture(Assets.getImageByKey("texture.tube.lower"));
        }
        setWidth(2);
        setHeight(1);
    }

}
