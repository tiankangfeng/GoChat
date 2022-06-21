package pers.XuLiushen.gochat.server.main;

import pers.XuLiushen.gochat.server.view.ServerView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 服务器类(放在独立的线程中)
 * @author XuLiushen
 * @Time 20201/5/20
 */
public class ChatServer extends Thread{
    /** 服务器端口 */
    int port;
    /** 服务器对象 */
    ServerSocket sc;

    /**
     * 构造函数
     * @param port 客户端与服务器连接的端口号
     */
    public ChatServer(int port){
        this.port=port;
    }

    /**
     * 创建服务器对象，运行服务器线程
     * @throws IOException
     */
    public void setUpServer(){
        try {
            sc= new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //开启服务器线程
        this.start();
    }

    /**
     *     重写run函数，注意要把accepct方法放到run函数中调用。
     */

    @Override
    public void run(){
        ServerView.showInfo("服务器开始运行");
        try {
            //不断等待用户的连接
            while(true){
                //获得连接的用户对象
                Socket client= sc.accept();
                //输出提示接收到服务器连接
                ServerView.showInfo("服务器接受到客户端连接："+client.getRemoteSocketAddress());
                //建立线程对象处理该用户，避免阻塞下一个用户的连接
                ServerThread st= new ServerThread (client);
                //启动用户对象线程
                st.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        ChatServer server = new ChatServer(11111);
        server.setUpServer();
    }
}
