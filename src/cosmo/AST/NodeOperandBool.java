package cosmo.AST;

public class NodeOperandBool extends NodeExpression {

    public String Bool_lit;     //String ou Byte?

    public void visit (Visitor v) {
        v.visitExpOperandBool(this);
    }
}
