package slime.absynt.absfc;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Class for repeat-statements. A repeat-statements consists of an expression
 * and a stmtlist to be executed until expression is evaluated to true.
 *
 * @author initially provided by Marco Wendel.
 * @version $Id: StmtRepeat.java,v 1.0 2002/06/12 19:38:34 mwe
 */

public class StmtRepeat extends Statement implements Serializable { 
  private static final long serialVersionUID = Version.getSUID();
  public slime.absynt.Expr expr;
  public LinkedList stmtlist;
  public int repeatdepth;

  public StmtRepeat (slime.absynt.Expr _expr, LinkedList _stmtlist, int _rdepth) {
    expr = _expr;
    stmtlist = _stmtlist;
    repeatdepth = _rdepth;
    nodetype = "repeat";
  }

  public StmtRepeat (slime.absynt.Expr _expr, LinkedList _stmtlist) {
    expr = _expr;
    stmtlist = _stmtlist;
    nodetype = "repeat";
  }
  
  public StmtRepeat() {
    expr = new slime.absynt.Constval(true);
    stmtlist = new LinkedList();
    nodetype = "repeat";
    hascontent = false;
  }

    private void readObject(java.io.ObjectInputStream stream)
	throws java.io.IOException,java.lang.ClassNotFoundException {
	// own
	expr       = (slime.absynt.Expr) (stream.readObject());
	stmtlist   = (LinkedList) (stream.readObject());
	repeatdepth= stream.readInt();
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
	stream.writeInt(     repeatdepth );
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


