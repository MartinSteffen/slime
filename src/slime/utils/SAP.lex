package slime.utils;
import java_cup.runtime.Symbol;

%%
%cup
%line
%{
%}

comment = ("//".*)
space   = [\ \t\n\b\015]+
letter  = [A-Za-z_]
digit   = [0-9]
number  = {digit}+
ident   = ({letter}({letter}|{digit})*)
%%


{comment}             { }
{space}               { }
";"                   { return new Symbol(sym.SEM);             }
":"                   { return new Symbol(sym.COLON);           }
"("                   { return new Symbol(sym.LPAREN);          }
")"                   { return new Symbol(sym.RPAREN);          }
"skip"                { return new Symbol(sym.SKIP);            }
":="                  { return new Symbol(sym.ASSIGN);          }
"int"                 { return new Symbol(sym.INT);             }
"bool"                { return new Symbol(sym.BOOL);            }
"true"                { return new Symbol(sym.TRUE);            }
"false"               { return new Symbol(sym.FALSE);           }
"and"                 { return new Symbol(sym.AND);             }
"or"                  { return new Symbol(sym.OR);              }
"not"                 { return new Symbol(sym.NEG);             }
"+"                   { return new Symbol(sym.PLUS);            }
"-"                   { return new Symbol(sym.MINUS);           }
"*"                   { return new Symbol(sym.TIMES);           }
"/"                   { return new Symbol(sym.DIV);             }
"<"                   { return new Symbol(sym.LESS);            }
">"                   { return new Symbol(sym.GREATER);         }
"<="                  { return new Symbol(sym.LEQ);             }
">="                  { return new Symbol(sym.GEQ);             }
"="                   { return new Symbol(sym.EQ);              }
"!="                  { return new Symbol(sym.NEQ);             }


{ident}                { return new Symbol(sym.IDENT, yytext()); }
{number}              { return new Symbol(sym.INTEGER, new Integer(yytext())); }
.                     { System.err.println("[Modul utils/parser] in line "+yyline+": unknow char: "+yytext()); }
