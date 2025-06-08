package github.PanheadGG.SuperMarioBros.core.scene;

import github.PanheadGG.SuperMarioBros.assets.Assets;
import github.PanheadGG.SuperMarioBros.callback.GameMenuCallback;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class GameMenu implements Drawable {
    private BufferedImage logo;
    private int pixelPerUnit;
    private KeyListener keyListener;
    private GameMenuCallback callback;

    public GameMenu(int pixelPerUnit) {
        this.pixelPerUnit = pixelPerUnit;
        logo = Assets.getImageByKey("texture.menu.logo");
        keyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    if(callback != null){
                        callback.onEnter();
                    }
                }
            }
        };
    }
    @Override
    public void drawFrame(Graphics g) {
        g.setFont(Assets.FONT);
        g.drawImage(logo, pixelPerUnit * 2, pixelPerUnit * 2,11*pixelPerUnit, (int) (5.5*pixelPerUnit), null);
        g.drawString("Press Enter to start", pixelPerUnit * 4, pixelPerUnit * 10);
    }

    @Override
    public Color getBackgroundColor() {
        return Color.BLACK;
    }

    public KeyListener getKeyListener() {
        return keyListener;
    }

    public void setEnterCallback(GameMenuCallback callback){
        this.callback = callback;
    }
}
