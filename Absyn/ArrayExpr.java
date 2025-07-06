/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;

/**
 * Expression indexing into an Array.
 */

public class ArrayExpr extends AssignableExpr
{
    public Expr target;
    public Expr index;
    public ArrayExpr(Expr target, Expr index)
    {
		this.target = target;
		this.index  = index;
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
