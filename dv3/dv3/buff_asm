; DV3 IO buffering   V3.01     1989 / 1993 Tony Tebby
;
; 2014-03-19  3.01  Added read-only check in dv3_sbyt (MK)

	section dv3

	xdef	dv3_tbyt
	xdef	dv3_fbyt
	xdef	dv3_sbyt

	xdef	dv3_fmul
	xdef	dv3_flin
	xdef	dv3_smul

	xdef	dv3_sload
	xdef	dv3_ssave

	xdef	dv3_slbupd
	xdef	dv3_a1upd

	xref	dv3_ckro

	xref	dv3_lcbf
	xref	dv3_albf

	xref	dv3_posa

	include 'dev8_dv3_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_k'
	include 'dev8_keys_hdr'

; Fetch or test byte setup

dbfb_loc
	move.l	d3c_fpos(a0),d4 	 ; position
	cmp.l	d3c_feof(a0),d4 	 ; length to end of file
	beq.s	db_eof			 ; ... end of file

	jmp	dv3_lcbf		 ; locate buffer

;+++
; Fetch byte from current position in file d6, drive d7, preserving the file
; pointers
;
;	d0  r	error return
;	d1  r	next byte
;	d2   s	amount left to be fetched
;	d4  r	file position
;	d5   s	sector number
;	d6 c  p file id
;	d7 c  p drive number
;	a1    p
;	a2   s	internal buffer address
;	a3 c  p linkage block address
;	a4 c  p pointer to physical definition
;	a5   s
;
;	status return standard
;---
dv3_tbyt
	move.l	d3c_fsect(a0),-(sp)	 ; current sector updated automatically
	bsr	dbfb_loc		 ; locate
	bne.s	dbt_exit		 ; ... oops
	move.b	(a2),d1 		 ; fetch byte
dbt_exit
	move.l	(sp)+,d3c_fsect(a0)	 ; current sector restored
	rts

;+++
; Fetch byte from current position in file d6, drive d7
;
;	d0  r	error return
;	d1  r	next byte
;	d2   s	amount left to be fetched
;	d4  r	file position
;	d5   s	sector number
;	d6 c  p file id
;	d7 c  p drive number
;	a1    p
;	a2   s	internal buffer address
;	a3 c  p linkage block address
;	a4 c  p pointer to physical definition
;
;	status return standard
;---
dv3_fbyt
	bsr	dbfb_loc		 ; locate
	bne.s	dbfb_exit		 ; ... oops
	move.b	(a2),d1 		 ; fetch byte
	addq.l	#1,d4			 ; move on to next byte
	move.l	d4,d3c_fpos(a0) 	 ; set current pointer
dbfb_exit
	rts

;+++
; Load all of file from file d6, drive d7
;
;	d0 cr	error return on buffer full / error return
;	d1 c  u byte count
;	d2 c  u amount left to be fetched
;	d3   s	amount to be fetched this time
;	d4  r	file position
;	d5   s	sector number
;	d6 c  p file id
;	d7 c  p drive number
;	a1 c  u buffer address
;	a2   s	internal buffer address
;	a3 c  p linkage block address
;	a4 c  p pointer to physical definition
;
;	status return standard
;---
dv3_sload
	move.l	#0,d1
	jsr	dv3_posa		 ; set position to start
	move.l	d3c_feof(a0),d2 	 ; whole of file
	sub.l	d3c_fpos(a0),d2 	 ; ... real end

;+++
; Fetch d2 bytes from current position in file d6, drive d7
;
;	d0 cr	error return on buffer full / error return
;	d1 c  u byte count
;	d2 c  u amount left to be fetched
;	d3   s	amount to be fetched this time
;	d4  r	file position
;	d5   s	sector number
;	d6 c  p file id
;	d7 c  p drive number
;	a1 c  u buffer address
;	a2   s	internal buffer address
;	a3 c  p linkage block address
;	a4 c  p pointer to physical definition
;
;	status return standard
;---
dv3_fmul
	sub.l	d1,d2			 ; max left to read
	beq.s	dbfm_rtok		 ; ... nothing

	move.l	d3c_feof(a0),d3 	 ; end of file
	move.l	d3c_fpos(a0),d4
	sub.l	d4,d3			 ; length to end of file
	beq.s	db_eof			 ; ... none
	cmp.l	d2,d3			 ; enough?
	bhs.s	dbfm_do 		 ; ... enough
	move.l	d3,d2			 ; ... end of file
	bsr.s	dbfm_do
	bne.s	dbfm_rts
