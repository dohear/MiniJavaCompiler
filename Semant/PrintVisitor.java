/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Semant;
import java.io.PrintWriter;
import java.util.ListIterator;

/**
 * Visitor prints AST in reparseable form.
 */

public class PrintVisitor implements Absyn.Visitor, Types.Visitor
{
    PrintWriter out;
    private int indentCount = 0;

    public PrintVisitor(PrintWriter out)
    {
		this.out = out;
    }

    public PrintVisitor()
    {
		this.out = new PrintWriter(System.out);
    }

    private void indent()
    {
		out.print('\n');
		for(int i = 0; i < indentCount; i++)
	    { out.print(' '); }
    }

    /** Visitor pattern dispatch. */
    //public void visit(Absyn ast) {}

    public void visit(Absyn.Program ast)
    {
		out.print("Program(");
		indentCount++;
		visit(ast.classes);
		indentCount--;
		out.println(")");
		out.flush();
    }

    public void visit(java.util.AbstractList list)
    {
		Absyn.Visitable v;
		if (null == list)
	    {
			indent();
			out.print("null");
			return;
	    }
		indent();
		out.print("AbstractList(");
		indentCount++;
		for (Object o : list)
	    {
			if (null == o)
 		    {   indent();   out.print("null");   }
			else
		    {   ((Absyn.Visitable)o).accept(this);    }
	    }
		out.print(")");
		indentCount--;
    }

    public void visit(Absyn.ClassDecl ast)
    {
		indent();
		out.print("ClassDecl(");
		indentCount++;
		out.print(ast.name + " " + ast.parent); 
		visit(ast.fields);
		visit(ast.methods);
		visit(ast.checktype);
		indentCount--;
		out.print(")");
    }

    public void visit(Absyn.ThreadDecl ast)
    {
		indent();
		out.print("ThreadDecl(");
		indentCount++;
		out.print(ast.name + " " + ast.parent); 
		visit(ast.fields);
		visit(ast.methods);
		visit(ast.checktype);
		indentCount--;
		out.print(")");
    }

    public void visit(Absyn.MethodDecl ast)
    {
		indent();
		out.print("MethodDecl(");
		indentCount++;
		if (null != ast.returnType)
	    { ast.returnType.accept(this); }
		else
	    { out.print("public_static_void"); }
		if (ast.synced) { out.print(" synchronized"); }
		out.print(" " + ast.name);
		visit(ast.params);
        visit(ast.locals);
		visit(ast.stmts);
		ast.returnVal.accept(this);
		visit(ast.checktype);
		indentCount--;
		out.print(")");
    }
    
    public void visit(Absyn.VoidDecl ast)
    {
		indent();
		out.print("VoidDecl(");
		out.print(ast.name);
		indentCount++;
		visit(ast.locals);
		visit(ast.stmts);
//		ast.returnVal.accept(this);
		visit(ast.checktype);
		indentCount--;
		out.print(")");
    }
    
    public void visit(Absyn.Formal ast)
    {
		indent();
		out.print("Formal(");
		ast.type.accept(this);
		out.print(" " + ast.name + ")");
    }

    public void visit(Absyn.ArrayType ast)
    {
		out.print("ArrayType(");
		ast.base.accept(this);
		out.print(")");
    }

    public void visit(Absyn.IdentifierType ast)
    {
		out.print("IdentifierType(" + ast.id + ")");
    }
    public void visit(Absyn.BooleanType ast)
    {   out.print("BooleanType");   }

    public void visit(Absyn.IntegerType ast)
    {   out.print("IntegerType");   }

    public void visit(Absyn.VarDecl ast)
    {
		indent();
		out.print("VarDecl(");
		indentCount++;
		ast.type.accept(this);
		out.print(" " + ast.name);
		if (null == ast.init)
	    {   out.print(" null");   } 
		else
	    {   ast.init.accept(this);   }
		if (null == ast.type)
	    {   out.print(" null");   }
		else
	    {   out.print(" "); ast.checktype.accept(this);   }
		indentCount--;
		out.print(")");
    }

