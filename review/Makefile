######################################################
## $Id: Makefile,v 1.3 2002-07-19 12:44:17 swprakt Exp $ 
######################################################

TEXDIR1     = $(HOME)/dc/tex/inputs
DOC         = main



all:	
	dvips  -Pwww -o main.ps main.dvi



%.pdf: %.ps
	ps2pdf -sPAPERSIZE=a4 $*.ps



main.dvi: *.tex 
	latex main.tex


smallps:	 main.ps
	rm -f main-small.ps
	psnup -4  main.ps > main-small.ps



clean:
	rm -f core *~ *.dvi _region_.* *.log *.aux *.ps *.blg *.idx xy/*.xyc
	rm -f *.html *.haux