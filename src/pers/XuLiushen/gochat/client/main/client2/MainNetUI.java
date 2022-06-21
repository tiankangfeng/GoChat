package pers.XuLiushen.gochat.client.main.client2;

import pers.XuLiushen.gochat.client.main.ChatClient;

import java.io.IOException;

public class MainNetUI {
    private ChatClient chatter;

    /**
     * 构造函数
     * @param ip 服务器ip地址
     * @param port 服务器端口号
     */
    public MainNetUI(String ip, int port) throws IOException {
        chatter= new ChatClient(ip, port);
        chatter.conn2server();
        //chatter.start();
        //chatter.register("花花", "123");
        //chatter.closeMe("客户端关闭连接");
    }

    public static void main(String[]args) throws IOException{
        MainNetUI chatter= new MainNetUI("localhost",11111);
    }
}

