/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;

/**
 * Boolean TRUE
 */

public class TrueExpr extends Expr
{
    public TrueExpr()
    {
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
