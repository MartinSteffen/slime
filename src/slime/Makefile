#----------------------------------------------------------------------
# developers' top-level Makefile for the Slime project
# $Id: Makefile,v 1.28 2002-07-09 11:59:46 swprakt Exp $
#----------------------------------------------------------------------
#
# The targets installing gobal web-infos can be executed only
# by the maintainers
# 
# "cvs log Makefile" for version log
# "cvs annotate Makefile" for changed lines in each version
#
######################################

TOOLNAME      = Slime

WEBDIR        = /home/info/www/inf/deRoever/SS02/Java/$(TOOLNAME)
WEBDOCDIR     = $(WEBDIR)/doc
BASELINESDIR  = $(WEBDIR)/baselines

SNAPSHOTSDIR  = $(WEBDIR)/snapshots
LOCALDOCDIR   = doc
PACKAGEDIRS   = absynt\
	        utils\
	        layout\
	        editor\
	        checks\
	        simulator\
		sfcparser


#               
#	        gui\
#	        utils\
#                io


################################################


all:	packets $(patsubst %.java,%.class,$(wildcard *.java))

debug:	; make JAVACOPTS="-g" all

run:	all
	java slime.gui.SlimeStart

packets:
	(cd ./absynt; make)
	(cd ./utils; make)
	(cd ./sfcparser; make)
	(cd ./layout; make)
	(cd ./gui; make)
	(cd ./simulator; make)
	(cd ./editor; make)
#	(cd ./checks; make)
#	(cd ./io; make)

# the checks is temporarily commented out from
# the compilation list, because of discrepancies with other packages
# especially the extended interfaces in absynt wrt. to the visitor.
# this will be resolved asap.  Steffen 2.07.02



#### the following it to be used _with care_! it's a temporary
# hack to keep on working. Of course one could also just branch
# off, but since we are at the end of the course, we better should
# not. [Steffen]

backintime1:
	cvs update -r secondcompilation-sep absynt/DoubleType.java absynt/Visitors.java 


clean:
	(cd ./absynt; make clean)
	(cd ./layout; make clean)	
	(cd ./utils; make clean)
	(cd ./editor; make clean)
	(cd ./gui; make clean)
	(cd ./checks; make clean)
	(cd ./simulator; make clean)
	(cd ./sfcparser; make clean)

%.class:	%.java
		javac $(JAVACOPTS) $<







