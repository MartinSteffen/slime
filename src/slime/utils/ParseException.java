package utils;

/**
 * Exception-Class for to report back parse-errors.
 *
 * @author Martin Steffen, Karsten Stahl
 * @version $Id: ParseException.java,v 1.1 2002-06-07 15:06:24 swprakt Exp $
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
