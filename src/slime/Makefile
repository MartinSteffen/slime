#----------------------------------------------------------------------
# $Id: Makefile,v 1.39 2002-07-19 13:53:29 swprakt Exp $
#----------------------------------------------------------------------
#
# developers' top-level Makefile for the Slime project
#
# The targets installing gobal web-infos can be executed only
# by the maintainers
######################################

TOOLNAME      = Slime


SNAPSHOTSDIR  = $(WEBDIR)/snapshots

PACKAGEDIRS   = absynt\
	        utils\
	        layout\
	        editor\
	        checks\
	        simulator\
		sfcparser\


################################################


all:	packets $(patsubst %.java,%.class,$(wildcard *.java))


run:	all
	java slime.Main

packets:
	(cd ./absynt; make)
	(cd ./utils; make)
	(cd ./sfcparser; make)
	(cd ./layout; make)
	(cd ./simulator; make)
	(cd ./editor; make)
	(cd ./checks; make)



clean:
	(cd ./absynt; make clean)
	(cd ./layout; make clean)	
	(cd ./utils; make clean)
	(cd ./editor; make clean)
	(cd ./checks; make clean)
	(cd ./simulator; make clean)
	(cd ./sfcparser; make clean)

%.class:	%.java
		javac $(JAVACOPTS) $<







