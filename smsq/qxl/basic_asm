; QXL BASIC Routines

	section exten

	xdef	qxl_prini

	xref	disp_update
	xref	ut_gxin1
	xref	ut_gtint
	xref	ut_procdef
	xref	ak_init
	xref	smsq_xreset
	xref	wmon_def		 ; to get it loaded

	include 'dev8_keys_err'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_68000'
	include 'dev8_keys_sys'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_mac_proc'
;+++
; RES_128
;---
res_128
	move.w	#128,d6
	bra.s	reset
;+++
; RES_SIZE size in K
;---
res_size
	jsr	ut_gxin1		 ; get one integer
	bne.s	qxb_rts
	move.w	(a6,a1.l),d6
reset
	swap	d6			 ; n*64K
	lsr.l	#2,d6			 ; n*16K
	clr.w	d6
	lsr.l	#4,d6			 ; n*1k

	lea	$20000,a4		 ; set bottom of RAM
	add.l	d6,a4

	moveq	#sms.xtop,d0
	trap	#1
	jmp	smsq_xreset

qxb_rts
	rts

	section procs

qxl_ext
	lea	qxl_xprocs,a1		  ; Modified TK2 procedures
	jmp	ut_procdef

qxl_prini
	lea	qxl_procs,a1		  ; QXL procs
	jsr	ut_procdef
	jmp	ak_init

	section procs

qxl_xprocs
	proc_stt
	proc_def CALL,callsq
	proc_def LRESPR,lrespr
	proc_end
	proc_stt
	proc_end

qxl_procs
	proc_stt
	proc_ref QXL_EXT
	proc_ref RES_128
	proc_ref RES_SIZE
	proc_ref DISP_UPDATE
	proc_def ALTKEY
	proc_end
	proc_stt
	proc_end

	end
