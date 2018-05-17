; Line edit and utilities	 1992 Tony Teby

	section sbas

	xdef	sb_ledit
	xdef	sb_lfist
	xdef	sb_lfind

	xref	sb_ressr

	include 'dev8_keys_sbasic'
	include 'dev8_keys_err'
	include 'dev8_smsq_sbas_parser_keys'
	include 'dev8_mac_assert'

;+++
; find first line >= given line starting from start of program
;
;	d0  r	actual line number, err.itnf found
;	d1   s
;	d4 c  p line to look for
;	a4  r	pointer to program file
;	****** sb_length returned correct ****** no, it is not set!!
;
;	status return =0    found
;		      >0    beyond
;		      <0    not found
;---
sb_lfist
;****	clr.w	sb_length(a6)		 ; length =0 ******

;+++
; find first line >= given line starting from current position
;
;	d0  r	actual line number, err.itnf found
;	d1  r	(previous) line length
;	d4 c  p line to look for
;	a4 cr	pointer to program file  (=sb_srcep(a6) if d0<0)
;	sb_length must be set correctly = length of previous line *****
;					= zero at start 	  ***** NO!!
;					     updated on return	  *****
;	status return =0    found
;		      >0    beyond
;		      <0    not found
;---
sb_lfind
	move.l	sb_srceb(a6),a4 	 ; base of program    *********
	moveq	#0,d0					      *********

;*****	      move.w  sb_length(a6),d0	       ; length
	addq.w	#2,d0			 ; + link pointer
	cmp.l	sb_srcep(a6),a4 	 ; at end of program file?
	bge.s	sblf_any		 ; ... yes, can only go back, if at all
	cmp.w	4(a6,a4.l),d4		 ; look at line number
	blt.s	sblf_back		 ; past, go back
	beq.s	sblf_set

	move.l	sb_srcep(a6),d1 	 ; do not search beyond here
sblf_upl
	add.w	(a6,a4.l),d0		 ; new line length
	add.w	d0,a4			 ; next line
	cmp.l	d1,a4
	bge.s	sblf_nf 		 ; no more
	cmp.w	4(a6,a4.l),d4		 ; the right line
	bgt.s	sblf_upl
	bra.s	sblf_set

sblf_any
	move.l	sb_srceb(a6),d1 	 ; base of program
	cmp.l	d1,a4			 ; any file at all
	beq.s	sblf_nf 		 ; ... no

sblf_back
	cmp.l	d1,a4			 ; at start?
	ble.s	sblf_set
	sub.w	d0,a4			 ; go back
	cmp.w	4(a6,a4.l),d4		 ; is it previous line?
	beq.s	sblf_sbk		 ; ... yes keep it
	bgt.s	sblf_nxt		 ; ... no it must be the next
	sub.w	(a6,a4.l),d0		 ; new length
	bra.s	sblf_back		 ; ... no

sblf_sbk
	sub.w	(a6,a4.l),d0		 ; set new length
	bra.s	sblf_set

sblf_nxt
	add.w	d0,a4			 ; it is the next
	cmp.l	sb_srcep(a6),a4 	 ; is next off end?
	bge.s	sblf_nf 		 ; ... yes

sblf_set
	subq.w	#2,d0			 ; actual line length
;******        move.w  d0,sb_length(a6)
	move.w	d0,d1
	move.w	4(a6,a4.l),d0
	cmp.w	d4,d0			 ; set condition codes

	rts

sblf_nf
	bsr.s	sblf_set
	moveq	#err.itnf,d0
sblf_rts
	rts

