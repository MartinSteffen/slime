package slime.sfcparser;

/**
 * <b>SFCParserException-Class</b><br>
 *    - returns standard, user-specified or predefined <br>
 *      SFCParseException with the errortype as <br>
 *	exception message
 * @author initially provided by Marco Wendel <mwe@informatik.uni-kiel.de>
 * $Id: SFCParseException.java,v 1.1 2002-06-25 15:02:52 swprakt Exp $
 * $Log: not supported by cvs2svn $
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


