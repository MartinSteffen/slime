package slime.absynt;
import java.io.Serializable;


/**
 * ``Type'' to denote absence of type information.
 * Before the type checker has found out the type, 
 * it is undefined (except for the dangers of nil-dereferencing,
 * one can think of it as a ``nil-reference'' to a type). So
 * Undef is not a type of the type system or the language,
 * but an internal representation useful for the type checker.
 *
 * After termination of the type checker, no type field must
 * be undefined.
 * @author Initially provided by Martin Steffen and Karsten Stahl.
 * @version $Id: UndefType.java,v 1.3 2002-06-24 20:08:11 swprakt Exp $
 */


public class UndefType  extends Type implements Serializable { 
  public Object accept (Visitors.IType ask) throws Exception {
    return ask.forUndefType();
  }
}




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: UndefType.java,v 1.3 2002-06-24 20:08:11 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.2  2002/06/14 06:37:39  swprakt
//	SFC's may be  named, now (as decided 12.6.2002).
//	Corresponding field + constructors added.
//	
//	[M. Steffen]
//	
//	Revision 1.1  2002/06/14 06:28:15  swprakt
//	*** empty log message ***
//	
//---------------------------------------------------------------------

