// --------------------------------------------------------------------
/**
 * Absfc2SFCConverter.java<br>
 * converts a given {@link slime.absfc.SFCabtree} to<br>
 * a required {@link slime.absynt.SFC}.<br>
 * @author initialy provided by Marco Wendel<br>
 * @version $Id: Absfc2SFCConverter.java,v 1.5 2002-06-28 20:30:49 swprakt Exp $<br>
*/
/*
 * Changelog:<br>
 * $Log: not supported by cvs2svn $
 * Revision 1.4  2002/06/28 15:04:55  swprakt<br>
 * Thanks for removing the entry ./sfcparser<br>
 * and forgetting ./editor/resources/images.<br>
 * Should still compile. (mwe)<br>
 * <br>
 * Revision 1.3  2002/06/27 14:30:56  swprakt<br>
 * tried to remove Error 19: "_public ugliness",<br>
 * now header comments consist of two parts, one<br>
 * for javadoc and one containing CVS Versio log.<br>
 * <br>
 * Revision 1.2  2002/06/26 06:33:03  swprakt<br>
 * Makefile geaendert nun mit fuer Slime gueltigem CLASSPATH "../..".<br>
 * Absfc2SFCConverter.java nun in slime.sfcparser-package.<br>
 * Parser nicht veraendert nur neu erzeugt. (mwe)<br>
 * <br>
 * Revision 1.1  2002/06/25 05:44:08  swprakt<br>
 * Phase 2 des Parsers:<br>
 * Soll eine Konvertierung von slime.absynt.absfc.SFCabtree<br>
 * nach slime.absynt.SFC durchfuehren koennen. Weitere<br>
 * Details werden falls moeglich dann am 26.06.2002<br>
 * besprochen. (mwe@informatik.uni-kiel.de)<br>
  */
// --------------------------------------------------------------------

package slime.sfcparser; // absynt.absfc;
import java.util.LinkedList;
import java.util.Iterator;
// import java.util.Stack;
import java.lang.String;
import java.io.FileOutputStream;

public class Absfc2SFCConverter {
    public final slime.absynt.Constval trueGuard =
       new slime.absynt.Constval( true );
    public final slime.absynt.Constval falseGuard =
       new slime.absynt.Constval( false );
    public int stepCounter;       /** for enumberating steps */
    public int transitionCounter; /** for enumberating transitions */
    public int statementCounter;  /** for enumberating statements */
    public int declarationCounter;/** for enumberating declarations */
    public int actionCounter;     /** for enumberating actions */
    public int processCounter;    /** for enumberating processes */
    private int runNr;            /** current state of tree processing - see notes */
    public boolean debugflag;     /** turn stdout debugoutput on/off */
    private boolean collectProcs; /** first run gathers all processes */
    private boolean curInProc;    /** determines whether currently in process or main */
    private slime.absynt.SFC theSFC = null;
    /** global LinkedList's for the SFC Program	-
	they stay the same for a single SFC */
    protected LinkedList myStepList;
    protected LinkedList myDeclarationList;
    protected LinkedList myTransitionList;
    protected LinkedList myActionList;
    protected LinkedList myProcessList;
    protected slime.absynt.Step myIStep;
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>Absfc2SFCConverter</b>
     * the constructor for creating a converter object, which transform <br>
     * a given {@link slime.absynt.absfc.SFCabtree}-tree to an equivalent <br>
     * {@link slime.absynt.SFC}-tree. The followgin code explains how to <br>
     * use this object: <br><code>
     * <br>
     * slime.absynt.SFC myWantedSFC = null;<br>
     * slime.absynt.absfc.SFCabtree theInputAbsfc =<br>
     *      someKindOfParserOutput( fileName );<br>
     * slime.absynt.absfc.Absfc2SFCConverter theConverter =<br>
     *  new slime.absynt.absfc.Absfc2SFCConverter( theInputAbsfc );<br>
     * myWantedSFC = theConverter.getSFC();<br>
     * <br>
     * </code>
     *
     * @author initially provided by Marco Wendel<br>
     * @version 1.1 should be running good<br>
     * @param slime.absynt.absfc.SFCabtree SFC2 the input meta-tree to convert<br>
     * @see slime.absynt.absfc.SFCabtree for the input format
     * @see slime.absynt.SFC for the output format
     * @see slime.sfcparser.ParserTest for an example
     **/
    public Absfc2SFCConverter( slime.absynt.absfc.SFCabtree absfcobj ) {
	stepCounter       = 0;  // for enumberating steps
	transitionCounter = 0;  // for enumberating transitions
	statementCounter  = 0;  // for enumberating statements
	actionCounter     = 0;  // for enumberating actions
	processCounter    = 0;  // for enumberating processes
	runNr             = 0;  // current state of tree processing - see notes
	debugflag         = true;  // turn stdout debugoutput on/off
	collectProcs      = true;  // first run gathers all processes
	curInProc         = false; // determines whether currently in process or main
	slime.absynt.SFC theSFC = new slime.absynt.SFC(); //null
	myStepList	  = new LinkedList();
	myDeclarationList = new LinkedList();
	myTransitionList  = new LinkedList();
	myActionList      = new LinkedList();
	myProcessList	  = new LinkedList();
	myIStep           = new slime.absynt.Step( "initial Step" );
	theSFC = convertTree( absfcobj );
    } // end constructor Absfc2SFCConverter( slime.absynt.absfc.SFCabtree )
 
