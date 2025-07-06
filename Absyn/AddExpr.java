/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;

/**
 * Addition Expressions.
 */

public class AddExpr extends BinOpExpr
{
    public AddExpr(Expr e1, Expr e2)
    {
		super(e1, e2);
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }}
