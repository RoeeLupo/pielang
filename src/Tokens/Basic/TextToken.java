package Tokens.Basic;

import Tokens.BASETOKEN;

public class TextToken extends Token<String>  {

    public TextToken(String data) {
        super("Text", data);
    }

}
