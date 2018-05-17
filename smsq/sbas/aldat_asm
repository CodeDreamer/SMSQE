; SBAS_ALDAT - Allocate / Release Data Space	  V2.00       1993   Tony Tebby

	section sbas

	xdef	sb_aldat
	xdef	sb_aldat8
	xdef	sb_aldatl
	xdef	sb_aldatnp

	xdef	sb_redat
	xdef	sb_redat8
	xdef	sb_redatl

	xdef	sb_chk$heap

	xref	sb_istop
	xref	sb_insmem

	xref	gu_achp0
	xref	gu_rchp

	include 'dev8_keys_sbasic'
	include 'dev8_keys_hpm'
	include 'dev8_mac_assert'


;+++
; Allocate data space (multiples of 8)
;
; If there is not enough memory, it stops
;
;	d1 c  p space to allocate / allocated
;	a0  r	space allocated
;	a2   s
;
;---
sb_aldatnp

;+++
; Allocate data space (multiples of 8)
;
; If there is not enough memory, it stops
;
;	d1 c  p space to allocate / allocated
;	a0  r	space allocated
;	a2   s
;
;---
sb_aldat
	cmp.l	#sb.aldat/2,d1		 ; more than half a block
	ble.s	sad_do			 ; ... no, normal allocate
	bra.s	sad_alx 		 ; allocate whole block

;+++
; Allocate 8 bytes of data space
;
; If there is not enough memory, it panics.
;
;	d1  r	8
;	a0  r	space allocated
;	a2   s
;
;---
sb_aldat8
	moveq	#8,d1
;+++
; Allocate for QLiberator calls to sb_putp
;---
sb_aldatl
sad_do
	lea	sb_frdat-4(a6),a0	 ; address of pseudo free space
sad_do4

sad_loop
	move.l	a0,a2			 ; keep previous free space pointer
	move.l	hpm_nxfr(a0),d0 	 ; next free space
	beq.s	sad_almem		 ; ... out of memory
	add.l	d0,a0
	cmp.l	hpm_len(a0),d1		 ; large enough?
	bgt.s	sad_loop		 ; ... no
	beq.s	sad_fit 		 ; ... exact

	add.l	d1,hpm_nxfr(a2) 	 ; point to new free space from previous
	lea	(a0,d1.l),a2		 ; absolute
	move.l	hpm_len(a0),hpm_len(a2)
	sub.l	d1,hpm_len(a2)		 ; new length

sad_fit
	move.l	hpm_nxfr(a0),d0 	 ; next free space
	beq.s	sad_sfree		 ; ... none
	add.l	a0,d0			 ; absolute
	sub.l	a2,d0			 ; make relative
sad_sfree
	move.l	d0,hpm_nxfr(a2)
sad_done
	rts

sad_almem
	move.l	#sb.aldat,d0		 ; fixed allocation
	bsr.s	sad_achp
	movem.l d1/a0,-(sp)		 ; save return params
	move.l	d1,d0
	move.l	#sb.aldat+dt_bbase,d1	 ; size of new free space
	sub.l	d0,d1
	add.l	d0,a0			 ; and base
	bsr.l	srd_do			 ; added to heap
	movem.l (sp)+,d1/a0
sad_rts
	rts
	bra.s	sad_chkheap

sad_alx
	moveq	#8,d0
	add.l	d1,d0			 ; allocate whole block + header
sad_achp
	move.l	d0,-(sp)
	jsr	gu_achp0
	bne.s	sad_panic

	moveq	#dt_bbase-dt_data,d0	 ; -header
	add.l	(sp)+,d0
	add.l	d0,sb_datap(a6) 	 ; more space in data area

	assert	dt_bbase,dt_blen,dt_bnext-4,dt_data-8
	move.l	d0,(a0)+
	lea	sb_datal(a6),a2
	move.l	(a2),(a0)+
	move.l	a0,(a2)
sad_arts
	rts
	bra.s	sad_chkheap		 ; check the heap

sad_panic
	jmp	sb_insmem		 ; ... panic
;+++
; this routine checks if a range of addresses (a0) to (a0,d1) is within a
; standard block in the heap
;
;	d0   s
;	d1 c  p size of range
;	a0 c  p address or range
;	a2   s
;---
sad_inheap
	cmp.l	#sb.flag,sb_flag(a6)	 ; SBASIC?
	bne.s	sih_rts
sad_inheapx
	movem.l a1/a2,-(sp)
	move.l	a0,d0
	and.b	#7,d0			 ; 8 byte boundary?
	bne.s	sad_error		 ; ... no
	moveq	#7,d0			 ; 8 byte multiple block?
	and.l	d1,d0
	bne.s	sad_error		 ; ... no

	lea	-(sb.aldat-dt_data+dt_bbase)(a0),a1 ; top rel block size
	add.l	d1,a1

	move.l	sb_datal(a6),d0 	 ; data block list
sih_loop
	move.l	d0,a2
	cmp.l	a2,a0			 ; base of range above base of block?
	blt.s	sih_next		 ; ... no
	cmp.l	a2,a1			 ; top in block?
	ble.s	sih_ok			 ; ... yes
sih_next
	move.l	-(a2),d0		 ; ... next block
	bne.s	sih_loop
	bra.s	sad_error

sih_ok
	movem.l (sp)+,a1/a2
sih_rts
	rts

sad_error
	dc.l	$4afbedeb
	bra.s	sad_error
	dc.w	'HEAP'

;+++
; This routine checks the heap free space list
;---
sad_chkheap
	cmp.l	#sb.flag,sb_flag(a6)	 ; SBASIC?
	bne.s	sch_rts
	movem.l d1/a0,-(sp)
	move.l	sb_datal(a6),d0 	 ; data block list
	move.l	sb_datap(a6),d1
	sub.l	sb_datab(a6),d1 	 ; size of heap

