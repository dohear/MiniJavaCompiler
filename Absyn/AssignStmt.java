/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;

/**
 * Assignment Statements.
 */

public class AssignStmt extends Stmt
{
    public AssignableExpr lhs;
    public Expr rhs;
    public AssignStmt(AssignableExpr lhs, Expr rhs)
    {
		this.lhs = lhs;
		this.rhs = rhs;
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
