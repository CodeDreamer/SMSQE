; Utility open a file using name, default and extension  1994 Tony Tebby
;
; 2018-01-10  1.01  Removed ".sav" extension for QDOS to save some bytes (MK)
; 2019-02-10  1.02  Fixed crash introduced in 1.01 (MK)
; 2020-08-24  1.03  Don't add default directories on names with drives (MK)

	section utils

	xdef	ut_opdefx
	xdef	ut_opdefxj

	xdef	uxt_bopt	; optional basic extensions
	xdef	uxt.bas

	xref	ut_fdef 	; fin default
	xref	ut_msexq	; message, file already exists, query
	xref	ut_fhead	; read file header

	include 'dev8_keys_sbasic'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_hdr'
	include 'dev8_keys_iod'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_sbsext_ut_opdefx_keys'
	include 'dev8_mac_assert'

buff_off equ	$40

uod_getext
	move.w	(a3),d0
	btst	#0,d0			 ; odd length?
	beq.s	uod_fext		 ; ... no
	move.l	-3(a3,d0.w),d1
	lsl.l	#8,d1
	move.b	1(a3,d0.w),d1		 ... all of extension
	bra.s	uod_lcx
uod_fext
	move.l	-2(a3,d0.w),d1
uod_lcx
	or.l	#$00202020,d1
	rol.l	#8,d1
	cmp.b	#'.',d1 		 ; is it a .extension?
	bne.s	uod_rrx
	move.b	#'_',d1 		 ; ... yes, make an _
uod_rrx
	ror.l	#8,d1
	rts

;+++
; add extension: if no name or name is too long does nothing!
;
;	d0   s
;	a1 c s	pointer to extension
;	a3 c  p pointer to name
;	a4   s
;
;	status returned 0 if name is too long
;---
uod_addext
	move.w	(a3),d0 		 ; name length
	beq.s	utx_rts0		 ; ... none
	cmp.w	#$30,d0 		 ; too long to fit?
	bhi.s	utx_rts0
	lea	2(a3,d0.w),a4		 ; old end of name
	addq.w	#4,(a3) 		 ; new length

	move.b	(a1)+,(a4)+
	move.b	(a1)+,(a4)+
	move.b	(a1)+,(a4)+
	move.b	(a1)+,(a4)+
utx_rts
	rts
utx_rts0
	moveq	#0,d0
	rts

uxt_bopt dc.w	0
	 dc.w	uxt.bas-*
	 dc.w	uxt..bas-*
ut_null  dc.w	0

uxt.bas  dc.l	 '_bas'
uxt..bas dc.l	 '.bas'

;+++
; Open file with name, defaults and extensions
;
;	d1  r	-1 (owner job ID)
;	d3 c  p open key
;	d7 c  p retry keys	bit 0	set for prompt if already exists
;				bit 1	set for ignore file types <> 0
;				bit 2	set for retry data default
;				bit 3	set for retry program default
;				bit 4	set for retry destination default
;				bit 7	set for extension list
;	a0  r	channel ID
;	a1 cr	pointer to name / returned with defaults / extensions required
;	a2 c  p pointer to extension list	if d7 bit 7 = 0 no extension
;						if (a2) = 0 optional list
;						if (a2) <> 0 extension required
;
;	status return and d0 standard
;---
ut_opdefx
	moveq	#-1,d1
;+++
; Open file (for job) with name, defaults and extensions
;
;	d1 c  p owner job ID
;	d2 c  p retry keys	bit 0	set for prompt if already exists
;				bit 1	set for ignore file types <> 0
;				bit 2	set for retry data default
;				bit 3	set for retry program default
;				bit 4	set for retry destination default
;				bit 7	set for extension list
;	d3 c  p open key
;	a0  r	channel ID
;	a1 cr	pointer to name / returned with defaults / extensions required
;	a2 c  p pointer to extension list	if d7 bit 7 = 0 no extension
;						if (a2) = 0 optional list
;						if (a2) <> 0 extension required
;
;	status return and d0 standard
;---
ut_opdefxj
uod.reg reg	d1/d2/d3/d7/a1/a2/a3/a4
stk_jid equ	$00
stk_key equ	$08
stk_name equ	$10
stk_ext equ	$14
	movem.l uod.reg,-(sp)
	move.l	d2,d7			 ; save retry keys

	move.l	sb_buffb(a6),a3 	 ; stick the name into bf
	lea	buff_off(a6,a3.l),a3	 ; leave room for the count
	move.l	a1,d0			 ; ... is there a pointer to a name?
	beq.l	uod_opdef		 ; ... no, just open with default
	move.w	(a6,a1.l),d0		 ; is there a name at all?
	beq.l	uod_opdef		 ; ... no, just open with default
	blt.l	uod_inam

