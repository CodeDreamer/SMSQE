; Initialise extensions   V2.02     1992 Tony Tebby
;
; 2005-08-29  2.02  Removed debug code (MK)
;
	section init

	xdef	init_ext

	xref	smsq_base

	include 'dev8_keys_sys'
	include 'dev8_smsq_smsq_ini_keys'
	include 'dev8_smsq_smsq_base_keys'

init_ext
; move.l #$20600,-(sp)	 #####
	move.l	smsq_base-4,d0
ixt_loop
; bsr.l  blat		 #####
	move.l	d0,a0
	move.l	(a0)+,-(sp)		 ; save link to next
	jsr	(a0)			 ; call extension
	move.l	(sp)+,d0		 ; link to next
ixt_next
	bne.s	ixt_loop		 ; ... there is one
; addq.l #4,sp		 #####
	rts

	end

blat
 tst.b	 ini_ouch
 ble.s	 blat_rts
 cmp.b	 #1,ini_ouch
 bne.s	 blat_rts
 movem.l d0/a4,-(sp)
 move.l  $0c(sp),a4
 move.l  #$ffff0000,(a4)
 addq.l  #4,$0c(sp)
 move.l  #4000000,d0
 subq.l  #1,d0
 bgt.s	 *-2
 movem.l (sp)+,d0/a4
blat_rts
 rts

	end
