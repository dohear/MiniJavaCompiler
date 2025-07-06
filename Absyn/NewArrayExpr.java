/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;
import java.util.LinkedList;

/**
 * Array Allocation.
 */

public class NewArrayExpr extends Expr
{
    public Type type;
    public LinkedList<Expr> dimensions;
    public NewArrayExpr(Type type, LinkedList<Expr> dimensions)
    {
		this.type = type;
		this.dimensions = dimensions;
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
