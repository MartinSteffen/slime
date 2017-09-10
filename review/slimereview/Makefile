clean:
	\rm -rf toburn/*


fetch: clean
	scp -r swprakt@sokrates:/home/swprakt/cvsroot/Slime      toburn
	mkdir  toburn/cvsroot
	mv toburn/Slime toburn/cvsroot
	scp -r ms@goofy:/home/ms/Swprakt/Slime toburn
	scp ms@goofy:/home/ms/Swprakt/Slime/cd/Readme.cd toburn/Readme
	scp -r ms@goofy:/home/ms/Swprakt/Slime/src/slime.jar  toburn
	scp -r ms@goofy:/home/ms/WWWdeRoever/SS02/Java/Slime  toburn/web
	cd toburn;tar cvf Slime.tar *

test:
	cd toburn;tar cvf Slime.tar *

fetchmails:
	mkdir Mail
	scp -r ms@goofy:/home/ms/Mail/*slime* ./Mail
fetchpitm:
	scp -r ms@goofy:/home/ms/Softtech/Lehre/JavaPITM .

fetchreview:
	scp -r ms@goofy:/home/ms/WWWdeRoever/SS02/Java/Slime/review/*  review



