; Open HISTORY Channel	 V2.00	   1989  Tony Tebby

	section history

	xdef	history_open
	xdef	history_name

	xref	cv_decil
	xref	cv_ssteq
	xref	gu_achpp
	xref	gu_rchp
	xref	iou_achb

	include 'dev8_keys_err'
	include 'dev8_keys_qlv'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_iod_history_data'
	include 'dev8_mac_assert'

;+++
; HISTORY channel open operations.
;---
history_open
	move.l	#$dfdfdfdf,d0		 ; upper / lower case mask
	and.l	2(a0),d0		 ; is this history?
	cmp.l	#'HIST',d0
	beq.s	hop_5to7
hop_nfx
	moveq	#err.fdnf,d0
	rts

	dc.w	7
history_name dc.w 'HISTORY   '

hop_5to7
	move.l	#$dfdfdf00,d0		 ; upper / lower case mask
	and.l	6(a0),d0		 ; is this history?
	cmp.l	#'ORY'<<8,d0
	bne.s	hop_nfx

	move.l	a0,-(sp)
	move.w	(a0)+,d2		 ; name length
	addq.l	#8,a0

	tst.b	d3			 ; delete?
	bpl.s	hop_open		 ; ... no
	subq.w	#8,d2			 ; ... yes, length of name
	blt.s	hop_nf			 ; not us
	beq.s	hdl_ok			 ; no name

	lea	(a0),a1
	bsr.s	hop_look		 ; look for name
	bne.s	hop_exit

	tst.w	hid_nuse(a2)		 ; any users?
	bne.s	hop_inus		 ; ... yes

	lea	hil_list(a3),a1 	 ; unlink
	assert	hid_link,0
	move.l	a2,a0
	move.w	mem.rlst,a2
	jsr	(a2)

	jsr	gu_rchp 		 ; and get rid of it

hdl_ok
	moveq	#0,d0
hop_exit
	move.l	(sp)+,a0
	rts

hop_nf
	moveq	#err.fdnf,d0
	bra.s	hop_exit
hop_inam
	moveq	#err.inam,d0
	bra.s	hop_exit
hop_inus
	moveq	#err.fdiu,d0
	bra.s	hop_exit
hop_ipar
	moveq	#err.ipar,d0
	bra.s	hop_exit

hop_look
	lea	hil_list(a3),a2
hop_lkloop
	assert	hid_link,0
	move.l	(a2),d0
	beq.s	hop_nfx 		 ; no more names, not found
	move.l	d0,a2
	lea	hid_name(a2),a0
	move.w	(a0)+,d0
	cmp.w	d2,d0
	bne.s	hop_lkloop
	jsr	cv_ssteq		 ; name matches?
	bne.s	hop_lkloop		 ; ... no, try again
	rts

hop_open
	subq.b	#ioa.kdir,d3		 ; dir
	bne.s	hop_ofile
	subq.w	#7,d2			 ; name length
	blt.s	hop_nf
	beq.s	hop_odir
	cmp.b	#'_',-(a0)		 ; underscore is acceptable
	bne.s	hop_nf
	subq.w	#1,d2
	bgt.s	hop_inam
hop_odir
	lea	hil_list(a3),a2
	moveq	#-1,d1			 ; number of blocks
hop_count
	assert	hid_link,0
	addq.l	#1,d1
	move.l	(a2),a2
	move.l	a2,d2
	bne.s	hop_count

	move.l	d1,d2
	lsl.l	#2,d2			 ; pointer to table
	moveq	#hic_hstb+4,d0		 ; size of directory channel block
	add.l	d2,d0
	jsr	iou_achb
	bne.s	hop_exit

	assert	hic_nrhs,hic_pths-4,hic_hstb-$8
	lea	hic_nrhs(a0),a1
	move.l	d1,(a1)+		 ; number in directory
	move.l	a1,(a1) 		 ; pointer
	addq.l	#4,(a1)+

	lea	hil_list(a3),a2

hop_copyn
	assert	hid_link,0
	move.l	(a2),a2
	move.l	a2,(a1)+
	bne.s	hop_copyn
	bra.l	hop_ok			 ; zero at end

