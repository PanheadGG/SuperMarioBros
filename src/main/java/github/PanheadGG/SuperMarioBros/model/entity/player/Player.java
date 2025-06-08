package github.PanheadGG.SuperMarioBros.model.entity.player;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.manager.SoundManager;
import github.PanheadGG.SuperMarioBros.model.entity.Entity;

import java.awt.image.BufferedImage;

public abstract class Player extends Entity {
    protected boolean upKeyPressed = false;
    protected DynamicImage walkImage;
    protected BufferedImage standImage;
    protected BufferedImage jumpImage;
    protected DynamicImage walkStar;
    protected DynamicImage standStar;
    protected DynamicImage jumpStar;


    protected double jumpStartY = 0;
    protected static final double TARGET_JUMP_HEIGHT = 4; // 目标跳跃高度

    protected String faceDirection = Status.RIGHT;
    protected boolean starTime = false;
    protected int invincibleTime = 0;
    protected boolean show = true;

    public static class Texture {
        public static final String STAND = "stand";
        public static final String JUMP = "jump";
        public static final String WALK = "walk";
    }

    public static class MarioType {
        public static final String MARIO = "mario";
        public static final String FIRE_MARIO = "fire_mario";
        public static final String SUPER_MARIO = "super_mario";
    }

    public void setUpKeyPressed(boolean upKeyPressed) {
        this.upKeyPressed = upKeyPressed;
    }

    public Player(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);

        walkImage = new DynamicImage(Assets.UNKNOWN_TEXTURE);
        standImage = Assets.UNKNOWN_TEXTURE;
        jumpImage = Assets.UNKNOWN_TEXTURE;
        walkStar = new DynamicImage(Assets.UNKNOWN_TEXTURE);
        standStar = new DynamicImage(Assets.UNKNOWN_TEXTURE);
        jumpStar = new DynamicImage(Assets.UNKNOWN_TEXTURE);
    }

    public Player(DynamicImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }

    public Player(BufferedImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }


    public void bounds() {
        setVelY(-8);
        setFalling(true);
    }


    public void setTexture(String texture) {
        switch (texture) {
            case Texture.WALK:
                if (starTime) setTexture(walkStar);
                else setTexture(walkImage);
                break;
            case Texture.JUMP:
                if (starTime) setTexture(jumpStar);
                else setTexture(jumpImage);
                break;
            case Texture.STAND:
                if (starTime) setTexture(standStar);
                else setTexture(standImage);
                break;
        }
    }

    @Override
    public void move(String action) {
        switch (action) {
            case Action.LEFT:
                leftMove();
                break;
            case Action.RIGHT:
                rightMove();
                break;
            case Action.STOP:
                stopMove();
                break;
        }
    }

    public void updateJump() {
        if (!jumpedHighest) {
            if (upKeyPressed) {
                if (!jumping) {
                    jumping = true;
                    velY = -10;
                    if (this instanceof Mario) SoundManager.playJump();
                    else SoundManager.playBigJump();
                    jumpStartY = y;
                } else {
                    if (jumpStartY - y < TARGET_JUMP_HEIGHT / 2) {
                        addExternalAccOnY(0);
                    } else if (jumpStartY - y >= TARGET_JUMP_HEIGHT) {
                        jumpedHighest = true;
                        falling = true;
                        velY = velY / 4;
                    }
                }
            } else {
                jumpedHighest = true;
                falling = true;
                velY = velY / 4;
            }
        }
    }

    private void leftMove() {
        if (Math.abs(getVelX()) > 1) {
            addExternalAccOnX(-10 * Math.signum(getVelX()));
        }
        switch (getHorizontalStatus()) {
            case Status.STOP:
                setHorizontalAcc(-30);
                setHorizontalStatus(Status.LEFT);
                break;
            case Status.LEFT:
                setHorizontalAcc(-30);
                faceDirection = Status.LEFT;
                break;
            case Status.RIGHT:
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
            case Status.STOP:
                setHorizontalAcc(30);
                setHorizontalStatus(Status.RIGHT);
                break;
            case Status.RIGHT:
                setHorizontalAcc(30);
                faceDirection = Status.RIGHT;
                break;
            case Status.LEFT:
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

    public Player changeTo(String mario) {
        Player p = null;
        switch (mario) {
            case MarioType.MARIO:
                p = new Mario(gameTickRate, pixelPerUnit);
                break;
            case MarioType.FIRE_MARIO:
                p = new FireMario(gameTickRate, pixelPerUnit);
                break;
            case MarioType.SUPER_MARIO:
                p = new SuperMario(gameTickRate, pixelPerUnit);
                break;
        }
        if (p != null) {
            p.x = x;
            p.y = (this instanceof Mario) ? y - 1 : y + 1;
            p.horizontalStatus = horizontalStatus;
            p.jumping = jumping;
            p.jumpedHighest = jumpedHighest;
            p.jumpStartY = jumpStartY;
            p.velX = velX;
            p.velY = velY;
            p.falling = falling;
            p.reverseDirection = reverseDirection;
            p.starTime = starTime;
            if (jumping) p.setTexture(Texture.JUMP);
            return p;
        }
        return this;
    }

    @Override
    public void update() {
        if (invincibleTime > 0) {
            invincibleTime--;
            if (invincibleTime % (gameTickRate / 4) < gameTickRate / 8) {
                show = true;
            } else {
                show = false;
            }
        } else {
            show = true;
        }
    }

    public String getFaceDirection() {
        return faceDirection;
    }

    public boolean isStarTime() {
        return starTime;
    }

    public void setStarTime(boolean starTime) {
        this.starTime = starTime;
    }

    public void setInvincibleTime(double s) {
        this.invincibleTime = (int) (s * gameTickRate);
    }

    public int getInvincibleTime() {
        return invincibleTime;
    }
}
