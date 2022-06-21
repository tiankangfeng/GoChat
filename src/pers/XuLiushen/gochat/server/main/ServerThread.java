package pers.XuLiushen.gochat.server.main;

import java.io.*;
import java.net.Socket;

import pers.XuLiushen.gochat.MSG.MSGTools.MSGDao;
import pers.XuLiushen.gochat.MSG.MSGType.*;
import pers.XuLiushen.gochat.server.bean.*;
import pers.XuLiushen.gochat.server.dao.*;
import pers.XuLiushen.gochat.server.view.ServerView;

/**
 * 服务器处理客户端通信线程类
 * @author XuLiushen
 * @Time 2021/5/21
 */
public class ServerThread extends Thread {
    //用户连接套接字
    private Socket client;
    //该线程处理的用户对象
    private User user;
    //聊天的输入输出流
    private InputStream ins;
    private OutputStream ous;
    //包装后的输入输出流
    private DataInputStream din;
    private DataOutputStream dou;

    /**
     * 构造函数(获得连接对象和输入输出流)
     * @param client
     * @throws IOException
     */
    public ServerThread(Socket client) throws IOException {
        this.client = client;
        ins = client.getInputStream();
        ous = client.getOutputStream();
        din = new DataInputStream(ins);
        dou = new DataOutputStream(ous);
    }

