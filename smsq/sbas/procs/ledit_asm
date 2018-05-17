; Line editing utilities        1992 Tony Teby

	section exten

	xdef	dline
	xdef	renum

	xref	ut_gtint
	xref	ut_gtin1
	xref	gu_achp0
	xref	gu_rchp

	xref	qa_nint
	xref	qa_float

	include 'dev8_keys_sbasic'
	include 'dev8_keys_err'
	include 'dev8_smsq_sbas_parser_keys'
	include 'dev8_mac_assert'

sb_ckrets
	move.l	sb_retsp(a6),d0
	sub.l	sb_retsb(a6),d0 	 ; return stack empty?
	beq.s	scr_rts 		 ; ... yes
	addq.l	#4,sp
	st	sb_redo(a6)
scr_rts
	rts


sb_chok
	moveq	#0,d0
;+++
; Mark program changed
;---
sb_change
	assert	sb.edt,$ff
	st	sb_edt(a6)		 ; program changed
	sf	sb_cont(a6)		 ; do not continue
	move.w	#sb.nact,sb_actn(a6)	 ; but no action
	tst.l	d0
	rts

;+++
; RENUM low TO high; start,increment
;
renum
	jsr	sb_ckrets		 ; check procedure stack empty
	move.l	#$00017fff,d5		 ; whole range
	move.l	#$0064000a,d4		 ; 100, 10
	move.l	a5,d7			 ; save top

	move.b	nt_vtype(a6,a3.l),d1	 ; check separator on first
	lsr.b	#nt.seps,d1

	move.l	a5,d0
	sub.l	a3,d0			 ; any parameters?
	ble.l	re_do			 ; ... no

	tst.b	d1			 ; separator on first
	beq.s	re_start		 ; start only
	subq.b	#nt.comma,d1
	beq.s	re_stinc		 ; start and increment
	subq.b	#nt.smicl-nt.comma,d1
	beq.s	re_low			 ; low only
	subq.b	#nt.to-nt.smicl,d1
	bne.s	re_ipar

	lea	re_to,a4		 ; range, next is to
	bra.s	re_glow
re_low
	lea	re_stchk,a4		 ; low only, next is start
re_glow
	move.w	#$0f0f,d0
	and.w	nt_usetp(a6,a3.l),d0	 ; null low?
	beq.s	re_lowd 		 ; ... yes

	bsr.s	re_gtint		 ; get one integer
	swap	d1
	move.w	d5,d1
	move.l	d1,d5			 ; set low
	cmp.l	d7,a5			 ; all params done?
	bge.s	re_do			 ; ... yes

re_lowd
	addq.l	#nt.len,a3
	move.b	nt_vtype(a6,a3.l),d1	 ; check separator on next
	lsr.b	#nt.seps,d1
	jmp	(a4)			 ; either TO or start check



re_ipar
	moveq	#err.ipar,d0
re_rtsa
	rts
re_gtint
	lea	nt.len(a3),a5		 ; one param only
	jsr	ut_gtint
	move.l	(sp)+,a2
	bne.s	re_rtsa
	move.w	(a6,a1.l),d1
	blt.s	re_ipar
	jmp	(a2)


re_to
	beq.s	re_high 		 ; last param
	subq.b	#nt.smicl,d1		 ; must be semicolon
	bne.s	re_ipar
re_high
	move.w	#$0f0f,d0
	and.w	nt_usetp(a6,a3.l),d0	 ; null high?
	beq.s	re_highd		 ; ... yes
	bsr.s	re_gtint		 ; get one integer
	move.w	d1,d5			 ; set high
	cmp.l	d7,a5			 ; all params done?
	bge.s	re_do			 ; ... yes

re_highd
	addq.l	#nt.len,a3
	move.b	nt_vtype(a6,a3.l),d1	 ; check separator on next
	lsr.b	#nt.seps,d1
re_stchk
	beq.s	re_start		 ; start only
	subq.b	#nt.comma,d1		 ; must be comma
	bne.s	re_ipar 		 ; ... no

re_stinc
	lea	re_inc,a4		 ; look at inc after start
	bra.s	re_gstart
re_start
	lea	re_do,a4
re_gstart
	move.w	#$0f0f,d0
	and.w	nt_usetp(a6,a3.l),d0	 ; null start?
	beq.s	re_startd		 ; ... yes

	bsr.s	re_gtint		 ; get one integer
	swap	d1
	move.w	d4,d1
	move.l	d1,d4			 ; set start

