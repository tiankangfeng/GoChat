package pers.XuLiushen.gochat.MSG.MSGType;


public class RegResMsg extends MSGHead {
    private byte state;
    public RegResMsg(int len, byte type, int dest, int src,byte state) {
        super(len, type, dest, src);
        this.state=state;
    }
    public byte getState(){
        return state;
    }

}



