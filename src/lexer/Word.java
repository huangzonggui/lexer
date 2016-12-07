package lexer;


/**
 * Created by hzg on 2016/11/21.
 */
public class Word extends Token {

    public String lexme = "";

    public Word(String s, int t) {
        super(t);
        this.lexme = s;
    }

    public String toString() {
        return this.lexme;
    }

    //非关键字的tag就是种别码要在这里拿
    public static final Word
            //运算符
            not = new Word("not", Tag.NOT),
            and = new Word("&&", Tag.AND),
            or = new Word("||", Tag.OR),
            add = new Word("+", Tag.ADD),
            reduce = new Word("-", Tag.REDUCE),
            multiply = new Word("*", Tag.MULTIPLY),
            divide = new Word("/", Tag.DIVIDE),
            g = new Word(">", Tag.GREATER),
            l = new Word("<", Tag.LESS),
            le = new Word("<=", Tag.LE),
            ge = new Word(">=", Tag.GE),
            eq = new Word("==", Tag.EQ),
            lg = new Word("<>", Tag.LG),

            //界符
            e = new Word("=", Tag.ASSIGN),
            ee=new Word(":=",Tag.EE),
            semicolon = new Word(";", Tag.SEMICOLON),
            comma = new Word(",", Tag.COMMA),
            singleQuotes = new Word("\'", Tag.SINGLEQUOTES),
            lAnnota = new Word("/*", Tag.LANNOTA),
            rAnnota = new Word("*/", Tag.RANNOTA),
            colon = new Word(":", Tag.COLON),
            leftBracket = new Word("(", Tag.LEFTBRACKET),
            rightBracket = new Word("(", Tag.RIGHTBRACKET),
            fullStop = new Word(".", Tag.FULLSTOP);
}
