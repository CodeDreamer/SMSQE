; Flip the cursor      V2.00   1998  Tony Tebby
; 2004-03-27	2.01	calls upon cn_spcur
;
;	This routine flips the cursor from visible to invisible, or vice
;	versa.
;
;	on entry to all of these routines, A6 MUST point to the sysvars

	section con
				
	include 'dev8_keys_sys'
	include 'dev8_keys_con'
	include DEV8_keys_jcb

	xref	cn_donl
	xref	cn_ssuba
	xref	cn_xblock
	xref	cn_crcol
	xref	cn_cksize
	xref	cn_spcur

	xdef	cn_ecurs
	xdef	cn_dcurs
	xdef	cn_curtg

cn_dcurs				; disable cursor
	tst.b	sd_curf(a0)		; is cursor suppressed ?
	beq.s	cnc_ok			; ... yes, do nothing

	sf	sd_curf(a0)		; suppressed now
	blt.s	cnc_ok			; was already invisible
	st	sd_curf(a0)		; we want to make it invisible
	bsr.s	cn_curtg		; toggle now
	sf	sd_curf(a0)		; and show suppressed again
cnc_ok
	moveq	#0,d0
	rts

cn_ecurs				; enable cursor
	tst.b	sd_curf(a0)		; cursor already active ?
	bne.s	cnc_ok			; ... yes, do nothing
	tst.b	sd_sflag(a0)		; size OK?
	beq.s	cnec_do 		; ... yes
	bgt.s	cnc_ok			; cursor out of window
	jsr	cn_cksize		; re-check size
	blo.s	cnc_ok			; ... no cursor
cnec_do
	move.b	#1,sd_curf(a0)		; set cursor to active and visible

	tst.b	sd_nlsta(a0)		; newline status
	beq.s	cn_curt0		; ... ok
	jsr	cn_donl 		; ... it is now


; for the cursor toggle routine, since the incorporation of cn_curspr, on entry
; sd_curf(a0) MUST reflect the WISHED new status of the cursor (sprite)
   

cn_curt0
	moveq	#0,d0

curreg	reg	d0-d4/d7/a0-a3
  
cn_curtg				; toggle cursor
	movem.l curreg,-(sp)
cnc_do
	moveq	#cn.bpcur,d3		; just the cursor
	jsr	cn_ssuba(pc)		; find that sub-area
	jsr	cn_spcur		; try with sprite cursor
	beq.s	cn_cuout		;  ... was ok
	move.l	sd_scrb(a0),a1		; get base address...
	move.w	sd_linel(a0),a2 	; ...and row increment
	move.l	cn_crcol,d7
	moveq	#-1,d6			; no stipple

	jsr	cn_xblock		; XOR a block like that
cn_cuout
	movem.l (sp)+,curreg
cnc_exit
	tst.l	d0
	rts
	end
