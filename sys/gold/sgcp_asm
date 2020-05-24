; SuperGoldCard cache and exception handler patches. Deciphered by Marcel Kilgus

	section patch

	xdef	sgc_qdos_sched
	xdef	sgc_min_sched
	xdef	sgc_boot_sched
	xdef	sgc_i2
	xdef	sgc_aerr
	xdef	sgc_div0
	xdef	sgc_chk
	xdef	sgc_trpv
	xdef	sgc_trac

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_jcbq'
	include 'dev8_sys_gold_keys'

; Handle cache after scheduler run (QDOS)
sgc_qdos_sched
	moveq	#9,d0			; C&E: clear and enable
	tst.b	sgo_cache+sgx_work	; cache?
	bne.s	sgc_1crt
	moveq	#0,d0			; disable cache
sgc_1crt
	dc.l	$4e7b0002		; set cache
	movem.l jcb_d0(a0),d0-d7/a0-a6
	rte

; Handle cache after scheduler run (Minerva)
sgc_min_sched
	moveq	#9,d0			; C&E: clear and enable
	tst.b	sgo_cache+sgx_work	; cache?
	bne.s	sgc_2crt
	moveq	#0,d0			; disable cache
sgc_2crt
	dc.l	$4e7b0002		; set cache
	movem.l -$3c(a0),d0-d7/a0-a6
	rte

; The Minerva boot code will jump right into the scheduler code, which ends
; with an RTE. So the 68020+ stack frame must be faked
sgc_boot_sched
	move.l	(sp)+,d0
	move.w	#$68,-(sp)		; fake 68020+ stack frame from exv_i2
	move.l	d0,-(sp)
	move.w	#9600,d1
	moveq	#sms.comm,d0
	rts

; Autovector interrupt 2
sgc_i2
	dc.w	$c
	move.l	d0,-(sp)
	moveq	#9,d0
	dc.l	$4e7b0002
	move.l	(sp)+,d0
	jmp	$12345678

; 68000 address error stack frame
ae0.fc	equ	$00		; function codes
ae0.fa	equ	$02		; faulting address
ae0.ir	equ	$06		; instruction
ae0.sr	equ	$08		; status register
ae0.pc	equ	$0a		; program counter
ae0.len equ	$0e

; 68020 address error stack frame (excerpt)
ae2.sr	equ	$00		; status register
ae2.pc	equ	$02		; program counter
ae2.fa	equ	$24		; faulting address
ae2.len equ	$5a

ae.diff equ	ae2.len-ae0.len

; Address error. Build fake 68000 stack frame
sgc_aerr
	dc.w	sgc_aerr_patch-*

	move.l	a6,-(sp)
	move.l	ae2.fa+4(sp),a6 	; stage B address
	subq.l	#2,a6			; backtrack
	move.l	a6,ae.diff+ae0.fa+4(sp)

	move.l	ae2.pc+4(sp),a6 	; program counter
	move.w	(a6),ae.diff+ae0.ir+4(sp) ; instruction
	addq.l	#2,a6
	move.l	a6,ae.diff+ae0.pc+4(sp) ; program counter
	move.l	(sp)+,a6

	move.w	#$68,ae.diff+ae0.len(sp); I have no idea
	move.w	ae2.sr(sp),ae.diff+ae0.sr(sp) ; status register

	add.w	#ae.diff,a7
	move.w	ae0.ir(sp),ae0.fc(sp)	; not sure what this is about
	and.w	#$ffe0,ae0.fc(sp)
	move.l	d0,-(sp)
	moveq	#$12,d0 		; "user program"
	btst	#5,ae0.sr+4(sp) 	; supervisor mode?
	beq.s	aerr_mode
	moveq	#$16,d0 		; "supervisor program"
aerr_mode
	or.w	d0,ae0.fc+4(sp)

	moveq	#9,d0			; enable code cache
	dc.l	$4e7b0002
	move.l	(sp)+,d0
sgc_aerr_patch
	jmp	$12345678

; Divide by 0
sgc_div0
	dc.w	div0_patch-*

	move.l	2(sp),6(sp)
	move.w	(sp),4(sp)
	move.w	#$68,$a(sp)
	move.l	d0,(sp)
	moveq	#9,d0
	dc.l	$4e7b0002
	move.l	(sp)+,d0
div0_patch
	jmp	$12345678

; CHK
sgc_chk
	dc.w	chk_patch-*

	move.l	2(sp),6(sp)
	move.w	(sp),4(sp)
	move.w	#$68,$a(sp)
	move.l	d0,(sp)
	moveq	#9,d0
	dc.l	$4e7b0002
	move.l	(sp)+,d0
chk_patch
	jmp	$12345678

; TRAPV
sgc_trpv
	dc.w	trpv_patch-*

	move.l	2(sp),6(sp)
	move.w	(sp),4(sp)
	move.w	#$68,$a(sp)
	move.l	d0,(sp)
	moveq	#9,d0
	dc.l	$4e7b0002
	move.l	(sp)+,d0
trpv_patch
	jmp	$12345678

; Trace
sgc_trac
	dc.w	trac_patch-*

	move.l	2(sp),6(sp)
	move.w	(sp),4(sp)
	move.w	#$68,$a(sp)
	move.l	d0,(sp)
	moveq	#9,d0
	dc.l	$4e7b0002
	move.l	(sp)+,d0
trac_patch
	jmp	$12345678

	end
