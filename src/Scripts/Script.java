package Scripts;

import Tokens.Advanced.ADVToken;

public abstract class Script {

    // This needs be set to the name of the command, or the keyword that will define that this indeed a ScriptToken.
    // By default the name will compared to the ADVToken name in the function CompareCommand.
    public String command;

    // You can call Super if you want to enter the command name yourself instead of doing that in the Program.main function.
    public Script(String command){
        this.command = command;
    }

    // ErrorMessage will be called in case any exception is thrown while translating the script.
    // It must return a string, that will indicate the error, and possibly what is needed to be done in order to fix that error.
    public String ErrorMessage(){
        return "The error occurred while translating the script " + command;
    }

    // Translate will be called whenever CompareCommand does make sure ADVToken is the proper script that can be handled in this class.
    // You need to return a string with the python code you generated from the ADVToken.
    public abstract String Translate(ADVToken script) throws Exception;

    // CompareCommand is called every time the Tokenizer get a ScriptToken and it needs to be translated.
    // This function should determine whether your Script can handle the ADVToken received or not.
    // By default the name will compared to the ADVToken name in the function CompareCommand.
    public boolean CompareCommand(ADVToken script){
        return script.GetName().equals(command);

    }

}
