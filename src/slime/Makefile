#----------------------------------------------------------------------
# developers' top-level Makefile for the Slime project
# $Id: Makefile,v 1.19 2002-07-02 07:01:13 swprakt Exp $
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
#		sfcparser


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
	(cd ./layout; make)
	(cd ./editor; make)
	(cd ./gui; make)
	(cd ./checks; make)
	(cd ./simulator; make)
#	(cd ./io; make)
#	(cd ./sfcparser; make)

# the sfc-parser is temporarily commented out from
# the compilation list, because of discrepancies in use of
# CLASSPATH. Not to waste time in analysing as simple compilation
# error, we just comment that out until an agreement is reached, sfc parser
# continues in his directory with his settings and his own
# choice of tools and at the same time
# the rest of the packages use their settings.
# We merge them as soon as possible. Steffen 2.07.02

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
	(cd ./sfcparser; make clean)

%.class:	%.java
		javac $(JAVACOPTS) $<

localdoc:	;
	javadoc  -version -author -d $(LOCALDOCDIR) $(PACKAGEDIRS)
	cp ./simulator/specification.txt $(LOCALDOCDIR)






