; SBAS_IDIM - Interpreter Dimension Array		 1994 Tony Tebby
; 11.09.2016  1.01 max string array size is 32766, not 32767 (wl)
	section sbas

	xdef	bo_dim		    ; dimension array

	xdef	sb_dodim
	xdef	sb_dumarray

	xref	sb_fint
	xref	sb_ierset
	xref	sb_ienimp

	xref	sb_clrval
	xref	sb_preset
	xref	sb_aldatnp
	xref	sb_redat

	include 'dev8_keys_sbasic'
	include 'dev8_keys_err4'
	include 'dev8_mac_assert'

;+++
; Within this routine
;
;	D6 is limit of arithmetic stack (with some bytes spare)
;	A1 is pointer to arithmetic stack
;	A2 is entry address
;	A3 is pointer to name table
;	A4 is pointer to program
;	A5 address of next token loop
;	A6 is pointer to system variables
;---
	
bdm_bdim
	moveq	#ern4.bdim,d0		 ; cannot do dimension
	bra.s	bdm_error

bdm_pdim
	moveq	#ern4.pdim,d0		 ; cannot do dimension
	bra.s	bdm_error

bdm_dmng
	moveq	#ern4.dmng,d0
	bra.s	bdm_error
bdm_dmov
	moveq	#ern4.dmov,d0

bdm_error
	jmp	sb_ierset


;--------------------------------- dimension array
bo_dim
	move.w	(a4)+,d3		 ; name index

	cmp.b	#nt.arr,nt_nvalp(a3,d3.w) ; array usage?
	bne.s	bdm_bdim		 ; ... no

	move.l	nt_value(a3,d3.w),a0
	move.l	(a0),d0 		 ; pointer to values
	beq.s	sb_dodim		 ; dummy
	cmp.l	a0,d0			 ; values in this alloc?
	blt.s	bdm_pdim		 ; ... below
	move.l	-(a0),d1		 ; length of alloc
	sub.l	d1,d0
	cmp.l	a0,d0
	bgt.s	bdm_pdim		 ; ... above
	jsr	sb_redat		 ; OK, release

;+++
; Do dimension operation
;
;	d3 c  p name index
;	(a4)+	number of dimensions
;	returns to (a5)
;---
sb_dodim
	move.b	#nt.arr,nt_nvalp(a3,d3.w) ; it is now array
	lea	sb_dumarray,a0
	move.l	a0,nt_value(a3,d3.w)	  ; dummy value

	move.w	(a4)+,d4		 ; number of indices
	move.w	d4,d1

	move.l	a6,a2			 ; use buffer to convert
	add.l	sb_cmdlb(a6),a2
	move.l	a2,d2			 ; save start

bdm_cmloop
	clr.w	-(a2)
	cmp.w	#ar.int,(a1)+		 ; is it an integer already?
	beq.s	bdm_move		 ; ... yes
	bsr	sb_fint
bdm_move
	move.w	(a1)+,-(a2)		 ; move
	blt.s	bdm_dmng		 ; ... not valid dimension
	subq.w	#1,d1
	bgt.s	bdm_cmloop

	move.w	d4,d0			 ; number of indices
	move.l	d2,a2
	moveq	#1,d7
	move.w	d7,-(a2)		 ; last increment is always one

	moveq	#6,d5			 ; space required for FP
	moveq	#$f,d1
	and.b	nt_vtype(a3,d3.w),d1	 ; variable type
	subq.b	#nt.fp,d1		 ; is it fp??
	beq.s	bdm_setdesc		 ; ... yes
	bgt.s	bdm_int 		 ; ... no, integer

	moveq	#1,d5			 ; size for string is char
	moveq	#1,d7			 ; round up
	add.w	-(a2),d7
;!!					 ; v.1.01
	bmi.s	bdm_dmov		 ; $7fff is not a valid string array dim
;!!
	bclr	#0,d7			 ; and make even
	move.w	d7,(a2)
	addq.w	#2,d7			 ; allow for string length
	subq.w	#1,d0			 ; is it single index?
	bne.s	bdm_setinc		 ; ... no
	move.l	d7,d5
	bra.s	bdm_alloc		 ; ... yes

