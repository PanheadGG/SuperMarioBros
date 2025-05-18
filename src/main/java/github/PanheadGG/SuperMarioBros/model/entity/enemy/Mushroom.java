package github.PanheadGG.SuperMarioBros.model.entity.enemy;

import github.PanheadGG.SuperMarioBros.assets.Asset;
import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.manager.SoundManager;
import github.PanheadGG.SuperMarioBros.utils.ImageUtil;

import java.awt.image.BufferedImage;

import static github.PanheadGG.SuperMarioBros.utils.ImageUtil.getSubImage;
import static github.PanheadGG.SuperMarioBros.utils.ImageUtil.mirror;

public class Mushroom extends Enemy {
    private DynamicImage walkImage;
    private BufferedImage squashedImage;
    private BufferedImage upsideDownImage;

    public enum Texture {
        WALK, SQUASHED, UPSIDE_DOWN;
    }

    public Mushroom(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
        init();
    }

    public void init() {
        BufferedImage enemiesImage = Asset.getImage(Asset.ENEMIES_IMAGE);
        BufferedImage image1 = getSubImage(enemiesImage, 0, 16, 16, 16);
        BufferedImage image2 = getSubImage(enemiesImage,16, 16, 16, 16);
        walkImage = new DynamicImage(new BufferedImage[]{image1, image2}, 8);
        squashedImage = getSubImage(enemiesImage, 16 * 2, 16, 16, 16);
        upsideDownImage = mirror(image1, ImageUtil.FlipType.VERTICAL);
        setTexture(walkImage);
        setWidth(1);
        setHeight(1);
        clearTime = gameTickRate;
        move(Action.LEFT);
    }

    public void setTexture(Texture texture) {
        switch (texture) {
            case WALK:
                setTexture(walkImage);
                break;
            case SQUASHED:
                setTexture(squashedImage);
                break;
            case UPSIDE_DOWN:
                setTexture(upsideDownImage);
                break;
        }
    }

    @Override
    public void move(Action action) {
        switch (action) {
            case LEFT:
                horizontalStatus = Status.LEFT;
                velX = -2;
                break;
            case RIGHT:
                horizontalStatus = Status.RIGHT;
                velX = 2;
                break;
            case SQUASHED:
                dead = true;
                horizontalStatus = Status.STOP;
                velX = 0;
                setTexture(Texture.SQUASHED);
                SoundManager.playStomp();
                break;
            case UPSIDE_DOWN:
                dead = true;
                horizontalStatus = Status.STOP;
                setTexture(Texture.UPSIDE_DOWN);
                break;
        }
    }
}
