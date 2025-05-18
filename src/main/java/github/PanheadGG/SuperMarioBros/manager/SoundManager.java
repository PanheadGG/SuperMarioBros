package github.PanheadGG.SuperMarioBros.manager;

import github.PanheadGG.SuperMarioBros.assets.Asset;
import github.PanheadGG.SuperMarioBros.player.MusicPlayer;

public class SoundManager {
    private static MusicPlayer background;
    private static MusicPlayer jump;
    private static MusicPlayer stomp;

    public static void playBackground() {
        if (background == null) {
            try {
                background = new MusicPlayer();
                background.load(Asset.BACKGROUND_AUDIO);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        background.loop(MusicPlayer.INFINITE);
    }

    public static void playJump() {
        if (jump == null) {
            jump = new MusicPlayer();
            jump.load(Asset.JUMP_AUDIO);
        }
        jump.play();
    }

    public static void playStomp() {
        if (stomp == null) {
            stomp = new MusicPlayer();
            stomp.load(Asset.STOMP_AUDIO);
        }
        stomp.play();
    }
}
