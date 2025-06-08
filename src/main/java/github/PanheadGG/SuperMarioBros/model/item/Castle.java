package github.PanheadGG.SuperMarioBros.model.item;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.core.Camera;

import java.awt.*;

public class Castle extends Item{
    public Castle(int gameTickRate,int pixelPerUnit,int x,int y) {
        super(gameTickRate,pixelPerUnit);
        this.x = x;
        this.y = y;
        setTexture(Assets.getImageByKey("texture.item.castle"));
        System.out.println(Assets.getURLByKey("texture.item.castle"));
        setWidth(5);
        setHeight(5);
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(getCurrentTexture(),(int)((x-2)*pixelPerUnit),(int)((y-4)*pixelPerUnit),(int)(width*pixelPerUnit),(int)(height*pixelPerUnit),null);
    }

    @Override
    public void draw(Graphics g, Camera camera) {
        g.drawImage(getCurrentTexture(),(int)((x-2-camera.getX())*pixelPerUnit),(int)((y-4-camera.getY())*pixelPerUnit),(int)(width*pixelPerUnit),(int)(height*pixelPerUnit),null);
    }

    @Override
    public Rectangle getRectangle() {
        return new Rectangle(getPixelX()+pixelPerUnit/2, getPixelY()-pixelPerUnit, pixelWidth/2, 2*pixelHeight);
    }
}
