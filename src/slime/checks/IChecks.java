package checks;
import java.lang.*;
import absynt.*;


/**
 * Top-level interface for the static analysis part of the
 * Slime tool.

 * @author Initially provided by Karsten Stahl, Martin Steffen.
 * @version  $Id: IChecks.java,v 1.4 2002-06-12 07:41:10 swprakt Exp $
 */


public interface IChecks{
  public boolean isWellTyped (SFC p)  throws CheckException;
  public boolean isWellformed (SFC p) throws CheckException;
}



//----------------------------------------------------------------------
//	checks/static analysis  Slime programs
//	----------------------------------------
//
//	$Log: not supported by cvs2svn $
//	Revision 1.3  2002/06/12 07:31:02  swprakt
//	minimalistic interface (no exceptions given as yet),
//	
//	compiles.
//	
//	M. Steffen
//	
//	Revision 1.2  2002/06/12 07:28:46  swprakt
//	*** empty log message ***
//	
//	Revision 1.1  2002/06/12 07:04:47  swprakt
//	*** empty log message ***
//	
//	
//---------------------------------------------------------------------
