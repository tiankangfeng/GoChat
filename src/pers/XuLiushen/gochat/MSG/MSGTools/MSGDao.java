package pers.XuLiushen.gochat.MSG.MSGTools;

import pers.XuLiushen.gochat.MSG.MSGType.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class MSGDao {

    /**
     *     构造方法私有，不允许构建该类的对象
     */
    private MSGDao(){}

    /**
     * 封装读消息头的方法
     * @param din 输入流
     * @return 返回读取到的消息对象
     * @throws IOException
     */
    public static MSGHead readMsgHead(DataInputStream din) throws IOException{
        //读总长
        int len=din.readInt();
        //记住务必要-4！已经读取了一个整数了！
        byte []data= new byte[len-4];
        din.readFully(data);
        //消息解包
        MSGHead msg= parseMsg(data);
        return msg;
    }

    /**
     *   消息解包，返回MSGHead对象
     * @param data 待解包的字节数据
     * @return 返回解包后的消息对象
     * @throws IOException
     */
    public static MSGHead parseMsg(byte[]data) throws IOException{
        //字节数组输入流
        ByteArrayInputStream bin= new ByteArrayInputStream(data);
        //包装输入流
        DataInputStream din = new DataInputStream(bin);
        int len= 4+data.length;
        byte type=din.readByte();
        int dest= din.readInt();
        int src= din.readInt();
        //注册请求消息
        if(type==0x01){
            byte[]data2=new byte[10];
            //昵称
            din.readFully(data2);
            String nickname=new String(data2).trim();
            //密码
            din.readFully(data2);
            String pwd=new String(data2).trim();
            System.out.println("MSGDao正在解包注册请求消息消息包,昵称:"+nickname+",密码:"+pwd);
            return new RegMsg(len,type,dest,src,nickname,pwd);
        }
        //登录请求消息
        else if(type==0x02){
            int accNum=din.readInt();
            byte[]data2= new byte[10];
            //密码
            din.readFully(data2);
            String pwd= new String(data2).trim();
            System.out.println("MSGDao正在解包登录请求消息消息包,账号："+accNum+",密码:"+pwd);
            return new LoginMsg(len,type,dest,src,accNum,pwd);

        }
        //注册应答消息
        else if(type==0x11){
            byte state=din.readByte();
            System.out.println("MSGDao正在解包注册应答消息包,state:"+state+",JKnum:"+dest);
            return new RegResMsg(len,type,dest,src,state);
        }
        //登录应答消息
        else if(type==0x12){
            byte state= din.readByte();
            System.out.println("MSGDao正在解包登录应答消息包,state:"+state);
            return new LoginResMsg(len,type,dest,src,state);
        }
        //普通文本消息
        else if(type==0x06){
            int l=len-13;
            byte[]content=new byte[l];
            din.readFully(content);
            String scontent=new String(content);
            System.out.println("MSGDao正在解包普通文本消息包msg:"+scontent);
            return new TextMsg(len,type,dest,src,scontent);
        }
        //文件消息
        else if(type==0x07){
            //文件名
            byte[]name= new byte[256];
            //文件数据
            byte[]FileData= new byte[len-13-256];
            din.readFully(name);
            din.readFully(FileData);
            String FileName= new String(name).trim();
            System.out.println("MSGDao正在解包文件消息包，文件名:"+FileName+"，文件长度："+len);
            return new FileMsg(len,type,dest,src,FileName,FileData);
            //断开连接消息
        }else if(type==0x20) {
            int l = len - 13;
            byte[] notice = new byte[l];
            din.readFully(notice);
            String snotice = new String(notice);
            System.out.println("MSGTools正在解包断开连接消息包，notice:" + snotice);
            return new OffServiceMsg(len, type, dest, src, snotice);
        }
        //(待完善)
        else {
            return null;
        }
    }

    /**
     * 消息打包，返回字节数组
     * @param msg 待打包的消息对象
     * @return 返回打包好的字节数组
     * @throws IOException
     */
    public static byte[] packMsg(MSGHead msg) throws IOException{
        //字节数组输出流
        ByteArrayOutputStream bou= new ByteArrayOutputStream();
        //包装输出流
        DataOutputStream dou= new DataOutputStream(bou);
        //写入消息头
        writeHead(msg,dou);
        byte type= msg.getType();
        //注册请求消息
        if(type==0x01){
            //强制转换
            RegMsg rm= (RegMsg)msg;
            //昵称
            writeString(rm.getNickname(),10,dou);
            //密码
            writeString(rm.getPwd(),10,dou);
            System.out.println("MSGDao正在包装注册请求类型的消息包,昵称:"+rm.getNickname()+",密码:"+rm.getPwd());
            return bou.toByteArray();
        }
        //登录请求消息
        else if(type==0x02){
            LoginMsg lm=(LoginMsg)msg;
            //账号
            dou.writeInt(lm.getAccNum());
            //密码
            writeString(lm.getPwd(),10,dou);
            System.out.println("MSGDao正在包装登录请求消息消息包,JKnum:"+lm.getAccNum()+",密码"+lm.getPwd());
            return bou.toByteArray();
        }
        //注册应答消息
        else if(type==0x11){
            RegResMsg rrm=(RegResMsg)msg;
            dou.writeByte(rrm.getState());
            System.out.println("MSGDao正在包装注册应答消息包,state:"+rrm.getState()+",JKnum:"+rrm.getDest());
            return bou.toByteArray();
        }
        //登录应答消息
        else if(type==0x12){
            LoginResMsg lrm=(LoginResMsg)msg;
            dou.writeByte(lrm.getState());
            System.out.println("MSGDao正在包装登录应答消息包,state:"+lrm.getState());
            return bou.toByteArray();
        }
        //普通文本消息
        else if(type==0x06){
            TextMsg tm =(TextMsg)msg;
            byte []data=tm.getmsgContent().getBytes();
            dou.write(data);
            dou.flush();
            System.out.println("MSGDao正在包装普通文本消息包,msg:"+tm.getmsgContent());
            return bou.toByteArray();
        }
        //文件消息
        else if(type==0x07){
            FileMsg fm= (FileMsg)msg;
            //文件名256字节
            MSGDao.writeString(fm.getFileName(),256, dou);
            dou.write(fm.getFileData());
            dou.flush();
            System.out.println("MSGDao正在包装文件消息包,文件名:"+fm.getFileName()+",文件总长："+fm.getLen());
            return bou.toByteArray();
            //连接断开消息
        }else if(type==0x20){
            OffServiceMsg osm=(OffServiceMsg)msg;
            byte[]data=osm.getNotice().getBytes();
            dou.write(data);
            dou.flush();
            System.out.println("MSGTools正在断开连接消息包,notice:"+osm.getNotice());
            return bou.toByteArray();
        }
        //(待完善)
        else {
            return null;
        }
    }

    /**
     * 发送消息对象
     * @param msg
     * @param dou
     * @throws IOException
     */
    private static void writeHead(MSGHead msg,DataOutputStream dou) throws IOException{
        //总长
        dou.writeInt(msg.getLen());
        //类型
        dou.writeByte(msg.getType());
        //目标
        dou.writeInt(msg.getDest());
        //来源
        dou.writeInt(msg.getSender());
    }

    /**
     * 发送定长的字符串
     * @param msg 待发送的字符串
     * @param len 字符串长度
     * @param dou 输出流
     * @throws IOException
     */
    public static void writeString(String msg, int len, DataOutputStream dou) throws IOException{
        byte []data= msg.getBytes();
        dou.write(data);
        while(len>data.length){
            //这里千万不要用writeChar()方法，每调用一次会多输出一个空字符
            dou.write('\0');
            len--;
        }
    }
}

