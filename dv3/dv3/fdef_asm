; DV3 Find Definition Block	   V3.00	   1992 Tony Tebby

	section dv3

	xdef	dv3_fdef

	include 'dev8_keys_err'
	include 'dev8_dv3_keys'
	include 'dev8_keys_sys'

;+++
; This is the routine that is used by interrupt servers to find the definition
; block for a drive.
;
;	d7 c  u drive number (byte) / drive ID/number
;	a3 c  p pointer to driver linkage (master or slave)
;	a4  r	pointer to definition block
;	a6 c  p pointer to system variables
;
;	Status return standard (0 or err.fdnf)
;---
dv3_fdef
dfd.reg reg	a2
	move.l	a2,-(sp)

	lea	sys_fsdd(a6),a2
	moveq	#0,d0			 ; id
dfd_fdrv
	move.l	(a2)+,a4
	cmp.l	ddf_ptddl(a4),a3	 ; our driver?
	bne.s	dfd_next		 ; ... no
	cmp.b	ddf_dnum(a4),d7 	 ; our drive?
	beq.s	dfd_done		 ; ... yes
dfd_next
	addq.b	#1,d0
	cmp.b	#16,d0
	blt.s	dfd_fdrv		 ; next

	moveq	#err.fdnf,d0		 ; no dd linkage
	bra.s	dfd_exit

dfd_done
	ext.w	d7
	swap	d7
	move.w	d0,d7			 ; number / ID
	swap	d7
	moveq	#0,d0
dfd_exit
	move.l	(sp)+,a2
	rts
	end
