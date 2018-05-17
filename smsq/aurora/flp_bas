100 OPEN #1,'con_250x12a40x50': CLS: BORDER 1,4
110 IF cmd$='': INPUT 'SMSQ/E GOLD options: Qmon Copy> ';cmd$
120 f$='dev8_sys_boot_file'
130 v$='dev8_smsq_smsq_vers'
140 h$='dev8_smsq_gold_host'
150 ld$='dev8_smsq_smsq_loader'
160 hi$='dev8_smsq_gold_hwinit'
170 qm$='dev8_qmon_gold_smsq'
180 os1$='dev8_smsq_smsq_os'
190 ca$='dev8_smsq_smsq_cache'
200 os2$='dev8_smsq_sbas_control'
210 ns1$='dev8_smsq_gold_nasty'
220 ns2$='dev8_smsq_gold_nasty_s'
230 ln1$='dev8_smsq_smsq_lang'
240 ln2$='dev8_smsq_gold_kbd_lang'
250 ln2a$='dev8_smsq_gold_kbd_abc_lang'
260 ln3$='dev8_smsq_sbas_lang'
270 ex1$='dev8_smsq_sbas_procs_x'   : REMark this must be after "roms"
280 ex2a$='dev8_smsq_gold_kbd_abc'
290 ex2$='dev8_smsq_gold_driver_most'
300 ex3a$='dev8_smsq_gold_driver_ql'
310 ex3b$='dev8_smsq_aurora_driver_8'
320 ex3q$='dev8_smsq_gold_qimi'
330 ex3w$='dev8_smsq_smsq_wman'
340 ex3sq$='dev8_smsq_aurora_sysspr'
350 ex3sh$='dev8_smsq_gold_sysspr'
360 ex4$='dev8_smsq_gold_driver_dv3'
370 ex5$='dev8_smsq_gold_driver_nd'
380 ex6$='dev8_smsq_gold_driver_nds'
390 ex8$='dev8_smsq_home_home'
400 ex9$='dev8_smsq_smsq_hotkey'
410 ex10$='dev8_smsq_gold_roms'
420 nl$='dev8_sys_boot_null'
430 :
440 IF 'c' INSTR cmd$ = 0
450   IF 'q' INSTR cmd$ = 0: qm$ = nl$
460 REMark  EW f$, v$,h$,ld$,hi$,os1$,ca$,os2$,qm$,ns1$,ns2$,ln1$,ln2$,ln2a$,ln3$,ex2a$,ex2$,ex3a$,ex3b$,ex3q$,ex3w$,ex3sh$,ex4$,ex5$,ex6$,ex9$,ex8$,ex10$,ex1$, 'dev8_SMSQ_GOLD_gold8'
470   EW f$, v$,h$,ld$,hi$,os1$,ca$,os2$,qm$,ns1$,ns2$,ln1$,ln2$,ln2a$,ln3$,ex2a$,ex2$,ex3a$,ex3b$,ex3q$,ex3w$,ex3sh$,ex4$,ex5$,ex6$,ex9$,ex8$,ex10$,ex1$, 'dev8_SMSQ_Aurora_SMSQE'
480 ELSE
490   rc 'dev8_smsq_gold_flp_bas'
500   rc f$
510   rc v$
520   rc h$
530   rc ld$
540   rc hi$
550   rc qm$
560   rc os1f$
570   rc os1$
580   rc ca$
590   rc os2$
600   rc ns1$
610   rc ns2$
620   rc ln1$
630   rc ln2$
640   rc ln2a$
650   rc ln3$
660   rc exc$
670   rc ex1$
680   rc ex2$
690   rc ex2a$
700   rc ex3a$
710   rc ex3b$
720   rc ex3q$
730   rc ex3w$
740   rc ex4$
750   rc ex5$
760   rc ex6$
770   rc ex9$
780   rc ex10$
790   rc nl$
800 END IF
810 :
820 DEFine PROCedure rc (a$)
830   IF a$<>'': COPY_O a$ TO 'ram8_' & a$ (6 TO)
840 END DEFine rcopy
850 :
860 DEFine PROCedure sa
870   SAVE_O dev8_smsq_aurora_flp_bas
880 END DEFine sa
