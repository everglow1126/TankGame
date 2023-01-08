package tankgame03;


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Vector;

public class MyPanel extends JPanel implements KeyListener, Runnable {
    protected static MyTank myTank = null; //我的坦克
    protected static Vector<EnemyTank> enemyTanks = new Vector<>(); //将敌方坦克存入集合
    protected static int enemyTanksNumber = 5; //初始化敌方坦克数量为5
    private Vector<Bomb> bombs = new Vector<>();//存放炸弹
    public static Vector<Tank> allTanks = new Vector<>();//存放所有坦克
    public static Vector<Block> blocks = new Vector<>();
    public static int breakableBlocksNum = 80;
    public static int ironBlocksNum = 50;
    //初始化三张爆炸图片
    Image image1 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/IMG_1925.GIF"));
    Image image2 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/IMG_1926.GIF"));
    Image image3 = Toolkit.getDefaultToolkit().getImage(Panel.class.getResource("/IMG_1927.GIF"));

    public MyPanel() throws IOException, ClassNotFoundException {
        if (TankGame03.continue_ == -1) {
            myTank = new MyTank(200, 250, 7); //初始化我方坦克
            allTanks.add(myTank);
            //初始化敌方坦克，默认数量为5个
            for (int i = 0; i < enemyTanksNumber; i++) {
                EnemyTank enemyTank = new EnemyTank((100 * (i + 1)), 0);//初始化敌方坦克的方向
                enemyTanks.add(enemyTank);//将创建的敌方坦克加入到集合
                allTanks.add(enemyTank);
                new Thread(enemyTank).start();//启动敌方坦克的线程
            }
        } else {
            Recorder.readRecord();//记录本局游戏信息
            for (int i = 0; i < enemyTanksNumber; i++) {
                new Thread(enemyTanks.get(i)).start();//启动敌方坦克的线程
            }
        }
        //增加可击毁的方块
        for (int i = 0; i < breakableBlocksNum / 2; i++) {
            blocks.add(new BreakableBlock(100 + (14 * i), 100));
        }
        for (int i = 0; i < breakableBlocksNum / 2; i++) {
            blocks.add(new BreakableBlock(100 + (14 * i), 125));
        }
        //增加不可击毁的铁块
        for (int i = 0; i < ironBlocksNum / 2; i++) {
            blocks.add(new IronBlock(100 + (22 * i), 350));
        }
        for (int i = 0; i < ironBlocksNum / 2; i++) {
            blocks.add(new IronBlock(100 + (22 * i), 375));
        }
        //爆炸音效
        new AePlayWave("src/bgmusic.wav").start();
    }

