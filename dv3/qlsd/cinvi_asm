	section dv3

	include 'dev8_mac_creg'

	xdef	cinvi

cinvi
; Check processor type (stolen from C68 runtime)
	movem.l a7,-(a7)		; See if the pre-decrement is pipelined?
	cmp.l	(a7)+,a7
	beq.s	no_caches		; 68000/68008/68010

; Must be 68020 then
	gcreg	cacr			; get CACR into d0
	or.w	#$0008,d0		; invalidate inst cache
	pcreg	cacr			; and set
no_caches
	rts

	end
