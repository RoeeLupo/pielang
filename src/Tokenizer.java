import Tokens.Advanced.*;
import Tokens.BaseToken;
import Tokens.Basic.*;
import Tokens.Tools;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Tokenizer {
    private Character[] separators = {'{', '}', ';', '(', ')', '|', '+', '*', '/', '-', '>', '~', '='}, ws = {' ', '\t', '\n'};
    private String[] operators = {"<=", ">=", "==", "<", ">", "!="};
    private String[] pairs = {"+=", "*=", "/=", "-=", "++", "--"};
    private static boolean enableComments = true, enableNoise = false;

    private Token[] Tokens(String s){
        LinkedList<Token> tokens = new LinkedList<>();
        int start = 0;
        int i = 0;
        int condition;
        String startedText = " ";
        char startedComment = ' ';
        boolean startedPure = false;
        boolean donttokenize = false;
        boolean dict = false;
        int line = 1;
        while(i < s.length()){
            if(s.charAt(i) == '\n')
                line++;
            if(!donttokenize) {
                condition = Tools.If( Tools.in(Tools.PairAt(s,i), pairs),
                                      Tools.in(Tools.PairAt(s,i), operators),
                                      Tools.in(s.charAt(i), ws),
                                      Tools.in(s.charAt(i), separators),
                                      s.charAt(i) == '"' || s.charAt(i) == '\'',
                                      s.charAt(i) == '#'
                );
                switch (condition) {
                    case 0:
                        if (start != i)
                            tokens.add(Tools.GenerateToken(s.substring(start, i), line));
                        tokens.add(new PairToken(Tools.PairAt(s, i), line));
                        i += Tools.PairAt(s, i).length() - 1;
                        start = i + 1;
                        break;
                    case 1: // in(PairAt(s, i), operators)
                        if (start != i)
                            tokens.add(Tools.GenerateToken(s.substring(start, i), line));
                        tokens.add(new OperatorToken(Tools.PairAt(s, i), line));
                        i += Tools.PairAt(s, i).length() - 1;
                        start = i + 1;
                        break;

                    case 2: // in(s.charAt(i), ws)
                        if (start != i)
                            tokens.add(Tools.GenerateToken(s.substring(start, i), line));
                        start = i + 1;
                        break;

                    case 3: // in(s.charAt(i), separators)
                        if (s.charAt(i) == '{' && tokens.getLast().GetData().equals('~')) {
                            tokens.removeLast();
                            startedPure = true;
                            donttokenize = true;
                            i++;
                            start = i;
                            continue;
                        }
                        if (start != i) {
                            tokens.add(Tools.GenerateToken(s.substring(start, i), line));
                        }
                        if (s.charAt(i) == '{' && tokens.getLast().GetText().equals("dict")) {
                            tokens.removeLast();
                            dict = true;
                            tokens.add(new TextToken("{", line));
                            start = i + 1;
                            break;
                        }
                        if (s.charAt(i) == '}' && dict) {
                            tokens.add(new TextToken("}", line));
                            dict = false;
                            start = i + 1;
                            break;
                        }
                        if (s.charAt(i) == '(')
                            tokens.add(new StartGroupToken(line));
                        else if (s.charAt(i) == ')')
                            tokens.add(new EndGroupToken(line));
                        else
                            tokens.add(new SeparatorToken(s.charAt(i), line));
                        start = i + 1;
                        break;

                    case 4: // s.charAt(i) == '"' || s.charAt(i) == '\''
                        if(Tools.StringAt(s, "\"\"\"", i)){
                            startedText = "\"\"\"";
                            donttokenize = true;
                        } else {
                            startedText = s.substring(i, i + 1);
                            donttokenize = true;
                        }
                        break;

                    case 5: // s.charAt(i) == '#'
                        if (s.charAt(i + 1) == '{') {
                            startedComment = '}';
                            donttokenize = true;
                            if (start != i && enableComments) {
                                tokens.add(Tools.GenerateToken(s.substring(start, i), line));
                                tokens.add(new SeparatorToken(';', line));
                            }
                            start = i + 2;
                        } else {
                            startedComment = '\n';
                            donttokenize = true;
                            if (start != i && !enableComments) {
                                tokens.add(Tools.GenerateToken(s.substring(start, i), line));
                                tokens.add(new SeparatorToken(';', line));
                            }
                            start = i;
                        }
                        break;

                    }
            } else {
                condition = Tools.If(
                        !startedText.equals(" "),
                                  startedPure,
                                  startedComment != ' '
                );
                switch (condition) {

                    case 0: // startedText != ' '
                        if (Tools.StringAt(s, startedText, i)) {
                            tokens.add(new TextToken(s.substring(start, i + startedText.length()), line));
                            start = i + startedText.length();
                            startedText = " ";
                            donttokenize = false;
                        }
                        break;

                    case 1: // startedPure
                        if (s.charAt(i) == '}') {
                            tokens.add(new TextToken(s.substring(start, i), line));
                            startedPure = false;
                            donttokenize = false;
                            start = i + 1;
                        }
                        break;

                    case 2: // startedComment != ' '
                        if (s.charAt(i) == '\n') {
                            if (startedComment == '\n') {
                                if (enableComments) {
                                    tokens.add(new TextToken(s.substring(start, i), line));
                                    tokens.add(new SeparatorToken(';', line));
                                }
                                donttokenize = false;
                                startedComment = ' ';
                                start = i + 1;
                            } else {
                                if (enableComments) {
                                    tokens.add(new TextToken("#" + s.substring(start, i), line));
                                    tokens.add(new SeparatorToken(';', line));
                                }
                                start = i + 1;
                            }
                        } else if (s.charAt(i) == '}') {
                            if (start != i && enableComments) {
                                tokens.add(new TextToken("#" + s.substring(start, i), line));
                                tokens.add(new SeparatorToken(';', line));
                            }
                            start = i + 1;
                            donttokenize = false;
                            startedComment = ' ';
                        }
                        break;
                    }
                }
            i++;
        }
        if(start < i)
            tokens.add(Tools.GenerateToken(s.substring(start,i), line));
        Token[] t = new Token[tokens.size()];
        return tokens.toArray(t);
    }

    private ADVToken[] ADVTokens(Token[] tokens) throws Exception {
        ListCommandToken main = new ListCommandToken("",null);
        LinkedList<ListCommandToken> all = new LinkedList<>();
        all.add(main);
        ListCommandToken current = all.get(0);
        LinkedList<ADVToken<BaseToken>> allcommands = new LinkedList<>();
        allcommands.add(new CommandToken());
        ADVToken<BaseToken> currentcommand = allcommands.get(0);
        int opengroups = 0;
        HashMap<String, String> replace = new HashMap<>();
        Token t;
        for(int i = 0; i < tokens.length; i++){
            t = tokens[i];
            if(replace.containsKey(t.GetText()))
                t = Tools.GenerateToken(replace.get(t.GetText()), t.GetLine());
            if(t.GetText().equals("replace")){
                replace.put(tokens[i + 1].GetText(), tokens[i + 2].GetText());
                i += 3;
                continue;
            }
            switch (t.GetType()) {
                case "StartGroup":
                    opengroups++;
                    allcommands.add(new GroupToken());
                    currentcommand = allcommands.getLast();
                    break;
                case "EndGroup":
                    opengroups--;
                    GroupToken temp = (GroupToken) allcommands.getLast();
                    allcommands.removeLast();
                    currentcommand = allcommands.getLast();
                    currentcommand.Append(temp);
                    break;
                case "Separator":
                    if (t.GetData().equals(';')) {
                        current.Append(currentcommand);
                        allcommands.clear();
                        allcommands.add(new CommandToken());
                        currentcommand = allcommands.getLast();
                    } else if ((t.GetData().equals('{'))) {
                        String ind = current.GetIndent();
                        if(currentcommand.GetData().get(0).equals("Scripts.Script") &&
                                (currentcommand.GetData().get(0).GetText().equals("to") || currentcommand.GetData().get(0).GetText().equals("elto")))
                            ind = ind.substring(0, ind.length()-1);
                        all.add(new ListCommandToken(ind + "\t", (CommandToken) currentcommand));
                        allcommands.clear();
                        allcommands.add(new CommandToken());
                        currentcommand = allcommands.getLast();
                        current = all.get(all.size() - 1);
                    } else if (t.GetData().equals('}')) {
                        allcommands.clear();
                        allcommands.add(new CommandToken());
                        currentcommand = allcommands.getLast();
                        all.get(all.size() - 2).Append(all.get(all.size() - 1));
                        all.removeLast();
                        current = all.get(all.size()-1);
                    } else if(t.GetText().equals("=")){
                        allcommands.removeLast();
                        allcommands.add(new SetVarToken(currentcommand, all.getLast().GetIndent()));
                        currentcommand = allcommands.getLast();
                    } else {
                        currentcommand.Append(t);
                    }
                    break;
                case "Pair":
                    if(t.GetText().equals("++"))
                        currentcommand.Append(new TextToken("+=1", t.GetLine()));
                    else if(t.GetText().equals("--"))
                        currentcommand.Append(new TextToken("-=1", t.GetLine()));
                    else
                        currentcommand.Append(t);
                    break;
                default:
                    currentcommand.Append(t);
                    break;
            }

        }
        ADVToken[] tt = new ADVToken[main.GetData().size()];
        return main.GetData().toArray(tt);
    }

    private String Translate(ADVToken[] advTokens) throws Exception {
        StringBuilder s = new StringBuilder();
        for(ADVToken t : advTokens) {
                s.append(t.Translate()).append("\n");
        }
        return s.toString();
    }

    private static void Execute(String path, boolean noise) throws Exception {
        Tokenizer t = new Tokenizer();
        String piecode = Tools.ReadFile(path);
        if(noise) {
            System.out.println("PIE CODE: \n");
            System.out.println(piecode);
            System.out.println("-----------------------------------------");
        }
        Token[] tokens = t.Tokens(piecode);
        if(noise) {
            System.out.println("BASIC TOKENS: \n");
            Tools.PrintArr(tokens);
            System.out.println("-----------------------------------------");
        }
        ADVToken[] advTokens = t.ADVTokens(tokens);
        if(noise) {
            System.out.println("ADVANCED TOKENS: \n");
            Tools.PrintArr(advTokens);
            System.out.println("-----------------------------------------");
        }
        String output = t.Translate(advTokens);
        if(noise) {
            System.out.println("TRANSLATED CODE: \n");
            System.out.println(output);
        }
        String[] pathArray = path.split(Pattern.quote("\\"));
        String filename = pathArray[pathArray.length-1].split(Pattern.quote("."))[0];
        if(pathArray.length > 1) {
            String pathtofile = String.join("/", Arrays.copyOfRange(pathArray, 0, pathArray.length - 1));
            Tools.WriteFile(pathtofile + "\\" + filename + ".py", output);
        } else
            Tools.WriteFile(filename + ".py", output);
    }

    public static void Start(String[] args) throws Exception {
        if(args.length > 1){
            for(String s : args) {
                switch (s.split(Pattern.quote("="))[0]) {
                    case("comment"):
                        if (s.split(Pattern.quote("="))[1].equals("false"))
                            Tokenizer.enableComments = false;
                        break;
                    case("noise"):
                        if (s.split(Pattern.quote("="))[1].equals("true"))
                            Tokenizer.enableNoise = true;
                        break;
                }
            }
        }
        if(args.length > 1 && args[1].equals("reverse"))
            RevertTool.Execute(args[0]);
        else
            Execute(args[0], Tokenizer.enableNoise);
    }
}
