; Atari take and release DMA	 1998	   Tony Tebby  QJUMP

	section adma

	xdef	at_takedma
	xdef	at_reldma

	include 'dev8_keys_atari_scc'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'

;+++
; This routine takes the DMA
;
;	d0  r	unchanged or err.nc
;	a6 c  p pointer to system variables
;
;	status return zero or negative
;
;---
at_takedma
	tas	sys_dmiu(a6)		 ; take the dma
	bne.s	att_check		 ; in use or special
	rts

att_check
	blt.s	att_nc			 ; in use

	or.w	#$0700,sr
	move.b	#scc_wint,scc_ctrb
	move.b	#scci.erine,scc_ctrb	 ; rx interrupts only

x	btst	#2,$ffff8c85 !!
	beq.s	x

	and.w	#$f8ff,sr
	cmp.b	d0,d0			 ; return OK
	rts

att_nc
	moveq	#err.nc,d0
	rts

;+++
; This routine releases the DMA
;
;	a6 c  p pointer to system variables
;
;	status return undefined
;---
at_reldma
	btst	#0,sys_dmiu(a6) 	 ; special?
	beq.s	atr_release		 ; ... no

	or.w	#$0700,sr
	move.b	#scc_wint,scc_ctrb
	move.b	#scci.eint+scci.etint+scci.erine,scc_ctrb  ; tx and rx interrupts
     ; we probably need to re-activate here
	and.w	#$f8ff,sr

atr_release
	bclr	#7,sys_dmiu(a6) 	 ; release the dma
	rts

	end
