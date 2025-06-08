package github.PanheadGG.SuperMarioBros.map;

import github.PanheadGG.SuperMarioBros.core.scene.GameEngine;
import github.PanheadGG.SuperMarioBros.manager.SoundManager;
import github.PanheadGG.SuperMarioBros.model.GameObject;
import github.PanheadGG.SuperMarioBros.model.block.Block;
import github.PanheadGG.SuperMarioBros.model.block.Breakable;
import github.PanheadGG.SuperMarioBros.model.block.Brick;
import github.PanheadGG.SuperMarioBros.model.block.Reward;
import github.PanheadGG.SuperMarioBros.model.entity.Entity;
import github.PanheadGG.SuperMarioBros.model.entity.enemy.Enemy;
import github.PanheadGG.SuperMarioBros.model.entity.enemy.Goomba;
import github.PanheadGG.SuperMarioBros.model.entity.enemy.Koopa;
import github.PanheadGG.SuperMarioBros.model.entity.player.FireMario;
import github.PanheadGG.SuperMarioBros.model.entity.player.Mario;
import github.PanheadGG.SuperMarioBros.model.entity.player.Player;
import github.PanheadGG.SuperMarioBros.model.entity.player.SuperMario;
import github.PanheadGG.SuperMarioBros.model.entity.prop.*;
import github.PanheadGG.SuperMarioBros.model.item.*;

import java.awt.*;
import java.util.Iterator;
import java.util.Objects;
import java.util.TreeSet;

public class GroundMap extends GameMap {

    protected Player player;
    protected TreeSet<GameObject> objects = new TreeSet<>();
    protected TreeSet<Block> blocks = new TreeSet<>();
    protected TreeSet<Enemy> enemies = new TreeSet<>();
    protected TreeSet<Entity> entities = new TreeSet<>();
    protected TreeSet<Item> items = new TreeSet<>();
    protected TreeSet<Block> bounceBlock = new TreeSet<>();
    protected final TreeSet<GameObject> renderObjects = new TreeSet<>();
    protected TreeSet<Prop> props = new TreeSet<>();
    protected TreeSet<Entity> entitiesToAdd = new TreeSet<>();
    protected Castle castle = null;

    private Iterator<GameObject> iterator = null;
    private GameObject toRenderObject = null;
    private int timeTickRemain = 0;
    private int fireCD = 0;

    private boolean readyToChangePlayer = false;

    public GroundMap(GameEngine engine) {
        super(engine);
    }

    public void add(GameObject object) {
        objects.add(object);
    }

    private void addRender(GameObject object) {
        synchronized (renderObjects) {
            if (object instanceof Enemy) {
                enemies.add((Enemy) object);
                entities.add((Entity) object);
            }
            if (object instanceof Block) {
                blocks.add((Block) object);
            }

            if (object instanceof Item) {
                items.add((Item) object);
            }

            if (object instanceof Prop) {
                props.add((Prop) object);
                entities.add((Entity) object);
            }

            if(object instanceof Castle){
                castle = (Castle) object;
            }

            if (renderObjects.contains(object)) return;
            renderObjects.add(object);


            System.out.printf("Set %s on (%.1f,%.1f)\n", object.getClass().getSimpleName(), object.getX(), object.getY());
        }
    }

    private void addScore(int score) {
        engine.addScore(score);
    }

    public void setPlayer(Player player) {
        if (this.player != null) this.player.setNeedClear(true);
        this.player = player;
        entities.add(player);
        addRender(player);
    }

    @Override
    public void update() {
        objectHandle();

        if (engine.getGameState().equals(GameEngine.GameState.GAME_RUNNING)) gameLoop();
        else if (engine.getGameState().equals(GameEngine.GameState.MARIO_LEVEL_UP)) doLevelUp();
        else if (engine.getGameState().equals(GameEngine.GameState.GAME_DYING)) items.forEach(GameObject::update);

        cameraUpdate();
        broadHandle();
    }

    private void doLevelUp() {
        if (timeTickRemain > 0) {
            timeTickRemain--;
        } else {
            engine.setGameState(GameEngine.GameState.GAME_RUNNING);
        }
    }

