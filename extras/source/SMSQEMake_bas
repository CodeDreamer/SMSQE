10 REMark $$asmb=dev8_extras_source_outptr_bin,0,10
20 REMark $$asmb=dev1_booty_qptr_bin,50,172
30 REMark $$asmb=dev8_extras_source_SMSQEMake_bin,0,10
40 REMark $$stak=50000
50 rem compiled with the basic_linker
60 :
70 compiled=not is_open(#0)
80 if not compiled
90   check_for_PE
100   clear:clchp
110   scr_size#0,maxx%,maxy%
120   maxy%=maxy%-28
130   outln#0,maxx%,maxy%,0,28
140   wset -1
150 else
160   open#1,"con"
170   check_for_PE
180   scr_size#1,maxx%,maxy%
190 end if
200 :
210 init
220 main
230 stop
240 :
250 def proc check_for_PE
260   if p_env(#1)<>2
270     cls#1:ink#1,2:print#1,"This software works only in the pointer environment"
280     a$=inkey$(#1,-1)
290     stop
300   end if
310 end def check_for_PE
320 :
330 DEFine PROCedure init
340 LOCal temp%,targets%,start%,lp%,v$
350   text=0:sprite=2:blob=4:pattern=6
360   retr=256:retn=-256:sous1=254:sous2=252:sous3=250
370   rem normal: hit = selects/deselects, do = returns & leaves selected
380   rem retr = return even on hit, reset item to available after,
390   rem retn = return even on hit, hit selects/deselects, do selects
400 :
410   hit$=CHR$(1):do$=CHR$(2):cancel$=CHR$(3):sleep$=CHR$(7):wake$=CHR$(8)
420   help$=CHR$(4):move$=CHR$(5):size$=CHR$(6):cr$=CHR$(10)
430 :
440   make_standard_sprites
450   colourway%=SMSCFG(2)                  : rem use first syspals
460   if colourway%>3 or colourway%<0:colourway%=0
470   temp%=set_colours% (2)      : rem always try to set highest colours
480   make_my_palette colourway%  : rem set colours according to system palette
490 :
500 restore 500
510 :
520 : rem make loose item attributes
530 :
540   DIM std_iattr(3,3)
550   RD_IATT std_iattr
560     DATA 1,sp.litemhigh : rem current item border size & colour
570     DATA sp.litemunabg,sp.litemunafg,0,0 : rem unavailable item paper & ink
580     DATA sp.litemavabg,sp.litemavafg,0,0 : rem available     "    "       "
590     DATA sp.litemselbg,sp.litemselfg,0,0 : rem selected
600 :
610   targets%=0
620   rem IF NOT compiled:cmd$="-tj-tg-st-mk-as"
630   quit_always%=0
640   quit_no_errors%=0
650   autostart%=0
660   do_make_it%=0
670   force_assembly$=""
680   delete_first%=0
690   select_all%=0
700   autochangesize%=(SMSCFG(3)=="Yes")
710   IF cmd$<>''
720       IF '-qa' INSTR cmd$:quit_always%=1
730       IF '-q0' INSTR cmd$:quit_no_errors%=1
740       IF '-as' INSTR cmd$:autostart%=1
750       IF '-mk' INSTR cmd$:do_make_it%=1
760       IF '-de' INSTR cmd$:delete_first%=1
770       IF '-sa' INSTR cmd$:select_all%=1
780       IF '-tg' INSTR cmd$:targets%=targets%+1 : rem Generic
790       IF '-ti' INSTR cmd$:targets%=targets%+2 : rem Atari
800       IF '-to' INSTR cmd$:targets%=targets%+4 : rem gOld
810       IF '-tq' INSTR cmd$:targets%=targets%+8 : rem Q40
820       IF '-tx' INSTR cmd$:targets%=targets%+16 : rem qXl
830       IF '-tu' INSTR cmd$:targets%=targets%+32 : rem aUrora
840       IF '-tj' INSTR cmd$:targets%=targets%+64 : rem Java
850       IF '-tr' INSTR cmd$:targets%=targets%+128 : rem ptRgen
860       IF '-tc' INSTR cmd$:targets%=targets%+256 : rem qpC
870       IF '-t8' INSTR cmd$:targets%=targets%+512 : REMark q68
880       IF '-ta' INSTR cmd$:targets%=255+256+512    : rem all targets
890       IF '-fa' INSTR cmd$:force_assembly$=" -f": rem force assembly of all files
900   ELSE
910     targets%=1
920   END IF
930   make_my_data targets%
940   xsize%=500                                      : rem window x size divisible by 4!
950   ysize%=200                                      : rem provisional y size%
960   rows%=-1                                        : rem make rows dependent on wdw size
970   rep$=SMSCFG(1)                        : rem dir with files
980   make_main_wdw
990   IF (select_all%):main_lfl%(all_it%)=129:set_statusses
1000   start%=1
1010   FOR lp%=target_its% TO target_its%+9
1020     IF targets% && start%: main_lfl%(lp%)=129
1030     start%=start%*2
1040   END FOR lp%
1050   IF do_make_it%:main_lfl%(make_it%)=129
1060   IF delete_first%:main_lfl%(del_it%)=129
1070 :
1080   warn_make
1090   v$=smscfg(0)
1100   about_make "SMSQEMake",v$
1110 :
1120 END DEFine init
1130 :
1140 define procedure main
1150 local lp%,item%,event%,swnum%,xrel%,yrel%,item%,lp2%,tot%,errors%
1160   if compiled
1170      dr_ppos main_defn,-1,-1,main_lfl%,main_mfl%,main_cty%
1180   else
1190      dr_puld main_defn,-1,-1,main_lfl%,main_mfl%,main_cty%
1200   end if
1210   pos_ptr main_defn,40*65536+20,1
1220   if autostart%
1230      errors%=do_the_work%
1240      if quit_always%:stop
1250      if quit_no_errors% and not errors%:stop
1260      if errors%
1270           errors%=warning%("There were errors"&chr$(10)&"check the highlit items",0,"errors!")
1280      endif
1290   endif
1300   repeat lp%
1310     rd_ptr main_defn,item%,swnum%,event%,xrel%,yrel%,main_lfl%,main_mfl%,main_cty%
1320     if swnum%= -1
1330       select on item%
1340             =esc%     : exit lp%                            : remark quit prog
1350             =move_it% : ch_win main_defn                    : remark move
1360             =size_it% : change_size xrel%,yrel%   : remark change size
1370             =sleep_it%: button main_defn,main_lfl%,main_mfl%,main_cty%,"SMSQ/E Make",4
1380             =all_it%  : set_statusses             : remark set all appsub item statusses
1390                           main_cty%(0,1)=1                  : remark and redraw
1400             =go_it%   : errors%=do_the_work%
1410                           if errors%
1420                                errors%=warning%("There were errors"&chr$(10)&"check the highlit items",0,"errors!")
1430                           endif
1440                           main_lfl%(del_it%)=1
1450             =dir_it%  : make_new_dir item%
1460             =target_its% to make_it%-1,alltargets_it%
1470                           if item%=alltargets_it%
1480                                machines%=-1
1490                                for lp2%= target_its% to make_it%-1
1500                                   main_lfl%(lp2%)=129
1510                                end for lp2%
1520                            else
1530                               start%=1:machines%=0
1540                               for lp2%=target_its% to make_it%-1
1550                                   if main_lfl%(lp2%):machines%=machines%+start%
1560                                   start%=start%*2
1570                               end for lp2%
1580                           endif
1590                           make_my_data machines%  : remark make data arrays
1600                           if autochangesize%
1610                               autochange_size xrel%,yrel%
1620                           else
1630                               app_addr=mk_main_app(app_addr,mdata$,0,0,1,0,0,0) : rem make apsub wdw
1640                               main_app_list=app_addr+180
1650                               ap_in main_defn,main_app_list : remark put it in
1660                               dr_wdrw main_defn:main_cty%(0,1)=1 : remark and redraw entire wdw
1670                           end if
1680             =info_it% : about "2013-2017"
1690       end select
1700     end if
1710   end repeat lp%
1720   dr_unst main_defn
1730   if compiled:close#1
1740   rechp espace_vide
1750 end define main
1760 :
1770 DEFine FuNction do_the_work%
1780 REMark this tries to compile all of the selected files
1790 REMark if the file was compiled OK, status (of the link file) set to available
1800 REMark if the file was not compiled ok, status left selected
1810 LOCal lp%,bound%,a$,my_error%,tot_err%
1820   bound%=DIMN(main_mfl%,1)              : REMark number of items to check
1830   tot_err%=0
1840   IF main_lfl%(del_it%)<>0
1850      EW "dev8_extras_del_all_bas";"auto stop"
1860   END IF
1870   FOR lp%=0 TO bound%
1880     a$=mdata$(lp%+1)                              : REMark potential file to compile
1890     IF a$(1)<>"*" AND main_mfl%(lp%,0)
1900        a$=rep$&a$                       : REMark add dir
1910        my_error%=compile_and_check% (a$): REMark try to compile
1920        IF NOT my_error%
1930              main_mfl%(lp%,0)=1
1940              DR_ADRW main_defn,0,main_mfl%,main_cty%
1950        END IF
1960        tot_err%=tot_err%+my_error%
1970     END IF
1980   END FOR lp%
1990   main_cty%(0,1)=1
2000   main_lfl%(go_it%)=1
2010   IF main_lfl%(make_it%)=128 AND NOT tot_err%:make_targets
2020   RETurn tot_err%
2030 END DEFine do_the_work%
2040 :
2050 DEFine FuNction compile_and_check% (a$)
2060 REMark this tries to compile a file, using the "make" prog
2070 REMark this returns 1 if errors, else 0
2080 LOCal chan%,is_ok$,ch_len
2090   if machine=17
2100     ew dev8_extras_source_make_bas;a$&" -w -l"&force_assembly$
2110   else
2120     ch_len=EXEPF_W ("make";a$&" -w -l"&force_assembly$)         : REMark exec make & wait
2130     IF ch_len<>0
2140        EXEC_W "make";a$&" -w -l"&force_assembly$       : REMark exec make & wait
2150     END IF
2160   endif
2170   is_ok$=a$(1 TO LEN(a$)-4)             : REMark file name w/o extension
2180   chan%=FOP_IN(is_ok$&'log')            : REMark open log file
2190   IF chan%<0:RETurn 1                             : REMark can't even open it!
2200   ch_len=FLEN(#chan%)                             : REMark file length
2210   GET#chan%\ch_len-10                             : REMark this is where error report is
2220   INPUT#chan%,is_ok$                              : REMark get it
2230   CLOSE#chan%
2240   IF NOT is_ok$=="No errors"
2250     RETurn 1                                      : REMark anything other than "no errors" signals errors
2260   ELSE
2270     RETurn 0
2280   END IF
2290 END DEFine compile_and_check%
2300 :
2310 DEFine PROCedure make_new_dir (poste%)
2320 REMark get athe new dir where the files are located
2330 LOCal quel_rep$
2340   IF MENU_OK
2350     quel_rep$= DIR_SELECT$("The Files are where?",,10,10,main_coul%,app_coul%)
2360   ELSE
2370     quel_rep$=getstr$(poste%,0,main_defn,a_lire$)           : REMark use menu extns if possible
2380   END IF
2390   IF LEN (quel_rep$) >= 5
2400      rep$=quel_rep$(1 TO 5)
2410   END IF
2420   CH_ITEM main_defn,-1,poste%,-1,"",rep$
2430   main_lfl%(poste%)=main_lfl%(poste%)+1
2440 END DEFine make_new_dir
2450 :
2460 DEFine PROCedure make_my_data (what_machine%)
2470 REMark what_machine% = 1 -> generic
2480 REMark what_machine% = 2 -> atari
2490 REMark what_machine% = 4 -> Gold Card
2500 REMark what_machine% = 8 -> Qx0
2510 REMark what_machine% = 16 -> QXL
2520 REMark what_machine% = 32 -> aurora
2530 REMark what_machine% = 64 -> java
2540 REMark what_machine% = 128 -> ptrgen
2550 REMark what_machine% = 256 -> qpc
2560 REMark what_machine% = 512 -> q68
2570 REMark these may be combined
2580   DIM mdata$(250,50)
2590   REMark first make generic data
2600   data_count%=1                         : REMark general data count (global var)
2610   IF what_machine%&&1
2620     generic_data                        : REMark set up generic data
2630     read_my_datas                       : REMark and read it
2640   END IF
2650   IF what_machine%&&2
2660      atari_data                         : REMark set up machine specific data....
2670      read_my_datas                      : REMark and read it
2680   END IF
2690   IF what_machine%&&4
2700      gc_data                                      : REMark set up machine specific data....
2710      read_my_datas                      : REMark and read it
2720   END IF
2730   IF what_machine%&&8
2740      q40_data                                     : REMark set up machine specific data....
2750      read_my_datas                      : REMark and read it
2760   END IF
2770   IF what_machine%&&16
2780      qxl_data                                     : REMark set up machine specific data....
2790      read_my_datas                      : REMark and read it
2800   END IF
2810   IF what_machine%&&32
2820      aurora_data                        : REMark set up machine specific data....
2830      read_my_datas                      : REMark and read it
2840   END IF
2850   IF what_machine%&&64
2860      java_data                                    : REMark set up machine specific data....
2870      read_my_datas                      : REMark and read it
2880   END IF
2890   IF what_machine%&&128
2900      ptrgen_data                        : REMark set up machine specific data....
2910      read_my_datas                      : REMark and read it
2920   END IF
2930   IF what_machine%&&256
2940      qpc_data                                     : REMark set up machine specific data....
2950      read_my_datas                      : REMark and read it
2960   END IF
2970   IF what_machine%&&512
2980      q68_data                                     : REMark set up machine specific data....
2990      read_my_datas                      : REMark and read it
3000   END IF
3010 END DEFine make_my_data
3020 :
3030 DEFine PROCedure read_my_datas
3040 REMark reads datas into the global var mdata$
3050 REMark uses data_count% from make_my_data
3060 LOCal lp%
3070   REPeat lp%
3080     READ mdata$(data_count%)
3090     IF mdata$(data_count%)="":EXIT lp%
3100     data_count%=data_count%+1
3110   END REPeat lp%
3120 END DEFine read_my_datas
3130 :
3140 DEFine PROCedure generic_data
3150 restore 3150
3160   DATA '**** -- SMSQ Generic -- ****'
3170   DATA 'smsq_smsq_loader_link'
3180   DATA 'smsq_smsq_fh_link'
3190   DATA 'smsq_smsq_link'
3200   DATA 'smsq_smsq_cache_link'
3210   DATA 'smsq_sbas_link'
3220   DATA 'smsq_smsq_lang_link'
3230   DATA 'smsq_sbas_lang_link'
3240   DATA 'smsq_sbas_procs_link'
3250   DATA 'smsq_smsq_wman_link'
3260   DATA 'smsq_smsq_hotkey_link'
3270   DATA 'smsq_smsq_vers_link'
3280   DATA 'smsq_home_link'
3290   DATA 'smsq_recent_link'
3300   DATA 'smsq_smsq_1mb_link',''
3310 END DEFine generic_data
3320 :
3330 DEFine PROCedure q40_data
3340 restore 3340
3350   DATA '**** -- Q40/Q60 -- ****'
3360   DATA 'smsq_q40_hwinit_link'
3370   DATA 'smsq_q40_nasty_link'
3380   DATA 'smsq_q40_cache_link'
3390   DATA 'smsq_q40_cachemode_link'
3400   DATA 'smsq_smsq_cache40c_link'
3410   DATA 'smsq_q40_driver_ser_link'
3420   DATA 'smsq_q40_driver_dv3_link'
3430   DATA 'smsq_q40_driver_ql_link'
3440   DATA 'smsq_q40_driver_16_link'
3450   DATA 'smsq_q40_kbd_lang_link'
3460   DATA 'smsq_smsq_q40_link'
3470   DATA 'smsq_q40_sysspr_link',''
3480 END DEFine q40_data
3490 :
3500 DEFine PROCedure atari_data
3510 restore 3510
3520   DATA '**** -- Atari -- ****'
3530   DATA 'smsq_atari_hwinit_link'
3540   DATA 'smsq_atari_nasty_link'
3550   DATA 'smsq_atari_driver_ql_link'
3560   DATA 'smsq_atari_driver_mo_link'
3570   DATA 'smsq_atari_driver_ser_link'
3580   DATA 'smsq_atari_driver_dv3_link'
3590   DATA 'smsq_atari_kbd_lang_link'
3600   DATA 'smsq_atari_sysspr_link'
3610   DATA 'sys_boot_st_host_link',''
3620 END DEFine atari_data
3630 :
3640 DEFine PROCedure gc_data
3650 restore 3650
3660   DATA '**** -- (Super)GoldCard -- ****'
3670   DATA 'smsq_gold_host_link'
3680   DATA 'smsq_gold_hwinit_link'
3690   DATA 'smsq_gold_nasty_link'
3700   DATA 'smsq_gold_nasty_s_link'
3710   DATA 'smsq_gold_driver_ql_link'
3720   DATA 'smsq_gold_driver_8_link'
3730   DATA 'smsq_gold_driver_most_link'
3740   DATA 'smsq_gold_driver_nd_link'
3750   DATA 'smsq_gold_driver_nds_link'
3760   DATA 'smsq_gold_driver_dv3_link'
3770   DATA 'smsq_gold_kbd_lang_link'
3780   DATA 'smsq_gold_kbd_abc_lng_link'
3790   DATA 'smsq_gold_kbd_abc_link'
3800   DATA 'smsq_gold_roms_link'
3810   DATA 'smsq_gold_qimi_link'
3820   DATA 'smsq_gold_sysspr_link',''
3830 END DEFine gc_data
3840 :
3850 DEFine PROCedure aurora_data
3860 restore 3860
3870   DATA '**** -- Aurora -- ****'
3880   DATA 'smsq_gold_host_link'
3890   DATA 'smsq_gold_hwinit_link'
3900   DATA 'smsq_gold_nasty_link'
3910   DATA 'smsq_gold_nasty_s_link'
3920   DATA 'smsq_aurora_driver_8_link'
3930   DATA 'smsq_gold_driver_ql_link'
3940   DATA 'smsq_gold_driver_most_link'
3950   DATA 'smsq_gold_driver_nd_link'
3960   DATA 'smsq_gold_driver_nds_link'
3970   DATA 'smsq_gold_driver_dv3_link'
3980   DATA 'smsq_gold_kbd_lang_link'
3990   DATA 'smsq_gold_kbd_abc_lng_link'
4000   DATA 'smsq_gold_kbd_abc_link'
4010   DATA 'smsq_gold_roms_link'
4020   DATA 'smsq_gold_qimi_link'
4030   DATA 'smsq_aurora_sysspr_link'
4040   DATA 'smsq_gold_sysspr_link',''
4050 END DEFine aurora_data
4060 :
4070 DEFine PROCedure qxl_data
4080 restore 4080
4090   DATA '**** -- QXL -- ****'
4100   DATA 'smsq_qxl_host_link'
4110   DATA 'smsq_qxl_hwinit_link'
4120   DATA 'smsq_qxl_nasty_e_link'
4130   DATA 'smsq_qxl_driver_ql_link'
4140   DATA 'smsq_qxl_driver_16_link'
4150   DATA 'smsq_qxl_driver_most_link'
4160   DATA 'smsq_qxl_driver_nd_link'
4170   DATA 'smsq_qxl_procs_link'
4180   DATA 'smsq_qxl_driver_dv3e_link'
4190   DATA 'smsq_qxl_kbd_lang_link'
4200   DATA 'smsq_qxl_ecache_link'
4210   DATA 'smsq_qxl_sysspr_link',''
4220 END DEFine qxl_data
4230 :
4240 DEFine PROCedure java_data
4250 restore 4250
4260   DATA '**** -- Java -- ****'
4270   DATA 'smsq_java_host_link'
4280   DATA 'smsq_java_smsq_link'
4290   DATA 'smsq_java_smsq_1mb_link'
4300   DATA 'smsq_java_hwinit_link'
4310   DATA 'smsq_java_driver_ql_link'
4320   DATA 'smsq_java_driver_8_link'
4330   DATA 'smsq_java_driver_16_link'
4340   DATA 'smsq_java_driver_most_link'
4350   DATA 'smsq_java_driver_dv3e_link'
4360   DATA 'smsq_java_ip_link'
4370   DATA 'smsq_gold_kbd_lang_link'
4380   DATA 'smsq_java_sysspr_link',''
4390 END DEFine java_data
4400 :
4410 DEFine PROCedure qpc_data
4420 restore 4420
4430   DATA "**** -- QPC -- ****"
4440   DATA 'smsq_qpc_host_link'
4450   DATA 'smsq_qpc_hwinit_link'
4460   DATA 'smsq_qpc_nasty_e_link'
4470   DATA 'smsq_qpc_driver_ql_link'
4480   DATA 'smsq_qpc_driver_16_link'
4490   DATA 'smsq_qpc_driver_8_link'
4500   DATA 'smsq_qpc_driver_ql_link'
4510   DATA 'smsq_qpc_driver_most_link'
4520   DATA 'smsq_qpc_driver_qsound_link'
4530   DATA 'smsq_qpc_procs_link'
4540   DATA 'smsq_qpc_driver_dv3e_link'
4550   DATA 'smsq_qpc_kbd_lang_link'
4560   DATA 'smsq_qpc_dos_link'
4570   DATA 'smsq_qpc_ip_link'
4580   DATA 'smsq_qpc_cdaudio_link'
4590   DATA 'smsq_smsq_qpc_link'
4600   DATA 'smsq_qpc_sysspr_link',''
4610 END DEFine qpc_data
4620 :
4630 DEFine PROCedure ptrgen_data
4640 restore 4640
4650   DATA "**** -- PtrGen -- ****"
4660   DATA "ee_wman_link","ee_ptr_link","ee_hot_german_link","ee_hot_french_link"
4670   DATA "ee_hot_english_link",""
4680 END DEFine ptrgen_data
4690 :
4700 DEFine PROCedure q68_data
4710 restore 4710
4720   DATA '**** -- Q68 -- ****'
4730   DATA 'smsq_q68_sbas_procs_link'
4740   DATA 'smsq_q68_hwinit_link'
4750   DATA 'smsq_q68_nasty_link'
4760   DATA 'smsq_q68_driver_dv3_link'
4770   DATA 'smsq_q68_driver_most_link'
4780   DATA 'smsq_q68_driver_ql_link'
4790   DATA 'smsq_q68_driver_16_link'
4800   DATA 'smsq_q68_driver_8_link'
4810   DATA 'smsq_q68_kbd_lang_link'
4820   DATA 'smsq_smsq_q68_link'
4830   DATA 'smsq_q40_sysspr_link',''
4840 END DEFine q68_data
4850 :
4860 DEFine PROCedure make_targets
4870 REMark targets are only made if there was no error during compilation
4880 REMark to achieve this, a primitive approach is used:
4890 REMark a check is made whether an appsub menu item status is still selected
4900 REMark if it is, then the
4910   LOCal lp%,current_target%,tot_targets%
4920   tot_targets%=0                        : REMark no targets to make yet
4930   current_target%=0                               : REMark and no target yet
4940   FOR lp%=0 TO data_count%-2
4950     IF main_mfl%(lp%,0)=16
4960       REMark we're covering a new target
4970       tot_targets%=tot_targets%+current_target% : REMark show old target
4980       current_target%=find_target%(lp%+1) : REMark get new target we're covering
4990     ELSE
5000       IF main_mfl%(lp%,0)<>0:current_target%=0
5010     END IF
5020   END FOR lp%
5030   tot_targets%=tot_targets%+current_target%
5040   REMark at the end, here, tot_targets holds a bitmap
5050   REMark of targets for which compilation was ok
5060   IF (machines%&&1) AND NOT (tot_targets% && 1):RETurn : REMark generic was asked for but not compiled correctly!
5070   IF tot_targets%&&2:atari_target
5080   IF tot_targets%&&4:gc_target
5090   IF tot_targets%&&8:qx0_target
5100   IF tot_targets%&&16:qxl_target
5110   IF tot_targets%&&32:aurora_target
5120   IF tot_targets%&&64:java_target
5130   REMark IF tot_targets%&&128:ptr_gen_target  : rem no need for this
5140   IF tot_targets%&&256:qpc_target
5150   IF tot_targets%&&512:q68_target
5160 END DEFine make_targets
5170 :
5180 DEFine PROCedure atari_target
5190   cmdl$='m'
5200   p$=rep$&'sys_boot_st_flp'
5210   f$=rep$&'sys_boot_file'
5220   ah$=rep$&'sys_boot_st_host'
5230   ld$=rep$&'smsq_smsq_loader'
5240   hi$=rep$&'smsq_atari_hwinit'
5250   os1f$=rep$&'smsq_smsq_fh_os'
5260   os1$=rep$&'smsq_smsq_os'
5270   ca$=rep$&'smsq_smsq_cache'
5280   os2$=rep$&'smsq_sbas_control'
5290   ns1$=rep$&'smsq_atari_nasty'
5300   ln1$=rep$&'smsq_smsq_lang'
5310   ln2$=rep$&'smsq_atari_kbd_lang'
5320   ln3$=rep$&'smsq_sbas_lang'
5330   ex1$=rep$&'smsq_sbas_procs_x'
5340   ex2$=rep$&'smsq_atari_driver_dv3'
5350   ex3$=rep$&'smsq_atari_driver_ser'
5360   ex4$=rep$&'smsq_atari_driver_mono'
5370   ex5$=rep$&'smsq_atari_driver_ql'
5380   ex6$=rep$&'smsq_smsq_wman'
5390   ex7$=rep$&'smsq_smsq_hotkey'
5400   ex8$=rep$&'smsq_atari_sysspr'
5410   ex9$=rep$&'smsq_home_home'
5420   ex10$=rep$&'smsq_recent_recent'
5430   nl$=rep$&'sys_boot_null'
5440   IF 'm' INSTR cmdl$ = 0: ex4$ = nl$
5450   EW f$, ah$,ld$,hi$,os1f$,os1$,ca$,os2$,ns1$,ln1$,ln2$,ln3$,ex1$,ex2$,ex3$,ex4$,ex5$,ex6$,ex7$,ex8$,ex9$, ex10$,rep$&'smsq_atari_SMSQ.PRG'
5460 END DEFine atari_target
5470 :
5480 DEFine PROCedure gc_target
5490 REMark there are  2 targets for the Goldcard!
5500   f$=rep$&'sys_boot_file'
5510   v$=rep$&'smsq_smsq_vers'
5520   h$=rep$&'smsq_gold_host'
5530   ld$=rep$&'smsq_smsq_loader'
5540   hi$=rep$&'smsq_gold_hwinit'
5550   os1$=rep$&'smsq_smsq_os'
5560   ca$=rep$&'smsq_smsq_cache'
5570   os2$=rep$&'smsq_sbas_control'
5580   ns1$=rep$&'smsq_gold_nasty'
5590   ns2$=rep$&'smsq_gold_nasty_s'
5600   ln1$=rep$&'smsq_smsq_lang'
5610   ln2$=rep$&'smsq_gold_kbd_lang'
5620   ln2a$=rep$&'smsq_gold_kbd_abc_lang'
5630   ln3$=rep$&'smsq_sbas_lang'
5640   ex1$=rep$&'smsq_sbas_procs_x'   : REMark this must be after "roms"
5650   ex2a$=rep$&'smsq_gold_kbd_abc'
5660   ex2$=rep$&'smsq_gold_driver_most'
5670   ex3a$=rep$&'smsq_gold_driver_ql'
5680   ex38$=rep$&'smsq_gold_driver_8'
5690   ex3q$=rep$&'smsq_gold_qimi'
5700   ex3w$=rep$&'smsq_smsq_wman'
5710   ex4$=rep$&'smsq_gold_driver_dv3'
5720   ex5$=rep$&'smsq_gold_driver_nd'
5730   ex6$=rep$&'smsq_gold_driver_nds'
5740   ex7$=rep$&'smsq_smsq_hotkey'
5750   ex8$=rep$&'smsq_gold_sysspr'
5760   ex9$=rep$&'smsq_home_home'
5770   ex10$=rep$&'smsq_gold_roms'
5780   ex11$=rep$&'smsq_recent_recent'
5790   nl$=rep$&'sys_boot_null'
5800   EW f$, v$,h$,ld$,hi$,os1$,ca$,os2$,ns1$,ns2$,ln1$,ln2$,ln2a$,ln3$,ex2a$,ex2$,ex3a$,ex3q$,ex3w$,ex4$,ex5$,ex6$,ex7$,ex8$,ex9$,ex10$, ex1$,ex11$,rep$&'SMSQ_GOLD_gold'
5810   EW f$, v$,h$,ld$,hi$,os1$,ca$,os2$,ns1$,ns2$,ln1$,ln2$,ln2a$,ln3$,ex2a$,ex2$,ex38$,ex3q$,ex3w$,ex4$,ex5$,ex6$,ex7$,ex8$,ex9$,ex10$, ex1$,ex11$,rep$&'SMSQ_GOLD_gold8'
5820 END DEFine gc_target
5830 :
5840 DEFine PROCedure aurora_target
5850   f$=rep$&'sys_boot_file'
5860   v$=rep$&'smsq_smsq_vers'
5870   h$=rep$&'smsq_gold_host'
5880   ld$=rep$&'smsq_smsq_loader'
5890   hi$=rep$&'smsq_gold_hwinit'
5900   os1$=rep$&'smsq_smsq_os'
5910   ca$=rep$&'smsq_smsq_cache'
5920   os2$=rep$&'smsq_sbas_control'
5930   ns1$=rep$&'smsq_gold_nasty'
5940   ns2$=rep$&'smsq_gold_nasty_s'
5950   ln1$=rep$&'smsq_smsq_lang'
5960   ln2$=rep$&'smsq_gold_kbd_lang'
5970   ln2a$=rep$&'smsq_gold_kbd_abc_lang'
5980   ln3$=rep$&'smsq_sbas_lang'
5990   ex1$=rep$&'smsq_sbas_procs_x'   : REMark this must be after "roms"
6000   ex2a$=rep$&'smsq_gold_kbd_abc'
6010   ex2$=rep$&'smsq_gold_driver_most'
6020   ex3a$=rep$&'smsq_gold_driver_ql'
6030   ex3b$=rep$&'smsq_aurora_driver_8'
6040   ex3q$=rep$&'smsq_gold_qimi'
6050   ex3w$=rep$&'smsq_smsq_wman'
6060   ex3sq$=rep$&'smsq_aurora_sysspr'
6070   ex3sh$=rep$&'smsq_gold_sysspr'
6080   ex4$=rep$&'smsq_gold_driver_dv3'
6090   ex5$=rep$&'smsq_gold_driver_nd'
6100   ex6$=rep$&'smsq_gold_driver_nds'
6110   ex8$=rep$&"smsq_home_home"
6120   ex9$=rep$&'smsq_smsq_hotkey'
6130   ex10$=rep$&'smsq_gold_roms'
6140   ex11$=rep$&'smsq_recent_recent'
6150   nl$=rep$&'sys_boot_null'
6160   EW f$, v$,h$,ld$,hi$,os1$,ca$,os2$,ns1$,ns2$,ln1$,ln2$,ln2a$,ln3$,ex2a$,ex2$,ex3a$,ex3b$,ex3q$,ex3w$,ex3sh$,ex4$,ex5$,ex6$,ex9$,ex10$,ex8$,ex1$,ex11$, rep$&'SMSQ_Aurora_SMSQE'
6170 END DEFine aurora_target
6180 :
6190 DEFine PROCedure qx0_target
6200   nl$=rep$&'sys_boot_null'
6210   f$=rep$&'sys_boot_file'
6220   qr$=rep$&'sys_boot_q40_rom'
6230   ld$=rep$&'smsq_smsq_loader'
6240   hi$=rep$&'smsq_q40_hwinit'
6250   os1$=rep$&'smsq_smsq_q40_os' : REMark -*** change for fast memory   **
6260   ca$=rep$&'smsq_smsq_cache40c'
6270   caq$=rep$&'smsq_q40_cache'
6280   caq4$=rep$&'smsq_q40_cachemode'
6290   os2$=rep$&'smsq_sbas_control'
6300   ns2$=rep$&'smsq_q40_nasty'
6310   ln1$=rep$&'smsq_smsq_lang'
6320   ln2$=rep$&'smsq_q40_kbd_lang'
6330   ln3$=rep$&'smsq_sbas_lang'
6340   ex1$=rep$&'smsq_sbas_procs_x'
6350   ex2$=rep$&'smsq_q40_driver_dv3'
6360   ex3$=rep$&'smsq_q40_driver_ser'
6370   ex4a$=rep$&'smsq_q40_driver_ql'
6380   ex4b$=rep$&'smsq_q40_driver_16'
6390   ex4w$=rep$&'smsq_smsq_wman'
6400   ex5$=rep$&'smsq_q40_sysspr'
6410   ex9$=rep$&'smsq_smsq_hotkey'
6420   ex10$=rep$&'smsq_home_home'
6430   ex11$=rep$&'smsq_recent_recent'
6440   EW f$, qr$,ld$,hi$,os1$,ca$,caq$,os2$,ln1$,ln2$,ln3$,ex1$,ex2$,ex3$,ex4a$,ex4b$,ex4w$,ex5$,ex9$,ex10$,ex11$,ns2$,caq4$, rep$&'smsq_q40_rom'
6450 END DEFine qx0_target
6460 :
6470 DEFine PROCedure qxl_target
6480   IF FOP_IN (rep$&'smsq_qxl_qxlh.exe') <> -7: PRINT 'QXLH.EXE exists'; cmd$: QUIT
6490   p$   = rep$&'sys_boot_file'
6500   bl$  = rep$&'smsq_qxl_qxl2_exe'
6510   hst$ = rep$&'smsq_qxl_host'
6520   hi$  = rep$&'smsq_qxl_hwinit'
6530   ld$  = rep$&'smsq_smsq_loader'
6540   os1$ = rep$&'smsq_smsq_os'
6550   ca$  = rep$&'smsq_smsq_cache'
6560   os2$ = rep$&'smsq_sbas_control'
6570   ns1$ = rep$&'smsq_qxl_nasty_e'
6580   ln1$ = rep$&'smsq_smsq_lang'
6590   ln2$ = rep$&'smsq_qxl_kbd_lang'
6600   ln3$ = rep$&'smsq_sbas_lang'
6610   ex1$ = rep$&'smsq_qxl_procs_x'
6620   ex2$ = rep$&'smsq_qxl_driver_most'  : REMark Keyboard before CON
6630   ex3$ = rep$&'smsq_qxl_driver_ql'
6640   ex3b$ = rep$&'smsq_qxl_driver_16'
6650   ex3w$ = rep$&'smsq_smsq_wman'
6660   ex4$ = rep$&'smsq_qxl_driver_nd'
6670   ex5$ = rep$&'smsq_qxl_driver_dv3e'
6680   ex6$ = rep$&'smsq_smsq_hotkey'
6690   ex7$ = rep$&'smsq_qxl_sysspr'
6700   ex8$ = rep$&'smsq_home_home'
6710   ex10$= rep$&'smsq_recent_recent'
6720   last$ = rep$&'smsq_qxl_ecache'
6730   dml$ = rep$&'smsq_qxl_dummy'
6740   qm$  = rep$&'qmon_qxl_bin'
6750   r$='ram1_smsqe.exe' : f$=rep$&'smsq_qxl_smsqe.exe'
6760   cct$=rep$&"extras_exe_cct"
6770   DELETE f$
6780   EW p$, hst$,ld$,hi$,os1$,ca$,os2$,ns1$,ln1$,ln2$,ln3$,ex1$,ex2$,ex3$,ex3b$,ex3w$,ex4$,ex5$,ex6$,ex7$,ex8$,ex11$,last$, r$
6790   EW cct$, bl$,r$, f$
6800   DELETE r$
6810   OPEN #4,f$
6820   lenf=FLEN(#4)
6830   lens% = INT ((lenf+511)/512)
6840   lenb% = lenf - lens% * 512
6850   IF lenb%: lenb% = lenb% + 512
6860   BPUT #4\2, lenb% MOD 256, lenb% DIV 256, lens% MOD 256, lens% DIV 256
6870   CLOSE #4
6880 END DEFine qxl_target
6890 :
6900 DEFine PROCedure java_target
6910   f$=rep$&'sys_boot_file'
6920   v$=rep$&'smsq_smsq_vers'
6930   h$=rep$&'smsq_java_host'
6940   ld$=rep$&'smsq_smsq_loader'
6950   hi$=rep$&'smsq_java_hwinit'
6960   os1$=rep$&'smsq_java_smsq_os'
6970   os3$=rep$&'smsq_java_smsq_1mb_os'     : REMark must be after os
6980   ca$=rep$&'smsq_smsq_cache'
6990   os2$=rep$&'smsq_sbas_control'
7000   ln1$=rep$&'smsq_smsq_lang'
7010   ln2$=rep$&'smsq_gold_kbd_lang'
7020   ln3$=rep$&'smsq_sbas_lang'
7030   sb$=rep$&'smsq_sbas_procs_x'
7040   dr1$=rep$&'smsq_java_driver_most'
7050   dr2$=rep$&'smsq_java_driver_ql'
7060   dr3$=rep$&'smsq_java_driver_8'
7070   dr4$=rep$&'smsq_java_driver_16'
7080   dr5$=rep$&'smsq_smsq_wman'
7090   dr6$=rep$&'smsq_java_driver_dv3e'
7100 rem  dr7$=rep$&'smsq_java_driver_qsd_qsd'
7110   hk$=rep$&'smsq_smsq_hotkey'
7120   sp$=rep$&'smsq_java_sysspr'
7130   hm$=rep$&'smsq_home_home'
7140   ip$=rep$&'smsq_java_ip_x'
7150   re$=rep$&'smsq_recent_recent'
7160   EW f$,h$,ld$,hi$,os1$,os3$,ca$,os2$,ln1$,ln2$,ln3$,sb$,dr1$,dr2$,dr3$,dr4$,dr5$,dr6$,hk$,sp$,hm$,ip$,re$, rep$&'SMSQ_java_java'
7170 END DEFine java_target
7180 :
7190 DEFine PROCedure qpc_target
7200   p$   = rep$&'sys_boot_file'
7210   hst$ = rep$&'smsq_qpc_host'
7220   hi$  = rep$&'smsq_qpc_hwinit'
7230   ld$  = rep$&'smsq_smsq_loader'
7240   os1$ = rep$&'smsq_smsq_qpc_os'
7250   ca$  = rep$&'smsq_smsq_cache'
7260   os2$ = rep$&'smsq_sbas_control'
7270   ns1$ = rep$&'smsq_qpc_nasty_e'
7280   ln1$ = rep$&'smsq_smsq_lang'
7290   ln2$ = rep$&'smsq_qpc_kbd_lang'
7300   ln3$ = rep$&'smsq_sbas_lang'
7310   ex1$ = rep$&'smsq_sbas_procs_x'
7320   ex2$ = rep$&'smsq_qpc_driver_most'  : REMark Keyboard before CON
7330   ex3$ = rep$&'smsq_qpc_driver_ql'
7340   ex3b$= rep$&'smsq_qpc_driver_16'
7350   ex3c$= rep$&'smsq_qpc_driver_8'
7360   ex3w$= rep$&'smsq_smsq_wman'
7370   ex3s$= rep$&'smsq_qpc_sysspr'
7380   ex4$ = rep$&'smsq_qpc_driver_dv3e'
7390   ex5$ = rep$&'smsq_smsq_hotkey'
7400   ex6$ = rep$&'smsq_qpc_procs_x'
7410   ex7$ = rep$&'smsq_qpc_cdaudio_x'
7420   ex8$ = rep$&'smsq_qpc_dos_x'
7430   ex9$ = rep$&'smsq_qpc_ip_x'
7440   exa$ = rep$&'smsq_home_home'
7450   exb$ = rep$&'smsq_recent_recent'
7460   exc$ = rep$&'smsq_qpc_driver_qsound'
7470   qm$ =  rep$&"qmon_qpc_smsq"
7480   r$='ram1_smsqe.bin'
7490   EW p$, hst$,ld$,hi$,os1$,ca$,os2$,ns1$,ln1$,ln2$,ln3$,ex1$,ex2$,ex3$,ex3b$,ex3c$,ex3w$,ex3s$,ex4$,ex5$,ex6$,ex7$,ex8$,ex9$,exc$,exa$,exb$, r$
7500   COPY_O r$,rep$&'smsq_qpc_smsqe.bin'
7510   DELETE r$
7520   REMark rd$='ram1_smsqe.deb'
7530   REMark EW p$, hst$,ld$,hi$,os1$,ca$,os2$,ns1$,ln1$,ln2$,ln3$,ex1$,ex2$,ex3$,ex3b$,ex3c$,ex3w$,ex3s$,ex4$,ex5$,ex6$,ex7$,ex8$,ex9$,exa$,qm$, rd$
7540   REMark COPY_O r$,qpc$&'release_smsqe.bin'
7550   REMark COPY_O rd$,qpc$&'exe_smsqe.bin'
7560 END DEFine qpc_target
7570 :
7580 DEFine PROCedure q68_target
7590   local c%
7600   tgt$= rep$&'smsq_q68_RAM_SYS'
7610   leng=350000
7620   nl$=rep$&'sys_boot_null'
7630   f$=rep$&'sys_boot_file'
7640   qr$=rep$&'sys_boot_q68_rom_bin'
7650   ld$=rep$&'smsq_smsq_loader'
7660   hi$=rep$&'smsq_q68_hwinit'
7670   os1$=rep$&'smsq_smsq_q68_os' : REMark change for fast memory
7680   ca$  = rep$&'smsq_smsq_cache'
7690   ns2$=rep$&'smsq_q68_nasty'
7700   os2$=rep$&'smsq_sbas_control'
7710   ln1$=rep$&'smsq_smsq_lang'
7720   ln2$=rep$&'smsq_q68_kbd_lang'
7730   ln3$=rep$&'smsq_sbas_lang'
7740   ex1$=rep$&'smsq_q68_sbas_procs_x'
7750   ex2$=rep$&'smsq_q68_driver_dv3'
7760   ex3$=rep$&'smsq_q68_driver_most'
7770   ex4a$=rep$&'smsq_q68_driver_ql'
7780   ex4b$=rep$&'smsq_q68_driver_16'
7790   ex4c$=rep$&'smsq_q68_driver_8'
7800   ex4w$=rep$&'smsq_smsq_wman'
7810   ex5$=rep$&'smsq_q40_sysspr'
7820   ex9$=rep$&'smsq_smsq_hotkey'
7830   ex10$=rep$&'smsq_home_home'
7840   ex11$=rep$&'smsq_recent_recent'
7850   EW f$, qr$,ld$,hi$,os1$,ca$,os2$,ln1$,ln2$,ln3$,ex1$,ex2$,ex3$,ex4a$,ex4b$,ex4c$,ex4w$,ex5$,ex9$,ex10$,ex11$,ns2$,tgt$ : rem no net
7860   COPY_O tgt$,rep$&"smsq_q68_SMSQ_4_WIN"
7870   c%=fop_in (tgt$)
7880   fleng=flen(#c%)
7890   close#c%
7900   a=ALCHP (leng)
7910   LBYTES tgt$,a
7920   poke_l a+16,fleng                               : rem set length here for loader
7930   SBYTES_O tgt$,a,leng
7940   RECHP a
7950 END DEFine q68_target
7960 :
7970 DEFine PROCedure findit
7980 LOCal lp%,a$
7990 restore 7990
8000   DATA p$, hst$,ld$,hi$,os1$,ca$,os2$,ns1$,ln1$,ln2$,ln3$,ex1$,ex2$,ex3$,ex3b$,ex3c$,ex3w$,ex3s$,ex4$,ex5$,ex6$,ex7$,ex8$,ex9$,exa$, r$,""
8010   REPeat lp%
8020     READ a$:IF a$="":EXIT lp%
8030     t%=FOP_IN(a$ )
8040     IF (t%<0)
8050        PRINT "not found "& a$
8060     END IF
8070  END REPeat lp%
8080 END DEFine findit
8090 :
8100 DEFine FuNction find_target% (current%)
8110   IF "SMSQ Generic" INSTR mdata$(current%):RETurn 1
8120   IF "Atari" INSTR mdata$(current%):RETurn 2
8130   IF "GoldCard" INSTR mdata$(current%):RETurn 4
8140   IF "Q40" INSTR mdata$(current%):RETurn 8
8150   IF "QXL" INSTR mdata$(current%):RETurn 16
8160   IF "Aurora" INSTR mdata$(current%):RETurn 32
8170   IF "Java" INSTR mdata$(current%):RETurn 64
8180   IF "Ptr_Gen" INSTR mdata$(current%):RETurn 128
8190   IF "QPC" INSTR mdata$(current%):RETurn 256
8200   IF "Q68" INSTR mdata$(current%):RETurn 512
8210   RETurn 0                                     : REMark huh???
8220 END DEFine find_target%
8230 :
8240 define procedure change_size (x%,y%)
8250 rem this uses the GLOBAL vars : maxx%,maxy%, xsize%,ysize%,main_xxx...
8260 local xrel%,yrel%,temp,a$,ttemp%(main_lit%),abs%,yabs%,t%,bt$,mtemp%(0,0)
8270   main_lfl%(size_it%)=1                 : remark reset to available
8280   dim mtemp%(dimn(main_mfl%,1),dimn(main_mfl%,2))
8290   arrcopy main_lfl%,ttemp%              : remark keep statusses
8300   arrcopy main_mfl%,mtemp%
8310   ch_win main_defn,xrel%,yrel%                    : remark get ptr displacement
8320   xsize%=xsize%-xrel%+3
8330   if xsize%<420:xsize%=420
8340   ysize%=ysize%-yrel%                             : remark new sizes
8350   t%=2^4+2^5                                      : rem return immediately from read ptr call
8360   rptr xabs%,yabs%,t%,t%,xrel%,yrel%,bt$: remark get absolute coords
8370   if xsize%>maxx%-16:xsize%=maxy%-16
8380   if ysize%>maxy%-16:ysize%=maxy%-16    : remark max values
8390   dr_unst main_defn                               : remark no more wdw defn...
8400   dr_remv main_defn                               : remark .... now!
8410   app_addr=0                                      : remark nor any appsub wdw
8420   rows%=-1
8430   make_main_wdw                         : remark make window anew
8440   arrcopy ttemp%,main_lfl%
8450   arrcopy mtemp%,main_mfl%
8460   t%=lxs%*3
8470   xabs%=xabs%-t%:if xabs%<0:xabs%=0     : remark position ptr nicely
8480   yabs%=yabs%-8:if yabs%<0:yabs%=0
8490   sptr xabs%,yabs%,0                              : remark now
8500   if compiled
8510      dr_ppos main_defn,-1,-1,main_lfl%,main_mfl%,main_cty%
8520   else
8530      dr_puld main_defn,-1,-1,main_lfl%,main_mfl%,main_cty%
8540   end if
8550   pos_ptr main_defn,x%*65536+y%,1      : remark position pointer in wdw
8560 end define change_size
8570 :
8580 def proc autochange_size (x%,y%)
8590 rem change size when items added/removed
8600 rem this tries to change the wdw position as little as possible
8610 local lp%,xrel%,yrel%,ttemp%(main_lit%),abs%,yabs%,t%,bt$
8620   t%=2^4+2^5                                      : rem return immediately from read ptr call
8630   rptr xabs%,yabs%,t%,t%,xrel%,yrel%,bt$: remark get absolute coords
8640   arrcopy main_lfl%,ttemp%              : remark keep statusses
8650   dr_unst main_defn                               : remark no more wdw defn...
8660   dr_remv main_defn                               : remark .... now!
8670   app_addr=0                                      : remark nor any appsub wdw
8680   rows%=data_count%-1
8690   make_main_wdw                         : remark make window anew
8700   for lp%=0 to dimn(main_lfl%,1)
8710      main_lfl%(lp%)=ttemp%(lp%)+1
8720   end for lp%
8730   set_statusses
8740   main_cty%(0,1)=1
8750   xabs%=xabs%-x%:if xabs%<0:xabs%=x%    : remark position ptr nicely
8760   yabs%=yabs%-y%:if yabs%<0:yabs%=y%
8770   sptr xabs%,yabs%,0                              : remark now
8780   if compiled
8790      dr_ppos main_defn,-1,-1,main_lfl%,main_mfl%,main_cty%
8800   else
8810      dr_puld main_defn,-1,-1,main_lfl%,main_mfl%,main_cty%
8820   end if
8830   pos_ptr main_defn,x%*65536+y%,1       : remark position pointer in wdw
8840 end define autochange_size
8850 :
8860 DEFine PROCedure set_statusses
8870 REMark set all statusses of menu appsub objects
8880 LOCal temp%,lp%,stat%
8890   temp%=DIMN(main_mfl%,1)               : REMark how many are there?
8900   stat%=main_lfl%(all_it%)              : REMark status of "all" item
8910   FOR lp%=0 TO temp%
8920     IF mdata$(lp%+1,1)='*'
8930       main_mfl%(lp%,0)=16               : REMark comments set to unavailable
8940     ELSE
8950       main_mfl%(lp%,0)=stat%            : REMark all other to status
8960     END IF
8970   END FOR lp%
8980 END DEFine set_statusses
8990 :
9000 define procedure make_main_wdw
9010 rem make the main window - a pretty standard window with 1 menu appsub wdw
9020 local main_lot,main_iwt,main_iot1,infy1%,lxs%,lys%,xesp%,nbr_post%,nbr_trgs%
9030 local title$,title_size%,title_x%,temp%,appwinoffset%,li_xorig%,li_xsize%
9040 :
9050   appwinoffset%=16                      : remark appsub win additional y offset
9060   lxs%=24:lys%=12                       : remark standard item x,y, sizes
9070   inf1y%=lys%+4                         : remark y orig of inner border
9080   xesp%=lxs%+2                                    : remark loose items x spacing
9090   nbr_post%=5                                     : remark nbr of item right & left in title bar
9100   li_xorig%=6
9110   li_xsize%=7*6 + 2
9120   nbr_trgs%=10                                     : rem number of targets
9130 :
9140   title$="dir: "&rep$&" "
9150   rem first make sure of the wdw sizes (we also come here from change_size!)
9160   temp%=(xsize%-li_xorig%) div nbr_trgs% : rem x size of 1 target loose item
9170   xsize%=temp%*nbr_trgs%+ li_xorig% +3
9180   xsize%=xsize%-(xsize% mod 4)                    : rem make into nbr divisible by 4
9190 :
9200   rem calculate the y size of the wdw so that it always looks nice, in that
9210   rem the rows in the appsub always fill the appsub entirely
9220   app_y_orig%=inf1y% + appwinoffset%    : rem y offset for appsub win within wdw
9230   calc_ysize 0                                    : rem set ysize% and rows% variables
9240 :
9250   rem first calc info obj size - this also influences one loose item, used
9260   rem in the title bar
9270   title_size%=len(title$)*6             : rem length in pixels
9280   title_x%=int((xsize%-title_size%)/2): rem x orig
9290 :
9300   rem first the loose items
9310 :
9320   esc%=0:move_it%=esc%+1:size_it%=move_it%+1
9330   sleep_it%=size_it%+1:all_it%=sleep_it%+1:go_it%=all_it%+1
9340   dir_it%=go_it%+1:target_its%=dir_it%+1:make_it%=target_its%+nbr_trgs%
9350   info_it%=make_it%+1:del_it%=info_it%+1:alltargets_it%=del_it%+1
9360 :
9370 restore 9370
9380 :
9390   main_lit%=20:dim main_lfl%(main_lit%)   : rem there are 21 items
9400   main_lot=rd_lot(std_iattr,main_lit%)
9410     data lxs%,lys%,4+xesp%*0,2,0,0,cancel$,text+retr,'ESC'
9420     data lxs%,lys%,4+xesp%*1,2,0,0,move$,sprite+retr,move_sprite
9430     data lxs%,lys%,4+xesp%*2,2,0,0,size$,sprite+retn,size_sprite
9440     data lxs%,lys%,xsize%-2-xesp%*1,2,0,0,sleep$,sprite+retr,sleep_sprite
9450     data lxs%,lys%,xsize%-2-xesp%*3,2,0,0,'A',sous1+retn,"All"
9460     data lxs%,lys%,xsize%-2-xesp%*4,2,0,0,'O',sous1+retn,"Ok"
9470     data 6*6,lys%-2,title_x%+30,3,0,0   ,'D',text+retn,rep$
9480     data li_xsize%,lys%,li_xorig%,inf1y%+2,0,0,        'G',sous1+retn,"Generic"
9490     data li_xsize%,lys%,li_xorig%+temp%,inf1y%+2,0,0,  'T',sous2+retn,"Atari"
9500     data li_xsize%,lys%,li_xorig%+temp%*2,inf1y%+2,0,0,'L',sous3+retn,"Goldcrd"
9510     data li_xsize%,lys%,li_xorig%+temp%*3,inf1y%+2,0,0,'Q',sous1+retn,"Q40/Q60"
9520     data li_xsize%,lys%,li_xorig%+temp%*4,inf1y%+2,0,0,'X',sous2+retn,"Qxl"
9530     data li_xsize%,lys%,li_xorig%+temp%*5,inf1y%+2,0,0,'U',sous2+retn,"Aurora"
9540     data li_xsize%,lys%,li_xorig%+temp%*6,inf1y%+2,0,0,'J',sous1+retn,"Java"
9550     data li_xsize%,lys%,li_xorig%+temp%*7,inf1y%+2,0,0,'P',sous1+retn,"PtrGen"
9560     data li_xsize%,lys%,li_xorig%+temp%*8,inf1y%+2,0,0,'C',sous3+retn,"QPC"
9570     data li_xsize%,lys%,li_yorig%+temp%*9,inf1y%+2,0,0,'8',sous3+retn,"Q68"
9580     data 4*6,lys%,xsize%-2-xesp%*2,2,0,0,'M',sous1+retn,"Make"
9590     data 4*6,lys%,xsize%-2-xesp%*5,2,0,0,'?',sous1+retn,"?"
9600     data lxs%,lys%,4+xesp%*3,2,0,0,"E",sous2+retn,"DEL"
9610     data lxs%,lys%,4+xesp%*4,2,0,0,"S",sous1+retn,"SaT"
9620 :
9630 rem now info wdw objects - here only one, the title
9640   rem make the title data: calculate size and x position of this object
9650   main_iot1=rd_iot(0)
9660     data title_size%,10,4,int((lys%-10)/2),sous1,sp.titlefg,0,0,title$
9670 :
9680 rem now the info wdws
9690   main_iwt=rd_iwt(2)                              : rem there are 3
9700   rem first the title bar
9710     data xsize%-8-(2*nbr_post%*xesp%),lys%+2,xesp%*nbr_post%+4,0       : rem x,y sizes & origs
9720     data 0,0,sp.infwinbd,sp.titlebg     : rem shadow, border size & col,paper col
9730     data 0                                        : rem object pointer (none)
9740 :
9750   rem now wdw containing title itself
9760     data title_size%+8,lys%,title_x%-4,2
9770     data 0,0,sp.infwindbd,sp.infwinbg
9780     data main_iot1                      : rem one object
9790 :
9800   rem inner border
9810     data xsize%-8,ysize%-inf1y%-2,4,inf1y%
9820     data 0,1,sp.infwinbd,sp.infwinbg
9830     data 0
9840 :
9850 rem now the appsub wdw - use the outptr functions for this
9860   main_app_list=0                       : rem appsub win list
9870   app_addr=0                                      : rem there is no appsub win yet
9880   menu_selkeys$=""                      : rem selkeys to exclude
9890   appw_sections%=1                      : rem display only one section
9900   app_addr=mk_main_app(app_addr,mdata$,0,0,1,0,0,0) : rem make apsub wdw
9910   main_app_list = app_addr+180                    : rem list built by outptr
9920   set_statusses                         : rem set statusses of menu items
9930 :
9940 rem the working defn
9950   main_defn=rd_wdef
9960     data 2,1,sp.winbd,sp.winbg                    : rem shad,border size & col,pap col
9970     data xsize%,ysize%,0,0              : rem x,y size & initial ptr pos
9980     data main_sprite,main_lot,main_iwt,main_app_list : rem sprite,loose items, info wins, appsubs
9990 end define make_main_wdw
10000 :
10010 def fn warning_pos% (mess$,channel%,title$,pos)
10020 rem warning wdw with pointer positioning
10030   pos_ptr main_defn,pos,-1
10040   return warning%(mess$,channel%,title$)
10050 end def warning_pos%
10060 :
10070 define function warning%(mess$,channel%,title$)
10080 rem shows a warning message (mess$) in a small window. hit esc or ok
10090 rem to return; returns 0 if esc, 1 if ok  hit
10100 local item%,lp%,chan%,y%,swnum%,infot%
10110   stop_info
10120   if channel%
10130     chan%=channel%
10140   else
10150     chan%=fopen("con")
10160   endif
10170   ch_item warn_defn,-3,0,-1,'','Attention'
10180   ch_item warn_defn,-1,0,-1,cancel$,'ESC'
10190   ch_item warn_defn,-1,1,-1,'O','OK'
10200   warn_lfl%(0)=1:warn_lfl%(1)=1
10210   poke_w warn_defn+44,sp.errmg                           : rem border colour of wdw
10220   poke_w warn_defn+46,sp.errbg                           : rem paper colour of wdw
10230   poke_w warn_wdw_addr+14,sp.errbg             : rem paper of title info wdw
10240   obj_addr=peek_l(warn_wdw_addr+16)            : rem ptr to object list
10250   poke_w obj_addr+10,sp.errfg                            : rem info object text  colour
10260   dr_puld warn_defn,-1,-1
10270   dr_iwdf #chan%,warn_defn,0
10280   wm_paper#chan%,sp.errbg
10290   wm_ink#chan%,sp.errfg
10300   cls#chan%
10310   if title$<>"":print#chan%,title$;" :"
10320   print #chan%,mess$
10330   pos_ptr warn_defn,160*65536+10,-1
10340   repeat lp%
10350     rd_ptr warn_defn,item%,swnum%,y%,y%,y%,warn_lfl%
10360     if swnum%=-1:exit lp%
10370   end repeat lp%
10380   if not channel%:close#chan%
10390   dr_unst warn_defn
10400   return item%
10410 end define warning%
10420 :
10430 def proc info_pos (mess$,channel%,pos)
10440 rem warning wdw with positioning
10450   pos_ptr main_defn,pos,-1
10460   info mess$,channel%
10470 end def info_pos
10480 :
10490 define procedure info(mess$,channel%)
10500 rem shows window containing mess$. channel% is a screen channel, if this is 0,
10510 rem a temp screen channel will be opened.
10520 local chan%,infot%,obj_addr
10530   if channel%
10540     chan%=channel%
10550   else
10560     chan%=fopen("con")
10570   endif
10580   ch_item warn_defn,-3,0,-1,"",'  Info.  '
10590   ch_item warn_defn,-1,0,-1,"",'Info'
10600   ch_item warn_defn,-1,1,-1,"",'Info'
10610   poke_w warn_defn+44,sp.infwinbg                 : rem paper colour of wdw
10620   poke_w warn_defn+46,sp.winbd                              : rem border around wdw
10630   poke_w warn_wdw_addr+14,sp.infwinbg             : rem paper of title info wdw
10640   obj_addr=peek_l(warn_wdw_addr+16)               : rem ptr to object list
10650   poke_w obj_addr+10,sp.infwinfg                  : rem info object text        colour
10660   dr_puld warn_defn,-1,-1
10670   pos_ptr warn_defn,90*65536+70,-1
10680   dr_iwdf #chan%,warn_defn,0
10690   wm_paper#chan%,sp.infwinbg
10700   wm_ink#chan%,sp.infwinfg
10710   cls#chan%
10720   print #chan%,mess$
10730   if not channel%:close#chan%
10740   info_is_pulled_down%=1
10750 end define info
10760 :
10770 def proc info_wait(mess$,channel%)
10780 rem shows info wdw, waits until user clicks, closed info wdw
10790   show_info mess$,channel%
10800   info_wait_to_close
10810 end def info_wait
10820 :
10830 def proc info_wait_to_close
10840 local item%,lp%,chan%,y%,swnum%,infot%
10850   repeat lp%
10860     rd_ptr warn_defn,item%,swnum%,y%,y%,y%,warn_lfl%
10870     if swnum%=-1:exit lp%
10880   end repeat lp%
10890   stop_info
10900 end def info_wait_to_close
10910 :
10920 def proc info_pos_wait (mess$,channel%,pos)
10930 rem positions & shows info wdw, waits until user clicks,closes info wdw
10940   info_pos mess$,channel%,pos
10950   info_wait_to_close
10960 end def info_pos_wait
10970 :
10980 def proc stop_info
10990   if info_is_pulled_down%
11000     dr_unst warn_defn
11010     info_is_pulled_down%=0
11020   endif
11030 end def stop_info
11040 :
11050 def proc show_info (mess$,channel%)
11060 rem either opens the info wdw and shows the message or shows the message in
11070 rem the existing info wdw
11080 local chan%
11090   if channel%
11100     chan%=channel%
11110   else
11120     chan%=fopen("con")
11130   endif
11140   if info_is_pulled_down%
11150     cls#chan%
11160     wm_paper#chan%,sp.infwinbg
11170     wm_ink#chan%,sp.infwinfg
11180     pe_cls#chan%
11190     pe_print #chan%,mess$
11200   else
11210     info mess$,chan%
11220   endif
11230   if not channel%:close#chan%
11240 end def show_info
11250 :
11260 DEFine PROCedure button (def_trav,menu_drap%,app_drap%,control%,chaine$,type_col%)
11270 REMark les parametres sont, dans l'ordre:
11280 REMark la dƒfinition de la fen‘tre, suivie des tableaux pour
11290 REMark les postes du menu dƒliƒ, les postes de la sous-fen‘tre d'appl.
11300 REMark et la dƒfinition de contr˜le, suivi du nom du bouton et
11310 REMark du type couleurs.
11320 REMark Il est supposƒ que vous avez ouvert une fen‘tre #1 si
11330 REMark le programme est compilƒ
11340   DR_UNST def_trav
11350   IF compiled: CLOSE#1
11360   rem button_wait chaine$
11370   BTN chaine$,type_col%
11380   IF compiled
11390     OPEN#1,"CON_"
11400     DR_PPOS def_trav,-1,-1,menu_drap%,app_drap%,control%
11410   ELSE
11420     DR_PULD def_trav,-1,-1,menu_drap%,app_drap%,control%
11430   END IF
11440 END DEFine button
11450 :
11460 DEFine FuNction centr$(ctr$,leng)
11470 rem this "centres" the string ctr$ (i.e. left padds it with spaces) so that
11480 rem it is centred in a window of leng width (leng is length in chars)
11490   LOCal l%,cta$
11500   cta$=ctr$
11510   strip_spaces cta$
11520   l%=INT(leng-LEN(cta$))/2
11530   if l%<0:return cta$
11540   return FILL$(' ',l%)&cta$&FILL$(' ',l%)
11550 END DEFine centr$
11560 :
11570 def proc calc_ysize (y_csize%)
11580 rem This tries to calculate the window y size depending on the given y size,
11590 rem the number of rows wished in the menu appsub window and the y position of
11600 rem the menu appsub window.
11610 rem It uses and changes the GLOBAL variables rows%, app_y_orig% and ysize%.
11620 rem It also uses the GLOBAL var maxy% which is the maximum y size a window
11630 rem could have in the current screen resolution.
11640 rem algo is as follows:
11650 rem if both ysize and rows% are undefined (=-1) : make wdw as big as possible
11660 rem if one is undefined, calc the other
11670 rem if both are defined, give precedence to the rows number
11680 rem
11690 rem param : y_csize% is the y character height, either 0 (10 pix) or 1 (20 pix)
11700 rem
11710 local y_spcg%
11720   if rows%=0:return
11730   y_spcg% = 12+(y_csize%*10)
11740   if rows%= -1
11750     if ysize%= -1
11760       rows%=(maxy%-14-app_y_orig%) div y_spcg% : rem max nbr of rows
11770     else
11780       rows%= (ysize%-app_y_orig%-4) div y_spcg%
11790     endif
11800   endif
11810   if rows%<2:rows%=2
11820   ysize%=rows%*y_spcg%+app_y_orig%+4
11830   if ysize%>=maxy%-14
11840     ysize%=maxy%-app_y_orig%-14
11850     rows%=ysize% div y_spcg%
11860     ysize%=rows%*y_spcg% + app_y_orig% + 4
11870   endif
11880   if ysize%<minysize%
11890     ysize%=minysize% -app_y_orig%-14
11900     rows%=ysize% div y_spcg%
11910     ysize%=rows%*y_spcg% + app_y_orig% + 4
11920   endif
11930 end def calc_ysize
11940 :
11950 DEFine PROCedure scroll_arrows(rows%,nbr_objects%,appfen,cty%)
11960 rem This determined whether an appsub wdw should have scroll arrows.
11970 rem Params: number of visible rows in the appsub wdw, ,total nbr of object rows
11980 rem in the abbsub wdw (not the visible rows, but the rows with objects),
11990 rem pointer to the appsub wdw defn returned by the APP_MAKE or APP_MAKE2 function
12000 rem and the control definition
12010   IF nbr_objects%<=rows%
12020     POKE_W appfen+150,0       : rem all objects fit , so no scroll arrows
12030                                         : rem and no y offset to start of menu
12040     poke_l appfen+96,0                  : rem no scroll bars
12050     cty%(1,2)=nbr_objects%    : rem nbr of columns/rows
12060   ELSE
12070     POKE_W appfen+150,6       : rem leave space for arrows
12080     cty%(1,2)=rows%-1                   : rem if scroll arrows are there, there is one line less in the window
12090     poke_l appfen+96,hex("2210222")
12100   END IF
12110   IF DIMN(cty%,1)=2:cty%(2,2)=cty%(1,2)
12120   cty%(0,1)=1                            : rem show that definition has changed
12130 END DEFine scroll_arrows
12140 :
12150 DEFine FuNction getstr$(poste%,drap%,defn,a$)
12160 REMark opens and sets a window over a loose item and proposes a sting to be edited.
12170 REMark parameters:
12180 REMark - item nbr
12190 REMark - flag whether result MUST be a number (flag<>0)
12200 REMark - the working definition
12210 REMark - the string to edit
12220 rem returns the string or ""
12230 rem uses the global (!) vars sp.litemselbg and sp.litemselfg
12240 rem
12250 local b$,chan%
12260   chan%=FOPEN("con")
12270   b$=getstr2$(poste%,drap%,defn,a$,chan%)
12280   close#chan%
12290   return b$
12300 END DEFine getstr$
12310 :
12320 DEFine FuNction getstr2$(poste%,drap%,defn,a$,chan%)
12330 REMark sets a window over a loose item and proposes a sting to be edited.
12340 REMark parameters:
12350 REMark - item nbr
12360 REMark - flag whether result MUST be a number (flag<>0)
12370 REMark - the working definition
12380 REMark - the string to edit
12390 remark - a screen channel to be used
12400 rem returns the string or ""
12410 rem uses the global (!) vars sp.litemselbg and sp.litemselfg
12420 rem
12430   LOCal b$,res
12440   b$=" ":b$=a$           : REMark c'est curieusement nƒcessaire
12450   wm_PAPER#chan%,sp.litemselbg
12460   wm_INK#chan%,sp.litemselfg
12470   DR_LWDF#chan%,defn,poste%             : rem open channel over item
12480   CLS#chan%
12490   res=GET_STR(#chan%,drap%,b$)                                : rem get string
12500   IF res<0:b$=""                        : rem ooops
12510   CH_ITEM defn,-1,poste%,-1,"",b$
12520   RETurn b$
12530 END DEFine getstr2$
12540 :
12550 def fn showcont$(string$,length%)
12560 rem this adds '...' at the end of a string, to show that it continues
12570 local l%,lengt%
12580   l%=len(string$)
12590   if not l%:return ""                             : rem empty string
12600   lengt%=length% div 6
12610   if l%<=lengt%:return string$                    : rem entire string fits wdw
12620   return string$(1 to lengt%-3)&"..."
12630 end def showcont$
12640 :
12650 def fn get_free_text$(defn,item%,prompt$,length%)
12660 rem this opens wdw over loose-item item% of the working definition defn
12670 rem allows free text to be entered
12680 rem params: working defintion,item over which wdw is to be opened
12690 rem           first value of string, length of max string in wdw, if this is...
12700 rem              -1, the length will be determined here
12710 local chan%,string$,tempsizes%(3)
12720   chan%=fopen('con')
12730   if chan%<0:return prompt$
12740   dr_lwdf#chan%,defn,item%              : rem open wdw over item
12750   paper#chan%,menu_brd
12760   ink#chan%,menu_ink
12770   cls#chan%
12780   string$=wedit$(#chan%,prompt$)
12790   if length%=-1
12800      wsipo#chan%,tempsizes%
12810      length%=tempsizes%(0)
12820   endif
12830   close#chan%
12840   if length%
12850     ch_item defn,-1,item%,-1,"",showcont$(string$,length%)
12860   else
12870     ch_item defn,-1,item%,-1,"",string$
12880   endif
12890   return string$
12900 end def get_free_text$
12910 :
12920 def proc make_my_palette (cw%)
12930   sp.winbd            = hex('0200') : rem Window border
12940   sp.winbg            = hex('0201') : rem Window background
12950   sp.winfg            = hex('0202') : rem Window foreground
12960   sp.winmg            = hex('0203') : rem Window middleground
12970   sp.titlebg          = hex('0204') : rem Title background
12980   sp.titletextbg  = hex('0205') : rem Title text background
12990   sp.titlefg          = hex('0206') : rem Title foreground
13000   sp.litemhigh        = hex('0207') : rem Loose item highlight
13010   sp.litemavabg   = hex('0208') : rem Loose item available background
13020   sp.litemavafg   = hex('0209') : rem Loose item available foreground
13030   sp.litemselbg   = hex('020a') : rem Loose item selected background
13040   sp.litemselfg   = hex('020b') : rem Loose item selected foreground
13050   sp.litemunabg   = hex('020c') : rem Loose item unavailable background
13060   sp.litemunafg   = hex('020d') : rem Loose item unavailable foreground
13070   sp.infwinbd         = hex('020e') : rem Information window border
13080   sp.infwinbg         = hex('020f') : rem Information window background
13090   sp.infwinfg         = hex('0210') : rem Information window foreground
13100   sp.infwinmg         = hex('0211') : rem Information window middleground
13110   sp.subinfbd         = hex('0212') : rem Subsidiary information window border
13120   sp.subinfbg         = hex('0213') : rem Subsidiary information window background
13130   sp.subinffg         = hex('0214') : rem Subsidiary information window foreground
13140   sp.subinfmg         = hex('0215') : rem Subsidiary information window middleground
13150   sp.appbd            = hex('0216') : rem Application window border
13160   sp.appbg            = hex('0217') : rem Application window background
13170   sp.appfg            = hex('0218') : rem Application window foreground
13180   sp.appmg            = hex('0219') : rem Application window middleground
13190   sp.appihigh         = hex('021a') : rem Application window item highlight
13200   sp.appiavabg        = hex('021b') : rem Application window item available background
13210   sp.appiavafg        = hex('021c') : rem Application window item available foreground
13220   sp.appiselbg        = hex('021d') : rem Application window item selected background
13230   sp.appiselfg        = hex('021e') : rem Application window item selected foreground
13240   sp.appiunabg        = hex('021f') : rem Application window item unavailable background
13250   sp.appiunafg        = hex('0220') : rem Application window item unavailable foreground
13260   sp.scrbar           = hex('0221') : rem Pan/scroll bar
13270   sp.scrbarsec        = hex('0222') : rem Pan/scroll bar section
13280   sp.scrbararr        = hex('0223') : rem Pan/scroll bar arrow
13290   sp.buthigh          = hex('0224') : rem Button highlight
13300   sp.butbd            = hex('0225') : rem Button border
13310   sp.butbg            = hex('0226') : rem Button background
13320   sp.butfg            = hex('0227') : rem Button foreground
13330   sp.hintbd           = hex('0228') : rem Hint border
13340   sp.hintbg           = hex('0229') : rem Hint background
13350   sp.hintfg           = hex('022a') : rem Hint foreground
13360   sp.hintmg           = hex('022b') : rem Hint middleground
13370   sp.errbg            = hex('022c') : rem Error message background
13380   sp.errfg            = hex('022d') : rem Error message foreground
13390   sp.errmg            = hex('022e') : rem Error message middleground
13400   sp.shaded           = hex('022f') : rem sp.shaded area
13410   sp.3ddark           = hex('0230') : rem Dark 3D border shade
13420   sp.3dlight          = hex('0231') : rem Light 3D border shade
13430   sp.vertfill         = hex('0232') : rem Vertical area fill
13440   sp.subtitbg         = hex('0233')  : rem Subtitle background
13450   sp.subtittxtbg  = hex('0234')  : rem Subtitle text background
13460   sp.subtitfg         = hex('0235')  : rem Subtitle foreground
13470   sp.mindexbg         = hex('0236')  : rem Menu index background
13480   sp.mindexfg         = hex('0237')  : rem Menu index foreground
13490   sp.separator        = hex('0238')  : rem Seperator lines etc.
13500   if cw%
13510     if not (is_open(#0) or is_open(#1))
13520       open#1,'con'
13530       sp_jobpal -1,cw%
13540       close#1
13550     else
13560       sp_jobpal -1,cw%
13570     endif
13580   endif
13590 end def make_my_palette
13600 :
13610 def fn set_colours% (force_mode%)
13620 rem this sets colour variables according to the colour mode wished, if possible.
13630 rem force_mode%=
13640 rem       0 if ql colours wished (mode 4 only)
13650 rem       1 if pal colours wished
13660 rem       2 if 24 bit colours wished
13670 rem This function returns the true QL mode set (4,8,32,33 etc.).
13680 rem This function also sets the GLOBAL variable mycolour_mode% to the
13690 rem mode 0,1,2 as above - whatever is possible on the machine, i.e.
13700 rem if colour_24 is wished on a 4 colour machine (which won't succeed)
13710 rem this function will set set that variable to 0, i.e. QL mode.
13720 rem
13730 rem This function needs the outptr toolkit
13740 local mode%
13750   mode%=rmode                                     : rem get current mode now
13760   select on force_mode%
13770       = 0                                         : rem mode 4 wished
13780        select on mode%
13790             = 4                                   : rem already on, so do nothing
13800             = 0,1,8 : mode 4            : rem old modes, set to 4 colours
13810             = remainder
13820               colour_ql                           : rem if higher colour mode, set to "mode 4"
13830               mode 4
13840        end select
13850        mode%=0                                    : rem new mode we're in  - 4 colour mode
13860       = 1                                         : rem PALette wished
13870        select on mode%
13880             = 16,32,33 : colour_pal:mode%=1  : rem we're in high colour mode, but PAL colours wished
13890             = remainder
13900               if mode%<>4:mode 4
13910               mode%=0
13920        end select
13930       = 2
13940        select on mode%
13950             = 32,33 : colour_24 :mode%=2          : rem we're in high col mode 24 bit colours
13960             = 16 : colour_pal : mode%=1
13970             = remainder
13980               if mode%<>4:mode 4
13990               mode%=0
14000        end select
14010   end select
14020   mycolour_mode%=mode%                            : rem set this global variable now:
14030   rem now set some colour variables
14040   select on mode%
14050        = 0                                        : rem Ql 4 mode colours
14060           Black            = 0
14070           White            = 7
14080           Red              = 2
14090           Green            = 4
14100           wg_striped     = 92
14110           br_striped     = 82
14120           wr_striped     = 107
14130           bg_striped     = 100
14140        = 1                              : rem Colour_pal
14150           Black            = 0
14160           White            = 1
14170           Red              = 2
14180           Green            = 3
14190           wg_striped     = light_green
14200           br_striped     = dark_red
14210           wr_striped     = light_red
14220           bg_striped     = dark_green
14230        = 2                              : rem colour_24
14240           Black            = hex('000000')
14250           White            = hex('FFFFFF')
14260           Red              = hex('FF0000')
14270           Green            = hex('00FF00')
14280           wg_striped     = light_green
14290           br_striped     = dark_red
14300           wr_striped     = light_red
14310           bg_striped     = dark_green
14320   end select
14330   mode%=rmode
14340   ret mode%                             : rem return current screen mode now
14350 end def set_colours%
14360 :
14370 def fn do_caps(a$,caps%)
14380 rem set string to capitales etc,caps% determines whether the string....
14390 REMark ...must be lower case (=0), upper case(=1), all words capitalized (=2),
14400 REMark ... first word only capitalized (=3), or nothing(=-1)
14410   SELect ON caps%
14420    =0: MIN a$
14430    =1: MAJ a$
14440    =2: CAP2 a$
14450    =3: CAP a$
14460   END SELect
14470   return a$
14480 end def do_caps
14490 :
14500 def fn mk_main_app (app_addr,subwinarray$,x_csize%,y_csize%, use_apm2%,hit_is_do%,sizex%,origx%)
14510 rem This makes a standard menu appsub with standard global variables.
14520 rem It presumes that the followng vars have been set:
14530 rem  * rows% holds the number of rows to be seen in the appsub window when there
14540 rem    are no scroll arrows (if there are, 1 less row will be seen)
14550 rem  * app_y_orig% = y origin of appsub within the main wdw
14560 rem  * xsize% = x size of main window (NOT subwindow)
14570 rem  * menu_selkeys$ holds the selkeys to exclude (string with upper cased keys)
14580 rem  *      set to "" if no selkeys wished, set to " " if no selkeys are excluded
14590 rem  * appw_sections% holds the nbr of sections to show
14600 rem  * main_cty% (DIMed here) is the  control sections array
14610 rem  * main_mfl% (DIMed here) is the menu items flag array
14620 rem
14630 rem Params:
14640 rem  * app_addr is the address of the appsub window, but 0 on first call
14650 rem  * subwinarry$ holds the data (2 dimensional string array)
14660 rem  * x, y_csize% are the csizes of objects in the appsub, standard values
14670 rem  * use_apm2% whether we use (=1) AP_MAKE2 or not (=0)
14680 rem  * hit_is_do% if hit should be = do
14690 rem  * sizex%= wished x size, leave at 0 if automatic size wished
14700 rem  * origx%= wished x orig, leave at 0 if automatic orig wished
14710 rem
14720 local temp%,selk%,app_attrs%(3,1),xs%,xo%
14730   if app_addr:rechp app_addr
14740   dim main_cty%(0):dim main_mfl%(0,0)
14750   temp%=x_csize%*16                               : rem x csize into upper nibble
14760   temp%=(temp%+y_csize%)*256            : rem y into lower, all into upper byte
14770   temp%=temp%+appw_sections%            : rem temp% is changed by the call to fen_app
14780   if menu_selkeys$=""
14790     selk%=0                                       : rem use no selkeys
14800   else
14810     selk%=1
14820   endif
14830   if hit_is_do%:selk%=selk%+256         : rem hit should be do
14840   if sizex%<1
14850     xs%=xsize%-16
14860   else
14870     xs%=sizex%
14880   endif
14890   if origx%<1
14900     xo%=0
14910   else
14920     xo%=origx%
14930   endif
14940   if use_apm2%
14950     app_addr=ap_make2(selk%,xs%,rows%,xo%,app_y_orig%,temp%,subwinarray$)
14960   else
14970     app_addr=ap_make(selk%,xs%,rows%,xo%,app_y_orig%,temp%,subwinarray$,menu_selkeys$&" ")
14980   endif                                 : rem on return temp%=nbr of objects
14990   IF app_addr = 0: RETurn 0             : rem  error, return 0
15000 :
15010   dim main_cty%(appw_sections%,2)       : rem control defn
15020   main_cty%(0,0)=1                      : rem at start show 1 section
15030   if temp%>rows%
15040     main_cty%(1,2)=rows%                : rem nbr of rows to show
15050   else
15060     main_cty%(1,2)=temp%                : rem there are less objects than rows
15070   endif
15080   main_cty%(1,2)=rows%
15090   if appw_sections%=2:main_cty%(2,2)=2
15100   if temp%
15110      dim main_mfl%(temp%-1,0)                     : rem flag array if there are any objects
15120   end if
15130   scroll_arrows rows%,temp%,app_addr,main_cty%
15140   return app_addr
15150 end define mk_main_app
15160 :
15170 def proc make_standard_sprites
15180 rem This makes standard move, sleep and wake sprites and sets the
15190 rem GLOBAL vars move_sprite,, sleep_sprite, wake_sprite and size_sprite to them.
15200 rem If there are system sprites, these are used.
15210 rem This also makes the "main_sprite" pointer sprite.
15220 :
15230   espace_vide=ALCHP(100)         : rem for pointers with no mask
15240 :
15250 restore 15250
15260   main_sprite=RD_SPRT(0)         : REMark ici un masque normal
15270     data 11,13,6,5,4
15280     data '     www     '
15290     data '     waw     '
15300     data '     waw     '
15310     data '     waw     '
15320     data 'wwwwwa awwwww'
15330     data 'waaaa   aaaaw'
15340     data 'wwwwwa awwwww'
15350     data '     waw     '
15360     data '     waw     '
15370     data '     waw     '
15380     data '     www     '
15390 :
15400   if sys_tspr_addr(6)<>0
15410     move_sprite=6
15420   else
15430 restore 15430
15440     move_sprite=RD_SPRT(1)         : REMark ici un masque 0
15450     data 10,14,5,4,4
15460     data 'aaaaaaaaaa    '
15470     data 'awwwwwwwwa    '
15480     data 'awwrrrrwwa    '
15490     data 'awwrrrrwwaaaaa'
15500     data 'awwrrwwwwwwwwa'
15510     data 'awwwwwwwwrrwwa'
15520     data 'aaaaawwrrrrwwa'
15530     data '    awwrrrrwwa'
15540     data '    awwwwwwwwa'
15550     data '    aaaaaaaaaa'
15560   endif
15570 :
15580   if sys_tspr_addr(10)<>0
15590     sleep_sprite=10
15600   else
15610 restore 15610
15620     sleep_sprite= RD_SPRT(1)             : REMark ici un masque 0
15630     data 7,15,0,0,4
15640     data 'wwwww          '
15650     data '   w           '
15660     data '  w  wwww      '
15670     data ' w     w       '
15680     data 'wwwww w  wwww  '
15690     data '     wwww  w   '
15700     data '          wwww '
15710   endif
15720 :
15730   if sys_tspr_addr(11)<>0
15740     wake_sprite=11
15750   else
15760 restore 15760
15770     wake_sprite=RD_SPRT(1)          : REMark ici un masque 0
15780     data 9,14,0,0,4
15790     data "             w"
15800     data "            w "
15810     data "      w   ww  "
15820     data "     ww www   "
15830     data "    wwwwww    "
15840     data "   www ww     "
15850     data "  w   w      "
15860     data " w            "
15870     data "w             "
15880   endif
15890 :
15900   if sys_tspr_addr(7)<>0
15910     size_sprite=7
15920   else
15930 restore 15930
15940     size_sprite=RD_SPRT(1)
15950     data 9,14,5,4,4
15960     data 'aaaaaaaaaaaaaa'
15970     data 'awwwwwwwwwwwwa'
15980     data 'awwrrrrrrrrwwa'
15990     data 'awwrrrrrrrrwwa'
16000     data 'awwrrwwwwwwwwa'
16010     data 'awwrrwwrrrrwwa'
16020     data 'awwrrwwrrrrwwa'
16030     data 'awwwwwwwwwwwwa'
16040     data 'aaaaaaaaaaaaaa'
16050   endif
16060 :
16070 end def make_standard_sprites
16080 :
16090 DEFine PROCedure warn_make
16100 rem makes warning & info window
16110 rem uses global vars sp.xxx
16120 rem SETS the global var warning_width% (nbr of chars in one line in wdw)
16130 local lys%,warn_lot,warn_iot1,warn_iwt
16140 local x%,y%,warning_paper,warning_brd
16150 restore 16150
16160   x%=200:y%=82
16170   lys%=10
16180   DIM warn_lfl%(1)
16190   warn_lot=RD_LOT(std_iattr,1)
16200     DATA 30,lys%,4,4,0,0,cancel$,text+retr,'ESC'
16210     DATA 30,lys%,x%-34,4,0,0,'O',text+retr,"OK"
16220 :
16230   warn_iot1=RD_IOT (0)
16240     DATA 12*9,10,0,1,text,sp.errfg,2,0,'Attention'
16250 :
16260   warn_iwt=RD_IWT(1)
16270     DATA x%-16,y%-32,8,24
16280     DATA 0,1,sp.winbd,sp.errbg
16290     DATA 0
16300 :
16310     DATA 110,12,(x%-110) div 2,4
16320     DATA 0,1,sp.winbd,sp.errbg
16330     DATA warn_iot1
16340 :
16350   warn_defn=RD_WDEF
16360     DATA 2,1,sp.winbd,sp.errbg
16370     DATA x%,y%,0,0
16380     DATA main_sprite,warn_lot,warn_iwt,0
16390 :
16400   warning_width%=(x%-16) div 6                    : rem width of warning window in chars
16410   info_is_pulled_down%=0
16420   warn_wdw_addr=peek_l(warn_defn+100)+20: rem ptr to 2nd info wdw
16430  rem warn_wdw_addr=peek_l(warn_wdw_addr+16): rem ptr to object list
16440 END DEFine warn_make
16450 :
16460 DEFine PROCedure about_make (titre$,version$)
16470 local lxs%,lys%,xesp%,inf1y%,abt_lot,longueur_titre%,titre_x%
16480 local abt_iwt,abt_iot1,abt_iot2
16490   ab_xt%=200:ab_yt%=120    : REMark tailles x et y: la taille x devrait
16500                                    : REMark ‘tre un chiffre divisible par 4
16510   lxs%=24:lys%=10                    : REMark tailles x et y pour postes standard
16520   inf1y%=lys%+4            : REMark posi. y de la fen. d'inf. intƒrieure
16530   xesp%=lxs%+2
16540   nbr_post%=0                      : REMark nombre de postes dans un coin
16550 :
16560 restore 16560
16570 wolfsoft_sprite=rD_SPrT(0)
16580     DATA 23,51,0,0,4
16590     DATA 'rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr'
16600     DATA 'rrgggggggggggggggggggggggggggggggggggggggggggggggrr'
16610     DATA 'rrggggggggrrrrrrrrrrrrrrrrgggggggggggggggggggggggrr'
16620     DATA 'rrgggggggggggggggggggggrgggggggggggggggggggggggggrr'
16630     DATA 'rrggggggggggggggggggggrggggggggggggggggggggggggggrr'
16640     DATA 'rrggggggggagggggagggrrrrrrrrrrrrrggaaaaaaaaggggggrr'
16650     DATA 'rrggggggggagggggaggggggggggggggggggagggggggggggggrr'
16660     DATA 'rrggggggggaggaggaggaaaaaaaggaggggggaaaaaaagggggggrr'
16670     DATA 'rrggggggggagaaagaggaawwaaaggaggggggagggggggggggggrr'
16680     DATA 'rrggggggggaaagaaaggaawwwwaggaggggggagggggggggggggrr'
16690     DATA 'rrggrrrrrgaagggaaggawwwwaaggaggggggagggggrrrrrrrgrr'
16700     DATA 'rrgrgggggrgggggggggaaawwaaggaggggggggggrrrgggggggrr'
16710     DATA 'rrgrggggggrggggggggaaaaaaaggaaaaaagggrrggrgggggggrr'
16720     DATA 'rrgrgggggggggggggrrrgggggggggggrrrggrggggrgggggggrr'
16730     DATA 'rrggrrrrrrggggggrgggrrgggggggrrggggggggggrgggggggrr'
16740     DATA 'rrggggggggrrrgggrgggggrrgggrrggggggggggggrgggggggrr'
16750     DATA 'rrgggggggggggrggrgggggggrggrgggggggggggggrgggggggrr'
16760     DATA 'rrggggggggggggrgrggggggggrgrrrrrrrgggggggrgggggggrr'
16770     DATA 'rrgrggggggggggrgrggggggggrgrgggggggggggggrgggggggrr'
16780     DATA 'rrggrgggggggrrgggrggggggrggrgggggggggggggrgggggggrr'
16790     DATA 'rrgggrrrrrrrggggggrrrrrrgggrgggggggggggggrgggggggrr'
16800     DATA 'rrgggggggggggggggggggggggggggggggggggggggggggggggrr'
16810     DATA 'rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr'
16820 :
16830   yinf1y%=inf1y%+4:
16840 :
16850   DIM abt_lfl%(0)
16860   abt_lot=RD_LOT(std_iattr,0)
16870     DATA ab_xt%-16,lys%,8,ab_yt%-lys%-8,0,0,cancel$,text+retr,'Super!'
16880 :
16890 : REMark  D'abord le titre qui est un objet d'une sous-fen‘tre d'info.
16900 :
16910   longueur_titre%=LEN(" Version "&version$)*6               : REMark on calcule la taille et la
16920   titre_x%=INT((ab_xt%-longueur_titre%)/2)  : REMark position x de cet objet
16930   abt_iot2=RD_IOT(1)
16940     DATA longueur_titre%,lys%,4,8,text,sp.winfg,0,0," Version "&version$
16950     DATA 52,24,ab_xt%-60,40,sprite,0,wolfsoft_sprite
16960   longueur_titre%=LEN(titre$)*6     : REMark on calcule la taille et la
16970   titre_x%=INT((ab_xt%-longueur_titre%)/2)  : REMark position x de cet objet
16980   abt_iot1=RD_IOT(0)
16990     DATA longueur_titre%,lys%,4,INT((lys%-10)/2),text,sp.titlefg,0,0,titre$
17000   abt_iwt=RD_IWT(2)               : REMark on veut 3 sous-fen. d'info
17010 REMark d'abord la fen‘tre contenant la barre entourant le titre
17020     DATA ab_xt%-8-(2*nbr_post%*xesp%),lys%+2,xesp%*nbr_post%+4,0  : REMark taille x,y, origines x,y
17030     DATA 0,0,sp.infwinbd,sp.titlebg     : REMark ombre, taille & couleur bordure
17040                                    : REMark puis couleur papier
17050     DATA 0                         : REMark pointeur vers objets
17060 REMark maintenant la fen‘tre qui contient le titre
17070     DATA longueur_titre%+8,lys%,titre_x%-2,2   : REMark on utilise titre_x% etc
17080     DATA 0,0, sp.infwinbd,sp.titletextbg
17090     DATA abt_iot1                 : REMark un objet, le titre
17100 REMark maintenant la fen‘tre de la bordure intƒrieure
17110     DATA ab_xt%-8,ab_yt%-inf1y%-2,4,inf1y%
17120     DATA 0,1,sp.infwinbd,sp.infwinbg
17130     DATA abt_iot2
17140   abt_defn=RD_WDEF
17150     DATA 2,1,sp.winbd,sp.winbg: REMark ombre, larg. & coul. bord., coul. papier
17160     DATA ab_xt%,ab_yt%,10,10 : REMark taille x,y et position pointeur
17170     DATA main_sprite,abt_lot,abt_iwt,0
17180 :
17190 end def about_make
17200 :
17210 DEFine PROCedure about (datet$)
17220   LOCal loop%,item%,dummy%,chan%
17230   DR_PULD abt_defn,-1,-1,abt_lfl%
17240   chan%=fopen('con')
17250   dr_iwdf#chan%,abt_defn,2
17260   wm_ink#chan%,sp.infwinfg
17270   wm_paper#chan%,sp.infwinbg
17280   at#chan%,4,2
17290   print#chan%,"Copyright (C) "
17300   at#chan%,5,2
17310   print#chan%, datet$
17320   at#chan%,6,2
17330   print#chan%,"Wolfgang H. Lenerz"
17340   close#chan%
17350   REPeat loop%
17360     RD_PTR abt_defn,item%,dummy%,dummy%,dummy%,dummy%,abt_lfl%
17370     SELect ON item%
17380             =0       : EXIT loop%
17390     END SELect
17400   END REPeat loop%
17410   DR_UNST abt_defn
17420 END DEFine about
17430 :
17440 def proc do_popup(string$)
17450 rem this pops up a small "hint" window showing the string$ passed
17460 rem as parameter.
17470 rem The string MUST be 1 line long and MUST fit
17480 rem inside the window (YOU must make sure of that!)
17490   do_popup2 string$,0
17500 end def do_popup
17510 :
17520 def proc popup_make
17530 rem make a small window defn
17540 local x%,y%,p_iot,popsub(0)
17550 restore 17550
17560   p_sprt=rd_sprt(0)                               : rem make an invisible pointer, all transparent
17570     data 1,2,0,0,4
17580     data '  '
17590   x%=14                                 : rem bogus window sizes
17600   y%=10
17610   popsub(0)=rd_appw                               : rem the info wdw
17620     DATA x%,y%,0,0
17630     DATA 0,0, sp.hintbd,sp.hintbg
17640     DATA 0,""
17650   popup_defn=RD_WDEF                              : rem the working defn
17660     DATA 0,1,sp.hintbd,sp.hintbg        : rem no shadow, border size & col, paper col
17670     DATA x%,y%,40,2
17680     DATA p_sprt,0,0,mk_awl(popsub)
17690 end def popup_make
17700 :
17710 def proc do_popup2(string$,main_defn)
17720 rem This pops up a small "hint" window showing the string$ passed
17730 rem as parameter.
17740 rem main_defn is the working defintion of the window over which the hint
17750 rem window is to hover. Position the pointer there where you need it before
17760 rem calling this.
17770 rem If the string is too long to fit into the window (which can only be as long
17780 rem as the outline of the working defn) then the string is split up at the
17790 rem first convenient space and put on a second line, and so on.
17800 rem If this would lead to the hint window being too high, only as many
17810 rem lines as possible will be displayed.
17820 rem !!!!!
17830 rem This presumes that a char is 10 pixels high & 6 pixels wide (standard ql 6x10 matrix)..
17840 rem !!!!!
17850 rem The string may contain the char chr$(10) - there will be a newline.
17860 rem
17870 rem note : do not try to pass a screen chaneel, open & close one here.
17880 rem
17890 local dummy%,lp%,address,con%,temp%,lastspace%,xs%,ys%,temp$
17900 local hint$(10),slen% ,maxlength%,popup_lfl%(0),popup_mfl%(0,0)
17910   if string$="":return                            : rem empty string, do nothing
17920   strip_spaces string$                         : rem no spaces at ends
17930   con%=fopen("con")
17940   if main_defn<>0
17950     xs%=peek_w (main_defn+32)
17960     ys%=peek_w(main_defn+34)            : rem get max sizes the hint wdw may have
17970   else
17980     scr_size#con%,xs%,ys%
17990   endif
18000   xs%=(xs%-4) div 6                               : rem max nbr of chars in 1 line
18010   ys%=(ys%-4) div 10                              : rem max nbr of lines in wdw
18020   if xs%=0 or ys%=0:close#con%:return   : rem nothing will really fit, just leave
18030   dim hint$(ys%,xs%)                              : rem array to contain string
18040   rem now parse the string, find newlines, make sure of string length
18050   dummy%=1                                        : rem start of current string section
18060   temp%=0                                         : rem line
18070   slen%=0                                         : rem current length of string
18080   for lp%=1 to len(string$)
18090     if string$(lp%)=chr$(10)
18100       hint$(temp%)=string$(dummy% to lp%-1): rem cut string here
18110       temp%=temp%+1
18120       if temp%=ys%:exit lp%             : rem max nbr of lines reached
18130       dummy%=lp%+1                      : rem this is where new start of string is
18140       lastspace%=lp%
18150     else
18160       if string$(lp%)=" "
18170           lastspace%=lp%                          : rem this is where last space is
18180       endif
18190     endif
18200     slen%=slen%+1                       : rem one more char
18210     if slen% > xs%
18220       hint$(temp%)=string$(dummy% to lastspace%-1) : rem too many chars in line
18230       slen%=0
18240       temp%=temp%+1                               : rem we need nother line
18250       if temp%=ys%:exit lp%             : rem max nbr of lines reached
18260       dummy%=lastspace%+1
18270     endif
18280   end for lp%
18290   if temp%<ys%
18300      hint$(temp%)=string$(dummy% to)       : rem add last part
18310      temp%=temp%+1
18320   endif
18330   maxlength%=0
18340   for lp%=0 to temp%
18350     temp$= hint$(lp%)
18360     strip_spaces temp$                         : rem use intermediary var, because of QLIB
18370     if maxlength%<len(temp$):maxlength%=len (temp$)
18380   end for lp%
18390   lp%=(maxlength%*6)+4                            : rem hint window length
18400   poke_w popup_defn+32,lp%              : rem set window xsize
18410   poke_w popup_defn+34,temp%*10+2       : rem
18420   address=peek_l(popup_defn+112)        : rem ptr to app sub wdw...
18430   address=peek_l(address)               : rem ...now
18440   poke_w address,lp%-2                            : rem set its length
18450   poke_w address+2,temp%*10             : rem set its height
18460   dr_puld popup_defn,-1,-1,popup_lfl%,popup_mfl%
18470   dr_awdf#con%,popup_defn,0             : rem pull wdw over info wdw
18480   wm_paper#con%,sp.hintbg
18490   wm_ink#con%,sp.hintfg
18500   for lp%=0 to dimn(hint$,1)
18510     if hint$(lp%)<>"":temp$=hint$(lp%):strip_spaces temp$:print#con%,temp$
18520   end for lp%
18530   temp%=0:slen%=0:xs%=0:ys%=0:dummy%=0
18540   RD_PTR popup_defn,temp%,dummy%,slen%,xs%,ys%,popup_lfl%,popup_mfl%: rem comes back immediatley
18550   RD_PTR popup_defn,temp%,dummy%,slen%,xs%,ys%,popup_lfl%,popup_mfl%
18560   close#con%
18570   dr_unst popup_defn
18580 end def do_popup2
18590 :
18600 DEFine FuNction RD_SPRT(drap%)
18610 REMark drap% determine si masque 0
18620   LOCal tmp_patt$(32,32)
18630   LOCal xs,ys,xo,yo, md, l
18640   READ ys,xs,xo,yo,md
18650   DIM tmp_patt$(ys,xs)
18660   FOR l=0 TO ys-1:READ tmp_patt$(l)
18670   l=ALCHP(SPRSP(xs,ys))
18680   SPSET l,xo,yo,md,tmp_patt$(0 TO ys-1,1 TO xs)
18690   IF drap%
18700     POKE_L l+16,espace_vide-(l+16)
18710   END IF
18720   RETurn l
18730 END DEFine RD_SPRT
18740 :
18750 DEFine FuNction RD_WDEF
18760 rem read window defn
18770   LOCal ldef%(3), lspr, lloose, linfo, lappl, lattr%(3)
18780   RD_WATT lattr%
18790   READ ldef%(0), ldef%(1), ldef%(2), ldef%(3)
18800   READ lspr, lloose, linf, lappl
18810   RETurn MK_WDEF (ldef%, lattr%, lspr, lloose, linf, lappl)
18820 END DEFine RD_WDEF
18830 :
18840 DEFine FuNction RD_LOT (lattr,nitem)
18850   LOCal count(3)
18860   LOCal item, ltyp, a$, lsk$
18870   LOCal ldef%(nitem,6), lptr(3,nitem), lstr$(nitem,85)
18880   lsk$=''
18890   FOR item = 0 TO nitem
18900     READ ldef%(item,0), ldef%(item,1), ldef%(item,2), ldef%(item,3)
18910     READ ldef%(item,4), ldef%(item,5)
18920     READ a$: lsk$=lsk$ & a$
18930     READ ltyp
18940     ldef%(item,6)=ltyp: ltyp=(ltyp MOD 256)/2
18950     IF ltyp>10 or ltyp <0:ltyp=0
18960     IF ltyp
18970       READ lptr(ltyp,count(ltyp))
18980     ELSE
18990       READ lstr$(count(0))
19000     END IF
19010     count(ltyp)=count(ltyp)+1
19020   END FOR item
19030   RETurn MK_LIL (lattr, ldef%(TO, 0 TO 1), ldef%(TO, 2 TO 3), ldef%(TO, 4 TO 5), lsk$, ldef%(TO, 6), lstr$, lptr(1), lptr(2), lptr(3))
19040 END DEFine RD_LOT
19050 :
19060 DEFine FuNction RD_IWT(nitem)
19070   LOCal item,mc1
19080   LOCal ldef%(nitem,3), latt%(nitem,3), lptr(nitem)
19090   FOR item = 0 TO nitem
19100     READ ldef%(item,0), ldef%(item,1), ldef%(item,2), ldef%(item,3)
19110     READ latt%(item,0), latt%(item,1), latt%(item,2), latt%(item,3)
19120     READ lptr(item)
19130   END FOR item
19140   RETurn MK_IWL (ldef%, latt%, lptr)
19150 END DEFine RD_IWT
19160 :
19170 DEFine FuNction RD_IOT(nitem)
19180   LOCal count(3)
19190   LOCal item, ltyp, work1, work2
19200   LOCal ldef%(nitem,4), lptr(3,nitem), lstr$(nitem,85)
19210   FOR item = 0 TO nitem
19220     READ ldef%(item,0), ldef%(item,1), ldef%(item,2), ldef%(item,3)
19230     READ ltyp
19240     ldef%(item,4)=ltyp:  ltyp=(ltyp MOD 256)/2
19250     IF ltyp >10:ltyp=0
19260     IF ltyp
19270       READ lptr(0,item),lptr(ltyp,count(ltyp))
19280     ELSE
19290       if mycolour_mode%=2
19300           read work1,work2,work3
19310           lptr(0,item)=WL_4_IOL(work1,work2,work3)
19320       else
19330           READ work1
19340           READ work2: work1=work1*256+work2
19350           READ work2: lptr(0,item)=work1*256+work2
19360       endif
19370       READ lstr$(count(0))
19380     END IF
19390     count(ltyp) = count(ltyp) + 1
19400   END FOR item
19410   RETurn MK_IOL (ldef%(TO, 0 TO 1), ldef%(TO, 2 TO 3), lptr(0), ldef%(TO, 4), lstr$, lptr(1), lptr(2), lptr(3))
19420 END DEFine RD_IOT
19430 :
19440 DEFine PROCedure RD_IATT (lattr)
19450   LOCal i
19460   READ lattr(0,0), lattr(0,1)
19470   FOR i=1 TO 3: READ lattr(i,0), lattr(i,1), lattr(i,2), lattr(i,3)
19480 END DEFine RD_IATT
19490 :
19500 DEFine PROCedure RD_WATT (lattr%)
19510   READ lattr%(0), lattr%(1),lattr%(2),lattr%(3)
19520 END DEFine RD_WATT
19530 :
19540 DEFine FuNction RD_APPW
19550   LOCal ldef%(3), lspr, sk$, lattr%(3)
19560   READ ldef%(0), ldef%(1), ldef%(2), ldef%(3)
19570   RD_WATT lattr%
19580   READ lspr, sk$
19590   RETurn MK_APPW (ldef%, lattr%, lspr,sk$)
19600 END DEFine
19610 :
