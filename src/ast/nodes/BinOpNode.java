/*
 *   Copyright (C) 2022 -- 2025  Zachary A. Kissel
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ast.nodes;

import ast.EvaluationException;
import environment.Environment;
import lexer.TokenType;

/**
 * This node represents a binary operation.
 * 
 * @author Zach Kissel
 */
public final class BinOpNode extends SyntaxNode
{
    private TokenType op;
    private SyntaxNode leftTerm;
    private SyntaxNode rightTerm;

    /**
     * Constructs a new binary operation syntax node.
     * 
     * @param lterm the left operand.
     * @param op    the binary operation to perform.
     * @param rterm the right operand.
     * @param line  the line of code the node is associated with.
     */
    public BinOpNode(SyntaxNode lterm, TokenType op, SyntaxNode rterm,
            long line)
    {
        super(line);
        this.op = op;
        this.leftTerm = lterm;
        this.rightTerm = rterm;
    }

    /**
     * Display a AST inferencertree with the indentation specified.
     * 
     * @param indentAmt the amout of indentation to perform.
     */
    public void displaySubtree(int indentAmt)
    {
        printIndented("BinOp[" + op + "](", indentAmt);
        leftTerm.displaySubtree(indentAmt + 2);
        rightTerm.displaySubtree(indentAmt + 2);
        printIndented(")", indentAmt);
    }

    /**
     * Evaluate the node.
     * 
     * @param env the executional environment we should evaluate the node under.
     * @return the object representing the result of the evaluation.
     * @throws EvaluationException if the evaluation fails.
     */
    @Override
    public Object evaluate(Environment env) throws EvaluationException {

        Object leftVal = leftTerm.evaluate(env);
        Object rightVal = rightTerm.evaluate(env);

        switch(op){

            case ADD:
            case SUB:
            case MULT:
            case DIV:
            case MOD:
                if(!(leftVal instanceof Integer || leftVal instanceof Double || !(rightVal instanceof Integer || rightVal instanceof Double))){

                    logError("Arithmetic operation '" + op + "' requires numeric operands.");

                    throw new EvaluationException();

                }

                double lnum = (leftVal instanceof Integer) ? ((Integer)leftVal).doubleValue() : (Double)leftVal;
                double rnum = (rightVal instanceof Integer) ? ((Integer)rightVal).doubleValue() : (Double)rightVal;

                switch(op){

                    case ADD:
                        return (leftVal instanceof Integer && rightVal instanceof Integer) ? (int) (lnum + rnum) : (lnum + rnum);

                    case SUB:
                        return (leftVal instanceof Integer && rightVal instanceof Integer) ? (int) (lnum - rnum) : (lnum - rnum);

                    case MULT:
                    return (leftVal instanceof Integer && rightVal instanceof Integer) ? (int) (lnum * rnum) : (lnum * rnum);

                    case DIV: 
                        if(rnum == 0){

                            logError("Division by zero.");

                            throw new EvaluationException();

                        }

                        int li = (Integer) leftVal;
                        int ri = (Integer) rightVal;

                        if(ri == 0){

                            logError("Mod by zero.");

                            throw new EvaluationException();

                        }

                        return li % ri;

                    default:
                        break;

                        }

                        break;
                    
                    case AND:
                    case OR:
                        if(!(leftVal instanceof Boolean) || !(rightVal instanceof Boolean)){

                            logError("Boolean operatjon '" + op + "' requires Boolean operands.");

                            throw new EvaluationException();

                        }

                        boolean lb = (Boolean) leftVal;
                        boolean rb = (Boolean) rightVal;

                        return(op == TokenType.AND) ? (lb && rb) : (lb || rb);

                    default:
                        logError("Unsupported binary operator:" + op);
                        
                        throw new EvaluationException();

                }

        return null;

        }
