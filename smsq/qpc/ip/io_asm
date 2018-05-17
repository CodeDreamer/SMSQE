; IP IO routines     V1.00				  2004  Marcel Kilgus

	section ip

	xdef	ip_io

	xref	io_ckchn

	include 'dev8_keys_err'
	include 'dev8_keys_socket'
	include 'dev8_smsq_qpc_keys'
	include 'dev8_smsq_qpc_ip_data'

ip_io
	move.l	a0,-(sp)
;	 cmp.l	 #ip.accept,d0
;	 beq.s	 ip_chan1
;	 cmp.l	 #ip.dup,d0
;	 bne.s	 ip_doio
;ip_chan1
;	 movem.l d0/d7/a0-a1/a3,-(sp)
;	 moveq	 #0,d0
;	 move.l  a1,a0			 ; check channel it
;	 jsr	 io_ckchn
;	 tst.l	 d0
;	 bmi.s	 ip_ipar
;	 cmp.l	 #chn.id,chn_id(a0)	 ; really IP channel?
;	 bne.s	 ip_ipar		 ; ... no
;	 move.l  chn_data(a0),a1	 ; data address for that channel
;	 move.l  chn_data(a0),a0	 ; Internal data
;	 dc.w	 qpc.ipio
;	 movem.l (sp)+,d0/d7/a0-a1/a3
;	 bra.s	 ip_exit

ip_doio
	move.l	chn_data(a0),a0 	; Internal data
	dc.w	qpc.ipio
ip_exit
	movem.l (sp)+,a0
	rts

;ip_ipar
;	 movem.l (sp)+,d0/d7/a0-a1/a3
;	 moveq	 #err.ipar,d0
;	 bra.s	 ip_exit

	end
