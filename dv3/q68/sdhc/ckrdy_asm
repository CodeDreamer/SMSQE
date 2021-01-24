; Q68 DV3 Check Ready Drive	 1.02			2020 W. Lenerz
; 2020-04-29	1.02 ensure drive defn is rebuilt after card initialization
; 2020-03-30	1.01 if drive not ready, try to init card  (based on MK's code)


	section dv3

	xdef	hd_ckrdy

	xref	inicrd
	xref	hd_rsint
	xref	hd_rdsect

	include 'dev8_dv3_qlsd_keys'
	include 'dev8_dv3_qlf_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_q68'
	include 'dev8_keys_qlw1'
	include 'dev8_keys_qlwa'

;+++
; This routine checks if the drive is ready and if the medium has changed
;
;	d7 c  p drive ID / number
;	a3 c  p linkage block
;	a4 c  p drive definition
;	a5 must be kept!!!!!
;	a6 c  p sysvars
;	status return 0, ERR.NC or ERR.MCHK
;
;---
reg.ckrdy reg	d1/d2/d5/a1/a2
hd_ckrdy
	movem.l reg.ckrdy,-(sp)
	lea	hdl_unit-1(a3),a1
	clr.l	d2
	move.b	(a1,d7.w),d2		; card nbr  ( -1, 0 or q68_coffst)
	blt.s	hcrw_err		; undefined card
	beq.s	hcr_tst
	moveq	#1,d2
hcr_tst
	btst	d2,ddl_rcnt(a3) 	; was card recently in use ( <2s )
	bne.s	hcrw_ok 		; ... yes, don't check for card change

; Check if update count in root sector has changed
	move.l	d2,d1
	lea	hdl_buff(a3),a1
	moveq	#0,d0
	moveq	#1,d2
	jsr	hd_rdsect		; read root sector
	beq.s	hcrw_check		; ... success, check if changed

; Read failed, try to initialise card
	lsl.l	#q68_dshft,d1		; 0 or q68.coff
	jsr	inicrd
	bne.s	hcrw_exit		; defective or missing card

	lea	hdl_buff(a3),a1 	; try to read root sector again
	moveq	#0,d0
	moveq	#1,d2
	jsr	hd_rdsect
	beq.s	hcrw_new
	bra.s	hcrw_exit

hcrw_check
; We cannot use the generic QLF/QW1 check format routine because that will try
; to interpret the root sector and error out if something's wrong. In our
; case the partition offset usually differs from card to card, so if the card
; changed then the sector we've just read is most likely NOT the root sector!
	lea	qdf_map(a4),a2		; assume QLWA drive
	moveq	#qwa_uchk/4,d0		; test up to update counter
	cmp.b	#ddf.qdos,ddf_ftype(a4) ; QLWA?
	beq.s	hcrw_loop		; ... yes

	lea	qwf_root(a4),a2 	; ... no, must be QUBIDE format then
	moveq	#qw1_updt/4,d0		; test up to update counter
hcrw_loop
	cmpm.l	(a2)+,(a1)+
	dbne	d0,hcrw_loop
	beq.s	hcrw_ok 		; update counter is still the same
hcrw_new
	sf	ddf_mstat(a4)		; card changed
hcrw_ok
	moveq	#0,d0
hcrw_exit
	movem.l (sp)+,reg.ckrdy
	rts

hcrw_err
	moveq	#err.mchk,d0		; error return
	bra.s	hcrw_exit


	end
