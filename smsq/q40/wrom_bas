100 WMON
110 INPUT 'Filename (ENTER = dev8_smsq_q40_rom)';f1$
120 IF f1$="":f1$="dev8_smsq_q40_rom"
130 OPEN_IN #5,f1$
140 OPEN_OVER #6,'dev8_smsq_q40_rom_lo'
150 OPEN_OVER #7,'dev8_smsq_q40_rom_hi'
160 REPeat
170  IF EOF(#5): EXIT
180  BGET #5,a,b: BPUT #7,b,a
190  IF EOF(#5): EXIT
200  BGET #5,a,b: BPUT #6,b,a
210 END REPeat
220 CLOSE #5,#6,#7
230 :
240 DEFine PROCedure sa
250   SAVE_O dev8_smsq_q40_wrom_bas
260 END DEFine sa
