/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;

/**
 * If Statements.
 */

public class IfStmt extends Stmt
{
    public Expr test;
    public Stmt thenStm;
    public Stmt elseStm;
    public IfStmt(Expr test, Stmt thenStm, Stmt elseStm)
    {
		this.test = test;
		this.thenStm = thenStm;
		this.elseStm = elseStm;
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
