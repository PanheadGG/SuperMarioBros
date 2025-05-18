package github.PanheadGG.SuperMarioBros.model.entity.player;

import github.PanheadGG.SuperMarioBros.assets.Asset;
import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.core.Camera;
import github.PanheadGG.SuperMarioBros.manager.SoundManager;

import java.awt.*;
import java.awt.image.BufferedImage;

import static github.PanheadGG.SuperMarioBros.utils.ImageUtil.getSubImage;

public class Mario extends Player {
    private DynamicImage walkImage;
    private BufferedImage standImage;
    private BufferedImage jumpImage;

    public enum Texture {
        STAND, JUMP, WALK
    }

    public Mario(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
        init();
    }

    private void init() {
        BufferedImage marioImage = Asset.getImage(Asset.MARIO_IMAGE);
        BufferedImage walkImage1 = getSubImage(marioImage, 0, 32, 16, 16);
        BufferedImage walkImage2 = getSubImage(marioImage, 16, 32, 16, 16);
        BufferedImage walkImage3 = getSubImage(marioImage, 32, 32, 16, 16);
        standImage = getSubImage(marioImage, 16 * 6, 32, 16, 16);
        jumpImage = getSubImage(marioImage, 16 * 4, 32, 16, 16);
        walkImage = new DynamicImage(new BufferedImage[]{walkImage1, walkImage2, walkImage3}, 18);
        setTexture(standImage);
        setWidth(1);
        setHeight(1);
        setLimitVelX(6);
    }


    public void setTexture(Texture texture) {
        switch (texture) {
            case WALK:
                setTexture(walkImage);
                break;
            case JUMP:
                setTexture(jumpImage);
                break;
            case STAND:
                setTexture(standImage);
                break;
        }
    }

    @Override
    public void move(Action action) {
        switch (action) {
            case LEFT:
                leftMove();
                break;
            case RIGHT:
                rightMove();
                break;
            case STOP:
                stopMove();
                break;
            case JUMP:
                jump();
                break;
        }
    }

    public void bounds() {
        setVelY(-12);
    }

    private void jump() {
        if (!isFalling()) {
            setVelY(-21);
            setFalling(true);
            setJumping(true);
            SoundManager.playJump();
        }
    }

    private void leftMove() {
        if (Math.abs(getVelX()) > 1) {
            addExternalAccOnX(-10 * Math.signum(getVelX()));
        }
        switch (getHorizontalStatus()) {
            case STOP:
                setHorizontalAcc(-30);
                setHorizontalStatus(Status.LEFT);
                break; // 添加 break 语句，避免 fall-through
            case LEFT:
                setHorizontalAcc(-30);
                break;
            case RIGHT:
                setHorizontalAcc(-30);
                addExternalAccOnX(-3.5);
                setHorizontalStatus(Status.LEFT);
                break;
        }
        if (isJumping()) setTexture(Texture.JUMP);
        else setTexture(Texture.WALK);
        reversed(true);
    }

    private void rightMove() {
        if (Math.abs(getVelX()) > 1) {
            addExternalAccOnX(-10 * Math.signum(getVelX()));
        }
        switch (getHorizontalStatus()) {
            case STOP:
                setHorizontalAcc(30);
                setHorizontalStatus(Status.RIGHT);
                break;
            case RIGHT:
                setHorizontalAcc(30);
                break;
            case LEFT:
                setHorizontalAcc(30);
                addExternalAccOnX(3.5);
                setHorizontalStatus(Status.RIGHT);
                break;
        }
        if (isJumping()) setTexture(Texture.JUMP);
        else setTexture(Texture.WALK);
        reversed(false);
    }

    private void stopMove() {
        setHorizontalAcc(0);


        if (Math.abs(getVelX()) > 1) {
            addExternalAccOnX(-20 * Math.signum(getVelX()));
        } else {
            setHorizontalStatus(Status.STOP);
            setVelX(0); // 当速度接近零时，直接设置速度为零
            setHorizontalAcc(0);
        }
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(getPixelX(), getPixelY(), (int) (pixelWidth - 2 * pixelPerUnit / 16.0), pixelHeight);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(getCurrentTexture(), getPixelX()-(int)(pixelPerUnit / 16.0), getPixelY(), pixelWidth, pixelHeight, null);
    }

    @Override
    public void draw(Graphics g, Camera camera) {
        g.drawImage(getCurrentTexture(), getPixelX()-(int)(pixelPerUnit / 16.0)-camera.getPixelX(), getPixelY()-camera.getPixelY(), pixelWidth, pixelHeight, null);
    }
}
