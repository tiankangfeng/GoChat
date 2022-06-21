package pers.XuLiushen.gochat.MSG.MSGType;

//注册消息
public class RegMsg extends MSGHead {

    private String nickname;
    private String pwd;
    public RegMsg(int len, byte type, int dest, int src,String nickname, String pwd) {
        super(len, type, dest, src);
        this.nickname=nickname;
        this.pwd=pwd;
        // TODO Auto-generated constructor stub
    }
    public String getNickname(){
        return nickname;
    }
    public String getPwd(){
        return pwd;
    }
}

