; (Super)GoldCard patch data. Deciphered by Marcel Kilgus
;
; Code in BOLD is original code sample that is to be patched.
; Additional lines have been added as comments to give some contexts

	section ver

	xdef	ver_list
	xdef	ver_indep
	xdef	ver_addr

	include 'dev8_sys_gold_keys'
	include 'dev8_keys_68000'
	include 'dev8_keys_iod'
	include 'dev8_keys_sys'
	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sbasic'
	include 'dev8_keys_qlv'

jsrp	macro	romad,old,patch
	dc.w	glp.ptch,[romad],[old],[patch]
	endm

jmpp	macro	romad,offset,patch
	dc.w	glp.vect,[romad],[offset],[patch]
	endm

word	macro	romad,old,patch
	dc.w	glp.word,[romad],[old],[patch]
	endm

trapint macro	calc,romad,patch
	dc.w	glp.trap,[calc],[romad],[patch]
	endm

vector	macro	calc,offset,patch
	dc.w	glp.vect,[calc],[offset],[patch]
	endm

time	macro	patch
	dc.w	glp.time,0,0,[patch]
	endm

addr	macro	calc,offset,patch
	dc.w	glp.addr,[calc],[offset],[patch]
	endm

indir	macro	calc,offset,patch
	dc.w	glp.indr,[calc],[offset],[patch]
	endm

ver_list
	dc.w	ver_ah-*,'02'	 AH
	dc.w	ver_jm-*,'03'	 JM
	dc.w	ver_js-*,'10'	 JS
	dc.w	ver_jsu-*,'10'	 JSU - same as JS but patches have moved
	dc.w	ver_jsp-*,'10'	 JS  - patched version
	dc.w	ver_mg-*,'13'	 MG
	dc.w	ver_fp-*,'13'	 FP - same as MG!! but patches are different
	dc.w	0,0		 NO version dependent patches

ver_js
	word	$4ac4,$000c,$0001	; ROM check
	word	$4ad0,$0010,$0001

	word	$0254,$d6c0,$d7c0	; MM .w to .l
	word	$3230,$d2c0,$d3c0

	word	$4356,$9002,$9802	; ATAN, ASIN, ACOS, ARC .w to .l

	jsrp	$662e,$6c0e,glk.chan	; patch channel check
ver_none
	dc.w	glp.end

ver_jsp
	word	$4ac4,$0003,$0001	; ROM check
	word	$4ac6,$8000,$0000	; check all from

	word	$0254,$d6c0,$d7c0	; MM .w to .l
	word	$3230,$d2c0,$d3c0

	word	$4356,$9002,$9802	; ATAN, ASIN, ACOS, ARC .w to .l

	jsrp	$662e,$6c0e,glk.chan	; patch channel check
	dc.w	glp.end

ver_jsu
	word	$4b0e,$000c,$0001	; ROM check
	word	$4b1a,$0010,$0001

	word	$0256,$d6c0,$d7c0	; MM .w to .l
	word	$327a,$d2c0,$d3c0

	word	$43a0,$9002,$9802	; ATAN, ASIN, ACOS, ARC .w to .l

	jsrp	$668c,$6c0e,glk.chan	; patch channel check

	dc.w	glp.end

ver_ah
	word	$4b04,$000c,$0001	; ROM check
;***	    word    $4b10,$0010,$0001

	word	$0252,$d6c0,$d7c0	; MM .w to .l
	word	$312c,$d2c0,$d3c0

	word	$43ae,$9002,$9802	; ATAN, ASIN, ACOS, ARC .w to .l

	jsrp	$6040,$6c0e,glk.chan	; patch channel check

	dc.w	glp.end

ver_jm
	word	$4b44,$000c,$0001	; ROM check
;***	    word    $4b50,$0010,$0001

	word	$0252,$d6c0,$d7c0	; MM .w to .l
	word	$3142,$d2c0,$d3c0

	word	$43ee,$9002,$9802	; ATAN, ASIN, ACOS, ARC .w to .l

	jsrp	$6088,$6c0e,glk.chan	; patch channel check

	dc.w	glp.end

ver_mg
	word	$4ab2,$000c,$0001	; ROM check
	word	$4abe,$0010,$0001

	word	$0254,$d6c0,$d7c0	; MM .w to .l
	word	$32ac,$d2c0,$d3c0

	word	$4344,$9002,$9802	; ATAN, ASIN, ACOS, ARC .w to .l

	jsrp	$2324,$4267,glk.graf	; patch point and short line

	dc.w	glp.end

ver_fp
	word	$4abe,$000c,$0001	; ROM check
	word	$4aca,$0010,$0001

	word	$0254,$d6c0,$d7c0	; MM .w to .l
	word	$32b8,$d2c0,$d3c0

	word	$4350,$9002,$9802	; ATAN, ASIN, ACOS, ARC .w to .l

	dc.w	glp.end

