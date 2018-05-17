; QXL set display size		      1999  Tony Tebby
; 2006.10.20	1.01	BLAT macro definitions commented out - macro wasn't used (wl)
	section con

	xdef	cn_disp_size

	xref	cn_disp_clear
	xref	gu_achpp
	xref	gu_rchp
	xref	qxl_xmode
	xref	qxl_mess_add

	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_smsq_qxl_keys'
	include 'dev8_smsq_qxl_comm_keys'
	include 'dev8_mac_assert'

*blat macro blv
* xref blatt
* move.b [blv],-(sp)
* jsr	 blatt
* add.w  #2,sp
* endm
*
*blatl macro blv
* xref blattl
* move.l [blv],-(sp)
* jsr	 blattl
* addq.l #4,sp
* endm


;+++
; Set size and colour depth according to D6 and D7
;
;	d6 c  p size requested
;	d7 c  p (word) colour depth requested
;	a3 c  p pointer to console linkage
;
;---
cn_disp_size
cnds.reg reg	d1/d2/d3/d4/d5/d6/d7/a0/a1/a2/a3/a4
	movem.l cnds.reg,-(sp)
; blat #$11
	move.l	qxl_scr_work,a2 	 ; screen work area

cnds_wait
	tst.b	qxl_vhold(a2)		 ; already in progress?
	bne.s	cnds_wait		 ; ... yes

	st	qxl_vhold(a2)		 ; hold update
; blat #$22
	move.l	qxl_message,a1
	move.b	d6,d1
	bgt.s	cnds_ckcmode

	moveq	#0,d6			 ; size -1 is really 0
	bra.s	cnds_ssize		 ; and do not check 0 or -1

cnds_ckcmode
	move.w	d7,d0
	beq.s	cnds_ckcloop		 ; colour depth 0
	subq.w	#1,d0			 ; no colour depth 1 (4 bit)

cnds_ckcloop
	btst	d6,qxl_ms_pcset+qxm_dvmode(a1,d0.w) ; this mode exists?
	bne.s	cnds_ssize
cnds_reduce
	subq.b	#1,d6
	move.b	d6,d1
	bgt.s	cnds_ckcloop

cnds_ssize
	move.b	d1,qxl_vsize(a2)	 ; new size
	move.b	d7,qxl_vcolr(a2)	 ; and colour depth
; blat #$80
; blat d1
; blat #$81
; blat d7
cnds_getp
	ext.w	d1
	muls	#10,d1
; blat d6
	movem.w qxl_qsizes(pc,d1.w),d0/d1/d2/d3/d4
	bra.s	cnds_adjs

		; vga offset, vga line length, qxl line length, screen size
	dc.w	47*80+8,80,128,512,256
qxl_qsizes
	dc.w	0,80,160,640,480
	dc.w	0,100,200,800,600
	dc.w	0,128,256,1024,768

cnds_adjs
	tst.b	d7
	beq.s	cnds_sets
	lsl.w	d7,d2			 ; adjust line length for resolution
	move.w	d2,d1			 ; vga length is the same
cnds_sets
	move.w	d0,qxl_vga0(a2)
	move.w	d1,qxl_vgal(a2)
	move.w	d2,qxl_scrl(a2)
	move.w	d4,qxl_scry(a2)
	clr.w	qxl_scrp(a2)		 ; start scanning at top
	move.b	#qxl.vchek,qxl_vchek(a2) ; standard screen checking
	tst.b	d7
	beq.s	cnds_setp		 ; QL mode display
	move.b	#1,qxl_vchek(a2)	 ; high colour

