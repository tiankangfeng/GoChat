package pers.XuLiushen.gochat.client.view;

import pers.XuLiushen.gochat.client.main.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * 客户端视图层
 * @author XuLiushen
 * @Time 2021/5/21
 */
public class ClientView extends Thread{
    private ChatClient chatter;//客户端逻辑层对象
    private JFrame loginUI;//登录界面

    public ClientView(ChatClient chatter){
        this.chatter = chatter;
        this.start();
    }

    @Override
    public void run() {
        showLoginFrame();
    }

    public void showLoginFrame(){
        //标题
        JFrame loginFrame= new JFrame("计科1805徐柳深通信应用客户端");
        //大小
        loginFrame.setSize(400,250);
        //位置默认中间
        loginFrame.setLocationRelativeTo(null);
        //默认关闭操作，退出程序
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //大小不可调节
        loginFrame.setResizable(false);
        //设置字体
        loginFrame.setFont(new Font("宋体", Font.PLAIN,14));
        //实例化FlowLayout流式布局类的对象，指定对齐方式为居中对齐组件之间的间隔为10个像素
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,10,10);
        //实例化流式布局类的对象
        loginFrame.setLayout(fl);

        //实例化JLabel标签对象，该对象显示“账号”
        JLabel labname = new JLabel("账号：");
        labname.setFont(new Font("宋体",Font.PLAIN,14));
        //将labname标签添加到窗体上
        loginFrame.add(labname);

        //实例化JTextField标签对象化
        JTextField text_name = new JTextField();
        Dimension dim1 = new Dimension(300,30);
        text_name.setPreferredSize(dim1);//设置除顶级容器组件以外其他组件的大小
        //将textName标签添加到窗体上
        loginFrame.add(text_name);

        //实例化JLabel标签对象，该对象显示“密码”
        JLabel labpass = new JLabel("密码：");
        labpass.setFont(new Font("宋体",Font.PLAIN,14));
        //将labpass添加到窗体上
        loginFrame.add(labpass);

        //实例化JPasswordField
        JPasswordField text_password = new JPasswordField();
        //设置大小
        text_password.setPreferredSize(dim1);
        //添加到窗体
        loginFrame.add(text_password);

        //实例化JButton组件
        //登录按钮
        JButton loginBtn = new JButton();
        //设置按键的显示内容
        Dimension dim2 = new Dimension(300,30);
        loginBtn.setText("登录");
        loginBtn.setFont(new Font("宋体",Font.PLAIN,14));
        //设置按键大小
        loginBtn.setSize(dim2);
        loginFrame.add(loginBtn);
        //注册按钮
        JButton regBtn = new JButton();
        //设置按键的显示内容
        regBtn.setText("注册");
        regBtn.setFont(new Font("宋体",Font.PLAIN,14));
        //设置按键大小
        regBtn.setSize(dim2);
        loginFrame.add(regBtn);

        //显示
        loginFrame.setVisible(true);

        //添加登录按钮监听器
        loginBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //获取输入的账号和密码
                int accNum = Integer.parseInt(text_name.getText().trim());
                String password = new String(text_password.getPassword());
                //调用逻辑层的登录方法
                chatter.loginServer(accNum,password);
                //关闭界面
                loginFrame.setVisible(false);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //添加注册按钮监听器
        regBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //鼠标点击，显示注册界面
                RegUI regUI = new RegUI(chatter);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        //添加窗口监听器
        loginFrame.addWindowListener(new WindowListener(){
            //窗口打开事件
            public void windowOpened(WindowEvent e) {
                chatter.conn2server();
            }
            //窗口关闭事件
            public void windowClosing(WindowEvent e) {
                chatter.closeMe("客户端窗口关闭");
            }
            public void windowClosed(WindowEvent e) {}
            public void windowIconified(WindowEvent e) {}
            public void windowDeiconified(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {}
        });



    }
    /**
     * 向控制台输出信息
     * @param notice 要输出的信息
     */
    public static void showInfo(String notice){
        System.out.println(notice);
    }
}
