package Tokens;

public abstract class Token <T>{
    protected String type;
    protected T data;

    public Token(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public T GetData(){
        return data;
    }

    @Override
    public abstract String toString();
}
