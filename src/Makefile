
#----------------------------------------------------------------------
# developers' top-level Makefile for the Slime project
# $Id: Makefile,v 1.13 2002-06-23 16:08:36 swprakt Exp $
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
PACKAGEDIRS   = slime.absynt\
	        slime.utils\
	        slime.layout\
	        slime.editor\
	        slime.checks
#	        gui\
#	        simulator\
#                io


################################################


doc:	;
	javadoc  -version -author -d $(WEBDOCDIR) $(PACKAGEDIRS)


all:	packets $(patsubst %.java,%.class,$(wildcard *.java))

debug:	; make JAVACOPTS="-g" all


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