sch_lblock
	move.l	d0,a0			 ; next block
	move.l	dt_blen(a0),d0		 ; length
	ble	sad_error
	sub.l	d0,d1			 ; take away length
	blt	sad_error		 ; linking problem
	move.l	-(a0),d0		 ; next
	bne.s	sch_lblock
	tst.l	d1			 ; size correct?
	bne.s	sad_error

	lea	sb_frdat-4(a6),a0	 ; address of pseudo free space
	move.l	hpm_nxfr(a0),d0 	 ; first free space
	beq.s	sch_done		 ; ... none
	add.l	d0,a0
sch_loop
	move.l	(a0),d1
	bsr	sad_inheapx		 ; ... in the heap?
	move.l	hpm_nxfr(a0),d0 	 ; next free space
	beq.s	sch_done		 ; ... all checked
	bmi	sad_error		 ; must not go backwards
	add.l	d0,a0
	bra.s	sch_loop

sch_done
	movem.l  (sp)+,d1/a0
sch_rts
	rts

;+++
; Release data space (multiples of 8)
;
;	d1 c s	space to allocate / allocated
;	a0 c s	space to release
;
;---
sb_redat
	cmp.l	#sb.aldat/2,d1		 ; more than half a block
	ble.s	srd_do			 ; ... no, normal release
	bra.s	srd_rlx 		 ; release whole block

;+++
; Release 8 bytes of data space
;
;	d1   r
;	a0 c s	space to release
;
;---
sb_redat8
	moveq	#8,d1
;+++
; Release for QLiberator calls to sb_putp
;---
sb_redatl
srd_do
srd.reg reg	a1/a2
	movem.l srd.reg,-(sp)
srd_do4
	lea	sb_frdat-4(a6),a1	 ; address of pseudo free space
srd_loop
	move.l	a1,a2			save pointer
	move.l	hpm_nxfr(a1),d0 	next free space
	beq.s	srd_lkin		... no more, add to end
	add.l	d0,a1
	cmp.l	a1,a0			next beyond this one?
	blt.s	srd_lkin		... yes, link in
	move.l	hpm_len(a1),d0		find end of this one
	add.l	a1,d0
	cmp.l	d0,a0			is new area contiguous?
	bne.s	srd_loop		... no

	add.l	d1,hpm_len(a1)		extend previous area
	move.l	a1,a0			and call it new
	bra.s	srd_ccont		check if contiguous with next

; link in a separate area

srd_lkin
	move.l	d1,hpm_len(a0)		set length of this one
	tst.l	d0			is there a next?
	beq.s	srd_slink		... no
	add.l	a2,d0			... yes
	sub.l	a0,d0			make relative
srd_slink
	move.l	d0,hpm_nxfr(a0) 	set link to next

	move.l	a0,d0			set previous link pointer
	sub.l	a2,d0
	move.l	d0,hpm_nxfr(a2)

srd_ccont
	move.l	hpm_nxfr(a0),d0 	next free space
	beq.s	srd_exit		... none
	cmp.l	hpm_len(a0),d0		is it at end?
	bne.s	srd_exok		... no
	move.l	hpm_nxfr(a0,d0.l),hpm_nxfr(a0) move next free pointer down
	beq.s	srd_addl		... end
	add.l	d0,hpm_nxfr(a0) 	... not end, adjust
srd_addl
	add.l	hpm_len(a0,d0.l),d0	extra length
	move.l	d0,hpm_len(a0)		set it

srd_exok
	moveq	#0,d0
srd_exit
	movem.l (sp)+,srd.reg
srd_rts
	rts
	bra	sad_chkheap


srd_rlx
	move.l	a1,-(sp)
	lea	sb_datal+4(a6),a1	 ; linked list of blocks
	move.l	a1,d0
srd_xloop
	move.l	d0,a1
	move.l	-(a1),d0		 ; next block
srd_check
	beq	sad_error		 ; heap error
	cmp.l	d0,a0			 ; this one
	bne.s	srd_xloop		 ; ... no

	move.l	-(a0),(a1)		 ; ... yes, link past
	move.l	(sp)+,a1
	move.l	d1,d0
	cmp.l	-(a0),d1		 ; the right length?
	bne	sad_error		 ; ... no
	jsr	gu_rchp 		 ; ... yes
	sub.l	d1,sb_datap(a6)
srd_rrts
	rts
	bra	sad_chkheap


;+++
; Heap checking patch
;---
sb_chk$heap
	move.w	sr,d0
	trap	#0
	lea	sad_do,a1		 ; patch allocate
	move.l	#$60000000+sad_patch-sad_do-2,(a1)
	lea	srd_do,a1		 ; patch release
	move.l	#$60000000+srd_patch-srd_do-2,(a1)

	move.w	#$4e4f,d1		 ; trap   #15	on exit then check
	lea	sad_rts,a1
	move.w	d1,(a1)
	lea	sad_arts,a1
	move.w	d1,(a1)
	lea	srd_rts,a1
	move.w	d1,(a1)
	lea	srd_rrts,a1
	move.w	d1,(a1)

	move.w	d0,sr
	bsr	sad_chkheap
	moveq	#0,d0
	rts

sad_patch
	bsr	sad_chkheap		 ; check heap before alloc
	lea	sb_frdat-4(a6),a0	 ; first instruction
	bra	sad_do4

srd_patch
	bsr	sad_inheap		 ; check release is into heap
	bsr	sad_chkheap		 ; check heap before release
	movem.l srd.reg,-(sp)		 ; first instruction
	bra	srd_do4

	end