    @Override
    //游戏区域
    public void paint(Graphics g) {
        super.paint(g);
        g.fillRect(0, 0, 1000, 750);//初始化背景
        g.setColor(Color.orange);
        Block block;
        //添加方块
        for (int i = 0; i < blocks.size(); i++) {
            block = blocks.get(i);
            if (block instanceof BreakableBlock) {
                if (((BreakableBlock) block).isLive) {
                    g.fillRect(block.x, block.y, 12, 20);
                } else {
                    blocks.remove(block);
                }
            } else {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(block.x, block.y, 20, 20);
            }
        }
        drawTank(1010, 40, g, 0, 1);//绘制记录用的坦克
        g.setColor(Color.black);
        g.drawString("您累积击毁敌方坦克", 1000, 20);//绘制记录数据
        g.drawString(String.valueOf(Recorder.hitEnemyTank), 1060, 70);//绘制记录数据
        if (myTank.isLive) {
            drawTank(myTank.getX(), myTank.getY(), g, myTank.getDirection(), 0);//调用方法绘制我方坦克
        }
        //遍历myTank的子弹集合
        for (int i = 0; i < myTank.bullets.size(); i++) {
            //在集合中取出一发
            Bullet bullet = myTank.bullets.get(i);
            //判断我方坦克是否发射子弹，并绘制
            if (bullet != null && bullet.isLive) {
                g.draw3DRect(bullet.getX(), bullet.getY(), 1, 1, false);
            } else {
                //如果我方坦克子弹不存活
                myTank.bullets.remove(bullet);
            }
        }

        //遍历敌方坦克集合
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            //判断敌方坦克是否存活
            if (enemyTank.isLive) {
                //绘制敌方坦克
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirection(), 1);//绘制敌方坦克
                //遍历敌方坦克的子弹集合，并绘制
                for (int j = 0; j < enemyTank.bullets.size(); j++) {
                    Bullet bullet = enemyTank.bullets.get(j);
                    if (bullet.isLive) {
                        g.draw3DRect(bullet.getX(), bullet.getY(), 1, 1, false);
                    } else {
                        enemyTank.bullets.remove(bullet);
                    }
                }
            } else {
                //如果敌方坦克被击毁则移除
                enemyTanks.remove(enemyTank);
                allTanks.remove(enemyTank);
                Recorder.setHitEnemyTank();
            }
        }
        //绘制炸弹动态效果
        for (int i = 0; i < bombs.size(); i++) {
            Bomb b = bombs.get(i);
            if (b.life > 6) {
                g.drawImage(image1, b.x, b.y, 60, 60, this);
            } else if (b.life > 3) {
                g.drawImage(image2, b.x, b.y, 60, 60, this);
            } else {
                g.drawImage(image3, b.x, b.y, 60, 60, this);
            }
            b.lifeDecrease();
            if (!b.isLive) {
                //移除炸弹
                bombs.remove(b);
            }
        }
    }

    //判定是否击中方块的方法
    public void hitBlock(Vector<Bullet> bullets) {
        Vector<Block> blocks1 = blocks;
        //遍历子弹集合
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);//取出子弹判定是否击中方块
            for (int j = 0; j < blocks1.size(); j++) {
                Block bl = blocks1.get(j);
                int x = bl.x;
                int y = bl.y;
                if (b.getX() > x && b.getX() < x + 12 && b.getY() < y + 20 && b.getY() > y) {
                    b.isLive = false;
                    if (bl instanceof BreakableBlock) {//如果是可击毁的方块则isLive置为false
                        ((BreakableBlock) bl).isLive = false;
                    }
                }
            }
        }
    }

    //子弹击中坦克的判定
    public boolean hitTank(Vector<Bullet> bullets, EnemyTank enemyTank) {
        //遍历我方坦克子弹集合
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            //根据敌方坦克方向划定击中区域进行判断
            switch (enemyTank.getDirection()) {
                case 0:
                case 2:
                    if (b.getX() > enemyTank.getX() && b.getX() < (enemyTank.getX() + 40)
                            && b.getY() > enemyTank.getY() && b.getY() < enemyTank.getY() + 60) {
                        b.isLive = false;
                        enemyTank.isLive = false;
                        bombs.add(new Bomb(enemyTank.getX(), enemyTank.getY()));
                        //爆炸音效
                        new AePlayWave("src/boom.wav").start();
                        return true;
                    }
                    break;
                case 1:
                case 3:
                    if (b.getX() > enemyTank.getX() && b.getX() < (enemyTank.getX() + 80)
                            && b.getY() > enemyTank.getY() && b.getY() < (enemyTank.getY() + 60)) {
                        b.isLive = false;
                        enemyTank.isLive = false;
                        bombs.add(new Bomb(enemyTank.getX(), enemyTank.getY()));
                        //爆炸音效
                        new AePlayWave("src/boom.wav").start();
                        return true;
                    }
                    break;
            }
        }
        return false;
    }

    //我方击中敌方坦克的方法
    public void hitEnemyTank() {
        for (int i = 0; i < enemyTanks.size(); i++) {
            hitTank(myTank.bullets, enemyTanks.get(i));
        }
    }

    //编写方法，判断子弹是否击中我方坦克
    public void hitMyTank() {
        //遍历敌方坦克集合
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);//取出一个敌方坦克
            //遍历敌方坦克子弹
            for (int j = 0; j < enemyTank.bullets.size(); j++) {
                Bullet b = enemyTank.bullets.get(j);
                switch (myTank.getDirection()) {
                    case 0:
                    case 2:
                        if (b.getX() > myTank.getX() && b.getX() < myTank.getX() + 40
                                && b.getY() > myTank.getY() && b.getY() < myTank.getY() + 60) {
                            b.isLive = false;
                            myTank.isLive = false;
                            bombs.add(new Bomb(myTank.getX(), myTank.getY()));
                            //爆炸音效
                            new AePlayWave("src/boom.wav").start();
                        }
                        break;
                    case 1:
                    case 3:
                        if (b.getX() > myTank.getX() && b.getX() < (myTank.getX() + 80)
                                && b.getY() > myTank.getY() && b.getY() < (myTank.getY() + 60)) {
                            b.isLive = false;
                            myTank.isLive = false;
                            bombs.add(new Bomb(myTank.getX(), myTank.getY()));
                            //爆炸音效
                            new AePlayWave("src/boom.wav").start();
                        }
                        break;
                }
            }
        }
    }

    /**
     * @param x         坦克的左上角横坐标
     * @param y         坦克左上角纵坐标
     * @param g         画笔
     * @param direction 坦克的方向（上下左右）
     * @param type      坦克的类型
     */
    //绘制坦克方法
    public void drawTank(int x, int y, Graphics g, int direction, int type) {
        //根据不同类型坦克设置不同颜色
        switch (type) {
            case 0://0为我方坦克
                g.setColor(Color.cyan);
                break;
            case 1://1为敌方坦克
                g.setColor(Color.yellow);
                break;
        }
        //根据坦克的方向绘制坦克
        switch (direction) {
            case 0: //0为坦克向上的方向
                g.fill3DRect(x, y, 10, 60, false);//画出坦克左轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false);//画出坦克身子
                g.fill3DRect(x + 30, y, 10, 60, false);//画出坦克右轮子
                g.fillOval(x + 10, y + 20, 20, 20);//画出坦克盖子
                g.drawLine(x + 20, y + 30, x + 20, y - 5);//画出坦克炮筒
                break;
            case 1: //1为坦克向左的方向
                g.fill3DRect(x, y, 60, 10, false);//画出坦克上轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false);//画出坦克身子
                g.fill3DRect(x, y + 30, 60, 10, false);//画出坦克下轮子
                g.fillOval(x + 20, y + 10, 20, 20);//画出坦克盖子
                g.drawLine(x + 30, y + 20, x - 5, y + 20);//画出坦克炮筒
                break;
            case 2: //2为坦克向下的方向
                g.fill3DRect(x, y, 10, 60, false);//画出坦克左轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false);//画出坦克身子
                g.fill3DRect(x + 30, y, 10, 60, false);//画出坦克右轮子
                g.fillOval(x + 10, y + 20, 20, 20);//画出坦克盖子
                g.drawLine(x + 20, y + 30, x + 20, y + 65);//画出坦克炮筒
                break;
            case 3: //3为坦克向右的方向
                g.fill3DRect(x, y, 60, 10, false);//画出坦克上轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false);//画出坦克身子
                g.fill3DRect(x, y + 30, 60, 10, false);//画出坦克下轮子
                g.fillOval(x + 20, y + 10, 20, 20);//画出坦克盖子
                g.drawLine(x + 30, y + 20, x + 65, y + 20);//画出坦克炮筒
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //根据wasd从而操控坦克移动
        if (e.getKeyCode() == KeyEvent.VK_W) {
            myTank.setDirection(0);
            if (myTank.isTouchTanks() && myTank.isTouchBlocks(12, 20)) {
                myTank.moveUp();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            myTank.setDirection(1);
            if (myTank.isTouchTanks() && myTank.isTouchBlocks(12, 20)) {
                myTank.moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_S) {
            myTank.setDirection(2);
            if (myTank.isTouchTanks() && myTank.isTouchBlocks(12, 20)) {
                myTank.moveDown();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            myTank.setDirection(3);
            if (myTank.isTouchTanks() && myTank.isTouchBlocks(12, 20)) {
                myTank.moveRight();
            }
        }
        //按J发射子弹
        if (e.getKeyCode() == KeyEvent.VK_F) {
            //控制我方坦克最多能射五发子弹
            if (myTank.bullets.size() < 5) {
                myTank.shot();
            }
        }
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void run() {
        while (true) {
            try {
                //每隔100ms就绘制重新绘制一次
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            hitEnemyTank();//判断我方子弹是否击中敌方坦克
            hitMyTank();//判断敌方子弹是否击中我方坦克
            hitBlock(myTank.bullets);//判定我方子弹是否击中方块
            this.repaint();//重绘
        }
    }
}