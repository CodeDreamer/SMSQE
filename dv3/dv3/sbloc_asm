; DV3 Locate Slave Block   V3.00    1992   Tony Tebby

	section dv3

	xdef	dv3_sbloc

	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_mac_assert'
;+++
; DV3 Locate slave block
;
;	d0  r	0, positive (not found) or err.nc (awaiting read)
;	d5 c  p sector required
;	d6 c  p file ID
;	d7 c  p drive id / number
;	a2 cr	ptr to ptr to current sb / pointer to slave block contents
;	a4 c  p pointer to drive definition
;
;	error status 0 OK, negative not complete or error, positive not found
;---
dv3_sbloc
dsl.reg  reg	d1/d2/d3/d6/d7/a0/a1
	movem.l dsl.reg,-(sp)

	swap	d6
	move.w	d5,d6			 ; file ID / file sector

	swap	d7
	lsl.w	#4,d7			 ; drive id in msnibble of lsbyte
	bset	#sbt..fsb,d7
	move.l	(a2),a1 		 ; current slave block
	move.l	a1,d3
	bne.s	dsl_ckc 		 ; ... ok
	move.l	sys_sbrp(a6),a1 	 ; ... not OK, start at current sb
	move.w	ddf_xslv(a4),d0
	assert	sbt.len,8
	lsl.w	#3,d0
	sub.w	d0,a1			 ; ... rather at start of group of sbs
dsl_ckc
	assert	sbt_file,sbt_sect-2
	cmp.l	sbt_file(a1),d6 	 ; right file / block?
	bne.s	dsl_look		 ; ... no
	moveq	#sbt.driv,d3
	and.b	sbt_stat(a1),d3
	cmp.b	d3,d7			 ; the right drive?
	bne.s	dsl_look		 ; ... no
	btst	#sbt..vld,sbt_stat(a1)	 ; valid?
	bne.l	dsl_chkx		 ; ... yes
	btst	#sbt..rrq,sbt_stat(a1)	 ; read requested?
	bne.s	dsl_nc			 ; ... yes

dsl_look
	move.l	sys_sbtb(a6),d0 	 ; base of slave block table
	move.l	sys_sbab(a6),d2 	 ; top of slave block area
	sub.l	a6,d2
	lsr.l	#sbt.shft,d2
	add.l	d0,d2			 ; top of area to look

	move.l	sys_fsbb(a6),d1 	 ; first fssb
	sub.l	a6,d1
	lsr.l	#sbt.shft,d1
	add.l	d0,d1			 ; first vacant sbt entry
	cmp.l	d1,a1			 ; our start is below bottom?
	blt.s	dsl_bot 		 ; ... yes, start at bottom
	cmp.l	d2,a1			 ; off top?
	blt.s	dsl_start		 ; ... no
dsl_bot
	move.l	d1,a1			 ; start at bottom
dsl_start
	move.l	a1,a0			 ; start both up and down here

dsl_loop
	addq.l	#8,a1			 ; next one up
	cmp.l	d2,a1			 ; at top?
	blt.s	dsl_ckup
	move.l	d1,a1			 ; start again at the bottom
dsl_ckup
	cmp.l	a1,a0			 ; all the way round?
	beq.l	dsl_nf			 ; ... yes

	assert	sbt_file,sbt_sect-2
	cmp.l	sbt_file(a1),d6 	 ; right file / block?
	bne.s	dsl_down		 ; ... no

	moveq	#sbt.driv,d3		 ; mask of drive bits
	and.b	sbt_stat(a1),d3
	cmp.b	d3,d7			 ; right drive?
	bne.s	dsl_down		 ; ... no

	btst	#sbt..vld,sbt_stat(a1)	 ; valid?
	bne.s	dsl_chkx		 ; ... yes
	btst	#sbt..rrq,sbt_stat(a1)	 ; read requested?
	bne.s	dsl_nc			 ; ... yes

dsl_down
	cmp.l	d1,a0			 ; at bottom?
	bgt.s	dsl_ckdn		 ; ... no
	move.l	d2,a0
dsl_ckdn
	subq.l	#8,a0
	cmp.l	a1,a0			 ; all the way round?
	beq.s	dsl_nf			 ; ... yes

	assert	sbt_file,sbt_sect-2
	cmp.l	sbt_file(a0),d6 	 ; right file / block?
	bne.s	dsl_loop		 ; ... no

	moveq	#sbt.driv,d3		 ; mask of drive bits
	and.b	sbt_stat(a0),d3
	cmp.b	d3,d7			 ; right drive?
	bne.s	dsl_loop		 ; ... no

	btst	#sbt..vld,sbt_stat(a0)	 ; valid?
	bne.s	dsl_chkx0		 ; ... yes
	btst	#sbt..rrq,sbt_stat(a0)	 ; read requested?
	beq.s	dsl_loop		 ; ... no

dsl_nc
	moveq	#err.nc,d0		 ; not complete
	bra.s	dsl_exit

dsl_chkx0
	move.l	a0,a1			 ; it's this one
	move.w	ddf_xslv(a4),d0
	beq.s	dsl_done
	move.l	(a1),d1 		 ; physical description
	assert	sbt.len,8
	move.w	d0,d2
	lsl.w	#3,d2
	sub.w	d2,a1			 ; back to start of group
	bra.s	dsl_chkn


dsl_chkx
	move.w	ddf_xslv(a4),d0
	beq.s	dsl_done
	move.l	(a1),d1 		 ; physical description
dsl_chkn
	subq.w	#1,d0			 ; 2 or 4 slave blocks?
	beq.s	dsl_chk2
	cmp.l	3*sbt.len(a1),d1	 ; 4th the same?
	bne.s	dsl_scrub
	cmp.l	2*sbt.len(a1),d1	 ; 3th the same?
	bne.s	dsl_scrub
dsl_chk2
	cmp.l	1*sbt.len(a1),d1	 ; 2nd the same?
	bne.s	dsl_scrub
	cmp.l	0*sbt.len(a1),d1	 ; 1st the same?
	bne.s	dsl_scrub
dsl_done
	move.l	a1,(a2) 		 ; current slave block
	move.l	a1,d0
	sub.l	sys_sbtb(a6),d0
	lsl.l	#sbt.shft,d0
	move.l	d0,a2
	add.l	a6,a2			 ; actual slave block pointer
	moveq	#0,d0			 ; zero return

dsl_exit
	movem.l (sp)+,dsl.reg
	rts

dsl_scrub
	addq.w	#1,d0
dsl_sclp
	cmp.l	(a1),d1 		 ; is this a matching SB?
	bne.s	dsl_scnxt
	move.b	#sbt.mpty,(a1)		 ; mark it as empty
dsl_scnxt
	addq.l	#8,a1
	dbra	d0,dsl_sclp

dsl_nf
	moveq	#1,d0			 ; +ve return
	bra.s	dsl_exit
	end
