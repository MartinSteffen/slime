package slime.absynt.absfc;
import java.io.Serializable;
import java.util.LinkedList;


/**
 * SFCabtree.java
 * Class for an (abstract sfc)-tree,
 * the top level syntactic construct,
 * i.e., the entry point.
 * @author initially provided by Marco Wendel.
 * @version $Id: SFCabtree.java,v 1.2 2002-06-25 05:25:02 swprakt Exp $
 */


public class SFCabtree extends Absfc implements Serializable{ 
    private static final long serialVersionUID = Version.getSUID();
    public String sfcprogname;
    public LinkedList stmtlist;
    public LinkedList proclist;
    public LinkedList varlist;
    public LinkedList declist;
    int procCnt; // Counters for descending the tree later
    int stmtCnt;
    int varCnt;
    int decCnt;
  /** 
   * Constructor just stores the arguments into the fields
   **/
  public SFCabtree ( String _sfcprogname, LinkedList _stmtlist ) {
    sfcprogname = _sfcprogname;
    stmtlist = _stmtlist;
//    proclist = _proclist;
//    varlist  = _varlist;
//    declist  = _declist;
    nodetype = "sfcabtree";
  }
  
  /** 
   * Constructor creates empty LinkedLists
   **/
  public SFCabtree() {
    stmtlist = new LinkedList();
//    proclist = new LinkedList();
//    varlist = new LinkedList();
//    declist = new LinkedList();      
    nodetype = "sfcabtree";
    hascontent = false;
  }

    private void readObject(java.io.ObjectInputStream stream)
	throws java.io.IOException,java.lang.ClassNotFoundException {
	// own
	sfcprogname = (String) (stream.readObject());
	stmtlist    = (LinkedList) (stream.readObject());
	proclist    = (LinkedList) (stream.readObject());
	varlist     = (LinkedList) (stream.readObject());
	declist     = (LinkedList) (stream.readObject());
	procCnt     = stream.readInt();
	stmtCnt     = stream.readInt();
	varCnt      = stream.readInt();
	decCnt      = stream.readInt();
	// Absfc
	depth      = stream.readInt();
	processed  = stream.readBoolean();
	hascontent = stream.readBoolean();
	nodetype   = (String) (stream.readObject());
	start_step = (slime.absynt.Step) stream.readObject();
	end_step   = (slime.absynt.Step) stream.readObject();
	stmt_trans = (slime.absynt.Transition) stream.readObject();
    }

    private void writeObject(java.io.ObjectOutputStream stream)
	throws java.io.IOException {
	// own
	stream.writeChars(   sfcprogname );
	stream.writeObject(  stmtlist );
	stream.writeObject(  proclist );
	stream.writeObject(  varlist );
	stream.writeObject(  declist );
	stream.writeInt(     procCnt );
	stream.writeInt(     stmtCnt );
	stream.writeInt(     varCnt );
	stream.writeInt(     decCnt );
	// Absfc
	stream.writeInt(     depth );
	stream.writeBoolean( processed );
	stream.writeBoolean( hascontent );
	stream.writeObject(  nodetype );
	stream.writeObject(  start_step );
	stream.writeObject(  end_step );
	stream.writeObject(  stmt_trans );
  }

}
// ---------------------------------------------------------------------
