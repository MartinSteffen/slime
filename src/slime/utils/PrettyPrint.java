


package slime.utils;

import slime.absynt.*;
import java.io.PrintStream;
import java.util.*;


/**
 * Pretty printer for SFC's
 * @author Initially provided by Karsten Stahl.
 * @version $Id: PrettyPrint.java,v 1.10 2002-07-11 12:49:25 swprakt Exp $
 */


public class PrettyPrint {

    private int column;
    private int tab;
    private boolean steps_long = false;
    public static final int NORM_COLUMN = 0; 
    public static final int NORM_TAB = 4;


    public PrettyPrint() {
        this(NORM_COLUMN, NORM_TAB);
    }

    private PrettyPrint(int i, int j) {
        try {
            column = i >= 0 ? i - i % j : 0;
            tab = j >= 0 ? j : NORM_TAB;
            return;
        }
        catch(ArithmeticException _ex) {
            column = i - i % NORM_TAB;
        }
        tab = NORM_TAB;
    }

    private PrettyPrint(int i, int j, boolean _steps_long) {
	steps_long = _steps_long;
        try {
            column = i >= 0 ? i - i % j : 0;
            tab = j >= 0 ? j : NORM_TAB;
            return;
        }
        catch(ArithmeticException _ex) {
            column = i - i % NORM_TAB;
        }
        tab = NORM_TAB;
    }

// ---------------------------------------------------------------------------

  /** public print method for pretty printing.
   *  It's overloaded to take care for the various cases.
   */

  public void print(Absynt absynt) {
    if(absynt != null) {
	    if(absynt instanceof SFC)
		output((SFC)absynt);
	    if(absynt instanceof Transition)
		output((Transition)absynt);
	    if(absynt instanceof Action)
		output((Action)absynt);
	    if(absynt instanceof StepAction)
		output((StepAction)absynt);
	    if(absynt instanceof Step)
		output((Step)absynt);
	    if(absynt instanceof Declaration)
		output((Declaration)absynt);
	    if(absynt instanceof Skip)
		output((Skip)absynt);
	    if(absynt instanceof Stmt)
		output((Stmt)absynt);
	    if(absynt instanceof Variable)
		output((Variable)absynt);
	    if(absynt instanceof Assign)
		output((Assign)absynt);
	    if(absynt instanceof B_expr)
		output((B_expr)absynt);
	    if(absynt instanceof U_expr)
		output((U_expr)absynt);
	    if(absynt instanceof Constval)
		output((Constval)absynt);
	}
    }

    public void printSAP(LinkedList _sap) {
	PrettyPrint prettyprint = new PrettyPrint(column + tab, tab);
	System.out.println("[StmtList] ");
	for (Iterator i = _sap.iterator(); i.hasNext();)
	    prettyprint.print((Stmt)i.next());
    }

    public void printExpession(LinkedList _sap) {
	PrettyPrint prettyprint = new PrettyPrint(column + tab, tab);
	System.out.println("[StmtList] ");
	for (Iterator i = _sap.iterator(); i.hasNext();)
	    prettyprint.print((Stmt)i.next());
    }

// ---------------------------------------------------------------------------

    private void output(SFC sfc) {
	System.out.println(whiteSpace(column) + "[SFC] ");
	/*	       
	 * Neuen Abstand erzeugen...die naechste Spalte ist einen
	 * tab weiter
	 */
	PrettyPrint prettyprint = new PrettyPrint(column + tab, tab);
	prettyprint.print(sfc.istep);
	PrettyPrint pp_long = new PrettyPrint(column + tab, tab, true);
	System.out.println(whiteSpace(column) + "[StepList] ");
	for (Iterator i = sfc.steps.iterator(); i.hasNext();)
	    pp_long.print((Step)i.next());
	System.out.println(whiteSpace(column) + "[TransitionList] ");
	for (Iterator i = sfc.transs.iterator(); i.hasNext();)
	    prettyprint.print((Transition)i.next());
	System.out.println(whiteSpace(column) + "[ActionList] ");
	for (Iterator i = sfc.actions.iterator(); i.hasNext();)
	    prettyprint.print((Action)i.next());
	System.out.println(whiteSpace(column) + "[DeclarationList] ");
	for (Iterator i = sfc.declist.iterator(); i.hasNext();)
	    prettyprint.print((Declaration)i.next());
    }
    
