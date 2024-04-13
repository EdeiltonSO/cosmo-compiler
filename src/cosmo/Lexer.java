package cosmo;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class Lexer {
    private String input;
    private char current_char;
    private int current_pos = 0, current_line = 1, current_column = 1;
    public byte current_kind;
    private StringBuffer current_spelling;

    public Lexer(String input) {
        this.input = input;
        this.current_char = input.charAt(current_pos);
        this.current_spelling = new StringBuffer();
    }

    private boolean lookahead(char c) {
        return !is_at_end() && input.charAt(current_pos + 1) == c;
    }

    private void advance() {
        current_pos++;
        current_char = !is_at_end() ? input.charAt(current_pos) : '\0';
        current_column++;
    }

    private void take_it() {
        current_spelling.append(current_char);
        advance();
    }

    private void skip_separator() {
        switch(current_char) {
            // Comentário
            case '!':
                advance();
                while (current_char != '\n' && current_char != '\0') {
                    advance();
                }
                if (current_char == '\n') {
                    advance();
                    current_line++;
                    current_column = 1;
                }
                break;
            // Separadores
            case ' ':
            case '\t':
            case '\r':
                advance();
                break;
            case '\n':
                advance();
                current_line++;
                current_column = 1;
                break;
        }
    }

    private byte scan_token() {
        switch(current_char) {
            case '+':
                take_it();
                return Token.PLUS;
            case '-':
                take_it();
                return Token.MINUS;
            case '/':
                take_it();
                return Token.DIVIDE;
            case '*':
                take_it();
                return Token.TIMES;
            case '<':
                take_it();
                return Token.LESS;
            case '>':
                take_it();
                return Token.GREATER;
            case '=':
                take_it();
                return Token.EQUALS;
            case ';':
                take_it();
                return Token.SEMICOLON;
            case ':':
                take_it();
                if (current_char == '=') {
                    take_it();
                    return Token.ASSIGN;
                } else {
                    return Token.COLON;
                }
            case ',':
                take_it();
                return Token.COMMA;
            case '(':
                take_it();
                return Token.LEFT_PAREN;
            case ')':
                take_it();
                return Token.RIGHT_PAREN;
            case '\0':
                return Token.EOF;
            default:
                if (Character.isDigit(current_char)) {
                    // integer-literal ou float-literal
                    return scan_number();
                } else if (current_char == '.') {
                    take_it();
                    // float-literal começando com ponto
                    if (Character.isDigit(current_char)) {
                        scan_float();
                        return Token.FLOAT_LITERAL;
                    } else {
                        return Token.DOT;
                    }
                } else if (Character.isLetter(current_char)) {
                    // identifier (variável, função, palavra reservada) ou bool-literal
                    return scan_identifier();
                } else {
                    take_it();
                    return Token.ERROR;
                }
        }
    }

    private void scan_float() {
        take_it();
        while (Character.isDigit(current_char)) {
            take_it();
        }
    }

    // <integer-literal> ::= <digit> | <integer-literal><digit>
    // <float-literal> ::= <digit>.<digit> | <digit>. | .<digit>
    private byte scan_number() {
        boolean is_float = false;
        while (Character.isDigit(current_char)) {
            take_it();
        }
        if (current_char == '.') {
            take_it();
            is_float = true;
            scan_float();
        }
        return is_float ? Token.FLOAT_LITERAL : Token.INT_LITERAL;
    }

    // "<identifier>", "<integer-literal>", "<float-literal>", "<bool-literal>", "or", "and"
    private byte scan_identifier() {
        while (Character.isLetterOrDigit(current_char)) {
            take_it();
        }

        switch (current_spelling.toString()) {
            case "or":
                return Token.OR;
            case "and":
                return Token.AND;
            case "true":
            case "false":
                return Token.BOOL_LITERAL;
            default:
                return Token.IDENTIFIER;
        }
    }

    public boolean is_at_end() {
        return current_pos >= input.length();
    }

    public List<Token> scan() {
        List<Token> tokens = new ArrayList<Token>();
        while (!is_at_end()) {
            while (current_char == '!' || current_char == ' ' || current_char == '\t' || current_char == '\r' || current_char == '\n') {
                skip_separator();
            }
            current_spelling.setLength(0);
            current_kind = scan_token();
            tokens.add(new Token(current_kind, current_spelling.toString(), current_column, current_line));
        }
        tokens.add(new Token(Token.EOF, "", current_column + 1, current_line));
        return tokens;
    }
}