; version independent patches
ver_indep
	word	min_tabl, 0, 0			; patch minerva table setup
	time	glk.mdvt			; mdv timings
	vector	mdv_frmt, 'MD', glk.mdf 	; format
	vector	vect4000, md.read,  glk.mdrd	; ... and vectors
	vector	vect4000, md.write, glk.mdwr
	vector	vect4000, md.verif, glk.mdvr
	vector	vect4000, md.rdhdr, glk.mdsh
	vector	mdv,mdvq_sel-mdv_patch,glk.mdsl ; try QL second patch first
	vector	mdv,mdvq_des-mdv_patch,glk.mdds ; try QL first
	vector	mdv,mdvm_sel-mdv_patch,glk.mdsl ; try Minerva 1.89 patches
	vector	mdv,mdvm_des-mdv_patch,glk.mdds ;
	vector	mdv,mdvo_sel-mdv_patch,glk.mdsl ; try Minerva 1.98 patches
	vector	mdv,mdvo_des-mdv_patch,glk.mdds ;

	vector	gc_i2c, $172, glk.i2ce		; Minerva I2C (1.89+) for GC
	vector	gc_i2c, $170, glk.i2c
	vector	sgc_i2c, $172, sgk.i2ce 	; Minerva I2C (1.89+) for SGC
	vector	sgc_i2c, $170, sgk.i2c

	jsrp	ser_look, 0, glk.sdly		; serial transmit delay

	word	min_reset,  0, $4e71		; patch minerva RESET to NOP
	vector	min_reset3, 0, glk.min.reset3

	trapint gc_only,  exv_trp1, glk.trp1	; GC trap 1 intercept
	trapint sgc_only, exv_trp1, sgk.trp1	; SGC trap 1 intercept
	indir	sgc_only, exv_trp2, sgk.trp2
	indir	sgc_only, exv_aerr, sgk.aerr
	indir	sgc_only, exv_div0, sgk.div0
	indir	sgc_only, exv_chk,  sgk.chk
	indir	sgc_only, exv_trpv, sgk.trpv
	trapint sgc_only, exv_prvv, sgk.privv
	indir	sgc_only, exv_trac, sgk.trac

	vector	sgc_8049_rte,  0, sgk.8049.rte
	jsrp	sgc_ser_sched, 0, sgk.ser.sched

	addr	sgc_qdos,  exv_trp0, sgk.qdos.trp0
	indir	sgc_min,   exv_trp0, sgk.min.trp0
	indir	sgc_no_mp, exv_trp3, sgk.trp3
	indir	sgc_only,  exv_trp4, sgk.trp4
	indir	sgc_only,  exv_i2,   sgk.i2

	jsrp	sgc_boot_sched, 0, sgk.boot.sched

	vector	sgc_qdos_sched, 0, sgk.qdos.sched
	vector	sgc_min_sched,	0, sgk.min.sched
	vector	sgc_int2_rte,	0, sgk.int2.rte

	indir	sgc_only, exv_ilin, sgk.ilin
	indir	sgc_only, exv_trp5, sgk.trp5
	indir	sgc_only, exv_trp6, sgk.trp6
	indir	sgc_only, exv_trp7, sgk.trp7
	indir	sgc_only, exv_trp8, sgk.trp8
	indir	sgc_only, exv_trp9, sgk.trp9
	indir	sgc_only, exv_trpa, sgk.trpa
	indir	sgc_only, exv_trpb, sgk.trpb
	indir	sgc_only, exv_trpc, sgk.trpc
	indir	sgc_only, exv_trpd, sgk.trpd
	indir	sgc_only, exv_trpe, sgk.trpe
	indir	sgc_only, exv_trpf, sgk.trpf

	indir	sgc_min, exv_alin, sgk.alin
	indir	sgc_min, exv_flin, sgk.flin

	vector	sgc_patch_patch, 0, sgk.nsbyt	; patch NET timing for SGC
	vector	sgc_patch_patch, 0, sgk.nrbyt	; patch NET timing for SGC
	jsrp	sgc_patch_patch, 0, sgk.nscot	; patch NET timing for SGC

	jsrp	auto_f1f2, 0, glk.f1f2		; autopress F1/F2

	word	sgc_destd, 0, $0048		; $10048 = ptr to DESTD$
	word	sgc_sdump, 0, $004a		; $1004a = ptr to SDUMP default

	jsrp	qdos_bvchnt, 0, glk.chnt	; fix some name table problem

	word	min_mdv1, 0, $D9C1		; fix .W to .L (fixed in later ROMs)
	word	min_mdv2, 0, $5441		; fix .B to .L (fixed in later ROMs)

	word	master1,0,$004E 		; Masterpiece base $004E0000
	word	master2,0,$0100 		; Masterpiece line pitch
	word	master3,0,$0400 		; Masterpiece X size
	word	master4,0,$0200 		; Masterpiece Y size
	word	master5,0,$004E 		; Masterpiece base $004E0000
	word	master6,0,$004E 		; Masterpiece base $004E0000
	word	master7,0,$0100 		; Masterpiece line pitch
	word	master8,0,$FF00 		; Masterpiece -ve line pitch
	word	master9,0,$0100 		; Masterpiece line pitch
	word	master10,0,$0100		; Masterpiece line pitch
	jsrp	master_qdos_mode,0,glmk.cls	; Masterpiece CLS on MODE (QDOS)
	jsrp	master_min_mode,0,glmk.cls2	; Masterpiece CLS on MODE (Minerva)
	indir	master,exv_trp3,glmk.ptr.gen	; Masterpiece patch PTR_GEN
	jsrp	master_qdos_boot,exv_ipc,glmk.cls3 ; Masterpiece CLS on boot
	vector	master_min_boot,exv_ipc,glmk.cls4  ; Masterpiece CLS on boot

	dc.w	glp.end

