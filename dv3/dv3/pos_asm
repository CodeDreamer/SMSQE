; DV3 file position routines		   V3.00	   1992 Tony Tebby

	section dv3

	xdef	dv3_posa
	xdef	dv3_posr
	xdef	dv3_posi

	include 'dev8_dv3_keys'
	include 'dev8_keys_err'
	include 'dev8_mac_assert'
;+++
; DV3 set initial file position
;
;	d0    p
;	d1  r	zero
;	d2   s
;	d5  r	sector number of previous sector
;	d6 c  p file id
;	d7 c  p drive number
;	a0 c  p channel base address
;	a2   s
;	a3 c  p linkage block address
;	a4 c  p pointer to physical definition
;
;	status return arbitrary
;---
dv3_posi
	move.l	ddf_fhlen(a4),d2	 ; beginning of file
	move.l	d2,d1			 ; absolute position
	clr.l	d3c_dgroup(a0)		 ; disk group is unset
	moveq	#-1,d0
	move.l	d0,d3c_fgroup(a0)	 ; silly file group number
	bra.s	d3p_spos


;+++
; DV3 relative file position
;
;	d0  r	error status
;	d1 cr	offset / position
;	d2   s	header length
;	d5  r	sector number of previous byte
;	d6 c  p file id
;	d7 c  p drive number
;	a0 c  p channel base address
;	a2   s
;	a3 c  p linkage block address
;	a4 c  p pointer to physical definition
;
;	status return not set
;---
dv3_posr
	move.l	ddf_fhlen(a4),d2	 ; beginning of file
	add.l	d3c_fpos(a0),d1 	 ; absolute position
	bvc.s	d3p_ckef
	bra.s	d3p_eof

;+++
; DV3 absolute file position
;
;	d0  r	error status
;	d1 cr	position
;	d2   s	header length
;	d5  r	sector number of previous byte
;	d6 c  p file id
;	d7 c  p drive number
;	a0 c  p channel base address
;	a2   s
;	a3 c  p linkage block address
;	a4 c  p pointer to physical definition
;
;	status return not set
;---
dv3_posa
	move.l	ddf_fhlen(a4),d2	 ; beginning of file
	add.l	d2,d1			 ; absolute position
	bvs.s	d3p_eof

d3p_ckef
	moveq	#0,d0			 ; assume ok
	cmp.l	d2,d1			 ; beginning of file?
	blt.s	d3p_begf
	cmp.l	d3c_feof(a0),d1 	 ; end of file?
;$$$$	ble.s	d3p_spos		 ; ... no
	blt.s	d3p_spos	; $$$$ prospero

d3p_eof
	move.l	d3c_feof(a0),d1 	 ; end of file
	bra.s	d3p_seof

d3p_begf
	move.l	d2,d1			 ; beginning of file
d3p_seof
	moveq	#err.eof,d0

; set position

d3p_spos
	move.l	d1,d3c_fpos(a0) 	 ; set position
	move.l	d1,d5			 ; save real position
	sub.l	d2,d1			 ; ignore header for return

; we now find the sector number, bearing in mind that, if the next byte is the
; first byte of a sector, the stored sector number is the sector which holds
; the last byte. Therefore, at true beginning of file, the stored sector number
; is -1

	subq.l	#1,d5
	blt.s	d3p_sstart		 ; start of file, special case

	moveq	#7,d4			 ; sector flag 0 is 128 byte sector
	add.b	ddf_slflag(a4),d4
	asr.l	d4,d5

	cmp.l	d3c_fsect(a0),d5	 ; the same sector?
	beq.s	d3p_rts 		 ; ... yes
	move.l	d5,d3
	divu	ddf_asect(a4),d3	 ; assume no more than 65536 groups/file
	cmp.w	d3c_fgroup+2(a0),d3	 ; the same group
	beq.s	d3p_ssect		 ; ... yes

	movem.l d0/d1,-(sp)
	assert	d3c_dgroup,d3c_fgroup-4
	movem.l d3c_dgroup(a0),d0/d1	 ; current drive and file group
	move.w	d3,d2			 ; msw d2 is zero, ignore remainder

	jsr	ddf_slocate(a4)
	blt.s	d3p_oops		 ; oops!!
	assert	d3c_dgroup,d3c_fgroup-4
	movem.l d0/d2,d3c_dgroup(a0)
d3p_exit
	movem.l (sp)+,d0/d1
d3p_ssect
	move.l	d5,d3c_fsect(a0)	 ; set sector
d3p_rts
	tst.l	d0
	rts
d3p_oops
	move.l	d0,(sp)
	bra.s	d3p_exit

d3p_sstart
	clr.l	d3c_dgroup(a0)		 ; no drive group
	move.l	d5,d3c_fgroup(a0)	 ; file group -1
	bra.s	d3p_ssect

	end
