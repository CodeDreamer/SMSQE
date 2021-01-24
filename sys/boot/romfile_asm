; ROM at address 0 or bootable file Host Module

	section base

	include 'dev8_keys_stella_bl'
	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_smsq_ini_keys'


; NB - this module calls the following module with 0 (respr) or 4 (ROM) in D4

base
	bra.s	user			 ; junk SP
	bra.s	debug
	dc.l	rom_start-base		 ; jump to start of ROM

user
	clr.b	ini_ouch		 ; clear debug flag
debug
ram_start
	moveq	#0,d4			 ; start at base of next module
	trap	#0
	move.w	#$2700,sr

	lea	base,a2 		 ; a resident extention might
	lea	host_end,a0		 ; overlap the final location
	lea	$28480,a1		 ; copy to here (above smsq stack)
	lea	jump-base(a1),a4	 ; continue execution here

locate_end
	move.l	sbl_mbase(a0),d0	 ; header length
	beq.s	res_copy		 ; no more
	add.l	sbl_mlength(a0),d0
	add.l	d0,a0			 ; next header
	bra.s	locate_end

res_copy
	move.l	(a2)+,(a1)+		 ; (overrun does not matter)
	cmp.l	a0,a2			 ; at end yet?
	blt.s	res_copy

	clr.l	(a1)+			 ; zero at end
	jmp	(a4)			 ; should jump to jump


rom_start
	moveq	#4,d4			 ; start 4 in from base of next module

jump
	lea	host_end,a0
	add.l	sbl_mbase(a0),a0	 ; code of next module (hw init)
	jmp	(a0,d4.l)

host_end
 ; all the rest of the modules will be inserted here
trailer
	dc.l	0			 ; no header
	dc.l	host_end-base		 ; length of module
	dc.l	0			 ; length
	dc.l	0			 ; checksum
	dc.l	0			 ; fixup
last_zero
	dc.l	0

	end
