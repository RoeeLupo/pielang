package Tokens.Advanced;

import Tokens.BASETOKEN;

public class GroupToken extends ADVToken<BASETOKEN> implements BASETOKEN {
    public GroupToken() {
        super("GroupToken");
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("GroupToken < ");
        for(BASETOKEN c: data){
            result.append(c).append(" ");
        }
        result.append(" >");
        return result.toString();
    }

    @Override
    public String getText() {
        return Translate();
    }

    @Override
    public void Append(BASETOKEN token) {
        this.data.add(token);
    }

    public String BaseTranslate(){
        StringBuilder result = new StringBuilder();
        for(BASETOKEN c: data){
            try {
                GroupToken g = (GroupToken) c;
                result.append(g.Translate()).append(" ");
            } catch (Exception e) {
                result.append(c.GetData()).append(" ");
            }
        }
        return result.toString();
    }

    @Override
    public String Translate() {
        return "(" +
                BaseTranslate() +
                ")";
    }
}
