package Tokens.Basic;

public class StartGroupToken extends Token<Character> {

    public StartGroupToken(int line) {
        super("StartGroup", '(', line);
    }

    @Override
    public String toString() {
        return "[StartGroupToken]";
    }
}
