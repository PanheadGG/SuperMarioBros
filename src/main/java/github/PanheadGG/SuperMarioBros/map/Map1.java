package github.PanheadGG.SuperMarioBros.map;

import github.PanheadGG.SuperMarioBros.core.GameEngine;
import github.PanheadGG.SuperMarioBros.model.GameObject;
import github.PanheadGG.SuperMarioBros.model.block.Block;
import github.PanheadGG.SuperMarioBros.model.block.Brick;
import github.PanheadGG.SuperMarioBros.model.entity.enemy.Mushroom;
import github.PanheadGG.SuperMarioBros.model.entity.Entity;
import github.PanheadGG.SuperMarioBros.model.entity.player.Mario;
import github.PanheadGG.SuperMarioBros.manager.SoundManager;

import java.awt.*;
import java.util.TreeSet;

public class Map1 extends GameMap {
    private static GameMap instance;

    public static GameMap getInstance(GameEngine engine) {
        if (instance == null) {
            instance = new Map1(engine);
        }
        return instance;
    }

    private Mario mario;
    private Mushroom mushroom;
    private TreeSet<Entity> group = new TreeSet<>();
    private TreeSet<Block> brickGroup = new TreeSet<>();
    //    private ArrayList<GameObject> objects = new ArrayList<>();
    private TreeSet<GameObject> objects = new TreeSet<>();

    public Map1(GameEngine engine) {
        super(engine);
        pixelPerUnit = engine.getPixelPerUnit();
        mario = new Mario(gameTickRate, pixelPerUnit);
        mushroom = new Mushroom(gameTickRate, pixelPerUnit);
        mario.setPosition(2, 2);
        mushroom.setPosition(4, 2);
        group.add(mushroom);
        group.add(mario);
        brickGroup.add(new Brick(pixelPerUnit, 1, 13));
        brickGroup.add(new Brick(pixelPerUnit, 1, 14));
        brickGroup.add(new Brick(pixelPerUnit, 3, 14));
        brickGroup.add(new Brick(pixelPerUnit, 5, 14));
        brickGroup.add(new Brick(pixelPerUnit, 6, 14));
        brickGroup.add(new Brick(pixelPerUnit, 6, 13));
        brickGroup.add(new Brick(pixelPerUnit, 2, 14));
        brickGroup.add(new Brick(pixelPerUnit, 4, 14));
        brickGroup.add(new Brick(pixelPerUnit, 6, 13));
        brickGroup.add(new Brick(pixelPerUnit, 6, 12));
        brickGroup.add(new Brick(pixelPerUnit, 6, 11));
        brickGroup.add(new Brick(pixelPerUnit, 6, 10));
        brickGroup.add(new Brick(pixelPerUnit, 7, 14));
        brickGroup.add(new Brick(pixelPerUnit, 8, 14));
        brickGroup.add(new Brick(pixelPerUnit, 9, 14));
        brickGroup.add(new Brick(pixelPerUnit, 10, 14));
        brickGroup.add(new Brick(pixelPerUnit, 11, 14));
        brickGroup.add(new Brick(pixelPerUnit, 11, 13));
        brickGroup.add(new Brick(pixelPerUnit, 11, 12));
        brickGroup.add(new Brick(pixelPerUnit, 11, 11));
        brickGroup.add(new Brick(pixelPerUnit, 11, 10));
        brickGroup.add(new Brick(pixelPerUnit, 11, 9));
        brickGroup.add(new Brick(pixelPerUnit, 11, 8));
        brickGroup.add(new Brick(pixelPerUnit, 11, 7));
        brickGroup.add(new Brick(pixelPerUnit, 11, 6));
        brickGroup.add(new Brick(pixelPerUnit, 11, 5));
        brickGroup.add(new Brick(pixelPerUnit, 11, 4));

        objects.addAll(group);
        objects.addAll(brickGroup);

        for (GameObject object : objects) {
            System.out.printf("%s on (%f,%f)\n", object.getClass().getSimpleName(), object.getX(), object.getY());
        }

        SoundManager.playBackground();
    }

    @Override
    public void update() {
        gameLoop();
    }

    @Override
    public void drawFrame(Graphics g) {
        for (GameObject object : objects) {
            object.draw(g);
        }
    }

    @Override
    public void afterCreateUI() {
        super.afterCreateUI();
        engine.addKeyListener(key);
    }

