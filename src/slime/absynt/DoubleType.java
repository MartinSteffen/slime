package slime.absynt;
import java.io.Serializable;


/**
 * Type for doubles
 * 
 * @author initially provided by Marco Wendel
 * @version $Id: DoubleType.java,v 1.1 2002-06-27 20:29:01 swprakt Exp $
 */
 /*
 * $Log: not supported by cvs2svn $
 */


public class DoubleType  extends Type implements Serializable { 
  /** 
    *  visitor acceptor
    */
  public Object accept (Visitors.IType ask) throws Exception {
    return ask.forDoubleType();
  }
}

