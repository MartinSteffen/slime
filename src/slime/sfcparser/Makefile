##########################
# $Id: Makefile,v 1.11 2002-07-12 06:27:37 swprakt Exp $                       
# Makefile for SFCParser 
# initially provided by Marco Wendel <mwe@informatik.uni-kiel.de> 
##########################
# $Log: not supported by cvs2svn $
# Revision 1.10  2002/07/12 06:26:15  swprakt
# flag -expect 9 removed from the call of the parser (cf. Error #73)
#
# [Steffen]
#
# Revision 1.9  2002/07/08 13:10:06  swprakt
# Small changes due to adding assignment-declarations-values
# as remarked by Andreas Niemann. (mwe)
#
# Revision 1.8  2002/06/28 20:01:07  swprakt
# Modified PrettyPrint for use with slime.absfc.SFCabtree,
# may now use it to debug Absfc2SFCConverter. I will try
# to check in a fully functional SFCParser incl. nested
# statements within one week, so 'round about 05/07/2002.
# (mwe)
#
# Revision 1.7  2002/06/28 08:03:12  swprakt
# old versions did conflict with "global" Makefile
# in src/slime, albeight the GLOBAL Makefile should
# exist in src. (mwe)
#
# Revision 1.6  2002/06/27 19:39:36  swprakt
# slightly improved exceptionhandling
#
# Revision 1.5  2002/06/26 10:39:47  swprakt
# yy_eof muss nun noch irgendwie behandelt werden...
#
# Revision 1.4  2002/06/26 08:27:04  swprakt
# neues Makefile, JLex jetzt in Slime/src/JLex,
# von wo aus es beim kompilieren auch aufgerufen
# wird.
#
# CVS ----------------------------------------------------------------------
#
# Revision 1.3  2002/06/26 07:28:38  swprakt
# ParserTest.java gehoert nun zum Package slime.sfcparser
# CVS
#
# Revision 1.2  2002/06/26 06:33:04  swprakt
# Makefile geaendert nun mit fuer Slime gueltigem CLASSPATH "../..".
# Absfc2SFCConverter.java nun in slime.sfcparser-package.
# Parser nicht veraendert nur neu erzeugt. (mwe)
#
# Revision 1.1  2002/06/25 15:02:50  swprakt
# missing files added
#
# Revision 1.2  2002/05/01 11:37:48  mwe
# The Makefile now has an identity string and
# a changelog
#                        
#
##########################

############# Declaration of Path & Files ###########

PACKAGE=slime.sfcparser
DOCDIR=apidoc
JLEXLOGFILE=JLex.log
JCUPLOGFILE=JCup.log

############# Compile Package SFCParser #############

all:	SFCParser.class Absfc2SFCConverter.class ParserTest.class

testexpr:
	java slime.sfcparser.ParserTest -e "((1*2*3*4*5*6*7)-(1+2+(3+4+(5+(6+(7*1))))))/(9+(-9)+1/1)"

testDOS:
	testsfc.bat

test:
	testsfc.sh
	for i in `seq 0 8`; do less out.$i.txt;done

Absfc2SFCConverter.class:
	javac Absfc2SFCConverter.java

ParserTest.class:
	javac ParserTest.java

SFCParser.class:  SFCParseException.class SFCParser.java SFCLex.class
	javac SFCParser.java

SFCLex.java: 
	#  SFC.lex
	java JLex.Main SFC.lex >$(JLEXLOGFILE)
	mv SFC.lex.java SFCLex.java

SFCLex.class: SFCLex.java
	javac SFCSymbols.java
	javac SFCLex.java

SFCParser.java:
	# SFC.cup
        #java java_cup.Main -package $(PACKAGE) -parser SFCParser -symbols SFCSymbols -nonterms -expect 3 -dump-grammar -dump-states -dump-tables -dump -debug SFCParser.cup 2>&1 $(JCUPLOGFILE)
	java java_cup.Main -package $(PACKAGE) -parser SFCParser -symbols SFCSymbols -nonterms < SFC.cup 

SFCParseException.class:
	javac SFCParseException.java
SFC.lex:
	# vi SFC.lex
SFC.cup:
	# vi SFC.cup
SFCSymbols.java:
	# vi SFC.lex

############## Cleaning the mess up ##################

clean:
	rm -f SFCLex.java SFCSymbols.java SFCParser.java $(JLEXLOGFILE) $(JCUPLOGFILE) *.bak *~ *.tmp *.class


############## Creating Documentation ################

doc:
	SFCParser.java SFCSymbols.java SFCParseException.java SFCLex.java
	javadoc -version -author -d $(DOCDIR) *.java

install:
	clear
	echo "Please try to compile the whole Slime-Package"
	echo "The MainMakefile should exist in the previous subdir"	

help:
	clear
	vi Makefile
	#echo GNU Makefile for Slime.SFCParser 
	#echo -------------------------------- 
	#echo 
	#echo run one or more of the following make sections :
	#echo all		-	Generates all needed for Parsertest
	#echo SFCLexer.java	-	Created through JLex from SFC.lex
	#echo SFCParser.java	-	Created through Cup from SFC.cup
	#echo clean		-	clean all produced files
	#echo doc		-	Create Java/Documentation



