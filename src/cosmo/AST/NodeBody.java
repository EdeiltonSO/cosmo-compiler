package cosmo.AST;

import java.util.ArrayList;

public class NodeBody {

   public ArrayList<NodeDeclaration> declarations;
   public NodeStatement  next_compound_statement;

   public void visit (Visitor v) {
      v.visitBody(this);
   }


}
