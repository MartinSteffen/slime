package slime.sfcparser;
import java_cup.runtime.Symbol;
  /**
    * initially provided by Marco Wendel <mwe@informatik.uni-kiel.de>
    * $Id: SFCLex.java,v 1.2 2002-06-26 08:27:05 swprakt Exp $
    * -----
    * $Log: not supported by cvs2svn $
    * Revision 1.2  2002/06/26 06:50:57  swprakt
    * removed unused entries in SFC.lex
    * and removed wrong entry in SFC.cup "stmtblock" in "stmtlist"
    *
    * Revision 1.1  2002/06/25 15:02:51  swprakt
    * missing files added
    *
    * Revision 1.3  2002/05/05 22:24:12  mwe
    * added something from the grammar
    * i think something like slime.absynt.process
    * might be useful. some constructors within
    * slime.absynt.constval have to be added if
    * double/string support is required.
    *
    * Revision 1.2  2002/05/01 11:35:08  mwe
    * first version of SFC.lex
    * ATTENTION: %function has to be changed or removed
    * you can set "scan with" and set the corresponding function
    * COMMENTS wellcome
    *
    * Revision 1.1  2002/05/01 11:06:27  mwe
    * *** empty log message ***
    *
    *
    * Comments:
    * %cup 	-	it should work together with cup
    * %char	-	yychar() is accessible in Symboldefinition
    * %line	-	yyline() contains the current line in src-file
    * %function - 	use instead of next_Token()
    *		- THIS HAS TO BE CHANGED TO GET A RUNNING SFCParser.class :)
    * %notunix	-	does not care about ^M :) (skips \r)
    * %init{
    * %init}
"++"       {return new Symbol(SFCSymbols.INC); }
"--"	   {return new Symbol(SFCSymbols.DEC); }
    **/


