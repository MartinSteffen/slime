#
# Makefile for documentation and shipping.
#
# The targets installing gobal web-infos can be executed only
# by the maintainers and only at the university pool,
# Dor compiling, change into the slime-subdirectory.
#
######################################################################


TOOLNAME      = Slime

PACKAGEDIRS   = slime\
	        slime.absynt\
	        slime.absynt.absfc\
	        slime.utils\
	        slime.layout\
	        slime.editor\
	        slime.checks\
	        slime.checks.tests\
	        slime.sfcparser\
		slime.simulator\



WEBDIR        = /home/info/www/inf/deRoever/SS02/Java/$(TOOLNAME)
WEBDOCDIR     = $(WEBDIR)/doc

SNAPSHOTSDIR  = $(WEBDIR)/snapshots

################################################

doc:	;
	javadoc  -version -author -d $(WEBDOCDIR) $(PACKAGEDIRS)

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
	    ./slime/simulator/*.class\
	    ./slime/checks/*.class\
	    ./slime/checks/tests/*.class\
	    ./slime/sfcparser/*.class\
	    ../lib/java_cup.jar\
	    ../lib/JLex.jar



jarinstall: 
	cp slime_v*.jar $(SNAPSHOTSDIR)



#----------------------------------------------------------------------
# $Id: Makefile,v 1.30 2002-07-19 13:53:28 swprakt Exp $
#----------------------------------------------------------------------

