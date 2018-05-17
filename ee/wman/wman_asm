* Link in the Window Manager Routines  V1.02   1986  Tony Tebby  QJUMP
*						2002  Marcel Kilgus
*
* 2002-11-13  1.01  Intialises system palette (MK)
* 2003-01-25  1.02  Moved QDOS specific code here (MK)
* 2003-05-27  1.03  Now uses new WMAN data handling (MK)
*
* THIS FILE IS NOT USED IN SMSQ/E, it is for stand-alone WMAN only!
* smsq_smsq_wman_asm is for SMSQ/E. I adapted it nontheless. Untested! (MK)

	section base
*
	xref	wm_entry
	xref	wm_initdata
	xref	wm_initp
;	 xref	 met_nptr
;	 xref	 met_pver

	xref.l	wm.pver

*
	include dev8_keys_qdos_sms
	include dev8_keys_qdos_io
	include dev8_keys_con
	include dev8_keys_sys
*
	bra.l	start
	section language
	section wman
start
	sub.l	a0,a0
	moveq	#iop.pick,d0		pick command window
	moveq	#0,d1
	moveq	#0,d2
	moveq	#-1,d3
	trap	#3

	moveq	#iop.pinf,d0		info
	trap	#3
	tst.l	d0
	bne.s	wm_nptr
	lea	met_pver,a1		error message
	cmp.l	#wm.pver,d1		up to date?
	blt.s	wm_perr 		... no

	bsr	wm_initdata		initialise working data
	bne.s	wm_rts
	bsr	wm_initp		initialise basic procedures

	moveq	#sms.info,d0
	trap	#1		  ; find system info

	move.l	sys_clnk(a0),a0 	 ; console linkage
	pea	wm_entry		 ; window manager entry vector
	move.l	(sp)+,pt_wman(a0)	 ; in pointer location
	moveq	#0,d0
	rts

;	 pea	 wm_entry(pc)		 find our entry vector
;	 move.l  sp,a1
;	 suba.l  a0,a0
;	 moveq	 #iop.slnk,d0		 set linkage
;	 moveq	 #pt_wman,d1		 the window manager vector
;	 moveq	 #4,d2			 4 bytes
;	 trap	 #3
;	 addq.l  #4,sp			 clean up stack
;
;	 tst.l	 d0
;	 beq.s	 wm_rts
wm_nptr
	lea	met_nptr,a1
wm_perr
	move.l	a1,d0
	bset	#31,d0
wm_rts
	rts

met_nptr
	dc.w	met_nptr2-*-2
	dc.b	'Pointer Interface is absent!'
met_nptr2
met_pver
	dc.w	met_pver2-*-2
	dc.b	'Pointer Interface too old!'
met_pver2
	dc.w	0
	end
