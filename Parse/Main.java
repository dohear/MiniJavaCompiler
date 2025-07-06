/**
 * COSC 4400 - Project 4
 * Explain briefly the functionality of the program.
 * @authors [Daniel O'Hear, Varisha Asim, Erik Gutierrez]
 * Instructor [Jack Forden]
 * TA-BOT:MAILTO [daniel.ohear@marquette.edu, varisha.asim@marquette.edu, erik.gutierrez@marquette.edu]
 */
package Parse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main
{   
    static final String  VERSION = "COSC 4400 Concurrent MiniJava Parser v1.13";
    public static void main(String [] args){ 
		java.io.Reader reader = null;
		InputStreamReader isr =
		new InputStreamReader(System.in);
		reader = new BufferedReader(isr);

		try {
			PrintWriter writer = new PrintWriter(System.out);
			Absyn.Program parse = 
			new MiniJava(reader).Goal();
			Absyn.PrintVisitor pv =
			new Absyn.PrintVisitor(writer);
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