package symbols;

import lexer.Tag;
import lexer.Word;

/**
 * Created by hzg on 2016/11/21.
 */
public class Type extends Word {
    public Type(String s, int tag) {
        super(s, tag);
    }

    public static final Type
            Int = new Type("int", Tag.INTEGER),
            Float = new Type("float", Tag.FLOAT),
            Char = new Type("char", Tag.CHAR),
            Bool = new Type("bool", Tag.BOOL);
}
