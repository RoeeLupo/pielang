package Tokens.Basic;

import Tokens.BASETOKEN;

public abstract class Token <T> implements BASETOKEN {
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
    public String toString(){
        return "["+type+"Token" + "='"+ data.toString() +"\']";
    }

    @Override
    public String getText(){
        return GetData().toString();
    }
}
