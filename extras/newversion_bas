100 CLS#1:CLS#2
110 init_vars
120 PRINT resultnfa$
130 get_current_drive_assignments   : REMark store current drive assignments for later restoration
140 show_info_and_warning
150 CLS
160 get_and_show_version
170 make_bins% =1
180 do_zip%    =1
190 make_html% =1
200 p make_bins%,do_zip%,make_html% : REMark do (all or part of) the work
210 restore_drive_assignments       : REMark reset drives to their original values
220 :
230 DEFine PROCedure init_vars
240   base_native$=NFA_USE$(1)      : REMark bas native dir, which must contain the "website" subdir
250   windrv%=4
260   windrive$="win"&windrv%&"_" : REMark windrive where files will be copied to          "
270   htmldrive$=base_native$&"website/" : REMark drive where html templates and required files are
280   resultnfa$=base_native$&"website/new/": REMark where result files will go
290   all_months$=make_all_months$  : REMark months strings
300   ichan%=2:ochan%=1             : REMark report channels
310   ddr$="ram3_"                  : REMark temp dir
320   qpc%=0                        : REMark not running under QPC
330 END DEFine init_vars
340 :
350 DEFine PROCedure show_info_and_warning
360   PRINT "This program makes a new version of"
370   PRINT "the SMSQ/E source files, ready to "
380   PRINT "upload to the website."
390   PRINT
400   UNDER 1
410   PRINT "READ the manual in the extras_help directory before use."
420   UNDER 0
430   PRINT
440   PRINT "This program makes WIN4_ with the sources,"
450   PRINT "it also makes the zip file conaining all"
460   PRINT "the sources and the zip files for the"
470   PRINT "individual source subdirectories and the."
480   PRINT "binaries. These files are first created"
490   PRINT "in "&ddr$&" and are then copied to:"
500   PRINT "NFA8_"
510   PRINT
520   PRINT "!!!!!"
530   PRINT "NFA7_ will be set to ";htmldrive$;"."
540   PRINT  "Make sure no files are open to this drive"
550   PRINT
560   PRINT "NFA8_ will be set to ";resultnfa$;"."
570   PRINT "ALL FILES CURRENTLY IN NFA8_ WILL BE DELETED!!!!"
580   PRINT
590   PRINT "WIN4_ will be set to "& resultnfa$&"SMSQE"&smsqe$&".win'"
600   PRINT "IF A FILE OF THIS NAME ALREADY EXISTS, IT WILL"
610   PRINT "BE DELETED FIRST!"
620   PRINT "WIN4 IS THEN FORMATTED!"
630   PRINT "!!!!!"
640   PRINT
650   PRINT "'CLINE_BIN' AND 'OUTPTR_BIN' MUST BE LOADED !"
660   PRINT
670   PRINT "This program also temporarily sets the"
680   PRINT "DATA_USE and PROG_USE dirs"
690   PRINT "(it resets them at the end)."
700   PRINT
710   PRINT "----------------------"
720   PRINT
730   PRINT "HIT ENTER TO CONTINUE OR ANY OTHER KEY TO STOP"
740 :
750   a$=INKEY$(-1)
760   IF a$<>CHR$(10):STOP
770 END DEFine show_info_and_warning
780 :
790 DEFine PROCedure get_and_show_version
800   get_smsqe_version ichan%,ochan%
810   smsqename$="smsqe"&smsq_vers$
820   PRINT#ochan%,"-> SMSQE name is : ";smsqename$
830   PRINT#ochan%,"-> SMSQE number is : ";svers$
840 END DEFine get_and_show_version
850 :
860 DEFine PROCedure sa
870   SAVE_O dev8_extras_newversion_bas
880 END DEFine sa
890 :
900 DEFine PROCedure p (make_binaries%,do_zip%,make_html%)
910 LOCal a$
920   IF NOT IS_EXTN ("current_line")
930      PRINT "CLINE_bin isn't loaded. Failed and stopped"
940      STOP
950   END IF                          : REMark if outptr isn't loaded prog breaks at "is_extn" above
960   check_native_dirs ichan%,ochan%
970   check_and_set_win4              : REMark check win4 can be used
980   CLS#ichan%
990   IF make_binaries%
1000     INPUT #ochan%,"Compile everything and recreate binaries first? (y/n) ";a$
1010     IF a$=="y"
1020       PRINT "Compiling and creating binaries...."
1030       EW dev8_extras_exe_SMSQEmake;"-q0 -as -mk -ta -sa"
1040      END IF
1050     PRINT #ochan%,"Making Q68 win container..."
1060     make_q68_win ""
1070   END IF
1080   IF do_zip%
1090     IF make_binaries%
1100       zip_binaries  1
1110       PRINT#ochan%,"Zipping all binaries to ";ddr$
1120       wdel_f ddr$,""
1130     END IF
1140     PRINT#ochan%;"Deleting all non source including binaries..."
1150     PRINT#ochan%;"Please be patient..."
1160     EW dev8_extras_del_all_bas;"auto stop"
1170     zip_sources 1
1180     DELETE "nfa8_SMSQE"&svers$&".WIN"
1190     DELETE "NFA8_Q68_SMSQ.WIN"
1200     zip_Win_file
1210     make_container      : REMark Now making the container file with the sources (zip under linux)
1220   END IF
1230   IF make_html%
1240     PRINT#ochan%,"Making html files..."
1250     today$=make_date$(0,0)
1260     make_oldversion_html
1270     make_versions ochan%
1280     make_repl_html "indexphp.template","index.php"
1290     make_repl_html "binaries.template","binaries.html"
1300   END IF
1310   PRINT#ochan%
1320   PRINT#ochan%," ----------------- DONE -----------------"
1330 END DEFine p
1340 :
1350 DEFine PROCedure get_smsqe_version (ichan%,ochan%)
1360 REMark this gets the version of SMSQE
1370 LOCal chan%,lp%,a$,t%
1380   CLS#ichan%
1390   INPUT#ochan%, "SMSQE version in format x.yy? (or Enter to get it from dev8_smsq_smsq_version_asm) ",smsq_vers$
1400   IF smsq_vers$=""
1410     chan%=FOP_IN('dev8_smsq_smsq_version_asm')
1420     REPeat lp%
1430       IF EOF(#chan%):EXIT lp%
1440       INPUT#chan%,a$
1450       IF "smsq_vers" INSTR a$  AND "equ" INSTR a$
1460         t%="'" INSTR a$
1470         IF t%:smsq_vers$=a$(t%+1 TO t%+4)
1480       END IF
1490     END REPeat lp%
1500     CLOSE#chan%
1510   END IF
1520   IF smsq_vers$=""
1530     PRINT#ochan%, "Failed to get smsq version!"
1540   ELSE
1550     PRINT#ochan%, "-> smsqe version : ",smsq_vers$
1560   END IF
1570   t%='.' INSTR smsq_vers$
1580   IF t%
1590     svers$=smsq_vers$(1)&smsq_vers$(3 TO 4)
1600   ELSE
1610     PRINT "No decimal point in version : wrong! Failed and stopped"
1620     STOP
1630   END IF
1640 END DEFine get_smsqe_version
1650 :
1660 DEFine PROCedure check_and_set_win4
1670 REMark check that the native file corresponding to win4 doesn't exist (!!!)
1680 LOCal a$,t%,natdrv4win4
1690   PRINT#ochan%,"Checking and setting win4_"
1700   natdrv4win4$="nfa8_SMSQE"&svers$&'.WIN'
1710   DELETE natdrv4win4$
1720   t%=FTEST(natdrv4win4$)
1730   IF t%<>-7
1740     CLS:PRINT "Error with win4"
1750     IF t%=0
1760       PRINT "It already exists"
1770     ELSE
1780       REPORT#1,t%
1790     END IF
1800     PRINT "WIN4 check failed - program stopped"
1810     STOP
1820   END IF
1830   WIN_DRIVE 4,NFA_USE$(8)&"SMSQE"&svers$&".WIN"
1840   PRINT#ochan%, "Win4 is: ";NFA_USE$(8)&"SMSQE"&svers$&".WIN"
1850 END DEFine check_and_set_win4
1860 :
1870 DEFine PROCedure check_native_dirs (ichan%,ochan%)
1880 LOCal lp%,t%,drive$
1890   PRINT #ochan%,"Setting NFA7_ and NFA8_"
1900   NFA_USE 7,htmldrive$
1910   t%= FTEST ("nfa7_")
1920   SELect ON t%
1930     =-1:PRINT#ochan%;htmldrive$;" is not a valid directory"
1940         PRINT#ochan%,'There seems to be a simple file with that name already!'
1950         PRINT#ochan%,"Failed and stopped":STOP
1960     =-7:
1970         PRINT "Cannot find ";htmldrive$
1980         PRINT#ochan%,"Failed and stopped":STOP
1990     =0:
2000     = REMAINDER
2010         PRINT "Error for ";htmldrive$;" : ";t%;" - STOPPED":STOP
2020   END SELect
2030   NFA_USE 8,resultnfa$
2040   t%= FTEST ("nfa8_")
2050   SELect ON t%
2060     =0 :PRINT#ochan%,"Deleting all files on nfa8_":wdel_f "nfa8_",""
2070     =-1:PRINT#ochan%;"Couldn't make directory '"&resultnfa$
2080         PRINT#ochan%,'There seems to be a simple file with that name already!'
2090         PRINT#ochan%,"Failed and progam is stopped":STOP
2100     =-7:
2110         MAKE_DIR "nfa8_"
2120     = REMAINDER
2130         PRINT "Error for ";resultnfa$;" : ";t%;" - STOPPED":STOP
2140   END SELect
2150   t%= FTEST ("nfa8_html_")
2160   SELect ON t%
2170     =0 :PRINT#ochan%,"Deleting all files on nfa8_html_":wdel_f "nfa8_html_",""
2180     =-1:PRINT#ochan%;"Couldn't make directory nfa8_html_"
2190         PRINT#ochan%,'There seems to be a simple file with that name already!'
2200         PRINT#ochan%,"Failed and progam is stopped":STOP
2210     =-7:
2220         MAKE_DIR "nfa8_html_"
2230     = REMAINDER
2240         PRINT "Error for ";resultnfa$;" : ";t%;" - STOPPED":STOP
2250   END SELect
2260   PRINT#ochan%,"NFA7_ is: ";NFA_USE$(7)
2270   PRINT#ochan%,"NFA8_ is: ";NFA_USE$(8)
2280 END DEFine check_native_dirs
2290 :
2300 DEFine PROCedure make_q68_win (extn$)
2310 LOCal mblen,c%,fleng,mdir$,olddir$
2320 :
2330   olddir$=WIN_DRIVE$(windrv%)
2340   WIN_DRIVE windrv%,""
2350   COPY_O "nfa7_1MB.win","nfa8_Q68_SMSQ.WIN" : REMark make copy of empty template file, set it as winX
2360   mdir$=NFA_USE$(8)
2370   WIN_DRIVE windrv%,mdir$&"Q68_SMSQ.WIN"    : REMark use as win drive
2380 :                                       : REMark copy Q68 smqe file to it
2390   PAUSE 50
2400   IF extn$<>"":COPY_O "dev1_progs_MenuConf_INF_"&extn$,"dev1_progs_MenuConf_INF"
2410   EW menuconfig;"\q\uDEV8_smsq_q68_SMSQ_4_WIN" : REMark set config
2420   COPY_O "DEV8_smsq_q68_SMSQ_4_WIN","win"&windrv%&"_Q68_SMSQ"
2430   MAKE_DIR "win"&windrv%&"_config"
2440   COPY DEV1_booty_Menu_rext_8e04,"win"&windrv%&"_config_menu_rext_english"
2450   COPY DEV1_progs_MenuConfig,"win"&windrv%&"_config_MenuConfig"
2460   COPY DEV1_q68_boot,"win"&windrv%&"_boot"
2470 :
2480   WIN_DRIVE windrv%,olddir$                   : REMark win drive X no longer exists
2490   IF extn$<>""
2500     COPY_O "dev1_progs_MenuConf_INF_mine","dev1_progs_MenuConf_INF"
2510   END IF
2520 :
2530   REMark PRINT "Created Q68 WIN file"
2540 :
2550 END DEFine make_q68_win
2560 :
2570 DEFine PROCedure make_versions (ochan%)
2580   PRINT#ochan%,"Making changes html text"
2590   EW dev8_extras_html_changes_bas;today$&" quit"
2600   PRINT#ochan%,"Making versions html text"
2610   EW dev8_extras_html_versions_bas;today$&" quit"
2620 END DEFine make_versions
2630 :
2640 DEFine PROCedure make_repl_html (infile$,outfile$)
2650 LOCal lp%,c%,o%,a$
2660   PRINT#ochan%,"Making "&outfile$&" file"
2670   c%=FOP_IN ("nfa7_"&infile$)
2680   IF c%<0:PRINT#ochan%, "failed to open input : ";c%;". STOPPED":STOP
2690   o%=FOP_OVER ("nfa8_html_"&outfile$)
2700   IF o%<0:PRINT#ochan%, "failed to open output - STOPPED":STOP
2710   REPeat lp%
2720     IF EOF(#c%):EXIT lp%
2730     INPUT#c%,a$
2740     a$=repl$(a$)
2750     a$=repl$(a$)                : REMark yes, run this twice
2760     PRINT#o%,a$
2770   END REPeat lp%
2780   CLOSE#c%:CLOSE#o%
2790 END DEFine make_repl_html
2800 :
2810 DEFine FuNction repl$(a$)
2820 REMark replace values in strings
2830 REMark xxxversionxxx is,eg 3.14
2840 REMark xxxsmsqexxx is, eg 314/smsqe314
2850 REMark xxxsubdirxxx is, eg 314/
2860 REMark xxxdatexxx is, eg. 28.02.2017
2870 REMark xxxsversxxx is, eg 314
2880 REMark
2890 REMark  smsq_vers$=eg 3.14
2900 REMark  svers$= eg 314
2910 LOCal t%,front$,end$
2920   front$=a$
2930   t%="xxxversionxxx" INSTR front$
2940   IF t%
2950     front$=front$(1 TO t%-1)&smsq_vers$&front$(t%+13 TO)
2960   END IF
2970   t%="xxxsmsqexxx" INSTR front$
2980   IF t%
2990     front$=front$(1 TO t%-1)&svers$&"/smsqe"&svers$&front$(t%+11 TO)
3000   END IF
3010   t%="xxxsubdirxxx" INSTR front$
3020   IF t%
3030     front$=front$(1 TO t%-1)&svers$&"/"&front$(t%+12 TO)
3040   END IF
3050   t%="xxxdatexxx" INSTR front$
3060   IF t%
3070     front$=front$(1 TO t%-1)&today$&front$(t%+10 TO)
3080   END IF
3090   t%="xxxsversxxx" INSTR front$
3100   IF t%
3110     front$=front$(1 TO t%-1)&svers$&front$(t%+11 TO)
3120   END IF
3130   RETurn front$
3140 END DEFine repl$
3150 :
3160 DEFine PROCedure make_container
3170 REMark this formats the win container
3180 LOCal what_chan%,counter
3190   PRINT#ochan%,"Now making the container file--------"
3200   PRINT#ochan%,"Formatting "&windrive$& " !"
3210   FORMAT windrive$&"25_SMSQE"&smsq_vers$
3220   PRINT#ochan%
3230   counter=0
3240   what_chan%=FOP_OVER('ram1_mydirs')
3250   get_dir_structure "dev8_",what_chan%
3260   CLOSE#what_chan%
3270   PRINT#ochan%,"Got dir structure, sorting..."
3280   sort_it
3290   PRINT#ochan%,"Making dir structure"
3300   make_dir_structure "ram1_mydirs2",windrive$
3310   PRINT#ochan%,"copying files, be patient..."
3320   fbackup "dev8_",windrive$
3330 END DEFine make_container
3340 :
3350 DEFine PROCedure get_dir_structure (dirr$,what_chan%)
3360 LOCal a$,lp,chan%,device$,file$
3370 LOCal k
3380   file$="ram1_ddir"&counter&"pkk"
3390   chan%=FOP_OVER(file$)
3400   IF chan%<0:RETurn
3410   DIR#chan%,dirr$
3420   CLOSE#chan%
3430   device$=dirr$(1 TO 4)&'_'
3440   chan%=FOP_IN(file$)
3450   IF chan%<0:RETurn
3460   INPUT#chan%,a$
3470   INPUT#chan%,a$
3480   REPeat lp
3490     IF EOF(#chan%):EXIT lp
3500     INPUT#chan%,a$
3510     k=LEN(a$)
3520     IF k<4:NEXT lp:EXIT lp
3530     IF a$(k-2 TO k)=' ->'
3540       counter=counter+1
3550       PRINT#what_chan%,a$(1 TO k-3)
3560       a$=device$&a$(1 TO k-3)&"_"
3570       PRINT#ochan%, "OK: ";a$
3580       get_dir_structure a$,what_chan%
3590     END IF
3600   END REPeat lp
3610   CLOSE#chan%
3620   DELETE file$
3630 END DEFine get_dir_structure
3640 :
3650 DEFine PROCedure sort_it
3660 LOCal a$,array$(counter+10,30),chan%,c,lp
3670   chan%=FOP_IN('ram1_mydirs')
3680   c=1
3690   REPeat lp
3700     IF EOF(#chan%):EXIT lp
3710     INPUT#chan%,a$
3720     array$(c)=a$
3730     c=c+1
3740   END REPeat lp
3750   ASORT array$,0
3760   CLOSE#chan%
3770   chan%=FOP_OVER('ram1_mydirs2')
3780   FOR lp=1 TO c
3790     a$=array$(lp)
3800     PRINT#chan%,a$
3810   END FOR lp
3820   CLOSE#chan%
3830 END DEFine sort_it
3840 :
3850 DEFine PROCedure make_dir_structure (file$,drive$)
3860 LOCal lp%,a$,chan%
3870   chan%=FOP_IN(file$)
3880   PRINT#ichan%, file$,chan%
3890   IF chan%<0: PRINT "File "&file$&" doesn't exist":RETurn
3900   REPeat lp%
3910     IF EOF(#chan%):EXIT lp%
3920     INPUT#chan%,a$
3930     IF a$="":NEXT lp%:EXIT lp%
3940     PRINT#ichan%, "Making "&drive$&a$&" ..."
3950     MAKE_DIR drive$&a$
3960   END REPeat lp%
3970   CLOSE#chan%
3980 END DEFine make_dir_structure
3990 :
4000 DEFine PROCedure fbackup (source$,dest$)
4010 REMark makes a forced backup, eventually deleting newer files on dest
4020 REMark and replacing them with the files on source$
4030 REMark fbackup "dir_with_new_files", "dir_with_old_files"
4040 LOCal chan%,lp%,myfile$,a$,t%,tc%,len_old,len_new,date_old,date_new,b$,mdir$,mdest$
4050   FOR lp%=1 TO 50
4060     myfile$="ram8_diffs_txt"&RND(1 TO 80)&RND (1 TO 80) & RND (1 TO 50)
4070     chan%=FOP_NEW (myfile$)            : REMark try to open unique file
4080     IF chan%>0:EXIT lp%
4090   END FOR lp%
4100   IF source$(LEN(source$))<>"_":source$=source$&"_"
4110   IF dest$(LEN(dest$))<>"_":dest$=dest$&"_"
4120   PRINT#ichan%, 'source= ';source$
4130   PRINT#ichan%, "dest= ";dest$
4140   mdir$=source$(1 TO 5)
4150   mdest$=dest$(1 TO 5)
4160   IF chan%<0:RETurn                     : REMark ooops!!!!
4170   WDIR#chan%,source$                    : REMark dir of this rep in file
4180   GET#chan%\0                           : REMark reset file pointer to start
4190   REPeat lp%
4200     IF EOF(#chan%):EXIT lp%
4210     INPUT#chan%,a$                      : REMark get filename
4220     IF a$="":NEXT lp%
4230     t%= ' ->' INSTR a$                  : REMark is it a subdir?
4240     IF t%
4250       tc%=LEN(source$)-4
4260       c$=a$(tc% TO)
4270       c$=c$(TO LEN(c$)-3)&"_"
4280       fbackup mdir$&a$(TO LEN(a$)-3)&'_',dest$&c$
4290       NEXT lp%
4300     END IF
4310     tc%=FOP_IN(mdir$&a$)                : REMark open source file
4320     date_old=FUPDT(#tc%)                : REMark file date of "old" source file
4330     CLOSE#tc%
4340     tc%=LEN(source$)-4                  : REMark length of name w/o subdirectory
4350     b$=a$(tc% TO)
4360     DELETE dest$&b$
4370     COPY mdir$&a$,dest$&b$
4380     tc%=FOPEN(dest$&b$)
4390     SET_FUPDT#tc%,date_old
4400     CLOSE#tc%
4410   END REPeat lp%
4420   CLOSE#chan%
4430   DELETE myfile$
4440 END DEFine fbackup
4450 :
4460 DEFine FuNction make_all_months$
4470 REMark this makes a string "JanFeb..." in the current language
4480 REMark this should be called during the initialisation part
4490 REMark eg. all_months$=make_all_months$ - dates_init below is a handy proc for that
4500 LOCal string$,lp%,a$,temp
4510   string$="":a$="":temp=0
4520   temp=60*60*24*31
4530   FOR lp%=0 TO 11
4540     a$=DATE$(lp%*temp)
4550     string$=string$&a$(6 TO 8)
4560   END FOR lp%
4570   RETurn string$
4580 END DEFine make_all_months$
4590 :
4600 DEFine FuNction make_date$(dflag%,what_date)
4610 REMark returns date as "01.01.1991" (dflag%=1) or "1991.01.31" (dflag%=0)
4620 REMark if what_date<>0, then it is this date that will be returned
4630 REMark this presumes that a variable "all_month$" exists!
4640   LOCal a$,b$,res
4650   b$=""
4660   IF what_date
4670     a$=DATE$(what_date)         : REMark make date passed as param into string
4680   ELSE
4690     a$=DATE$                    : REMark current date into string
4700   END IF
4710   b$=a$(6 TO 8)                 : REMark 3 letter month abbreviation
4720   res= b$ INSTR all_months$             : REMark find it
4730   IF NOT res
4740        all_months$=make_all_months$     : REMark not found?, make all_month$
4750        res= b$ INSTR all_months$        : REMark and retry
4760   END IF
4770   res=(res+2)/3                 : REMark this is the month in figures
4780   b$=res:IF res<10:b$="0"&b$            : REMark add leading 0 if necessary
4790   IF dflag%:RETurn a$(10 TO 11)&"."&b$&"."&a$(1 TO 4)
4800   RETurn a$(1 TO 4)&"."&b$&"."&a$(10 TO 11)
4810 END DEFine make_date$
4820 :
4830 REMark ------------- zipping --------------
4840 :
4850 DEFine PROCedure zip_sources (docopy%)
4860   del_zips
4870   zip_all_sources_into_one_file
4880   zip_individual_source_dirs
4890   IF docopy%:  fbackup "ram3_","nfa8_"
4900   del_zips
4910 END DEFine zip_sources
4920 :
4930 DEFine PROCedure zip_all_sources_into_one_file
4940 REMark This zips the entire SMSQE sources into one zip file.
4950 REMark Ensure that all non source files have been erased first!
4960 LOCal zip$
4970   PRINT#ochan%, "Zipping all sources into one zip file... "
4980   do_zip " -Q4r9 "&ddr$&"smsqe"&svers$&".zip"&" dd_* dv3_* ee_* extras_* iod_* keys_* lang_* mac_* minerva_* sbsext_* smsq_* sys_* uti_* util_* nfa1_qxl_* changes_txt readme_txt styleguide_txt whats_new_txt licence_doc licence_txt "
4990 END DEFine zip_all_sources_into_one_file
5000 :
5010 DEFine PROCedure zip_Win_file
5020   PRINT#ochan%, "Zipping the win file..."
5030   do_zip " -Q4r9 nfa8_smsqe"&svers$&"src.win nfa8_SMSQE"&svers$&".WIN"
5040 END DEFine zip_Win_file
5050 :
5060 DEFine PROCedure zip_individual_source_dirs
5070 LOCal lp%,dirname$
5080   RESTORE CURRENT_LINE : REMark make sure no other DATA statement is between this proc anf the "del_zips" proc
5090   PRINT#ochan%,"Zipping individual source dirs..."
5100   REPeat lp%
5110     READ dirname$
5120     IF dirname$="":EXIT lp%
5130     PRINT#ichan%, "Zipping "&dirname$
5140     do_zip " -Q4r9 "&ddr$&dirname$&".zip "&dirname$&"_*"
5150   END REPeat lp%
5160   do_qxl_pc
5170   DATA "dd","dv3","ee","extras","iod","keys","lang","mac","minerva","sbsext","smsq","sys","uti","util"
5180   DATA ""
5190 END DEFine zip_individual_source_dirs
5200 :
5210 DEFine PROCedure do_qxl_pc
5220 LOCal a$
5230   a$="nfa1_qxl_*"
5240   PRINT#ichan%, "Zipping qxl..."
5250   do_zip " -Q4j9 "&ddr$&"qxl.zip "& a$
5260 END DEFine do_qxl_pc
5270 :
5280 DEFine PROCedure del_zips
5290 REMark delete all zips on ddr$
5300 LOCal extns(0)
5310   DIM extns$(1,3)
5320   extns$(1)="zip"
5330   wdel_f ddr$,extns$
5340 END DEFine del_zips
5350 :
5360 DEFine PROCedure s
5370 REMark this resets the data etc dirs to acceptable values, no longer used
5380   DEST_USE dev1_
5390   DATA_USE dev1_basic_
5400   PROG_USE dev1_progs_
5410 END DEFine s
5420 :
5430 DEFine PROCedure zip_binaries (do_delete%)
5440 REMark This zips all binary versions and then possibly deletes them.
5450 REMark They are zipped into one big file and into individual files
5460   LOCal nbr%,lp%,source$,dest$,zip$,f$,zp$
5470   f$ =ddr$&"smsqe"&svers$&"_binaries.zip"
5480   REMark first of all, all into one
5490   PRINT#ochan%, "Zipping binaries into one file..."
5500   zip$ =' -Q4j9 '&f$&' '
5510   RESTORE CURRENT_LINE
5520   REPeat lp%
5530     READ source$,dest$
5540     IF source$="":EXIT lp%
5550     PRINT#ichan%, "copying "& source$
5560     COPY_O source$,ddr$&dest$
5570     zip$=zip$&ddr$&dest$ & ' '
5580   END REPeat lp%
5590   do_zip zip$
5600 :
5610   REMark now zip up each individual file
5620   PRINT#ochan%, "Zipping each binary separately..."
5630   zip$ =' -Q4j9 '
5640   RESTORE CURRENT_LINE
5650   REPeat lp%
5660     READ source$,dest$
5670     IF source$="" OR "_txt" INSTR source$:EXIT lp%
5680     PRINT#ichan%, "copying "& source$
5690     COPY_O source$,ddr$&dest$
5700     zp$=zip$&ddr$&dest$&".zip" &" " & ddr$&dest$
5710     do_zip zp$
5720     IF do_delete% AND NOT ("_txt" INSTR source$): DELETE source$
5730   END REPeat lp%
5740   REMark now delete files that aren't zipped
5750   RESTORE CURRENT_LINE
5760   REPeat lp%
5770     READ dest$,dest$
5780     IF dest$="":EXIT lp%
5790     DELETE ddr$&dest$
5800   END REPeat lp%
5810   REMark now copy all remaining files to nfa8_
5820   fbackup ddr$,"nfa8_"
5830   PRINT#ochan%, "Binaries done..."
5840   DATA "dev8_smsq_atari_SMSQ.PRG",  "SMSQE.PRG"       : REMark PRG file for ATARI ST/TT, ready to be used
5850   DATA "dev8_smsq_gold_gold",       "GoldCard_bin"    : REMark gold card Ql colours
5860   DATA "dev8_smsq_gold_gold8",      "GoldCard_256colours_bin"
5870   DATA "dev8_smsq_q40_rom",         "Q40_rom"         : REMark q40/q60 use as rom / lrespr file
5880   DATA "dev8_smsq_qxl_smsqe.exe",   "SMSQEQXL.EXE"    : REMark EXE to be run under DOS on i386 compatible systems with a QXL card installed, ready to be used
5890   DATA "dev8_smsq_aurora_SMSQE",    "Aurora_bin"      : REMark aurora 8 bit
5900   DATA "dev8_smsq_java_java",       "SMSQE"           : REMark Code file for SMSQmulator, ready to be used
5910   DATA "nfa8_Q68_SMSQ.WIN",         "Q68_SMSQ.WIN"    : REMark smsqe for Q68, win drive
5920   DATA "dev8_smsq_qpc_smsqe.bin",   "SMSQE.bin"       : REMark Code file for QPC2, ready to be used
5930   DATA "dev8_ee_ptr_gen",           "ptr_gen"         : REMark Part of Extended Environment for the non SMSQ/E (e.g. QDOS, SMSQ), ready to be used
5940   DATA "dev8_ee_wman_wman",         "wman"            : REMark Part of Extended Environment for the non SMSQ/E (e.g. QDOS, SMSQ), ready to be used
5950   DATA "dev8_ee_hot_rext_english",  "hot_rext_english": REMark Part of Extended Environment for the non SMSQ/E (e.g. QDOS, SMSQ), ready to be used
5960   DATA "dev8_ee_hot_rext_french",   "hot_rext_french" : REMark Part of Extended Environment for the non SMSQ/E (e.g. QDOS, SMSQ), ready to be used
5970   DATA "dev8_ee_hot_rext_german",   "hot_rext_german" : REMark Part of Extended Environment for the non SMSQ/E (e.g. QDOS, SMSQ), ready to be used
5980   DATA "dev8_whats_new_txt",        "whats_new_txt"
5990   DATA "dev8_changes_txt",          "changes_txt"
6000   DATA "dev8_readme_txt",           "readme_txt"
6010   DATA "","" : REMark leave TWO empty strings here
6020 END DEFine zip_binaries
6030 :
6040 DEFine PROCedure do_zip (zip$)
6050 LOCal destt$,progg$,dataa$
6060   destt$=DESTD$
6070   progg$=PROGD$
6080   dataa$=DATAD$
6090   DEST_USE "dev8_"  :PROG_USE "dev8_" :DATA_USE "dev8_"
6100   EW dev1_progs_zip;zip$
6110   DEST_USE destt$ :PROG_USE progg$ :DATA_USE dataa$
6120 END DEFine do_zip
6130 :
6140 DEFine PROCedure do_copy
6150 LOCal lp%,a$ ,f$
6160   PAUSE 80
6170   COPY_O ddr$&smsqe$,"nfa2_new_"&smsqe$
6180   RESTORE 950
6190   REPeat lp%
6200     READ a$
6210     IF a$="":EXIT lp%
6220     COPY_O ddr$&a$&".zip","nfa2_new_"&a$&".zip"
6230   END REPeat lp%
6240   COPY_O ddr$&"qxl.zip","nfa2_new_qxl.zip"
6250   f$ =ddr$&"smsqe"&version$&"_binaries.zip"
6260   COPY_O f$,"nfa2_new_"&"smsqe"&version$&"_binaries.zip"
6270 END DEFine do_copy
6280 :
6290 DEFine PROCedure wdel_f (source$,array_with_extns$)
6300 REMark Delete either all files on dir$, or only all files with any extension
6310 REMark contained in the array_with_extns$ string array. This does NOT recurse
6320 REMark into subdirs.
6330 LOCal lp%,iters%,chan%,myname$,a$,lp2%,dir$
6340   myname$="wdel_f" &RND(0 TO 100) &RND(0 TO 100) &RND(0 TO 100) &RND(0 TO 100) &RND(0 TO 100)
6350   chan%=FOP_OVER("ram1_"&myname$)
6360   IF chan%<0:RETurn             : REMark can't open channel, give up
6370   IF LEN(source$)>5
6380     dir$=source$(1 TO 5)
6390   ELSE
6400     dir$=source$
6410   END IF
6420   DIR#chan%,source$
6430   GET#chan%\0
6440   INPUT#chan%,a$
6450   INPUT#chan%,a$
6460   iters%=DIMN(array_with_extns$,1)
6470   IF iters%=0
6480                                     : REMark no extension - delete everything
6490     REPeat lp%
6500       IF EOF(#chan%):EXIT lp%
6510       INPUT#chan%,a$
6520       IF a$==myname$ OR a$="":NEXT lp%
6530       IF endswith% (" ->",a$): NEXT lp%: REMark don't go into subdirs
6540       DELETE dir$&a$
6550     END REPeat lp%
6560   ELSE
6570                                   : REMark only delete files with matching extension
6580     REPeat lp%
6590       IF EOF(#chan%):EXIT lp%
6600       INPUT#chan%,a$
6610       IF a$==myname$ OR a$="":NEXT lp%
6620       FOR lp2%=0 TO iters%
6630         IF endswith% (array_with_extns$(lp2%),a$)
6640           DELETE dir$&a$
6650           EXIT lp2%
6660         END IF
6670       END FOR lp2%
6680     END REPeat lp%
6690   END IF
6700   CLOSE#chan%
6710   DELETE "ram1_"&myname$
6720 END DEFine wdel_f
6730 :
6740 DEFine FuNction endswith%(extension$,name$)
6750 REMark checks whether name$ ends with extension$. Returns 1 if yes, 0 if not. If
6760 REMark either of the params is "", returns 0.
6770 LOCal elen%,nlen%
6780   IF extension$="" OR name$="":RETurn 0
6790   elen%=LEN(extension$)
6800   nlen%=LEN(name$)
6810   IF elen%>nlen%:RETurn 0
6820   IF name$(nlen%-elen%+1 TO)== extension$: RETurn 1
6830   RETurn 0
6840 END DEFine endswith%
6850 :
6860 DEFine PROCedure w
6870 LOCal extns(0)
6880   DIM extns$(1,3)
6890   extns$(1)="asm"
6900   wdel_f "ram3_",extns$
6910 END DEFine w
6920 :
6930 DEFine PROCedure get_current_drive_assignments
6940   current_nfa7$=NFA_USE$(7)
6950   current_nfa8$=NFA_USE$(8)
6960   current_win4$=WIN_DRIVE$(windrv%)
6970 END DEFine get_current_drive_assignment
6980 :
6990 DEFine PROCedure restore_drive_assignments
7000   NFA_USE 7,current_nfa7$
7010   NFA_USE 8,current_nfa8$
7020   WIN_DRIVE windrv%,current_win4$
7030 END DEFine restore_drive_assignments
7040 :
7050 DEFine PROCedure make_oldversion_html
7060 LOCal c%,o%,a$,lp%,versn,versn$
7070   PRINT#ochan%, "Making oldversions html"
7080   c%=FOP_IN("nfa7_oldversions.html")
7090   IF c%<0:PRINT#ochan%, "failed to open input : ";c%;". STOPPED":STOP
7100   o%=FOP_OVER ("nfa8_html_oldversions.html")
7110   IF o%<0:PRINT#ochan%, "failed to open output - STOPPED":STOP
7120   versn=svers$
7130   versn=versn-1
7140   versn$=versn
7150   versn$=versn$(1)&"."&versn$(2 TO)
7160   REPeat lp%
7170     IF EOF(#c%):EXIT lp%
7180     INPUT#c%,a$
7190     IF "</ul>" INSTR a$
7200        PRINT#o%,'     <li> <a href = "old/smsqe'&versn&'src.win.zip">SMSQ/E v. '&versn$&'</a></li>'
7210      END IF
7220      PRINT#o%,a$
7230    END REPeat lp%
7240    CLOSE#c%
7250    CLOSE#o%
7260 REMark   copy_o "nfa8_html_oldversions.html","nfa7_oldversions.html"
7270 END DEFine make_oldversion_html
7280 :
