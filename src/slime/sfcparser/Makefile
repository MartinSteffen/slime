##########################
# $Id: Makefile,v 1.4 2002-06-26 08:27:04 swprakt Exp $                       
# Makefile for SFCParser 
# initially provided by Marco Wendel <mwe@informatik.uni-kiel.de> 
##########################
# $Log: not supported by cvs2svn $
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
ICLASSPATH=../..

############# Compile Package SFCParser #############

all:	SFCParser.class Absfc2SFCConverter ParserTest

test:
	java -classpath $(ICLASSPATH) slime.sfcparser.ParserTest

Absfc2SFCConverter:
	javac -classpath $(ICLASSPATH) Absfc2SFCConverter.java

ParserTest:
#	jar cf sfcparser.jar ../../slime/sfcparser/*.class
	javac -classpath $(ICLASSPATH) ParserTest.java

SFCParser.class:  SFCParseException.class SFCParser.java SFCLex.class
	javac -classpath $(ICLASSPATH) SFCParser.java SFCSymbols.java 

SFCLex.java: #
	#  SFC.lex
	java -classpath $(ICLASSPATH) JLex.Main SFC.lex >$(JLEXLOGFILE)
	mv SFC.lex.java SFCLex.java

SFCLex.class: SFCLex.java
	javac -classpath $(ICLASSPATH) SFCLex.java

SFCParser.java:
	# SFC.cup
        #java java_cup.Main -package $(PACKAGE) -parser SFCParser -symbols SFCSymbols -nonterms -expect 3 -dump-grammar -dump-states -dump-tables -dump -debug SFCParser.cup 2>&1 $(JCUPLOGFILE)
	# above all options that make sense
	java -classpath $(ICLASSPATH) java_cup.Main -package $(PACKAGE) -parser SFCParser -symbols SFCSymbols -nonterms -expect 3 < SFC.cup 

SFCParseException.class:
	javac -classpath $(ICLASSPATH)  SFCParseException.java
SFC.lex:
	# vi SFC.lex
SFC.cup:
	# vi SFC.cup
SFCSymbols.java:
	# vi SFC.lex

############## Cleaning the mess up ##################

clean:
	rm -f *.class

mrproper: clean
	rm -f SFCLex.java SFCSymbols.java SFCParser.java $(JLEXLOGFILE) $(JCUPLOGFILE) *.bak *~ *.tmp


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
	#echo clean		-	Delete SFCParser.class
	#echo mrproper		-	Clean all produced files
	#echo doc		-	Create Java/Documentation



