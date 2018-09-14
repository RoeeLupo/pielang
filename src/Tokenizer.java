import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import Tokens.*;
import java.util.LinkedList;
import java.util.zip.Adler32;

public class Tokenizer {
    private Character[] separators = {'{', '}', ';', '(', ')', '|', '+', '*', '/', '-', '>', '~'}, ws = {' ', '\t', '\n'};
    private String[] commands = {"loop"};

    private String ReadFile(String filePath){
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

    private Token[] Tokens(String s){
        LinkedList<Token> tokens = new LinkedList<>();
        int start = 0;
        int i = 0;
        char startedText = ' ';
        boolean startedPure = false;
        while(i < s.length()){
            if(!startedPure && startedText == ' ') {
                if (in(s.charAt(i), separators)) {
                    if(s.charAt(i) == '{' && tokens.getLast().GetData().equals('~')) {
                        tokens.removeLast();
                        startedPure = true;
                        i++;
                        start = i;
                        continue;
                    }
                    if (start != i)
                        tokens.add(GenerateToken(s.substring(start, i)));
                    tokens.add(new SeparatorToken(s.charAt(i)));
                    start = i + 1;
                } else if (in(s.charAt(i), ws)) {
                    if (start != i)
                        tokens.add(GenerateToken(s.substring(start, i)));
                    start = i + 1;
                } else if(s.charAt(i) == '"' || s.charAt(i) == '\''){
                    if (start != i)
                        tokens.add(GenerateToken(s.substring(start, i)));
                    startedText = s.charAt(i);
                }
            } else {
                if(startedText != ' ') {
                    if (s.charAt(i) == startedText) {
                        tokens.add(new TextToken(s.substring(start, i + 1)));
                        startedText = ' ';
                        start = i + 1;
                    }
                } else {
                    if (s.charAt(i) == '}') {
                        tokens.add(new TextToken(s.substring(start, i)));
                        startedPure = false;
                        start = i + 1;
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
        CommandToken temp = new CommandToken();
        for(Token t: tokens){
            if(t.getType().equals("Separator")) {
                if (t.GetData().equals(';')) {
                    current.Append(temp);
                    temp = new CommandToken();
                } else if ((t.GetData().equals('{'))) {
                    all.add(new ListCommandToken(current.GetIndent() + "\t", temp));
                    temp = new CommandToken();
                    current = all.get(all.size() - 1);
                } else if (t.GetData().equals('}')) {
                    temp = new CommandToken();
                    all.get(all.size() - 2).Append(all.get(all.size() - 1));
                    all.removeLast();
                } else {
                    temp.Append(t);
                }
            } else {
                    temp.Append(t);
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

    private boolean in(Object o, Object[] arr){
        for(Object obj : arr)
            if(o.equals(obj))
                return true;
        return false;
    }

    public static void main(String[] args){
        Tokenizer t = new Tokenizer();
        System.out.println("PIE CODE: \n");
        System.out.println(t.ReadFile(args[0]));
/*
        System.out.println("-----------------------------------------");
        System.out.println("BASE TOKENS: \n");
        t.PrintArr(t.Tokens(t.ReadFile(args[0])));
        System.out.println("-----------------------------------------");
        System.out.println("TOKENS: \n");
        t.PrintArr(t.ADVTokens(t.Tokens(t.ReadFile("args[0]))));
*/
        System.out.println("-----------------------------------------");
        System.out.println("TRANSLATED CODE: \n");
        System.out.println(t.Translate(t.ADVTokens(t.Tokens(t.ReadFile(args[0])))));
    }
}
