; SMSQ_CACHE Cache Handling Fiddles 68040 copyback  V2.0  1994  Tony Tebby
; 2005-11-12	2.01	fix bugs in COPYBACK mode. (TG)

; This module is checked after the main SMSQ OS module has been loaded
; There is no permanent cade and the select routine always returns 0.
; As a side effect, however, it sets up the cache routines in the base area
; and, if required, moves the trap #0 to #3 entry points.

	section header

	xref smsq_end

	include 'dev8_mac_creg'
	include 'dev8_keys_68000'
	include 'dev8_keys_stella_bl'
	include 'dev8_smsq_smsq_base_keys'

header_base
	dc.l	cache_set-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-cache_set	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	0			 ; main level
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	33,'SMSQ Copyback Cache Handling Code '
	dc.l	'2.01'
	dc.w	$200a

	section base

; this version sets only 68040 caches for copyback

cache_set
	lea	cache_40c,a1		 ; cache defs

	move.l	a5,-(sp)
	lea	sms.cinvd,a5
	moveq	#($10*7)/2-1,d1 	 ; copy seven lots of $10 bytes
cache_copy
	move.w	(a1)+,d0		 ; data to write
	jsr	sms.wbase		 ; write it
	dbra	d1,cache_copy

	lea	sms.cenab,a5
	move.w	#$4ef9,d0
	jsr	sms.wbase
	lea	enable_caches,a1
	move.l	a1,d0
	swap	d0
	jsr	sms.wbase
	lea	enable_caches,a1
	move.l	a1,d0
	jsr	sms.wbase

	lea	sms.cenai,a5
	move.w	#$4ef9,d0
	jsr	sms.wbase
	lea	enable_caches,a1
	move.l	a1,d0
	swap	d0
	jsr	sms.wbase
	lea	enable_caches,a1
	move.l	a1,d0
	jsr	sms.wbase

	move.l	(sp)+,a5

	moveq	#0,d0			 ; nothing to load
	rts

* Before we enable a cache, we must make sure that the data previously
* stored in it is either saved (for the data cache, in the event it was
* already enabled before the call) or invalidated.
*
* The previous versions of the routines didn't ensure for this and a call to
* them while the data cache was already enabled resulted in invlidating data
* that was not yet copied back (in copyback mode) !  Also, as there is no
* distinction between instruction and data cache disabling (both are disabled
* together), there should be none at enable time: the previous version of the
* routine invalidated the instruction cache only when re-enabling it, but
* didn't make sure that the data cache was enabled or invalidated !

enable_caches
	move.w	sr,-(sp)		 ; save status register.
	ori.w	#$0700,sr		 ; disable interrupts.
	gcreg	cacr			 ; get cacr in d0.
	btst	#31,d0			 ; data cache enabled ?
	beq.s	no_cpushd		 ; if no, skip...
	cpushd				 ; save our data !
no_cpushd
	cinva				 ; invalidate both caches.
	move.l	#$a0c08000,d0		 ; enable all caches, invalidate branch
	pcreg	cacr
	move.w (sp)+,sr 		 ; restore status register.
	rts

cache_40c
	rts
	dc.w	0,0,0,0,0,0,0		 ; pad to 16 bytes

	cpusha				 ; (push data and) invalidate inst cache
	rts
	dc.w	0,0,0,0,0,0		 ; pad to 16 bytes

	moveq	#0,d0			 ; disable everything
	pcreg	cacr
	cpusha				 ; then push
	rts
	dc.w	0,0,0			 ; pad to 16 bytes

	moveq	#0,d0			 ; in copyback mode disable instruction
	pcreg	cacr			 ; does not ensure that the instruction
	cpusha				 ; cache will be re-filled correctly
	rts				 ; so we must disable both
	dc.w	0,0,0

	rts				 ; this will be replaced with a jmp
	dc.w	0,0,0,0,0,0,0		 ; pad to 16 bytes.

	rts				 ; this will be replaced with a jmp
	dc.w	0,0,0,0,0,0,0		 ; pad to 16 bytes.

	dc.w	0,0,0,0,0,0,0		 ; no tricks on trap
	cpusha				 ; push data, invalidate instruction cache on load

cache_end
	end