    private void visit(Absyn.BinOpExpr ast, String opType)
    {
		indent();
		out.print(opType + "(");
		indentCount++;
		ast.e1.accept(this);
		ast.e2.accept(this);
		indentCount--;
		out.print(")");
	
    }

    public void visit(Absyn.AddExpr ast)   { visit(ast, "AddExpr"); }
    public void visit(Absyn.AndExpr ast)   { visit(ast, "AndExpr"); }
    public void visit(Absyn.DivExpr ast)   { visit(ast, "DivExpr"); }
    public void visit(Absyn.EqualExpr ast) { visit(ast, "EqualExpr"); }
    public void visit(Absyn.GreaterExpr ast)   { visit(ast, "GreaterExpr"); }
    public void visit(Absyn.LesserExpr ast){ visit(ast, "LesserExpr"); }
    public void visit(Absyn.MulExpr ast)   { visit(ast, "MulExpr"); }
    public void visit(Absyn.NotEqExpr ast) { visit(ast, "NotEqExpr"); }
    public void visit(Absyn.OrExpr ast)    { visit(ast, "OrExpr"); }
    public void visit(Absyn.SubExpr ast)   { visit(ast, "SubExpr"); }

    public void visit(Absyn.IdentifierExpr ast)
    {
		indent();
		out.print("IdentifierExpr(" + ast.id + ")");
    }

    public void visit(Absyn.IntegerLiteral ast)
    {
		indent();
		out.print("IntegerLiteral(" + ast.value + ")");
    }

    public void visit(Absyn.StringLiteral ast)
    {
		indent();
		out.print("StringLiteral(" + ast.value + ")");
    }

    public void visit(Absyn.ArrayExpr ast)
    {
		indent();
		out.print("ArrayExpr(");
		indentCount++;
		ast.target.accept(this);
		ast.index.accept(this);
		indentCount--;
		out.print(")");
    }

    public void visit(Absyn.AssignStmt ast)
    {
		indent();
		out.print("AssignStmt(");
		indentCount++;
		ast.lhs.accept(this);
		ast.rhs.accept(this);
		indentCount--;
		out.print(")");
    }

    public void visit(Absyn.BlockStmt ast)
    {
		indent();
		out.print("BlockStmt(");
		indentCount++;
		visit(ast.stmts);
		indentCount--;
		out.print(")");
    }

    public void visit(Absyn.XinuCallStmt ast)
    {
		indent();
		out.print("XinuCallStmt(");
		indentCount++;
		out.print(ast.method);
		visit(ast.args);
		//indent();
		//out.print(ast.typeIndex);
		indentCount--;
		out.print(")");
    }

    public void visit(Absyn.XinuCallExpr ast)
    {
		indent();
		out.print("XinuCallExpr(");
		indentCount++;
		out.print(ast.method);
		visit(ast.args);
		//indent();
		//out.print(ast.typeIndex);
		indentCount--;
		out.print(")");
    }

    public void visit(Absyn.CallExpr ast)
    {
		indent();
		out.print("CallExpr(");
		indentCount++;
		ast.target.accept(this);
		indent(); out.print(ast.method);
		visit(ast.args);
		indent();
		out.print(ast.typeIndex);
		indentCount--;
		out.print(")");
    }

    public void visit(Absyn.FieldExpr ast)
    {
		indent();
		out.print("FieldExpr(");
		indentCount++;
		ast.target.accept(this);
		indent(); out.print(ast.field);
		indent(); out.print(ast.typeIndex + ")");
		indentCount--;
    }

    public void visit(Absyn.IfStmt ast)
    {
		indent();
		out.print("IfStmt(");
		indentCount++;
		ast.test.accept(this);
		ast.thenStm.accept(this);
		if (null != ast.elseStm)
	    {   ast.elseStm.accept(this);   }
		else
	    {   indent(); out.print("null");   }
		indentCount--;
		out.print(")");
    }

    public void visit(Absyn.NewArrayExpr ast)
    {
		indent();
		out.print("NewArrayExpr(");
		indentCount++;
		ast.type.accept(this);
		visit(ast.dimensions);
		indentCount--;
		out.print(")");	
    }

