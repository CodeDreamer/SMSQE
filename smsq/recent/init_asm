; Recent thing initialization  V1.00  (c) 2015 W. Lenerz


; 2015-10-01	1.00		initial version

	section init

	xdef	rcnt_name
	xdef	rcnt_init

	xref	rcnt_thing
	xref	gu_thjmp		; set up thing
	xref	gu_achpp		; get mem
	xref.l	rcnt_vers		; my version
	xref	rcfg_nbr
	xref	rcfg_use
	xref	rcnt_procs		; basic procedure init structure
	xref	smsq_end

	include dev8_keys_thg
	include dev8_keys_err
	include dev8_keys_k
	include dev8_keys_sys
	include dev8_keys_qdos_sms
	include dev8_keys_recent_thing
	include dev8_mac_thg
	include dev8_mac_assert
	include dev8_keys_chp
	include dev8_keys_qlv

	section header

header_base
	dc.l	rcnt_init-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-rcnt_init	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	0			 ; main level
	dc.b	0
	dc.w	smsq_name-*
smsq_name
	dc.w	nmend-*-2
	dc.b	'Recent thing'
nmend
	dc.l	rcnt_vers
	dc.w	$200a

	section base

;---------------------------------------------------------------------
;
; Initialise the thing itself, (it uses the standard USE, FREE etc calls)
;
;---------------------------------------------------------------------

rcnt_init

	lea	rcfg_use,a1
	tst.b	(a1)			; link the thing in at all?
	beq.s	rct_out 		; no

	lea	rcnt_procs,a1		; basic keywords
	move.w	sb.inipr,a2
	jsr	(a2)			; link them in

	lea	rcnt_name,a0
	moveq	#sms.zthg,d0
	jsr	gu_thjmp		; zap previous thing of same name

	clr.l	d0
	move.l	#rc_entryl,d1		; length of 1 entry in list (filename + string length +job iD + even up)
	lea	rcfg_nbr,a1
	move.b	(a1),d0 		; nbr of entries in list wished
	mulu	d1,d0			; * length of one entry
	add.l	#rcnt_end,d0		; + space for linkage & others
	move.l	d0,d1			; keep
	jsr	gu_achpp		; get mem

	move.l	a0,a1			; keep, will be ptr to thing
	add.l	a0,d1			; end of space for list
	lea	rcnt_thing,a0		; our Thing
	move.l	a0,th_thing(a1) 	; ... set pointer to it
	lea	th_verid(a1),a0
	move.l	#rcnt_vers,(a0)+	; and version
	lea	rcnt_name,a3		; thing name
	move.l	(a3)+,(a0)+		; copy
	move.l	(a3)+,(a0)+
	lea	rcnt_end(a1),a3 	; this is where the first filename goes
	move.l	a3,rcnt_first(a1)
	move.l	d1,rcnt_stop(a1)	; end of list
	moveq	#sms.lthg,d0
	jsr	gu_thjmp		; link in thing now
	moveq	#sms.info,d0
	trap	#1
	st	sys_rthg(a0)
rct_out rts

rcnt_name
	dc.w   6,'Recent',$aa

	end
