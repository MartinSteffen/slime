
package utils;


import java.io.IOException;
import java.io.*;
import java.util.*;
import absynt.*;


/**
 * Standalone file to test the parser on input files
 * @author Martin Steffen, Karsten Stahl
 * @version $Id: TestParser.java,v 1.1 2002-06-07 15:04:19 swprakt Exp $
 *
 * ============== //-->
 */
public class TestParser {
  public static void main (String args []) {
    try{
      utils.Parser parser = new utils.Parser();
      PrettyPrint pp = new PrettyPrint();
      System.out.println("Input SAP program:");
      String _sap = new String();
      BufferedReader in
	  = new BufferedReader(new InputStreamReader(System.in));
      _sap = in.readLine();
      System.out.println("parse: " + _sap);
      LinkedList sap = parser.parseSAP(_sap);
      System.out.println("*** Test: pretty print the result of parsing ***");
      pp.printSAP(sap);
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






