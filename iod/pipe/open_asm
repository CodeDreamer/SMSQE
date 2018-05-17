; Open PIPE Channel   V2.00     1989  Tony Tebby

	section pipe

	xdef	pipe_open
	xdef	pipe_name

	xref	pipe_remove

	xref	cv_decil
	xref	cv_ssteq
	xref	gu_achpp
	xref	iou_achb

	include 'dev8_keys_qlv'
	include 'dev8_keys_iod'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_hdr'
	include 'dev8_keys_qu'
	include 'dev8_keys_chn'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_iod_pipe_data'
	include 'dev8_mac_assert'
;+++
; PIPE channel open operations.
;---
pipe_open
	move.l	a0,-(sp)		 ; save name address
	move.l	#$dfdfdfdf,d0		 ; upper / lower case mask
	move.w	(a0)+,d2
	and.l	(a0)+,d0		 ; is this pipe?
	cmp.l	pipe_name,d0
	beq.s	pipo_check
pipo_nf
	moveq	#err.fdnf,d0
pipo_ex1
	bra.l	pipo_exit

	  dc.w	  4
pipe_name dc.w	'PIPE      '

pipo_check
	tst.w	d3			 ; delete?
	bpl.s	pipo_open		 ; ... no

pipo_del
	subq.w	#5,d2			 ; name?
	blt.s	pipo_nf
	beq.s	pipo_okx

	lea	1(a0),a1		 ; start of name
	bsr.s	pipo_look		 ; exists?
	bne.s	pipo_ex1

	assert	pin_nin+2,pin_nout
	tst.l	pin_nin(a2)		 ; any users at all?
	bne.l	pipo_inus		 ; ... yes
	move.l	a0,a4
	move.l	a2,a0			 ; remove this pipe
	jsr	pipe_remove
	move.l	a4,a0

pipo_okx
	moveq	#0,d0
pipo_ex2
	bra.l	pipo_exit
pipo_inam
	moveq	#err.inam,d0
	bra.s	pipo_ex2

; look for named pipe: returns a2 = named pipe

pipo_look
	lea	pil_list(a3),a2
pipo_loop
	assert	pin_link,0
	move.l	(a2),d0
	beq.s	pipol_nf		 ; no more names, new pipe
	move.l	d0,a2
	lea	pin_name(a2),a0
	move.w	(a0)+,d0
	cmp.w	d2,d0
	bne.s	pipo_loop
	jsr	cv_ssteq		 ; name matches?
	bne.s	pipo_loop		 ; ... no, try again
	rts
pipol_nf
	moveq	#err.fdnf,d0
	rts


pipo_open
	cmp.b	#ioa.kdir,d3		 ; dir?
	bne.s	pipo_opipe		 ; ... no

	subq.w	#4,d2			 ; pipe
	blt.s	pipo_nf
	beq.s	pipo_dchk		 ; just 'PIPE'

	cmp.b	#'_',(a0)		 ; underscore is acceptable
	bne.s	pipo_nf
	subq.w	#1,d2
	bgt.s	pipo_inam		 ; extra characters not allowed

    ; at this point it could be a dierectory or an input pipe

pipo_dchk
	move.w	d3,d7
	lsl.w	#2,d7
	move.l	sys_chtb(a6),a2
	move.l	(a2,d7.w),d0		 ; channel address
	bmi.s	pipo_odir		 ; not valid - it is a directory
	move.l	d0,a2
	move.l	d3,d0
	swap	d0			 ; check tag
	cmp.w	chn_tag(a2),d0
	beq.l	pipo_in 		 ; it is OK for input
*
pipo_odir
	lea	pil_list(a3),a2
	moveq	#-1,d1			 ; number of blocks
pipo_count
	assert	pin_link,0
	addq.l	#1,d1
	move.l	(a2),a2
	move.l	a2,d2
	bne.s	pipo_count

	move.l	d1,d2
	lsl.l	#2,d2			 ; pointer to table
	moveq	#pic_nmtb+4,d0		 ; size of directory channel block
	add.l	d2,d0
	jsr	iou_achb
	bne.s	pipo_ex2

	assert	pic_dirf,pic_nrnm-4,pic_ptnm-8,pic_nmtb-$c
	lea	pic_dirf(a0),a1
	subq.l	#1,(a1)+		 ; flag directory
	move.l	d1,(a1)+		 ; number in directory
	move.l	a1,(a1) 		 ; pointer
	addq.l	#4,(a1)+

	lea	pil_list(a3),a2

pipo_copyn
	assert	pin_link,0
	move.l	(a2),a2
	move.l	a2,(a1)+
	bne.s	pipo_copyn
	bra.l	pipo_ok 		 ; zero at end

