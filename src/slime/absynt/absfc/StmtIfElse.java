package slime.absynt.absfc;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Class for if-else-statements. An if-else statements consists of an expression
 * and a stmtlist to be executed if expression is evaluated to true and another
 * stmtlist to be executed if expression is evaluated to false.
 *
 * @author initially provided by Marco Wendel.
 * @version $Id: StmtIfElse.java,v 1.0 2002/06/12 19:27:44 mwe
 */

public class StmtIfElse extends Statement implements Serializable { 
    private static final long serialVersionUID = Version.getSUID();
    public slime.absynt.Expr expr;
    public LinkedList ifstmtlist;
    public LinkedList elsestmtlist;
    public int ifelsedepth;  
    public boolean emptyelse;

  public StmtIfElse (slime.absynt.Expr _expr, LinkedList _ifstmtlist, LinkedList _elsestmtlist, int _idepth) {
    expr = _expr;
    ifstmtlist = _ifstmtlist;
    elsestmtlist = _elsestmtlist;
    ifelsedepth = _idepth;
    nodetype = "ifelse";
  }

  public StmtIfElse (slime.absynt.Expr _expr, LinkedList _ifstmtlist, LinkedList _elsestmtlist) {
    expr = _expr;
    ifstmtlist = _ifstmtlist;
    elsestmtlist = _elsestmtlist;
    nodetype = "ifelse";
  }


  public StmtIfElse (slime.absynt.Expr _expr, LinkedList _ifstmtlist) {
    expr = _expr;
    ifstmtlist = _ifstmtlist;
    elsestmtlist = new LinkedList();
    nodetype = "ifelse";
    emptyelse = true;
  }

  public StmtIfElse (slime.absynt.Expr _expr) {
    expr = _expr;
    ifstmtlist = new LinkedList();
    elsestmtlist = new LinkedList();
    nodetype = "ifelse";
    hascontent = false;
  }

  public StmtIfElse () {
    expr = new slime.absynt.Constval(false);
    ifstmtlist = new LinkedList();
    elsestmtlist = new LinkedList();
    nodetype = "ifelse";
    hascontent = false;
  }

    private void readObject(java.io.ObjectInputStream stream)
	throws java.io.IOException,java.lang.ClassNotFoundException {
	// own
	expr        = (slime.absynt.Expr) (stream.readObject());
	ifstmtlist  = (LinkedList) (stream.readObject());
	elsestmtlist= (LinkedList) (stream.readObject());
	ifelsedepth = stream.readInt();
	emptyelse   = stream.readBoolean();
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
	stream.writeObject(  ifstmtlist );
	stream.writeObject(  elsestmtlist );
	stream.writeInt(     ifelsedepth );
	stream.writeBoolean( emptyelse );
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
