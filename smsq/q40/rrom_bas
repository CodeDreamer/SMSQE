100 INPUT 'Filename ';fn$
110 f1$ = 'win1_smsq_q40_'&fn$
120 flo$ = 'flp1_'&fn$&'LO'
130 fhi$ = 'flp1_'&fn$&'HI'
150 OPEN_IN #6,flo$
160 OPEN_IN #7,fhi$
170 OPEN_OVER #5,f1$
200 REPeat
210  IF EOF(#7): EXIT
220  BGET #7,a,b: BPUT #5,b,a
230  IF EOF(#6): EXIT
240  BGET #6,a,b: BPUT #5,b,a
250 END REPeat
260 CLOSE #5,#6,#7
