100 DEFine PROCedure dv(a,b)
110   LOCal l
120   PRINT a;'/';b;'=';
130   qa=1:q=0
140   REPeat l
150     IF b>a THEN EXIT l
160     b=b+b:qa=qa+qa
170   END REPeat l
180   REPeat l
190     b=b/2:qa=qa/2
200     IF qa<.7 THEN EXIT l
210     IF b>a THEN NEXT l
220     q=q+qa:a=a-b
230   END REPeat l
240   PRINT q!'remainder'!a
250 END DEFine 
260 :
270 DEFine PROCedure pd(n)
280   y=-5.877599E6
290   IF n>=HEX('7ffe8eba') THEN 
300     RESTORE 580
310   ELSE 
320     RESTORE 630
330   END IF 
500   REPeat l
510     READ x$:IF x$='****' THEN EXIT l
520     READ m,max
530     dv n,x$+0:IF q>max THEN q=max:n=n+x$
540     y=y+q*m
550   END REPeat l
560   PRINT y!'day'!n
570 END DEFine 
580 DATA 146097,400,6E6
590 DATA 36524,100,3
600 DATA 1461,4,25
610 DATA 365,1,3
620 DATA '****'
630 DATA 1461,4,6E6
640 DATA 365,1,3
650 DATA '****'
