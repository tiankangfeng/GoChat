package pers.XuLiushen.gochat.MSG.MSGType;

public class LoginResMsg extends MSGHead{
    private byte state;

    public LoginResMsg(int len, byte type, int dest, int src, byte state) {
        super(len, type, dest, src);
        this.state=state;
    }

    public byte getState(){
        return state;
    }

}


