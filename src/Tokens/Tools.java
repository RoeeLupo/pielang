package Tokens;

import Tokens.Advanced.ListCommandToken;
import Tokens.Basic.Token;

import java.util.LinkedList;

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
            return s.toString();
        } else {
            //can't happen anyway
            return "error";
        }
    }
}
