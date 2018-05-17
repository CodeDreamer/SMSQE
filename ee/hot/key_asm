; Function and procedure for ALTKEYs  V2.02    1988   Tony Tebby   QJUMP

	section hotkey

	xdef	altkey
	xdef	hot_key
	xdef	hot_cmd

	xref	hot_thus
	xref	hot_thfr
	xref	hot_rter

	xref	hk_fitmc
	xref	hk_remvc
	xref	hk_newck
	xref	hk_newst

	xref	ut_gtnm1
	xref	gu_achpp

	include 'dev8_keys_err'
	include 'dev8_keys_k'
	include 'dev8_mac_assert'
	include 'dev8_ee_hk_data'
	include 'dev8_ee_hot_bv'

;+++
;    error = HOT_CMD (character,strings) setup command for ALT character
;    error = HOT_CMD (character)	 clear command for ALT character
;---
hot_cmd
	moveq	#hki.cmd,d4		 ; set command type
	bsr.s	altcmd			 ; create it
	bra.s	ha_fex
;+++
;    error = HOT_KEY (character,strings) setup strings for ALT character
;    error = HOT_KEY (character)	 clear strings for ALT character
;    error = HOT_KEY			 clear all altkey strings
;---
hot_key
	bsr.s	altkey			 ; set altkey
ha_fex
	tst.l	d6			 ; parameter error?
	bpl.l	hot_rter		 ; ... no, return it
ha_rts
	rts
;+++
;	ALTKEY character,strings	setup strings for ALT character
;	ALTKEY character		clear strings for ALT character
;	ALTKEY				clear all altkey strings
;---
altkey
	moveq	#hki.stuf,d4		 ; set altkey type
altcmd
	moveq	#0,d6			 ; no semantic error
	moveq	#0,d7
	move.l	a3,a4			 ; a3 is parameter pointer
	jsr	hot_thus		 ; find thing
	bne.s	ha_rts

	assert	hki.cmd,0
	tst.b	d4			 ; hot_cmd?
	beq.s	ha_ckstk		 ; ... yes, there must be parameters
	cmp.l	a4,a5			 ; any parameters
	beq.l	ha_rmall		 ; ... no, remove all

ha_ckstk
	exg	a3,a4
	move.l	bv_rip(a6),d5		 ; find size of RI stack
	sub.l	bv_ribas(a6),d5

	bsr.l	ut_gtnm1		 ; get altkey name
	bne.s	ha_fr4 
	moveq	#err.ipar,d0		 ; assume bad
	cmp.w	#1,(a6,a1.l)		 ; just one character?
	bne.s	ha_fr4
	move.b	2(a6,a1.l),d7		 ; this is our character
	clr.w	d6			 ; no strings
	move.l	a3,a0			 ; start pointer
ha_nxstr
	lea	-8(a5),a3		 ; top parameter
	cmp.l	a3,a0
	beq.s	ha_fr4			 ; all done
	addq.w	#1,d6
	bsr.l	ut_gtnm1		 ; get another string / name
	bne.s	ha_fr4
	subq.l	#8,a5
	move.l	a1,bv_rip(a6)		 ; stack them all up
	bra.s	ha_nxstr
ha_fr4
	move.l	a4,a3
	bne.s	ha_frer 		 ; ... oops

	add.l	bv_ribas(a6),d5
	move.l	d5,bv_rip(a6)		 ; put stack back
	sub.l	a1,d5			 ; this much stack used
	move.l	a1,a4			 ; save pointer

; before finding new definition, remove the old

ha_rkey
	bsr.s	ha_rdef 		 ; remove this definition

	tst.w	d6			 ; any strings?
	beq.s	ha_free 		 ; ... no, done

	move.w	d7,d1
	jsr	hk_newck		 ; can we add this?
	bne.s	ha_free 		 ; ... no

	moveq	#hki_name+2,d0
	add.l	d5,d0			 ; space required for ALTKEY
	bsr.l	gu_achpp		 ; allocate it in heap
	bne.s	ha_free 		 ; ... oops
	move.l	a0,a2
	assert	hki_id,hki_type-2,0
	move.w	#hki.id,(a2)+		 ; item is stuffer
	move.w	d4,(a2)+
	addq.l	#hki_name+2-4,a2

; now copy all the strings

ha_ssloop
	move.w	(a6,a4.l),d1		 ; length of string
	bra.s	ha_sclend
ha_scloop
	move.b	2(a6,a4.l),(a2)+	 ; set character
	addq.l	#1,a4
ha_sclend
	dbra	d1,ha_scloop

	subq.w	#1,d6			 ; another string?
	ble.s	ha_add			 ; ... no

	move.b	#k.nl,(a2)+		 ; put in a newline

	moveq	#3,d1			 ; fiddle around to get to next string
	add.l	a4,d1
	bclr	#0,d1
	move.l	d1,a4
	bra.s	ha_ssloop		 ; copy next string

ha_add
	lea	hki_name+2(a0),a1	 ; start of string
	move.l	a2,d1			 ; end
	sub.l	a1,d1
	move.l	a1,a2
	assert	hki_ptr+4,hki_name
	move.w	d1,-(a1)		 ; length
	move.l	a2,-(a1)		 ; pointer

	move.w	d7,d1			 ; key
	move.l	a0,a1			 ; item
	jsr	hk_newst
	bra.s	ha_free

ha_rmall		 ; remove all
ha_rloop
	bsr.s	ha_rdef 		 ; remove this definition
	subq.b	#1,d7			 ; next
	bne.s	ha_rloop		 ; ... all done
	bra.s	ha_free

ha_frer
	moveq	#-1,d6			 ; flag semantic error
ha_free
	jmp	hot_thfr		 ; free hotkey thing

; remove definition for d7

ha_rdef
	move.w	d7,d1
	jsr	hk_fitmc		 ; find item
	bne.s	ha_rrts 		 ; ... none
	cmp.w	hki_type(a1),d4 	 ; our type?
	bne.s	ha_rrts 		 ; ... no
	jsr	hk_remvc		 ; remove it
ha_rrts
	moveq	#0,d0
	rts
	end
