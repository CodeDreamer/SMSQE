; General sprite cache	   V2.14			 1999	Tony Tebby
;							  2002	Marcel Kilgus
;
; 2001-06-29  2.01  Fixed severe bug in cache setup (MK)
; 2002-11-12  2.10  Added 24bit sprite routines (MK)
; 2002-12-15  2.11  Increase the support of sprite mode (JG)
; 2003-01-12  2.12  Added support for bigger sprites (MK)
;		    Added mode 33 support for QPC/QXL (MK)
; 2003-02-17  2.13  Moved sprite list scan to pt_fsprd (MK)
; 2003-02-21  2.14  Added RLE compression and Alpha channel support (MK)
;		    Moved generic code to ptr_spcch_asm (MK)

	section driver

	xdef	pt_cchset
	xdef	pt_cchloc

	xref.s	pt.spmax
	xref.s	pt.blmax
	xref.s	pt.sppref
	xref.s	pt.spxsw

	xref	pt_fsprd
	xref	pt_derle
	xref	ptc_convert
	xref	ptc_pattern
	xref	ptc_mask
	xref	pt_sppref     ; sprite preference table
	xref	pt_spnative   ; and native mode

	xref	sp_zero

	xref	gu_achpp
	xref	gu_rchp

	include 'dev8_keys_con'
	include 'dev8_keys_qdos_pt'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sysspr'

;+++
; Sprite cache setup
;
;	a3 c  p pointer to pointer linkage
;	a5 c  p pointer to sprite cache
;
;---
pt_cchset
	movem.l a1/a2,-(sp)

; the cache table has 16 byte entries
;
ptc_defn    equ $00	; long	pointer to definition
ptc_native  equ $04	; long	pointer to native version
ptc_tcache  equ $08	; long	temporary cache pointer (for big sprites)
ptc_vcch    equ $0c	; byte	cache control version number
ptc_mcch    equ $0e	; word	cache control original mode ident
ptc.length  equ $10

	lea	(a5),a1
	lea	4*ptc.length(a5),a2

	clr.l	(a1)+
	move.l	a2,(a1)+
	clr.l	(a1)+
	lea	ptc.length-ptc_vcch(a1),a1
	lea	pt.spmax(a2),a2
	clr.l	(a1)+
	move.l	a2,(a1)+
	clr.l	(a1)+
	lea	ptc.length-ptc_vcch(a1),a1
	lea	pt.spmax(a2),a2
	clr.l	(a1)+
	move.l	a2,(a1)+
	clr.l	(a1)+
	lea	ptc.length-ptc_vcch(a1),a1
	lea	pt.blmax(a2),a2
	clr.l	(a1)+
	move.l	a2,(a1)+
	clr.l	(a1)+
	movem.l (sp)+,a1/a2
	moveq	#0,d0
	rts

;+++
; Sprite cache locate sprite
;
;	d0 c  s -ve pointer,
;		0 ordinary sprite  (or pattern with mask!!!)
;		1 pattern
;		2 blob
;	d2    r pointer to the correct form sprite
;	a1 c  r pointer to sprite type 0 / pointer to native mode sprite
;	a3 c  p pointer to pointer linkage
;
;---
pt_cchloc
ptcc.reg reg	d1-d7/a0/a2-a6
stk_d2	equ	4
	movem.l ptcc.reg,-(sp)
	move.b	d0,d7			 ; mask of pattern / blob to convert
	bne.s	ptc_cchent		 ; ok, look for cache entry
	moveq	#3,d7			 ; 0 is actually both
ptc_cchent
	addq.w	#1,d0
	lsl.w	#4,d0			 ; 4 long word entries
	move.l	pt_spcch(a3),a5 	 ; sprite cache table
	add.w	d0,a5

	move.l	ptc_tcache(a5),d0	 ; remove temp cache if any
	beq.s	ptc_fspr
	move.l	d0,a0
	movem.l a1/a3,-(sp)
	moveq	#sms.rchp,d0
	trap	#1
	movem.l (sp)+,a1/a3
	clr.l	ptc_tcache(a5)		 ; no temp cache anymore
ptc_fspr
	jsr	pt_fsprd		 ; find fitting sprite definition

	move.l	a1,stk_d2(sp)		 ; return this
	sub.w	#pt.sppref-1,d3 	 ; adjust for table (native = 0)
	bne.s	ptc_checkcache
	move.b	pto_ctrl(a1),d0 	 ; sprite control
	andi.b	#pto.cmp,d0		 ; can sprite really be used directly?
	beq	ptc_retnative		 ; yes

ptc_checkcache
	move.b	pto_ctrl(a1),d0 	 ; sprite control
	andi.b	#pto.cver,d0		 ; only get sprite cache version
	cmp.b	#pto.fupd,d0		 ; force conversion?
	beq.s	ptc_index		 ; ... yes

	cmp.l	ptc_defn(a5),a1 	 ; object is cached?
	bne.s	ptc_index		 ; ... no, go ahead and convert

	cmp.b	ptc_vcch(a5),d0 	 ; ... is version the same?
	bne.s	ptc_index		 ; ... no
	cmp.w	ptc_mcch(a5),d5 	 ; ... is also converted mode the same ?
	beq	ptc_retcch		 ; ... yes

