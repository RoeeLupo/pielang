package Tokens.Basic;

public class EndGroupToken extends Token<Character> {

    public EndGroupToken() {
        super("EndGroup", ')');
    }

    @Override
    public String toString() {
        return "[EndGroupToken]";
    }
}