pipo_opipe
	moveq	#0,d5			 ; no length given
	move.l	pil_defl(a3),d6 	 ; default length
	subq.w	#4,d2			 ; name length
	blt	pipo_nf
	beq.l	pipo_nname		 ; no name

	moveq	#'_',d4

	cmp.b	(a0),d4 		 ; underscore
	bne	pipo_nf

	move.l	a0,a1			 ; start of _name

	move.w	d2,d0
	add.w	d0,a0			 ; end of name
	move.l	a0,d7

	subq.w	#1,d0			 ; more characters after?
	beq.l	pipo_nname		 ; no

pipo_flen
	cmp.b	-(a0),d4
	dbeq	d0,pipo_flen

	move.l	a0,d2			 ; assume this is end of name
	addq.l	#1,a0
	jsr	cv_decil		 ; we need long integer
	bne.s	pipo_nnum
	move.l	d1,d5			 ; length given
	cmp.l	a0,d7			 ; all a number?
	beq.s	pipo_name		 ; ... yes
	bra.l	pipo_ipar

pipo_nnum
	move.l	d6,d1			 ; no length, use default
	move.l	d7,d2			 ; end of name

pipo_name
	moveq	#4,d6
	add.l	d1,d6			 ; pipe length
	and.w	#$fffc,d6		 ; +1 rounded up to long word

	cmp.l	a1,d2			 ; is there a name?
	beq.s	pipo_nname

	addq.l	#1,a1			 ; real start
	sub.l	a1,d2
	ble.l	pipo_ipar		 ; no name
	cmp.w	#pin.name,d2
	bhi.l	pipo_ipar		 ; name too long

	bsr	pipo_look		 ; look for name
	lea	pin_qu(a2),a2		 ; set just in case
	beq.s	pipo_ochn

	moveq	#pin_qu+qu.hdlen,d0
	add.l	d6,d0			 ; new queue
	jsr	gu_achpp
	bne.l	pipo_exit

	assert	pin_link,0
	move.l	a0,-pin_qu(a2)		 ; new name at end of list

	move.l	a0,a2
	addq.l	#pin_name,a0
	move.w	d2,(a0)+		 ; set name
	bra.s	pipo_nlend
pipo_nloop
	move.b	(a1)+,(a0)+
pipo_nlend
	dbra	d2,pipo_nloop

	lea	pin_qu(a2),a2
	move.l	d6,d1			 ; queue length
	move.w	ioq.setq,a3
	jsr	(a3)			 ; set queue

pipo_ochn
	moveq	#pic.len,d0		 ; allocate input channel
	jsr	iou_achb
	bne.s	pipo_exit
	cmp.b	#ioa.kshr,d3		 ; input pipe?
	beq.s	pipo_nin		 ; ... yes
	addq.w	#1,pin_nout-pin_qu(a2)	 ; one more output
	move.l	a2,pic_qout(a0)
	bclr	#qu.eoff,qu_eoff(a2)	 ; not end of file now
	bra.s	pipo_ok
pipo_nin
	addq.w	#1,pin_nin-pin_qu(a2)	 ; one more input
	move.l	a2,pic_qin(a0)
	bra.s	pipo_ok

pipo_nname
	tst.l	d5			 ; length given?
	beq.s	pipo_in 		 ; ... no
	moveq	#pic_qu+qu.hdlen,d0	 ; channel block + queue header
	add.l	d6,d0
	jsr	iou_achb		 ; allocate in heap
	bne.s	pipo_exit		 ; ... oops

	move.l	d6,d1			 ; length
	move.w	ioq.setq,a3
	lea	pic_qu(a0),a2
	jsr	(a3)			 ; set queue
	move.l	a2,pic_qout(a0) 	 ; and pointer
	bra.s	pipo_ok

pipo_in
	lsl.w	#2,d3			 ; look for other end
	move.l	sys_chtb(a6),a1
	add.w	d3,a1			 ; channel table
	move.l	(a1),a1 		 ; channel block
	swap	d3
	cmp.w	chn_tag(a1),d3		 ; correct channel
	bne.s	pipo_ipar		 ; ... no
	lea	iod_iolk(a3),a2
	cmp.l	chn_drvr(a1),a2 	 ; correct driver?
	bne.s	pipo_ipar		 ; ... no
	lea	pic_qu(a1),a2		 ; address of queue
	tst.l	(a2)			 ; in use?
	bne.s	pipo_inus		 ; ... yes
	moveq	#pic.len,d0		 ; allocate input channel
	jsr	iou_achb
	bne.s	pipo_exit
	move.l	a2,pic_qin(a0)
	move.l	a0,(a2)
pipo_ok
	move.l	a0,(sp)
	moveq	#0,d0
pipo_exit
	move.l	(sp)+,a0
	rts
pipo_ipar
	moveq	#err.ipar,d0
	bra.s	pipo_exit
pipo_inus
	moveq	#err.fdiu,d0
	bra.s	pipo_exit

	end
