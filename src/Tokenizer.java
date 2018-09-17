import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import Tokens.Advanced.ADVToken;
import Tokens.Advanced.CommandToken;
import Tokens.Advanced.GroupToken;
import Tokens.Advanced.ListCommandToken;
import Tokens.BASETOKEN;
import Tokens.Basic.*;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class Tokenizer {
    private Character[] separators = {'{', '}', ';', '(', ')', '|', '+', '*', '/', '-', '>', '~'}, ws = {' ', '\t', '\n'};
    private String[] operators = {"<=", ">=", "==", "<", ">", "!="};
    private String[] commands = {"loop", "compare", "to", "elseto"};
    private static boolean enableComments = true, enableNoise = false;

    public static String ReadFile(String filePath){
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath)))
        {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }

    public static void WriteFile(String filepath, String text){
        try {
            PrintWriter out = new PrintWriter(filepath);
            out.println(text);
            out.close();
        } catch (Exception ignored){

        }

    }

    private Token[] Tokens(String s){
        LinkedList<Token> tokens = new LinkedList<>();
        int start = 0;
        int i = 0;
        char startedText = ' ';
        char startedComment = ' ';
        boolean startedPure = false;
        boolean donttokenize = false;
        while(i < s.length()){
            if(!donttokenize) {
                if (in(pairAt(s,i), operators)){
                    if (start != i)
                        tokens.add(GenerateToken(s.substring(start, i)));
                    tokens.add(new OperatorToken(pairAt(s, i)));
                    start = i + pairAt(s, i).length();
                } else if (in(s.charAt(i), ws)) {
                    if (start != i)
                        tokens.add(GenerateToken(s.substring(start, i)));
                    start = i + 1;
                } else if (in(s.charAt(i), separators)) {
                    if(s.charAt(i) == '{' && tokens.getLast().GetData().equals('~')) {
                        tokens.removeLast();
                        startedPure = true;
                        donttokenize = true;
                        i++;
                        start = i;
                        continue;
                    }
                    if (start != i)
                        tokens.add(GenerateToken(s.substring(start, i)));
                    if(s.charAt(i) == '(')
                        tokens.add(new StartGroupToken());
                    else if(s.charAt(i) == ')')
                        tokens.add(new EndGroupToken());
                    else
                        tokens.add(new SeparatorToken(s.charAt(i)));
                    start = i + 1;
                } else if(s.charAt(i) == '"' || s.charAt(i) == '\''){
                    if (start != i)
                        tokens.add(GenerateToken(s.substring(start, i)));
                    startedText = s.charAt(i);
                    donttokenize = true;
                } else if(s.charAt(i) == '#'){
                    if(s.charAt(i+1) == '{'){
                        startedComment = '}';
                        donttokenize = true;
                        if (start != i && enableComments) {
                            tokens.add(GenerateToken(s.substring(start, i)));
                            tokens.add(new SeparatorToken(';'));
                        }
                        start = i + 2;
                    } else {
                        startedComment = '\n';
                        donttokenize = true;
                        if (start != i && !enableComments) {
                            tokens.add(GenerateToken(s.substring(start, i)));
                            tokens.add(new SeparatorToken(';'));
                        }
                        start = i;
                    }
                }
            } else {
                if(startedText != ' ') {
                    if (s.charAt(i) == startedText) {
                        tokens.add(new TextToken(s.substring(start, i + 1)));
                        startedText = ' ';
                        donttokenize = false;
                        start = i + 1;
                    }
                } else if(startedPure){
                    if (s.charAt(i) == '}') {
                        tokens.add(new TextToken(s.substring(start, i)));
                        startedPure = false;
                        donttokenize = false;
                        start = i + 1;
                    }
                } else if(startedComment != ' '){
                    if(s.charAt(i) == '\n'){
                        if(startedComment == '\n'){
                            if(enableComments) {
                                tokens.add(new TextToken(s.substring(start, i)));
                                tokens.add(new SeparatorToken(';'));
                            }
                            donttokenize = false;
                            startedComment = ' ';
                            start = i+1;
                        } else {
                            if(enableComments) {
                                tokens.add(new TextToken("#" + s.substring(start, i)));
                                tokens.add(new SeparatorToken(';'));
                            }
                            start = i+1;
                        }
                    } else if (s.charAt(i) == '}'){
                        if(start != i && enableComments){
                            tokens.add(new TextToken("#" + s.substring(start, i)));
                            tokens.add(new SeparatorToken(';'));
                        }
                        start = i + 1;
                        donttokenize = false;
                        startedComment = ' ';
                    }
                }
            }
            i++;
        }
        if(start < i)
            tokens.add(GenerateToken(s.substring(start,i)));
        Token[] t = new Token[tokens.size()];
        return tokens.toArray(t);
    }

    private ADVToken[] ADVTokens(Token[] tokens){
        ListCommandToken main = new ListCommandToken("",null);
        LinkedList<ListCommandToken> all = new LinkedList<>();
        all.add(main);
        ListCommandToken current = all.get(0);
        LinkedList<ADVToken<BASETOKEN>> allcommands = new LinkedList<>();
        allcommands.add(new CommandToken());
        ADVToken<BASETOKEN> currentcommand = allcommands.get(0);
        int opengroups = 0;
        for(Token t: tokens){
            switch (t.getType()) {
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
                        if(currentcommand.GetData().get(0).getType().equals("Script") &&
                                (currentcommand.GetData().get(0).getText().equals("to") || currentcommand.GetData().get(0).getText().equals("elseto")))
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
                    } else if(t.getText().equals("+") && currentcommand.GetData().getLast().getText().equals("+")){
                        currentcommand.GetData().removeLast();
                        currentcommand.Append(new TextToken("+=1"));
                    } else if(t.getText().equals("-") && currentcommand.GetData().getLast().getText().equals("-")){
                        currentcommand.GetData().removeLast();
                        currentcommand.Append(new TextToken("-=1"));
                    } else if(t.getText().equals("*") && currentcommand.GetData().getLast().getText().equals("*")){
                        currentcommand.GetData().removeLast();
                        currentcommand.Append(new TextToken("*=" + currentcommand.GetData().getLast().getText()));
                    } else {
                        currentcommand.Append(t);
                    }
                    break;
                case "Operator":
                    if(t.getText().equals("++")) {
                        currentcommand.Append(new TextToken("+=1"));
                        break;
                    }
                default:
                    currentcommand.Append(t);
                    break;
            }

        }
        ADVToken[] t = new ADVToken[main.GetData().size()];
        return main.GetData().toArray(t);
    }

    private String Translate(ADVToken[] advTokens){
        StringBuilder s = new StringBuilder();
        for(ADVToken t : advTokens) {
                s.append(t.Translate()).append("\n");
        }
        return s.toString();
    }

    private Token GenerateToken(String text){
        if(in(text, commands))
            return new ScriptToken(text);
        try {
            return new NumToken(Integer.parseInt(text));
        } catch (Exception e){
            return new TextToken(text);
        }
    }

    private void PrintArr(Object[] arr){
        for(Object token : arr)
            System.out.println(token);
    }

    private static boolean in(Object o, Object[] arr){
        for(Object obj : arr)
            if(o.equals(obj))
                return true;
        return false;
    }

    private String pairAt(String s, int i){
        if(s.length() <= i+1)
            return s.substring(i,i+1);
        String temp = s.substring(i, i+2);
        if(in(temp.charAt(1), ws))
            return temp.substring(0,1);
        return s.substring(i, i+2);
    }

    public static void Execute(String path, boolean noise){
        Tokenizer t = new Tokenizer();
        String piecode = ReadFile(path);
        if(noise) {
            System.out.println("PIE CODE: \n");
            System.out.println(piecode);
            System.out.println("-----------------------------------------");
        }
        Token[] tokens = t.Tokens(piecode);
        if(noise) {
            System.out.println("BASIC TOKENS: \n");
            t.PrintArr(tokens);
            System.out.println("-----------------------------------------");
        }
        ADVToken[] advTokens = t.ADVTokens(tokens);
        if(noise) {
            System.out.println("ADVANCED TOKENS: \n");
            t.PrintArr(advTokens);
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
            WriteFile(pathtofile + "\\" + filename + ".py", output);
        } else
            WriteFile(filename + ".py", output);
    }

    public static void main(String[] args){
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
        Execute(args[0], Tokenizer.enableNoise);
    }
}
