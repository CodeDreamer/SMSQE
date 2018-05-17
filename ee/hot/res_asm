; Functions to set resident HOTKEYs  V2.03    1988   Tony Tebby   QJUMP

	section hotkey

	xdef	hot_res
	xdef	hot_res1
	xdef	hot_chp
	xdef	hot_chp1

	xref	hot_park
	xref	hot_thar
	xref	hot_thact
	xref	hk_newck
	xref	hk_newst
	xref	hk_sprc
	xref	hk_dflts

	xref	hot_rter
	xref	gu_thjmp
	xref	gu_achpp
	xref	gu_rchp
	xref	gu_fopen
	xref	gu_fclos
	xref	gu_iowp

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_err'
	include 'dev8_keys_hdr'
	include 'dev8_keys_thg'
	include 'dev8_ee_hot_bv' 
	include 'dev8_ee_hk_data'
	include 'dev8_ee_hk_xhdr'

;+++
; error = HOT_xxx (key, filename |program name| |I| G|P|U| |window|memory|)
;---
hot_res
	moveq	#hki.xthg,d6		 ; execute thing in respr
	bra.s	hrs_do
hot_chp
	moveq	#hki.xttr,d6		 ; execute thing in heap
	bra.s	hrs_do
hot_res1
	moveq	#hki.wake,d6		 ; wake / execute in respr
	bra.s	hrs_do
hot_chp1
	moveq	#hki.wktr,d6		 ; wake / execute in heap


hrs_do
	swap	d6
	jsr	hot_park		 ; get parameters
	bne.l	hrs_rts 		 ; ... oops

frame	equ	$c
	movem.l d1/d2/d3,-(sp)		 ; keep guardian params safe
	move.w	d7,d1
	lea	hk_newck,a2		 ; check if hotkey is available
	jsr	hot_thact
	bne.l	hrs_rter

	move.l	bv_bfbas(a6),a1 	 ; filename
	move.w	d4,d0			 ; parameter string?
	bne.s	hrs_open
	move.w	d5,d0			 ; Job / wake name?
	bne.s	hrs_open
	move.w	(a6,a1.l),d0		 ; all the name
hrs_open
	move.w	(a6,a1.l),d1		 ; keep length
	move.w	d0,(a6,a1.l)		 ; ... just the filename
	lea	hrs_fexck,a2		 ; open and check
	jsr	hot_thar
	bne.l	hrs_rter
	move.l	bv_bfbas(a6),a1
	move.w	d1,(a6,a1.l)

	move.l	a0,a5			 ; channel ID

	btst	#hki..trn+16,d6 	 ; resident proc area for program itself
	bne.s	hrs_heap
	move.l	d2,d1
	moveq	#sms.arpa,d0		 ; allocate space
	movem.l d2/d3,-(sp)
	trap	#do.sms2
	movem.l (sp)+,d2/d3
	tst.l	d0
	bne.s	hrs_heap		 ; failed, try heap
	move.l	a0,a4			 ; load program here

	moveq	#(th.len+thh_strt+4+hkh.plen+hkh.hlen)/2+sms.mxjn+4,d0
	add.w	d0,d0			 ; length of thing
	jsr	gu_achpp
	bne.l	hrs_close
	bra.s	hrs_load

hrs_heap
	moveq	#(th.len+thh_strt+4+hkh.plen+hkh.hlen)/2+sms.mxjn+4,d0
	add.w	d0,d0			 ; length of thing
	move.l	d0,a4			 ; start of program
	add.l	d2,d0			 ; plus length of program
	jsr	gu_achpp
	bne.l	hrs_close		 ; oops
	add.l	a0,a4			 ; abs start of program

hrs_load
	moveq	#iof.load,d0		 ; load file
	move.l	a4,a1			 ; start of program
	exg	a0,a5
	jsr	gu_iowp
	exg	a5,a0
	bne.l	hrs_rchp		 ; oops, return heap

	move.l	bv_bfbas(a6),a1 	 ; get name into buffer
	addq.l	#2,a1
	move.w	d5,d0			 ; any name given
	beq.s	hrs_pnam		 ; ... no, use program name
	lea	1(a1,d5.w),a2
	cmp.b	#hki.jsep,-1(a6,a2.l)	 ; job name?
	bne.s	hrs_pnam
	sub.w	-2(a6,a1.l),d0
	not.w	d0			 ; length of job name
	beq.s	hrs_pnam		 ; ... none
	cmp.w	#sms.mxjn,d0		 ; too long?
	bhi.s	hrs_pnam		 ; ... yes, use program name

	move.w	d5,-2(a6,a1.l)		 ; remove job name from end
	moveq	#0,d5			 ; ... gone now

	bsr.l	hrs_mitem		 ; move item up or down to fit