    private void output(Transition transition){
	System.out.println(whiteSpace(column) + "[Transition] " );
	PrettyPrint pp = new PrettyPrint(column + tab, tab);
	PrettyPrint prettyprint = new PrettyPrint(column + 2*tab, tab);
	pp.print("[SourceSteps]");
	for (Iterator i = transition.source.iterator(); i.hasNext();)
	    prettyprint.print((Step)i.next());
	pp.print(transition.guard);
	pp.print("[TargetSteps] ");
	for (Iterator i = transition.target.iterator(); i.hasNext();)
	    prettyprint.print((Step)i.next());
    }

    private void output(Action action){
	System.out.println(whiteSpace(column) + "[Action] "
			   + action.a_name);
	PrettyPrint prettyprint = new PrettyPrint(column + 2*tab, tab);
	System.out.println(whiteSpace(column+tab) + "[StmtList] ");
	for (Iterator i = action.sap.iterator(); i.hasNext();)
	    prettyprint.print((Stmt)i.next());
    }

    private void output(StepAction stepaction){
	System.out.println(whiteSpace(column) + "[StepAction] ");
	PrettyPrint prettyprint = new PrettyPrint(column + tab, tab);
	prettyprint.print(stepaction.qualifier);
	prettyprint.print(stepaction.a_name);
    }

    private void output(ActionQualifier aqf){
	String string="[unknown] ";
	if(aqf instanceof Nqual)
	    string="[N] ";
	System.out.println(whiteSpace(column) + "[Actionqualifier] "
			   + string);
    }

    private void output(Step step) {
	System.out.println(whiteSpace(column) + "[Step] " + step.name);
	if (steps_long) {
	    PrettyPrint prettyprint = new PrettyPrint(column + tab, tab);
	    for (Iterator i = step.actions.iterator(); i.hasNext();)
		prettyprint.print((StepAction)i.next());
	}
    }

    private void output(Declaration dec) {
	System.out.println(whiteSpace(column) + "[Declaration] ");
	PrettyPrint prettyprint = new PrettyPrint(column + tab, tab);
	prettyprint.print(dec.var);
	prettyprint.print(dec.type);
	prettyprint.print(dec.val);
    }
    
    private void output(Skip skip){
	System.out.println(whiteSpace(column) +"[Skip] ");
    }
    
    private void output(Stmt stmt){
	System.out.println(whiteSpace(column) +"[Stmt] ");
	PrettyPrint prettyprint = new PrettyPrint(column + tab, tab);
    }

    private void output(Variable variable){
	System.out.println(whiteSpace(column)+"[Variable] " + 
			   variable.name);
	PrettyPrint prettyprint = new PrettyPrint(column +tab, tab);
	prettyprint.print(variable.type);
    }

    private void output(Assign assign){
	System.out.println(whiteSpace(column) + "[Assign] ");
	PrettyPrint prettyprint = new PrettyPrint(column + tab, tab);
	prettyprint.print(assign.var);
	prettyprint.print(assign.val);
    }

    private void output(B_expr bexpr){
	System.out.println(whiteSpace(column) + "[B_expr] ");
	PrettyPrint prettyprint = new PrettyPrint(column + tab, tab);
	prettyprint.print(bexpr.left_expr);
	prettyprint.print(bexpr.op);
	prettyprint.print(bexpr.right_expr);
    }

    private void output(Expr expr){
	System.out.println(whiteSpace(column) + "[Expr] " );
	PrettyPrint prettyprint = new PrettyPrint(column + tab, tab);
    }    
    
    private void output(U_expr uexpr){
	System.out.println(whiteSpace(column) + "[U_Expr] ");
	PrettyPrint prettyprint = new PrettyPrint(column + tab, tab);
	prettyprint.print(uexpr.op);
	prettyprint.print(uexpr.sub_expr);
	//prettyprint.print(uexpr.type);
    }


