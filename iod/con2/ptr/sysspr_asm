; System sprites code				V1.00	1991 Tony Tebby
;							 2003 Marcel Kilgus
;							 2003 W. Lenerz
;
; 2004-04-02	1.01	xdef'd sp_error (wl)


	section driver

	xdef	pt_ssref
	xdef	pt_isspr
	xdef	pt_sspr
	xdef	sp_error

	xref	gu_achpp

	include 'dev8_keys_con'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_sysspr'

;+++
; Convert references to a system sprite into a real address.
;
;	A1 cr	ptr to sprite / real ptr to sprite
;	A3 c  p ptr to console linkage block
;---
pt_ssref
	movem.l d0/a2,-(sp)
	moveq	#0,d0
pt_stable
	move.l	pt_sstb(a3),a2		 ; pointer to sprite table
	cmp.w	#sp.max,a1		 ; is ptr small enough to be in table?
	bge.s	pts_fspr		 ; no, go on
	add.l	a1,a1
	add.l	a1,a1			 ; get offset in table
	move.l	2(a2,a1.l),a1		 ; thus absolute pointer to sprite
	move.l	a1,d0
	bne.s	pts_exit
	lea	sp_error,a1
	bra.s	pts_exit
pts_fspr
	tst.l	d0			 ; already checked for system sprite?
	bne.s	pts_exit		 ; yep
	tst.b	(a1)			 ; mode 0 = system sprite
	bne.s	pts_exit
	move.w	(a1),d0 		 ; take number from definition
	move.l	d0,a1
	moveq	#1,d0			 ; second run
	bra.s	pt_stable		 ; and try to locate sprite
pts_exit
	movem.l (sp)+,d0/a2
	rts

;+++
; Init system sprite table
;
;	A3 c  p ptr to console linkage block
;---
pt_isspr
	move.l	a0,-(sp)
	move.l	#sp.max,d0
	lsl.w	#2,d0		; long words
	addq.l	#8,d0		; plus header
	jsr	gu_achpp	; allocate memory for it
	bne.s	pis_rts
	move.w	#sp.max,d0
	move.w	d0,(a0)+	; save max number
	move.l	a0,pt_sstb(a3)	; save pointer
	clr.w	(a0)+		; currently no sprite in table
	subq.w	#1,d0
pis_loop
	clr.l	(a0)+
	dbf	d0,pis_loop	; ensure table is clean
	moveq	#0,d0
pis_rts
	movem.l (sp)+,a0
	rts

;+++
;  CON vector $0C			PV_SSPR
;
;	Set system sprites/Get system sprite address
;
;  Call parameters			Return parameters
;
;  D1.w sprite number / -ve		D1   pres. / Max allowed | max current
;  D2					D2   preserved
;  D3					D3+  all preserved
;
;  A0					A0   preserved
;  A1	pointer to sprite / 0		A1   preserved / pointer to sprite
;  A2					A2   preserved
;  A3	pointer to CON linkage block	A3   preserved
;  A4					A4   preserved
;  A5	not used by any routine
;  A6	not used by any routine
;
;
; this gets or sets a system sprite or returns the max nbr of system sprites
;  * if d1 is a negative nbr (-1 is suggested), then on return d1 contains:
;     max nbr of space in table for sys sprites | highest nbr of current system sprite
;  else:
;    * if a1 = 0, then one gets the address of the system sprite the number
;	of which is passed in D1. The address is returned in a1.
;	This address MAY be 0, in which case the system sprite requested does
;	not exist. This will only happen if somebody fiddled with the table
;	contrary to recommendations
;    * if a1 <> then it contains the address of a sprite that will be a system
;	sprite, d1 contains the number of that sprite. This sprite is not
;	" copied to a safe place", it is the responsibility of the calling
;	job to make sure that the sprite doesn't just disappear
;
;  Error returns:
;	IPAR	Illegal sprite number (set/get)
;	ICHN	Channel not open/ invalid channel ID (from general trap handler)
;	  NC	not complete (from general trap handler)
;	ITNF	there are no system sprites !
;---

