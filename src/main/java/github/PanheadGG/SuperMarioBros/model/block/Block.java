package github.PanheadGG.SuperMarioBros.model.block;

import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.model.GameObject;

import java.awt.image.BufferedImage;

public abstract class Block extends GameObject {
    protected double originY;
    protected boolean canCrashed = true;
    public boolean isCrashed = false;
    public boolean canCrashed(){
        return canCrashed;
    }
    public void setCanCrashed(boolean canCrashed){
        this.canCrashed = canCrashed;
    }
    public boolean notCrashed(){
        return !isCrashed;
    }
    public void crashed(){
        originY = y;
        isCrashed = true;
        velY = -5;
        verticalAcc = 50;
    }
    public boolean isCrashed(){
        return isCrashed;
    }

    public Block(int pixelPerUnit) {
        super(pixelPerUnit);
    }

    public Block(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
    }

    public Block(DynamicImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }

    public Block(BufferedImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
    }

    @Override
    public void update() {
        super.update();
        if(isCrashed&&y>originY){
            y = originY;
            isCrashed = false;
            velY = 0;
            verticalAcc = 0;
        }
//        System.err.println("x: "+x+" y: "+y+" originY: "+originY+" isCrashed: "+isCrashed+" velY: "+velY+" verticalAcc: "+verticalAcc);
    }
}
