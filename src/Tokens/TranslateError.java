package Tokens;

public class TranslateError extends Exception {

    String message;

    public TranslateError(String message){
        this.message = message;

    }

    @Override
    public String getMessage(){
        return message;
    }
    public synchronized Throwable fillInStackTrace()  { return this; }

}
