package Tokens.Basic;

import Tokens.BASETOKEN;

public class StartGroupToken extends Token<Character> {

    public StartGroupToken() {
        super("StartGroup", '(');
    }

    @Override
    public String toString() {
        return "[StartGroupToken]";
    }
}
