100 p
110 :
120 DEFine PROCedure p
130   make_bins% =1     : REMark 1 tomake bin files, 0 if not
140   do_zip%    =1     : REMark 1 to make zip files, 0 if not
150   make_html% =1     : REMark 1 to make html files, 0 if not
160   strt make_binaries%,do_zip%,make_html%
170 END DEFine p
180 :
190 DEFine PROCedure strt (make_binaries%,do_zip%,make_html%)
200   init_vars
210   CLS#ochan%:CLS#ichan%
220   PRINT#ochan%, "Files got to : ";resultnfa$
230   get_current_drive_assignments   : REMark store current drive assignments for later restoration
240   show_info_and_warning
250   CLS#ochan%
260   get_and_show_version
270   make_new_v make_bins%,do_zip%,make_html% : REMark do (all or part of) the work
280   restore_drive_assignments       : REMark reset drives to their original values
290 END DEFine strt
300 :
310 DEFine PROCedure init_vars
320   base_native$=NFA_USE$(1)      : REMark base native dir, which must contain the "website/smsqe" subdir
330   windrv%=4
340   windrive$="win"&windrv%&"_"   : REMark windrive where files will be copied to          "
350   htmldrive$=base_native$&"/website/smsqe/" : REMark drive where html templates and required files are
360   resultnfa$=base_native$&"/website/smsqe/new/": REMark where result files will go
370   all_months$=make_all_months$  : REMark months strings
380   ichan%=2:ochan%=1             : REMark report channels
390   ddr$="ram3_"                  : REMark temp dir
400   qpc%=0                        : REMark not running under QPC
410 END DEFine init_vars
420 :
430 DEFine PROCedure show_info_and_warning
440   PRINT "This program makes a new version of"
450   PRINT "the SMSQ/E source files, ready to "
460   PRINT "upload to the website."
470   PRINT
480   UNDER 1
490   PRINT "READ the manual in the extras_help directory before use."
500   UNDER 0
510   PRINT
520   PRINT "This program makes WIN4_ with the sources,"
530   PRINT "it also makes the zip file conaining all"
540   PRINT "the sources and the zip files for the"
550   PRINT "individual source subdirectories and the."
560   PRINT "binaries. These files are first created"
570   PRINT "in "&ddr$&" and are then copied to:"
580   PRINT "NFA8_"
590   PRINT
600   PRINT "!!!!!"
610   PRINT "NFA7_ will be set to ";htmldrive$;"."
620   PRINT  "Make sure no files are open to this drive"
630   PRINT
640   PRINT "NFA8_ will be set to ";resultnfa$;"."
650   PRINT "ALL FILES CURRENTLY IN NFA8_ WILL BE DELETED!!!!"
660   PRINT
670   PRINT "WIN4_ will be set to "& resultnfa$&"SMSQE"&smsqe$&".win'"
680   PRINT "IF A FILE OF THIS NAME ALREADY EXISTS, IT WILL"
690   PRINT "BE DELETED FIRST!"
700   PRINT "WIN4 IS THEN FORMATTED!"
710   PRINT "!!!!!"
720   PRINT
730   PRINT "'CLINE_BIN' AND 'OUTPTR_BIN' MUST BE LOADED !"
740   PRINT
750   PRINT "This program also temporarily sets the"
760   PRINT "DATA_USE and PROG_USE dirs"
770   PRINT "(it resets them at the end)."
780   PRINT
790   PRINT "----------------------"
800   PRINT
810   PRINT "HIT ENTER TO CONTINUE OR ANY OTHER KEY TO STOP"
820 :
830   a$=INKEY$(-1)
840   IF a$<>CHR$(10):STOP
850 END DEFine show_info_and_warning
860 :
870 DEFine PROCedure get_and_show_version
880   get_smsqe_version ichan%,ochan%
890   smsqename$="smsqe"&smsq_vers$
900   PRINT#ochan%,"-> SMSQE name is : ";smsqename$
910   PRINT#ochan%,"-> SMSQE number is : ";svers$
920 END DEFine get_and_show_version
930 :
940 DEFine PROCedure sa
950   SAVE_O dev8_extras_newversion_bas
960 END DEFine sa
970 :
980 DEFine PROCedure make_new_v (make_binaries%,do_zip%,make_html%)
990 LOCal a$
1000   IF NOT IS_EXTN ("current_line")
1010      PRINT "CLINE_bin isn't loaded. Failed and stopped"
1020      STOP
1030   END IF                          : REMark if outptr isn't loaded prog breaks at "is_extn" above
1040   check_native_dirs ichan%,ochan%
1050   check_and_set_win4              : REMark check win4 can be used
1060   CLS#ichan%
1070   IF make_binaries%
1080     INPUT #ochan%,"Compile everything and recreate binaries first? (y/n) ";a$
1090     IF a$=="y"
1100       PRINT "Compiling and creating binaries...."
1110       EW dev8_extras_exe_SMSQEmake;"-q0 -as -mk -ta -sa"
1120      END IF
1130     PRINT #ochan%,"Making Q68 win container..."
1140     make_q68_win ""
1150   END IF
1160   IF do_zip%
1170     IF make_binaries%
1180       zip_binaries  1
1190       PRINT#ochan%,"Zipping all binaries to ";ddr$
1200       wdel_f ddr$,""
1210     END IF
1220     PRINT#ochan%;"Deleting all non source including binaries..."
1230     PRINT#ochan%;"Please be patient..."
1240     EW dev8_extras_del_all_bas;"auto stop"
1250     zip_sources 1
1260     DELETE "nfa8_SMSQE"&svers$&".WIN"
1270     DELETE "NFA8_Q68_SMSQ.WIN"
1280     zip_Win_file
1290     make_container      : REMark Now making the container file with the sources (zip under linux)
1300   END IF
1310   IF make_html%
1320     PRINT#ochan%,"Making html files..."
1330     today$=make_date$(0,0)
1340     make_oldversion_html
1350     make_versions ochan%
1360     make_repl_html "indexphp.template","index.php"
1370     make_repl_html "binaries.template","binaries.html"
1380   END IF
1390   PRINT#ochan%
1400   PRINT#ochan%," ----------------- DONE -----------------"
1410 END DEFine make_new_v
1420 :
1430 DEFine PROCedure get_smsqe_version (ichan%,ochan%)
1440 REMark this gets the version of SMSQE
1450 LOCal chan%,lp%,a$,t%
1460   CLS#ichan%
1470   INPUT#ochan%, "SMSQE version in format x.yy? (or Enter to get it from dev8_smsq_smsq_version_asm) ",smsq_vers$
1480   IF smsq_vers$=""
1490     chan%=FOP_IN('dev8_smsq_smsq_version_asm')
1500     REPeat lp%
1510       IF EOF(#chan%):EXIT lp%
1520       INPUT#chan%,a$
1530       IF "smsq_vers" INSTR a$  AND "equ" INSTR a$
1540         t%="'" INSTR a$
1550         IF t%:smsq_vers$=a$(t%+1 TO t%+4)
1560       END IF
1570     END REPeat lp%
1580     CLOSE#chan%
1590   END IF
1600   IF smsq_vers$=""
1610     PRINT#ochan%, "Failed to get smsq version!"
1620   ELSE
1630     PRINT#ochan%, "-> smsqe version : ",smsq_vers$
1640   END IF
1650   t%='.' INSTR smsq_vers$
1660   IF t%
1670     svers$=smsq_vers$(1)&smsq_vers$(3 TO 4)
1680   ELSE
1690     PRINT "No decimal point in version : wrong! Failed and stopped"
1700     STOP
1710   END IF
1720 END DEFine get_smsqe_version
1730 :
1740 DEFine PROCedure check_and_set_win4
1750 REMark check that the native file corresponding to win4 doesn't exist (!!!)
1760 LOCal a$,t%,natdrv4win4
1770   PRINT#ochan%,"Checking and setting win4_"
1780   natdrv4win4$="nfa8_SMSQE"&svers$&'.WIN'
1790   DELETE natdrv4win4$
1800   t%=FTEST(natdrv4win4$)
1810   IF t%<>-7
1820     CLS:PRINT "Error with win4"
1830     IF t%=0
1840       PRINT "It already exists"
1850     ELSE
1860       REPORT#1,t%
1870     END IF
1880     PRINT "WIN4 check failed - program stopped"
1890     STOP
1900   END IF
1910   WIN_DRIVE 4,NFA_USE$(8)&"SMSQE"&svers$&".WIN"
1920   PRINT#ochan%, "Win4 is: ";WIN_USE$(4)
1930 END DEFine check_and_set_win4
1940 :
1950 DEFine PROCedure check_native_dirs (ichan%,ochan%)
1960 LOCal lp%,t%,drive$
1970   PRINT #ochan%,"Setting NFA7_ and NFA8_"
1980   NFA_USE 7,htmldrive$
1990   t%= FTEST ("nfa7_")
2000   SELect ON t%
2010     =-1:PRINT#ochan%;htmldrive$;" is not a valid directory"
2020         PRINT#ochan%,'There seems to be a simple file with that name already!'
2030         PRINT#ochan%,"Failed and stopped":STOP
2040     =-7:
2050         PRINT "Cannot find ";htmldrive$
2060         PRINT#ochan%,"Failed and stopped":STOP
2070     =0:
2080     = REMAINDER
2090         PRINT "Error for ";htmldrive$;" : ";t%;" - STOPPED":STOP
2100   END SELect
2110   NFA_USE 8,resultnfa$
2120   t%= FTEST ("nfa8_")
2130   SELect ON t%
2140     =0 :PRINT#ochan%,"Deleting all files on nfa8_":wdel_f "nfa8_",""
2150     =-1:PRINT#ochan%;"Couldn't make directory '"&resultnfa$
2160         PRINT#ochan%,'There seems to be a simple file with that name already!'
2170         PRINT#ochan%,"Failed and progam is stopped":STOP
2180     =-7:
2190         MAKE_DIR "nfa8_"
2200     = REMAINDER
2210         PRINT "Error for ";resultnfa$;" : ";t%;" - STOPPED":STOP
2220   END SELect
2230   t%= FTEST ("nfa8_html_")
2240   SELect ON t%
2250     =0 :PRINT#ochan%,"Deleting all files on nfa8_html_":wdel_f "nfa8_html_",""
2260     =-1:PRINT#ochan%;"Couldn't make directory nfa8_html_"
2270         PRINT#ochan%,'There seems to be a simple file with that name already!'
2280         PRINT#ochan%,"Failed and progam is stopped":STOP
2290     =-7:
2300         MAKE_DIR "nfa8_html_"
2310     = REMAINDER
2320         PRINT "Error for ";resultnfa$;" : ";t%;" - STOPPED":STOP
2330   END SELect
2340   PRINT#ochan%,"NFA7_ is: ";NFA_USE$(7)
2350   PRINT#ochan%,"NFA8_ is: ";NFA_USE$(8)
2360 END DEFine check_native_dirs
2370 :
2380 DEFine PROCedure make_q68_win (extn$)
2390 LOCal mblen,c%,fleng,mdir$,olddir$
2400 :
2410   olddir$=WIN_DRIVE$(windrv%)
2420   WIN_DRIVE windrv%,""
2430   COPY_O "nfa7_1MB.win","nfa8_Q68_SMSQ.WIN" : REMark make copy of empty template file, set it as winX
2440   mdir$=NFA_USE$(8)
2450   WIN_DRIVE windrv%,mdir$&"Q68_SMSQ.WIN"    : REMark use as win drive
2460 :                                       : REMark copy Q68 smqe file to it
2470   PAUSE 50
2480   IF extn$<>"":COPY_O "dev1_progs_MenuConf_INF_"&extn$,"dev1_progs_MenuConf_INF"
2490   EW menuconfig;"\q\uDEV8_smsq_q68_SMSQ_4_WIN" : REMark set config
2500   COPY_O "DEV8_smsq_q68_SMSQ_4_WIN","win"&windrv%&"_Q68_SMSQ"
2510   MAKE_DIR "win"&windrv%&"_config"
2520   COPY DEV1_booty_Menu_rext_8e04,"win"&windrv%&"_config_menu_rext_english"
2530   COPY DEV1_progs_MenuConfig,"win"&windrv%&"_config_MenuConfig"
2540   COPY DEV1_q68_boot,"win"&windrv%&"_boot"
2550 :
2560   WIN_DRIVE windrv%,olddir$                   : REMark win drive X no longer exists
2570   IF extn$<>""
2580     COPY_O "dev1_progs_MenuConf_INF_mine","dev1_progs_MenuConf_INF"
2590   END IF
2600 :
2610   REMark PRINT "Created Q68 WIN file"
2620 :
2630 END DEFine make_q68_win
2640 :
2650 DEFine PROCedure make_versions (ochan%)
2660   PRINT#ochan%,"Making changes html text"
2670   EW dev8_extras_html_changes_bas;today$&" quit"
2680   PRINT#ochan%,"Making versions html text"
2690   EW dev8_extras_html_versions_bas;today$&" quit"
2700 END DEFine make_versions
2710 :
2720 DEFine PROCedure make_repl_html (infile$,outfile$)
2730 LOCal lp%,c%,o%,a$
2740   PRINT#ochan%,"Making "&outfile$&" file"
2750   c%=FOP_IN ("nfa7_"&infile$)
2760   IF c%<0:PRINT#ochan%, "failed to open input : ";c%;". STOPPED":STOP
2770   o%=FOP_OVER ("nfa8_html_"&outfile$)
2780   IF o%<0:PRINT#ochan%, "failed to open output - STOPPED":STOP
2790   REPeat lp%
2800     IF EOF(#c%):EXIT lp%
2810     INPUT#c%,a$
2820     a$=repl$(a$)
2830     a$=repl$(a$)                : REMark yes, run this twice
2840     PRINT#o%,a$
2850   END REPeat lp%
2860   CLOSE#c%:CLOSE#o%
2870 END DEFine make_repl_html
2880 :
2890 DEFine FuNction repl$(a$)
2900 REMark replace values in strings
2910 REMark xxxversionxxx is,eg 3.14
2920 REMark xxxsmsqexxx is, eg 314/smsqe314
2930 REMark xxxsubdirxxx is, eg 314/
2940 REMark xxxdatexxx is, eg. 28.02.2017
2950 REMark xxxsversxxx is, eg 314
2960 REMark
2970 REMark  smsq_vers$=eg 3.14
2980 REMark  svers$= eg 314
2990 LOCal t%,front$,end$
3000   front$=a$
3010   t%="xxxversionxxx" INSTR front$
3020   IF t%
3030     front$=front$(1 TO t%-1)&smsq_vers$&front$(t%+13 TO)
3040   END IF
3050   t%="xxxsmsqexxx" INSTR front$
3060   IF t%
3070     front$=front$(1 TO t%-1)&svers$&"/smsqe"&svers$&front$(t%+11 TO)
3080   END IF
3090   t%="xxxsubdirxxx" INSTR front$
3100   IF t%
3110     front$=front$(1 TO t%-1)&svers$&"/"&front$(t%+12 TO)
3120   END IF
3130   t%="xxxdatexxx" INSTR front$
3140   IF t%
3150     front$=front$(1 TO t%-1)&today$&front$(t%+10 TO)
3160   END IF
3170   t%="xxxsversxxx" INSTR front$
3180   IF t%
3190     front$=front$(1 TO t%-1)&svers$&front$(t%+11 TO)
3200   END IF
3210   RETurn front$
3220 END DEFine repl$
3230 :
3240 DEFine PROCedure make_container
3250 REMark this formats the win container
3260 LOCal what_chan%,counter
3270   PRINT#ochan%,"Now making the container file--------"
3280   PRINT#ochan%,"Formatting "&windrive$& " !"
3290   FORMAT windrive$&"25_SMSQE"&smsq_vers$
3300   PRINT#ochan%
3310   counter=0
3320   what_chan%=FOP_OVER('ram1_mydirs')
3330   get_dir_structure "dev8_",what_chan%
3340   CLOSE#what_chan%
3350   PRINT#ochan%,"Got dir structure, sorting..."
3360   sort_it
3370   PRINT#ochan%,"Making dir structure"
3380   make_dir_structure "ram1_mydirs2",windrive$
3390   PRINT#ochan%,"copying files, be patient..."
3400   fbackup "dev8_",windrive$
3410 END DEFine make_container
3420 :
3430 DEFine PROCedure get_dir_structure (dirr$,what_chan%)
3440 LOCal a$,lp,chan%,device$,file$
3450 LOCal k
3460   file$="ram1_ddir"&counter&"pkk"
3470   chan%=FOP_OVER(file$)
3480   IF chan%<0:RETurn
3490   DIR#chan%,dirr$
3500   CLOSE#chan%
3510   device$=dirr$(1 TO 4)&'_'
3520   chan%=FOP_IN(file$)
3530   IF chan%<0:RETurn
3540   INPUT#chan%,a$
3550   INPUT#chan%,a$
3560   REPeat lp
3570     IF EOF(#chan%):EXIT lp
3580     INPUT#chan%,a$
3590     k=LEN(a$)
3600     IF k<4:NEXT lp:EXIT lp
3610     IF a$(k-2 TO k)=' ->'
3620       counter=counter+1
3630       PRINT#what_chan%,a$(1 TO k-3)
3640       a$=device$&a$(1 TO k-3)&"_"
3650       PRINT#ochan%, "OK: ";a$
3660       get_dir_structure a$,what_chan%
3670     END IF
3680   END REPeat lp
3690   CLOSE#chan%
3700   DELETE file$
3710 END DEFine get_dir_structure
3720 :
3730 DEFine PROCedure sort_it
3740 LOCal a$,array$(counter+10,30),chan%,c,lp
3750   chan%=FOP_IN('ram1_mydirs')
3760   c=1
3770   REPeat lp
3780     IF EOF(#chan%):EXIT lp
3790     INPUT#chan%,a$
3800     array$(c)=a$
3810     c=c+1
3820   END REPeat lp
3830   ASORT array$,0
3840   CLOSE#chan%
3850   chan%=FOP_OVER('ram1_mydirs2')
3860   FOR lp=1 TO c
3870     a$=array$(lp)
3880     PRINT#chan%,a$
3890   END FOR lp
3900   CLOSE#chan%
3910 END DEFine sort_it
3920 :
3930 DEFine PROCedure make_dir_structure (file$,drive$)
3940 LOCal lp%,a$,chan%
3950   chan%=FOP_IN(file$)
3960   PRINT#ichan%, file$,chan%
3970   IF chan%<0: PRINT "File "&file$&" doesn't exist":RETurn
3980   REPeat lp%
3990     IF EOF(#chan%):EXIT lp%
4000     INPUT#chan%,a$
4010     IF a$="":NEXT lp%:EXIT lp%
4020     PRINT#ichan%, "Making "&drive$&a$&" ..."
4030     MAKE_DIR drive$&a$
4040   END REPeat lp%
4050   CLOSE#chan%
4060 END DEFine make_dir_structure
4070 :
4080 DEFine PROCedure fbackup (source$,dest$)
4090 REMark makes a forced backup, eventually deleting newer files on dest
4100 REMark and replacing them with the files on source$
4110 REMark fbackup "dir_with_new_files", "dir_with_old_files"
4120 LOCal chan%,lp%,myfile$,a$,t%,tc%,len_old,len_new,date_old,date_new,b$,mdir$,mdest$
4130   FOR lp%=1 TO 50
4140     myfile$="ram8_diffs_txt"&RND(1 TO 80)&RND (1 TO 80) & RND (1 TO 50)
4150     chan%=FOP_NEW (myfile$)            : REMark try to open unique file
4160     IF chan%>0:EXIT lp%
4170   END FOR lp%
4180   IF source$(LEN(source$))<>"_":source$=source$&"_"
4190   IF dest$(LEN(dest$))<>"_":dest$=dest$&"_"
4200   PRINT#ichan%, 'source= ';source$
4210   PRINT#ichan%, "dest= ";dest$
4220   mdir$=source$(1 TO 5)
4230   mdest$=dest$(1 TO 5)
4240   IF chan%<0:RETurn                     : REMark ooops!!!!
4250   WDIR#chan%,source$                    : REMark dir of this rep in file
4260   GET#chan%\0                           : REMark reset file pointer to start
4270   REPeat lp%
4280     IF EOF(#chan%):EXIT lp%
4290     INPUT#chan%,a$                      : REMark get filename
4300     IF a$="":NEXT lp%
4310     t%= ' ->' INSTR a$                  : REMark is it a subdir?
4320     IF t%
4330       tc%=LEN(source$)-4
4340       c$=a$(tc% TO)
4350       c$=c$(TO LEN(c$)-3)&"_"
4360       fbackup mdir$&a$(TO LEN(a$)-3)&'_',dest$&c$
4370       NEXT lp%
4380     END IF
4390     tc%=FOP_IN(mdir$&a$)                : REMark open source file
4400     date_old=FUPDT(#tc%)                : REMark file date of "old" source file
4410     CLOSE#tc%
4420     tc%=LEN(source$)-4                  : REMark length of name w/o subdirectory
4430     b$=a$(tc% TO)
4440     DELETE dest$&b$
4450     COPY mdir$&a$,dest$&b$
4460     tc%=FOPEN(dest$&b$)
4470     SET_FUPDT#tc%,date_old
4480     CLOSE#tc%
4490   END REPeat lp%
4500   CLOSE#chan%
4510   DELETE myfile$
4520 END DEFine fbackup
4530 :
4540 DEFine FuNction make_all_months$
4550 REMark this makes a string "JanFeb..." in the current language
4560 REMark this should be called during the initialisation part
4570 REMark eg. all_months$=make_all_months$ - dates_init below is a handy proc for that
4580 LOCal string$,lp%,a$,temp
4590   string$="":a$="":temp=0
4600   temp=60*60*24*31
4610   FOR lp%=0 TO 11
4620     a$=DATE$(lp%*temp)
4630     string$=string$&a$(6 TO 8)
4640   END FOR lp%
4650   RETurn string$
4660 END DEFine make_all_months$
4670 :
4680 DEFine FuNction make_date$(dflag%,what_date)
4690 REMark returns date as "01.01.1991" (dflag%=1) or "1991.01.31" (dflag%=0)
4700 REMark if what_date<>0, then it is this date that will be returned
4710 REMark this presumes that a variable "all_month$" exists!
4720   LOCal a$,b$,res
4730   b$=""
4740   IF what_date
4750     a$=DATE$(what_date)         : REMark make date passed as param into string
4760   ELSE
4770     a$=DATE$                    : REMark current date into string
4780   END IF
4790   b$=a$(6 TO 8)                 : REMark 3 letter month abbreviation
4800   res= b$ INSTR all_months$             : REMark find it
4810   IF NOT res
4820        all_months$=make_all_months$     : REMark not found?, make all_month$
4830        res= b$ INSTR all_months$        : REMark and retry
4840   END IF
4850   res=(res+2)/3                 : REMark this is the month in figures
4860   b$=res:IF res<10:b$="0"&b$            : REMark add leading 0 if necessary
4870   IF dflag%:RETurn a$(10 TO 11)&"."&b$&"."&a$(1 TO 4)
4880   RETurn a$(1 TO 4)&"."&b$&"."&a$(10 TO 11)
4890 END DEFine make_date$
4900 :
4910 REMark ------------- zipping --------------
4920 :
4930 DEFine PROCedure zip_sources (docopy%)
4940   del_zips
4950   zip_all_sources_into_one_file
4960   zip_individual_source_dirs
4970   IF docopy%:  fbackup "ram3_","nfa8_"
4980   del_zips
4990 END DEFine zip_sources
5000 :
5010 DEFine PROCedure zip_all_sources_into_one_file
5020 REMark This zips the entire SMSQE sources into one zip file.
5030 REMark Ensure that all non source files have been erased first!
5040 LOCal zip$
5050   PRINT#ochan%, "Zipping all sources into one zip file... "
5060   do_zip " -Q4r9 "&ddr$&"smsqe"&svers$&".zip"&" dd_* dv3_* ee_* extras_* iod_* keys_* lang_* mac_* minerva_* sbsext_* smsq_* sys_* uti_* util_* nfa1_qxl_* changes_txt readme_txt styleguide_txt whats_new_txt licence_doc licence_txt "
5070 END DEFine zip_all_sources_into_one_file
5080 :
5090 DEFine PROCedure zip_Win_file
5100   PRINT#ochan%, "Zipping the win file..."
5110   do_zip " -Q4r9 nfa8_smsqe"&svers$&"src.win nfa8_SMSQE"&svers$&".WIN"
5120 END DEFine zip_Win_file
5130 :
5140 DEFine PROCedure zip_individual_source_dirs
5150 LOCal lp%,dirname$
5160   RESTORE CURRENT_LINE : REMark make sure no other DATA statement is between this proc anf the "del_zips" proc
5170   PRINT#ochan%,"Zipping individual source dirs..."
5180   REPeat lp%
5190     READ dirname$
5200     IF dirname$="":EXIT lp%
5210     PRINT#ichan%, "Zipping "&dirname$
5220     do_zip " -Q4r9 "&ddr$&dirname$&".zip "&dirname$&"_*"
5230   END REPeat lp%
5240   do_qxl_pc
5250   DATA "dd","dv3","ee","extras","iod","keys","lang","mac","minerva","sbsext","smsq","sys","uti","util"
5260   DATA ""
5270 END DEFine zip_individual_source_dirs
5280 :
5290 DEFine PROCedure do_qxl_pc
5300 LOCal a$
5310   a$="nfa1_qxl_*"
5320   PRINT#ichan%, "Zipping qxl..."
5330   do_zip " -Q4j9 "&ddr$&"qxl.zip "& a$
5340 END DEFine do_qxl_pc
5350 :
5360 DEFine PROCedure del_zips
5370 REMark delete all zips on ddr$
5380 LOCal extns(0)
5390   DIM extns$(1,3)
5400   extns$(1)="zip"
5410   wdel_f ddr$,extns$
5420 END DEFine del_zips
5430 :
5440 DEFine PROCedure s
5450 REMark this resets the data etc dirs to acceptable values, no longer used
5460   DEST_USE dev1_
5470   DATA_USE dev1_basic_
5480   PROG_USE dev1_progs_
5490 END DEFine s
5500 :
5510 DEFine PROCedure zip_binaries (do_delete%)
5520 REMark This zips all binary versions and then possibly deletes them.
5530 REMark They are zipped into one big file and into individual files
5540   LOCal nbr%,lp%,source$,dest$,zip$,f$,zp$
5550   f$ =ddr$&"smsqe"&svers$&"_binaries.zip"
5560   REMark first of all, all into one
5570   PRINT#ochan%, "Zipping binaries into one file..."
5580   zip$ =' -Q4j9 '&f$&' '
5590   RESTORE CURRENT_LINE
5600   REPeat lp%
5610     READ source$,dest$
5620     IF source$="":EXIT lp%
5630     PRINT#ichan%, "copying "& source$
5640     COPY_O source$,ddr$&dest$
5650     zip$=zip$&ddr$&dest$ & ' '
5660   END REPeat lp%
5670   do_zip zip$
5680 :
5690   REMark now zip up each individual file
5700   PRINT#ochan%, "Zipping each binary separately..."
5710   zip$ =' -Q4j9 '
5720   RESTORE CURRENT_LINE
5730   REPeat lp%
5740     READ source$,dest$
5750     IF source$="" OR "_txt" INSTR source$:EXIT lp%
5760     PRINT#ichan%, "copying "& source$
5770     COPY_O source$,ddr$&dest$
5780     zp$=zip$&ddr$&dest$&".zip" &" " & ddr$&dest$
5790     do_zip zp$
5800     IF do_delete% AND NOT ("_txt" INSTR source$): DELETE source$
5810   END REPeat lp%
5820   REMark now delete files that aren't zipped
5830   RESTORE CURRENT_LINE
5840   REPeat lp%
5850     READ dest$,dest$
5860     IF dest$="":EXIT lp%
5870     DELETE ddr$&dest$
5880   END REPeat lp%
5890   REMark now copy all remaining files to nfa8_
5900   fbackup ddr$,"nfa8_"
5910   PRINT#ochan%, "Binaries done..."
5920   DATA "dev8_smsq_atari_SMSQ.PRG",  "SMSQE.PRG"       : REMark PRG file for ATARI ST/TT, ready to be used
5930   DATA "dev8_smsq_gold_gold",       "GoldCard_bin"    : REMark gold card Ql colours
5940   DATA "dev8_smsq_gold_gold8",      "GoldCard_256colours_bin"
5950   DATA "dev8_smsq_q40_rom",         "Q40_rom"         : REMark q40/q60 use as rom / lrespr file
5960   DATA "dev8_smsq_qxl_smsqe.exe",   "SMSQEQXL.EXE"    : REMark EXE to be run under DOS on i386 compatible systems with a QXL card installed, ready to be used
5970   DATA "dev8_smsq_aurora_SMSQE",    "Aurora_bin"      : REMark aurora 8 bit
5980   DATA "dev8_smsq_java_java",       "SMSQE"           : REMark Code file for SMSQmulator, ready to be used
5990   DATA "nfa8_Q68_SMSQ.WIN",         "Q68_SMSQ.WIN"    : REMark smsqe for Q68, win drive
6000   DATA "dev8_smsq_qpc_smsqe.bin",   "SMSQE.bin"       : REMark Code file for QPC2, ready to be used
6010   DATA "dev8_ee_ptr_gen",           "ptr_gen"         : REMark Part of Extended Environment for the non SMSQ/E (e.g. QDOS, SMSQ), ready to be used
6020   DATA "dev8_ee_wman_wman",         "wman"            : REMark Part of Extended Environment for the non SMSQ/E (e.g. QDOS, SMSQ), ready to be used
6030   DATA "dev8_ee_hot_rext_english",  "hot_rext_english": REMark Part of Extended Environment for the non SMSQ/E (e.g. QDOS, SMSQ), ready to be used
6040   DATA "dev8_ee_hot_rext_french",   "hot_rext_french" : REMark Part of Extended Environment for the non SMSQ/E (e.g. QDOS, SMSQ), ready to be used
6050   DATA "dev8_ee_hot_rext_german",   "hot_rext_german" : REMark Part of Extended Environment for the non SMSQ/E (e.g. QDOS, SMSQ), ready to be used
6060   DATA "dev8_whats_new_txt",        "whats_new_txt"
6070   DATA "dev8_changes_txt",          "changes_txt"
6080   DATA "dev8_readme_txt",           "readme_txt"
6090   DATA "","" : REMark leave TWO empty strings here
6100 END DEFine zip_binaries
6110 :
6120 DEFine PROCedure do_zip (zip$)
6130 LOCal destt$,progg$,dataa$
6140   destt$=DESTD$
6150   progg$=PROGD$
6160   dataa$=DATAD$
6170   DEST_USE "dev8_"  :PROG_USE "dev8_" :DATA_USE "dev8_"
6180   EW dev1_progs_zip;zip$
6190   DEST_USE destt$ :PROG_USE progg$ :DATA_USE dataa$
6200 END DEFine do_zip
6210 :
6220 DEFine PROCedure do_copy
6230 LOCal lp%,a$ ,f$
6240   PAUSE 80
6250   COPY_O ddr$&smsqe$,"nfa2_new_"&smsqe$
6260   RESTORE 1030
6270   REPeat lp%
6280     READ a$
6290     IF a$="":EXIT lp%
6300     COPY_O ddr$&a$&".zip","nfa2_new_"&a$&".zip"
6310   END REPeat lp%
6320   COPY_O ddr$&"qxl.zip","nfa2_new_qxl.zip"
6330   f$ =ddr$&"smsqe"&version$&"_binaries.zip"
6340   COPY_O f$,"nfa2_new_"&"smsqe"&version$&"_binaries.zip"
6350 END DEFine do_copy
6360 :
6370 DEFine PROCedure wdel_f (source$,array_with_extns$)
6380 REMark Delete either all files on dir$, or only all files with any extension
6390 REMark contained in the array_with_extns$ string array. This does NOT recurse
6400 REMark into subdirs.
6410 LOCal lp%,iters%,chan%,myname$,a$,lp2%,dir$
6420   myname$="wdel_f" &RND(0 TO 100) &RND(0 TO 100) &RND(0 TO 100) &RND(0 TO 100) &RND(0 TO 100)
6430   chan%=FOP_OVER("ram1_"&myname$)
6440   IF chan%<0:RETurn             : REMark can't open channel, give up
6450   IF LEN(source$)>5
6460     dir$=source$(1 TO 5)
6470   ELSE
6480     dir$=source$
6490   END IF
6500   DIR#chan%,source$
6510   GET#chan%\0
6520   INPUT#chan%,a$
6530   INPUT#chan%,a$
6540   iters%=DIMN(array_with_extns$,1)
6550   IF iters%=0
6560                                     : REMark no extension - delete everything
6570     REPeat lp%
6580       IF EOF(#chan%):EXIT lp%
6590       INPUT#chan%,a$
6600       IF a$==myname$ OR a$="":NEXT lp%
6610       IF endswith% (" ->",a$): NEXT lp%: REMark don't go into subdirs
6620       DELETE dir$&a$
6630     END REPeat lp%
6640   ELSE
6650                                   : REMark only delete files with matching extension
6660     REPeat lp%
6670       IF EOF(#chan%):EXIT lp%
6680       INPUT#chan%,a$
6690       IF a$==myname$ OR a$="":NEXT lp%
6700       FOR lp2%=0 TO iters%
6710         IF endswith% (array_with_extns$(lp2%),a$)
6720           DELETE dir$&a$
6730           EXIT lp2%
6740         END IF
6750       END FOR lp2%
6760     END REPeat lp%
6770   END IF
6780   CLOSE#chan%
6790   DELETE "ram1_"&myname$
6800 END DEFine wdel_f
6810 :
6820 DEFine FuNction endswith%(extension$,name$)
6830 REMark checks whether name$ ends with extension$. Returns 1 if yes, 0 if not. If
6840 REMark either of the params is "", returns 0.
6850 LOCal elen%,nlen%
6860   IF extension$="" OR name$="":RETurn 0
6870   elen%=LEN(extension$)
6880   nlen%=LEN(name$)
6890   IF elen%>nlen%:RETurn 0
6900   IF name$(nlen%-elen%+1 TO)== extension$: RETurn 1
6910   RETurn 0
6920 END DEFine endswith%
6930 :
6940 DEFine PROCedure w
6950 LOCal extns(0)
6960   DIM extns$(1,3)
6970   extns$(1)="asm"
6980   wdel_f "ram3_",extns$
6990 END DEFine w
7000 :
7010 DEFine PROCedure get_current_drive_assignments
7020   current_nfa7$=NFA_USE$(7)
7030   current_nfa8$=NFA_USE$(8)
7040   current_win4$=WIN_DRIVE$(windrv%)
7050 END DEFine get_current_drive_assignment
7060 :
7070 DEFine PROCedure restore_drive_assignments
7080   NFA_USE 7,current_nfa7$
7090   NFA_USE 8,current_nfa8$
7100   WIN_DRIVE windrv%,current_win4$
7110 END DEFine restore_drive_assignments
7120 :
7130 DEFine PROCedure make_oldversion_html
7140 LOCal c%,o%,a$,lp%,versn,versn$
7150   PRINT#ochan%, "Making oldversions html"
7160   c%=FOP_IN("nfa7_oldversions.html")
7170   IF c%<0:PRINT#ochan%, "failed to open input : ";c%;". STOPPED":STOP
7180   o%=FOP_OVER ("nfa8_html_oldversions.html")
7190   IF o%<0:PRINT#ochan%, "failed to open output - STOPPED":STOP
7200   versn=svers$
7210   versn=versn-1
7220   versn$=versn
7230   versn$=versn$(1)&"."&versn$(2 TO)
7240   REPeat lp%
7250     IF EOF(#c%):EXIT lp%
7260     INPUT#c%,a$
7270     IF "</ul>" INSTR a$
7280        PRINT#o%,'     <li> <a href = "old/smsqe'&versn&'src.win.zip">SMSQ/E v. '&versn$&'</a></li>'
7290      END IF
7300      PRINT#o%,a$
7310    END REPeat lp%
7320    CLOSE#c%
7330    CLOSE#o%
7340 REMark   copy_o "nfa8_html_oldversions.html","nfa7_oldversions.html"
7350 END DEFine make_oldversion_html
7360 :
7370 DEFine PROCedure r
7380   restore_drive_assignments       : REMark reset drives to their original values
7390 END DEFine r
