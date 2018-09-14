package Tokens;

import java.util.LinkedList;

public abstract class ADVToken <T> implements Translatable {

    protected String type;
    protected LinkedList<T> data;

    public ADVToken(String type) {
        this.type = type;
        this.data = new LinkedList<>();
    }

    public String getType() {
        return type;
    }

    public LinkedList<T> GetData(){
        return data;
    }

    @Override
    public abstract String toString();

    public abstract void Append(T t);

}
