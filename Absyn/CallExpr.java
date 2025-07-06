/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Absyn;
import java.util.LinkedList;
/**
 * Method Call.
 */

public class CallExpr extends Expr
{
    public Expr target;
    public String method;
    public LinkedList<Expr> args;
    public int typeIndex;
    public CallExpr(Expr target, String method, LinkedList<Expr> args)
    {
		this.target = target;
		this.method = method;
		this.args   = args;
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
