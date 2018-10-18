package Tokens;

import Tokens.Advanced.CommandToken;
import Tokens.Advanced.GroupToken;
import Tokens.Advanced.ListCommandToken;
import Tokens.Basic.NumToken;
import Tokens.Basic.ScriptToken;
import Tokens.Basic.TextToken;
import Tokens.Basic.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;

public class Tools {
    //linux
    //private static HashMap<String, LinkedList<String>> types = new HashMap<>();
    private static String[] commands = {"loop", "compare", "to", "elto", "replace"};
    private static Character[] ws = {' ', '\t', '\n'};

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

    public static void PrintArr(Object[] arr){
        for(Object token : arr)
            System.out.println(token);
    }

    public static String PairAt(String s, int i){
        if(s.length() <= i+1)
            return s.substring(i,i+1);
        String temp = s.substring(i, i+2);
        if(Tools.in(temp.charAt(1), ws))
            return temp.substring(0,1);
        return s.substring(i, i+2);
    }

    public static boolean StringAt(String text, String s, int i){
        if(text.length() < i + s.length())
            return false;
        return text.substring(i, i + s.length()).equals(s);
    }

    public static boolean in(Object o, Object[] arr){
        for(Object obj : arr)
            if(o.equals(obj))
                return true;
        return false;
    }

    public static Token GenerateToken(String text){
        if(in(text, commands))
            return new ScriptToken(text);
        try {
            return new NumToken(Integer.parseInt(text));
        } catch (Exception e){
            return new TextToken(text);
        }
    }

    public static String TranslateScript(ListCommandToken l){
        String command = l.GetTitle().GetData().get(0).GetText();
        if(command.equals("loop"))
            return TranslateLoop(l);
        else if(command.equals("compare"))
            return TranslateCompare(l);
        //can't happen anyway
        return "error";
    }

    private static String TranslateLoop(ListCommandToken l){
        StringBuilder s = new StringBuilder();
        LinkedList<BaseToken> title = (LinkedList <BaseToken>) l.GetTitle().GetData().get(1).GetData();
        String name = title.get(0).GetText();
        StringBuilder from = new StringBuilder(), to = new StringBuilder(), construct = new StringBuilder();
        int i;
        for(i = 1; i < title.size() && !title.get(i).GetText().equals(">"); i++)
            from.append(title.get(i).GetText()).append(" ");
        i++;
        for(; i < title.size() && !title.get(i).GetText().equals("|"); i++)
            to.append(title.get(i).GetText()).append(" ");
        construct.append(title.get(i+1).GetText()).append("=").append(title.get(i+2).GetText());
        s.append(name).append("=").append(from).append("\n");
        s.append(l.GetIndent(), 0, l.GetIndent().length()-1).append("while ").append(name).append("<").append(to).append(": \n");
        s.append(l.TranslateCommands());
        s.append(l.GetIndent()).append(name).append(construct);
        return s.toString();
    }

    private static String TranslateCompare(ListCommandToken l){
        StringBuilder s = new StringBuilder();
        String baseVar = ((GroupToken) l.GetTitle().GetData().get(1)).BaseTranslate();
        String operator = l.GetTitle().GetData().get(2).GetText();
        ListCommandToken to;
        to = (ListCommandToken) l.GetData().get(0);
        s.append("if (").append(baseVar)
                .append(operator).append(" ").append(((GroupToken)to.GetTitle().GetData().get(1)).BaseTranslate()).append("):\n");
        s.append(to.TranslateCommands());
        String ifstr;
        for(int i = 1; i < l.GetData().size(); i++){
            to = (ListCommandToken) l.GetData().get(i);
            if(to.GetTitle().GetData().get(0).GetText().equals("elto")) {
                ifstr = "elif";
            }
            else {
                ifstr = "if";
            }
            s.append(l.GetIndent(), 0, l.GetIndent().length()-1).append(ifstr).append(" (").append(baseVar)
                    .append(operator).append(" ").append(((GroupToken)to.GetTitle().GetData().get(1)).BaseTranslate()).append("):\n");
            s.append(to.TranslateCommands());
        }
        return s.toString();
    }

    public static int If(boolean... conditions){
        for(int i = 0; i < conditions.length; i++)
            if(conditions[i])
                return i;
        return -1;
    }

    /*
    public static void UpdateType(String type, String newtype){
        LinkedList<String> temp;
        if(types.containsKey(type))
            temp = types.get(type);
        else
            temp = new LinkedList<>();
        temp.add(newtype);
        types.put(type, temp);
    }

    public static boolean isType(String base, String type){
        if(base.equals(type))
            return true;
        if(!types.containsKey(base) || types.containsKey(type))
            return false;
        for(String s : types.get(type))
            if(isType(base, s)) return true;
        return false;
    }
    */

}
