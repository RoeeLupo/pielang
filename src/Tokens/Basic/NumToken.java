package Tokens.Basic;

public class NumToken extends Token<Integer>   {

    public NumToken(int data, int line) {
        super("Number", data, line);
    }

}
