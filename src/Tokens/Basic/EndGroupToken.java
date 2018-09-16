package Tokens.Basic;

import Tokens.BASETOKEN;

public class EndGroupToken extends Token<Character> {

    public EndGroupToken() {
        super("EndGroup", ')');
    }

    @Override
    public String toString() {
        return "[EndGroupToken]";
    }
}
