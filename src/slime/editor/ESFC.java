package slime.editor;

import slime.absynt.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.lang.reflect.*;

/**
 * An object of this class is used to add some functionality to the sfc.
 * <br><br>
 * Status: nearly complete,  but i am not satisfied with it <br>
 * Known bugs: update does not remove unused variables <br>
 * @author Andreas Niemann
 * @version $Id: ESFC.java,v 1.12 2002-07-06 12:22:29 swprakt Exp $
 */

public final class ESFC{

    /**
     * Some color definitions for the SFC
     */
    private static final Color INITIAL            = Color.darkGray;
    private static final Color SOURCE             = Color.blue;
    private static final Color TARGET             = Color.red;
    private static final Color STEP_NORMAL        = Color.lightGray;
    private static final Color TRANSITION_NORMAL  = Color.gray;
    private static final Color ACTION_NORMAL      = Color.white;

    /**
     * The font used for the SFC
     */
    private static final Font DATA_FONT =
    new Font( "Sanserif", Font.PLAIN, 12 );
    
    private SFC        sfc;
    private LinkedList sourceSteps;
    private LinkedList targetSteps;
    private Hashtable  colors;
    private Hashtable  guardPositions;
    private DrawBoard  drawBoard;
    private Object     selectedObject;
    private Object     markedObject;

    /*
     * Some bools for stat of sfc.
     */
    private boolean    checked;
    private boolean    layouted;
    
    /**
     * Sets the colors for all steps and transitions for the given SFC. 
     */
    protected ESFC(SFC sfc) {
	this.sfc            = sfc;
	this.colors         = this.createColorHashtable();
	this.sourceSteps    = new LinkedList();
	this.targetSteps    = new LinkedList();
    }

    /**
     * Sets the guard positions using the transition as key and a
     * Position object as value.
     */ 
    protected void setGuardPositions(Hashtable guardPositions) {
	this.guardPositions = guardPositions;
    }

    /**
     * Returns the guard positions represented by a hashtable.
     */ 
    protected Hashtable getGuardPositions() {
	if (this.guardPositions == null)
	    return new Hashtable();
	else 
	    return this.guardPositions;
    }

    /**
     * Returns the font used by this ESFC.
     */
    protected Font getFont() {
	return DATA_FONT;
    }

    /**
     * Returns the SFC of this ESFC.
     */
    protected SFC getSFC() {
	return this.sfc;
    }

    /**
     * Returns the selected Object of this SFC.
     */ 
    protected Object getSelectedObject() {
	return this.selectedObject;
    }

    /**
     * Selects the given objekt of this SFC. It does not check
     * wether the objekt belongs to the SFC or not.
     */
    protected void setSelectedObject(Object o) {
	this.selectedObject = o;
    }

    /**
     * Sets the DrawBoard for this ESFC.
     */
    protected void setDrawBoard(DrawBoard drawBoard) {
	this.drawBoard = drawBoard;
    }

    /**
     * Returns the DrawBoard used by this ESFC.
     */
    protected DrawBoard getDrawBoard() {
	return this.drawBoard;
    }

    /**
     * Marks this ESFC as checked or unchecked.
     */
    protected void setChecked(boolean flag) {
	this.checked = flag;
	if (!flag) 
	    this.setLayouted(flag);
    }

    /**
     * Returns true if the SFC is cheched.
     */
    protected boolean isChecked() {
	return this.checked;
    }

    /**
     * Marks this ESFC as layouted or not layouted.
     */
    protected void setLayouted(boolean flag) {
	this.layouted = flag;
    }

    /**
     * Returns true if the SFC is layouted. 
     */
    protected boolean isLayouted() {
	return this.layouted;
    }

    /**
     * Returns the i-th step of the step list.
     */
    protected Step getStep(int i) {
	LinkedList steps = this.sfc.steps;
	
	if ((i < 0) || (i >= steps.size())) return null;

	return (Step)steps.get(i);
    }

    /**
     * Returns the positon of the i-th step in the step list.
     */
    protected Position getPosition(int i) {
	Step step = this.getStep(i);
	
	if (step == null) return null;

	return step.pos;
    }

    /**
     * Returns the i-th transition of the transition list.
     */
    protected Transition getTransition(int i) {
	LinkedList transitions = this.sfc.transs;

	if ((i < 0) || (i >= transitions.size())) return null;
	
	return (Transition)transitions.get(i);
    }

    /**
     * Returns the i-th action of the action list.
     */
    protected slime.absynt.Action getAction(int i) {
	LinkedList actions = this.sfc.actions;

	if ((i < 0) || (i >= actions.size())) return null;
	
	return (slime.absynt.Action)actions.get(i);
    }

