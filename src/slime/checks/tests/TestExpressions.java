
package slime.checks.tests;
import java.io.*;

import java.util.*;
import slime.absynt.*;

/**
 * Test expression <break>
 * This is just a list of string expressions that can be iterated through for testing.
 * @author Martin Steffen
 * @version $Id: TestExpressions.java,v 1.3 2002-07-05 17:38:33 swprakt Exp $
 * ---------------------------------------------------------------
 */


public class TestExpressions {
  public LinkedList elist = new LinkedList();
  TestExpressions() {
    elist.addLast ("true");
    elist.addLast ("1");
    elist.addLast ("false");
    elist.addLast ("1+41");
    elist.addLast ("1*2");
    elist.addLast ("1*2+3");
    elist.addLast ("1+2*3");
    elist.addLast ("1+2*3*5");
    elist.addLast ("1*2+3*5");
    elist.addLast ("1/2-3*5");
    elist.addLast ("1/2-3*5");
    elist.addLast ("(12-3*5)");
    elist.addLast ("((12-3*5))");
    elist.addLast ("((12-(3*5)))");
    elist.addLast ("(((12)-(3*5)))");
    elist.addLast ("(((-12)-(3*5)))");
    elist.addLast ("1=2");
    elist.addLast ("1 <= 2");
    elist.addLast ("(1 <= 2) and (1 = 2)");
    elist.addLast ("(1 <= 2) and (1 = -2)");
    elist.addLast ("1+2=4");
    elist.addLast ("(1 <= 2) or  (-1 = (-2 * 4))");
    elist.addLast ("(1 <= true)");
    elist.addLast (" - true");
  }
}