    public slime.absynt.SFC getSFC() {
	return theSFC;
    } // end getSFC
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>convertTree</b>
     * runs 2 phases on the given SFCabtree:<br>
     * 1. gather all processes and write their steps/transitions - their <br>
     *    sfc information to the {@link slime.absynt.absfc.Process} data <br>
     *    {@link slime.absynt.absfc.Process.internalStepList}, <br>
     *    {@link slime.absynt.absfc.Process.internalTransitionList}, <br>
     *    {@link slime.absynt.absfc.Process.internalActionList}, <br>
     *    {@link slime.absynt.absfc.Process.internalDeclarationList} and <br>
     *    in a later version to {@link slime.absynt.absfc.Process.internalProcessList} <br>
     * 2. process the whole SFCabtree and link the process-sfc-parts to the
     *    whole generated program-sfc. <br>
     * it then return the generated {@link slime.absynt.SFC}.
     * @author initially provided by Marco Wendel<br>
     * @version 1.1 should be running good<br>
     * @param slime.absynt.absfc.SFCabtree srctree the input meta-tree to convert<br>
     * @return slime.absynt.SFC the generated slime.absynt.SFC tree.<br>
     **/
    protected slime.absynt.SFC convertTree( slime.absynt.absfc.SFCabtree srctree) {
	String sfcname = (String) srctree.sfcprogname;
	LinkedList s = (LinkedList) srctree.stmtlist;
	LinkedList p = (LinkedList) srctree.proclist;
	LinkedList v = (LinkedList) srctree.varlist;
	LinkedList d = (LinkedList) srctree.declist;
	int sC = srctree.stmtCnt = 0;
	int pC = srctree.procCnt = 0;
	int vC = srctree.varCnt  = 0;
	int dC = srctree.decCnt  = 0;
	/** link end of init SFC to begin of program */
	slime.absynt.Step beginProgramStep = initializeSFC();
	slime.absynt.Step nextStmtStartStep = beginProgramStep;
	/** COLLECT ALL PROCESS-INFORMATION */
	curInProc    = false;
	collectProcs = true; 
	/** iterate over all first-level-statements 
	for (Iterator i = s.iterator(); i.hasNext();) {
	    slime.absynt.absfc.Absfc currentStatement = 
		(slime.absynt.absfc.Absfc) i.next();
	    dbgOut( 2, "gathering processes - firstlevelstatement : " + currentStatement.nodetype ); 
	    nextStmtStartStep = processStatementList( currentStatement, nextStmtStartStep );
	} // end of for-i
	*/
	nextStmtStartStep = processStatementList( s, nextStmtStartStep );
	/** NOW DO THE REAL RUN */
	curInProc    = false;
	collectProcs = false; 
	/** reinitialize the SFC and all lists. this is  possible,
	    because all processinformation is held within the SFC2 **/
	beginProgramStep = initializeSFC(); 
	nextStmtStartStep = beginProgramStep;
	/** iterate over all first-level-statements 
	for (Iterator i = s.iterator(); i.hasNext();) {
	    sC++; // increase firstlevel statementCounter
	    slime.absynt.absfc.Absfc currentStatement = 
		(slime.absynt.absfc.Absfc) i.next();
	    dbgOut( 2, "firstlevelstatement nr. " + sC +
		       " : " + currentStatement.nodetype ); 
	    nextStmtStartStep = processStatementList( currentStatement, nextStmtStartStep );
	} // end of for-i
	*/
	nextStmtStartStep = processStatementList( s, nextStmtStartStep );
	/** last Step returned is the last step of the whole program */
	slime.absynt.Step endProgramStep = nextStmtStartStep;
	/** put everything together */
	theSFC = new slime.absynt.SFC( 
	    (slime.absynt.Step) myIStep,
	    (LinkedList) myStepList,
	    (LinkedList) myTransitionList,
	    (LinkedList) myActionList,
	    (LinkedList) myDeclarationList );
	return theSFC;
    } // end of convertTree
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>initializeSFC</b>
     * initializes the SFC with the first 2 steps and the <br>
     * first standard transition <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.1 should be running good<br>
     * @param <br>
     * @return slime.absynt.Step the latest step<br>
     **/
    protected slime.absynt.Step initializeSFC(){
	/** initialize counters */
	stepCounter       = 0;
	transitionCounter = 0;
	statementCounter  = 0;
	actionCounter     = 0;
	processCounter    = 0;
	runNr             = 0;
	/** initialize global lists */
	theSFC	          = new slime.absynt.SFC(); 
	myStepList	  = new LinkedList();
	myDeclarationList = new LinkedList();
	myTransitionList  = new LinkedList();
	myActionList      = new LinkedList();
	myProcessList	  = new LinkedList();
	/** build initial step */
	slime.absynt.Step sourceStep  = 
	    new slime.absynt.Step( (new Integer ( stepCounter )).toString() ); /** create Step nr. 0 */
	myStepList.add( sourceStep ); /** put initial Step nr. 0 into myStepList */
	stepCounter++; /** increase stepcounter */
	myIStep = sourceStep; /** set SFC.istep to step nr. 0 */
	LinkedList sourceStepList = new LinkedList(); /** create empty list for source steps */
	// sourceStepList.clear(); /** empty sourceStepList */
	sourceStepList.addFirst( sourceStep );/** add step nr. 0 to sourceStepList */
	slime.absynt.Step targetStep = 
	    new slime.absynt.Step( (new Integer ( stepCounter )).toString() ); /** create Step nr. 1 */
	myStepList.add( targetStep ); /** put initial Step nr. 1 into myStepList */
	stepCounter++;/** increase stepcounter */
	LinkedList targetStepList = new LinkedList(); /** create empty list for target steps */
	// targetStepList.clear(); /** empty targetStepList */
	targetStepList.addFirst( targetStep ); /** add step nr. 0 to sourceStepList */
	slime.absynt.Transition transition = 
	    new slime.absynt.Transition( 
		sourceStepList,
		trueGuard, 
		targetStepList 
		); /** create transition nr. 0 */
	transitionCounter++; /** increase transitionCounter */
	// myTransitionList.clear();/** clear transitionList */
	myTransitionList.add( transition ); /** add transition nr. 0 to myTransitionList */
	return targetStep; /** return the latest step as the actual step */
    } // end of initializeSFC
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * processStatementList - modifies global class attributes to <br>
     * convert the abstract sfc tree into a regular sfc<br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param java.util.LinkedList s - input statement list<br>
     * @param slime.absynt.Step startStep is the step to begin the first stmt with<br>
     * @return java.util.LinkedList - containing the counters<br>
     */
    protected slime.absynt.Step processStatementList( 
	java.util.LinkedList theStatementList, 
	slime.absynt.Step startStep 
	) {
	int stmtCnt = 0; /** Counter for statements */
	int procCnt = 0; /** Counter for processes */
	int variCnt = 0; /** Counter for variables */
	int declCnt = 0; /** Counter for declarations */
        slime.absynt.Step returnedStep = new slime.absynt.Step("a step that will be returned");
	slime.absynt.Step nextStmtStartStep = (slime.absynt.Step)startStep;
	for (Iterator listIterator = theStatementList.iterator(); listIterator.hasNext();) {
	    statementCounter++; // increase statementCounter
	    slime.absynt.absfc.Absfc currentStatement =
		(slime.absynt.absfc.Absfc) listIterator.next();
	    /** check if current statement is any statement at all */  
	    if ( currentStatement != null ) {
		/** check whether current Statement was already processed */
		if ( !(currentStatement.processed == true) || runNr < 4 )  {
		    /** check which kind of statement the currentstatement is */
                    if ( currentStatement instanceof slime.absynt.absfc.StmtIf) {
                        returnedStep = processIf( (Object)currentStatement , nextStmtStartStep );
                    } else if ( currentStatement instanceof slime.absynt.absfc.StmtIfElse) {
                        returnedStep = processIfElse( (Object)currentStatement , nextStmtStartStep );
		    } else if ( currentStatement instanceof slime.absynt.absfc.StmtWhile) {
                        returnedStep = processWhile( (Object)currentStatement , nextStmtStartStep );
		    } else if ( currentStatement instanceof slime.absynt.absfc.StmtRepeat) {
                        returnedStep = processRepeat( (Object)currentStatement , nextStmtStartStep );
		    } else if ( currentStatement instanceof slime.absynt.absfc.StmtAssign) {
                        returnedStep = processAssign( (Object)currentStatement , nextStmtStartStep );
                    } else if ( currentStatement instanceof slime.absynt.absfc.StmtDecl) {
                        returnedStep = processDecl( (Object)currentStatement , nextStmtStartStep );
                    } else if ( currentStatement instanceof slime.absynt.absfc.StmtSplit) {
                        returnedStep = processSplit( (Object)currentStatement , nextStmtStartStep );
		    } else if ( currentStatement instanceof slime.absynt.absfc.StmtJoin) {
                        returnedStep = processJoin( (Object)currentStatement , nextStmtStartStep );
		    } else if ( currentStatement instanceof slime.absynt.absfc.StmtInput) {
                        returnedStep = processInput( (Object)currentStatement , nextStmtStartStep );
                    } else if ( currentStatement instanceof slime.absynt.absfc.StmtOutput) {
                        returnedStep = processOutput( (Object)currentStatement , nextStmtStartStep );
		    } else if ( currentStatement instanceof slime.absynt.absfc.Process) {
                        returnedStep = processProcess( (Object)currentStatement , nextStmtStartStep );
		    } else if ( currentStatement instanceof slime.absynt.absfc.Comment) {
                        returnedStep = processComment( (Object)currentStatement , nextStmtStartStep );
		    } else if ( currentStatement instanceof slime.absynt.absfc.SFCabtree) {
                        returnedStep = processSubprogram( (Object)currentStatement , nextStmtStartStep );
		    } else { // unknown absfc statement type ...
			dbgOut( 2, "Unknown absfc statement type" );
		    } /** end of classifying stmtlist object */
                    /* the next Statement starts where the last ended 
		       clone is use just for security reasons :) and for slowing down */
		    nextStmtStartStep = (slime.absynt.Step)returnedStep;
		} // end if-currentStatement.processed==false
	    } // end if-currentStatement!=null
        } // end of for-i
        return returnedStep;
    } // end processStatementList
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /** 
     * <b> dbgOut </b><br>
     * outputs a debug string, output type depends on first parameter<br>
     * lineFlag: 1=normal output to stdout, 2=if debugflag turned on<br>
     *         >=3= do not output anything<br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param java.lang.int lineFlag - determines type of output to Stdout.<br>
     * @param java.lang.String outputString - the debug string to be displayed<br>
     * @return void<br>
     **/
    protected void dbgOut(int lineFlag, java.lang.String outputString) {
	boolean viewOutputOnStdOut = true;
	String outputfilename = "sfc2dbgout.txt";
	if (viewOutputOnStdOut) {
	    if (lineFlag==0) { /** simple printline */
		System.out.println( outputString );
	    } else if (lineFlag==1) {
		System.out.print( outputString ); /** simple print */
	    } else if ( (lineFlag==2) && (debugflag==true) ) {
		/** output even debuglines */
		System.out.println( outputString ); 
	    } // end of if-lineFlag
	} else {
	    // show nothing - insert your stringwriter here ...
	    try {
		FileOutputStream fos = new FileOutputStream( outputfilename );
		fos.write( outputString.getBytes() );
		fos.close();
	    } catch (Exception e) {
		dbgOut( 0, "Something went wrong with "+outputfilename+" !");
	    } // end try-catch
	} // end of if-viewOutputOnStdOut
    } // end of dbgOut
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>getStepByName</b>
     * searches for a Step named <i>stepname</i> in <br>
     * the steplist <i>myStepList</i> and return the<br>
     * corresponding step - if it exists.<br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param String stepName - Name of step to search for<br>
     * @return slime.absynt.Step - the step with the name given by parameter<br>
     **/
    protected slime.absynt.Step getStepByName(String stepName) {
	slime.absynt.Step aStep = null;
	for (Iterator i = myStepList.iterator(); i.hasNext();) {
	    slime.absynt.Step currentStep = 
		(slime.absynt.Step) i.next();
	    if ( stepName.equals( currentStep.name ) ) {
		aStep = currentStep;
	    } // end if-stepName.equals
	} // end of for-i
	if (aStep == null)
	    dbgOut( 0, "Not all steps occur in myStepList!" );
	return aStep;
    } // end of getStepByName
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>getVariableByName</b>
     * searches for a variable named <i>variableName</i> in <br>
     * the variable declarationlist <i>myDeclarationList</i> and <br>
     * returns the corresponding variable - if it exists.<br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param String variableName - Name of variable to search for<br>
     * @return slime.absynt.Variable - the variable with the name given by parameter<br>
     **/
    protected slime.absynt.Variable getVariableByName(String variableName) {
	slime.absynt.Variable aVariable = null;
	for (Iterator i = myDeclarationList.iterator(); i.hasNext();) {
	    slime.absynt.Declaration currentDeclaration =
		(slime.absynt.Declaration) i.next();
	    if ( variableName.equals( currentDeclaration.var.name ) ) {
		aVariable = currentDeclaration.var;
	    } // end if-variableName.equals
	} // end of for-i
	if (aVariable == null)
	    dbgOut( 0, "Not all variables occur in myDeclarationList!");
	return aVariable;
    } // end of getVariableByName
    // --------------------------------------------------------------------


    
    // --------------------------------------------------------------------
    /**
     * <b>getProcessByName</b><br>
     * searches for a process named <i>processName</i> in <br>
     * the processlist <i>myProcessList</i> and <br>
     * returns the corresponding process - if it exists.<br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param String processName - Name of process to search for<br>
     * @return slime.absynt.absfc.Process - the process with the name given by parameter<br>
     **/
    protected slime.absynt.absfc.Process getProcessByName(String processName) {
	slime.absynt.absfc.Process aProcess = null;
	for (Iterator i = myProcessList.iterator(); i.hasNext();) {
	    slime.absynt.absfc.Process currentProcess =
		(slime.absynt.absfc.Process) i.next();
	    if ( processName.equals( currentProcess.name ) ) {
		aProcess = currentProcess;
	    } // end if-processName.equals
	} // end of for-i
	if (aProcess == null)
	    System.err.println("Not all processes occur in myProcessList!");
	return aProcess;
    } // end of getProcessByName
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>processAssign</b><br>
     * processes a {@link slime.absynt.absfc.StmtAssign <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    protected slime.absynt.Step processAssign( Object curStmt, slime.absynt.Step lastStartStep ) {
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    dbgOut( 2 , "StmtAssign" );
	    slime.absynt.absfc.StmtAssign tmpNode =
		(slime.absynt.absfc.StmtAssign) curStmt;
	    slime.absynt.Variable   lvar             = tmpNode.var;
	    slime.absynt.Expr       lexp             = tmpNode.val;
	    slime.absynt.Assign     lass             = new slime.absynt.Assign( lvar, lexp );
	    slime.absynt.Action     lact             = null;
	    LinkedList              actionlist       = new LinkedList();
	    LinkedList              outeractionlist  = new LinkedList();
	    slime.absynt.Step       sourceStep       = null;            
	    LinkedList              sourceStepList   = new LinkedList();
	    slime.absynt.Step       targetStep       = null;            
	    LinkedList              targetStepList   = new LinkedList();
	    slime.absynt.Transition transition       = null;
	    /** add Assignment to actionlist */
	    actionlist.add( lass ); 
	    /** create new action */
	    lact = new slime.absynt.Action( (new Integer(actionCounter)).toString(), actionlist );
	    /** increase actioncounter */
	    actionCounter++;
	    outeractionlist.add( lact );
	    /** create new targetstep */
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString(), outeractionlist );
	    /** increase stepcounter */
	    myStepList.add( targetStep );	    
	    stepCounter++;
	    /** sourcestep is the end step from the previous statement */
	    sourceStep = (slime.absynt.Step)lastStartStep;
	    /** clear already empty sourcesteplist */
	    sourceStepList.clear();
	    /** add old end step to sourcesteplist */
	    sourceStepList.add( sourceStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( sourceStepList, trueGuard, targetStepList );
	    myTransitionList.add( transition );
	    transitionCounter++;
	    return targetStep;
	} else { /** currently collecting processes, 
		     but this statement is not within a process,
		     so we will simply skip its processing **/
	    return lastStartStep;
	} // end if
    } // end processAssign
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>newStep</b><br>
     * creates a new {@link slime.absynt.Step} <br>
     * and adds it to {@link slime.sfcparser.Absfc2SFCConverter.myStepList} <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @return slime.absynt.Step the new step.<br>
     **/
    public slime.absynt.Step newStep() {
	slime.absynt.Step out = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	myStepList.add( out );
	dbgOut(0,"New step nr. "+stepCounter+" created and added to myStepList");
	stepCounter++;
	return out;
    } // end of newStep
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>processDecl</b><br>
     * processes a {@link slime.absynt.absfc.StmtDecl} <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    protected slime.absynt.Step processDecl( Object curStmt, slime.absynt.Step lastStartStep ) {
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    dbgOut( 2 , "StmtDecl" );
	    slime.absynt.absfc.StmtDecl tmpNode =
		(slime.absynt.absfc.StmtDecl) curStmt;
	    slime.absynt.Type        ltyp            = tmpNode.type;
	    slime.absynt.Variable    lvar            = tmpNode.var;
	    slime.absynt.Expr        lexp            = tmpNode.expr;
	    slime.absynt.Declaration ldecl           = null; 
	    slime.absynt.Step        sourceStep      = null;            
	    LinkedList               sourceStepList  = new LinkedList();
	    slime.absynt.Step        targetStep      = null;            
	    LinkedList               targetStepList  = new LinkedList();
	    slime.absynt.Transition  transition      = null;
	    LinkedList               actionlist      = new LinkedList();
	    LinkedList               outeractionlist = new LinkedList();
	    slime.absynt.Action      lact            = null;
	    slime.absynt.Constval    lconsv          = new slime.absynt.Constval( true );
            /////////////////////////// neuer Constructor ... ?
	    ldecl = new slime.absynt.Declaration( lvar, ltyp, lconsv );
	    declarationCounter++; // increase declarationCounter
	    myDeclarationList.add( ldecl );
	    actionlist.add( ldecl ); // add Declaration to actionlist
	    lact = new slime.absynt.Action( (new Integer(actionCounter)).toString(), actionlist );
	    actionCounter++;
	    outeractionlist.add( lact );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString(), outeractionlist );
	    stepCounter++; // increase stepCounter
	    sourceStep = (slime.absynt.Step)lastStartStep;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition(
		sourceStepList, trueGuard, targetStepList ); // create transition
	    myTransitionList.add( transition );
	    transitionCounter++;
	    dbgOut( 2 , "StmtDecl - finished processing declaration" );
	    return targetStep;
	} else { /** currently collecting processes, 
		     but this statement is not within a process,
		     so we will simply skip its processing **/
	    return lastStartStep;
	} // end if
    } // end processDecl
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>processInput</b><br>
     * processes a {@link slime.absynt.absfc.StmtInput <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    protected slime.absynt.Step processInput( Object curStmt, slime.absynt.Step lastStartStep ) {
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    dbgOut( 2 , "StmtInput" );
	    slime.absynt.absfc.StmtInput tmpNode =
		(slime.absynt.absfc.StmtInput) curStmt;
	    slime.absynt.Variable   lvar           = tmpNode.var;
	    slime.absynt.Expr       lexp           = tmpNode.val;
	    slime.absynt.InputAction linp  = new slime.absynt.InputAction( "input", lvar, lexp ); /* HAS TO BE CREATED */
	    slime.absynt.Step       sourceStep     = null;            
	    LinkedList              sourceStepList = new LinkedList();
	    slime.absynt.Step       targetStep     = null;            
	    LinkedList              targetStepList = new LinkedList();
	    slime.absynt.Transition transition     = null;
	    LinkedList              actionlist     = new LinkedList();
	    LinkedList              outeractionlist= new LinkedList();
	    slime.absynt.Action     lact           = null;
	    actionlist.add( linp ); // add Input to actionlist
	    lact = new slime.absynt.Action( (new Integer(actionCounter)).toString(), actionlist );
	    actionCounter++;
	    outeractionlist.add( lact );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString(), outeractionlist );
	    stepCounter++;
	    sourceStep = (slime.absynt.Step)lastStartStep;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition(
		sourceStepList, trueGuard, targetStepList ); // create transition
	    myTransitionList.add( transition );
	    transitionCounter++;
	    dbgOut( 2 , "StmtInput - finished processing input" );
	    return targetStep;
	} else { /** currently collecting processes, 
		     but this statement is not within a process,
		     so we will simply skip its processing **/
	    return lastStartStep;
	} // end if
    } // end processInput
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>processOutput</b><br>
     * processes a {@link slime.absynt.absfc.StmtOutput <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.1 should be running good<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    protected slime.absynt.Step processOutput( Object curStmt, slime.absynt.Step lastStartStep ) {
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    dbgOut( 2 , "StmtOutput" );
	    slime.absynt.absfc.StmtOutput tmpNode =
		(slime.absynt.absfc.StmtOutput) curStmt;
	    slime.absynt.Variable   lvar           = tmpNode.var;
	    slime.absynt.Expr       lexp           = tmpNode.val;
	    slime.absynt.OutputAction loup = new slime.absynt.OutputAction( "output", lvar, lexp ); /* HAS TO BE CREATED */
	    slime.absynt.Step       sourceStep     = null;            
	    LinkedList              sourceStepList = new LinkedList();
	    slime.absynt.Step       targetStep     = null;            
	    LinkedList              targetStepList = new LinkedList();
	    slime.absynt.Transition transition     = null;
	    LinkedList              actionlist     = new LinkedList();
	    LinkedList              outeractionlist= new LinkedList();
	    slime.absynt.Action     lact           = null;
	    actionlist.add( loup ); // add output to actionlist
	    lact = new slime.absynt.Action( (new Integer(actionCounter)).toString(), actionlist );
	    actionCounter++;
	    outeractionlist.add( lact );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString(), outeractionlist );
	    stepCounter++;
	    sourceStep = (slime.absynt.Step)lastStartStep;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition(
		sourceStepList, trueGuard, targetStepList ); // create transition
	    myTransitionList.add( transition );
	    transitionCounter++;
	    dbgOut( 2 , "StmtOutput - finished processing output" );
	    return targetStep;
	} else { /** currently collecting processes, 
		     but this statement is not within a process,
		     so we will simply skip its processing **/
	    return lastStartStep;
	} // end if
    } // end processOutput
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>processComment</b><br>
     * processes a {@link slime.absynt.absfc.Comment <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    protected slime.absynt.Step processComment (Object curStmt, slime.absynt.Step lastStartStep) {
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    slime.absynt.absfc.Comment tmpNode = (slime.absynt.absfc.Comment) curStmt;
	    String lcomment = tmpNode.content;
	    dbgOut( 2 , "Comment: "+lcomment );
	    slime.absynt.Step       sourceStep       = lastStartStep;            
	    LinkedList              sourceStepList   = new LinkedList();
	    slime.absynt.Step       targetStep	     = null;
	    LinkedList              targetStepList   = new LinkedList();
	    slime.absynt.Transition transition       = null;
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    sourceStepList.add( sourceStep );
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( sourceStepList, trueGuard, targetStepList );
	    myTransitionList.add( transition );
	    transitionCounter++;
	    return targetStep; // optional do a SkipAction here
	} else { /** currently collecting processes, 
		     but this statement is not within a process,
		     so we will simply skip its processing **/
	    return lastStartStep;
	} // end if
    } // end processComment
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>processIf</b><br>
     * processes a {@link slime.absynt.absfc.StmtIf <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    protected slime.absynt.Step processIf (Object curStmt, slime.absynt.Step lastStartStep) {
	/** this makes it impossible to spawn new processes within an if-statement! */
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    dbgOut( 2 , "StmtIf" );
	    slime.absynt.absfc.StmtIf tmpNode        = (slime.absynt.absfc.StmtIf) curStmt;
	    LinkedList                ifcase         = tmpNode.stmtlist;
	    slime.absynt.Expr         ifguard        = tmpNode.expr;
	    slime.absynt.Step         sourceStep     = lastStartStep;            
	    LinkedList                sourceStepList = new LinkedList();
	    slime.absynt.Step         targetStep     = null;
	    LinkedList                targetStepList = new LinkedList();
	    slime.absynt.Transition   transition     = null;
	    slime.absynt.Step         ifStart        = null;
	    slime.absynt.Step         ifEnd          = null;
	    slime.absynt.Step         ifcaseStart    = null;
	    slime.absynt.Step         ifcaseEnd      = null;
	    /*
	      so aehnlich:
	      lastStartStep----trueTrans-----ifStartStep-----e-guard----ifcaseStartStep--.....-ifendstep---trueguard
	      \                                                            /
	      \-----!e-guard-----notifcaseStartStep-----------------<----/
	      |
	      return notifcaseStartStep
	    */
	    /** create ifStartStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)lastStartStep; // not required :)
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step ( (new Integer(stepCounter)).toString() );       
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition(
		(LinkedList)sourceStepList,
		trueGuard, 
		(LinkedList)targetStepList);
	    myTransitionList.add( transition );
	    transitionCounter++;
	    ifStart = (slime.absynt.Step)targetStep; /*** START OF IF-STATEMENT ***/
	    /** create notifcaseStartStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)ifStart;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( 
		(LinkedList)sourceStepList, 
		new slime.absynt.U_expr( slime.absynt.Expr.NEG, ifguard ), 
		(LinkedList)targetStepList );
	    myTransitionList.add( transition );
	    transitionCounter++;
	    ifEnd = (slime.absynt.Step)targetStep; /*** END OF IF-STATEMENT ***/
	    /** create ifcaseStartStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)ifStart;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( sourceStepList, ifguard, targetStepList );
	    myTransitionList.add( transition );
	    transitionCounter++;
	    ifcaseStart = (slime.absynt.Step)targetStep; /*** START OF IF-CASE ***/
	    ifcaseEnd   = processStatementList( ifcase, ifcaseStart ); /*** END OF IF-CASE ***/
	    /** create last transition, joining both cases */
	    sourceStep = (slime.absynt.Step)ifEnd;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    sourceStep = (slime.absynt.Step)ifcaseEnd;
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( sourceStepList, trueGuard, targetStepList );
	    myTransitionList.add( transition );
	    transitionCounter++;
	    dbgOut( 2 , "StmtIf - finished processing stmtlist" );
	    return targetStep;
	} else { /** currently collecting processes, 
		     but this statement is not within a process,
		     so we will simply skip its processing **/
	    return lastStartStep;
	} // end if
    } // end of processIf
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>processIfElse</b><br>
     * processes a {@link slime.absynt.absfc.StmtIfElse <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    protected slime.absynt.Step processIfElse( Object curStmt, slime.absynt.Step lastStartStep ) {
	/** this makes it impossible to spawn new processes within an if-else-statement! */
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    dbgOut( 2 , "StmtIfElse" );
	    slime.absynt.absfc.StmtIfElse tmpNode =
		(slime.absynt.absfc.StmtIfElse) curStmt;
	    LinkedList              ifcase           = tmpNode.ifstmtlist;
	    LinkedList              elsecase         = tmpNode.elsestmtlist;
	    slime.absynt.Expr       ifguard          = tmpNode.expr;
	    slime.absynt.Step       sourceStep       = null;            
	    LinkedList              sourceStepList   = new LinkedList();
	    slime.absynt.Step       targetStep       = null;            
	    LinkedList              targetStepList   = new LinkedList();
	    slime.absynt.Transition transition       = null;
	    slime.absynt.Step       ifStart          = null;
	    slime.absynt.Step       ifEnd            = null;
	    slime.absynt.Step       ifcaseStart      = null;
	    slime.absynt.Step       ifcaseEnd        = null;
	    slime.absynt.Step       elsecaseStart    = null;
	    slime.absynt.Step       elsecaseEnd      = null;
	    /* So aehnlich:
	       lastStartStep----trueTrans----->
	       ifelseStartStep-----e-guard----ifcaseStartStep--.....-ifendstep---trueguard
	       \                            
	       \-----!e-guard-----notifcaseStartStep--......-elseendstep---trueguard
	       von den beiden trueguards aus zum endifelsestep,
	       return endifelsestep
	    */
	    /** create ifStartStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)lastStartStep;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step( (new Integer( stepCounter )).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( 
		(LinkedList)sourceStepList, 
		trueGuard, 
		(LinkedList)targetStepList );
	    myTransitionList.add( transition );
	    transitionCounter++;
	    ifStart = (slime.absynt.Step)targetStep; /*** START OF IF-STATEMENT ***/
	    /** create elsecaseStartStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)ifStart;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( 
		(LinkedList)sourceStepList, 
		new slime.absynt.U_expr( slime.absynt.Expr.NEG, ifguard ), 
		(LinkedList)targetStepList );
	    myTransitionList.add( transition );
	    transitionCounter++;
	    elsecaseStart = (slime.absynt.Step)targetStep; /*** START OF ELSE-CASE ***/
	    /** create ifcaseStartStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)ifStart;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( 
		sourceStepList, 
		ifguard, 
		targetStepList );
	    myTransitionList.add( transition );
	    transitionCounter++;
	    ifcaseStart = (slime.absynt.Step)targetStep; /*** START OF IF-CASE ***/
	    ifcaseEnd   = processStatementList( ifcase, ifcaseStart ); /*** END OF IF-CASE ***/
	    dbgOut( 2 , "StmtIfElse - finished processing ifstmtlist" );
	    elsecaseEnd = processStatementList( elsecase, elsecaseStart ); /*** END OF ELSE-CASE ***/
	    dbgOut( 2 , "StmtIfElse - finished processing elsestmtlist" );
	    /** create last transition, joining both cases */
	    sourceStep = (slime.absynt.Step)ifcaseEnd;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    sourceStep = (slime.absynt.Step)elsecaseEnd;
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( 
		sourceStepList, 
		trueGuard, 
		targetStepList );
	    myTransitionList.add( transition );
	    transitionCounter++;
	    dbgOut( 2 , "StmtIf - finished processing stmtlist" );
	    return targetStep;
	} else { /** currently collecting processes, 
		     but this statement is not within a process,
		     so we will simply skip its processing **/
	    return lastStartStep;
	} // end if
    } // end processIfElse
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>processWhile</b>
     * processes a {@link slime.absynt.absfc.StmtWhile <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    protected slime.absynt.Step processWhile( Object curStmt, slime.absynt.Step lastStartStep ) {
	/** this makes it impossible to spawn new processes within a while-statement! */
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    dbgOut( 2 , "StmtWhile" );
	    slime.absynt.absfc.StmtWhile tmpNode =
		(slime.absynt.absfc.StmtWhile) curStmt;
	    slime.absynt.Step       sourceStep     = null;            
	    LinkedList              sourceStepList = new LinkedList();
	    slime.absynt.Step       targetStep     = null;            
	    LinkedList              targetStepList = new LinkedList();
	    slime.absynt.Transition transition     = null;
	    LinkedList              wstmtlist      = tmpNode.stmtlist;
	    slime.absynt.Expr       whileguard     = tmpNode.expr;
	    slime.absynt.Step       whileStart     = null;
	    slime.absynt.Step       whileEnd       = null;
	    slime.absynt.Step       loopStart      = null;
	    slime.absynt.Step       loopEnd        = null;
	    /*
	      so aehnlich:                     /------<----------------<-----------------------<-----\             
	      lastStartStep----trueTrans-----whileStart-----e-guard----loopStartp--.....-loopEnd---trueguard
	           \                                                  
	            \-----!e-guard-----whileEnd 
	      return whileEnd
	    */
	    /** create whileStartStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)lastStartStep;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( sourceStepList, trueGuard, targetStepList );
	    myTransitionList.add( transition );
	    transitionCounter++;
	    whileStart = (slime.absynt.Step)targetStep; /*** START OF WHILE-STATEMENT ***/
	    /** create whileEndStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)whileStart;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( 
		sourceStepList, 
		new slime.absynt.U_expr( slime.absynt.Expr.NEG, whileguard ), 
		targetStepList );
	    myTransitionList.add( transition );
	    transitionCounter++;
	    whileEnd = (slime.absynt.Step)targetStep; /*** END OF WHILE-STATEMENT ***/
	    /** create loopStartStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)whileStart;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( sourceStepList, whileguard, targetStepList );
	    myTransitionList.add( transition );
	    transitionCounter++;
	    loopStart = (slime.absynt.Step)targetStep; /*** START OF LOOP ***/
	    loopEnd   = processStatementList( wstmtlist, loopStart ); /*** END OF LOOP ***/
	    dbgOut( 2 , "StmtWhile - finished processing stmtlist" );
	    /** create last transition, from loop-End to whileStart */
	    sourceStep = (slime.absynt.Step)loopEnd;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( whileStart );
	    transition = new slime.absynt.Transition( sourceStepList, trueGuard, targetStepList );
	    myTransitionList.add( transition );
	    transitionCounter++;
	    dbgOut( 2 , "StmtWhile - finished processing while" );
	    return whileEnd;
	} else { /** currently collecting processes, 
		     but this statement is not within a process,
		     so we will simply skip its processing **/
	    return lastStartStep;
	} // end if
    } // end processWhile
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>processRepeat</b><br>
     * processes a {@link slime.absynt.absfc.StmtRepeat <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    protected slime.absynt.Step processRepeat( Object curStmt, slime.absynt.Step lastStartStep ) {
	/** this makes it impossible to spawn new processes within a repeat-statement! */
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    dbgOut( 2 , "StmtRepeat" );
	    slime.absynt.absfc.StmtRepeat tmpNode =
		(slime.absynt.absfc.StmtRepeat) curStmt;
	    LinkedList              rstmtlist      = tmpNode.stmtlist;
	    slime.absynt.Expr       untilguard     = tmpNode.expr;
	    slime.absynt.Step       sourceStep     = null;            
	    LinkedList              sourceStepList = new LinkedList();
	    slime.absynt.Step       targetStep     = null;            
	    LinkedList              targetStepList = new LinkedList();
	    slime.absynt.Transition transition     = null;
	    slime.absynt.Step       repeatStart    = null;
	    slime.absynt.Step       repeatEnd      = null;
	    slime.absynt.Step       loopEnd        = null;
	    /*
	      so aehnlich:                                             
	      lastStartStep----trueTrans
	                           /
	                /------<--/
	               /
	      repeatStart-----trueGuard---.....-until---e-guard---repeatEnd
	      \                             |
	       -----<-----------!e-guard----/
	    */
	    /** create repeatStartStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)lastStartStep;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step( (new Integer( stepCounter )).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( 
		(LinkedList)sourceStepList, 
		trueGuard, 
		(LinkedList)targetStepList );
	    myTransitionList.add( transition );
	    transitionCounter++;
	    repeatStart = (slime.absynt.Step)targetStep; /*** START OF REPEAT-STATEMENT ***/
	    loopEnd  = processStatementList( rstmtlist, repeatStart ); /*** END OF LOOP ***/
	    dbgOut( 2 , "StmtRepeat - finished processing stmtlist" );
	    /** create looping from loopEnd to repeatStart and corresponding transition */
	    sourceStep = (slime.absynt.Step)loopEnd;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    targetStep = (slime.absynt.Step)repeatStart;
	    stepCounter++;
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( 
		(LinkedList)sourceStepList, 
		new slime.absynt.U_expr( slime.absynt.Expr.NEG, untilguard ), 
		(LinkedList)targetStepList);
	    myTransitionList.add( transition );
	    transitionCounter++;
	    /** create transition leading out of loop */
	    sourceStep = (slime.absynt.Step)loopEnd;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( sourceStepList, untilguard, targetStepList );
	    myTransitionList.add( transition );
	    transitionCounter++;
	    repeatEnd = (slime.absynt.Step)targetStep; /*** END OF REPEAT-UNTIL ***/
	    dbgOut( 2 , "StmtRepeat - finished processing repeat-until" );
	    return repeatEnd;
	} else { /** currently collecting processes, 
		     but this statement is not within a process,
		     so we will simply skip its processing **/
	    return lastStartStep;
	} // end if
    } // end processRepeat
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>processSplit</b><br>
     * processes a {@link slime.absynt.absfc.StmtSplit <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    protected slime.absynt.Step processSplit( Object curStmt, slime.absynt.Step lastStartStep ) {
	
        dbgOut( 2 , "StmtSplit" );
        slime.absynt.absfc.StmtSplit tmpNode =
            (slime.absynt.absfc.StmtSplit) curStmt;
        LinkedList              plist            = tmpNode.proclist;
        int                     spawnedProcesses = plist.size();
        int                     curProc          = 0;
	// boolean              areSubprocesses  = tmpNode.isSubSplit;
	slime.absynt.Step       sourceStep	 = null;            
	LinkedList              sourceStepList   = new LinkedList();
	slime.absynt.Step       targetStep	 = null;            
	LinkedList              targetStepList   = new LinkedList();
	slime.absynt.Transition transition       = null;
	slime.absynt.Step       splitStart       = null;
	slime.absynt.Step       splitEnd         = null;
        slime.absynt.Step[]     procStart        = new slime.absynt.Step[ spawnedProcesses ];
        slime.absynt.Step[]     procEnd          = new slime.absynt.Step[ spawnedProcesses ];
	/** create initial transition from last statement to split statement */
	sourceStep = (slime.absynt.Step)lastStartStep;
	sourceStepList.clear();
	sourceStepList.add( sourceStep );
	targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	stepCounter++;
	myStepList.add( targetStep );
	targetStepList.clear();
	targetStepList.add( targetStep );
	transition = new slime.absynt.Transition( 
	    (LinkedList)sourceStepList, 
	    trueGuard, 
	    (LinkedList)targetStepList );
	transitionCounter++;
	myTransitionList.add( transition );
	splitStart = (slime.absynt.Step)targetStep;
	/** empty targetStepList to fill it afterwards with all the processStartSteps */
	targetStepList.clear();
	/** process all processes in plist */
        for (Iterator processIterator = plist.iterator(); processIterator.hasNext(); ) {
	    if (curProc < spawnedProcesses) { // do not allow Array-out-of-Bounds!
		procStart[ curProc ] = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
		stepCounter++;
		myStepList.add( (slime.absynt.Step)(procStart[curProc]) );
		procEnd[ curProc ] = processProcess( processIterator.next(), procStart[ curProc ] );
		targetStepList.add( procStart[ curProc ] );
		curProc++;
	    } // end if-curProc<spawnedProcesses
        } // end for-n
	/** create transition from splitStart to all processes */
	sourceStep = (slime.absynt.Step)splitStart;
	sourceStepList.clear();
	sourceStepList.add( sourceStep );
	transition = new slime.absynt.Transition( 
	    (LinkedList)sourceStepList, 
	    trueGuard, 
	    (LinkedList)targetStepList );
	transitionCounter++;
	myTransitionList.add( transition );
	/** create transition for normal program flow (main-process) */
	sourceStep = (slime.absynt.Step)splitStart;
	sourceStepList.clear();
	sourceStepList.add( sourceStep );
	targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	stepCounter++;
	myStepList.add( targetStep );
	targetStepList.clear();
	targetStepList.add( targetStep );
	transition = new slime.absynt.Transition( sourceStepList, trueGuard, targetStepList );
	transitionCounter++;
	myTransitionList.add( transition );
	splitEnd = (slime.absynt.Step)targetStep;
        dbgOut( 2 , "StmtSplit - finished processing processlist" );
	return splitEnd;
    } // end processSplit
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>processJoin</b><br>
     * processes a {@link slime.absynt.absfc.StmtJoin <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    protected slime.absynt.Step processJoin( Object curStmt, slime.absynt.Step lastStartStep ) {
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    dbgOut( 2 , "StmtJoin" );
	    slime.absynt.absfc.StmtJoin tmpNode =
		(slime.absynt.absfc.StmtJoin) curStmt;
	    LinkedList              pnlist           = tmpNode.proclist;
	    LinkedList              locproclist      = new LinkedList();
	    int                     nrofprocs        = pnlist.size();
	    int                     curProc          = 0;
	    slime.absynt.Step       sourceStep       = null;            
	    LinkedList              sourceStepList   = new LinkedList();
	    slime.absynt.Step       targetStep       = null;            
	    LinkedList              targetStepList   = new LinkedList();
	    slime.absynt.Transition transition       = null;
	    slime.absynt.Step       joinStart        = null;            
	    slime.absynt.Step       joinEnd          = null;            
	    slime.absynt.absfc.Process[] procArray   = new slime.absynt.absfc.Process[ nrofprocs ];
	    /** create joinStartStep */
	    sourceStep = (slime.absynt.Step)lastStartStep;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( 
		sourceStepList, 
		trueGuard, 
		targetStepList );
	    transitionCounter++;
	    myTransitionList.add( transition );
	    joinStart = (slime.absynt.Step)targetStep;
	    /** 
	     * 2 Moeglichkeiten:
	     * 1. Prozesse muessen einen kompletten durchlauf durchgefuehrt haben
	     * 2. Prozesse werden unterbrochen und koennen somit an beliebiger
	     *    Stelle/Step zurueckgefueht werden.
	     * In jedem Fall wird ein Flag boolean isRunning benoetigt.
	     * Im unguenstigsten Fall wuerde vor jedem ProcessStatement ueberprueft
	     * werden, ob der Prozess den folgenden Befehl ausfuehren darf oder nicht
	     **/
	    /** empty sourceStepList before filling it with the end_Steps of the processes */
	    sourceStepList.clear();
	    /** first of all there must have been the myProcessList generated ! */
	    for (Iterator j=pnlist.iterator(); j.hasNext(); ) {
		slime.absynt.absfc.Process lproc = getProcessByName( (String)j.next() );
		if (curProc < nrofprocs) {
		    procArray[ curProc] = (slime.absynt.absfc.Process)lproc;
		    curProc++;
		    sourceStepList.add( lproc.end_step ); // sind nat. keine echten prozesse
		/*
		  sourceStepList.addAll( lproc.internalStepList );
		  /** macht schon eher sinn, dann muss jedoch der guard so gewaehlt werden,
		      dass ein Uebergang von jedem Step eines jedes betroffenen Prozesses
		      zum ende des Join-Statements dann - und nur dann Moeglich ist,
		      wenn das Join-Statement aufgerufen wurde....
		*/
		} // end if-curProc<nrofprocs
	    } // end for-Iterator j
	    /** the connection to the joinStatement */
	    sourceStepList.add( joinStart );
	    /* hier muesste natuerlich nun etwas komplizierteres hin ...*/
	    /** link all ending Processes together with join */
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( sourceStepList, trueGuard, targetStepList );
	    /*
	      die join statements muessten interne variables bekommen ...
	      slime.absynt.Expr joiningExpr = new slime.absynt.B_expr( ...
	    */
	    transitionCounter++;
	    myTransitionList.add( transition );
	    /** create joinEndStep */
	    sourceStep = (slime.absynt.Step)targetStep;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    targetStep = new slime.absynt.Step( (new Integer(stepCounter)).toString() );
	    stepCounter++;
	    myStepList.add( targetStep );
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( sourceStepList, trueGuard, targetStepList );
	    transitionCounter++;
	    myTransitionList.add( transition );
	    joinEnd = (slime.absynt.Step)targetStep;
	    dbgOut( 2 , "StmtJoin - finished processing joining processes" );
	    return joinEnd;
	} else { /** currently collecting processes, 
		     but this statement is not within a process,
		     so we will simply skip its processing **/
	    return lastStartStep;
	} // end if
    } // end processJoin
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>processProcess</b><br>
     * processes a {@link slime.absynt.absfc.Process <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    protected slime.absynt.Step processProcess( Object curStmt, slime.absynt.Step lastStartStep ) {
	    dbgOut( 2 , "Process: " );
	    slime.absynt.absfc.Process tmpNode =
		(slime.absynt.absfc.Process) curStmt;
	    /** add current Process to global process list */
	    String                  pname            = tmpNode.name;
	    LinkedList              pstmtlist        = tmpNode.stmtlist;
	    // LinkedList              pdecllist        = tmpNode.decllist;
	    slime.absynt.Step       sourceStep       = null;            
	    LinkedList              sourceStepList   = new LinkedList();
	    slime.absynt.Step       targetStep	     = null;            
	    LinkedList              targetStepList   = new LinkedList();
	    slime.absynt.Transition transition       = null;
	    slime.absynt.Step       procStartStep    = null;
	    slime.absynt.Step       procEndStep      = null;
	if (collectProcs) {
	    myProcessList.add( tmpNode );
	    /** remember that from now on being within a process */
	    curInProc = true;
	    /** empty global myStepList, myTransitionList, myDeclarationList, myActionList
		they are filled with the inner sfc of the process */
	    myStepList.clear();
	    myTransitionList.clear();
	    myDeclarationList.clear();
	    myActionList.clear();
	    /** create the start Step of the new process */
	    java.lang.StringBuffer theStringBuffer = new java.lang.StringBuffer("");
	    theStringBuffer.append("Process-").append( (new Integer( processCounter ).toString() ));
	    theStringBuffer.append("-").append( ( new Integer( stepCounter ) ).toString() );
	    sourceStep = new slime.absynt.Step( theStringBuffer.toString() );
	    stepCounter++;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    myStepList.add( sourceStep );
	    procStartStep = (slime.absynt.Step)sourceStep;
	    tmpNode.start_step = (slime.absynt.Step)procStartStep;
	    procEndStep = processStatementList( pstmtlist, procStartStep );
	    tmpNode.end_step = (slime.absynt.Step)procEndStep;
	    myStepList.add( procEndStep );
	    dbgOut( 2 , "Process: - finished processing stmtlist" );
	    tmpNode.internalStepList        = (LinkedList)myStepList;
	    tmpNode.internalTransitionList  = (LinkedList)myTransitionList;
	    tmpNode.internalActionList      = (LinkedList)myActionList;
	    tmpNode.internalDeclarationList = (LinkedList)myDeclarationList;
	    /** this process was processed so we have to increase processCounter */
	    processCounter++;
	    /** propagate that current process was left */
	    curInProc = false;
	    return tmpNode.end_step;
	} else { // !collectProcs - get Steps and Transitions from first run ...
	    /** get the lastStartStep from the split statement */
	    sourceStep = (slime.absynt.Step)lastStartStep;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    /** get the processStartStep */
	    procStartStep = (slime.absynt.Step)tmpNode.start_step;
	    targetStep = (slime.absynt.Step)procStartStep;
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    transition = new slime.absynt.Transition( sourceStepList, trueGuard, targetStepList );
	    transitionCounter++;
	    myTransitionList.add( transition );
	    /** get all information from the first run through this process 
		and merge it with the global tree information */
	    myStepList.addAll( tmpNode.internalStepList );
	    myTransitionList.addAll( tmpNode.internalTransitionList );
	    myActionList.addAll( tmpNode.internalActionList );
	    myDeclarationList.addAll( tmpNode.internalDeclarationList );
	    /** get the processEndStep */
	    procEndStep = (slime.absynt.Step)tmpNode.end_step;
	    return procEndStep;
	} // end if
    } // end processProcess
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>processSubprogram</b><br>
     * processes a {@link slime.absynt.absfc.SFCabtree <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    protected slime.absynt.Step processSubprogram( Object curStmt, slime.absynt.Step lastStartStep ) {
        dbgOut( 2 , "SFCabtree:" );
        slime.absynt.absfc.SFCabtree tmpNode =
            (slime.absynt.absfc.SFCabtree) curStmt;
        LinkedList rstmtlist = tmpNode.stmtlist;
        LinkedList rproclist = tmpNode.proclist;
        LinkedList rvarilist = tmpNode.varlist;
        LinkedList rdecllist = tmpNode.declist;
        int rsCnt = tmpNode.stmtCnt;
        int rpCnt = tmpNode.procCnt;
        int rvCnt = tmpNode.varCnt;
        int rdCnt = tmpNode.decCnt;
        // theStack.push( currentStatement );
        // = processStatementList( rstmtlist );
        // dbgOut( 2 , "SFCabtree: - finished processing main-stmtlist of SFCabtree" );
        // removeThisFromStack();
	return new slime.absynt.Step( "404 Error - class not found" );
    } // end processSubprogram
    // --------------------------------------------------------------------


    
} // end of class Absfc2SFCConverter








