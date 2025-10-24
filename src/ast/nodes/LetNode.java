package ast.nodes;

import ast.EvaluationException;
import environment.Environment;
import lexer.Token;

public class LetNode extends SyntaxNode {

    private Token id;
    private SyntaxNode valueExpr;
    private SyntaxNode bodyExpr;

    public LetNode(Token id, SyntaxNode valueExpr, SyntaxNode bodyExpr, int line) {
        super(line);
        this.id = id;
        this.valueExpr = valueExpr;
        this.bodyExpr = bodyExpr;
    }

    public Token getId() {
        return id;
    }

    public SyntaxNode getValueExpr() {
        return valueExpr;
    }

    public SyntaxNode getBodyExpr() {
        return bodyExpr;
    }

    public String toString() {
        return "LetNode(" + id.getValue() + ", " + valueExpr.toString() + ", " + bodyExpr.toString() + ")";
    }

    public void displaySubtree(int indent) {
        String pad = " ".repeat(indent);

        System.out.println(pad + "LetNode: " + id.getValue());

        System.out.println(pad + " Value Expression:");
        valueExpr.displaySubtree(indent + 4);

        System.out.println(pad + " Body Expression:");
        bodyExpr.displaySubtree(indent + 4);

    }

    @Override
    public Object evaluate(Environment env) throws EvaluationException {

        Object value = valueExpr.evaluate(env);

        Environment localEnv = env.copy();

        localEnv.updateEnvironment(id, value);

        return bodyExpr.evaluate(localEnv);
    }

}
