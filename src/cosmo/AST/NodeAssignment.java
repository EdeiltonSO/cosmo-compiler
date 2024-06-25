
package cosmo.AST;
public class NodeAssignment extends NodeStatement{

  public  String identifier;

  public  NodeExpression next;    //next expression

  public void visit (Visitor v) {
    v.visitAssignment(this);
  }

}
