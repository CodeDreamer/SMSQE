100 p
110 :
120 DEFine PROCedure sa
130   SAVE_O DEV8_smsq_q40_boot_make_bas
140 END DEFine sa
150 :
160 DEFine PROCedure p
170   init
180   info
190   PRINT "Assemble SMSQE now ? (y/n)":answ$=INKEY$(-1)
200   IF answ$=="y"
210     DELETE ofile$
220     assemble_smsqe "DEV8_extras_exe_SMSQEmake","-q0 -z -as -mk -tq -tg -sa"
230   END IF
240   PRINT "Configure SMSQE now ? (y/n)":answ$=INKEY$(-1)
250   IF answ$=="y"
260     EW menuconfig;"\Q \U "&ofile$
270   END IF
280   gzip_file
290   REMark mk_h_rom
300   mk_file
310   mk_roms
320   PRINT "Program finished."
330 END DEFine p
340 :
350 DEFine PROCedure mk_h_rom
360   PRINT "Make ROM header now ? (y/n)":answ$=INKEY$(-1)
370   IF answ$=="y"
380     make_header_rom "dev8_smsq_q40_boot_gz_link"
390   END IF
400 END DEFine mk_h_rom
410 :
420 DEFine PROCedure init
430   dev$="dev8_"
440   ofile$=dev$&"smsq_q40_rom"                  : REMark orig file before compression
450   unzipfile$=dev$&"smsq_q40_boot_compr_rom"   : REMark file with boot code & decompression routine
460   compressed_file$=dev$&"smsq_q40_boot_Q40_gz": REMark temporary compressed file
470   result$=dev$&"smsq_q40_boot_Q40_ROM"        : REMark resulting rom code
480 END DEFine init
490 :
500 DEFine PROCedure assemble_smsqe (maker$,params$)
510   PRINT "Building SMSQ/E..."
520   EW maker$;params$
530   PRINT "SMSQE built."
540 END DEFine assemble_smsqe
550 :
560 DEFine PROCedure gzip_file
570   PRINT "Gzipping file..."
580   COPY_O ofile$,"ram1_Q40_ROM"
590   EW gzip;"-f -n ram1_Q40_ROM"
600   COPY_O "ram1_Q40_ROM_gz",compressed_file$
610   PRINT "File compressed and copied to "&compressed_file$
620 END DEFine gzip_file
630 :
640 DEFine PROCedure make_header_rom (lkfile$)
650   PRINT "Assembling special ROM header..."
660   DELETE dev8_smsq_q40_boot_ROM_REL            : REMark make sure file is recompiled!
670   EW "make";lkfile$
680   PRINT "Header made."
690 END DEFine make_header_rom
700 :
710 DEFine PROCedure mk_file
720 LOCal llen1,llen2,llen3,extn$,ofile$
730   PRINT "Combining files..."
740   gz=1
750   llen=FLEN(\unzipfile$)
760   llen2=FLEN(\ofile$)                      : REMark length of uncompressed file
770   llen3=FLEN(\compressed_file$)
780   IF (llen MOD 2)<>0:llen=llen+1           : REMark make sure it's even
790   IF (llen2 MOD 2)<>0:llen2=llen2+1        : REMark make sure it's even
800   addr=ALCHP(llen+llen3)
810   LBYTES unzipfile$,addr                   : REMark the decompression
820   POKE_L addr+llen-6, llen2                : REMark length of uncompressed file
830   LBYTES compressed_file$,addr+llen
840   llen2=llen+llen3
850   IF (llen2 MOD 2)<>0:llen2=llen2+1        : REMark make sure it's even
860   SBYTES_O result$, addr, llen2
870   RECHP addr
880   PRINT "File made. The result file is: "\result$
890 END DEFine mk_file
900 :
910 DEFine PROCedure info
920 LOCal a$
930   PRINT "This program assumes that:"
940   PRINT " - The original ROM file is "
950   PRINT "   ";ofile$
960   PRINT " - The compressed code header file is:"
970   PRINT "   ";unzipfile$
980   PRINT " - The compressed file will be:"
990   PRINT "   ";compressed_file$
1000   PRINT " - The resulting file will be:"
1010   PRINT "   ";result$
1020   PRINT " - MenuConfig is at "
1030   PRINT "   ";PROGD$&"menuconfig"
1040   PRINT " - The program 'gzip' is at "
1050   PRINT "   ";PROGD$&"gzip"
1060   PRINT
1070   PRINT "If this isn't so, amend the 'init' procedure"
1080   PRINT\\"Hit ENTER to continue..."
1090   a$=INKEY$(-1)
1100 END DEFine info
1110 :
1120 DEFine PROCedure mk_roms
1130   PRINT "Making roms, please be patient..."
1140   OPEN_IN #5,result$
1150   OPEN_OVER #6,result$&'_LO'
1160   OPEN_OVER #7,result$&"_HI"
1170   REPeat
1180     IF EOF(#5): EXIT
1190     BGET #5,a,b: BPUT #7,b,a
1200     IF EOF(#5): EXIT
1210     BGET #5,a,b: BPUT #6,b,a
1220   END REPeat
1230   CLOSE #5,#6,#7
1240   PRINT "Done. The result Roms are: "\result$ &"_HI"
1250   PRINT result$&"_LO"
1260 END DEFine mk_roms
