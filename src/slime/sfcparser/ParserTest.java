package slime.sfcparser;
import slime.absynt.Expr;
import java.io.*;

/**
 * ParserTest <br>
 * for testing the SFCParser class in commandline <br>
 * mode with ASCII SFC-formated files or short sfc expressions<br>
 * @author Marco Wendel
 * @version $Id: ParserTest.java,v 1.5 2002-06-27 14:30:56 swprakt Exp $
 * ---------------------------------------------------------------
 */
/* $Log: not supported by cvs2svn $
 * Revision 1.4  2002/06/26 10:39:48  swprakt
 * yy_eof muss nun noch irgendwie behandelt werden...
 *
 * Revision 1.3  2002/06/26 07:28:38  swprakt
 * ParserTest.java gehoert nun zum Package slime.sfcparser
 * CVS
 *
 * Revision 1.2  2002/06/26 06:33:04  swprakt
 * Makefile geaendert nun mit fuer Slime gueltigem CLASSPATH "../..".
 * Absfc2SFCConverter.java nun in slime.sfcparser-package.
 * Parser nicht veraendert nur neu erzeugt. (mwe)
 *
 * Revision 1.1  2002/06/25 06:01:31  swprakt
 * added Absfc2SFCConverter Object and
 * theConverter.getSFC(); missing return of SFC,
 * possible not compilable...
 *
 * Revision 1.1  2002/05/01 11:42:50  mwe
 * The commandline tool for testing the parsing
 * awfully neither a useful parser to test exists
 * nor a gui. Did you meet on 30.04.2002 in the
 * skyscraper ???
 *
 */
public class ParserTest {
    public static void main (String args []) {
	String      version = "1.0";
	slime.absynt.Expr sfcexpr = null;
	if ( (args.length == 0)  || (args.length > 100) )
	{
	    System.out.println("SFCParserTest " + version + " - Part of the Slime Project");
	    System.out.println("Usage: java SFCParserTest <SFCProgramFile>");
	    System.out.println("       java SFCParserTest -e <SFCExpression>");
	    System.out.println(" ");
	    System.exit(1);
	} // end of if args.length
	try {
	    if ( (args[0].equals("-e"))  || (args[0].equals("-E")) 
	      || (args[0].equals("--e")) || (args[0].equals("--E")) 
	       ) 
	    {
		slime.sfcparser.SFCParser mySFCParser = new slime.sfcparser.SFCParser();
		StringBuffer sbuf = new StringBuffer("");
		for (int i=1; i<args.length; i++) {
		    sbuf.append(args[i]);
		}
		sbuf.append("\n");
		String theExpression = sbuf.toString();
		sfcexpr = mySFCParser.parseExpression( theExpression );
		if (sfcexpr == null) 
		{
		    System.out.println("SFCParser: " + args[1] + " is no allowed sfc expression.");
		} else {
		    // 
		    // ***MORE*** parseExpression errorhandling ***RFC***
		    // 
		} // end of if-sfcexpr
	    } else {
	        File sfcfile = new File( args[0] );
		slime.sfcparser.SFCParser mySFCParser = new slime.sfcparser.SFCParser();
		// mSFCParser.debug=1;
		slime.absynt.absfc.SFCabtree absfctree = mySFCParser.parseFile( sfcfile );
		slime.sfcparser.Absfc2SFCConverter theConverter = new slime.sfcparser.Absfc2SFCConverter( absfctree );
		slime.absynt.SFC theSFC = theConverter.getSFC();
		// ***MORE*** 
	    } // end of if-args-else
	} catch (FileNotFoundException fnfe) {
	    System.err.println("SFCParser-Error: The file \"" + args[0] + "\" was not found !");
	    System.exit(1);
	} catch (IOException ioe) {
	    System.err.println( ioe.toString() );
	    System.exit(1);
//	} catch (SFCParseException sfce) {
//	    System.err.println( "SFCParseException: " + sfce.toString() );
//	    System.exit(1);
	} catch (Exception e) {
	    System.err.println( e.toString() );
	    System.exit(1);
	} // end of try-catch
    } // end of main
} // end of public class ParserTest






