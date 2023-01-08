package tankgame03;

import java.io.File;
import java.io.Serializable;
import java.util.Vector;

public class Tank implements Serializable {
    int x;  //坦克横坐标
    int y;  //坦克纵坐标
    int direction = 0; //坦克的方向0 1 2 3 对应上左下右,默认为向上
    int speed = 5; //坦克速度 默认为5
    Vector<Bullet> bullets = new Vector<>();//Vector存放坦克子弹
    boolean isLive = true;//坦克是否存活
    boolean move = true;
    Vector<Tank> tanks = new Vector<>();

    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Tank(Vector<Tank> tanks) {
        this.tanks = tanks;
    }

    public void shot() {
        Bullet bullet = null;
        //判断子弹方向创建子弹对象
        switch (getDirection()) {
            case 0:
                bullet = new Bullet(getX() + 20, getY() - 5, 0);
                break;
            case 1:
                bullet = new Bullet(getX() - 5, getY() + 20, 1);
                break;
            case 2:
                bullet = new Bullet(getX() + 20, getY() + 65, 2);
                break;
            case 3:
                bullet = new Bullet(getX() + 65, getY() + 20, 3);
                break;
        }
        //将创建的子弹放入子弹集合
        bullets.add(bullet);
        //启动子弹线程
        new Thread(bullet).start();
        //爆炸音效
        new AePlayWave("src/shot.wav").start();
    }

    public boolean inBound() {
        switch (direction) {
            case 0:
                if (getY() - 5 < 0) {
                    return false;
                }
                break;
            case 1:
                if (getX() - 5 < 0) {
                    return false;
                }
                break;
            case 2:
                if (getY() + 65 > 750) {
                    return false;
                }
                break;
            case 3:
                if (getX() + 65 > 1000) {
                    return false;
                }
        }
        return true;
    }

    public boolean isTouchBlocks(int length, int w) {
        Block b;
        for (int i = 0; i < MyPanel.blocks.size(); i++) {
            b = MyPanel.blocks.get(i);
            switch (direction) {
                case 0:
                    if ((x > b.x && x < b.x + length && y < b.y + w && y > b.y)
                            || (x + 40 > b.x && x + 40 < b.x + length && y < b.y + w && y > b.y)) {
                        return false;
                    }
                    break;
                case 2:
                    if ((x > b.x && x < b.x + length && y + 62 < b.y + w && y + 62 > b.y)
                            || (x + 40 > b.x && x + 40 < b.x + length && y + 62 < b.y + w && y + 62 > b.y)) {
                        return false;
                    }
                    break;
                case 1:
                    if ((x > b.x && x < b.x + length && y < b.y + 42 && y > b.y)
                            || (x > b.x && x < b.x + length && y + 40 < b.y + w && y + 40 > b.y)) {
                        return false;
                    }
                    break;
                case 3:
                    if ((x + 60 > b.x && x + 60 < b.x + length && y < b.y + 42 && y > b.y)
                            || (x + 60 > b.x && x + 60 < b.x + length && y + 40 < b.y + w && y + 40 > b.y)) {
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    public boolean isTouchTanks() {
        Tank tank;
        for (int i = 0; i < MyPanel.allTanks.size(); i++) {
            tank = MyPanel.allTanks.get(i);
            if (this != tank) {
                switch (direction) {
                    case 0:
                        switch (tank.direction) {
                            case 0:
                            case 2:
                                if ((x > tank.x && x < tank.x + 40 && y < tank.y + 60 && y > tank.y)
                                        || (x + 40 > tank.x && x + 40 < tank.x + 40 && y < tank.y + 60 && y > tank.y)) {
                                    return false;
                                }
                                break;
                            case 1:
                            case 3:
                                if ((x > tank.x && x < tank.x + 60 && y < tank.y + 40 && y > tank.y)
                                        || (x + 40 > tank.x && x + 40 < tank.x + 60 && y < tank.y + 40 && y > tank.y)) {
                                    return false;
                                }
                        }
                    case 1:
                        switch (tank.direction) {
                            case 0:
                            case 1:
                                if ((x > tank.x && x < tank.x + 40 && y < tank.y + 60 && y > tank.y)
                                        || (x > tank.x && x < tank.x + 40 && y + 40 < tank.y + 60 && y + 40 > tank.y)) {
                                    return false;
                                }
                                break;
                            case 2:
                            case 3:
                                if ((x > tank.x && x < tank.x + 60 && y < tank.y + 40 && y > tank.y)
                                        || (x > tank.x && x < tank.x + 60 && y + 40 < tank.y + 40 && y + 40 > tank.y)) {
                                    return false;
                                }
                        }
                        break;
                    case 2:
                        switch (tank.direction) {
                            case 0:
                            case 1:
                                if ((x > tank.x && x < tank.x + 40 && y + 60 < tank.y + 60 && y + 60 > tank.y)
                                        || (x + 40 > tank.x && x + 40 < tank.x + 40 && y + 60 < tank.y + 60 && y + 60 > tank.y)) {
                                    return false;
                                }
                                break;
                            case 2:
                            case 3:
                                if ((x > tank.x && x < tank.x + 60 && y + 60 < tank.y + 40 && y + 60 > tank.y)
                                        || (x + 40 > tank.x && x + 40 < tank.x + 60 && y + 60 < tank.y + 40 && y + 60 > tank.y)) {
                                    return false;
                                }
                        }
                        break;
                    case 3:
                        switch (tank.direction) {
                            case 0:
                            case 1:
                                if ((x + 60 > tank.x && x + 60 < tank.x + 40 && y < tank.y + 60 && y > tank.y)
                                        || (x + 60 > tank.x && x + 60 < tank.x + 40 && y + 40 < tank.y + 60 && y + 40 > tank.y)) {
                                    return false;
                                }
                                break;
                            case 2:
                            case 3:
                                if ((x + 60 > tank.x && x + 60 < tank.x + 60 && y < tank.y + 40 && y > tank.y)
                                        || (x + 60 > tank.x && x + 60 < tank.x + 60 && y + 40 < tank.y + 40 && y + 40 > tank.y)) {
                                    return false;
                                }
                        }
                        break;
                }
            }
        }
        return true;
    }

    public void moveUp() {
        if (inBound()) {
            y -= speed;
        }
    }

    public void moveLeft() {
        if (inBound()) {
            x -= speed;
        }
    }

    public void moveDown() {
        if (inBound()) {
            y += speed;
        }
    }

    public void moveRight() {
        if (inBound()) {
            x += speed;
        }
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    public int getX() {
        return x;
    }

    public int getSpeed() {
        return speed;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