hop_ofile
	moveq	#0,d5			 ; no key
	move.l	hil_defl(a3),d6 	 ; default length
	subq.w	#7,d2			 ; name length
	blt	hop_nf
	beq.s	hop_nname		 ; no name

	moveq	#'_',d4

	cmp.b	-(a0),d4		 ; underscore?
	bne	hop_nf

	cmp.w	#2,d2			 ; key given?
	blt	hop_nf			 ; bad, bad
	beq.s	hop_key 		 ; must be key
	cmp.b	2(a0),d4		 ; something then underscore?
	bne.s	hop_num 		 ; ... no
hop_key
	addq.l	#1,a0
	move.b	(a0)+,d5		 ; set key in lsbyte
	subq.w	#2,d2
	beq.s	hop_nname		 ; and no name

hop_num
	move.l	a0,a1			 ; start of name / number
	add.w	d2,a0			 ; end of name / number
	move.l	a0,d7

hop_flen
	cmp.b	-(a0),d4		 ; find leading underscore
	bne.s	hop_flen

	move.l	a0,d2			 ; assume this is end of name
	addq.l	#1,a0
	jsr	cv_decil		 ; we need long integer
	bne.s	hop_name
	cmp.l	a0,d7			 ; all a number?
	bne	hop_ipar		 ; ... no

	moveq	#$f,d6
	add.l	d1,d6			 ; length given
	and.w	#$fff0,d6		 ; rounded up to a line

	move.l	d2,d7			 ; end of name

hop_name
	cmp.l	a1,d7			 ; is there a name?
	beq.s	hop_nname

	addq.l	#1,a1			 ; real start
	sub.l	a1,d7
	ble.l	hop_ipar		 ; no name
	cmp.w	#hid.name,d7
	bhi.l	hop_ipar		 ; name too long

	move.w	d7,d2
	bsr	hop_look		 ; look for nname
	blt.s	hop_newn		 ; ... new history
	bra.s	hop_ochn		 ; ... we have it

hop_nname
	bsr.s	hop_alhist		 ; allocate history
	bne.l	hop_exit
	move.l	a0,a2			 ; save pointer
	bra.s	hop_sethist

hop_alhist
	moveq	#hid_hbuf+hid.spre,d0
	add.l	d6,d0			 ; new history
	jmp	gu_achpp

hop_newn
	bsr.s	hop_alhist		 ; allocate history
	bne.l	hop_exit

	assert	hid_link,0
	move.l	a0,(a2) 		 ; new name at end of list

	move.l	a0,a2
	addq.l	#hid_name,a0
	move.w	d2,(a0)+		 ; set name
	bra.s	hop_nlend
hop_nloop
	move.b	(a1)+,(a0)+
hop_nlend
	dbra	d2,hop_nloop


hop_sethist
	lea	hid_hbuf(a2),a0
	add.l	d6,a0
	move.l	a0,hid_hptr(a2) 	 ; set history pointer
	move.l	a0,hid_htop(a2) 	 ; and top

hop_ochn
	moveq	#hic.bufl,d0		 ; allocate buffer
	jsr	gu_achpp
	bne.s	hop_rhist		 ; oops
	move.l	a0,a4

	moveq	#hic.len,d0		 ; allocate input channel
	jsr	iou_achb
	bne.s	hop_rbuff

	move.l	a4,hic_buff(a0)
	moveq	#hic.bufl,d0
	move.l	d0,hic_bufl(a0)
	addq.w	#1,hid_nuse(a2) 	 ; one more user
	move.l	a2,hic_hist(a0)

hop_ok
	moveq	#0,d0
	addq.l	#4,sp			 ; done
	rts


hop_rhist
	tst.w	hid_name(a2)		 ; private?
	bne.s	hop_rbuff		 ; ... no
	move.l	a1,a0
	jsr	gu_rchp 		 ; ... yes, return it
hop_rbuff
	move.l	a4,a0
	jsr	gu_rchp
	bra.l	hop_exit
	end
