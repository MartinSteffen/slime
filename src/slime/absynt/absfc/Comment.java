package slime.absynt.absfc;
import java.io.Serializable;

/**
 * Comment Class for abstract SFC tree
 * 
 * @author initially provided by Marco Wendel.
 * @version $Id: Comment.java,v 1.0 2002/06/12 19:57:56 mwe
 */


public class Comment extends Statement implements Serializable { 
    private static final long serialVersionUID = Version.getSUID();
    public String content;
    
    public Comment(String _content) {
	content = _content;
	nodetype = "comment";
    }    

    public Comment() {
	content = "";
	nodetype = "comment";
	hascontent = false;
    }    

    private void readObject(java.io.ObjectInputStream stream)
	throws java.io.IOException,java.lang.ClassNotFoundException {
	// own
	content    = (String) stream.readObject();
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
	stream.writeObject(  content );
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

