package Tokens;

public class SeparatorToken extends Token<Character> {

    public SeparatorToken(Character data) {
        super("Separator", data);
    }

    @Override
    public String toString() {
        return "SeparatorToken{" +
                "type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
