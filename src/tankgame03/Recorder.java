package tankgame03;

import java.io.*;
import java.util.Vector;

public class Recorder {
    public static ObjectOutputStream oos = null;
    public static int hitEnemyTank = 0; //击中敌方坦克数量
    public static FileOutputStream fos = null;
    public static FileInputStream fis = null;
    public static ObjectInputStream ois = null;
    public static int leftEnemyTank = 0;//上局剩余坦克数量
    //把本局游戏的所有记录存盘的方法
    public static void record() throws IOException {
        fos = new FileOutputStream("/Users/everglow/Desktop/TankGame.dot");
        oos = new ObjectOutputStream(fos);
        leftEnemyTank = MyPanel.enemyTanksNumber - hitEnemyTank;
        oos.writeInt(leftEnemyTank);//剩余敌方坦克的数量
        oos.writeInt(hitEnemyTank);//击中敌方坦克的数量
        Vector<EnemyTank> tanks = MyPanel.enemyTanks;
        for (int i = 0; i < tanks.size(); i++) {
            oos.writeObject(tanks.get(i));//所有敌方坦克的信息通过对象流存储到文件
        }
        oos.writeObject(MyPanel.myTank);//我方坦克的信息通过对象流存储到文件
        oos.close();
    }
    //读取上局游戏的所有信息
    public static void readRecord() throws IOException, ClassNotFoundException {
        fis = new FileInputStream("/Users/everglow/Desktop/TankGame.dot");
        ois = new ObjectInputStream(fis);
        MyPanel.enemyTanksNumber = ois.readInt();
        hitEnemyTank = ois.readInt();
        Tank tank = null;
        for (int i = 0; i < MyPanel.enemyTanksNumber; i++) {
            tank = (EnemyTank)ois.readObject();
            MyPanel.enemyTanks.add((EnemyTank) tank);
            MyPanel.allTanks.add((EnemyTank)tank);
        }
        tank = (MyTank) ois.readObject();
        MyPanel.myTank = (MyTank) tank;
        MyPanel.allTanks.add((MyTank)tank);
    }
    //记录击中敌方坦克的数量
    public static void setHitEnemyTank(){
        hitEnemyTank++;
    }
}
