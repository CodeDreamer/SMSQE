100 REMark Please read the "How To" document before using these procedures
110 :
120 DEFine PROCedure qmake_it
130   DELETE dev8_dv3_atari_lib
140   EW qmake ;'\c dev8_smsq_atari_hwinit_ \b \0 all \1 \2'
150   EW qmake ;'\c dev8_smsq_atari_nasty_ \b \0 all \1 \2'
160   EW qmake ;'\c dev8_smsq_atari_driver_ql_ \b \0 all \1 \2'
170   EW qmake ;'\c dev8_smsq_atari_driver_mo_ \b \0 all \1 \2'
180   EW qmake ;'\c dev8_smsq_atari_driver_ser_ \b \0 all \1 \2'
190   EW qmake ;'\c dev8_smsq_atari_driver_dv3_ \b \0 all \1 \2'
200   EW qmake ;'\c dev8_smsq_atari_kbd_lang_ \b \0 all \1 \2'
210   EW qmake ;'\c dev8_smsq_atari_sysspr_ \b \0 all \1 \2'
220   EW qmake ;'\c dev8_sys_boot_st_host_ \b \0 all \1 \2'
230 END DEFine qmake_it
240 :
250 DEFine PROCedure qmake_it_full
260   qmake_it
270   EW qmake ;'\c dev8_smsq_smsq_loader_ \b \0 all \1 \2'
280   EW qmake ;'\c dev8_smsq_smsq_fh_ \b \0 all \1 \2'
290   EW qmake ;'\c dev8_smsq_smsq_ \b \0 all \1 \2'
300   EW qmake ;'\c dev8_smsq_smsq_cache_ \b \0 all \1 \2'
310   EW qmake ;'\c dev8_smsq_sbas_ \b \0 all \1 \2'
320   EW qmake ;'\c dev8_smsq_smsq_lang_ \b \0 all \1 \2'
330   EW qmake ;'\c dev8_smsq_sbas_lang_ \b \0 all \1 \2'
340   EW qmake ;'\c dev8_smsq_sbas_procs_ \b \0 all \1 \2'
350   EW qmake ;'\c dev8_smsq_smsq_wman_ \b \0 all \1 \2'
360   EW qmake ;'\c dev8_smsq_smsq_hotkey_ \b \0 all \1 \2'
370 END DEFine qmake_it_full
380 :
390 DEFine PROCedure make_it
400 REMark compile only atari specific parts
410   CLS:PRINT 'Compiling Atari specific parts...'
420   DELETE dev8_dv3_atari_lib
430   my_error=0
440   compile_and_check 'dev8_smsq_atari_hwinit_link'
450   compile_and_check 'dev8_smsq_atari_nasty_link'
460   compile_and_check 'dev8_smsq_atari_driver_ql_link'
470   compile_and_check 'dev8_smsq_atari_driver_mo_link'
480   compile_and_check 'dev8_smsq_atari_driver_ser_link'
490   compile_and_check 'dev8_smsq_atari_driver_dv3_link'
500   compile_and_check 'dev8_smsq_atari_kbd_lang_link'
510   compile_and_check 'dev8_smsq_atari_sysspr_link'
520   compile_and_check 'dev8_sys_boot_st_host_link'
530   PRINT "Atari specific parts finished! "
540   IF my_error
550     PRINT "There were ";my_error;" errors !!! "
560   ELSE
570     PRINT  "- No errors -"
580   END IF
590 END DEFine make_it
600 :
610 DEFine PROCedure make_it_full
620   make_it
630   PRINT
640   PRINT "Compiling SMSQ/E generic parts ..."
650   compile_and_check 'dev8_smsq_smsq_loader_link'
660   compile_and_check 'dev8_smsq_smsq_fh_link'
670   compile_and_check 'dev8_smsq_smsq_link'
680   compile_and_check 'dev8_smsq_smsq_cache_link'
690   compile_and_check 'dev8_smsq_sbas_link'
700   compile_and_check 'dev8_smsq_smsq_lang_link'
710   compile_and_check 'dev8_smsq_sbas_lang_link'
720   compile_and_check 'dev8_smsq_sbas_procs_link'
730   compile_and_check 'dev8_smsq_smsq_wman_link'
740   compile_and_check 'dev8_smsq_smsq_hotkey_link'
750   PRINT "Generic parts finished! "
760   IF my_error
770     PRINT "There were ";my_error;" errors !!! "
780   ELSE
790     PRINT  "- No errors -"
800   END IF
810 END DEFine qmake_it_full
820 :
830 DEFine PROCedure sa
840   SAVE_O dev8_smsq_atari_make_bas
850 END DEFine sa
860 :
870 DEFine PROCedure compile_and_check (a$)
880 LOCal chan%,is_ok$,ch_len
890   EXEP_W "make";a$&" -w -l"
900   is_ok$=a$(1 TO LEN(a$)-4)
910   chan%=FOP_IN(is_ok$&'log'):  REMark open log file
920   IF chan%<0
930      PRINT a$& " : "&"couldn't open log file"
940      RETurn
950   END IF
960   ch_len=FLEN(#chan%)
970   GET#chan%\ch_len-10
980   INPUT#chan%,is_ok$
990   CLOSE#chan%
1000   IF NOT is_ok$=="No errors"
1010     PRINT a$ & " : Compile/link error!"
1020     my_error=my_error+1
1030   ELSE
1040    PRINT a$ & " : OK"
1050   END IF
1060 END DEFine compile_and_check
1070 :
1080 DEFine PROCedure t
1090   LRUN dev8_smsq_atari_flp_bas
1100 END DEFine t
1110 :
1120 DEFine PROCedure p
1130   make_it_full
1140 END DEFine p
