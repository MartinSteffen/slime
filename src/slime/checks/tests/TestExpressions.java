
package slime.checks.tests;
import java.io.*;

import java.util.*;
import slime.absynt.*;

/**
 * Test expression <break>
 * This is just a list of string expressions that can be iterated through for testing.
 * @author <a href="http://www.informatik.uni-kiel.de/~ms" target="_top">Martin Steffen</a> and Karsten Stahl.
 * @version $Id: TestExpressions.java,v 1.5 2002-07-10 14:32:51 swprakt Exp $
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
    elist.addLast ("1/2-3*x");
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


