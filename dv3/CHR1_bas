100 REMark - This patch should allows DV3 drivers to handle files
110 REMark - with chr$(1) in the name without creating a dummy extension
120 :
125 REPeat
130   INPUT 'SMSQE Filename> ';fn$
140   IF fn$='': QUIT
150   fch = FOPEN (fn$)
160   IF fch<0: PRINT fn$;' ': REPORT fch: NEXT
170   fl = FLEN (#fch)
180 :
190   FOR fps1 = $8 TO fl-$280 STEP 2
200     GET #fch\(fps1), ck1%,ck2%,ck3%
210     IF ck1% = $5E00
220       IF ck2% = $6061 AND ck3% = $6263
230         GET #fch\(fps1+$100), ck1%
240         GET #fch\(fps1+$200), ck2%
250         IF ck1% = $5e5f and ck2% = $5e5f:  EXIT fps1
260       END IF
270     END IF
280   END FOR fps1
290
300   IF fps1 >= fl-$280
310     CLOSE #fch
320     PRINT 'Unknown Version'
330   ELSE
340     WPUT #fch\fps1-$5E, $005F
350     WPUT #fch\fps1+$100, $5E01
360     WPUT #fch\fps1+$200, $5E01
370     CLOSE #fch
380     INPUT 'DONE, FERTIG, FINI',a$
390     QUIT
400   END IF
410 END REPeat
