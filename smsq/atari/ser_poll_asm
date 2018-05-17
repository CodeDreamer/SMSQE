; SER polling routine  V2.00	 1991 Tony Tebby

	section ser

	xdef	ser_poll

	include 'dev8_keys_atari'
	include 'dev8_keys_atari_scc'
	include 'dev8_keys_buf'
	include 'dev8_keys_par'
	include 'dev8_mac_assert'

;+++
; SER polling routine
; If there is a carrier detect timout, it checks it.
;---
ser_poll
	move.w	prd_cdef(a3),d1 	 ; ticks to end of file?
	beq.s	spl_ser2		 ; ... none
	move.l	prd_ibuf(a3),d0 	 ; buffer?
	beq.s	spl_ser2		 ; ... no
	move.l	d0,a2
	btst	#mfp..dcd,mfp_dcd	 ; carrier?
	beq.s	spl_cact1		 ; ... yes
	subq.w	#1,prd_cdct(a3) 	 ; ... no, countdown
	bgt.s	spl_ser2		 ; still counting
	blt.s	spl_supp1		 ; ... suppress it
	tas	buf_eoff(a2)		 ; ... timed out
	bra.s	spl_ser2
spl_supp1
	clr.w	prd_cdct(a3)		 ; no timeout now
	bra.s	spl_ser2
spl_cact1
	move.w	d1,prd_cdct(a3) 	 ; carrier - reset timer

spl_ser2
	lea	ser_inc(a3),a4		 ; ser2
	move.w	prd_cdef(a4),d1 	 ; ticks to end of file?
	beq.s	spl_ser3		 ; ... none
	move.l	prd_ibuf(a4),d0 	 ; buffer?
	beq.s	spl_ser3		 ; ... no
	bsr.s	spl_scc
spl_ser3
	lea	ser_inc*2(a3),a4	 ; ser3
	move.w	prd_cdef(a4),d1 	 ; ticks to end of file?
	beq.s	spl_rts 		 ; ... none
	move.l	prd_ibuf(a4),d0 	 ; buffer?
	beq.s	spl_rts 		 ; ... no

spl_scc
	move.l	d0,a2
	move.w	sr,d0
	move.w	#$2700,sr
	move.w	prd_hwb(a4),a1
	move.b	#scc_rstat,(a1)
	move.b	(a1),d1
	move.w	d0,sr

	btst	#sccs..dcd,d1		 ; carrier?
	bne.s	spl_cacts		 ; ... yes
	subq.w	#1,prd_cdct(a4) 	 ; ... no, countdown
	bgt.s	spl_rts 		 ; still counting
	blt.s	spl_supps		 ; ... suppress it
	tas	buf_eoff(a2)		 ; ... timed out
	bra.s	spl_rts
spl_supps
	clr.w	prd_cdct(a4)		 ; no timeout now
	bra.s	spl_rts
spl_cacts
	move.w	prd_cdef(a4),prd_cdct(a4) ; carrier - reset timer
spl_rts
	rts
	end
