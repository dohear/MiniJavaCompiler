package Semant;

import java.io.Reader;

import Parse.MiniJava;
import Parse.ParseException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main
{   
    public static void main(String [] args) 
    {
	InputStreamReader isr =	new InputStreamReader(System.in);
        Reader reader = new BufferedReader(isr);

	try
	{
	TypeChecker checker = new TypeChecker();
	Absyn.Program parse = new MiniJava(reader).Goal();
	PrintWriter writer = new PrintWriter(System.out);


	Semant.PrintVisitor pv = new Semant.PrintVisitor(writer);
	checker.visit(parse);
	pv.visit(parse);
	writer.flush();
	}
    catch (ParseException p)
	{
	    System.out.println(p.toString());
	    System.exit(-1);
	}
    }
}