package Tokens;

import Scripts.Script;
import Tokens.Advanced.ADVToken;
import Tokens.Basic.NumToken;
import Tokens.Basic.ScriptToken;
import Tokens.Basic.TextToken;
import Tokens.Basic.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

public class Tools {
    //linux
    //private static HashMap<String, LinkedList<String>> types = new HashMap<>();
    private static LinkedList<String> commands = new LinkedList<>();
    private static Character[] ws = {' ', '\t', '\n'};
    private static LinkedList<Script> scripts = new LinkedList<>();

    public static void AddScript(Script s){
        Tools.scripts.add(s);
        Tools.commands.add(s.command);
    }

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

    public static boolean in(Object o, LinkedList arr){
        for(Object obj : arr)
            if(o.equals(obj))
                return true;
        return false;
    }

    public static Token GenerateToken(String text, int line){
        if(in(text, commands))
            return new ScriptToken(text, line);
        try {
            return new NumToken(Integer.parseInt(text), line);
        } catch (Exception e){
            return new TextToken(text, line);
        }
    }

    public static String TranslateScript(ADVToken script) throws Exception {
        for(Script s : scripts){
            if(s.CompareCommand(script)){
                try {
                    return s.Translate(script);
                }
                catch (Exception e) {
                    throw new TranslateError("ScriptError: " + s.ErrorMessage());
                }
            }
        }
        return "ScriptError: Command not found";
    }

    public static int If(boolean... conditions){
        for(int i = 0; i < conditions.length; i++)
            if(conditions[i])
                return i;
        return -1;
    }

    public static int compareIndent(String s1, String s2){
        return s1.length() - s2.length();
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
