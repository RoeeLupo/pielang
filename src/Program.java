import Scripts.CompareScript;
import Scripts.LoopScript;
import Tokens.Tools;

public class Program {
    public static void main(String[] args) throws Exception {
        Tools.AddScript(new LoopScript());
        Tools.AddScript(new CompareScript());
        Tokenizer.Start(args);
    }
}
