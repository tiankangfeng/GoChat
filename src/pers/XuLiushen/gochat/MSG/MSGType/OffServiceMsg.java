package pers.XuLiushen.gochat.MSG.MSGType;

public class OffServiceMsg extends MSGHead{

	private String notice;
	
	public OffServiceMsg(int len, byte type, int dest, int src, String notice) {
		super(len, type, dest, src);
		this.notice=notice;
	}

	public String getNotice(){
		return notice;
	}
}
