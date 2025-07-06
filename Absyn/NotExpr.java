/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;

/**
 * Boolean (Logical) Not Expressions.
 */

public class NotExpr extends Expr
{
    public Expr e1;
    public NotExpr(Expr e1)
    {
		this.e1 = e1;
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
