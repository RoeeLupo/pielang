import Tokens.Tools;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class RevertTool {

    private Character[] ws = {'\n', '\t', ' '};

    public String Revert(String text){
        int i = 0;
        LinkedList<String> indents = new LinkedList<>();
        StringBuilder output = new StringBuilder();
        String skip = "";
        String current_indent = "";
        boolean colon_line = false;
        boolean started_text = false;
        int condition;
        while(i < text.length()){
            if(skip.length() > 0){
                if(Tools.StringAt(text, skip, i)){
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
                        text.charAt(i) == '\"',
                        text.charAt(i) == '(',
                        text.charAt(i) == '[',
                        text.charAt(i) == '{',
                        text.charAt(i) == ':');
                switch (condition) {
                    case -1:
                        if(!Tools.in(text.charAt(i), ws)) {
                            if(indents.size() >= 1 ){
                                while(indents.size() > 0 && (Tools.compareIndent(current_indent, indents.getLast()) <= 0) && !colon_line){
                                    output.append("}\n"+indents.getLast());
                                    indents.removeLast();
                                }
                            }
                            started_text = true;
                        }
                        else if(text.charAt(i) == '\n') {
                            if(started_text) {
                                output.append(';');
                                started_text = false;
                            }
                            current_indent = "";
                            colon_line = false;
                        } else {
                            if(!started_text)
                                current_indent += text.charAt(i);
                        }
                        output.append(text.charAt(i));
                        i++;
                        break;
                    case 0: // #
                        if(started_text){
                            started_text = false;
                            output.append(';');
                        }
                        skip = "\n";
                        output.append('#');
                        i++;
                        break;
                    case 1: // """
                        skip = "\"\"\"";
                        output.append("\"\"\"");
                        i += 3;
                        break;
                    case 2: // \
                        skip = "\n";
                        i++;
                        break;
                    case 3: // "
                        skip = "\"";
                        output.append("\"");
                        i++;
                        break;
                    case 4: // (
                        skip = ")";
                        output.append("(");
                        i++;
                        break;
                    case 5: // [
                        skip = "]";
                        output.append("[");
                        i++;
                        break;
                    case 6: // {
                        skip = "}";
                        output.append("dict{");
                        i++;
                        break;
                    case 7: // :
                        colon_line = true;
                        started_text = false;
                        indents.add(current_indent);
                        output.append("{");
                        i++;
                        break;
                }
            }
        }
        while(indents.size() > 0){
            output.append(indents.getLast() + "}\n");
            indents.removeLast();
        }

        return output.toString();
    }

    public static void Execute(String path){
        RevertTool d = new RevertTool();
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
