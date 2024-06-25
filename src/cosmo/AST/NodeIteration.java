package cosmo.AST;
public class NodeIteration extends NodeStatement {

    public NodeExpression cond;

    public NodeStatement body;

    public void visit (Visitor v) {
        v.visitIteration(this);
    }
}


