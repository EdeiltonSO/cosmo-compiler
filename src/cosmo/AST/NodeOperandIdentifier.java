package cosmo.AST;
public class NodeOperandIdentifier extends NodeExpression{

   public String identifier;

   public void visit (Visitor v) {
      v.visitExpOperandIdentifier(this);
   }
}
