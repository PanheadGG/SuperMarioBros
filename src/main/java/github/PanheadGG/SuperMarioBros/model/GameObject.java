package github.PanheadGG.SuperMarioBros.model;

import github.PanheadGG.SuperMarioBros.assets.Asset;
import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.core.Camera;
import github.PanheadGG.SuperMarioBros.utils.ImageUtil;

import java.awt.*;
import java.awt.image.BufferedImage;

import static github.PanheadGG.SuperMarioBros.utils.ImageUtil.mirror;

public abstract class GameObject implements Comparable {
    protected double x = 0;
    protected double y = 0;
    protected double velX = 0;
    protected double velY = 0;
    protected double horizontalAcc = 0;
    protected double verticalAcc = 0;
    protected ImageType imageType = ImageType.UNKNOWN;
    protected DynamicImage animatedImage = null;
    protected BufferedImage bufferedImage = null;
    protected double width = 0;
    protected double height = 0;
    protected int pixelWidth = 0;
    protected int pixelHeight = 0;
    protected boolean falling = true;
    protected boolean jumping = false;
    protected boolean jumpedHighest = false;
    protected int pixelPerUnit = 0;
    protected int gameTickRate;
    protected boolean reverseDirection = false;
    protected double limitVelX = 0;
    protected double limitVelY = 0;
    protected boolean needClear = false;
    protected int id = 0;
    private static int idCounter = 0;

    public enum ImageType {
        UNKNOWN, STATIC, DYNAMIC
    }

    public boolean isNeedClear() {
        return needClear;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
        pixelWidth = (int) (width * pixelPerUnit);
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
        pixelHeight = (int) (height * pixelPerUnit);
    }

    public void setNeedClear(boolean needClear) {
        this.needClear = needClear;
    }

    public void setLimitVelX(double limitVelX) {
        if (limitVelX < 0) limitVelX = -limitVelX;
        this.limitVelX = limitVelX;
    }

    public void setLimitVelY(double limitVelY) {
        if (limitVelY < 0) limitVelY = -limitVelY;
        this.limitVelY = limitVelY;
    }

    public void setReverseDirection(boolean reverseDirection) {
        this.reverseDirection = reverseDirection;
    }

    public boolean isReversed() {
        return reverseDirection;
    }

    public void reversed(boolean reverseDirection) {
        this.reverseDirection = reverseDirection;
    }


    public GameObject(int pixelPerUnit) {
        this.pixelPerUnit = pixelPerUnit;
        id = idCounter++;
    }

    public GameObject(int gameTickRate, int pixelPerUnit) {
        this.gameTickRate = gameTickRate;
        this.pixelPerUnit = pixelPerUnit;
        id = idCounter++;
    }

    public GameObject(DynamicImage image, int gameTickRate, int pixelPerUnit) {
        animatedImage = image;
        imageType = ImageType.DYNAMIC;
        this.gameTickRate = gameTickRate;
        this.pixelPerUnit = pixelPerUnit;
        pixelWidth = image.getWidth();
        pixelHeight = image.getHeight();
        id = idCounter++;
    }

    public GameObject(BufferedImage image, int gameTickRate, int pixelPerUnit) {
        bufferedImage = image;
        imageType = ImageType.STATIC;
        this.gameTickRate = gameTickRate;
        this.pixelPerUnit = pixelPerUnit;
        pixelWidth = image.getWidth();
        pixelHeight = image.getHeight();
        id = idCounter++;
    }

    public BufferedImage getCurrentTexture() {
        switch (imageType) {
            case STATIC:
                if (reverseDirection) return mirror(bufferedImage, ImageUtil.FlipType.HORIZONTAL);
                else return bufferedImage;
            case DYNAMIC:
                if (reverseDirection) return mirror(animatedImage.getTexture(), ImageUtil.FlipType.HORIZONTAL);
                else return animatedImage.getTexture();
            case UNKNOWN:
            default:
                return Asset.UNKNOWN_TEXTURE;
        }
    }

