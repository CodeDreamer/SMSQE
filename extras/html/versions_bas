100 REMark   THIS PROGRAM MAKES THE HTML FILE ""NFA2_versions.html" "
110 REMark   USED ON THE SMSQE WEBSITE.
120 :
130 REMark   THE RESULTING FILES ARE COPIED TO NFA2_
140 REMark   WHICH SHOULD POINT TO THE "website" subdir.
150 :
160 REMark   THIS  PROGRAM NEEDS THE FILES "VERSION_HEADER" AND "VERSIONS_FOOTER" WWHICH SUOUULD
170 REMark   LIE IN THE EXTRAS8HTML SUBDIRECTORY.
180 :
190 REMark   THE INFO IS TAKEN FROM THE "dev8_smsq_smsq_version_asm" FILE.
200 :
210 REMark   THIS PROGRAM IS COPYRIGHT (C) W. LENERZ 2004-2018
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
410   lp%= " " instr cmd$
420   if lp%
430     today$= cmd$(1 to lp%-1)
440   else
450     today$=" (unnown)"
460   end if
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
630         PRINT#outc%;"            </TBODY>"
640         PRINT#outc%;"          </TABLE>"
650       END IF
660       t%="." INSTR a$
670       b$=a$(1 TO t%+2)                      : REMark this is the version number
680       PRINT#outc%;"  <BR><p><p>"
690       PRINT#outc%;"  <H2><U>SMSQ/E Version "&b$&"</U></H2>"
700       PRINT#outc%;"  <p> "
710       PRINT#outc%;'          <TABLE ';lg$;' border=0 cellPadding=5 cellSpacing=1 Width="100%">'
720       PRINT#outc%;"            <TBODY>"
730       flag%=1
740       a$=strip_tab$(a$(t%+3 TO))
750       IF a$="":NEXT lp%
760     END IF
770     b$=""
780     REPeat lp2%
790       b$=a$(LEN(a$))
800       IF b$ INSTR ".!":EXIT lp2%
810       b$=ginput$
820       if b$=";":exit lp2%
830       IF b$<>"":b$=strip_tab$(b$(2 TO))
840       IF b$="":EXIT lp2%
850       a$=a$&" "&b$
860     END REPeat lp2%
870     PRINT#outc%;"                <TR><TD ";dg$;" > "&a$&"</TD></TR>"
880   END REPeat lp%
890   PRINT#outc%;"            </TBODY>"
900   PRINT#outc%;"          </TABLE>"
910   copy_file "dev8_extras_html_versions_footer",outc%
920   PRINT#outc%;"<br><br>"
930   print#outc%,"Page last updated on ";today$;"."
940   CLOSE#outc%
950   CLOSE#inc%
960 END DEFine make_into_html
970 :
980 DEFine PROCedure copy_file (mfile$,chan%)
990 REMark copy content of text file mfile$ into channel chan%
1000 LOCal inc%,lp%,a$
1010   inc%=FOP_IN(mfile$)
1020   IF inc%<0:RETurn                         : REMark open file failed
1030   REPeat lp%
1040     IF EOF(#inc%):EXIT lp%
1050     INPUT#inc%,a$                       : REMark get one line
1060     PRINT#chan%,a$                      : REMark and print it
1070   END REPeat lp%
1080   CLOSE#inc%
1090 END DEFine copy_file
1100 :
1110 DEFine FuNction ginput$
1120 LOCal lp%,a$
1130   REPeat lp%
1140     IF EOF(#inc%):RETurn ""
1150     INPUT#inc%,a$
1160     a$=STRIP_SPACES$(a$)
1170     IF "-------" INSTR a$:RETurn ""
1180     IF a$<>"":RETurn a$
1190   END REPeat lp%
1200 END DEFine ginput$
1210 :
1220 DEFine PROCedure p
1230   make_into_html  "dev8_smsq_smsq_version_asm";"NFA8_html_versions.html"
1240   PRINT "done"\\"copied to NFA8_html_versions.html"
1250   IF "quit" instr cmd$:QUIT
1260 END DEFine p
1270 :
1280 DEFine PROCedure sa
1290   SAVE_O dev8_extras_html_versions_bas
1300 END DEFine sa
1310 :
1320 DEFine FuNction strip_tab$(a$)
1330 REMark returns string stripped of tab and spaces
1340 LOCal b$,t%,lp%
1350   b$=STRIP_SPACES$(a$)
1360   IF b$="":RETurn ""
1370   REPeat lp%
1380     IF b$(1)<> CHR$(9):RETurn STRIP_SPACES$(b$)
1390     IF LEN(b$)=1:RETurn ""
1400     b$=b$(2 TO)
1410   END REPeat lp%
1420   RETurn ""
1430 END DEFine strip_tab$
