package cosmo.AST;

public class NodeExpressionOperator extends NodeExpression{

    public String operator;

    public NodeExpression left, right;

    public void visit (Visitor v) {
        v.visitExpressionOperator(this);
    }
}
