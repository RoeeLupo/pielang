package Scripts;

import Tokens.Advanced.ADVToken;
import Tokens.Advanced.ListCommandToken;
import Tokens.BaseToken;
import java.util.LinkedList;

public class LoopScript extends Script {

    public LoopScript(){
        super("loop");
    }

    @Override
    public String ErrorMessage() {
        return "Syntax: loop(name from > to | update){}";
    }

    @Override
    public String Translate(ADVToken script) throws Exception {
        ListCommandToken l = (ListCommandToken) script;
        StringBuilder s = new StringBuilder();
        LinkedList<BaseToken> title = (LinkedList<BaseToken>) l.GetTitle().GetData().get(1)/*group*/.GetData(); // all params
        String name = title.get(0).GetText();
        StringBuilder from = new StringBuilder(), to = new StringBuilder(), construct = new StringBuilder();
        int i;
        for (i = 1; i < title.size() && !title.get(i).GetText().equals(">"); i++)
            from.append(title.get(i).GetText()).append(" ");
        i++;
        for (; i < title.size() && !title.get(i).GetText().equals("|"); i++)
            to.append(title.get(i).GetText()).append(" ");
        construct.append(title.get(i + 1).GetText()).append("=").append(title.get(i + 2).GetText());
        s.append(name).append("=").append(from).append("\n");
        s.append(l.GetIndent(), 0, l.GetIndent().length() - 1).append("while ").append(name).append("<").append(to).append(": \n");
        s.append(l.TranslateCommands());
        s.append(l.GetIndent()).append(name).append(construct);
        return s.toString();
    }

}
