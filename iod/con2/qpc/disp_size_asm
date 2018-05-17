; QPC set display size		      1999  Tony Tebby
;				      2000  Marcel Kilgus
	section con

	xdef	cn_disp_size

	xref	cn_disp_clear
	xref	gu_achpp
	xref	gu_rchp
	xref	qpc_xmode

	include 'dev8_keys_con'
	include 'dev8_keys_sys'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_mac_assert'

;+++
; Set size and colour depth according to D6 and D7
;
;	d6 c  p x/y resolution requested
;	d7 c  p (word) colour depth requested
;	a3 c  p pointer to console linkage
;
;---
cn_disp_size
cnds.reg reg	d1/d2/d3/d4/d5/d6/d7/a0/a1/a2/a3/a4
	movem.l cnds.reg,-(sp)
	move.l	#qpc_scr_work,a2	 ; screen work area
	bra.s	cnds_getmode

cnds_reduce
	lea	mode_tab(pc),a0
cnds_reduce_loop
	cmp.l	#$02000100,d6		 ; 512x256 mode?
	ble.s	cnds_reduce_color

	cmp.l	(a0),d6
	bge.s	cnds_newmode
	add.w	#4,a0
	bra.s	cnds_reduce_loop

cnds_reduce_color
	move.l	#$02000100,d6
	moveq	#0,d7			 ; last chance, use 4-colour mode
	bra.s	cnds_getmode

cnds_newmode
	move.l	(a0),d6 		 ; try smaller mode (last had err_imem)
cnds_getmode
	lea	color_tab(pc),a0
	move.w	d7,-(a7)
	move.b	(a0,d7.w),d7		 ; convert to bpp
	dc.w	qpc.cmode+6		 ; get best display mode
	move.w	d7,d0			 ; real bpp
	move.w	(a7)+,d7		 ; Restore SMSQ colour value

;	 move.b  d1,qpc_vsize(a2)	  ; new size
;	 move.b  d7,qpc_vcolr(a2)	  ; and colour depth

	move.l	d6,d3			 ; x/y resolution
	swap	d3			 ; x resolution
	move.w	d6,d4			 ; y resolution

	move.w	d3,d2			 ; x
	mulu	d0,d2			 ; bit per line
	lsr.l	#3,d2			 ; /8 -> byte per line
	move.w	d2,qpc_scrl(a2)
	move.w	d4,qpc_scry(a2)

	move.w	d2,d0
	mulu	d4,d0			 ; size of screen
	move.l	d0,pt_scrsz(a3)
	move.w	d2,pt_scinc(a3)
	move.w	d3,pt_xscrs(a3)
	move.w	d4,pt_yscrs(a3)

	move.l	d0,d5			 ; screen size
	move.l	qpc_scrb(a2),a0 	 ; old base
	moveq	#0,d4			 ; mark not cleared

	cmp.l	#$8000,d0		 ; is QL screen required ???
	bgt.s	cnds_chkscr		 ; ... no
	moveq	#0,d5			 ; ... no screen
cnds_chkscr
	move.l	a0,d0
	beq.s	cnds_alloc		 ; ... no old screen
	cmp.l	a6,a0			 ; was it QL screen
	ble.s	cnds_alloc		 ; ... yes

	cmp.l	qpc_scra(a2),d5 	 ; allocation large enough?
	ble.s	cnds_setscr		 ; ... yes

	cmp.l	sys_ramt(a6),a0 	 ; old allocation at or above ramtop?
	bge.s	cnds_alloc		 ; ... yes, cannot return

	jsr	gu_rchp 		 ; return old screen
	clr.l	qpc_scra(a2)
	clr.l	qpc_scrb(a2)

cnds_alloc
	move.l	d5,d0
	jsr	gu_achpp		 ; allocate the new memory
	bne	cnds_reduce
	st	d4			 ; screen is clean
	move.l	d5,qpc_scra(a2) 	 ; new allocation

cnds_setscr
	move.l	a0,qpc_scrb(a2) 	 ; new screen base
	move.l	a0,pt_scren(a3)
	cmp.l	#$02000100,d6		 ; 512x256?
	bne.s	cnds_clear		 ; ... no
	tst.b	d7			 ; QL mode?
	bne.s	cnds_clear		 ; ... no
	moveq	#2,d0
	swap	d0
	move.l	d0,qpc_scrb(a2) 	 ; set QL screen
	move.l	d0,pt_scren(a3)

cnds_clear
	tst.b	d4			 ; dirty screen?
	bne.s	cnds_setpc		 ; ... no
	jsr	cn_disp_clear

cnds_setpc
; all set up - notify QPC
	move.l	#qpc_scr_work,a2	 ; screen work area

	move.l	qpc_scrb(a2),a0
	dc.w	qpc.sbase		 ; set screen base
	dc.w	qpc.smode		 ; finally set last mode we've found

	tst.b	d7			 ; ql mode?
	bne.s	cnds_ok 		 ; no

	jsr	qpc_xmode		 ; set QL mode display

cnds_ok
	moveq	#0,d0
cnds_exit
	movem.l (sp)+,cnds.reg
	rts

color_tab
	dc.b	2,4,8,16,24		 ; bpp

mode_tab
	dc.w	800,600,640,480,640,350,512,256 ; some common modes

	end
