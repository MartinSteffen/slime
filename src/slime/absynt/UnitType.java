package slime.absynt;
import java.io.Serializable;


/**
 * Type for well-type program side-effect constructs.
 * It can be seen as the type for statements, i.e., the programs
 * constructs that do not give back a value (for instance transitions).
 * In these cases the type checker reports the type Unit. It is 
 * not for use in programs. The choice of Unit was made
 * (instead of OK or something else) because  this is a standard
 * choice int more laborate situations (such as languages combining
 * functions and imperative statements.
 * The statement ``skip'' can be seen as the unit-value.
 * C-programmer (or Java-programmer etc) can think of the type
 * as Void.
 * 
 * @author <a href="http://www.informatik.uni-kiel.de/~ms" target="_top">Martin Steffen</a> and Karsten Stahl.
 * @version $Id: UnitType.java,v 1.3 2002-07-10 17:04:30 swprakt Exp $
 */


public class UnitType  extends Type implements Serializable { 
  /** 
      visitor acceptor
  */
  public Object accept (Visitors.IType ask) throws Exception {
    return ask.forBoolType();
  }


  /** type equality.
   */
  public boolean equals (Type t) {
    return  (t instanceof  UnitType);
  }


}




//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: UnitType.java,v 1.3 2002-07-10 17:04:30 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.2  2002/06/24 20:08:11  swprakt
//	Types extended with visitor methods,
//	also extended is the type ckecker, which is a visitor to the absynt.
//	
//	[Steffen]
//	
//	Revision 1.1  2002/06/14 06:13:31  swprakt
//	*** empty log message ***
//	
//	
//---------------------------------------------------------------------

