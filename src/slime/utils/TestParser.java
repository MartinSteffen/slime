package utils;

import java.io.IOException;
import java.io.*;
import java.util.*;
import absynt.*;


/** Standalone file to test the parser on input files
 * @author Martin Steffen, Karsten Stahl
 * @version $Id: TestParser.java,v 1.3 2002-06-10 14:41:57 swprakt Exp $
 *
 * ============== //-->
 */
public class TestParser {
    
    /** main method to call from commandline
     * @param args from commandline
     */
    public static void main(String args []) {
        try{
            utils.Parser parser = new utils.Parser();
            PrettyPrint pp = new PrettyPrint();
            System.out.println("Input SAP program:");
            String _s = new String();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            _s = in.readLine();
            System.out.println("parse: " + _s);
            LinkedList sap = parser.parseSAP(_s);
            System.out.println("*** Test: pretty print the result of parsing ***");
            pp.printSAP(sap);

            utils.ExprParser eparser = new utils.ExprParser();
            System.out.println("Input expression:");
            _s = in.readLine();
            System.out.println("parse: " + _s);
            absynt.Expr e = eparser.parseExpr(_s);
            System.out.println("*** Test: pretty print the result of parsing ***");
            pp.print(e);

        } catch (ParseException e){
            System.err.println(e.toString());
            System.exit(1);
        } catch (IOException e){
            System.err.println(e.toString());
            System.exit(1);
        } catch (Exception e){
            System.err.println(e.toString());
            System.exit(1);
        }
    }
}
