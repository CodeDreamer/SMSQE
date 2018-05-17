100 REMark Please read the "How To" document before using these procedures
110 :
120 DEFine PROCedure qmake_it
130   EW qmake ;'\c dev8_smsq_q40_hwinit_ \b \0 all \1 \2'
140   EW qmake ;'\c dev8_smsq_q40_nasty_ \b \0 all \1 \2'
150   EW qmake ;'\c dev8_smsq_q40_cache_ \b \0 all \1 \2'
160   EW qmake ;'\c dev8_smsq_q40_driver_ser_ \b \0 all \1 \2'
170   EW qmake ;'\c dev8_smsq_q40_driver_dv3_ \b \0 all \1 \2'
180   EW qmake ;'\c dev8_smsq_q40_driver_ql_ \b \0 all \1 \2'
190   EW qmake ;'\c dev8_smsq_q40_driver_16_ \b \0 all \1 \2'
200   EW qmake ;'\c dev8_smsq_q40_kbd_lang_ \b \0 all \1 \2'
210   EW qmake ;'\c dev8_smsq_q40_sysspr_ \b \0 all \1 \2'
220   EW qmake ;'\c dev8_smsq_sbas_procs_q60_ \b \0 all \1 \2'
230 END DEFine qmake_it
240 :
250 DEFine PROCedure qmake_it_full
260   qmake_it
270 REMark SMSQ/E generic part
280   EW qmake ;'\c dev8_smsq_smsq_loader_ \b \0 all \1 \2'
290   EW qmake ;'\c dev8_smsq_smsq_1mb_ \b \0 all \1 \2' : REMark change for fast memory
300   REMark EW qmake ;'\c dev8_smsq_smsq_ \b \0 all \1 \2' : REMark before change for fast memory
310   EW qmake ;'\c dev8_smsq_smsq_cache40c_ \b \0 all \1 \2'
320   EW qmake ;'\c dev8_smsq_sbas_ \b \0 all \1 \2'
330   EW qmake ;'\c dev8_smsq_smsq_lang_ \b \0 all \1 \2'
340   EW qmake ;'\c dev8_smsq_sbas_lang_ \b \0 all \1 \2'
350   EW qmake ;'\c dev8_smsq_smsq_wman_ \b \0 all \1 \2'
360   EW qmake ;'\c dev8_smsq_smsq_hotkey_ \b \0 all \1 \2'
370 END DEFine qmake_it_full
380 :
390 DEFine PROCedure make_it
400 REMark compile only the Q40 specific parts
410   CLS:PRINT 'compiling Q40 specific parts...'
420   my_error=0
430   compile_and_check 'dev8_smsq_q40_hwinit_link'
440   compile_and_check 'dev8_smsq_q40_nasty_link'
450   compile_and_check 'dev8_smsq_q40_cache_link'
460   compile_and_check 'dev8_smsq_smsq_cache40c_link'
470   compile_and_check 'dev8_smsq_q40_driver_ser_link'
480   compile_and_check 'dev8_smsq_q40_driver_dv3_link'
490   compile_and_check 'dev8_smsq_q40_driver_ql_link'
500   compile_and_check 'dev8_smsq_q40_driver_16_link'
510   compile_and_check 'dev8_smsq_q40_kbd_lang_link'
520   compile_and_check 'dev8_smsq_sbas_procs_q60_link'
530   compile_and_check 'dev8_smsq_q40_sysspr_link'
535   compile_and_check 'dev8_smsq_q40_cachemode_link'
540   PRINT "Q40 specific parts finished."
550   IF my_error
560      PRINT "There were ";my_error;" errors !!! "
570   ELSE
580     PRINT  "- No errors -"
590   END IF
600 END DEFine make_it
610 :
620 DEFine PROCedure make_it_full
630   REMark compile SMSQ/E and the Q40 specific parts
640   REMark start with Q40 parts
650   make_it
660   PRINT:PRINT "compiling SMSQ/E generic parts ..."
670   REMark SMSQ/E generic part
680   compile_and_check 'dev8_smsq_smsq_loader_link'
690   compile_and_check 'dev8_smsq_smsq_1mb_link': REMark change for fast memory
700   compile_and_check 'dev8_smsq_sbas_link'
710   compile_and_check 'dev8_smsq_smsq_lang_link'
720   compile_and_check 'dev8_smsq_sbas_lang_link'
730   compile_and_check 'dev8_smsq_smsq_wman_link'
740   compile_and_check 'dev8_smsq_smsq_hotkey_link'
750   compile_and_check 'dev8_smsq_sbas_procs_link'
760   PRINT 'finished'
770   IF my_error
780   PRINT "There were ";my_error;" errors !!! "
790   ELSE
800     PRINT  "- No errors -"
810   END IF
820 END DEFine make_it_full
830 :
840 DEFine PROCedure sa
850   SAVE_O dev8_smsq_q40_make_bas
860 END DEFine sa
870 :
880 DEFine PROCedure compile_and_check (a$)
890 LOCal chan%,is_ok$,ch_len
900   PRINT a$;
910   EXEP_W "make";a$&" -w -l"
920   is_ok$=a$(1 TO LEN(a$)-4)
930   chan%=FOP_IN(is_ok$&'log'):  REMark open log file
940   IF chan%<0
950      PRINT a$& " : "&"couldn't open log file"
960      RETurn
970   END IF
980   ch_len=FLEN(#chan%)
990   GET#chan%\ch_len-10
1000   INPUT#chan%,is_ok$
1010   CLOSE#chan%
1020   IF NOT is_ok$=="No errors"
1030     PRINT " : Compile/link error!"
1040     my_error=my_error+1
1050   ELSE
1060     PRINT " -OK"
1070   END IF
1080 END DEFine compile_and_check
1090 :
1100 DEFine PROCedure p
1110   make_it_full
1120 END DEFine p
1130 :
1140 DEFine PROCedure T
1150   LRUN dev8_smsq_q40_flp_bas
1160 END DEFine T
