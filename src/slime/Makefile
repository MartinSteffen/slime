#----------------------------------------------------------------------
# developers' top-level Makefile for the Slime project
# $Id: Makefile,v 1.37 2002-07-19 12:34:00 swprakt Exp $
#----------------------------------------------------------------------
#
# The targets installing gobal web-infos can be executed only
# by the maintainers
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
		sfcparser\
	        gui
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
	(cd ./simulator; make)
	(cd ./editor; make)
	(cd ./checks; make)
	(cd ./gui; make)
#	(cd ./io; make)




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







