; DV3 flush buffer routine	     V3.00	     1992 Tony Tebby

	section dv3

	xdef	dv3_flush

	include 'dev8_dv3_keys'
;+++
; DV3 flush buffer routine
;---
dv3_flush
dfl.reg reg	d4-d7/a5
	movem.l dfl.reg,-(sp)

	move.l	a2,a4			 ; drive definition block
	move.l	ddf_ptddl(a4),a3	 ; real linkage
	moveq	#0,d6
	move.w	sbt_file(a1),d6
	moveq	#0,d7
	move.b	ddf_dnum(a4),d7
	moveq	#-1,d0			 ; urgent
	jsr	ddl_fflush(a3)		 ; flush all file sectors
	movem.l (sp)+,dfl.reg
	rts
	end