hrs_sstlp
	move.b	(a6,a2.l),(a6,a1.l)	 ; copy characters
	addq.l	#1,a1
	addq.l	#1,a2
	subq.w	#1,d0
	bgt.s	hrs_sstlp
	bra.s	hrs_sthg

hrs_pnam
	lea	6(a4),a2		 ; program header
	cmp.w	#hkh.flag,(a2)+ 	 ; flagged?
	bne.s	hrs_sthg		 ; ... no, use filename
	move.w	(a2)+,d0		 ; length of program name
	beq.s	hrs_sthg		 ; ... none
	cmp.w	#sms.mxjn,d0		 ; too long?
	bls.s	hrs_pnset		 ; ... no
	moveq	#sms.mxjn,d0		 ; ... yes, limit it
hrs_pnset
	bsr.l	hrs_mitem		 ; adjust

hrs_pnlp
	move.b	(a2)+,(a6,a1.l) 	 ; copy characters
	addq.l	#1,a1
	subq.w	#1,d0
	bgt.s	hrs_pnlp

hrs_sthg
	move.l	a0,a1			 ; thing linkage address
	lea	th_name(a0),a0		 ; fill in name
	move.l	a0,a3			 ; keep pointer to name
	bsr.l	hrs_cpnm		 ; copy just program name

	lea	th_name+sms.mxjn+4(a1),a0
	move.l	a0,th_thing(a1) 	 ; set pointer to thing
	move.l	#thh.flag,(a0)+ 	 ; flag
	moveq	#tht.exec,d0		 ; type
	move.l	d0,(a0)+
	tst.b	d6			 ; impure?
	beq.s	hrs_pure
	move.l	a4,d0			 ; start of program
	sub.l	th_thing(a1),d0 	 ; relative
	move.l	d0,(a0)+
	move.l	d2,(a0)+		 ; copy all of program
	bra.s	hrs_data
hrs_pure
	moveq	#thh_strt+4+hkh.plen,d0  ; start of header
	move.l	d0,(a0)+
	moveq	#hkh.hlen+sms.mxjn+2,d0  ; length of header
	move.l	d0,(a0)+
hrs_data
	move.l	d3,(a0)+		 ; data space
	moveq	#thh_strt+4,d0
	move.l	d0,(a0)+		 ; start of code

	movem.l (sp),d1/d2/d3
	jsr	hk_sprc 		 ; fill in preamble

	move.w	#hkh.jmpl,(a0)+
	move.l	a4,(a0)+
	move.w	#hkh.flag,(a0)+
	bsr.l	hrs_cpnm		 ; copy program name (again)

	move.l	a1,a0			 ; reset base
	moveq	#sms.lthg,d0		 ; link in thing
	jsr	gu_thjmp
	st	d6

;* the code here will not work with the present item structure
;*	  beq.s   hrs_item

;*	  addq.w  #2,(a3)		   ; name two longer
;*	  move.l  a1,a0
;*	  moveq   #sms.lthg,d0		   ; and try again
;*	  jsr	  gu_thjmp
	bne.s	hrs_rchp		 ; ... oops

hrs_item
	moveq	#hki_name+4,d0		 ; allocate heap item
	move.l	bv_bfbas(a6),a1
	add.w	(a6,a1.l),d0		 ; including name
	jsr	gu_achpp
	bne.s	hrs_rthg
	move.l	a0,a1			 ; save base

	move.w	#hki.id,(a0)+		 ; set id
	swap	d6
	move.w	d6,(a0)+		 ; type
	clr.l	(a0)+			 ; pointer
	bsr.l	hrs_cnam

	move.w	d7,d1			 ; key
	lea	hk_newst,a2
	jsr	hot_thact		 ; do it
	beq.s	hrs_close		 ; ... done

	move.l	a1,a0
	jsr	gu_rchp 		 ; return this bit of heap

