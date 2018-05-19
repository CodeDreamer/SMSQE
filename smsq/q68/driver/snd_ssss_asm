; Q68 SMSQ Sampled Sound System  V1.02	  2017-2018 W. Lenerz
; 1.02 improved queue handing

; partially based on
; Q40 SMSQ Sampled Sound System  V2.00	  1999  Tony Tebby

	section sound

	xdef	ssss_init

	xref	cpy_mmod
	xref	gu_achpp
	xref	snd_nam
	include 'dev8_keys_java'
	include 'dev8_keys_q68'
	include 'dev8_keys_iod'
	include 'dev8_keys_q40'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_68000'
	include 'dev8_smsq_smsq_base_keys'
	include 'dev8_mac_proc'



; a0				<- a3 at start of poll routine
; 8 (a0)	iod_pllk
; 12(a0)	iod_plad
; 16(a0)	'SSSS'		<- A3 when called from user vectors
; 20(a0)	Ptr to vectors
; 24(a0)	RTE
; ...
; 30 (a0)	qstart


ssss_spec   equ 16
ssss_sema   equ 30 -ssss_spec		; relative to A3 in user vectors
ssss_qstart equ ssss_sema+4
ssss_qin    equ ssss_qstart+4
ssss_qout   equ ssss_qin+4
ssss_qend   equ ssss_qout+4
ssss_qbase  equ ssss_qend+4
ssss.len    equ 2*100*1024
ssss_end    equ ssss_qbase+ssss.len+ssss_spec


;+++
; SSSS initialisation.
; Setup the queues, vectors and link in the polling routine.
; This is not quite standard. The polling routine link is offset by ssss_spec,
; the special space needed for the SSSS mrker and the vector.
;
;	a0-a3  scratch
;	status return standard
;---
ssss_init
	move.l	#ssss_end,d0		; allocate a block for the queue
	move.l	d0,d1			; keep mem needed
	jsr	gu_achpp
	bne	out
	
	move.l	a0,a3
	move.l	 #'wolf',(a0)
	add.l	#ssss_spec,a3		; link header - leave room for special space

	move.l	a0,d0
	add.l	d1,d0			; point to end of space
	subq.l	#8,d0			; a little margin
	move.l	d0,ssss_qend(a3)	; this is the end of the queue
	lea	ssss_qbase(a3),a2	; base
	move.l	a2,ssss_qstart(a3)	; is also start of space for queue
	move.l	a2,ssss_qin(a3) 	; and incoming
	move.l	a2,ssss_qout(a3)	; and outgoing

	lea	ssss_vector,a2
	move.l	(a2),(a3)+		; 0 ssss flag
	move.l	a2,(a3)+		; 4 facility vector
	move.w	#$4e73,(a3)		; 8 RTE just in case this actually does get called   14
	lea	exv_i4,a1
	move.l	a3,(a1) 		; store interrupt vector

	move.l	a0,-(a7)
	lea	poll_hdr,a0
	jsr	cpy_mmod		; use polling routine as minimodule
	move.l	4(a0),a1
	move.l	(a7)+,a0

	lea	iod_plad(a0),a3 	; frame interrupt polling
	move.l	a1,(a3)

	lea	iod_pllk(a0),a0 	; link it in
	moveq	#sms.lpol,d0
	trap	#do.sms2

;;;;;  debug code ;;;;; ----------------------------
	genif	debug = 1
	cmp.l	#'gold',q68_jflg	  ; SMSQ68mulator?
	bne.s	ssss_jmp
	move.w	#0,$d000
	moveq	#jt5.sssi,d0
	dc.w	jva.trp5
	endgen
;!!!!!!!!!!!!!	------------------------------------
ssss_jmp
       jmp	snd_nam

;+++++++++++++++++++++++
; Interrupt server  : this is called on every frame interrupt. It must take
; 2 bytes out of the queue and send them to the DAC, one for left and one for
; the right channel.
; Q68 needs sound to be output to the DAC in words, where the high byte
; is the actuel sound byte, the low byte is 0.
; To reach a 20 Khz rate, I need to move (2 * )400 words on every frame inter-
; rupt.
; The first word needs to be sent to the right channel, the second to the
; left channel (inverse of Q40).
;
;	d0   s
;	d7   s
;
;-------------------------

poll_hdr
	dc.w	ssss_poll-*
	dc.w	poll_end-*

ssss_poll
	add.l	#ssss_spec,a3		; point to start of my data ('SSSS')
	tst.b	ssss_sema(a3)		; is sound being killed?
	beq.s	do_snd			; ... no
	subq.b	#1,ssss_sema(a3)	; avoid sound being killed forever
	rts

do_snd	move.l	ssss_qout(a3),a2	; next out
	move.l	#1023,d2		; go around 1024 times max
	move.l	ssss_qin(a3),a1 	; where next byte inserted goes
	move.l	ssss_qend(a3),a4

sndlp
	cmp.l	a2,a1			; any bytes left to copy?
	beq.s	ssss_done		; ... no, done
	tst.b	sound_full		; can DAC handle more sound?
	bne.s	ssss_done		; ... no, done
	cmp.l	a2,a4			; are we at end of buffer ?
	beq.s	set_bse 		; ... yes; wrap to start of buffer
	move.w	(a2)+,d0		; ... no : left/right sound 00LR
