package slime.sfcparser;

/**
 * <b>SFCParserException-Class</b><br>
 *    - returns standard, user-specified or predefined <br>
 *      SFCParseException with the errortype as <br>
 *	exception message<br>
 *    - awfully it is not possible (without modifying java_cup source<br>
 *      this is currently not our tasks) to override the report_error, <br>
 *	report_fatal_error,... with a method throwing Exceptions, otherwise<br>
 *	it would be a nice feature to throw some kind of exception like <br>
 *	this one from the java_cup-generated parser. If the supervisors of <br>
 *	this course have some time left, they might try to enhance cup for<br>
 *	their needs.<br>
 *      
 * @author initially provided by Marco Wendel <mwe@informatik.uni-kiel.de>
 * $Id: SFCParseException.java,v 1.3 2002-06-28 20:30:50 swprakt Exp $
 */
/* $Log: not supported by cvs2svn $
/* Revision 1.2  2002/06/27 14:30:57  swprakt
/* tried to remove Error 19: "_public ugliness",
/* now header comments consist of two parts, one
/* for javadoc and one containing CVS Versio log.
/*
 * Revision 1.1  2002/06/25 15:02:52  swprakt
 * missing files added
 *
 * Revision 1.1  2002/05/01 11:06:27  mwe
 * *** empty log message ***
 *
 */

public class SFCParseException extends Exception {
    
    /**
     * Default-Constructor<br>
     * creates exception with the standard error<br>
     * <b>"syntax error"</b><br>
     * @see Exception
     */
    SFCParseException(){
        super("syntax error");
    }
	
    /**
     * Constructor<br>
     * creates exception with user-specified String errorString<br>
     * @param errorString <b>contains errortype</b>
     * @see Exception
     */
    SFCParseException(String errorString){
        super(errorString);
    }

} // end of public class SFCParseException


