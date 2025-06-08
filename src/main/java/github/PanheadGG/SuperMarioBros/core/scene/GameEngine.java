package github.PanheadGG.SuperMarioBros.core.scene;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.assets.DynamicImage;
import github.PanheadGG.SuperMarioBros.callback.GameOverCallback;
import github.PanheadGG.SuperMarioBros.manager.UIManager;
import github.PanheadGG.SuperMarioBros.manager.SoundManager;
import github.PanheadGG.SuperMarioBros.map.GameMap;
import github.PanheadGG.SuperMarioBros.map.MapLoader;
import github.PanheadGG.SuperMarioBros.timer.MicroTimer;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GameEngine implements Runnable, Drawable {
    private int gameTickRate = 200;
    private int refreshRate = 165;
    private int pixelPerUnit = 80;
    private int tickOfGameTickRate = 0;
    private int mapWidth;
    private int mapHeight;
    private GameMap map;
    private int score = 0;
    private int coinCount = 0;
    private int time = 400;
    private int currentTick = 0;
    private DynamicImage scoreCoin;
    private BufferedImage plusImage;

    private UIManager ui = null;

    private KeyListener keyListener;
    private Color backgroundColor = new Color(0, 0, 0, 255);
    ;
    private String gameState = GameState.LOADING_MAP;
    private int starManTime = 0;

    private int timeout = 0;

    private GameOverCallback gameOverCallback;

    private boolean runnable = true;

    public static class GameState {
        public static String GAME_OVER = "GAME_OVER";
        public static String GAME_WIN = "GAME_WIN";
        public static String GAME_RUNNING = "GAME_RUNNING";
        public static String GAME_DYING = "GAME_DYING";
        public static String GAME_TIME_OUT = "GAME_TIME_OUT";
        public static String MARIO_LEVEL_UP = "MARIO_LEVEL_UP";
        public static String LOADING_MAP = "LOADING_MAP";
    }

    public GameEngine() {
        scoreCoin = Assets.getDynamicImageByKey("texture.item.score_coin");
        scoreCoin.setGameTickRate(gameTickRate);
        plusImage = Assets.getImageByKey("texture.item.plus");
//        loadMap();
    }

    public void setUI(UIManager ui) {
        this.ui = ui;
    }

    public UIManager getUI() {
        return ui;
    }

    public void loadMap() {
        MapLoader loader = new MapLoader();
        loader.setGroundMap(this);
        loader.load(GameEngine.class.getResource("/assets/map/level_1.json"));
        map = loader.getGroundMap();
        mapWidth = map.getMapWidth();
        mapHeight = map.getMapHeight();
        pixelPerUnit = map.getPixelPerUnit();
        backgroundColor = loader.getBackgroundColor();
        keyListener = map.getKeyListener();
        time = loader.getTime();
        timeout = 100;
    }


    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setKeyListener(KeyListener keyListener) {
        this.keyListener = keyListener;
    }

    public KeyListener getKeyListener() {
        return keyListener;
    }

    public void setStarManTime(int starManTime) {
        this.starManTime = starManTime;
    }

    public int getStarManTime() {
        return starManTime;
    }

    public void drawFrame(Graphics g) {
        if (gameState.equals(GameState.GAME_RUNNING) || gameState.equals(GameState.GAME_WIN) || gameState.equals(GameState.GAME_DYING) || gameState.equals(GameState.MARIO_LEVEL_UP) || gameState.equals(GameState.GAME_TIME_OUT)) {
            map.drawFrame(g);
        } else if (gameState.equals(GameState.LOADING_MAP)) {

        }
        g.setFont(Assets.getFont(35f));
        g.drawString("MARIO", pixelPerUnit, pixelPerUnit);
        g.drawString(String.format("%06d", score), pixelPerUnit, (int) (1.5 * pixelPerUnit));

        if (scoreCoin != null && plusImage != null) {
            g.drawImage(scoreCoin.getTexture(), (int) (pixelPerUnit * 5), pixelPerUnit, pixelPerUnit / 2, pixelPerUnit / 2, null);
            g.drawImage(plusImage, (int) (pixelPerUnit * 5.5), pixelPerUnit, pixelPerUnit / 2, pixelPerUnit / 2, null);
        }
        g.drawString(String.format("%02d", coinCount), (int) (6 * pixelPerUnit), (int) (1.5 * pixelPerUnit));
        g.drawString("WORLD", pixelPerUnit * 8, pixelPerUnit);
        g.drawString(map.getMapName(), pixelPerUnit * 8, (int) (1.5 * pixelPerUnit));

        g.drawString("TIME", pixelPerUnit * 12, pixelPerUnit);
        g.drawString(String.format("%4d", time), pixelPerUnit * 12, (int) (1.5 * pixelPerUnit));
    }

    @Override
    public void run() {
        double tps = 0;
        long start = System.currentTimeMillis();
        long end;
        int tick = 0;
        while (runnable) {
            tick++;
            end = System.currentTimeMillis();
            if (tick >= gameTickRate) {
                tick = 0;
                tps = gameTickRate * 1000 / (double) (end - start);
//                System.out.println("TPS: "+tps);
                start = System.currentTimeMillis();
            }
            update();
            if(map!=null) map.update();
            MicroTimer.sleep(1000000 / gameTickRate);
        }
    }

    public void update() {
        scoreCoin.update();
        if (timeout > 0) {
            timeout--;
        }

        if (gameState.equals(GameState.LOADING_MAP) && timeout == 0) {
            ui.setBackground(backgroundColor);
            gameState = GameState.GAME_RUNNING;
            SoundManager.playBackground(SoundManager.BackgroundMusic.MAIN);
            timeout = -1;
        } else if (gameState.equals(GameState.GAME_DYING) && timeout == 0) {
            gameState = GameState.GAME_OVER;
            runnable = false;
            gameOverCallback.onGameOver(GameOverUI.Reason.GAME_OVER, score);
        } else if (gameState.equals(GameState.GAME_TIME_OUT) && timeout == 0) {
            runnable = false;
            gameOverCallback.onGameOver(GameOverUI.Reason.TIME_OUT, score);
        }

        if (gameState.equals(GameState.GAME_RUNNING)) {
            currentTick++;
            currentTick = currentTick % gameTickRate;
            if (currentTick == 0) {
                if (starManTime > 0) starManTime--;
                if (starManTime == 2 && time > 100)
                    SoundManager.playBackground(SoundManager.BackgroundMusic.MAIN);
                else if (starManTime == 2)
                    SoundManager.playBackground(SoundManager.BackgroundMusic.SPEED_UP);
                time--;
                if (time == 100)
                    SoundManager.playBackground(SoundManager.BackgroundMusic.SPEED_UP);
            }
            if (time <= 0) {
                gameState = GameState.GAME_TIME_OUT;
                SoundManager.playBackground(SoundManager.BackgroundMusic.STOP);
                SoundManager.playOutOfTime();
                timeout = 4 * gameTickRate;
            }
        } else if (gameState.equals(GameState.LOADING_MAP)) {
            if (ui != null) {
                ui.setBackground(new Color(0, 0, 0, 255));
            }
        } else if (gameState.equals(GameState.GAME_WIN)) {
            while (time-- > 0) {
                addScore(50);
                MicroTimer.sleep(20_000);
            }
            SoundManager.stopCountDown();
            runnable = false;
            gameOverCallback.onGameOver(GameOverUI.Reason.WIN, score);

            System.gc();
        }
    }


    public int getGameTickRate() {
        return gameTickRate;
    }

    public int getPixelPerUnit() {
        return pixelPerUnit;
    }

    public void onWindowSizeChanged(ComponentEvent e) {

    }

    public void addScore(int score) {
        this.score += score;
    }

    public int getScore() {
        return score;
    }

    public void addCoinCount(int coinCount) {
        this.coinCount += coinCount;
    }

    public int getCoinCount() {
        return coinCount;
    }

    public String getGameState() {
        return gameState;
    }

    public void setGameState(String gameState) {
        this.gameState = gameState;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout * gameTickRate;
    }

    public void setGameOverCallback(GameOverCallback gameOverCallback) {
        this.gameOverCallback = gameOverCallback;
    }
}
