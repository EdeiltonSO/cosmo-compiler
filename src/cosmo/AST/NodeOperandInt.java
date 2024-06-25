package cosmo.AST;
public class NodeOperandInt extends NodeExpression{

    public String int_lit;  //isso deveria ser String ou int?

    public void visit (Visitor v) {
        v.visitExpOperandInt(this);
    }
}
