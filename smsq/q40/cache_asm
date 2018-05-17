; Extra cache handling for Q40 for SMSQ
; This adds a scheduler routine to push the data cache to the end of the list
; (i.e. the first to be added)
; This keeps the screen tidy if the copyback cache handling is used

	section header

	xref	smsq_end

header_base
	dc.l	cache_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-cache_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	28,'SMSQ Q40 copyback cache tidy'
	dc.l	'    '
	dc.w	$200a


	section cache

	include 'dev8_keys_iod'
	include 'dev8_keys_qdos_sms'
	include 'dev8_mac_creg'

cache_base
	moveq	#iod_shad+4,d1
	moveq	#0,d2
	moveq	#sms.achp,d0
	trap	#do.sms2		 ; allocate linkage

	lea	ca_sched,a1		 ; cache routine
	move.l	a1,iod_shad(a0)

	lea	iod_shlk(a0),a0 	 ; scheduler linkage
	moveq	#sms.lshd,d0
	trap	#do.sms2		 ; do code until rts as extop

	rts

;+++
; Q40 SMSQ cache server copys the data cache back
;---
ca_sched
	cpushd
	rts

	end
