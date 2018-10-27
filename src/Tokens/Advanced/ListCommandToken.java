package Tokens.Advanced;

import Tokens.BaseToken;
import Tokens.Tools;

public class ListCommandToken extends ADVToken<ADVToken> {
    private CommandToken title;
    private String indent;

    public ListCommandToken(String indent, CommandToken title) {
        super("ListCommandToken");
        this.indent = indent;
        this.title = title;
        if(title == null)
            command = "";
        else
            this.command = title.GetData().get(0).GetText();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("ListCommandToken { ").append(title).append(" } <\n");
        for(ADVToken c: data){
            result.append(indent).append(c).append("\n");
        }
        result.append(indent, 0, indent.length()-1).append(" >");
        return result.toString();
    }

    public String TranslateCommands() throws Exception {
        StringBuilder s = new StringBuilder();
        for (ADVToken aData : this.data)
            s.append(indent).append(aData.Translate()).append("\n");
        return s.toString();
    }

    @Override
    public String Translate() throws Exception{
        if(title.GetData().get(0).equals("Scripts.Script"))
            return Tools.TranslateScript(this);
        else
            return title.Translate() + ":\n" + TranslateCommands();
    }

    public String GetIndent(){
        return indent;
    }

    public CommandToken GetTitle(){
        return title;
    }
}
