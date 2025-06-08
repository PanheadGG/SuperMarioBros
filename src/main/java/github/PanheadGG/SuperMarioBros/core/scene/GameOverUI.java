package github.PanheadGG.SuperMarioBros.core.scene;

import github.PanheadGG.SuperMarioBros.assets.Assets;

import java.awt.*;

public class GameOverUI implements Drawable {
    public static final class Reason{
        public static final String GAME_OVER = "GAME OVER";
        public static final String WIN = "YOU WIN";
        public static final String TIME_OUT = "TIME OUT";
    }

    private String reason;
    private int score = 0;
    private int pixelPerUnit;

    public GameOverUI(String reason,int score ,int pixelPerUnit){
        this.reason = reason;
        this.score = score;
        this.pixelPerUnit = pixelPerUnit;
    }

    public GameOverUI(int pixelPerUnit){
        this.reason = "";
        this.score = 0;
        this.pixelPerUnit = pixelPerUnit;
    }

    public void set(String reason,int score){
        this.reason = reason;
        this.score = score;
    }

    @Override
    public void drawFrame(Graphics g) {
        g.setFont(Assets.FONT);
        g.drawString(reason, pixelPerUnit * 6, pixelPerUnit * 6);
        g.drawString("Score: "+score, pixelPerUnit * 6, pixelPerUnit * 7);
        g.drawString("Press Enter to continue", pixelPerUnit * 4, pixelPerUnit * 8);
    }

    @Override
    public Color getBackgroundColor() {
        return Color.BLACK;
    }
}
