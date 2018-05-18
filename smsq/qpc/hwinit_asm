; SMSQ QPC Hardware Initialisation V3	v1.03		     2005 Marcel Kilgus
;
; 2005-12-09  1.01  Taskbar button configuration
; 2006-05-15  1.02  BGIO configuration and CTRL+C behaviour configuration
; 2017-02-12  1.03  Fixed 3rd slice of mod table, start at $10000 and not $fffc
;
; If d7 = 0	the RAM is setup.
;		$0003,d4 is written to address 0 'ROM' or 'RES'
;		It returns  pointer to config info in a2
;			    pointer to smsq write base routine in a3
;			    pointer to module table in a4
;			    pointer to communications block in a5
;			    pointer to module install code in a6
;			    ROM or RES in d4
;			    zero in d5
;			    zero in d6
;			    top of RAM in d7

	xref	smsq_end

	include 'dev8_keys_68000'
	include 'dev8_keys_sys'
	include 'dev8_keys_stella_bl'
	include 'dev8_smsq_smsq_ini_keys'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_bl_keys'
	include 'dev8_smsq_smsq_config_keys'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_mac_multiconfig'
	include 'dev8_mac_assert'

	section hwinit

header_base
	dc.l	hwinit_base-header_base  ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-hwinit_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	0			 ; main level
	dc.b	0
	dc.w	hwinit_name-*

hwinit_name
	dc.w	36,'QPC Hardware Initialisation for SMSQ'
	dc.l	'    '

qpc_cx
qpc_xres dc.w	800
qpc_yres dc.w	600
qpc_colr dc.b	3
qpc_mem  dc.b	32
qpc_par1 dc.b	1
qpc_par2 dc.b	2
qpc_par3 dc.b	3
qpc_par4 dc.b	0
qpc_par5 dc.b	0
qpc_par6 dc.b	0
qpc_par7 dc.b	0
qpc_par8 dc.b	0
qpc_ser1 dc.b	1
qpc_ser2 dc.b	2
qpc_ser3 dc.b	3
qpc_ser4 dc.b	4
qpc_ser5 dc.b	5
qpc_ser6 dc.b	6
qpc_ser7 dc.b	7
qpc_ser8 dc.b	8
qpc_bflp dc.b	0
qpc_bwin dc.b	1
qpc_altg dc.b	0
qpc_lang dc.w	44
qpc_wkbd dc.b	1,0

qpc_scfg dc.b	0
qpc_winm dc.b	1
qpc_xwin dc.w	800
qpc_ywin dc.w	600
qpc_ontp dc.b	0
qpc_lprt dc.b	0
qpc_fpri dc.b	1
qpc_bpri dc.b	1
qpc_prn1 dc.l	0
qpc_prn2 dc.l	0
qpc_prn3 dc.l	0
qpc_prn4 dc.l	0
qpc_prn5 dc.l	0
qpc_prn6 dc.l	0
qpc_prn7 dc.l	0
qpc_prn8 dc.l	0
qpc_prf1 dc.b	1
qpc_prf2 dc.b	1
qpc_prf3 dc.b	1
qpc_prf4 dc.b	0
qpc_prf5 dc.b	0
qpc_prf6 dc.b	0
qpc_prf7 dc.b	0
qpc_prf8 dc.b	0
qpc_clse dc.b	0
qpc_powr dc.b	1
qpc_rtio dc.b	1
qpc_snd  dc.b	1
qpc_bvol dc.w	75
qpc_svol dc.b	100
qpc_tbar dc.b	0
qpc_kstf dc.b	250
qpc_curs dc.b	1
qpc_bgio dc.b	1
qpc_ctrc dc.b	1

qpc_win1 dc.w	254,18
	 dc.b	'%INIPATH%\QPC1.WIN'
	 ds.b	236
qpc_win2 dc.w	254,18
	 dc.b	'%INIPATH%\QPC2.WIN'
	 ds.b	236
qpc_win3 dc.w	254,18
	 dc.b	'%INIPATH%\QPC3.WIN'
	 ds.b	236
qpc_win4 dc.w	254,18
	 dc.b	'%INIPATH%\QPC4.WIN'
	 ds.b	236
qpc_win5 dc.w	254,18
	 dc.b	'%INIPATH%\QPC5.WIN'
	 ds.b	236
qpc_win6 dc.w	254,18
	 dc.b	'%INIPATH%\QPC6.WIN'
	 ds.b	236
qpc_win7 dc.w	254,18
	 dc.b	'%INIPATH%\QPC7.WIN'
	 ds.b	236
qpc_win8 dc.w	254,18
	 dc.b	'%INIPATH%\QPC8.WIN'
	 ds.b	236

qpc_dos1 dc.w	199,3
	 dc.b	'C:\'
	 ds.b	197
