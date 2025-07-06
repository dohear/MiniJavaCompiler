/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;

/**
 * Reference to an identifier.
 */

public class IdentifierExpr extends AssignableExpr
{
    public String id;
    public IdentifierExpr(String id)
    {
		this.id = id;
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
