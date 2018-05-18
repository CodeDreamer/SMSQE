; DV3 MSDOS Map Update		  V3.00 	  1993 Tony Tebby

	section dv3

	xdef	msd_umap
	xdef	msd_setmu

	xref	dv3_psector

	include 'dev8_dv3_keys'
	include 'dev8_keys_dos'
	include 'dev8_dv3_msd_keys'

;+++
; DV3 MSDOS Map Update
;
;	d0  r	physical sector to write
;	d1 cr	operation status, 0 on first call, 0 when done
;	d7 c  p drive ID / number
;	a1  r	pointer to sector to write
;	a3 c  p pointer to linkage
;	a4 c  p pointer to drive definition
;
;	status return zero (=d1) when done
;
;---
msd_umap
	lea	mdf_mupd(a4),a1
	tst.l	d1			 ; is is second go?
	blt.s	mum_tryf		 ; ... yes
	bgt.s	msd_ckfat
	sf	ddf_mupd(a4)		 ; map not updated now
msd_ckfat
	cmp.b	#1,dos_fats+mdf_map(a4)  ; two fats?
	ble.s	mum_final		 ; ... no
	tst.w	d1			 ; first fat sector?
	bne.s	mum_trys		 ; ... no

mum_nxt1
	addq.w	#1,d1			 ; next (ignoring root)
mum_trys
	cmp.w	mdf_msect(a4),d1	 ; all map sectors flushed?
	bge.s	mum_final
	tst.b	(a1,d1.w)		 ; this sector updated?
	beq.s	mum_nxt1		 ; ... no

;	 move.w  d1,d0			  ; this sector
;	 mulu	 ddf_slen(a4),d0	  ; offset in map
	lea	mdf_map(a4),a1
;	 add.w	 d0,a1			  ; map sector

	moveq	#0,d0
	move.w	d1,d0
	lsl.l	#8,d0
	lsl.l	#1,d0
	add.l	d0,a1

	moveq	#0,d0
	move.w	d1,d0
	subq.w	#1,d0			 ; -1
	add.w	mdf_ress(a4),d0 	 : + reserved
	add.w	mdf_sfat(a4),d0 	 ; second fat
	bra.s	mum_lsec		 ; set sector

mum_final
	moveq	#-1,d1			 ; start again, msb set
mum_nxtf
	addq.w	#1,d1
mum_tryf
	cmp.w	mdf_mdsect(a4),d1	 ; all sectors flushed?
	bge.s	mum_done
	tst.b	(a1,d1.w)		 ; this sector updated?
	beq.s	mum_nxtf		 ; ... no

	sf	(a1,d1.w)		 ; not updated now


;	 move.w  d1,d0			  ; this sector
;	 mulu	 ddf_slen(a4),d0	  ; offset in map
	lea	mdf_map(a4),a1
;	 add.w	 d0,a1			  ; map sector

	moveq	#0,d0
	move.w	d1,d0
	lsl.l	#8,d0
	lsl.l	#1,d0
	add.l	d0,a1
			
	moveq	#0,d0
	move.w	d1,d0			 ; may be this sector
	beq.s	mum_lsec		 ; root
	cmp.w	mdf_msect(a4),d0	 ; sector in map?
	blt.s	mum_ress		 ; ... yes, just add number of res sect
	sub.w	mdf_msect(a4),d0	 ; position in directory
	addq.w	#1,d0			 ; + root
	add.w	mdf_sftx(a4),d0 	 ; + sectors in fats
mum_ress
	subq.w	#1,d0			 ; now have to add number of res sects
	add.w	mdf_ress(a4),d0 	 ; sector to save
mum_lsec
	jsr	dv3_psector
	addq.w	#1,d1			 ; next sector
	rts

mum_done
	moveq	#0,d0
	moveq	#0,d1
	rts

;+++
; DV3 MSDOS mark map / root sector updated
;
;	a2 c  p pointer to bit of map updated
;	d7 c  p drive ID / number
;	a4 c  p pointer to drive definition
;
;	status return according to d0
;
;---
msd_setmu
	move.l	d0,-(sp)
	move.l	a2,d0
	sub.l	a4,d0
	sub.l	#mdf_map,d0		 ; offset of update within map area
	lsr.l	#8,d0
	lsr.w	#1,d0
	add.w	#mdf_mupd,d0		 ; offset within table
	st	(a4,d0.w)		 ; set updated
	st	ddf_mupd(a4)		 ; map updated
	move.l	(sp)+,d0
	rts
	end
