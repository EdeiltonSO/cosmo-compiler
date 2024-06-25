package cosmo.AST;



public class Printer implements Visitor{


    //<program> ::= program <id> ; <body>
    public void visitProgram(NodeProgram P) {

        if(P != null) {
            System.out.println(P.name);
            if(P.next != null) P.next.visit(this);
        }

    }

    public void visitAssignment(NodeAssignment A) {

        if(A != null){
            System.out.print(A.identifier + " := ");
            if(A.next != null) {
                A.next.visit(this);
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

                if(B.declarations != null) {
                    for ( NodeDeclaration dec  : B.declarations) {
                        dec.visit(this);
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

            System.out.print("if ");
            if(C.conditional != null) {
                C.conditional.visit(this);
                System.out.println(" then ");
            }
            if(C.if_body != null) {
                C.if_body.visit(this);
            }
            if(C.else_body != null) {
                System.out.println("else ");
                C.else_body.visit(this);
            }
            if(C.nextstatement != null) {
                C.nextstatement.visit(this);
                System.out.println("");
            }


        }

    }

    // <declaration> ::= var <id> : <type>
    public void visitDeclaration(NodeDeclaration D) {

        if(D != null){
            System.out.println("var " + D.identifier + " : " + D.type);
        }

    }

    public void visitExpressionOperator(NodeExpressionOperator Eo) {

        if(Eo != null){

            Eo.left.visit(this);
            System.out.print(" " + Eo.operator + " ");
            Eo.right.visit(this);

        }
    }

    public void visitIteration(NodeIteration It) {

        if(It != null){

            System.out.print("while ( ");
            if(It != null) {
                It.cond.visit(this);
                System.out.println(" ) do ");
            }

            if(It != null) {
                It.body.visit(this);
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
            System.out.print(Obool.Bool_lit);
        }

    }

    public void visitExpOperandIdentifier(NodeOperandIdentifier Oid) {

        if(Oid != null){

            System.out.print(Oid.identifier);
        }

    }

    public void visitExpOperandInt(NodeOperandInt Oint) {

        if(Oint != null) {

            System.out.print(Oint.int_lit);
        }

    }


    public void print (NodeProgram p)
    {
        System.out.println("---> Iniciando a impressao da arvore");
        p.visit (this);
    }
}