qpc_dos2 dc.w	199,3
	 dc.b	'D:\'
	 ds.b	197
qpc_dos3 dc.w	199,3
	 dc.b	'E:\'
	 ds.b	197
qpc_dos4 dc.w	199,3
	 dc.b	'F:\'
	 ds.b	197
qpc_dos5 dc.w	199,3
	 dc.b	'G:\'
	 ds.b	197
qpc_dos6 dc.w	199,3
	 dc.b	'H:\'
	 ds.b	197
qpc_dos7 dc.w	199,3
	 dc.b	'I:\'
	 ds.b	197
qpc_dos8 dc.w	199,3
	 dc.b	'J:\'
	 ds.b	197

qpc_flp1 dc.w	255,4
	 dc.b	'A:\'
	 ds.b	251
qpc_flp2 dc.w	255,4
	 dc.b	'B:\'
	 ds.b	251

smsq_len dc.l	0

	xref.l	smsq_vers
	mkcfstart
	mkcfhead {Generic settings},smsq_vers
	mkcfitem 'QPCM',byte,0,qpc_mem,,,\
       {Memory size (in MB)},1,128

	mkcfitem 'QPCL',word,0,qpc_lang,,,\
       {Country Code (33=F, 44=GB 49=D, 39=IT)},0,$7fff

	mkcfitem 'QPCK',code,0,qpc_wkbd,,,\
       {Keyboard driver}\
	0,S,{SMSQ/E},1,W,{Windows}

	mkcfitem 'QPCA',code,0,qpc_altg,,,\
       {Function of AltGr key}\
	0,A,{AltGr},1,L,{Alt},2,C,{Ctrl}

	mkcfitem 'QPCs',byte,'S',qpc_kstf,,,\
       {Stuffer buffer key for edit line calls},0,$ff

prio	mkcfitem 'QP2F',code,0,qpc_fpri,,,\
       {Foreground priority}\
	0,I,{Idle},1,L,{Lowest},2,B,{Below normal},3,N,{Normal}

	mkcfitem 'QP2B',code,0,qpc_bpri,,,\
       {Background priority},.prio

	mkcfitem 'QP2P',code,0,qpc_powr,,,\
       {Power management}\
	0,D,{Disabled},1,E,{Enabled},2,S,{Suspend when minimized}

yesno	mkcfitem 'QP2S',code,0,qpc_scfg,,,\
       {Show config dialog on next startup}\
	0,Y,{Yes},1,N,{No}

	mkcfitem 'QP2s',code,0,qpc_snd,,,\
       {Enable sound}\
	0,N,{No},1,Y,{Yes},2,A,{Automatic}

	mkcfitem 'QP2v',word,0,qpc_bvol,,,\
       {Default beep volume (0 = lowest, 100 = highest)},0,100

	mkcfitem 'QP2V',byte,0,qpc_svol,,,\
       {Default SSSS volume (0 = lowest, 100 = highest)},0,100

