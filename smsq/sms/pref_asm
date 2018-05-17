; Language Preference	  V2.00    1986  Tony Tebby

	section sms

	xdef	sms_pref

	include 'dev8_keys_sys'
	include 'dev8_keys_ldm'
	include 'dev8_mac_assert'

;+++
; Find preferred language dependent module
;
;	d0 c  s module type required LSW, group MSW
;	d1 c  p language number required or 0
;	d2 c  p language name
;	a5 cr	pointer to preference table / pointer to module (or 0)
;---
sms_pref
spr.reg reg	d3/d4/d5/d6/a0/a1
	movem.l spr.reg,-(sp)
	sub.l	a0,a0			 ; default default is 0
	swap	d0			 ; the group and type the right way
	move.l	sys_ldmlst(a6),d4	 ; language dependent list
	moveq	#-2,d6			 ; no table found
spr_loop
	move.l	d4,a1
	move.l	(a1)+,d4
	ble.s	spr_done		 ; no more to check
	exg	d4,a1
	assert	0,ldm_type,ldm_group-2,ldm_lang-4
	cmp.l	(a1)+,d0		 ; correct type of module
	bne.s	spr_loop		 ; no, try again
	move.w	(a1),d3 		 ; compare pref languages against this
	cmp.w	d3,d1			 ; the one we want?
	beq.s	spr_match		 ; ... yes

	move.w	d6,d5			 ; already tried one?
	bgt.s	spr_check		 ; ... yes, check this one

	addq.l	#ldp_defs,a5		 ; first language
spr_last
	addq.w	#2,d6
	tst.w	(a5)+			 ; look for last preference
	bne.s	spr_last
	subq.w	#2,a5

	move.l	a1,a0			 ; save this address as default
	move.w	d6,d5

spr_check
	cmp.w	-(a5),d3
	beq.s	spr_found
	subq.w	#2,d5
	bgt.s	spr_check

	add.w	d6,a1			 ; restore preference table
	bra.s	spr_loop

spr_found
	subq.w	#2,d5			 ; is this really the preferred?
	beq.s	spr_match		 ; ... yes
	move.l	a1,a0			 ; save new preference
	move.w	d5,d6
	bra.s	spr_loop		 ; no, we might get a better one

spr_done
	move.l	a0,a1			 ; ... the best we have found
	move.l	a1,d0			 ; any module found?
	bne.s	spr_match		 ; ... yes
	move.l	d0,a5
	bra.s	spr_exit

spr_match
	lea	ldm_module-ldm_lang(a1),a5  ; ... yes, set address
	add.l	(a5),a5
spr_exit
	movem.l (sp)+,spr.reg
	rts
	end
