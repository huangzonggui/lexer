package lexer;

/**
 * Created by hzg on 2016/10/24.
 */

import java.io.*;
import java.util.*;

public class Lexer {
    public static int line = 1;     /* 记录行号 */
    char peek = ' ';        /* 下一个读入字符 */

    //单词
    Hashtable<String, Word> words = new Hashtable<String, Word>();

    //符号表
    private HashMapSub<String, Integer> table = new HashMapSub<String, Integer>();//记录种别码
    //token序列(因为通过table.put进去的没有按顺序)
    private List<String> tokens = new LinkedList<String>();//记录顺序
    /* 读取文件变量 */
    BufferedReader reader = null;
    /* 保存当前是否读取到了文件的结尾  */
    private Boolean isEnd = false;

    /* 是否读取到文件的结尾 */
    public Boolean getReaderState() {
        return this.isEnd;
    }

    //将关键字装到words中
    void reserve(Word w) {
        words.put(w.lexme, w);
    }

    /*
     * 构造函数中将关键字和类型添加到hashtable words中
     */
    public Lexer() {
        /* 初始化读取文件变量 */
        try {
            reader = new BufferedReader(new FileReader("输入.txt"));
        } catch (IOException e) {
            System.out.print(e);
        }

        //关键字加载进来是为了判断是否存在这个单词，来判断是否是关键字，第280行左右
        /* 生成关键字后，将关键字装到word中 */
        this.reserve(new Word("program", Tag.PROGRAM));
        this.reserve(new Word("var", Tag.VAR));
        this.reserve(new Word("interger", Tag.INTEGER));
        this.reserve(new Word("bool", Tag.BOOL));
        this.reserve(new Word("real", Tag.REAL));
        this.reserve(new Word("char", Tag.CHAR));
        this.reserve(new Word("const", Tag.REAL));
        this.reserve(new Word("real", Tag.CONST));
        this.reserve(new Word("begin", Tag.BEGIN));
        this.reserve(new Word("if", Tag.IF));
        this.reserve(new Word("then", Tag.THEN));
        this.reserve(new Word("else", Tag.ELSE));
        this.reserve(new Word("while", Tag.WHILE));
        this.reserve(new Word("do", Tag.DO));
        this.reserve(new Word("for", Tag.FOR));
        this.reserve(new Word("to", Tag.TO));
        this.reserve(new Word("end", Tag.END));
        this.reserve(new Word("read", Tag.READ));
        this.reserve(new Word("write", Tag.WRITE));
        this.reserve(new Word("true", Tag.TRUE));
        this.reserve(new Word("false", Tag.FALSE));

        /* 类型 */
        this.reserve(new Word("int", Tag.INT));
        this.reserve(new Word("float", Tag.FLOAT));
    }

    public void readch() throws IOException {
        /* 这里应该是使用的是 */
        peek = (char) reader.read();
        if ((int) peek == 0xffff) {
            this.isEnd = true;
        }
    }

    public Boolean readch(char ch) throws IOException {
        readch();
        if (this.peek != ch) {
            return false;
        }

        this.peek = ' ';
        return true;
    }

