package absynt;
import java.io.Serializable;


/**
 * A class to store 2-dimensional positioning information.
 * 
 * @author Initially provided by Martin Steffen.
 * @version $Id: Position.java,v 1.1 2002-04-16 13:53:51 swprakt Exp $
 */


public class Position implements Serializable { 

  public float x;
  public float y;

  // -----------------
  public Position( float _x, float _y ) {
    x=_x; y=_y;
  }

  public Position() {
	  this.x = -1;
	  this.y = -1;  }

}






//----------------------------------------------------------------------
//	Abstract syntax for Snot programs
//	------------------------------------
//
//	$Id: Position.java,v 1.1 2002-04-16 13:53:51 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	
//	
//---------------------------------------------------------------------