ptc_index
	move.l	a1,ptc_defn(a5) 	 ; save address
	move.b	d0,ptc_vcch(a5) 	 ; set version
	move.w	d5,ptc_mcch(a5) 	 ; set original mode
	add.w	d3,d3			 ; index convert code

	move.l	ptc_native(a5),a0	 ; cache entry address

	lea	pto.hdrl(a0),a4 	 ; start of data
	move.l	(a1)+,d0
	move.w	pt_spnative,(a0)+
	move.w	d0,(a0)+
	moveq	#0,d1
	moveq	#0,d2
	move.w	(a1)+,d1		 ; x size
	move.w	(a1)+,d2		 ; y size
	move.w	d1,(a0)+
	move.w	d2,(a0)+
	move.w	d1,d0
	or.w	d2,d0
	lsr.w	#6,d0			 ; big sprite?
	beq.s	ptc_setorg
	move.l	d1,d0
	addq.w	#1,d0
	bclr	#0,d0
	mulu	d2,d0
	cmp.l	#pt.spspx*pt.spspy,d0	 ; giant sprite?
	ble.s	ptc_setorg

	clr.l	ptc_defn(a5)		 ; ... yes, do not cache
	move.w	#pt.spxsw,d4
	lsl.l	d4,d0			 ; and try to allocate mem on the fly
	cmp.b	#3,d7			 ; if sprite we need twice as much mem
	bne.s	ptc_notempspr
	add.l	d0,d0
ptc_notempspr
	move.l	a0,a4
	jsr	gu_achpp
	exg	a4,a0			 ; a4 is now new start of data
	beq.s	ptc_tempcache

	lea	sp_zero(pc),a1		 ; couldn't allocate mem, do not draw
	bra	ptc_fspr

ptc_tempcache
	move.l	a4,ptc_tcache(a5)	 ; save temporary cache address

ptc_setorg
	move.l	(a1)+,(a0)+		 ; origin
	move.w	d3,-(sp)
	bsr.s	ptc_setpattern
	move.w	(sp)+,d3
	bsr.s	ptc_setmask
	clr.l	(a0)+			 ; ... no next

ptc_retcch
	move.l	ptc_native(a5),a1	 ; native mode object is here

ptc_retnative
	movem.l (sp)+,ptcc.reg
	moveq	#0,d0
	rts

ptc_nofill
	addq.l	#4,a1			 ; skip in source
ptc_nodata
	clr.l	(a0)+			 ; no data
	rts

; Get a valid mask. Also handle alpha channel correctly
ptc_setmask
	asr.b	#1,d7			 ; fill this bit?
	bcc.s	ptc_nofill

	lea	ptc_mask(pc),a6
	move.w	(a6,d3.w),d4		 ; routine to use
	move.l	a1,a2
	move.l	(a1)+,d0		 ; original data offset
	beq.s	ptc_nodata
	add.l	d0,a2			 ; make absolute

	move.b	pto_ctrl-pto_nobj(a1),d0
	andi.b	#pto.mcmp,d0		 ; d0 > 0 = mask compressed

	btst	#pto..alph,pto_ctrl-pto_nobj(a1) ; is it an alpha channel?
	beq.s	pst_noalpha

	tst.b	d0			 ; data compressed?
	beq.s	pst_directa		 ; no, use it directly

	move.l	a4,a6
	jsr	pt_derle		 ; decompress into cache
	move.l	a4,a2
pst_directa
	sub.l	a0,a2			 ; make relative
	move.l	a2,(a0)+		 ; set pointer to data
	rts

; This is like ptc_setmask, we just don't need to check for an alpha channel.
ptc_setpattern
	asr.b	#1,d7			 ; fill this bit?
	bcc.s	ptc_nofill

	lea	ptc_pattern(pc),a6
	move.w	(a6,d3.w),d4		 ; routine to use
	move.l	a1,a2
	move.l	(a1)+,d0		 ; original data offset
	beq.s	ptc_nodata
	add.l	d0,a2			 ; make absolute

	move.b	pto_ctrl-pto_mask(a1),d0
	andi.b	#pto.pcmp,d0		 ; d0 > 0 = pattern compressed

pst_noalpha
	tst.b	d0			 ; data compressed?
	beq.s	pst_norle		 ; ...no

	tst.w	d3			 ; native mode sprite?
	beq.s	pst_native		 ; yes, decompress into cache
	suba.l	a6,a6			 ; allocate buffer for conversion
	jsr	pt_derle		 ; decompress RLE
	move.l	a6,a2			 ; new buffer as source for conversion
	bsr.s	psc_convert
	move.l	a0,-(sp)
	move.l	a6,a0
	jsr	gu_rchp 		 ; release RLE buffer
	move.l	(sp)+,a0
	rts

pst_native
	move.l	a4,a6
	jsr	pt_derle		 ; decompress into cache
	move.l	a4,a2
	add.l	d0,a4			 ; update cache pointer
pst_direct
	sub.l	a0,a2			 ; make relative
	move.l	a2,(a0)+		 ; set pointer to data
	rts

pst_norle
	tst.w	d3			 ; native mode sprite?
	beq.s	pst_direct		 ; yes, use source directly

psc_convert
	move.l	a4,d0			 ; address of data
	sub.l	a0,d0
	move.l	d0,(a0)+		 ; offset to data
	jmp	ptc_convert(pc,d4.w)

	end
