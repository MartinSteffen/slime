
all:	$(patsubst %.java,%.class,$(wildcard *.java))


%.class:	%.java
		javac $(JAVACOPTS) $<


clean:
	cd tests; make clean
	\rm -f *.class *~

localdoc:	;
	javadoc  -version -author -d doc *.java


######################################################
## $Id: Makefile,v 1.4 2002-07-10 17:04:31 swprakt Exp $
######################################################