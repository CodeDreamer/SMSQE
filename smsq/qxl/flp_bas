100 OPEN #1,'con_250x12a40x50': CLS: BORDER 1,4
110 INPUT 'SMSQ/E QXL options: Copy> ';cmd$
120 IF FOP_IN ('dev8_smsq_qxl_qxlh.exe') <> -7: INPUT 'QXLH.EXE exists'; cmd$: QUIT
130 p$   = 'dev8_sys_boot_file'
140 bl$  = 'dev8_smsq_qxl_qxl2_exe'
150 hst$ = 'dev8_smsq_qxl_host'
160 hi$  = 'dev8_smsq_qxl_hwinit'
170 ld$  = 'dev8_smsq_smsq_loader'
180 os1$ = 'dev8_smsq_smsq_os'
190 ca$  = 'dev8_smsq_smsq_cache'
200 os2$ = 'dev8_smsq_sbas_control'
210 ns1$ = 'dev8_smsq_qxl_nasty_e'
220 ln1$ = 'dev8_smsq_smsq_lang'
230 ln2$ = 'dev8_smsq_qxl_kbd_lang'
240 ln3$ = 'dev8_smsq_sbas_lang'
250 ex1$ = 'dev8_smsq_qxl_procs_x'
260 ex2$ = 'dev8_smsq_qxl_driver_most'  : REMark Keyboard before CON
270 ex3$ = 'dev8_smsq_qxl_driver_ql'
280 ex3b$ = 'dev8_smsq_qxl_driver_16'
290 ex3w$ = 'dev8_smsq_smsq_wman'
300 ex4$ = 'dev8_smsq_qxl_driver_nd'
310 ex5$ = 'dev8_smsq_qxl_driver_dv3e'
320 ex6$= 'dev8_smsq_smsq_hotkey'
330 ex7$ = 'dev8_smsq_qxl_sysspr'
340 last$ = 'dev8_smsq_qxl_ecache'
350 dml$ = 'dev8_smsq_qxl_dummy'
360 qm$  = 'dev8_qmon_qxl_bin'
370 IF 'c' INSTR cmd$ = 0
380 :
390   r$='ram1_smsqe.exe' : f$='dev8_smsq_qxl_smsqe.exe'
400   DELETE f$
410       EW p$, hst$,ld$,hi$,os1$,ca$,os2$,ns1$,ln1$,ln2$,ln3$,ex1$,ex2$,ex3$,ex3b$,ex3w$,ex4$,ex5$,ex6$,ex7$,last$, r$
420   REMark EW p$, hst$,ld$,hi$,os1$,ca$,os2$,ns1$,ln1$,ln2$,ln3$,ex1$,ex2$,ex3$,ex3b$,ex3w$,     ex5$,ex9$,qm$,last$, r$
430   EW cct, bl$,r$, f$
440   DELETE r$
450   OPEN #4,f$
460   lenf=FLEN(#4)
470   lens% = INT ((lenf+511)/512)
480   lenb% = lenf - lens% * 512
490   IF lenb%: lenb% = lenb% + 512
500   BPUT #4\2, lenb% MOD 256, lenb% DIV 256, lens% MOD 256, lens% DIV 256
510   CLOSE #4
520 ELSE
530   rc 'dev8_smsq_qxl_flp_bas'
540   rc 'dev8_smsq_smsq_version_asm'
550   rc p$
560   rc bl$
570   rc hst$
580   rc ld$
590   rc hi$
600   rc qm$
610   rc os1$
620   rc ca$
630   rc os2$
640   rc ns1$
650   rc ln1$
660   rc ln2$
670   rc ln3$
680   rc ex1$
690   rc ex2$
700   rc ex3$
710   rc ex3b$
720   rc ex3w$
730   rc ex4$
740   rc ex5$
750   rc ex9$
760   rc last$
770 END IF
780 DEFine PROCedure rc (a$)
790   IF a$<> '': COPY_O a$ TO 'ram8_' & a$ (6 TO)
800 END DEFine rcopy
810 :
820 DEFine PROCedure sa
830   SAVE_O dev8_smsq_qxl_flp_bas
840 END DEFine sa
850 :
860 DEFine PROCedure b
870   LOAD dev8_smsq_qxl_make_bas
880 END DEFine b
890 :
