package pers.XuLiushen.gochat.client.bean;

public class Friend {
	private String nickName;//昵称
	private int accNum;//账号
	
	public Friend(String name, int accNum){
		this.nickName=name;
		this.accNum=accNum;
	}
	
	public int getAccNum(){
		return accNum;
	}

	public String getNickName(){
		return nickName;
	}
}
