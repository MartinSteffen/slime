import slime.absynt.Expr;
import java.io.*;

/**
 * ParserTest <br>
 * for testing the SFCParser class in commandline <br>
 * mode with ASCII SFC-formated files or short sfc expressions<br>
 * @author Marco Wendel
 * @version $Id: ParserTest.java,v 1.2 2002-06-26 06:33:04 swprakt Exp $
 * ---------------------------------------------------------------
 * $Log: not supported by cvs2svn $
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
	if ( (args.length == 0)  || (args.length > 4) )
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
		sfcexpr = mySFCParser.parseExpression( args[1] );
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






