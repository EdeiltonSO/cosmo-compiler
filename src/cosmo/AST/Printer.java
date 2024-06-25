package cosmo.AST;



public class Printer implements Visitor{


    //<program> ::= program <id> ; <body>
    public void visitProgram(NodeProgram P) {

        if(P != null) {
            System.out.println(P.name); //imprime o nome do programa
            if(P.next != null) P.next.visit(this);
        }

    }

    public void visitAssignment(NodeAssignment A) {

        if(A != null){
            System.out.print(A.identifier + " := ");
            if(A.next != null) {    //essa verificação pode não ser necessária
                A.next.visit(this);     //acessa o metodo de visitação de expressão
                System.out.println("");
            }
            if(A.nextstatement != null) {
                A.nextstatement.visit(this);
                System.out.println("");
            }

        }

    }

    // <body> ::= (<declaration>;)* <compound-statement>
    public void visitBody(NodeBody B) {

            if(B != null){

                if(B.declarations != null) {    //tem que chamar visit(this) e fazer a impressão para cada ponteiro da lista de ponteiros "declarations"
                    for ( NodeDeclaration dec  : B.declarations) {
                        dec.visit(this); //errado pq essa porcaria é um array-list
                    }
                }

                if(B.next_compound_statement != null) {
                    B.next_compound_statement.visit(this);
                }

            }

    }

    // <conditional> ::= if <expression> then <statement> (else <statement> | ε)
    public void visitConditional(NodeConditional C) {

        if (C != null){

            //printar "if exp1 the c1 else c2" ou em outra formatação?
            System.out.print("if ");
            if(C.conditional != null) {
                C.conditional.visit(this);//expressão da condicional
                System.out.println(" then ");
            }
            if(C.if_body != null) {
                C.if_body.visit(this);  //comando realizado se a expressão for verdadeira
            }
            if(C.else_body != null) {
                System.out.println("else ");
                C.else_body.visit(this);//comando realizado se a expressão for falsa
            }
            if(C.nextstatement != null) {
                C.nextstatement.visit(this);
                System.out.println("");
            }


        }

    }

    // <declaration> ::= var <id> : <type>
    public void visitDeclaration(NodeDeclaration D) {

        //O nó de declaração não possui outros nós como atributo, portanto, basta printar seus atributos Strings
        if(D != null){
            System.out.println("var " + D.identifier + " : " + D.type);
        }

    }

    public void visitExpressionOperator(NodeExpressionOperator Eo) {

        //nenhum tratamento está sendo feito a respeito de expressões com parenteses.
        if(Eo != null){

            Eo.left.visit(this);    //expressão à esquerda
            System.out.print(" " + Eo.operator + " ");    //operador
            Eo.right.visit(this);   //expressão à direita

        }
        //System.out.println("");
    }

    public void visitIteration(NodeIteration It) {

        if(It != null){

            System.out.print("while ( ");
            if(It != null) {
                It.cond.visit(this);
                System.out.println(" ) do ");   //expressão a ser avaliada
            }

            if(It != null) {
                It.body.visit(this);        //comando a ser executado
            }
            if(It.nextstatement != null) {
                It.nextstatement.visit(this);
                System.out.println("");
            }

        }

    }

    public void visitExpOperandBool(NodeOperandBool Obool) {

        if(Obool != null)
        {
            System.out.print(Obool.Bool_lit); //String que representa o booleano literal
        }
        //System.out.println("");

    }

    public void visitExpOperandIdentifier(NodeOperandIdentifier Oid) {

        if(Oid != null){

            System.out.print(Oid.identifier); //String que representa um identificador(nome) de variável
        }
        //System.out.println("");

    }

    public void visitExpOperandInt(NodeOperandInt Oint) {

        if(Oint != null) {

            System.out.print(Oint.int_lit);   //String que representa um inteiro literal
        }
        //System.out.println("");

    }


    public void print (NodeProgram p)
    {
        System.out.println("---> Iniciando a impressao da arvore");
        p.visit (this);
    }
}
