package slime.absynt.absfc;
import java.io.Serializable;


/**
 * An assignment-statement in the Abstract SFC tree
 * 
 * @author initially provided by Marco Wendel.
 * @version $Id: StmtAssign.java,v 1.0 2002/06/12 19:53:45 mwe
 */


public class StmtAssign extends Statement implements Serializable { 
  private static final long serialVersionUID = Version.getSUID();
  public slime.absynt.Variable var;
  public slime.absynt.Expr     val;

  public StmtAssign ( slime.absynt.Variable x, slime.absynt.Expr e ) {
    var = x;
    val = e;
    nodetype = "assignment";
  }
 
  public StmtAssign () {
    var = null;
    val = null;
    nodetype = "assignment";
    hascontent = false;
  }

    private void readObject(java.io.ObjectInputStream stream)
	throws java.io.IOException,java.lang.ClassNotFoundException {
	// own
	var        = (slime.absynt.Variable) (stream.readObject());
	val        = (slime.absynt.Expr) (stream.readObject());
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
	stream.writeObject(  var );
	stream.writeObject(  val );
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
