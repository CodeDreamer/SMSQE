; SMSQmulator control extension thing, floating point extns (c) W.Lenerz 2017

; v. 1.02 2020 Nov 27 removed dead & test code (DEG/RAD conversion)
; v. 1.01 corrected SQRT
; v. 1.00

	section exten
			 
	xdef	jt_qlfp
	xdef	qpc_patchfpu

	xref	thp_ostr
	xref	thp_nul
	xref	jt_sget 		; next thing extension

	include 'dev8_keys_thg'
	include 'dev8_keys_qlv'
	include 'dev8_keys_java'
	include 'dev8_mac_thg'
	include 'dev8_dv3_keys'

jt_qlfp thg_extn QLFP,jt_iefp,thp_nul
	lea	qpc_patch2ql,a2 	; routine to jump to
	bra.s	fp_comn

jt_iefp thg_extn {IEFP},jt_sget,thp_nul
	lea	qpc_patchfpu,a2
fp_comn move.w	sr,d7
	trap	#0			; go into supervisor mode
	move.w	#$2700,sr		; no interrupts
	jsr	(a2)			; patch vectors
	move.w	d7,sr			; reset status as it was
exit	moveq	#0,d0
	rts

; this is a pure copy of the corresponding code in qpc
; patch the FP routines to call java routines

qpc_patchfpu
	movem.l a0/a1,-(a7)
	movea.w $11c,a0 		 ; FP vector (points to a jump)
	movea.l 2(a0),a0		 ; Get address from jmp and use as base
	lea	qpc_patchtable(pc),a1
qpc_patchloop
	move.w	(a1)+,d0
	beq.s	qpc_endpatch
	move.w	(a1)+,d1
	cmp.w	(a0,d0.w),d1		 ; Original code ok?
	bne.s	qpc_endpatch		 ; No, just quit patching
	move.w	(a1)+,(a0,d0.w) 	 ; Patch new code
	bra.s	qpc_patchloop
qpc_endpatch
	movem.l (a7)+,a0/a1
	rts

ib	equ	$ab00

qpc_patchtable
;		adr  ,origi,patch
	dc.w	$0a2c,$48e7,ib+0	 ; ADDD
	dc.w	$0a62,$48e7,ib+1	 ; ADD
	dc.w	$0a3c,$48e7,ib+2	 ; SUBD
	dc.w	$0a5e,$4eba,ib+3	 ; SUB
	dc.w	$0ad8,$3011,ib+4	 ; DOUBLE
	dc.w	$0aec,$5351,ib+5	 ; HALVE
	dc.w	$08ae,$48e7,ib+6	 ; DIVD
	dc.w	$08be,$48e7,ib+7	 ; DIV
	dc.w	$08b6,$4eba,ib+8	 ; 1/x
	dc.w	$0934,$48e7,ib+9	 ; MULD
	dc.w	$0948,$48e7,ib+10	 ; MUL
	dc.w	$093c,$48e7,ib+11	 ; x^2
;	 dc.w	 $09fc,$2341,ib+12	  ; SQRT (wrong)
	dc.w	$0afe,$2029,ib+12	 ; SQRT
	dc.w	$06b0,$48e7,ib+13	 ; COS
	dc.w	$06bc,$48e7,ib+14	 ; SIN
	dc.w	$0702,$4eba,ib+15	 ; COT
	dc.w	$0714,$4eba,ib+16	 ; TAN
	dc.w	0			 ; 17 is for deg/rad

; patch old smsq/e routines back in

qpc_patch2ql
	movem.l a0/a1,-(a7)
	movea.w $11c,a0 		; FP vector (points to a jump)
	movea.l 2(a0),a0		; Get address from jmp and use as base
	lea	qpc_patchtable(pc),a1
qpc_patchlp
	move.w	(a1)+,d0
	beq.s	qpc_end
	move.w	(a1)+,d1		: original SMSQ/E code
	move.w	(a1)+,d2		; patched code
	cmp.w	(a0,d0.w),d2		; patched code ok?
	bne.s	qpc_end 		; no, just quit patching
	move.w	d1,(a0,d0.w)		; patch original code back in
	bra.s	qpc_patchlp
qpc_end
	movem.l (a7)+,a0/a1
	rts


	end
