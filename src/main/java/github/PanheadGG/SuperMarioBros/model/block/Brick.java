package github.PanheadGG.SuperMarioBros.model.block;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.assets.DynamicImage;

import java.awt.image.BufferedImage;

public class Brick extends Block implements Breakable, Reward {
    private String[] rewards;
    private int rewardIndex = 0;
    private boolean hasReward;
    private boolean canBeBroken;

    @Override
    public boolean hasRewards() {
        return hasReward;
    }

    @Override
    public String nextReward() {
        if (rewardIndex >= rewards.length) {
            canCrashed = false;
            return "";
        }
        if (rewardIndex == rewards.length - 1) {
            if (hasReward) {
                setRewardedTexture();
                setCanCrashed(false);
            }
            return rewards[rewardIndex++];
        }
        return rewards[rewardIndex++];
    }

    public Brick(int pixelPerUnit, int gameTickRate ,double x, double y) {
        super(gameTickRate, pixelPerUnit);
        canBeBroken = true;
        hasReward = false;
        originY = y;
        setPosition(x, y);
        init();
    }
    public Brick(int pixelPerUnit,int gameTickRate, double x, double y, String[] rewards) {
        super(gameTickRate,pixelPerUnit);
        canBeBroken = false;
        hasReward = true;
        originY = y;
        this.rewards = rewards;
        setPosition(x, y);
        init();
    }

    public Brick(int pixelPerUnit) {
        super(pixelPerUnit);
        init();
    }

    public Brick(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
        init();
    }

    public Brick(DynamicImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
        init();
    }

    public Brick(BufferedImage image, int gameTickRate, int pixelPerUnit) {
        super(image, gameTickRate, pixelPerUnit);
        init();
    }

    private void init() {
        setTexture(Assets.getImageByKey("texture.block.brick"));
        setWidth(1);
        setHeight(1);
    }

    public void setRewardedTexture() {
        setTexture(Assets.getImageByKey("texture.block.lucky.after"));
    }

    @Override
    public boolean canBeBroken() {
        return canBeBroken;
    }
}
