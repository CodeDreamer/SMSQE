100 REMark   THIS PROGRAM MAKES THE HTML FILE ""NFA2_versions.html" "
110 REMark   USED ON THE SMSQE WEBSITE.
120 :
130 REMark   THE RESULTING FILES ARE COPIED TO NFA2_
140 REMark   WHICH SHOULD POINT TO THE "website" subdir.
150 :
160 REMark   THIS  PROGRAM NEEDS THE FILES "VERSION_HEADER" AND "VERSIONS_FOOTER"
170 REMark   WHICH SHOULD LIE IN THE EXTRAS_HTML SUBDIRECTORY.
180 :
190 REMark   THE INFO IS TAKEN FROM THE "dev8_smsq_smsq_version_asm" FILE.
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
320 LOCal inc%,outc%,lp%,a$,lp2%,lp3%,t%,flag%
330   inc%=FOP_IN(mfile$)
340   IF inc%<0:RETurn
350   outc%=FOP_OVER(resfile$)
360   IF outc%<0
370     CLOSE#inc%
380     RETurn
390   END IF
400   init
410   lp%= " " INSTR cmd$
420   IF lp%
430     today$= cmd$(1 TO lp%-1)
440   ELSE
450     today$=" (unknown)"
460   END IF
470   copy_file "dev8_extras_html_versions_header",outc%
480   flag%=0
490   REPeat lp%
500     INPUT#inc%,a$
510     IF "xdef" INSTR a$:EXIT lp%   : REMark get to first separator
520   END REPeat lp%
530   REPeat lp%
540     IF EOF(#inc%):EXIT lp%
550     a$=ginput$                              : REMark get one line
560     IF "#&#" INSTR a$:NEXT lp%:EXIT lp%     : REMark ignore all those lines
570     IF '-------------' INSTR a$:EXIT lp%    : REMark this is the end
580     IF LEN(a$)<3:NEXT lp%                   : REMark line not long enough
590     IF a$(1)<>";":NEXT lp%                  : REMark only treat commented lines
600     a$=strip_tab$(a$(2 TO ))                : REMark strip off ";", tab & space
610     IF a$(1) INSTR "123456789" AND (a$(2)='.' OR a$(2) INSTR "123456789" )
620       IF flag%
630         PRINT#outc%;"        </TBODY>"
640         PRINT#outc%;"      </TABLE>"
650       END IF
660       t%="." INSTR a$
670       b$=a$(1 TO t%+2)                      : REMark this is the version number
680       PRINT#outc%;"      <BR><br><br>"
690       PRINT#outc%;'      <H2 class="h2cntr">SMSQ/E Version '&b$&'</H2>'
700       PRINT#outc%;'      <TABLE ';lg$;' border=0 cellPadding=5 cellSpacing=1 Width="100%">'
710       PRINT#outc%;"         <TBODY>"
720       flag%=1
730       a$=strip_tab$(a$(t%+3 TO))
740       IF a$="":NEXT lp%
750     END IF
760     b$=""
770     REPeat lp2%
780       b$=a$(LEN(a$))
790       IF b$ INSTR ".!":EXIT lp2%
800       b$=ginput$
810       IF b$=";":EXIT lp2%
820       IF b$<>"":b$=strip_tab$(b$(2 TO))
830       IF b$="":EXIT lp2%
840       a$=a$&" "&b$
850     END REPeat lp2%
860     PRINT#outc%;"           <TR><TD ";dg$;" > "&a$&"</TD></TR>"
870   END REPeat lp%
880   PRINT#outc%;"        </TBODY>"
890   PRINT#outc%;"      </TABLE>"
900   copy_file "dev8_extras_html_versions_footer",outc%
910   PRINT#outc%,"      Page last updated on ";today$;"."
920   PRINT#outc%;"      <br><br>"
930   PRINT#outc%;"    </div>"
940   PRINT#outc%;"  </BODY>"
950   PRINT#outc%;"</HTML>"
960   CLOSE#outc%
970   CLOSE#inc%
980 END DEFine make_into_html
990 :
1000 DEFine PROCedure copy_file (mfile$,chan%)
1010 REMark copy content of text file mfile$ into channel chan%
1020 LOCal inc%,lp%,a$
1030   inc%=FOP_IN(mfile$)
1040   IF inc%<0:RETurn                         : REMark open file failed
1050   REPeat lp%
1060     IF EOF(#inc%):EXIT lp%
1070     INPUT#inc%,a$                       : REMark get one line
1080     PRINT#chan%,a$                      : REMark and print it
1090   END REPeat lp%
1100   CLOSE#inc%
1110 END DEFine copy_file
1120 :
1130 DEFine FuNction ginput$
1140 LOCal lp%,a$
1150   REPeat lp%
1160     IF EOF(#inc%):RETurn ""
1170     INPUT#inc%,a$
1180     a$=STRIP_SPACES$(a$)
1190     IF "-------" INSTR a$:RETurn ""
1200     IF a$<>"":RETurn a$
1210   END REPeat lp%
1220 END DEFine ginput$
1230 :
1240 DEFine PROCedure p
1250   make_into_html  "dev8_smsq_smsq_version_asm";"NFA8_html_versions.html"
1260   PRINT "done"\\"copied to NFA8_html_versions.html"
1270   IF "quit" INSTR cmd$:QUIT
1280 END DEFine p
1290 :
1300 DEFine PROCedure sa
1310   SAVE_O dev8_extras_html_versions_bas
1320 END DEFine sa
1330 :
1340 DEFine FuNction strip_tab$(a$)
1350 REMark returns string stripped of tab and spaces
1360 LOCal b$,t%,lp%
1370   b$=STRIP_SPACES$(a$)
1380   IF b$="":RETurn ""
1390   REPeat lp%
1400     IF b$(1)<> CHR$(9):RETurn STRIP_SPACES$(b$)
1410     IF LEN(b$)=1:RETurn ""
1420     b$=b$(2 TO)
1430   END REPeat lp%
1440   RETurn ""
1450 END DEFine strip_tab$
