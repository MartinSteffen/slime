########################################################
## $Id: Makefile,v 1.4 2002-06-14 16:50:49 swprakt Exp $
########################################################


HEVEA       = hevea
HEVEAOPTS   = -fix -exec xxdate.exe
HACHA       = hacha

DOC         = main
CODEDIR     = 
############# Achtung: die Versionen ändern sich!
WWWDIR      = /home/info/www/inf/deRoever/SS02/Java/Slime/requirements3

main.dvi: main.tex
	latex main

main.ps:  main.dvi
	dvips -o main.ps main.dvi

main-small.ps:
	psnup -2 main.ps > main-small.ps

index.html: $(DOC).html
	$(HACHA) -o index.html $(DOC).html

html: $(DOC).tex
	$(HEVEA) $(HEVEAOPTS) ../tex/inputs/macros.hva -o $(DOC)-sec.html $(DOC).tex
	$(HACHA) -o index.html $(DOC)-sec.html



clean:
	rm -f *.aux *.log *.dvi main.ps main.pfd *.bbl *.blg *~ 
	rm *.html *motif.gif *.haux

www:
	cp *.html *.ps *.gif *.pdf $(WWWDIR)





