/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;

/**
 * Unary Negation Expressions.
 */

public class NegExpr extends Expr
{
    public Expr e1;
    public NegExpr(Expr e1)
    {
		this.e1 = e1;
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
