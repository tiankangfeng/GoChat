package pers.XuLiushen.gochat.server.bean;

public class User {
    /**姓名*/
    private String name;
    /**昵称*/
    private String nickName;
    /**地址*/
    private String address;
    /**密码*/
    private String pwd;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getAccNum() {
        return accNum;
    }

    public void setAccNum(int accNum) {
        this.accNum = accNum;
    }

    /**账号*/
    int accNum;

    public void setName(String name){
        this.name=name;
    }
    public String getName(){
        return name;
    }

    public String getnickName(){
        return nickName;
    }

    public void setnickName(String nickName){
        this.nickName=nickName;
    }

    public void setAddress(String address){
        this.address=address;
    }

    public String getAddress(){
        return address;
    }

    public String getPWD(){
        return pwd;
    }

    public void setPWD(String pwd)
    {
        this.pwd=pwd;
    }


}