    /* print wird hier ueberladen, da Op vom typ integer ist
     */
    private void print(int op){
	String string;
	switch(op){
	case Expr.PLUS :
	    string ="<PLUS> ";
	    break;
	case Expr.MINUS:
	    string ="<MINUS> ";
	    break;
	case Expr.TIMES:
	    string ="<TIMES> ";
	    break;
	case Expr.DIV:
	    string ="<DIV> ";
	    break;
	case Expr.AND:
	    string ="<AND> ";
	    break;
	case Expr.OR:
	    string ="<OR> ";
	    break;
	case Expr.NEG:
	    string ="<NEG>";
	    break;
	case Expr.EQ:
	    string ="<EQ> ";
	    break;
	case Expr.LESS:
	    string ="<LESS> ";
	    break;
	case Expr.GREATER:
	    string ="<GREATER> ";
	    break;
	case Expr.LEQ:
	    string ="<LEQ> ";
	    break;
	case Expr.GEQ:
	    string ="<GEQ> ";
	    break;
	case Expr.NEQ:
	    string ="<NEQ> ";
	    break;
	case Expr.MOD:
	    string ="<MOD> ";
	    break;
	case Expr.POW:
	    string ="<POW> ";
	    break;
	default:
	    string ="NULL ";
	    break;
	}
    	System.out.println(whiteSpace(column)+ string);
    }

    private void print(String s){
    	System.out.println(whiteSpace(column)+ s);
    }

    private void output(Constval constval){
        if(constval != null){
            System.out.println(whiteSpace(column) + "[Constval] " + 
                               constval.val);
            PrettyPrint prettyprint = new PrettyPrint(column + tab, tab);
            //prettyprint.print(constval.type);
        }
    }


    public void print(Type type){
	String string;
	string="[Type] ";
	if(type !=null){
	    if(type instanceof BoolType)
		string="[BoolType] ";
	    if(type instanceof IntType)
		string="[IntType] ";
	    if(type instanceof DoubleType)
		string="[DoubleType] ";
	    if(type instanceof UnitType) // not a user type
		string="[UnitType] ";
	    if(type instanceof UndefType) // not a user type
		string="[UndefType] ";
	    System.out.println(whiteSpace(column) + string);
	}	    
	PrettyPrint prettyprint = new PrettyPrint(column + tab, tab);
    }
 
    
    /* Hier wird festgelegt, wie weit die Ausgabe eingerueckt
     * wird. Bei allen "tab" Abstaenden drucken
     * wir ein | anstelle eines Leerzeichen
     */
    
    private String whiteSpace(int i) {
        String s = "";
        for(int j = 0; j < i; j++)
            s = j % tab != 0 ? s + " " : s + "|";
	
        return s;
    }      
}



//----------------------------------------------------------------------
//	Pretty-Printer
//	--------------
//
//	$Id: PrettyPrint.java,v 1.10 2002-07-11 12:49:25 swprakt Exp $
//
//	$Log: not supported by cvs2svn $
//	Revision 1.9  2002/07/11 07:14:35  swprakt
//	I replaced the cases like
//	
//		case 5:
//		    string ="<NEQ> ";
//	by
//		case Expr.NEQ
//		    string ="<NEQ> ";
//	
//	The explicit mention of 5 etc is a serious breach of
//	modularity and encapsulation.
//	
//	[Steffen]
//	
//	Revision 1.8  2002/07/11 06:51:12  swprakt
//	I removed the >>>>> ``comments'' [Steffen]
//	
//	Revision 1.7  2002/07/08 13:19:34  swprakt
//	uncommented some syntax errors. (mwe)
//	<<<<<<<<<<<<<<
//	=============
//	
//	Revision 1.6  2002/07/07 06:49:56  swprakt
//	I added clauses for the 2 non-user types
//	
//		Undeftype
//		UnitType
//	
//	they are internally used in the type checker and I
//	want to test it.
//	
//	[Steffen]
//	
//	Revision 1.5  2002/07/05 10:05:48  swprakt
//	I converted the print for Type from private to public.
//	
//	[Steffen]
//	
//	Revision 1.4  2002/07/04 16:33:29  swprakt
//	Added support for MOD(13)="%" and POW(14)="^". (mwe)
//	
//	Revision 1.3  2002/06/12 18:52:08  swprakt
//	reorganization of the package-structure
//	
//		src/<package>  => src/slime/<package>
//	
//	
//	as decided in the group meeting. consequently, some adaption had to be done
//	(wrt. import, package name etc). It compiles again.
//	
//	[M. Steffen]
//	
//	Revision 1.2  2002/06/07 15:04:18  swprakt
//	added parser for SAP programs, added function printSAP() to PP
//	
//	Revision 1.1  2002/04/16 13:57:54  swprakt
//	Slime initial version
//	
//	
//---------------------------------------------------------------------