re_startd
	addq.l	#nt.len,a3
	jmp	(a4)

re_inc
	lea	nt.len(a3),a5		 ; increment must be last parameter
	cmp.l	d7,a5
	bne.s	re_ipar

	move.w	#$0f0f,d0
	and.w	nt_usetp(a6,a3.l),d0	 ; null increment?
	beq.s	re_do			 ; ... yes

	bsr.s	re_gtint		 ; get one integer
	move.w	d1,d4			 ; set increment

;***
; We now have the start and increment in d4 and the range in d5
;---
re_do

; first we make the table - stage 1 is counting the lines

	move.l	sb_srceb(a6),a4 	 ; base of program
	add.l	a6,a4
	moveq	#2,d0
	move.l	sb_srcep(a6),a5 	 ; do not scan beyond here
	add.l	a6,a5
	moveq	#0,d7			 ; number of lines
	bra.s	re_cntnext
re_cntlin
	addq.l	#4,d7
	add.w	(a4),d0 		 ; new line length
	add.w	d0,a4			 ; next line
re_cntnext
	cmp.l	a5,a4
	blt.s	re_cntlin		 ; more

	move.l	d7,d0			 ; space required
	bne.s	re_achp 		 ; ... some
re_rtsb
	rts
re_achp
	addq.l	#8,d0			 ; two sentinals
	addq.l	#4,d7			 ; but only lower is in search
	jsr	gu_achp0
	bne.s	re_rtsb 		 ; oops, no space
	move.l	a0,d6			 ; keep base

;			  - stage two is filling it in

	clr.l	(a0)+			 ; sentinal is old line 0, new line 0
	move.l	sb_srceb(a6),a4 	 ; base of program
	add.l	a6,a4
	moveq	#2,d0
	swap	d5			 ; low range
re_fill_low
	move.w	4(a4),d1
	cmp.w	d5,d1			 ; in range?
	bge.s	re_chkl 		 ; ... yes
	move.w	d1,(a0)+		 ; old line number
	move.w	d1,(a0)+		 ; and new line number
	add.w	(a4),d0 		 ; new line length
	add.w	d0,a4			 ; next line
	cmp.l	a5,a4
	blt.s	re_fill_low		 ; more
re_exit_ok1
	bra.l	re_exit_ok

re_chkl
	move.l	d4,d3			 ; increment in d4
	swap	d3			 ; start value in d3
	cmp.w	-2(a0),d3		 ; is start below or at previous?
	ble.s	re_ornga		 ; ... yes

	swap	d5
	cmp.w	d5,d1			 ; already above range?
	bgt.s	re_exit_ok1

re_set_range
	move.w	4(a4),d1
	cmp.w	d5,d1			 ; in range?
	bgt.s	re_chkh 		 ; ... no
	move.w	d1,(a0)+		 ; old line number
	move.w	d3,(a0)+		 ; and new line number
	add.w	d4,d3
	bvs.l	re_orng 		 ; oops, overflow
	add.w	(a4),d0 		 ; new line length
	add.w	d0,a4			 ; next line
	cmp.l	a5,a4
	blt.s	re_set_range		 ; more
	bra.s	re_setlno

re_chkh
	cmp.w	d3,d1			 ; is next above new line number
re_ornga
	ble.l	re_orng 		 ; ... no

re_fill_high
	move.w	4(a4),d1
	move.w	d1,(a0)+		 ; old line number
	move.w	d1,(a0)+		 ; and new line number
	add.w	(a4),d0 		 ; new line length
	add.w	d0,a4			 ; next line
	cmp.l	a5,a4
	blt.s	re_fill_high		 ; more

re_setlno
	move.l	#$ffff7fff,(a0) 	 ; upper sentinal is 65535/32767
	move.l	sb_srceb(a6),a4 	 ; base of program
	add.l	a6,a4
	moveq	#2,d0
	move.l	d6,a0
	addq.l	#4,a0			 ; skip sentinal

re_setl_loop
	move.l	(a0)+,d1
	move.w	d1,4(a4)		 ; old line number
	add.w	(a4),d0 		 ; new line length
	add.w	d0,a4			 ; next line
	cmp.l	a5,a4
	blt.s	re_setl_loop		 ; more

