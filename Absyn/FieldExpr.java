/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;

/**
 * Field lookup on object reference.
 */

public class FieldExpr extends AssignableExpr
{
    public Expr target;
    public String field;
    public int typeIndex;
    public FieldExpr(Expr target, String field)
    {
		this.target = target;
		this.field  = field;
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
