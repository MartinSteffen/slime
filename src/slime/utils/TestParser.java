package slime.utils;

import java.io.IOException;
import java.io.*;
import java.util.*;
import slime.absynt.*;


/** Standalone file to test the parser on input files
 * @author Martin Steffen, Karsten Stahl
 * @version $Id: TestParser.java,v 1.4 2002-06-12 18:52:09 swprakt Exp $
 *
 * ============== //-->
 */
public class TestParser {
    
    /** main method to call from commandline
     * @param args from commandline
     */
    public static void main(String args []) {
        try{
            slime.utils.Parser parser = new slime.utils.Parser();
            PrettyPrint pp = new PrettyPrint();
            System.out.println("Input SAP program:");
            String _s = new String();
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            _s = in.readLine();
            System.out.println("parse: " + _s);
            LinkedList sap = parser.parseSAP(_s);
            System.out.println("*** Test: pretty print the result of parsing ***");
            pp.printSAP(sap);

            slime.utils.ExprParser eparser = new slime.utils.ExprParser();
            System.out.println("Input expression:");
            _s = in.readLine();
            System.out.println("parse: " + _s);
            slime.absynt.Expr e = eparser.parseExpr(_s);
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
