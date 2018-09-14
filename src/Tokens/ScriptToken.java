package Tokens;

public class ScriptToken extends Token<String> {
    public ScriptToken(String data) {
        super("Script", data);
    }

    @Override
    public String toString() {
        return "ScriptToken{" +
                "type='" + type + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
