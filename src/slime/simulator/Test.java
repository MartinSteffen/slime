package slime.simulator;
import  slime.absynt.SFC;
import  slime.absynt.Example;


/**
 * This class  is a simple test wrapper for the simulator for
 * the common example.
 * @author Initially provided by Immo Grabe.
 * @version $Id: Test.java,v 1.3 2002-07-19 11:47:26 swprakt Exp $
 */


public class Test{
  public Test(){
    Simulator simulator = new Simulator(Example.getExample1());
  }
}