    /**
     * 重写run方法
     */
    @Override
    public void run() {
        //处理该用户
        try {
            processClient();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理该用户首次连接（注册或者登录请求）
     * @throws IOException
     */
    public void processClient() throws IOException {
        while(true) {
            //读消息头
            MSGHead msg = MSGDao.readMsgHead(din);
            int msgType = msg.getType();
            switch (msgType) {
                //注册消息
                case 0x01:{
                    //注册，得到注册昵称密码，响应返回分配的账号
                    //强制转换
                    RegMsg rm = (RegMsg) msg;
                    //获得昵称
                    String nickname = rm.getNickname();
                    //获得密码
                    String pwd = rm.getPwd();
                    //注册并获得账号
                    user = new User();
                    user.setnickName(nickname);
                    user.setPWD(pwd);
                    //注册用户并获得账号
                    int accNum = UserDao.Register(user);
                    //发送给用户注册应答消息
                    byte type = 0x11, state = 0;
                    RegResMsg rrm = new RegResMsg(13 + 1, type, accNum, 0, state);
                    //打包消息
                    byte[] data = MSGDao.packMsg(rrm);
                    dou.write(data);
                    dou.flush();
                    break;
                }
                //登陆消息
                case 0x02:{
                    //登录，得到登录的用户名，密码，核对是否正确并响应是否成功
                    LoginMsg lm = (LoginMsg) msg;
                    //账号
                    int accNum = lm.getAccNum();
                    //密码
                    String pwd = lm.getPwd();
                    user = new User();
                    user.setAccNum(accNum);
                    user.setPWD(pwd);
                    byte type = 0x12, state;
                    if (!UserDao.checkLogin(this, user)) {
                        //登陆失败，结束函数;
                        state = 1;
                        //发送登陆失败应答消息
                        LoginResMsg lrm = new LoginResMsg(13 + 1, type, 0, 0, state);
                        byte[] data = MSGDao.packMsg(lrm);
                        dou.write(data);
                        dou.flush();
                        return;
                    } else {
                        //开始通信
                        user = new User();
                        user.setAccNum(accNum);
                        user.setPWD(pwd);
                        user.setAddress(client.getRemoteSocketAddress().toString());
                        //在线用户列表中加入该用户的线程对象
                        UserDao.addOnlineUser(this);
                        //登陆成功，发送登录成功消息，与用户通信
                        state = 0;
                        LoginResMsg lrm = new LoginResMsg(13 + 1, type, 0, 0, state);
                        byte[] data = MSGDao.packMsg(lrm);
                        dou.write(data);
                        dou.flush();
                        beginChat();
                    }
                    break;
                }
                //客户端主动连接关闭消息
                case 0x20:{
                    ServerView.showInfo("客户" + client.getRemoteSocketAddress() + "连接断开");
                    //回馈给客户端
                    byte[] data = MSGDao.packMsg(msg);
                    dou.write(data);
                    dou.flush();
                    //关闭socket
                    client.close();
                    ServerView.showInfo("关闭线程,关闭Socket");
                    //停止处理该客户对象
                    return;
                }
                default:{
                    System.out.println("没有如果");
                }
            }
        }
    }

    /**
     *     与用户正式通信
     */
    public void beginChat() throws IOException{
        ServerView.showInfo("正式聊天");
        while(true){
            MSGHead msg=MSGDao.readMsgHead(din);
            int type = msg.getType();
            switch (type) {
                //文本消息
                case 0x06: {
                    TextMsg tm = (TextMsg) msg;
                    //控制台提示信息
                    ServerView.showInfo("接收到来自账号" + tm.getSender() + "发给" + tm.getDest() + "的消息：" + tm.getmsgContent());
                    //获取转发线程对象
                    ServerThread st = UserDao.getDest(tm.getDest());
                    if(st != null) {
                        //打包消息成为字节数组
                        byte[] data = MSGDao.packMsg(tm);
                        //转发消息
                        st.sendData(data);
                        //用户不存在或在线
                    }else{
                        destNotExit();
                    }
                    break;
                }
                //文件消息
                case 0x07: {
                    FileMsg fm = (FileMsg) msg;
                    //将文件发送给指定客户
                    System.out.println("收到来自用户" + fm.getSender() + "发给" + fm.getDest() + "的文件：" + fm.getFileName());
                    //获取转发线程对象
                    ServerThread st = UserDao.getDest(fm.getDest());
                    if(st != null) {
                        //打包消息成为字节数组
                        byte[] data = MSGDao.packMsg(fm);
                        //转发消息
                        st.sendData(data);
                    }else{//用户不存在或在线
                        destNotExit();
                    }
                    break;
                }
                //关闭消息
                case 0x20: {
                    OffServiceMsg osm = (OffServiceMsg) msg;
                    ServerView.showInfo("收到来自用户" + osm.getSender() + "的关闭连接消息" + osm.getNotice());
                    //客户端主动断开连接，回馈断开消息给客户端
                    if (osm.getSender() == 0) {
                        byte[] data = MSGDao.packMsg(osm);
                        dou.write(data);
                        dou.flush();
                    }
                    //关闭socket
                    client.close();
                    System.out.println("关闭线程,关闭Socket");
                    //通知UserDao下线，移除在线用户
                    UserDao.removeOfflineUser(this);
                    //停止读取消息
                    return;
                }
                default: {
                    //未知数据包
                    System.out.println("接收到未知数据包！");
                }
            }
        }
    }

    /**
     * 发送打包后的消息（字节数组）给对应的用户对象
     * @param data 字节数组
     */
    public void sendData(byte[] data){
        try {
            dou.write(data);
            dou.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送用户不存在
     */
    public void destNotExit(){
        try {
            String notice = "用户不存在或不在线";
            ServerView.showInfo(notice);
            byte[] data = notice.getBytes();
            int len = 13 + data.length;
            byte msgType = 0x06;
            TextMsg tmsg = new TextMsg(len, msgType, user.getAccNum(), 0, notice);
            byte[] packedData = MSGDao.packMsg(tmsg);
            this.sendData(packedData);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     *    服务器端主动关闭该用户
     */
    public void closeMe(String notice, int problem) throws IOException{
        byte type=0x20;
        int len= 13+notice.getBytes().length;
        OffServiceMsg osm= new OffServiceMsg(len,type,problem,1,notice);
        byte []data=MSGDao.packMsg(osm);
        dou.write(data);
        dou.flush();
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return user;
    }

    public Socket getClient() {
        return client;
    }

}

