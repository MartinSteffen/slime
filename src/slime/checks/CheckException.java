package slime.checks;
import java.lang.*;
import slime.absynt.*;


/**
 * Generic exception for the check class.
 * More specific exceptions are subclasses from this one.
 * Since the checks will be implemented as visitor to the abstract
 * syntax, the most general exception must be a subclass of
 * the general visitor exception offered byte the abstract syntax.

 * @author Initially provided by Karsten Stahl, Martin Steffen.
 * @version  $Id: CheckException.java,v 1.4 2002-06-13 12:34:28 swprakt Exp $
 */



public class CheckException extends Exception {
  protected String message;
  public    String getMessage(){return message;}
}


//----------------------------------------------------------------------
//	checks/static analysis  Slime programs
//	----------------------------------------
//
//	$Log: not supported by cvs2svn $
//	Revision 1.3  2002/06/13 09:28:51  swprakt
//	OK
//	
//	Revision 1.2  2002/06/12 18:51:59  swprakt
//	reorganization of the package-structure
//	
//		src/<package>  => src/slime/<package>
//	
//	
//	as decided in the group meeting. consequently, some adaption had to be done
//	(wrt. import, package name etc). It compiles again.
//	
//	[M. Steffen]
//	
//	Revision 1.1  2002/06/12 07:04:47  swprakt
//	*** empty log message ***
//	
//	
//---------------------------------------------------------------------
