	section utils

	xdef	get_linkage

	include 'dev8_dv3_qlsd_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_keys_iod'

;+++
; Search QLSD WIN driver linkage block
;
;	d0  r err_fdnf or 0
;	d1  s
;	a0  r system variables
;	a3  r driver linkage block
;---
get_linkage
	moveq	#sms.info,d0
	trap	#1
	move.l	#'WIN0',d1
chk_dev lea	sys_fsdl(a0),a3 	    ; device list
srch_lp move.l	(a3),d0 		    ; is there another device?
	beq.s	err_nf			    ; no, leave with error ->
	move.l	d0,a3			    ; this is the device
	cmp.l	iod_dnam+2-iod_iolk(a3),d1  ; it is WIN?
	bne.s	srch_lp 		    ; ... no
	cmp.l	#qlsd.magic,qlsd_magic-iod_iolk(a3) ; Check if this is really the driver we expect
	bne.s	srch_lp 		    ; ... no
	sub.w	#iod_iolk,a3		    ; most code assumes no offset here
	moveq	#0,d0
	rts
err_nf
	moveq	#err.fdnf,d0
	rts

	end
