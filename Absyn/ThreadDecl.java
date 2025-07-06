/* Copyright (C) 2013, Marquette University.  All rights reserved. */
package Absyn;
import java.util.LinkedList;

/**
 * Thread Class Declaration Blocks
 */

public class ThreadDecl extends ClassDecl
{
    public ThreadDecl(String name, 
					  LinkedList<VarDecl> fields, 
					  LinkedList<MethodDecl> methods)
    {
		super(name, "Thread", fields, methods);
    }

    /** Visitor pattern dispatch. */
    public void accept(Visitor v) {v.visit(this); }
    public Types.Type accept(TypeVisitor v) { return v.visit(this); }
}
