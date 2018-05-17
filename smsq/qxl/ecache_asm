; QXL SMSQ/E Enable Caches and interrupts (last module)

	section header

	xref	smsq_end

header_base
	dc.l	ecache_base-header_base  ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-ecache_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	0			 ; always select
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	24,'SMSQ QXL Enable Caches  '
	dc.l	'    '
	dc.w	$200a


	section init

	include 'dev8_keys_sys'
	include 'dev8_keys_psf'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_smsq_qxl_keys'

ecache_base

; The caches are enabled

	moveq	#sms.xtop,d0
	trap	#do.sms2		 ; do code until rts as extop


; and.w #$f8ff,sr
;y
; st qxl_neth
; nop
; nop
; nop
; st qxl_netl
; nop
; nop
; nop
; bra.s y

	clr.w	sys_castt(a6)		 ; say cache on
	jmp	sms.cenab

	end
