package Tokens;

public interface BASETOKEN <T>{
    String toString();
    T GetData();
    String getType();
    String getText();
}
