; SMSQmulator SFA Driver initialisation    V1.00    (c) W.Lenerz 2012

; This handles linking in the driver into the OS.
;
; copyright (C) w. Lenerz 2012	published under the SMSQE licence
;
; based on:
; RAM Disk disk initialisation	V2.02	 1985	Tony Tebby   QJUMP


	section nfa

	xdef	sfa_init

	xref.l	nfa_vers	; get version

	xref	ut_procdef
	xref	iou_ddst
	xref	iou_ddlk

	xref	nfa_niy 	; not implemented error

	xref	nfa_opn 	; open file
	xref	nfa_clo 	; close file
	xref	nfa_io		; do file io


	include 'dev8_keys_iod'
	include 'dev8_mac_proc'
	include 'dev8_dd_rd_data'
	include 'dev8_mac_config02'
	include 'dev8_keys_java'


maxchars  equ	96

	mkcfhead  {SFA Drives},'1_00'


noyes	mkcfitem 'JVW0',code,'D',disable,,,\
       {Disable SFA drives?} \
	0,N,{No},1,Y,{Yes}


	mkcfitem 'JVS1',string,'1',w1name,,,\
	{Name for SFA Drive 1?},$0000
			    
	mkcfitem 'JVS2',string,'2',w2name,,,\
	{Name for SFA Drive 2?},$0000
				      
	mkcfitem 'JVS3',string,'3',w3name,,,\
	{Name for SFA Drive 3?},$0000
				
	mkcfitem 'JVS4',string,'4',w4name,,,\
	{Name for SFA Drive 4?},$0000
				      
	mkcfitem 'JVS5',string,'5',w5name,,,\
	{Name for SFA Drive 5?},$0000

	mkcfitem 'JVS6',string,'6',w6name,,,\
	{Name for SFA Drive 6?},$0000

	mkcfitem 'JVS7',string,'7',w7name,,,\
	{Name for SFA Drive 7?},$0000

	mkcfitem 'JVS8',string,'8',w8name,,,\
	{Name for SFA Drive 8?},$0000


	mkcfend

	dc.l	jva_cfgf1
	dc.l	jva_cfgf2
	dc.l	jva_cfgf3
	dc.l	jva_cfgf4
	dc.l	'SFA0'


disable dc.b	0
			
w1name	dc.w	maxchars
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
; Initialise SFA_USE and DD linkage and get device going.
;
;	status return standard
;---
exit	clr.l	d0
	rts
sfa_init
	lea	proctab,a1
	jsr	ut_procdef
	lea	disable(PC),a1
	tst.b	(a1)
	bne.s	exit

	move.l	4,d0		; get config info
	beq.s	no_cfg
	move.l	a1,-(a7)
	move.l	d0,a1
	move.l	jva_sfau(a1),d0
	beq.s	none
	lea	use_nam+2,a1
	move.l	d0,(a1)
none	move.l	(a7)+,a1
no_cfg	lea	nfa_vect,a3	 ; set up dd linkage
	jsr	iou_ddst
	jmp	iou_ddlk

nfa_vect
	dc.l	rdd_end 	; length of linkage
	dc.l	rdd.plen	; length of physical definition
use_nam dc.w	3,'SFA0'	; device name (usage)
	dc.w	3,'SFA0'	; device name (real)

null	dc.w	0		; external interrupt server (none)
	dc.w	0		; polling server (none)
	dc.w	0		; scheduler server  (none)

	dc.w	nfa_io-*	; io operations
	dc.w	nfa_opn-*	; open operation
	dc.w	nfa_clo-*	; close
	dc.w	0		; forced slaving
	dc.w	0		; dummy
	dc.w	0		; dummy
	dc.w	nfa_niy-*	; format  : is not possible here

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


; the basic procedures
	section procs

proctab
	proc_stt
	proc_def SFA_USE
	proc_end
	proc_stt
	proc_def SFA_USE$ , sfa_useq
	proc_end

	end
