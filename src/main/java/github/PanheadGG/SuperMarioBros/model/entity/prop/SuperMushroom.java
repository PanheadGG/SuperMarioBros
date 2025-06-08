package github.PanheadGG.SuperMarioBros.model.entity.prop;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.core.Camera;

import java.awt.*;

public class SuperMushroom extends Prop {
    private boolean preAnimation = true;
    private int timeRemain = 0;

    public SuperMushroom(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
        timeRemain = gameTickRate;
        init();
    }

    private void init() {
        setTexture(Assets.getImageByKey("texture.prop.super_mushroom"));
        setWidth(1);
        setHeight(1);
        horizontalStatus = Status.STOP;
    }

    @Override
    public void update() {
        if (timeRemain <= 0) {
            if (!preAnimation && horizontalStatus.equals(Status.STOP)) {
                move(Action.RIGHT);
            }
            preAnimation = false;
        } else {
            timeRemain--;
        }
    }

    @Override
    public void move(String action) {
        switch (action) {
            case Action.LEFT:
                horizontalStatus = Status.LEFT;
                velX = -5;
                break;
            case Action.RIGHT:
                horizontalStatus = Status.RIGHT;
                velX = 5;
                break;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (preAnimation) {
            g.drawImage(getCurrentTexture(), getPixelX(), (int) (getPixelY() + ((double) timeRemain / (double) gameTickRate) * pixelPerUnit), pixelWidth, pixelHeight, null);
        } else {
            g.drawImage(getCurrentTexture(), getPixelX(), getPixelY(), pixelWidth, pixelHeight, null);
        }
    }

    @Override
    public void draw(Graphics g, Camera camera) {
        if (preAnimation) {
            g.drawImage(getCurrentTexture(), getPixelX() - camera.getPixelX(), (int) (getPixelY() - camera.getPixelY() + ((double) timeRemain / (double) gameTickRate) * pixelPerUnit), pixelWidth, pixelHeight, null);
        } else {
            g.drawImage(getCurrentTexture(), getPixelX() - camera.getPixelX(), getPixelY() - camera.getPixelY(), pixelWidth, pixelHeight, null);
        }
    }
}
