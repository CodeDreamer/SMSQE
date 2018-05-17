;	Console I/O Entry	  1998 Tony Tebby
; 2004-03-27		1.01	compatible with new cursor toggle
;
;	Entry point for all Bit Image I/O calls: translates the
;	supplied parameters to those used by the medium level routines
;	and calls them.
;
;	Registers:
;		Entry				Exit
;	D0	I/O key 			error code
;	D1	parameter			return
;	D2	parameter			smashed
;	D3	0 on first entry, else -1	smashed
;	D4-D7					smashed
;	A0	base of cdb			preserved
;	A1	parameter			return
;	A2
;	A3	base of dddb			preserved
;	A4/A5					preserved
;	A6	base of sysvars 		preserved
;	A7	system stack pointer		preserved
;
	section con

	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_con'

	xref	cn_curtg
	xref	cn_donl
	xref.s	cn.iotab

	xref	cn_iotab

	xdef	cn_io

cn..crem equ	0
cn.cremv equ	1

cn_io
	cmp.w	#cn.iotab,d0		; out of range?
	bhi.s	cn_unimp
	move.w	d0,d7
	add.w	d7,d7			; get offset in table
	move.w	cn_iotab(pc,d7.w),d7	; offset of routine
	bclr	#cn..crem,d7		; does it require the cursor removed?
	beq.s	cn_doio 		; no, just do I/O

	tst.b	sys_dfrz(a6)		; display frozen?
	bne.s	cni_nc			; .... yyyyyyes
	move.b	sd_curf(a0),-(a7)	; keep old status
	tst.b	sd_curf(a0)		; cursor visible?
	ble.s	cni_dfio		; ... no, ok
	move.b	#-1,sd_curf(a0)
	jsr	cn_curtg(pc)		; toggle cursor - make invisible
cni_dfio
	bsr.s	cn_doio 		; do the I/O
	move.b	(a7)+,sd_curf(a0)	; old cursor status
	tst.b	sd_curf(a0)		; needs to be visible?
	ble.s	cni_rtd0		; nope, just return, then

;
; The next bit if code is intended to reproduce a bug in the QL console driver
; which does not do a pending newline on exit from SBYT or SMUL if the cursor
; is enabled (indeed, it goes completely wrong if the newline is implicit)
;
	btst	#0,sd_nlsta(a0) 	; pending newline required?
	beq.s	cni_crest		; ... no
	jsr	cn_donl 		; ... yes, do it
;	 move.b  #1,sd_curf(a0) 	 ; show cursor needs to be visible
cni_crest
	jmp	cn_curtg		; yes, flip it back
cni_rtd0
	tst.l	d0
	rts

cni_nc
	moveq	#err.nc,d0
	rts

cn_doio
	jmp	cn_iotab(pc,d7.w)

cn_unimp
	moveq	#err.ipar,d0
	rts
	end
