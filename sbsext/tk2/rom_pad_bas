100 REMark Pad file to exactly 16kb
110 file$ = "dev8_sbsext_tk2_rom"
120 IF FLEN(\file$) < 16384 THEN
130   mem=ALCHP(16384)
140   LBYTES dev8_sbsext_tk2_rom,mem
150   SBYTES_O dev8_sbsext_tk2_rom,mem,16384
160   RECHP mem
170 END IF
