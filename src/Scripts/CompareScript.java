package Scripts;

import Tokens.Advanced.ADVToken;
import Tokens.Advanced.GroupToken;
import Tokens.Advanced.ListCommandToken;

public class CompareScript extends Script {

    public CompareScript(){
        super("compare");
    }

    @Override
    public String ErrorMessage() {
        return "Syntax: compare(name) operator{ to(name1){} elto(name2){} }";
    }

    @Override
    public String Translate(ADVToken script) throws Exception {
        ListCommandToken l = (ListCommandToken) script;
        StringBuilder s = new StringBuilder();
        String baseVar = ((GroupToken) l.GetTitle().GetData().get(1)).BaseTranslate();
        String operator = l.GetTitle().GetData().get(2).GetText();
        ListCommandToken to;
        to = (ListCommandToken) l.GetData().get(0);
        s.append("if (").append(baseVar)
                .append(operator).append(" ").append(((GroupToken) to.GetTitle().GetData().get(1)).BaseTranslate()).append("):\n");
        s.append(to.TranslateCommands());
        String ifstr;
        for (int i = 1; i < l.GetData().size(); i++) {
            to = (ListCommandToken) l.GetData().get(i);
            if (to.GetTitle().GetData().get(0).GetText().equals("elto")) {
                ifstr = "elif";
            } else {
                ifstr = "if";
            }
            s.append(l.GetIndent(), 0, l.GetIndent().length() - 1).append(ifstr).append(" (").append(baseVar)
                    .append(operator).append(" ").append(((GroupToken) to.GetTitle().GetData().get(1)).BaseTranslate()).append("):\n");
            s.append(to.TranslateCommands());
        }
        return s.toString();
    }

}
