package checks;
import java.lang.*;
import absynt.*;


/**
 * Top-level class providing the functionality for  the static analysis
 * par for  Slime.
 * @author Initially provided by Karsten Stahl, Martin Steffen.
 * @version  $Id: Checks.java,v 1.1 2002-06-12 09:18:56 swprakt Exp $
 */



public class Checks implements IChecks {
  public boolean isWellTyped(SFC p) throws CheckException {
    return true;}

  public boolean isWellformed(SFC p) throws CheckException{
    return true;}
}



//----------------------------------------------------------------------
//	checks/static analysis  Slime programs
//	----------------------------------------
//
//	$Log: not supported by cvs2svn $
//	
//---------------------------------------------------------------------
