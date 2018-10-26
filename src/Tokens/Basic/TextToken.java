package Tokens.Basic;

public class TextToken extends Token<String>  {

    public TextToken(String data, int line) {
        super("Text", data, line);
    }

}