no_z	move.b	d0,sound_right
	rol.w	#8,d0
	move.b	d0,sound_left
cont	dbf	d2,sndlp		; 1024 times max
 
ssss_done

;------------------------------------------
;;;;  debug code ;;;;;
	genif	debug = 1
	cmp.l	#'gold',q68_jflg	; SMSQ68mulator?
	bne.s	setx			; no
	move.l	ssss_qout(a3),d0
	cmp.l	d0,a2			; was anything changed?
	beq.s	setx			; no, so no need to call add routine
sds	moveq	#10,d0
	dc.w	$a005
	endgen
;
;-------------------------------------------
setx	move.l	a2,ssss_qout(a3)	; new queue out ptr
out	rts

; restart at base
set_bse
	lea	ssss_qbase(a3),a2	; end of queue space reached, so...
	bra.s	sndlp			; start at top

poll_end

; utility vector

ssss_vector
	dc.l	'SSSS'
	bra.s	sss_add1
	nop
	bra.s	sss_setm
	nop
	bra.s	sss_addm
	nop
	bra.l	sss_kill
;	 nop
	bra.l	sss_sample

; add a sample

sss_add1
	move.l	ssss_qin(a3),a1
	cmp.l	ssss_qend(a3),a1		; qin at end?
	beq.s	sssa_chk			; ... yes, check add possible
ssss_add
	move.b	d1,(a1)+			; put samples in
	move.b	d2,(a1)+
sssa_updt
	move.l	a1,ssss_qin(a3) 		; update in pointer
sssa_rs rts
sssa_chk					; qin is at end
	cmp.l	ssss_qout(a3),a1		; is qout there, too?
	bne.s	sssa_rs 			; ... no, so I can't add!
	move.l	ssss_qstart(a3),a1		; .... yes, start at top
	bra.s	ssss_add


; set up to add multiple samples
; on return a1 pointo to where bytes may be inserted, up to a2
sss_setm
	move.l	ssss_qin(a3),a1 		; next in
	move.l	ssss_qout(a3),a2		; next out
	cmp.l	a1,a2				; queue empty?
	bne.s	ck_wrap 			; no, check wrap ->

; here in and out were the same : queue is empty
	move.l	ssss_qend(a3),a2		; always copy to end of buffer
	cmp.l	a2,a1				; were in/out at end aleady?
	beq.s	ssss1				; yes, need to wrap, the whole buffer is available again
ssssx	rts
; here both in and out were at end of buffer; reset in to start of buffer
ssss1	move.l	ssss_qstart(a3),a1		; the whole buffer is available again
	rts

; check whether we're wrapped or not
ck_wrap bgt.s	ssss2				; we were wrapped ->

; here we weren't wrapped, in > out
	cmp.l	ssss_qend(a3),a1		; is in at end of buffer?
	bne.s	ssss3				; no, so we can fill to there
; here in is at the end of the buffer already, can we wrap?
	cmp.l	ssss_qstart(a3),a2		; is out at start of buffer?
	beq.s	occupied			; ..yes, the entire buffer is...
						;   ... taken, nothing is free ->
	move.l	ssss_qstart(a3),a1		; ..no, we're actually wrappping now

; here we are wrapped around, the free space is from in to out-2
ssss2	subq.l	#2,a2				;
	rts
 
ssss3	move.l	ssss_qend(a3),a2		; always copy to end of buffer
	rts

occupied
	move.l	ssss_qin(a3),a1 		; next in
	move.l	a1,a2				; are the same, don't copy!!!!!
	rts


; add multiple samples (new qin ptr)

sss_addm
	cmp.l	ssss_qend(a3),a1	      ; end of queue
	blt.s	sss_aset		      ; ... no
	move.l	ssss_qend(a3),ssss_qin(a3)  ; ... back to beginning
	rts
sss_aset
	move.l	a1,ssss_qin(a3)
	rts


; kill the sound

sss_kill
	st	ssss_sema(a3)			; show the sound is being killed
	move.l	ssss_qstart(a3),ssss_qin(a3)
	move.l	ssss_qstart(a3),ssss_qout(a3)
	sf	ssss_sema(a3)
	rts

;	 movem.l d1/d2/a1/a2,-(sp)
;	 move.l  ssss_qin(a3),a2    ; the bytes will go here
;
;	 moveq	 #$ffffff80,d1
;	 moveq	 #$ffffff80,d2
;sss_kloop
;	 bsr	 sss_add1	    ; add zero
;	 beq.s	 sss_kloop	    ; keep on until queue is not full
;
;	 move.l  a2,ssss_qout(a3)   ; one sample only in queue
;	 movem.l (sp)+,d1/d2/a1/a2
;	 rts


; return length  of samples in queue

sss_sample
	move.l	ssss_qin(a3),d0
	sub.l	ssss_qout(a3),d0
	bpl.s	sss_sset
	add.l	ssss_qend(a3),d0
	sub.l	ssss_qstart(a3),d0
sss_sset
	lsr.l	#1,d0
	rts

	end
