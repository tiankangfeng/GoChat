package pers.XuLiushen.gochat.client.main;
import pers.XuLiushen.gochat.MSG.MSGType.*;
import pers.XuLiushen.gochat.MSG.MSGTools.*;
import pers.XuLiushen.gochat.client.bean.User;
import pers.XuLiushen.gochat.client.dao.ClientDao;
import pers.XuLiushen.gochat.client.view.ClientChatView;
import pers.XuLiushen.gochat.client.view.ClientView;
import java.io.*;
import java.net.Socket;

/**
 * 客户端逻辑层
 * @author Xuliushen
 * @Time 2021/5/21
 */
public class ChatClient extends Thread {
    /**客户端数据处理对象*/
    private ClientDao clientDao;
    /**客户端视图对象*/
    private ClientView clientView;
    /**客户端聊天视图对象*/
    private ClientChatView clientChatView;
    /**连接的服务器的ip和端口*/
    private String serverIp;
    private int serverPort;
    /** 与服务器连接的对象 */
    private Socket client;
    /** 聊天的输入输出流 */
    private InputStream ins;
    private OutputStream ous;
    /** 包装后的输入输出流 */
    private DataInputStream din;
    private DataOutputStream dou;

    /**
     * 通过给定的ip和端口建立客户对象
     * @param ip 服务器ip地址
     * @param port 服务器端口号
     */
    public ChatClient(String ip, int port){
        //初始化数据层对象
        clientDao = new ClientDao();
        //初始化视图层对象
        clientView = new ClientView(this);
        this.serverIp=ip;
        this.serverPort=port;
    }

