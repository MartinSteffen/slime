package slime.absynt.absfc;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Join Assignment for abstract SFC tree
 * 
 * @author initially provided by Marco Wendel.
 * @version $Id: StmtJoin.java,v 1.0 2002/06/12 20:06:12 mwe
 */


public class StmtJoin extends Statement implements Serializable { 
    private static final long serialVersionUID = Version.getSUID();
    public LinkedList proclist;
    public int procdepth;
    
    public StmtJoin( LinkedList _proclist, int _pdepth ) {
	proclist = _proclist;
	procdepth = _pdepth;
	nodetype = "join";
    }    

    public StmtJoin( LinkedList _proclist ) {
	proclist = _proclist;
	nodetype = "join";
    }    

    public StmtJoin() {
	proclist = new LinkedList();
	nodetype = "join";
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
