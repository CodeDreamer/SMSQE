; SMSQ_CACHE Cache Handling Fiddles  V2.0   1994  Tony Tebby
;
; This module is checked after the main SMSQ OS module has been loaded
; There is no permanent cade and the select routine always returns 0.
; As a side effect, however, it sets up the cache routines in the base area
; and, if required, moves the trap #0 to #3 entry points.

	section header

	include 'dev8_mac_creg'
	include 'dev8_keys_68000'
	include 'dev8_keys_stella_bl'
	include 'dev8_smsq_smsq_base_keys'

header_base
	dc.l	cache_end-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	0			 ; loaded length
	dc.l	0			 ; checksum
	dc.l	cache_set-header_base	 ; select!!!
	dc.b	0			 ; main level
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	24,'SMSQ Cache Handling Code'
	dc.l	'    '
	dc.w	$200a

; this version sets the caches according to processor type

cache_set
	lea	cache_table,a1		 ; table
	moveq	#$70,d0
	and.b	sbl_ptype(a5),d0	 ; processor
	lsr.w	#3,d0			 ; index
cache_index
	add.l	d0,a1
	move.l	a1,a0
	add.w	(a1),a1 		 ; routine table

	move.l	a5,-(sp)
	lea	sms.cinvd,a5
	moveq	#($10*7)/2-1,d1 	 ; copy seven lots of $10 bytes
cache_copy
	move.w	(a1)+,d0		 ; data to write
	jsr	sms.wbase		 ; write it
	dbra	d1,cache_copy

	move.l	(sp)+,a5

	moveq	#0,d0			 ; nothing to load
	rts


cache_table
	dc.w	ca_000-*
	dc.w	ca_010-*
	dc.w	ca_020-*
	dc.w	ca_030-*
	dc.w	ca_040-*
	dc.w	ca_050-*
	dc.w	ca_060-*
	dc.w	ca_070-*


	xdef	ca_000,ca_020,ca_030,ca_040,cache_end ; for map
ca_000
ca_010
	rts
	dc.w	0,0,0,0,0,0,0		 ; pad to 16 bytes

	rts
	dc.w	0,0,0,0,0,0,0		 ; pad to 16 bytes

	rts
	dc.w	0,0,0,0,0,0,0		 ; pad to 16 bytes

	rts
	dc.w	0,0,0,0,0,0,0		 ; pad to 16 bytes

	rts
	dc.w	0,0,0,0,0,0,0		 ; pad to 16 bytes

	rts
	dc.w	0,0,0,0,0,0,0		 ; pad to 16 bytes

	dc.w	0,0,0,0,0,0,0,0 	 ; 16 bytes

ca_020
	rts
	dc.w	0,0,0,0,0,0,0		 ; pad to 16 bytes

	gcreg	cacr			 ; get CACR into d0
	or.w	#$0008,d0		 ; invalidate inst cache
	pcreg	cacr			 ; and set
	rts
	dc.w	0			 ; pad to 16 bytes

	moveq	#0,d0			 ; disable cache
	pcreg	cacr			 ; and set
	rts
	dc.w	0,0,0,0 		 ; pad to 16 bytes

	moveq	#0,d0			 ; disable cache
	pcreg	cacr			 ; and set
	rts
	dc.w	0,0,0,0 		 ; pad to 16 bytes

	move.w	#$0009,d0		 ; enable / inval both cache
	pcreg	cacr			 ; and set
	rts
	dc.w	0,0,0			 ; pad to 16 bytes

	move.w	#$0009,d0		 ; enable / inval inst cache
	pcreg	cacr			 ; and set
	rts
	dc.w	0,0,0			 ; pad to 16 bytes

	dc.w	12			 ; 12 bytes patch
	gcreg	cacr,d7 		 ; get CACR into d7
	or.w	#$0008,d7		 ; invalidate inst cache
	pcreg	cacr,d7 		 ; and set
	dc.w	0			 ; pad to 16 bytes

ca_030
	gcreg	cacr			 ; get CACR into d0
	or.w	#$0800,d0		 ; invalidate data cache
	pcreg	cacr			 ; and set
	rts
	dc.w	0			 ; pad to 16 bytes

	gcreg	cacr			 ; get CACR into d0
	or.w	#$0008,d0		 ; invalidate inst cache
	pcreg	cacr			 ; and set
	rts
	dc.w	0			 ; pad to 16 bytes

	moveq	#0,d0			 ; disable both caches
	pcreg	cacr			 ; and set
	rts
	dc.w	0,0,0,0 		 ; pad to 16 bytes

	gcreg	cacr			 ; get CACR into d0
	clr.b	d0			 ; disable instruction cache
	pcreg	cacr			 ; and set
	rts
	dc.w	0,0			 ; pad to 16 bytes

	move.w	#$1919,d0		 ; enable / inval both caches
	pcreg	cacr			 ; and set
	rts
	dc.w	0,0,0			 ; pad to 16 bytes

	move.w	#$1119,d0		 ; enable both / inval inst cache
	pcreg	cacr			 ; and set
	rts
	dc.w	0,0,0			 ; pad to 16 bytes

	dc.w	12			 ; 12 byte patch
	gcreg	cacr,d7 		 ; get CACR into d7
	or.w	#$0808,d7		 ; invalidate both caches
	pcreg	cacr,d7 		 ; and set
	dc.w	0

ca_040
ca_050
ca_060
ca_070
	rts
	dc.w	0,0,0,0,0,0,0		 ; pad to 16 bytes

	cinvi				 ; invalidate inst cache
	rts
	dc.w	0,0,0,0,0,0		 ; pad to 16 bytes

	cpushd				 ; push dirty data
	moveq	#0,d0			 ; disable everything
	pcreg	cacr
	rts
	dc.w	0,0,0			 ; pad to 16 bytes

	gcreg	cacr			 ; get cache control
	clr.w	d0			 ; disable all bits of inst word
	pcreg	cacr
	rts
	dc.w	0,0			 ; pad to 16 bytes

	cinva				 ; invalidate both caches
	move.l	#$a0c08000,d0		 ; enable all caches, invalidate branch
	pcreg	cacr
	rts
	dc.w	0			 ; pad to 16 bytes

	cinvi				 ; invalidate inst cache
	move.l	#$a0c08000,d0		 ; enable all caches, invalidate branch
	pcreg	cacr
	rts
	dc.w	0			 ; pad to 16 bytes

	dc.w	0,0,0,0,0,0,0		 ; no tricks on trap
	cinvi				 ; invalidate instruction cache on load

cache_end
	end
