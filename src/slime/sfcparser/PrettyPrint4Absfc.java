
/**
 * Pretty printer for abstract SFC trees {@link slime.absfc.SFCabtree}
 * @author Initially provided by Karsten Stahl. <br>
 * @author modified for SFCabtree by Marco Wendel. <br>
 * @version $Id: PrettyPrint4Absfc.java,v 1.2 2002-07-02 12:29:48 swprakt Exp $
 */

package slime.sfcparser;

import slime.absynt.*;
import slime.absynt.absfc.*;
import java.io.PrintStream;
import java.util.*;

public class PrettyPrint4Absfc {
    private int column;
    private int tab;
    private boolean steps_long = false;
    public static final int NORM_COLUMN = 0; 
    public static final int NORM_TAB = 4;
    public PrettyPrint4Absfc() {
        this(NORM_COLUMN, NORM_TAB);
    }
    private PrettyPrint4Absfc(int i, int j) {
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
    private PrettyPrint4Absfc(int i, int j, boolean _steps_long) {
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

    /* public print methode
     * Hier wird gecheckt welche Instanz das uebergebene 
     * Objekt hat und output wird aufgerufen.
     * Da wir auf die Instanz typecasten, reicht es aus
     * die output methode entsprechend zu ueberladen
     */
    public void print(Absynt absynt) {
	if(absynt != null) {
	    if(absynt instanceof SFCabtree)  output((SFCabtree)absynt);
	    if(absynt instanceof StmtIf)     output((StmtIf)absynt);
	    if(absynt instanceof StmtWhile)  output((StmtWhile)absynt);
	    if(absynt instanceof StmtRepeat) output((StmtRepeat)absynt);
	    if(absynt instanceof StmtIfElse) output((StmtIfElse)absynt);
	    if(absynt instanceof StmtInput)  output((StmtInput)absynt);
	    if(absynt instanceof StmtOutput) output((StmtOutput)absynt);
	    if(absynt instanceof StmtDecl)   output((StmtDecl)absynt);
	    if(absynt instanceof StmtAssign) output((StmtAssign)absynt);
	    if(absynt instanceof StmtSplit)  output((StmtSplit)absynt);
	    if(absynt instanceof StmtJoin)   output((StmtSplit)absynt);
	    if(absynt instanceof slime.absynt.absfc.Process)    output((slime.absynt.absfc.Process)absynt);
	    if(absynt instanceof Variable)  output((Variable)absynt);
	    if(absynt instanceof B_expr)    output((B_expr)absynt);
	    if(absynt instanceof U_expr)    output((U_expr)absynt);
	    if(absynt instanceof Constval)  output((Constval)absynt);
/*	    if(absynt instanceof Type)      output((Type)absynt);
	    if(absynt instanceof BoolType)  output((BoolType)absynt);
	    if(absynt instanceof IntType)   output((IntType)absynt);
	    if(absynt instanceof M_Type)    output((M_Type)absynt);
*/
	}
    }

    public void printStmts(LinkedList stmtlist) {
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + tab, tab);
	System.out.println("[StmtList] ");
	for (Iterator i = stmtlist.iterator(); i.hasNext();)
	    prettyprint.print((Absfc)i.next());
    }

    public void printExpession(LinkedList _sap) {
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + tab, tab);
	System.out.println("[StmtList] ");
	for (Iterator i = _sap.iterator(); i.hasNext();)
	    prettyprint.print((Stmt)i.next());
    }