vervec	macro	name
[name]	equ	*-ver_addr+1
	dc.w	ver_[name]-*
	endm

; Address calculation helper function table
ver_addr
	vervec	mdv_frmt
	vervec	vect4000
	vervec	gc_i2c
	vervec	mdv
	vervec	ser_look
	vervec	min_reset
	vervec	min_tabl
	vervec	sgc_8049_rte
	vervec	sgc_ser_sched
	vervec	sgc_qdos
	vervec	sgc_only
	vervec	sgc_boot_sched
	vervec	gc_only
	vervec	sgc_min_sched
	vervec	sgc_qdos_sched
	vervec	sgc_int2_rte
	vervec	sgc_min
	vervec	x22
	vervec	sgc_i2c
	vervec	min_reset3
	vervec	sgc_patch_patch
	vervec	auto_f1f2
	vervec	sgc_sdump
	vervec	sgc_destd
	vervec	qdos_bvchnt
	vervec	min_mdv1
	vervec	min_mdv2
x36	dc.w	0
x38	dc.w	0
x3a	dc.w	0
	vervec	master1
	vervec	master2
	vervec	master3
	vervec	master4
	vervec	master5
	vervec	master6
	vervec	master7
	vervec	master8
	vervec	master9
	vervec	master10
	vervec	master_qdos_mode
	vervec	master_min_mode
	vervec	master
	vervec	sgc_no_mp
	vervec	master_qdos_boot
	vervec	master_min_boot


;+++
; Address calculation helper routines
;
;	Registers:
;		Entry				Exit
;	D2.w	Calculation parameter	 D2.l	0 = apply patch, skip otherwise
;	D5.l	ROM version		 D5.l	preserved
;	D6.w	patch command		 D6.w
;	D7.w	0 = 68020, 2 = 68000	 D7.w	preserved
;	A1				 A1	Calculated address
;	A2	pointer to patch value	 A2	preserved
;	A3	patch table base	 A3	preserved
;	A4	QL ROM mirror address	 A4	preserved
;
;	CCR according to d2
;---

; Find pointer to MDV format routine
ver_mdv_frmt
	lea	8(a4),a0
	moveq	#3,d1
	move.w	#($c000-8)/2-1,d0
mdv_lfbyt
	cmp.w	(a0)+,d2		; look for 'MD'
	dbeq	d0,mdv_lfbyt
	bne.l	ver_bad
	cmp.w	-4(a0),d1		; prefixed by $0003?
	bne.s	mdv_lfbyt		; (should be dbra)
	cmp.w	#'67',d5
	ble.s	mdv_qdos		; old Minerva or QDOS?
	sub.l	a4,a0
	lea	-10(a0),a1		; rel ptr to format routine
	add.w	-10(a4,a0.l),a1 	; make ptr absolute
	bra.s	ver_ok
mdv_qdos
	move.l	-12(a0),a1		; abs ptr to format routine
	bra.s	ver_ok

; SGC and Minerva >= 1.89
ver_sgc_i2c
	cmp.w	#'89',d5
	blo.l	ver_np
	tst.w	d7
	beq.s	ver_vect4000
	bra.s	ver_np

; GC and Minerva >= 1.89
ver_gc_i2c
	cmp.w	#'89',d5
	blo.s	ver_np
	tst.w	d7
	beq.s	ver_np

; Vectors with offset $4000
ver_vect4000
	move.w	d2,a1			; vector number
	move.w	(a4,a1.l),a1		; vector
	add.w	#$4000,a1		; + offset
ver_ok
	moveq	#0,d2			; we've used this now
	rts

; Fix Minerva table setup code
ver_min_tabl
	cmp.w	#'13',d5
	ble.s	ver_np
	movem.l min_tdck,d1-d4		; eight bytes patch / eight bytes mask
	bsr.s	ver_mlm
	movem.w min_tdpt,d0/d1		; data register setup
	beq.s	min_tabp		; patch it

	movem.l min_tack,d1-d4		; eight bytes patch / eight bytes mask
	bsr.s	ver_mlm
	movem.w min_tapt,d0/d1		; address register setup
	bne.s	ver_np

min_tabp
	add.l	a4,a1
	move.w	(a1,d0.w),d2
	eor.w	d1,d2			; change add word to add long
	sub.l	a4,a1
	move.w	d2,(a1,d0.w)
	bra.s	ver_np

; Search 0008-C000
ver_mam
	move.w	#8,a1			; search through all rom
	move.l	#$c000,a0
	bra.s	ver_mchk

