package Tokens;

import Tokens.Advanced.ADVToken;
import Tokens.Advanced.GroupToken;
import Tokens.Advanced.ListCommandToken;
import Tokens.Basic.OperatorToken;
import Tokens.Basic.Token;

import java.util.LinkedList;
import java.util.List;

public class Tools {

    public static String TranslateCommand(ListCommandToken l){
        StringBuilder s = new StringBuilder();
        if(l.GetTitle().GetData().get(0).GetData().equals("loop")){
            LinkedList<BASETOKEN> title = (LinkedList <BASETOKEN>) l.GetTitle().GetData().get(1).GetData();
            String name = title.get(0).getText();
            StringBuilder from = new StringBuilder(), to = new StringBuilder(), construct = new StringBuilder();
            int i = 1;
            for(i = 1; i < title.size() && !title.get(i).getText().equals(">"); i++)
                from.append(title.get(i).getText()).append(" ");
            i++;
            for(; i < title.size() && !title.get(i).getText().equals("|"); i++)
                to.append(title.get(i).getText()).append(" ");
            construct.append(title.get(i+1).getText()).append("=").append(title.get(i+2).getText());
            s.append(name).append("=").append(from).append("\n");
            s.append(l.GetIndent(), 0, l.GetIndent().length()-1).append("while ").append(name).append("<").append(to).append(": \n");
            s.append(l.TranslateCommands());
            s.append(l.GetIndent()).append(name).append(construct);
        } else if(l.GetTitle().GetData().get(0).GetData().equals("compare")){
            String basevar = ((GroupToken) l.GetTitle().GetData().get(1)).BaseTranslate();
            String operator = l.GetTitle().GetData().get(2).getText();
            ListCommandToken to;
            to = (ListCommandToken) l.GetData().get(0);
            s.append("if (").append(basevar)
                    .append(operator).append(" ").append(((GroupToken)to.GetTitle().GetData().get(1)).BaseTranslate()).append("):\n");
            s.append(to.TranslateCommands());
            String ifstr = "";
            for(int i = 1; i < l.GetData().size(); i++){
                to = (ListCommandToken) l.GetData().get(i);
                if(to.GetTitle().GetData().get(0).getText().equals("elto")) {
                    ifstr = "elif";
                }
                else {
                    ifstr = "if";
                }
                s.append(l.GetIndent(), 0, l.GetIndent().length()-1).append(ifstr).append(" (").append(basevar)
                        .append(operator).append(" ").append(((GroupToken)to.GetTitle().GetData().get(1)).BaseTranslate()).append("):\n");
                s.append(to.TranslateCommands());
            }
        }
        return s.toString();
    }
}
