package slime.absynt.absfc;
import java.io.Serializable;

/**
 * Version class to provide serial version for 
 * serialized objects of this package.
 *
 * @author initially provided by Marco Wendel.
 * @version  $Id: Version.java,v 1.3 2002-06-19 08:33:16 swprakt Exp $
 */

public class Version implements Serializable {
    private static final int radix = 36;
    private static long version = 1356099454469L;
    private static String thisVersionString = "Slime";

    Version() { 
	renderVersion();
    } 

    Version( String versionString ) {
	thisVersionString = versionString;
	renderVersion();
    } 
    
    public static void setVersion( String versionString ) {
	thisVersionString = versionString;
	renderVersion();
    }
    
    public static long getVersion() {
	renderVersion();
	return version;
    }
	
    public static long getSUID() {
	renderVersion();
	return version;
    }


    public static void renderVersion() {
	try {
		version = java.lang.Long.parseLong( thisVersionString, radix); 
	} catch (NumberFormatException nfe) {
	        System.out.println("Falsche Versions-Kennung :)");
	}
    } 

}
// ---------------------------------------------------------------------