; Search 0200-0600
ver_mlm
	move.w	#$200,a1		; search through lower rom
	move.w	#$600,a0

; Search for (masked) 8 byte pattern
;
; d1/d2 = value
; d3/d4 = value mask
; a1	= start address
; a2	= end address
;
; returns d2=0 if found, d2=1 otherwise
ver_mchk
	move.l	d3,d0
	and.l	(a4,a1.l),d0		; masked 4 bytes
	cmp.l	d1,d0			; match?
	bne.s	ver_mnxt		; ... no
	move.l	d4,d0
	and.l	4(a4,a1.l),d0		; masked next four bytes
	cmp.l	d2,d0			; match?
	beq.s	ver_ok
ver_mnxt
	addq.l	#2,a1
	cmp.l	a0,a1			; end of scan
	blt.s	ver_mchk
ver_np
	moveq	#1,d2			; no patch
	rts

; Minerva system initialisation code - fixed in later Minerva versions
;set_tabb
;	MOVE.L	D3,(A0)+		; set current table pointer
;	MOVE.L	D3,(A0)+		; ... and bottom
;	ADD.W	D0,D3			; add length
;	MOVE.L	D3,(A0) 		; and put in top
;	RTS
min_tdck dc.w	 $20c0,$20c0,$d040,$2080   set tables using data register
	 dc.w	 $f1f8,$f1f8,$f1f8,$f1b8   mask out registers used
min_tdpt dc.w	 4,%0000000011000000	   patch 4/5 bytes
min_tack dc.w	 $20c8,$20c8,$d0c0,$2088   set tables using address register
	 dc.w	 $f1f8,$f1f8,$f1f8,$f1b8   mask out registers used
min_tapt dc.w	 4,%0000000100000000	   patch 4/5 bytes

ver_ser_look
	movem.l ser_trns+2,d1/d2	; ser xmit load address (omitting lea)
	bsr.l	ver_lkal
	bne.l	ver_bad
	move.w	ser_trns,d2
	subq.l	#2,a1
	cmp.w	(a4,a1.l),d2		; and lea
	bne.l	ver_bad
ver_ok1
	moveq	#0,d2
	rts

ser_trns
	LEA	$18020,A1		; ser test transmit empty bit
	BTST	#1,(A1)

; Minerva reset code 3
ver_min_reset3
	cmp.w	#'13',d5
	ble.s	ver_np
	movem.l min_rst3,d1/d2		; another version reset
	bsr.s	ver_lrst
	bne.s	ver_np
	addq.l	#2,a1
ver_ok2
	moveq	#0,d2
	rts

; Minerva reset code
ver_min_reset
	cmp.w	#'13',d5
	ble.s	ver_np
	movem.l min_rst1,d1/d2		; early version reset
	bsr.s	ver_lrst
	beq.s	min_prst		; patch old reset code
	movem.l min_rst2,d1/d2
	bra.s	ver_lrst		; try new version

min_prst
	cmp.l	#$ffffffff,8(a4,a1.l)	; is next long word FFFFFFFF as well?
	bne	ver_np			; ... no, give up
	lea	min_nrst,a0
	move.l	(a0)+,(a1)+		; put new rest code on old
	move.l	(a0)+,(a1)+
	move.l	(a0)+,(a1)+		; do not save old code!
	bra	ver_np			; no patch now

ver_lrst
	move.w	#$180,a0		; search area of reset
	moveq	#$10,d3
	bra.s	ver_lkrg

min_rst1
	RESET				; old Minerva RESET code
	dc.w	$ffff,$ffff,$ffff

min_rst2
	RESET				; current Minerva RESET code
	MOVEM.L 0,a0/a1
	JMP	(a1)

min_nrst
	move.w	#$2700,sr		; new code for old
	movem.l 0,a0/a1
	jmp	(a1)

min_rst3
	TRAP #0 			; another reset
	MOVE.L	$5c,a5
	RESET

; MDV select/deselect code
ver_mdv
	movem.l mdv_patch(pc,d2.w),d1/d2 ; search pattern
ver_lkal
	move.w	#($c000-8)/2,d3
	move.w	#$8,a0			; search through rom
ver_lkrg
	sub.l	a1,a1
	add.l	a4,a0
ver_swap
	swap	d1
ver_check
	cmp.w	(a0)+,d1		; match?
	dbeq	d3,ver_check
	bne.s	ver_done
	swap	d1
	cmp.w	(a0),d1
	bne.s	ver_swap
	cmp.l	2(a0),d2
	bne.s	ver_swap

	move.l	a1,-(sp)
	move.l	(sp)+,a1
	bne.l	ver_bad 		; two patches found!!

	lea	-2(a0),a1
	bra.s	ver_swap		; try again

ver_done
	move.l	a1,d2			; any thing found?
	beq.l	ver_np			; ... no
	sub.l	a4,a1
ver_ok3
	moveq	#0,d2
	rts

