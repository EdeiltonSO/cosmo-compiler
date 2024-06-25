package cosmo.AST;
public class NodeProgram {
    public String name;
    public NodeBody next;

    public void visit (Visitor v) {
        v.visitProgram(this);
    }

}
