/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;

/**
 * The "this" reference.
 */

public class ThisExpr extends Expr
{
    public ThisExpr()
    {
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
