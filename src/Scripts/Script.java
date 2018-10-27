package Scripts;

import Tokens.Advanced.ADVToken;

public abstract class Script {

    public String command;

    public abstract String ErrorMessage();
    public abstract String Translate(ADVToken script) throws Exception;
    public abstract boolean CompareCommand(ADVToken script);

}
