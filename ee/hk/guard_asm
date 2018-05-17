; Guardian  V2.00     1988   Tony Tebby   QJUMP

	section hotkey

	xdef	hk_guard
	xdef	hk_gdstart

	xref	hk_cons
	xref	hk_grab

	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_ioa'

;+++
; Open and clear guardian window using definition following the call.
; Then, if next word is non-zero, grab all but memory specified.
;
;	all registers preserved
;	status return arbitrary
;---
hk_guard
reglist reg	d0-d3/d6/d7/a0/a1/a6
stk_rtad equ	$24
	movem.l reglist,-(sp)
	move.l	stk_rtad(sp),-(sp)	 ; point to definition
	add.l	#$c,stk_rtad+4(sp)	 ; skip it
	lea	hgd_exit,a6		 ; and return address


;+++
; Open and clear guardian window using definition following the call.
; Then, if next word is non-zero, grab all but memory specified.
;
;	registers d0-d3/d6/d7/a0/a1 set to zero
;	pops one long word from stack
;	jmps to  (a6) when finished
;---
hk_gdstart
	move.l	(sp)+,a1		 ; the definition

	move.b	$8(a1),d6		 ; freeze flag
	move.w	$a(a1),d7		 ; grabber memory
	tst.w	(a1)
	beq.s	hgd_done		 ; nop

	moveq	#myself,d1		 ; mine
	jsr	hk_cons

	moveq	#forever,d3

	tst.w	(a1)			 ; unlock?
	blt.s	hgd_unlk		 ; ... unlock

	moveq	#iop.outl,d0		 ; define window outline
	moveq	#0,d1			 ; no shadow
	moveq	#iopo.set,d2		 ; just set
	trap	#do.io
	moveq	#iow.clra,d0		 ; clear
	trap	#do.io

	tst.b	d6			 ; freeze?
	bpl.s	hgd_grab		 ; ... no
	moveq	#iop.pick,d0		 ; ... yes, set it
	moveq	#iopp.frz,d1
	trap	#do.io

hgd_grab
	move.w	d7,d1			 ; grabber memory
	beq.s	hgd_done		 ; ... none
	jsr	hk_grab 		 ; grab
	bra.s	hgd_done

hgd_unlk
	moveq	#iop.pick,d0		 ; pick???
	moveq	#iopp.nlk,d1		 ; ... no lock
	trap	#do.io

hgd_done
	moveq	#0,d0			 ; clear our regs
	moveq	#0,d1
	moveq	#0,d2
	moveq	#0,d3
	moveq	#0,d6
	moveq	#0,d7
	move.l	d0,a0
	move.l	d0,a1

	jmp	(a6)

;*********************************************************
; A6 points to here for hk_guard

hgd_exit
	movem.l (sp)+,reglist
	rts
	end
