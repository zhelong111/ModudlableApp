package bean;

public class EventMsg {
    private Object value;
    private int code;

    public EventMsg(int code, Object value) {
        this.code = code;
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
