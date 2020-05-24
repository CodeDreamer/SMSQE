100 DEV_USE 8,"win1_"
110 PROG_USE "dev8_extras_exe_"
120 LRESPR dev8_extras_blinker_bin
130 LRESPR "dev8_extras_source_outptr_bin"
140 LRESPR dev8_extras_cline_bin
150 GO TO 220
160 ERT HOT_RES ('z',dev8_extras_exe_QMAC)
170 ERT HOT_REMV('z')
180 ERT HOT_RES('z',dev8_extras_exe_make)
190 ERT HOT_REMV('z')
200 ERT HOT_RES ('z',dev8_extras_exe_linker)
210 ERT HOT_REMV('z')
220 mk
230 qm
240 REMark a=ALCHP(100000)
250 li
260 EX dev8_extras_exe_smsqemake
270 REMark EX dev8_extras_source_m_60000_obj
280 HOT_GO
290 HOT_STUFF ("dev8_smsq_smsq_loader_asm dev8_smsq_smsq_loader_err dev8_smsq_smsq_loader_rel -errors")
300 DELETE dev8_smsq_smsq_loader_rel
310 :
320 DEFine PROCedure sa
330   SAVE_O dev8_boot
340 END DEFine sa
350 :
360 DEFine PROCedure e
370 EX dev8_extras_exe_smsqemake
380 END DEFine e
390 :
400 DEFine PROCedure d
410  DELETE dev8_smsq_smsq_loader_rel
420 END DEFine d
430 :
440 DEFine PROCedure mb
450   d
460   EX dev8_extras_source_make_bas;"dev8_smsq_smsq_loader_link2"
470 END DEFine mb
480 :
490 DEFine PROCedure m
500   d
510   EX dev8_extras_exe_make;"dev8_smsq_smsq_loader_link"
520 END DEFine m
530 :
540 DEFine PROCedure mt
550   d
560   EXEP make;"dev8_smsq_smsq_loader_link"
570 END DEFine mt
580 :
590 DEFine PROCedure s
600   d
610   EX dev8_extras_exe_smsqemake
620 END DEFine s
630 :
640 DEFine PROCedure di
650   DIR dev8_smsq_smsq_loader_
660 END DEFine di
670 :
680 DEFine PROCedure c
690   COPY dev8_smsq_smsq_loader_log,scr
700 END DEFine c
710 :
720 DEFine PROCedure c1
730   COPY dev8_smsq_smsq_loader_err,scr
740 END DEFine c1
750 :
760 DEFine PROCedure r
770   d
780   RESET
790 END DEFine r
800 :
810 DEFine PROCedure qm
820   ERT HOT_RES ('z',dev8_extras_exe_QMAC)
830   ERT HOT_REMV('z')
840 END DEFine qm
850 :
860 DEFine PROCedure li
870   ERT HOT_RES ('z',dev8_extras_exe_linker)
880   ERT HOT_REMV('z')
890 END DEFine li
900 :
910 DEFine PROCedure mk
920   ERT HOT_RES('z',dev8_extras_exe_make)
930   ERT HOT_REMV('z')
940 END DEFine mk
950 :
960 DEFine PROCedure e
970   EXEP "make";"dev8_smsq_smsq_loader_link"
980 END DEFine e
990 :
1000 DEFine PROCedure de
1010   EX dev8_extras_del_all_bas
1020 END DEFine de
1030 :
