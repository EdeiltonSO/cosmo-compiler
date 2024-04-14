package cosmo;

public class Token {
    public final static byte IDENTIFIER = 0, INT_LITERAL = 1, FLOAT_LITERAL = 2, BOOL_LITERAL = 3, PLUS = 4, MINUS = 5,
        DIVIDE = 6, TIMES = 7, OR = 8, AND = 9, LESS = 10, GREATER = 11, EQUALS = 12, SEMICOLON = 13, COLON = 14,
        ASSIGN = 15, COMMA = 16, DOT = 17, LEFT_PAREN = 18, RIGHT_PAREN = 19, ERROR = 20, EOF = 21;

    public final static String[] spellings = {
        "<identifier>", "<integer-literal>", "<float-literal>", "<bool-literal>", "+", "-", "/", "*", "or", "and", "<",
        ">", "=", ";", ":", ":=", ",", ".", "(", ")", "<error>", "<eof>"
    };

    public byte kind;
    public String spelling;
    public int column, line;

    public Token(byte kind, String spelling, int column, int line) {
        this.kind = kind;
        this.spelling = spelling;
        this.column = column;
        this.line = line;
    }
}
