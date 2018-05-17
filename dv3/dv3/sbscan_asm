; DV3 Scan Slave Blocks + Utilities  V3.00    1992   Tony Tebby

	section dv3

	xdef	dv3_sbscan
	xdef	dv3_sbfile
	xdef	dv3_sbrange

	xdef	dv3_sbtr
	xdef	dv3_sbclr
	xdef	dv3_sbid

	include 'dev8_keys_sys'
	include 'dev8_dv3_keys'


; clear action routine

dcl_do
	move.b	#sbt.mpty,sbt_stat(a2)	 ; clear
	moveq	#0,d0
	rts

;+++
; DV3 clear slave blocks for drive
;
;	a4 c  p pointer to drive definition
;	a6 c  p pointer to sysvar
;
;	d1-d7/a0/a1/a3-a6 preserved
;---
dv3_sbclr
	pea	dcl_do

;+++
; DV3 scan slave blocks between bottom and top of free memory calling action
; routine for all slave blocks used by a drive. The scan is stopped on an
; error return from the action routine.
; The action routine address should be on the top of stack
;
;	a4 c  p pointer to drive definition
;	a6 c  p pointer to sysvar
;
;	d1-d7/a0-a6 preserved
;
;	error status as returned from action routine
;
; On entry to the action routine, the following registers are set
;
;	d0 cr	status byte (less ID + file bit) / returned as error
;	d1-d2	as call value - should be preserved
;	d3-d4	should be preserved
;	d5-d7	as call value - should be preserved
;	a0	should be preserved
;	a1 c  p pointer to slave block
;	a2 c  p pointer to slave block table
;	a3-a5	as call values - should be preserved
;	a6 c  p pointer to sysvar
;
;	error status as returned from action routine
;---
dv3_sbscan
dss.reg  reg	d3/d4/a0/a1/a2
	move.l	(sp)+,d0
	movem.l dss.reg,-(sp)
	move.l	d0,a0			 ; action routine address
	move.b	ddf_drid(a4),d3
	lsl.b	#4,d3			 ; drive id in msnibble of lsbyte
	bset	#sbt..fsb,d3		 ; filing system

	move.l	sys_sbtb(a6),a2 	 ; base of slave block table
	move.l	sys_fsbb(a6),a1 	 ; first fssb
	move.l	a1,d0
	sub.l	a6,d0
	lsr.l	#sbt.shft,d0
	add.l	d0,a2			 ; first vacant sbt entry

	move.l	sys_sbab(a6),d4 	 ; top of slave block area

dss_loop
	moveq	#sbt.driv,d0		 ; mask of drive bits
	and.b	sbt_stat(a2),d0
	cmp.b	d0,d3			 ; right drive?
	bne.s	dss_next		 ; ... no
	moveq	#sbt.inus,d0
	and.b	sbt_stat(a2),d0 	 ; set status
	beq.s	dss_next

	jsr	(a0)			 ; do action routine
	bne.s	dss_exit

dss_next
	addq.l	#8,a2			 ; next slave block
	add.w	#512,a1
	cmp.l	d4,a1			 ; at top?
	blo.s	dss_loop		 ; ... not yet

	moveq	#0,d0
dss_exit
	movem.l (sp)+,dss.reg
	rts
;+++
; DV3 scan slave blocks between slave block (a0) and sb (a1) calling action
; routine for all slave blocks used by a drive. The scan is stopped on an
; error return from the action routine.
; The action routine address should be on the top of stack.
;
;	a0 c  p lowest entry in slave block table
;	a1 c  p highest entry in slave block table (inclusive)
;	a4 c  p pointer to drive definition
;	a6 c  p pointer to sysvar
;
;	d1-d7/a0-a6 preserved
;
;	error status as returned from action routine
;
; On entry to the action routine, the following registers are set
;
;	d0 cr	status byte (less ID + file bit) / returned as error
;	d1-d2	as call value - should be preserved
;	d3-d4	should be preserved
;	d5-d7	as call value - should be preserved
;	a0	should be preserved
;	a1 c  p pointer to slave block
;	a2 c  p pointer to slave block table
;	a3-a5	as call values - should be preserved
;	a6 c  p pointer to sysvar
;
;	error status as returned from action routine
;---
dv3_sbrange
dsr.reg  reg	d3/d4/a0/a1/a2
	move.l	(sp)+,d0
	movem.l dsr.reg,-(sp)
	move.l	a0,a2			 ; initial slave block entry
	move.l	a1,d4			 ; top of range
	move.l	d0,a0			 ; action routine address

	move.b	ddf_drid(a4),d3
	lsl.b	#4,d3			 ; drive id in msnibble of lsbyte
	bset	#sbt..fsb,d3		 ; filing system

	move.l	a2,d0
	sub.l	sys_sbtb(a6),d0
	lsl.l	#sbt.shft,d0
	lea	(a6,d0.l),a1		 ; first slave block

