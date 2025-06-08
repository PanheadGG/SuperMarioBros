package github.PanheadGG.SuperMarioBros.model.item;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.core.Camera;
import github.PanheadGG.SuperMarioBros.manager.SoundManager;

import java.awt.*;

public class Coin extends Item {
    private double originY;
    private boolean animation = false;
    private int clearTime = 1;

    public Coin(int gameTickRate, int pixelPerUnit, double x, double y) {
        super(gameTickRate, pixelPerUnit);
        setPosition(x, y);
        originY = y;
        velY = -20;
        verticalAcc = 50;
        init();
    }

    private void init() {
        setTexture(Assets.getDynamicImageByKey("texture.item.coin"));
        SoundManager.playCoin();
        setWidth(1);
        setHeight(1);
    }

    @Override
    public void draw(Graphics g, Camera camera) {
        if (!animation) {
            g.drawImage(getCurrentTexture(), getPixelX() - camera.getPixelX(), getPixelY() - camera.getPixelY(), pixelWidth, pixelHeight, null);
        } else {
            g.setFont(Assets.FONT);
            g.drawString("200", getPixelX() - camera.getPixelX(), getPixelY() - camera.getPixelY());
        }

    }

    @Override
    public void update() {
        super.update();
        if (!animation && y > originY) {
            animation = true;
            clearTime = gameTickRate/2;
            y -= 0.5;
        }
        if (animation) {
            velY = -1;
            verticalAcc = 0;
            clearTime--;
            if (clearTime <= 0) {
                needClear = true;
            }
        }
    }
}