db_eof
	moveq	#err.eof,d0
dbfm_rts
	rts

dbfm_rtok
dbfl_rtok
	moveq	#0,d0
	rts

dbfm_do

; loop unbuffering blocks

dbfm_next
	jsr	dv3_lcbf		 ; locate buffer
	bne.s	dbfm_exit		 ; ... oops

	cmp.l	d3,d2			 ; all to be fetched?
	bhs.s	dbfm_unbf		 ; ... yes
	move.l	d2,d3

; unbuffer d3 bytes

dbfm_unbf
	move.l	a1,d0			 ; destination address
	or.w	d4,d0			 ; or source address (ish)

	add.l	d3,d1			 ; more fetched (or will be)
	add.l	d3,d4			 ; new buffer pointer
	sub.l	d3,d2			 ; amount left

	lsr.w	#1,d0			 ; odd?
	bcs.s	dbfm_bend		 ; ... yes, just copy bytes

	ror.l	#5,d3			 ; copy 8 long words at a time
	bra.s	dbfm_lwend

dbfm_lword
	move.l	(a2)+,(a1)+		 ; copy long word
	move.l	(a2)+,(a1)+		 ; copy long word
	move.l	(a2)+,(a1)+		 ; copy long word
	move.l	(a2)+,(a1)+		 ; copy long word
	move.l	(a2)+,(a1)+		 ; copy long word
	move.l	(a2)+,(a1)+		 ; copy long word
	move.l	(a2)+,(a1)+		 ; copy long word
	move.l	(a2)+,(a1)+		 ; copy long word
dbfm_lwend
	dbra	d3,dbfm_lword

	clr.w	d3
	rol.l	#5,d3			 ; amount remaining
	bra.s	dbfm_bend

dbfm_byte
	move.b	(a2)+,(a1)+		 ; bytes at a time
dbfm_bend
	dbra	d3,dbfm_byte

	move.l	d2,d0			 ; any more to go?
	bne.s	dbfm_next		 ; ... yes

dbfm_exit
	move.l	d4,d3c_fpos(a0) 	 ; set current pointer
	tst.l	d0
	rts

	page
;+++
; Fetch d2 bytes, stopping at <NL> or <CR><NL>, from file d6, drive d7
;
;	d0 cr	error return on buffer full / error return
;	d1 c  u byte count
;	d2 c  u amount left to be fetched
;	d3   s	amount to be fetched this time
;	d4  r	file position
;	d5   s	sector number
;	d6 c  p file id
;	d7 c  p drive number
;	a1 c  u buffer address
;	a2   s	internal buffer address
;	a3 c  p linkage block address
;	a4 c  p pointer to physical definition
;
;	status return standard
;---
dv3_flin
	moveq	#err.bffl,d0		 ; buffer full is error
	sub.l	d1,d2			 ; max left to read
	beq.s	dbfl_rtok		 ; ... nothing

	move.l	d3c_feof(a0),d3 	 ; end of file
	move.l	d3c_fpos(a0),d4
	sub.l	d4,d3			 ; length to end of file
	beq.s	db_eof			 ; ... none
	cmp.l	d2,d3			 ; enough?
	bhs.s	dbfl_do 		 ; ... enough
	move.l	d3,d2			 ; ... end of file
	moveq	#err.eof,d0

dbfl_do
	move.l	d0,-(sp)		 ; error return on buffer full

; loop unbuffering but checking for end of line <NL> or <CR><NL>

dbfl_next
	jsr	dv3_lcbf		 ; locate next buffer
	bne.s	dbfl_err		 ; ... oops

	cmp.l	d3,d2			 ; all to be fetched?
	bhs.s	dbfl_unbf		 ; ... yes
	move.l	d2,d3

