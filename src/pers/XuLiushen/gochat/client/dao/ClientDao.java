package pers.XuLiushen.gochat.client.dao;

import pers.XuLiushen.gochat.client.bean.Friend;
import pers.XuLiushen.gochat.client.bean.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ClientDao {

    private User user;//用户对象
    private String path;//接收文件的存储路
    private HashMap<String, ArrayList<Friend>> friends= new HashMap<String,ArrayList<Friend> >();//用户好友列表（分组名-分组中的好友集合）

    public ClientDao(){
        path="F:\\JavaProjectInIDEA\\GoChat\\src\\pers\\XuLiushen\\gochat\\client\\buffer\\";//默认保存文件的路径
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 获取指定分组中的好友列表
     * @param groupName 组名
     * @return 好友列表
     */
    public ArrayList<Friend> getFriendsInGroup(String groupName){
        groupName.trim();
        ArrayList<Friend> fList = friends.get(groupName);
        return fList;
    }

    /**
     * 向指定分组中添加好友
     * @param groupName
     * @param friend
     */
    public boolean addFriend(String groupName, Friend friend){
        if(friend == null)
            return false;
        //获取指定分组中的好友列表
        ArrayList<Friend> fList = friends.get(groupName);
        if(fList == null) //分组不存在
            return false;
        //向该列表中添加好友
        fList.add(friend);
        return true;
    }

    /**
     * 删除指定分组中好友（根据账号）
     * @param groupName
     * @param accNum
     * @return
     */
    public boolean deleteFriend(String groupName, int accNum){
        //获取指定分组中的好友列表
        ArrayList<Friend> fList = friends.get(groupName);
        //分组是否存在？
        if(fList == null)
            return false;
        //从列表中删除指定好友
        for(Friend f:fList){
            if(f.getAccNum() == accNum){
                fList.remove(f);
                return true;
            }
        }
        return false;
    }

}
