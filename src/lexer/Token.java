package lexer;

/**
 * Created by hzg on 2016/11/21.
 */
public class Token {
    public final int tag;

    public Token(int t) {
        this.tag = t;
    }

    public String toString(){
        return "" + (char) tag;
    }

//    public static void main(String[] args){
//        Token tok=new Token('a');
//        System.out.println(tok);
//    }
}
