100 DEFine PROCedure p
110   make_it_full
120 END DEFine p
130 :
140 REMark Please read the "How To" document before using these procedures
150 :
160 DEFine PROCedure qmake_it
170   EW qmake ;'\c dev8_smsq_gold_host_ \b \0 all \1 \2'
180   EW qmake ;'\c dev8_smsq_gold_hwinit_ \b \0 all \1 \2'
190   EW qmake ;'\c dev8_smsq_gold_nasty_ \b \0 all \1 \2'
200   EW qmake ;'\c dev8_smsq_gold_nasty_s_ \b \0 all \1 \2'
210   EW qmake ;'\c dev8_smsq_gold_driver_ql_ \b \0 all \1 \2'
220   EW qmake ;'\c dev8_smsq_gold_driver_most_ \b \0 all \1 \2'
230   EW qmake ;'\c dev8_smsq_gold_driver_nd_ \b \0 all \1 \2'
240   EW qmake ;'\c dev8_smsq_gold_driver_nds_ \b \0 all \1 \2'
250   EW qmake ;'\c dev8_smsq_gold_driver_dv3_ \b \0 all \1 \2'
260   EW qmake ;'\c dev8_smsq_gold_kbd_lang_ \b \0 all \1 \2'
270   EW qmake ;'\c dev8_smsq_gold_kbd_abc_lng_ \b \0 all \1 \2'
280   EW qmake ;'\c dev8_smsq_gold_kbd_abc_ \b \0 all \1 \2'
290   EW qmake ;'\c dev8_smsq_gold_roms_ \b \0 all \1 \2'
300   EW qmake ;'\c dev8_smsq_gold_qimi_ \b \0 all \1 \2'
310   EW qmake ;'\c dev8_smsq_gold_sysspr_ \b \0 all \1 \2'
320 END DEFine qmake_it
330 :
340 DEFine PROCedure qmake_it_full
350   qmake_it
360 :
370   EW qmake ;'\c dev8_smsq_smsq_loader_ \b \0 all \1 \2'
380   EW qmake ;'\c dev8_smsq_smsq_vers_ \b \0 all \1 \2'
390   EW qmake ;'\c dev8_smsq_smsq_ \b \0 all \1 \2'
400   EW qmake ;'\c dev8_smsq_smsq_cache_ \b \0 all \1 \2'
410   EW qmake ;'\c dev8_smsq_sbas_ \b \0 all \1 \2'
420   EW qmake ;'\c dev8_smsq_smsq_lang_ \b \0 all \1 \2'
430   EW qmake ;'\c dev8_smsq_sbas_lang_ \b \0 all \1 \2'
440   EW qmake ;'\c dev8_smsq_sbas_procs_ \b \0 all \1 \2'
450   EW qmake ;'\c dev8_smsq_smsq_wman_ \b \0 all \1 \2'
460   EW qmake ;'\c dev8_smsq_smsq_hotkey_ \b \0 all \1 \2'
470 END DEFine qmake_it_full
480 :
490 DEFine PROCedure make_it
500 REMark compile only GoldCard specific parts
510   CLS:PRINT 'Compiling GoldCard specific parts...'
520   my_error=0
530   compile_and_check 'dev8_smsq_gold_host_link'
540   compile_and_check 'dev8_smsq_gold_hwinit_link'
550   compile_and_check 'dev8_smsq_gold_nasty_link'
560   compile_and_check 'dev8_smsq_gold_nasty_s_link'
570   compile_and_check 'dev8_smsq_gold_driver_ql_link'
580   compile_and_check 'dev8_smsq_gold_driver_most_link'
590   compile_and_check 'dev8_smsq_gold_driver_nd_link'
600   compile_and_check 'dev8_smsq_gold_driver_nds_link'
610   compile_and_check 'dev8_smsq_gold_driver_dv3_link'
620   compile_and_check 'dev8_smsq_gold_kbd_lang_link'
630   compile_and_check 'dev8_smsq_gold_kbd_abc_lng_link'
640   compile_and_check 'dev8_smsq_gold_kbd_abc_link'
650   compile_and_check 'dev8_smsq_gold_roms_link'
660   compile_and_check 'dev8_smsq_gold_qimi_link'
670   compile_and_check 'dev8_smsq_gold_sysspr_link'
680   PRINT "GoldCard specific parts finished"
690   IF my_error
700     PRINT "There were ";my_error;" errors !!! "
710   ELSE
720     PRINT  "- No errors -"
730   END IF
740 END DEFine make_it
750 :
760 DEFine PROCedure make_it_full
770   make_it
780 :
790   PRINT
800   PRINT "Compiling SMSQ/E generic parts ..."
810   compile_and_check 'dev8_smsq_smsq_loader_link'
820   compile_and_check 'dev8_smsq_smsq_vers_link'
830   compile_and_check 'dev8_smsq_smsq_link'
840   compile_and_check 'dev8_smsq_smsq_cache_link'
850   compile_and_check 'dev8_smsq_sbas_link'
860   compile_and_check 'dev8_smsq_smsq_lang_link'
870   compile_and_check 'dev8_smsq_sbas_lang_link'
880   compile_and_check 'dev8_smsq_sbas_procs_link'
890   compile_and_check 'dev8_smsq_smsq_wman_link'
900   compile_and_check 'dev8_smsq_smsq_hotkey_link'
910   PRINT "Generic parts finished! "
920   IF my_error
930     PRINT "There were ";my_error;" errors !!! "
940   ELSE
950     PRINT  "- No errors -"
960   END IF
970 END DEFine make_it_full
980 :
990 DEFine PROCedure sa
1000   SAVE_O dev8_smsq_gold_make_bas
1010 END DEFine sa
1020 :
1030 DEFine PROCedure compile_and_check (a$)
1040 LOCal chan%,is_ok$,ch_len
1050   EXEP_W "make";a$&" -w -l"
1060   is_ok$=a$(1 TO LEN(a$)-4)
1070   chan%=FOP_IN(is_ok$&'log'):  REMark open log file
1080   IF chan%<0
1090      PRINT a$& " : "&"couldn't open log file"
1100      RETurn
1110   END IF
1120   ch_len=FLEN(#chan%)
1130   GET#chan%\ch_len-10
1140   INPUT#chan%,is_ok$
1150   CLOSE#chan%
1160   IF NOT is_ok$=="No errors"
1170     PRINT a$ & " : Compile/link error!"
1180     my_error=my_error+1
1190   ELSE
1200    PRINT a$ & " : OK"
1210   END IF
1220 END DEFine compile_and_check
1230 :
1240 DEFine PROCedure t
1250   LRUN dev8_smsq_gold_flp_bas
1260 END DEFine t
