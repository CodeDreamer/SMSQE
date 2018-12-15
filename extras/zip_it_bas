100 CLS#1:CLS#2
110 init_vars
120 get_smsqe_version ichan%,ochan%
130 show_info_and_warning
140 check_win4                  : REMark check win4 can be used
150 p
160 :
170 DEFine PROCedure init_vars
180   windrive$="win4_"             : REMark windrive where files will be copied to          "
190   htmldrive$=NFA_USE$(1)&"website/"    : REMark drive where html templates are
200   resultnfa$=NFA_USE$(1)&"website/new/": REMark where result files will go
210   all_months$=make_all_months$: REMark months strings
220   ichan%=2:ochan%=1             : REMark report channels
230   ddr$="ram3_"                  : REMark temp dir
240   qpc%=0                        : REMark not running under QPC
250 END DEFine init_vars
260 :
270 DEFine PROCedure show_info_and_warning
280   PRINT "This program makes a new version of"
290   PRINT "the SMSQ/E source files, ready to "
300   PRINT "upload to the website."
310   PRINT
320   PRINT "This program makes WIN4_ with the sources,"
330   PRINT "it also makes the zip file conaining all"
340   PRINT "the sources and the zip files for the"
350   PRINT "individual source subdirectories and the."
360   PRINT "binaries. These files are first created"
370   PRINT "in "&ddr$&" and are then copied to:"
380   PRINT "NFA8_"
390   PRINT
400   PRINT "!!!!!"
410   PRINT "NFA8_ will be set to ";resultnfa8$;"."
420   PRINT "ALL FILES CURRENTLY IN NFA8_ WILL BE DELETED!!!!"
430   PRINT
440   PRINT "WIN4_ will be set to "& resultnfa$&"SMSQE"&smsqe$&".win'"
450   PRINT "IF A FILE OF THIS NAME ALREADY EXISTS, IT WILL"
460   PRINT "BE DELETED FIRST!"
470   PRINT "WIN4 IS THEN FORMATTED!"
480   PRINT "!!!!!"
490   PRINT
500   PRINT "'CLINE_BIN' AND 'OUTPTR_BIN' MUST BE LOADED !"
510   PRINT
520   PRINT "This program also temporarily sets the"
530   PRINT "DATA_USE and PROG_USE dirs"
540   PRINT "(it resets them at the end)."
550   PRINT "This prog also sets (and then resets) the WIN8_"
560   PRINT "and NFA8_ drives, so make sure no file is open there."
570   PRINT
580   PRINT "----------------------"
590   PRINT
600   PRINT "HIT ENTER TO CONTINUE OR ANY OTHER KEY TO STOP"
610 :
620   a$=INKEY$(-1)
630   IF a$<>CHR$(10):STOP
640 END DEFine show_info_and_warning
650 :
660 DEFine PROCedure sa
670   SAVE_O dev8_extras_newversion_bas
680 END DEFine sa
690 :
700 DEFine PROCedure p
710 LOCal a$
720   IF NOT IS_EXTN ("cline")
730      PRINT "cline isn't loaded. Failed and stopped"
740   END IF
750   CLS#ichan%:CLS#ochan%
760   INPUT #ochan%,"Compile everything first? (y/n) ";a$
770   IF a$=="y":EW dev8_extras_exe_SMSQEmake;"-q0 -as -mk -ta -sa"
780   check_native_dirs ichan%,ochan%
790   PRINT#ochan%,"Zipping all binaries to ";ddr$
800   PRINT#ochan%;"Deleting all non source files but keeping binaries..."
810   PRINT#ochan%;"Please be patient..."
820   EW dev8_extras_del_all_bas;"auto stop keep"
830   PRINT #ochan%,"Making Q68 win container"
840   PRINT#ochan%;"Please be patient..."
850   EW DEV8_extras_mkq68win_bas
860   PRINT#ochan%, "zipping all to ";ddr$
870 REMark  EW dev8_extras_zip_it_bas;"quit binaries docopy version"&svers$
880   PRINT#ochan%,"Making version and changelog html files..."
890   today$=make_date$(0,0)
900   make_versions ochan%
910   make_indexphp ochan%
920   make_container
930   PRINT#ochan%
940   PRINT#ochan%," ----------------- DONE -----------------"
950 END DEFine p
960 :
970 DEFine PROCedure get_smsqe_version (ichan%,ochan%)
980 REMark this gets the version of SMSQE
990 LOCal chan%,lp%,a$,t%
1000   CLS#ichan%
1010   INPUT#ochan%, "SMSQE version in format x.yy? (or Enter to get it from dev8_smsq_smsq_version_asm) ",smsq_vers$
1020   IF smsq_vers$=""
1030     chan%=FOP_IN('dev8_smsq_smsq_version_asm')
1040     REPeat lp%
1050       IF EOF(#chan%):EXIT lp%
1060       INPUT#chan%,a$
1070       IF "smsq_vers" INSTR a$  AND "equ" INSTR a$
1080         t%="'" INSTR a$
1090         IF t%:smsq_vers$=a$(t%+1 TO t%+4)
1100       END IF
1110     END REPeat lp%
1120     CLOSE#chan%
1130   END IF
1140   IF smsq_vers$=""
1150     PRINT#ochan%, "Failed to get smsq version!"
1160   ELSE
1170     PRINT#ochan%, "-> smsqe version : ",smsq_vers$
1180   END IF
1190   t%='.' INSTR smsq_vers$
1200   IF t%
1210     svers$=smsq_vers$(1)&smsq_vers$(3 TO 4)
1220   ELSE
1230     PRINT "no decimal point in version : wrong! Failed and stopped"
1240     STOP
1250   END IF
1260 END DEFine get_smsqe_version
1270 :
1280 DEFine PROCedure check_native_dirs (ichan%,ochan%)
1290 LOCal lp%,t%,drive$
1300 REMark get new dirs for : * Subdirs  * SMSQE itself
1310   smsqename$="smsqe"&smsq_vers$
1320   PRINT#ochan%,"-> SMSQE name is : ";smsqename$
1330   NFA_USE 8,htmldrive$
1340   t%= FTEST ("nfa8_")
1350   SELect ON t%
1360     =-1:PRINT#ochan%;htmldrive$;" is not a valid directory"
1370         PRINT#ochan%,'There seems to be a simple file with that name already!'
1380         PRINT#ochan%,"Failed and stopped":STOP
1390     =-7:
1400         PRINT "Cannot find ";htmldrive$
1410         PRINT#ochan%,"Failed and stopped":STOP
1420     =0:
1430     = REMAINDER
1440         PRINT "Error for ";htmldrive$;" : ";t%;" - STOPPED":STOP
1450   END SELect
1460   NFA_USE 8,resultnfa$
1470   t%= FTEST ("nfa8_")
1480   SELect ON t%
1490     =0:PRINT#ochan%, "Deleting all files on ",resultnfa$:WDEL nfa8_
1500     =-1:PRINT#ochan%;"Couldn't make directory '"&resultnfa$
1510         PRINT#ochan%,'There seems to be a simple file with that name already!'
1520         PRINT#ochan%,"Failed and stopped":STOP
1530     =-7:
1540         MAKE_DIR "nfa8_"
1550     = REMAINDER
1560         PRINT "Error for ";resultnfa$;" : ";t%;" - STOPPED":STOP
1570   END SELect
1580 END DEFine check_native_dirs
1590 :
1600 DEFine PROCedure make_versions (ochan%)
1610   PRINT#ochan%,"Making changes html text"
1620   EW dev8_extras_html_changes_bas;today$&" quit"
1630   PRINT#ochan%,"Making versions html text"
1640   EW dev8_extras_html_versions_bas;today$&" quit"
1650 END DEFine make_versions
1660 :
1670 DEFine PROCedure make_indexphp (ochan%)
1680 LOCal lp%,c%,o%,a$
1690   PRINT#ochan%,"Making index.php file"
1700   c%=FOP_IN (htmldrive$&"indexphp.template")
1710   IF c%<0:PRINT#ochan%, "failed to open input : ";c%;". STOPPED":STOP
1720   o%=FOP_OVER (resultnfa$&"index.php")
1730   IF o%<0:PRINT#ochan%, "failed to open output - STOPPED":STOP
1740   REPeat lp%
1750     IF EOF(#c%):EXIT lp%
1760     INPUT#c%,a$
1770     a$=repl$(a$)
1780     PRINT#o%,a$
1790   END REPeat lp%
1800   CLOSE#c%:CLOSE#o%
1810 END DEFine make_indexphp
1820 :
1830 DEFine FuNction repl$(a$)
1840 REMark replace values in strings
1850 REMark xxxversionxxx is,eg 3.14
1860 REMark xxxsmsqexxx is, eg 314/smsqe314
1870 REMark xxxsubdirxxx is, eg 314/
1880 REMark xxxdatexxx is, eg. 28.02.2017
1890 REMark
1900 REMark  smsq_vers$=eg 3.14
1910 REMark  svers$= eg 314
1920 LOCal t%,front$,end$
1930   front$=a$
1940   t%="xxxversionxxx" INSTR front$
1950   IF t%
1960     front$=front$(1 TO t%-1)&smsq_vers$&front$(t%+13 TO)
1970   END IF
1980   t%="xxxsmsqexxx" INSTR front$
1990   IF t%
2000     front$=front$(1 TO t%-1)&svers$&"/smsqe"&svers$&front$(t%+11 TO)
2010   END IF
2020   t%="xxxsubdirxxx" INSTR front$
2030   IF t%
2040     front$=front$(1 TO t%-1)&svers$&"/"&front$(t%+12 TO)
2050   END IF
2060   t%="xxxdatexxx" INSTR front$
2070   IF t%
2080     front$=front$(1 TO t%-1)&today$&front$(t%+10 TO)
2090   END IF
2100   RETurn front$
2110 END DEFine repl$
2120 :
2130 DEFine PROCedure make_container
2140 REMark this formats the win container
2150 LOCal what_chan%,counter
2160   PRINT#ochan%,"Now making the container file--------"
2170   PRINT#ochan%,"Formatting "&windrive$& " !"
2180   FORMAT windrive$&"25_SMSQE"&smsq_vers$
2190   PRINT#ochan%
2200   counter=0
2210   what_chan%=FOP_OVER('ram1_mydirs')
2220   get_dir_structure "dev8_",what_chan%
2230   CLOSE#what_chan%
2240   PRINT#ochan%,"Got dir structure, sorting..."
2250   sort_it
2260   PRINT#ochan%,"Making dir structure"
2270   make_dir_structure "ram1_mydirs2",windrive$
2280   PRINT#ochan%,"copying files, be patient..."
2290   fbackup "dev8_",windrive$
2300 END DEFine make_container
2310 :
2320 DEFine PROCedure get_dir_structure (dirr$,what_chan%)
2330 LOCal a$,lp,chan%,device$,file$
2340 LOCal k
2350   file$="ram1_ddir"&counter&"pkk"
2360   chan%=FOP_OVER(file$)
2370   IF chan%<0:RETurn
2380   DIR#chan%,dirr$
2390   CLOSE#chan%
2400   device$=dirr$(1 TO 4)&'_'
2410   chan%=FOP_IN(file$)
2420   IF chan%<0:RETurn
2430   INPUT#chan%,a$
2440   INPUT#chan%,a$
2450   REPeat lp
2460     IF EOF(#chan%):EXIT lp
2470     INPUT#chan%,a$
2480     k=LEN(a$)
2490     IF k<4:NEXT lp:EXIT lp
2500     IF a$(k-2 TO k)=' ->'
2510       counter=counter+1
2520       PRINT#what_chan%,a$(1 TO k-3)
2530       a$=device$&a$(1 TO k-3)&"_"
2540       PRINT#ochan%, "OK: ";a$
2550       get_dir_structure a$,what_chan%
2560     END IF
2570   END REPeat lp
2580   CLOSE#chan%
2590   DELETE file$
2600 END DEFine get_dir_structure
2610 :
2620 DEFine PROCedure sort_it
2630 LOCal a$,array$(counter+10,30),chan%,c,lp
2640   chan%=FOP_IN('ram1_mydirs')
2650   c=1
2660   REPeat lp
2670     IF EOF(#chan%):EXIT lp
2680     INPUT#chan%,a$
2690     array$(c)=a$
2700     c=c+1
2710   END REPeat lp
2720   ASORT array$,0
2730   CLOSE#chan%
2740   chan%=FOP_OVER('ram1_mydirs2')
2750   FOR lp=1 TO c
2760     a$=array$(lp)
2770     PRINT#chan%,a$
2780   END FOR lp
2790   CLOSE#chan%
2800 END DEFine sort_it
2810 :
2820 DEFine PROCedure make_dir_structure (file$,drive$)
2830 LOCal lp%,a$,chan%
2840   chan%=FOP_IN(file$)
2850 PRINT file$,chan%
2860   IF chan%<0: PRINT "File "&file$&" doesn't exist":RETurn
2870   REPeat lp%
2880     IF EOF(#chan%):EXIT lp%
2890     INPUT#chan%,a$
2900     IF a$="":NEXT lp%:EXIT lp%
2910     PRINT "Making "&drive$&a$&" ..."
2920     MAKE_DIR drive$&a$
2930   END REPeat lp%
2940   CLOSE#chan%
2950 END DEFine make_dir_structure
2960 :
2970 DEFine PROCedure fbackup (source$,dest$)
2980 REMark makes a forced backup, eventually deleting newer files on dest
2990 REMark and replacing them with the files on source$
3000 REMark fbackup "dir_with_new_files", "dir_with_old_files"
3010 LOCal chan%,lp%,myfile$,a$,t%,tc%,len_old,len_new,date_old,date_new,b$,mdir$,mdest$
3020   FOR lp%=1 TO 50
3030     myfile$="ram8_diffs_txt"&RND(1 TO 80)&RND (1 TO 80) & RND (1 TO 50)
3040     chan%=FOP_NEW (myfile$)            : REMark try to open unique file
3050     IF chan%>0:EXIT lp%
3060   END FOR lp%
3070   IF source$(LEN(source$))<>"_":source$=source$&"_"
3080   IF dest$(LEN(dest$))<>"_":dest$=dest$&"_"
3090   PRINT#ochan%, 'source= ';source$
3100   PRINT#ochan%, "dest= ";dest$
3110   mdir$=source$(1 TO 5)
3120   mdest$=dest$(1 TO 5)
3130   IF chan%<0:RETurn                     : REMark ooops!!!!
3140   WDIR#chan%,source$                    : REMark dir of this rep in file
3150   GET#chan%\0                           : REMark reset file pointer to start
3160   REPeat lp%
3170     IF EOF(#chan%):EXIT lp%
3180     INPUT#chan%,a$                      : REMark get filename
3190     IF a$="":NEXT lp%
3200     t%= ' ->' INSTR a$                  : REMark is it a subdir?
3210     IF t%
3220       tc%=LEN(source$)-4
3230       c$=a$(tc% TO)
3240       c$=c$(TO LEN(c$)-3)&"_"
3250       fbackup mdir$&a$(TO LEN(a$)-3)&'_',dest$&c$
3260       NEXT lp%
3270     END IF
3280     tc%=FOP_IN(mdir$&a$)                : REMark open source file
3290     date_old=FUPDT(#tc%)                : REMark file date of "old" source file
3300     CLOSE#tc%
3310     tc%=LEN(source$)-4                  : REMark length of name w/o subdirectory
3320     b$=a$(tc% TO)
3330 REMark    PRINT mdir$&a$;"  -->  ";dest$&b$
3340     DELETE dest$&b$
3350     COPY mdir$&a$,dest$&b$
3360     tc%=FOPEN(dest$&b$)
3370     SET_FUPDT#tc%,date_old
3380     CLOSE#tc%
3390   END REPeat lp%
3400   CLOSE#chan%
3410   DELETE myfile$
3420 END DEFine fbackup
3430 :
3440 DEFine PROCedure check_win4
3450 REMark check that the native file corresponding to win4 doesn't exist (!!!)
3460 LOCal a$,t%
3470   natdrv4win4$=NFA_USE$(2)&"SMSQE"
3480   a$=natdrv4win4$&svers$&'.WIN'
3490   t%=FTEST(a$)
3500   IF t%<>-7
3510     CLS:PRINT "Error with win4"
3520     IF t%=0
3530       PRINT "It already exists"
3540     ELSE
3550       REPORT#1,t%
3560     END IF
3570     PRINT "WIN4 check failed - program stopped"
3580     STOP
3590   END IF
3600   natdrv4win4$=a$
3610   WIN_USE 4,natdrv4win4$
3620 END DEFine check_win4
3630 :
3640 DEFine FuNction make_all_months$
3650 REMark this makes a string "JanFeb..." in the current language
3660 REMark this should be called during the initialisation part
3670 REMark eg. all_months$=make_all_months$ - dates_init below is a handy proc for that
3680 LOCal string$,lp%,a$,temp
3690   string$="":a$="":temp=0
3700   temp=60*60*24*31
3710   FOR lp%=0 TO 11
3720     a$=DATE$(lp%*temp)
3730     string$=string$&a$(6 TO 8)
3740   END FOR lp%
3750   RETurn string$
3760 END DEFine make_all_months$
3770 :
3780 DEFine FuNction make_date$(dflag%,what_date)
3790 REMark returns date as "01.01.1991" (dflag%=1) or "1991.01.31" (dflag%=0)
3800 REMark if what_date<>0, then it is this date that will be returned
3810 REMark this presumes that a variable "all_month$" exists!
3820   LOCal a$,b$,res
3830   b$=""
3840   IF what_date
3850     a$=DATE$(what_date)         : REMark make date passed as param into string
3860   ELSE
3870     a$=DATE$                    : REMark current date into string
3880   END IF
3890   b$=a$(6 TO 8)                 : REMark 3 letter month abbreviation
3900   res= b$ INSTR all_months$             : REMark find it
3910   IF NOT res
3920        all_months$=make_all_months$     : REMark not found?, make all_month$
3930        res= b$ INSTR all_months$        : REMark and retry
3940   END IF
3950   res=(res+2)/3                 : REMark this is the month in figures
3960   b$=res:IF res<10:b$="0"&b$            : REMark add leading 0 if necessary
3970   IF dflag%:RETurn a$(10 TO 11)&"."&b$&"."&a$(1 TO 4)
3980   RETurn a$(1 TO 4)&"."&b$&"."&a$(10 TO 11)
3990 END DEFine make_date$
4000 :
4010 REMark ------------- zipping --------------
4020 :
4030 DEFine PROCedure zip1
4040   IF a$<>""
4050     IF "binaries" INSTR a$:binaries%=1
4060     IF "delete" INSTR a$: del%=1
4070     IF "docopy" INSTR a$:docopy%=1
4080     IF "quit" INSTR a$:quit%=1
4090     t%="version" INSTR a$
4100     IF t% : version$=a$(t%+7 TO t%+10)
4110   END IF
4120 END DEFine
4130 :
4140 DEFine PROCedure do_my_zips
4150   del_zips                              : REMark delete all zip files
4160   do_all
4170   do_individual_dirs
4180   IF docopy%:  do_copy a$
4190 END DEFine p
4200 :
4210 DEFine PROCedure do_all
4220 REMark this zips the entire SMSQE sources
4230 REMark ensure that all non source files have been erased first!
4240 LOCal zip$
4250   del_zips
4260   IF binaries%:do_binaries del%
4270   PRINT "Zipping all to one zip file "
4280   do_zip " -Q4r9 "&ddr$&smsqe$&" dd_* dv3_* ee_* extras_* iod_* keys_* lang_* mac_* minerva_* sbsext_* smsq_* sys_* uti_* util_*  changes_txt readme_txt styleguide_txt whats_new_txt licence_doc licence_txt"
4290 END DEFine do_all
4300 :
4310 DEFine PROCedure do_individual_dirs
4320 LOCal lp%,dirname$
4330   RESTORE CURRENT_LINE : REMark make sure no other DATA statement is between this proc anf the "del_zips" proc
4340   REPeat lp%
4350     READ dirname$
4360     IF dirname$="":EXIT lp%
4370     PRINT "Zipping "&dirname$
4380     do_zip " -Q4r9 "&ddr$&dirname$&".zip "&dirname$&"_*"
4390   END REPeat lp%
4400   do_qxl_pc
4410 END DEFine do_individual_dirs
4420 :
4430 DEFine PROCedure do_qxl_pc
4440 LOCal a$
4450   a$="nfa1_qxl_*"
4460   do_zip " -Q4j9 "&ddr$&"qxl.zip "& a$
4470 END DEFine do_qxl_pc
4480 :
4490 DEFine PROCedure sa
4500   SAVE_O dev8_extras_zip_it_bas
4510 END DEFine sa
4520 :
4530 DEFine PROCedure del_zips
4540 REMark just delete the zip files
4550 LOCal lp%,dirname$
4560   DELETE ddr$&smsqe$
4570   RESTORE CURRENT_LINE
4580   REPeat lp%
4590     READ dirname$
4600     IF dirname$="":EXIT lp%
4610   DELETE ddr$&dirname$&".zip"
4620   END REPeat lp%
4630   DATA "dd","dv3","ee","extras","iod","keys","lang","mac","minerva","sbsext","smsq","sys","uti","util","qxl"
4640   DATA ""
4650 END DEFine d
4660 :
4670 DEFine PROCedure s
4680 REMark this resets the data etc dirs to acceptable values, no longer used
4690   DEST_USE dev1_
4700   DATA_USE dev1_basic_
4710   PROG_USE dev1_progs_
4720 END DEFine s
4730 :
4740 DEFine PROCedure do_binaries (do_delete%)
4750 REMark this zips all binary versions and then possibly deletes them
4760 REMark they are zipped into one big fila and into individual files
4770   LOCal nbr%,lp%,source$,dest$,zip$,f$,zp$
4780   f$ =ddr$&"smsqe"&version$&"_binaries.zip"
4790   CLS
4800   REMark first of all, all into one
4810   PRINT "Zipping binaries into one file..."
4820   zip$ =' -Q4j9 '&f$&' '
4830   RESTORE CURRENT_LINE
4840   REPeat lp%
4850     READ source$,dest$
4860     IF source$="":EXIT lp%
4870     PRINT "copying "& source$
4880     COPY_O source$,ddr$&dest$
4890     zip$=zip$&ddr$&dest$ & ' '
4900     IF do_delete% AND NOT ("_txt" INSTR source$): DELETE source$
4910   END REPeat lp%
4920   DELETE f$
4930   do_zip zip$
4940 :
4950   REMark now zip up each individual file
4960   PRINT "Zipping each binar separately..."
4970   zip$ =' -Q4j9 '&f$&' '
4980   RESTORE CURRENT_LINE
4990   REPeat lp%
5000     READ source$,dest$
5010     PRINT dest$
5020     IF source$="" OR "_txt" INSTR source$:EXIT lp%
5030     PRINT "copying "& source$
5040     COPY_O source$,ddr$&dest$
5050     zp$=zip$&ddr$&dest$&".zip" &" " & ddr$&dest$
5060     do_zip zp$
5070   END REPeat lp%
5080   DELETE f$
5090   do_zip zip$
5100   REMark now delete destinations
5110   RESTORE CURRENT_LINE
5120   REPeat lp%
5130     READ dest$,dest$
5140     IF dest$="":EXIT lp%
5150     DELETE ddr$&dest$
5160   END REPeat lp%
5170   PRINT "Binaries done..."
5180   DATA "dev8_smsq_atari_SMSQ.PRG",  "SMSQE.PRG"       : REMark PRG file for ATARI ST/TT, ready to be used
5190   DATA "dev8_smsq_gold_gold",       "GoldCard_bin"    : REMark gold card Ql colours
5200   DATA "dev8_smsq_gold_gold8",      "GoldCard_256colours_bin"
5210   DATA "dev8_smsq_q40_rom",         "Q40_rom"         : REMark q40/q60 use as rom / lrespr file
5220   DATA "dev8_smsq_qxl_smsqe.exe",   "SMSQEQXL.EXE"    : REMark EXE to be run under DOS on i386 compatible systems with a QXL card installed, ready to be used
5230   DATA "dev8_smsq_aurora_SMSQE",    "Aurora_bin"      : REMark aurora 8 bit
5240   DATA "dev8_smsq_java_java",       "SMSQE"           : REMark Code file for SMSQmulator, ready to be used
5250   DATA "nfa1_Q68_SMSQ.WIN",         "Q68_SMSQ.WIN"    : REMark smsqe for Q68, win drive
5260   DATA "dev8_smsq_qpc_smsqe.bin",   "SMSQE.bin"       : REMark Code file for QPC2, ready to be used
5270   DATA "dev8_ee_ptr_gen",           "ptr_gen"         : REMark Part of Extended Environment for the non SMSQ/E (e.g. QDOS, SMSQ), ready to be used
5280   DATA "dev8_ee_wman_wman",         "wman"            : REMark Part of Extended Environment for the non SMSQ/E (e.g. QDOS, SMSQ), ready to be used
5290   DATA "dev8_ee_hot_rext_english",  "hot_rext_english": REMark Part of Extended Environment for the non SMSQ/E (e.g. QDOS, SMSQ), ready to be used
5300   DATA "dev8_ee_hot_rext_french",   "hot_rext_french" : REMark Part of Extended Environment for the non SMSQ/E (e.g. QDOS, SMSQ), ready to be used
5310   DATA "dev8_ee_hot_rext_german",   "hot_rext_german" : REMark Part of Extended Environment for the non SMSQ/E (e.g. QDOS, SMSQ), ready to be used
5320   DATA "dev8_whats_new_txt",        "whats_new_txt"
5330   DATA "dev8_changes_txt",          "changes_txt"
5340   DATA "dev8_readme_txt",           "readme_txt"
5350   DATA "","" : REMark leave TWO empty strings here
5360 END DEFine do_binaries
5370 :
5380 DEFine PROCedure do_zip (zip$)
5390 LOCal destt$,progg$,dataa$
5400   destt$=DESTD$
5410   progg$=PROGD$
5420   dataa$=DATAD$
5430   DEST_USE "dev8_"  :PROG_USE "dev8_" :DATA_USE "dev8_"
5440   EW dev1_progs_zip;zip$
5450   DEST_USE destt$ :PROG_USE progg$ :DATA_USE dataa$
5460 END DEFine do_zip
5470 :
5480 DATA "dev8_ee_wman_erstr_asm","dev8_ee_wman_data_asm"
5490 DATA "dev8_iod_con2_ptr_wremv_asm"
5500 DATA ""
5510 :
5520 DEFine PROCedure do_copy
5530 LOCal lp%,a$ ,f$
5540   PAUSE 80
5550   COPY_O ddr$&smsqe$,"nfa2_new_"&smsqe$
5560   RESTORE 830
5570   REPeat lp%
5580     READ a$
5590     IF a$="":EXIT lp%
5600     COPY_O ddr$&a$&".zip","nfa2_new_"&a$&".zip"
5610   END REPeat lp%
5620   COPY_O ddr$&"qxl.zip","nfa2_new_qxl.zip"
5630   f$ =ddr$&"smsqe"&version$&"_binaries.zip"
5640   COPY_O f$,"nfa2_new_"&"smsqe"&version$&"_binaries.zip"
5650 END DEFine do_copy
5660 :
5670 DEFine PROCedure wdel_f (source$,array_with_extns$)
5680 REMark Delete either all files on dir$, or only all files with any extension
5690 REMark contained in the array_with_extns$ string array. This does NOT recurse
5700 REMark into subdirs.
5710 LOCal lp%,iters%,chan%,myname$,a$,lp2%,dir$
5720   myname$="wdel_f" &RND(0 TO 100) &RND(0 TO 100) &RND(0 TO 100) &RND(0 TO 100) &RND(0 TO 100)
5730   chan%=FOP_OVER("ram1_"&myname$)
5740   IF chan%<0:RETurn             : REMark can't open channel, give up
5750   IF LEN(source$)>5
5760     dir$=source$(1 TO 5)
5770   ELSE
5780     dir$=source$
5790   END IF
5800   DIR#chan%,source$
5810   GET#chan%\0
5820   INPUT#chan%,a$
5830   INPUT#chan%,a$
5840   iters%=DIMN(array_with_extns$,1)
5850   IF iters%=0
5860                                     : REMark no extension
5870 PRINT "now"
5880     REPeat lp%
5890       IF EOF(#chan%):EXIT lp%
5900       INPUT#chan%,a$
5910       IF a$==myname$ OR a$="":NEXT lp%
5920       IF endswith% (" ->",a$): NEXT lp%: REMark don't go into subdirs
5930       DELETE dir$&a$
5940     END REPeat lp%
5950   ELSE
5960                                   : REMark only delete files with matching extension
5970     REPeat lp%
5980       IF EOF(#chan%):EXIT lp%
5990       INPUT#chan%,a$
6000       IF a$==myname$ OR a$="":NEXT lp%
6010       FOR lp2%=0 TO iters%
6020         IF endswith% (array_with_extns$(lp2%),a$)
6030           DELETE dir$&a$
6040           EXIT lp2%
6050         END IF
6060       END FOR lp2%
6070     END REPeat lp%
6080   END IF
6090   CLOSE#chan%
6100   DELETE "ram1_"&myname$
6110 END DEFine wdel_f
6120 :
6130 DEFine FuNction endswith%(extension$,name$)
6140 REMark checks whether name$ ends with extension$. Returns 1 if yes, 0 if not. If
6150 REMark either of the params is "", returns 0.
6160 LOCal elen%,nlen%
6170   IF extension$="" OR name$="":RETurn 0
6180   elen%=LEN(extension$)
6190   nlen%=LEN(name$)
6200   IF elen%>nlen%:RETurn 0
6210   IF name$(nlen%-elen%+1 TO)== extension$: RETurn 1
6220   RETurn 0
6230 END DEFine endswith%
6240 :
6250 DEFine PROCedure w
6260 LOCal extns(0)
6270   DIM extns$(1,3)
6280   extns$(1)="asm"
6290   wdel_f "ram3_",extns$
6300 END DEFine w