bdm_int
	moveq	#2,d5			 ; size of int
bdm_setdesc
	subq.w	#1,d0			 ; last index does not require check
	beq.s	bdm_last
bdm_cloop
	moveq	#1,d1
	add.w	-(a2),d1		 ; next index size
	mulu	d1,d7
	move.l	d7,d1
	swap	d1
	tst.w	d1			 ; overflowed
	bne	bdm_dmov		 ; ... yes
bdm_setinc
	move.w	d7,-(a2)		 ; set next multiplier
	subq.w	#1,d0
	bgt.s	bdm_cloop

bdm_last
	moveq	#1,d1
	add.w	-(a2),d1		 ; last index size
	mulu	d1,d7

	move.l	d7,d1
	swap	d1			 ; msw of total size
	mulu	d5,d1
	swap	d1
	tst.w	d1			 ; overflowed?
	bne	bdm_dmov		 ; ... yes
	mulu	d7,d5
	add.l	d1,d5			 ; total size
	bvs	bdm_dmov		 ; ... overflowed
bdm_alloc
	moveq	#dt_index-dt_allc+7,d1	 ; header space required + rounding
	move.w	d4,d7
	lsl.w	#2,d4
	add.w	d4,d1			 ; all but array
	add.l	d5,d1			 ; all required
	and.w	#$fff8,d1		 ; round to multiple of 8
	jsr	sb_aldatnp		 ; allocate hole (no panic)
	assert	dt_allc+4,dt_offs,dt_nindx-4,dt_index-6
	move.l	d1,(a0)+		 ; allocation
	move.l	a0,nt_value(a3,d3.w)	 ; where the item is

	lea	dt_index(a0,d4.w),a2	 ; first element of array
	move.l	a2,(a0)+
	move.w	d7,(a0)+		 ; number of indices

	move.l	d2,a2			 ; get buffer again
	sub.w	d4,a2

bdm_cploop
	move.l	(a2)+,(a0)+
	subq.w	#1,d7
	bgt.s	bdm_cploop

;;	  cmp.b   #nt.st,nt_vtype(a3,d3.w) ; string?
;;	  bne.s   bdm_clear		   ; ... no
;;
;;	  move.w  -4(a0),d1		   ; final dimension
;;	  moveq   #2,d2
;;	  add.w   d1,d2 		   ; increment
;;	  subq.w  #1,d1
;;	  lsr.w   #1,d1 		   ; dbra
;;
;;	  move.w  #'  ',d3		   ; space filled
;;
;;bdm_sclear
;;	  clr.w   (a0)+ 		   ; no length
;;	  move.w  d1,d0
;;bdm_sploop
;;	  move.w  d3,(a0)+		   ; space fill
;;	  dbra	  d0,bdm_sploop
;;	  sub.l   d2,d5 		   ; more
;;	  bgt.s   bdm_sclear		   ; ... yes
;;	  jmp	  (a5)

bdm_clear
	move.l	d5,d1			 ; size of array
	lsr.l	#5,d1			 ; 32 bytes at a time
	moveq	#0,d0
	bra.s	bdm_c16end
bdm_c16loop
	move.l	d0,(a0)+
	move.l	d0,(a0)+
	move.l	d0,(a0)+
	move.l	d0,(a0)+
	move.l	d0,(a0)+
	move.l	d0,(a0)+
	move.l	d0,(a0)+
	move.l	d0,(a0)+
bdm_c16end
	subq.l	#1,d1
	bge.s	bdm_c16loop

	and.w	#$1f,d5
	bra.s	bdm_c2end
bdm_c2loop
	move.w	d0,(a0)+
bdm_c2end
	subq.w	#2,d5
	bge.s	bdm_c2loop

	jmp	(a5)

	dc.l	0		... no storage
sb_dumarray
	dc.l	0		... no offset
	dc.w	0		... no dimensions

	end
