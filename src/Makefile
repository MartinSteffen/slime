#----------------------------------------------------------------------
# developers' top-level Makefile for the Slime project
# $Id: Makefile,v 1.24 2002-07-10 16:43:40 swprakt Exp $
#----------------------------------------------------------------------
#
# The targets installing gobal web-infos can be executed only
# by the maintainers
######################################################################


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
	        slime.checks.tests\
	        slime.sfcparser\
		slime.gui\
		slime.gui.listener\
		slime.gui.model\
		slime.simulator\


################################################

classes:
	cd slime;
	cd absynt; make clean; make
	cd absfc; make clean; make
	cd ../..
	cd layout; make clean; make
	cd editor; make clean; make
	cd checks; make clean; make
	cd gui; make clean; make
	cd simulator; make clean; make


all:	packets $(patsubst %.java,%.class,$(wildcard *.java))

debug:	; make JAVACOPTS="-g" all

doc:	;
	javadoc  -version -author -d $(WEBDOCDIR) $(PACKAGEDIRS)


### the following is a temporary target to jar the stuff.
#   it is not yet the final delivery jar. [Steffen]


## first option: c = create
jar:
	jar cvfm  slime.jar META-INF/MANIFEST.MF ./slime/absynt/*.class \
	    ./slime/absynt/absfc/*.class\
	    ./slime/utils/*.class \
            ./slime/editor/*.class\
	    ./slime/editor/resources/images/*\
            ./slime/gui/*.class\
	    ./slime/simulator/*.class\
	    ./slime/checks/*.class\
	    ./slime/checks/tests/*.class\
	    ./slime/sfcparser/*.class

#jar:
#	jar cvf slime.jar ./slime/absynt/*.class \
#	    ./java_cup/*.class \
#	    ./java_cup/runtime/*.class \
#	    ./slime/absynt/absfc/*.class \
#	    ./slime/utils/*.class \
# 	    ./slime/editor/*.class\
# 	    ./slime/editor/resources/images/*.* \
# 	    ./slime/gui/listener/*.class \
# 	    ./slime/gui/model/*.class \
# 	    ./slime/gui/*.class \
# 	    ./slime/layout/*.class \
# 	    ./slime/simulator/*.class \
# 	    ./slime/checks/*.class \
# 	    ./slime/checks/tests/*.class \
# 	    ./slime/sfcparser/*.class

jarinstall: 
	cp slime_v*.jar $(BASELINESDIR)


clean:


########################################################################
# $Log: not supported by cvs2svn $
# Revision 1.23  2002/07/09 07:16:31  swprakt
# *** empty log message ***
#
# Revision 1.22  2002/07/05 18:49:55  swprakt
# *** empty log message ***
#
# Revision 1.21  2002/07/05 18:48:27  swprakt
# Added checks.test to the doc target. [Steffen]
#
# Revision 1.20  2002/07/04 06:47:48  swprakt
# I removed Lex and yacc stuff from the makefile, they are gone
# [Steffen]
#
# Revision 1.19  2002/07/03 17:18:12  swprakt
# Something has to happen with those
# Input/OutputActions, either they are
# going to be supported by Visitors
# and Simulator, or I will have to remove
# them from the parser. (mwe)
#
# Revision 1.18  2002/06/27 16:52:33  swprakt
# added java_cup/*.class and
# added java_cup/runtime/*.class to jar-file
# (mwe)
#
# Revision 1.17  2002/06/27 16:50:47  swprakt
# snot ? the last version of the global
# makefile was not for this package.
# This Makefile should work until no
# new packages are added. (mwe)
#
#