; Does filename start with a known device name? Don't add default dir then!
; Note: we assume all directory devices have 3 character names
	subq.w	#5,d0			 ; must have at least 5 chars for check
	bcs.s	uod_start
	cmp.b	#'_',6(a6,a1.l) 	 ; underscore at right place?
	bne.s	uod_start		 ; no, we can skip test
	move.b	5(a6,a1.l),d0		 ; drive number must be between 1 and 8
	sub.b	#'1',d0
	bcs.s	uod_start
	subq.b	#8,d0
	bcc.s	uod_start

	moveq	#sms.info,d0
	trap	#1
	lea	sys_fsdl(a0),a0 	 ; list with directory device drivers
	move.l	#$dfdfdf00,d1
	and.l	2(a6,a1.l),d1		 ; First 3 chars of filename
uod_fsloop
	move.l	(a0),d0 		 ; next driver
	beq.s	uod_start		 ; no more
	move.l	d0,a0
	move.l	#$dfdfdf00,d0
	and.l	iod_dnus-iod_iolk+2(a0),d0
	cmp.l	d0,d1
	bne.s	uod_fsloop		 ; no match, try next

	andi.b	#$ff-uod.datd-uod.prgd-uod.dstd,d7 ; match, don't try def dirs!
uod_start
	lea	ut_null,a4		 ; no default first time round
uod_tryd
	move.w	(a4)+,d2		 ; get character count of default
	move.l	a3,a0
	move.w	d2,(a0)+		 ; keep length
	bra.s	uod_dle
uod_dlp
	move.b	(a4)+,(a0)+		 ; copy default a byte at a time
uod_dle
	dbra	d2,uod_dlp

	move.l	stk_name(sp),d0
	beq.s	uod_xt
	lea	(a6,d0.l),a1
	move.w	(a1)+,d2		 ; get character count of name
	add.w	d2,(a3) 		 ; and total
	cmp.w	#44,(a3)		 ; is total>max file name?
	bhi.s	uod_opdef		 ; ... yes, try other default
	bra.s	uod_nle
uod_nlp
	move.b	(a1)+,(a0)+		 ; copy one byte at a time
uod_nle
	dbra	d2,uod_nlp

uod_xt
	lea	ut_null,a2
	assert	uod..extn,7
	tst.b	d7			 ; extensions?
	bpl.s	uod_opnx		 ; ... no
	move.l	stk_ext(sp),a2		 ; extension list
	move.w	(a2)+,d0		 ; compulsory?
	bne.s	uod_cpxt		 ; ... yes

uod_opnx
	bsr.l	uod_open		 ; try open
	bge.s	uod_exit		 ; ok or give up

	tst.w	(a2)			 ; any extensions?
	beq.s	uod_opdef		 ; ... no, try another default

	move.l	a2,a4
	bsr	uod_getext		 ; get the extension, to see if we add
uod_chkx
	move.w	(a4)+,d0
	beq.s	uod_allx		 ; none matches, try all extensions
	cmp.l	-2(a4,d0.w),d1		 ; this extension matches?
	bne.s	uod_chkx		 ; ... no, try another
	bra.s	uod_opdef		 ; ... yes, do not add extensions

