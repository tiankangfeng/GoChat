package pers.XuLiushen.gochat.MSG.MSGType;

/**
 * 消息头的公共父类：类型+总长度+源+目的
 */
public class MSGHead {
    private int totalLen;
    private byte type;
    private int dest;
    private int src;

    public MSGHead(int len,byte type, int dest,int src){
        this.totalLen=len;
        this.type=type;
        this.dest=dest;
        this.src=src;
    }
    public int getLen(){
        return totalLen;
    }
    public byte getType(){
        return type;
    }
    public int getDest(){
        return dest;
    }
    public int getSender(){
        return src;
    }
}

