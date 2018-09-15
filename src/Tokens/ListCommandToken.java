package Tokens;

import java.util.TooManyListenersException;

public class ListCommandToken extends ADVToken<ADVToken> {
    private CommandToken title;
    private String indent;

    public ListCommandToken(String indent, CommandToken title) {
        super("ListCommandToken");
        this.indent = indent;
        this.title = title;
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

    public String TranslateCommands(){
        StringBuilder s = new StringBuilder();
        for (ADVToken aData : this.data) {

            if(aData.GetData().get(0).equals("Text") && (((String) ((TextToken) aData.GetData().get(0)).GetData())).charAt(0) == '#')
                s.append(aData.Translate()).append("\n");
            else
                s.append(indent).append(aData.Translate()).append("\n");
        }
        return s.toString();
    }

    @Override
    public String Translate() {
        if(title.GetData().get(0).getType().equals("Script"))
            return Tools.TranslateCommand(this);
        else {
            StringBuilder s = new StringBuilder();
            s.append(title.Translate()).append(":\n").append(TranslateCommands());
            return s.toString();
        }
    }

    @Override
    public void Append(ADVToken t) {
        this.data.add(t);
    }

    public String GetIndent(){
        return indent;
    }

    public CommandToken GetTitle(){
        return title;
    }
}
