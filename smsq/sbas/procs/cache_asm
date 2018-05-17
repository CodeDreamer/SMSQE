; SBAS_PROCS_CACHE - SBASIC Set Cache ON / OFF	V2.00	 1994	Tony Tebby

	section exten

	xdef	cache_on
	xdef	cache_off

	include 'dev8_keys_qdos_sms'

;+++
; CACHE_ON
;---
cache_on
	moveq	#1,d1
	bra.s	cache
;+++
; CACHE_OFF
;---
cache_off
	moveq	#0,d1

cache
	moveq	#sms.cach,d0		 ; set cache
	trap	#do.smsq
	rts
	end
