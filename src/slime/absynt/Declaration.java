package slime.absynt;
import java.io.Serializable;


/**
 * Declaration of a variables.
 * 
 * @author Initially provided by Karsten Stahl.
 * @version $Id: Declaration.java,v 1.4 2002-06-25 04:45:12 swprakt Exp $
 */


public class Declaration extends Absynt implements Serializable { 
  public Variable var;
  public Type     type;
  public Constval val;

  public Declaration (Variable _var, Type _type, Constval _val) {
    var  = _var;
    type = _type;
    val  = _val;
  }

  /** 
      visitor acceptor
  */
  public Object accept (Visitors.IDeclaration ask) throws Exception {
    return ask.forDeclaration(var,type,val);
  }
}


//----------------------------------------------------------------------
//	Abstract syntax for Slime programs
//	------------------------------------
//
//	$Id: Declaration.java,v 1.4 2002-06-25 04:45:12 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.3  2002/06/12 18:51:54  swprakt
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
//	Revision 1.2  2002/04/16 19:02:54  swprakt
//	OK
//	
//	Revision 1.1  2002/04/16 13:53:47  swprakt
//	Slime initial version
//	
//	
//	
//---------------------------------------------------------------------

