package editor;

import absynt.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * For the Slime project of the Fortgeschrittenen-Praktikum.
 * @author Andreas Niemann
 * @version $Id: ESFC.java,v 1.4 2002-06-07 14:36:26 swprakt Exp $
 */

final class ESFC{

    private static final Color INITIAL = Color.darkGray;
    private static final Color SOURCE  = Color.blue;
    private static final Color TARGET  = Color.red;
    private static final Color NORMAL  = Color.black;

    private SFC        sfc;
    private LinkedList sourceSteps;
    private LinkedList targetSteps;
    private Hashtable  colors;
    private DrawBoard  drawBoard;
    private Object     selectedObject;
    
    protected ESFC(SFC sfc) {
	this.sfc    = sfc;
	this.colors = this.createColorHashtable();
	this.sourceSteps = new LinkedList();
	this.targetSteps = new LinkedList();
    }

    protected SFC getSFC() {
	return this.sfc;
    }

    protected Object getSelectedObject() {
	return this.selectedObject;
    }

    protected void setSelectedObject(Object o) {
	this.selectedObject = o;
    }

    protected void setDrawBoard(DrawBoard drawBoard) {
	this.drawBoard = drawBoard;
    }

    protected DrawBoard getDrawBoard() {
	return this.drawBoard;
    }

    protected LinkedList getSourceSteps() {
	return this.sourceSteps;
    }

    protected LinkedList getTargetSteps() {
	return this.targetSteps;
    }

    protected int getNumberOfSourceSteps() {
	return this.sourceSteps.size();
    }

    protected int getNumberOfTargetSteps() {
	return this.targetSteps.size();
    }

    protected void clearSourceSteps() {
	for (int i=0; i<this.sourceSteps.size(); i++) {
	    Step step = (Step)this.sourceSteps.get(i);
	    this.colors.put(step, NORMAL);
	}
	this.sourceSteps = new LinkedList();
    }

    protected void clearTargetSteps() {
	for (int i=0; i<this.targetSteps.size(); i++) {
	    Step step = (Step)this.targetSteps.get(i);
	    this.colors.put(step, NORMAL);
	}
	this.targetSteps = new LinkedList();
    }

    protected void removeStep(Step step) {
	this.removeSourceStep(step);
	this.removeTargetStep(step);
  	LinkedList transitionList = this.sfc.transs;
  	for (int i=0; i<transitionList.size(); i++) {
  	    Transition transition = (Transition)transitionList.get(i);
  	    LinkedList source = transition.source;
  	    LinkedList target = transition.target;
  	    for (int s=0; s<source.size(); s++) {
  		Step sStep = (Step)source.get(s);
  		if (sStep == step)
  		    source.remove(s--);
  	    }
  	    for (int t=0; t<target.size(); t++) {
  		Step tStep = (Step)target.get(t);
  		if (tStep == step)
  		    target.remove(t--);
  	    }
  	    if ((source.size() == 0) || (target.size() == 0))
  		transitionList.remove(i--);
  	}
  	this.sfc.steps.remove(step);
	this.colors.remove(step);
    }

    protected void removeSourceStep(Step step) {
	this.sourceSteps.remove(step);
	this.colors.put(step, NORMAL);
    }

    protected void removeTargetStep(Step step) {
	this.targetSteps.remove(step);
	this.colors.put(step, NORMAL);
    }

    protected void addSourceStep(Step step) {
	this.sourceSteps.add(step);
	this.colors.put(step, SOURCE);
    }

    protected void addTargetStep(Step step) {
	this.targetSteps.add(step);
	this.colors.put(step, TARGET);
    }

    protected boolean isSourceStep(Step step) {
	return (this.sourceSteps.indexOf(step) != -1);
    }

    protected boolean isTargetStep(Step step) {
	return (this.targetSteps.indexOf(step) != -1);
    }
    
    private Hashtable createColorHashtable() {
	Hashtable colors = new Hashtable();
	for (int nr=0; nr<this.sfc.steps.size(); nr++) { 
	    Step step = (Step)(this.sfc.steps).get(nr);
	    if (step == this.sfc.istep)
		colors.put(step, INITIAL);
	    else
		colors.put(step, NORMAL);
	}
	for (int nr=0; nr<sfc.transs.size(); nr++) { 
	    Transition transition = (Transition)(this.sfc.transs).get(nr);
	    colors.put(transition, Color.black);
	}
	return colors;
    }

    protected void addStep(String name, int x, int y) {
	Step step = new Step(name);
	Position position = new Position((float)x, (float)y);
	step.pos = position;
	this.sfc.steps.add(step);
	colors.put(step, NORMAL);
    }

    protected void addTransition(LinkedList source, Expr expr, LinkedList target) {
	Transition transition = new Transition(source, expr, target);
	this.sfc.transs.add(transition);
	colors.put(transition, NORMAL);
	this.clearSourceSteps();
	this.clearTargetSteps();
    }

    protected Hashtable getColorHashtable() {
	return this.colors;
    }

    protected void highlight(Object key, Color color) {
	if (this.colors.containsKey(key))
	    this.colors.put(key, color);
    }

    protected void deHighlight(Object key) {
	this.highlight(key, NORMAL); //FIX ME: Inititial Step != NORMAL!!!!
    }

