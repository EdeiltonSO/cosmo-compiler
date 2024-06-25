package cosmo.AST;

public interface Visitor {

    public void visitAssignment (NodeAssignment A);
    public void visitBody (NodeBody B);
    public void visitConditional (NodeConditional C);
    public void visitDeclaration (NodeDeclaration D);
    public void visitExpressionOperator (NodeExpressionOperator Eo);
    public void visitIteration (NodeIteration It);
    public void visitExpOperandBool (NodeOperandBool Obool);
    public void visitExpOperandIdentifier (NodeOperandIdentifier Oid);
    public void visitExpOperandInt (NodeOperandInt Oint);
    public void visitProgram (NodeProgram P);
}
