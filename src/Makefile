
#----------------------------------------------------------------------
# developers' top-level Makefile for the Slime project
# $Id: Makefile,v 1.18 2002-06-27 16:52:33 swprakt Exp $
#----------------------------------------------------------------------
#
# $Log: not supported by cvs2svn $
# Revision 1.17  2002/06/27 16:50:47  swprakt
# snot ? the last version of the global
# makefile was not for this package.
# This Makefile should work until no
# new packages are added. (mwe)
#
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
PACKAGEDIRS   = slime.absynt\
	        slime.absynt.absfc\
	        slime.utils\
	        slime.layout\
	        slime.editor\
	        slime.checks\
	        slime.sfcparser\
		slime.gui\
		slime.gui.listener\
		slime.gui.model\
		slime.simulator\
		Jlex\
		java_cup\
		java_cup.runtime
#                io


################################################


doc:	;
	javadoc  -version -author -d $(WEBDOCDIR) $(PACKAGEDIRS)


all:	packets $(patsubst %.java,%.class,$(wildcard *.java))

debug:	; make JAVACOPTS="-g" all


jar:
	jar cvf slime.jar ./slime/absynt/*.class \
	    ./java_cup/*.class \
	    ./java_cup/runtime/*.class \
	    ./slime/absynt/absfc/*.class \
	    ./slime/utils/*.class \
	    ./slime/editor/*.class\
	    ./slime/editor/resources/images/*.* \
	    ./slime/gui/listener/*.class \
	    ./slime/gui/model/*.class \
	    ./slime/gui/*.class \
	    ./slime/layout/*.class \
	    ./slime/simulator/*.class \
	    ./slime/checks/*.class \
	    ./slime/sfcparser/*.class
	

jarinstall: 
	cp slime_v*.jar $(BASELINESDIR)


clean:


