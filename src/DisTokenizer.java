import Tokens.Tools;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class DisTokenizer {

    private Character[] ws = {'\n', '\t', ' '};

    public String Revert(String text){
        int i = 0;
        LinkedList<StringBuilder> indents = new LinkedList<>();
        indents.add(new StringBuilder());
        StringBuilder output = new StringBuilder();
        String skip = "";
        boolean started_text = false;
        int condition;
        while(i < text.length()){
            if(skip.length() > 0){
                if(Tools.StringAt(text, skip, i)){
                    System.out.println("End: " + skip);
                    output.append(skip);
                    i += skip.length();
                    skip = "";
                } else {
                    output.append(text.charAt(i));
                    i++;
                }
            } else {
                condition = Tools.If(text.charAt(i) == '#',
                        Tools.StringAt(text, "\"\"\"", i),
                        text.charAt(i) == '\\',
                        text.charAt(i) == '\"');
                switch (condition) {
                    case -1:
                        if(!Tools.in(text.charAt(i), ws)) {
                            if(indents.size() >= 2 && indents.getLast().toString().equals(indents.get(indents.size()-2).toString()))
                                indents.removeLast();
                            started_text = true;
                        }
                        else if(text.charAt(i) == '\n') {
                            if(started_text) {
                                output.append(';');
                                started_text = false;
                            }
                            indents.add(new StringBuilder());
                        } else {
                            if(!started_text)
                                indents.getLast().append(text.charAt(i));
                        }
                        output.append(text.charAt(i));
                        i++;
                        break;
                    case 0: // #
                        System.out.println("Start: #");
                        if(started_text){
                            started_text = false;
                            output.append(';');
                        }
                        skip = "\n";
                        output.append('#');
                        i++;
                        break;
                    case 1: // """
                        System.out.println("Start: \"\"\"");
                        skip = "\"\"\"";
                        output.append("\"\"\"");
                        i += 3;
                        break;
                    case 2: // \
                        System.out.println("Start: \\");
                        skip = "\n";
                        output.append("\\");
                        i++;
                        break;
                    case 3: // "
                        System.out.println("Start: \"");
                        skip = "\"";
                        output.append("\"");
                        i++;
                        break;
                }
            }
        }
        for(StringBuilder s : indents)
            System.out.println("indent: <" + s.toString() + ">");
        return output.toString();
    }

    public static void Execute(String path){
        DisTokenizer d = new DisTokenizer();
        String text = Tools.ReadFile(path);
        String output = d.Revert(text);
        String[] pathArray = path.split(Pattern.quote("\\"));
        String filename = pathArray[pathArray.length-1].split(Pattern.quote("."))[0];
        if(pathArray.length > 1) {
            String pathtofile = String.join("/", Arrays.copyOfRange(pathArray, 0, pathArray.length - 1));
            Tools.WriteFile(pathtofile + "\\" + filename + ".pie", output);
        } else
            Tools.WriteFile(filename + ".pie", output);
    }
}
