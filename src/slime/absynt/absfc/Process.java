package slime.absynt.absfc;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Process Class for abstract SFC tree
 * 
 * @author initially provided by Marco Wendel.
 * @version $Id: Process.java,v 1.0 2002/06/12 19:57:56 mwe
 */


public class Process extends Statement implements Serializable { 
    private static final long serialVersionUID = Version.getSUID();
    public String name;
    public LinkedList stmtlist;
    public int procdepth;
    public LinkedList internalStepList;
    public LinkedList internalTransitionList;
    public LinkedList internalActionList;
    public LinkedList internalDeclarationList;

    public String toString() {
	StringBuffer out = new StringBuffer("");
	out.append("process ");
	out.append( name );
	out.append( " { \n");
	for (java.util.Iterator i = stmtlist.iterator(); i.hasNext();){
	    Object o = i.next();
	    out.append( o.toString() );
	}
	out.append("\n } // end of process \n" );
	return out.toString();
    }
    
    public Process(String _name, LinkedList _stmtlist) {
	name = _name;
	stmtlist = _stmtlist;
	nodetype = "process";
	internalStepList        = new LinkedList();
	internalTransitionList  = new LinkedList();
	internalActionList      = new LinkedList();
	internalDeclarationList = new LinkedList();
    }    

    public Process(String _name) {
	name = _name;
	stmtlist = new LinkedList();
	nodetype = "process";
	hascontent = false;
	internalStepList        = new LinkedList();
	internalTransitionList  = new LinkedList();
	internalActionList      = new LinkedList();
	internalDeclarationList = new LinkedList();
    }    

    public Process() {
	name = "";
	stmtlist = new LinkedList();
	nodetype = "process";
	hascontent = false;
	internalStepList        = new LinkedList();
	internalTransitionList  = new LinkedList();
	internalActionList      = new LinkedList();
	internalDeclarationList = new LinkedList();
    }    

    private void readObject(java.io.ObjectInputStream stream)
	throws java.io.IOException,java.lang.ClassNotFoundException {
	// own
	name       = (String) (stream.readObject());
	stmtlist   = (LinkedList) (stream.readObject());
	procdepth  = stream.readInt();
	internalStepList        = (LinkedList) (stream.readObject());
	internalTransitionList  = (LinkedList) (stream.readObject());
	internalActionList      = (LinkedList) (stream.readObject());
	internalDeclarationList = (LinkedList) (stream.readObject());
	
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
	stream.writeObject(  name );
	stream.writeObject(  stmtlist );
	stream.writeInt(     procdepth );
	stream.writeObject(  internalStepList );
	stream.writeObject(  internalTransitionList );
	stream.writeObject(  internalActionList );
	stream.writeObject(  internalDeclarationList );
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
