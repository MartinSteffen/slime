
all:	$(patsubst %.java,%.class,$(wildcard *.java))


test:  
	java slime.checks.tests.TypecheckTest
	java slime.checks.tests.WellformedTest

%.class:	%.java
		javac $(JAVACOPTS) $<


clean:
	\rm -f *.class *~

localdoc:	;
	javadoc  -version -author -d doc *.java


######################################################
## $Id: Makefile,v 1.4 2002-07-08 10:25:28 swprakt Exp $
######################################################