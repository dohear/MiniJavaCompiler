/**
 * COSC 4400 - Project 5
 * Explain briefly the functionality of the program.
 * @authors [Daniel O'Hear, Erik Gutierrez, Varisha Asim]
 * Instructor [Jack Forden]
 * TA-BOT:MAILTO [daniel.ohear@marquette.edu, erik.gutierrez@marquette.edu, varisha.asim@marquette.edu]
 */

/* Copyright (C) 1997-2003, Purdue Research Foundation of Purdue University.
 * All rights reserved.  */
/* Copyright (C) 2007, Marquette University.  All rights reserved. */
package Semant;

import Types.Type;
import Types.ARRAY;
import Types.BOOLEAN;
import Types.CLASS;
import Types.FIELD;
import Types.FUNCTION;
import Types.INT;
import Types.NIL;
import Types.OBJECT;
import Types.RECORD;
import Types.STRING;
import Types.VOID;

import java.lang.reflect.Method;
import java.sql.Types;
import java.util.Iterator;

import Absyn.MethodDecl;

/**
 * Interface for Type Visitor Pattern traversals.
 */

public class TypeChecker implements Absyn.TypeVisitor
{
    Symbol.Table<CLASS>        classEnv = new Symbol.Table<CLASS>();
	//given as type Types.Type
    Symbol.Table<Type>   varEnv   = new Symbol.Table<Type>();

    public static final BOOLEAN BOOLEAN = new BOOLEAN();
    public static final INT     INT     = new INT();
    public static final NIL     NIL     = new NIL();
    public static final VOID    VOID    = new VOID();
    public static final STRING  STRING  = new STRING();

    private CLASS currentClass;
    private FUNCTION currentMethod;

    public int errors = 0;
    private void error(Absyn.Absyn ast, String msg)
    {
        errors++;
		if (null == ast)
			System.err.println("ERROR " + msg + ": line not available");
		else
	    {
			System.err.print("ERROR " + msg + ": ");
			java.io.PrintWriter pw = new java.io.PrintWriter(System.err);
			Absyn.PrintVisitor pv = new Absyn.PrintVisitor(pw);
			pv.indentCount = 10;
			ast.accept(pv);
			pw.flush();
			System.err.println();
	    }
    }

    private void checkType(Type found, Type required, Absyn.Absyn ast)
    {
        if (!found.coerceTo(required))
            error(ast, "incompatible types: "
                  + required + " required, but "
                  + found + " found");
    }


	private void makeXinuType()
	{
		RECORD xinuFormals = new RECORD();
		CLASS xinuClass = new CLASS("Xinu");
		classEnv.put("Xinu", xinuClass);
		currentClass = xinuClass;

		java.util.LinkedList<Absyn.MethodDecl> xinuMethods
			= new java.util.LinkedList<Absyn.MethodDecl>();

		java.util.LinkedList<Absyn.Formal> params = new
			java.util.LinkedList<Absyn.Formal>();
		params.add(new Absyn.Formal(
					   new Absyn.IdentifierType("String"), "arg"));

		xinuMethods.add(new Absyn.MethodDecl(null, false, "print",
											 params, null, null, null));

		params = new java.util.LinkedList<Absyn.Formal>();
		params.add(new Absyn.Formal(
					   new Absyn.IntegerType(), "arg"));
		xinuMethods.add(new Absyn.MethodDecl(null, false, "printint",
											 params, null, null, null));
		xinuMethods.add(new Absyn.MethodDecl(null, false, "sleep",
											 params, null, null, null));

		params = new java.util.LinkedList<Absyn.Formal>();
		xinuMethods.add(new Absyn.MethodDecl(new Absyn.IntegerType(), 
											 false, "readint", params,
											 null, null, null));

		xinuMethods.add(new Absyn.MethodDecl(null, false, "println", params,
											 null, null, null));

		xinuMethods.add(new Absyn.MethodDecl(null, false, "yield", params,
											 null, null, null));

		params = new java.util.LinkedList<Absyn.Formal>();
		params.add(new Absyn.Formal(
					   new Absyn.IdentifierType("Thread"), "arg"));
		xinuMethods.add(new Absyn.MethodDecl(null, false, "threadCreate",
											 params, null, null, null));

		for (Absyn.MethodDecl md : xinuMethods)
		{
			visit(md);
			}
		for (FIELD m : xinuClass.methods)
		{
			xinuClass.instance.methods.put(m.type, m.name);
			}
	}

