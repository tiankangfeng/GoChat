package pers.XuLiushen.gochat.client.view;

import pers.XuLiushen.gochat.client.main.ChatClient;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 注册界面
 * @author XuLiushen
 * @Time 2021/5/21
 */
public class RegUI extends JFrame {
    private ChatClient chatter;//用户逻辑层对象
    private RegUI regUI;

    public RegUI(ChatClient chatter){
        this.chatter = chatter;
        regUI = this;
        showUI();//展示界面
    }

    /**
     * 展示注册界面
     */
    public void showUI(){
        //标题
        this.setTitle("注册新用户");
        //大小
        this.setSize(400,250);
        //位置默认中间
        this.setLocationRelativeTo(null);
        //默认关闭操作,不做任何事情
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        //大小不可调节
        this.setResizable(false);
        //设置字体
        this.setFont(new Font("宋体", Font.PLAIN,14));
        //实例化FlowLayout流式布局类的对象，指定对齐方式为居中对齐组件之间的间隔为10个像素
        FlowLayout fl = new FlowLayout(FlowLayout.CENTER,10,10);
        //实例化流式布局类的对象
        this.setLayout(fl);

        //实例化JLabel标签对象，该对象显示“昵称”
        JLabel labname = new JLabel("昵称：");
        labname.setFont(new Font("宋体",Font.PLAIN,14));
        //将labname标签添加到窗体上
        this.add(labname);

        //实例化JTextField标签对象化
        JTextField text_name = new JTextField();
        Dimension dim1 = new Dimension(300,30);
        text_name.setPreferredSize(dim1);//设置除顶级容器组件以外其他组件的大小
        //将textName标签添加到窗体上
        this.add(text_name);

        //实例化JLabel标签对象，该对象显示“密码”
        JLabel labpass = new JLabel("密码：");
        labpass.setFont(new Font("宋体",Font.PLAIN,14));
        //将labpass添加到窗体上
        this.add(labpass);

        //实例化JPasswordField
        JPasswordField text_password = new JPasswordField();
        //设置大小
        text_password.setPreferredSize(dim1);
        //添加到窗体
        this.add(text_password);

        //实例化JButton组件
        //确认按钮
        JButton nextBtn = new JButton();
        //设置按键的显示内容
        Dimension dim2 = new Dimension(300,30);
        nextBtn.setText("确认");
        nextBtn.setFont(new Font("宋体",Font.PLAIN,14));
        //设置按键大小
        nextBtn.setSize(dim2);
        this.add(nextBtn);
        //取消按钮
        JButton cancelBtn = new JButton();
        //设置按键的显示内容
        cancelBtn.setText("取消");
        cancelBtn.setFont(new Font("宋体",Font.PLAIN,14));
        //设置按键大小
        cancelBtn.setSize(dim2);
        this.add(cancelBtn);

        //显示界面
        this.setVisible(true);

        //添加监听器
        //确认按钮监听器
        nextBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //获取输入的用户昵称和密码
                String nickname = text_name.getText().trim();
                String password = new String(text_password.getPassword()).trim();
                //调用用户逻辑层的注册方法
                chatter.register(nickname,password);
                //界面消失
                regUI.setVisible(false);
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

        //取消按钮监听器
        cancelBtn.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //界面消失
                regUI.setVisible(false);
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


    }

}
