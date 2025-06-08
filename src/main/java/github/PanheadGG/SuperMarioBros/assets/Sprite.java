package github.PanheadGG.SuperMarioBros.assets;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Sprite {
    private int column=1;
    private URL url;
    private int fps=1;
    public Sprite(int column, URL url, int fps) {
        this.column = column;
        this.url = url;
        this.fps = fps;
    }

    public int getColumn() {
        return column;
    }

    public URL getUrl() {
        return url;
    }

    public int getFps() {
        return fps;
    }

    public DynamicImage getDynamicImage(){
        if(url==null) return new DynamicImage(Assets.UNKNOWN_TEXTURE);
        try {
//            System.out.println(url);
            BufferedImage image = ImageIO.read(url);
            int width = image.getWidth() / column;
            int height = image.getHeight();
            BufferedImage[] images = new BufferedImage[column];
            for (int i = 0; i < column; i++) {
                images[i] = image.getSubimage(i * width, 0, width, height);
            }
            return new DynamicImage(images, fps);
        } catch (IOException e) {
            return new DynamicImage(Assets.UNKNOWN_TEXTURE);
        }
    }
    public DynamicImage getDynamicImage(int tps){
        if(url==null) return new DynamicImage(Assets.UNKNOWN_TEXTURE);
        try {
//            System.out.println(url);
            BufferedImage image = ImageIO.read(url);
            int width = image.getWidth() / column;
            int height = image.getHeight();
            BufferedImage[] images = new BufferedImage[column];
            for (int i = 0; i < column; i++) {
                images[i] = image.getSubimage(i * width, 0, width, height);
            }
            return new DynamicImage(images,tps ,fps);
        } catch (IOException e) {
            return new DynamicImage(Assets.UNKNOWN_TEXTURE);
        }
    }
}
