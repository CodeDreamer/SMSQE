100 OPEN #1,'con_300x12a40x30': CLS: BORDER 1,4
110 IF cmd$='':INPUT 'SMSQ/E Milan options: Qmon Copy> ';cmd$
140 f$='win1_sys_boot_file'
150 ah$='win1_sys_boot_st_host'
160 ld$='win1_smsq_milan_loader'
170 qm$='win1_qmon_milan_smsq'
180 os1f$='win1_smsq_smsq_fh_os'
190 os1$='win1_smsq_smsq_os'
200 ca$='win1_smsq_smsq_cache'
210 os2$='win1_smsq_sbas_control'
220 ns1$='win1_smsq_milan_nasty'
230 ln1$='win1_smsq_smsq_lang'
240 ln2$='win1_smsq_milan_kbd_lang'
250 ln3$='win1_smsq_sbas_lang'
260 ex1$='win1_smsq_sbas_procs_x'
270 ex2$='win1_smsq_milan_driver_dv3'
280 ex3$='win1_smsq_milan_driver_ser'
290 ex4$='win1_smsq_milan_driver_con'
310 ex9$='win1_smsq_smsq_hotkey'
320 nl$='win1_sys_boot_null'
340 IF 'c' INSTR cmd$ = 0
350   IF 'q' INSTR cmd$ = 0: qm$ = nl$
360   IF 'n' INSTR cmd$ = 0
370     rem EW f$, ah$,ld$,os1f$,os1$,ca$,os2$,qm$,ns1$,ln1$,ln2$,ln3$,ex1$,ex2$,ex3$,ex4$,ex9$, 'flp1_auto_SMSQ.PRG'
375     EW f$, ah$,ld$,qm$, 'flp1_auto_SMSQ.PRG'
380   ELSE
390     rem EW p$, ah$,ld$,os1f$,os1$,ca$,os2$,qm$,ns1$,ln1$,ln2$,ln3$,ex1$,ex2$,ex3$,ex4$,ex9$, 'flp1_*d2d';'smsq'
400   END IF
410 ELSE
420   rc 'win1_smsq_atari_flp_bas'
430   rc p$
440   rc f$
450   rc ah$
460   rc ld$
470   rc qm$
480   rc os1f$
490   rc os1$
500   rc ca$
510   rc os2$
520   rc ns1$
530   rc ln1$
540   rc ln2$
550   rc ln3$
560   rc ex1$
570   rc ex2$
580   rc ex3$
590   rc ex4$
600   rc ex5$
610   rc ex9$
620   rc nl$
630 END IF
650 DEFine PROCedure rc (a$)
660   COPY_O a$ TO 'ram8_' & a$ (6 TO)
670 END DEFine rcopy