noyes	mkcfitem 'QP2C',code,0,qpc_clse,,,\
       {Disallow closing QPC through the system menu}\
	0,N,{No},1,Y,{Yes}

	mkcfitem 'QP2t',code,0,qpc_tbar,,,\
       {Show QPC task button in}\
	0,T,{Task bar}\
	1,N,{Notification area ("system tray")}\
	2,M,{Task bar when active, notification area when minimized}

	mkcfitem 'QPCS',code,'S',qpc_ctrc,,,\
       {Use new CTRL+C switch behaviour},.noyes
	mkcfblend

	mkcfhead {Display settings},{smsq_vers}
	mkcfitem 'QPCX',word,0,qpc_xres,,,\
       {X resolution},512,8192

	mkcfitem 'QPCY',word,0,qpc_yres,,,\
       {Y resolution},256,8192

	mkcfitem 'QPCC',code,0,qpc_colr,,,\
       {Display colours}\
	0,Q,{QL},2,A,{Aurora compatible 8 bit},3,H,{High colour 16 bit}

	mkcfitem 'QP2W',code,0,qpc_winm,,,\
       {Display mode}\
	0,F,{Fullscreen},1,W,{Window}

	mkcfitem 'QP2X',word,0,qpc_xwin,,,\
       {X window size},512,2048

	mkcfitem 'QP2Y',word,0,qpc_ywin,,,\
       {Y window size},256,2048

	mkcfitem 'QP2K',code,0,qpc_rtio,,,\
       {Keep aspect ratio},.noyes

	mkcfitem 'QP2T',code,0,qpc_ontp,,,\
       {Window always on top},.noyes

	mkcfitem 'QP2c',code,'C',qpc_curs,,,\
       {Use sprite for cursor},.noyes

	mkcfitem 'QPCb',code,'B',qpc_bgio,,,\
       {Enable CON background I/O},.noyes
	mkcfblend

	mkcfhead {WIN/BOOT settings},{smsq_vers}
	mkcfitem 'QPC1',string,0,qpc_win1,,,{Filename for WIN1},0
	mkcfitem 'QPC2',string,0,qpc_win2,,,{WIN2},0
	mkcfitem 'QPC3',string,0,qpc_win3,,,{WIN3},0
	mkcfitem 'QPC4',string,0,qpc_win4,,,{WIN4},0
	mkcfitem 'QPC5',string,0,qpc_win5,,,{WIN5},0
	mkcfitem 'QPC6',string,0,qpc_win6,,,{WIN6},0
	mkcfitem 'QPC7',string,0,qpc_win7,,,{WIN7},0
	mkcfitem 'QPC8',string,0,qpc_win8,,,{WIN8},0
	mkcfitem 'QPCF',code,0,qpc_bflp,,,\
       {If floppy disk inserted, boot from}\
	 0,N,{Neither},1,A,{A:(FLP1)},2,B,{B:(FLP2)}
	mkcfitem 'QPCB',code,0,qpc_bwin,,,\
       {Otherwise boot from}\
	1,1,{WIN1},2,2,{WIN2},3,3,{WIN3},4,4,{WIN4}\
	5,5,{WIN5},6,6,{WIN6},7,7,{WIN7},8,8,{WIN8}
	mkcfblend

	mkcfhead {DOS device settings},{smsq_vers}
	mkcfitem 'QPD1',string,0,qpc_dos1,,,{Base directory for DOS1},0
	mkcfitem 'QPD2',string,0,qpc_dos2,,,{DOS2},0
	mkcfitem 'QPD3',string,0,qpc_dos3,,,{DOS3},0
	mkcfitem 'QPD4',string,0,qpc_dos4,,,{DOS4},0
	mkcfitem 'QPD5',string,0,qpc_dos5,,,{DOS5},0
	mkcfitem 'QPD6',string,0,qpc_dos6,,,{DOS6},0
	mkcfitem 'QPD7',string,0,qpc_dos7,,,{DOS7},0
	mkcfitem 'QPD8',string,0,qpc_dos8,,,{DOS8},0
	mkcfblend

	mkcfhead {FLP device settings},{smsq_vers}
	mkcfitem 'QPF1',string,0,qpc_flp1,,,{FLP1 image file or drive},0
	mkcfitem 'QPF2',string,0,qpc_flp2,,,{FLP2},0
	mkcfblend

	mkcfhead {SER settings},{smsq_vers}
sercom	mkcfitem 'QPS1',code,0,qpc_ser1,,,\
       {SER1 serial port}\
	0,N,{none},1,1,{COM1},2,2,{COM2},3,3,{COM3},4,4,{COM4},\
	5,5,{COM5},6,6,{COM6},7,7,{COM7},8,8,{COM8}

	mkcfitem 'QPS2',code,0,qpc_ser2,,,\
       {SER2 serial port},.sercom

	mkcfitem 'QPS3',code,0,qpc_ser3,,,\
       {SER3 serial port},.sercom

	mkcfitem 'QPS4',code,0,qpc_ser4,,,\
       {SER4 serial port},.sercom

	mkcfitem 'QPS5',code,0,qpc_ser5,,,\
       {SER5 serial port},.sercom

	mkcfitem 'QPS6',code,0,qpc_ser6,,,\
       {SER6 serial port},.sercom

	mkcfitem 'QPS7',code,0,qpc_ser7,,,\
       {SER7 serial port},.sercom

	mkcfitem 'QPS8',code,0,qpc_ser8,,,\
       {SER8 serial port},.sercom

	mkcfitem 'QP2L',code,0,qpc_lprt,,,\
       {Leave SER ports open},.noyes
	mkcfblend

	mkcfhead {PAR settings},{smsq_vers}
