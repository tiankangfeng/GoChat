package pers.XuLiushen.gochat.MSG.MSGType;

public class LoginMsg extends MSGHead{

    private int accNum;//登录账号
    private String pwd;

    public LoginMsg(int len, byte type, int dest, int src,int accNum, String pwd) {
        super(len, type, dest, src);
        this.accNum=accNum;
        this.pwd=pwd;
    }
    public int getAccNum(){
        return accNum;
    }
    public String getPwd(){
        return pwd;
    }
}
