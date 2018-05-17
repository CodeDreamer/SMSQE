; SBAS_IDATA - DATA Statement Handling		     1994 Tony Tebby

	section sbas

	xdef	bo_readf	   ; READ
	xdef	bo_read
	xdef	bo_readd
	xdef	bo_restore

	xdef	sb_dpset
	xdef	sb_dlsiset

	xref	sb_ierset

	xref	sb_fint

	xref	sb_adddst
	xref	sb_dstadd

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

;------------------------------------------------------------------
bo_restore
	cmp.w	#ar.int,(a1)+		 ; is it an integer already?
	beq.s	brst_chk		 ; ... yes
	jsr	sb_fint
brst_chk
	move.w	(a1)+,d0
	bgt.s	brst_set
	moveq	#1,d0			 ; always start at 1
brst_set
	swap	d0
	move.w	#$100,d0		 ; line and item number
	move.l	d0,sb_dline(a6)
	move.l	a5,-(sp)

;+++
; Set data item pointer (points to parameter of preceding RTS2)
;---
sb_dpset
	move.l	sb_dtstb(a6),d0 	 ; any data statements?
	beq.s	sdp_end 		 ; ... no
	move.l	sb_dline(a6),d0 	 ; next line, statement, item
	jsr	sb_dstadd
	bmi.s	sdp_end
	move.l	d0,a0
	move.b	sb_ditem(a6),d1 	 ; number of items to skip
	bra.s	sdp_check

sdp_item
	add.w	d0,a0			 ; next item
sdp_check
	move.w	-2(a0),d0		 ; length of next item
	beq.s	sdp_end
	subq.b	#1,d1
	bcc.s	sdp_item

	move.l	a0,sb_ndata(a6) 	 ; next data item
	rts

sdp_end
	moveq	#-1,d0
	move.l	d0,sb_ndata(a6) 	 ; mark end of data
	rts

;--------------------------------------------------- READ
bo_readf
	tst.l	sb_readp(a6)		 ; program pointer saved?
	bne.s	bor_recr		 ; ... yes, recursive reads!
	bsr.s	sb_dpset		 ; find first data item
	move.l	sb_ndata(a6),sb_readd(a6) ; save data pointer

;---------------------------------------------------
bo_read
	move.l	a4,sb_readp(a6) 	 ; save program pointer
	move.l	sb_ndata(a6),d0
	bmi.s	bor_eod 		 ; end of data
	move.l	d0,a4
	move.l	a4,a0
	move.w	-2(a4),d0		 ; length of this item
	beq.s	bor_eod 		 ; end of file
	add.w	d0,a0			 ; next item
	move.l	a0,sb_ndata(a6) 	 ; save it
	jsr	(a5)			 ; evaluate next data value
	move.l	sb_readp(a6),a4
	jmp	(a5)			 ; and continue

bor_eod
	move.l	sb_readp(a6),a4 	 ; failure address
	clr.l	sb_readp(a6)

	moveq	#ern4.eod,d0
	jmp	sb_ierset

bor_recr
	clr.l	sb_readp(a6)		 ; clear read to get error address
	moveq	#ern4.recr,d0
	jmp	sb_ierset

;--------------------------------------------------------
bo_readd
	clr.l	sb_readp(a6)		 ; no longer in read
	move.l	a5,-(sp)

sb_dlsiset
	tst.l	sb_dttbb(a6)		 ; any data statement table?
	beq.s	sdl_rts 		 ; ... no

	move.l	sb_ndata(a6),d0 	 ; pointer to next data item
	bmi.s	sdl_eod 		 ; end of data
	move.l	d0,d4
	jsr	sb_adddst		 ; convert to statement
	tst.l	d0			 ; it exists?
	bmi.s	sdl_eod
	bra.s	sdl_eloop

sdl_loop
	addq.b	#1,d0			 ; next item
	add.w	d1,a2			 ; where it is
sdl_eloop
	move.w	-2(a2),d1
	beq.s	sdl_eod
	cmp.l	d4,a2			 ; here yet?
	blt.s	sdl_loop

	move.l	d0,sb_dline(a6) 	 ; save it
	rts

sdl_eod
	move.l	sb_dttbp(a6),a0 	 ; last data statement
	move.l	st_line-st.len(a0),d0
	addq.b	#1,d0			 ; next statement
	lsl.w	#8,d0
	move.l	d0,sb_dline(a6) 	 ; next data item off end of data
	moveq	#-1,d0
	move.l	d0,sb_ndata(a6)
sdl_rts
	rts

	end
