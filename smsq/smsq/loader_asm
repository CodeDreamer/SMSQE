; SMSQ Standard Loader V3.1

	xref.l	smsq_vers

; In this version, the hardware initialisation module follows the loader.
; This is followed by the operating system base module.
; The hardware initialistion module is called with d7=0.
; The processor type is saved.
; The config is copied.
; A reset routine is copied to the base of memory.
; This is followed by the hardware initialisation module.
; All the rest of the modules are then copied to base or high memory.
; The headers are thrown away and replaced by a link pointer which points to
; the link pointer of the next module.
; The operating system base module is called with suggested RAMTOP in a4.

; The stack pointer must be valid on call, the hardware initialisation
; routine may change it

	include 'dev8_keys_stella_bl'
	include 'dev8_keys_68000'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_assert'

	section header

header_base
	dc.l	loader_base-header_base  ; length of header
	dc.l	0			 ; module length unknown
	dc.l	0			 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	0			 ; main level
	dc.b	0
	dc.w	loader_name-*
	dc.l	smsq_vers
loader_name
	dc.w	18,'SMSQ System Loader'
	dc.l	'    '

	section loader

loader_base
; The next module is called to set
;			    pointer to config info in a2
;			    pointer to smsq write base routine in a3
;			    pointer to module table in a4
;			    pointer to communications block in a5
;			    pointer to module install code in a6
;			    stack pointer in a7 (usually just below sysvar)
;			    d4 may be 'ROM ' or 'RES ' (if set on call)
;			    if d7 HI RAM, d5 is top of base RAM, otherwise 0
;			    if d7 HI RAM, d6 is base of HI RAM, otherwise 0
;			    top of RAM in d7

	moveq	#0,d7			 ; first time call - memory test
	lea	ldm_end,a0
	add.l	sbl_mbase(a0),a0
	jsr	(a0)			 ; call the hardware initialisation

	move.l	a5,a0			 ; save comms block pointer
	lea	sms.machine,a5
	move.w	4(a0),d0
	jsr	(a3)			 ; set machine type
	move.w	6(a0),d0
	jsr	(a3)

	assert	sms.conf,sms.machine+4
	moveq	#(sms.ctop-sms.conf)/2-1,d1
ld_config_loop
	move.w	(a2)+,d0
	jsr	(a3)			 ; copy config info
	dbra	d1,ld_config_loop

	move.l	d7,a2			 ; write to base at top
	lea	$10(a3),a1
	move.l	-(a1),-(a2)		 ; copy write to base routine
	move.l	-(a1),-(a2)
	move.l	-(a1),-(a2)
	move.l	-(a1),-(a2)
	move.w	#jmp.l,d0
	lea	sms.wbase,a5
	jsr	(a3)			 ; set jmp to write to base routine
	move.l	a2,d0
	swap	d0
	jsr	(a3)
	swap	d0
	jsr	(a3)

	move.l	a2,d7			 ; new RAMTOP
	move.l	a0,a5			 ; comms block

	move.l	a4,a0
ldm_mttop
	tst.l	(a0)+			 ; look for end of table
	bne.s	ldm_mttop
ldm_mtcopy
	move.l	-(a0),-(sp)		 ; store table on stack
	cmp.l	a4,a0
	bne.s	ldm_mtcopy

	clr.l	-(sp)			 ; dummy first link
ldm_mtable equ 4

	move.l	sp,a3			 ; dummy first link here

	lea	ldm_mtable(sp),a4	 ; module base table
	move.l	(a4),a1
	lea	ldm_reset,a0		 ; reset routine
	moveq	#ldm_resetend-ldm_reset+$f+4,d0
	add.l	a1,d0			 ; end rounded up
	and.w	#$fff0,d0
	subq.w	#4,d0
	move.l	d0,(a4) 		 ; link for next module
	move.l	d0,a2
	jsr	(a6)			 ; install it
	subq.l	#4,a1			 ; next link

	lea	ldm_end,a0		 ; first module = hw init

ldm_loop
	move.l	sbl_mbase(a0),d3	 ; base of module
	ble.s	ldm_setram		 ; ... no more
	move.l	sbl_select(a0),d0	 ; select?
	beq.s	ldm_load		 ; ... no

