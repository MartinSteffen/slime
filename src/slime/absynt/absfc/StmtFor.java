package slime.absynt.absfc;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Class for for-statements. 
 *
 * @author initially provided by Marco Wendel.
 * @version $Id: StmtFor.java,v 1.0 2002/06/12 19:35:32 mwe
 */

public class StmtFor extends Statement implements Serializable { 
    private static final long serialVersionUID = Version.getSUID();
    public slime.absynt.Variable var;
    public slime.absynt.Type vartype;
    public slime.absynt.Expr expr1;
    public slime.absynt.Expr expr2;
    public slime.absynt.absfc.Statement stmt;
    public LinkedList stmtlist;
    public int fordepth;

    public String toString() { // not to be used
	StringBuffer out = new StringBuffer("");
	out.append("for ("); // expr) { \n");
	for (java.util.Iterator i=stmtlist.iterator(); i.hasNext(); ) {
	    Object o = i.next();
	    out.append( o.toString()+"\n" );
	}
	out.append("} // end while \n");
	return out.toString();
    }

  public StmtFor (slime.absynt.Type _vartype,
		  slime.absynt.Variable _var,
		  slime.absynt.Expr _expr1,
		  slime.absynt.Expr _expr2,
		  slime.absynt.absfc.Statement _stmt,
		  LinkedList _stmtlist) {
      vartype = _vartype;
      var = _var;
      expr1 = _expr1;
      expr2 = _expr2;
      stmt = _stmt;
      stmtlist = _stmtlist;
      nodetype = "for";
  }

  public StmtFor (slime.absynt.Variable _var,
		  slime.absynt.Expr _expr1,
		  slime.absynt.Expr _expr2,
		  slime.absynt.absfc.Statement _stmt,
		  LinkedList _stmtlist) {
      var = _var;
      expr1 = _expr1;
      expr2 = _expr2;
      stmt = _stmt;
      stmtlist = _stmtlist;
      nodetype = "for";
  }

  public StmtFor (slime.absynt.Variable _var,
		  slime.absynt.Expr _expr1,
		  slime.absynt.Expr _expr2,
		  LinkedList _stmtlist) {
      var = _var;
      expr1 = _expr1;
      expr2 = _expr2;
      stmtlist = _stmtlist;
      nodetype = "for";
  }

  public StmtFor (LinkedList _stmtlist) {
      stmtlist = _stmtlist;
      nodetype = "for";
  }

    private void readObject(java.io.ObjectInputStream stream)
	throws java.io.IOException,java.lang.ClassNotFoundException {
	// own
	var        = (slime.absynt.Variable)(stream.readObject());
	vartype    = (slime.absynt.Type)(stream.readObject());
	expr1      = (slime.absynt.Expr) (stream.readObject());
	expr2      = (slime.absynt.Expr) (stream.readObject());
	stmt       = (slime.absynt.absfc.Statement) (stream.readObject());
	stmtlist   = (LinkedList) (stream.readObject());
	fordepth   = stream.readInt();
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
	stream.writeObject(  vartype );
	stream.writeObject(  expr1 );
	stream.writeObject(  expr2 );
	stream.writeObject(  stmt );
	stream.writeObject(  stmtlist );
	stream.writeInt(     fordepth );
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