uod_allx
	move.w	(a2)+,d0		 ; next extension
	beq.s	uod_opdef		 ; ... no more
	lea	-2(a2,d0.w),a1
	bsr	uod_addext		 ; add it
	beq.s	uod_opdef		 ; too long
	bsr.s	uod_open
	bge.s	uod_exit		 ; open or give up
	subq.w	#4,(a3) 		 ; remove extension
	bra.s	uod_allx		 ; and try again

uod_cpxt
	lea	-2(a2,d0.w),a1		 ; extension
	bsr	uod_getext		 ; get the extension
	cmp.l	(a1),d1 		 ; already there?
	beq.s	uod_ocpxt		 ; ... yes
	bsr	uod_addext		 ; ... no, add extension
	beq.s	uod_opdef		 ; ... ... cannot do
uod_ocpxt
	bsr.s	uod_open		 ; try opening it
	bge.s	uod_exit		 ; ok or give up

uod_opdef
	moveq	#sys_datd-sys.defo,d2
	bclr	#uod..datd,d7		 ; data default?
	bne.s	uod_sdef		 ; ... yes
	moveq	#sys_prgd-sys.defo,d2
	bclr	#uod..prgd,d7		 ; program default?
	bne.s	uod_sdef		 ; ... yes
	moveq	#sys_dstd-sys.defo,d2
	bclr	#uod..dstd,d7		 ; destination default?
	beq.s	uod_fdnf		 ; ... no, give up
uod_sdef
	bsr.l	ut_fdef 		 ; find default
	move.l	a4,d0			 ; any?
	beq.s	uod_opdef		 ; ... no, try another
	tst.w	(a4)			 ; any?
	beq.s	uod_opdef		 ; ... no, try another
	bra	uod_tryd		 ; ... yes, try this default

uod_exit
	sub.l	a6,a3
	move.l	a3,stk_name(sp) 	 ; set name returned
	tst.l	d0
	movem.l (sp)+,uod.reg
	rts

uod_inam
	moveq	#err.inam,d0
	bra.s	uod_exit

uod_fdnf
	moveq	#err.fdnf,d0
	bra.s	uod_exit

uod_open
	moveq	#ioa.delf,d0		 ; assume delete
	move.b	stk_key+3+4(sp),d3	 ; key
	bmi.s	uod_trap2		 ; ... it is
uod_opk
	moveq	#ioa.open,d0		 ; open
uod_trap2
	move.l	a3,a0			 ; file name
	move.l	stk_jid+4(sp),d1	 ; owner job ID
	trap	#do.ioa
	tst.l	d0
	beq.s	uod_chk 		 ; OK

	moveq	#err.fex,d3		 ; file already exists?
	cmp.l	d0,d3
	bne.s	uod_cknf		 ; ... no, check for not found (or inam)
	btst	#uod..prmt,d7		 ; prompt for overwrite?
	beq.s	uod_fex 		 ; ... no

	move.l	a3,a1
	sub.l	a6,a1
	bsr.l	ut_msexq		 ; write message and query
	bne.s	uod_fex
	moveq	#ioa.kovr,d3		 ; overwrite
	bra.s	uod_opk

uod_chk
	btst	#uod..typ0,d7		 ; type zero files only?
	beq.s	uod_rts 		 ; ... no, OK

	jsr	ut_fhead		 ; read file header
	bne.s	uod_ok			 ; OK!!
	tst.b	hdr_type(a6,a1.l)	 ; file type?
	beq.s	uod_rts 		 ; 0

	moveq	#ioa.clos,d0		 ; forget this one
	trap	#do.ioa
	moveq	#err.fdnf,d0		 ; treat as not found

uod_rtd0
	tst.l	d0			 ; set status
uod_rts
	rts

uod_cknf
	moveq	#err.fdnf,d3		 ; if not found, continue
	cmp.l	d3,d0
	beq.s	uod_rtd0
	moveq	#err.inam,d3		 ; if invalid name, continue
	cmp.l	d3,d0
	beq.s	uod_rtd0
	bra.s	uod_give_up

uod_ok
	moveq	#0,d0
	rts

uod_fex
	moveq	#err.fex,d0		 ; file exists
uod_give_up
	moveq	#1,d1			 ; ... give up
	rts

	end
