package github.PanheadGG.SuperMarioBros.model.entity.player;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.core.Camera;

import java.awt.*;

public class FireMario extends Player {
    private static FireMario instance = null;

    public static FireMario getInstance(int gameTickRate, int pixelPerUnit) {
        if (instance == null) {
            instance = new FireMario(gameTickRate, pixelPerUnit);
        }
        instance.setGameTickRate(gameTickRate);
        instance.setPixelPerUnit(pixelPerUnit);
        return instance;
    }

    public FireMario(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
        init();
    }

    private void init() {
        walkImage = Assets.getDynamicImageByKey("texture.mario.fire.walk");
        standImage = Assets.getImageByKey("texture.mario.fire.stand");
        jumpImage = Assets.getImageByKey("texture.mario.fire.jump");
        walkStar = Assets.getDynamicImageByKey("texture.mario.fire.star.walk");
        standStar = Assets.getDynamicImageByKey("texture.mario.fire.star.stand");
        jumpStar = Assets.getDynamicImageByKey("texture.mario.fire.star.jump");
        setTexture(standImage);
        setWidth(1);
        setHeight(2);
        setLimitVelX(6);
        setLimitVelY(15);
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(getPixelX(), getPixelY(), (int) (pixelWidth - 2 * pixelPerUnit / 16.0), pixelHeight);
    }

    @Override
    public void draw(Graphics g) {
        if (show) {
            g.drawImage(getCurrentTexture(), getPixelX() - (int) (pixelPerUnit / 16.0), getPixelY(), pixelWidth, pixelHeight, null);
        }
    }

    @Override
    public void draw(Graphics g, Camera camera) {
        if (show) {
            g.drawImage(getCurrentTexture(), getPixelX() - (int) (pixelPerUnit / 16.0) - camera.getPixelX(), getPixelY() - camera.getPixelY(), pixelWidth, pixelHeight, null);
        }
    }
}
