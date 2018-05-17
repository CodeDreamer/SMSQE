; ROM / supervisor mode call Host Module

	section base

	include 'dev8_keys_stella_bl'

base
rom_base
	bra.s	start			 ; junk SP
	dc.w	0
	dc.l	start-rom_base

start
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