; The table is set up, we now need to scan the program looking for GOTO GOSUB
; and restores

	move.l	d5,d4
	swap	d4			 ; low range in d4, high in d5

	move.l	sb_srceb(a6),a4 	 ; base of program
	lea	6(a6,a4.l),a4		 ; first real token
	move.l	sb_arthb(a6),a1
	add.l	a6,a1			 ; stack

re_sstate
	bsr.s	re_ntok
	cmp.b	#tkb.key,d0		 ; keyword?
	bne.s	re_ends 		 ; ... no, try next
	move.w	(a4)+,d0
	subq.b	#tkk.if,d0		 ; IF is skip expression
	beq.s	re_if
	subq.b	#tkk.go-tkk.if,d0	 ; GO TO / GO SUB is classic
	beq.s	re_tosub
	subq.b	#tkk.rest-tkk.go,d0	 ; restore is too
	beq.s	re_rest
	subq.b	#tkk.else-tkk.rest,d0	 ; else is the start of a statement
	beq.s	re_sstate
	subq.b	#tkk.on-tkk.else,d0	 ; on expr
	bne.s	re_ends

	bsr.s	re_skpexp		 ; skip expression
	bne.s	re_ends 		 ; not a keyword after expression!!
	cmp.b	#tkk.go,d0		 ; is it GO?
	bne.s	re_ends
	bsr.s	re_ntok 		 ; get TO or SUB
	addq.l	#2,a4			 ; and skip it
re_onloop
	bsr.s	re_ntok
	cmp.b	#$f0,d0 		 ; starts with floating point?
	blo.s	re_onnext
	bsr.l	re_update		 ; update address
	subq.l	#6,a4			 ; reset pointer
re_onnext
	bsr.s	re_skpexp
	cmp.b	#tks.cmma,d0		 ; comma?
	beq.s	re_onloop		 ; ... yes
	bra.s	re_nxts 		 ; ... no, we've reached the end

re_if
	bsr.s	re_skpexp		 ; skip expression
	bne.s	re_nxts 		 ; the end of the statement
	bra.s	re_sstate		 ; should have been THEN

re_tosub
	bsr.s	re_ntok 		 ; get TO or SUB
	addq.l	#2,a4			 ; and skip it
re_rest
	bsr.s	re_ntok 		 ; get start of expression
	cmp.b	#$f0,d0 		 ; starts with floating point?
	blo.s	re_ends
	bsr.l	re_update		 ; update address

re_ends
	move.w	#1<<tks.coln+1<<tks.eol,d1 ; search for end of line or statement
	bsr.s	re_ssym

re_nxts
	cmp.b	#tks.coln,d0		 ; another statement?
	beq.s	re_sstate
	addq.l	#6,a4			 ; skip link and line number
	cmp.l	a5,a4			 ; at end?
	blt.s	re_sstate

re_exit_ok
	bsr	sb_chok 		 ; changed
re_rchp
	move.l	d6,a0
	jmp	gu_rchp

re_orng
	moveq	#err.orng,d0
	bra.s	re_rchp

rent_skip
	addq.l	#2,a4
re_ntok
	move.b	(a4),d0 		 ; next token
	cmp.b	#tkb.spce,d0		 ; space?
	beq.s	rent_skip		 ; ... yes, skip it
	rts

re_skpexp
	move.w	#1<<tks.coln+1<<tks.cmma+1<<tks.eol+1,d1 ; end of expr / start of sub

; routine to search for one or more symbols (or any keyword) in the program
; THEN is treated as equivalent to :
;	d0  r	symbol / keyword key (byte)
;	d1 c  p symbol mask (bit zero set for stop on keyword)
;	status 0 if keyword
re_ssym
res_look0
	moveq	#0,d0
res_look
	move.b	(a4)+,d0
	add.b	d0,d0			 ; float?
	bvc.s	res_float		 ; ... yes
	lsr.b	#1,d0
	move.b	res_table(pc,d0.w),d0
	jmp	res_table(pc,d0.w)
res_table
	dc.b	res_2byte-res_table	 $80
	dc.b	res_keyw-res_table	 $81
	dc.b	res_2byte-res_table
	dc.b	res_2byte-res_table
	dc.b	res_symbols-res_table	 $84
	dc.b	res_2byte-res_table	 $85
	dc.b	res_2byte-res_table	 $86
	dc.b	res_2byte-res_table
	dc.b	res_4byte-res_table	 $88
	dc.b	res_2byte-res_table
	dc.b	res_2byte-res_table
	dc.b	res_string-res_table	 $8b
	dc.b	res_string-res_table	 $8c
	dc.b	res_4byte-res_table	 $8d
	dc.b	res_2byte-res_table	 $8e

	dc.b	0

