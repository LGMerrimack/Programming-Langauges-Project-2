package ast.nodes;

import environment.Environment;

public class LetNode extends SyntaxNode {

    private Token identifier;
    private SyntaxNode valueExpr;
    private SyntaxNode bodyExpr;

    public LetNode(Token identifier, SyntaxNode valueExpr, SyntaxNode bodyExpr, int lineNumber) {
        super(lineNumber);
        this.identifier = identifier;
        this.valueExpr = valueExpr;
        this.bodyExpr = bodyExpr;
    }

    public Object evaluate(Environment env) {
        Object value = valueExpr.evaluate(env);
        Environment localEnv = env.copy();

        localEnv.updateEnvironment(identifier, getValue(), value);

        return bodyExpr.evaluate(localEnv);
    }

    @Override
    public String toString() {
        return "(let " + identifier.getValue() + " = " + valueExpr + " in " + bodyExpr.toString() + ")";
    }

}