; the sprite table has the following format:
;
;	-2	max nbr of sprites possible in table
;	0	nbr of sprites currently in table
;	2+	long word absolute pointers (i.e real addresses of sprites)
;			  ========


; sprite nbr too big for table
sspr_ipar
	moveq	#err.ipar,d0
	bra.s	sspr_out

; no sprite table
sspr_nf
	moveq	#err.itnf,d0
	bra.s	sspr_out

pt_sspr
	move.l	a2,-(sp)
	move.l	pt_sstb(a3),d0		; pointer to sprite table
	beq.s	sspr_nf 		; ... but there is none
	move.l	d0,a2			; point to sprite table
	tst.w	d1			; do we want to get the nbr of sprites?
	bmi.s	sspr_nbr		; ... yes
	move.w	-2(a2),d0		; max nbr of sprite
	cmp.w	d0,d1
	bgt.s	sspr_ipar		; sprite wished exceeds max nbr
	move.l	a1,d0			; get/set?
	bne.s	sspr_set		; set sprite
sspr_get
	moveq	#0,d0
	move.w	d1,d0
	lsl.l	#2,d0			; long word pointers
	move.l	2(a2,d0.l),a1		; absolute pointer to sprite
sspr_ok
	moveq	#0,d0			; no error
sspr_out
	move.l	(sp)+,a2
	rts

sspr_set
	move.w	(a2),d0 		; current highest nbr of system sprites
	cmp.w	d1,d0			; is sprite wished higher?
	bge.s	sspr_no 		; ...no
	move.w	d1,(a2) 		; new higest current sprite
sspr_no
	moveq	#0,d0
	move.w	d1,d0
	lsl.l	#2,d0			; pointers are long words
	lea	2(a2,d0.l),a2		; + nbr word
	move.l	a1,(a2) 		; put it in
	bra.s	sspr_ok 		;

sspr_nbr
	move.l	-2(a2),d1		; max space | current highest nbr
	bra.s	sspr_ok

; Sprite that is shown in case of an error
sp_error
	dc.w	$0100,$0000	     ;form, time/adaption
	dc.w	$0009,$0008	     ;x size, y size
	dc.w	$0000,$0000	     ;x origin, y origin
	dc.l	cp4_err-*	     ;pointer to colour pattern
	dc.l	pm4_err-*	     ;pointer to pattern mask
	dc.l	s8_err-*	     ;pointer to next definition
cp4_err
	dc.w	$00C1,$0080
	dc.w	$0063,$0000
	dc.w	$0036,$0000
	dc.w	$001C,$0000
	dc.w	$001C,$0000
	dc.w	$0036,$0000
	dc.w	$0063,$0000
	dc.w	$00C1,$0080
pm4_err
	dc.w	$C1C1,$8080
	dc.w	$6363,$0000
	dc.w	$3636,$0000
	dc.w	$1C1C,$0000
	dc.w	$1C1C,$0000
	dc.w	$3636,$0000
	dc.w	$6363,$0000
	dc.w	$C1C1,$8080

s8_err
	dc.w	$0101,$0000	;form, time/adaption
	dc.w	$000A,$000A	;x size, y size
	dc.w	$0000,$0000	;x origin, y origin
	dc.l	cp8_err-*	;pointer to colour pattern
	dc.l	pm8_err-*	;pointer to pattern mask
	dc.l	0		;pointer to next definition
cp8_err
	dc.w	$0080,$0080
	dc.w	$0080,$0080
	dc.w	$0022,$0000
	dc.w	$0022,$0000
	dc.w	$0008,$0000
	dc.w	$0008,$0000
	dc.w	$0022,$0000
	dc.w	$0022,$0000
	dc.w	$0080,$0080
	dc.w	$0080,$0080
pm8_err
	dc.w	$C0C0,$C0C0
	dc.w	$C0C0,$C0C0
	dc.w	$3333,$0000
	dc.w	$3333,$0000
	dc.w	$0C0C,$0000
	dc.w	$0C0C,$0000
	dc.w	$3333,$0000
	dc.w	$3333,$0000
	dc.w	$C0C0,$C0C0
	dc.w	$C0C0,$C0C0

	end
