package checks;
import java.lang.*;


/**
 * Generic exception for the check class.
 * More specific exceptions are subclasses from this one

 * @author Initially provided by Karsten Stahl, Martin Steffen.
 * @version  $Id: CheckException.java,v 1.1 2002-06-12 07:04:47 swprakt Exp $
 */



public abstract class CheckException extends Exception{
  protected String message;
  public    String getMessage(){return message;}
}


//----------------------------------------------------------------------
//	checks/static analysis  Slime programs
//	----------------------------------------
//
//	$Log: not supported by cvs2svn $
//	
//---------------------------------------------------------------------
