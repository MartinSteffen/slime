#----------------------------------------------------------------------
# developers' top-level Makefile for the Slime project
# $Id: Makefile,v 1.26 2002-07-09 06:56:55 swprakt Exp $
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



### the following is a temporary target to jar the stuff.
#   it is not yet the final delivery jar. [Steffen]

jar:
	jar cvf slime.jar ./absynt/*.class \
	    ./absynt/absfc/*.class
	    ./utils/*.class \
            ./editor/*.class\
	    ./editor/resources/images/*\
            ./gui/*.class\
	    ./simulator/*.class\
	    ./checks/*.class\
	    ./checks/tests/*.class\
	    ./sfcparser/*.class


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







