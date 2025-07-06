/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;

/**
 * Boolean (Logical) Or Expressions.
 */

public class OrExpr extends BinOpExpr
{
    public OrExpr(Expr e1, Expr e2)
    {
		super(e1, e2);
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
