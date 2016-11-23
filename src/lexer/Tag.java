package lexer;

/**
 * Created by hzg on 2016/11/21.
 * Sample语言单词的编码
 */
public class Tag {
    public final static int
            //关键字
            PROGRAM = 1,
            VAR = 2,
            INTEGER = 3,
            BOOL = 4,
            REAL = 5,
            CHAR = 6,
            CONST = 7,
            BEGIN = 8,
            IF = 9,
            THEN = 10,
            ELSE = 11,
            WHILE = 12,
            DO = 13,
            FOR = 14,
            TO = 15,
            END = 16,
            READ = 17,
            WRITE = 18,
            TRUE = 19,
            FALSE = 20,
            FLOAT = 54,
            INT = 55,
    //运算符
    NOT = 21,
            AND = 22,
            OR = 23,
            ADD = 24,//+
            REDUCE = 25,//-
            MULTIPLY = 26,/* * */
            DIVIDE = 27,/* / */
            LESS = 28,/* < */
            GREATER = 29,/* > */
            LE = 30,/* <= */
            GE = 31,/* >= */
            EQ = 32,/* == */
            LG = 33,//<>
    //标识符
    ID = 34,
    //常数
    CONSTANT = 35,
//            INTEGERCONSTANT = 35,//整常数
//            REALCONSTANT = 36,//实常数
//            CHARCONSTANT = 37,//字符常数
//            BOOLCONSTANT = 38,//布尔常数
    //界符
    ASSIGN = 39,/* = */
            SEMICOLON = 40,/* ; */
            COMMA = 41,/* , */
            SINGLEQUOTES = 42,/* ' */
            LANNOTA = 43,/* /* */
            RANNOTA = 44,/* /* */
            COLON = 45,/* : */
            LEFTBRACKET = 46,/* ( */
            RIGHTBRACKET = 47,/* ) */
            FULLSTOP = 48;/* . */
}
