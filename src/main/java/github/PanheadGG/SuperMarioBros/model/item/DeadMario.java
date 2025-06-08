package github.PanheadGG.SuperMarioBros.model.item;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.model.entity.player.Player;

public class DeadMario extends Item{
    private int timeout = 0;
    private DeadMario(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
        init();
    }
    public DeadMario(Player player) {
        super(player.getGameTickRate(), player.getPixelPerUnit());
        x = player.getX();
        y = player.getY();
        init();
    }
    private void init() {
        setTexture(Assets.getImageByKey("texture.mario.dead"));
        timeout = gameTickRate;
        setWidth(1);
        setHeight(1);
    }

    @Override
    public void update() {
        super.update();
        if(timeout >= 0){
            timeout--;
        }
        if(timeout == 0){
            velY = -15;
            verticalAcc = 40;
        }
    }
}
