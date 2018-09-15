package Tokens;

import java.security.KeyStore;

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
    public boolean equals(Object obj) {
        return type.equals(obj);
    }

    @Override
    public abstract String toString();
}
