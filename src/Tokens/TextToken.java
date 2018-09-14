package Tokens;

public class TextToken extends Token<String> {

    public TextToken(String data) {
        super("Text", data);
    }

    @Override
    public String toString() {
        return "TextToken{" +
                "type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