mdv_patch
; ------ QDOS patches
;
;	 MOVEQ	#$02,D2 		; stop mdv-motor
;	 MOVEQ	#$07,D1
;	 BRA.S	mdv_do
mdvq_des dc.w	$7402,$7207,$6004

;	 MOVEQ	#$03,D2 		; transmit for mdv-motor control
;	 SUBQ.W #1,D1
;mdv_do  MOVE.B D2,(A3)
;	 MOVEQ	#$39,D0
mdvq_sel dc.w	$7403,$5341,$1682,$7039

; ------ Minerva 1.89 patched
;
;md_selec
;	 MOVEQ	#pc.selec,D2		; clock in select bit first
;	 SUBQ.W #1,D1			; and clock it through n times
;	 BRA.S	clk_1st
mdvm_sel dc.w	$7403,$5341,$6004

;md_desel
;	 MOVEQ	#8-1,D1 		; deselect all
;clk_loop
;	 MOVEQ	#pc.desel,D2		; clock in deselect bit
;clk_1st
;	 MOVE.B D2,(A3) 		; clock high
;	 MOVEQ	#$39,D0
;	 ROR.L	D0,D0			; us delay
mdvm_des dc.w	$7207,$7402,$1682,$7039

; ------ Minerva 1.98 patches (probably 1.90+, too)
;
; md_selec again (this patch is completely redundant)
mdvo_sel dc.w	$7403,$5341,$6004

; same as before but with #$3A as ROR.L delay
mdvo_des dc.w	$7207,$7402,$1682,$703a


; SGC, QDOS/Minerva <= 1.67: fix RTE after 8049 command code
ver_sgc_8049_rte
	tst.w	d7
	bne.l	ver_np			; SGC only
	cmp.w	#'67',d5
	bgt.l	ver_np
	movem.l sgc_8049_rte_old+2,d1/d2
	bsr	ver_lkal		; look for code snipped
	bne.l	ver_bad 		; don't apply patch if not found
	addq.l	#2,a1
ver_ok4
	moveq	#0,d2
	rts

sgc_8049_rte_old
	OR.B	SYS_QLIR(A6),D7 	;
	MOVE.B	D7,1(A0)		; move.b d7,1(a0)
	RTE				; move	 (a7)+,sr
					; rts

; Serial I/O receive code assumes it can just use RTE to restore SR
sio_sched_old
	MOVE.W	SR,-(A7)		; jsr	 sgc_p2c
	OR.W	#$0700,SR		; ...
	SF	D4			; sf	 d4
;	BSR.S	...			; ...
;	RTE				; rts

; SGC, QDOS/Minerva <= 1.67: fix serial I/O scheduler code
ver_sgc_ser_sched
	tst.w	d7
	bne.l	ver_np
	cmp.w	#'67',d5
	bgt.l	ver_np
	movem.l sio_sched_old,d1/d2
	bsr	ver_lkal
	bne.l	ver_bad
	cmp.w	#$4e73,10(a4,a1.l)	; RTE?
	bne.s	ver_bad 		; ... no
	move.w	#$4e75,10(a1)		; ... exchange with RTS
ver_ok5
	moveq	#0,d2
	rts

; SGC, QDOS
ver_sgc_qdos
	tst.w	d7
	bne.l	ver_np
	cmp.w	#'13',d5
	bgt.l	ver_np
	move.w	d2,a1
ver_ok6
	moveq	#0,d2
	rts

; SGC
ver_sgc_only
	tst.w	d7
	bne.l	ver_np
	move.w	d2,a1
	moveq	#0,d2
	rts

; SGC, Minerva > 1.67, fake 68020 INT2 stack frame for initial scheduler run
ver_sgc_boot_sched
	tst.w	d7
	bne.l	ver_np
	cmp.w	#'67',d5
	ble.l	ver_np
	movem.l sgc_boot_sched_old,d1/d2
	bsr	ver_lkal
	bne.s	ver_bad
	moveq	#0,d2
	rts

sgc_boot_sched_old
	MOVE.W	#9600,D1		 ; jsr	 gl_sgc3 -> move.w #exv_i2,-(sp)
	MOVEQ	#sms.comm,D0		 ; ...
	TRAP	#1			 ; trap  #1
;	JMP	SS_RJ0(PC)
;	...
;	RTE

; GC
ver_gc_only
	tst.w	d7
	beq.l	ver_np
	move.w	d2,a1
	moveq	#0,d2
	rts

; SGC, Minerva > 1.67: clear cache (if enabled) after scheduler run
ver_sgc_min_sched
	tst.w	d7
	bne.l	ver_np
	cmp.w	#'67',d5
	ble.l	ver_np
	movem.l sgc_min_sched_old,d1/d2
	bsr	ver_lkal
	beq.l	ver_ok
ver_bad
	moveq	#-1,d2
	rts

; SGC, QDOS/Minerva <= 1.67: clear cache (if enabled) after scheduler run
ver_sgc_qdos_sched
	tst.w	d7
	bne.l	ver_np
	cmp.w	#'67',d5
	bgt.l	ver_np
	movem.l sgc_qdos_sched_old,d1/d2
	bsr	ver_lkal
	bne.s	ver_bad
	moveq	#0,d2
	rts

