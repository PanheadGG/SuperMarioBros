package github.PanheadGG.SuperMarioBros.model.entity.enemy;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.manager.SoundManager;
import github.PanheadGG.SuperMarioBros.model.GameObject;
import github.PanheadGG.SuperMarioBros.model.item.UpsideDownGoomba;
import github.PanheadGG.SuperMarioBros.utils.ImageUtil;

import java.awt.image.BufferedImage;

import static github.PanheadGG.SuperMarioBros.utils.ImageUtil.getSubImage;
import static github.PanheadGG.SuperMarioBros.utils.ImageUtil.mirror;

public class Goomba extends Enemy {
    private DynamicImage walkImage;
    private BufferedImage squashedImage;
    private BufferedImage upsideDownImage;

    public enum Texture {
        WALK, SQUASHED, UPSIDE_DOWN;
    }

    public Goomba(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
        init();
    }

    public void init() {
        walkImage = Assets.getDynamicImageByKey("texture.enemy.goomba.walk");
        squashedImage = Assets.getImageByKey("texture.enemy.goomba.squashed");
        upsideDownImage = mirror(walkImage.get(0), ImageUtil.FlipType.VERTICAL);
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
    public void move(String action) {
        switch (action) {
            case Action.LEFT:
                horizontalStatus = Status.LEFT;
                velX = -2;
                break;
            case Action.RIGHT:
                horizontalStatus = Status.RIGHT;
                velX = 2;
                break;
            case Action.SQUASHED:
                dead = true;
                horizontalStatus = Status.STOP;
                velX = 0;
                setTexture(Texture.SQUASHED);
                SoundManager.playStomp();
                break;
            case Action.UPSIDE_DOWN:
                dead = true;
                velY = -5;
                verticalAcc = 25;
                horizontalStatus = Status.STOP;
                setTexture(Texture.UPSIDE_DOWN);
                break;
        }
    }

    @Override
    public GameObject getUpsideDownObj() {
        return new UpsideDownGoomba(gameTickRate, pixelPerUnit,x,y,velX);
    }
}
