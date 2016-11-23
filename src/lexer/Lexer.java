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

    /* token序列 */
    private HashMapSub<String, Integer> tokens = new HashMapSub<String, Integer>();
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
        /* 生成关键字后，将关键字装到keywords中 */
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
//        this.reserve(new Word("char", Tag.CHAR));上面有了
        this.reserve(new Word("float", Tag.FLOAT));
//        this.reserve(new Word("bool",Tag.BOOL));
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
                tokens.put("+", Word.add.tag);
                return null;
            case '-':
                readch();
                tokens.put("-", Word.reduce.tag);
                return null;
            case '=':
                if (readch('=')) {
                    tokens.put("==", Word.eq.tag);
//                    return Word.eq;
                    return null;
                } else {
                    tokens.put("=", Word.e.tag);
//                    return Word.e;
                    return null;
                }
            case '>':
                if (readch('=')) {
                    tokens.put(">=", Word.ge.tag);
//                    return Word.ge;
                    return null;
                } else {
                    tokens.put(">", Word.g.tag);
//                    return new Token('>');
                    return null;
                }
            case '<':
                if (readch('=')) {
                    tokens.put("<=", Word.le.tag);
//                    return Word.le;
                    return null;
                } else if (readch('>')) {
                    tokens.put("<>", Word.lg.tag);
                    return null;
                } else {
                    tokens.put("<", Word.l.tag);
//                    return new Token('<');
                    return null;
                }
            case '*':
                if (readch('/')) {
                    tokens.put("*/", Word.rAnnota.tag);
                    return null;
                } else {
                    tokens.put("*", Word.multiply.tag);
//                    return new Token('*');
                    return null;
                }
            case '/':
                if (readch('*')) {
                    tokens.put("/*", Word.lAnnota.tag);
                    return null;
//                    return new Token('/*');
                } else {
                    tokens.put("/", Word.divide.tag);
//                    return new Token('/');
                    return null;
                }
            case ':':
                readch();
                tokens.put(":", Word.colon.tag);
//                return new Token(':');
                return null;
            case ';':
                readch();
                tokens.put(";", Word.semicolon.tag);
//                return new Token(';');
                return null;
            case ',':
                readch();
                tokens.put(",", Word.comma.tag);
//                return new Token(',');
                return null;
            case '\'':
                readch();
                tokens.put("\'", Word.singleQuotes.tag);
//                return new Token('\'');
                return null;
            case '(':
                readch();
                tokens.put("(", Word.leftBracket.tag);
//                return new Token('(');
                return null;
            case ')':
                readch();
                tokens.put(")", Word.rightBracket.tag);
//                return new Token(')');
                return null;
            case '.':
                readch();
                tokens.put(".", Word.fullStop.tag);
//                return new Token('.');
                return null;

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
            tokens.put(n.toString(), 35);
//            return n;
            return null;
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

            Word w = words.get(s);
            /* 如果是关键字或者是类型的话，w不应该是空的 */
            if (w != null) {
                switch (s) {
                    case "program":
                        tokens.put(s, words.get("program").tag);
                        return null;
                    case "var":
                        tokens.put(s, words.get("var").tag);
                        return null;
                    case "interger":
                        tokens.put(s, words.get("interger").tag);
                        return null;
                    case "bool":
                        tokens.put(s, words.get("bool").tag);
                        return null;
                    case "real":
                        tokens.put(s, words.get("real").tag);
                        return null;
                    case "char":
                        tokens.put(s, words.get("char").tag);
                        return null;
                    case "const":
                        tokens.put(s, words.get("const").tag);
                        return null;
                    case "begin":
                        tokens.put(s, words.get("begin").tag);
                        return null;
                    case "if":
                        tokens.put(s, words.get("if").tag);
                        return null;
                    case "then":
                        tokens.put(s, words.get("then").tag);
                        return null;
                    case "else":
                        tokens.put(s, words.get("else").tag);
                        return null;
                    case "while":
                        tokens.put(s, words.get("while").tag);
                        return null;
                    case "do":
                        tokens.put(s, words.get("do").tag);
                        return null;
                    case "for":
                        tokens.put(s, words.get("for").tag);
                        return null;
                    case "to":
                        tokens.put(s, words.get("to").tag);
                        return null;
                    case "end":
                        tokens.put(s, words.get("end").tag);
                        return null;
                    case "read":
                        tokens.put(s, words.get("read").tag);
                        return null;
                    case "write":
                        tokens.put(s, words.get("write").tag);
                        return null;
                    case "true":
                        tokens.put(s, words.get("true").tag);
                        return null;
                    case "false":
                        tokens.put(s, words.get("false").tag);
                        return null;

                    case "int":
                        tokens.put(s, words.get("int").tag);
                        return null;
                    case "float":
                        tokens.put(s, words.get("float").tag);
                        return null;

                }
//                return w; /* 说明是关键字 或者是类型名 */
                return null;

            }

            /* 否则就是一个标识符id（identifier） */
            tokens.put(s, Tag.ID);
            return null;
        }

        /* peek中的任意字符都被认为是词法单元返回 */
        Token tok = new Token(peek);
        if ((int) peek != 0xffff)
            tokens.put(tok.toString(), -1);//出错？？其他
        peek = ' ';

//        return tok;
        return null;
    }

    //读取完文件的代码后，保存Tokens
    public void saveTokens() throws IOException {
//        FileWriter writer = new FileWriter("Tokens表.txt");
        FileWriter writer = new FileWriter("输出.txt");
        writer.write("【单词的值】       【种别码】\n");//\r Mac \n Unix/Linux \r\n Windows
        writer.write("\r\n");
        String tok = tokens.toString();
        //写入文件
        writer.write(tok + "\r\n");
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