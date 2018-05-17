; Set or reset fount			v2.00   1998  Tony Tebby
;
;	Registers:
;		Entry			Exit
;	A1	0/-1/^ fount 1
;	A2	0/-1/^ fount 2
;
	section con

	include 'dev8_keys_con'

	xdef	cn_fount

	xref	cn_dfnt1
	xref	cn_dfnt2

cn_fount
	cmp.l	#'DEFF',d2		; default fount?
	beq.s	cn_deff
	bsr.s	cnf_sfnt		; set a fount
	addq.l	#4,a0			; point to the other one
	addq.l	#pt_sfnt2-pt_sfnt1,a3	; and its default
	exg	a1,a2			; swap addresses
	bsr.s	cnf_sfnt		; and set it
	subq.l	#4,a0
	subq.l	#pt_sfnt2-pt_sfnt1,a3
	exg	a1,a2
	moveq	#0,d0
	rts

cnf_sfnt
	move.l	a1,d0			; setting fount?
	bne.s	cnf_ndef		; not to default
	move.l	pt_sfnt1(a3),a1 	; resetting to default
cnf_ndef
	addq.l	#1,d0			; not setting?
	beq.s	cnf_exit		; no
	move.l	a1,sd_font(a0)		; yes, set it then
cnf_exit
	rts


cn_deff
	move.l	a1,d0			; set to default of defaults?
	bne.s	cnf_df1 		; ... no
	lea	cn_dfnt1,a1		; ... yes
	bra.s	cnf_sdf1
cnf_df1
	addq.l	#1,d0			; leave unchanged?
	beq.s	cnf_ck2 		; ... yes
cnf_sdf1
	move.l	a1,pt_sfnt1(a3)

cnf_ck2
	move.l	a2,d0			; set to default of defaults?
	bne.s	cnf_df2 		; ... no
	lea	cn_dfnt2,a2		; ... yes
	bra.s	cnf_sdf2
cnf_df2
	addq.l	#1,d0			; leave unchanged?
	beq.s	cnf_rts

cnf_sdf2
	move.l	a2,pt_sfnt2(a3)
	moveq	#0,d0
cnf_rts
	rts

	end
