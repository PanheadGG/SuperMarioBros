package github.PanheadGG.SuperMarioBros.map;

import github.PanheadGG.SuperMarioBros.core.GameEngine;
import github.PanheadGG.SuperMarioBros.manager.SoundManager;
import github.PanheadGG.SuperMarioBros.model.GameObject;
import github.PanheadGG.SuperMarioBros.model.block.Brick;
import github.PanheadGG.SuperMarioBros.model.entity.enemy.Mushroom;
import github.PanheadGG.SuperMarioBros.model.entity.player.Mario;

public class Map2 extends GroundMap{
    public Map2(GameEngine engine) {
        super(engine);


        Mario mario = new Mario(gameTickRate, pixelPerUnit);
        Mushroom mushroom = new Mushroom(gameTickRate, pixelPerUnit);
        mario.setPosition(1, 2);
        mushroom.setPosition(4, 13-1);

        add(mushroom);
        setPlayer(mario);
        add(new Brick(pixelPerUnit, 1, 13-1));
        add(new Brick(pixelPerUnit, 1, 14-1));
        add(new Brick(pixelPerUnit, 3, 14-1));
        add(new Brick(pixelPerUnit, 5, 14-1));
        add(new Brick(pixelPerUnit, 6, 14-1));
        add(new Brick(pixelPerUnit, 6, 13-1));
        add(new Brick(pixelPerUnit, 2, 14-1));
        add(new Brick(pixelPerUnit, 4, 14-1));
        add(new Brick(pixelPerUnit, 6, 13-1));
        add(new Brick(pixelPerUnit, 6, 12-1));
        add(new Brick(pixelPerUnit, 6, 11-1));
        add(new Brick(pixelPerUnit, 6, 10-1));
        add(new Brick(pixelPerUnit, 7, 14-1));
        add(new Brick(pixelPerUnit, 8, 14-1));
        add(new Brick(pixelPerUnit, 9, 14-1));
        add(new Brick(pixelPerUnit, 10, 14-1));
        add(new Brick(pixelPerUnit, 11, 14-1));
        add(new Brick(pixelPerUnit, 11, 13-1));
        add(new Brick(pixelPerUnit, 11, 12-1));
        add(new Brick(pixelPerUnit, 11, 11-1));
        add(new Brick(pixelPerUnit, 11, 10-1));
        for(int i=12;i<40;i++){
            add(new Brick(pixelPerUnit, i, 14-1));
        }


        SoundManager.playBackground();
//        System.out.println(player.isFalling());
    }
}