public class SFCLex implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 256;
	private final int YY_EOF = 257;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yychar;
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	public SFCLex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	public SFCLex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private SFCLex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yychar = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yychar = yychar
			+ yy_buffer_index - yy_buffer_start;
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NOT_ACCEPT,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NOT_ACCEPT,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NOT_ACCEPT,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NOT_ACCEPT,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NOT_ACCEPT,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,258,
"3:8,4:2,1,3,4,1,3:18,4,13,3:3,10,11,3,6,7,5,8,33,9,3,2,41:10,3,34,15,14,16," +
"3:2,22,27,35,39,20,21,39,38,25,36,29,23,39,26,28,30,39,18,24,17,19,39,37,39" +
":3,3:4,40,3,22,27,35,39,20,21,39,38,25,36,29,23,39,26,28,30,39,18,24,17,19," +
"39,37,39:3,31,12,32,3:130,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,105,
"0,1,2,3,1:2,4,1:4,5,6,7,8,9,1:4,10,11,1:6,12:16,13,14,15,13,16,17,18,19:2,2" +
"0,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,19,36,12,37,38,39,40,41,42,4" +
"3,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,12,64,65,66,6" +
"7,68")[0];

	private int yy_nxt[][] = unpackFromString(69,42,
"1,2,3,4,2,5,6,7,8,9,10,45,49,11,12,13,14,15,93,96,97,98,99:2,68,46,99,100,1" +
"01,99,102,16,17,18,19,99,103,104,99:3,20,-1:43,2,-1:2,2,-1:39,21,-1:2,44,-1" +
":41,69,-1:50,24,-1:41,25,-1:41,26,-1:41,27,-1:44,99,70,99:12,-1:4,99:5,71:2" +
",-1:41,20,-1:2,21:40,-1:17,99:14,-1:4,99:5,71:2,-1:2,44:3,48,44:36,-1:11,22" +
",-1:47,99:4,28,99:4,53,99:4,-1:4,99:5,71:2,-1:2,47,44:2,48,44:36,-1:12,23,-" +
"1:46,99:14,-1:4,29,99:4,71:2,-1:2,52:3,54,52:36,-1:17,30,99:12,85,-1:4,99:5" +
",71:2,-1:2,52:3,54,52,51,52:34,-1:17,99:3,31,99:10,-1:4,99:5,71:2,-1:17,99:" +
"3,32,99:10,-1:4,99:5,71:2,-1:17,99:13,33,-1:4,99:5,71:2,-1:17,99:6,34,99:7," +
"-1:4,99:5,71:2,-1:17,99:9,35,99:4,-1:4,99:5,71:2,-1:17,99:6,36,99:7,-1:4,99" +
":5,71:2,-1:17,99:3,37,99:10,-1:4,99:5,71:2,-1:17,38,99:13,-1:4,99:5,71:2,-1" +
":17,39,99:13,-1:4,99:5,71:2,-1:17,99:3,40,99:10,-1:4,99:5,71:2,-1:17,41,99:" +
"13,-1:4,99:5,71:2,-1:17,42,99:13,-1:4,99:5,71:2,-1:17,99:7,43,99:6,-1:4,99:" +
"5,71:2,-1:17,99:4,50,99:7,76,95,-1:4,99:5,71:2,-1:17,99:2,55,99:11,-1:4,99:" +
"5,71:2,-1:17,99:13,81,-1:4,99:5,71:2,-1:17,82,99:13,-1:4,99:5,71:2,-1:17,99" +
":7,56,99:6,-1:4,99:5,71:2,-1:17,99:6,83,99:7,-1:4,99:5,71:2,-1:17,99:8,57,9" +
"9:5,-1:4,99:5,71:2,-1:17,99:11,58,99:2,-1:4,99:5,71:2,-1:17,99:11,87,99:2,-" +
"1:4,99:5,71:2,-1:17,99:8,59,99:5,-1:4,99:5,71:2,-1:17,99:8,88,99:5,-1:4,99:" +
"5,71:2,-1:17,99:3,89,99:10,-1:4,99:5,71:2,-1:17,99:8,60,99:5,-1:4,99:5,71:2" +
",-1:17,99:7,61,99:6,-1:4,99:5,71:2,-1:17,99:8,62,99:5,-1:4,99:5,71:2,-1:17," +
"99:2,63,99:11,-1:4,99:5,71:2,-1:17,99:13,90,-1:4,99:5,71:2,-1:17,99:14,-1:4" +
",91,99:4,71:2,-1:17,99:6,64,99:7,-1:4,99:5,71:2,-1:17,99:5,65,99:8,-1:4,99:" +
"5,71:2,-1:17,99:2,66,99:11,-1:4,99:5,71:2,-1:17,99:3,92,99:10,-1:4,99:5,71:" +
"2,-1:17,99:7,67,99:6,-1:4,99:5,71:2,-1:17,99:3,72,99:10,-1:4,99:5,71:2,-1:1" +
"7,86,99:13,-1:4,99:5,71:2,-1:17,99:6,84,99:7,-1:4,99:5,71:2,-1:17,99:9,73,9" +
"9:4,-1:4,99:5,71:2,-1:17,99:6,74,99:7,-1:4,99:5,71:2,-1:17,99:5,75,99:8,-1:" +
"4,99:5,71:2,-1:17,99:11,77,99:2,-1:4,99:5,71:2,-1:17,99:2,94,99:11,-1:4,99:" +
"5,71:2,-1:17,99,78,99:12,-1:4,99:5,71:2,-1:17,99:11,79,99:2,-1:4,99:5,71:2," +
"-1:17,99:14,-1:4,99:3,80,99,71:2");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {
				return null;
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{}
					case -3:
						break;
					case 3:
						{return new Symbol(SFCSymbols.DIV); 	/* Expr.DIV     =  3 */}
					case -4:
						break;
					case 4:
						{System.out.println( "Error during lexical analysis"+
	    "\nLine number = "  + yyline + 
	    "\nChar number = "  + yychar +
	    "\nText content = " + yytext() ); 		/* LexError */}
					case -5:
						break;
					case 5:
						{return new Symbol(SFCSymbols.MUL); 	/* Expr.TIMES   =  2 */}
					case -6:
						break;
					case 6:
						{return new Symbol(SFCSymbols.LPAREN); 	/* */}
					case -7:
						break;
					case 7:
						{return new Symbol(SFCSymbols.RPAREN); 	/* */}
					case -8:
						break;
					case 8:
						{return new Symbol(SFCSymbols.ADD); 	/* Expr.PLUS    =  0 */}
					case -9:
						break;
					case 9:
						{return new Symbol(SFCSymbols.SUB); 	/* Expr.MINUS   =  1 */}
					case -10:
						break;
					case 10:
						{return new Symbol(SFCSymbols.MOD); 	/* Expr.MOD     =  undef */}
					case -11:
						break;
					case 11:
						{return new Symbol(SFCSymbols.NOT); 	/* Expr.NEG     =  6 */}
					case -12:
						break;
					case 12:
						{return new Symbol(SFCSymbols.ASSIGN); 	/* Class Assign */}
					case -13:
						break;
					case 13:
						{return new Symbol(SFCSymbols.LT);  	/* Expr.LESS    =  8 */}
					case -14:
						break;
					case 14:
						{return new Symbol(SFCSymbols.GT);  	/* Expr.GREATER =  9 */}
					case -15:
						break;
					case 15:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -16:
						break;
					case 16:
						{return new Symbol(SFCSymbols.LPSET); 	/* */}
					case -17:
						break;
					case 17:
						{return new Symbol(SFCSymbols.RPSET); 	/* */}
					case -18:
						break;
					case 18:
						{return new Symbol(SFCSymbols.COMMA); 	/* */}
					case -19:
						break;
					case 19:
						{return new Symbol(SFCSymbols.SEMICOLON);	/* */}
					case -20:
						break;
					case 20:
						{return new Symbol(SFCSymbols.INTEGER, 
		   new Integer(yytext())); 		/* t INTEGER */}
					case -21:
						break;
					case 21:
						{return new Symbol(SFCSymbols.COMMENT);}
					case -22:
						break;
					case 22:
						{return new Symbol(SFCSymbols.AND); 	/* Expr.AND     =  4 */}
					case -23:
						break;
					case 23:
						{return new Symbol(SFCSymbols.OR);  	/* Expr.OR      =  5 */}
					case -24:
						break;
					case 24:
						{return new Symbol(SFCSymbols.NEQ); 	/* Expr.NEQ     = 12 */}
					case -25:
						break;
					case 25:
						{return new Symbol(SFCSymbols.EQ);   /* Expr.EQ      =  7 */}
					case -26:
						break;
					case 26:
						{return new Symbol(SFCSymbols.LEQ); 	/* Expr.LEQ     = 10 */}
					case -27:
						break;
					case 27:
						{return new Symbol(SFCSymbols.GEQ); 	/* Expr.GEQ     = 11 */}
					case -28:
						break;
					case 28:
						{return new Symbol(SFCSymbols.IF); 		/* */}
					case -29:
						break;
					case 29:
						{return new Symbol(SFCSymbols.SFCPRG);	/* Begin SFC-Source-File */}
					case -30:
						break;
					case 30:
						{return new Symbol(SFCSymbols.INTTYPE); 	/* Class IntType */}
					case -31:
						break;
					case 31:
						{return new Symbol(SFCSymbols.TRUE);	        /* */}
					case -32:
						break;
					case 32:
						{return new Symbol(SFCSymbols.ELSE); 	/* */}
					case -33:
						break;
					case 33:
						{return new Symbol(SFCSymbols.SKIP); 	/* Class Skip */}
					case -34:
						break;
					case 34:
						{return new Symbol(SFCSymbols.BOOLTYPE); 	/* Class BoolType */}
					case -35:
						break;
					case 35:
						{return new Symbol(SFCSymbols.JOIN); 	/* */}
					case -36:
						break;
					case 36:
						{return new Symbol(SFCSymbols.UNTIL); 	/* */}
					case -37:
						break;
					case 37:
						{return new Symbol(SFCSymbols.FALSE); 	/* */}
					case -38:
						break;
					case 38:
						{return new Symbol(SFCSymbols.SPLIT); 	/* */}
					case -39:
						break;
					case 39:
						{return new Symbol(SFCSymbols.INPUT); 	/* ***RFC*** syntax !? */}
					case -40:
						break;
					case 40:
						{return new Symbol(SFCSymbols.LPSET); 	/* */}
					case -41:
						break;
					case 41:
						{return new Symbol(SFCSymbols.REPEAT); 	/* */}
					case -42:
						break;
					case 42:
						{return new Symbol(SFCSymbols.OUTPUT); 	/* ***RFC*** syntax !? */}
					case -43:
						break;
					case 43:
						{return new Symbol(SFCSymbols.PROCESS); 	/* */}
					case -44:
						break;
					case 45:
						{System.out.println( "Error during lexical analysis"+
	    "\nLine number = "  + yyline + 
	    "\nChar number = "  + yychar +
	    "\nText content = " + yytext() ); 		/* LexError */}
					case -45:
						break;
					case 46:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -46:
						break;
					case 47:
						{return new Symbol(SFCSymbols.COMMENT);}
					case -47:
						break;
					case 49:
						{System.out.println( "Error during lexical analysis"+
	    "\nLine number = "  + yyline + 
	    "\nChar number = "  + yychar +
	    "\nText content = " + yytext() ); 		/* LexError */}
					case -48:
						break;
					case 50:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -49:
						break;
					case 51:
						{return new Symbol(SFCSymbols.COMMENT);}
					case -50:
						break;
					case 53:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -51:
						break;
					case 55:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -52:
						break;
					case 56:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -53:
						break;
					case 57:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -54:
						break;
					case 58:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -55:
						break;
					case 59:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -56:
						break;
					case 60:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -57:
						break;
					case 61:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -58:
						break;
					case 62:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -59:
						break;
					case 63:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -60:
						break;
					case 64:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -61:
						break;
					case 65:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -62:
						break;
					case 66:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -63:
						break;
					case 67:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -64:
						break;
					case 68:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -65:
						break;
					case 70:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -66:
						break;
					case 71:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -67:
						break;
					case 72:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -68:
						break;
					case 73:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -69:
						break;
					case 74:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -70:
						break;
					case 75:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -71:
						break;
					case 76:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -72:
						break;
					case 77:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -73:
						break;
					case 78:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -74:
						break;
					case 79:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -75:
						break;
					case 80:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -76:
						break;
					case 81:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -77:
						break;
					case 82:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -78:
						break;
					case 83:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -79:
						break;
					case 84:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -80:
						break;
					case 85:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -81:
						break;
					case 86:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -82:
						break;
					case 87:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -83:
						break;
					case 88:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -84:
						break;
					case 89:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -85:
						break;
					case 90:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -86:
						break;
					case 91:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -87:
						break;
					case 92:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -88:
						break;
					case 93:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -89:
						break;
					case 94:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -90:
						break;
					case 95:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -91:
						break;
					case 96:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -92:
						break;
					case 97:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -93:
						break;
					case 98:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -94:
						break;
					case 99:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -95:
						break;
					case 100:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -96:
						break;
					case 101:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -97:
						break;
					case 102:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -98:
						break;
					case 103:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -99:
						break;
					case 104:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -100:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
