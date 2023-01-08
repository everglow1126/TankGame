package tankgame03;

import java.io.Serializable;

class Bullet implements Runnable, Serializable {

    private int x;//子弹坐标
    private int y;//子弹是否射出边界
    boolean isLive = true;//子弹是否存活
    private int speed = 6; //子弹速度
    private int direction;//子弹方向

    public Bullet(int x, int y, int direction) {
        this.x = x;
        this.y = y;
        this.direction = direction;
    }


    public int getX() {
        return x;
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

    //子弹射击的线程
    @Override
    public void run() {
        while (true) {
            try {
                //休眠50ms
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //根据子弹方向移动
            switch (direction) {
                case 0:
                    y -= speed;
                    break;
                case 1:
                    x -= speed;
                    break;
                case 2:
                    y += speed;
                    break;
                case 3:
                    x += speed;
                    break;
            }
            //判断子弹是否走出边界，子弹生命已结束
            if (!(x <= 1000 && x >= 0 && y <= 750 && y >= 0 && isLive)) {
                isLive = false;
                break;
            }
        }
    }
}
