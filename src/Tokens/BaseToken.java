package Tokens;

public interface BaseToken<T>{
    //Only being used in debugging mode
    String toString();

    //Returns raw data
    T GetData();

    String GetType();
    String GetText();

    int GetLine();
}
