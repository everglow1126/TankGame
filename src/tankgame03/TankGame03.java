package tankgame03;


import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import static tankgame03.Recorder.record;

public class TankGame03 extends JFrame implements KeyListener {
    //面板属性默认为空
    MyPanel myPanel = null;
    static int continue_ = 0;

    public static void main(String[] args) throws IOException, ClassNotFoundException {


        new TankGame03();
    }

    public TankGame03() throws IOException, ClassNotFoundException {
        System.out.println("请输入选择 ：1.新游戏    2.继续上局游戏");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();
        if (choice == 1) {
            continue_ = -1;
        } else {
            continue_ = 1;
        }
        myPanel = new MyPanel();//创建面板
        Thread thread = new Thread(myPanel);
        thread.start();
        add(myPanel);//加入面板(游戏的绘图区域)到窗口
        addKeyListener(myPanel);
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Recorder.record();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_C) {
            continue_ = 1;
        }
        if (e.getKeyCode() == KeyEvent.VK_N) {
            continue_ = -1;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
