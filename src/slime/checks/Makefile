
all:	$(patsubst %.java,%.class,$(wildcard *.java))
	cd tests; make


%.class:	%.java
		javac $(JAVACOPTS) $<


clean:
	cd tests; make clean
	\rm -f *.class *~

localdoc:	;
	javadoc  -version -author -d doc *.java


######################################################
## $Id: Makefile,v 1.5 2002-07-10 17:22:01 swprakt Exp $
######################################################