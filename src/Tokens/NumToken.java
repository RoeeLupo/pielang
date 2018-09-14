package Tokens;

public class NumToken extends Token<Integer> {

    public NumToken(int data) {
        super("Number", data);
    }

    @Override
    public String toString() {
        return "NumToken{" +
                "type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