    public Type visit(Absyn.Program ast)
    {
		RECORD program = new RECORD();
		Symbol.Table<Type> localEnv = new Symbol.Table<Type>();
		classEnv.beginScope();

		classEnv.put("String", new CLASS("String"));
		classEnv.put("Thread", new CLASS("Thread"));
		makeXinuType();

		for (Absyn.ClassDecl c : ast.classes) {
			/*
			* Pass 1 - Register classes and detect duplicates
			* - Add all classes to the symbol table to track them.
			* - Create incomplete class types for later use.
			* - Check for duplicate class definitions and raise errors.
			*/
			currentClass = (CLASS)c.accept(this);
			if (classEnv.get(c.name) != null){
				error(c, "class " + c.name + " already exists!");
			}
			classEnv.put(c.name, currentClass);
			program.put(currentClass.instance, c.name);
		}
	
		for (Absyn.ClassDecl c : ast.classes) {
			/*
			* Pass 2 - Set up inheritance and define class members
			* - Link child classes to their parent classes (if any).
			* - Add fields and methods to each class.
			* - Ensure parent classes exist and raise errors if not found.
			*/
			currentClass = classEnv.get(c.name);
			if (c.parent != null){
				if (classEnv.get(c.parent) == null) {
					error(ast, "PARENT NOT FOUND");
					break;
				}
				CLASS parentClass = classEnv.get(c.name);
				currentClass.parent = parentClass;
			}
			RECORD currmethods = new RECORD();
			for(Absyn.MethodDecl m : c.methods ){
				Type methodType = m.accept(this);
			}
			RECORD currfields = new RECORD();
			for(Absyn.VarDecl cf : c.fields){
				Type fieldType = visitFields(cf);
				currfields.put(fieldType, cf.name);
			}
		}

        if (errors > 0) return program;

		for (Absyn.ClassDecl c : ast.classes) {
			/*
		* Pass 3 - Detect inheritance cycles and merge class members
		* - Check for cyclic inheritance and raise errors if found.
		* - Merge inherited fields and methods from parent classes.
		* - Ensure overridden methods match the parent types.
		*/
			currentClass = classEnv.get(c.name);
			CLASS parentClass = new CLASS(c.parent);
			if (c.parent == null) {
				continue;
			}
			if((classEnv.get((c.parent))).parent.name == c.name){
				error(ast, "you have cyclic inherritance, donut");
				break;
			}

			for (FIELD parentField : parentClass.fields) {
				FIELD overwritenField = currentClass.fields.get(parentField.name);
				if (overwritenField != null) {
					if (!overwritenField.type.equals(parentField.type)) {
						error(c, "Field " + overwritenField.name +  " has incompatible type with parent class field");
						break;
					}
				} else {
					currentClass.fields.put(parentField.type, parentField.name);
				}
			}

			for (FIELD parentMethod : parentClass.methods) {
				FIELD overwritenMethod = currentClass.methods.get(parentMethod.name);
				if (overwritenMethod != null) {
					if (!parentMethod.type.equals(overwritenMethod.type)) {
						error(c, "Method " + parentMethod.name + " has incompatible return type with parent class method");
						break;
					}
				} else {
					currentClass.instance.methods.put(parentMethod.type, parentMethod.name);
				}
			}
			mergeParents(parentClass, currentClass.instance);
		}
		
        if (errors > 0) return program;

			/*
			* Pass 4 - Check method bodies for correctness
			* - Manage scopes for fields, parameters, and local variables.
			* - Verify return types match method declarations (except for "main").
			* - Detect and raise errors for duplicate variables within the same scope.
			*/
		for (Absyn.ClassDecl c : ast.classes)
	    {
			currentClass = classEnv.get(c.name);
			classEnv.beginScope();

			for(Absyn.MethodDecl m : c.methods)
			{
				// Get the current method and its declared type
				Type returnType = m.checktype.result;
				if (returnType == null) {
					error(m, "Method " + m.name + " has an invalid declaration");
				}

				varEnv.beginScope();

				// Check Parameters:
				for(Absyn.Formal p : m.params){
					Type paramType = p.checktype;
					// Add Parameter.
					varEnv.put(p.name, paramType);
				}

				//Check Statements:
				for (Absyn.VarDecl locals : m.locals){
					// TODO: here lies the issue
					// abandon all hope, yee who enter here
					visitFields(locals);
					//locals.accept(this);
				}

				for(Absyn.Stmt stmt : m.stmts)
				{
					stmt.accept(this);
				}
				// Check Special Case (main):
				if (!m.name.equals("main"))
				{
					// For all methods except "main", validate return type
					Type expectedReturn = currentMethod.result;
					Type actualReturn = m.returnVal.accept(this);
					if (actualReturn != expectedReturn)
					{
						error(m, "Return type of method " + m.name + " does not match its declaration. Expected: " + expectedReturn + ", Found: " + actualReturn);
					}
				}
				else
				{
					// Special case for "main": Ensure no return type
					if (m.checktype.result != VOID)
					{
						error(m, "Main method must have a return type of VOID");
					}
				}
				varEnv.endScope();
			}
			classEnv.endScope();
			}
			return program;
	}
    public Type visit(Absyn.ClassDecl ast)
    {
		// This method processes class declarations by associating the class name with a
		// new `CLASS` type object. The class type is then registered in the `classEnv`
		// to make it accessible throughout the program. Ensure that every class declaration
		// is correctly typed and added to the environment without conflicts.
		// Step 1: Check if the class name already exists in the environment
		if (classEnv.get(ast.name) != null) {
			// Report an error if there's a naming conflict
			error(ast, "class already exists!");
		}
		// Step 2: Create a new CLASS type object for the class
		CLASS currClass = new CLASS(ast.name);
		// Step 4: Return the type of the class declaration (could be `null` if not needed)
		ast.checktype = currClass;
		return currClass;
	}

