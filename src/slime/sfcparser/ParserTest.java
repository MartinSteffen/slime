package slime.sfcparser;
import java.io.*;

/**
 * ParserTest <br>
 * for testing the SFCParser class in commandline <br>
 * mode with ASCII SFC-formated files or short sfc expressions<br>
 * @author Marco Wendel
 * @version $Id: ParserTest.java,v 1.10 2002-07-02 13:32:20 swprakt Exp $
 * ---------------------------------------------------------------
 */
/* $Log: not supported by cvs2svn $
/* Revision 1.9  2002/07/02 12:29:47  swprakt
/* Phase 1 completed, correct parsing of complex
/* SFC programs into slime.absynt.absfc.SFCabtree.
/*
/* Phase 2 still in progress: today fixed some bug
/* with PrettyPrint4Absfc - partially the print(SFCabtree)
/* by simply outcommenting the lists for vars and decls.
/*
/* Left to do in phase 2:
/* verifying the correct(and complete!) creation of
/* the varlist, decllist for the slime.absynt.SFC.
/*
/* 02.07.2002 mwe@informatik.uni-kiel.de
/*
/* Revision 1.8  2002/06/28 20:01:07  swprakt
/* Modified PrettyPrint for use with slime.absfc.SFCabtree,
/* may now use it to debug Absfc2SFCConverter. I will try
/* to check in a fully functional SFCParser incl. nested
/* statements within one week, so 'round about 05/07/2002.
/* (mwe)
/*
/* Revision 1.7  2002/06/28 08:03:12  swprakt
/* old versions did conflict with "global" Makefile
/* in src/slime, albeight the GLOBAL Makefile should
/* exist in src. (mwe)
/*
/* Revision 1.6  2002/06/27 19:39:36  swprakt
/* slightly improved exceptionhandling
/*
/* Revision 1.5  2002/06/27 14:30:56  swprakt
/* tried to remove Error 19: "_public ugliness",
/* now header comments consist of two parts, one
/* for javadoc and one containing CVS Versio log.
/*
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

	String version = "1.8";
	String treestring = "sorry - it is empty";
	slime.absynt.SFC                    theSFC = null;
	slime.absynt.Expr                   sfcexpr = null;
	slime.absynt.absfc.SFCabtree        absfctree = null;
	slime.sfcparser.SFCParser           mySFCParser = null;
	slime.sfcparser.Absfc2SFCConverter  theConverter = null;
	// slime.utils.PrettyPrint             prettyPrinter = new slime.utils.PrettyPrint();
	slime.sfcparser.PPActions prettyPrinter =  new slime.sfcparser.PPActions();
	slime.sfcparser.PrettyPrint4Absfc   prettyAbsfc = new slime.sfcparser.PrettyPrint4Absfc();

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
		mySFCParser = new slime.sfcparser.SFCParser();
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
		    prettyPrinter.print( sfcexpr );
		    // 
		    // ***MORE*** parseExpression errorhandling ***RFC***
		    // 
		} // end of if-sfcexpr
	    } else {
		System.out.println("ParserTest: before File");
	        File sfcfile = new File( args[0] );
		System.out.println("ParserTest: before SFCParser");
		mySFCParser = new slime.sfcparser.SFCParser();
		// mSFCParser.debug=1;
		System.out.println("ParserTest: before parseFile");
		absfctree = mySFCParser.parseFile( sfcfile );
		System.out.println("ParserTest: before prettyAbsfc.print");
		prettyAbsfc.print( absfctree );
		System.out.println("ParserTest: before Absfc2SFCConverter");
		theConverter = new slime.sfcparser.Absfc2SFCConverter( absfctree );
		System.out.println("ParserTest: before getSFC");
		theSFC = theConverter.getSFC();
		System.out.println("ParserTest: before prettyPrinter.print");
		prettyPrinter.print( theSFC );
		//System.out.println("after parseFile");
		//treestring = absfctree.toString();
		//System.out.println("after absfctree.toString() ");
		//System.out.println( treestring );
		// ***MORE*** 
	    } // end of if-args-else
	} catch (FileNotFoundException fnfe) {
	    System.err.println("ParserTest: " + "The file \"" + args[0] + "\" was not found !");
	    System.exit(1);
	} catch (IOException ioe) {
	    System.err.println( "ParserTest: " + ioe.toString() );
	    System.exit(1);
	} catch (slime.sfcparser.SFCParseException sfce) {
	    System.err.println( "ParserTest->" + "SFCParseException: " + sfce.toString() );
	    System.exit(1);
	} catch (NullPointerException npe) {
	    System.err.println( "ParserTest: "+ npe.toString() );
	    System.exit(1);
	} catch (Exception e) {
	    System.err.println( "ParserTest: " + e.toString() );
	    // System.out.println( treestring );
	    System.exit(1);
	} // end of try-catch
    } // end of main
} // end of public class ParserTest