dsr_loop
	moveq	#sbt.driv,d0		 ; mask of drive bits
	and.b	sbt_stat(a2),d0
	cmp.b	d0,d3			 ; right drive?
	bne.s	dsr_next		 ; ... no
	moveq	#sbt.inus,d0
	and.b	sbt_stat(a2),d0 	 ; set status
	beq.s	dsr_next

	jsr	(a0)			 ; do action routine
	bne.s	dsr_exit

dsr_next
	addq.l	#8,a2			 ; next slave block
	add.w	#512,a1
	cmp.l	d4,a2			 ; at top?
	bls.s	dsr_loop		 ; ... not yet

	moveq	#0,d0

dsr_exit
	movem.l (sp)+,dsr.reg
	rts

;+++
; DV3 change file ID  for a file
;
;	d1 c  p new file ID
;	d6 c  p old file ID
;	a2	smashed
;	a4 c  p pointer to drive definition
;	a6 c  p pointer to sysvar
;
;	d1-d7/a0/a1/a3-a6 preserved
;---
dv3_sbid
	pea	did_do
	bra.s	dv3_sbfile

; change file ID action routine

did_do
	move.w	d1,sbt_file(a2) 	 ; new file ID
	moveq	#0,d0
	rts


; truncate action routine

dst_do
	move.w	sbt_sect(a2),d0
	cmp.l	d5,d0			 ; truncate this one?
	ble.s	dst_rtok		 ; ... no
	move.b	#sbt.mpty,sbt_stat(a2)	 ; ... yes
dst_rtok
	moveq	#0,d0
	rts

;+++
; DV3 truncate slave blocks for a file from d5
;
;	d5 c  p last sector to keep (if negative, scrub all)
;	d6 c  p file ID
;	a2	smashed
;	a4 c  p pointer to drive definition
;	a6 c  p pointer to sysvar
;
;	d1-d7/a0/a1/a3-a6 preserved
;---
dv3_sbtr
	pea	dst_do

;+++
; DV3 scan slave blocks between bottom and top of free memory calling action
; routine for all slave blocks used by a file. The scan is stopped on an
; error return from the action routine
; The action routine address should be on the top of stack
;
;	d6 c  p file ID
;	a4 c  p pointer to drive definition
;	a6 c  p pointer to sysvar
;
;	d1-d7/a0-a6 preserved
;
;	error status as returned from action routine
;
; On entry to the action routine, the following registers are set
;
;	d0 cr	status byte (less ID + file bit) / returned as error
;	d1-d2	as call value - should be preserved
;	d3-d4	should be preserved
;	d5-d7	as call value - should be preserved
;	a0	should be preserved
;	a1 c  p pointer to slave block
;	a2 c  p pointer to slave block table
;	a3-a5	as call values - should be preserved
;	a6 c  p pointer to sysvar
;
;	error status as returned from action routine
;---
dv3_sbfile
dsf.reg  reg	d3/d4/a0/a1/a2

	move.l	(sp)+,d0
	movem.l dsf.reg,-(sp)
	move.l	d0,a0			 ; action routine address
	move.b	ddf_drid(a4),d3
	lsl.b	#4,d3			 ; drive id in msnibble of lsbyte
	bset	#sbt..fsb,d3		 ; filing system

	move.l	sys_sbtb(a6),a2 	 ; base of slave block table
	move.l	sys_fsbb(a6),a1 	 ; first fssb
	move.l	a1,d0
	sub.l	a6,d0
	lsr.l	#sbt.shft,d0
	add.l	d0,a2			 ; first vacant sbt entry

	move.l	sys_sbab(a6),d4 	 ; top of slave block area

dsf_loop
	cmp.w	sbt_file(a2),d6 	 ; the right file?
	bne.s	dsf_next
	moveq	#sbt.driv,d0		 ; mask of drive bits
	and.b	sbt_stat(a2),d0
	cmp.b	d0,d3			 ; right drive?
	bne.s	dsf_next		 ; ... no
	moveq	#sbt.inus,d0
	and.b	sbt_stat(a2),d0 	 ; set status
	beq.s	dsf_next

	jsr	(a0)			 ; do action routine
	bne.s	dsf_exit

dsf_next
	addq.l	#8,a2			 ; next slave block
	add.w	#512,a1
	cmp.l	d4,a1			 ; at top?
	blo.s	dsf_loop		 ; ... not yet

	moveq	#0,d0

dsf_exit
	movem.l (sp)+,dsf.reg
	rts
	end
