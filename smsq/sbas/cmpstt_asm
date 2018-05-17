; SBAS_CMPSTT - Compile Statement Table     V2.00     1994 Tony Tebby
;
; 2003-02-03  2.01  Fixed bug in the handling of dynamically sized compiler
;		    tokens (i.e. only bo.formp).
;		    In this case a PROCedure with 358 parameters (sic! I'm not
;		    kidding, this was real life code!) crashed SBasic (MK)

	section sbas

	xdef	sb_cmpstt
	xdef	sb_cmpdst

	xref	sb_alwrk
	xref	sb_ixtable

	include 'dev8_keys_sbasic'

;+++
; SBASIC Compile Data Statement Table.
; This also copies the program, eliminating the statement and newlines
;
;	a6 c  p pointer to SuperBASIC variables
;
;	All other registers smashed
;	Status return standard
;---
sb_cmpdst
	lea	sb_dttbe(a6),a1 	 ; statement table estimated work area
	move.l	sb_dtstb(a6),a3 	 ; program token
	move.l	sb_dtstp(a6),a4 	 ; and top
	bsr.s	sbcs_do
	move.l	a3,sb_dtstp(a6) 	 ; and where it is
	move.l	a0,sb_dttbp(a6) 	 ; set running pointers
	move.l	(a0)+,(a0)		 ; set top address = -1
	moveq	#0,d0
	rts

;+++
; SBASIC Compile Statement Table.
; This also copies the program, eliminating the statement and newlines
;
;	a5 c  p pointer to compiler token base
;	a6 c  p pointer to SuperBASIC variables
;
;	All other registers smashed
;	Status return standard
;---
sb_cmpstt
	lea	sb_sttbe-sb_ptokb(a5),a1 ; statement table estimated work area
	move.l	sb_progb-sb_ptokb(a5),a3 ; program token
	move.l	sb_progp-sb_ptokb(a5),a4 ; and top
	bsr.s	sbcs_do
	move.l	a3,sb_progp-sb_ptokb(a5) ; and where it is
	move.l	a0,sb_sttbp-sb_ptokb(a5) ; set running pointers
	subq.l	#2,a3
	move.l	a3,4(a0)		 ; set top address = end - 2
	moveq	#0,d0
	rts


sbcs_do
	moveq	#st.len*2,d0		 ; start and end sentinals
	add.l	(a1)+,d0		 ; + estimated work area
	jsr	sb_alwrk		 ; allocate it

	move.l	a3,a1			 ; where to copy it to
	lea	sb_ixtable,a2		 ; extension table

	addq.l	#st_addr,a0		 ; line 0
	move.l	a3,(a0)+		 ; is start sentinal

scs_loop
	cmp.l	a4,a1			 ; all processed?
	beq.s	scs_done		 ; ... yes

	move.w	(a1)+,d0		 ; next token
	ble.s	scs_statement
	move.w	d0,(a3)+
scs_exten
	move.w	(a2,d0.w),d0		 ; flag + number of extension words
	beq.s	scs_loop
	ext.w	d0			 ; fixed?
	bgt.s	scs_copy
	move.w	(a1)+,d0		 ; count
	move.w	d0,(a3)+
	beq.s	scs_loop		 ; ... none
	add.w	d0,d0			 ; in words
scs_copy
	move.w	(a1)+,(a3)+		 ; 2 byte
	subq.w	#2,d0			 ; ** in 2.01
	bgt.s	scs_copy
	bra.s	scs_loop

scs_statement
	beq.s	scs_nstmt		 ; next statement
	moveq	#0,d1			 ; statement on line
	move.w	d0,d1
	not.w	d1			 ; line number
	swap	d1
scs_nstmt
	addq.w	#1,d1
	tst.w	(a1)			 ; is next token another end of stt
	ble.s	scs_loop		 ; ... yes, do not creat dummy entry
	move.l	d1,(a0)+		 ; set statement
	move.l	a3,(a0)+		 ; and where it is
	bra.s	scs_loop

scs_done
	not.l	(a0)			 ; top sentinal
	rts

	end
