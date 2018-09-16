package Tokens.Basic;

import Tokens.BASETOKEN;

public class NumToken extends Token<Integer>   {

    public NumToken(int data) {
        super("Number", data);
    }

}
