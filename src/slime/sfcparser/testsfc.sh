#!/bin/bash
for i in `seq 0 9`; do java slime.sfcparser.ParserTest example.$i.sfc > out.$i.txt; done

