package github.PanheadGG.SuperMarioBros.manager;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.player.MusicPlayer;

import java.net.URL;

public class SoundManager {
    private static MusicPlayer background;
    private static MusicPlayer jump;
    private static MusicPlayer stomp;
    private static MusicPlayer bigJump;
    private static MusicPlayer coin;
    private static MusicPlayer brickSmash;
    private static MusicPlayer propAppears;
    private static MusicPlayer levelUp;
    private static MusicPlayer bump;
    private static MusicPlayer kick;
    private static MusicPlayer death;
    private static MusicPlayer outOfTime;
    private static MusicPlayer stageClear;
    private static MusicPlayer gameOver;
    private static MusicPlayer countDown;

    public static void playKick() {
        if (kick == null) {
            kick = new MusicPlayer();
            kick.load(Assets.getURLByKey("sound.prop.kick"));
        }
        kick.play();
    }

    public static void playBump() {
        if (bump == null) {
            bump = new MusicPlayer();
            bump.load(Assets.getURLByKey("sound.block.bump"));
        }
        bump.play();
    }

    public final static class BackgroundMusic {
        public static final String MAIN = "music.background.main";
        public static final String SPEED_UP = "music.background.speed_up";
        public static final String UNDERGROUND = "music.background.underground";
        public static final String STARMAN = "music.background.starman";
        public static final String STOP = "STOP";
    }

    public static void playBackground(String backgroundMusic) {
        if (background == null) {
            background = new MusicPlayer();
        }
        if (backgroundMusic.equals(BackgroundMusic.STOP)) {
            background.pause();
            return;
        }
        if (background.isPaused()) {
            background.resume();
        }
        background.load(Assets.getURLByKey(backgroundMusic));
        background.loop(MusicPlayer.INFINITE);
    }

    public static void playJump() {
        if (jump == null) {
            jump = new MusicPlayer();
            jump.load(Assets.getURLByKey("sound.mario.jump"));
        }
        jump.play();
    }

    public static void playBigJump() {
        if (bigJump == null) {
            bigJump = new MusicPlayer();
            bigJump.load(Assets.getURLByKey("sound.mario.super.jump"));
        }
        bigJump.play();
    }

    public static void playBrickSmash() {
        if (brickSmash == null) {
            brickSmash = new MusicPlayer();
            brickSmash.load(Assets.getURLByKey("sound.brick.smash"));
        }
        brickSmash.play();
    }


    public static void playCoin() {
        if (coin == null) {
            coin = new MusicPlayer();
            coin.load(Assets.getURLByKey("sound.mario.coin"));
        }
        coin.play();
    }

    public static void playStomp() {
        if (stomp == null) {
            stomp = new MusicPlayer();
            stomp.load(Assets.getURLByKey("sound.mario.stomp"));
        }
        stomp.play();
    }

    public static void playLoop(URL url) {
        if (background == null) {
            background = new MusicPlayer();
            background.load(url);
        }
        background.loop(MusicPlayer.INFINITE);
    }

    public static void playPropAppears() {
        if (propAppears == null) {
            propAppears = new MusicPlayer();
            propAppears.load(Assets.getURLByKey("sound.prop.appears"));
        }
        propAppears.play();
    }

    public static void playLevelUp() {
        if (levelUp == null) {
            levelUp = new MusicPlayer();
            levelUp.load(Assets.getURLByKey("sound.mario.level_up"));
        }
        levelUp.play();
    }

    public static void playDeath() {
        if (death == null) {
            death = new MusicPlayer();
            death.load(Assets.getURLByKey("sound.mario.death"));
        }
        death.play();
    }

    public static void playOutOfTime() {
        if (outOfTime == null) {
            outOfTime = new MusicPlayer();
            outOfTime.load(Assets.getURLByKey("music.out_of_time"));
        }
        outOfTime.play();
    }
    public static void playStageClear() {
        if (stageClear == null) {
            stageClear = new MusicPlayer();
            stageClear.load(Assets.getURLByKey("music.stage_clear"));
        }
        stageClear.play();
    }
    public static void playGameOver() {
        if (gameOver == null) {
            gameOver = new MusicPlayer();
            gameOver.load(Assets.getURLByKey("music.game_over"));
        }
        gameOver.play();
    }
    public static void playCountDown() {
        if (countDown == null) {
            countDown = new MusicPlayer();
            countDown.load(Assets.getURLByKey("sound.clip.count_down"));
        }
        countDown.play();
    }
    public static void stopCountDown() {
        if (countDown == null) {
            return;
        }
        countDown.stop();
    }
}
