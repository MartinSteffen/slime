package slime.absynt;
import java.io.Serializable;

/**
 * Abstract class to provide coordinates and locations.

 * The classes of this package
 * are a straighforward implemementation of the EBNF definition,
 * so in most cases, the code is self-explanatory.
 * 
 * @author Initially provided by Martin Steffen.
 * @version  $Id: Absynt.java,v 1.3 2002-06-12 18:51:52 swprakt Exp $
 */



abstract public class Absynt implements Serializable {
  public Position pos;
};





//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Absynt.java,v 1.3 2002-06-12 18:51:52 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.2  2002/04/16 19:02:52  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:42  swprakt
//	Slime initial version
//	
//	
//---------------------------------------------------------------------