    /**
     * Returns the action with the given name.
     */
    protected slime.absynt.Action getAction(String name) {
	LinkedList actions = this.sfc.actions;

	for (int i=0; i<actions.size(); i++)
	    if (((slime.absynt.Action)(actions.get(i))).a_name.equals(name))
		return this.getAction(i);
	
	return null;
    }

    /**
     * Invoces the given method on the given object for every step as
     * the first argument together with the other given arguments.
     */
    protected void forAllSteps(Object o, String method, Object[] args) {
	Object[] newArgs = this.getNewArgs(args);
	Method   m       = this.getMethod(o, method);

	for (int i=0; i<this.sfc.steps.size(); i++) {
	    newArgs[0] = this.getStep(i);
	    this.forOne(o, m, newArgs);
	}
    }

    /**
     * Invoces the given method on the given object for every position as
     * the first argument together with the other given arguments.
     */
    protected void forAllPositions(Object o, String method, Object[] args) {
	Object[] newArgs = this.getNewArgs(args);
	Method   m       = this.getMethod(o, method);

	for (int i=0; i<this.sfc.steps.size(); i++) {
	    newArgs[0] = this.getPosition(i);
	    this.forOne(o, m, newArgs);
	}
    }

    /**
     * Invoces the given method on the given object for every transition as
     * the first argument together with the other given arguments.
     */
    protected void forAllTransitions(Object o, String method, Object[] args) {
	Object[] newArgs = this.getNewArgs(args);
	Method   m       = this.getMethod(o, method);

	for (int i=0; i<this.sfc.transs.size(); i++) {
	    newArgs[0] = this.getTransition(i);
	    this.forOne(o, m, newArgs);
	}
    }

    /**
     * Invoces the given method on the given object for every action as
     * the first argument together with the other given arguments.
     */
    protected void forAllActions(Object o, String method, Object[] args) {
	Object[] newArgs = this.getNewArgs(args);
	Method   m       = this.getMethod(o, method);

	for (int i=0; i<this.sfc.actions.size(); i++) {
	    newArgs[0] = this.getAction(i);
	    this.forOne(o, m, newArgs);
	}
    }

    private void forOne(Object o, Method m, Object[] args) {
	try {
	    m.invoke(o, args);
	} catch (IllegalAccessException iae) {
	    System.out.println("Reflection error");
	} catch (InvocationTargetException iae) {
	    System.out.println("Invocation error");
	}
    }

    private Object[] getNewArgs(Object[] args) {
	Object[] newArgs;

	if (args != null) 
	    newArgs = new Object[args.length+1];
	else 
	    newArgs = new Object[1];

	for (int i=1; i<newArgs.length; i++) 
	    newArgs[i] = args[i-1];

	return newArgs;
    }

    /**
     * Returns the first found method with the given name.
     */
    private static Method getMethod(Object o, String name) {
	Method[] methods = o.getClass().getDeclaredMethods();
	for (int i=0; i<methods.length; i++) 
	    if ((methods[i].getName()).equals(name))
		return methods[i];
	return null;
    }

    /**
     * Returns a list steps which are marked as source.
     */
    protected LinkedList getSourceSteps() {
	return this.sourceSteps;
    }

    /**
     * Returns a list steps which are marked as target.
     */
    protected LinkedList getTargetSteps() {
	return this.targetSteps;
    }

    /**
     * Returns the number of steps which are marked as source.
     */
    protected int getNumberOfSourceSteps() {
	return this.sourceSteps.size();
    }

    /**
     * Returns the number of steps which are marked as target.
     */
    protected int getNumberOfTargetSteps() {
	return this.targetSteps.size();
    }

    /**
     * Removes highlightning from all steps in the sourcelist
     * and sets this sourcelist to an empty list.
     */
    protected void clearSourceSteps() {
	this.clearSteps(this.sourceSteps);
	this.sourceSteps = new LinkedList();
    }

    /**
     * Removes highlightning from all steps in the sourcelist
     * and sets this targetlist to an empty list.
     */
    protected void clearTargetSteps() {
	this.clearSteps(this.targetSteps);
	this.targetSteps = new LinkedList();
    }


    /**
     * Removes highlightning from all steps in the given list
     * and sets this list to an empty list.
     */
    private void clearSteps(LinkedList listOfSteps) {
	for (int i=0; i<listOfSteps.size(); i++) {
	    Step step = (Step)listOfSteps.get(i);
	    this.colors.put(step, STEP_NORMAL);
	}
    }

    /**
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

    /**
     * Removes the given transition.
     */
    protected void removeTransition(Transition transition) {
	this.sfc.transs.remove(transition);
	this.setChecked(false);
    }

    /**
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

    /**
     * Removes the given step from the sourcelist.
     */
    protected void removeSourceStep(Step step) {
	this.sourceSteps.remove(step);
	this.colors.put(step, STEP_NORMAL);
    }

