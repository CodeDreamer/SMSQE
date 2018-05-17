; Initialise an SBASIC stub	V2.02	 1992	Tony Tebby  QJUMP

	section sbas

	xdef	init_sbst

	xref	mem_xtpa
	xref	gu_achp0

	include 'dev8_keys_sys'
	include 'dev8_keys_sbasic'

;+++
; Initialise SuperBASIC stub
;
;	d0/d1	scratch
;	a0/a1	scratch
;	a6 c  p pointer to system variables area
;---
init_sbst
	move.l	#sb.almin,d1		 ; allocate a chunk for SuperBASIC
	jsr	mem_xtpa
	add.l	d1,sys_tpab(a6) 	 ; not in TPA
	lea	sb_offs(a0),a1		 ; base of basic

sbi_cloop
	clr.l	(a0)+
	subq.w	#4,d1
	bgt.s	sbi_cloop

	move.l	#$80,d0 		; allocate a bit of heap
	jsr	gu_achp0

	sub.l	a1,a0			 ; relative address
	moveq	#sb_retsp/4+1,d1

sbi_sloop
	move.l	a0,(a1)+
	dbra	d1,sbi_sloop

	tst.l	d0
	rts

	end
