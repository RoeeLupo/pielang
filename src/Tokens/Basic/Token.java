package Tokens.Basic;

import Tokens.BaseToken;

public abstract class Token <T> implements BaseToken {
    private String type;
    protected T data;

    public Token(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public T GetData(){
        return data;
    }

    @Override
    public String GetType() {
        return type;
    }

    //obj : Always String
    @Override
    public boolean equals(Object obj) {
        return type.equals(obj);
    }

    @Override
    public String toString(){
        return "["+type+"Token" + "='"+ data.toString() +"\']";
    }

    //Seems useless, but it gives an option to get the data as text
    //And you can choose how you want to format the text
    @Override
    public String GetText(){
        return GetData().toString();
    }
}
