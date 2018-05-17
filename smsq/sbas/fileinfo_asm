; SMSQ_SBAS_FILEINFO	  FileInfo Thing

	section sbas

	include 'dev8_keys_qdos_ioa'
	include 'dev8_keys_thg'
	include 'dev8_keys_err'
	include 'dev8_keys_qlv'

	xdef	sb_fileinfo

	xref	gu_thexn
	xref	gu_thini
	xref	gu_fopen
	xref	gu_fclos

sb_name dc.w	6,'SBASIC'

;+++
; Initialise Thing
;---
sb_fileinfo
	lea	sbfi_tab,a1
	jsr	gu_thini
	bne.s	sbfi_rts
	lea	sbfi_use,a2
	move.l	a2,th_use(a1)
	moveq	#0,d0
sbfi_rts
	rts

sbfi_tab
	dc.l	th_name+10		 ; size of linkage
	dc.l	sbfi_thing-*		 ; thing
	dc.l	'1.00'			 ; version
sbfi_name
	dc.w	8,'FileInfo'

sbfi_use
	move.l	a1,d6			 ; save this
	move.w	mem.achp,a0
	moveq	#$18,d1
	jsr	(a0)			 ; allocate usage block
	move.l	d6,d2
	lea	sbfi_entry,a2		 ; entry code
	move.l	#$00020004,d3		 ; set facilities
	tst.l	d0
	rts


sbfi_thing
	dc.l	thh.flag	    ; flag
	dc.l	0

sbfi_entry
sbfi.reg  reg	d1/d3/a0/a1
	movem.l sbfi.reg,-(sp)

	move.l	a0,a1
	move.w	(a1)+,d0
	add.w	d0,a1			 ; end of filename
	moveq	#3,d0
sbfi_extl
	move.b	-(a1),d1
	ror.l	#8,d1
	dbra	d0,sbfi_extl

	and.l	#$ffdfdfdf,d1		 ; upper case

	cmp.l	#'_BAS',d1		 ; _BAS file?
	beq.s	sbfi_do 		 ; ... yes
	cmp.l	#'_SAV',d1		 ; _SAV file?
	beq.s	sbfi_do 		 ; ... yes
	cmp.l	#'.BAS',d1		 ; _BAS file?
	beq.s	sbfi_do 		 ; ... yes
	cmp.l	#'.SAV',d1		 ; _SAV file?
	bne.s	sbfi_nf 		 ; ... no

sbfi_do
	tst.b	d3			 ; execute <> 0
	beq.s	sbfi_ok 		 ; ... test, just say OK

	moveq	#ioa.kshr,d3
	jsr	gu_fopen		 ; open the file
	bne.s	sbfi_exit

	clr.w	-(sp)			 ; set up parameters
	move.l	a0,-(sp)
	move.l	#$00080001,-(sp)	 ; size of parameter fram
	move.l	sp,a1			 ; parameter
	lea	sb_name,a0
	sub.l	a2,a2
	moveq	#0,d1
	move.l	#$00080000,d2
	jsr	gu_thexn

	addq.l	#4,sp
	move.l	(sp)+,a0		 ; channel ID
	addq.w	#2,sp
	bne.s	sbfi_close

	moveq	#ioa.sown,d0		 ; transfer ownership
	trap	#2
	tst.l	d0
	beq.s	sbfi_exit		 ; it has transferred
sbfi_close
	jsr	gu_fclos		 ; the job may have gone leaving the
					 ; channel so close it if open

sbfi_ok
	moveq	#0,d0
sbfi_exit
	movem.l (sp)+,sbfi.reg
	rts
sbfi_nf
	moveq	#err.fdnf,d0
	bra	sbfi_exit
	end
