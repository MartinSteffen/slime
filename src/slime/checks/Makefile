
all:	$(patsubst %.java,%.class,$(wildcard *.java))


%.class:	%.java
		javac $(JAVACOPTS) $<


clean:
	\rm -f *.class *~

localdoc:	;
	javadoc  -version -author -d doc *.java


######################################################
## $Id: Makefile,v 1.3 2002-06-12 18:52:00 swprakt Exp $
######################################################