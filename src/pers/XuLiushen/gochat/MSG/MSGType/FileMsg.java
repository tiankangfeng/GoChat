package pers.XuLiushen.gochat.MSG.MSGType;

public class FileMsg extends MSGHead{

    private String FileName;
    private byte[] FileData;

    public FileMsg(int len, byte type, int dest, int src, String FileName,byte[] FileData) {
        super(len, type, dest, src);
        this.FileName=FileName;
        this.FileData=FileData;
    }

    public String getFileName(){
        return FileName;
    }

    public byte[] getFileData(){
        return FileData;
    }
}

