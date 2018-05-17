100 OPEN #1,'con_300x12a40x30': CLS: BORDER 1,4
110 IF cmd$='':INPUT 'SMSQ/E Atari options: Qmon Copy New Mono> ';cmd$
120 p$='dev8_sys_boot_st_flp'
130 f$='dev8_sys_boot_file'
140 ah$='dev8_sys_boot_st_host'
150 ld$='dev8_smsq_smsq_loader'
160 hi$='dev8_smsq_atari_hwinit'
170 qm$='dev8_qmon_atari_smsq'
180 os1f$='dev8_smsq_smsq_fh_os'
190 os1$='dev8_smsq_smsq_os'
200 ca$='dev8_smsq_smsq_cache'
210 os2$='dev8_smsq_sbas_control'
220 ns1$='dev8_smsq_atari_nasty'
230 ln1$='dev8_smsq_smsq_lang'
240 ln2$='dev8_smsq_atari_kbd_lang'
250 ln3$='dev8_smsq_sbas_lang'
260 ex1$='dev8_smsq_sbas_procs_x'
270 ex2$='dev8_smsq_atari_driver_dv3'
280 ex3$='dev8_smsq_atari_driver_ser'
290 ex4$='dev8_smsq_atari_driver_mono'
300 ex5$='dev8_smsq_atari_driver_ql'
310 ex6$='dev8_smsq_smsq_wman'
320 ex7$='dev8_smsq_smsq_hotkey'
330 ex8$='dev8_smsq_atari_sysspr'
340 nl$='dev8_sys_boot_null'
350 IF 'c' INSTR cmd$ = 0
360   IF 'q' INSTR cmd$ = 0: qm$ = nl$
370   IF 'm' INSTR cmd$ = 0: ex4$ = nl$
380   IF 'n' INSTR cmd$ = 0
390   EW f$, ah$,ld$,hi$,os1f$,os1$,ca$,os2$,qm$,ns1$,ln1$,ln2$,ln3$,ex1$,ex2$,ex3$,ex4$,ex5$,ex6$,ex7$,ex8$, 'dev8_smsq_atari_SMSQ.PRG'
400   ELSE
410     EW p$, ah$,ld$,hi$,os1f$,os1$,ca$,os2$,qm$,ns1$,ln1$,ln2$,ln3$,ex1$,ex2$,ex3$,ex4$,ex5$,ex6$,ex7$,ex8$, 'flp1_*d2d';'smsq'
420   END IF
430 ELSE
440   rc 'dev8_smsq_atari_flp_bas'
450   rc p$
460   rc f$
470   rc ah$
480   rc ld$
490   rc hi$
500   rc qm$
510   rc os1f$
520   rc os1$
530   rc ca$
540   rc os2$
550   rc ns1$
560   rc ln1$
570   rc ln2$
580   rc ln3$
590   rc ex1$
600   rc ex2$
610   rc ex3$
620   rc ex4$
630   rc ex5$
640   rc ex6$
650   rc ex7$
660   rc ex8$
670   rc nl$
680 END IF
690 DEFine PROCedure rc (a$)
700   COPY_O a$ TO 'ram8_' & a$ (6 TO)
710 END DEFine rcopy
720 :
730 DEFine PROCedure sa
740   SAVE_O dev8_smsq_atari_flp_bas
750 END DEFine sa
760 :
