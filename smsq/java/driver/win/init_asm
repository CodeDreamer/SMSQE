; SMSQmulator WIN Driver initialisation  V1.03 (c) W.Lenerz 2012-2020

; This handles linking in the driver into the OS.
;
; copyright (C) w. Lenerz 2012-2020 published under the SMSQE licence
; v. 1.03 added WIN_REMV
; v. 1.02 data/prog defaults are set
; v. 1.01 implement win_drive & win_drive$
; v. 1.00 device is called WIN
; v. 0.01 functional
; v. 0.00 stub
; based on:
; RAM Disk disk initialisation	V2.02	 1985	Tony Tebby   QJUMP

	section nfa

	xdef	win_init

	xref.l	nfa_vers	; get version

	xref	ut_procdef
	xref	iou_ddst
	xref	iou_ddlk


	xref	nfa_opn 	; open file
	xref	nfa_clo 	; close file
	xref	nfa_io		; do file io
	xref	nfa_fmt 	; format drive
	xref	nfa_niy

	include 'dev8_keys_iod'
	include 'dev8_mac_proc'
	include 'dev8_dd_rd_data'
	include 'dev8_mac_config02'
	include 'dev8_keys_java'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'

maxchars  equ	96

	mkcfhead  {WIN Drives},'1_00'

noyes	mkcfitem 'JVW0',code,'D',disable,,,\
       {Disable WIN drives?} \
	0,N,{No},1,Y,{Yes}


	mkcfitem 'JVW1',string,1,w1name,,,\
	{Name for WIN Drive 1?},0
			    
	mkcfitem 'JVW2',string,'2',w2name,,,\
	{Name for WIN Drive 2?},$0000
				      
	mkcfitem 'JVW3',string,'3',w3name,,,\
	{Name for WIN Drive 3?},$0000
				
	mkcfitem 'JVW4',string,'4',w4name,,,\
	{Name for WIN Drive 4?},$0000
				      
	mkcfitem 'JVW5',string,'5',w5name,,,\
	{Name for WIN Drive 5?},$0000

	mkcfitem 'JVW6',string,'6',w6name,,,\
	{Name for WIN Drive 6?},$0000

	mkcfitem 'JVW7',string,'7',w7name,,,\
	{Name for WIN Drive 7?},$0000

	mkcfitem 'JVW8',string,'8',w8name,,,\
	{Name for WIN Drive 8?},$0000

	mkcfend


	dc.l	jva_cfgf1
	dc.l	jva_cfgf2
	dc.l	jva_cfgf3
	dc.l	jva_cfgf4
	dc.l	'WIN0'

disable dc.b	0
			
w1name	dc.w	maxchars+4
	ds.b	maxchars+4

w2name	dc.w	maxchars
	ds.b	maxchars+4

w3name	dc.w	maxchars
	ds.b	maxchars+4

w4name	dc.w	maxchars
	ds.b	maxchars+4

w5name	dc.w	maxchars
	ds.b	maxchars+4

w6name	dc.w	maxchars
	ds.b	maxchars+4

w7name	dc.w	maxchars
	ds.b	maxchars+4

w8name	dc.w	maxchars
	ds.b	maxchars+4


;+++
; Initialise WIN_USE and DD linkage and get device going.
;
;	status return standard
;---
exit	clr.l	d0
	rts
win_init
	lea	proctab,a1
	jsr	ut_procdef	; initialise basic keywords (always)
	lea	disable(PC),a1
	tst.b	(a1)		; do we link in WIN drive at all?
	bne.s	exit		; no
	move.l	4,d0		; get config info
	beq.s	no_cfg
	move.l	a1,-(a7)
	move.l	d0,a1
	move.l	jva_winu(a1),d0
	beq.s	none
	lea	use_nam+2,a1
	move.l	d0,(a1)
none	move.l	(a7)+,a1
no_cfg	
	lea	nfa_vect,a3		  ; set up dd linkage
	jsr	iou_ddst
 
	jmp	iou_ddlk
	jsr	iou_ddlk

hdi_found
	moveq	#sms.xtop,d0
	trap	#do.smsq
	lea	sys_datd(a6),a0 	 ; data default
	bsr.s	hdi_dset
	lea	sys_prgd(a6),a0
hdi_dset
	move.l	(a0),d0 		 ; set?
	beq.s	hdi_rts
	move.l	d0,a0
	move.l	hdi_boot+2,2(a0)	 ; set boot device as default
	moveq	#0,d0
hdi_rts
	rts

hdi_boot dc.w	 9,'win1_boot'

nfa_vect
	dc.l	rdd_end 	; length of linkage
	dc.l	rdd.plen	; length of physical definition
use_nam dc.w	3,'WIN0'	; device name (usage)
	dc.w	3,'WIN0'	; device name (real)

null	dc.w	0		; external interrupt server (none)
	dc.w	0		; polling server (none)
	dc.w	0		; scheduler server  (none)

	dc.w	nfa_io-*	; io operations
	dc.w	nfa_opn-*	; open operation
	dc.w	nfa_clo-*	; close
	dc.w	0		; forced slaving
	dc.w	0		; dummy
	dc.w	0		; dummy
	dc.w	nfa_fmt-*	; format

	dc.w	0		; check all slave blocks read
	dc.w	0		; flush all buffers
	dc.w	0		; get occupancy information
	dc.w	0		; load
	dc.w	0		; save
	dc.w	0		; truncate
	dc.w	0		; locate buffer
	dc.w	0		; locate / allocate buffer
	dc.w	0		; mark buffer updated
	dc.w	0		; allocate first sector
	dc.w	0		; check medium for open operation
	dc.w	0		; format drive
	dc.w	0		; read sector
	dc.w	0		; write sector
	dc.w	-1

; the basic procedures (in driver_nfa_use and driver_nfa_useq)
	section procs

proctab
	proc_stt
	proc_def WIN_USE
	proc_def WIN_DRIVE
	proc_def WIN_REMV
	proc_end
	proc_stt
	proc_def WIN_USE$ , win_useq
	proc_def WIN_DRIVE$ , win_useq
	proc_end



	end