    public Type visit(Absyn.ThreadDecl ast) 
    {
		// Similar to class declarations, this method handles thread declarations by assigning 
		// them a `CLASS` type. The thread's type is also stored in the `classEnv`. Ensure that 
		// the thread is treated like a specialized class, and conflicts with other declarations 
		// are avoided. 

		// Retrieve the thread's name
		String threadName = ast.name;

		// Check if the thread name is already declared in the class environment
		if (classEnv.get(threadName) != null) {
			error(ast, "Thread " + threadName + " is already defined");
		}

		// Create a new CLASS type to represent the thread
		CLASS threadClass = new CLASS(threadName);

		// Add fields and methods to the thread class
		// Process the list of fields
		for (Absyn.VarDecl field : ast.fields) {
			Type fieldType = field.type.accept(this); // Type-check the field's type
			if (fieldType != null) {
				threadClass.fields.put(fieldType, field.name);
			}
		}

		// Process the list of methods
		for (Absyn.MethodDecl method : ast.methods) {
			FUNCTION methodType = (FUNCTION) method.accept(this); // Type-check the method
			if (methodType != null) {
				threadClass.methods.put(methodType, method.name);
			}
		}

		// Add the thread class to the class environment
		classEnv.put(threadName, threadClass);

		// Return the new CLASS type
		return threadClass;
			
    }

    public Type visit(Absyn.MethodDecl ast)
    {
	// This method handles method declarations by assigning types to return values and
	// parameters, and adding them to the current class's method environment. A `FUNCTION`
	// object is created to represent the method.
	//

		// check if method already exists
		if (currentClass.methods.get(ast.name) != null) {
			error(ast, "Method " + ast.name + " is already defined in class " + currentClass.name);
		}
		Type returnType;

		if(ast.returnType != null){
			returnType = ast.returnType.accept(this);
		}
		else{
			returnType = VOID;
		}

		// Create a Record for storing the param types
		RECORD paramTypes = new RECORD();


		FUNCTION method = new FUNCTION(ast.name, currentClass.instance, paramTypes, returnType);

		//set method decl.checktype (likely returntype) ??
		ast.checktype = method;

		// Add the method to the current class's method environment
		currentClass.methods.put(method, ast.name);

		// For each param, get its type and stores in the record
		for (Absyn.Formal param : ast.params) {
			Type paramType = visit(param);
			paramTypes.put(paramType, param.name); // Add to RECORD
		}
		// Add params to the var environment
		for (Absyn.Formal param : ast.params) {
			Type paramType = visit(param);
			varEnv.put(param.name, paramType);
		}

		// Return the method
		return method;
	}
	

