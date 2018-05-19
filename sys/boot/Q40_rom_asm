; ROM at address 0 or bootable file Host Module
; v. 1.01 modified to work if code is compressed (wl).


	section base

	filetype 0

	include 'dev8_keys_sys'
	include 'dev8_keys_q40'
	include 'dev8_keys_stella_bl'
	include 'dev8_smsq_smsq_ini_keys'
	include 'dev8_mac_creg'

; NB - this module calls the following module with "RES" (lrespr) or "ROM" in D4
; NB to NB : if the code is compressed, it is ALWAYS called with "RES"

base
	bra.s	user			; junk SP
	bra.s	debug
	dc.l	rom_start-base+Q40_ROM	; jump to start of ROM

ID	dc.w	16,'  Q40           ',$0a0a

user					; code called when LRESPR'd
	clr.b	ini_ouch		; clear debug flag
	trap	#0
	bra.s	ram_start
debug					; code called from unpack ROM
	st	q40_lowrom		; clear all existing data
	move.l	#$400,a0
	move.w	#319,d2
lplcl	clr.l	(a0)+
	dbf	d2,lplcl
	st	q40_lowram

ram_start				; common to LRESPR & unpacked code
	move.l	#'RES ',d4		; resident extension
	move.w	#$2700,sr

	lea	base,a2 		; a resident extention might
	lea	host_end,a0		; overlap the final location
	lea	$28480,a1		; copy to here (above smsq stack)
	lea	jump-base(a1),a4	; continue execution here

locate_end
	move.l	sbl_mbase(a0),d0	; header length
	beq.s	res_copy		; no more
	add.l	sbl_mlength(a0),d0
	add.l	d0,a0			; next header
	bra.s	locate_end

res_copy
	move.l	(a2)+,(a1)+		; (overrun does not matter)
	cmp.l	a0,a2			; at end yet?
	blt.s	res_copy

	clr.l	(a1)+			; zero at end

	moveq	#0,d0			; disable everything
	pcreg	vbr
	pcreg	cacr
	cpusha				; then push and invalidate

	jmp	(a4)			; should jump to jump

rom_start
	moveq	#0,d0			; disable everything
	pcreg	vbr
	pcreg	cacr
	cinva				; then invalidate

	lea	$28000,sp		; dummy stack
	lea	$20000,a0
	moveq	#$3f,d0
rom_cls
	clr.l	(a0)+
	dbra	d0,rom_cls

	move.l	#'ROM ',d4		; ROM
	clr.b	ini_ouch		; clear debug flag

jump
	lea	host_end,a0
	add.l	sbl_mbase(a0),a0	; code of next module (hw init)
	jmp	(a0)

host_end
 ; all the rest of the modules will be inserted here
trailer
	dc.l	0			; no header
	dc.l	host_end-base		; length of module
	dc.l	0			; length
	dc.l	0			; checksum
	dc.l	0			; fixup
last_zero
	dc.l	0

	end
