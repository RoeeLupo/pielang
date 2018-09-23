package Tokens.Advanced;

import Tokens.BaseToken;

public class GroupToken extends ADVToken<BaseToken> implements BaseToken {
    public GroupToken() {
        super("GroupToken");
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("GroupToken < ");
        for(BaseToken c: data)
            result.append(c).append(" ");
        result.append(" >");
        return result.toString();
    }

    @Override
    public String GetText() {
        return Translate();
    }

    public String BaseTranslate(){
        StringBuilder result = new StringBuilder();
        for(BaseToken c: data)
            result.append(c.GetText()).append(" ");
        return result.toString();
    }

    @Override
    public String Translate() {
        return "(" + BaseTranslate() + ")";
    }
}
