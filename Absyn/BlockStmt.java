/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;
import java.util.LinkedList;

/**
 * Curly-brace delimited block of statements.
 */

public class BlockStmt extends Stmt
{
    public LinkedList<Stmt> stmts;
    public BlockStmt(LinkedList<Stmt> stmts)
    {
		this.stmts = stmts;
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