sgc_qdos_sched_old
	MOVEM.L $20(A0),D0-D7/A0-A6	; $20 = jb_d0
	RTE

sgc_min_sched_old
	MOVEM.L -$3C(A0),D0-D7/A0-A6	; -$3c = jb_d0-jb_a0-7*4
	RTE

; SGC: handle return from int2
ver_sgc_int2_rte
	tst.w	d7
	bne.l	ver_np
	move.l	exv_i2(a4),a1		; scan from int2 vector
	move.l	#$c000,a0		; to end of ROM
	movem.l sgc_int2_rte_old,d1-d4
	bsr.l	ver_mchk
	addq.l	#2,a1
	bne.s	ver_bad
	moveq	#0,d2
	rts

sgc_int2_rte_old
	DC.W	0
	MOVEM.L (SP)+,D7/A5/A6		; jmp sgc_int2_rte
	RTE

	DC.W	$0000,$FFFF,$FFFF,$FFFF ; instruction mask (search 6 bytes)

; SGC, Minerva only
ver_sgc_min
	tst.w	d7
	bne.l	ver_np
	cmp.w	#'13',d5
	ble.l	ver_np
	move.w	d2,a1
	moveq	#0,d2
	rts

; UNUSED. QDOS only: syscall return
ver_x22
	cmp.w	#'13',d5
	bgt.l	ver_np
	movem.l x22_old,d1/d2
	bsr	ver_lkal
	addq.l	#2,a1
	bne.l	ver_bad
	moveq	#0,d2
	rts

x22_old
	MOVEQ	#0,D0
	BTST	#sr..s-8,$C(A7)
;	BNE.S	SYSCALL_RET
;	TST.W	SYS_PICT(A6)
;	BNE	SCHEDULER
;SYSCALL_RET
;	MOVEM.L (A7)+,D7/A5-A6
;	RTE

; SGC: patch the patch code!
ver_sgc_patch_patch
	tst.w	d7
	bne.l	ver_np
	move.w	(a2),d1 		; patch value used as offset
	lea	2(a3,d1.w),a1		; ... into patch table + 2
	add.w	(a1),a1
	moveq	#0,d2
	rts

; QDOS only: AutoF1/F2
ver_auto_f1f2
	cmp.w	#'13',d5
	bgt.l	ver_np
	movem.l auto_f1f2_old,d1/d2
	bsr	ver_lkal
	bne.l	ver_bad
	moveq	#0,d2
	rts

auto_f1f2_old
	MOVEQ	#1,D0
	MOVEQ	#-1,D3
	TRAP	#3
	MOVEQ	#0,D6

; SGC: patch SDUMP default device
ver_sgc_sdump
	tst.w	d7
	bne.s	ver_np6
	move.l	#$10000,a0
	move.w	(a2),d1 		; $004a
	lea	(a0,d1.w),a0
	add.w	(a0),a0
	move.w	#4,(a0)
	move.l	#'PARD',2(a0)
ver_np6
	moveq	#1,d2
	rts

; SGC: patch DESTD$ default device
ver_sgc_destd
	tst.w	d7
	bne.s	ver_np7
	move.l	#$10000,a0
	move.w	(a2),d1 		; $0048
	lea	(a0,d1.w),a0
	add.w	(a0),a0
	move.l	#'PAR ',2(a0)
ver_np7
	moveq	#1,d2
	rts

; QDOS: reserve space in name table
ver_qdos_bvchnt
	subq.l	#4,sp			; temporary stack space
	cmp.w	#'13',d5		; QDOS?
	bgt.s	ver_np4 		; ... no, exit

	movem.l x30_ol1,d1/d2		; search first SuperBasic snipped
	bsr	ver_lkal
	bne.s	ver_np4

	lea	-22(a1),a1		; ->move.b #nt.var,nt_valp(a6,a3.l)
	move.l	x30_ol2,d1
	cmp.l	-4(a4,a1.l),d1		; preceded by move.l sb_nmtbp(a6),a3?
	bne.s	ver_np4 		; ... no

	move.l	a1,(a7)
	movem.l bv_chnt,d1-d4		; code that reserves entry in name table
	bsr.l	ver_mam 		; search it
	bne.s	ver_np4

	move.w	(a2),d1 		; patch table index ($86)
	addq.l	#2,a1			; skip BRA.S to start of bv_chnt
	lea	(a3,d1.w),a0
	add.w	(a0),a0 		; gl_bvchnt
	addq.l	#6,a0			; skip MOVEM.L in patch code
	move.l	a1,(a0) 		; patch in JSR to bv_chnt

	move.l	(a7)+,a1
	moveq	#0,d2
	rts
ver_np4
	addq.l	#4,sp
	moveq	#1,d2
	rts

;	MOVEA.L sb_nmtbp(A6),A3 		; = x30_ol2
;	MOVE.B	#nt.var,nt_nvalp(A6,A3.L)	; jsr gl_bvchnt
;	MOVE.W	#-1,nt_name(A6,A3.L)
;	MOVE.L	#-1,nt_value(A6,A3.L)
;	BSR.S	L0962C
x30_ol1 MOVE.B	-7(A6,A3.l),$1(A6,A2.l)
	MOVE.L	A3,sb_nmtbp(A6)