    public Type visit(Absyn.VoidDecl ast) 
{
    // Check if the method is already defined in the current class
    String methodName = ast.name;

    if (currentClass.methods.get(methodName) != null) {
        error(ast, "Method " + methodName + " is already defined in class " + currentClass.name);
    }

    // Create a FUNCTION object to represent the method\
	RECORD paramTypes = new RECORD();
	FUNCTION method = new FUNCTION(methodName, currentClass.instance, paramTypes, VOID);
	
	//set method decl.checktype (likely returntype) ??
	ast.checktype = method;
	currentClass.methods.put(method, methodName);
	
    // Process parameters
	// For each param, get its type and stores in the record
	for (Absyn.Formal param : ast.params) {
			Type paramType = param.type.accept(this);
			if (method.formals.get(param.name) != null){
				error(param, "Duplicate parameter " + param.name + " in method " + methodName);
				continue;
			}
			paramTypes.put(paramType, param.name); // Add to RECORD
		}

	// Add params to the var environment
	for (Absyn.Formal param : ast.params) {
		Type paramType = param.type.accept(this);
		if ( varEnv.get(param.name) != null){
			error(ast, "repeat param: " + param.name);
		}
			varEnv.put(param.name, paramType);
		}
    // Return the method
    return method;
}

    public Type visitFields(Absyn.VarDecl ast)
    {
		/*
		* Handle field declarations in classes
		* - Determine the field's type using the `accept` method.
		* - Assign the type to the field's `checktype`.
		* - Register the field in the class environment, raising an error for name conflicts.
		*/
		// Get field type
		Type fieldType = ast.type.accept(this);

		// checking if the field type is valid
		if (fieldType == null) {
			error(ast, "Field " + ast.name + " has an invalid type");
		}

		// assigning type to field's `checktype`
		ast.checktype = fieldType;

		// check if name conflicts with previously defined fields
		if (currentClass.fields.get(ast.name) != null) {
			error(ast, "Field " + ast.name + " is already defined in class " + currentClass.name);
		}

		// Register the field in the current class
		currentClass.fields.put(fieldType, ast.name);
		//ast.checktype = fieldType;
		// Return the field's type
		return fieldType;
    }

    public Type visit(Absyn.VarDecl ast)
    {
		Type varType = ast.type.accept(this);
		ast.checktype = varType;
		varEnv.put(ast.name, varType);
		checkType(varType, ast.init.accept(this), ast);
		return varType;
    }
	
    private boolean isLoop(CLASS c)
    {
        String name = c.name;
        boolean any;
        c.name = null;
        if (null == name) any = true;
        else if (c.parent != null) any = isLoop(c.parent);
        else any = false;
        c.name = name;
        return any;
    }
    
    private void mergeParents(CLASS parent, OBJECT instance)
    {
		if (null == parent) return;
        mergeParents(parent.parent, instance);
		for (FIELD f : parent.fields)
	    {
			//instance.fields.put(f.type, f.name);
	    }
		for (FIELD m : parent.methods)
	    {
			FIELD old = instance.methods.get(m.name);
			//if (old == null) instance.methods.put(m.type, m.name);
			//else old.type = m.type;
	    }
    }
    
    public Type visit(java.util.AbstractList list)
    {
		for (Object o : list)
	    {
			((Absyn.Visitable)o).accept(this);
	    }
		return null;
    }