    /**
     * Removes the given step from the targetlist.
     */
    protected void removeTargetStep(Step step) {
	this.targetSteps.remove(step);
	this.colors.put(step, STEP_NORMAL);
    }

    /**
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

    /**
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

    /**
     * Returns true if the given step is a source step.
     */
    protected boolean isSourceStep(Step step) {
	return (this.sourceSteps.indexOf(step) != -1);
    }

    /**
     * Returns true if the given step is a target step.
     */
    protected boolean isTargetStep(Step step) {
	return (this.targetSteps.indexOf(step) != -1);
    }
    
    /**
     * Creates the color hashtable for this SFC.
     */
    private Hashtable createColorHashtable() {
	Hashtable colors = new Hashtable();
	Object[]  args   = new Object[1];

	args[0] = colors;
	this.forAllSteps(this, 
			 "setDefaultColorInHastableForStep", 
			 args);
	this.forAllTransitions(this, 
			       "setDefaultColorInHastableForTransition",
			       args);
	this.forAllActions(this, 
			   "setDefaultColorInHastableForAction",
			   args);
	return colors;
    }

    private void setDefaultColorInHastableForStep(Step step, 
						  Hashtable colors) {
	colors.put(step, STEP_NORMAL);
    }	

    private void setDefaultColorInHastableForTransition(Transition transition, 
							Hashtable colors) {
	colors.put(transition, TRANSITION_NORMAL);
    }

    private void setDefaultColorInHastableForAction(slime.absynt.Action action, 
						    Hashtable colors) {
	colors.put(action, ACTION_NORMAL);
    }
    
    /**
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

    /**
     * Adds a new transition with the given source and target steps and
     * the given expression as guard.
     */
    protected void addTransition(LinkedList source, Expr expr, LinkedList target) {
	Transition transition = new Transition(source, expr, target);
	this.sfc.transs.add(transition);
	colors.put(transition, TRANSITION_NORMAL);
	this.clearSourceSteps();
	this.clearTargetSteps();
	this.setChecked(false);
    }

    /**
     * Adds the given action to this sfc.
     */
    protected void addAction(slime.absynt.Action action) {
	this.sfc.actions.add(action);
	colors.put(action, ACTION_NORMAL);
	this.setChecked(false);
    }

    /**
     * Return the color hashtable used for this SFC.
     */
    protected Hashtable getColorHashtable() {
	return this.colors;
    }

    /**
     * Highlights the given object by setting the color value
     * in the color hashtable for this object.
     */
    protected void highlight(Object key, Color color) {
	if (this.colors.containsKey(key))
	    this.colors.put(key, color);
    }

    /**
     * Set the color value
     */
    protected void deHighlight(Object key) {
	if (!this.colors.containsKey(key))
	    return;
	if (key instanceof Transition)
	    this.highlight(key, TRANSITION_NORMAL);
	else if (key instanceof Step)
	    this.highlight(key, STEP_NORMAL);
	else
	    this.highlight(key, STEP_NORMAL);
    }

    /**
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

    /**
     * Returns true iff the given name is used for an action.
     */
    protected boolean actionNameExists(String name) {
	LinkedList actions = this.sfc.actions;
	for (int i=0; i<actions.size(); i++) {
	    slime.absynt.Action action = (slime.absynt.Action)actions.get(i);
	    if (name.equals(action.a_name))
		return true;
	}
	return false;
    }

    protected boolean equals(Declaration a, Declaration b) {
	// Oder nur Strings vergleichen ????
	boolean sameVar  = this.equals(a.var, b.var);
	boolean sameType = this.equals(a.type, b.type);
	boolean sameVal  = this.equals(a.val,b.val);
	return (sameVar && sameType && sameVal);
    }

    protected boolean equals(Variable a, Variable b) {
	// Oder vielleicht lieber wie bei Type???
	boolean sameName = (a.name).equals(b.name);
	boolean sameType  = this.equals(a.type, b.type);
	return (sameName && sameType);
    }

    protected boolean equals(Type a, Type b) {
	return print(a).equals(print(b));
    }

    protected boolean equals(Expr a, Expr b) {
	return output(a).equals(output(b));
    }
    
    /**
     * Do not look at runtime complexity :-(
     */
    protected void updateActions() {
	LinkedList actionList = this.sfc.actions;
	
	for(int i=0; i<actionList.size(); i++) 
	    if (!isActionInOneList(((slime.absynt.Action)actionList.get(i)).a_name)) {
		actionList.remove(i--);
	    }
    }
    
    private boolean isActionInOneList(String name) {
	LinkedList stepList = this.sfc.steps;

	for(int i=0; i<stepList.size(); i++)
	    if (isActionInThisList(((Step)stepList.get(i)).actions, name))
		return true;
	return false;
    }

    private boolean isActionInThisList(LinkedList list, String name) {
	for(int i=0; i<list.size(); i++) 
	    if (((StepAction)list.get(i)).a_name.equals(name))
		return true;
	return false;
    }

