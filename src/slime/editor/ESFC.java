package slime.editor;

import slime.absynt.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;

/**
 * An object of this class is used to add some functionality to the sfc.
 * <br><br>
 * Status: nearly complete,  but i am not satisfied wit it <br>
 * Known bugs: - <br>
 * @author Andreas Niemann
 * @version $Id: ESFC.java,v 1.9 2002-06-14 11:21:03 swprakt Exp $
 */

public final class ESFC{

    /*
     * Some color definitions for the SFC
     */
    private static final Color INITIAL            = Color.darkGray;
    private static final Color SOURCE             = Color.blue;
    private static final Color TARGET             = Color.red;
    private static final Color STEP_NORMAL        = Color.black;
    private static final Color TRANSITION_NORMAL  = Color.gray;

    /*
     * The font used for the SFC
     */
    private static final Font DATA_FONT =
    new Font( "Sanserif", Font.PLAIN, 12 );
    
    private SFC        sfc;
    private LinkedList sourceSteps;
    private LinkedList targetSteps;
    private Hashtable  colors;
    private DrawBoard  drawBoard;
    private Object     selectedObject;
    private Object     markedObject;

    /*
     * 
     */
    private boolean    checked;
    private boolean    layouted;
    
    /*
     * Sets the colors for all steps and transitions for the given SFC. 
     */
    protected ESFC(SFC sfc) {
	this.sfc    = sfc;
	this.colors = this.createColorHashtable();
	this.sourceSteps = new LinkedList();
	this.targetSteps = new LinkedList();
    }

    /*
     * Returns the font used by this ESFC.
     */
    protected Font getFont() {
	return DATA_FONT;
    }

    /*
     * Returns the SFC of this ESFC.
     */
    protected SFC getSFC() {
	return this.sfc;
    }

    /*
     * Returns the selected Object of this SFC.
     */ 
    protected Object getSelectedObject() {
	return this.selectedObject;
    }

    /*
     * Selects the given objekt of this SFC. It does not check
     * wether the objekt belongs to the SFC or not.
     */
    protected void setSelectedObject(Object o) {
	this.selectedObject = o;
    }

    /*
     * Sets the DrawBoard for this ESFC.
     */
    protected void setDrawBoard(DrawBoard drawBoard) {
	this.drawBoard = drawBoard;
    }

    /*
     * Returns the DrawBoard used by this ESFC.
     */
    protected DrawBoard getDrawBoard() {
	return this.drawBoard;
    }

    /*
     * Marks this ESFC as checked or unchecked.
     */
    protected void setChecked(boolean flag) {
	this.checked = flag;
	if (!flag)
	    this.setLayouted(flag);
    }

    /*
     * Returns true if the SFC is cheched.
     */
    protected boolean isChecked() {
	return this.checked;
    }

    /*
     * Marks this ESFC as layouted or not layouted.
     */
    protected void setLayouted(boolean flag) {
	this.layouted = flag;
    }

    /*
     * Returns true if the SFC is layouted. 
     */
    protected boolean isLayouted() {
	return this.layouted;
    }

    /*
     * Returns a list steps which are marked as source.
     */
    protected LinkedList getSourceSteps() {
	return this.sourceSteps;
    }

    /*
     * Returns a list steps which are marked as target.
     */
    protected LinkedList getTargetSteps() {
	return this.targetSteps;
    }

    /*
     * Returns the number of steps which are marked as source.
     */
    protected int getNumberOfSourceSteps() {
	return this.sourceSteps.size();
    }

    /*
     * Returns the number of steps which are marked as target.
     */
    protected int getNumberOfTargetSteps() {
	return this.targetSteps.size();
    }

    /*
     * Removes highlightning from all steps in the sourcelist
     * and sets this sourcelist to an empty list.
     */
    protected void clearSourceSteps() {
	this.clearSteps(this.sourceSteps);
    }

    /*
     * Removes highlightning from all steps in the sourcelist
     * and sets this targetlist to an empty list.
     */
    protected void clearTargetSteps() {
	this.clearSteps(this.targetSteps);
    }


    /*
     * Removes highlightning from all steps in the given list
     * and sets this list to an empty list.
     */
    private void clearSteps(LinkedList listOfSteps) {
	for (int i=0; i<listOfSteps.size(); i++) {
	    Step step = (Step)listOfSteps.get(i);
	    this.colors.put(step, STEP_NORMAL);
	}
	listOfSteps = new LinkedList();
    }

