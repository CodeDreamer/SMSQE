; Find name in directory   V2.03    1987  Tony Tebby  QJUMP

	section iou

	xdef	iou_fnam

	xref	iou_fmlw
	xref	iou_cknm
	xref	iou_ckdn

	include 'dev8_keys_iod'
	include 'dev8_keys_err'
	include 'dev8_keys_chn'
	include 'dev8_keys_hdr'
	include 'dev8_keys_qdos_ioa'
;+++
; Find name in directory.
;
;	d1   s
;	d2   s
;	d3 cr	zero or directory pointer for sub-directory
;	d4 cr	zero or directory ID for sub-directory (msb set if genuine dir)
;	d5  r	directory pointer for name or empty entry
;	d6 cr	drive / sub-directory file ID
;	d7   s	byte count
;	a0 c  p base of channel block  
;	a1   s	pointer to name
;	a2   s	buffer pointer
;	a3 c  p linkage block pointer
;	a4 c  p physical definition pointer
;
;	status return standard
;---
iou_fnam
reglist  reg	 d3/d4/d5/a0/a4
regchan  reg	 a0/a4
stk_dpt  equ	 $00
stk_did  equ	 $04
stk_sdpt equ	 $08
stk_chan equ	 $0c
stk_pdef equ	 $10

	moveq	#0,d5			 ; preset zero pointer (will become neg)
	movem.l reglist,-(sp)		 ; save regs
	move.l	iod_hdrl(a4),d5 	 ; start from here!
	move.l	iod_rdfs(a4),chn_csb(a0) ; set first guess at slave block  
	jsr	iod_lcbf(a3)		 ; locate first one
	move.l	chn_csb(a0),iod_rdfs(a4) ; and keep it
ifn_floop
	cmp.l	chn_feof(a0),d5 	 ; at end of file?
	bhs.l	ifn_fdnf		 ; ... yes, not found

	lea	chn_opwk(a0),a1 	 ; spare space for directory entry
	moveq	#hdr.len,d7		 ; length to read
	jsr	iou_fmlw		 ; read directory entry
	bne.l	ifn_exit		 ; ... not there
	tst.w	hdr_name+chn_opwk(a0)	 ; vacant?
	beq.l	ifn_empty		 ; ... yes
	lea	hdr_name+chn_opwk(a0),a4 ; point to name
	move.b	hdr_type+chn_opwk(a0),d0 ; directory type?
	addq.b	#1,d0
	bne.s	ifn_cknm		 ; ... no
	tst.b	stk_did(sp)		 ; are we just looking for no name?
	bmi.s	ifn_floop		 ; ... yes, ignore directory

	jsr	iou_ckdn		 ; check directory name
	movem.l stk_chan(sp),regchan
	bne.s	ifn_floop		 ; ... no
	moveq	#-hdr.len,d3
	add.l	d5,d3			 ; sub-directory dir position
	move.l	d3,stk_dpt(sp)
	move.l	d6,stk_did(sp)
	move.w	hdr_flid+chn_opwk(a0),d6 ; sub-directory file ID
	move.w	d6,chn_flid(a0) 	 ; new file ID 
	clr.l	stk_sdpt(sp)		 ; no empty entry yet
	moveq	#-hdr.len,d0
	add.l	hdr_flen+chn_opwk(a0),d0
	move.l	iod_hdrl(a4),d5
	add.l	d5,d0
	move.l	d0,chn_feof(a0) 	 ; new EOF
	move.w	hdr_name+chn_opwk(a0),d0
	addq.w	#1,d0
	move.w	d0,chn_qdid(a0) 	 ; keep directory length safe
	move.w	chn_name(a0),d0
	cmp.w	hdr_name+chn_opwk(a0),d0 ; was it actually directory?
	beq.s	ifn_sdir		 ; ... yes
	subq.w	#1,d0			 ; could it have a trailing underscore?
	cmp.w	hdr_name+chn_opwk(a0),d0
	bne.s	ifn_floop		 ; ... no
	cmp.b	#'_',chn_name+2(a0,d0.w)
	bne.l	ifn_floop		 ; ... no
	cmp.b	#ioa.kdir,chn_accs(a0)	 ; is it open directory?
	beq.s	ifn_isdir		 ; ... yes
	tas	stk_did(sp)		 ; it is possibly open directory
	bra.l	ifn_floop

ifn_cknm
	jsr	iou_cknm		 ; characters match?
	movem.l stk_chan(sp),regchan
	bne.l	ifn_floop		 ; ... no, it is not the same
	bclr	#7,stk_did(sp)		 ; ... yes, its not a directory
	move.l	d5,stk_sdpt(sp) 	 ; set d5 pointer return
	bra.s	ifn_exok

ifn_empty
	tst.l	stk_sdpt(sp)		 ; have we already found an empty entry?
	bne.l	ifn_floop		 ; ... yes
	move.l	d5,stk_sdpt(sp) 	 ; ... no, save the pointer to this one
	bra.l	ifn_floop

; name not found

ifn_fdnf
	tst.b	stk_did(sp)		 ; could it be a dirctory?
	bmi.s	ifn_isdir		 ; ... yes
	move.l	#err.fdnf,d0		 ; file not found
	bra.s	ifn_exit

ifn_isdir
	subq.w	#1,chn_name(a0) 	 ; remove underscore from end
ifn_sdir
	tas	stk_did(sp)		 ; genuine directory found
ifn_exok
	moveq	#0,d0			 ; OK
ifn_exit
	movem.l (sp)+,reglist		 ; restore regs
	moveq	#hdr.len,d1
	sub.l	d1,d5			 ; backspace pointer to start of entry
	tst.l	d0			 ; set error
	rts
	end
