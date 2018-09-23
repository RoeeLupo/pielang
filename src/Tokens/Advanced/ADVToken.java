package Tokens.Advanced;

import Tokens.Basic.Token;

import java.util.LinkedList;

public abstract class ADVToken <T> extends Token<LinkedList<T>> {


    public ADVToken(String type) {
        super(type, new LinkedList<>());
    }

    //Had to return LinkedList<T> for the times you use ADVToken without specifying T
    public LinkedList<T> GetData(){
        return data;
    }

    public void Append(T t){
        this.data.add(t);
    }

    //Basically does the same thing as GetText, but only ADVTokens have this function
    public abstract String Translate();

}
