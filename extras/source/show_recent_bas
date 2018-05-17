10 rem $$asmb=dev1_booty_qptr_bin,0,172
20 rem $$asmb=dev1_booty_outptr_bin,0,10
30 rem $$asmb=DEV1_booty_blinker_bin,0,10
40 rem $$stak=2000
50 compiled=NOT (IS_OPEN(#0))
60 IF NOT compiled
70     IF NOT_SBASIC:PRINT "This only works in sbasic":stop
80     CLEAR:CLCHP
90     SCR_SIZE#0,maxx%,maxy%
100     maxy%=maxy%-28
110     OUTLN#0,maxx%,maxy%,0,28
120     WSET -1
130 ELSE
140     OPEN#1,"con"
150     SCR_SIZE#1,maxx%,maxy%
160 END IF
170 :
180 IF P_ENV(#1)<>2
190    CLS#1:INK#1,2:PRINT#1,"This software works only in the Pointer Environment"
200    a$=INKEY$(#1,-1)
210    STOP
220 END IF
230 :
240 IF NOT is_extn("RCNT_INFO")
250    CLS#1:INK#1,2:PRINT#1,"This software needs the Recent thing"
260    a$=INKEY$(#1,-1)
270    STOP
280 END IF
290 :
300 init
310 main
320 IF not compiled:CLCHP
330 STOP
340 :
350 define procedure main
360   local lp%,item%,event%,swnum%,xrel%,yrel%,timeout% , chosen$
370   local displaying_jobs%
380   displaying_jobs%=0
390   timeout%=-2
400   if compiled
410      open#1,"con"
420      scr_size#1,xsz%,ysz%
430      dr_ppos main_defn,-1,-1,main_lfl%,main_mfl%,main_cty%
440   else
450      scr_size#1,xsz%,ysz%
460      dr_puld main_defn,-1,-1,main_lfl%,main_mfl%,main_cty%
470   end if
480   repeat lp%
490     rd_ptrt main_defn,item%,swnum%,event%,timeout%,xrel%,yrel%,main_lfl%,main_mfl%,main_cty%
500     if swnum%= -1
510       select on item%
520           =-1                 : next lp%                    : rem do nothing on timeout
530           =esc_it%  : exit lp%                    : rem quit
540           =move_it%           : ch_win main_defn  : rem move
550           =sleep_it%          : button main_defn,main_lfl%,main_mfl%,main_cty%,titre$
560           =wake_it%           : refresh_list                : rem re-read list
570           =jobs_it%           : show_jobs
580           =info_it%           : show_info current_job$: rem show some info on currenly selected job
590       end select
600     else
610       if item%>0 and item%<dimn(appsub_data$,1)
620           if displaying_jobs%
630             if item%=dimn(appsub_data$,1)-1
640               current_job$=""
650             else
660               current_job$=appsub_data$(item%+1)
670             endif
680             displaying_jobs%=0
690             main_lfl%(info_it%)=1
700             refresh_list
710           else
720             chosen$=appsub_data$(item%+1)
730             hot_stuff chosen$
740           endif
750       end if
760       main_mfl%(0,0)=129                          : rem this always stays selected
770     end if
780   end repeat lp%
790   dr_unst main_defn
800   if compiled:close#1
810 end define main
820 :
830 def proc refresh_list
840   make_appsub_data current_job$
850   mk_main_app
860   dr_idrw main_defn,-5                        : rem redraw inner border wdw of main wdw
870   ap_in main_defn,app_addr+180
880   dr_ascr main_defn,app_addr
890 end def refresh_list
900 :
910 def proc show_info (jobnm$)
920 local lp%,item%,event%,swnum%,xrel%,yrel%,scr_chan%,size
930 local leng%,nbr%,size,max%,tit$
940   tit$="Info on : " & jobnm$
950   if jobnm$= main_sbasic$
960     size=rcnt_info(0,nbr%,leng%,max%)   : rem get info on job
970   else
980     if jobnm$<>""
990       size=RCNT_INFO(jobnm$,nbr%,leng%,max%) : rem get info on job
1000     else
1010       tit$="Info on : General list"
1020       size=rcnt_info(-3,nbr%,leng%,max%): rem info on general list
1030     endif
1040   endif
1050   dr_puld info_defn,-1,-1,info_lfl%
1060   scr_chan%=fopen("scr")
1070   dr_iwdf#scr_chan%,info_defn,2
1080   wm_paper#scr_chan%, sp.infwinbg
1090   wm_ink#scr_chan%,sp.infwinfg
1100   print#scr_chan%\\tit$
1110   print#scr_chan%\\"Size needed for GALL/GALJ       : ";size
1120   print#scr_chan%,"Number of files curently in list: ";nbr%
1130   print#scr_chan%,"Max current file name length    : ";leng%
1140   print#scr_chan%,"Max number of files in list     : ";max%
1150   event%=0
1160   rep lp%
1170     rd_ptr info_defn,item%,swnum%,event%,xrel%,yrel%,info_lfl%
1180     if swnum%=-1
1190       if item%=imove_it%
1200            ch_win info_defn
1210       else
1220           exit lp%
1230       endif
1240     endif
1250   end rep lp%
1260   dr_unst info_defn
1270   close#scr_chan%
1280 end def show_info
1290 :
1300 DEFine FuNction make_standard_app_sub (adresse,tableau$,nr_sects%,selk%,phys%,a_exclure$)
1310 rem make a typical wman appsub wdw, with strings in the items
1320 rem the parameters are: the address (0 at first)
1330 rem the string array, the number of sections
1340 rem a flag whether we want selection keystrokes (flag = 1) and whether hit=do (flag = flag +256)
1350 rem an integer array with x,y, sizes & origs
1360 rem a string with chars to be excluded from the selection keystrokes
1370 LOCal app_attributs%(3,1),temp%,bord%,pap%,ori_x%,ori_y%
1380 LOCal tail_x%,tail_y%,fleches%,bar%,bar_sect%,arrows%,selkey%
1390 :
1400   IF adresse:RECHP adresse              : rem if this is a second call, release old
1410   tail_x%=phys%(0)                      : rem x size  - just to make this clear
1420   tail_y%=phys%(1)                      : rem y "size"      we could use this directly
1430   ori_x%=phys%(2)                       : rem
1440   ori_y%=phys%(3)                       : rem origs x,y
1450 :
1460   adresse=AP_MAKE(selk%,tail_x%,tail_y%,ori_x%,ori_y%,nr_sects%,tableau$,a_exclure$)
1470   IF adresse<0: RETurn 0                : rem  error, return 0
1480 :
1490   RETurn adresse                        : rem return the address!
1500 END DEFine make_standard_app_sub
1510 :
1520 DEFine FuNction make_standard_app_sub2 (adresse,tableau$,nr_sects%,selk%,phys%)
1530 rem make a typical wman appsub wdw, with strings in the items
1540 rem the parameters are: the address (0 at first)
1550 rem the string array, the number of sections
1560 rem a flag whether we want selection keystrokes and whether hit=do
1570 rem an integer array with x,y, sizes & origs
1580 rem here there are NO strings to be excluded from selection keystrokes.
1590 rem THIS MUST ONLY BE USED IN SBASIC OR COMPILED BASIC
1600 LOCal app_attributs%(3,1),temp%,bord%,pap%,ori_x%,ori_y%
1610 LOCal tail_x%,tail_y%,fleches%,bar%,bar_sect%,arrows%,selkey%
1620 :
1630   IF adresse:RECHP adresse              : rem if this is a second call, release old
1640   tail_x%=phys%(0)                      : rem x size  - just to make this clear
1650   tail_y%=phys%(1)                      : rem y "size"      we could use this directly
1660   ori_x%=phys%(2)                       : rem
1670   ori_y%=phys%(3)                       : rem origs x,y
1680 :
1690   adresse=AP_MAKE2(selk%,tail_x%,tail_y%,ori_x%,ori_y%,nr_sects%,tableau$)
1700   IF adresse<0: RETurn 0                : rem  error, return 0
1710 :
1720   RETurn adresse                        : rem return the address!
1730 END DEFine make_standard_app_sub2
1740 :
1750 def fn warning_pos% (mess$,channel%,title$,pos)
1760 rem warning wdw with pointer positioning
1770   pos_ptr main_defn,pos,-1
1780   return warning%(mess$,channel%,title$)
1790 end def warning_pos%
1800 :
1810 define function warning%(mess$,channel%,title$)
1820 rem shows a warning message (mess$) in a small window. hit esc or ok
1830 rem to return; returns 0 if esc, 1 if ok  hit
1840 local item%,lp%,chan%,y%,swnum%,infot%
1850   if channel%
1860     chan%=channel%
1870   else
1880     chan%=fopen("con")
1890   endif
1900   ch_item warn_defn,-3,0,-1,'','Attention'
1910   ch_item warn_defn,-1,0,-1,cancel$,'ESC'
1920   ch_item warn_defn,-1,1,-1,'O','OK'
1930   warn_lfl%(0)=1:warn_lfl%(1)=1
1940   poke_w warn_defn+44,sp.errmg                           : rem border colour of wdw
1950   poke_w warn_defn+46,sp.errbg                           : rem paper colour of wdw
1960   poke_w warn_wdw_addr+14,sp.errbg             : rem paper of title info wdw
1970   obj_addr=peek_l(warn_wdw_addr+16)            : rem ptr to object list
1980   poke_w obj_addr+10,sp.errfg                            : rem info object text  colour
1990   dr_puld warn_defn,-1,-1
2000   dr_iwdf #chan%,warn_defn,0
2010   wm_paper#chan%,sp.errbg
2020   wm_ink#chan%,sp.errfg
2030   cls#chan%
2040   if title$<>"":print#chan%,title$;" :"
2050   print #chan%,mess$
2060   pos_ptr warn_defn,160*65536+10,-1
2070   repeat lp%
2080     rd_ptr warn_defn,item%,swnum%,y%,y%,y%,warn_lfl%
2090     if swnum%=-1:exit lp%
2100   end repeat lp%
2110   if not channel%:close#chan%
2120   dr_unst warn_defn
2130   return item%
2140 end define warning%
2150 :
2160 def proc info_pos (mess$,channel%,pos)
2170 rem warning wdw with positioning
2180   pos_ptr main_defn,pos,-1
2190   info mess$,channel%
2200 end def info_pos
2210 :
2220 define procedure info(mess$,channel%)
2230 rem shows window containing mess$
2240 local chan%,infot%,obj_addr
2250   if channel%
2260     chan%=channel%
2270   else
2280     chan%=fopen("con")
2290   endif
2300   ch_item warn_defn,-3,0,-1,"",'  Info.  '
2310   ch_item warn_defn,-1,0,-1,"",'Info'
2320   ch_item warn_defn,-1,1,-1,"",'Info'
2330   poke_w warn_defn+44,sp.infwinbg                 : rem paper colour of wdw
2340   poke_w warn_defn+46,sp.winbd                              : rem border around wdw
2350   poke_w warn_wdw_addr+14,sp.infwinbg             : rem paper of title info wdw
2360   obj_addr=peek_l(warn_wdw_addr+16)               : rem ptr to object list
2370   poke_w obj_addr+10,sp.infwinfg                  : rem info object text        colour
2380   dr_puld warn_defn,-1,-1
2390   pos_ptr warn_defn,90*65536+70,-1
2400   dr_iwdf #chan%,warn_defn,0
2410   wm_paper#chan%,sp.infwinbg
2420   wm_ink#chan%,sp.infwinfg
2430   cls#chan%
2440   print #chan%,mess$
2450   if not channel%:close#chan%
2460   info_is_pulled_down%=1
2470 end define info
2480 :
2490 def proc info_wait(mess$,channel%)
2500 rem shows info wdw and waits until user clicks
2510   info mess$,channel%
2520   info_wait_to_close
2530 end def info_wait
2540 :
2550 def proc info_wait_to_close
2560 local item%,lp%,chan%,y%,swnum%,infot%
2570   repeat lp%
2580     rd_ptr warn_defn,item%,swnum%,y%,y%,y%,warn_lfl%
2590     if swnum%=-1:exit lp%
2600   end repeat lp%
2610   dr_unst warn_defn
2620 end def info_wait_to_close
2630 :
2640 def proc info_pos_wait (mess$,channel%,pos)
2650 rem positions & shows info wdw and waits until user clicks
2660   info_pos mess$,channel%,pos
2670   info_wait_to_close
2680 end def info_pos_wait
2690 :
2700 def proc stop_info
2710   dr_unst warn_defn
2720   info_is_pulled_down%=0
2730 end def stop_info
2740 :
2750 DEFine PROCedure button (def_trav,menu_drap%,app_drap%,control%,chaine$,type_col%)
2760 REMark les parametres sont, dans l'ordre:
2770 REMark la dÉfinition de la fenëtre, suivie des tableaux pour
2780 REMark les postes du menu dÉliÉ, les postes de la sous-fenëtre d'appl.
2790 REMark et la dÉfinition de contròle, suivi du nom du bouton et
2800 REMark du type couleurs.
2810 REMark Il est supposÉ que vous avez ouvert une fenëtre #1 si
2820 REMark le programme est compilÉ
2830   DR_UNST def_trav
2840   IF compiled: CLOSE#1
2850   button_wait chaine$
2860 rem  BTN chaine$,type_col%
2870   IF compiled
2880     OPEN#1,"CON_"
2890     DR_PPOS def_trav,-1,-1,menu_drap%,app_drap%,control%
2900   ELSE
2910     DR_PULD def_trav,-1,-1,menu_drap%,app_drap%,control%
2920   END IF
2930 END DEFine button
2940 :
2950 DEFine PROCedure make_colours (colourway%)
2960 REMark this initialises the GLOBAL variables menu_pap, menu_brd, menu_inv,
2970 REMark menu_ink and menu_bar according to the colourway passed as parameter.
2980 REMark More importantly, this sets the sp.xxx GLOBAL variables.
2990 REMark The GLOBAl variable mycolour_mode% must have been set prior to calling
3000 REMark this function, preferrably via the function set_colours%.
3010 REMark Moreover the colours black, white,red, green,wg_stripes, etc...
3020 REMark must have been set prior to calling this procedure:
3030 REMark Preferrably, they were also set via the function set_colours%
3040 :
3050 rem this function needs the extension WL_MK16 (outptr_bin)
3060 :
3070 REMark the colourways% are:
3080 REMark 0= white/green
3090 REMark 1= black/red
3100 REMark 2= white/red
3110 REMark 3= black/green
3120 remark 4-7= use system palettes
3130 :
3140 rem the Global war mycolour_mode% is:
3150 rem 0 = we are in QL mode 4
3160 rem 1 = we are in PAL mode (256 colours)
3170 rem 2 - we are in 24 bit colour mode
3180 rem preferrably, this is set via the function set_colours%
3190 :
3200   if colourway%>7 or colourway%<0: colourway%=0: REMark must be between 0 & 7
3210 :
3220 rem first make sure that what we request is possible.
3230 rem colourways 4-7 are only possible if we are in 8 or 24 bit colour mode
3240 rem if it is requested in another mode, it is set to 0
3250 :
3260   if colourway%>3
3270      make_my_palette colourway%-4: rem set colours according to system palette
3280      return                         : rem premature exit
3290   endif
3300 :
3310 rem this now is only the old colourways
3320   select on mycolour_mode%
3330     =0                                  : rem QL 4 colours
3340       SELect ON colourway%
3350           =0:                           : remark white/green
3360                 menu_pap=white
3370                 menu_brd=green
3380                 menu_ink=black
3390                 menu_bar=wg_striped
3400                 menu_inv=red
3410           =1:                           : remark black/red
3420                 menu_pap=black
3430                 menu_brd=red
3440                 menu_ink=white
3450                 menu_bar=br_striped
3460                 menu_inv=green
3470           =2:                           : remark white/red
3480                 menu_pap=white
3490                 menu_brd=red
3500                 menu_ink=black
3510                 menu_bar=wr_striped
3520                 menu_inv=green
3530           =3:                           : remark black/green
3540                 menu_pap=black
3550                 menu_brd=green
3560                 menu_ink=white
3570                 menu_bar=bg_striped
3580                 menu_inv=red
3590       END SELect
3600     =1                                  : rem pal colours
3610       SELect ON colourway%
3620           =0:                           : remark white/green
3630                 menu_pap=white+256
3640                 menu_brd=green+256
3650                 menu_ink=black+256
3660                 menu_bar=wg_striped+256
3670                 menu_inv=red +256
3680           =1:                           : remark black/red
3690                 menu_pap=black+256
3700                 menu_brd=red  +256
3710                 menu_ink=white +256
3720                 menu_bar=br_striped +256
3730                 menu_inv=green +256
3740                 omenu_pap=black+256
3750           =2:                           : remark white/red
3760                 menu_pap=white +256
3770                 menu_brd=red   +256
3780                 menu_ink=black +256
3790                 menu_bar=wr_striped +256
3800                 menu_inv=green +256
3810           =3:                           : remark black/green
3820                 menu_pap=black +256
3830                 menu_brd=green +256
3840                 menu_ink=white +256
3850                 menu_bar=bg_striped +256
3860                 menu_inv=red   +256
3870       END SELect
3880     =2                                  : rem 24 bit colours
3890       SELect ON colourway%
3900           =0:                           : remark white/green
3910                 menu_pap=WL_MK16(white)
3920                 menu_brd=WL_MK16(green)
3930                 menu_ink=WL_MK16(black)
3940                 menu_bar=WL_MK16(wg_striped)
3950                 menu_inv=WL_MK16(red)
3960           =1:                           : remark black/red
3970                 menu_pap=WL_MK16(black)
3980                 menu_brd=WL_MK16(red)
3990                 menu_ink=WL_MK16(white)
4000                 menu_bar=WL_MK16(br_striped)
4010                 menu_inv=WL_MK16(green)
4020           =2:                           : remark white/red
4030                 menu_pap=WL_MK16(white)
4040                 menu_brd=WL_MK16(red)
4050                 menu_ink=WL_MK16(black)
4060                 menu_bar=WL_MK16(wr_striped)
4070                 menu_inv=WL_MK16(green)
4080           =3:                           : remark black/green
4090                 menu_pap=WL_MK16(black)
4100                 menu_brd=WL_MK16(green)
4110                 menu_ink=WL_MK16(white)
4120                 menu_bar=WL_MK16(bg_striped)
4130                 menu_inv=WL_MK16(red)
4140       END SELect
4150   end select
4160 :
4170   make_my_old_palette
4180 :
4190 END DEFine make_colours
4200 :
4210 def proc make_my_palette (cw%)
4220   sp.winbd            = hex('0200') : rem Window border
4230   sp.winbg            = hex('0201') : rem Window background
4240   sp.winfg            = hex('0202') : rem Window foreground
4250   sp.winmg            = hex('0203') : rem Window middleground
4260   sp.titlebg          = hex('0204') : rem Title background
4270   sp.titletextbg  = hex('0205') : rem Title text background
4280   sp.titlefg          = hex('0206') : rem Title foreground
4290   sp.litemhigh        = hex('0207') : rem Loose item highlight
4300   sp.litemavabg   = hex('0208') : rem Loose item available background
4310   sp.litemavafg   = hex('0209') : rem Loose item available foreground
4320   sp.litemselbg   = hex('020a') : rem Loose item selected background
4330   sp.litemselfg   = hex('020b') : rem Loose item selected foreground
4340   sp.litemunabg   = hex('020c') : rem Loose item unavailable background
4350   sp.litemunafg   = hex('020d') : rem Loose item unavailable foreground
4360   sp.infwinbd         = hex('020e') : rem Information window border
4370   sp.infwinbg         = hex('020f') : rem Information window background
4380   sp.infwinfg         = hex('0210') : rem Information window foreground
4390   sp.infwinmg         = hex('0211') : rem Information window middleground
4400   sp.subinfbd         = hex('0212') : rem Subsidiary information window border
4410   sp.subinfbg         = hex('0213') : rem Subsidiary information window background
4420   sp.subinffg         = hex('0214') : rem Subsidiary information window foreground
4430   sp.subinfmg         = hex('0215') : rem Subsidiary information window middleground
4440   sp.appbd            = hex('0216') : rem Application window border
4450   sp.appbg            = hex('0217') : rem Application window background
4460   sp.appfg            = hex('0218') : rem Application window foreground
4470   sp.appmg            = hex('0219') : rem Application window middleground
4480   sp.appihigh         = hex('021a') : rem Application window item highlight
4490   sp.appiavabg        = hex('021b') : rem Application window item available background
4500   sp.appiavafg        = hex('021c') : rem Application window item available foreground
4510   sp.appiselbg        = hex('021d') : rem Application window item selected background
4520   sp.appiselfg        = hex('021e') : rem Application window item selected foreground
4530   sp.appiunabg        = hex('021f') : rem Application window item unavailable background
4540   sp.appiunafg        = hex('0220') : rem Application window item unavailable foreground
4550   sp.scrbar           = hex('0221') : rem Pan/scroll bar
4560   sp.scrbarsec        = hex('0222') : rem Pan/scroll bar section
4570   sp.scrbararr        = hex('0223') : rem Pan/scroll bar arrow
4580   sp.buthigh          = hex('0224') : rem Button highlight
4590   sp.butbd            = hex('0225') : rem Button border
4600   sp.butbg            = hex('0226') : rem Button background
4610   sp.butfg            = hex('0227') : rem Button foreground
4620   sp.hintbd           = hex('0228') : rem Hint border
4630   sp.hintbg           = hex('0229') : rem Hint background
4640   sp.hintfg           = hex('022a') : rem Hint foreground
4650   sp.hintmg           = hex('022b') : rem Hint middleground
4660   sp.errbg            = hex('022c') : rem Error message background
4670   sp.errfg            = hex('022d') : rem Error message foreground
4680   sp.errmg            = hex('022e') : rem Error message middleground
4690   sp.shaded           = hex('022f') : rem sp.shaded area
4700   sp.3ddark           = hex('0230') : rem Dark 3D border shade
4710   sp.3dlight          = hex('0231') : rem Light 3D border shade
4720   sp.vertfill         = hex('0232') : rem Vertical area fill
4730   sp.subtitbg         = hex('0233')  : rem Subtitle background
4740   sp.subtittxtbg  = hex('0234')  : rem Subtitle text background
4750   sp.subtitfg         = hex('0235')  : rem Subtitle foreground
4760   sp.mindexbg         = hex('0236')  : rem Menu index background
4770   sp.mindexfg         = hex('0237')  : rem Menu index foreground
4780   sp.separator        = hex('0238')  : rem Seperator lines etc.
4790   if not (is_open(#0) or is_open(#1))
4800     open#1,'con'
4810     sp_jobpal -1,cw%
4820     close#1
4830   else
4840     sp_jobpal -1,cw%
4850   endif
4860 end def make_my_palette
4870 :
4880 def proc make_my_old_palette
4890 rem this translates the colours used in the wdws into 4 colour mode...
4900   sp.winbd          =         menu_ink   : rem Window border
4910   sp.winbg          =         menu_pap   : rem Window background
4920   sp.winfg          =         menu_pap   : rem Window foreground
4930   sp.winmg          =         menu_pap   : rem Window middleground
4940   sp.titlebg        =         menu_bar   : rem Title background
4950   sp.titletextbg=   menu_pap   : rem Title text background
4960   sp.titlefg        =         menu_ink   : rem Title foreground
4970   sp.litemhigh      =         menu_brd   : rem Loose item highlight
4980   sp.litemavabg =   menu_pap   : rem Loose item available background
4990   sp.litemavafg =   menu_ink   : rem Loex fifiose item available foreground
5000   sp.litemselbg =   menu_brd   : rem Loose item selected background
5010   sp.litemselfg =   menu_ink   : rem Loose item selected foreground
5020   sp.litemunabg =   menu_pap   : rem Loose item unavailable background
5030   sp.litemunafg =   menu_brd   : rem Loose item unavailable foreground
5040   sp.infwinbd       =         menu_brd   : rem Information window border
5050   sp.infwinbg       =         menu_pap   : rem Information window background
5060   sp.infwinfg       =         menu_ink   : rem Information window foreground
5070   sp.infwinmg       =         menu_pap   : rem Information window foreground
5080   sp.subinfbd       =         menu_brd   : rem Information window border (sub)
5090   sp.subinfbg       =         menu_pap   : rem Information window background
5100   sp.subinffg       =         menu_ink   : rem Information window foreground
5110   sp.subinfmg       =         menu_pap   : rem Information window foreground
5120   sp.appbd          =         menu_brd   : rem Application window border
5130   sp.appbg          =         menu_pap   : rem Application window background
5140   sp.appfg          =         menu_ink   : rem Application window foreground
5150   sp.appmg          =         menu_pap   : rem Application window middleground
5160   sp.appihigh       =         menu_ink   : rem Application window item highlight
5170   sp.appiavabg      =         menu_pap   : rem Application window item available background
5180   sp.appiavafg      =         menu_ink   : rem Application window item available foreground
5190   sp.appiselbg      =         menu_brd   : rem Application window item selected background
5200   sp.appiselfg      =         menu_ink   : rem Application window item selected foreground
5210   sp.appiunabg      =         menu_pap   : rem Application window item unavailable background
5220   sp.appiunafg      =         menu_brd   : rem Application window item unavailable foreground
5230   sp.scrbar         =         menu_ink   : rem Pan/scroll bar
5240   sp.scrbarsec      =         menu_pap   : rem bar section
5250   sp.scrbararr      =         menu_brd   : rem Pan/scroll bar arrow
5260   sp.buthigh        =         menu_ink   : rem Button highlight
5270   sp.butbd          =         menu_brd   : rem Button border
5280   sp.butbg          =         menu_pap   : rem Button background
5290   sp.butfg          =         menu_ink   : rem Button foreground
5300   sp.hintbd         =         menu_brd   : rem Hint border
5310   sp.hintbg         =         menu_ink   : rem Hint background
5320   sp.hintfg         =         menu_pap   : rem Hint foreground
5330   sp.hintmg         =         menu_ink   : rem Hint middleground
5340   sp.errbg          =         menu_inv   : rem Error message background
5350   sp.errfg          =         menu_ink   : rem Error message foreground
5360   sp.errmg          =         menu_ink   : rem Error message middleground
5370   sp.shaded         =         0            : rem sp.shaded area
5380   sp.3ddark         =         0            : rem Dark 3D border shade
5390   sp.3dlight        =         white        : rem Light 3D border shade
5400   sp.vertfill       =         menu_pap   : rem Vertical area fill
5410   sp.subtitbg       =         menu_pap   : rem Subtitle background
5420   sp.subtittxtbg=   menu_pap   : rem Subtitle text background
5430   sp.subtitfg       =         menu_ink   : rem Subtitle foreground
5440   sp.mindexbg       =         menu_pap   : rem Menu index background
5450   sp.mindexfg       =         menu_brd   : rem same,        foreground
5460   sp.separator      =         menu_brd   : rem Seperator lines etc.
5470 end def make_my_old_palette
5480 :
5490 DEFine FuNction centr$(ctr$,leng)
5500 rem this "centres" the string ctr$ (i.e. left padds it with spaces) so that
5510 rem it is centred in a window of leng width (leng is length in chars)
5520   LOCal l%,cta$
5530   cta$=strip_spaces$(ctr$)
5540   l%=INT(leng-LEN(cta$))/2
5550   if l%<0:return cta$
5560   return FILL$(' ',l%)&cta$&FILL$(' ',l%)
5570 END DEFine centr$
5580 :
5590 define fn menus_init_menu (title$,array_with_prompts$,selkeys$)
5600 rem makes a menu window with menu items in 1 column
5610   return menus_init_menu_cols (title$,array_with_prompts$,selkeys$,1)
5620 end def
5630 :
5640 def fn menus_init_menu_cols (title$,array_with_prompts$,selkeys$,columns%)
5650 rem makes a menu window with menu items
5660 rem this presupposes that the colours have been set
5670 rem this prosupposes that a variable called "std_iattr" exists
5680 rem this presupposes that the variables defining the item types and event codes have been set
5690 rem this presupposes that this entire options window WILL FIT into the main
5700 rem outline. YOU must make sure of that.
5710 rem
5720 rem parameters:
5730 rem the title of the window
5740 rem the string array containing the items
5750 rem the selkeys for the items (a string of length dimn(array,1)+1)
5760 rem nbr of columns, 1 or 2
5770 rem
5780 rem returns the wdw definition, or -1 if error
5790 local temp1%,temp2%,temp3%,temp4,lp%,temp%
5800 local lxs%,lys%,xesp%,inf1y%,nbr_post%,yinf1y%
5810 local wxsize%,wysize%,wspcg%
5820 local opts_iot1,opts_iwt,opts_lot
5830 local longueur_titre%,titre_x%,opts_defn
5840 local oszsiz%(0,0),oszorgs%(0,0),oszjus%(0,0),sk$
5850 local oszblb(1),oszstr$(0),osztyp%(0),opts_lit%
5860 :
5870 rem now make window(s) ---------------------------------
5880 :
5890   if columns%<1 or columns%>2:return -1
5900   temp1%=len (selkeys$)-1     : rem number of "rows" -1
5910   if dimn(array_with_prompts$,1) <>temp1%
5920     return -1
5930   endif
5940   wxsize%=dimn(array_with_prompts$,2)+2
5950   wspcg%=wxsize%*6  +4
5960   wxsize%=wxsize%*6  +20      : rem sizes for strings + spacing
5970   if columns%=2:wxsize%=wxsize%+wspcg%
5980 :
5990   lxs%=24:lys%=12             : rem standard item sizes
6000   inf1y%=lys%+4               : rem y orig of inner wdw
6010   xesp%=lxs%+2
6020   nbr_post%=1                           : rem 1 item in each corner (2 x esc)
6030 :
6040   if columns%=1
6050     wysize%=inf1y%+4+(temp1%+1)*12+8
6060   else
6070     wysize%=inf1y%+4+(temp1%+1)*6+8
6080     if (temp1%+1) && 1:wysize%=wysize%+12
6090   endif
6100 :
6110   longueur_titre%=len(title$)*6 : rem title object x size
6120   temp2%=longueur_titre% + xesp%*nbr_post%*2 + 8 + 36 : rem min xsize of wdw
6130   if temp2%>wxsize% : wxsize%= temp2% +20
6140   titre_x%=(wxsize%-longueur_titre%)/2   : rem title x position
6150 :
6160 : rem  loose menu items. Here there are 1 standard one (esc) + 1 per menu choice
6170   yinf1y%=inf1y%+4
6180 :
6190   opts_lit%=temp1%+1:dim opts_lfl%(opts_lit%)
6200 :
6210   temp2%= dimn(array_with_prompts$,2)
6220   if temp2%<3:temp2%=3
6230   dim oszstr$(opts_lit%,temp2%) : rem strings at least 3 chars wide for "esc".
6240   dim oszblb(opts_lit%)
6250   dim oszjus%(opts_lit%,1)
6260   dim oszorg%(opts_lit%,1)
6270   dim osztyp%(opts_lit%)
6280   dim oszsiz%(opts_lit%,1)
6290   sk$=cancel$
6300 :
6310   oszstr$(0)="ESC"
6320   oszsiz%(0,0)=lxs%
6330   oszsiz%(0,1)=lys%
6340   oszorg%(0,0)=4
6350   oszorg%(0,1)=2
6360   osztyp%(0)=0+retr
6370 :
6380   temp3%=dimn(array_with_prompts$,2)*6+8 : rem x size
6390   temp%=yinf1y%
6400 :
6410   for lp%=1 to temp1%+1
6420     oszstr$(lp%)=array_with_prompts$(lp%-1)
6430     oszsiz%(lp%,0)=temp3%
6440     oszsiz%(lp%,1)=lys%
6450     if columns%=1
6460       oszorg%(lp%,0)=8
6470       oszorg%(lp%,1)=temp%
6480       temp%=temp%+lys%
6490     else
6500       if lp%&&1
6510           oszorg%(lp%,0)=8
6520           oszorg%(lp%,1)=temp%
6530       else
6540           oszorg%(lp%,0)=8 +wspcg%
6550           oszorg%(lp%,1)=temp%
6560           temp%=temp%+lys%
6570       endif
6580     endif
6590     oszjus%(lp%,0)=1                          : rem justif left
6600     temp2%=selkeys$(lp%) instr array_with_prompts$(lp%-1)
6610     if temp2%<> 0
6620        osztyp%(lp%)=retr + 256-(2*temp2%)
6630     else
6640        osztyp%(lp%)=retr
6650     endif
6660     sk$=sk$&selkeys$(lp%)
6670   end for lp%
6680   opts_lot=mk_lil(std_iattr,oszsiz%,oszorg%,oszjus%,sk$,osztyp%,oszstr$,oszblb,oszblb,oszblb)
6690 :
6700 restore 6700
6710 :
6720   rem now the info objects
6730 :
6740   rem window title
6750   opts_iot1=rd_iot(0)
6760     data longueur_titre%,10,4,int((lys%-10)/2),text,sp.titlefg,0,0,title$
6770 :
6780   remark now the info wdws
6790 :
6800   opts_iwt=rd_iwt(2)                              : remark there are 3
6810 :
6820 remark first the title bar
6830 :
6840     data wxsize%-8-(2*nbr_post%*xesp%),lys%+3,xesp%*nbr_post%+4,0       : remark x,y sizes & origs
6850     data 0,0,sp.infwinbd,sp.titlebg     : remark shadow, border size & col,paper col
6860     data 0                                        : remark object pointer (none)
6870 :
6880 remark now wdw containing title itself
6890 :
6900     data longueur_titre%+8,lys%+1,titre_x%-4,2
6910     data 0,0,sp.infwindbd,sp.infwinbg
6920     data opts_iot1                      : remark one object
6930 :
6940 remark inner border
6950 :
6960     data wxsize%-8,wysize%-inf1y%-2,4,inf1y%
6970     data 0,1,sp.infwinbd,sp.infwinbg
6980     data 0
6990 :
7000 : rem  the working definition
7010 :
7020   opts_defn=rd_wdef
7030     data 2,1,sp.winbd,sp.winbg: rem ombre, larg. & coul. bord., coul. papier
7040     data wxsize%,wysize%,wxsize%/2,wysize%/2 : rem taille x,y et position pointeur
7050     data main_sprite,opts_lot,opts_iwt,0
7060 :
7070   return opts_defn
7080 end define menus_init_menu
7090 :
7100 def fn menus_get_choice%(opts_defn,opts_values$)
7110 local lil_fl%(0),temp1%,item%,swnum%,event%,xrel%,yrel%
7120   temp1%=dimn (opts_values$,1)
7130   dim lil_fl%(temp1%+1)
7140   dr_puld opts_defn,-1,-1,lil_fl%
7150   rd_ptr opts_defn,item%,swnum%,event%,xrel%,yrel%,lil_fl%
7160   dr_unst opts_defn
7170   return item%-1
7180 end def menus_get_choice%
7190 :
7200 DEFine PROCedure scroll_arrows(rangees%,nbr_objets%,appfen,cty%)
7210 rem Cette procÉdure dÉcide s'il faut laisser les flêches de dÉfilement
7220 rem params: le nombre de rangÉes, le nombre total d'objets
7230 rem le pointer vers la sous-fen d'app et la dÉfinition de controle
7240   IF nbr_objets%<=rangees%
7250     POKE_W appfen+150,0       : rem no y offset to start of menu
7260     cty%(1,2)=nbr_objets%     : rem nbr of columns/rows
7270   ELSE
7280     POKE_W appfen+150,6
7290     cty%(1,2)=rangees%-1      : rem if scroll arrows are there, there is one line less in the window
7300   END IF
7310   IF DIMN(cty%,1)=2:cty%(2,2)=cty%(1,2)
7320   cty%(0,1)=1                            : rem show that definition has changed
7330 END DEFine scroll_arrows
7340 :
7350 def fn set_colours% (force_mode%)
7360 rem this sets colour variables according to the colour mode wished, if possible.
7370 rem force_mode%=
7380 rem       0 if ql colours wished (mode 4 only)
7390 rem       1 if pal colours wished
7400 rem       2 if 24 bit colours wished
7410 rem This function returns the true QL mode set (4,8,32,33 etc.).
7420 rem This function also sets the GLOBAL variable mycolour_mode% to the
7430 rem mode 0,1,2 as above - whatever is possible on the machine, i.e.
7440 rem if colour_24 is wished on a 4 colour machine (which won't succeed)
7450 rem this function will set set that variable to 0, i.e. QL mode.
7460 rem
7470 rem This function needs the outptr toolkit
7480 local mode%
7490   mode%=rmode                                     : rem get current mode now
7500   select on force_mode%
7510       = 0                                         : rem mode 4 wished
7520        select on mode%
7530             = 4                                   : rem already on, so do nothing
7540             = 0,1,8 : mode 4            : rem old modes, set to 4 colours
7550             = remainder
7560               colour_ql                           : rem if higher colour mode, set to "mode 4"
7570               mode 4
7580        end select
7590        mode%=0                                    : rem new mode we're in  - 4 colour mode
7600       = 1                                         : rem PALette wished
7610        select on mode%
7620             = 16,32,33 : colour_pal:mode%=1  : rem we're in high colour mode, but PAL colours wished
7630             = remainder
7640               if mode%<>4:mode 4
7650               mode%=0
7660        end select
7670       = 2
7680        select on mode%
7690             = 32,33 : colour_24 :mode%=2          : rem we're in high col mode 24 bit colours
7700             = 16 : colour_pal : mode%=1
7710             = remainder
7720               if mode%<>4:mode 4
7730               mode%=0
7740        end select
7750   end select
7760   mycolour_mode%=mode%                            : rem set this global variable now:
7770   rem now set some colour variables
7780   select on mode%
7790        = 0                                        : rem Ql 4 mode colours
7800           Black            = 0
7810           White            = 7
7820           Red              = 2
7830           Green            = 4
7840           grey             = 255
7850           dull_pink      = 19
7860           Blue             = 0
7870           Magenta        = 2
7880           Yellow           = 7
7890           Cyan             = 0
7900           Dark_slate     = 0
7910           Slate_grey     = 255
7920           Dark_grey      = 255
7930           Light_grey     = 255
7940           Ash_grey       = 255
7950           Dark_red       = 210
7960           Light_green    = 220
7970           Mustard        = 242
7980           Dark_green     = 228
7990           Sea_blue       = 0
8000           Dark_blue      = 0
8010           Purple           = 0
8020           Shocking_pink  = 234
8030           Orange           = 234
8040           Lime_green     = 4
8050           Apple_green    = 4
8060           Bright_blue    = 0
8070           Mauve            = 0
8080           Peach            = 7
8090           Light_yellow   = 7
8100           Light_blue     = 0
8110           Sky_blue       = 0
8120           Rose_pink      = 234
8130           Pink             = 234
8140           Beige            = 7
8150           Pastel_pink    = 234
8160           Pastel_yellow  = 7
8170           Pastel_green   = 4
8180           Pastel_cyan    = 0
8190           Pastel_blue    = 0
8200           Pastel_rose    = 234
8210           Brick            = 210
8220           Light_khaki    = 7
8230           Dull_green     = 228
8240           Dull_cyan      = 0
8250           Steel_blue     = 0
8260           Dull_pink      = 234
8270           Brown            = 0
8280           Khaki            = 7
8290           Dusky_green    = 4
8300           Dusky_blue     = 0
8310           Midnight_blue  = 0
8320           Plum             = 0
8330           Dusky_pink     = 234
8340           Buff             = 0
8350           Avocado        = 4
8360           Dull_turquoise = 4
8370           Dull_blue      = 0
8380           Faded_purple   = 234
8390           Cerise           = 2
8400           Tan_normal     = 0
8410           Grass_green    = 4
8420           Sea_green      = 4
8430           Ultramarine    = 0
8440           Deep_purple    = 234
8450           Light_orange   = 7
8460           light_red      = 2
8470           wg_striped     = 92
8480           br_striped     = 82
8490           wr_striped     = 107
8500           bg_striped     = 100
8510        = 1                              : rem Colour_pal
8520           Black            = 0
8530           White            = 1
8540           Red              = 2
8550           Green            = 3
8560           Blue             = 4
8570           Magenta        = 5
8580           Yellow           = 6
8590           Cyan             = 7
8600           Dark_slate     = 8
8610           Slate_grey     = 9
8620           Dark_grey      = 10
8630           Grey             = 11
8640           Light_grey     = 12
8650           Ash_grey       = 13
8660           Dark_red       = 14
8670           Light_green    = 15
8680           Mustard        = 16
8690           Dark_green     = 17
8700           Sea_blue       = 18
8710           Dark_blue      = 19
8720           Purple           = 20
8730           Shocking_pink  = 21
8740           Orange           = 22
8750           Lime_green     = 23
8760           Apple_green    = 24
8770           Bright_blue    = 25
8780           Mauve            = 26
8790           Peach            = 27
8800           Light_yellow   = 28
8810           Light_blue     = 29
8820           Sky_blue       = 30
8830           Rose_pink      = 31
8840           Pink             = 32
8850           Beige            = 33
8860           Pastel_pink    = 34
8870           Pastel_yellow  = 35
8880           Pastel_green   = 36
8890           Pastel_cyan    = 37
8900           Pastel_blue    = 38
8910           Pastel_rose    = 39
8920           Brick            = 40
8930           Light_khaki    = 41
8940           Dull_green     = 42
8950           Dull_cyan      = 43
8960           Steel_blue     = 44
8970           Dull_pink      = 45
8980           Brown            = 46
8990           Khaki            = 47
9000           Dusky_green    = 48
9010           Dusky_blue     = 49
9020           Midnight_blue  = 50
9030           Plum             = 51
9040           Dusky_pink     = 52
9050           Buff             = 53
9060           Avocado        = 54
9070           Dull_turquoise = 55
9080           Dull_blue      = 56
9090           Faded_purple   = 57
9100           Cerise           = 58
9110           Tan_normal     = 59
9120           Grass_green    = 60
9130           Sea_green      = 61
9140           Ultramarine    = 62
9150           Deep_purple    = 63
9160           pinkish        = 77
9170           light_red      = 109
9180           Light_orange   = 208
9190           wg_striped     = light_green
9200           br_striped     = dark_red
9210           wr_striped     = light_red
9220           bg_striped     = dark_green
9230        = 2                              : rem colour_24
9240           Black            = hex('000000')
9250           White            = hex('FFFFFF')
9260           Red              = hex('FF0000')
9270           Green            = hex('00FF00')
9280           Blue             = hex('0000FF')
9290           Magenta        = hex('FF00FF')
9300           Yellow           = hex('FFFF00')
9310           Cyan             = hex('00FFFF')
9320           Dark_slate     = hex('242424')
9330           Slate_grey     = hex('494949')
9340           Dark_grey      = hex('6D6D6D')
9350           Grey             = hex('929292')
9360           Light_grey     = hex('B6B6B6')
9370           Ash_grey       = hex('DBDBDB')
9380           Dark_red       = hex('920000')
9390           Light_green    = hex('B6FFB6')
9400           Mustard        = hex('929200')
9410           Dark_green     = hex('009200')
9420           Sea_blue       = hex('009292')
9430           Dark_blue      = hex('000092')
9440           Purple           = hex('920092')
9450           Shocking_pink  = hex('FF0092')
9460           Orange           = hex('FF9200')
9470           Lime_green     = hex('92FF00')
9480           Apple_green    = hex('00FF92')
9490           Bright_blue    = hex('0092FF')
9500           Mauve            = hex('9200FF')
9510           Peach            = hex('FFB6B6')
9520           Light_yellow   = hex('FFFFB6')
9530           Light_blue     = hex('B6FFFF')
9540           Sky_blue       = hex('B6B6FF')
9550           Rose_pink      = hex('FFB6FF')
9560           Pink             = hex('FFB6DB')
9570           Beige            = hex('FFDBB6')
9580           Pastel_pink    = hex('FFDBDB')
9590           Pastel_yellow  = hex('FFFFDB')
9600           Pastel_green   = hex('DBFFDB')
9610           Pastel_cyan    = hex('DBFFFF')
9620           Pastel_blue    = hex('DBDBFF')
9630           Pastel_rose    = hex('FFDBFF')
9640           Brick            = hex('B66D6D')
9650           Light_khaki    = hex('B6B66D')
9660           Dull_green     = hex('6DB66D')
9670           Dull_cyan      = hex('6DB6B6')
9680           Steel_blue     = hex('6D6DB6')
9690           Dull_pink      = hex('B66DB6')
9700           Brown            = hex('6D2424')
9710           Khaki            = hex('6D6D24')
9720           Dusky_green    = hex('246D24')
9730           Dusky_blue     = hex('246D6D')
9740           Midnight_blue  = hex('24246D')
9750           Plum             = hex('6D246D')
9760           Dusky_pink     = hex('B64992')
9770           Buff             = hex('B69249')
9780           Avocado        = hex('92B649')
9790           Dull_turquoise = hex('49B692')
9800           Dull_blue      = hex('4992B6')
9810           Faded_purple   = hex('9249B6')
9820           Cerise           = hex('920049')
9830           Tan_normal     = hex('924900')
9840           Grass_green    = hex('499200')
9850           Sea_green      = hex('009249')
9860           Ultramarine    = hex('004992')
9870           Deep_purple    = hex('490092')
9880           Light_orange   = hex('ffb624')
9890           pinkish        = hex('C0037B')
9900           Light_red      = hex('FC1729')
9910           wg_striped     = light_green
9920           br_striped     = dark_red
9930           wr_striped     = light_red
9940           bg_striped     = dark_green
9950   end select
9960   mode%=rmode
9970   ret mode%                             : rem return current screen mode now
9980 end def set_colours%
9990 :
10000 DEFine PROCedure init
10010 rem this initialises the following global variables:
10020 rem all the windows (_defn,_lfl%,_cty%,mfl% etc...)
10030 rem all the sprites
10040 rem item types (text, sprite) , event codes (hit$, do$)
10050 rem v. 1.04
10060 local temp1%,temp2%,temp3%,nbr_post%,temp4%
10070 local main_iot1,main_iot2,main_iwt,main_lot,main_lit%
10080 local longueur_titre%,titre_x%,tt%
10090 :
10100   text=0:sprite=2:blob=4:pattern=6
10110   retr=256:retn=-256:sous1=254:sous2=252
10120 :
10130 : rem events
10140   hit$=CHR$(1):do$=CHR$(2):cancel$=CHR$(3):sleep$=CHR$(7):wake$=CHR$(8)
10150   help$=CHR$(4):move$=CHR$(5):size$=CHR$(6):cr$=CHR$(10)
10160 :
10170   current_job$=""             : rem job currently displayed
10180   main_sbasic$="Main SBasic"  : rem name for job 0
10190   show_jobs$="xxxMake Jobsxxx"          : rem name when we want to show jobs info
10200 :
10210   main_coul%=4      : rem make colours
10220   app_coul%=0
10230   btn_coul%=0:      rem =LNKCOL(3)
10240   temp1%=set_colours% (2)                : rem always try to set highest colours
10250   rem if mycolour_mode%<2 and main_coul%>3:main_coul%=0
10260   rem if mycolour_mode%<2 and app_coul%>3:app_coul%=0
10270   rem if mycolour_mode%<2 and btn_coul%>3:btn_coul%=0
10280   make_colours main_coul%               : rem make colours now
10290 :
10300   titre$="Recent Files"
10310 :
10320 rem now make window(s) ---------------------------------
10330 :
10340   xsize%=272                            : rem x size, should be multiple of 4
10350   ysize%=-1                             : rem y size of wdw : -1 means it's calculated...
10360                                         : rem with respect to the number of rows in appsub wdw
10370   make_appsub_data current_job$ : rem data to fill appsub wdw with
10380   rangees%=dimn(appsub_data$,1) : rem GLOBAL var: nbr of visible rows in appsub
10390   if rangees%<8:rangees%=8
10400 :
10410   lxs%=24:lys%=12             : rem tailles x et y pour postes standard
10420   inf1y%=lys%+4               : rem posi. y de la fen. d'inf. intÉrieure
10430   xesp%=lxs%+2
10440   nbr_post%=3
10450 :
10460 : rem now calculate the final y size. Even if a real y size is given, this may
10470 : rem change as the y size is calculated so that the rows fit neatly inside the
10480 : rem appsub window.
10490 : rem maxx% and maxy% are the maximum sizes the wdw may have (see start_bas)
10500 : rem if both ysize% and rangees%=-1: make wdw as high as screen allows
10510 :
10520   appwinoffset%=inf1y%                  : rem y offset for appsub win within wdw
10530   if rangees%= -1
10540     if ysize%= -1
10550       rangees%=(maxy%-14-appwinoffset%) div 12 : rem max nbr of rows
10560     endif
10570   endif
10580   if ysize%= -1
10590     ysize%=rangees%*12+appwinoffset%+4
10600   else
10610     ysize%=ysize%-appwinoffset%-4
10620     ysize%=(ysize% div 12) *12 + appwinoffset%+4
10630   endif
10640   if ysize%>=maxy%-14
10650     ysize%=maxy%-appwinoffset%-10
10660     rangees%=ysize% div 12
10670     ysize%=rangees%*12+appwinoffset% +4
10680   endif
10690 :
10700   make_standard_sprites       : rem leave this before the restore
10710 :
10720 restore 10720
10730 :
10740   DIM std_iattr(3,3)
10750   RD_IATT std_iattr
10760     DATA 1,sp.litemhigh : REMark size & colour current item border
10770     DATA sp.litemunabg,sp.litemunafg,0,0 : rem unavailable item paper & ink
10780     DATA sp.litemavabg,sp.litemavafg,0,0 : REMark available
10790     DATA sp.litemselbg,sp.litemselfg,0,0 : REMark selected
10800 :
10810   longueur_titre%=LEN(titre$)*6 : rem on calcule la taille et la
10820   titre_x%=(xsize%-longueur_titre%)/2 : rem position x de cet objet
10830 :
10840 : rem  Loose items, the first 4 are standard
10850   esc_it%=0:move_it%=esc_it%+1
10860   sleep_it%=move_it%+1
10870   wake_it%=sleep_it%+1
10880   jobs_it%=wake_it%+1
10890   info_it%=jobs_it%+1
10900 :
10910   main_lit%=5:DIM main_lfl%(main_lit%)
10920   main_lot=RD_LOT(std_iattr,main_lit%)
10930     DATA lxs%,lys%,4+xesp%*0,2,0,0,cancel$,text+retr,'ESC'
10940     DATA lxs%,lys%,4+xesp%*1,2,0,0,move$,sprite+retr,move_sprite
10950     DATA lxs%,lys%,xsize%-2-xesp%*1,2,0,0,sleep$,sprite+retr,sleep_sprite
10960     DATA lxs%,lys%,xsize%-2-xesp%*2,2,0,0,wake$,sprite+retr,wake_sprite
10970     DATA lxs%,lys%,xsize%-2-xesp%*3,2,0,0,"J",sous1+retr,"Jobs"
10980     DATA lxs%,lys%,4+xesp%*2,2,0,0,"I",sous1+retr,"Info"
10990 :
11000   main_iot1=RD_IOT(0)
11010     DATA longueur_titre%,10,4,INT((lys%-10)/2)+1,text,sp.titlefg,0,0,titre$
11020 :
11030   REMark now the info wdws
11040 :
11050   main_iwt=RD_IWT(2)                              : REMark there are 3
11060 :
11070 REMark first the title bar
11080 :
11090     DATA xsize%-8-(2*nbr_post%*xesp%),lys%+3,xesp%*nbr_post%+4,0       : REMark x,y sizes & origs
11100     DATA 0,0,sp.infwinbd,sp.titlebg     : REMark shadow, border size & col,paper col
11110     DATA 0                                        : REMark object pointer (none)
11120 :
11130 REMark now wdw containing title itself
11140 :
11150     DATA longueur_titre%+8,lys%+1,titre_x%-4,2
11160     DATA 0,0,sp.infwindbd,sp.infwinbg
11170     DATA main_iot1                      : REMark one object
11180 :
11190 REMark inner border
11200 :
11210     DATA xsize%-8,ysize%-inf1y%-2,4,inf1y%
11220     DATA 0,1,sp.infwinbd,sp.infwinbg
11230     DATA 0
11240 :
11250 rem now application subwindow
11260 :
11270   main_app_list=0
11280   app_addr=0                                      : REMark there is no appsub wdw yet
11290   mk_main_app
11300   if app_addr<>0:main_app_list = app_addr+180 : REMark list built by OUTPTR
11310 :
11320 : rem  window definition
11330 :
11340   main_defn=RD_WDEF
11350     DATA 2,1,sp.winbd,sp.winbg: rem ombre, larg. & coul. bord., coul. papier
11360     DATA xsize%,ysize%,10,ysize%-6 : rem taille x,y et position pointeur
11370     DATA main_sprite,main_lot,main_iwt,main_app_list
11380 :
11390   warn_make         :         rem make warning definition
11400   init_info_wdw
11410 END DEFine init
11420 :
11430 DEFine PROCedure init_info_wdw
11440 rem make info wdw defn
11450 local info_iot1,info_iot2,info_iwt,info_lot,info_lit%
11460 local longueur_titre%,titre_x%,titre$
11470 local ixsize%,nbr_post%
11480 :
11490   ixsize%=xsize%-16                      : rem x size, should be multiple of 4
11500   iysize%=100                            : rem y size of wdw
11510   nbr_post%=1
11520   titre$='Info'
11530 :
11540 restore 11540
11550 :
11560   longueur_titre%=LEN(titre$)*6 : rem on calcule la taille et la
11570   titre_x%=(ixsize%-longueur_titre%)/2 : rem position x de cet objet
11580 :
11590 : rem  Loose items, the first 4 are standard
11600   iesc_it%=0:imove_it%=iesc_it%+1
11610 :
11620   info_lit%=1:DIM info_lfl%(info_lit%)
11630   info_lot=RD_LOT(std_iattr,info_lit%)
11640     DATA lxs%,lys%,4+xesp%*0,2,0,0,cancel$,text+retr,'ESC'
11650     DATA lxs%,lys%,ixsize%-2-xesp%*1,2,0,0,move$,sprite+retr,move_sprite
11660 :
11670   info_iot1=RD_IOT(0)
11680     DATA longueur_titre%,10,4,INT((lys%-10)/2),text,sp.titlefg,0,0,titre$
11690 :
11700   REMark now the info wdws
11710 :
11720   info_iwt=RD_IWT(2)                              : REMark there are 3
11730 :
11740 REMark first the title bar
11750 :
11760     DATA ixsize%-8-(2*nbr_post%*xesp%),lys%+3,xesp%*nbr_post%+4,0   : REMark x,y sizes & origs
11770     DATA 0,0,sp.infwinbd,sp.titlebg     : REMark shadow, border size & col,paper col
11780     DATA 0                                        : REMark object pointer (none)
11790 :
11800 REMark now wdw containing title itself
11810 :
11820     DATA longueur_titre%+8,lys%+1,titre_x%-4,2
11830     DATA 0,0,sp.infwindbd,sp.infwinbg
11840     DATA info_iot1                      : REMark one object
11850 :
11860 REMark inner border
11870 :
11880     DATA ixsize%-8,iysize%-inf1y%-2,4,inf1y%
11890     DATA 0,1,sp.infwinbd,sp.infwinbg
11900     DATA 0
11910 :
11920 : rem  window definition
11930 :
11940   info_defn=RD_WDEF
11950     DATA 2,1,sp.winbd,sp.winbg: rem ombre, larg. & coul. bord., coul. papier
11960     DATA ixsize%,iysize%,10,iysize%-6 : rem taille x,y et position pointeur
11970     DATA main_sprite,info_lot,info_iwt,0
11980 :
11990 END DEFine init_info_wdw
12000 :
12010 DEFine PROCedure mk_main_app
12020 rem make main application sub window
12030 local t2%,temp%,sections%,att_phys%(3)
12040   dim main_cty%(0):dim main_mfl%(0,0)
12050   sections%=2
12060   temp%=sections%
12070   att_phys%(0)=xsize%-16                : rem xsize calculated on main wdw x size
12080   att_phys%(1)=rangees%                 : rem y size,nominal,in rows
12090   att_phys%(2)=4                        : rem x origin
12100   att_phys%(3)=inf1y +appwinoffset%     : rem y origin
12110   app_addr=make_standard_app_sub2(app_addr,appsub_data$,temp%,257,att_phys%)
12120   if app_addr
12130       dim main_cty%(sections%,2)        : rem ctrl defn
12140       main_cty%(0,0)=1
12150       if temp%>rangees%
12160           main_cty%(1,2)=rangees%       : rem show 1 section
12170       else
12180           main_cty%(1,2)=temp%
12190       endif
12200       if sections%=2:main_cty%(2,2)=main_cty%(1,2) : rem but allow 2
12210       if temp%
12220              dim main_mfl%(temp%-1,0)   : rem flag array
12230              main_mfl%(0,0)=128
12240       else
12250              poke_w app_addr,peek_w(app_addr)+8
12260       end if
12270       scroll_arrows rangees%,temp%,app_addr,main_cty%
12280       if main_app_list<>0
12290           poke_l main_app_list+2,app_addr
12300       endif
12310   end if
12320 end define mk_main_app
12330 :
12340 def proc make_appsub_data (jobnm$)
12350 rem  this gets the data from the recent thing into an array
12360 local lp%,leng%,nbr%,size,max%,tit$
12370   if jobnm$=show_jobs$
12380      get_jobs_data                      : rem get data on jobs, not files
12390      return                                       : rem PREMATURE EXIT
12400   endif
12410   leng%=0:nbr%=0:max%=0
12420   if jobnm$= main_sbasic$
12430     size=rcnt_info(0,nbr%,leng%,max%)   : rem get info on job
12440     if size<0
12450       leng%=warning%("Couldn't find this job",0,jobnm$&":")
12460       show_jobs
12470       return
12480     endif
12490     dim appsub_data$(nbr%+1,45)         : rem array for files for this job
12500     rcnt_garj 0, appsub_data$                     : rem get names of files
12510     tit$="***--- Files for : "&jobnm$ & " ---***"
12520   else
12530     if jobnm$<>""
12540       size=RCNT_INFO(jobnm$,nbr%,leng%,max%) : rem get info on job
12550       if size<0
12560           leng%=warning%("Couldn't find this job",0,jobnm$&":")
12570           show_jobs
12580           return
12590       endif
12600       dim appsub_data$(nbr%+1,45)       : rem array for files for this job
12610       rcnt_garj jobnm$, appsub_data$    : rem get names of files
12620       tit$="***--- Files for : "&jobnm$ & " ---***"
12630     else
12640       size=rcnt_info(-3,nbr%,leng%,max%): rem info on general list
12650       dim appsub_data$(nbr%+1,45)
12660       rcnt_garr appsub_data$
12670       tit$="***--- General file list ---***"
12680     endif
12690   endif
12700   :
12710   : rem for the outptr keywords, I need the data to start at elmt 1, not elmt 0
12720   : rem so move all elements in the array 1 down. I also want to add a title,
12730   : rem so I move them 2 down
12740   :
12750   for lp%=nbr%+1 to 2 step -1
12760     appsub_data$(lp%)=appsub_data$(lp%-2)
12770   end for lp%
12780   appsub_data$(1)=tit$
12790 end def make_appsub_data
12800 :
12810 def proc get_jobs_data
12820 rem this gets a list of all jobs into the basic array appsub_data$.
12830 rem it also gets the job id into the array job_ids, which isn't used any further.
12840 local a,address,nbr%,lp%,leng%,jbid,memspace
12850   memspace=10000              : rem space for buffer
12860   rep lp%
12870     a=alchp(memspace)                   : rem get some common heap space for the buffer
12880     nbr%=rcnt_jobs(memspace,a)          : rem get all jobs
12890     if nbr%>-1:exit lp%       : rem call was ok
12900     rechp a                             : rem i need more space!
12910     memspace=memspace+10000   : rem so try with this much
12920     if memspace>100000
12930       dim appsub_data$(1,30)  : rem names of jobs
12940       appsub_data$(1)="serious error in prog"
12950       dim job_ids(1)                    : rem job ids
12960       return
12970     endif                               : rem there is something serioulsy wrong, i presume
12980   end rep lp%
12990   address= a                            : rem keep
13000   dim appsub_data$(nbr%+2,80)           : rem for names of jobs
13010   dim job_ids(nbr%+2)                   : rem job ids
13020   for lp% = 2 to nbr%+1
13030     jbid= peek_l(address)     : rem get jobid
13040     if jbid=-1:exit lp%       : rem this ** shouldn't happen **
13050     job_ids(lp%)=jbid
13060     address=address+4                   : rem point after job id
13070     leng%=peek_w (address)    : rem length of name
13080     address=address+2
13090     appsub_data$(lp%)=get_nm$(address,leng%,lp%) : rem get the name
13100     address=address+leng% +(leng%&&1)   : rem point next entry
13110   end for lp%
13120   appsub_data$(nbr%+2)="(Show general list)"
13130   rechp a                               : rem done
13140   appsub_data$(1)="***--- List of jobs ---***"
13150 end define getjobs
13160 :
13170 define function get_nm$ (address,t%,index%)
13180 rem this gets the name from the buffer, starting at address, simple peek$
13190 local lp,a$
13200   if t%<0 : return "illegal"  : rem huh?
13210   if t%=0
13220      if job_ids(index%)=0
13230           return main_sbasic$
13240      else
13250           return "Unknown job (no name)": rem job had no name
13260      endif
13270   endif
13280   return peek$(address,t%)
13290 end define get_nm$
13300 :
13310 def proc show_jobs
13320   current_job$=show_jobs$: rem show list of jobs
13330   refresh_list
13340   displaying_jobs%=1
13350   main_lfl%(info_it%)=17
13360 end def show_jobs
13370 :
13380 def proc make_standard_sprites
13390 rem This makes standard move, sleep and wake sprites and sets the
13400 rem GLOBAL vars move_sprite,, zzz_sprite, wake_sprite to them.
13410 rem If there are system sprites, these are used.
13420 rem This also makes the old "main_sprite" pointer sprite.
13430 :
13440   espace_vide=ALCHP(100)         : rem for pointers with no mask
13450 :
13460 restore 13460
13470   main_sprite=RD_SPRT(0)         : REMark ici un masque normal
13480     DATA 11,13,6,5,4
13490     DATA '     www     '
13500     DATA '     waw     '
13510     DATA '     waw     '
13520     DATA '     waw     '
13530     DATA 'wwwwwa awwwww'
13540     DATA 'waaaa   aaaaw'
13550     DATA 'wwwwwa awwwww'
13560     DATA '     waw     '
13570     DATA '     waw     '
13580     DATA '     waw     '
13590     DATA '     www     '
13600 :
13610   if sys_tspr_addr(6)<>0
13620     move_sprite=6
13630   else
13640 restore 13640
13650     move_sprite=RD_SPRT(1)         : REMark ici un masque 0
13660     DATA 10,14,5,4,4
13670     DATA 'aaaaaaaaaa    '
13680     DATA 'awwwwwwwwa    '
13690     DATA 'awwrrrrwwa    '
13700     DATA 'awwrrrrwwaaaaa'
13710     DATA 'awwrrwwwwwwwwa'
13720     DATA 'awwwwwwwwrrwwa'
13730     DATA 'aaaaawwrrrrwwa'
13740     DATA '    awwrrrrwwa'
13750     DATA '    awwwwwwwwa'
13760     DATA '    aaaaaaaaaa'
13770   endif
13780 :
13790   if sys_tspr_addr(10)<>0
13800     sleep_sprite=10
13810   else
13820 restore 13820
13830     sleep_sprite= RD_SPRT(1)             : REMark ici un masque 0
13840     DATA 7,15,0,0,4
13850     DATA 'wwwww          '
13860     DATA '   w           '
13870     DATA '  w  wwww      '
13880     DATA ' w     w       '
13890     DATA 'wwwww w  wwww  '
13900     DATA '     wwww  w   '
13910     DATA '          wwww '
13920   endif
13930 :
13940   if sys_tspr_addr(11)<>0
13950     wake_sprite=11
13960   else
13970 restore 13970
13980     wake_sprite=RD_SPRT(1)          : REMark ici un masque 0
13990     DATA 9,14,0,0,4
14000     DATA "             w"
14010     DATA "            w "
14020     DATA "      w   ww  "
14030     DATA "     ww www   "
14040     DATA "    wwwwww    "
14050     DATA "   www ww     "
14060     DATA "  w   w      "
14070     DATA " w            "
14080     DATA "w             "
14090   endif
14100 :
14110   if sys_tspr_addr(7)<>0
14120     size_sprite=7
14130   else
14140 restore 14140
14150     size_sprite=RD_SPRT(1)
14160     DATA 9,14,5,4,4
14170     DATA 'aaaaaaaaaaaaaa'
14180     DATA 'awwwwwwwwwwwwa'
14190     DATA 'awwrrrrrrrrwwa'
14200     DATA 'awwrrrrrrrrwwa'
14210     DATA 'awwrrwwwwwwwwa'
14220     DATA 'awwrrwwrrrrwwa'
14230     DATA 'awwrrwwrrrrwwa'
14240     DATA 'awwwwwwwwwwwwa'
14250     DATA 'aaaaaaaaaaaaaa'
14260   endif
14270 :
14280 end def make_sprites
14290 :
14300 DEFine PROCedure warn_make
14310 rem makes warning & info window
14320 rem uses global vars sp.xxx
14330 rem SETS the global var warning_width% (nbr of chars in one line in wdw)
14340 local lys%,warn_lot,warn_iot1,warn_iwt
14350 local x%,y%,warning_paper,warning_brd
14360 restore 14360
14370   x%=200:y%=82
14380   lys%=10
14390   DIM warn_lfl%(1)
14400   warn_lot=RD_LOT(std_iattr,1)
14410     DATA 30,lys%,4,4,0,0,cancel$,text+retr,'ESC'
14420     DATA 30,lys%,x%-34,4,0,0,'O',text+retr,"OK"
14430 :
14440   warn_iot1=RD_IOT (0)
14450     DATA 12*9,10,0,1,text,sp.errfg,2,0,'Attention'
14460 :
14470   warn_iwt=RD_IWT(1)
14480     DATA x%-16,y%-32,8,24
14490     DATA 0,1,sp.winbd,sp.errbg
14500     DATA 0
14510 :
14520     DATA 110,12,(x%-110) div 2,4
14530     DATA 0,1,sp.winbd,sp.errbg
14540     DATA warn_iot1
14550 :
14560   warn_defn=RD_WDEF
14570     DATA 2,1,sp.winbd,sp.errbg
14580     DATA x%,y%,0,0
14590     DATA main_sprite,warn_lot,warn_iwt,0
14600 :
14610   warning_width%=(x%-16) div 6                    : rem width of warning window in chars
14620   info_is_pulled_down%=0
14630   warn_wdw_addr=peek_l(warn_defn+100)+20: rem ptr to 2nd info wdw
14640  rem warn_wdw_addr=peek_l(warn_wdw_addr+16): rem ptr to object list
14650 END DEFine warn_make
14660 :
14670 DEFine PROCedure about_make (titre$,version$)
14680 local lxs%,lys%,xesp%,inf1y%,abt_lot,longueur_titre%,titre_x%
14690 local abt_iwt,abt_iot1,abt_iot2
14700   ab_xt%=200:ab_yt%=120    : REMark tailles x et y: la taille x devrait
14710                                    : REMark ëtre un chiffre divisible par 4
14720   lxs%=24:lys%=10                    : REMark tailles x et y pour postes standard
14730   inf1y%=lys%+4            : REMark posi. y de la fen. d'inf. intÉrieure
14740   xesp%=lxs%+2
14750   nbr_post%=0                      : REMark nombre de postes dans un coin
14760 :
14770 restore 14770
14780 wolfsoft_sprite=rD_SPrT(0)
14790     DATA 23,51,0,0,4
14800     DATA 'rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr'
14810     DATA 'rrgggggggggggggggggggggggggggggggggggggggggggggggrr'
14820     DATA 'rrggggggggrrrrrrrrrrrrrrrrgggggggggggggggggggggggrr'
14830     DATA 'rrgggggggggggggggggggggrgggggggggggggggggggggggggrr'
14840     DATA 'rrggggggggggggggggggggrggggggggggggggggggggggggggrr'
14850     DATA 'rrggggggggagggggagggrrrrrrrrrrrrrggaaaaaaaaggggggrr'
14860     DATA 'rrggggggggagggggaggggggggggggggggggagggggggggggggrr'
14870     DATA 'rrggggggggaggaggaggaaaaaaaggaggggggaaaaaaagggggggrr'
14880     DATA 'rrggggggggagaaagaggaawwaaaggaggggggagggggggggggggrr'
14890     DATA 'rrggggggggaaagaaaggaawwwwaggaggggggagggggggggggggrr'
14900     DATA 'rrggrrrrrgaagggaaggawwwwaaggaggggggagggggrrrrrrrgrr'
14910     DATA 'rrgrgggggrgggggggggaaawwaaggaggggggggggrrrgggggggrr'
14920     DATA 'rrgrggggggrggggggggaaaaaaaggaaaaaagggrrggrgggggggrr'
14930     DATA 'rrgrgggggggggggggrrrgggggggggggrrrggrggggrgggggggrr'
14940     DATA 'rrggrrrrrrggggggrgggrrgggggggrrggggggggggrgggggggrr'
14950     DATA 'rrggggggggrrrgggrgggggrrgggrrggggggggggggrgggggggrr'
14960     DATA 'rrgggggggggggrggrgggggggrggrgggggggggggggrgggggggrr'
14970     DATA 'rrggggggggggggrgrggggggggrgrrrrrrrgggggggrgggggggrr'
14980     DATA 'rrgrggggggggggrgrggggggggrgrgggggggggggggrgggggggrr'
14990     DATA 'rrggrgggggggrrgggrggggggrggrgggggggggggggrgggggggrr'
15000     DATA 'rrgggrrrrrrrggggggrrrrrrgggrgggggggggggggrgggggggrr'
15010     DATA 'rrgggggggggggggggggggggggggggggggggggggggggggggggrr'
15020     DATA 'rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr'
15030 :
15040   yinf1y%=inf1y%+4:
15050 :
15060   DIM abt_lfl%(0)
15070   abt_lot=RD_LOT(std_iattr,0)
15080     DATA ab_xt%-16,lys%,8,ab_yt%-lys%-8,0,0,cancel$,text+retr,'Super!'
15090 :
15100 : REMark  D'abord le titre qui est un objet d'une sous-fenëtre d'info.
15110 :
15120   longueur_titre%=LEN(" Version "&version$)*6               : REMark on calcule la taille et la
15130   titre_x%=INT((ab_xt%-longueur_titre%)/2)  : REMark position x de cet objet
15140   abt_iot2=RD_IOT(1)
15150     DATA longueur_titre%,lys%,4,8,text,sp.winfg,0,0," Version "&version$
15160     DATA 52,24,ab_xt%-60,40,sprite,0,wolfsoft_sprite
15170   longueur_titre%=LEN(titre$)*6     : REMark on calcule la taille et la
15180   titre_x%=INT((ab_xt%-longueur_titre%)/2)  : REMark position x de cet objet
15190   abt_iot1=RD_IOT(0)
15200     DATA longueur_titre%,lys%,4,INT((lys%-10)/2),text,sp.titlefg,0,0,titre$
15210   abt_iwt=RD_IWT(2)               : REMark on veut 3 sous-fen. d'info
15220 REMark d'abord la fenëtre contenant la barre entourant le titre
15230     DATA ab_xt%-8-(2*nbr_post%*xesp%),lys%+2,xesp%*nbr_post%+4,0  : REMark taille x,y, origines x,y
15240     DATA 0,0,sp.infwinbd,sp.titlebg     : REMark ombre, taille & couleur bordure
15250                                    : REMark puis couleur papier
15260     DATA 0                         : REMark pointeur vers objets
15270 REMark maintenant la fenëtre qui contient le titre
15280     DATA longueur_titre%+8,lys%,titre_x%-2,2   : REMark on utilise titre_x% etc
15290     DATA 0,0, sp.infwinbd,sp.titletextbg
15300     DATA abt_iot1                 : REMark un objet, le titre
15310 REMark maintenant la fenëtre de la bordure intÉrieure
15320     DATA ab_xt%-8,ab_yt%-inf1y%-2,4,inf1y%
15330     DATA 0,1,sp.infwinbd,sp.infwinbg
15340     DATA abt_iot2
15350   abt_defn=RD_WDEF
15360     DATA 2,1,sp.winbd,sp.winbg: REMark ombre, larg. & coul. bord., coul. papier
15370     DATA ab_xt%,ab_yt%,10,10 : REMark taille x,y et position pointeur
15380     DATA main_sprite,abt_lot,abt_iwt,0
15390 :
15400 end def about_make
15410 :
15420 DEFine PROCedure about (datet$)
15430   LOCal loop%,item%,dummy%,chan%
15440   DR_PULD abt_defn,-1,-1,abt_lfl%
15450   chan%=fopen('con')
15460   dr_iwdf#chan%,abt_defn,2
15470   wm_ink#chan%,sp.infwinfg
15480   wm_paper#chan%,sp.infwinbg
15490   at#chan%,4,2
15500   print#chan%,"Copyright (c) "
15510   at#chan%,5,2
15520   print#chan%, datet$
15530   at#chan%,6,2
15540   print#chan%,"Wolfgang H. Lenerz"
15550   close#chan%
15560   REPeat loop%
15570     RD_PTR abt_defn,item%,dummy%,dummy%,dummy%,dummy%,abt_lfl%
15580     SELect ON item%
15590             =0       : EXIT loop%
15600     END SELect
15610   END REPeat loop%
15620   DR_UNST abt_defn
15630 END DEFine about
15640 :
15650 DEFine FuNction RD_SPRT(drap%)
15660 REMark drap% determine si masque 0
15670   LOCal tmp_patt$(32,32)
15680   LOCal xs,ys,xo,yo, md, l
15690   READ ys,xs,xo,yo,md
15700   DIM tmp_patt$(ys,xs)
15710   FOR l=0 TO ys-1:READ tmp_patt$(l)
15720   l=ALCHP(SPRSP(xs,ys))
15730   SPSET l,xo,yo,md,tmp_patt$(0 TO ys-1,1 TO xs)
15740   IF drap%
15750     POKE_L l+16,espace_vide-(l+16)
15760   END IF
15770   RETurn l
15780 END DEFine RD_SPRT
15790 :
15800 DEFine FuNction RD_WDEF
15810 rem read window defn
15820   LOCal ldef%(3), lspr, lloose, linfo, lappl, lattr%(3)
15830   RD_WATT lattr%
15840   READ ldef%(0), ldef%(1), ldef%(2), ldef%(3)
15850   READ lspr, lloose, linf, lappl
15860   RETurn MK_WDEF (ldef%, lattr%, lspr, lloose, linf, lappl)
15870 END DEFine RD_WDEF
15880 :
15890 DEFine FuNction RD_LOT (lattr,nitem)
15900   LOCal count(3)
15910   LOCal item, ltyp, a$, lsk$
15920   LOCal ldef%(nitem,6), lptr(3,nitem), lstr$(nitem,85)
15930   lsk$=''
15940   FOR item = 0 TO nitem
15950     READ ldef%(item,0), ldef%(item,1), ldef%(item,2), ldef%(item,3)
15960     READ ldef%(item,4), ldef%(item,5)
15970     READ a$: lsk$=lsk$ & a$
15980     READ ltyp
15990     ldef%(item,6)=ltyp: ltyp=(ltyp MOD 256)/2
16000     IF ltyp>10 or ltyp <0:ltyp=0
16010     IF ltyp
16020       READ lptr(ltyp,count(ltyp))
16030     ELSE
16040       READ lstr$(count(0))
16050     END IF
16060     count(ltyp)=count(ltyp)+1
16070   END FOR item
16080   RETurn MK_LIL (lattr, ldef%(TO, 0 TO 1), ldef%(TO, 2 TO 3), ldef%(TO, 4 TO 5), lsk$, ldef%(TO, 6), lstr$, lptr(1), lptr(2), lptr(3))
16090 END DEFine RD_LOT
16100 :
16110 DEFine FuNction RD_IWT(nitem)
16120   LOCal item,mc1
16130   LOCal ldef%(nitem,3), latt%(nitem,3), lptr(nitem)
16140   FOR item = 0 TO nitem
16150     READ ldef%(item,0), ldef%(item,1), ldef%(item,2), ldef%(item,3)
16160     READ latt%(item,0), latt%(item,1), latt%(item,2), latt%(item,3)
16170     READ lptr(item)
16180   END FOR item
16190   RETurn MK_IWL (ldef%, latt%, lptr)
16200 END DEFine RD_IWT
16210 :
16220 DEFine FuNction RD_IOT(nitem)
16230   LOCal count(3)
16240   LOCal item, ltyp, work1, work2
16250   LOCal ldef%(nitem,4), lptr(3,nitem), lstr$(nitem,85)
16260   FOR item = 0 TO nitem
16270     READ ldef%(item,0), ldef%(item,1), ldef%(item,2), ldef%(item,3)
16280     READ ltyp
16290     ldef%(item,4)=ltyp:  ltyp=(ltyp MOD 256)/2
16300     IF ltyp >10:ltyp=0
16310     IF ltyp
16320       READ lptr(0,item),lptr(ltyp,count(ltyp))
16330     ELSE
16340       if mycolour_mode%=2
16350           read work1,work2,work3
16360           lptr(0,item)=WL_4_IOL(work1,work2,work3)
16370       else
16380           READ work1
16390           READ work2: work1=work1*256+work2
16400           READ work2: lptr(0,item)=work1*256+work2
16410       endif
16420       READ lstr$(count(0))
16430     END IF
16440     count(ltyp) = count(ltyp) + 1
16450   END FOR item
16460   RETurn MK_IOL (ldef%(TO, 0 TO 1), ldef%(TO, 2 TO 3), lptr(0), ldef%(TO, 4), lstr$, lptr(1), lptr(2), lptr(3))
16470 END DEFine RD_IOT
16480 :
16490 DEFine PROCedure RD_IATT (lattr)
16500   LOCal i
16510   READ lattr(0,0), lattr(0,1)
16520   FOR i=1 TO 3: READ lattr(i,0), lattr(i,1), lattr(i,2), lattr(i,3)
16530 END DEFine RD_IATT
16540 :
16550 DEFine PROCedure RD_WATT (lattr%)
16560   READ lattr%(0), lattr%(1),lattr%(2),lattr%(3)
16570 END DEFine RD_WATT
16580 :
16590 DEFine FuNction RD_APPW
16600   LOCal ldef%(3), lspr, sk$, lattr%(3)
16610   READ ldef%(0), ldef%(1), ldef%(2), ldef%(3)
16620   RD_WATT lattr%
16630   READ lspr, sk$
16640   RETurn MK_APPW (ldef%, lattr%, lspr,sk$)
16650 END DEFine
16660 :
