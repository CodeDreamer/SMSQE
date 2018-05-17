; Create or add to bootloader type file

; ex boot_file, list of files, output file; 'ID'

; If the first file is only four bytes long, it is used to set hdr_xtra.
; The file header is copied from the first (host module) file, the trailer is
; removed and subsequent files are concantenated (after calculation of length
; and checksum) before the host module trailer is replaced at the end.

	section base

	xref	boot_addmod

	include 'dev8_keys_err'
	include 'dev8_keys_hdr'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_qdos_ioa'

	data	$200		; not much space

	bra.s	start
	dc.w	0,0,$4afb,10,'Boot File '
	dc.w	$4afb		; Special job

	section special

	section main
start
	add.l	a4,a6			 ; base of data area

	moveq	#err.ipar,d0
	move.w	(sp)+,d5		 ; number of channels
	subq.w	#1,d5			 ; two?
	ble.s	suicide 		 ; ... no
	lea	(sp),a5 		 ; pointer to file list
	clr.l	-(sp)			 ; set extra
	move.l	(a5),a0 		 ; check for four byte file
	moveq	#iof.rhdr,d0
	moveq	#$40,d2
	moveq	#-1,d3
	lea	(a6),a1
	trap	#do.io
	moveq	#4,d2
	cmp.l	(a6),d2 		 ; four byte file
	bne.s	addmod			 ; ... no
	moveq	#iob.fmul,d0
	trap	#do.io
	move.l	-(a1),(sp)		 ; extra
	addq.l	#4,a5			 ; skip file
	subq.w	#1,d5
	ble.s	suicide 		 ; ... oops

addmod
	jsr	boot_addmod		 ; add modules
	bne.s	suicide

	move.l	d2,(a2) 		 ; set length in header
	move.l	d2,d4			 ; keep it

	move.l	(a5)+,a0		 ; output channel ID
	exg	a1,a2
	move.l	(sp),hdr_xtra(a1)	 ; set extra
	moveq	#14,d2
	moveq	#iof.shdr,d0		 ; set header
	bsr.s	do_io

	move.l	a2,a1
	move.l	d4,d2			 ; recover file pointers

	move.w	(a5)+,d0		 ; any string to patch (starting at $c)
	beq.s	save			 ; ... no
	lea	$c(a1),a2
patch
	move.b	(a5)+,(a2)+		 ; copy string
	subq.w	#1,d0
	bgt.s	patch

save
	moveq	#iof.save,d0
	bsr.s	do_io

suicide
	move.l	d0,d3			 ; error return
	moveq	#sms.frjb,d0		 ; force remove
	moveq	#myself,d1		 ; ... me
	trap	#do.sms2

do_io
	moveq	#forever,d3
	trap	#do.io
	tst.l	d0
	bne.s	suicide
rts
	rts

	end
