 // --------------------------------------------------------------------
/**
 * Absfc2SFCConverter.java<br>
 * converts a given {@link slime.absfc.SFCabtree} to<br>
 * a required {@link slime.absynt.SFC}.<br>
 * @author initialy provided by Marco Wendel<br>
 * @version $Id: Absfc2SFCConverter.java,v 1.11 2002-07-04 16:27:18 swprakt Exp $<br>
*/
/*
 * Changelog:<br>
 * $Log: not supported by cvs2svn $
 * Revision 1.10  2002/07/03 19:41:25  swprakt
 * Erste voll funktionsfaehige Absfc2SFCConverter-Version.
 * Liefert korrekte Step-Benennung und kompletten Baum.
 * Leider noch fehlend: InputAction und OutputAction-
 * Behandlung im PrettyPrint. (mwe)
 *
 * Revision 1.9  2002/07/03 16:50:08  swprakt
 * SFC generates and does not conflic with PrettyPrint.
 * Previous conflicts with PrettyPrint occured because
 * noone felt responsible for input & output. (mwe)
 *
 * Revision 1.8  2002/07/02 19:26:48  swprakt
 * Now supporting StepActions, but the PrettyPrint.java
 * fails at Action... Tomorrow we will have a chart
 * expaining why to use such a construct as ActionQualifier
 * and StepAction and Action and ...
 * (mwe)
 *
 * Revision 1.7  2002/07/02 15:23:49  swprakt
 * you may look at example.procs.sfc and the output
 * output.procs.txt for an idea what i am doing
 * currently. The naming scheme for steps and actions has to
 * be improved... (mwe)
 *
 * Revision 1.6  2002/07/02 12:29:47  swprakt
 * Phase 1 completed, correct parsing of complex
 * SFC programs into slime.absynt.absfc.SFCabtree.
 *
 * Phase 2 still in progress: today fixed some bug
 * with PrettyPrint4Absfc - partially the print(SFCabtree)
 * by simply outcommenting the lists for vars and decls.
 *
 * Left to do in phase 2:
 * verifying the correct(and complete!) creation of
 * the varlist, decllist for the slime.absynt.SFC.
 *
 * 02.07.2002 mwe@informatik.uni-kiel.de
 *
 * Revision 1.5  2002/06/28 20:30:49  swprakt
 * updated package information
 *
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
// import java.util.Stack; for someone who wants to enhance slime (supporting functions)
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
    // the following counter(used for some naming of objects
    // are about to be used in the next version...
    public int procStepCounter;       /** for enumberating steps in processes */
    public int procTransitionCounter; /** for enumberating transitions in processes */
    public int procStatementCounter;  /** for enumberating statements in processes */
    public int procDeclarationCounter;/** for enumberating declarations in processes */
    public int procActionCounter;     /** for enumberating actions in processes */
    public int procProcessCounter;    /** for enumberating processes in processes */
    public int processCounter;    /** for enumberating processes in processes */
    private int runNr;            /** current state of tree processing - see notes */
    public boolean debugflag;     /** turn stdout debugoutput on/off */
    private boolean collectProcs; /** first run gathers all processes */
    private boolean curInProc;    /** determines whether currently in process or main */
    private slime.absynt.SFC theSFC = null;
    /** global LinkedList's for the SFC Program	-
	they stay the same for a single SFC */
    private LinkedList myStepList;
    private LinkedList myDeclarationList;
    private LinkedList myTransitionList;
    private LinkedList myActionList;
    private LinkedList myProcessList;
    private slime.absynt.Step myIStep;
    /** switch whether output should be shown or redirected to outputfilename */
    public boolean viewOutputOnStdOut = true;
    /** used for output if viewOutputOnStdOut is false */
    public String outputfilename = "sfc2dbgout.txt";
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
     * </code><br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.1 should be running good<br>
     * @param slime.absynt.absfc.SFCabtree SFC2 the input meta-tree to convert<br>
     * @see slime.absynt.absfc.SFCabtree for the input format
     * @see slime.absynt.SFC for the output format
     * @see slime.sfcparser.ParserTest for an example
     **/
    public Absfc2SFCConverter( slime.absynt.absfc.SFCabtree absfcobj ) {
	stepCounter             = 0; // for enumberating steps
	transitionCounter       = 0; // for enumberating transitions
	statementCounter        = 0; // for enumberating statements
	actionCounter           = 0; // for enumberating actions
	processCounter          = 0; // for enumberating processes
	procStepCounter         = 0; // for enumberating steps within processes
	procTransitionCounter   = 0; // for enumberating transitions within processes
	procStatementCounter    = 0; // for enumberating statements within processes
	procDeclarationCounter  = 0; // for enumberating declarations within processes
	procActionCounter       = 0; // for enumberating actions within processes
	procProcessCounter      = 0; // for enumberating subprocesses within processes
	runNr                   = 0; // current state of tree processing - see notes
	debugflag               = true;  // turn stdout debugoutput on/off
	collectProcs            = true;  // first run gathers all processes
	curInProc               = false; // determines whether currently in process or main
	slime.absynt.SFC theSFC = new slime.absynt.SFC(); //null
	myStepList	        = new LinkedList(); // the global steplist
	myDeclarationList       = new LinkedList(); // the global declarationlist
	myTransitionList        = new LinkedList(); // the global transitionslist
	myActionList            = new LinkedList(); // the global actionlist
	myProcessList	        = new LinkedList(); // the global processlist
	myIStep                 = new slime.absynt.Step( "initial Step" ); // the initial step
	theSFC                  = convertTree( absfcobj ); // do the convertion
    } // end constructor Absfc2SFCConverter( slime.absynt.absfc.SFCabtree )
    // --------------------------------------------------------------------
 


    // --------------------------------------------------------------------
    /**
     * <b>getSFC</b>
     * returns the generated SFC-Object<br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.1 should be running good<br>
     * @see slime.absynt.absfc.SFCabtree for the input format
     * @see slime.absynt.SFC for the output format
     * @see slime.sfcparser.ParserTest for an example
     * @return slime.absynt.SFC the generated SFC.
     **/
    public slime.absynt.SFC getSFC() {
	return theSFC;
    } // end getSFC
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>cleanProcessCounters</b>
     * sets all process-internal counters to zero.<br>
     * The counters are mainly used for naming steps and actions,<br>
     * but you may also use them for creating statistics.<br> 
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     **/
    private void clearProcessCounters() {
	procStepCounter        = 0;
	procTransitionCounter  = 0;
	procStatementCounter   = 0;
	procDeclarationCounter = 0;
	procActionCounter      = 0;
	procProcessCounter     = 0;
    } // end cleanProcessCounters
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>convertTree</b>
     * runs 2 phases on the given SFCabtree:<br>
     * 1. gather all processes and write their steps/transitions - their <br>
     *    sfc information to the
     * @see slime.absynt.absfc.Process data <br>
     * @see slime.absynt.absfc.Process.internalStepList, <br>
     * @see slime.absynt.absfc.Process.internalTransitionList, <br>
     * @see slime.absynt.absfc.Process.internalActionList, <br>
     * @see slime.absynt.absfc.Process.internalDeclarationList and <br>
     *    in a later version to 
     * @see slime.absynt.absfc.Process.internalProcessList <br>
     * 2. process the whole SFCabtree and link the process-sfc-parts to the
     *    whole generated program-sfc. <br>
     * it then return the generated @see slime.absynt.SFC.
     * @author initially provided by Marco Wendel<br>
     * @version 1.1 should be running good<br>
     * @param slime.absynt.absfc.SFCabtree srctree the input meta-tree to convert<br>
     * @return slime.absynt.SFC the generated slime.absynt.SFC tree.<br>
     **/
    private slime.absynt.SFC convertTree( slime.absynt.absfc.SFCabtree srctree) {
	String sfcname = (String) srctree.sfcprogname;
	LinkedList s = (LinkedList) srctree.stmtlist;
	LinkedList p = (LinkedList) srctree.proclist;
	LinkedList v = (LinkedList) srctree.varlist;
	LinkedList d = (LinkedList) srctree.declist;
	int sC = srctree.stmtCnt = 0;
	int pC = srctree.procCnt = 0;
	int vC = srctree.varCnt  = 0;
	int dC = srctree.decCnt  = 0;
	/** clean the process counters and the internal myProcessList */
	clearProcessCounters();
	/** link end of init SFC to begin of program */
	slime.absynt.Step beginProgramStep = initializeSFC();
	slime.absynt.Step nextStmtStartStep = beginProgramStep;
	//*******************************************************************************
	/** COLLECT ALL PROCESS-INFORMATION */
	curInProc    = false;
	collectProcs = true; 
	nextStmtStartStep = processStatementList( s, nextStmtStartStep );
	//*******************************************************************************
	/** NOW DO THE REAL RUN */
	curInProc    = false;
	collectProcs = false; 
	/** reinitialize the SFC and all lists. this is  possible,
	    because all processinformation is held within the SFC2 **/
	myStepList.clear();
	myTransitionList.clear();
	myDeclarationList.clear();
	myActionList.clear();
	beginProgramStep = initializeSFC(); 
	nextStmtStartStep = beginProgramStep;
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
    private slime.absynt.Step initializeSFC(){
	/** initialize counters */
	stepCounter       = 0;
	transitionCounter = 0;
	statementCounter  = 0;
	actionCounter     = 0;
	/** initialize global lists */
	theSFC	          = new slime.absynt.SFC(); 
	myStepList	  = new LinkedList();
	myDeclarationList = new LinkedList();
	myTransitionList  = new LinkedList();
	myActionList      = new LinkedList();
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
	    new slime.absynt.Transition( sourceStepList, (slime.absynt.Expr)trueGuard, targetStepList 
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
    private slime.absynt.Step processStatementList( 
	java.util.LinkedList theStatementList, 
	slime.absynt.Step startStep 
	) {
	int stmtCnt = 0; /** Counter for statements */
	int procCnt = 0; /** Counter for processes */
	int variCnt = 0; /** Counter for variables */
	int declCnt = 0; /** Counter for declarations */
	int wloopCnt = 0; /** Counter for whileloops */
	int rloopCnt = 0; /** Counter for repeatloops */
	/** you might want to save counters in the absfc structures... */
	/** step returned by the process<Stmttype> methods for chaining
	    the steps and transitions together */
        slime.absynt.Step returnedStep = new slime.absynt.Step("a step that will be returned");
	/** step given as parameter for the process<Stmttype> methods for chaining
	    the steps and transistions together */
	slime.absynt.Step nextStmtStartStep = (slime.absynt.Step)startStep;
	for (Iterator listIterator = theStatementList.iterator(); listIterator.hasNext();) {
	    stmtCnt++;
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
		    } else if ( currentStatement instanceof slime.absynt.absfc.StmtFor) {
                        returnedStep = processFor( (Object)currentStatement , nextStmtStartStep );
		    } else if ( currentStatement instanceof slime.absynt.absfc.StmtRepeat) {
                        returnedStep = processRepeat( (Object)currentStatement , nextStmtStartStep );
		    } else if ( currentStatement instanceof slime.absynt.absfc.StmtAssign) {
			variCnt++;
                        returnedStep = processAssign( (Object)currentStatement , nextStmtStartStep );
                    } else if ( currentStatement instanceof slime.absynt.absfc.StmtDecl) {
			variCnt++;
			declCnt++;
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
			procCnt++;
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
    private void dbgOut(int lineFlag, java.lang.String outputString) {
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
	    // currently using the two global+public variables
	    // viewOutputOnStdOut & outputfilename
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
    private slime.absynt.Step getStepByName(String stepName) {
	slime.absynt.Step aStep = null;
	for (Iterator i = myStepList.iterator(); i.hasNext();) {
	    slime.absynt.Step currentStep = 
		(slime.absynt.Step) i.next();
	    if ( stepName.equals( (String)currentStep.name ) ) {
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
    private slime.absynt.Variable getVariableByName(String variableName) {
	slime.absynt.Variable aVariable = null;
	for (Iterator i = myDeclarationList.iterator(); i.hasNext();) {
	    slime.absynt.Declaration currentDeclaration =
		(slime.absynt.Declaration) i.next();
	    if ( variableName.equals( (String)currentDeclaration.var.name ) ) {
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
    private slime.absynt.absfc.Process getProcessByName(String processName) {
	slime.absynt.absfc.Process aProcess = null;
	for (Iterator i = myProcessList.iterator(); i.hasNext();) {
	    slime.absynt.absfc.Process currentProcess =
		(slime.absynt.absfc.Process) i.next();
	    if ( processName.equals( (String)currentProcess.name ) ) {
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
     * <b>newStep</b><br>
     * creates a new @see slime.absynt.Step <br>
     * and adds it to @see slime.sfcparser.Absfc2SFCConverter.myStepList <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @return slime.absynt.Step the new step.<br>
     **/
    private slime.absynt.Step newStep() {
	slime.absynt.Step out = null;
	if (curInProc && collectProcs) {
	    out = new slime.absynt.Step(
		"p-"+((new Integer(processCounter)).toString())+
		"-"+((new Integer(procStepCounter)).toString()) );
	    dbgOut(0,"New Process-step nr. p-" 
		   + processCounter + "-" 
		   + procStepCounter 
		   + " created and added to myStepList");
	    procStepCounter++;
	} else {
	    out = new slime.absynt.Step( 
		(new Integer(stepCounter)).toString() );
	    dbgOut(0,"New Program-step nr. " 
		   + stepCounter 
		   + " created and added to myStepList");
	    stepCounter++;
	} // end if-curInProc&&collectProcs
	myStepList.add( out );
	return out;
    } // end of newStep
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>newStep</b><br>
     * creates a new @see slime.absynt.Step using the provided stepactionlist<br>
     * and adds it to @see slime.sfcparser.Absfc2SFCConverter.myStepList <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param java.util.LinkedList stepactionlist contains the actions for the generated step.<br>
     * @return slime.absynt.Step the new step.<br>
     **/
    private slime.absynt.Step newStep(LinkedList stepactionlist) {
	slime.absynt.Step out = null;
	if (curInProc && collectProcs) {
	    out = new slime.absynt.Step(
		"p-"+((new Integer(processCounter)).toString())+
		"-"+((new Integer(procStepCounter)).toString()),
		stepactionlist );
	    dbgOut(0,"New Process-step nr. p-" 
		   + processCounter + "-" 
		   + procStepCounter 
		   + " created and added to myStepList");
	    procStepCounter++;
	} else {
	    out = new slime.absynt.Step( 
		(new Integer(stepCounter)).toString(),
		stepactionlist);
	    dbgOut(0,"New Program-step nr. " 
		   + stepCounter 
		   + " created and added to myStepList");
	    stepCounter++;
	} // end if-curInProc&&collectProcs
	myStepList.add( out );
	return out;
    } // end of newStep
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>newAction</b><br>
     * creates a new {@link slime.absynt.Step} <br>
     * and adds it to {@link slime.sfcparser.Absfc2SFCConverter.myStepList} <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param LinkedList inlist contains the statements to use for the returned action
     * @return slime.absynt.Action the new action.<br>
     **/
    private slime.absynt.Action newAction( LinkedList sap_inlist ) {
	slime.absynt.Action out = null;
	if (curInProc && collectProcs) {
	    out = new slime.absynt.Action( 
		"p-"+((new Integer(processCounter)).toString())+
		((new Integer(procActionCounter)).toString()), sap_inlist );
	    dbgOut(0,"New Process-Action nr. p-" 
		   + processCounter + "-"
		   + procActionCounter 
		   + " created and added to myActionList");
	    procActionCounter++;
	} else {
	    out = new slime.absynt.Action( 
		(new Integer(actionCounter)).toString(), sap_inlist );
	    dbgOut(0,"New Program-Action nr. " 
		   + actionCounter 
		   + " created and added to myActionList");
	    actionCounter++;
	} // end if-curInProc&&collectProcs
	myActionList.add( out );
	return out;
    } // end of newAction
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>newStepAction</b><br>
     * creates a new {@link slime.absynt.Step} <br>
     * and adds it to {@link slime.sfcparser.Absfc2SFCConverter.myStepList} <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param slime.absynt.Action actionToUse action from which the name is taken for the stepaction.<br>
     * @return slime.absynt.StepAction the new StepAction.<br>
     **/
    private slime.absynt.StepAction newStepAction( slime.absynt.Action actionToUse ) {
	slime.absynt.StepAction out = null;
	slime.absynt.ActionQualifier auaq = getActionQualifier(0); // new slime.absynt.Nqual();
	String nameToPutInStepAction = actionToUse.a_name;
	out = new slime.absynt.StepAction( auaq, nameToPutInStepAction );
	return out;
    } // end of newStepAction
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>getActionQualifier</b><br>
     * returns a @see slime.absynt.ActionQualifier <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param int qualifiertype determines the type of the ActionQualifier
     * @return slime.absynt.ActionQualifier depends on qualifiertype<br>
     **/
    private slime.absynt.ActionQualifier getActionQualifier( int qualifiertype ) {
	switch (qualifiertype) {
	    case 0 : return (new slime.absynt.Nqual()); 
	    case 1 : return (new slime.absynt.Nqual()); 
	    case 2 : return (new slime.absynt.Nqual()); 
	    case 3 : return (new slime.absynt.Nqual()); 
	    default : return (new slime.absynt.Nqual());
	} // end switch-qualifiertype
    } // end getActionQualifier
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>newTransition</b><br>
     * creates a new @see slime.absynt.Transition <br>
     * and adds it to @see slime.sfcparser.Absfc2SFCConverter.myTransitionList.<br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param LinkedList ssl <br>
     * @param slime.absynt.Expr tg <br>
     * @param LinkedList tsl <br>
     **/
    private void newTransition( LinkedList ssl, slime.absynt.Expr tg, LinkedList tsl ) {
	slime.absynt.Transition out = new slime.absynt.Transition( ssl, tg, tsl );
	transitionCounter++;
	myTransitionList.add( out );
	// return out;
    } // end of newTransitionn
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>newTransition</b><br>
     * creates a new @see slime.absynt.Transition <br>
     * and adds it to @see slime.sfcparser.Absfc2SFCConverter.myTransitionList.<br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param slime.absynt.Step ss <br>
     * @param slime.absynt.Expr tg <br>
     * @param slime.absynt.Step ts <br>
     * @return void<br>
     **/
    private void newTransition( slime.absynt.Step ss, slime.absynt.Expr tg, slime.absynt.Step ts ) {
	LinkedList              sourceStepList   = new LinkedList();
	LinkedList              targetStepList   = new LinkedList();
	sourceStepList.add( ss );
	targetStepList.add( ts );
	slime.absynt.Transition out = new slime.absynt.Transition( sourceStepList, tg, targetStepList );
	transitionCounter++;
	myTransitionList.add( out );
	// return out;
    } // end of newTransitionn
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
    private slime.absynt.Step processAssign( Object curStmt, slime.absynt.Step lastStartStep ) {
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
	    slime.absynt.Step       targetStep       = null;            
	    slime.absynt.StepAction stepaction       = null;
	    /** add Assignment to actionlist */
	    actionlist.add( lass ); 
	    /** create new action */
	    lact = newAction( actionlist );
	    stepaction = newStepAction( lact );
	    outeractionlist.add( stepaction );
	    /** create new targetstep */
	    targetStep = newStep( outeractionlist );
	    /** sourcestep is the end step from the previous statement */
	    sourceStep = (slime.absynt.Step)lastStartStep;
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
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
    private slime.absynt.Step processDecl( Object curStmt, slime.absynt.Step lastStartStep ) {
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    dbgOut( 2 , "StmtDecl" );
	    slime.absynt.absfc.StmtDecl tmpNode =
		(slime.absynt.absfc.StmtDecl) curStmt;
	    slime.absynt.Type        ltyp            = tmpNode.type;
	    slime.absynt.Variable    lvar            = tmpNode.var;
	    slime.absynt.Expr        lexp            = tmpNode.expr;
	    slime.absynt.Declaration ldecl           = null; 
	    slime.absynt.Step        sourceStep      = null;            
	    slime.absynt.Step        targetStep      = null;            
	    LinkedList               actionlist      = new LinkedList();
	    LinkedList               outeractionlist = new LinkedList();
	    slime.absynt.Action      lact            = null;
	    slime.absynt.Constval    lconsv          = tmpNode.val; // hmm ...
	    slime.absynt.StepAction  stepaction      = null;
	    ldecl = new slime.absynt.Declaration( lvar, ltyp, lconsv );
	    declarationCounter++; // increase declarationCounter
	    myDeclarationList.add( ldecl );
	    targetStep = newStep();
	    sourceStep = (slime.absynt.Step)lastStartStep;
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
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
    private slime.absynt.Step processInput( Object curStmt, slime.absynt.Step lastStartStep ) {
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    dbgOut( 2 , "StmtInput" );
	    slime.absynt.absfc.StmtInput tmpNode =
		(slime.absynt.absfc.StmtInput) curStmt;
	    slime.absynt.Variable   lvar           = tmpNode.var;
	    slime.absynt.Expr       lexp           = tmpNode.val;
	    slime.absynt.InputAction linp  = new slime.absynt.InputAction( "input", lvar, lexp ); /* HAS TO BE CREATED */
	    slime.absynt.Step       sourceStep     = null;            
	    slime.absynt.Step       targetStep     = null;            
	    LinkedList              actionlist     = new LinkedList();
	    LinkedList              outeractionlist= new LinkedList();
	    slime.absynt.Action     lact           = null;
	    slime.absynt.StepAction stepaction     = null;
	    actionlist.add( linp ); // add Input to actionlist
	    lact = newAction( actionlist );
	    stepaction = newStepAction( lact );
	    outeractionlist.add( stepaction );
	    targetStep = newStep( outeractionlist );
	    sourceStep = (slime.absynt.Step)lastStartStep;
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep ); // create transition
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
    private slime.absynt.Step processOutput( Object curStmt, slime.absynt.Step lastStartStep ) {
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    dbgOut( 2 , "StmtOutput" );
	    slime.absynt.absfc.StmtOutput tmpNode =
		(slime.absynt.absfc.StmtOutput) curStmt;
	    slime.absynt.Variable   lvar           = tmpNode.var;
	    slime.absynt.Expr       lexp           = tmpNode.val;
	    slime.absynt.OutputAction loup = new slime.absynt.OutputAction( "output", lvar, lexp ); /* HAS TO BE CREATED */
	    slime.absynt.Step       sourceStep     = null;            
	    slime.absynt.Step       targetStep     = null;            
	    LinkedList              actionlist     = new LinkedList();
	    LinkedList              outeractionlist= new LinkedList();
	    slime.absynt.Action     lact           = null;
	    slime.absynt.StepAction stepaction     = null;
	    actionlist.add( loup ); // add output to actionlist
	    lact = newAction( actionlist );
	    outeractionlist.add( lact );
	    stepaction = newStepAction( lact );
	    outeractionlist.add( stepaction );
	    targetStep = newStep( outeractionlist );
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
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
    private slime.absynt.Step processComment (Object curStmt, slime.absynt.Step lastStartStep) {
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    slime.absynt.absfc.Comment tmpNode = (slime.absynt.absfc.Comment) curStmt;
	    String lcomment = tmpNode.content;
	    dbgOut( 2 , "Comment: "+lcomment );
	    slime.absynt.Step       sourceStep       = lastStartStep;            
	    slime.absynt.Step       targetStep	     = null;
	    targetStep = newStep();
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
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
    private slime.absynt.Step processIf (Object curStmt, slime.absynt.Step lastStartStep) {
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
	    targetStep = newStep();
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
	    ifStart = (slime.absynt.Step)targetStep; /*** START OF IF-STATEMENT ***/
	    /** create notifcaseStartStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)ifStart;
	    targetStep = newStep();
	    newTransition( sourceStep, 
			   (slime.absynt.Expr)(new slime.absynt.U_expr( slime.absynt.Expr.NEG, ifguard )), 
			   targetStep );
	    ifEnd = (slime.absynt.Step)targetStep; /*** END OF IF-STATEMENT ***/
	    /** create ifcaseStartStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)ifStart;
	    targetStep = newStep();
	    newTransition( sourceStep, ifguard, targetStep );
	    ifcaseStart = (slime.absynt.Step)targetStep; /*** START OF IF-CASE ***/
	    ifcaseEnd   = processStatementList( ifcase, ifcaseStart ); /*** END OF IF-CASE ***/
	    /** create last transition, joining both cases */
	    sourceStep = (slime.absynt.Step)ifEnd;
	    sourceStepList.clear();
	    sourceStepList.add( sourceStep );
	    sourceStep = (slime.absynt.Step)ifcaseEnd;
	    sourceStepList.add( sourceStep );
	    targetStep = newStep();
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    newTransition( sourceStepList, (slime.absynt.Expr)trueGuard, targetStepList );
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
    private slime.absynt.Step processIfElse( Object curStmt, slime.absynt.Step lastStartStep ) {
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
	    targetStep = newStep();
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
	    ifStart = (slime.absynt.Step)targetStep; /*** START OF IF-STATEMENT ***/
	    /** create elsecaseStartStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)ifStart;
	    targetStep = newStep();
	    newTransition( sourceStep, 
			   (slime.absynt.Expr)(new slime.absynt.U_expr( slime.absynt.Expr.NEG, ifguard )), 
			   targetStep );
	    elsecaseStart = (slime.absynt.Step)targetStep; /*** START OF ELSE-CASE ***/
	    /** create ifcaseStartStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)ifStart;
	    targetStep = newStep();
	    newTransition( sourceStep, ifguard, targetStep );
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
	    targetStep = newStep();
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    newTransition( sourceStepList, (slime.absynt.Expr)trueGuard, targetStepList );
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
     * processes a @see slime.absynt.absfc.StmtFor <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    private slime.absynt.Step processWhile( Object curStmt, slime.absynt.Step lastStartStep ) {
	/** this makes it impossible to spawn new processes within a while-statement! */
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    dbgOut( 2 , "StmtWhile" );
	    slime.absynt.absfc.StmtWhile tmpNode =
		(slime.absynt.absfc.StmtWhile) curStmt;
	    slime.absynt.Step       sourceStep     = null;            
	    slime.absynt.Step       targetStep     = null;            
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
	    targetStep = newStep();
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
	    whileStart = (slime.absynt.Step)targetStep; /*** START OF WHILE-STATEMENT ***/
	    /** create whileEndStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)whileStart;
	    targetStep = newStep();
	    newTransition( sourceStep, 
			   (slime.absynt.Expr)(new slime.absynt.U_expr( slime.absynt.Expr.NEG, whileguard )), 
			   targetStep );
	    whileEnd = (slime.absynt.Step)targetStep; /*** END OF WHILE-STATEMENT ***/
	    /** create loopStartStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)whileStart;
	    targetStep = newStep();
	    newTransition( sourceStep, whileguard, targetStep );
	    loopStart = (slime.absynt.Step)targetStep; /*** START OF LOOP ***/
	    loopEnd   = processStatementList( wstmtlist, loopStart ); /*** END OF LOOP ***/
	    dbgOut( 2 , "StmtWhile - finished processing stmtlist" );
	    /** create last transition, from loop-End to whileStart */
	    sourceStep = (slime.absynt.Step)loopEnd;
	    targetStep = newStep();
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
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
     * <b>processFor</b>
     * processes a @see slime.absynt.absfc.StmtFor <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    private slime.absynt.Step processFor( Object curStmt, slime.absynt.Step lastStartStep ) {
	/** this makes it impossible to spawn new processes within a while-statement! */
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    dbgOut( 2 , "StmtFor" );
	    slime.absynt.absfc.StmtFor tmpNode = (slime.absynt.absfc.StmtFor) curStmt;

	    LinkedList              fstmtlist      = tmpNode.stmtlist;
	    slime.absynt.Type       loopvartype    = tmpNode.vartype;
	    slime.absynt.Variable   loopvar        = tmpNode.var;
	    slime.absynt.Expr       forinitexpr    = tmpNode.expr1;
	    slime.absynt.Expr       forwhileguard  = tmpNode.expr2;
	    slime.absynt.absfc.Statement modifierstmt = tmpNode.stmt;
	    slime.absynt.Assign     lass             = new slime.absynt.Assign( loopvar, forinitexpr );
	    slime.absynt.Action     lact             = null;
	    slime.absynt.StepAction stepaction       = null;
	    LinkedList              actionlist       = new LinkedList();
	    LinkedList              outeractionlist  = new LinkedList();
	    slime.absynt.Step       sourceStep     = null;            
	    slime.absynt.Step       targetStep     = null;            
	    slime.absynt.Step       forStart       = null;
	    slime.absynt.Step       forEnd         = null;
	    slime.absynt.Step       forGuardStep   = null;
	    slime.absynt.Step       loopStart      = null;
	    slime.absynt.Step       loopEnd        = null;

	    /*
	      so aehnlich:                                         /----<------modifierstmt----<-----\             
	      lastStartStep----trueTrans-----forStart--initVar---forwhileguard----loopStart--.....-loopEnd
	                                                             \                                                  
	                                                              \-----!forwhileguard-----forEnd 
	      return forEnd
	    */
	    /** create forStartStep and corresponding transition */
	    sourceStep = (slime.absynt.Step)lastStartStep;
	    targetStep = newStep();
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
	    forStart = (slime.absynt.Step)targetStep; /*** START OF FOR-STATEMENT ***/
	    /** now do the INITIAL ASSIGNMENT */
	    sourceStep = forStart;
	    actionlist.add( lass ); 
	    lact = newAction( actionlist );
	    stepaction = newStepAction( lact );
	    outeractionlist.add( stepaction );
	    forGuardStep = newStep( outeractionlist );
	    targetStep = forGuardStep;
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
	    /** now test if for-guard is satisfied */
	    sourceStep = forGuardStep;
	    loopStart = newStep();
	    targetStep = loopStart; /** START OF LOOP */
	    newTransition( sourceStep, (slime.absynt.Expr)forwhileguard, targetStep );
	    loopEnd = processStatementList( fstmtlist, loopStart ); /*** END OF LOOP ***/
	    /** go back from end of loop to a new test of the expression */
	    /** but before doing so adjust variables by calling the modifier */
	    java.util.LinkedList modifierstatementlist = new LinkedList();
	    modifierstatementlist.add( modifierstmt );
	    sourceStep = processStatementList( modifierstatementlist, loopEnd );
	    targetStep = forGuardStep;
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep ); /** JUMP BACK */
	    /** jump away from For-loop, if for-guard is not satisfied */
	    sourceStep = forGuardStep;
	    targetStep = newStep();
	    newTransition( sourceStep, 
			   (slime.absynt.Expr)(new slime.absynt.U_expr( slime.absynt.Expr.NEG, forwhileguard )), 
			   targetStep );
	    forEnd = (slime.absynt.Step)targetStep; /*** END OF FOR-STATEMENT ***/
	    return forEnd;
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
    private slime.absynt.Step processRepeat( Object curStmt, slime.absynt.Step lastStartStep ) {
	/** this makes it impossible to spawn new processes within a repeat-statement! */
	if ( (curInProc && collectProcs) || (!collectProcs) ) {
	    dbgOut( 2 , "StmtRepeat" );
	    slime.absynt.absfc.StmtRepeat tmpNode =
		(slime.absynt.absfc.StmtRepeat) curStmt;
	    LinkedList              rstmtlist      = tmpNode.stmtlist;
	    slime.absynt.Expr       untilguard     = tmpNode.expr;
	    slime.absynt.Step       sourceStep     = null;            
	    slime.absynt.Step       targetStep     = null;            
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
	    targetStep = newStep();
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
	    repeatStart = (slime.absynt.Step)targetStep; /*** START OF REPEAT-STATEMENT ***/
	    loopEnd  = processStatementList( rstmtlist, repeatStart ); /*** END OF LOOP ***/
	    dbgOut( 2 , "StmtRepeat - finished processing stmtlist" );
	    /** create looping from loopEnd to repeatStart and corresponding transition */
	    sourceStep = (slime.absynt.Step)loopEnd;
	    targetStep = (slime.absynt.Step)repeatStart;
	    newTransition( sourceStep, 
			   (slime.absynt.Expr)(new slime.absynt.U_expr( slime.absynt.Expr.NEG, untilguard )), 
			   targetStep );
	    /** create transition leading out of loop */
	    sourceStep = (slime.absynt.Step)loopEnd;
	    targetStep = newStep();
	    newTransition( sourceStep, untilguard, targetStep );
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
    private slime.absynt.Step processSplit( Object curStmt, slime.absynt.Step lastStartStep ) {
	
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
	slime.absynt.Step       splitStart       = null;
	slime.absynt.Step       splitEnd         = null;
        slime.absynt.Step[]     procStart        = new slime.absynt.Step[ spawnedProcesses ];
        slime.absynt.Step[]     procEnd          = new slime.absynt.Step[ spawnedProcesses ];
	/** create initial transition from last statement to split statement */
	sourceStep = (slime.absynt.Step)lastStartStep;
	targetStep = newStep();
	newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
	splitStart = (slime.absynt.Step)targetStep;
	/** empty targetStepList to fill it afterwards with all the processStartSteps */
	targetStepList.clear();
	/** process all processes in plist */
        for (Iterator processIterator = plist.iterator(); processIterator.hasNext(); ) {
	    if (curProc < spawnedProcesses) { // do not allow Array-out-of-Bounds!
		procStart[ curProc ] = newStep();
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
	newTransition( sourceStepList, (slime.absynt.Expr)trueGuard, targetStepList );
	/** create transition for normal program flow (main-process) */
	sourceStep = (slime.absynt.Step)splitStart;
	targetStep = newStep();
	newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
	splitEnd = (slime.absynt.Step)targetStep;
        dbgOut( 2 , "StmtSplit - finished processing processlist" );
	return splitEnd;
    } // end processSplit
    // --------------------------------------------------------------------



    // --------------------------------------------------------------------
    /**
     * <b>processJoin</b><br>
     * processes a @see slime.absynt.absfc.StmtJoin <br>
     * and creates the needed steps and transitions and <br>
     * adds them to myStepList and myTransitionList. <br>
     * The latest step wihtin the flow is assumed to be myStep <br>
     * @author initially provided by Marco Wendel<br>
     * @version 1.0<br>
     * @param Object curStmt the current statement to be converted to SFC<br>
     * @param slime.absynt.Step lastStartStep is the step to begin this stmt with<br>
     * @return slime.absynt.Step the step after "executing" the current statement.<br>
     **/
    private slime.absynt.Step processJoin( Object curStmt, slime.absynt.Step lastStartStep ) {
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
	    slime.absynt.Step       joinStart        = null;            
	    slime.absynt.Step       joinEnd          = null;            
	    slime.absynt.absfc.Process[] procArray   = new slime.absynt.absfc.Process[ nrofprocs ];
	    /** create joinStartStep */
	    sourceStep = (slime.absynt.Step)lastStartStep;
	    targetStep = newStep();
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
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
		String curProc2JoinName = (String)j.next();
		dbgOut( 2, "Current process in joining-stmt: "+ curProc2JoinName );
		slime.absynt.absfc.Process lproc = getProcessByName( curProc2JoinName  );
		if (curProc < nrofprocs) {
		    procArray[ curProc ] = (slime.absynt.absfc.Process)lproc;
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
	    targetStep = newStep();
	    targetStepList.clear();
	    targetStepList.add( targetStep );
	    newTransition( sourceStepList, (slime.absynt.Expr)trueGuard, targetStepList );
	    /*
	      die join statements muessten interne variables bekommen ...
	      slime.absynt.Expr joiningExpr = new slime.absynt.B_expr( ...
	    */
	    /** create joinEndStep */
	    sourceStep = (slime.absynt.Step)targetStep;
	    targetStep = newStep();
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
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
    private slime.absynt.Step processProcess( Object curStmt, slime.absynt.Step lastStartStep ) {
	    dbgOut( 2 , "Process: "+processCounter );
	    slime.absynt.absfc.Process tmpNode =
		(slime.absynt.absfc.Process) curStmt;
	    /** add current Process to global process list */
	    String                  pname            = tmpNode.name;
	    LinkedList              pstmtlist        = tmpNode.stmtlist;
	    // LinkedList              pdecllist        = tmpNode.decllist; we decided not to work with scopes ...
	    slime.absynt.Step       sourceStep       = null;            
	    LinkedList              sourceStepList   = new LinkedList();
	    slime.absynt.Step       targetStep	     = null;            
	    LinkedList              targetStepList   = new LinkedList();
	    slime.absynt.Transition transition       = null;
	    slime.absynt.Step       procStartStep    = null;
	    slime.absynt.Step       procEndStep      = null;
	if (collectProcs) {
	    /** remember that from now on being within a process */
	    curInProc = true;
	    /** empty global myStepList, myTransitionList, myDeclarationList, myActionList
		they are filled with the inner sfc of the process */
	    myStepList.clear();
	    myTransitionList.clear();
	    myDeclarationList.clear();
	    myActionList.clear();
	    /** clear all process-dependend counters */
	    clearProcessCounters();
	    /** create the start Step of the new process */
	    java.lang.StringBuffer theStringBuffer = new java.lang.StringBuffer("");
	    theStringBuffer.append("Process-").append( (new Integer( processCounter ).toString() ));
	    theStringBuffer.append("-").append( ( new Integer( procStepCounter ) ).toString() );
	    sourceStep = newStep(); // new slime.absynt.Step( theStringBuffer.toString() );
	    procStartStep = (slime.absynt.Step)sourceStep;
	    tmpNode.start_step = (slime.absynt.Step)procStartStep;
	    procEndStep = processStatementList( pstmtlist, procStartStep );
	    tmpNode.end_step = (slime.absynt.Step)procEndStep;
	    // myStepList.add( procEndStep ); was already added in processStatementList ...
	    dbgOut( 2 , "Process: - finished processing stmtlist" );
	    tmpNode.internalStepList.addAll( myStepList );
	    tmpNode.internalTransitionList.addAll( myTransitionList );
	    tmpNode.internalActionList.addAll( myActionList );
	    tmpNode.internalDeclarationList.addAll( myDeclarationList );
	    /** add this process to the global processlist */
	    myProcessList.add( tmpNode );
	    /** this process was processed so we have to increase processCounter */
	    processCounter++;
	    /** propagate that current process was left */
	    curInProc = false;
	    return tmpNode.end_step;
	} else { // !collectProcs - get Steps and Transitions from first run ...
	    dbgOut(2, "Process -> not in collectProcs now... ");
	    /** get the lastStartStep from the split statement */
	    sourceStep = (slime.absynt.Step)lastStartStep;
	    /** get the processStartStep */
	    procStartStep = (slime.absynt.Step)tmpNode.start_step;
	    targetStep = (slime.absynt.Step)procStartStep;
	    newTransition( sourceStep, (slime.absynt.Expr)trueGuard, targetStep );
	    /** get all information from the first run through this process 
		and merge it with the global tree information */
	    myStepList.addAll(        tmpNode.internalStepList );
	    myTransitionList.addAll(  tmpNode.internalTransitionList );
	    myActionList.addAll(      tmpNode.internalActionList );
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
    private slime.absynt.Step processSubprogram( Object curStmt, slime.absynt.Step lastStartStep ) {
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




/*
  Lost crap:
  ----------

	// iterate over all first-level-statements 
	for (Iterator i = s.iterator(); i.hasNext();) {
	    sC++; // increase firstlevel statementCounter
	    slime.absynt.absfc.Absfc currentStatement = 
		(slime.absynt.absfc.Absfc) i.next();
	    dbgOut( 2, "firstlevelstatement nr. " + sC +
		       " : " + currentStatement.nodetype ); 
	    nextStmtStartStep = processStatementList( currentStatement, nextStmtStartStep );
	} // end of for-i
 */




