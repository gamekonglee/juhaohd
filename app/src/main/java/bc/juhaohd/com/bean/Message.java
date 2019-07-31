package bc.juhaohd.com.bean;

/**
 * Created by bocang on 18-2-28.
 */

public class Message
{
    int type;
    String msg;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Message(int type, String msg) {
        this.type = type;
        this.msg = msg;
    }
}
