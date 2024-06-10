package cosmo;

import java.util.List;

public class Parser {
    private List<Token> tokens;
    private Token current_token;
    private int current_pos = 0;
    private int error_count = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        current_token = tokens.get(0);
    }

    private void report_error(Token token, String expected) {
        System.err.println(
            current_token.line + ":" + current_token.column + ": expected `" + expected + "`, got `" + Token.spellings[current_token.kind] + "`"
        );
        error_count++;
    }

    private void accept(byte expected) {
        if (current_token.kind == expected) {
            current_token = tokens.get(++current_pos);
        } else {
            report_error(current_token, Token.spellings[expected]);
        }
    }

    private void accept_it() {
        current_token = tokens.get(++current_pos);
    }

    private void expect_identifier() {
        if (current_token.kind != Token.IDENTIFIER) {
            report_error(current_token, Token.spellings[Token.IDENTIFIER]);
        }
    }

    private void parse_program() {
        expect_identifier();
        if (current_token.spelling.equals("program")) {
            accept_it();
        } else {
            report_error(current_token, "program");
        }
        accept(Token.IDENTIFIER);
        accept(Token.SEMICOLON);
        parse_body();
        accept(Token.DOT);
    }

    private void parse_body() {
        parse_declaration();
        parse_compound_statement();
    }

    private void parse_declaration() {
        expect_identifier();
        if (current_token.spelling.equals("var")) {
            accept_it();
        } else {
            report_error(current_token, "var");
        }
        accept(Token.IDENTIFIER);
        accept(Token.COLON);
        parse_type();
    }

    private void parse_type() {
        expect_identifier();
        if (current_token.spelling.equals("integer")) {
            accept_it();
        } else if (current_token.spelling.equals("boolean")) {
            accept_it();
        } else {
            report_error(current_token, "integer or boolean");
        }
    }

    private void parse_compound_statement() {
        expect_identifier();
        if (current_token.spelling.equals("begin")) {
            accept_it();
        } else {
            report_error(current_token, "begin");
        }
        parse_statement_list();
        expect_identifier();
        if (current_token.spelling.equals("end")) {
            accept_it();
        } else {
            report_error(current_token, "end");
        }
    }

    // TODO:
    // <statement_list> ::= <statement>; | <statement_list> <statement>; | empty
    // ==
    // <statement_list> ::= (<statement>;)*
    private void parse_statement_list() {
    }

    public void parse() {
        parse_program();

        if (current_token.kind != Token.EOF) {
            report_error(current_token, Token.spellings[Token.EOF]);
        }

        if (error_count != 0) {
            System.exit(1);
        }
    }
}
