package slime.sfcparser;
import java_cup.runtime.Symbol;
  /**
    * initially provided by Marco Wendel <mwe@informatik.uni-kiel.de>
    * $Id: SFCLex.java,v 1.3 2002-06-26 09:19:34 swprakt Exp $
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
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NOT_ACCEPT,
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
		/* 69 */ YY_NO_ANCHOR,
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
		/* 86 */ YY_NOT_ACCEPT,
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
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,258,
"3:8,4:2,1,3,4,1,3:18,4,36,3:3,33,34,3,6,7,5,31,39,32,3,2,42:10,28,8,37,29,3" +
"8,3:2,26,30,13,40,23,12,40,25,14,21,27,20,40,15,19,16,40,22,11,18,17,40,24," +
"40:3,3:4,41,3,26,30,13,40,23,12,40,25,14,21,27,20,40,15,19,16,40,22,11,18,1" +
"7,40,24,40:3,9,35,10,3:130,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,107,
"0,1,2,3,1:2,4,1:4,5,1:3,6,7,8,1,9,10,11,1:7,11:15,12,13,14,12,15,16,17,18:2" +
",19,20,21,22,23,11,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42" +
",43,44,45,46,47,48,49,50,18,51,52,53,54,55,56,57,58,11,59,60,61,62,63,64,65" +
",66,67,68,69")[0];

	private int yy_nxt[][] = unpackFromString(70,43,
"1,2,3,4,2,5,6,7,8,9,10,11,85,95,46,95,97,99,100,101,95,102,103,104,105,95:3" +
",45,49,106,12,13,14,53,56,15,16,17,18,95:2,19,-1:44,2,-1:2,2,-1:40,20,-1:2," +
"44,-1:42,86,-1:48,95,50,95:3,54,95:10,57,-1:2,95,-1:9,95,58:2,-1:29,26,-1:4" +
"2,27,-1:42,28,-1:55,19,-1:2,20:41,-1:11,95:17,-1:2,95,-1:9,95,58:2,-1:2,44:" +
"3,48,44:37,-1:29,22,-1:24,95,21,95:2,59,95:12,-1:2,95,-1:9,95,58:2,-1:2,47," +
"44:2,48,44:37,-1:29,23,-1:24,95:2,29,95:14,-1:2,95,-1:9,95,58:2,-1:2,52:3,5" +
"5,52:37,-1:34,24,-1:19,95:9,98,95:7,-1:2,95,-1:9,95,58:2,-1:2,52:3,55,52,51" +
",52:35,-1:35,25,-1:18,95:3,65,95:13,-1:2,95,-1:9,95,58:2,-1:11,95:5,91,95,3" +
"0,95:9,-1:2,95,-1:9,95,58:2,-1:11,95:8,66,95:8,-1:2,95,-1:9,95,58:2,-1:11,9" +
"5:7,67,95:9,-1:2,95,-1:9,95,58:2,-1:11,95:6,68,95:10,-1:2,95,-1:9,95,58:2,-" +
"1:11,95:5,70,95:11,-1:2,95,-1:9,95,58:2,-1:11,71,95:16,-1:2,95,-1:9,95,58:2" +
",-1:11,95:5,31,95:11,-1:2,95,-1:9,95,58:2,-1:11,95:2,94,95:14,-1:2,95,-1:9," +
"95,58:2,-1:11,95:3,77,95:13,-1:2,95,-1:9,95,58:2,-1:11,95:12,32,95:4,-1:2,9" +
"5,-1:9,95,58:2,-1:11,95:4,33,95:12,-1:2,95,-1:9,95,58:2,-1:11,95:12,79,95:4" +
",-1:2,95,-1:9,95,58:2,-1:11,95:12,34,95:4,-1:2,95,-1:9,95,58:2,-1:11,95:9,8" +
"0,95:7,-1:2,95,-1:9,95,58:2,-1:11,95:9,35,95:7,-1:2,95,-1:9,95,58:2,-1:11,9" +
"5:7,36,95:9,-1:2,95,-1:9,95,58:2,-1:11,95:12,37,95:4,-1:2,95,-1:9,95,58:2,-" +
"1:11,95:7,38,95:9,-1:2,95,-1:9,95,58:2,-1:11,95:9,39,95:7,-1:2,95,-1:9,95,5" +
"8:2,-1:11,95:6,82,95:10,-1:2,95,-1:9,95,58:2,-1:11,95:15,83,95,-1:2,95,-1:9" +
",95,58:2,-1:11,95:12,40,95:4,-1:2,95,-1:9,95,58:2,-1:11,84,95:16,-1:2,95,-1" +
":9,95,58:2,-1:11,95:7,41,95:9,-1:2,95,-1:9,95,58:2,-1:11,95:7,42,95:9,-1:2," +
"95,-1:9,95,58:2,-1:11,43,95:16,-1:2,95,-1:9,95,58:2,-1:11,95:15,87,95,-1:2," +
"95,-1:9,95,58:2,-1:11,95:9,93,95:7,-1:2,95,-1:9,95,58:2,-1:11,95:3,69,95:13" +
",-1:2,95,-1:9,95,58:2,-1:11,95:8,73,95:8,-1:2,95,-1:9,95,58:2,-1:11,95:7,92" +
",95:9,-1:2,95,-1:9,95,58:2,-1:11,95:6,76,95:10,-1:2,95,-1:9,95,58:2,-1:11,9" +
"5:5,78,95:11,-1:2,95,-1:9,95,58:2,-1:11,75,95:16,-1:2,95,-1:9,95,58:2,-1:11" +
",95:12,81,95:4,-1:2,95,-1:9,95,58:2,-1:11,95:3,72,95:13,-1:2,95,-1:9,95,58:" +
"2,-1:11,95:11,60,95:5,-1:2,95,-1:9,95,58:2,-1:11,95:3,74,95:13,-1:2,95,-1:9" +
",95,58:2,-1:11,95:4,61,95:12,-1:2,95,-1:9,95,58:2,-1:11,95:11,62,95:5,-1:2," +
"95,-1:9,95,58:2,-1:11,95:6,90,95:10,-1:2,95,-1:9,95,58:2,-1:11,95:8,88,95:8" +
",-1:2,95,-1:9,95,58:2,-1:11,95:12,63,95:4,-1:2,95,-1:9,95,58:2,-1:11,95:9,6" +
"4,95:7,-1:2,95,-1:9,95,58:2,-1:11,95:14,96,95:2,-1:2,95,-1:9,95,58:2,-1:11," +
"95:8,89,95:8,-1:2,95,-1:9,95,58:2");

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
						{return new Symbol(SFCSymbols.SEMICOLON);	/* */}
					case -9:
						break;
					case 9:
						{return new Symbol(SFCSymbols.LPSET); 	/* */}
					case -10:
						break;
					case 10:
						{return new Symbol(SFCSymbols.RPSET); 	/* */}
					case -11:
						break;
					case 11:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -12:
						break;
					case 12:
						{return new Symbol(SFCSymbols.ADD); 	/* Expr.PLUS    =  0 */}
					case -13:
						break;
					case 13:
						{return new Symbol(SFCSymbols.SUB); 	/* Expr.MINUS   =  1 */}
					case -14:
						break;
					case 14:
						{return new Symbol(SFCSymbols.MOD); 	/* Expr.MOD     =  undef */}
					case -15:
						break;
					case 15:
						{return new Symbol(SFCSymbols.NOT); 	/* Expr.NEG     =  6 */}
					case -16:
						break;
					case 16:
						{return new Symbol(SFCSymbols.LT);  	/* Expr.LESS    =  8 */}
					case -17:
						break;
					case 17:
						{return new Symbol(SFCSymbols.GT);  	/* Expr.GREATER =  9 */}
					case -18:
						break;
					case 18:
						{return new Symbol(SFCSymbols.COMMA); 	/* */}
					case -19:
						break;
					case 19:
						{return new Symbol(SFCSymbols.INTEGER, 
		   new Integer(yytext())); 		/* t INTEGER */}
					case -20:
						break;
					case 20:
						{return new Symbol(SFCSymbols.COMMENT);}
					case -21:
						break;
					case 21:
						{return new Symbol(SFCSymbols.IF); 		/* */}
					case -22:
						break;
					case 22:
						{return new Symbol(SFCSymbols.ASSIGN); 	/* Class Assign */}
					case -23:
						break;
					case 23:
						{return new Symbol(SFCSymbols.EQ);   /* Expr.EQ      =  7 */}
					case -24:
						break;
					case 24:
						{return new Symbol(SFCSymbols.AND); 	/* Expr.AND     =  4 */}
					case -25:
						break;
					case 25:
						{return new Symbol(SFCSymbols.OR);  	/* Expr.OR      =  5 */}
					case -26:
						break;
					case 26:
						{return new Symbol(SFCSymbols.NEQ); 	/* Expr.NEQ     = 12 */}
					case -27:
						break;
					case 27:
						{return new Symbol(SFCSymbols.LEQ); 	/* Expr.LEQ     = 10 */}
					case -28:
						break;
					case 28:
						{return new Symbol(SFCSymbols.GEQ); 	/* Expr.GEQ     = 11 */}
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
						{return new Symbol(SFCSymbols.SKIP); 	/* Class Skip */}
					case -32:
						break;
					case 32:
						{return new Symbol(SFCSymbols.TRUE);	        /* */}
					case -33:
						break;
					case 33:
						{return new Symbol(SFCSymbols.JOIN); 	/* */}
					case -34:
						break;
					case 34:
						{return new Symbol(SFCSymbols.ELSE); 	/* */}
					case -35:
						break;
					case 35:
						{return new Symbol(SFCSymbols.BOOLTYPE); 	/* Class BoolType */}
					case -36:
						break;
					case 36:
						{return new Symbol(SFCSymbols.SPLIT); 	/* */}
					case -37:
						break;
					case 37:
						{return new Symbol(SFCSymbols.FALSE); 	/* */}
					case -38:
						break;
					case 38:
						{return new Symbol(SFCSymbols.INPUT); 	/* ***RFC*** syntax !? */}
					case -39:
						break;
					case 39:
						{return new Symbol(SFCSymbols.UNTIL); 	/* */}
					case -40:
						break;
					case 40:
						{return new Symbol(SFCSymbols.LPSET); 	/* */}
					case -41:
						break;
					case 41:
						{return new Symbol(SFCSymbols.OUTPUT); 	/* ***RFC*** syntax !? */}
					case -42:
						break;
					case 42:
						{return new Symbol(SFCSymbols.REPEAT); 	/* */}
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
						{System.out.println( "Error during lexical analysis"+
	    "\nLine number = "  + yyline + 
	    "\nChar number = "  + yychar +
	    "\nText content = " + yytext() ); 		/* LexError */}
					case -51:
						break;
					case 54:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -52:
						break;
					case 56:
						{System.out.println( "Error during lexical analysis"+
	    "\nLine number = "  + yyline + 
	    "\nChar number = "  + yychar +
	    "\nText content = " + yytext() ); 		/* LexError */}
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
					case 69:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -66:
						break;
					case 70:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -67:
						break;
					case 71:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -68:
						break;
					case 72:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -69:
						break;
					case 73:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -70:
						break;
					case 74:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -71:
						break;
					case 75:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -72:
						break;
					case 76:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -73:
						break;
					case 77:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -74:
						break;
					case 78:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -75:
						break;
					case 79:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -76:
						break;
					case 80:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -77:
						break;
					case 81:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -78:
						break;
					case 82:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -79:
						break;
					case 83:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -80:
						break;
					case 84:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -81:
						break;
					case 85:
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
					case 105:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -101:
						break;
					case 106:
						{return new Symbol(SFCSymbols.IDENTIFIER, 
		    yytext()); 				/* t IDENTIFIER */}
					case -102:
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
