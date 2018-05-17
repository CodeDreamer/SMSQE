100 rem compress OS file
110
120 open #1,'con_512x256a40x40': cls: border 1,4
130
140 open_in #3,'flp1_smsq_gold'
150
160 comp=2
170 select on comp
180
190 =1
200   nw=flen (#3) / 2
210
220   dim count(255,255)
230   for w=1 to nw
240     bget #3,x%,y%
250     count(x%,y%)=count(x%,y%)+1
260   endfor w
270
280   close #3
290
300   dim xorder(255),yorder(255),corder(255)
310
320   for x=0 to 255
330     print x
340     for y=0 to 255
350       c=count(x,y)
360       if c>corder(255)
370         for i=1 to 255
380           if c>corder(i)
390             for j=254 to i step -1
400               corder(j+1)=corder(j)
410               xorder(j+1)=xorder(j)
420               yorder(j+1)=yorder(j)
430             endfor j
440             corder(i)=c
450             xorder(i)=x
460             yorder(i)=y
470             exit i
480           endif
490         endfor i
500       endif
510     endfor y
520   endfor x
530
540   accum=0
550   for i=1 to 255
560     accum=accum+corder(i)
570     print_using '.### ',accum*1000/nw
580     if (i mod 16) = 0: print
590   endfor i
600
610 =2
620   nb=flen (#3)
630
640   dim count(255)
650   for b=1 to nb
660     bget #3,x%
670     count(x%)=count(x%)+1
680   endfor b
690
700   close #3
710
720   dim xorder(255),corder(255)
730
740   for x=0 to 255
750     c=count(x)
760     if c>corder(255)
770       for i=1 to 255
780         if c>corder(i)
790           for j=254 to i step -1
800             corder(j+1)=corder(j)
810             xorder(j+1)=xorder(j)
820           endfor j
830           corder(i)=c
840           xorder(i)=x
850           exit i
860         endif
870       endfor i
880     endif
890   endfor x
900
910   accum=0
920   for i=1 to 255
930     accum=accum+corder(i)
940     print_using '.### ',accum*1000/nb
950     if (i mod 16) = 0: print
960   endfor i
970
980 endsel
990
1000 input a$
