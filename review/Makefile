######################################################
## $Id: Makefile,v 1.1 2002-07-02 15:00:12 swprakt Exp $ 
######################################################

TEXDIR1     = $(HOME)/dc/tex/inputs
DOC         = main
HEVEA       = hevea
HEVEAOPTS   = -fix
HACHA       = hacha



all:	
	dvips -o main.ps main.dvi





main.dvi: *.tex 
	latex main.tex


smallps:	 main.ps
	rm -f main-small.ps
	psnup -4  main.ps > main-small.ps


html: macs.hva $(DOC)-web.tex
	$(HEVEA) $(HEVEAOPTS) $(TEXDIR1)/ocamlweb.sty foils.hva macs.hva $(DOC)-web.tex
	$(HACHA) -o index.html $(DOC)-web.html



clean:
	rm -f core *~ *.dvi _region_.* *.log *.aux *.ps *.blg *.idx xy/*.xyc
	rm -f *.html *.haux