    /**
     * Do not look at runtime complexity :-(
     */
    private void updateDeclarations() {
	LinkedList declarationList = this.sfc.declist;
	
	for(int i=0; i<declarationList.size(); i++) {
	    Declaration declaration = (Declaration)declarationList.get(i);
	    if (!isVariableInActionList(declaration.var.name)) {
		declarationList.remove(i--);
	    }
	}
    }

    private boolean isVariableInActionList(String name) {
	LinkedList actionList = this.sfc.actions;

	for(int j=0; j<actionList.size(); j++)
	    if (isVariableInThisAction(
		(slime.absynt.Action)actionList.get(j), name))
		return true;
	return false;
    }

    private boolean isVariableInThisAction(slime.absynt.Action action, 
					   String name) {
	//for(int i=0; i<list.size(); i++) 
	//    if (((StepAction)list.get(i)).a_name.equals(name))
	//	return true;
	//return false;
	return true;
    }

    
    
    /*
     * AB HIER FOLGT DIE NOTLOESUNG FUER DIE ERMMITTLUNG VON STRING-REPRAESENTATIONEN 
     * DER EINZELNEN SFC OBJECTE .... :-(
     *
     */
    protected static String print(Absynt absynt) {
	if(absynt != null) {
	    if(absynt instanceof slime.absynt.Action)
		return output((slime.absynt.Action)absynt);
	    if(absynt instanceof Transition)
		return output((Transition)absynt);
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
	    if(absynt instanceof Type)
	        return output((Type)absynt);
	    //if(absynt instanceof BoolType)
		//    output((BoolType)absynt);
	    //if(absynt instanceof IntType)
		//    output((IntType)absynt);
	    /*if(absynt instanceof M_Type)
	      output((M_Type)absynt);*/
	}
	return "*";
    }

    protected static String output(Transition transition){
	return print(transition.guard);
    }
		       
    protected static String output(slime.absynt.Action action){
	String s = action.a_name + ": ";
	for (Iterator i = action.sap.iterator(); i.hasNext();) {
	    s += print((Stmt)i.next());
	    if (i.hasNext())
		s += "; ";
	}
	return s;
    }
    
    protected static String output(StepAction stepaction){
	String s = "[StepAction] ";
	s += print(stepaction.qualifier);
	s += print(stepaction.a_name);
	return s;
    }

    protected static String output(ActionQualifier aqf){
	String s="";
	if(aqf instanceof Nqual)
	    s="N ";
	return s;
    }

    protected static String output(Step step) {
	String s = "[Step] " + step.name + " ";
	for (Iterator i = step.actions.iterator(); i.hasNext();)
	    s += print((StepAction)i.next());
	return s;
    }

    protected static String output(Declaration dec) {
	String s = "";
	s += print(dec.type) + " ";
	s += print(dec.var) + ":= ";
        s += print(dec.val);
	return s;
    }
    
    protected static String output(Skip skip){
	return "skip";
    }
    
    protected static String output(Stmt stmt){
	String s = "";
	return s;
    }

    protected static String output(Variable variable){
	String s = variable.name+" ";
	if (variable.inputvar && variable.outputvar)
	    s = "INOUT "+s;
	else if (variable.inputvar)
	    s = "IN "+s;
	else if (variable.outputvar)
	    s = "OUT "+s;
	//s += print(variable.type);
	return s;
    }
  public boolean inputvar = false;
  public boolean outputvar = false;

    protected static String output(Assign assign){
	String s = "";
	s += print(assign.var)+":=";
	s += print(assign.val);
	return s;
    }

    protected static String output(B_expr bexpr){
	String s = "";
	s += print(bexpr.left_expr);
	s += print(bexpr.op);
	s += print(bexpr.right_expr);
	return s;
    }

    protected static String output(Expr expr){
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
    
    protected static String output(U_expr uexpr){
	String s = "";
	s += print(uexpr.op);
	s += print(uexpr.sub_expr);
	//prettyprint.print(uexpr.type);
	return s;
    }


    /* print wird hier ueberladen, da Op vom typ integer ist
     */
    private static String print(int op){
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
	    string ="and ";
	    break;
	case 5:
	    string ="or ";
	    break;
	case 6:
	    string ="not ";
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
	    string ="!= ";
	    break;
	default:
	    string ="NULL ";
	    break;
	}
    	return string;
    }

    protected static String output(Constval constval){
	String s = "";
        if(constval != null){
            s = constval.val.toString()+" ";
	}
	return s;
    }

    protected static String output(Type type){
	String string = new String();
	if(type !=null){
	    if(type instanceof BoolType)
		string="[bool]";
	    if(type instanceof IntType)
		string="[int]";
	}	    
	return string;
    }    

    private static String print(String string){
	return string;
    }
}











































































