    public void gameLoop() {
        objects.removeIf(GameObject::isNeedClear);
        group.removeIf(GameObject::isNeedClear);
        brickGroup.removeIf(GameObject::isNeedClear);

        for (Entity entity : group) {
            entity.update();
        }

        if (key.isUpKeyPressed()) {
            mario.move(Mario.Action.JUMP);
        }

        if (key.isLeftKeyPressed() && !key.isRightKeyPressed()) {
            mario.move(Mario.Action.LEFT);
        }

        if (key.isRightKeyPressed() && !key.isLeftKeyPressed()) {
            mario.move(Mario.Action.RIGHT);
        }

        if (mario.getHorizontalStatus() == Entity.Status.STOP) {
            if (mario.isJumping()) mario.setTexture(Mario.Texture.JUMP);
            else mario.setTexture(Mario.Texture.STAND);
        }


        if (key.isLeftKeyPressed() == key.isRightKeyPressed()) {
            mario.move(Mario.Action.STOP);
        }

        for (Entity entity : group) {
            if (entity == mushroom && mushroom.isDead()) mushroom.clearTime();
            boolean onFloor = false;
            if (entity == mushroom && !mushroom.isDead()) {
                if (mushroom.aboveIs(mario)) {
                    mushroom.setDead(true);
                    mushroom.move(Mushroom.Action.SQUASHED);
                    mario.bounds();
                    SoundManager.playStomp();
                }
            }
            for (Block block : brickGroup) {
                if (entity == mushroom && !mushroom.isDead()) {
                    if (mushroom.leftIs(block)) {
                        mushroom.move(Mushroom.Action.RIGHT);
                    }
                    if (mushroom.rightIs(block)) {
                        mushroom.move(Mushroom.Action.LEFT);
                    }
                }
                if (entity.aboveIs(block) && entity.isJumping()) {
                    entity.setFalling(true);
                    entity.setJumping(false);
                    entity.setJumpedHighest(true);
//                    entity.setPositionByPixelY(block.getPixelY()+ block.getPixelHeight());
                    entity.setVelY(-entity.getVelY() / 4);
                }
                if (entity.leftIs(block) && entity.getHorizontalStatus() == Entity.Status.LEFT) {
//                    entity.setPositionByPixel(block.getRectangle().x + entity.getPixelWidth(), entity.getPixelY());
                    entity.setPositionByPixel(entity.getPixelX()+1,entity.getPixelY());
//                    entity.setVelX(-entity.getVelX() / 4);
                    entity.setVelX(0);
                    break;
                }
                if (entity.rightIs(block) && entity.getHorizontalStatus() == Entity.Status.RIGHT) {
                    if(entity==mario) {
                        System.out.println(entity.getPixelWidth());
                        System.out.printf("x:%d,y:%d,width:%d,height:%d\n", entity.getRectangle().x, entity.getRectangle().y, entity.getRectangle().width, entity.getRectangle().height);
                    }
//                    entity.setPositionByPixel(block.getRectangle().x - block.getRectangle().pixelWidth-(entity.getPixelX()-entity.getRectangle().x), entity.getPixelY());
                    entity.setPositionByPixel(block.getRectangle().x - entity.getPixelWidth(), entity.getPixelY());
                    entity.setPositionByPixelX(entity.getPixelX()-1);
//                    entity.setVelX(-entity.getVelX() / 4);
                    entity.setVelX(0);
                    break;
                }
                if (entity.isJumping() && entity.getVelY() > 0) entity.setJumpedHighest(true);
                if (entity.on(block) && entity.isJumpedHighest()) {
                    onFloor = true;
                    entity.setVelY(0);
                    entity.setFalling(false);
                    entity.setJumping(false);
                    entity.setJumpedHighest(false);
                    if (entity.getHorizontalStatus() != Entity.Status.STOP) {
                        if (entity == mario) {
                            mario.setTexture(Mario.Texture.WALK);
                        }
                    }
                    entity.setPositionByPixel(entity.getPixelX(), block.getPixelY() - block.getPixelHeight());
                    break;
                }

            }


            if (entity.getPixelX() <= -1) {
                entity.setPositionByPixel(0, entity.getPixelY());
                entity.setVelX(-entity.getVelX() / 4);
            }

            if (entity.getPixelX() >= ui.getWidth() - entity.getPixelWidth() + 1) {
                entity.setPositionByPixel(ui.getWidth() - entity.getPixelWidth(), entity.getPixelY());
                entity.setVelX(-entity.getVelX() / 4);
            }


            if (entity.getPixelY() < ui.getHeight() - entity.getPixelHeight() && !onFloor) {
                entity.setVerticalAcc(50);
                entity.setFalling(true);
                entity.setJumpedHighest(true);
            }
            if (entity.getPixelY() >= ui.getHeight() - entity.getPixelHeight() + 1) {
                entity.setVerticalAcc(0);
                entity.setVelY(0);
                entity.setFalling(false);
                entity.setJumping(false);
                entity.setJumpedHighest(false);
                if (entity.getHorizontalStatus() != Entity.Status.STOP) {
                    if (entity == mario) {
                        mario.setTexture(Mario.Texture.WALK);
                    }
                }
                entity.setPositionByPixel(entity.getPixelX(), ui.getHeight() - entity.getPixelHeight());
            }
        }

//        if (mario.collided(mushroom)) System.out.println("Collided");


//        System.out.println(mario.getVelY());
    }
}
