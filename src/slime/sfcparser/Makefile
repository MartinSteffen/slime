##########################
# $Id: Makefile,v 1.1 2002-06-25 15:02:50 swprakt Exp $                       
# Makefile for SFCParser 
# initially provided by Marco Wendel <mwe@informatik.uni-kiel.de> 
##########################
# $Log: not supported by cvs2svn $
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
CLASSPATH=./java_cup.jar:./JLex.jar:./absynt.jar:./
ECLASSPATH=./java_cup.jar:./JLex.jar:./absynt.jar:./sfclexer.jar:./absfc.jar:./
XCLASSPATH=./sfcparser.jar:./absynt.jar:./

############# Compile Package SFCParser #############

all:	SFCParser.class

ParserTest:
#	jar cf sfcparser.jar ../../slime/sfcparser/*.class
	javac -classpath $(XCLASSPATH) ParserTest.java

SFCParser.class: SFCLex.class SFCParseException.class SFCParser.java
	javac -classpath $(ECLASSPATH) SFCParser.java SFCSymbols.java 

SFCLex.java: #
	#  SFC.lex
	java -classpath $(CLASSPATH) JLex.Main SFC.lex >$(JLEXLOGFILE)
	mv SFC.lex.java SFCLex.java
	
SFCLex.class: SFCLex.java
	javac -classpath $(CLASSPATH) SFCLex.java

SFCParser.java:
	# SFC.cup
        #java java_cup.Main -package $(PACKAGE) -parser SFCParser -symbols SFCSymbols -nonterms -expect 3 -dump-grammar -dump-states -dump-tables -dump -debug SFCParser.cup 2>&1 $(JCUPLOGFILE)
	# above all options that make sense
	java -classpath $(CLASSPATH) java_cup.Main -package $(PACKAGE) -parser SFCParser -symbols SFCSymbols -nonterms -expect 3 < SFC.cup 

SFCParseException.class:
	javac -classpath $(CLASSPATH)  SFCParseException.java
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
	rm -f SFCLexer.java SFCSymbols.java SFCParser.java $(JLEXLOGFILE) $(JCUPLOGFILE) *.bak *~ *.tmp


############## Creating Documentation ################

doc:
	SFCParser.java SFCSymbols.java SFCParseException.java SFCLexer.java
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
	#echo all		-	SFCParser.class
	#echo SFCLexer.java	-	Created through JLex from SFC.lex
	#echo SFCParser.java	-	Created through Cup from SFC.cup
	#echo clean		-	Delete SFCParser.class
	#echo mrproper		-	Clean all produced files
	#echo doc		-	Create Java/Documentation
	


