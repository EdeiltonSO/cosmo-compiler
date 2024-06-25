package cosmo;

import cosmo.AST.*;

import java.util.ArrayList;
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

    private String accept(byte expected) {
        String value = "";
        if (current_token.kind == expected) {
            value = current_token.spelling;
            current_token = tokens.get(++current_pos);
        } else {
            report_error(current_token, Token.spellings[expected]);
        }

        return value;
    }

    private String accept_it() {
        String value = current_token.spelling;
        current_token = tokens.get(++current_pos);

        return value;
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
    //talvez program não deva retornar nada, ou então o seu retorno só será utilizado em uma etapa futura
    private NodeProgram parse_program() {

        NodeProgram ASTProgram = new NodeProgram();

        expect_identifier();
        if (current_token.spelling.equals("program")) {
            accept_it();
        } else {
            report_error(current_token, "program");
        }
        ASTProgram.name = accept(Token.IDENTIFIER);
        accept(Token.SEMICOLON);

        ASTProgram.next = parse_body();
        accept(Token.DOT);

        return ASTProgram;


    }

    // <body> ::= (<declaration>;)* <compound-statement>
    //não estou gostando desse array list
    private NodeBody parse_body() {
        NodeBody ASTBody = new NodeBody();

        ArrayList<NodeDeclaration> dec = new ArrayList<NodeDeclaration>();

        while (current_token.kind == Token.IDENTIFIER && !current_token.spelling.equals("begin")) {
            dec.add(parse_declaration());
            accept(Token.SEMICOLON);
        }
        ASTBody.declarations = dec;

        ASTBody.next_compound_statement = parse_compound_statement();

        return ASTBody;
    }


    // <declaration> ::= var <id> : <type>
    private NodeDeclaration parse_declaration() {
        NodeDeclaration ASTDeclaration = new NodeDeclaration();
        expect_identifier();

        if (current_token.spelling.equals("var")) {
            accept_it();
        } else {
            report_error(current_token, "var");
        }
        ASTDeclaration.identifier =  accept(Token.IDENTIFIER);
        accept(Token.COLON);

        ASTDeclaration.type = parse_type();

        return ASTDeclaration;

    }

    // <type> ::= integer | boolean
    private String parse_type() {

        expect_identifier();
        if (current_token.spelling.equals("integer")) {
            return accept_it();
        } else if (current_token.spelling.equals("boolean")) {
            return accept_it();
        } else {
            report_error(current_token, "integer or boolean");
            return "";
        }
    }

    // <compound-statement> ::= begin <statement-list> end
    private NodeStatement parse_compound_statement() {
        NodeStatement ASTStatement;

        expect_identifier();
        if (current_token.spelling.equals("begin")) {
            accept_it();
        } else {
            report_error(current_token, "begin");
        }

        ASTStatement = parse_statement_list();
        expect_identifier();

        if (current_token.spelling.equals("end")) {
            accept_it();
        } else {
            report_error(current_token, "end");
        }

        return ASTStatement;
    }

    // <statement_list> ::= (<statement>;)*
  /*  private void parse_statement_list() {
        NodeStatement ASTStatement;

        while (current_token.kind == Token.IDENTIFIER) {
            if (current_token.spelling.equals("end"))
                break;

            parse_statement();
            accept(Token.SEMICOLON);
        }
    } */

    private NodeStatement parse_statement_list() {
        NodeStatement ASTStatement;

        if(current_token.kind == Token.IDENTIFIER && !current_token.spelling.equals("end")) {
            ASTStatement = parse_statement();
            accept(Token.SEMICOLON);
           // ASTStatement.next = parse_statement_list();

            if(current_token.spelling.equals("end")){
                ASTStatement.nextstatement = null;

            }else   //considerando que seja outro identificador e não um erro.
            {
                ASTStatement.nextstatement = parse_statement_list();
            }

            return ASTStatement;

        }

        //se não achar pelo menos um identificador, retorna null. No entanto, o que significa não achar um identificador?
        ASTStatement = null;
        return ASTStatement;


    }

    // <statement> ::= <assignment> | <conditional> | <iteration> | <compound-statement>
    private NodeStatement parse_statement() {
        NodeStatement ASTStatement;
        expect_identifier();
        if (current_token.spelling.equals("if")) {  //modificação aqui, (guardar identificadores?)
            ASTStatement = parse_conditional();   //add
            return ASTStatement;
        } else if (current_token.spelling.equals("while")) {
            ASTStatement = parse_iteration();
            return ASTStatement;
        } else if (current_token.spelling.equals("begin")) {
            ASTStatement = parse_compound_statement();
            return ASTStatement;
        } else {
            ASTStatement = parse_assignment();      //add
            return ASTStatement;
        }
    }

    // <conditional> ::= if <expression> then <statement> (else <statement> | ε)
    private NodeConditional parse_conditional() {

        NodeConditional ASTConditional = new NodeConditional();

        expect_identifier();
        if (current_token.spelling.equals("if")) {
            accept_it();
        } else {
            report_error(current_token, "if");
        }

        ASTConditional.conditional = parse_expression();

        expect_identifier();
        if (current_token.spelling.equals("then")) {
            accept_it();
        } else {
            report_error(current_token, "then");
        }
        ASTConditional.if_body = parse_statement();

        if (current_token.spelling.equals("else")) {
            accept_it();
            ASTConditional.else_body = parse_statement();
        }

        return ASTConditional;
    }

    // <expression> ::=
    // <factor> (<op-mul> <factor>)* (<op-ad> <factor> (<op-mul> <factor>)*)* (<op-rel>  <factor> (<op-mul> <factor>)* (<op-ad> <factor> (<op-mul> <factor>)*)*)*
    private NodeExpression parse_expression() {

        NodeExpression ASTExp ;

        ASTExp = parse_factor();

        if (current_token.kind == Token.RIGHT_PAREN){
            accept_it();
        }

        if (is_operator_mul()) {
            NodeExpressionOperator ASTExpOp = new NodeExpressionOperator();
            ASTExpOp.operator = accept_it();
            ASTExpOp.left = ASTExp;
            ASTExpOp.right = parse_expression();
            ASTExp = ASTExpOp;

        } else if(is_operator_ad()) {
            NodeExpressionOperator ASTExpOp = new NodeExpressionOperator();
            ASTExpOp.operator = accept_it();
            ASTExpOp.left = ASTExp;
            ASTExpOp.right = parse_expression();
            ASTExp = ASTExpOp;

        } else if(is_operator_rel()) {
            NodeExpressionOperator ASTExpOp = new NodeExpressionOperator();
            ASTExpOp.operator = accept_it();
            ASTExpOp.left = ASTExp;
            ASTExpOp.right = parse_expression();
            ASTExp = ASTExpOp;

        }

        return ASTExp;
    }

    // <factor> ::= <id> | <literal> | ( <expression> )
    private NodeExpression parse_factor() {

        NodeExpression ASTExp;

        if (current_token.kind == Token.LEFT_PAREN) {
            accept_it();
        }

        if (current_token.kind == Token.IDENTIFIER) {
            NodeOperandIdentifier ASTOperand = new NodeOperandIdentifier();
            ASTOperand.identifier = accept_it();
            ASTExp = ASTOperand;
            return ASTExp;

        } else if (current_token.kind == Token.INT_LITERAL){
            NodeOperandInt ASTOperand = new NodeOperandInt();
            ASTOperand.int_lit = accept_it();       //so funciona se o tipo for String
            ASTExp = ASTOperand;
            return ASTExp;

        }else if (current_token.kind == Token.BOOL_LITERAL) {
            NodeOperandBool ASTOperand = new NodeOperandBool();
            ASTOperand.Bool_lit = accept_it();      //so funciona se o tipo for String
            ASTExp = ASTOperand;
            return ASTExp;

        }else {
            report_error(current_token, "<id>, <literal> or (");
            return null;
        }


    }

    // <iteration> ::= while <expression> do <statement>
    private NodeIteration parse_iteration() {

        NodeIteration ASTIteration = new NodeIteration();

        expect_identifier();
        if (current_token.spelling.equals("while")) {
            accept_it();
        } else {
            report_error(current_token, "while");
        }
        ASTIteration.cond = parse_expression();

        expect_identifier();
        if (current_token.spelling.equals("do")) {
            accept_it();
        } else {
            report_error(current_token, "do");
        }
        ASTIteration.body = parse_statement();

        return ASTIteration;
    }

    // <assignment> := <id> := <expression>
    private NodeAssignment parse_assignment() {
        NodeAssignment ASTAssignment = new NodeAssignment();
        ASTAssignment.identifier = accept(Token.IDENTIFIER);
        accept(Token.ASSIGN);
        ASTAssignment.next = parse_expression();
        return ASTAssignment;
    }

    public NodeProgram parse() {
        NodeProgram ASTProgram = parse_program();


        if (current_token.kind != Token.EOF) {
            report_error(current_token, Token.spellings[Token.EOF]);
        }

        return ASTProgram;
    }
}
