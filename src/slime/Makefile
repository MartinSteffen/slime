
#----------------------------------------------------------------------
# developers' top-level Makefile for the Slime project
# $Id: Makefile,v 1.15 2002-06-28 15:07:31 swprakt Exp $
#----------------------------------------------------------------------

# The targets installing gobal web-infos can be executed only
# by the maintainers
# 
# Why should the developers(students) not be informed about
# what is happening with their files ??? (mwe)
# $Log: not supported by cvs2svn $
#

#
######################################

TOOLNAME      = Slime

WEBDIR        = /home/info/www/inf/deRoever/SS02/Java/$(TOOLNAME)
WEBDOCDIR     = $(WEBDIR)/doc
BASELINESDIR  = $(WEBDIR)/baselines

SNAPSHOTSDIR  = $(WEBDIR)/snapshots
LOCALDOCDIR   = doc
PACKAGEDIRS   = absynt\
	        layout\
	        editor\
	        checks\
	        simulator\
		sfcparser
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
	(cd ./layout; make)
	(cd ./editor; make)
	(cd ./gui; make)
	(cd ./checks; make)
	(cd ./simulator; make)
	(cd ./io; make)
	(cd ./sfcparser; make)

jar:
	jar cvf slime.jar ./absynt/*.class \
	    ./absynt/absfc/*.class
	    ./utils/*.class \
            ./editor/*.class\
	    ./editor/resources/images/*\
            ./gui/*.class\
	    ./simulator/*.class\
	    ./checks/*.class\
	    ./io/*.class\
	    ./sfcparser/*.class
		

jarinstall: 
	cp snot_v*.jar $(BASELINESDIR)


clean:
	(cd ./absynt; make clean)
	(cd ./layout; make clean)	
	(cd ./utils; make clean)
	(cd ./editor; make clean)
	(cd ./gui; make clean)
	(cd ./checks; make clean)
	(cd ./simulator; make clean)
	/cd ./sfcparser; make clean)


%.class:	%.java
		javac $(JAVACOPTS) $<


localdoc:	;
	javadoc  -version -author -d $(LOCALDOCDIR) $(PACKAGEDIRS)
	cp ./simulator/specification.txt $(LOCALDOCDIR)





