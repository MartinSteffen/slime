package slime.utils;

/**
 * Exception-Class for to report back parse-errors.
 *
 * @author Martin Steffen, Karsten Stahl
 * @version $Id: ParseException.java,v 1.2 2002-06-12 18:52:07 swprakt Exp $
 *
 */

public class ParseException extends Exception{

    
    /**
     * Default-Konstruktor, generates vanilla exception
     */
    ParseException(){
        super("syntax error");
    }
	
    ParseException(String errString){
        super(errString);
    }

}
