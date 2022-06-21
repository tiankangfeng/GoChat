package pers.XuLiushen.gochat.server.dao;

import pers.XuLiushen.gochat.server.bean.User;
import pers.XuLiushen.gochat.server.main.ServerThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * DAO数据层：用户数据类
 * @author XuLiushen
 * @Time 2021/5/20
 */
public class UserDao {
    /** 已注册用户集合 */
    private static Map<Integer,User> userDB= new HashMap<Integer,User>();
    //在线用户集合
    private static ArrayList<ServerThread> stList= new ArrayList<ServerThread>();
    //账号分配流水号
    private static int userAssignedNum=0;

    /**
     *     保护构造函数，不允许建立该对象
     */
    private UserDao(){}

    /**
     * 核对登录信息
     * @param st 处理用户与服务器连接的线程
     * @param user 登录的用户
     * @return 登录结果
     */
    public static boolean checkLogin(ServerThread st,User user){
            //账号不存在
            if(!userDB.containsKey(user.getAccNum() )){
                System.out.println(user.getAccNum()+"账号不存在");
                return false;
            }
            User user2=userDB.get(user.getAccNum());
            if(user2.getPWD().equals(user.getPWD())){
                //添加到在线用户队列
                stList.add(st);
                //下发用户的好友列表（待完善）
                //下发用户信息
                return true;
            }
            System.out.println("密码:"+user.getPWD()+"错误,正确的密码是："+user2.getPWD());
            return false;
        }

    /**
     *     注册新用户,返回分配的账号
     */
    public static int Register(User user){
        userAssignedNum++;
        user.setAccNum(2020+userAssignedNum);//分配账号
        userDB.put(user.getAccNum(), user);
        System.out.println("用户"+user.getAccNum()+"注册成功,昵称:"+user.getNickName()+"密码："+user.getPWD());
        return user.getAccNum();
    }

    /**
     * 注销用户
     */
    public static void removeUser(int accNum){
            userDB.remove(accNum);
            System.out.println("用户"+accNum+"注销成功");
    }

    /**
     * 添加在线的用户线程对象
     * @param st 用户线程对象
     */
    public static void addOnlineUser(ServerThread st){
        stList.add(st);
    }

    /**
     *  从在线用户列表中移除已经下线的线程对象
     * @param st 已经下线的服务器与用户通信线程对象
     */
    public static void removeOfflineUser(ServerThread st){
        int accNum = st.getUser().getAccNum();
        for(ServerThread s:stList){
            if(s.getUser().getAccNum() == accNum ){
                stList.remove(s);
                break;
            }
        }
    }

    /**
     * 获取消息转发对象
     * @return 消息转发对象
     * @param accNum 转发对象的账号
     */
    public static ServerThread getDest(int accNum){
        //根据账号遍历在线用户的集合寻找目标的用户线程对象
        for(ServerThread st:stList){
            if(st.getUser().getAccNum() == accNum){
                return st;
            }
        }
        return null;
    }



}
