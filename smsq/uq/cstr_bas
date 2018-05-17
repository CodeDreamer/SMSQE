2000 DIM cvals(255)
2010 c$='Aa†Äåçé°ÅBbCc®àDdEeè£ÉêëFfGgHhIiíìîïJjKkLlMmNn©âOo§Ññóò•ÖPpQqRrSsúTtUußáôöõVvWwXxYyZz™ä¶Ü¢Ç´ã¨≠ÆØ∞±≤'
2020 OPEN_OVER #5,cstr_tab
2030 cval=0
2040 FOR char=0 TO 31: cvals(char)=cval: cval=cval+1
2050 FOR char=192 TO 255: cvals(char)=cval: cval=cval+1
2060 FOR char=32 TO 47: cvals(char)=cval: cval=cval+1
2070 FOR char=58 TO 191
2080   IF NOT (CHR$(char) INSTR c$): cvals(char)=cval: cval=cval+1
2090 END FOR char
2100 REMark PRINT #3,!cvals!:STOP
2110 FOR char=48 TO 57: cvals(char)=cval: cval=cval+1
2115 FOR c=1 TO LEN(c$): cvals(CODE(c$(c)))=cval: cval=cval+1
2120 pout
2125 FOR char=CODE('A') TO CODE ('Z'): cvals(char)=cvals(char)+1
2126 FOR char=160 TO 171: cvals(char)=cvals(char)+1
2128 pout
2130 CLOSE #5
2140 DEFine PROCedure pout
2160   cval$=FILL$(' ',256)
2170   FOR i=0 TO 240 STEP 16
2180     PRINT #5;'        dc.b    ';
2190     FOR j=0 TO 15: PRINT #5;',$';HEX$(cvals(i+j),8);
2200     PRINT #5
2210   END FOR i
2220   FOR i=0 TO 255: cval$(cvals(i)+1)=CHR$(i)
2230   FOR i=0 TO 240 STEP 16
2240     FOR j=0 TO 15: PRINT cval$(i+j+1);
2250     PRINT
2260   END FOR i
2270 END DEFine 
