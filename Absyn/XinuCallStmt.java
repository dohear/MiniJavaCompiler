/* Copyright (C) 2013, Marquette University.  All rights reserved. */
package Absyn;
import java.util.LinkedList;

/**
 * Xinu Statements.
 */

public class XinuCallStmt extends Stmt
{
    public String method;
    public LinkedList<Expr> args;
	  public int typeIndex;
    public XinuCallStmt(String method, LinkedList<Expr> args)
    {
		this.method = method;
		this.args   = args;
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
