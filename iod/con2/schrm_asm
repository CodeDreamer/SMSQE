;	Set character attributes	V2.01	  1990 Tony Tebby
;
; 2016-04-16  2.01  Added iow.salp (MK)

	section con

	xdef	cn_sulat
	xdef	cn_sovat
	xdef	cn_scsiz
	xdef	cn_salp

	xref	cn_cksize_s
	xref	cn_dopnl
	xref	cn_scral

	include 'dev8_keys_con'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_mac_assert'
;+++
;	Set bit for underline
;
;	Registers:
;		Entry				Exit
;	D1	mode to set			smashed
;	A0	pointer to cdb
;---
cn_sulat
	and.b	#%11111110,sd_cattr(a0) 	 ; remove old attribute
	and.b	#%00000001,d1
	or.b	d1,sd_cattr(a0)
	bra.s	cns_ok

;+++
;	Set bits for mode
;
;	Registers:
;		Entry				Exit
;	D1	mode to set			smashed
;	A0	pointer to cdb
;---
cn_sovat
	and.b	#%11,d1
	lsl.b	#2,d1
	and.b	#%11110011,sd_cattr(a0)
	or.b	d1,sd_cattr(a0)
	bra.s	cns_ok
;+++
;	Set bits for character size
;
;	Registers:
;		Entry				Exit
;	D1	x size				smashed
;	D2	y size
;	A0	pointer to cdb
;---
cn_scsiz
	jsr	cn_dopnl		 ; do pending new line
	and.w	#%11,d1
	and.w	#%01,d2
	add.w	d1,d1
	add.w	d2,d1			 ; composite size 8*x+4*y
	lsl.w	#2,d1

	cmp.b	#ptm.ql8,pt_dmode(a3)	 ; which mode are we in?
	bne.s	cncs_do 		 ; not 8 colour
	or.b	#$10,d1 		 ; size 16 upwards only for mode 8
cncs_do
	move.l	cns_inc(pc,d1.w),d2	 ; character increments
	jsr	cn_cksize_s		 ; set and check size

	lsl.w	#2,d1
	and.b	#%10001111,sd_cattr(a0)
	or.b	d1,sd_cattr(a0)

	add.w	sd_ypos(a0),d2		 ; off bottom now?
	sub.w	sd_ysize(a0),d2
	ble.s	cns_nl
	move.w	d2,d1
	sub.w	d2,sd_ypos(a0)		 ; new position
	bge.s	cns_scrl
	clr.w	sd_ypos(a0)
cns_scrl
	neg.w	d1
	jsr	cn_scral		 ; scroll up
cns_nl
	swap	d2
	add.w	sd_xpos(a0),d2
	cmp.w	sd_xsize(a0),d2 	 ; overlap RHS?
	ble.s	cns_ok
	move.b	#1,sd_nlsta(a0) 	 ; pending newline

cns_ok
	moveq	#0,d0
	rts
cns_or
	moveq	#err.orng,d0
	rts

cns_inc dc.w	6,10
	dc.w	6,20
	dc.w	8,10
	dc.w	8,20
	dc.w   12,10
	dc.w   12,20
	dc.w   16,10
	dc.w   16,20

;+++
;	Set alpha blending weight
;
;	Registers:
;		Entry				Exit
;	D1	weight				preserved
;	A0	pointer to cdb
;---
cn_salp
	move.b	d1,sd_alpha(a0) 	 ; set value
	bra.s	cns_ok

	end