    public Token scan() throws IOException {
        /* 消除空格 */
        for (; ; readch()) {
            if (peek == ' ' || peek == '\t')
                //不做任何处理
                continue;
            else if (peek == '\n')
                //回车符
                line = line + 1;
            else
                break;
        }

        //1、符号：读到第一个字符的时候，判断下一个是什么，以便区分==,>=,<=：状态机
        switch (peek) {
            case '+':
                readch();
                tokens.add("+");
                table.put("+", Word.add.tag);
                return new Word("+", Tag.ADD);
            case '-':
                readch();
                tokens.add("-");
                table.put("-", Word.reduce.tag);
                return Word.reduce;
            case '=':
                if (readch('=')) {
                    tokens.add("==");
                    table.put("==", Word.eq.tag);
                    return Word.eq;
                } else {
                    tokens.add("=");
                    table.put("=", Word.e.tag);
                    return Word.e;
                }
            case '>':
                if (readch('=')) {
                    tokens.add(">=");
                    table.put(">=", Word.ge.tag);
                    return Word.ge;
                } else {
                    tokens.add(">");
                    table.put(">", Word.g.tag);
                    return Word.g;
                }
            case '<':
                if (readch('=')) {
                    tokens.add("<=");
                    table.put("<=", Word.le.tag);
                    return Word.le;
                } else if (readch('>')) {
                    tokens.add("<>");
                    table.put("<>", Word.lg.tag);
                    return Word.lg;
                } else {
                    tokens.add("<");
                    table.put("<", Word.l.tag);
                    return Word.l;
                }
            case '*':
                if (readch('/')) {
                    tokens.add("*/");
                    table.put("*/", Word.rAnnota.tag);
                    return Word.rAnnota;
                } else {
                    tokens.add("*");
                    table.put("*", Word.multiply.tag);
                    return Word.multiply;
                }
            case '/':
                if (readch('*')) {
                    tokens.add("/*");
                    table.put("/*", Word.lAnnota.tag);
                    return Word.lAnnota;
                } else {
                    tokens.add("/");
                    table.put("/", Word.divide.tag);
                    return Word.divide;
                }
            case ':':
                if (readch('=')) {
                    tokens.add(":=");
                    table.put(":=", Word.ee.tag);
                    return Word.ee;
                } else {
                    tokens.add(":");
                    table.put(":", Word.colon.tag);
                    return Word.colon;
                }
            case ';':
                readch();
                tokens.add(";");
                table.put(";", Word.semicolon.tag);
                return Word.semicolon;
            case ',':
                readch();
                tokens.add(",");
                table.put(",", Word.comma.tag);
                return Word.comma;
            case '\'':
                readch();
                tokens.add("\'");
                table.put("\'", Word.singleQuotes.tag);
                return Word.singleQuotes;
            case '(':
                readch();
                tokens.add("(");
                table.put("(", Word.leftBracket.tag);
                return Word.leftBracket;
            case ')':
                readch();
                tokens.add(")");
                table.put(")", Word.rightBracket.tag);
                return Word.rightBracket;
            case '.':
                readch();
                tokens.add(".");
                table.put(".", Word.fullStop.tag);
                return Word.fullStop;
            case '#':
                readch();
                tokens.add("#");
                table.put("#", "结束");
                return new Token(peek);

        }

        /* 2、数字：下面是对数字的识别，根据文法的规定的话，这里的
         * 数字只要是能够识别整数就行.
         */
        if (Character.isDigit(peek)) {
            int value = 0;
            do {
                value = 10 * value + Character.digit(peek, 10);
                readch();
            } while (Character.isDigit(peek));

            Num n = new Num(value);
            tokens.add(n.toString());//常数
            table.put(n.toString(), Tag.CONSTANT);//n.toString的原因是因为在写时候通过string key来获取种别码的，如果是int型的话，获取不到种别码
            return n;
        }

        /*
         * 3、字母：关键字、标识符、not、and、or的识别
         */
        if (Character.isLetter(peek)) {
            StringBuffer sb = new StringBuffer();

            /* 首先得到整个的一个分割 */
            do {
                sb.append(peek);
                readch();
            } while (Character.isLetterOrDigit(peek));

            /* 判断是关键字、标识符 、*/
            String s = sb.toString();
            tokens.add(s);

            Word w = words.get(s);
            /* 如果是关键字或者是类型的话，w不应该是空的 */
            if (w != null) {
                switch (s) {
                    case "program":
                        table.put("program", Tag.PROGRAM);
                        return new Word("program", Tag.PROGRAM);
                    case "var":
                        table.put("var", Tag.VAR);
                        return new Word("var", Tag.VAR);
                    case "interger":
                        table.put("interger", Tag.INTEGER);
                        return new Word("interger", Tag.INTEGER);
                    case "bool":
                        table.put("bool", Tag.BOOL);
                        return new Word("bool", Tag.BOOL);
                    case "real":
                        table.put("real", Tag.REAL);
                        return new Word("real", Tag.REAL);
                    case "char":
                        table.put("char", Tag.CHAR);
                        return new Word("char", Tag.CHAR);
                    case "const":
                        table.put("const", Tag.CONST);
                        return new Word("const", Tag.CONST);
                    case "begin":
                        table.put("begin", Tag.BEGIN);
                        return new Word("begin", Tag.BEGIN);
                    case "if":
                        table.put("if", Tag.IF);
                        return new Word("if", Tag.IF);
                    case "then":
                        table.put("then", Tag.THEN);
                        return new Word("then", Tag.THEN);
                    case "else":
                        table.put("else", Tag.ELSE);
                        return new Word("else", Tag.ELSE);
                    case "while":
                        table.put("while", Tag.WHILE);
                        return new Word("while", Tag.WHILE);
                    case "do":
                        table.put("do", Tag.DO);
                        return new Word("do", Tag.DO);
                    case "for":
                        table.put("for", Tag.FOR);
                        return new Word("for", Tag.FOR);
                    case "to":
                        table.put("to", Tag.TO);
                        return new Word("to", Tag.TO);
                    case "end":
                        table.put("end", Tag.END);
                        return new Word("end", Tag.END);
                    case "read":
                        table.put("read", Tag.READ);
                        return new Word("read", Tag.READ);
                    case "write":
                        table.put("write", Tag.WRITE);
                        return new Word("write", Tag.WRITE);
                    case "true":
                        table.put("true", Tag.TRUE);
                        return new Word("true", Tag.TRUE);
                    case "false":
                        table.put("false", Tag.FALSE);
                        return new Word("false", Tag.FALSE);
                    case "int":
                        table.put("int", Tag.INT);
                        return new Word("int", Tag.INT);
                    case "float":
                        table.put("float", Tag.FLOAT);
                        return new Word("float", Tag.FLOAT);

                }
            }

            /* 否则就是一个标识符id（identifier） */
            table.put(s, Tag.ID);
            return new Word(s, Tag.ID);
        }

        Token tok = new Token(peek);
        table.put(tok.toString(), Tag.ERROR);
        if ((int) peek != 0xffff)
            tokens.add(tok.toString());//其他%、^、&、@等符号
        peek = ' ';

        return tok;
    }

    //保存词法分析结果
    public void saveToken() throws IOException {
        FileWriter writer = new FileWriter("Tokens.txt");
        writer.write("【单词的值】       【种别码】\n");//\r Mac \n Unix/Linux \r\n Windows
        writer.write("\r\n");
        for (int i = 0; i < tokens.size(); i++) {
            String tok = tokens.get(i);
            writer.write(tok + "\t\t\t\t" + table.get(tok) + "\r\n");
        }
        writer.flush();
    }

    //所有的关键字
    public void saveWords() throws IOException {
        FileWriter writer = new FileWriter("words.txt");
        writer.write("所有的关键字\n");
        writer.write("【关键字】       【种别码】\n");
        writer.write("\r\n");
        Enumeration<String> e = words.keys();
        while (e.hasMoreElements()) {
            String str = e.nextElement();
            Word word = words.get(str);
            writer.write(str + "\t\t\t\t\t" + word.tag + "\r\n");
        }
        writer.flush();
    }

}