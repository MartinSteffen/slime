package slime.absynt.absfc;
import java.io.Serializable;

/**
 * Declaration of a variables within the abstract sfc tree.
 * 
 * @author initially provided by Marco Wendel
 * @version $Id: StmtDecl.java,v 1.0 2002/06/12 20:14:08 mwe
 */


public class StmtDecl extends Statement implements Serializable { 
  private static final long serialVersionUID = Version.getSUID();
  public slime.absynt.Variable var;
  public slime.absynt.Type     type;
  public slime.absynt.Constval val;
  public slime.absynt.Expr expr;
  public boolean hasvalue;
  public boolean isexpr;

    public String toString() {
	StringBuffer out = new StringBuffer("");
	out.append("type var := constval;");
	return out.toString();
    }

  public StmtDecl ( slime.absynt.Variable  _var, 
                    slime.absynt.Type      _type,
		    slime.absynt.Constval  _val) {
    var  = _var;
    type = _type;
    val  = _val;
    hasvalue = true;
    nodetype = "declaration";
    isexpr = false;
  }

  public StmtDecl ( slime.absynt.Variable  _var, 
                    slime.absynt.Type      _type,
		    slime.absynt.Expr      _expr) {
    var  = _var;
    type = _type;
    expr = _expr;
    hasvalue = true;
    nodetype = "declaration";
    isexpr = true;
  }

  public StmtDecl ( slime.absynt.Variable  _var, 
                    slime.absynt.Type      _type ) {
    var  = _var;
    type = _type;
    val  = null;
    nodetype = "declaration";
    hasvalue = false;
    isexpr = false;
  }

  public StmtDecl () {
    var  = null;
    type = null;
    val  = null;
    nodetype = "declaration";
    hasvalue = false;
    hascontent = false;
    isexpr = false;
  }

    private void readObject(java.io.ObjectInputStream stream)
	throws java.io.IOException,java.lang.ClassNotFoundException {
	// own
	var        = (slime.absynt.Variable) (stream.readObject());
	type       = (slime.absynt.Type) (stream.readObject());
	val        = (slime.absynt.Constval) (stream.readObject());
	expr       = (slime.absynt.Expr) (stream.readObject());
	hasvalue   = stream.readBoolean();
	isexpr     = stream.readBoolean();
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
	stream.writeObject(  type );
	stream.writeObject(  expr );
	stream.writeBoolean( hasvalue );
	stream.writeBoolean( isexpr );
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

