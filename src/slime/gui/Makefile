#----------------------------------------------------------------------
#	Gui for  Slime SFC's
#	-------------------------------
#----------------------------------------------------------------------


all:	$(patsubst %.java,%.class,$(wildcard *.java))


#### temporary target

run:
	java slime.gui.SlimeStart


debug:	; make JAVACOPTS="-g" all

%.class:	%.java
		javac $(JAVACOPTS) $<


clean:
	\rm -f *.class *~ gui.dfPackage gui.dfPackage.wmf