    public void visit(Absyn.NewObjectExpr ast)
    {
		indent();
		out.print("NewObjectExpr(");
		indentCount++;
		ast.type.accept(this);
		indentCount--;
		out.print(")");	
    }

    public void visit(Absyn.WhileStmt ast)
    {
		indent();
		out.print("WhileStmt(");
		indentCount++;
		ast.test.accept(this);
		ast.body.accept(this);
		indentCount--;
		out.print(")");	
    }

    public void visit(Absyn.NegExpr ast)
    {
		indent();
		out.print("NegExpr(");
		indentCount++;
		ast.e1.accept(this);
		indentCount--;
		out.print(")");
    }

    public void visit(Absyn.NotExpr ast)
    {
		indent();
		out.print("NotExpr(");
		indentCount++;
		ast.e1.accept(this);
		indentCount--;
		out.print(")");
    }

    public void visit(Absyn.FalseExpr ast) 
    {  indent(); out.print("FalseExpr");  }
    public void visit(Absyn.NullExpr ast)  
    {  indent(); out.print("NullExpr");   }
    public void visit(Absyn.ThisExpr ast)  
    {  indent(); out.print("ThisExpr");   }
    public void visit(Absyn.TrueExpr ast)  
    {  indent(); out.print("TrueExpr");   }

    public void visit(Types.Type t)
    {
		if (null == t) {   out.print("null");  return;   }
		if (t instanceof Types.ARRAY)    visit((Types.ARRAY)t);
		if (t instanceof Types.BOOLEAN)  visit((Types.BOOLEAN)t);
		if (t instanceof Types.CLASS)    visit((Types.CLASS)t);
		if (t instanceof Types.FIELD)    visit((Types.FIELD)t);
		if (t instanceof Types.FUNCTION) visit((Types.FUNCTION)t);
		if (t instanceof Types.INT)      visit((Types.INT)t);
		if (t instanceof Types.NIL)      visit((Types.NIL)t);
		if (t instanceof Types.OBJECT)   visit((Types.OBJECT)t);
		if (t instanceof Types.RECORD)   visit((Types.RECORD)t);
		if (t instanceof Types.VOID)     visit((Types.VOID)t);
    }

    public void visit(Types.ARRAY a) 
    {
		indent(); out.print("ARRAY(");
		indentCount++;
		visit(a.element);
		indentCount--;
		out.print(")");
    }

    public void visit(Types.BOOLEAN b)
    {   indent(); out.print("BOOLEAN");   }

    public void visit(Types.CLASS c) 
    {
		indent(); out.print("CLASS(" + c.name);
		indentCount++;
		indent(); out.print(c.parent);
		visit(c.methods);
		visit(c.fields);
		indent(); out.print("OBJECT(" + c.instance.myClass.name);
		indentCount++;
		visit(c.instance.methods);
		visit(c.instance.fields);
		indentCount--;
		out.print(")");		    


		indentCount--;
		out.print(")");
    }

    public void visit(Types.FIELD f)
    {
		indent(); out.print("FIELD(" + f.index + " " + f.name);
		indentCount++;
		visit(f.type);
		indentCount--;
		out.print(")");		    
    }

    public void visit(Types.FUNCTION f) 
    {
		indent(); out.print("FUNCTION(" + f.name);
		indentCount++;
		visit(f.self);
		visit(f.formals);
		visit(f.result);
		indentCount--;
		out.print(")");		    
    }

    public void visit(Types.INT i) 
    {   indent(); out.print("INT");   }
    
    public void visit(Types.STRING s) 
    {   indent(); out.print("STRING");   }
    
    public void visit(Types.NIL n)
    {   indent(); out.print("NIL");   }
    
    public void visit(Types.OBJECT o)
    {	indent(); out.print("OBJECT(" + o.myClass.name + ")");   }

    public void visit(Types.RECORD r)
    {
		indent(); out.print("RECORD(");
		indentCount++;
		for(java.util.Iterator iter = r.iterator();
			iter.hasNext(); )
	    {
			visit((Types.FIELD)iter.next());
	    }
		indentCount--;
		out.print(")");		    
    }

    public void visit(Types.VOID v) 
    {   indent(); out.print("VOID");   }
    
}