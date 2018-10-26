package Tokens.Basic;

public class OperatorToken extends Token<String> {

    public OperatorToken(String data, int line) {
        super("Operator", data, line);
    }

}