parlpt	mkcfitem 'QPP1',code,0,qpc_par1,,,\
       {PAR1 printer port}\
	0,N,{none},1,1,{LPT1},2,2,{LPT2},3,3,{LPT3},4,P,{Printer}

	mkcfitem 'QPP2',code,0,qpc_par2,,,\
       {PAR2},.parlpt

	mkcfitem 'QPP3',code,0,qpc_par3,,,\
       {PAR3},.parlpt

	mkcfitem 'QPP4',code,0,qpc_par4,,,\
       {PAR4},.parlpt

	mkcfitem 'QPP5',code,0,qpc_par5,,,\
       {PAR5},.parlpt

	mkcfitem 'QPP6',code,0,qpc_par6,,,\
       {PAR6},.parlpt

	mkcfitem 'QPP7',code,0,qpc_par7,,,\
       {PAR7},.parlpt

	mkcfitem 'QPP8',code,0,qpc_par8,,,\
       {PAR8},.parlpt

	mkcfitem 'QP21',long,0,qpc_prn1,,,\
       {PAR1 printer ID (use QPC2 to edit)},0,-1

	mkcfitem 'QP22',long,0,qpc_prn2,,,\
       {PAR2},0,-1

	mkcfitem 'QP23',long,0,qpc_prn3,,,\
       {PAR3},0,-1

	mkcfitem 'QP24',long,0,qpc_prn4,,,\
       {PAR4},0,-1

	mkcfitem 'QP25',long,0,qpc_prn5,,,\
       {PAR5},0,-1

	mkcfitem 'QP26',long,0,qpc_prn6,,,\
       {PAR6},0,-1

	mkcfitem 'QP27',long,0,qpc_prn7,,,\
       {PAR7},0,-1

	mkcfitem 'QP28',long,0,qpc_prn8,,,\
       {PAR8},0,-1

	mkcfitem 'QPPa',code,0,qpc_prf1,,,\
       {Use printer filter for PAR1},.noyes

	mkcfitem 'QPPb',code,0,qpc_prf2,,,\
       {PAR2},.noyes

	mkcfitem 'QPPc',code,0,qpc_prf3,,,\
       {PAR3},.noyes

	mkcfitem 'QPPd',code,0,qpc_prf4,,,\
       {PAR4},.noyes

	mkcfitem 'QPPe',code,0,qpc_prf5,,,\
       {PAR5},.noyes

	mkcfitem 'QPPf',code,0,qpc_prf6,,,\
       {PAR6},.noyes

	mkcfitem 'QPPg',code,0,qpc_prf7,,,\
       {PAR7},.noyes

; When the last item is done using a reference (".noyes") QPC crashes!
	mkcfitem 'QPPh',code,0,qpc_prf8,,,\
       {PAR8}\
	0,N,{No},1,Y,{Yes}
	mkcfblend
	mkcfend

; copy module at (a0) to 4(a1)-(a2) linking to (a3) and updating a3 to a1

qpc_mdinst
	move.l	a1,(a3) 		 ; link
	move.l	a1,a3
	clr.l	(a1)+
qpc_mdloop
	move.w	(a0)+,(a1)+		 ; copy module
	cmp.l	a2,a1
	blt.s	qpc_mdloop
	rts


hwinit_base
	bra.s	hwinit
	dc.w	0

hwinit_hreset				; special hard reset entry
	lea	-1,a4			; max memory
	jmp	sms.base		; hard reset = soft reset

hwinit
	move.l	(sp)+,a0		; return address

	tst.l	d7			; memory already set?
	beq.s	hwi_mem 		; ... no
	jmp	(a0)

hwi_mem
	dc.w	qpc.ramt+4		; get ramtop in a4

	clr.l	0			; clear reset vector
	clr.l	4

	moveq	#0,d5
	moveq	#0,d6
	move.l	a4,d7

	lea	qpc_sconf,a2		 ; config info
	lea	sms_wbase,a3		 ; write to base
	lea	mod_table,a4		 ; module table

	assert	sms_bflp,sms_bwin-1
	assert	sms_xres,sms_yres-2
	move.w	qpc_bflp(pc),sms_bflp(a2); sms_bflp, sms_bwin
	move.w	qpc_lang(pc),sms_defl(a2); sms_defl
	move.l	qpc_xres(pc),sms_xres(a2); sms_xres, sms_yres
	move.b	qpc_colr(pc),sms_ismode(a2); sms_ismode
	move.b	qpc_kstf(pc),sms_kstuf(a2); sms_kstuf
	move.b	qpc_curs(pc),sms_curd(a2); cursor sprite default
	move.b	qpc_bgio(pc),sms_bgio(a2)
	move.b	qpc_ctrc(pc),sms_ctrlc(a2)
	move.b	qpc_wkbd(pc),sms_winkbd(a2)

	clr.l	-(sp)			 ; facilities
	clr.l	-(sp)			 ; language
	dc.w	qpc.ver 		 ; get CPU level in D1
	ror.l	#8,d1
	add.l	#0<<16+0<<8+sys.mqpc,d1  ; no mmu/fpu, std disp
	move.l	d1,-(sp)
	move.l	#'QPC ',-(sp)		 ; QPC
	move.l	sp,a5			 ; loader communications block
	lea	qpc_mdinst,a6

	jmp	(a0)

; SMSQ write to base routine

sms_wbase
	move.w	d0,(a5)+
	rts

; loader tables

mod_table
	dc.l	sms.base-4,$4000	 ; 1st slice, up to vector table
	dc.l	$4200,$c000		 ; 2nd slice, after vector table to ROM
	dc.l	$10000,$17d00		 ; 3rd slice, after ROM to I/O area
	dc.l	0

	end
