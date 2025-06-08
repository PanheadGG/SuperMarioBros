package github.PanheadGG.SuperMarioBros.model.entity.prop;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.core.Camera;

import java.awt.*;

public class FireFlower extends Prop {
    private boolean preAnimation = true;
    private int timeRemain = 0;

    public FireFlower(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
        timeRemain = gameTickRate;
        init();
    }

    private void init() {
        setTexture(Assets.getDynamicImageByKey("texture.prop.fire_flower"));
        setWidth(1);
        setHeight(1);
        horizontalStatus = Status.STOP;
    }

    @Override
    public void update() {
        if (timeRemain <= 0) {
            preAnimation = false;
        } else {
            timeRemain--;
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
