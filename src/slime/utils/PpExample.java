
package utils;

import utils.*;
import absynt.*;
import java.util.*;


/**
   Beispiel fuer die Verwendung des Pretty Printers.
 * @author Initially provided by Martin Steffen.
 * @version  $Id: PpExample.java,v 1.2 2002-06-07 15:04:17 swprakt Exp $    
 */


public class PpExample {
    public static void main(String argv[]) {
	PrettyPrint pp = new PrettyPrint();
	SFC sfc1 = Example.getExample1();
	LinkedList sap1 = Example.getExampleSAP1();
	B_expr expr1 = Example.getExampleExpression1();
	System.out.println("*** SFC1 ***");
	pp.print(sfc1);
	System.out.println("*** SAP1 ***");
	pp.printSAP(sap1);
	System.out.println("*** Expression1 ***");
	pp.print(expr1);
    }
}


//----------------------------------------------------------------------
//	Example for the pretty printer
//	------------------------------
//
//	$Id: PpExample.java,v 1.2 2002-06-07 15:04:17 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.1  2002/04/16 13:57:54  swprakt
//	Slime initial version
//	
//	
//---------------------------------------------------------------------

