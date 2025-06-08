package github.PanheadGG.SuperMarioBros.model.entity.enemy;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.model.GameObject;
import github.PanheadGG.SuperMarioBros.model.entity.Entity;
import github.PanheadGG.SuperMarioBros.model.item.UpsideDownKoopa;

public class Koopa extends Enemy{
    public static class Status {
        public static final String WALK = "walk";
        public static final String STATIC_SHELL = "static_shell";
        public static final String MOVING_SHELL = "moving_shell";
    }

    public static class Action{
        public static final String LEFT = "left";
        public static final String RIGHT = "right";
        public static final String SHELL = "shell";
    }

    private String status= Status.WALK;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Koopa(int gameTickRate, int pixelPerUnit) {
        super(gameTickRate, pixelPerUnit);
        init();
    }

    private void init() {
        setTexture(Assets.getDynamicImageByKey("texture.enemy.koopa.walk"));
        setWidth(1);
        setHeight(1.5);
        move(Action.LEFT);
        status=Status.WALK;
    }
    @Override
    public void move(String action) {
        switch (action) {
            case Action.LEFT:
                horizontalStatus = Entity.Status.LEFT;
                reversed(false);
                velX = -2;
                if(status.equals(Status.STATIC_SHELL)||status.equals(Status.MOVING_SHELL)) {
                    status = Status.MOVING_SHELL;
                    velX = -8;
                }
                break;
            case Action.RIGHT:
                horizontalStatus = Entity.Status.RIGHT;
                reversed(true);
                velX = 2;
                if(status.equals(Status.STATIC_SHELL)||status.equals(Status.MOVING_SHELL)) {
                    status = Status.MOVING_SHELL;
                    velX = 8;
                }
                break;
            case Action.SHELL:
                horizontalStatus = Entity.Status.STOP;
                status = Status.STATIC_SHELL;
                setTexture(Assets.getImageByKey("texture.enemy.koopa.shell"));
                y +=1;
                setHeight((double) 14 /16);
                velX  = 0;
                break;
        }
    }

    @Override
    public GameObject getUpsideDownObj() {
        return new UpsideDownKoopa(gameTickRate,pixelPerUnit,x,y,velX);
    }
}
