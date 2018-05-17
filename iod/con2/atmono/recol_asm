;	Recolour a window	v0.00   1991  Tony Tebby
;
;	Monochrome
;
;	Registers:
;		Entry				Exit
;	D0					0
;	D1-D7					smashed
;	A0	CDB				preserved
;	A1	recolour list			smashed
;	A2/A5					smashed
;
;	All other registers preserved
;
	section con
;
	include 'dev8_keys_con'
;
	xref	cn_xblock
;
	xdef	cn_recol
;
cn_recol
	move.b	7(a1),d0
	cmp.b	(a1),d0 		; normal order?
	bgt.s	cnr_ok

	movem.l sd_xmin(a0),d1/d2	; set area to clear
	exg	d1,d2
	moveq	#0,d6
	moveq	#-1,d7			; xor with -1

	move.l	sd_scrb(a0),a1		; set base address
	move.w	sd_linel(a0),a2 	; and row increment
;
	jsr	cn_xblock(pc)		; xor the block
;
cnr_ok
	moveq	#0,d0
	rts
	end