hrs_rthg
	move.l	d0,d4
	move.l	a3,a0
	moveq	#sms.rthg,d0		 ; remove thing
	jsr	gu_thjmp
	move.l	d4,d0
	bra.s	hrs_close

hrs_rchp
	jsr	gu_rchp 		 ; can't do it
hrs_close
	move.l	a5,a0
	jsr	gu_fclos		 ; close file
hrs_rter
	add.w	#frame,sp
	jmp	hot_rter		 ; and return

hrs_rts
	rts

hrs_mitem
	move.w	d4,d1			 ; parameter
	bne.s	hrs_miset		 ; ... its there
	move.w	d5,d1			 ; wake name
	bne.s	hrs_miset
	move.w	d0,-2(a6,a1.l)		 ; ... nothing to move - set length
	rts

hrs_miset
	move.w	d1,d2			 ; old bottom
	neg.w	d1
	add.w	-2(a6,a1.l),d1		 ; amount to move
	add.w	d1,d0			 ; new length
	move.w	d0,-2(a6,a1.l)
	sub.w	d1,d0
	sub.w	d0,d2			 ; and distance to move
	beq.s	hrs_rts 		 ; ... no move
	movem.l a1/a2,-(sp)
	blt.s	hrs_mup 		 ; ... move up

	add.w	d0,a1			 ; new bottom
	lea	(a1,d2.w),a2		 ; old bottom
hrs_mdloop
	move.b	(a6,a2.l),(a6,a1.l)
	addq.l	#1,a1
	addq.l	#1,a2
	subq.w	#1,d1
	bgt.s	hrs_mdloop
	bra.s	hrs_mdone

hrs_mup
	add.w	d0,a1			 ; new bottom
	add.w	d1,a1			 ; new top
	lea	(a1,d2.w),a2		 ; old top
hrs_muloop
	subq.l	#1,a1
	subq.l	#1,a2
	move.b	(a6,a2.l),(a6,a1.l)
	subq.w	#1,d1
	bgt.s	hrs_muloop

hrs_mdone
	tst.w	d4			 ; parameter string?
	beq.s	hrs_mwake
	sub.w	d2,d4			 ; ... move it
hrs_mwake
	tst.w	d5			 ; Wake name?
	beq.s	hrs_mexit
	sub.w	d2,d5			 ; ... move it
hrs_mexit
	movem.l (sp)+,a1/a2
	rts

hrs_cpnm
	move.l	bv_bfbas(a6),d0 	 ; copy just program name
	move.w	d4,d1			 ; parameter string
	bne.s	hrs_clen		 ; ... yes, only go this far
	move.w	d5,d1			 ; wake?
	bne.s	hrs_clen		 ; .... yes, do not include this

hrs_cnam
	move.l	bv_bfbas(a6),d0 	 ; start of name string in buffer
	move.w	(a6,d0.l),d1
hrs_clen
	move.w	d1,(a0)+		 ; length of name
hrs_cloop	 
	move.b	2(a6,d0.l),(a0)+	 ; copy name
	addq.l	#1,d0
	subq.w	#1,d1
	bgt.s	hrs_cloop
hrs_ok
	moveq	#0,d0
	rts
;+++
; Open file (a1) and check if executable
;
;	d2  r	file length
;	d3  r	dataspace
;	a0  r	channel id
;	a1 c  s pointer to file name (hkd_buf2 - set by hot_thar)
;	status return standard
;---
hrs_fexck
	lea	hkd_buf1(a3),a4 	 ; defaults buffer
	move.l	a4,-(sp)
	exg	a1,a0			 ; normal pointers
	lea	gu_fopen,a4		 ; open file
	moveq	#ioa.kshr,d3		 ; shared
	jsr	hk_dflts		 ; for defaults
	addq.l	#4,sp
	bne.s	hfc_rts 		 ; ... oops

	moveq	#iof.rhdr,d0		 ; read header
	moveq	#hdr.set,d2		 ; minimum
	jsr	gu_iowp
	bne.s	hk_fclos		 ; ... oops, close file

	cmp.b	#1,hdr_type(a1) 	 ; executable?
	bne.s	hfc_ijob		 ; ... no
	move.l	hdr_flen(a1),d2 	 ; file length
	move.l	hdr_data(a1),d3 	 ; data space
	moveq	#0,d0
hfc_rts
	rts

hfc_ijob
	moveq	#err.ijob,d0
hk_fclos
	jmp	gu_fclos
	end
