100 REMark $$asmb=dev8_extras_source_outptr_bin,0,10
110 REMark $$asmb=dev1_booty_Qlib_ext,0,12
120 rem $$stak=15000
130 title$='MAKE v1.26'
140 IF JOB$(-1)<>''
150   msgch=FOPEN('con_100x100a0x40'): INK #msgch,7
160   mainch=FOPEN('nul')
170   OUTLN#msgch,512,42,0,28
180   REMark $$heap=4096
190   REMark $$stak=2048
200   REMark $$buff=256
210 ELSE
220   WINDOW#2,512,224,0,32
230   msgch=4: OPEN #4,'con':mainch=5:OPEN#5,'con'
240   cmd$='dev1_asm_lib_menus_lnk':INK #msgch,7
250 END IF
260 DIM subs$(7,30)
270 WINDOW #msgch;512,42,0,28
280 BORDER #msgch;1,2,0:PAPER #msgch;0
290 IF cmd$<>'':PE_PRINT #msgch,cmd$:make cmd$:STOP
300 REPeat l
310   CLS #msgch
320   PRINT #msgch;'Please enter linker command file name and options'\' (-i|f -p -c|l|x|n -r|w|q|t <name> -s<n> <name>)'\'Files possibly on "';DATAD$;'".'
330   REPeat m
340     INPUT #msgch;'Command ?:';x$
350     IF x$='' : EXIT m
360     IF x$=='stop' OR x$=='quit' : STOP
370     make x$
380   END REPeat m
390 END REPeat l
400 :
410 DEFine PROCedure make(opt$)
420 LOCal ext_len%,force_asm%,lnk_ext$,no_wait%
430 : REMark  Set up required options
440 :
450   dev$='':prg$=PROGD$:CLS #msgch
460   lnk_ext$='':ext_len%=0
470   Q_ERR_ON "EW"
480   a="_lnk" INSTR opt$
490   IF a
500      ext_len%=4:lnk_ext$='lnk'       : REMark try to find my extension
510   ELSE
520      a="_link" INSTR opt$: IF a:ext_len%=5:lnk_ext$='link'
530   END IF
540   IF a
550     opt$=opt$ & '   '               : REMark if we have an extension, filter it out
560     opt$=opt$(1 TO a-1)&opt$(a+ext_len% TO)
570   ELSE
580     a="_asm" INSTR opt$             : REMark called for a simple _asm file...
590     IF a:logch=-1:rtflag%=do_simple_asm (opt$):IF NOT rtflag%:PRINT#msgch; "Errors !!! - tap a key":a$=INKEY$(#msgch,-1)
600     RETurn
610   END IF
620   link$=get_item$(1,opt$)           : REMark get link file
630   IF link$(1)=='-' : link$='':ELSE link$=link$&'_'
640   REMark  Defaults are assemble only new files, allow assembly, no link
650   force_asm%=0:xrf$='':no_asm%=0:do_link%=0:as_list%=0:log$=dev$&link$&'log':no_wait%=0
660   prfch=-1
670   csource=0:prog$=' \p':rle$='_rel': map=1: map$=' -nolist'
680   fsource=0:fopt$=''
690 :
700 REMark now parse options of command string
710 :
720   IF '-f' INSTR opt$ : force_asm%=1     : REMark  Force assembly
730   IF '-i' INSTR opt$ : no_asm%=1        : REMark  Inhibit assembly
740   IF '-w' INSTR opt$ : no_wait%=1       : REMark don't stop after error
750   IF '-l' INSTR opt$ : map$=' \m': do_link%=1 : REMark  Link if possible
760   IF '-x' INSTR opt$ : map$=' \x': do_link%=1 : REMark  Produce xref
770   IF '-n' INSTR opt$ : map$=''   : do_link%=1 : REMark  No map
780   IF '-o' INSTR opt$
790     fopt$='/'&get_item$(('-o' INSTR opt$)+2,opt$)
800   END IF                                                : REMark  Compiler options
810 :
820   IF '-p' INSTR opt$ : as_list%=1                  : REMark  Listing file
830 :
840   spos=1                                                : REMark now do substitutes
850   REPeat subs%
860     IF '-s' INSTR opt$(spos TO)
870       spos=('-s' INSTR opt$(spos TO))+1+spos
880       snum='0'&opt$(spos)
890       subs$(snum)=get_item$(spos+1,opt$)      : REMark parse out substitutes
900       IF '-' INSTR subs$(snum)=1: subs$(snum)=''
910     ELSE
920       EXIT subs%
930     END IF
940   END REPeat subs%
950 :
960   IF '-e' INSTR opt$ THEN
970     log1$=get_item$(('-e' INSTR opt$)+2,opt$) :  REMark  Set log file
980     IF log$(1)<>'-' : log$=log1$
990   END IF
1000   logch=FOP_OVER(log$)
1010   dt$=DATE$:dt$=dt$(5 TO 12)&dt$(1 TO 5)&'at'&dt$(12 TO)
1020   plog title$&' on'&dt$&CHR$(10)&'Command string: '&opt$&CHR$(10)
1030   IF dev$<>""
1040     plog 'Source directory: '&dev$&CHR$(10)&CHR$(10) : REMark say hello
1050   ELSE
1060     plog 'Source directory: '&link$&CHR$(10)&CHR$(10)
1070   END IF
1080   IF logch<0 : plog 'Unable to open log file '&dev$&log$&'log'&CHR$(10)
1090 :
1100   in_rel$=''
1110   IF '-r' INSTR opt$
1120     in_rel$=dev$&get_item$(('-r' INSTR opt$)+2,opt$):  REMark       Input file to link
1130     l=LEN(in_rel$):IF in_rel$(l-3 TO l)=='_REL': in_rel$=in_rel$(1 TO l-4)
1140   END IF
1150 :
1160   IF '-q' INSTR opt$
1170     csource=1:rle$='_rlc'               :  REMark    Input QC file to link
1180     in_rel$=dev$&get_item$(('-q' INSTR opt$)+2,opt$)
1190     l=LEN(in_rel$):IF in_rel$(l-3 TO l)=='_RLC': in_rel$=in_rel$(1 TO l-4)
1200   END IF
1210 :
1220   IF '-t' INSTR opt$
1230     fsource=1:rle$='_rlf'               :  REMark    Input Fortran file to link
1240     in_rel$=dev$&get_item$(('-t' INSTR opt$)+2,opt$)
1250     l=LEN(in_rel$):IF in_rel$(l-3 TO l)=='_RLF': in_rel$=in_rel$(1 TO l-4)
1260   END IF
1270 :
1280   IF '-c' INSTR opt$
1290     lcf$=dev$&link$&'lib'               :  REMark    Concatenate local library
1300     l_nam=LEN(lcf$):do_link%=do_cctf(lcf$,as_list%,1)
1310     IF logch>=0 : CLOSE #logch:PE_PRINT #msgch;'Log file closed.'
1320     RETurn
1330   END IF
1340 :
1350   lcf$=dev$&link$&lnk_ext$              : REMark make linker control file name
1360 :
1370 : REMark  Do any assemblies required
1380 :
1390   IF in_rel$<>'': IF try_asm('INPUT '&in_rel$&rle$,as_list%)<0: do_link%=-1
1400   linkch=FOP_IN(lcf$)                   : REMark open linker control file
1410   IF linkch<0
1420     plog 'Error opening '&lcf$&' - aborting.'&CHR$(10)
1430     IF logch>=0 : CLOSE #logch          : REMark OOOPS!
1440     RETurn
1450   END IF
1460 :
1470   REPeat gline%
1480     IF EOF(#linkch): EXIT gline%        : REMark do until no more lines
1490     INPUT #linkch;a$;                   : REMark get command
1500     IF a$='' : NEXT gline%              : REMark skip blank lines
1510     REPeat try_subs%
1520       subs='#' INSTR a$                 : REMark try to do substitution
1530       IF NOT subs: EXIT try_subs%       : REMark no need
1540       subn=a$(subs+1)                   : REMark sub number
1550       SELect ON subs
1560         =1:a$=subs$(subn) & a$(3 TO)
1570         =LEN(a$)-1: a$=a$(1 TO subs-1) & subs$(subn)
1580         =REMAINDER : a$=a$(1 TO subs-1) & subs$(subn) & a$(subs+2 TO)
1590       END SELect
1600     END REPeat try_subs%
1610     tterr= try_asm(a$,as_list%)         : REMark assemble file if necessary
1620     IF (NOT tterr) AND (do_link%<>-1):do_link% =1:map$= " \m"
1630     IF tterr<0 : do_link%=-1
1640   END REPeat gline%
1650   CLOSE #linkch                         : REMark close linker control file
1660   plog CHR$(10)&'Linker command file closed.'&CHR$(10)
1670 :
1680 : REMark  Now link if necessary
1690 :
1700   rtflag%=0
1710   SELect ON do_link%
1720     =1: IF prog$==' \p'
1730           prog$= lcf$(1 TO LEN(lcf$)-ext_len%)&'_exe': REMark resulting program file
1740         END IF
1750         mapf$=dev$ & link$ & lnk_ext$&'_map'  : REMark linker error (map) file
1760         lcm$='\l ' & lcf$ & map$ &mapf$& ' \p'&prog$:REMark  linker command file
1770         IF in_rel$<>'' : lcm$=in_rel$&rle$&' '&lcm$: mapf$=in_rel$ & '_map'
1780         FOR subn=0 TO 7
1790           IF subs$(subn)<>'': lcm$=lcm$ & ' \' & subn & ' ' & subs$(subn)
1800         END FOR subn
1810         plog 'Linking: '&lcm$&CHR$(10)
1820         nm$=''
1830         my_err=EXEPF_W('Linker',#mainch;lcm$) : REMark use linker thing
1840         IF my_err=-7
1850           plog 'Linker thing error: '&my_err&' ... now using linker file'
1860           EW prg$&'Linker',#mainch;lcm$:nm$=''    : REMark if pb, use linker prog
1870           my_err=Q_ERR
1880         END IF
1890         IF my_err
1900           er=my_err: wr=0: ud=0         : REMark some error in linking
1910         ELSE
1920           erfch=FOP_IN(mapf$)           : REMark open linker error file
1930           IF erfch>0
1940              GET #erfch\FLEN(#erfch)-10:INPUT #erfch,a$
1950              not_hex%=0
1960              FOR mlp=1 TO 8: IF NOT a$(mlp) INSTR '0123456789abcdef':not_hex%=1
1970              IF not_hex%
1980                plog "linker didn't finish this file"
1990                ud=0:wr=0:er=-1
2000              ELSE
2010                er=HEX(a$(1 TO 4))          : REMark get linker errors/warnings
2020                wr=HEX(a$(5 TO 8))
2030                ud=0
2040                IF wr<0 OR er<0 : er=-1
2050              END IF
2060              CLOSE #erfch
2070           ELSE
2080              plog 'Unable to open linker error file "'&mapf$&'".'&CHR$(10)
2090              ud=0:wr=0:er=-1
2100           END IF
2110         END IF
2120         IF wr>0 : nm$=wr:er$=' warning'&FILL$('s',wr>1)
2130         IF er>0 : nm$=er:er$=' error'&FILL$('s',er>1)
2140         IF er<0 : nm$='Fatal':er$=' error'
2150         IF nm$<>'' : plog nm$&er$&' found during link.'&CHR$(10):rtflag%=1
2160     =0: plog 'Done.'&CHR$(10)                 : REMark no linking necessary
2170     =-1:plog 'Errors - no link.'&CHR$(10):rtflag%=1:REMark OOOPS
2180   END SELect
2190   IF NOT rtflag%
2200     plog "No errors" & CHR$(10)
2210   END IF
2220   IF logch>=0 : CLOSE #logch:PE_PRINT #msgch;'Log file closed.'
2230   IF rtflag% AND NOT no_wait%:PRINT#msgch; "Errors !!! - tap a key":a$=INKEY$(#msgch,-1)
2240 END DEFine make
2250 :
2260 DEFine FuNction get_item$(off,x$)
2270 REMark this parses options and returns the string corresponding to one option
2280 LOCal res$
2290   res$=no_white$(x$(off TO LEN(x$)))&' ': REMark get rid of leading spaces
2300   RETurn res$(1 TO ' ' INSTR res$-1)         : REMark return string until next space
2310 END DEFine  get_item$
2320 :
2330 DEFine FuNction no_white$(x$)
2340 REMark gets rid of leading spaces
2350 LOCal lp%,res$:res$=x$
2360   REPeat lp%
2370     IF LEN(res$)<1 : EXIT lp%
2380     IF res$(1)<>' ' : EXIT lp%
2390     IF LEN(res$)>1 : res$=res$(2 TO LEN(res$))
2400   END REPeat lp%
2410   RETurn res$
2420 END DEFine no_white$
2430 :
2440 DEFine FuNction try_asm(rel$,as_list%)
2450 REMark this attempts to assemble/compile a file, if necessary
2460 LOCal rlt,l_nam,ext$,ccr
2470   IF rel$(1 TO 7)=='PROGRAM': prog$=' \p'&get_item$(8,rel$): RETurn 1
2480   IF no_asm%: RETurn 1
2490   IF NOT(rel$(1 TO 5)=='INPUT' OR rel$(1 TO 7)=='LIBRARY') : RETurn 1
2500   rel$=no_white$(rel$(' ' INSTR rel$ TO LEN(rel$))):l_nam=LEN(rel$)
2510   IF rel$='*' : RETurn 1
2520   ext$=rel$(l_nam-3 TO l_nam):rlt=ext$ INSTR '_rel_lib_rlc_rlf_dnc'
2530   SELect ON rlt
2540     =17:IF FTEST(rel$)<>-7 : RETurn 1        : REMark dnc
2550        plog rel$&' not found.'&CHR$(10):RETurn -1
2560     =13:RETurn do_f77(rel$,as_list%)         : REMark fortran
2570     =9:ccr=do_qc(rel$,as_list%)    : REMark c file
2580        SELect ON ccr
2590          =-1:RETurn -1             : REMark error
2600          =0,1:RETurn do_asm(rel$,as_list%) : REMark now try to assemble
2610        END SELect
2620     =5:RETurn do_cctf(rel$,as_list%,1)       : REMark concatenate library
2630     =1:RETurn do_asm(rel$,as_list%)          : REMark assemble file
2640     =REMAINDER :plog 'File '&rel$&' is not a _REL file: ignored.'&CHR$(10)
2650   END SELect
2660   RETurn 1
2670 END DEFine try_asm
2680 :
2690 DEFine FuNction do_cctf(lib$,as_list%,cctx)
2700   LOCal rel$,asr,libdt
2710   do_cct=0
2720   cct$=lib$(1 TO l_nam-3):cct$=cct$&'cct':cctch=FOP_IN(cct$)
2730   IF cctch=-7 AND cctx: cct$=cct$&'x': cctch=FOP_IN(cct$)
2740   SELect ON cctch
2750     =-7:plog 'No _CCT for '&lib$&' - assumed OK.'&CHR$(10):RETurn 1
2760     =-999 TO -1
2770       plog "Couldn't open _CCT file "&cct$&'.'&CHR$(10)
2780       RETurn -1
2790   END SELect
2800   plog 'Processing '&cct$&'.'&CHR$(10)
2810   IF FTEST(lib$)=-7
2820     do_cct=1
2830     libdt=0
2840   ELSE
2850     libdt=FUPDT(\lib$)
2860     IF libdt<FUPDT(#cctch) : do_cct=1
2870   END IF
2880   REPeat read_cct
2890     IF EOF(#cctch) : EXIT read_cct
2900     INPUT #cctch;rel$
2910     IF rel$='' : NEXT read_cct
2920     IF NOT(rel$(1) INSTR 'abcdefghijklmnopqrstuvwxyz') : NEXT read_cct
2930     rel$=get_item$(1,rel$)
2940     asr=try_asm('INPUT '&rel$,as_list%)
2950     IF FTEST(rel$) : asr=-1
2960     SELect ON asr
2970       =1:IF libdt<FUPDT(\rel$) AND do_cct>=0 : do_cct=1
2980       =0:IF do_cct>=0 : do_cct=1
2990       =-1:do_cct=-1
3000     END SELect
3010   END REPeat read_cct
3020   CLOSE #cctch
3030   SELect ON do_cct
3040     =1: plog 'Concatenating '
3050         EW prg$&'cctf',cct$,lib$
3060         my_err=Q_ERR
3070         IF my_err
3080            plog '- fatal error.'&CHR$(10):RETurn -1
3090         ELSE
3100            plog '- OK.'&CHR$(10)
3110         END IF
3120     =0: plog 'Done.'&CHR$(10):RETurn 1
3130     =-1:plog 'Assembler errors - no concatenation.'&CHR$(10):RETurn -1
3140   END SELect
3150   RETurn 0
3160 END DEFine do_cctf
3170 :
3180 DEFine FuNction do_asm(rel$,as_list%)
3190   REMark returns -1 if error, 0 if OK, 1 if no need to assemble
3200   LOCal asm$,listf$,listo$,re_ass:l_nam=LEN(rel$)
3210   asm$=rel$(1 TO l_nam-3):asm$=asm$&'asm'
3220   IF as_list%=0
3230     listf$=rel$(1 TO l_nam-3)&'err'
3240     listo$=' -errors'
3250   ELSE
3260     listf$=rel$(1 TO l_nam-3)&'lst'
3270     listo$=' -nosym'
3280   END IF
3290   re_ass=1
3300   IF FTEST(rel$)=0 : re_ass=(FUPDT(\rel$)<FUPDT(\asm$))
3310   IF re_ass OR force_asm%
3320     plog 'Assembling '&asm$
3330     my_err=EXEPF_W('QMac',#mainch;asm$&' '&listf$&' '&rel$&listo$)
3340     IF my_err=-7
3350      REMark plog 'Qmac thing error: '&my_err
3360      EW prg$&'Qmac',#mainch;asm$&' '&listf$&' '&rel$&listo$
3370      my_err=Q_ERR
3380     END IF
3390     IF my_err
3400       er$='fatal':wr$='0'
3410     ELSE
3420       erfch=FOP_IN(listf$)
3430       IF erfch<0 : plog " - can't open error file."&CHR$(10):RETurn -1
3440       GET #erfch\FLEN(#erfch)-245
3450       er$=numaft(#erfch,'total errors',14)
3460       wr$=numaft(#erfch,'total warnings',17)
3470       CLOSE #erfch
3480     END IF
3490     IF er$<>'0'
3500       er$=' - '&er$&' error'&FILL$('s',(er$<>'1' AND er$<>'fatal'))&'.'&CHR$(10)
3510       plog er$:DELETE rel$:RETurn -1
3520     END IF
3530     IF wr$<>'0'
3540       wr$=' - '&wr$&' warning'&FILL$('s',wr$<>1)&'.'&CHR$(10):plog wr$
3550       RETurn 0
3560     END IF
3570     plog ' - OK.'&CHR$(10)
3580     IF listo$==' -errors' : DELETE listf$
3590     RETurn 0
3600   END IF
3610   RETurn 1
3620 END DEFine  do_asm
3630 :
3640 DEFine PROCedure plog(x$)
3650   PE_PRINT #msgch;x$;
3660   IF logch>=0 : PRINT #logch;x$;
3670 END DEFine plog
3680 :
3690 DEFine FuNction numaft(ch,x$,off)
3700   LOCal rd$,ans,l,m
3710   rd$=FILL$(' ',200)
3720   REPeat l
3730     IF EOF(#ch) : RETurn -1
3740     Q_ERR_ON 'input'
3750     REPeat m
3760       INPUT #ch;rd$;
3770       ans=Q_ERR
3780       IF ans=0 : EXIT m
3790       IF ans<>-5 : RETurn 1000-ans
3800     END REPeat m
3810     Q_ERR_OFF 'input'
3820     IF x$ INSTR rd$ : EXIT l
3830   END REPeat l
3840   ans=x$ INSTR rd$+off:IF ans<1 : ans=1
3850   ans=rd$(ans TO)
3860   RETurn ans
3870 END DEFine numaft
3880 :
3890 DEFine FuNction do_qc(rel$,as_list%)
3900   REMark returns -1 if error, 0 if OK, 1 if no need to compile
3910   LOCal c$,asm$,listf$,listo$,re_ass,my_err:l_nam=LEN(rel$)
3920   asm$=rel$(1 TO l_nam-3):c$=asm$&'c':asm$=asm$&'asm'
3930   IF as_list%=0
3940     listf$=rel$(1 TO l_nam-3)&'cer'
3950     listo$=' -l '
3960   ELSE
3970     listf$=rel$(1 TO l_nam-3)&'cls'
3980     listo$=' -c -l '
3990   END IF
4000   re_ass=1
4010   IF FTEST(asm$)<>-7 : re_ass=(FUPDT(\asm$)<FUPDT(\c$))
4020   IF re_ass OR force_asm%
4030     plog 'Compiling '&c$
4040     IF prfch>0 : PRINT #prfch;"spl '";listf$;"'"
4050     my_err=EXEPF_W('qc',#mainch;c$&' '&listo$&listf$)
4060     IF my_err=-7
4070      plog 'QC thing not found'&CHR$(10)
4080      EW prg$&'qc',#mainch;c$&' '&listo$&listf$
4090      my_err=Q_ERR
4100     END IF
4110     IF my_err
4120       DELETE asm$
4130       er$=' - compiler error(s).'&CHR$(10)
4140       plog er$:DELETE rel$:RETurn -1
4150     ELSE
4160       plog ' - OK.'&CHR$(10)
4170       IF listo$==' -l ' : DELETE listf$
4180       RETurn 0
4190     END IF
4200   END IF
4210   RETurn 1
4220 END DEFine do_qc
4230 :
4240 DEFine FuNction do_f77(rel$,as_list%)
4250   REMark returns -1 if error, 0 if OK, 1 if no need to compile
4260   LOCal ftn$,xfopt$,re_ass,fo$,lo$:l_nam=LEN(rel$)
4270   ftn$=rel$(1 TO l_nam-4)
4280   fo$='gu':lo$='l'
4290   xfopt$=fopt$:IF xfopt$='' : xfopt$='/gu'
4300   FOR i=1 TO 2:IF NOT(fo$(i) INSTR xfopt$) : xfopt$=xfopt$&fo$(i)
4310   IF as_list%=1
4320     FOR i=1 TO 1:IF NOT(lo$(i) INSTR xfopt$) : xfopt$=xfopt$&fo$(i)
4330   END IF
4340   re_ass=1
4350   IF FTEST(rel$)<>-7 : re_ass=(FUPDT(\rel$)<FUPDT(\ftn$&'_for'))
4360   IF re_ass OR force_asm%
4370     plog 'Compiling '&ftn$
4380     IF prfch>0 : PRINT #prfch;"spl '";ftn$;"_prn'"
4390     DELETE ftn$&'_log'
4400     my_err=EXEPF_W('f77';ftn$&xfopt$ )
4410     IF my_err
4420        plog 'QC thing not found'&CHR$(10)
4430        EW prg$&'f77';ftn$&xfopt$
4440        my_err=Q_ERR
4450     END IF
4460     IF my_err
4470       er$=' - fatal error.'&CHR$(10)
4480       plog er$:DELETE rel$:RETurn -1
4490     END IF
4500     erfch=FOP_IN(ftn$&'_log')
4510     IF erfch<0 : plog " - fatal error."&CHR$(10):DELETE ftn$&'_rel':DELETE rel$:RETurn -1
4520     GET #erfch\FLEN(#erfch)-300
4530     wr=numaft(#erfch,'COMPILATION',-999)
4540     er=numaft(#erfch,'FATAL COMPI',-999)
4550     CLOSE #erfch:re_ass=(wr>0)+2*(er>0)
4560     SELect ON re_ass
4570       =0:plog ' - OK.'&CHR$(10)
4580         IF NOT('l' INSTR xfopt$) : DELETE ftn$&'_LOG'
4590         DELETE rel$:RENAME ftn$&'_rel' TO rel$:RETurn 0
4600       =1:lo$=wr:lo$=' - '&lo$&' warning'&FILL$('s',wr>1)&'.'&CHR$(10)
4610         plog lo$
4620         DELETE rel$:RENAME ftn$&'_rel' TO rel$:RETurn 0
4630       =2,3:lo$=er:lo$=' - '&lo$&' error'&FILL$('s',er>1)&'.'&CHR$(10)
4640         plog lo$
4650         DELETE rel$:DELETE ftn$&'_rel':RETurn -1
4660     END SELect
4670   END IF
4680   RETurn 1
4690 END DEFine do_f77
4700 :
4710 DEFine FuNction do_simple_asm(rel$)
4720   REMark returns 0 if error, 1 if OK
4730   LOCal er$,wr$
4740   LOCal prg$,asm$,listf$,listo$,re_ass:l_nam=LEN(rel$)
4750   er$="0":wr$="0"
4760   prg$=PROGD$
4770   asm$=rel$:rel$=rel$(1 TO LEN(rel$)-3)
4780   listf$=rel$&'err'
4790   rel$=rel$&'bin'
4800   listo$=' -errors -NOLINK'
4810   plog 'Assembling '&asm$&" "&listf$&" "&rel$&listo$
4820   my_err=EXEPF_W('QMac',#mainch;asm$&' '&listf$&' '&rel$&listo$)
4830   IF my_err=-7
4840      REMark plog 'QMac thing not found'&CHR$(10)
4850      EW prg$&'Mac',#mainch;asm$&' '&listf$&' '&rel$&listo$
4860      my_err=Q_ERR
4870   END IF
4880   IF my_err
4890       er$='fatal':wr$='0'
4900   ELSE
4910       erfch=FOP_IN(listf$)
4920       IF erfch<0 : plog " - can't open error file."&CHR$(10):RETurn 0
4930       GET #erfch\FLEN(#erfch)-245
4940       er$=numaft(#erfch,'total errors',14)
4950       wr$=numaft(#erfch,'total warnings',17)
4960       CLOSE #erfch
4970   END IF
4980   IF er$<>'0'
4990       er$=' - '&er$&' error'&FILL$('s',(er$<>'1' AND er$<>'fatal'))&'.'&CHR$(10)
5000       plog er$:DELETE rel$:RETurn 0
5010   END IF
5020   IF wr$<>'0'
5030       wr$=' - '&wr$&' warning'&FILL$('s',wr$<>1)&'.'&CHR$(10):plog wr$
5040   END IF
5050   plog ' - OK.'&CHR$(10)
5060   IF er$='0' AND wr$='0':DELETE listf$
5070   RETurn 1
5080 END DEFine  do_simple_asm
5090 :
5100 DEFine PROCedure sa
5110   SAVE_O dev8_extras_source_make_bas
5120 END DEFine
