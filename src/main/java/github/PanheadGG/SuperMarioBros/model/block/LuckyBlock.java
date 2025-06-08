package github.PanheadGG.SuperMarioBros.model.block;

import github.PanheadGG.SuperMarioBros.assets.Assets;

public class LuckyBlock extends Block implements Reward {
    private String[] rewards;
    private int rewardIndex = 0;
    private boolean hasReward;

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
                setTexture(Texture.AFTER);
            }
            return rewards[rewardIndex++];
        }
        return rewards[rewardIndex++];
    }

    public static class Texture {
        public static final String BEFORE = "before";
        public static final String AFTER = "after";
    }

    public LuckyBlock(int pixelPerUnit, double x, double y, int gameTickRate, String[] rewards) {
        super(pixelPerUnit);
        setPosition(x, y);
        originY = y;
        hasReward = true;
        this.gameTickRate = gameTickRate;
        this.rewards = rewards;
        init();
    }

    public LuckyBlock(int pixelPerUnit, double x, double y, int gameTickRate) {
        super(pixelPerUnit);
        setPosition(x, y);
        originY = y;
        hasReward = false;
        this.gameTickRate = gameTickRate;
        this.rewards = new String[0];
        init();
    }

    private void init() {
        setTexture(Assets.getDynamicImageByKey("texture.block.lucky.before"), gameTickRate);
        setWidth(1);
        setHeight(1);
    }

    public void setTexture(String texture) {
        if (Texture.BEFORE.equals(texture)) {
            setTexture(Assets.getDynamicImageByKey("texture.block.lucky.before"), gameTickRate);
        }
        if (Texture.AFTER.equals(texture)) {
            setTexture(Assets.getImageByKey("texture.block.lucky.after"));
        }
    }
}
