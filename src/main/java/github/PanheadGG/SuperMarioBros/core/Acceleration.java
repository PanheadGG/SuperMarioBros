package github.PanheadGG.SuperMarioBros.core;

import github.PanheadGG.SuperMarioBros.model.GameObject;

public class Acceleration {
    public void calcX(GameObject object) {
        object.setVelX(object.getVelX() + object.getHorizontalAcc());
    }
    public void calcY(GameObject object) {
        object.setVelY(object.getVelY() + object.getVerticalAcc());
    }
    public void calc(GameObject object){
        calcX(object);
        calcY(object);
    }
}
