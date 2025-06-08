package github.PanheadGG.SuperMarioBros.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GameKeyListener implements KeyListener {
    private static GameKeyListener instance;
    private boolean isLeftKeyPressed;
    private boolean isRightKeyPressed;
    private boolean isUpKeyPressed;
    private boolean isAttackKeyPressed;

    public static GameKeyListener getInstance() {
        if (instance == null) {
            instance = new GameKeyListener();
        }
        return instance;
    }

    public GameKeyListener() {
        isLeftKeyPressed = false;
        isRightKeyPressed = false;
        isUpKeyPressed = false;
        isAttackKeyPressed = false;
    }


    public boolean isLeftKeyPressed() {
        return isLeftKeyPressed;
    }

    public void setLeftKeyPressed(boolean leftKeyPressed) {
        isLeftKeyPressed = leftKeyPressed;
    }

    public boolean isRightKeyPressed() {
        return isRightKeyPressed;
    }

    public void setRightKeyPressed(boolean rightKeyPressed) {
        isRightKeyPressed = rightKeyPressed;
    }

    public boolean isUpKeyPressed() {
        return isUpKeyPressed;
    }

    public void setUpKeyPressed(boolean upKeyPressed) {
        isUpKeyPressed = upKeyPressed;
    }

    public boolean isAttackKeyPressed() {
        return isAttackKeyPressed;
    }

    public void setAttackKeyPressed(boolean attackKeyPressed) {
        isAttackKeyPressed = attackKeyPressed;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                setLeftKeyPressed(true);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                setRightKeyPressed(true);
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_K:
                setUpKeyPressed(true);
                break;
            case KeyEvent.VK_J:
                setAttackKeyPressed(true);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                setLeftKeyPressed(false);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                setRightKeyPressed(false);
                break;
            case KeyEvent.VK_W:
            case KeyEvent.VK_K:
                setUpKeyPressed(false);
                break;
            case KeyEvent.VK_J:
                setAttackKeyPressed(false);
                break;
        }
    }
}
