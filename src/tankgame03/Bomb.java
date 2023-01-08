package tankgame03;

public class Bomb {
    int x,y; //炸弹坐标
    boolean isLive = true;//炸弹是否还存在
    int life = 9;//炸弹生命周期
    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;

    }
    //用来制造动画效果
    public void lifeDecrease(){
        if (life > 0){
            life--;
        }else {
            isLive = false;
        }
    }
}
