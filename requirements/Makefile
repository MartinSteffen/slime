########################################################
## $Id: Makefile,v 1.6 2002-07-19 12:33:59 swprakt Exp $
########################################################


HEVEA       = hevea
HEVEAOPTS   = -fix -exec xxdate.exe
HACHA       = hacha

DOC         = main
CODEDIR     = 
############# Achtung: die Versionen �ndern sich!
WWWDIR      = /home/info/www/inf/deRoever/SS02/Java/Slime/requirementsxxxx

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