cnds_setp
	move.w	d2,d0
	mulu	d4,d0			 ; size of screen
	move.l	d0,pt_scrsz(a3)
	move.w	d2,pt_scinc(a3)
	move.w	d3,pt_xscrs(a3)
	move.w	d4,pt_yscrs(a3)

	move.l	d0,d1			 ; screen size
	move.l	d0,d5			 ; copy size
	move.l	qxl_scrb(a2),a0 	 ; old base
	moveq	#0,d4			 ; mark not cleared

	cmp.l	#$8000,d0		 ; is QL screen required ???
	bgt.s	cnds_chkscr		 ; ... no
	moveq	#0,d1			 ; ... no screen
cnds_chkscr
	add.l	d1,d5			 ; total size
	move.l	a0,d0
	beq.s	cnds_alloc		 ; ... no old screen
	cmp.l	a6,a0			 ; was it QL screen
	bgt.s	cnds_chkalloc		 ; ... no
	move.l	qxl_cpyb(a2),a0 	 ; ... this is base of allocation
cnds_chkalloc
	cmp.l	qxl_scra(a2),d5 	 ; allocation large enough?
	ble.s	cnds_setscr		 ; ... yes
; blatl d0

	cmp.l	sys_ramt(a6),a0 	 ; old allocation at or above ramtop?
	bge.s	cnds_alloc		 ; ... yes, cannot return

	jsr	gu_rchp 		 ; return old screen
	clr.l	qxl_scra(a2)
	clr.l	qxl_scrb(a2)

cnds_alloc
	move.l	d5,d0
	jsr	gu_achpp		 ; allocate the new memory
	bne	cnds_reduce
	st	d4			 ; screen is clean
	move.l	d5,qxl_scra(a2) 	 ; new allocation

; blatl  -$10(a0)
; move.l a0,a4
; add.l  d1,a4
; add.l  d1,a4
; blatl  (a4)
; blat #$44

cnds_setl
; not.l  (a0)
; not.l  $04(a0)
; not.l  $08(a0)
; not.l  $0c(a0)
; not.b  $20(a0)
; not.b  $24(a0)
; not.b  $28(a0)
cnds_setscr
	move.l	a0,qxl_scrb(a2) 	 ; new screen base
	move.l	a0,pt_scren(a3)
	add.l	d1,a0
	move.l	a0,qxl_cpyb(a2) 	 ; new copy area
	tst.l	d1			 ; QL screen?
	bne.s	cnds_clear		 ; ... no
	moveq	#2,d0
	swap	d0
	move.l	d0,qxl_scrb(a2) 	 ; set QL screen
	move.l	d0,pt_scren(a3)

cnds_clear
	tst.b	d4			 ; dirty screen?
	bne.s	cnds_setpc		 ; ... no
	jsr	cn_disp_clear

cnds_setpc
; blat #$88
; all set up - notify the QXL

	move.l	qxl_scr_work,a2 	 ; screen work area
	move.l	qxl_message,a3		 ; message area

	lea	qxl_ms_vmode+qxl_ms_len(a3),a1 ; mode message length
	move.l	#4<<16+qxm.vmode<<8,(a1)       ; ... message length and key

	move.b	d7,d0			 ; internal colour
	beq.s	cnds_sclr		 ; QL mode
	subq.b	#1,d0			 ; VGA mode is one different
cnds_sclr
	move.b	d0,qxm_vclr-qxl_ms_len(a1) ; set colour
	move.b	d6,qxm_vres-qxl_ms_len(a1) ; size
; blat #$01
; blatl 1(a1)

	jsr	qxl_mess_add		 ; add message to queue
; blat #$11
cnds_waits
	tst.b	qxl_vhold(a2)		 ; change mode in progress?
	bne.s	cnds_waits		 ; ... yes

	clr.w	qxl_vconv(a2)		 ; assume no conversion
	tst.b	d7			 ; ql mode?
	bne.s	cnds_ok 		 ; no

	jsr	qxl_xmode		 ; set QL mode display

cnds_ok
	moveq	#0,d0
cnds_exit
	movem.l (sp)+,cnds.reg
	rts

cnds_bang
	bra.s	cnds_exit

	end
