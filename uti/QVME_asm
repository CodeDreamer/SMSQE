; QVME routine

	section   utility

	 include  dev8_keys_thg
	 include  dev8_keys_menu
	 include  dev8_mac_xref

	xdef	  ut_q_siz
	xdef	  ut_q_rat
	xdef	  ut_q_blk

;+++
;		Entry				Exit
;
;	A5	pointer to parameters
;
;	Error returns:	ERR.NI	Thing or THING not implemented
;---
qvme_reg   reg	d2-d3/a0-a5

ut_q_siz
	 movem.l  qvme_reg,-(sp)
	 move.l   #'SIZE',d2		     ; we need size
	 bra.s	  qvme_all
ut_q_rat
	 movem.l  qvme_reg,-(sp)
	 move.l   #'RATE',d2		     ; we need rate
	 bra.s	  qvme_all
ut_q_blk
	 movem.l  qvme_reg,-(sp)
	 move.l   #'BLNK',d2		     ; we need blank
qvme_all
	 lea	  qvme_thg,a1
	 xjsr	  ut_thuse		    ; try to use QVME-Thing
	 tst.l	  d0
	 bne.s	  no_qvme		    ; failed

	 move.l   a1,a4 		     ; address of thing
	 move.l   a5,a1

	 jsr	  thh_code(a4)		     ; do it

	 lea	  qvme_thg,a1
	 xjsr	  ut_thfre		     ; and free QVME-Extension
no_qvme
	 movem.l  (sp)+,qvme_reg
	 tst.l	  d0
	 rts

qvme_thg dc.w	 4,'QVME'


	 end