; unbuffer up to d3 bytes

dbfl_unbf
	move.l	d3,d0			 ; max amount to copy
	beq.s	dbfl_cont
	subq.w	#1,d0
	moveq	#k.nl,d5		 ; character to check

dbfl_byte
	move.b	(a2)+,(a1)		 ; copy byte
	cmp.b	(a1)+,d5		 ; was it end of line?
dbfl_bend
	dbeq	d0,dbfl_byte		 ; next
	beq.s	dbfl_nl 		 ; ... <NL> found

dbfl_cont
	add.l	d3,d1			 ; more fetched
	add.l	d3,d4			 ; update pointer
	sub.l	d3,d2			 ; any more?
	bne.s	dbfl_next		 ; ... yes

	move.l	(sp)+,d0		 ; error on buffer full / end of file
	bra.s	dbfl_exit

dbfl_nl
	sub.w	d0,d3			 ; amount actually fetched
	add.l	d3,d1
	add.l	d3,d4			 ; update pointer

	cmp.b	#1,d1			 ; at least two chars?
	ble.s	dbfl_ok

	cmp.b	#k.cr,-2(a1)		 ; <CR><NL>?
	bne.s	dbfl_ok 		 ; ... no
	subq.w	#2,a1
	move.b	d5,(a1)+		 ; ... yes, reset to <NL>
	subq.w	#1,d1			 ; number fetched

dbfl_ok
	moveq	#0,d0			 ; done, no error
dbfl_err
	addq.l	#4,sp			 ; not default error return
dbfl_exit
	move.l	d4,d3c_fpos(a0) 	 ; set current pointer
	tst.l	d0
	rts

;+++
; Save d2 bytes to (start of file) current position in file d6, drive d7
;
;	d1 c  u byte count
;	d2 c  u amount left to be sent
;	d3   s	amount to be sent this time
;	d4  r	file position
;	d5   s	sector number
;	d6 c  p file id
;	d7 c  p drive number
;	a1 cr	buffer address
;	a2   s	internal buffer address
;	a3 c  p linkage block address
;	a4 c  p pointer to physical definition
;
;	status return standard
;---
dv3_ssave
;**	   move.l  d2,-(sp)
;**	   move.l  #0,d1
;**	   jsr	   dv3_posa		    ; set position to start
;**	   move.l  (sp)+,d2		    ; d2's worth of file

;+++
; Send d2 bytes to current position in file d6, drive d7
;
;	d1 c  u byte count
;	d2 c  u amount left to be sent
;	d3   s	amount to be sent this time
;	d4  r	file position
;	d5   s	sector number
;	d6 c  p file id
;	d7 c  p drive number
;	a1 cr	buffer address
;	a2   s	internal buffer address
;	a3 c  p linkage block address
;	a4 c  p pointer to physical definition
;
;	status return standard
;---
dv3_smul
	jsr	dv3_ckro		 ; check read only
	sub.l	d1,d2			 ; amount left to send
	ble.s	dbsm_rtok		 ; ... none
	move.l	d3c_fpos(a0),d4 	 ; file position

; loop buffering blocks

dbsm_next
	jsr	dv3_albf		 ; locate / allocate buffer
	bne.s	dbsm_exit		 ; ... oops

	cmp.l	d3,d2			 ; all to be sent?
	bhs.s	dbsm_unbf		 ; ... yes
	move.l	d2,d3

; buffer d3 bytes

dbsm_unbf
	move.l	a1,d0			 ; source
	or.w	d4,d0			 ; or destination

	add.l	d3,d1			 ; more sent (or will be)
	add.l	d3,d4			 ; new file pointer
	sub.l	d3,d2			 ; amount left

	lsr.w	#1,d0			 ; source or destination odd?
	bcs.s	dbsm_bend		 ; ... yes, just copy bytes

	ror.l	#5,d3			 ; copy 8 long words at a time
	bra.s	dbsm_lwend

