package slime.checks.tests;
import java.io.*;

import java.util.*;
import slime.absynt.*;

/**
 * WellformedTest <break>
 * A little testing of the wellformedness checker, using the parser
 *
 * @author <a href="http://www.informatik.uni-kiel.de/~ms" target="_top">Martin Steffen</a> and Karsten Stahl. 
 * @version $Id: WellformedTest.java,v 1.3 2002-07-10 17:11:43 swprakt Exp $
 * ---------------------------------------------------------------
 */


public class WellformedTest {
  public static void main (String args []) {
    slime.absynt.SFC                    theSFC = null;
    slime.absynt.Expr                   sfcexpr = null;
    slime.absynt.absfc.SFCabtree        absfctree = null;
    slime.sfcparser.SFCParser           mySFCParser = null;
    slime.sfcparser.Absfc2SFCConverter  theConverter = null;
    slime.utils.PrettyPrint             pp = new slime.utils.PrettyPrint();
    slime.checks.Typecheck              tc =  new slime.checks.Typecheck();
    slime.checks.Wellformed             wc =  new slime.checks.Wellformed();
    SFC sfc1 = Example.getExample1();
    //     slime.checks.Wellformed.Initcheck wci  =  new slime.checks.Wellformed.Initcheck();
    try {
      wc.check(sfc1);    
    }
    catch (Exception ex) {
      System.out.println (ex.toString());
      System.out.println (ex.getMessage());
      System.out.println ("Well formed exception");
      System.exit(0);
    }
    // wci.Initcheck.check(null);
    // slime.sfcparser.PPActions prettyPrinter =  new slime.sfcparser.PPActions(); for the version without that StepAction...
    slime.sfcparser.PrettyPrint4Absfc   prettyAbsfc = new slime.sfcparser.PrettyPrint4Absfc();
    try {
      mySFCParser = new slime.sfcparser.SFCParser();
      slime.utils.ExprParser eparser = new slime.utils.ExprParser();
      //		
      TestExpressions testel = new TestExpressions();
      for (Iterator i = testel.elist.iterator(); i.hasNext(); ) {
	String  es = (String)i.next();
	System.out.print (es + " =>");
	slime.absynt.Expr ea = eparser.parseExpr(es);   // parse
	if (ea == null) 
	  {
	    System.out.println("SFCParser: " + args[1] + " is no allowed sfc expression.");
	  } else {
	    pp.print( ea );
	    try {
	      slime.absynt.Type t = tc.check(ea);
	      System.out.print("of type ");
	      pp.print (t);
	    } 
	    catch (slime.checks.CheckException ex)
	      {
		System.out.println ("Exception thrown: >>>");
		System.out.println (ex.getMessage());  // we must be dynamic (=method) here
	      };
	  }
      }; // end loop
      System.out.println ("checking programs now");
      pp.print(sfc1);
      slime.absynt.Type t = tc.check(sfc1);
      System.out.print ("Result type = ");
      pp.print (t);
      
    } catch (FileNotFoundException fnfe) {
      System.err.println("TypecheckTest: " + "The file \"" + args[0] + "\" was not found !");
      System.exit(1);
    } catch (IOException ioe) {
      System.err.println( "TypecheckTest: " + ioe.toString() );
      System.exit(1);
    } catch (slime.sfcparser.SFCParseException sfce) {
      System.err.println( "TypecheckTest->" + "SFCParseException: " + sfce.toString() );
      System.exit(1);
    } catch (NullPointerException npe) {
      System.err.println( "TypecheckTest: "+ npe.toString() );
      System.exit(1);
    } catch (Exception e) {
      System.err.println( "TypecheckTest: " + e.toString() );
      System.err.println( "TypecheckTest: " + e.getMessage() );
      // System.out.println( treestring );
      System.exit(1);
    } // end of try-catch
  } // end of main
} 


