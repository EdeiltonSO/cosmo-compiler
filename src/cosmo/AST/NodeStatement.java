package cosmo.AST;
public abstract class NodeStatement {

    public NodeStatement nextstatement;

    public abstract void visit (Visitor v);
}
