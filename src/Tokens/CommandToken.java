package Tokens;

public class CommandToken extends ADVToken<Token> {

    public CommandToken() {
        super("CommandToken");
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("CommandToken < ");
        for(Token c: data){
            result.append(c).append(" ");
        }
        result.append(" >");
        return result.toString();
    }

    @Override
    public String Translate() {
        StringBuilder result = new StringBuilder();
        for(Token c: data){
            result.append(c.GetData()).append(" ");
        }
        return result.toString();
    }

    @Override
    public void Append(Token t) {
        this.data.add(t);
    }

}
