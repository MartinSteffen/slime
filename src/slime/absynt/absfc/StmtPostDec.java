package slime.absynt.absfc;
import java.io.Serializable;


/**
 * An increment-statement, that is postevaluated in the Abstract SFC tree
 * 
 * @author initially provided by Marco Wendel.
 * @version $Id: StmtPostDec.java,v 1.0 2002/06/12 19:53:45 mwe
 */


public class StmtPostDec extends Statement implements Serializable { 
  private static final long serialVersionUID = Version.getSUID();
  public slime.absynt.Variable var;

    public String toString() {
	StringBuffer out = new StringBuffer("");
	out.append("var := expr;");
	return out.toString();
    }

  public StmtPostDec ( slime.absynt.Variable x ) {
    var        = x;
    nodetype   = "postdec";
  }
 
  public StmtPostDec () {
    var        = null;
    nodetype   = "postdec";
    hascontent = false;
  }

    private void readObject(java.io.ObjectInputStream stream)
	throws java.io.IOException,java.lang.ClassNotFoundException {
	// own
	var        = (slime.absynt.Variable) (stream.readObject());
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
