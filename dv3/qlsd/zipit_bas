100 p
110 DEFine FuNction make_targets$
120 LOCal a$,b$,tot$,s$,d$,lp%
130   a$=""
140   s$="dev1_q68_qlsd2_"
150   d$="ram4_QLSD_"
160   tot$=""
170   REPeat lp%
180     READ a$
190 PRINT a$
200     IF a$="":EXIT lp%
210     COPY_O s$&a$,d$&a$
220     tot$=tot$&" "&d$&a$
230   END REPeat lp%
240   RETurn tot$
250 END DEFine
260 :
270 DEFine PROCedure source_targets
280   RESTORE 280
290   DATA "win_init_asm"
300   DATA "fpart_asm"
310   DATA "rsect_asm"
320   DATA "wsect_asm"
330   DATA "sndcmd_asm"
340   DATA "hd_hold_asm"
350   DATA "keys"
360   DATA "rom_asm"
370   DATA "addfd_asm"
380   DATA "basic_asm"
390   DATA "getstr_asm"
400   DATA "link_asm"
410   DATA "version_asm"
420   DATA "open_asm"
430   DATA "qlf_table_asm"
440   DATA "setfd_asm"
450   DATA "redef_asm"
460   DATA "inicrd_asm"
470   DATA "drv_cct"
480   DATA "dv3_link"
490   DATA "txt"
500   DATA "zipit_bas"
510   DATA ""
520 END DEFine source_targets
530 :
540 DEFine PROCedure targets
550   RESTORE 550
560   DATA "driver_bin"
570   DATA "txt"
580   DATA "doc"
590   DATA ""
600 END DEFine targets
610 :
620 DEFine PROCedure s
630 REMark this resets the data etc dirs to acceptable values
640   DEST_USE dev1_
650   DATA_USE dev1_basic_
660   PROG_USE dev1_progs_
670 END DEFine s
680 :
690 DEFine PROCedure do_zip (zip$)
700   DEST_USE "dev1_"  :PROG_USE "dev1_" :DATA_USE "dev1_"
710   EW dev1_progs_zip;zip$
720   s
730 END DEFine do_zip
740 :
750 DEFine PROCedure p
760   DELETE 'dev1_q68_qlsd2_zip'
770   zip$=' -Q4j9 '&'dev1_q68_qlsd2'
780   targets
790   zip$=zip$&make_targets$
800   COPY_O "nfa1_qlsd_qlsd.pdf", "ram4_qlsd.pdf"
810   zip$=zip$& " ram4_qlsd.pdf"
820   do_zip zip$
830   DELETE 'dev1_q68_qlsd2_source_zip'
840   zip$=' -Q4j9 '&'dev1_q68_qlsd2_source_zip'
850   source_targets
860   zip$=zip$&make_targets$
870   do_zip zip$
880   COPY_O "dev1_q68_qlsd2_zip","nfa1_qlsd.zip"
890   COPY_O "dev1_q68_qlsd2_source_zip","nfa1_qlsdsrc.zip"
900 END DEFine p
910 :
920 DEFine PROCedure sa
930   SAVE_O dev1_Q68_QLSD2_zipit_bas
940 END DEFine sa
