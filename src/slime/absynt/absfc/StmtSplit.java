package slime.absynt.absfc;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Split Assignment for abstract SFC tree
 * 
 * @author initially provided by Marco Wendel.
 * @version $Id: StmtSplit.java,v 1.0 2002/06/12 20:01:24 mwe
 */


public class StmtSplit extends Statement implements Serializable { 
    private static final long serialVersionUID = Version.getSUID();
    public LinkedList proclist;
    public int procdepth;
    
    public String toString() {
	StringBuffer out = new StringBuffer("");
	    out.append("split { \n");
	for (java.util.Iterator i=proclist.iterator(); i.hasNext(); ) {
	    Object o = i.next();
	    out.append( o.toString()+"\n" );
	}
	out.append("} // end split \n");
	return out.toString();
    }

    public StmtSplit( LinkedList _proclist, int _pdepth ) {
	proclist = _proclist;
	procdepth = _pdepth;
	nodetype = "split";
    }    

    public StmtSplit( LinkedList _proclist ) {
	proclist = _proclist;
	nodetype = "split";
    }    

    public StmtSplit() {
	proclist = new LinkedList();
	nodetype = "split";
	hascontent = false;
    }    

    private void readObject(java.io.ObjectInputStream stream)
	throws java.io.IOException,java.lang.ClassNotFoundException {
	// own
	proclist   = (LinkedList) (stream.readObject());
	procdepth  = stream.readInt();
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
	stream.writeObject(  proclist );
	stream.writeInt(     procdepth );
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
