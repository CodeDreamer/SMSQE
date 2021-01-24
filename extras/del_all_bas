100 get_dev
110 PRINT "This program needs the 'ouptr_bin', 'is_extn' and 'current_line' extensions found on "&mdev$
120 PRINT "Type 'l' to load them. Type 'p' to proceed with the deletion"
130 REMark "current_line" is in "cline_bin"
140 REMark read the documentation in the help subdirectory
150 REMark also for the 'current_line' keyword
160 REMark lrespr cline_bin before using this prog.
170 REMark if the "restoring" message always refers to 0, then you haven't loaded cline_bin
180 :
190 auto%=0:stop%=0:keep%=0
200 a$=cmd$
210 IF a$<>""
220   IF "auto" INSTR a$:auto%=1
230   IF "stop" INSTR a$:stop%=1
240   IF "keep" INSTR a$:keep%=1: REMark keep binaries
250 END IF
260 IF auto%:p
270 IF stop%:QUIT
280 :
290 DEFine PROCedure p
300   IF NOT IS_EXTN ('CURRENT_LINE')
310      PRINT "cline_bin not loaded"
320   ELSE
330      get_dev
340      delete_all_targets
350   END IF
360 END DEFine p
370 :
380 DEFine PROCedure get_dev
390   mdev$="DEV8_"
400 END DEFine get_dev
410 :
420 DEFine PROCedure delete_all_targets
430 REMark this deletes all targets and all intermediary targets, thus forcing
440 REMark a complete rebuild. Caution, this also deletes all lib and rel files
450 LOCal exten$(1)
460   DIM exten$(4,3)
470   exten$(0) = "rel"
480   exten$(1) = "err"
490   exten$(2) = "map"
500   exten$(3) = "log"
510   exten$(4) = "lib"
520   PRINT "Deleting all files with the extensions rel, err, map, log, lib"
530   delete_files_with_extension exten$,mdev$,1
540   COPY mdev$&"minerva_inc_err_copy",mdev$&"minerva_inc_err"
550   COPY mdev$&"keys_err_copy",mdev$&"keys_err"
560   PRINT "Deleting all targets"
570   generic
580   atari
590   gold
600   q40
610   qxl
620   aurora
630   java
640   ptrgen
650   qpc
660   q68
670   qlsd
680 END DEFine delete_all_targets
690 :
700 DEFine PROCedure generic
710   read_datas CURRENT_LINE,"Generic"
720   delete_files
730   DATA "dd_qlnd_new" ,"dd_nd_new","dv3_test",'smsq_sbas_lang','smsq_sbas_control'
740   DATA "smsq_sbas_procs_x","smsq_smsq_cache","smsq_smsq_cache40c","smsq_smsq_fh_os"
750   DATA "smsq_smsq_hotkey","smsq_smsq_lang","smsq_smsq_loader","smsq_sbas_lang"
760   DATA "smsq_smsq_os","smsq_smsq_1mb_os","smsq_smsq_vers","smsq_smsq_wman"
770   DATA "util_thg_thing",'smsq_home_home','smsq_recent_recent',"smsq_sbas_lang"
780   DATA "",""
790 END DEFine generic
800 :
810 DEFine PROCedure atari
820   read_datas CURRENT_LINE,"Atari"
830   delete_files
840   DATA "smsq_atari_driver_dv3","smsq_atari_driver_ser","smsq_atari_driver_ql"
850   DATA "smsq_atari_driver_mono","smsq_atari_hwinit","smsq_atari_nasty"
860   DATA "smsq_atari_kbd_lang"
870   DATA "sys_boot_st_host","smsq_atari_sysspr"
880   DATA "","smsq_atari_SMSQ.PRG",""
890 END DEFine atari_targets
900 :
910 DEFine PROCedure gold
920   read_datas CURRENT_LINE,"(Super)Goldcard"
930   delete_files
940   DATA "smsq_gold_driver_dv3","smsq_gold_driver_most","smsq_gold_driver_nd","smsq_gold_driver_8"
950   DATA "smsq_gold_driver_nds","smsq_gold_driver_ql","smsq_gold_host"
960   DATA "smsq_gold_hwinit","smsq_gold_kbd_abc","smsq_gold_kbd_abc_lang"
970   DATA "smsq_gold_kbd_lang","smsq_gold_nasty","smsq_gold_nasty_s"
980   DATA "smsq_gold_qimi","smsq_gold_roms","sys_gold_boot"
990   DATA "smsq_gold_sysspr"
1000   DATA     ""
1010   DATA     "smsq_gold_gold8","smsq_gold_gold"
1020   DATA     ""
1030 END DEFine gold
1040 :
1050 DEFine PROCedure java
1060   read_datas CURRENT_LINE,"SMSQmulator"
1070   delete_files
1080   DATA "smsq_java_driver_most","smsq_java_driver_ql","smsq_java_host","smsq_java_smsq_os"
1090   DATA "smsq_java_driver_16"
1100   DATA "smsq_java_driver_8","smsq_java_smsq_1mb_os"
1110   DATA "smsq_java_driver_nfa_nfa","smsq_java_smsq_os"
1120   DATA "smsq_java_hwinit","smsq_java_host","smsq_java_sysspr"
1130   DATA "smsq_java_driver_dv3e","smsq_java_ip_x"
1140   DATA ""
1150   DATA "SMSQ_java_java"
1160   DATA ""
1170 END DEFine java
1180 :
1190 DEFine PROCedure qpc
1200   read_datas CURRENT_LINE,"QPC"
1210   delete_files
1220   DATA 'smsq_qpc_host'
1230   DATA 'smsq_smsq_qpc_os'
1240   DATA 'smsq_qpc_hwinit'
1250   DATA 'smsq_qpc_nasty_e'
1260   DATA 'smsq_qpc_kbd_lang'
1270   DATA 'smsq_qpc_driver_most'
1280   DATA 'smsq_qpc_driver_ql'
1290   DATA 'smsq_qpc_driver_16'
1300   DATA 'smsq_qpc_driver_8'
1310   DATA 'smsq_qpc_sysspr'
1320   DATA 'smsq_qpc_driver_dv3e'
1330   DATA 'smsq_qpc_procs_x'
1340   DATA 'smsq_qpc_cdaudio_x'
1350   DATA 'smsq_qpc_dos_x'
1360   DATA 'smsq_qpc_ip_x'
1370   DATA 'smsq_qpc_driver_qsound'
1380   DATA ""
1390   DATA "smsq_qpc_smsqe.bin"
1400   DATA ""
1410 END DEFine qpc
1420 :
1430 DEFine PROCedure aurora
1440   read_datas CURRENT_LINE,"Aurora"
1450   delete_files
1460   DATA "smsq_gold_driver_dv3","smsq_gold_driver_most","smsq_gold_driver_nd"
1470   DATA "smsq_gold_driver_nds","smsq_gold_driver_ql","smsq_gold_host"
1480   DATA "smsq_gold_hwinit","smsq_gold_kbd_abc","smsq_gold_kbd_abc_lang"
1490   DATA "smsq_gold_kbd_lang","smsq_gold_nasty","smsq_gold_nasty_s"
1500   DATA "smsq_gold_qimi","smsq_gold_roms","sys_gold_boot"
1510   DATA "smsq_gold_sysspr","smsq_aurora_driver_8","smsq_aurora_sysspr"
1520   DATA ""
1530   DATA "smsq_aurora_smsqe","smsq_gold_gold","smsq_gold_gold8"
1540   DATA ""
1550 END DEFine aurora
1560 :
1570 DEFine PROCedure q40
1580   read_datas CURRENT_LINE,"Qx0"
1590   delete_files
1600   DATA "smsq_q40_driver_dv3","smsq_q40_driver_16","smsq_q40_cache"
1610   DATA "smsq_q40_driver_ser","smsq_q40_driver_ser","smsq_q40_hwinit"
1620   DATA "smsq_q40_kbd_lang","smsq_q40_nasty","smsq_q40_os"
1630   DATA "smsq_q40_driver_ql","smsq_sbas_procs_x_q60"
1640   DATA "smsq_q40_sysspr","smsq_q40_cachemode","smsq_smsq_q40_os"
1650   DATA ""
1660   DATA "smsq_q40_rom"
1670   DATA "smsq_q40_rom_lo","smsq_q40_rom_hi"
1680   DATA "smsq_q40_boot_Q40_gz","smsq_q40_boot_Q40_ROM","smsq_q40_boot_Q40_ROM_HI"
1690   DATA "smsq_q40_boot_Q40_ROM_LO"
1700   DATA ""
1710 END DEFine q40
1720 :
1730 DEFine PROCedure qxl
1740   read_datas CURRENT_LINE,"QXL"
1750   delete_files
1760   DATA "smsq_qxl_driver_16","smsq_qxl_driver_nd","smsq_qxl_driver_most"
1770   DATA "smsq_qxl_ecache","smsq_qxl_driver_ql","smsq_qxl_host","smsq_qxl_hwinit"
1780   DATA "smsq_qxl_kbd_lang","smsq_qxl_nasty_e","smsq_qxl_procs_x"
1790   DATA "smsq_qxl_test","smsq_qxl_driver_dv3e","smsq_qxl_sysspr"
1800   DATA ""
1810   DATA "smsq_qxl_smsqe.exe"
1820   DATA ""
1830 END DEFine qxl
1840 :
1850 DEFine PROCedure q68
1860   read_datas CURRENT_LINE,"Q68"
1870   delete_files
1880   DATA "smsq_q68_driver_16","smsq_q68_driver_nd","smsq_q68_driver_most"
1890   DATA "smsq_q68_driver_ql","smsq_q68_hwinit" ,"smsq_smsq_q68_os"
1900   DATA "smsq_q68_kbd_lang","smsq_q68_nasty","smsq_q68_sbas_procs_x"
1910   DATA "smsq_q68_driver_dv3","smsq_q40_sysspr","smsq_q68_driver_8"
1920   DATA ""
1930   DATA "smsq_q68_RAM_SYS","smsq_q68_SMSQ_4_WIN"
1940   DATA ""
1950 END DEFine q68
1960 :
1970 DEFine PROCedure ptrgen
1980   read_datas CURRENT_LINE;"Qdos EE"
1990   delete_files
2000   DATA ""
2010   DATA "ee_wman_wman","ee_ptr_gen","ee_hot_rext_german","ee_hot_rext_french","ee_hot_rext_english",""
2020 END DEFine ptrgen
2030 :
2040 DEFine PROCedure qlsd
2050   read_datas CURRENT_LINE,"QL-SD"
2060   delete_files
2070   DATA ""
2080   DATA "dv3_qlsd_driver_bin","dv3_qlsd_driver_rom", "dv3_qlsd_driverWQ_bin"
2090   DATA ""
2100 END DEFine qlsd
2110 :
2120 DEFine PROCedure read_datas (line_number,target$)
2130 REMark this reads the datas as of the line number given as params
2140 REMark and puts them in the global arrays delete$ and keep$
2150 LOCal lp%,a$
2160   DIM delete$(100,60)
2170   RESTORE line_number                      : REMark start reading from here
2180   PRINT "   ";target$
2190   FOR lp% = 0 TO 100
2200     READ a$                                : REMark get filename of modules
2210     IF a$ ="":EXIT lp%                     : REMark finished
2220     delete$(lp%)=mdev$&a$
2230   END FOR lp%
2240   DIM keep$(100,60)
2250   FOR lp% = 0 TO 100
2260     READ a$                                : REMark get filename of entire smsqe file(s)
2270     IF a$ ="":EXIT lp%                     : REMark finished
2280     keep$(lp%)=mdev$&a$
2290   END FOR lp%
2300 END DEFine read_datas
2310 :
2320 DEFine PROCedure delete_files
2330 REMark delete all files held in delete$
2340 LOCal stop_here%,lp%
2350   stop_here%=DIMN(delete$,1)
2360   FOR lp%=0 TO stop_here%
2370     IF delete$(lp%)="":EXIT lp%
2380     DELETE delete$(lp%)
2390   END FOR lp%
2400   IF NOT keep%
2410     stop_here%=DIMN(keep$,1)
2420     FOR lp%=0 TO stop_here%
2430       IF keep$(lp%)="":EXIT lp%
2440       DELETE keep$(lp%)
2450     END FOR lp%
2460   END IF
2470 END DEFine delete_files
2480 :
2490 DEFine PROCedure sa
2500   SAVE_O dev8_extras_del_all_bas
2510 END DEFine sa
2520 :
2530 DEFine PROCedure delete_files_with_extension(ext$,mdir$,ddown%)
2540 REMark this deletes all files in the dir mdir$ with the extension ext$
2550 REMark possibly drilling down to all subirs(ddown%<>0) to do the same there
2560 REMark by calling itself recursively
2570 REMark It would be more efficient to call it with an array of extensions
2580 LOCal chan%,lp%,myfile$,a$,device$,t%,exlen%,b$,count%,i%
2590   FOR lp%=1 TO 50
2600     myfile$="ram1_dele_extns"&RND(1 TO 80)&RND (1 TO 80) & RND (1 TO 50)
2610     chan%=FOP_OVER (myfile$)               : REMark try to open unique file
2620     IF chan%>0:EXIT lp%
2630   END FOR lp%
2640   IF chan%<0:RETurn                        : REMark ooops!!!!
2650   exlen%=3
2660   device$=mdir$(1 TO 5)
2670   WDIR#chan%,mdir$                         : REMark dir of this rep in file
2680   GET#chan%\0                              : REMark reset file pointer to start
2690   count%=DIMN(ext$,1)
2700   REPeat lp%
2710     IF EOF(#chan%):EXIT lp%
2720     INPUT#chan%,a$                         : REMark get filename
2730     IF a$="":NEXT lp%
2740     a$=device$&a$
2750     t%= ' ->' INSTR a$                     : REMark is it a subdir?
2760     IF t% AND ddown%
2770       delete_files_with_extension ext$,a$(1 TO t%-1),ddown% : REMark yes, possibly drill down
2780       NEXT lp%
2790     END IF
2800     t%=LEN(a$)
2810     IF t%<exlen%:NEXT lp%                  : REMark filename is shorter than extension
2820     b$= a$(t%-exlen%+1 TO)
2830     FOR i%=0 TO count%
2840       IF  b$==ext$(i%) : DELETE a$ :EXIT i%
2850     END FOR i%
2860   END REPeat lp%
2870   CLOSE#chan%
2880   DELETE myfile$
2890 END DEFine delete_files_with_extension
2900 :
2910 DEFine PROCedure l
2920   LRESPR "dev8_extras_source_outptr_bin"
2930   LRESPR "dev8_extras_blinker_bin"
2940   LRESPR "dev8_extras_cline_bin"
2950 END DEFine l
2960 :
