package cosmo.AST;

public class NodeDeclaration {
   public String identifier;

   public String type;

   public void visit (Visitor v) {
      v.visitDeclaration(this);
   }
}