;	RTS

x30_ol2 MOVE.L	sb_nmtbp(A6),A3

; Routine that reserves space in the name table
;	BRA.S	bv_down
bv_chnt
;	MOVEQ	#1+1+2+4,D1	two byte, a word and a longword
;	MOVEQ	#bv_ntp,D2	name table
;	BRA.S	bv_up
	DC.W	$6000,$7208,$741C,$6000 ; BRA.S / MOVEQ / MOVEQ / BRA.S
	DC.W	$FF00,$FFFF,$FFFF,$FF00

; Minerva: fix ADD.W to ADD.L in MDV gap interrupt server
ver_min_mdv1
	cmp.w	#'13',d5
	ble.s	ver_np8
	movem.l x32_old,d1/d2
	bsr	ver_lkal
	bne.s	ver_np8
	addq.l	#6,a1
	moveq	#0,d2
	rts
ver_np8
	moveq	#1,d2
	rts

x32_old LSL.L	#3,D1
	MOVE.L	SYS_SBTB(A6),A4
	ADD.W	D1,A4

; Minerva: fix ADD.B to ADD.W in MDV gap interrupt server
ver_min_mdv2
	cmp.w	#'13',d5
	ble.s	ver_np9
	movem.l x34_old,d1/d2
	bsr	ver_lkal
	bne.s	ver_np9
	addq.l	#6,a1
	moveq	#0,d2
	rts
ver_np9
	moveq	#1,d2
	rts

x34_old SF	D2
	MOVE.B	D2,$EF(A6)
	ADDQ.B	#2,D1

; QDOS only
; Seems to contain support code for the Miracle Masterpiece graphics card :-o
ver_master_qdos
	cmp.w	#'13',d5
	bgt.s	ver_masterq_np
	tst.l	d6			; maybe masterpiece flag
	bmi.s	ver_masterq_rts
ver_masterq_np
	addq.l	#4,sp			; skip patch altogether
	moveq	#1,d2
ver_masterq_rts
	rts

;
ver_master1
	bsr.s	ver_master_qdos
	movem.l master1_old,d1/d2
	bsr	ver_lkal
	addq.l	#2,a1
	moveq	#0,d2
	rts

; Masterpiece: OPEN channel code, setting pointer to screen memory in CDB
master1_old
	MOVE.L	#$20000,(A2)+		; move.l  #$4e0000,(a2)+

master2_old
	MOVE.W	#$80,$64(A0)		; move.w  #$100,sd_linel(a0)
	LEA	$3E(A0),A1

; Masterpiece: OPEN channel code, set line length
ver_master2
	bsr.s	ver_master_qdos
	movem.l master2_old,d1/d2
	bsr	ver_lkal
	bne.l	ver_bad
	addq.l	#2,a1
	moveq	#0,d2
	rts

; Masterpiece: trap#3, iow.defw, check X resolution
ver_master3
	bsr.s	ver_master_qdos
	movem.l master3_old,d1-d4
	bsr	ver_mam
	bne.l	ver_bad
	addq.l	#2,a1
	moveq	#0,d2
	rts

master3_old
;	CMPI.W	#$0200,D4		; cmpi.w  #$0400,d4
;	BHI.S	...
;	MOVE.W	D1,D4
	DC.W	$0C44,$0200,$6200,$3801
	DC.W	$FFFF,$FFFF,$FF00,$FFFF

; Masterpiece: trap#3, iow.defw, check Y resolution
ver_master4
	bsr.s	ver_master_qdos
	movem.l master4_old,d1-d4
	bsr	ver_mam
	bne.l	ver_bad
	addq.l	#4,a1
	moveq	#0,d2
	rts

master4_old
;	BCS.S	...
;	CMPI.W	#$0100,D4		; cmpi.w  #$0200,d4
;	BHI.S	...
	DC.W	$6500,$0C44,$0100,$6200
	DC.W	$FF00,$FFFF,$FFFF,$FF00

; Masterpiece CON code, patch screen base
ver_master5
	bsr.l	ver_master_qdos
	movem.l master5_old,d1/d2
	bsr	ver_lkal
	bne.l	ver_bad
	addq.l	#2,a1
	moveq	#0,d2
	rts

master5_old
	MOVE.L	#$20000,A5		; move.l  #$4E0000,a5
	LSL.L	#7,D1

; Masterpiece CON code, patch screen base
ver_master6
	bsr.l	ver_master_qdos
	movem.l master6_old,d1/d2
	bsr	ver_lkal
	bne.l	ver_bad
	addq.l	#2,a1
	moveq	#0,d2
	rts

master6_old
	MOVE.L	#$20000,A1		; move.l  #$4E0000,a5
	ADD.W	D1,A1

