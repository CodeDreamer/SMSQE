; DV3 close routines		   V3.01	   1992 Tony Tebby
;
; 2020-05-14  3.01  Correctly calculate heap block length for Minerva (MK)

	section dv3

	xdef	dv3_close
	xdef	dv3_clnb

	xref	dv3_flsh
	xref	dv3_flhdr
	xref	dv3_slen

	include 'dev8_dv3_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_qlv'
	include 'dev8_mac_assert'

;+++
; DV3 close routine
;     If direct sector: sets normal.
;     Flushes if updated.
;     Decrements number of files open.
;     Unlinks channel from list.
;
;	d6    p file id
;	d7    p drive number
;	a0 c	channel base address
;	a2   s	internal buffer address
;	a3 c  p linkage block address
;	a4    p pointer to physical definition
;
;---
dv3_close
dv3_clnb
dcl.reg reg	d4-d7/a0/a1/a3/a4/a5
dcl.a0	equ	16
	movem.l dcl.reg,-(sp)		 ; save lots of registers

	move.l	d3c_qdend(a0),d0	 ; length of fake block
	add.l	d0,(a0) 		 ; add back to reset length

	assert	d3c_flid,d3c_drid-4,d3c_drnr-6,d3c_ddef-8
	movem.l d3c_flid(a0),d6/d7/a4	 ; file ID / drive number / def block

	move.l	ddf_ptddl(a4),a3	 ; set linkage

	move.l	a0,a5
	lea	sys_fsch(a6),a1
	lea	d3c_qdlink(a5),a0
	move.w	mem.rlst,a2
	jsr	(a2)			 ; unlink old block
	move.l	a5,a0

	tst.b	d3c_atype(a0)		 ; access type?
	ble.s	dcl_norm

	not.b	ddf_ftype(a4)		 ; direct sector access cancelled
	move.l	ddf_fsave(a4),ddf_flong(a4) ; restore format
	jsr	dv3_slen

	and.b	#$3f,ddf_mstat(a4)	 ; status positive: check medium
	tst.b	d3c_updt(a0)		 ; updated?
	bge.s	dcl_done		 ; ... no
	clr.b	ddf_mstat(a4)		 ; ... yes, force restart
	bra.s	dcl_done		 ; one fewer files and unlink

dcl_norm
	tst.b	d3c_updt(a0)		 ; file updated?
	bge.s	dcl_flhdr		 ; ... no

	moveq	#0,d3			 ; first entry - set date, version etc.
	jsr	dv3_flsh		 ; flush file buffers

	bra.s	dcl_done

dcl_flhdr
	tst.b	d3c_usef(a0)		 ; any parts of the header set?
	beq.s	dcl_done		 ; ... no
	jsr	dv3_flhdr		 ; ... yes

dcl_done
	subq.b	#1,ddf_nropen(a4)	 ; one fewer files
	bne.s	dcl_unlk
	jsr	ddl_done(a3)		 ; all done

dcl_unlk
	lea	ddf_chnlst-d3c_link(a4),a2
	move.l	a2,d0

dcl_look
	move.l	d0,a2
	move.l	d3c_link(a2),d0 	 ; next link
	beq.s	dcl_rchn		 ; ... none
	cmp.l	d0,a0			 ; this channel?
	bne.s	dcl_look		 ; ... no

	move.l	d3c_link(a0),d3c_link(a2) ; move link

dcl_rchn
	move.l	dcl.a0(sp),a0
	move.w	mem.rchp,a2		 ; and remove from heap
	jsr	(a2)
	movem.l (sp)+,dcl.reg
	rts

	end
