#
#
# Makefile for documantation and shipping.
#
# The targets installing gobal web-infos can be executed only
# by the maintainers and only at the university pool,
# Dor compiling, change into the slime-subdirectory.
#
######################################################################


TOOLNAME      = Slime

WEBDIR        = /home/info/www/inf/deRoever/SS02/Java/$(TOOLNAME)
WEBDOCDIR     = $(WEBDIR)/doc

SNAPSHOTSDIR  = $(WEBDIR)/snapshots

################################################

## first option: c = create
jar:
	jar cvf  slime.jar \
	    ./slime/*.class\
	    ./slime/absynt/*.class\
	    ./slime/absynt/absfc/*.class\
	    ./slime/layout/*.class\
	    ./slime/utils/*.class\
            ./slime/editor/*.class\
	    ./slime/editor/resources/images/*\
            ./slime/gui/*.class\
	    ./slime/simulator/*.class\
	    ./slime/checks/*.class\
	    ./slime/checks/tests/*.class\
	    ./slime/sfcparser/*.class


## for the cd-rom, I incorporate the cup and lex sources
#  for dynamic loading. This allows to start the stuff
#  with java -x slime.jar.  For the version on our web-page
#  I don't want to incorporate external code into the package.

cdjar:
	jar cvfm  slime.jar META-INF/MANIFEST.MF \
	    ./slime/*.class\
	    ./slime/absynt/*.class \
	    ./slime/absynt/absfc/*.class\
	    ./slime/layout/*.class\
	    ./slime/utils/*.class \
            ./slime/editor/*.class\
	    ./slime/editor/resources/images/*\
            ./slime/gui/*.class\
	    ./slime/simulator/*.class\
	    ./slime/checks/*.class\
	    ./slime/checks/tests/*.class\
	    ./slime/sfcparser/*.class\
	    ../lib/java_cup.jar\
	    ../lib/JLex.jar



jarinstall: 
	cp slime_v*.jar $(SNAPSHOTSDIR)



#----------------------------------------------------------------------
# $Id: Makefile,v 1.27 2002-07-19 11:58:30 swprakt Exp $
#----------------------------------------------------------------------