    /*
     * Removes the given step from this SFC and all transitions
     * which are connected to this step. If neccessary it is
     * removed from the sourcelist or the targetlist.
     */
    protected void removeStep(Step step) {
	// Remove it from sourcelist if neccessary.
	this.removeSourceStep(step);

	// Remove it from targetlist if neccessary.
	this.removeTargetStep(step);

	// Remove all transitions which are connected with the step.
  	LinkedList transitionList = this.sfc.transs;
  	for (int i=0; i<transitionList.size(); i++) {
  	    Transition transition = (Transition)transitionList.get(i);
	    if (isStepConnectedWithTransition(step, transition))	
  		transitionList.remove(i--);
  	}

	// Remove it from this SFC.
  	this.sfc.steps.remove(step);

	// Remove color entry in hashtable.
	this.colors.remove(step);

	// Set istep to null if step was istep.
	if (step == this.sfc.istep)
	    this.sfc.istep = null;

	// Mark as uncheck.
	this.setChecked(false);
    }

    /*
     * Returns true if the given step is connected as source or target
     * to the given transition.
     */
    private boolean isStepConnectedWithTransition(Step step, Transition transition) {
	LinkedList source = transition.source;
	for (int s=0; s<source.size(); s++) 
	    if (step == (Step)source.get(s))
		return true;
	LinkedList target = transition.target;
	for (int t=0; t<target.size(); t++) 
	    if (step == (Step)target.get(t))
		return true;
	return false;
    }

    /*
     * Removes the given step from the sourcelist.
     */
    protected void removeSourceStep(Step step) {
	this.sourceSteps.remove(step);
	this.colors.put(step, STEP_NORMAL);
    }

    /*
     * Removes the given step from the targetlist.
     */
    protected void removeTargetStep(Step step) {
	this.targetSteps.remove(step);
	this.colors.put(step, STEP_NORMAL);
    }

    /* 
     * The given step will be added to the sourcelist if it is not
     * already in and will be removed optinally from the targetlist.
     * Otherwise it will be removed from the sourcelist.
     */ 
    protected void addSourceStep(Step step) {
	if (this.isSourceStep(step)) {
	    this.removeSourceStep(step);
	    return;
	}
	if (this.isTargetStep(step)) 
	    this.removeTargetStep(step);
	this.sourceSteps.add(step);
	this.colors.put(step, SOURCE);
    }

    /* 
     * The given step will be added to the targetlist if it is not
     * already in and will be removed optinally from the sourcelist.
     * Otherwise it will be removed from the targetlist.
     */ 
    protected void addTargetStep(Step step) {
	if (this.isTargetStep(step)) {
	    this.removeTargetStep(step);
	    return;
	}
	if (this.isSourceStep(step)) 
	    this.removeSourceStep(step);
	this.targetSteps.add(step);
	this.colors.put(step, TARGET);
    }

    /*
     * Returns true if the given step is a source step.
     */
    protected boolean isSourceStep(Step step) {
	return (this.sourceSteps.indexOf(step) != -1);
    }

    /*
     * Returns true if the given step is a target step.
     */
    protected boolean isTargetStep(Step step) {
	return (this.targetSteps.indexOf(step) != -1);
    }
    
    /*
     * Creates the color hashtable for this SFC.
     */
    private Hashtable createColorHashtable() {
	Hashtable colors = new Hashtable();
	for (int nr=0; nr<this.sfc.steps.size(); nr++) { 
	    Step step = (Step)(this.sfc.steps).get(nr);
	    if (step == this.sfc.istep)
		colors.put(step, INITIAL);
	    else
		colors.put(step, STEP_NORMAL);
	}
	for (int nr=0; nr<sfc.transs.size(); nr++) { 
	    Transition transition = (Transition)(this.sfc.transs).get(nr);
	    colors.put(transition, TRANSITION_NORMAL);
	}
	return colors;
    }

    /*
     * Adds a new step with the given name at the given position.
     */
    protected Step addStep(String name, int x, int y) {
	Step step = new Step(name);
	Position position = new Position((float)x, (float)y);
	step.pos = position;
	this.sfc.steps.add(step);
	colors.put(step, STEP_NORMAL);
	this.setChecked(false);
	return step;
    }

    /*
     * Adds a new transition with the given source and target steps and
     * the given expression as guard.
     */
    protected void addTransition(LinkedList source, Expr expr, LinkedList target) {
	Transition transition = new Transition(source, expr, target);
	System.out.println(source.size());
	this.sfc.transs.add(transition);
	colors.put(transition, TRANSITION_NORMAL);
	this.clearSourceSteps();
	this.clearTargetSteps();
	this.setChecked(false);
    }

    /*
     * Return the color hashtable used for this SFC.
     */
    protected Hashtable getColorHashtable() {
	return this.colors;
    }

    /*
     * Highlights the given object by setting the color value
     * in the color hashtable for this object.
     */
    protected void highlight(Object key, Color color) {
	if (this.colors.containsKey(key))
	    this.colors.put(key, color);
    }

    /*
     * Set the color value
     */
    protected void deHighlight(Object key) {
	if (key instanceof Transition)
	    this.highlight(key, TRANSITION_NORMAL);
	else if (key instanceof Step)
	    if (key == this.sfc.istep)
		this.highlight(key, INITIAL);
	    else
		this.highlight(key, STEP_NORMAL);
	else
	    this.highlight(key, STEP_NORMAL);
    }