; Masterpiece CON code, patch line length
ver_master7
	bsr.l	ver_master_qdos
	movem.l master7_old,d1/d2
	bsr	ver_lkal
	bne.l	ver_bad
	addq.l	#4,a1
	moveq	#0,d2
	rts

master7_old
	ADD.L	A1,A2
	MOVE.W	#$80,A3 		; move.w  #$0100,a3
	LSR.W	#2,D1

; Masterpiece CON code, patch line length
ver_master8
	bsr.l	ver_master_qdos
	movem.l master8_old,d1/d2
	bsr	ver_lkal
	bne.l	ver_bad
	addq.l	#4,a1
	moveq	#0,d2
	rts

master8_old
	EXG	A1,A2
	MOVE.W	#-$80,A3		; move.w  #-$100,a3
	ADD.L	A3,A1

; Masterpiece CON code, patch line length
ver_master9
	bsr.l	ver_master_qdos
	movem.l master9_old,d1-d4
	bsr	ver_mam
	bne.l	ver_bad
	addq.l	#4,a1
	moveq	#0,d2
	rts

master9_old
;	ADDA.L	#$00000080,A5		; adda.l  #$00000100,a5
;	BRA.S	...
	DC.W	$DBFC,$0000,$0080,$6000
	DC.W	$FFFF,$FFFF,$FFFF,$FF00

; Masterpiece CON code, patch line length
ver_master10
	bsr.l	ver_master_qdos
	movem.l master10_old,d1/d2
	bsr	ver_lkal
	bne.l	ver_bad
	addq.l	#4,a1
	moveq	#0,d2
	rts

master10_old
	SUB.L	#$80,A5 		; sub.l   #$0100,a5
	SWAP	D3

; Masterpiece, QDOS: clear screen on MODE call
ver_master_qdos_mode
	bsr.l	ver_master_qdos
	movem.l master11_old,d1/d2
	bsr	ver_lkal
	bne.l	ver_bad
	addq.l	#4,a1
	moveq	#0,d2
	rts

master11_old
	MOVE.W	#$1FFF,D0
	CLR.L	-(A6)			; jsr	  gl_master_cls
	DBF	D0,*-2			; ...

; Minerva + Masterpiece
ver_master_minerva
	cmp.w	#'13',d5
	ble.s	ver_masterm_np
	tst.l	d6
	bmi.s	ver_masterm_rts
ver_masterm_np
	addq.l	#4,sp
	moveq	#1,d2
ver_masterm_rts
	rts

; Masterpiece, Minerva: clear screen on MODE call
ver_master_min_mode
	bsr.s	ver_master_minerva
	movem.l master12_old,d1/d2
	bsr	ver_lkal
	bne.l	ver_bad
	addq.l	#4,a1
	moveq	#0,d2
	rts

;sd_fresh
;	move.l	a5,a1
master12_old
	MOVE.W	#$1FFF,D0
	CLR.L	(A1)+
	DBF	D0,*-2

; Masterpiece available?
ver_master
	tst.l	d6
	bpl.l	ver_np
	move.w	d2,a1
	moveq	#0,d2
	rts

; SGC + no masterpiece
ver_sgc_no_mp
	tst.w	d7
	bne.l	ver_np
	tst.l	d6
	bmi.l	ver_np
	move.w	d2,a1
	moveq	#0,d2
	rts

; Masterpiece and QDOS. Patch in clearing of VRAM on boot
ver_master_qdos_boot
	bsr.l	ver_master_qdos
	move.l	(a4,d2.w),a1	; RESET vector
	lea	$1a(a1),a1	; moveq #0,d0 / moveq #-1,d1 / moveq #-1,d7
	moveq	#0,d2
	rts

; Masterpiece and Minerva. Patch in clearing of VRAM on boot
ver_master_min_boot
	bsr.s	ver_master_minerva
	move.l	(a4,d2.w),a1
	movem.l min_cls_old,d1/d2
	cmp.l	(a4,a1.l),d1	; "cmp.l $5c,a5"
	bne.s	min_cls_np

	rol.l	#8,d2
	cmp.b	4(a4,a1.l),d2	; "beq.s"
	bne.s	min_cls_np

	move.b	5(a4,a1.l),d2	; beq.s offset
	cmp.b	#$ff,d2
	beq.s	min_cls_np

	tst.b	d2
	beq.s	min_cls_np

	addq.l	#6,a1		; patch in JMPs in glm_cls4
	ext.w	d2
	ext.l	d2
	add.l	a1,d2
	move.w	(a2),d1
	lea	(a3,d1.w),a0
	adda.w	(a0),a0
	move.l	d2,-4(a0)
	move.w	(a0)+,d1
	move.l	a1,(a0,d1.w)
	move.l	a0,-(a1)
	move.w	#$4ef9,-(a1)
min_cls_np
	moveq	#1,d2
	rts

; Start of ram test: come here at power on and after "reset" instruction
;ss_ramt
;	CMP.L	magic,A5	check for magic number in a5
min_cls_old
	CMP.L	$5C,A5
	BEQ.L	*+2
;	MOVEQ	#-1,D7
;	DBRA	D7,*		waste some time in case of glitch

	end
