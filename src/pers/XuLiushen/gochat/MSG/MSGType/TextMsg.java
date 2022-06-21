package pers.XuLiushen.gochat.MSG.MSGType;


public class TextMsg extends MSGHead {
    private String msgContent;
    public TextMsg(int len, byte type, int dest, int src,String msgContent) {
        super(len, type, dest, src);
        this.msgContent=msgContent;
    }
    public String getmsgContent(){
        return msgContent;
    }
}


