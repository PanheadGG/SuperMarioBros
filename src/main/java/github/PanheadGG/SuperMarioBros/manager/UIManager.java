package github.PanheadGG.SuperMarioBros.manager;

import github.PanheadGG.SuperMarioBros.core.scene.Drawable;
import github.PanheadGG.SuperMarioBros.timer.MicroTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class UIManager extends JFrame implements Runnable {
    private Drawable draw;
    private BufferStrategy bufferStrategy;
    private int refreshRate;

    private boolean isDragging;
    private int xx,yy;

    public UIManager(Drawable draw, int width, int height, int refreshRate) {
        this.draw = draw;
        this.refreshRate = refreshRate;
        setSize(width, height);
        init();
    }
    public UIManager(int width, int height, int refreshRate) {
        this.refreshRate = refreshRate;
        setSize(width, height);
        init();
    }
    private void init() {
        setTitle("Mario");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        enableInputMethods(false);
//        setUndecorated(true);//隐藏标题栏
        setResizable(false);//禁止缩放
        setVisible(true);
        createBufferStrategy(2); // 创建双缓冲
        bufferStrategy = getBufferStrategy();
        setLocationRelativeTo(null);
        setForeground(Color.WHITE);

        //实现无边框拖拽界面
        //监听最初位置

        this.addMouseListener(new MouseAdapter() {    //给JFrame窗体添加一个鼠标监听
            public void mousePressed(MouseEvent e) {     //鼠标点击时记录一下初始位置
                isDragging = true;
                xx = e.getX();
                yy = e.getY();
            }

            public void mouseReleased(MouseEvent e) {  //鼠标松开时
                isDragging = false;
            }
        });
        //时刻更新鼠标位置
        this.addMouseMotionListener(new MouseMotionAdapter() { //添加指定的鼠标移动侦听器，以接收发自此组件的鼠标移动事件。如果侦听器 l 为 null，则不会抛                                                         出异常并且不执行动作。
            public void mouseDragged(MouseEvent e) {
                //修改位置
                if (isDragging) {                                //只要鼠标是点击的（isDragging），就时刻更改窗体的位置
                    int left = getLocation().x;
                    int top = getLocation().y;
                    setLocation(left + e.getX() - xx, top + e.getY() - yy);

                }
            }
        });

/*
        // 添加窗口大小变化监听器
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if(draw!=null) engine.onWindowSizeChanged(e);
            }
        });*/
    }

    public void setRefreshRate(int refreshRate) {
        this.refreshRate = refreshRate;
    }

    @Override
    public void update(Graphics g) {
        // 重写 update 方法，避免默认的背景擦除
        paint(g);
    }

    @Override
    public void paint(Graphics g) {
        // 检查 bufferStrategy 是否为 null，避免空指针异常
        if (bufferStrategy == null) {
            createBufferStrategy(2); // 确保 bufferStrategy 被正确初始化
            bufferStrategy = getBufferStrategy();
        }

        // 使用双缓冲进行绘制
        do {
            do {
                Graphics graphics = bufferStrategy.getDrawGraphics();
                try {
                    // 清除屏幕
                    graphics.clearRect(0, 0, getWidth(), getHeight());
                    // 调用游戏引擎的绘制方法
                    if(draw!=null){
                        draw.drawFrame(graphics);
                    }else {
                        setBackgroundColor(Color.BLACK);
                    }
                } finally {
                    graphics.dispose();
                }
            } while (bufferStrategy.contentsRestored());
            bufferStrategy.show();
        } while (bufferStrategy.contentsLost());
    }

    @Override
    public void run() {
        double fps = 0;
        long start = System.currentTimeMillis();
        long end;
        int frame = 0;
        while (true) {
            repaint();
            end = System.currentTimeMillis();
            frame++;
            if (frame >= refreshRate) {
                frame = 0;
                fps = refreshRate * 1000 / (double) (end - start);
//                System.out.println("FPS: "+fps);
                start = System.currentTimeMillis();
            }
            MicroTimer.sleep(1000000 / refreshRate);
        }
    }

    public void setBackgroundColor(Color color){
        this.getContentPane().setBackground(color);
    }

    public void setDraw(Drawable draw){
        this.draw = draw;
    }

    public void setKeyListener(KeyListener keyListener){
        KeyListener[] keyListeners = this.getKeyListeners();
        for(KeyListener keyListener1 : keyListeners){
            this.removeKeyListener(keyListener1);
        }
        this.addKeyListener(keyListener);
    }
}