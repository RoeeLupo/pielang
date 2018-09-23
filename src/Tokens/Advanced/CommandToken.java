package Tokens.Advanced;

import Tokens.BaseToken;

public class CommandToken extends ADVToken<BaseToken>  {
//should hold only tokens and group tokens
    public CommandToken() {
        super("CommandToken");
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("CommandToken < ");
        for(BaseToken c: data){
            result.append(c).append(" ");
        }
        result.append(" >");
        return result.toString();
    }

    @Override
    public String Translate() {
        StringBuilder result = new StringBuilder();
        for(BaseToken c: data)
                result.append(c.GetText()).append(" ");
        return result.toString();
    }

}
