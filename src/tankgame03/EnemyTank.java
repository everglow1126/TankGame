package tankgame03;

import java.util.Vector;

public class EnemyTank extends Tank implements Runnable {


    public EnemyTank(int x, int y) {
        super(x, y);
        setSpeed(2);
        setDirection(2);
    }

    @Override
    public void run() {
        while (true) {
            //如果敌方坦克子弹小于2颗就创建子弹
            if (bullets.size() <= 2 && isLive) {
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                //shot();
            }
            //通过敌方坦克方向，使坦克移动
            switch (getDirection()) {
                case 0:
                    //移动30步再更改随机方向
                    for (int i = 0; i < 30; i++) {
                        if (isTouchTanks() && isTouchBlocks(14 * 40, 42)) {
                            moveUp();
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    break;
                case 1:
                    for (int i = 0; i < 30; i++) {
                        if (isTouchTanks() && isTouchBlocks(14 * 40, 42)) {
                            moveLeft();
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i < 30; i++) {
                        if (isTouchTanks() && isTouchBlocks(14 * 40, 42)) {
                            moveDown();
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    break;
                case 3:
                    for (int i = 0; i < 30; i++) {
                        if (isTouchTanks() && isTouchBlocks(14 * 40, 42)) {
                            moveRight();
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                    break;
            }
            //随机设置敌方坦克移动方向[0,3]
            setDirection((int) (Math.random() * 4));
            if (!isLive) {
                break;
            }
        }
    }
}
