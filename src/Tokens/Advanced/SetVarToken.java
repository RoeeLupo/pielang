package Tokens.Advanced;

import Tokens.BaseToken;
import Tokens.Tools;

import java.util.LinkedList;

public class SetVarToken extends ADVToken<BaseToken> {
    private ADVToken<BaseToken> header = new CommandToken();
    private boolean controlled = false;
    private String name, indent;
    public static LinkedList<String> controlledVars = new LinkedList<>();

    public SetVarToken(ADVToken<BaseToken> header, String indent) {
        super("SetVarToken");
        this.header = header;
        if(header.GetData().get(0).GetText().equals("pure")){
            controlled = true;
            name = header.GetData().get(1).GetText();
            controlledVars.add(name);
        } else if(header.GetData().getLast().GetText().charAt(0) == ':')
            name = header.GetData().get(header.GetData().size()-1).GetText();
        else
            name = header.GetData().getLast().GetText();
        this.indent = indent;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("SetVarToken < header{");
        result.append(header).append("} content{");
        for(BaseToken c: data){
            result.append(c).append(" ");
        }
        result.append("} >");
        return result.toString();
    }

    @Override
    public String Translate() {
        StringBuilder result = new StringBuilder();
        if(controlled || /*controled is false && */ !Tools.in(name, controlledVars.toArray())) {
            if(controlled)
                result.append(header.Translate().substring(header.Translate().indexOf(" ")+1)).append("= ");
            else
                result.append(header.Translate()).append("=");
            for (BaseToken c : data)
                result.append(c.GetText()).append(" ");
        } else {
            result.append("Temp_Pure_Var = ");
            for (BaseToken c : data)
                result.append(c.GetText()).append(" ");
            result.append("\n").append(indent)
                    .append("if (type(").append(name).append(") != type(Temp_Pure_Var)) : raise Exception(\"PureTypeError: Types don't match.\")\n");
            result.append(indent).append(name).append(" = Temp_Pure_Var");
        }
        return result.toString();
    }
}
