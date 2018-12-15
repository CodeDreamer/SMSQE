;SMSQ Q68 DV3 Main Routine W. Lenerz 2017-2018

; 2018-05-08  1.01 preset card type in sysvars

	section header

	xref	smsq_end
	xref.l	dv3_vers

header_base
	dc.l	dv3_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-dv3_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	nm_end-*-2
	dc.b	'SMSQ DV3 DRIVERS FOR Q68'
nm_end
	dc.l	dv3_vers
	dc.w	$200a

	section base

	include dev8_keys_sys

	xref	dv3_init
	xref	win_init
	xref	dv3_addfd
	xref	qw1_table		; qubide type filesystem, links to the others
	xref	fat_init
	xref	qub_init

dv3_base
	bra.l	init

	section init

init	moveq	#0,d0
	trap	#1			; get sysvars into a0
	move.w	#$80,sys_q8ct(a0)	; preset card 2 to undetermined
					; I'm presuming here that card 1 will
					; always be an SDHC card. This is
					; certainly true on a cold reset; but
					; not necessarily on a warm reset.

	jsr	dv3_init		; initialise
	lea	qw1_table,a1
	jsr	dv3_addfd
	jsr	win_init
	jsr	fat_init
	jsr	qub_init

	rts

	end
