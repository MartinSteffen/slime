#----------------------------------------------------------------------
#	Abstract syntax for Slime SFC's
#	-------------------------------
#----------------------------------------------------------------------


all:	
	javac *.java


run:
	java slime.simulator.Main

debug:	; make JAVACOPTS="-g" all

%.class:	%.java
		javac $(JAVACOPTS) $<


clean:
	\rm -f *.class *~

localdoc:	;
	javadoc  -version -author -d doc *.java