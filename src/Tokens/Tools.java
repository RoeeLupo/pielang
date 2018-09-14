package Tokens;

import java.util.LinkedList;

public class Tools {

    public static String TranslateCommand(ListCommandToken l){
        StringBuilder s = new StringBuilder();
        if(l.GetTitle().GetData().get(0).GetData().equals("loop")){
            LinkedList<Token> title = l.GetTitle().GetData();
            s.append(title.get(2).GetData()).append("=").append(title.get(3).GetData()).append("\n");
            s.append(l.GetIndent(), 0, l.GetIndent().length()-1).append("while ").append(title.get(2).GetData()).append("<").append(title.get(5).GetData()).append(": \n");
            s.append(l.TranslateCommands());
            s.append(l.GetIndent()).append(title.get(2).GetData()).append(title.get(7).GetData()).append("=").append(title.get(8).GetData());
            return s.toString();
        } else {
            //can't happen anyway
            return "error";
        }
    }

}