    //等玩家靠近了才开始渲染
    private void objectHandle() {
        if (iterator == null) {
            iterator = objects.iterator();
        }
        if (toRenderObject == null && iterator.hasNext()) {
            toRenderObject = iterator.next();
        }
        if (!iterator.hasNext()) return;
        if (toRenderObject.getX() - (camera == null ? 0 : camera.getX()) - mapWidth <= 4) {
            addRender(toRenderObject);
            while (iterator.hasNext()) {
                GameObject object = iterator.next();
                toRenderObject = object;
//                System.out.println(toRenderObject);
                if (object.getX() - (camera == null ? 0 : camera.getX()) - mapWidth <= 4) {
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
//        enemies.removeIf(GameObject::isNeedClear);
//        entities.removeIf(GameObject::isNeedClear);
//        blocks.removeIf(GameObject::isNeedClear);
//        bounceBlock.removeIf(Block::notCrashed);
//        renderObjects.removeIf(GameObject::isNeedClear);
        synchronized (renderObjects) {
            renderObjects.removeIf(GameObject::isNeedClear);
            enemies.removeIf(GameObject::isNeedClear);
            entities.removeIf(GameObject::isNeedClear);
            blocks.removeIf(GameObject::isNeedClear);
            items.removeIf(GameObject::isNeedClear);
            props.removeIf(GameObject::isNeedClear);
        }
    }

    private void broadHandle() {
        if (camera == null) {
            if (player.getPixelX() < 0 && Objects.equals(player.getHorizontalStatus(), Entity.Status.LEFT)) {
                player.setPositionByPixelX(0);
                player.setVelX(0);
            }
        } else {
            if (player.getPixelX() - camera.getPixelX() < 0 && Objects.equals(player.getHorizontalStatus(), Entity.Status.LEFT)) {
                player.setPositionByPixelX(camera.getPixelX());
                player.setVelX(0);
            }
        }
        if(player.getY()>=14&&!player.isNeedClear()){
            playerDie();
        }
    }

    private void cameraUpdate() {
        if (camera != null) {
//            if(mapWidth-player.getX() <= 15) return;
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
        if(castle!=null&&player.collided(castle)) {
            engine.setGameState(GameEngine.GameState.GAME_WIN);
            SoundManager.playBackground(SoundManager.BackgroundMusic.STOP);
            SoundManager.playStageClear();
            SoundManager.playCountDown();
        }

        for (Entity entity : entitiesToAdd) {
            addRender(entity);
        }
        entitiesToAdd.clear();
        if (readyToChangePlayer) changePlayer();
        player.update();
        synchronized (renderObjects) {
            renderObjects.removeIf(GameObject::isNeedClear);
            enemies.removeIf(GameObject::isNeedClear);
            entities.removeIf(GameObject::isNeedClear);
            blocks.removeIf(GameObject::isNeedClear);
            items.removeIf(GameObject::isNeedClear);
            props.removeIf(GameObject::isNeedClear);
        }

        if (player.isStarTime() && engine.getStarManTime() <= 0) player.setStarTime(false);

        for (Entity entity : entities) {
            //处理腾空情况
            if (entity.getVelY() + entity.getHeight() >= 0) entity.setFalling(true);
            if (entity instanceof Star) entity.setFalling(true);
            boolean leftOb = false;//左边是否有障碍物
            boolean rightOb = false;//右边是否有障碍物
            boolean aboveOb = false;//上方是否有障碍物
            boolean underOb = false;//下方是否有障碍物
            for (Block block : blocks) {
                if (!entity.on(block) && entity.isFalling()) {
                    entity.setVerticalAcc(25);
                }
                //处理跳到最高点
                if (entity.getVelY() > 0 && !entity.isJumpedHighest()) {
                    entity.setFalling(true);
                    entity.setJumpedHighest(true);
                }
                //检测上方方块
                if (entity.aboveIs(block) && entity.isJumping()) {
                    if (block instanceof Reward) {
                        Reward rewardBlock = (Reward) block;
                        if (rewardBlock.hasRewards() && !entity.isJumpedHighest()) {
                            String reward = rewardBlock.nextReward();
                            if ("coin".equals(reward)) {
                                Coin coin = new Coin(gameTickRate, pixelPerUnit, block.getX(), block.getY());
                                addRender(coin);
                                addScore(200);
                                addCoin(1);
                            }
                            if ("super_mushroom_or_flower".equals(reward)) {
                                Prop prop;
                                if (player instanceof Mario) prop = new SuperMushroom(gameTickRate, pixelPerUnit);
                                else prop = new FireFlower(gameTickRate, pixelPerUnit);
                                prop.setPosition(block.getX(), block.getY() - 1);
                                entitiesToAdd.add(prop);
                                SoundManager.playPropAppears();
                            }
                            if ("star".equals(reward)) {
                                Prop prop = new Star(gameTickRate, pixelPerUnit);
                                prop.setPosition(block.getX(), block.getY() - 1);
                                entitiesToAdd.add(prop);
                                SoundManager.playPropAppears();
                            }
                        }
                    }
                    if (block instanceof Breakable && ((Breakable) block).canBeBroken()) {
                        if (block instanceof Brick) {
                            if (player instanceof SuperMario || player instanceof FireMario) {
                                Brick brick = (Brick) block;
                                addRender(new BrokenBrick(gameTickRate, pixelPerUnit, brick.getX(), brick.getY(), BrokenBrick.Direction.TOP_LEFT));
                                addRender(new BrokenBrick(gameTickRate, pixelPerUnit, brick.getX() + 0.5, brick.getY(), BrokenBrick.Direction.TOP_RIGHT));
                                addRender(new BrokenBrick(gameTickRate, pixelPerUnit, brick.getX(), brick.getY() + 0.5, BrokenBrick.Direction.BOTTOM_LEFT));
                                addRender(new BrokenBrick(gameTickRate, pixelPerUnit, brick.getX() + 0.5, brick.getY() + 0.5, BrokenBrick.Direction.BOTTOM_RIGHT));
                                SoundManager.playBrickSmash();
                                addScore(50);
                                brick.crashed();
                                brick.setNeedClear(true);
                            }
                        }
                    }
                    if (block.canCrashed() && !block.isCrashed()) {
                        block.crashed();
                        bounceBlock.add(block);
                    }
                    entity.setVelY(-entity.getVelY() / 4);
                    entity.setJumpedHighest(true);
                    entity.setFalling(true);
                    entity.setJumping(true);
                    aboveOb = true;
                }
                //检测下方方块
                if (entity.on(block) && entity.isFalling() && entity.isJumpedHighest()) {
                    entity.setPositionByPixelY(block.getRectangle().y - entity.getRectangle().height);
                    entity.setVelY(0);
                    entity.setVerticalAcc(0);
                    entity.setJumpedHighest(false);
                    entity.setFalling(false);
                    entity.setJumping(false);
                    underOb = true;
                    if (entity instanceof FireBall) {
                        ((FireBall) entity).bounce();
                    }
                    if (entity instanceof Star && !((Star) entity).isPreAnimation()) {
                        ((Star) entity).bounce();
                    }
                    if (entity instanceof Goomba && block.isCrashed()) {
                        Goomba goomba = (Goomba) entity;
                        UpsideDownGoomba upsideDownGoomba = new UpsideDownGoomba(gameTickRate, pixelPerUnit, goomba.getX(), goomba.getY(), goomba.getVelX());
                        goomba.setDead(true);
                        goomba.setTexture(Goomba.Texture.UPSIDE_DOWN);
                        goomba.setNeedClear(true);
                        addRender(upsideDownGoomba);
                        addRender(new ScoreText(100, gameTickRate, pixelPerUnit, entity.getX(), entity.getY()));
                        addScore(100);
                    }
                    if (block.isCrashed()) {
                        if (entity.getHorizontalStatus().equals(Entity.Status.LEFT))
                            entity.move(Entity.Status.RIGHT);
                        if (entity.getHorizontalStatus().equals(Entity.Status.RIGHT))
                            entity.move(Entity.Status.LEFT);
                    }
                }
                //检测左边方块
                if (entity.leftIs(block) && Objects.equals(entity.getHorizontalStatus(), Entity.Status.LEFT) && !block.isCrashed()) {
                    if (entity instanceof FireBall && !entity.isNeedClear()) {
                        addRender(new Boom(gameTickRate, pixelPerUnit, entity.getX(), entity.getY()));
                        entity.setNeedClear(true);
                        SoundManager.playBump();
                    }
                    entity.setPositionByPixelX(block.getRectangle().x + block.getRectangle().width + 1);
                    entity.setVelX(0);
                    leftOb = true;
                }
                //检测右边方块
                if (entity.rightIs(block) && Objects.equals(entity.getHorizontalStatus(), Entity.Status.RIGHT) && !block.isCrashed()) {
                    if (entity instanceof FireBall && !entity.isNeedClear()) {
                        addRender(new Boom(gameTickRate, pixelPerUnit, entity.getX(), entity.getY()));
                        entity.setNeedClear(true);
                        SoundManager.playBump();
                    }
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

            if (leftOb && Objects.equals(entity.getHorizontalStatus(), Entity.Status.LEFT))
                entity.move(Entity.Action.RIGHT);
            if (rightOb && Objects.equals(entity.getHorizontalStatus(), Entity.Status.RIGHT))
                entity.move(Entity.Action.LEFT);

            //处理Enemies
            for (Enemy enemy : enemies) {
                if (enemy == entity) continue;
                if (entity instanceof Enemy) {
                    if (entity.leftIs(enemy)) {
//                        enemy.move(Enemy.Action.LEFT);
                        if (entity instanceof Koopa && ((Koopa) entity).getStatus().equals(Koopa.Status.MOVING_SHELL)) {
                            if (enemy instanceof Goomba) {
                                enemy.setDead(true);
                                enemy.setNeedClear(true);
                                addRender(enemy.getUpsideDownObj());
                                addRender(new ScoreText(100, gameTickRate, pixelPerUnit, enemy.getX(), enemy.getY()));
                                addScore(100);
                                SoundManager.playStomp();
                            }
                            break;
                        }
                        entity.move(Enemy.Action.RIGHT);
                        break;
                    }
                    if (entity.rightIs(enemy)) {
                        if (entity instanceof Koopa && ((Koopa) entity).getStatus().equals(Koopa.Status.MOVING_SHELL)) {
                            if (enemy instanceof Goomba) {
                                enemy.setDead(true);
                                enemy.setNeedClear(true);
                                addRender(enemy.getUpsideDownObj());
                                addRender(new ScoreText(100, gameTickRate, pixelPerUnit, enemy.getX(), enemy.getY()));
                                addScore(100);
                                SoundManager.playStomp();
                            }
                            break;
                        }
                        entity.move(Enemy.Action.LEFT);
                        break;
                    }
                }
                if (entity instanceof Player) {
                    if (entity.aboveIs(enemy)) {
                        if (enemy instanceof Goomba && !enemy.isDead()) {
                            if (entity.crashed(enemy, GameObject.Position.BOTTOM, 0.25)) {
//                                System.out.println("Crashed Top");
                            }
                        }
                    }
                    if (entity.leftIs(enemy)) {
                        if (player.isStarTime()) {
                            enemy.setDead(true);
                            enemy.setNeedClear(true);
                            addRender(enemy.getUpsideDownObj());
                            addRender(new ScoreText(100, gameTickRate, pixelPerUnit, enemy.getX(), enemy.getY()));
                            addScore(100);
                            SoundManager.playStomp();
                        } else {
                            if (player.getInvincibleTime() <= 0) {
                                if (enemy instanceof Goomba && !enemy.isDead()) {
                                    if (entity.crashed(enemy, GameObject.Position.LEFT, 0.25)) {
                                        readyToChangePlayer = true;
                                    }
                                }
                                if (enemy instanceof Koopa && !enemy.isNeedClear()) {
                                    Koopa koopa = (Koopa) enemy;
                                    if (koopa.getStatus().equals(Koopa.Status.STATIC_SHELL)) {
                                        koopa.move(Koopa.Action.LEFT);
                                        koopa.setStatus(Koopa.Status.MOVING_SHELL);
                                    } else {
                                        if (entity.crashed(enemy, GameObject.Position.LEFT, 0.25)) {
                                            readyToChangePlayer = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (entity.rightIs(enemy) && !enemy.isDead()) {
                        if (player.isStarTime()) {
                            enemy.setDead(true);
                            enemy.setNeedClear(true);
                            addRender(enemy.getUpsideDownObj());
                            addRender(new ScoreText(100, gameTickRate, pixelPerUnit, enemy.getX(), enemy.getY()));
                            addScore(100);
                            SoundManager.playStomp();
                        } else {
                            if (player.getInvincibleTime() <= 0) {
                                if (enemy instanceof Goomba && !enemy.isDead()) {
                                    if (entity.crashed(enemy, GameObject.Position.RIGHT, 0.25)) {
                                        readyToChangePlayer = true;
                                    }
                                }
                                if (enemy instanceof Koopa && !enemy.isNeedClear()) {
                                    Koopa koopa = (Koopa) enemy;
                                    if (koopa.getStatus().equals(Koopa.Status.STATIC_SHELL)) {
                                        koopa.move(Koopa.Action.RIGHT);
                                        koopa.setStatus(Koopa.Status.MOVING_SHELL);
                                    } else {
                                        if (entity.crashed(enemy, GameObject.Position.LEFT, 0.25)) {
                                            readyToChangePlayer = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if (entity.on(enemy)) {
                        readyToChangePlayer = false;
                        if (enemy instanceof Goomba && !enemy.isDead()) {
                            if (entity.crashed(enemy, GameObject.Position.TOP, 0.1)) {
                                enemy.setDead(true);
                                enemy.move(Enemy.Action.SQUASHED);
                                addRender(new ScoreText(100, gameTickRate, pixelPerUnit, enemy.getX(), enemy.getY()));
                                addScore(100);
                                player.bounds();
                            }
                        }
                        if (enemy instanceof Koopa) {
                            Koopa koopa = (Koopa) enemy;
                            if (koopa.getStatus().equals(Koopa.Status.WALK)) {
                                koopa.move(Koopa.Action.SHELL);
                                addRender(new ScoreText(100, gameTickRate, pixelPerUnit, enemy.getX(), enemy.getY()));
                                addScore(100);
                                SoundManager.playStomp();
                                player.bounds();
                            }
                            if (koopa.getStatus().equals(Koopa.Status.MOVING_SHELL)) {
                                if (koopa.getHorizontalStatus().equals(Entity.Status.LEFT)) {
                                    koopa.setHorizontalStatus(Entity.Status.RIGHT);
                                } else if (koopa.getHorizontalStatus().equals(Entity.Status.RIGHT)) {
                                    koopa.setHorizontalStatus(Entity.Status.LEFT);
                                }
                                addRender(new ScoreText(500, gameTickRate, pixelPerUnit, koopa.getX(), koopa.getY()));
                                addScore(500);
                                player.bounds();
                                SoundManager.playStomp();
                                koopa.setVelX(-koopa.getVelX());
                            }
                        }
                    }
                }
            }
            if (entity instanceof Enemy && ((Enemy) entity).isDead()) ((Enemy) entity).clearTime();
            entity.updatePositionX();
        }


        for (Prop prop : props) {
            if (prop.collided(player)) {
//                System.out.println("Collided");
                if (prop instanceof SuperMushroom) {
                    setPlayer(player.changeTo(Player.MarioType.SUPER_MARIO));
                    prop.setNeedClear(true);
                    engine.setGameState(GameEngine.GameState.MARIO_LEVEL_UP);
                    timeTickRemain = gameTickRate;
                    SoundManager.playLevelUp();
                } else if (prop instanceof FireFlower && player instanceof SuperMario) {
                    setPlayer(player.changeTo(Player.MarioType.FIRE_MARIO));
                    prop.setNeedClear(true);
                    engine.setGameState(GameEngine.GameState.MARIO_LEVEL_UP);
                    timeTickRemain = gameTickRate;
                    SoundManager.playLevelUp();
                } else if (prop instanceof FireFlower && player instanceof FireMario) {
                    prop.setNeedClear(true);
                    addRender(new ScoreText(1000, gameTickRate, pixelPerUnit, prop.getX(), prop.getY()));
                    addScore(1000);
                    timeTickRemain = gameTickRate;
                    SoundManager.playLevelUp();
                } else if (prop instanceof Star) {
                    prop.setNeedClear(true);
                    addRender(new ScoreText(1000, gameTickRate, pixelPerUnit, prop.getX(), prop.getY()));
                    addScore(1000);
                    player.setStarTime(true);
                    SoundManager.playLevelUp();
                    SoundManager.playBackground(SoundManager.BackgroundMusic.STARMAN);
                    engine.setStarManTime(15);
                }
            }
            for (Enemy enemy : enemies) {
                if (enemy.collided(prop)) {
                    if (prop instanceof FireBall) {
                        enemy.setDead(true);
                        enemy.setNeedClear(true);
                        addRender(enemy.getUpsideDownObj());
                        addRender(new ScoreText(100, gameTickRate, pixelPerUnit, enemy.getX(), enemy.getY()));
                        addScore(100);
                        prop.setNeedClear(true);
                        addRender(new Boom(gameTickRate, pixelPerUnit, enemy.getX(), enemy.getY()));
                        SoundManager.playStomp();
                    }
                }
            }
        }


        player.setUpKeyPressed(key.isUpKeyPressed());

        if (key.isLeftKeyPressed() && !key.isRightKeyPressed()) {
            player.move(Player.Action.LEFT);
        }

        if (key.isRightKeyPressed() && !key.isLeftKeyPressed()) {
            player.move(Player.Action.RIGHT);
        }

        if (fireCD > 0) fireCD--;

        if (key.isAttackKeyPressed() && player instanceof FireMario && fireCD <= 0) {
            FireBall ball = new FireBall(gameTickRate, pixelPerUnit);
            if (player.getFaceDirection().equals(Entity.Status.LEFT)) {
                ball.setPosition(player.getX() - 0.8, player.getY() + 0.5);
                ball.setHorizontalStatus(Entity.Status.LEFT);
                ball.setVelX(-6);
            } else if (player.getFaceDirection().equals(Entity.Status.RIGHT)) {
                ball.setPosition(player.getX() + 0.8, player.getY() + 0.5);
                ball.setHorizontalStatus(Entity.Status.RIGHT);
                ball.setVelX(6);
            }
            addRender(ball);
            SoundManager.playKick();
            fireCD = gameTickRate / 2;
        }

        if (Objects.equals(player.getHorizontalStatus(), Entity.Status.STOP)) {
            if (player.isJumping()) player.setTexture(Player.Texture.JUMP);
            else player.setTexture(Player.Texture.STAND);
        }


        if (key.isLeftKeyPressed() == key.isRightKeyPressed()) {
            player.move(Player.Action.STOP);
        }
        player.updateJump();
        for (Prop prop : props) prop.update();
        for (Item item : items) item.update();
        for (Block block : bounceBlock) block.update();
        for (GameObject object : renderObjects) object.updateTexture();
    }

    private void addCoin(int i) {
        engine.addCoinCount(1);
    }

    private void changePlayer() {
        if (player instanceof SuperMario || player instanceof FireMario) {
            Player p = player.changeTo(Player.MarioType.MARIO);
            setPlayer(p);
            p.setInvincibleTime(3);
        }else {
            playerDie();
        }

        readyToChangePlayer = false;
    }

    private void playerDie(){
        addRender(new DeadMario(player));
        player.setNeedClear(true);
        engine.setGameState(GameEngine.GameState.GAME_DYING);
        SoundManager.playBackground(SoundManager.BackgroundMusic.STOP);
        SoundManager.playDeath();
        engine.setTimeout(4);
    }

    @Override
    public void drawFrame(Graphics g) {
        TreeSet<GameObject> render;
        synchronized (renderObjects) {
            render = new TreeSet<>(renderObjects);
        }


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
