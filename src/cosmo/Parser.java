package cosmo;

import java.util.List;

public class Parser {
    private List<Token> tokens;
    private Token current_token;
    private int current_pos = 0;
    public int error_count = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        current_token = tokens.get(0);
    }

    public int get_error_count() {
        return error_count;
    }

    private void report_error(Token token, String expected) {
        System.err.println(
            current_token.line + ":" + current_token.column + ": error: expected `" + expected + "`, got `" + current_token.spelling + "`"
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

    // <op-ad> ::= + | - | or
    private boolean is_operator_ad() {
        if (current_token.kind == Token.PLUS || current_token.kind == Token.MINUS || current_token.kind == Token.OR) {
            return true;
        }

        return false;
    }

    // <op-mul> ::= * | / | and
    private boolean is_operator_mul() {
        if (current_token.kind == Token.TIMES || current_token.kind == Token.DIVIDE || current_token.kind == Token.AND) {
            return true;
        }

        return false;
    }

    // <op-rel> ::= < | > | =
    private boolean is_operator_rel() {
        if (current_token.kind == Token.LESS || current_token.kind == Token.GREATER || current_token.kind == Token.EQUALS) {
            return true;
        }

        return false;
    }

    // <program> ::= program <id> ; <body> .
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

    // <body> ::= (<declaration>;)* <compound-statement>
    private void parse_body() {
        while (current_token.kind == Token.IDENTIFIER && !current_token.spelling.equals("begin")) {
            parse_declaration();
            accept(Token.SEMICOLON);
        }

        parse_compound_statement();
    }

    // <declaration> ::= var <id> : <type>
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

    // <type> ::= integer | boolean
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

    // <compound-statement> ::= begin <statement-list> end
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

    // <statement_list> ::= (<statement>;)*
    private void parse_statement_list() {
        while (current_token.kind == Token.IDENTIFIER) {
            if (current_token.spelling.equals("end"))
                break;

            parse_statement();
            accept(Token.SEMICOLON);
        }
    }

    // <statement> ::= <assignment> | <conditional> | <iteration> | <compound-statement>
    private void parse_statement() {
        expect_identifier();
        if (current_token.spelling.equals("if")) {
            parse_conditional();
        } else if (current_token.spelling.equals("while")) {
            parse_iteration();
        } else if (current_token.spelling.equals("begin")) {
            parse_compound_statement();
        } else {
            parse_assignment();
        }
    }

    // <conditional> ::= if <expression> then <statement> (else <statement> | ε)
    private void parse_conditional() {
        expect_identifier();
        if (current_token.spelling.equals("if")) {
            accept_it();
        } else {
            report_error(current_token, "if");
        }
        parse_expression();

        expect_identifier();
        if (current_token.spelling.equals("then")) {
            accept_it();
        } else {
            report_error(current_token, "then");
        }
        parse_statement();

        if (current_token.spelling.equals("else")) {
            accept_it();
            parse_statement();
        }
    }

    // <expression> ::=
    // <factor> (<op-mul> <factor>)* (<op-ad> <factor> (<op-mul> <factor>)*)* (<op-rel>  <factor> (<op-mul> <factor>)* (<op-ad> <factor> (<op-mul> <factor>)*)*)*
    private void parse_expression() {
        parse_factor();

        while (is_operator_mul()) {
            accept_it();
            parse_factor();
        }

        while (is_operator_ad()) {
            accept_it();
            parse_factor();
            while (is_operator_mul()) {
                accept_it();
                parse_factor();
            }
        }

        while (is_operator_rel()) {
            accept_it();
            parse_factor();

            while (is_operator_mul()) {
                accept_it();
                parse_factor();
            }

            while (is_operator_ad()) {
                accept_it();
                parse_factor();
                while (is_operator_mul()) {
                    accept_it();
                    parse_factor();
                }
            }
        }
    }

    // <factor> ::= <id> | <literal> | ( <expression> )
    private void parse_factor() {
        if (current_token.kind == Token.IDENTIFIER) {
            accept_it();
        } else if (current_token.kind == Token.INT_LITERAL || current_token.kind == Token.BOOL_LITERAL) {
            accept_it();
        } else if (current_token.kind == Token.LEFT_PAREN) {
            accept_it();
            parse_expression();
            accept(Token.RIGHT_PAREN);
        } else {
            report_error(current_token, "<id>, <literal> or (");
        }
    }

    // <iteration> ::= while <expression> do <statement>
    private void parse_iteration() {
        expect_identifier();
        if (current_token.spelling.equals("while")) {
            accept_it();
        } else {
            report_error(current_token, "while");
        }
        parse_expression();

        expect_identifier();
        if (current_token.spelling.equals("do")) {
            accept_it();
        } else {
            report_error(current_token, "do");
        }
        parse_statement();
    }

    // <assignment> := <id> := <expression>
    private void parse_assignment() {
        accept(Token.IDENTIFIER);
        accept(Token.ASSIGN);
        parse_expression();
    }

    public void parse() {
        parse_program();

        if (current_token.kind != Token.EOF) {
            report_error(current_token, Token.spellings[Token.EOF]);
        }
    }
}
