; DV3 USE (prc interface) V3.00     1986  Tony Tebby  QJUMP

	section exten

	xdef	dv3_usep

	include 'dev8_dv3_keys'
	include 'dev8_keys_err'
;+++
; This is the model of the procedure RAM_USE, FLP_USE etc.
;
;	XXX_USE yyy	or   XXX_USE 
;
;	a1 c  p parameter list
;	a2 c  p thing linkage
;
;	status return standard
;---
dv3_usep
	move.l	a0,-(sp)
	lea	ddl_dname+2-ddl_thing(a2),a0 ; standard name
	move.l	4(a1),d0		 ; any parameter?
	beq.s	dus_upnam		 ; ... no
	move.l	d0,a0			 ; ... yes, this is it
	cmp.w	#3,(a0)+		 ; 3 characters long?
	bne.s	dus_ipar		 ; ... oops
dus_upnam
	move.l	(a0),d0 		 ; get new name
	and.l	#$5f5f5f00,d0		 ; in upper case
	add.b	#'0',d0 		 ; ending with '0'

	move.l	d0,ddl_dnuse+2-ddl_thing(a2) ; set new name (but not length)
	moveq	#0,d0

dus_exit
	move.l	(sp)+,a0
	rts
dus_ipar
	moveq	#err.ipar,d0
	bra.s	dus_exit
	end
