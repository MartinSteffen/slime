
#----------------------------------------------------------------------
# developers' top-level Makefile for the Slime project
# $Id: Makefile,v 1.12 2002-06-13 09:28:12 swprakt Exp $
#----------------------------------------------------------------------

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
	        checks
#	        gui\
#	        simulator\
#                io


################################################



all:	packets $(patsubst %.java,%.class,$(wildcard *.java))

debug:	; make JAVACOPTS="-g" all

run:	all
	java gui.Gui



packets:
	(cd ./absynt; make)
	(cd ./utils; make)
	(cd ./layout; make)
	(cd ./editor; make)
	(cd ./gui; make)
	(cd ./checks; make)
	(cd ./simulator; make)
	(cd ./io; make)

jar:
	jar cvf snot.jar ./absynt/*.class \
	    ./utils/*.class \
            ./editor/*.class\
            ./gui/*.class\
	    ./simulator/*.class\
	    ./checks/*.class\
	    ./io/*.class


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



%.class:	%.java
		javac $(JAVACOPTS) $<


localdoc:	;
	javadoc  -version -author -d $(LOCALDOCDIR) $(PACKAGEDIRS)
	cp ./simulator/specification.txt $(LOCALDOCDIR)