res_2byte
	addq.l	#1,a4
	bra.s	res_look

res_4byte
	addq.l	#3,a4
	bra.s	res_look

res_float
	addq.l	#5,a4
	bra.s	res_look

res_string				 ; round up string
	moveq	#3,d0
	add.w	1(a4),d0
	bset	#0,d0			 ; to one more
	add.l	d0,a4
	bra.s	res_look0

res_keyw
	move.b	(a4)+,d0
	cmp.b	#tkk.then,d0		 ; then?
	btst	#0,d1			 ; stop on keyword
	beq.s	res_look		 ; ... yes
	cmp.b	d0,d0			 ; set Z
	rts

res_then
	beq.s	res_then		 ; ... yes
	moveq	#tks.coln,d0		 ; = :
	bra.s	res_scheck

res_symbols
	move.b	(a4)+,d0

res_scheck
	btst	d0,d1			 ; required symbol?
	beq.s	res_look		 ; ... no
	rts

; routine to update line number (FP) in program
;
;	d0   s
;	d1   s
;	d2   s
;	d3   s
;	d4 c  p low range
;	d5 c  p high range
;	d6 c  p address of table
;	d7 c  p size of table
;	a1 c  p pointer to stack
;	a2   s
;	a4 c  u pointer to line number / updated to end
;
re_update
	move.w	(a4)+,d0
	and.w	#$0fff,d0
	move.l	(a4)+,-(a1)		 ; the floating point number
	move.w	d0,-(a1)
	jsr	qa_nint 		 ; int it
	bne.s	reu_nop2		 ; nop
	move.w	(a1)+,d0		 ; int
	ble.s	reu_nop
	cmp.w	d4,d0			 ; below range?
	blo.s	reu_nop 		 ; yes, do not change
	cmp.w	d5,d0			 ; above range?
	bhi.s	reu_nop 		 ; yes, do not change

	bsr.s	re_look
	move.w	d0,-(a1)
	jsr	qa_float		 ; float it
	move.w	(a1)+,d0
	or.w	#$f000,d0
	move.w	d0,-6(a4)
	move.l	(a1)+,-4(a4)		 ; new value
reu_nop
	rts
reu_nop2
	addq.l	#2,a1
	rts

; routine to look up new line number
;
;	d0 cr	line number to look for / new line
;	d1   s
;	d2   s
;	d3   s
;	d6 c  p address of table
;	d7 c  p size of table
;	a2   s
;
;
re_look
	moveq	#$fffffffc,d3		 ; mask of table addresses
	move.l	d6,a2
	move.l	d7,d2

rel_low
	move.l	d2,d1			 ; save old step size
	lsr.l	#1,d2			 ; new step size
	and.l	d3,d2			 ; in 4 byte units
	beq.s	rel_next		 ; must be next
	cmp.w	(a2,d2.l),d0		 ; found it yet?
	blo.s	rel_low 		 ; ... no, below
	beq.s	rel_this		 ; ... yes

rel_high
	add.l	d2,a2			 ; upper half required
	sub.l	d2,d1			 ; size of upper half
	move.l	d1,d2
	lsr.l	#1,d2			 ; new step size
	and.l	d3,d2			 ; in 4 byte units
	beq.s	rel_next		 ; must be next
	cmp.w	(a2,d2.l),d0		 ; found it yet?
	bhi.s	rel_high		 ; ... no, above
	blo.s	rel_low 		 ; ... no, below

rel_this
	move.w	2(a2,d2.l),d0		 ; line found
rel_rts
	rts

rel_next
	move.w	6(a2,d2.l),d0		 ; next line
	rts





;+++
; DLINE n	(one param, sep null)
; DLINE TO	(one param, sep TO)
; DLINE n TO	(one param, sep TO)
; DLINE  TO m	(two param, sep TO/null)
; DLINE n TO m	(two param, sep TO/null)
; ......... repeated
;
dline
	jsr	sb_ckrets		 ; check procedure stack empty

