; ROM at address 0 or bootable file Host Module
; v. 1.01 (c) W. Lenerz 2017
; This is a ROM module before the real ROM module.  It is used when SMSQ/E has
; been compressed and thus needs to be unpacked.
; This sets the screen mode to 1 and then unpacks the compressed code into the
; upper screen area which isn't used.
; It then jumps to the "debug" section in the original ROM in that area. That
; then copies it back to wherever it needs it.


	section base

	include 'dev8_keys_sys'
	include 'dev8_keys_q40_multiIO'
	include 'dev8_keys_q40'
	include 'dev8_keys_stella_bl'
	include 'dev8_smsq_smsq_ini_keys'
	include 'dev8_mac_creg'

	xref	dddata
	xref	unpack

show_addr equ	$fe8ffc00
strt_add equ	Q40_screen+$30000
;strt_add equ	 $100000

; NB - this calls the next module with "RES " (respr) or "ROM " (ROM) in D4

base	bra.s	user			; junk SP
	bra.s	debug
	dc.l	rom_start-base+Q40_ROM	; jump to start of ROM
;	 dc.l	 rom_start-base  ; jump to start of ROM  USE WITH SOFTROM only!

ID	dc.w	16,'  Q40           ',$0a0a

user
debug	trap	#0			; code when LRESPR'd
rom_start
	move.w	#$2700,sr		; code when in ROM
	move.b	#1,q40_dmode
	moveq.l #0,d1
	move.w	#$1fff,d0
	move.l	#$20000,a0		; clear screen
lllp1	move.l	d1,(a0)+
	dbf	d0,lllp1

cont	clr.b	ini_ouch		; clear debug flag
	move.l	#strt_add,a1		; where the decompressed code goes
	move.l	a1,a7			; set as stack...
	add.l	#500*1024,a7		; ... but far above myself
	lea	dddata+2(pc),a0 	; point to compressed data

; a0 = where to decompress from , a1 =	where to decompress to

	move.l	a0,a5			; compressed data (necessary for gzip)
	add.w	#$a,a5			: (necessary for gzip)
	move.l	a1,a4			; uncompressed data
	clr.l	d0			; (necessary for propack)

	jsr	unpack			; uncompress now
			 
;	 lea	 show,a0
;	 lea	 tsl,a2
;	 move.l  a2,d0
;	 sub.l	 a0,d0
;	 lsr.w	 #2,d0
;	 move.l  #show_addr,a2
;lpm	 move.l  (a0)+,(a2)+
;	 dbf	 d0,lpm

	pea	2(a1)			; point to decompressed code

clear_regs				; set all regs to 0
	clr.l	d0			; (simulate freshly started cpu?)
	clr.l	d1			; NB D7 NEEDS to be set to 0!
	clr.l	d2
	clr.l	d3
	clr.l	d4
	clr.l	d5
	clr.l	d6
	clr.l	d7
	move.l	d0,a0
	move.l	d0,a1
	move.l	d0,a2
	move.l	d0,a3
	move.l	d0,a4
	move.l	d0,a5
	move.l	d0,a6
	rts				; <- will actually jump to decompressed code

	end