    /*
     * Returns the width of the name-box of the given step. 
     */
    protected static int getWidth(Step step) {
	final int MIN_WIDTH = 30;
	JPanel panel = new JPanel();
	int    width = 8 + panel.getFontMetrics(DATA_FONT).stringWidth(step.name);
	return (MIN_WIDTH > width) ? MIN_WIDTH : width;
    }

    protected static int getWidthOfActionList(LinkedList actions) {
	int size = actions.size();
	if (size == 0)
	    return 0;
	int max = 0;
	
	JPanel panel = new JPanel();

	for (int i=0; i<size; i++) {
	    StepAction stepAction = (StepAction)actions.get(i);
	    int        length = panel.getFontMetrics(DATA_FONT).stringWidth(stepAction.a_name);

	    if (length > max)
		max = length;
	}
	return SFCPainter.ACTION_GAP+max+8;
    }

    /*
     * AB HIER FOLGT DIE NOTLOESUNG FUER DIE ERMMITTLUNG VON STRING-REPRAESENTATIONEN 
     * DER EINZELNEN SFC OBJECTE .... :-(
     *
     */
    
    protected String print(Absynt absynt) {
	if(absynt != null) {
	    if(absynt instanceof slime.absynt.Action)
		return output((slime.absynt.Action)absynt);
	    if(absynt instanceof StepAction)
		return output((StepAction)absynt);
	    if(absynt instanceof Step)
		return output((Step)absynt);
	    if(absynt instanceof Declaration)
		return output((Declaration)absynt);
	    if(absynt instanceof Skip)
		return output((Skip)absynt);
	    if(absynt instanceof Assign)
		return output((Assign)absynt);
	    if(absynt instanceof Stmt)
		return output((Stmt)absynt);
	    if(absynt instanceof Variable)
		return output((Variable)absynt);
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
	return "*";
    }
		       
    protected String output(slime.absynt.Action action){
	String s = action.a_name + ": ";
	for (Iterator i = action.sap.iterator(); i.hasNext();) {
	    s += print((Stmt)i.next());
	    if (i.hasNext())
		s += ", ";
	}
	return s;
    }
    
    protected String output(StepAction stepaction){
	String s = "[StepAction] ";
	s += print(stepaction.qualifier);
	s += print(stepaction.a_name);
	return s;
    }

    protected String output(ActionQualifier aqf){
	String s="[unknown] ";
	if(aqf instanceof Nqual)
	    s="[N] ";
	s = "[Actionqualifier] " + s + " ";
	return s;
    }

    protected String output(Step step) {
	String s = "[Step] " + step.name + " ";
	for (Iterator i = step.actions.iterator(); i.hasNext();)
	    s += print((StepAction)i.next());
	return s;
    }

    protected String output(Declaration dec) {
	String s = "";
	s += print(dec.type) + " ";
	s += print(dec.var) + ":=";
        s += print(dec.val);
	return s;
    }
    
    protected String output(Skip skip){
	return "[Skip] ";
    }
    
    protected String output(Stmt stmt){
	String s = "";
	return s;
    }

    protected String output(Variable variable){
	String s = variable.name;
	//s += print(variable.type);
	return s;
    }

    protected String output(Assign assign){
	String s = "";
	s += print(assign.var)+":=";
	s += print(assign.val);
	return s;
    }

    protected String output(B_expr bexpr){
	String s = "";
	s += print(bexpr.left_expr);
	s += print(bexpr.op);
	s += print(bexpr.right_expr);
	return s;
    }

    protected String output(Expr expr){
	String s = "";
	if(expr instanceof B_expr)
	    return output((B_expr)expr);
	if(expr instanceof U_expr)
	    return output((U_expr)expr);
	if(expr instanceof Constval)
	    return output((Constval)expr);
	if(expr instanceof Variable)
	    return output((Variable)expr);
	return s;
    }    
    
    protected String output(U_expr uexpr){
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
	    string ="+";
	    break;
	case 1:
	    string ="-";
	    break;
	case 2:
	    string ="*";
	    break;
	case 3:
	    string ="/";
	    break;
	case 4:
	    string ="&";
	    break;
	case 5:
	    string ="|";
	    break;
	case 6:
	    string ="!";
	    break;
	case 7:
	    string ="=";
	    break;
	case 8:
	    string ="<";
	    break;
	case 9:
	    string =">";
	    break;
	case 10:
	    string ="<=";
	    break;
	case 11:
	    string ="=>";
	    break;
	case 12:
	    string ="<>";
	    break;
	default:
	    string ="NULL";
	    break;
	}
    	return string;
    }

    protected String output(Constval constval){
	String s = "";
        if(constval != null){
            s = constval.val.toString();
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
		string="[Bool] ";
	    if(type instanceof IntType)
		string="[Int] ";
	}	    
	return string;
    }    
}











































































































