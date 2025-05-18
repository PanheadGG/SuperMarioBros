package github.PanheadGG.SuperMarioBros.assets;

import java.awt.*;
import java.awt.image.BufferedImage;

public class DynamicImage {
    private BufferedImage[] images;
    private int gameTickRate = 1;
    private int tick;
    private int rate = 1;
    private boolean flipped = false;

    public DynamicImage(BufferedImage[] images, int gameTickRate, int rate) {
        this.images = images;
        this.gameTickRate = gameTickRate;
        tick = 0;
        this.rate = rate;
    }

    public DynamicImage(BufferedImage[] images, int rate) {
        this.images = images;
        tick = 0;
        this.rate = rate;
    }

    public DynamicImage(BufferedImage image) {
        this.images = new  BufferedImage[]{image};
        tick = 0;
    }

    public void setFlipped(boolean flipped) {
        this.flipped = flipped;
    }
    public BufferedImage getTexture() {
        // 计算当前帧的索引
        int current = (tick * rate) / gameTickRate;

        // 保证索引不会超出图片数组的范围
        current = current % images.length;

        return flipped ?mirror(images[current],1):images[current];
    }

    public void setGameTickRate(int gameTickRate) {
        this.gameTickRate = gameTickRate;
    }

    public void update() {
        tick++;
        // 当 tick 达到 gameTickRate 的倍数时重置为 0
        if (tick >= gameTickRate) {
            tick = 0;
        }
    }

    public int getWidth() {
        return images[0].getWidth();
    }

    public int getHeight() {
        return images[0].getHeight();
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    /**
     * 图片水平翻转
     *
     * @param source   图片源 （为优化内存，原图将会关闭。保留最新的指针）
     * @param flipType 翻转类型（1水平翻转，2垂直翻转）默认为1
     * @return
     */
    public static BufferedImage mirror(BufferedImage source, Integer flipType) {
        int width = source.getWidth();
        int height = source.getHeight();
        BufferedImage result = new BufferedImage(width, height, source.getType());
        Graphics graphics = result.getGraphics();
        try {
            if (flipType != null && flipType.equals(2)) {
                graphics.drawImage(source, 0, 0, width, height, 0, height, width, 0, null);
            } else {
                graphics.drawImage(source, 0, 0, width, height, width, 0, 0, height, null);
            }
        } catch (Exception ignored) {
        } finally {
            graphics.dispose();
            source.flush();
        }
        return result;
    }
}
