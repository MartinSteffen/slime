package slime.absynt.absfc;

/**
 * Version class to provide serial version for 
 * serialized objects of this package.
 *
 * @author initially provided by Marco Wendel.
 * @version  $Id: Version.java,v 1.5 2002-06-27 20:29:02 swprakt Exp $
 */

public class Version {
    private static final int radix = 36;
    private static long version;
    private static String thisVersionString = "slime";

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
	        System.out.println("Version: NumberFormatException");
	}
    } 

}
// ---------------------------------------------------------------------