    public void setGameTickRate(int gameTickRate) {
        this.gameTickRate = gameTickRate;
        if (animatedImage != null) setGameTickRate(gameTickRate);
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVelX() {
        return velX;
    }

    public void setVelX(double velX) {
        this.velX = velX;
    }

    public double getVelY() {
        return velY;
    }

    public void setVelY(double velY) {
        this.velY = velY;
    }

    public double getHorizontalAcc() {
        return horizontalAcc;
    }

    public void setHorizontalAcc(double horizontalAcc) {
        this.horizontalAcc = horizontalAcc;
    }

    public double getVerticalAcc() {
        return verticalAcc;
    }

    public void setVerticalAcc(double verticalAcc) {
        this.verticalAcc = verticalAcc;
    }

    public int getPixelWidth() {
        return pixelWidth;
    }

    public int getPixelHeight() {
        return pixelHeight;
    }

    public boolean isFalling() {
        return falling;
    }

    public void setFalling(boolean falling) {
        this.falling = falling;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public boolean isJumpedHighest() {
        return jumpedHighest;
    }

    public void setJumpedHighest(boolean jumpedHighest) {
        this.jumpedHighest = jumpedHighest;
    }

    public int getPixelPerUnit() {
        return pixelPerUnit;
    }

    public void setPixelPerUnit(int pixelPerUnit) {
        this.pixelPerUnit = pixelPerUnit;
    }

    public int getPixelX() {
        return (int) (x * pixelPerUnit);
    }

    public int getPixelY() {
        return (int) (y * pixelPerUnit);
    }

    public void updateVelX() {
        velX += horizontalAcc / gameTickRate;
        if (limitVelX == 0) return;
        if (velX > limitVelX) velX = limitVelX;
        if (velX < -limitVelX) velX = -limitVelX;
    }

    public void updateVelY() {
        velY += verticalAcc / gameTickRate;
        if (limitVelY == 0) return;
        if (velY > limitVelY) velY = limitVelY;
        if (velY < -limitVelY) velY = -limitVelY;
    }

    public void updateVelocity() {
        updateVelX();
        updateVelY();
    }

    public void updatePosition() {
        updateVelocity();
        x += velX / gameTickRate;
        y += velY / gameTickRate;
    }

    public void updatePositionX() {
        updateVelX();
        x += velX / gameTickRate;
    }

    public void updatePositionY() {
        updateVelY();
        y += velY / gameTickRate;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        updatePosition();
        if (animatedImage != null) animatedImage.update();
    }

    public void updateTexture() {
        if (animatedImage != null) animatedImage.update();
    }

    public void setTexture(DynamicImage image) {
        this.animatedImage = image;
        image.setGameTickRate(gameTickRate);
        this.bufferedImage = null;
        imageType = ImageType.DYNAMIC;
        if (pixelWidth == -1 || pixelHeight == -1) {
            pixelWidth = image.getWidth();
            pixelHeight = image.getHeight();
        }
    }

    public void setTexture(BufferedImage image) {
        this.animatedImage = null;
        this.bufferedImage = image;
        imageType = ImageType.STATIC;
        if (pixelWidth == -1 || pixelHeight == -1) {
            pixelWidth = image.getWidth();
            pixelHeight = image.getHeight();
        }
    }

    public void draw(Graphics g) {
        g.drawImage(getCurrentTexture(), getPixelX(), getPixelY(), pixelWidth, pixelHeight, null);
    }

    public void draw(Graphics g, Camera camera) {
        g.drawImage(getCurrentTexture(), getPixelX() - camera.getPixelX(), getPixelY() - camera.getPixelY(), pixelWidth, pixelHeight, null);
    }

    //通过pixelX修改x
    public void setPositionByPixelX(int pixelX) {
        x = (double) pixelX / pixelPerUnit;
    }

    public void setPositionByPixelY(int pixelY) {
        y = (double) pixelY / pixelPerUnit;
    }

    public void setPositionByPixel(int pixelX, int pixelY) {
        setPositionByPixelX(pixelX);
        setPositionByPixelY(pixelY);
    }

    public void addExternalAccOnX(double accX) {
        velX += accX / gameTickRate;
    }

    public void addExternalAccOnY(double accY) {
        velY += accY / gameTickRate;
    }

    public Rectangle getRectangle() {
        return new Rectangle(getPixelX(), getPixelY(), getPixelWidth(), getPixelHeight());
    }

    public boolean collided(Rectangle rectangle) {
        return getRectangle().intersects(rectangle);
    }

    public boolean collided(GameObject object) {
        return getRectangle().intersects(object.getRectangle());
    }

    public static final class Position {
        public static final int TOP = 0;
        public static final int BOTTOM = 1;
        public static final int LEFT = 2;
        public static final int RIGHT = 3;
    }

    public boolean crashed(GameObject object, int position, double percent) {
        Rectangle self = getRectangle();
        Rectangle o = object.getRectangle();
        switch (position) {
            case Position.TOP:
                return (double) (self.y + self.height - o.y) / o.height > percent;
            case Position.BOTTOM:
                return (double) (o.y + o.height - self.y) / o.height > percent;
            case Position.LEFT:
                return (double) (self.x + self.width - o.x) / o.width > percent;
            case Position.RIGHT:
                return (double) (o.x + o.width - self.x) / o.width > percent;
            default:
                return false;
        }
    }

    public boolean on(GameObject object) {
        Rectangle self = getRectangle();
        self.y += self.height + (int) (velY / gameTickRate * pixelPerUnit);
        self.height = 1;
        return self.intersects(object.getRectangle());
    }


    public boolean leftIs(GameObject object) {
        Rectangle self = getRectangle();
        self.width = 1;
        self.x -= 1 - velX / gameTickRate * pixelPerUnit;
        return self.intersects(object.getRectangle());
    }

    public boolean rightIs(GameObject object) {
        Rectangle self = getRectangle();
        self.x += self.width + (int) (velX / gameTickRate * pixelPerUnit);
        self.width = 1;
        return self.intersects(object.getRectangle());
    }

    public boolean aboveIs(GameObject object) {
        Rectangle self = getRectangle();
        self.y -= 1 - (velY / gameTickRate * pixelPerUnit);
        self.height = 1;
        return self.intersects(object.getRectangle());
    }


    public int compareTo(Object object) {
        GameObject gameObject;
        if (object instanceof GameObject) gameObject = (GameObject) object;
        else return 1;
        if (this.x - gameObject.x > 0) {
            return 1;
        }
        if (this.x - gameObject.x == 0) {
            if (this.y - gameObject.y > 0) return 1;
            else if (this.y - gameObject.y == 0) {
                if (getClass().getSimpleName().compareTo(object.getClass().getSimpleName()) == 0)
                    return gameObject.id - id;
            } else return -1;
        }
        return -1;
    }

    public boolean checkClear(Camera camera) {
        if ((camera == null ? 0 : camera.getX()) - x >= 2) {
            needClear = true;
            return true;
        }
        return false;
    }
}
