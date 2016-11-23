package lexer;

/**
 * Created by hzg on 2016/11/21.
 */
public class Num extends Token {
    private final int value;

    public Num(int v) {
        super(v);
        this.value = v;
    }

    public String toString() {
        return "" + value;
    }
}
