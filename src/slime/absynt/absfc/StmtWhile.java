package slime.absynt.absfc;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Class for while-statements. A while-statements consists of an expression
 * and a stmtlist to be executed until expression is evaluated to false.
 *
 * @author initially provided by Marco Wendel.
 * @version $Id: StmtWhile.java,v 1.0 2002/06/12 19:35:32 mwe
 */

public class StmtWhile extends Statement implements Serializable { 
  private static final long serialVersionUID = Version.getSUID();
  public slime.absynt.Expr expr;
  public LinkedList stmtlist;
  public int whiledepth;

    public String toString() {
	StringBuffer out = new StringBuffer("");
	out.append("while (expr) { \n");
	for (java.util.Iterator i=stmtlist.iterator(); i.hasNext(); ) {
	    Object o = i.next();
	    out.append( o.toString()+"\n" );
	}
	out.append("} // end while \n");
	return out.toString();
    }

  public StmtWhile (slime.absynt.Expr _expr, LinkedList _stmtlist, int _wdepth) {
    expr = _expr;
    stmtlist = _stmtlist;
    whiledepth = _wdepth;
    nodetype = "while";
  }

  public StmtWhile (slime.absynt.Expr _expr, LinkedList _stmtlist) {
    expr = _expr;
    stmtlist = _stmtlist;
    nodetype = "while";
  }

  public StmtWhile () {
    expr = new slime.absynt.Constval(false);
    stmtlist = new LinkedList();
    nodetype = "while";
    hascontent = false;
  }

    private void readObject(java.io.ObjectInputStream stream)
	throws java.io.IOException,java.lang.ClassNotFoundException {
	// own
	expr       = (slime.absynt.Expr) (stream.readObject());
	stmtlist   = (LinkedList) (stream.readObject());
	whiledepth = stream.readInt();
	// Absfc
	depth      = stream.readInt();
	processed  = stream.readBoolean();
	hascontent = stream.readBoolean();
	nodetype   = (String) (stream.readObject());
	start_step = (slime.absynt.Step) stream.readObject();
	end_step   = (slime.absynt.Step) stream.readObject();
	stmt_trans = (slime.absynt.Transition) stream.readObject();
	// Statement
	elemNr     = stream.readInt();
    }

    private void writeObject(java.io.ObjectOutputStream stream)
	throws java.io.IOException {
	// own
	stream.writeObject(  expr );
	stream.writeObject(  stmtlist );
	stream.writeInt(     whiledepth );
	// Absfc
	stream.writeInt(     depth );
	stream.writeBoolean( processed );
	stream.writeBoolean( hascontent );
	stream.writeObject(  nodetype );
	stream.writeObject(  start_step );
	stream.writeObject(  end_step );
	stream.writeObject(  stmt_trans );
	// Statement
	stream.writeInt(     elemNr );
  }

  
}
// ---------------------------------------------------------------------



