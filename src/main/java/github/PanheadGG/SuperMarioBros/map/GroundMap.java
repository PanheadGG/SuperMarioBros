package github.PanheadGG.SuperMarioBros.map;

import github.PanheadGG.SuperMarioBros.assets.Asset;
import github.PanheadGG.SuperMarioBros.core.GameEngine;
import github.PanheadGG.SuperMarioBros.model.Air;
import github.PanheadGG.SuperMarioBros.model.GameObject;
import github.PanheadGG.SuperMarioBros.model.block.Block;
import github.PanheadGG.SuperMarioBros.model.entity.Entity;
import github.PanheadGG.SuperMarioBros.model.entity.enemy.Enemy;
import github.PanheadGG.SuperMarioBros.model.entity.enemy.Mushroom;
import github.PanheadGG.SuperMarioBros.model.entity.player.Mario;
import github.PanheadGG.SuperMarioBros.model.entity.player.Player;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.TreeSet;

public class GroundMap extends GameMap {

    protected Player player;
    protected TreeSet<GameObject> objects = new TreeSet<>();
    protected TreeSet<Block> blocks = new TreeSet<>();
    protected TreeSet<Enemy> enemies = new TreeSet<>();
    protected TreeSet<Entity> entities = new TreeSet<>();
    protected TreeSet<GameObject> renderObjects = new TreeSet<>();
    protected TreeSet<Air> backgrounds = new TreeSet<>();

    protected BufferedImage background = Asset.getImage(Asset.MAP_1_BG);

    private Iterator<GameObject> iterator = null;
    private GameObject toRenderObject = null;

    public GroundMap(GameEngine engine) {
        super(engine);
    }

    public void add(GameObject object) {
        objects.add(object);
    }

    private void addRender(GameObject object) {
        if (object instanceof Enemy) {
            enemies.add((Enemy) object);
            entities.add((Entity) object);
        }
        if (object instanceof Block) {
            blocks.add((Block) object);
        }

        if (renderObjects.contains(object)) return;
        renderObjects.add(object);

        System.out.printf("Set %s on (%f,%f)\n", object.getClass().getSimpleName(), object.getX(), object.getY());
    }

    public void setPlayer(Player player) {
        this.player = player;
        entities.add(player);
        addRender(player);
    }

    @Override
    public void afterCreateUI() {
        super.afterCreateUI();
        engine.addKeyListener(key);
    }

    @Override
    public void update() {
        objectHandle();
        gameLoop();
        cameraUpdate();
        broadHandle();
    }

    //等玩家靠近了才开始渲染
    private void objectHandle() {
        if (iterator == null) {
            iterator = objects.iterator();
        }
        if (toRenderObject == null && iterator.hasNext()) {
            toRenderObject = iterator.next();
        }
        if (toRenderObject.getX() - (camera == null ? 0 : camera.getX()) - mapWidth <= 2) {
            addRender(toRenderObject);
            while (iterator.hasNext()) {
                GameObject object = iterator.next();
                toRenderObject = object;
//                System.out.println(toRenderObject);
                if (object.getX() - (camera == null ? 0 : camera.getX()) - mapWidth <= 2) {
                    addRender(object);
                } else {
                    break;
                }
            }
        }
        for (GameObject object : enemies) {
            if (!object.checkClear(camera)) break;
        }
        for (GameObject object : entities) {
            if (!object.checkClear(camera)) break;
        }
        for (GameObject object : blocks) {
            if (!object.checkClear(camera)) break;
        }
        for (GameObject object : renderObjects) {
            if (!object.checkClear(camera)) break;
        }
        enemies.removeIf(GameObject::isNeedClear);
        entities.removeIf(GameObject::isNeedClear);
        blocks.removeIf(GameObject::isNeedClear);
        renderObjects.removeIf(GameObject::isNeedClear);
    }

    private void broadHandle() {
        if (camera == null) {
            if (player.getPixelX() < 0 && player.getHorizontalStatus() == Entity.Status.LEFT) {
                player.setPositionByPixelX(0);
                player.setVelX(0);
            }
        } else {
            if (player.getPixelX() - camera.getPixelX() < 0 && player.getHorizontalStatus() == Entity.Status.LEFT) {
                player.setPositionByPixelX(camera.getPixelX());
                player.setVelX(0);
            }
        }
    }

    private void cameraUpdate() {
        if (camera != null) {
            if ((player.getX() - camera.getX()) / mapWidth >= 0.5) {
                if (player.getVelX() <= 0) {
                    camera.moveX((double) 1 / pixelPerUnit);
                } else {
                    camera.moveX(player.getVelX() / pixelPerUnit);
                }
            }
        }
    }

