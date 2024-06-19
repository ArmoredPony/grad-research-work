set key autotitle columnheader left top Left
set grid mxtics mytics
set datafile separator "\t"
set style data hist
set boxwidth 0.8
set style fill solid 1.0
plot for [i=1:7] 'errors.csv' using i:key(i)