; SMSQ_LANG_KBD_SETT  SMSQ KBD set keyboard table    1994	Tony Tebby

	xdef	kbd_sett
	xdef	kbd_table
	xdef	kbd_thing
	xdef	kbd_tnam

	xref	ut_gxli1
	xref	ut_gxnm1
	xref	cv_upcas

	include 'dev8_keys_err'
	include 'dev8_keys_ldm'
	include 'dev8_keys_sys'
	include 'dev8_keys_iod'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_thg'
	include 'dev8_smsq_kbd_keys'
	include 'dev8_mac_thg'

	section exten


kbd_tnam dc.b	0,3,'KBD',$a

kbd_2olw dc.w  thp.opt+thp.ulng  ; optional long word
	 dc.w  thp.opt+thp.ulng  ; optional long word
	 dc.w  0

;+++
; This is the Thing with all the KBD extensions
;
;	a1 c  p parameter stack pointer
;	a2 c  p thing linkage block
;
;---
kbd_thing

KBDT	thg_extn KBDT,,kbd_2olw
kbdt.reg reg	d1/d2/d3/a0/a1/a3
	movem.l kbdt.reg,-(sp)
	move.l	(a1),d0		 ; table
	lea	-kb_thgl(a2),a3	 ; linkage
	bsr.s	kbd_sett		 ; set table
	movem.l (sp)+,kbdt.reg
	rts

kbs_ipar
	moveq	#err.ipar,d0
	bra.s	kbs_rts

;+++
; KBD_TABLE lang
;---
kbd_table
	jsr	ut_gxli1		 ; try to get an integer
	bne.s	kbt_car		 ; must be a car registration
	moveq	#0,d2
	move.l	(a6,a1.l),d1		 ; is it unset (=0)
	bne.s	kbt_link		 ; ... no, set this
kbt_car
	jsr	ut_gxnm1		 ; try to get name or string
	bne.s	kbs_rts

	move.w	(a6,a1.l),d0		 ; number of characters
	ble.s	kbs_ipar		 ; ... oops
	cmp.w	#4,d0			 ; more than 4
	bhi.s	kbs_ipar		 ; ... oops
	move.l	#'    ',-(sp)
	move.l	sp,a0
kbt_copy
	move.b	2(a6,a1.l),d1
	jsr	cv_upcas
	move.b	d1,(a0)+		 ; an upper case character
	addq.l	#1,a1
	subq.w	#1,d0
	bgt.s	kbt_copy

	move.l	(sp)+,d2		 ; car reg code
	moveq	#0,d1

kbt_link
	moveq	#sms.xtop,d0
	trap	#do.sms2
	move.l	sys_klnk(a6),a3	 ; get keyboard linkage

;+++
; Sets the pointers to the appropriate keyboard table.
;
;	d1 c  p language number or pointer to table or 0
;	d2 c  p car registration code if d1=0
;	d3   s
;	a0   s
;	a1   s
;	a3 c  p pointer to kbd linkage
;---
kbd_sett
	move.w	d1,a1
	cmp.l	d1,a1			 ; sensible address?
	beq.s	kbs_loc		 ; ... no
	move.l	d1,a1
	asr.w	#1,d1			 ; ... odd?
	bcs.s	kbs_ipar
	bra.s	kbs_puta1

kbs_loc
	moveq	#sms.fprm,d0
	moveq	#ldm.kbdt,d3
	trap	#do.sms2		 ; look in OS lists!!

kbs_put
	addq.l	#2,a1			 ; skip language code
kbs_puta1
	move.l	a1,a0
	add.w	(a1)+,a0
	add.w	(a1),a1		 ; table addresses

	move.l	a0,kb_ktab(a3)		 ; keyboard table
	move.l	a1,kb_nstab(a3)

	moveq	#0,d0
kbs_rts
	rts
	end
