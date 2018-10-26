package Tokens.Basic;

public class EndGroupToken extends Token<Character> {

    public EndGroupToken(int line) {
        super("EndGroup", ')', line);
    }

    @Override
    public String toString() {
        return "[EndGroupToken]";
    }
}
