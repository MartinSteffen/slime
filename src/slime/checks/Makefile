
all:	$(patsubst %.java,%.class,$(wildcard *.java))


%.class:	%.java
		javac $(JAVACOPTS) $<


clean:
	\rm -f *.class *~

localdoc:	;
	javadoc  -version -author -d doc *.java
