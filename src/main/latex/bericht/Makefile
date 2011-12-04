FILENAME = bericht
OPEN = evince

all: compile clean

compile: $(FILENAME).tex
	pdflatex --synctex=-1 --interaction=nonstopmode $(FILENAME).tex
	makeindex -s $(FILENAME).ist -t $(FILENAME).glg -o $(FILENAME).gls $(FILENAME).glo
	makeindex -s $(FILENAME).ist -t $(FILENAME).alg -o $(FILENAME).acr $(FILENAME).acn
	bibtex $(FILENAME)
	pdflatex --synctex=-1 --interaction=nonstopmode $(FILENAME).tex
	makeindex -s $(FILENAME).ist -t $(FILENAME).glg -o $(FILENAME).gls $(FILENAME).glo
	makeindex -s $(FILENAME).ist -t $(FILENAME).alg -o $(FILENAME).acr $(FILENAME).acn
	bibtex $(FILENAME)
	pdflatex --synctex=-1 --interaction=nonstopmode $(FILENAME).tex
	pdflatex --synctex=-1 --interaction=nonstopmode $(FILENAME).tex

open: compile clean
	$(OPEN) $(FILENAME).pdf

.PHONY: clean mrproper
clean:
	-rm *.aux
	-rm ./*/*.aux
	-mv $(FILENAME).tex _B.tex
	-mv $(FILENAME).pdf _B.pdf
	-rm $(FILENAME).???
	-mv _B.tex $(FILENAME).tex
	-mv _B.pdf $(FILENAME).pdf
	-rm $(FILENAME).synctex
mrproper: clean
	-rm $(FILENAME).pdf
