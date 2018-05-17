100 REMark plase read the "howto" doc before using this program
110 :
120 DEFine PROCedure p
130   make_it_full
140 END DEFine p
150 :
160 DEFine PROCedure qmake_it
170   EW qmake ;'\c dev8_smsq_qxl_host_ \b \0 all \1 \2'
180   EW qmake ;'\c dev8_smsq_qxl_hwinit_ \b \0 all \1 \2'
190   EW qmake ;'\c dev8_smsq_qxl_nasty_e_ \b \0 all \1 \2'
200   EW qmake ;'\c dev8_smsq_qxl_driver_ql_ \b \0 all \1 \2 '
210   EW qmake ;'\c dev8_smsq_qxl_driver_16_ \b \0 all \1 \2 '
220   EW qmake ;'\c dev8_smsq_qxl_driver_most_ \b \0 all \1 \2 '
230   EW qmake ;'\c dev8_smsq_qxl_driver_nd_ \b \0 all \1 \2'
240   EW qmake ;'\c dev8_smsq_qxl_procs_ \b \0 all \1 \2'
250   EW qmake ;'\c dev8_smsq_qxl_driver_dv3e_ \b \0 all \1 \2'
260   EW qmake ;'\c dev8_smsq_qxl_kbd_lang_ \b \0 all \1 \2'
270   EW qmake ;'\c dev8_smsq_qxl_ecache_ \b \0 all \1 \2'
280   EW qmake ;'\c dev8_smsq_qxl_sysspr_ \b \0 all \1 \2'
290 END DEFine qmake_it
300 :
310 DEFine PROCedure qmake_it_full
320   qmake_it
330 :
340   EW qmake ;'\c dev8_smsq_smsq_loader_ \b \0 all \1 \2'
350   EW qmake ;'\c dev8_smsq_smsq_ \b \0 all \1 \2'
360   EW qmake ;'\c dev8_smsq_smsq_cache_ \b \0 all \1 \2'
370   EW qmake ;'\c dev8_smsq_sbas_ \b \0 all \1 \2'
380   EW qmake ;'\c dev8_smsq_smsq_lang_ \b \0 all \1 \2'
390   EW qmake ;'\c dev8_smsq_sbas_lang_ \b \0 all \1 \2'
400   EW qmake ;'\c dev8_smsq_sbas_procs_ \b \0 all \1 \2'
410   EW qmake ;'\c dev8_smsq_smsq_wman_ \b \0 all \1 \2'
420   EW qmake ;'\c dev8_smsq_smsq_hotkey_ \b \0 all \1 \2'
430 END DEFine qmake_it_full
440 :
450 DEFine PROCedure make_it
460   PRINT "Compiling QXL specific parts"
470   compile_and_check 'dev8_smsq_qxl_host_link'
480   compile_and_check 'dev8_smsq_qxl_hwinit_link'
490   compile_and_check 'dev8_smsq_qxl_nasty_e_link'
500   compile_and_check 'dev8_smsq_qxl_driver_ql_link'
510   compile_and_check 'dev8_smsq_qxl_driver_16_link'
520   compile_and_check 'dev8_smsq_qxl_driver_most_link'
530   compile_and_check 'dev8_smsq_qxl_driver_nd_link'
540   compile_and_check 'dev8_smsq_qxl_procs_link'
550   compile_and_check 'dev8_smsq_qxl_driver_dv3e_link'
560   compile_and_check 'dev8_smsq_qxl_kbd_lang_link'
570   compile_and_check 'dev8_smsq_qxl_ecache_link'
580   compile_and_check 'dev8_smsq_qxl_sysspr_link'
590   PRINT "QXL specific parts finished"
600   IF my_error
610     PRINT "There were ";my_error;" errors !!! "
620   ELSE
630     PRINT  "- No errors -"
640   END IF
650 END DEFine qmake_it
660 :
670 DEFine PROCedure make_it_full
680   make_it
690 :
700   PRINT:PRINT "Compiling SMSQ generic parts"
710   compile_and_check 'dev8_smsq_smsq_loader_link'
720   compile_and_check 'dev8_smsq_smsq_link'
730   compile_and_check 'dev8_smsq_smsq_cache_link'
740   compile_and_check 'dev8_smsq_sbas_link'
750   compile_and_check 'dev8_smsq_smsq_lang_link'
760   compile_and_check 'dev8_smsq_sbas_lang_link'
770   compile_and_check 'dev8_smsq_sbas_procs_link'
780   compile_and_check 'dev8_smsq_smsq_wman_link'
790   compile_and_check 'dev8_smsq_smsq_hotkey_link'
800   PRINT "SMSQ Generic parts finished"
810   IF my_error
820     PRINT "There were ";my_error;" errors !!! "
830   ELSE
840     PRINT  "- No errors -"
850   END IF
860 END DEFine qmake_it_full
870 :
880 DEFine PROCedure sa
890   SAVE_O dev8_smsq_qxl_make_bas
900 END DEFine sa
910 :
920 DEFine PROCedure compile_and_check (a$)
930 LOCal chan%,is_ok$,ch_len
940   EXEP_W "make";a$&" -w -l"
950   is_ok$=a$(1 TO LEN(a$)-4)
960   chan%=FOP_IN(is_ok$&'log'):  REMark open log file
970   IF chan%<0
980      PRINT a$& " : "&"couldn't open log file"
990      RETurn
1000   END IF
1010   ch_len=FLEN(#chan%)
1020   GET#chan%\ch_len-10
1030   INPUT#chan%,is_ok$
1040   CLOSE#chan%
1050   IF NOT is_ok$=="No errors"
1060     PRINT a$ & " : Compile/link error!"
1070     my_error=my_error+1
1080   ELSE
1090    PRINT a$ & " : OK"
1100   END IF
1110 END DEFine compile_and_check
1120 :
1130 DEFine PROCedure t
1140   LRUN dev8_smsq_qxl_flp_bas
1150 END DEFine t
