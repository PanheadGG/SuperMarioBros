package github.PanheadGG.SuperMarioBros.model.entity.prop;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.core.Camera;

import java.awt.*;

public class Star extends Prop {
    private boolean preAnimation = true;
    private int timeRemain = 0;

    public Star(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
        timeRemain = gameTickRate;
        init();
    }

    private void init() {
        setTexture(Assets.getDynamicImageByKey("texture.prop.star"));
        setWidth(1);
        setHeight(1);
        horizontalStatus = Status.STOP;
        verticalAcc = 50;
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

    public void bounce(){
        velY = -11;
        verticalAcc = 50;
    }

    @Override
    public void move(String action) {
        switch (action) {
            case Action.LEFT:
                horizontalStatus = Status.LEFT;
                velX = -6;
                break;
            case Action.RIGHT:
                horizontalStatus = Status.RIGHT;
                velX = 6;
                break;
        }
    }

    public boolean isPreAnimation() {
        return preAnimation;
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
