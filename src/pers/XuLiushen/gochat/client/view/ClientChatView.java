package pers.XuLiushen.gochat.client.view;

import pers.XuLiushen.gochat.client.main.ChatClient;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 * 聊天界面类
 * @author XuLiushen
 * @Time 2021/5/25
 */
public class ClientChatView extends JFrame {

    JFrame clientChatView = null;
    private ChatClient chatter;//客户端逻辑层对象
    private JTextArea jta = null;//消息显示文本框

    public ClientChatView(ChatClient chatter){
        clientChatView = this;
        this.chatter = chatter;
        showChatView();
    }

    /**
     * 显示聊天界面
     */
    public void showChatView(){
        //标题
        this.setTitle("聊天界面");
        //位置默认中间
        this.setLocationRelativeTo(null);
        //默认关闭操作，退出程序
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //大小可调节
        this.setResizable(true);
        //设置字体
        this.setFont(new Font("宋体", Font.PLAIN,14));

        //消息显示区域
        JPanel jp_show = new JPanel();
        JPanel jp_enter = new JPanel();              //消息输入区域
        JPanel jp_center = new JPanel();             //右侧整体

        JMenuBar jmb = new JMenuBar();             //消息显示区上方菜单
        jmb.add(new JMenu("消息"));
        jmb.add(new JMenu("好友列表"));

        jta = new JTextArea();           //消息显示区文本框
        jta.setEditable(false);
        JScrollPane jsp = new JScrollPane(jta);    //消息显示区滚动条

        JTextArea jtas = new JTextArea();          //消息输入区文本框
        JScrollPane jsps = new JScrollPane(jtas);  //消息输入区滚动条
        JPanel jp_enter_top = new JPanel();        //消息输入区顶端
        JPanel jp_enter_bottom = new JPanel();     //消息输入区底端

        JTextArea jtacc = new JTextArea();         //账号输入文本框

        JLabel jl_acc = new JLabel("发送账号");           //发送账号
        JTextField jt_acc = new JTextField();                  //发送账号输入框
        jt_acc.setPreferredSize(new Dimension(70,30));
        JButton jbt_send = new JButton("发送");          //发送消息按钮
        JButton jbt_file = new JButton("发送文件");      //发送文件消息按钮

        this.setSize(550,400);
        this.setLayout(new BorderLayout(10,5));
        this.add(jp_center,BorderLayout.CENTER);

        //右侧布局
        jp_center.setLayout(new GridLayout(2,1));
        jp_center.add(jp_show);
        jp_center.add(jp_enter);

        //顶部消息显示框
        jp_show.setLayout(new BorderLayout());
        jp_show.add(jmb,BorderLayout.NORTH);
        jp_show.add(jsp,BorderLayout.CENTER);

        //下面消息输入框
        //输入框布局
        jp_enter.setLayout(new BorderLayout());
        jp_enter.add(jp_enter_top,BorderLayout.NORTH);
        jp_enter.add(jsps,BorderLayout.CENTER);
        jp_enter.add(jp_enter_bottom,BorderLayout.SOUTH);

        //消息输入框上方左边对齐
        jp_enter_top.setLayout(new FlowLayout(FlowLayout.LEFT));
        jp_enter_top.add(new JLabel(new ImageIcon("image/iconblue.png")));
        //消息输入框下方右边对齐
        jp_enter_bottom.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jp_enter_bottom.add(jl_acc);
        jp_enter_bottom.add(jt_acc);
        jp_enter_bottom.add(jbt_send);
        jp_enter_bottom.add(jbt_file);

        this.setVisible(true);

        //添加监听器
        //1.点击发送按钮发送消息
        jbt_send.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //1.获取输入的消息内容
                String content = jtas.getText().trim();
                if(content != null) {
                    //2，获取发送账号
                    String sDest = jt_acc.getText().trim();
                    if (sDest != null) {
                        int dest = Integer.parseInt(sDest);
                        //3.发送消息
                        chatter.sendMSG(content, dest);
                        //4.清空发送区域
                        jtas.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, "error",
                                "请输入发送账号", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "error",
                            "请输入消息内容", JOptionPane.ERROR_MESSAGE);
                }
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

        //2.点击发送文件按钮，发送文件
        jbt_file.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //1.获取发送账号
                String sDest = jt_acc.getText().trim();
                if (sDest != null) {
                    int dest = Integer.parseInt(sDest);
                    System.out.println("dest:"+dest);
                    //弹出文件选择框，获取文件路径
                    String path = null;
                    JFileChooser fileChooser = new JFileChooser();
                    FileSystemView fsv = FileSystemView.getFileSystemView();  //注意了，这里重要的一句
                    fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
                    fileChooser.setDialogTitle("请选择要发送的文件...");
                    fileChooser.setApproveButtonText("确定");
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    int result = fileChooser.showOpenDialog(clientChatView);
                    if (JFileChooser.APPROVE_OPTION == result) {
                        path=fileChooser.getSelectedFile().getPath();
                        System.out.println("path: "+path);
                    }
                    if(path != null) {
                        chatter.sendFile(path, dest);
                    } else {
                        JOptionPane.showMessageDialog(null, "error",
                                "请选择要发送的文件", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "error",
                            "请输入要发送的账号", JOptionPane.ERROR_MESSAGE);
                }
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

        //客户端关闭聊天界面则退出程序
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {
                chatter.closeMe("客户端关闭聊天界面");
            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

    }

    /**
     * 显示接收到的消息
     * @param from 发送方账号
     * @param msg
     */
    public void showReceiveMessage(int from, String msg){
        /** 服务器发来的消息 */
        if(from == 0){
            jta.append("系统提示：" + msg + "\n");
        }else {
            jta.append("me<-:" + from + ":" + msg + "\n");
        }
    }

    /**
     * 显示发送的消息
     * @param dest 发送对象
     * @param msg 消息内容
     */
    public void showSendMessage(int dest, String msg){
        jta.append("me->"+dest+":"+msg+"\n");
    }

    /**
     * 显示提示消息
     * @param msg
     */
    public void showMessage(String msg){
        jta.append(msg+"\n");
    }

}