dbsm_lword
	move.l	(a1)+,(a2)+		 ; copy long word
	move.l	(a1)+,(a2)+		 ; copy long word
	move.l	(a1)+,(a2)+		 ; copy long word
	move.l	(a1)+,(a2)+		 ; copy long word
	move.l	(a1)+,(a2)+		 ; copy long word
	move.l	(a1)+,(a2)+		 ; copy long word
	move.l	(a1)+,(a2)+		 ; copy long word
	move.l	(a1)+,(a2)+		 ; copy long word
dbsm_lwend
	dbra	d3,dbsm_lword

	clr.w	d3
	rol.l	#5,d3			 ; amount remaining
	bra.s	dbsm_bend

dbsm_byte
	move.b	(a1)+,(a2)+		 ; copy byte
dbsm_bend
	dbra	d3,dbsm_byte
	move.l	a1,d0
	bsr.s	dv3_slbupd
	move.l	d0,a1
	move.l	d2,d0			 ; any more to go?
	bne.s	dbsm_next		 ; ... yes

dbsm_exit
	move.l	d4,d3c_fpos(a0) 	 ; set file position
	cmp.l	d3c_feof(a0),d4 	 ; new end of file?
	bls.s	dbsm_rtd0		 ; ... no
	move.l	d4,d3c_feof(a0) 	 ; set end of file
dbsm_rtd0
	tst.l	d0
dbsb_rts
	rts

dbsm_rtok
	moveq	#0,d0
	rts

;+++
; Send byte to current position in file d6, drive d7
;
;	d0  r	error return
;	d1  r	next byte
;	d2   s	amount left to be fetched
;	d4  r	file position
;	d5   s	sector number
;	d6 c  p file id
;	d7 c  p drive number
;	a1    p
;	a2   s	internal buffer address
;	a3 c  p linkage block address
;	a4 c  p pointer to physical definition
;
;	status return standard
;---
dv3_sbyt
	jsr	dv3_ckro		 ; check read only
	move.l	d3c_fpos(a0),d4 	 ; file position
	jsr	dv3_albf		 ; locate / allocate buffer
	bne.s	dbsb_rts		 ; ... oops
	move.b	d1,(a2) 		 ; copy byte
	addq.l	#1,d4			 ; next buffer position
	move.l	d4,d3c_fpos(a0) 	 ; set file position
	cmp.l	d3c_feof(a0),d4 	 ; new end of file?
	bls.s	dbsb_upd		 ; ... no
	move.l	d4,d3c_feof(a0) 	 ; set end of file
dbsb_upd
;***	drop through to dv3_slbupd

;+++
; Mark current slave block updated
;
;	a1 s
;	all other registers preserved
;	status according to d0
;---
dv3_slbupd
	move.l	d3c_csb(a0),a1		 ; slave block
;+++
; Mark slave block (a1) updated
;
;	all registers preserved
;	status according to d0
;---
dv3_a1upd
	bset	#sbt..wrq,sbt_stat(a1)	 ; 1st sector updated
	bne.s	dsu_rtd0		 ; it was already

	cmp.w	#1,ddf_xslv(a4) 	 ; 512, 1024 or 2048 bytes / sector?
	blt.s	dsu_l
	beq.s	dsu_1024
	bset	#sbt..wrq,sbt_stat+3*sbt.len(a1) ; 4th sector updated
	bset	#sbt..wrq,sbt_stat+2*sbt.len(a1) ; 3rd sector updated
dsu_1024
	bset	#sbt..wrq,sbt_stat+1*sbt.len(a1) ; 2nd sector updated
	bset	#sbt..wrq,sbt_stat+0*sbt.len(a1) ; 1st sector updated (beware)

dsu_l
	cmp.l	ddf_slbl(a4),a1 	 ; below range?
	bhs.s	dsu_h
	move.l	a1,ddf_slbl(a4) 	 ; set low range
dsu_h
	cmp.l	ddf_slbh(a4),a1 	 ; above range?
	bls.s	dsu_mark
	move.l	a1,ddf_slbh(a4) 	 ; set high range
dsu_mark
	jmp	ddl_slbupd(a3)		 ; mark updated medium

dsu_rtd0
	tst.l	d0
	rts
	end
