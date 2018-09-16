package Tokens.Advanced;

import Tokens.BASETOKEN;
import Tokens.Basic.Token;

public class CommandToken extends ADVToken<BASETOKEN>  {
//should hold only tokens and group tokens
    public CommandToken() {
        super("CommandToken");
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("CommandToken < ");
        for(BASETOKEN c: data){
            result.append(c).append(" ");
        }
        result.append(" >");
        return result.toString();
    }

    @Override
    public String Translate() {
        StringBuilder result = new StringBuilder();
        for(BASETOKEN c: data){
            try{
                GroupToken g = (GroupToken) c;
                result.append(g.Translate()).append(" ");
            } catch (Exception e) {
                result.append(c.GetData()).append(" ");
            }
        }
        return result.toString();
    }

    @Override
    public void Append(BASETOKEN t) {
        this.data.add(t);
    }

}
