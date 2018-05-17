; SMSQmulator MEM Driver initialisation  V1.00 (c) W.Lenerz 2014
;
; based on smsq_java_driver_win_init_asm

	section nfa

	xdef	mem_init

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

maxchars  equ	96

	mkcfhead  {MEM Drives},'1_00'


noyes	mkcfitem 'JVM0',code,'D',disable,,,\
       {Disable MEM drives?} \
	0,N,{No},1,Y,{Yes}


	mkcfitem 'JVM1',string,'1',w1name,,,\
	{Name for WIN Drive 1?},$0000
			    
	mkcfitem 'JVM2',string,'2',w2name,,,\
	{Name for WIN Drive 2?},$0000
				      
	mkcfitem 'JVM3',string,'3',w3name,,,\
	{Name for WIN Drive 3?},$0000
				
	mkcfitem 'JVM4',string,'4',w4name,,,\
	{Name for WIN Drive 4?},$0000
				      
	mkcfitem 'JVM5',string,'5',w5name,,,\
	{Name for WIN Drive 5?},$0000

	mkcfitem 'JVM6',string,'6',w6name,,,\
	{Name for WIN Drive 6?},$0000

	mkcfitem 'JVM7',string,'7',w7name,,,\
	{Name for WIN Drive 7?},$0000

	mkcfitem 'JVM8',string,'8',w8name,,,\
	{Name for WIN Drive 8?},$0000


	mkcfend


	dc.l	jva_cfgf1
	dc.l	jva_cfgf2
	dc.l	jva_cfgf3
	dc.l	jva_cfgf4
	dc.l	'MEM0'

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
; Initialise MEM_USE and DD linkage and get device going.
;
;	status return standard
;---
exit	clr.l	d0
	rts
mem_init
	lea	proctab,a1
	jsr	ut_procdef	; initialise basic keywords (always)
	lea	disable(PC),a1
	tst.b	(a1)		; do we link in MEM drive at all?
	bne.s	exit		; no
	move.l	4,d0		; get config info
	beq.s	no_cfg
	move.l	a1,-(a7)
	move.l	d0,a1
	move.l	jva_memu(a1),d0
	beq.s	none
	lea	use_nam+2,a1
	move.l	d0,(a1)
none	move.l	(a7)+,a1
no_cfg	
	lea	nfa_vect,a3		  ; set up dd linkage
	jsr	iou_ddst
	jmp	iou_ddlk

nfa_vect
	dc.l	rdd_end 	; length of linkage
	dc.l	rdd.plen	; length of physical definition
use_nam dc.w	3,'MEM0'	; device name (usage)
	dc.w	3,'MEM0'	; device name (real)

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

; the basic procedures
; v 0.00 none is impemented
	section procs

proctab
	proc_stt
	proc_def MEM_USE
	proc_def MEMD_WRITE
	proc_end
	proc_stt
	proc_def MEM_USE$ , mem_useq
	proc_end

	end