    private void gameLoop() {
        for (Entity entity : entities) {
            //处理腾空情况
            if (entity.getVelY() + entity.getHeight() >= 0) entity.setFalling(true);
            boolean leftOb = false;//左边是否有障碍物
            boolean rightOb = false;//右边是否有障碍物
            boolean aboveOb = false;//上方是否有障碍物
            boolean underOb = false;//下方是否有障碍物
            for (Block block : blocks) {
                if (!entity.on(block) && entity.isFalling()) {
                    entity.setVerticalAcc(50);
                }
                //处理跳到最高点
                if (entity.getVelY() > 0) {
                    entity.setFalling(true);
                    entity.setJumpedHighest(true);
                }
                //检测下方方块
                if (entity.on(block) && entity.isFalling() && entity.isJumpedHighest()) {
                    entity.setPositionByPixelY(block.getRectangle().y - entity.getRectangle().height);
                    entity.setVelY(0);
                    entity.setVerticalAcc(0);
                    entity.setJumpedHighest(false);
                    entity.setFalling(false);
                    entity.setJumping(false);
                    if (entity instanceof Mario) ((Mario) player).setTexture(Mario.Texture.WALK);
                    underOb = true;
                }
                //检测上方方块
                if (entity.aboveIs(block) && entity.isJumping()) {
                    entity.setVelY(-entity.getVelY() / 4);
                    entity.setJumpedHighest(true);
                    entity.setFalling(true);
                    entity.setJumping(true);
                    aboveOb = true;
                }
                //检测左边方块
                if (entity.leftIs(block) && entity.getHorizontalStatus() == Entity.Status.LEFT) {
                    entity.setPositionByPixelX(block.getRectangle().x + block.getRectangle().width + 1);
                    entity.setVelX(0);
                    leftOb = true;
                }
                //检测右边方块
                if (entity.rightIs(block) && entity.getHorizontalStatus() == Entity.Status.RIGHT) {
                    entity.setPositionByPixelX(block.getRectangle().x - entity.getRectangle().width);
                    entity.setVelX(0);
                    rightOb = true;
                }
            }
            //处理上下移动
            if (entity.isFalling() && !underOb) {
                entity.updatePositionY();
            }
            //处理上下移动
            if (entity.isJumping() && !aboveOb) {
                entity.updatePositionY();
            }

            if (entity instanceof Enemy) {
                if (leftOb && entity.getHorizontalStatus() == Entity.Status.LEFT)
                    ((Enemy) entity).move(Enemy.Action.RIGHT);
                if (rightOb && entity.getHorizontalStatus() == Entity.Status.RIGHT)
                    ((Enemy) entity).move(Enemy.Action.LEFT);
            }

            //处理Enemies
            for (Enemy enemy : enemies) {
                if (entity instanceof Enemy) {
                    if (entity.leftIs(enemy)) {
                        ((Enemy) entity).move(Enemy.Action.RIGHT);
                        break;
                    }
                    if (entity.rightIs(enemy)) {
                        ((Enemy) entity).move(Enemy.Action.LEFT);
                        break;
                    }
                }
                if (entity instanceof Player) {
                    if (entity.on(enemy)) {
                        if (enemy instanceof Mushroom && !enemy.isDead()) {
                            if (entity.crashed(enemy, GameObject.Position.TOP, 0.1)) {
                                enemy.setDead(true);
                                enemy.move(Enemy.Action.SQUASHED);
                                if (entity instanceof Mario) ((Mario) player).bounds();
                            }
                        }
                    }
                    if (entity.aboveIs(enemy)) {
                        if (enemy instanceof Mushroom && !enemy.isDead()) {
                            if (entity.crashed(enemy, GameObject.Position.BOTTOM, 0.25)) {
//                                System.out.println("Crashed Top");
                            }
                        }
                    }
                    if (entity.leftIs(enemy)) {
                        if (enemy instanceof Mushroom && !enemy.isDead()) {
                            if (entity.crashed(enemy, GameObject.Position.LEFT, 0.25)) {
//                                System.out.println("Crashed Left");
                            }
                        }
                    }
                    if (entity.rightIs(enemy) && !enemy.isDead()) {
                        if (enemy instanceof Mushroom) {
                            if (entity.crashed(enemy, GameObject.Position.RIGHT, 0.25)) {
//                                System.out.println("Crashed Right");
                            }
                        }
                    }
                }
            }
            if (entity instanceof Enemy && ((Enemy) entity).isDead()) ((Enemy) entity).clearTime();
            entity.updatePositionX();
        }

        enemies.removeIf(GameObject::isNeedClear);
        entities.removeIf(GameObject::isNeedClear);
        blocks.removeIf(GameObject::isNeedClear);
        renderObjects.removeIf(GameObject::isNeedClear);

        if (player instanceof Mario) {
            Mario mario = (Mario) player;
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
        }
        for (GameObject object : renderObjects) object.updateTexture();
    }

    @Override
    public void drawFrame(Graphics g) {
        TreeSet<GameObject> render = new TreeSet<>(renderObjects);
        g.setColor(backgroundColor);
        g.fillRect(0, 0, ui.getWidth(), ui.getHeight());

        if (camera == null) {
//            g.drawImage(background,0,0,212*pixelPerUnit,14*pixelPerUnit,null);
            for (GameObject object : render) {
                object.draw(g);
            }
        } else {
//            g.drawImage(background,-camera.getPixelX(),-camera.getPixelY(),212*pixelPerUnit,14*pixelPerUnit,null);
            for (GameObject object : render) {
                object.draw(g, camera);
            }
        }
    }
}
