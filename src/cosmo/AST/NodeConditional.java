package cosmo.AST;
public class NodeConditional extends NodeStatement{

    public NodeExpression conditional; //expressão a ser verificada

    public NodeStatement if_body; //comando a ser executado caso a expressão seja verdadeira

    public NodeStatement else_body; //comando a ser executado caso a expressão seja falsa

    public void visit (Visitor v) {
        v.visitConditional(this);
    }
}
