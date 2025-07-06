/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;
import java.util.LinkedList;

/**
 * Run (Void) Method Declaration structure.
 */

public class VoidDecl extends MethodDecl
{
    public VoidDecl(String name,
				   LinkedList<VarDecl> locals,
				   LinkedList<Stmt> stmts)
    {
		super(null, false, name, null,
			  locals, stmts, null);
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
