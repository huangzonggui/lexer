import lexer.Lexer;

import java.io.IOException;

/**
 * Created by hzg on 2016/11/21.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        Lexer lexer=new Lexer();
        //如果没有读到文件的结尾，继续循环scan
        while (lexer.getReaderState()==false) {
            lexer.scan();
        }
        lexer.saveToken();
        lexer.saveWords();
    }
}
