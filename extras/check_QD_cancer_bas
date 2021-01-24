100 REMark Can be used to find files where QD has added unwanted Linefeeds towards the end.
110 REMark Copyright (C) W. Lenerz 2020.
120 REMark Needs the qd_bas extension.
130 :
140 DEFine PROCedure check_files_for_QD_cancer (report_chan%,mdir$,extn$)
150 REMark device$ is global
160 LOCal chan%,lp%,myfile$,a$,t%,res,temp
170   FOR lp%=1 TO 50
180     myfile$="ram1_check_QD_cancer"&RND(1 TO 80)&RND (1 TO 80) & RND (1 TO 50)
190     chan%=FOP_OVER (myfile$)            : REMark try to open unique file
200     IF chan%>0:EXIT lp%
210   END FOR lp%
220   IF chan%<0:RETurn chan%               : REMark ooops!!!!
230   WDIR#chan%,mdir$                      : REMark dir of this rep in file
240   GET#chan%\0                           : REMark reset file pointer to start
250   REPeat lp%
260     IF EOF(#chan%):EXIT lp%
270     INPUT#chan%,a$                      : REMark get filename
280     IF a$="":NEXT lp%
290     a$=device$&a$                       : REMark device $ if from caller
300     global_count = global_count+1
310     t%= ' ->' INSTR a$                  : REMark is it a subdir?
320     IF t%
330       check_files_for_QD_cancer report_chan%,a$(1 TO t%-1),extn$: REMark yes, treat it
340       NEXT lp%
350     END IF
360     IF endswith%(a$,extn$)
365 REMark IF a$=="DEV8_smsq_q68_hwinit_asm":QMON#1
370        matching_count=matching_count+1
380        res=QD_BAD(a$)
390        IF res AND res<>$10002
395 kk=res
400          temp=INT(res/65536)
410          res=res-(temp*65536)
420          PRINT#report_chan%,temp,res,HEX$(kk,32),a$
430        END IF
440     END IF
450   END REPeat lp%
460   CLOSE#chan%
470   DELETE myfile$
480 END DEFine check_files_for_QD_cancer
490 :
500 DEFine FuNction treat (device$)
510 LOCal lp%
520   chan%=FOP_OVER ("ram1_check_QD_cancer")       : REMark try to open unique file
530   IF chan%<0:RETurn chan%                       : REMark ooops!!!!
540   PRINT#chan%,"last","inter","name"
550   check_files_for_QD_cancer chan%,device$,"_asm"
560   CLOSE#chan%
570   RETurn 1
580 END DEFine treat
590 :
600 DEFine FuNction endswith% (a$,extn$)
610 REMark checks whether a$ ends with extn$. returns 1 if so, else 0
620 LOCal t%,exlen%
630   t%=LEN(a$)
640   exlen%=LEN(extn$)
650   IF exlen%=0:RETurn 1
660   IF t%<exlen%:RETurn 0           : REMark filename is shorter than extension
670   RETurn a$(t%-exlen%+1 TO)==extn$
680 END DEFine endswith%
690 :
700 DEFine PROCedure p
710   global_count=0
720   matching_count=1
730   CLS
740   CLOSE
750   PRINT treat ("dev8_"),global_count,matching_count
760 END DEFine p
770 :
780 DEFine PROCedure sa
790   SAVE_O dev8_extras_check_QD_cancer_bas
800 END DEFine sa
810 :
