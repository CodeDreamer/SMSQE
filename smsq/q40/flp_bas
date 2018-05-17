110 IF cmd$='':INPUT 'SMSQ/E Q40 options: Qmon Copy Version> ';cmd$
120 IF 'v' INSTR cmd$
130   INPUT 'Version (2x9x) ';v$
140   p$='Q40 V' & v$
150 END IF
160 nl$='dev8_sys_boot_null'
170 f$='dev8_sys_boot_file'
180 qr$='dev8_sys_boot_q40_rom'
190 ld$='dev8_smsq_smsq_loader'
200 hi$='dev8_smsq_q40_hwinit'
210 qm$='dev8_qmon_q40_smsq'
220 os1$='dev8_smsq_smsq_1mb_os' : REMark change for fast memory
230 ca$='dev8_smsq_smsq_cache40c'
240 caq$='dev8_smsq_q40_cache'
250 os2$='dev8_smsq_sbas_control'
260 ns1$=nl$
270 ns2$='dev8_smsq_q40_nasty'
280 ln1$='dev8_smsq_smsq_lang'
290 ln2$='dev8_smsq_q40_kbd_lang'
300 ln3$='dev8_smsq_sbas_lang'
310 ex1$='dev8_smsq_sbas_procs_x_q60'
320 ex2$='dev8_smsq_q40_driver_dv3'
330 ex3$='dev8_smsq_q40_driver_ser'
340 ex4a$='dev8_smsq_q40_driver_ql'
350 ex4b$='dev8_smsq_q40_driver_16'
360 ex4w$='dev8_smsq_smsq_wman'
370 ex5$='dev8_smsq_q40_sysspr'
375 ex6$='dev8_smsq_q40_cachemode'
380 ex9$='dev8_smsq_smsq_hotkey'
390 IF 'c' INSTR cmd$ = 0
400   IF 'q' INSTR cmd$ = 0: qm$ = nl$
410   EW f$, qr$,ld$,hi$,os1$,ca$,caq$,os2$,qm$,ns1$,ln1$,ln2$,ln3$,ex1$,ex2$,ex3$,ex4a$,ex4b$,ex4w$,ex5$,ex6$,ex9$,ns2$, 'dev8_smsq_q40_rom'; p$
420 ELSE
430   rc 'dev8_smsq_q40_flp_bas'
440   rc nl$
450   rc f$
460   rc qr$
470   rc hi$
480   rc ld$
490   rc qm$
500   rc os1$
510   rc ca$
520   rc caq$
530   rc os2$
540   rc ns1$
550   rc ns2$
560   rc ln1$
570   rc ln2$
580   rc ln3$
590   rc ex1$
600   rc ex2$
610   rc ex3$
620   rc ex4a$
630   rc ex4b$
640   rc ex4w$
650   rc ex5$
660   rc ex9$
670   rc nl$
680 END IF
690 DEFine PROCedure rc (a$)
700   COPY_O a$ TO 'ram8_' & a$ (6 TO)
710 END DEFine rcopy
720 :
730 DEFine PROCedure sa
740   SAVE_O dev8_smsq_q40_flp_bas
750 END DEFine sa
760 :
770 DEFine PROCedure t
780   LRESPR dev8_smsq_q40_rom
790 END DEFine t
