; Disk (floppy) OS Host Module

	section base

	include 'dev8_keys_stella_bl'

base
	trap	#0
	move.w	#$2700,sr		 ; interrupts disabled
	lea	host_end,a0
	add.l	sbl_mbase(a0),a0	 ; code of next module (= loader)
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
