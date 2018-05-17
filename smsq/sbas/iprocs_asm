; SBAS_IPROCS - PROC/FN (and Arrays) Setup    1994 Tony Tebby

	section sbas

	xdef	bo_cpcall	    ; procedure, function and array setup
	xdef	bo_spcall
	xdef	bo_spcallc
	xdef	bo_pcall
	xdef	bo_cfnref
	xdef	bo_sfnref
	xdef	bo_sfnrefc
	xdef	bo_cfnaref
	xdef	bo_sfnaref
	xdef	bo_sfnarefc

	xdef	sb_rrets
	xdef	sb_setpfr

	xref	sb_ierset

	xref	sb_sttadd

	xref	sb_rrt24
	xref	sb_aldat

	include 'dev8_keys_sbasic'
	include 'dev8_keys_err4'
	include 'dev8_mac_assert'

;+++
; Within this routine
;
;	D6 is limit of arithmetic stack (with some bytes spare)
;	A1 is pointer to arithmetic stack
;	A2 is entry address
;	A3 is pointer to name table
;	A4 is pointer to program
;	A5 address of next token loop
;	A6 is pointer to system variables
;---
;-------------------------------- procedure, function and array setup
bo_pcall
	move.l	a4,d5			 ; assume mc proc with setup
	move.w	(a4)+,d3		 ; procedure name
	move.l	nt_value(a3,d3.w),d4	 ; value

	moveq	#-nt.mcprc,d0
	add.b	nt_nvalp(a3,d3.w),d0	 ; mc proc?
	beq.s	bcp_dop 		 ; ... yes
	addq.b	#nt.mcprc-nt.sbprc,d0	 ; sb proc?
	bne.s	bcp_bprc		 ; ... no

	moveq	#0,d5			 ; no setup address
	tst.b	sb_cmdl(a6)		 ; command line?
	bne.s	bcp_dop 		 ; ... yes
	move.l	d4,d0			 ; line and statement number
	jsr	sb_sttadd		 ; to address
	move.l	d0,d4
	bra.s	bcp_dop

;---------------------------------
bo_spcall
bo_spcallc
	move.l	(a4)+,d4		 ; procedure address
	moveq	#0,d5
	bra.s	bcp_dop
bo_cpcall
	move.l	a4,d5			 ; setup
	move.w	(a4)+,d3		 ; procedure name
	move.l	nt_value(a3,d3.w),d4	 ; value
bcp_dop
	move.l	#rt.proc<<24,d3

;+++
; set proc fun stack frame
;---
bcp_do
	move.l	a5,-(sp)
;+++
; set proc fun return stack frame
;
;	d3 c  p return frame type (in msb)
;	d4 c  p pointer to procfn
;	d5 c  p setup (mc) 0 (sbasic)
;
;---
sb_setpfr
	moveq	#rt.pfsize,d0
	move.l	sb_retsp(a6),a2 	 ; return stack
	add.l	a2,d0
	cmp.l	sb_retst(a6),d0 	 ; full?
	blt.s	bcp_srets		 ; no
	bsr.l	sb_rrets		 ; ... yes
bcp_srets
	add.l	a6,a2
	move.l	sb_nmtbp(a6),d0
	sub.l	sb_nmtbb(a6),d0
	move.l	d0,(a2)+		 ; pointer to base of frame
	move.l	d0,(a2)+		 ; pointer to top of frame
	move.l	d5,(a2)+		 ; setup / topn
	move.l	d4,(a2)+		 ; pointer to proc/fn
	move.l	d3,(a2)+		 ; set type / arith stack
	addq.l	#4,a2
	sub.l	a6,a2
	move.l	a2,sb_retsp(a6) 	 ; save stack pointer
	rts

bcp_bprc
	moveq	#ern4.bprc,d0		 ; unknown procedure
bo_ierset
	jmp	sb_ierset

bcp_bref
	moveq	#ern4.bref,d0
	bra.s	bo_ierset

bo_sfnref
bo_sfnrefc
	moveq	#0,d5
	bra.s	bcf_do
bo_cfnref
	move.l	a4,d5
bcf_do
	move.l	(a4)+,d4		 ; procedure address
	move.l	#rt.fun<<24,d3		 ; must be function
	bra.s	bcp_do			 ; ... yes

bo_sfnaref
bo_sfnarefc
	moveq	#0,d5
	move.l	(a4)+,d4		 ; procedure address
	move.w	(a4)+,d1
	move.l	#rt.fun<<24,d3		 ; must be function
	move.b	nt_nvalp(a3,d1.w),d0	 ; usage
	cmp.b	#nt.sbfun,d0		 ; sb fun?
	beq.s	bcp_do			 ; ... yes
	bra.s	bar_do

bo_cfnaref
	move.l	a4,d5
	move.l	(a4)+,d4		 ; procedure address
	move.w	(a4)+,d1
	move.l	#rt.fun<<24,d3		 ; assume function
	move.b	nt_nvalp(a3,d1.w),d0	 ; usage
	cmp.b	#nt.mcfun,d0		 ; mc fun?
	beq	bcp_do			 ; ... yes

bar_do
	cmp.b	#nt.arr,d0		 ; is it array?
	bne.s	bcp_bref
	moveq	#rt.sasize,d0
	move.l	sb_retsp(a6),a2 	 ; return stack
	add.l	a2,d0
	cmp.l	sb_retst(a6),d0 	 ; full?
	blt.s	bar_srets		 ; no
	bsr.s	sb_rrets		 ; ... yes
bar_srets
	add.l	a6,a2
	move.l	#rt.array<<24,(a2)+	 ; set type
	clr.l	(a2)+			 ; and nothing
	sub.l	a6,a2
	move.l	a2,sb_retsp(a6) 	 ; save stack pointer
	jmp	(a5)


sb_rrets
	movem.l d1/d2/d3/a1,-(sp)
	jsr	sb_rrt24		 ; allocate another bit
	movem.l (sp)+,d1/d2/d3/a1
	move.l	sb_retsp(a6),a2 	 ; return stack
	rts

	end
