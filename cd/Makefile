###############################################################
## $Id: Makefile,v 1.2 2002-07-19 05:16:23 swprakt Exp $
###############################################################

TARGET=ms@vires.informatik.uni-kiel.de:/home/ms/SLIMEREVIEW/toburn

upload:
	scp -r swprakt@sokrates:/home/swprakt/cvsroot/Slime $(TARGET)/cvsroot
	scp -r ms@goofy:/home/ms/Swprakt/Slime $(TARGET)
	scp ms@goofy:/home/ms/Swprakt/Slime/cd/Readme.cd $(TARGET)/Readme
	scp -r ms@goofy:/home/ms/Swprakt/Slime/src/slime.jar  $(TARGET)
	scp -r ms@goofy:/home/ms/WWWdeRoever/SS02/Java/Slime  $(TARGET)/web
	cd toburn;tar cvf Slime.tar *



uploadmail:
	scp -r ms@goofy:/home/ms/Mail/*slime* $(TARGET)/Mail



