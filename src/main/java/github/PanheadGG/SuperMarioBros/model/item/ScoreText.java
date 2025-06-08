package github.PanheadGG.SuperMarioBros.model.item;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.core.Camera;

import java.awt.*;

public class ScoreText extends Item{
    private String text = "";
    private int clearTime = 1;
    public ScoreText(String text,int gameTickRate, int pixelPerUnit,double x,double y) {
        super(gameTickRate, pixelPerUnit);
        this.text = text;
        setPosition(x,y);
        clearTime = gameTickRate/2;
    }

    public ScoreText(int score,int gameTickRate, int pixelPerUnit,double x,double y) {
        super(gameTickRate, pixelPerUnit);
        this.text = String.valueOf(score);
        setPosition(x,y);
        clearTime = gameTickRate/2;
    }

    @Override
    public void update() {
        super.update();
        velY = -1;
        verticalAcc = 0;
        clearTime--;
        if (clearTime <= 0) {
            needClear = true;
        }
    }

    @Override
    public void draw(Graphics g, Camera camera) {
        g.setFont(Assets.FONT);
        g.drawString(text, getPixelX() - camera.getPixelX(), getPixelY() - camera.getPixelY());
    }

    @Override
    public void draw(Graphics g) {
        g.setFont(Assets.FONT);
        g.drawString(text, getPixelX(), getPixelY());
    }
}