dline_loop
	moveq	#0,d4
	move.w	#$7fff,d5		 ; default range

	move.l	a5,d0
	sub.l	a3,d0			 ; any params?
	beq.s	dl_rts			 ; ... no

	moveq	#$fffffff0,d1
	and.b	nt_vtype(a6,a3.l),d1	 ; first parameter type
	cmp.b	#nt.comma<<nt.seps,d1	 ; comma?
	beq.s	dl_1par 		 ; ... yes, one parameter

	subq.w	#nt.len,d0		 ; more than one?
	bgt.s	dl_2par 		 ; two params: full range

	tst.b	d1			 ; one param, any separator
	bne.s	dl_TO			 ; ... yes, must be TO
dl_1par
	jsr	ut_gtin1		 ; get an integer
	bne.s	dl_rts
	move.w	(a6,a1.l),d4		 ; from and to the same
	move.w	d4,d5
	bra.s	dl_rng			 ; do range

dl_TO
	cmp.b	#nt.to<<nt.seps,d1	 ; TO?
	bne.s	dl_ipar 		 ; ... no
	move.w	#$0f0f,d0
	and.w	nt_usetp(a6,a3.l),d0	 ; null to?
	beq.s	dl_rng

dl_nTO
	jsr	ut_gtin1
	bne.s	dl_rts
	move.w	(a6,a1.l),d4
	bra.s	dl_rng

dl_ipar
	moveq	#err.ipar,d0
dl_rts
	bra	sb_change

dl_2par
	cmp.b	#nt.to<<nt.seps,d1	 ; two params, separator TO?
	bne.s	dl_ipar 		 ; ... no
	move.b	nt_vtype+nt.len(a6,a3.l),d1 ; next has no sep or comma?
	lsr.b	#nt.seps,d1
	beq.s	dl_2parn
	subq.b	#nt.comma,d1
	bne.s	dl_ipar

dl_2parn
	move.w	#$0f0f,d0
	and.w	nt_usetp(a6,a3.l),d0	 ; null to?
	beq.s	dl_TOx

	jsr	ut_gtin1
	bne.s	dl_rts
	move.w	(a6,a1.l),d4		 ; from

dl_TOx
	addq.l	#nt.len,a3
	move.w	#$0f0f,d0
	and.w	nt_usetp(a6,a3.l),d0	 ; to null?
	beq.s	dl_rng

	jsr	ut_gtin1
	bne.s	dl_rts
	move.w	(a6,a1.l),d5		 ; up to top

dl_rng
	addq.l	#nt.len,a3		 ; parameter taken

	bsr.s	sb_lfist		 ; find starting line
	blt	dline_loop		 ; off end of program

dl_end
	move.l	a4,a0			 ; save start when we look for end
	addq.w	#2,d1			 ; length of prev line including link
	move.w	d1,d0
	move.l	sb_srcep(a6),d7
	bra.s	dl_lend

dl_loop
	add.w	(a6,a0.l),d0		 ; next line length
	add.w	d0,a0			 ; next line
	cmp.l	d7,a0
	bge.s	dl_do			 ; no more
dl_lend
	cmp.w	4(a6,a0.l),d5		 ; past the TO line?
	bge.s	dl_loop 		 ; no

; Here we wish to delete from line (a4), length d1, up to but not including
; line (a0) length d0

dl_do
	add.w	(a6,a0.l),d0		 ; next line length
	sub.w	d1,d0			 ; new length difference
	move.w	d0,(a6,a4.l)
	bra.s	dl_cend

dl_copy
	addq.l	#2,a0
	addq.l	#2,a4
	move.w	(a6,a0.l),(a6,a4.l)	 ; copy
dl_cend
	cmp.l	d7,a0
	blt.s	dl_copy

	move.l	a4,sb_srcep(a6)
	bra	dline_loop

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

	move.l	sb_srceb(a6),d1 	 ; base of program
;*****	      move.w  sb_length(a6),d0	       ; length
	addq.w	#2,d0			 ; + link pointer
	cmp.l	sb_srcep(a6),a4 	 ; at end of program file?
	bge.s	sblf_any		 ; ... yes, can only go back, if at all
	cmp.w	4(a6,a4.l),d4		 ; look at line number
	ble.s	sblf_set		 ; already there

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
	cmp.l	d1,a4			 ; any file at all
	beq.s	sblf_nf 		 ; ... no

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

	end
