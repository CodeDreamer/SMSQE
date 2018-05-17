100 r$='ram1_smsq.exe' : f$='flp1_smsq.exe'
110 delete f$
115 nl$  = 'win1_sys_boot_null'
120 bl$  = 'win1_smsq_qxl_qxlh_exe'
130 ld$  = 'win1_smsq_qxl_loader'
140 os1$ = 'win1_smsq_smsq_os'
150 ca$  = 'win1_smsq_smsq_cache'
160 os2$ = 'win1_smsq_sbas_control'
170 ns1$ = 'win1_smsq_qxl_nasty_e'
180 ln1$ = 'win1_smsq_smsq_lang'
190 ln2$ = 'win1_smsq_qxl_kbd_lang'
200 ln3$ = 'win1_smsq_sbas_lang'
210 ex1$ = 'win1_smsq_qxl_procs_x'
220
230 ex2$ = 'win1_smsq_qxl_driver_ql'    : rem CON driver should be first(ish)
240 ex3$ = 'nl$' : rem 'win1_smsq_qxl_driver_nd'
250 ex4$ = 'win1_smsq_qxl_driver_dv3e'
260 qm$  = 'win1_qmon_qxl_bin'
270
280 p$   = 'win1_sys_boot_file'
290
300 EW p$, ld$,os1$,ca$,os2$,ns1$,ln1$,ln2$,ln3$,ex1$,ex2$,ex3$,ex4$, r$
310 EW cct, bl$,r$, f$
320 delete r$
330
340 OPEN #4,f$
350 lenf=FLEN(#4)
360 lens% = INT ((lenf+511)/512)
370 lenb% = lenf - lens% * 512
380 IF lenb%: lenb% = lenb% + 512
390 BPUT #4\2, lenb% MOD 256, lenb% DIV 256, lens% MOD 256, lens% DIV 256
400 CLOSE #4
