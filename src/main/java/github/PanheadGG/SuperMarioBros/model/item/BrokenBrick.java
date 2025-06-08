package github.PanheadGG.SuperMarioBros.model.item;

import github.PanheadGG.SuperMarioBros.assets.Assets;

public class BrokenBrick extends Item {
    public static final class Direction {
        public static final String TOP_LEFT = "top_left";
        public static final String TOP_RIGHT = "top_right";
        public static final String BOTTOM_LEFT = "bottom_left";
        public static final String BOTTOM_RIGHT = "bottom_right";
    }

    public BrokenBrick(int gameTickRate, int pixelPerUnit, double x, double y, String direction) {
        super(gameTickRate, pixelPerUnit);
        setPosition(x, y);
        init(direction);
    }

    private void init(String direction) {
        setTexture(Assets.getDynamicImageByKey("texture.item.broken_brick"));
        verticalAcc = 50;
        setWidth(0.5);
        setHeight(0.5);
        switch (direction) {
            case Direction.TOP_LEFT:
                velX = -5;
                velY = -15;
                break;
            case Direction.TOP_RIGHT:
                velX = 5;
                velY = -15;
                break;
            case Direction.BOTTOM_LEFT:
                velX = -5;
                velY = -5;
                break;
            case Direction.BOTTOM_RIGHT:
                velX = 5;
                velY = -5;
                break;
        }
    }
}