    /* The Statements */
    public Type visit(Absyn.AssignStmt ast){
		Type var = ast.lhs.accept(this);
		Type expr = ast.rhs.accept(this);
		checkType(var, expr, ast);
		return VOID;
    }
    public Type visit(Absyn.BlockStmt ast){
		for(Absyn.Stmt stmt : ast.stmts){
			stmt.accept(this);
		}
		return VOID;
    }
    public Type visit(Absyn.IfStmt ast)
    {
		/*
		* Handle `if` statements and type-check branches
		* - Ensure the condition evaluates to `BOOLEAN`
		* - Visit the "then" branch to confirm it is valid.
		* - If an "else" branch exists, visit and validate it.
		* - Return `VOID` since `if` statements do not produce a value.
		*/
		Type conditionType = ast.test.accept(this);
		if(!conditionType.equals(BOOLEAN)){
			error(ast, "not boolean");}
		if (ast.thenStm != null){
			ast.thenStm.accept(this);}
		if (ast.elseStm != null){
			ast.elseStm.accept(this);}
		return VOID;
    }

	public Type visit(Absyn.XinuCallStmt ast)
	{
		
		return new OBJECT(new CLASS(ast.method));
	}

    public Type visit(Absyn.XinuCallExpr ast)
	{
		return new OBJECT(new CLASS(ast.method));
	}

	public Type visit(Absyn.WhileStmt ast) 
	{ 
		Type conditionType = ast.test.accept(this);

		if (conditionType != BOOLEAN) {
			error(ast.test, "test type must be boolean");
		}

		Type bodyType = ast.body.accept(this);

		return bodyType;
	}

	private Type visit(Absyn.BinOpExpr e, String op, Type t, Type rt) 
	{ 
		Type left = e.e1.accept(this);
		Type right = e.e2.accept(this);
		checkType(left, t, e.e1);
		checkType(right, t, e.e2);
		return rt;
	}

	private Type visit(Absyn.BinOpExpr e, String op, Type t) 
	{   
		Type left = e.e1.accept(this);
		Type right = e.e2.accept(this);
		checkType(left, t, e.e1);
		checkType(right, t, e.e2);
		return t;
	}


    private Type visit (Absyn.BinOpExpr e, String op)
    {
      	Type left = e.e1.accept(this);
		Type right = e.e2.accept(this);
		
		if (!left.coerceTo(right) && !right.coerceTo(left)) {
			error(e, "incompatible operand for operator " + op);
			return null;
		}
		return BOOLEAN;
    }

    /* The Expressions */
    public Type visit(Absyn.AddExpr ast)  { return visit(ast, "+", INT); }
    public Type visit(Absyn.AndExpr ast)  { return visit(ast, "&&", BOOLEAN); }
    public Type visit(Absyn.DivExpr ast)  { return visit(ast, "/", INT); }
    public Type visit(Absyn.MulExpr ast)  { return visit(ast, "*", INT); }
    public Type visit(Absyn.SubExpr ast)  { return visit(ast, "-", INT); }
    public Type visit(Absyn.EqualExpr ast){ return visit(ast, "=="); }
    public Type visit(Absyn.GreaterExpr ast)
    { return visit(ast, ">", INT, BOOLEAN); }

    public Type visit(Absyn.LesserExpr ast)
    { return visit(ast, "<", INT, BOOLEAN); }

    public Type visit(Absyn.NotEqExpr ast) { return visit(ast, "!="); }
	public Type visit(Absyn.NotExpr ast) 
	{ 
		// This method processes logical negation expressions (`!`). 
		// It ensures that the operand is of type `BOOLEAN`.
		// If the operand's type is incompatible, an error is raised.
		// The result of this expression is always `BOOLEAN`.
		Type operandType = ast.accept(this);
        if(!operandType.equals(BOOLEAN)){
            error(ast,null);
        }
        return BOOLEAN;
    }


	public Type visit(Absyn.NegExpr ast) 
	{ 
		//
		// This method handles arithmetic negation expressions (`-`). 
		// It ensures that the operand is of type `INT`.
		// If the operand's type is not compatible, an error is raised.
		// The result of this expression is always `INT`.
		Type operandType = ast.accept(this);
        if(!operandType.equals(INT)){
            error(ast,null);
        }
        return INT;
	}

    
    public Type visit(Absyn.OrExpr ast) { 
		return visit(ast, "||", BOOLEAN); 
	}

