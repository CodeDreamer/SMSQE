; SBAS_RETSTK - Translate return stack addresses/statements 2017 Marcel Kilgus
;
; 2018-01-01  1.01  Fixed "end of program/command line" problems (MK)

	section sbas

	xdef	sb_ret2pos
	xdef	sb_ret2adr

	xref	sb_addsttx
	xref	sb_sttaddx

	include 'dev8_keys_sbasic'
	include 'dev8_mac_assert'

;+++
; Change address entries on return stack into line/statement numbers
;---
sb_ret2pos
	move.l	sb_retsp(a6),d1 	 ; return stack
	cmp.l	sb_retsb(a6),d1 	 ; any?
	beq.s	srt_clrmde		 ; ... no, we set mode to default
	tst.b	sb_rtmde(a6)		 ; return stack mode?
	bne.s	srt_rts 		 ; already in line numbers
	move.b	#1,sb_rtmde(a6) 	 ; return stack mode now line numbers

srt.reg reg	d0/d2/d3/d4/d5/d7/a0/a1/a2/a3/a4/a5
	movem.l srt.reg,-(sp)
	lea	sb_cv2pos,a5		 ; ...to position
	moveq	#-2,d6			 ; to add before translating rt_def
	moveq	#0,d7			 ; to add after translating rt_def
	bra.s	srt_do

srt_clrmde
	clr.b	sb_rtmde(a6)		 ; return stack mode in abs adresses
srt_rts
	rts

; addsttx/sttaddx cannot handle the position after the last token correctly,
; so we handle this case explicitely here
sb_cv2pos
	moveq	#-2,d1			 ; offset to STOP token
	add.l	sb_progp(a6),d1 	 ; ptr to implicit STOP in program
	cmp.l	d0,d1			 ; are we there?
	bne	sb_addsttx		 ; no, translate normally
	moveq	#0,d0			 ; yes, just mark with 0
	rts

sb_cv2adr
	tst.l	d0			 ; marked as "end of program"?
	bne	sb_sttaddx		 ; no, translate normally
	moveq	#-2,d0			 ; offset to STOP token
	add.l	sb_progp(a6),d0 	 ; ptr to implicit STOP at end
	rts

;+++
; Change line/statements entries on return stack into absolute addresses
;---
sb_ret2adr
	move.l	sb_retsp(a6),d1 	 ; return stack
	cmp.l	sb_retsb(a6),d1 	 ; any?
	beq.s	srt_clrmde		 ; ... no, we set mode to default
	tst.b	sb_rtmde(a6)		 ; return stack mode?
	beq.s	srt_rts 		 ; already abs adresses, do nothing
	clr.b	sb_rtmde(a6)		 ; return stack mode now abs adresses

	movem.l srt.reg,-(sp)
	lea	sb_cv2adr,a5		 ; ...to address
	moveq	#0,d6			 ; to add before translating rt_def
	moveq	#2,d7			 ; to add after translating rt_def
srt_do
	move.l	sb_retsp(a6),a1 	 ; return stack

srt_loop
	move.b	rt_type(a6,a1.l),d0	 ; return stack type
	bgt.s	srt_proc		 ; procedure / function frame
	blt.s	srt_sarr		 ; always skip array frame

	move.l	rt_ret(a6,a1.l),d0	 ; return address/statment
	jsr	(a5)			 ; translate
	move.l	d0,rt_ret(a6,a1.l)	 ; ...and save back
srt_sarr
	assert	rt.sasize,rt.gssize	 ; skip array and gosub
	subq.l	#rt.sasize,a1
srt_next
	cmp.l	sb_retsb(a6),a1 	 ; another frame?
	bgt.s	srt_loop		 ; ... yes
srt_exit
	movem.l (sp)+,srt.reg
	rts

srt_proc
	move.l	rt_def(a6,a1.l),d0	 ; parameters swapped?
	bpl.s	srt_parm		 ; ... no, don't translate
	neg.l	d0
	add.l	d6,d0			 ; rt_def is offset to bo_formp
	jsr	(a5)			 ; translate
	add.l	d7,d0			 ; rt_def is offset to bo_formp
	neg.l	d0
	move.l	d0,rt_def(a6,a1.l)	 ; ...and save back
srt_parm
	move.l	rt_ret(a6,a1.l),d0	 ; return address/statement
	bmi.s	srt_skip		 ; skip if "return to command line"
	jsr	(a5)			 ; translate
	move.l	d0,rt_ret(a6,a1.l)	 ; ...and save back
srt_skip
	sub.w	#rt.pfsize,a1
	bra.l	srt_next

	end
