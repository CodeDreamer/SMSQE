; DV3 Direct Sector IO	    V3.00		  1992 Tony Tebby

	section dv3

	xdef	dv3_dsect

	xref	dv3_ckro

	include 'dev8_dv3_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_io'
	include 'dev8_mac_assert'

;+++
; Direct Sector IO routines
;
;	d0 c	operation
;	d1 cr	amount transferred / byte / position etc.
;	d2 c	buffer size
;	d5   s	file pointer
;	d7 c  p drive ID / number
;	a0 c	channel base address
;	a1 cr	buffer address
;	a2   s
;	a3 c  p linkage block address
;	a4 c  p pointer to physical definition
;---
dv3_dsect
dds.off equ	$7f
	subq.b	#iob.fmul,d0		 ; is it fetch multiple?
	beq.s	dds_read
	subq.b	#iob.smul-iob.fmul,d0	 ; is it send multiple?
	beq.s	dds_write
	sub.b	#iof.posa-iob.smul,d0	 ; is it position?
	beq.s	dds_posab
	subq.b	#iof.posr-iof.posa,d0	 ; relative?
	beq.s	dds_posre
	moveq	#err.ipar,d0		 ; ... no
	rts

; read a sector

dds_read
	move.l	a1,-(sp)		 ; save pointer
	moveq	#ddl_rsect-dds.off,d3
	bclr	#1,d2			 ; is there a word length at the start?
	beq.s	dds_length

	move.w	ddf_slen(a4),(a1)+	 ; set length
	bra.s	dds_length

; write a sector

dds_write
	jsr	dv3_ckro		 ; read only?

	move.l	a1,-(sp)		 ; save pointer

	moveq	#ddl_wsect-dds.off,d3
	bclr	#1,d2			 ; is there a word length at the start?
	beq.s	dds_length
	addq.l	#2,a1			 ; skip it
dds_length
	moveq	#0,d0
	tst.w	d2			 ; was it just length?
	beq.s	dds_a1

	moveq	#err.ipar,d0
	cmp.w	ddf_slen(a4),d2 	 ; the right length?
	bne.s	dds_a1

; set up for read/write

	move.l	d3c_fpos(a0),d0 	 ; position
	moveq	#1,d2			 ; one sector only
	jsr	dds.off(a3,d3.w)	 ; do it
	bne.s	dds_a1
	add.w	ddf_slen(a4),a1 	 ; ... OK update to end of buffer
dds_a1
	move.l	a1,d1			 ; set d1 to difference in a1
	sub.l	(sp)+,d1
	tst.l	d0
	rts


; set the file position

dds_posab
	move.l	d1,d0
	sub.l	#iofp.off,d0		 ; request first sector offset
	blo.s	dds_posado
	beq.s	dds_posoff
	assert	iofp.off+1,iofp.def
	subq.l	#1,d0
	bne.s	dds_poserr
	moveq	#-1,d1			 ; flag IDE def block
	bra.s	dds_posset

dds_posoff
	move.l	ddf_psoff(a4),d1	 ; return physical sector offset
dds_posado
	tst.l	d1
	bmi.s	dds_poserr
dds_posset
	move.l	d1,d3c_fpos(a0) 	 ; set position
dds_posre
	move.l	d3c_fpos(a0),d1 	 ; read position
dds_posok
	moveq	#0,d0
	rts

dds_poserr
	moveq	#err.ipar,d0
	rts
	end
