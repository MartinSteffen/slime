package slime.absynt.absfc;

/**
 * Version class to provide serial version for 
 * serialized objects of this package.
 *
 * @author initially provided by Marco Wendel.
 * @version  $Id: Version.java,v 1.4 2002-06-25 05:25:02 swprakt Exp $
 */

public class Version {
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