;+++
; Edit line into program file
;
; NOTE: this routine assumes that there is a spare word after the end of the
;	program file for a dummy link pointer: respf must ensure that this is
;	true.
;
;	d1/d2/d3/d4 all smashed
;	a1   s	pointer to token list
;	a0   s
;	a4  r	pointer to next line
;
;	return normal: not done, no line number
;	       +2    : ok
;---
sb_ledit
	move.l	sb_cmdlb(a6),a1 	 ; line to put in
	move.l	sb_cmdlp(a6),d2 	 ; end
	sub.l	a1,d2			 ; ... thus length
	beq.s	sblf_rts		 ; ... no line

	cmp.w	#tkw.lno,(a6,a1.l)	 ; starts with line number?
	bne.s	sblf_rts		 ; ... no

	assert	sb.edt,$ff
	st	sb_edt(a6)		 ; program edited

	move.w	2(a6,a1.l),d4		 ; line number

	move.l	sb_srcep(a6),a4 	 ; start looking at end
	moveq	#2,d0
	sub.w	(a6,a4.l),d0		 ; line length

	bsr	sblf_any		 ; find line
	bne.s	sble_add		 ; add to program

	subq.l	#8,d2
	blt.s	sble_del		 ; delete, no line length
	addq.l	#8,d2			 ; ... replace, use proper length

sble_del
	move.l	d2,d4			 ; keep length safe (-2 for delete)
	move.l	d2,d5
;*****	      move.w  sb_length(a6),d1
	move.w	(a6,a4.l),d0		 ; old difference
	add.w	d0,d1			 ; old line length
	lea	2(a4,d1.w),a0
	add.w	d0,(a6,a0.l)		 ; eliminate old line
	sub.w	d1,d5			 ; difference between new and old
	sub.w	d0,d1			 ; (restore previous line length)
	ext.l	d5
	bgt.s	sble_mup		 ; ... move up

	move.l	a0,d3			 ; next line
	add.l	d5,d3			 ; move down to here
	move.l	sb_srcep(a6),d0
	add.l	d5,d0			 ; new top
	move.l	d0,sb_srcep(a6) 	 ; new top
	sub.l	d3,d0

sble_dlp
	move.l	(a6,a0.l),(a6,d3.l)	 ; move down
	addq.l	#2,a0
	addq.l	#2,d3
	subq.l	#2,d0
	bge.s	sble_dlp		 ; (one extra word)

	bra.s	sble_link		 ; re-link and copy

sble_add
	cmp.w	#6,d2			 ; is it just line number
	ble.s	sble_ok 		 ; ... yes, done
	move.l	d2,d4
	addq.w	#2,d2			 ; we need the link word as well
	move.l	d2,d5			 ; save difference

sble_mup
	add.l	sb_srcep(a6),d2
	cmp.l	sb_srcet(a6),d2 	 ; allocate a bit?
	blt.s	sble_mum		 ; ... no

	sub.l	sb_srceb(a6),a4 	 ; make source pointer relative to base
	move.w	d1,-(sp)
	move.l	d5,d1
	jsr	sb_ressr		 ; and reserve space in program source
	move.w	(sp)+,d1
	add.l	sb_srceb(a6),a4

sble_mum
	move.l	sb_srcep(a6),a0 	 ; top end to move
	add.l	d5,sb_srcep(a6)
	move.l	a0,d0
	sub.l	a4,d0			 ; bytes to move
	move.l	d5,d3
	add.l	a0,d3			 ; where we want it

sble_ulp
	move.w	(a6,a0.l),(a6,d3.l)	 ; copy including dummy word at end
	subq.l	#2,a0
	subq.l	#2,d3
	subq.l	#2,d0
	bge.s	sble_ulp

sble_link
	move.w	d4,d3			 ; new line length
	ble.s	sble_ok 		 ; ... none

;****	   sub.w   sb_length(a6),d3	    ; difference
	sub.w	d1,d3			 ; difference
	move.w	d3,(a6,a4.l)		 ; in link
	lsr.w	#1,d4
	bra.s	sble_cle
sble_clp
	addq.l	#2,a4
	move.w	(a6,a1.l),(a6,a4.l)	 ; copy new line in
	addq.l	#2,a1
sble_cle
	dbra	d4,sble_clp

	sub.w	d3,2(a6,a4.l)		 ; adjust next link length

sble_ok
	addq.l	#2,(sp)
sble_rts
	rts
	end