    private void output(SFCabtree sfcabtree) {
	System.out.println( whiteSpace(column) + "[SFCabtree] ");
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + tab, tab);
	prettyprint.print(sfcabtree.sfcprogname);
	System.out.println( whiteSpace(column) + "[Counters] ");
	System.out.println( whiteSpace(column+tab) + "procCnt = " + sfcabtree.procCnt);
	System.out.println( whiteSpace(column+tab) + "stmtCnt = " + sfcabtree.stmtCnt);
	System.out.println( whiteSpace(column+tab) + "varCnt = " + sfcabtree.varCnt);
	System.out.println( whiteSpace(column+tab) + "decCnt = " + sfcabtree.decCnt);
	PrettyPrint4Absfc pp_long = new PrettyPrint4Absfc(column + tab, tab, true);
	System.out.println(whiteSpace(column) + "[Statements] ");
	for (Iterator i = sfcabtree.stmtlist.iterator(); i.hasNext();)
	    pp_long.print((Absynt)i.next());
	/* 
	   System.out.println(whiteSpace(column) + "[Processes] ");
	for (Iterator i = sfcabtree.proclist.iterator(); i.hasNext();)
	    prettyprint.print((slime.absynt.absfc.Process)i.next());
	System.out.println(whiteSpace(column) + "[VariableList] ");
       	  for (Iterator i = sfcabtree.varlist.iterator(); i.hasNext();)
	    prettyprint.print((Variable)i.next());
	System.out.println(whiteSpace(column) + "[DeclarationList] ");
	for (Iterator i = sfcabtree.declist.iterator(); i.hasNext();)
	    prettyprint.print((Declaration)i.next());
	*/
    }
    
    private void output(StmtIf ifstmt){
	System.out.println(whiteSpace(column) + "[If] " );
	PrettyPrint4Absfc pp = new PrettyPrint4Absfc(column + tab, tab);
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + 2*tab, tab);
	pp.print("[if-case]");
	for (Iterator i = ifstmt.stmtlist.iterator(); i.hasNext();)
	    prettyprint.print((Absfc)i.next());
	pp.print("[Condition] ");
	pp.print( ifstmt.expr );
    }

    private void output(StmtIfElse ifelsestmt){
	System.out.println(whiteSpace(column) + "[If-Else] " );
	PrettyPrint4Absfc pp = new PrettyPrint4Absfc(column + tab, tab);
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + 2*tab, tab);
	pp.print("[if-case]");
	for (Iterator i = ifelsestmt.ifstmtlist.iterator(); i.hasNext();)
	    prettyprint.print((Absfc)i.next());
	pp.print("[Condition] ");
	pp.print( ifelsestmt.expr );
	pp.print("[else-case] ");
	for (Iterator i = ifelsestmt.elsestmtlist.iterator(); i.hasNext();)
	    prettyprint.print((Absfc)i.next());
    }

    private void output(StmtWhile whilestmt){
	System.out.println(whiteSpace(column) + "[While] " );
	PrettyPrint4Absfc pp = new PrettyPrint4Absfc(column + tab, tab);
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + 2*tab, tab);
	pp.print("[while-loop]");
	for (Iterator i = whilestmt.stmtlist.iterator(); i.hasNext();)
	    prettyprint.print((Absfc)i.next());
	pp.print("[Condition] ");
	pp.print( whilestmt.expr );
    }

    private void output(StmtSplit splitstmt){
	System.out.println(whiteSpace(column) + "[Split] " );
	PrettyPrint4Absfc pp = new PrettyPrint4Absfc(column + tab, tab);
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + 2*tab, tab);
	pp.print("[spawned processes] ");
	for (Iterator i = splitstmt.proclist.iterator(); i.hasNext();)
	    prettyprint.print((slime.absynt.absfc.Process)i.next());
	pp.print("[depth] ");
	pp.print( splitstmt.procdepth );
    }

    private void output(StmtJoin joinstmt){
	System.out.println(whiteSpace(column) + "[Join] " );
	PrettyPrint4Absfc pp = new PrettyPrint4Absfc(column + tab, tab);
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + 2*tab, tab);
	pp.print("[joined processes] ");
	for (Iterator i = joinstmt.proclist.iterator(); i.hasNext();)
	    prettyprint.print( (String) i.next() );
	pp.print("[depth] ");
	pp.print( joinstmt.procdepth );
    }

    private void output(slime.absynt.absfc.Process procstmt){
	System.out.println(whiteSpace(column) + "[Process] " );
	PrettyPrint4Absfc pp = new PrettyPrint4Absfc(column + tab, tab);
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + 2*tab, tab);
	pp.print("[Name] ");
	pp.print( procstmt.name );
	pp.print("[statements in process] ");
	for (Iterator i = procstmt.stmtlist.iterator(); i.hasNext();)
	    prettyprint.print( (Absfc) i.next() );
	pp.print("[depth] ");
	pp.print( procstmt.procdepth );
    }

    private void output(StmtRepeat repeatstmt){
	System.out.println(whiteSpace(column) + "[Repeat] " );
	PrettyPrint4Absfc pp = new PrettyPrint4Absfc(column + tab, tab);
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + 2*tab, tab);
	pp.print("[repeat-loop]");
	for (Iterator i = repeatstmt.stmtlist.iterator(); i.hasNext();)
	    prettyprint.print((Absfc)i.next());
	pp.print("[until Condition] ");
	pp.print( repeatstmt.expr );
    }

    private void output(StmtInput inputstmt){
	System.out.println(whiteSpace(column) + "[Input] " );
	PrettyPrint4Absfc pp = new PrettyPrint4Absfc(column + tab, tab);
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + 2*tab, tab);
	pp.print("[Variable]");
	pp.print( inputstmt.var );
	pp.print("[Modified with expression]");
	pp.print( inputstmt.val );
    }

    private void output(StmtOutput outputstmt){
	System.out.println(whiteSpace(column) + "[Output] " );
	PrettyPrint4Absfc pp = new PrettyPrint4Absfc(column + tab, tab);
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + 2*tab, tab);
	pp.print("[Variable]");
	pp.print( outputstmt.var );
	pp.print("[Modified with expression]");
	pp.print( outputstmt.val );
    }

    private void output(StmtDecl dec) {
	System.out.println(whiteSpace(column) + "[Declaration] ");
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + tab, tab);
	prettyprint.print(dec.var);
	prettyprint.print(dec.type);
	prettyprint.print(dec.val);
    }

    private void output(StmtAssign assign){
	System.out.println(whiteSpace(column) + "[Assignment] ");
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + tab, tab);
	prettyprint.print(assign.var);
	prettyprint.print(assign.val);
    }

    private void output(Variable variable){
	System.out.println(whiteSpace(column)+"[Variable] " + variable.name);
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column +tab, tab);
	prettyprint.print(variable.type);
    }

    private void output(B_expr bexpr){
	System.out.println(whiteSpace(column) + "[B_expr] ");
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + tab, tab);
	prettyprint.print(bexpr.left_expr);
	prettyprint.print(bexpr.op);
	prettyprint.print(bexpr.right_expr);
    }

    private void output(Expr expr){
	System.out.println(whiteSpace(column) + "[Expr] " );
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + tab, tab);
    }    
    
    private void output(U_expr uexpr){
	System.out.println(whiteSpace(column) + "[U_Expr] ");
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + tab, tab);
	prettyprint.print(uexpr.op);
	prettyprint.print(uexpr.sub_expr);
	//prettyprint.print(uexpr.type);
    }

    private void output(Constval constval){
        if(constval != null){
            System.out.println(whiteSpace(column) + "[Constval] " + 
                               constval.val);
            PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + tab, tab);
            //prettyprint.print(constval.type);
        }
    }

    /* print wird hier ueberladen, da Op vom typ integer ist
     */
    private void print(int op){
	String string;
	switch(op){
	case 0 :    string ="<PLUS> ";	    break;
	case 1:	    string ="<MINUS> ";	    break;
	case 2:	    string ="<TIMES> ";	    break;
	case 3:	    string ="<DIV> ";	    break;
	case 4:	    string ="<AND> ";	    break;
	case 5:	    string ="<OR> ";	    break;
	case 6:	    string ="<NEG>";	    break;
	case 7:	    string ="<EQ> ";	    break;
	case 8:	    string ="<LESS> ";	    break;
	case 9:	    string ="<GREATER> ";   break;
	case 10:    string ="<LEQ> ";	    break;
	case 11:    string ="<GEQ> ";	    break;
	case 12:    string ="<NEQ> ";	    break;
	default:    string ="NULL ";	    break;
	}
    	System.out.println(whiteSpace(column)+ string);
    }

    private void print(String s){
    	System.out.println(whiteSpace(column)+ s);
    }

    private void print(Type type){
	String string;
	string="[Type] ";
	if(type !=null){
	    if(type instanceof BoolType)
		string="[BoolType] ";
	    if(type instanceof IntType)
		string="[IntType] ";
	    System.out.println(whiteSpace(column) + string);
	}	    
	PrettyPrint4Absfc prettyprint = new PrettyPrint4Absfc(column + tab, tab);
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

} // end PrettyPrint4Asfc