    /**
     *  重写Run函数,读取服务器消息
     */
    @Override
    public void run(){
        try {
            readFromServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *  连接服务器
     */
    public boolean conn2server(){
        try {
            client= new Socket(this.serverIp,this.serverPort);
            ins=client.getInputStream();
            ous=client.getOutputStream();
            din=new DataInputStream(ins);
            dou= new DataOutputStream(ous);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *  注册账号
     * @param nickname 昵称
     * @param pwd 密码
     */
//    public void register(String nickname, String pwd){
//        //设置用户对象部分信息
//        User user = new User();
//        user.setNickname(nickname);
//        user.setPwd(pwd);
//        clientDao.setUser(user);
//        //发送注册消息到服务器
//        int len= 13+10+10;
//        byte type=0x01;
//        RegMsg mg= new RegMsg(len,type,0,0,nickname,pwd);
//        try {
//            byte []data= MSGDao.packMsg(mg);
//            dou.write(data);
//            dou.flush();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//
//    }

    /** 注册账号,得到账号 */
    public void register(String nickname, String pwd){
        //设置用户对象部分信息
        User user = new User();
        user.setNickname(nickname);
        user.setPwd(pwd);
        clientDao.setUser(user);
        try {
            //发送注册消息到服务器
            int len = 13 + 10 + 10;
            byte type = 0x01;
            RegMsg mg = new RegMsg(len, type, 0, 0, nickname, pwd);
            byte[] data = MSGDao.packMsg(mg);
            dou.write(data);
            dou.flush();
            //3.等待服务器的响应
            RegResMsg rrm = (RegResMsg) MSGDao.readMsgHead(din);
            byte state = rrm.getState();
            if (state == 0) {
                int accNum = rrm.getDest();
                user.setAccNum(accNum);
                ClientView.showInfo("注册成功，您的JK号：" + accNum);
            } else {
                ClientView.showInfo("注册失败");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

//    public void loginServer(int accNum, String pwd){
//        //1.设置用户对象的部分消息
//        User user = new User();
//        user.setAccNum(accNum);
//        user.setPwd(pwd);
//        if(clientDao.getUser() == null)
//            clientDao.setUser(user);
//        //2.发送登录请求消息给客户端
//        int len=13+4+10;
//        byte type=0x02;
//        LoginMsg lg = new LoginMsg(len,type,0,0,accNum,pwd);//登录请求消息
//        try {
//            byte[] data = MSGDao.packMsg(lg);//消息打包
//            dou.write(data);//发送消息
//            dou.flush();
//        }catch (IOException e){
//            e.printStackTrace();
//        }
//    }
    /**
     * 登陆服务器
     * @param accNum 账号
     * @param pwd 密码
     * @throws IOException
     */
    public void loginServer(int accNum, String pwd){
        //1.设置用户对象的部分消息
        User user = new User();
        user.setAccNum(accNum);
        user.setPwd(pwd);
        if(clientDao.getUser() == null) {
            clientDao.setUser(user);
        }
        try {
            //2，发送登录请求消息给服务器端
            int len= 13+4+10;
            byte type=0x02;
            LoginMsg lg = new LoginMsg(len,type,0,0,accNum,pwd);
            byte []data=MSGDao.packMsg(lg);
            dou.write(data);
            dou.flush();
            //3.等待服务器端的登录请求响应消息
            LoginResMsg lrm=(LoginResMsg)MSGDao.readMsgHead(din);
            byte state=lrm.getState();
            if(state==0){
                ClientView.showInfo("登录成功！");
                this.start();//开启线程
                //显示聊天界面
                clientChatView = new ClientChatView(this);
            }
            else{
                ClientView.showInfo("登录失败！账号或密码错误");
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 读取服务器消息
     * @throws IOException
     */
    public void readFromServer() throws IOException {
        //不停从服务器读取消息
        while (true) {
            //读取消息头
            MSGHead msg = MSGDao.readMsgHead(din);
            //消息类型
            byte type = msg.getType();
            switch (type) {
//                case 0x11: {//注册应答消息
//                    RegResMsg rrm = (RegResMsg) msg;
//                    byte state = rrm.getState();
//                    if (state == 0) {//注册成功
//                        int accNum = rrm.getDest();
//                        //设置数据层的用户对象的账号
//                        User user = clientDao.getUser();
//                        user.setAccNum(accNum);
//                        ClientView.showInfo("注册成功，您的账号：" + accNum);
//                    } else {//注册失败
//                        ClientView.showInfo("注册失败");
//                    }
//                    break;
//                }
//                case 0x12: {//登录应答消息
//                    LoginResMsg lrm = (LoginResMsg) msg;
//                    byte state = lrm.getState();
//                    if (state == 0) {//登录成功
//                        ClientView.showInfo("登陆成功！");
//                    } else {
//                        ClientView.showInfo("登陆失败！");
//                    }
//                    break;
//                }
                //普通文本消息
                case 0x06: {
                    TextMsg tm = (TextMsg) msg;
                    int from = tm.getSender();
                    String msgContent = tm.getmsgContent();
                    //显示到聊天界面
                    clientChatView.showReceiveMessage(from,msgContent);
                    ClientView.showInfo("收到来自" + from + "的消息：" + msgContent);
                    break;
                }
                //文件消息
                case 0x07: {
                    FileMsg fm = (FileMsg) msg;
                    int from = fm.getSender();
                    ClientView.showInfo("收到来自" + from + "的文件");
                    clientChatView.showReceiveMessage(from,":发送了文件");
                    String FileName = fm.getFileName();
                    byte[] FileData = fm.getFileData();
                    saveFile(from,FileName, FileData);
                    break;
                }
                //断开连接消息
                case 0x20: {
                    OffServiceMsg osm = (OffServiceMsg) msg;
                    System.out.println(osm.getNotice());
                    //服务端主动断开,回馈给服务器
                    if (osm.getSender() == 1) {
                        byte[] data = MSGDao.packMsg(osm);
                        dou.write(data);
                        dou.flush();
                    }
                    //关闭Socket
                    client.close();
                    //结束消息读取
                    return;
                }
                default: {
                    System.out.println("pass");
                }
            }
        }
    }

    /**
     * 发送文本信息给dest账号
     * @param msg 消息内容
     * @param dest 目的
     * @throws IOException
     */
    public void sendMSG(String msg,int dest){
        try {
            byte[] data = msg.getBytes();
            int len = 13 + data.length;
            byte type = 0x06;
            int src = clientDao.getUser().getAccNum();
            //文本消息
            TextMsg tm = new TextMsg(len, type, dest, src, msg);
            //消息打包
            byte[] text = MSGDao.packMsg(tm);
            dou.write(text);
            dou.flush();
            //显示到聊天界面
            clientChatView.showSendMessage(dest,msg);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     *  发送文件信息
     * @param fileName 文件名
     * @param dest 目的账号
     */
    public void sendFile(String fileName,int dest){
        try {
            //文件对象
            File file = new File(fileName);
            //文件名
            String name = file.getName();
            //文件输入流
            FileInputStream fin = new FileInputStream(file);
            int len = fin.available();
            //文件数据
            byte[] FileData = new byte[len];
            fin.read(FileData);
            fin.close();
            byte type = 0x07;
            FileMsg fm = new FileMsg(13 + 256 + len, type, dest, clientDao.getUser().getAccNum(), name, FileData);
            byte[] data = MSGDao.packMsg(fm);
            dou.write(data);
            dou.flush();
            //显示到界面
            clientChatView.showSendMessage(dest,fileName);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * 保存文件
     * @param fileName 文件名
     * @param fileData 文件数据
     * @throws IOException
     */
    public void saveFile(int from, String fileName,byte[]fileData) throws IOException{
        fileName = clientDao.getPath()+fileName;
        File file= new File(fileName);
        FileOutputStream fou= new FileOutputStream(file.getPath());
        //将文件数据输出到文件
        fou.write(fileData);
        clientChatView.showMessage("来自"+from+"的文件"+fileName+"保存完毕！");
        ClientView.showInfo("来自"+from+"的文件"+fileName+"保存完毕！");
    }

    /**
     * 发送意外或者手动退出消息
     */
    public void closeMe(String notice) {
        byte[]data=notice.getBytes();
        byte type=0x20;
        int len=13+data.length;
        OffServiceMsg osm= new OffServiceMsg(len,type,0,0,notice);
        try {
            //打包
            byte []msg=MSGDao.packMsg(osm);
            dou.write(msg);
            dou.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}