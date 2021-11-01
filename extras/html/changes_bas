100 REMark   THIS PROGRAM MAKES THE HTML FILE "CHANGES_HTML"
110 REMark   USED ON THE SMSQE WEBSITE.
120 :
130 REMark   THE RESULTING FILES ARE COPIED TO NFA2_
140 REMark   WHICH SHOULD POINT TO THE "website" subdir
150 :
160 REMark   THIS  PROGRAM NEEDS THE FILES "CHANGES_HEADER" AND "CHANGES_FOOTER"
170 REMark   WHICH SHOULD  LIE IN THE EXTRAS_HTML SUBDIRECTORY.
180 :
190 REMark   THE INFO IS TAKEN FROM THE "CHANGES_TXT" FILE IN DEV8_
200 :
210 REMark   THIS PROGRAM IS COPYRIGHT (C) W. LENERZ 2004-2021
220 REMark   ALL RIGHTS RESERVED.
230 :
240 p
250 :
260 DEFine PROCedure init
270   lg$='class="lightTD"'
280   dg$='class="darkTD"'
290 END DEFine init
300 :
310 DEFine PROCedure make_into_html(mfile$,resfile$)
320 LOCal inc%,outc%,lp%,a$,lp2%,lp3%,t%,t1%
330   inc%=FOP_IN(mfile$)
340   IF inc%<0:RETurn
350   outc%=FOP_OVER(resfile$)
360   IF outc%<0
370     CLOSE#inc%
380     RETurn
390   END IF
400   init
410   lp%= " " instr cmd$
420   if lp%
430     today$= cmd$(1 to lp%-1)
440   else
450     today$=" (unknown)"
460   end if
470   copy_file "dev8_extras_html_changes_header",outc%
480   REPeat lp%
490     INPUT#inc%,a$
500     IF "----------" INSTR a$:EXIT lp%   : REMark get to first separator
510   END REPeat lp%
520   REPeat lp%
530     IF EOF(#inc%):EXIT lp%
540     a$=ginput$                          : REMark this is the version
550     IF a$="":NEXT lp%                   : REMark what? premature end!
560     PRINT#outc%;"  <BR><p><p>"
570     PRINT#outc%;'  <H2 class="h2cntr">SMSQ/E Version '&a$&"</H2>"
580     PRINT#outc%;'          <TABLE ';lg$;' border=0 cellPadding=5 cellSpacing=1 Width="100%">'
590     PRINT#outc%;"            <TBODY>"
600 REMark now the headers & data within this version
610     REPeat lp2%
620       a$=ginput$
630       IF a$="":EXIT lp2%
640       t%=CODE (a$)
650       IF t%>64 AND t%<90
660         PRINT#outc%;"              <TR>"
670         PRINT#outc%;"                <TD ";lg$;" ><B>"&a$&"</B></TD>"
680         PRINT#outc%;"              </TR>"
690          ELSE
700         PRINT#outc%;"              <TR>"
710         t%=" "INSTR a$                  : REMark end of filename
720         t1%=CHR$(9) INSTR a$
730         IF NOT t%:t%=t1%
740         IF (t1%) AND (t1%<t%):t%=t1%
750         IF t%
760           b$=a$(1 TO t%-1)
770           PRINT#outc%;"                <TD ";dg$;" > "&b$&"</TD>"
780         ELSE
790           PRINT#outc%;"                <TD ";dg$;" > "&a$&"</TD>"
800           PRINT#outc%;"                <TD ";dg$;" > </TD>"
810           PRINT#outc%;"                <TD ";dg$;" > </TD>"
820           PRINT#outc%;"              </TR>"
830           NEXT lp2%
840         END IF
850         a$=strip_tab$(a$(t%+1 TO))     : REMark strip off filename
860         t%=" " INSTR a$
870         t1%=CHR$(9) INSTR a$
880         IF NOT t%:t%=t1%
890         IF (t1%) AND (t1%<t%):t%=t1%
900         b$=a$(1 TO t%-1)               : REMark the version
910         IF b$<>""
920           PRINT#outc%;'                <TD style="text-align:center;" '&dg$&">"&b$&"</TD>"
930           a$=strip_tab$(a$(t%+1 TO))   : REMark strip off version, get comments
940           IF a$<>""
950             PRINT#outc%;"                <TD "&dg$&">"&a$&"</TD>"
960           END IF
970         END IF
980         PRINT#outc%;"              </TR>"
990       END IF
1000     END REPeat lp2%
1010     PRINT#outc%;"            </TBODY>"
1020     PRINT#outc%;"          </TABLE>"
1030   END REPeat lp%
1040   copy_file "dev8_extras_html_changes_footer",outc%
1050   PRINT#outc%,"      Page last updated on ";today$;"."
1060   PRINT#outc%;"      <br><br>"
1070   PRINT#outc%;"    </div>"
1080   PRINT#outc%;"  </BODY>"
1090   PRINT#outc%;"</HTML>"
1100   close#outc%
1110   CLOSE#inc%
1120 END DEFine make_into_html
1130 :
1140 DEFine PROCedure copy_file (mfile$,chan%)
1150 REMark copy content of text file mfile$ into channel chan%
1160 LOCal inc%,lp%,a$
1170   inc%=FOP_IN(mfile$)
1180   IF inc%<0:RETurn                         : REMark open file failed
1190   REPeat lp%
1200     IF EOF(#inc%):EXIT lp%
1210     INPUT#inc%,a$                       : REMark get one line
1220     PRINT#chan%,a$                      : REMark and print it
1230   END REPeat lp%
1240   CLOSE#inc%
1250 END DEFine copy_file
1260 :
1270 DEFine FuNction ginput$
1280 LOCal lp%,a$
1290   REPeat lp%
1300     IF EOF(#inc%):RETurn ""
1310     INPUT#inc%,a$
1320     a$=strip_tab$(a$)
1330     IF "-------" INSTR a$:RETurn ""
1340     IF a$<>"":RETurn a$
1350   END REPeat lp%
1360 END DEFine ginput$
1370 :
1380 DEFine PROCedure p
1390   PRINT 'Remember, each version must be separated by "----------------"'
1400   make_into_html  "dev8_changes_txt";"NFA8_html_chngelog.html"
1410   PRINT "done"
1420   PRINT "copied to NFA8_html_chngelog.html"
1430   IF "quit" instr cmd$:QUIT
1440 END DEFine p
1450 :
1460 DEFine PROCedure sa
1470   SAVE_O dev8_extras_html_changes_bas
1480 END DEFine sa
1490 :
1500 DEFine FuNction strip_tab$(a$)
1510 REMark returns string stripped of tab and spaces
1520 LOCal b$,t%,lp%
1530   b$=STRIP_SPACES$(a$)
1540   IF b$="":RETurn ""
1550   REPeat lp%
1560     IF b$(1)<> CHR$(9):RETurn STRIP_SPACES$(b$)
1570     IF LEN(b$)=1:RETurn ""
1580     b$=b$(2 TO)
1590   END REPeat lp%
1600   RETurn ""
1610 END DEFine strip_tab$
