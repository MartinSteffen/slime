package slime.absynt.absfc;
import java.io.Serializable;

/**
 * Version class to provide serial version for 
 * serialized objects of this package.
 *
 * @author initially provided by Marco Wendel.
 * @version  $Id: Version.java,v 1.1 2002-06-19 07:38:59 swprakt Exp $
 */

public class Version implements Serializable {
    private static final int radix = 6;
    private static long version;
    private static String thisVersionString = "Slime v1.0";

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