ldm.sreg reg	d3/d5/d6/d7/a0/a3/a6
	movem.l ldm.sreg,-(sp)
	jsr	(a0,d0.l)		 ; do select routine
	movem.l (sp)+,ldm.sreg
	tst.b	d0
	bgt.s	ldm_load

ldm_skip
	add.l	sbl_mlength(a0),a0	 ; skip module
	add.l	d3,a0
	bra.s	ldm_loop

ldm_load
	move.l	sbl_rlength(a0),d2	 ; real length
	beq.s	ldm_skip		 ; ... none, ignore it
	lea	ldm_mtable(sp),a4	 ; module base table
ldm_blook
	move.l	(a4)+,d0		 ; another module area?
	beq.s	ldm_top
	move.l	d0,a1			 ; base of module in this area
	lea	4(a1,d2.l),a2		 ; the end of the copy
	cmp.l	(a4)+,a2		 ; is this within the area?
	bge.s	ldm_blook		 ; ... no
ldm_fload
	moveq	#$f+4,d0		 ; round up and allow for link
	add.l	a2,d0
	and.w	#$fff0,d0		 ; round up to 4 long word line
	subq.l	#4,d0			 ; link goes here
	move.l	d0,-8(a4)		 ; next module in this area
	bra.s	ldm_snext

ldm_top
	move.l	d7,a2			 ; the end in top area
	sub.l	d2,d7			 ; the start
	and.w	#$fff0,d7		 ; rounded down to line of 4 long words
	subq.l	#4,d7			 ; the link
	move.l	d7,a1

ldm_snext
	move.l	sbl_mlength(a0),d1	 ; module length
	add.l	d3,a0			 ; the start of original
	add.l	a0,d1			 ; the header of the next
	jsr	(a6)			 ; install module

	move.l	d1,a0			 ; header of next module
	bra	ldm_loop


ldm_setram
	move.l	d7,a4			 ; RAMTOP
	tst.l	d6			 ; HI RAM?
	bne.s	ldm_hiram		 ; ... yes

	move.l	d7,d0
	lea	sms.usetop,a5		 ; saved for reference
	bsr.s	ldm_wblong
	assert	sms.framb,sms.framt-4
	lea	sms.framb,a5
	moveq	#0,d0
	bsr.s	ldm_wblong		 ; no FAST RAM
	bsr.s	ldm_wblong
	bra.s	ldmr_jump

ldm_hiram
	move.l	d5,d0
	lea	sms.usetop,a5		 ; base RAM top
	bsr.s	ldm_wblong
	assert	sms.framb,sms.framt-4
	lea	sms.framb,a5
	move.l	d6,d0
	bsr.s	ldm_wblong		 ; FAST RAM
	move.l	d7,d0
	bsr.s	ldm_wblong
	bra.s	ldmr_jump

ldm_wblong
	swap	d0
	bsr.s	ldm_wbword
	swap	d0
ldm_wbword
	jmp	sms.wbase

ldm_reset
	bra.s	ldm_softreset
	dc.w	0
ldm_hreset				 ; hard reset address
	lea	ldm_reset-4(pc),a0
	move.l	(a0),a0 		 ; address of next module = hwinit
	jmp	8(a0)			 ; hard reset entry

ldm_softreset
	move.w	#$2700,sr
	lea	ldm_reset-4(pc),a0
	move.l	a0,-(sp)
	move.l	sms.usetop,d5		 ; top of base RAM
	move.l	sms.framb,d6
	move.l	sms.framt,d7
	bne.s	ldmr_hwinit
	move.l	d5,d7			 ; only one RAM
	moveq	#0,d5
ldmr_hwinit
	move.l	(a0),a0 		 ; address of next module = hwinit
	jsr	4(a0)

ldmr_jump
	move.l	(sp)+,a0		 ; first link = reset routine
	move.l	(a0),a0 		 ; next link = hardware init
	move.l	(a0),a0 		 ; next link = OS base
	jmp	4(a0)
ldm_resetend

ldm_end
	end
