package github.PanheadGG.SuperMarioBros.utils;

import github.PanheadGG.SuperMarioBros.assets.Assets;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

public class ImageUtil {
    public static final class FlipType {
        public static final int HORIZONTAL = 1;
        public static final int VERTICAL = 2;
    }
    /**
     * 图片水平翻转
     *
     * @param source   图片源 （为优化内存，原图将会关闭。保留最新的指针）
     * @param flipType 翻转类型（1水平翻转，2垂直翻转）默认为1
     */
    public static BufferedImage mirror(BufferedImage source, int flipType) {
        if(source==null) return Assets.UNKNOWN_TEXTURE;
        int width = source.getWidth();
        int height = source.getHeight();
        BufferedImage result = new BufferedImage(width, height, source.getType());
        Graphics graphics = result.getGraphics();
        try {
            if (flipType == FlipType.VERTICAL) {
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
    public static BufferedImage getSubImage(BufferedImage source, int x, int y, int width, int height) {
        try {
            source.getSubimage(32, 32, 16, 16);
        } catch (RasterFormatException e) {
            return Assets.UNKNOWN_TEXTURE;
        }
        return source.getSubimage(x, y, width, height);
    }
}
