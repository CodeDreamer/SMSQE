; SGC + Masterpiece graphics card patches. Deciphered by Marcel Kilgus

	section sgc

	xdef	glm_cls
	xdef	glm_cls2
	xdef	glm_ptr_gen
	xdef	glm_cls3
	xdef	glm_cls4

	include 'dev8_sys_gold_keys'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_sys'

; Clear screen
glm_cls
	move.l	#$4e0000,a6
	move.w	#$7fff,d0
glm_cls_loop
	clr.l	(a6)+
	dbf	d0,glm_cls_loop
	rts

; Clear screen
glm_cls2
	move.l	#$4e0000,a1
	move.w	#$7fff,d0
glm_cls2_loop
	clr.l	(a1)+
	dbf	d0,glm_cls2_loop
	rts

; Look for PTR_GEN init code snipped called through iow.xtop
glm_ptr_gen
	dc.w	glm_pe_patch-*

	move.l	d0,-(sp)
	cmpi.b	#iow.xtop,d0
	bne.s	glm_pe_end
	movem.l a2/a3,-(sp)
	lea	pt_exset(pc),a3
	move.l	a3,d0
	lea	pt_extop(pc),a3
	sub.l	a3,d0			; size of code snippet
	lsr.l	#1,d0			; in words
	subq.l	#2,d0			; minus the LEA address
glm_pe_loop
	cmpm.w	(a2)+,(a3)+
	dbne	d0,glm_pe_loop
	bne.s	glm_pe_end2

	addq.l	#4,a2			; skip to after "move.l a3,d1"
	lea	pt_wsize(pc),a3
	move.l	a3,d1			; preload our data block into d1
	move.l	a2,(sp) 		; new XTOP start address
glm_pe_end2
	movem.l (sp)+,a2/a3
glm_pe_end
	moveq	#$9,d0			; clear/enable code cache
	dc.l	$4e7b0002		; movec  d0,cacr
	move.l	(sp)+,d0
glm_pe_patch
	jmp	$12345678

pt_extop
	lea	$5c(a3),a3		; bm_scren
	moveq	#$fffffff7,d0
	and.b	sys_qlmr(a6),d0
	bne.s	pt_exset
	lea	pt_extop(pc),a3
pt_exset
	move.l	a3,d1

pt_wsize
	dc.l	$004e0000,$00020000
	dc.w	$0100,$0400,$0200	; standard sizes

; Clear screen
glm_cls3
	move.l	#$004e0000,a3		; Masterpiece screen base
	move.l	#$00008000,d0		; 256KB VRAM
glm_cls3_loop
	clr.l	(a3)+
	subq.l	#1,d0
	bne.s	glm_cls3_loop
	moveq	#$0,d0
	moveq	#-1,d1
	moveq	#-1,d7
	rts

glm_cls4_jmp
	jmp	$12345678

; Clear screen
glm_cls4
	dc.w	glm_cls4_patch-*

	move.l	#$004e0000,a0		; Masterpiece screen base
	move.l	#$00008000,d0		; 256KB VRAM
glm_cls4_loop
	clr.l	(a0)+
	subq.l	#1,d0
	bne.s	glm_cls4_loop
	cmpa.l	$5c,a5
	beq.s	glm_cls4_jmp
glm_cls4_patch
	jmp	$12345678

	end
