100 DEFine PROCedure p
110   make_it_full
120 END DEFine p
130 :
140 DEFine PROCedure make_it
150 REMark compile only Aurora specific parts
160   CLS:PRINT 'Compiling Aurora specific parts...'
170   my_error=0
180   compile_and_check 'dev8_smsq_gold_host_link'
190   compile_and_check 'dev8_smsq_gold_hwinit_link'
200   compile_and_check 'dev8_smsq_gold_nasty_link'
210   compile_and_check 'dev8_smsq_gold_nasty_s_link'
220   compile_and_check 'dev8_smsq_gold_driver_ql_link'
230   compile_and_check 'dev8_smsq_aurora_driver_8_link'
240   compile_and_check 'dev8_smsq_gold_driver_most_link'
250   compile_and_check 'dev8_smsq_gold_driver_nd_link'
260   compile_and_check 'dev8_smsq_gold_driver_nds_link'
270   compile_and_check 'dev8_smsq_gold_driver_dv3_link'
280   compile_and_check 'dev8_smsq_gold_kbd_lang_link'
290   compile_and_check 'dev8_smsq_gold_kbd_abc_lng_link'
300   compile_and_check 'dev8_smsq_gold_kbd_abc_link'
310   compile_and_check 'dev8_smsq_gold_roms_link'
320   compile_and_check 'dev8_smsq_gold_qimi_link'
330   compile_and_check 'dev8_smsq_aurora_sysspr_link'
340   compile_and_check 'dev8_smsq_gold_sysspr_link'
350   PRINT "Aurora specific parts finished"
360   IF my_error
370     PRINT "There were ";my_error;" errors !!! "
380   ELSE
390     PRINT  "- No errors -"
400   END IF
410 END DEFine make_it
420 :
430 DEFine PROCedure make_it_full
440   make_it
450 :
460   PRINT
470   PRINT "Compiling SMSQ/E generic parts ..."
480   compile_and_check 'dev8_smsq_smsq_loader_link'
490   compile_and_check 'dev8_smsq_smsq_vers_link'
500   compile_and_check 'dev8_smsq_smsq_link'
510   compile_and_check 'dev8_smsq_smsq_cache_link'
520   compile_and_check 'dev8_smsq_sbas_link'
530   compile_and_check 'dev8_smsq_smsq_lang_link'
540   compile_and_check 'dev8_smsq_sbas_lang_link'
550   compile_and_check 'dev8_smsq_sbas_procs_link'
560   compile_and_check 'dev8_smsq_smsq_wman_link'
570   compile_and_check 'dev8_smsq_smsq_hotkey_link'
580   compile_and_check 'dev8_smsq_smsq_home_link'
590   compile_and_check 'dev8_smsq_smsq_fh_link'
600   compile_and_check 'dev8_smsq_smsq_1mb_link'
610   PRINT "Generic parts finished! "
620   IF my_error
630     PRINT "There were ";my_error;" errors !!! "
640   ELSE
650     PRINT  "- No errors -"
660   END IF
670 END DEFine make_it_full
680 :
690 DEFine PROCedure sa
700   SAVE_O dev8_smsq_aurora_make_bas
710 END DEFine sa
720 :
730 DEFine PROCedure compile_and_check (a$)
740 LOCal chan%,is_ok$,ch_len
750   EXEP_W "make";a$&" -w -l"
760   is_ok$=a$(1 TO LEN(a$)-4)
770   chan%=FOP_IN(is_ok$&'log'):  REMark open log file
780   IF chan%<0
790      PRINT a$& " : "&"couldn't open log file"
800      RETurn
810   END IF
820   ch_len=FLEN(#chan%)
830   GET#chan%\ch_len-10
840   INPUT#chan%,is_ok$
850   CLOSE#chan%
860   IF NOT is_ok$=="No errors"
870     PRINT a$ & " : Compile/link error!"
880     my_error=my_error+1
890   ELSE
900    PRINT a$ & " : OK"
910   END IF
920 END DEFine compile_and_check
930 :
940 DEFine PROCedure t
950   LRUN dev8_smsq_aurora_flp_bas
960 END DEFine t