    public Type visit(Absyn.ArrayExpr ast) 
    { 
		Type target = ast.target.accept(this);
		if(!(target instanceof ARRAY)){
			error(ast, "target not array type " + target);
			return VOID;
		}
		return target;

    }

    public Type visit(Absyn.CallExpr ast)
    {	
		Type target = ast.target.accept(this);
		if (!(target instanceof OBJECT))
	    {
			error(ast, "target not object, type "
				  + target);
			return VOID;
	    }
		FIELD meth  = ((OBJECT)target).methods.get(ast.method);
		if (null == meth)
	    {
			error(ast, "cannot resolve method " 
				  + ast.method);
			return VOID;
	    }
		FUNCTION methType = (FUNCTION)meth.type;
		if ((null != methType.self) && (!target.coerceTo(methType.self)))
	    {
			error(ast, "implicit self parameter not "
				  + "compatible" + methType.self + ", "
				  + target);
	    }
		Iterator formals = methType.formals.iterator();
		Iterator actuals = ast.args.iterator();
		// while (formals.hasNext() && actuals.hasNext())
	    // {
		// 	Type formal = (Type)formals.next();
		// 	Absyn.Expr actual = (Absyn.Expr)actuals.next();
		// 	checkType(actual.accept(this), 
		// 			  ((FIELD)formal).type,
		// 			  actual);
	    //}
		if (formals.hasNext() || actuals.hasNext())
            error(ast, "mismatch in number of arguments");
		ast.typeIndex = meth.index;
		return methType.result; 
    }

    public Type visit(Absyn.FieldExpr ast)
    {
		Type target = ast.target.accept(this);
		if ((target instanceof ARRAY) && (ast.field.equals("length")))
		{
			ast.typeIndex = -1;
			return INT;
		}
		if (!(target instanceof OBJECT))
	    {
			error(ast, "target not object, type "
				  + target);
			return VOID;
	    }
		FIELD field  = ((OBJECT)target).fields.get(ast.field);
		if (null == field)
	    {
			error(ast, "cannot resolve field " 
				  + ast.field);
			return VOID;
	    }
		ast.typeIndex = field.index;
		return field.type;
    }

    public Type visit(Absyn.Formal ast)
    {
		ast.checktype = ast.type.accept(this);
		return ast.checktype;
    }

public Type visit(Absyn.IdentifierExpr ast) 
{
    // Look up the identifier in the variable environment
	Type id = varEnv.get(ast.id); 
	if (id == null) {
		error(ast, "var was not declared");
		return VOID;
	}
    // Return the type of the identifier
    return id;
}

    public Type visit(Absyn.NewArrayExpr ast)
    {
		Type t = ast.type.accept(this);
		for (Absyn.Expr e : ast.dimensions)
	    {
			if (null != e) checkType(e.accept(this), INT, ast);
			t = new ARRAY(t);
	    }
		return t;
    }

    public Type visit(Absyn.NewObjectExpr ast)
    {   
		Type t = ast.type.accept(this);
		if (!(t instanceof OBJECT)){
			error(ast, null);
			return VOID; 
		}
		return t; 
    }

    public Type visit(Absyn.NullExpr ast) 
	{
		 return NIL; 
	}

    public Type visit(Absyn.ThisExpr ast)
	{
			if (currentClass == null){
			error(ast, null);
			return VOID;
		}
		return currentClass;
    }

    /* The Types */
    public Type visit(Absyn.ArrayType ast) 
	{     
		return new ARRAY(ast.base.accept(this));
	}

    public Type visit(Absyn.IdentifierType ast) 
    { 
		return new OBJECT(new CLASS(ast.id));
    }

    public Type visit(Absyn.IntegerType ast)    { return new INT(); }
    public Type visit(Absyn.IntegerLiteral ast) {  return new INT(); }
    public Type visit(Absyn.StringLiteral ast)  { return new STRING(); }

    /* The Booleans. */
    public Type visit(Absyn.BooleanType ast) {  return new BOOLEAN(); }
    public Type visit(Absyn.FalseExpr ast)   {   return new BOOLEAN(); }  
    public Type visit(Absyn.TrueExpr ast)    {  return new BOOLEAN(); } 
}