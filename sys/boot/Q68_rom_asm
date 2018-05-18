; ROM at address 0 or bootable file Host Module

	filetype 0

	section base

	include 'dev8_keys_sys'
	include 'dev8_keys_q68'
	include 'dev8_keys_stella_bl'
	include 'dev8_smsq_smsq_ini_keys'
	include 'dev8_mac_creg'


; NB - this module calls the following module with 0 (respr) or 4 (ROM) in D4

base
	bra.l	user
	ds.b	424			; this fills the start of the file so that
					; that the real start of the program is
	dc.l	'ici!'
					; at a sector start on the disk
ID	dc.w	14,'  Q68       ',$0a0a

user	st	led
	clr.b	ini_ouch		; clear debug flag
	move.w	#$2700,sr
	move.l	#ini_sstk,a1	       ; copy to here (above smsq stack)
	lea	-4(a1),a7		; stack in a safe palce
	clr.l	d7			; show that this is a hard reset
jump					; a4 jumps to here
	lea	host_end,a0
	add.l	sbl_mbase(a0),a0	; code of next module (hw init or loader)
	jmp	(a0)

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