    public static int getWidth(Step step) {
	final int MIN_WIDTH = 30;
	final int WIDTH_PER_CHARACTER = 7;
	int lengthOfName = step.name.length();
	int width = WIDTH_PER_CHARACTER * lengthOfName + 8;
	return (MIN_WIDTH > width) ? MIN_WIDTH : width;
    }

    public static int getWidthOfActionList(LinkedList actions) {
	int size = actions.size();
	if (size == 0)
	    return 0;
	int max = 0;
	for (int i=0; i<size; i++) {
	    StepAction stepAction = (StepAction)actions.get(i);
	    int length = stepAction.a_name.length();
	    if (length > max)
		max = length;
	}
	return SFCPainter.ACTION_GAP+max*7+8;
    }

    public String print(Absynt absynt) {
	if(absynt != null) {
	    if(absynt instanceof absynt.Action)
		return output((absynt.Action)absynt);
	    if(absynt instanceof StepAction)
		return output((StepAction)absynt);
	    if(absynt instanceof Step)
		return output((Step)absynt);
	    if(absynt instanceof Declaration)
		return output((Declaration)absynt);
	    if(absynt instanceof Skip)
		return output((Skip)absynt);
	    if(absynt instanceof Stmt)
		return output((Stmt)absynt);
	    if(absynt instanceof Variable)
		return output((Variable)absynt);
	    if(absynt instanceof Assign)
		return output((Assign)absynt);
	    if(absynt instanceof B_expr)
		return output((B_expr)absynt);
	    if(absynt instanceof U_expr)
		return output((U_expr)absynt);
	    if(absynt instanceof Constval)
		return output((Constval)absynt);
	    //	if(absynt instanceof Type)
	    //  output((Type)absynt);
	    //if(absynt instanceof BoolType)
	    //    output((BoolType)absynt);
	    //if(absynt instanceof IntType)
	    //    output((IntType)absynt);
	    /*if(absynt instanceof M_Type)
	      output((M_Type)absynt);*/
	}
	return "";
    }
		       
    public String output(absynt.Action action){
	String s = action.a_name + ": ";
	for (Iterator i = action.sap.iterator(); i.hasNext();)
	    s += print((Stmt)i.next());
	return s;
    }
    
    public String output(StepAction stepaction){
	String s = "[StepAction] ";
	s += print(stepaction.qualifier);
	s += print(stepaction.a_name);
	return s;
    }

    public String output(ActionQualifier aqf){
	String s="[unknown] ";
	if(aqf instanceof Nqual)
	    s="[N] ";
	s = "[Actionqualifier] " + s + " ";
	return s;
    }

    public String output(Step step) {
	String s = "[Step] " + step.name + " ";
	for (Iterator i = step.actions.iterator(); i.hasNext();)
	    s += print((StepAction)i.next());
	return s;
    }

    public String output(Declaration dec) {
	String s = "";
	s += print(dec.type) + " ";
	s += print(dec.var) + ":=";
        s += print(dec.val);
	return s;
    }
    
    public String output(Skip skip){
	return "[Skip] ";
    }
    
    public String output(Stmt stmt){
	String s = "";
	if (stmt instanceof Assign) 
	    s = output((Assign)stmt);
	return s;
    }

    public String output(Variable variable){
	String s = variable.name;
	//s += print(variable.type);
	return s;
    }

    public String output(Assign assign){
	String s = "";
	s += print(assign.var)+":=";
	s += print(assign.val)+"; ";
	return s;
    }

    public String output(B_expr bexpr){
	String s = "";
	s += print(bexpr.left_expr);
	s += print(bexpr.op);
	s += print(bexpr.right_expr);
	return s;
    }

    public String output(Expr expr){
	String s = "";
	//if (expr instanceof Constval)
	    
	return s;
    }    
    
    public String output(U_expr uexpr){
	String s = "";
	s += print(uexpr.op);
	s += print(uexpr.sub_expr);
	//prettyprint.print(uexpr.type);
	return s;
    }


    /* print wird hier ueberladen, da Op vom typ integer ist
     */
    private String print(int op){
	String string;
	switch(op){
	case 0 :
	    string ="+ ";
	    break;
	case 1:
	    string ="- ";
	    break;
	case 2:
	    string ="* ";
	    break;
	case 3:
	    string ="/ ";
	    break;
	case 4:
	    string ="& ";
	    break;
	case 5:
	    string ="| ";
	    break;
	case 6:
	    string ="! ";
	    break;
	case 7:
	    string ="= ";
	    break;
	case 8:
	    string ="< ";
	    break;
	case 9:
	    string ="> ";
	    break;
	case 10:
	    string ="<= ";
	    break;
	case 11:
	    string ="=> ";
	    break;
	case 12:
	    string ="<> ";
	    break;
	default:
	    string ="NULL ";
	    break;
	}
    	return string;
    }

    public String output(Constval constval){
	String s = "";
        if(constval != null){
            s = constval.val.toString();
	    //prettyprint.print(constval.type);
	}
	return s;
    }


    private String print(String string){
	return string;
    }

    private String print(Type type){
	String string;
	string="[Type] ";
	if(type !=null){
	    if(type instanceof BoolType)
		string="[BoolType] ";
	    if(type instanceof IntType)
		string="[IntType] ";
	}	    
	return string;
    }    
}









