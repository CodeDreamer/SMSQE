; (Super)Gold card boot ROM. Deciphered in large parts by Marcel Kilgus
;
; The (S)GC ROM is 64kb in size and consists of three parts:
;
; $0000-$7FFF  FLP, RAM + TK2. Copied to $10000
; $8000-$BFFF  Patches + Basic commands + PAR + N + DEV. Copied to $1c200
; $C000-$FFFF  Boot/patch engine (this file plus qlvers_asm). Discarded
;
; GoldCard:
;   The ROM is mapped to $40000, with the last 16KB (this part) also being
;   mapped to $C000, so the normal OS boots and finds only 128KB of RAM plus
;   this ROM as the first extension ROM. This results in the "double boot".
;
; SuperGoldCard:
;   The ROM is mapped both to address 0 and $40000, so it gets activated
;   immediately on boot. The OS is mapped to $400000.
;
; With a write access to $1c060 the ROMs are mapped out of the address space
; and the RAM takes over. In case of SGC the copies of the SGC-ROM in RAM are
; write protected, on GC only the OS ROM area is protected.

	section boot

	xref	ver_list
	xref	ver_indep
	xref	ver_addr

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_qdos_io'
	include 'dev8_keys_68000'
	include 'dev8_sys_gold_keys'
	include 'dev8_mac_assert'

base
	dc.l	$4afb0001
	dc.w	0
	dc.w	start-base
	dc.w	0			; no message

start
	lea	*,a0			; on SGC this ROM is the boot ROM
	cmp.l	$4,a0			; are we the RESET vector?
	beq.s	setup			; ... yes!
	trap	#0			; ... no, there is an OS, go super
setup
	sub.l	a0,a0			; assumed base of QL ROM (GC)
	move	#$3700,sr    !!!!!!!!!
	move.w	sr,d1
	lea	glk_bas0,a6

	sf	glo_rdis(a6)		; disable RAM / enable ROM
	moveq	#0,d6
	moveq	#2,d7			; 16 MHz 68000
	cmp.w	#$2700,d1		; 68000?
	beq.s	copy			; ... yes

	move.w	#$2700,sr		; ... no, reset to normal
	moveq	#9,d0
	dc.l	$4e7b0002		; clear and enable cache
	sf	sgo_scr2(a6)		; disable second screen
	movem.l glo_ptch(a6),d0/d1/d2/d3/d4/d5/d6/d7 ; not sure, waste time?
	move.l	#$28480,sp		; initial stack pointer
	moveq	#$20,d6 		; 68020
	moveq	#0,d7			; ... and its table
	move.l	#sgk_qlrom,a0		; reset base of ROM
copy
	move.l	a0,a4			; 0 (GC) or $400000 (SGC)
	sub.l	a1,a1			; copy ROM to RAM
	move.w	#$c000/4-1,d0
rom_to_ram
	move.l	(a0)+,(a1)+		; write into shadow RAM
	dbra	d0,rom_to_ram

	lea	base,a0 		; always $c000 I think?
	cmp.l	a1,a0			; also always $c000, strange
	bne.s	copy_gold		; I think this jump never happens
	lea	glk_boot,a0		; $4c000
copy_gold
	sub.l	#glk_boot-(glk_gold+8),a0 ; -$bfff8 = $0008
	lea	glx_gold-$c000(a1),a1	; copy GOLD code to here ($10000)
	move.w	#(glk.gold-8)/4-1,d0
	move.l	#$4afb0001,(a1)+	; add ROM header
	clr.w	(a1)+
	move.w	#$1c,(a1)+		; code start offset
gold_copy
	move.l	(a0)+,(a1)+		; copy GOLD part of ROM to RAM
	dbra	d0,gold_copy

	lea	glx_ptch-(glx_gold+glk.gold)(a1),a1 ; adjust to sgx_ptch
	move.l	a1,a3
	move.w	#glk.ptch/4-1,d0
rom_patch
	move.l	(a0)+,(a1)+		; copy patches etc. to RAM
	dbra	d0,rom_patch

	move.l	#$1a1a1a1a,$3ff4(a6)	 ; $1FFF4... why?
	move.w	d7,glk.card+glo_ptch(a6) ; GC (2) or SGC (0)
	move.b	d6,glk.proc+glo_ptch(a6) ; 68000 (0) or 68020 ($20)

; Now we try to find the version
; Any version in the form 1.02-1.99 matches (. can also be A-Z)
	move.l	#$BFFE,a0
	adda.l	a4,a0
	move.l	a4,a1
ver_find
	cmp.l	a1,a0
	ble.l	ver_bad
	move.w	-(a0),d5
	cmp.w	#'1.',d5
	beq.s	verf_check
	cmp.w	#'1A',d5
	blt.s	ver_find
	cmp.w	#'1Z',d5
	bgt.s	ver_find
verf_check
	move.w	2(a0),d5
	cmp.w	#'02',d5
	blt.s	ver_find
	cmp.w	#'99',d5
	bgt.s	ver_find
	cmp.b	#'0',d5
	blt.s	ver_find
	cmp.b	#'9',d5
	bgt.s	ver_find

	lea	ver_list,a0		; QDOS specific version patches
	bclr	#31,d7

ver_look
	move.l	a0,a2
	add.w	(a0)+,a2		; QL patch address list offset
	cmp.w	(a0),d5
	bhi.s	ver_next		; ... not yet right version
	blo.l	ver_bad 		; ... past it
	move.w	glp_addr(a2),a1
	move.w	(a4,a1.l),d0
	cmp.w	glp_oldw(a2),d0 	; first patch correct?
	beq.s	patch			; version dependent patches
ver_next
	tst.w	(a0)+			; end of list?
	bgt.s	ver_look		; ... no

patch_all
	bset	#31,d7			; version independent patches
	bne.l	reset			; already done
	lea	ver_indep,a2		; version independent list

patch
	move.w	(a2)+,d6		; next patch
	beq.s	patch_all

	moveq	#0,d1
	move.w	(a2)+,d1		; ROM address / calculation routine offset
	move.l	d1,a1

	move.w	(a2)+,d2		; old data / calculation parameter
	bclr	#0,d1			; calculate address?
	beq.s	patch_value		; ... no
	lea	ver_addr,a1		; calculation table
	add.w	d1,a1
	add.w	(a1),a1
	jsr	(a1)			; calculate
	bne.s	patch_bad

patch_value
	move.w	(a2)+,d1		; patch value
	lea	(a3,d1.w),a0		; data / patch table entry
	add.w	(a0),a0
	move.w	patch_tab(pc,d6.w),d6
	jmp	patch_tab(pc,d6.w)

patch_tab
	dc.w	patch_all-patch_tab
	dc.w	patch_word-patch_tab
	dc.w	patch_trap-patch_tab
	dc.w	patch_vect-patch_tab
	dc.w	patch_patch-patch_tab
	dc.w	patch_time-patch_tab
	dc.w	patch_addr-patch_tab
	dc.w	patch_indir-patch_tab

patch_bad
	addq.l	#2,a2
	bpl.s	patch
	bra.l	ver_bad

patch_word
	tst.w	d2			; check old data
	beq.s	patch_data		; no check
	cmp.w	(a4,a1.l),d1		; already a fixed ROM version?
	beq.s	patch			; ... next
	cmp.w	(a4,a1.l),d2		; correct old data?
	bne.l	ver_bad
patch_data
	move.w	d1,(a1) 		; patch word
	bra.s	patch

patch_trap
	move.l	(a4,a1.l),-4(a0)	; move trap vector to return address
	move.l	a0,(a1) 		; new trap vector
	bra.s	patch

patch_vect
	move.w	#$4ef9,d0
patch_jchk
	tst.w	d2			; check old data
	beq.s	patch_jmp		; no check
	cmp.w	(a4,a1.l),d2		; correct old data?
	bne.l	ver_bad
patch_jmp
	move.w	d0,(a1)+		; jmp / jsr
	move.l	a0,(a1) 		; to new
	bra.s	patch
patch_patch
	move.w	#$4eb9,d0
	bra.s	patch_jchk

patch_time
	tst.w	d7			; standard speed?
	beq.l	patch			; ... yes
	move.w	(a0)+,d2		; number of timings to patch
	bra.s	ptm_eloop
ptm_loop
	move.w	(a0,d7.w),(a0)		; copy timing constant down
	addq.w	#4,a0
ptm_eloop
	dbeq	d2,ptm_loop
	bra.l	patch

patch_addr
	move.l	a0,(a1)
	bra.l	patch

patch_indir
	move.w	(a0)+,d1
	move.l	(a4,a1.l),(a0,d1.w)	; move data
	move.l	a0,(a1) 		; and point to it
	bra.l	patch

	section ver


	section done
reset
	sub.l	a5,a5			; this code only works for GC,
	movem.l (a5),a0/a1		; probably left in by accident

	movem.l (a4),a0/a1		; this code works for GC & SGC
	move.l	a0,sp			; set sp
	lea	glo_rena(a6),a6
	sf	(a6)			; disable ROM, let RAM take over
	jmp	(a1)			; jump into OS

; OS version string not found
ver_bad
	lea	message,a0
	sf	$18063
	move.l	#$20800,a1
	moveq	#20-1,d0
verb_lp1
	moveq	#($80-$10)/4-1,d1
	addq.l	#8,a1
verb_lp2
	move.l	(a0)+,(a1)+
	dbra	d1,verb_lp2
	addq.l	#8,a1
	dbra	d0,verb_lp1
	bra.s	ver_bad

; mestext in graphical representation
message
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0F0F,$C3C3,$0000,$0000,$0303,$FCFC,$0F0F,$C3C3
	dc.w	$0303,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$3030,$0000,$0000,$0000,$0000,$0000,$3030
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0303,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0303,$0000,$0000,$0000,$0000,$0303
	dc.w	$0303,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$3030,$3333,$0000,$0000,$0303,$0303,$3030,$3333
	dc.w	$CFCF,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0303,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0303
	dc.w	$0303,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$3030,$3333,$0000,$0000,$0303,$0303,$3030,$3333
	dc.w	$3333,$0000,$0303,$0303,$0F0F,$C3C3,$3C3C,$0F0F
	dc.w	$C0C0,$3030,$0F0F,$C3C3,$FCFC,$0000,$0000,$3030
	dc.w	$0F0F,$C0C0,$0000,$3F3F,$C0C0,$FCFC,$0F0F,$C0C0
	dc.w	$0000,$3333,$C0C0,$FCFC,$0F0F,$F0F0,$FCFC,$0F0F
	dc.w	$C3C3,$FCFC,$0303,$0000,$FCFC,$0F0F,$C0C0,$FFFF
	dc.w	$0303,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$3030,$3333,$0000,$0000,$0303,$FCFC,$3030,$3333
	dc.w	$0303,$0000,$0303,$0303,$3030,$3333,$C3C3,$3030
	dc.w	$0000,$3030,$3030,$3333,$0303,$0000,$0000,$3030
	dc.w	$3030,$0000,$0000,$3030,$3333,$0303,$0303,$0000
	dc.w	$0000,$3C3C,$3333,$0303,$3030,$0303,$0303,$3030
	dc.w	$3333,$0303,$0303,$0303,$0000,$3030,$3333,$0303
	dc.w	$0303,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$3333,$3333,$0000,$0000,$0303,$3030,$3030,$3333
	dc.w	$0303,$0000,$0303,$0303,$3F3F,$F3F3,$0000,$0F0F
	dc.w	$C0C0,$3030,$3030,$3333,$0303,$0000,$0000,$3030
	dc.w	$0F0F,$C0C0,$0000,$3030,$3333,$0303,$0303,$0000
	dc.w	$0000,$3030,$0303,$FFFF,$3030,$0303,$0303,$3030
	dc.w	$3333,$0303,$0303,$0000,$FCFC,$3F3F,$F3F3,$0303
	dc.w	$0303,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$3030,$C3C3,$0000,$0000,$0303,$0C0C,$3030,$3333
	dc.w	$0303,$0000,$0000,$CCCC,$3030,$0303,$0000,$0000
	dc.w	$3030,$3030,$3030,$3333,$0303,$0000,$0000,$3030
	dc.w	$0000,$3030,$0000,$3030,$3333,$0303,$0303,$0000
	dc.w	$0000,$3030,$0303,$0000,$3030,$0303,$0303,$3030
	dc.w	$3333,$0303,$0303,$0000,$0303,$3030,$0303,$0303
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0F0F,$3333,$FFFF,$0000,$0303,$0303,$0F0F,$C3C3
	dc.w	$0303,$0000,$0000,$3030,$0F0F,$F3F3,$0000,$0F0F
	dc.w	$C0C0,$0C0C,$0F0F,$C3C3,$0303,$0000,$0000,$0C0C
	dc.w	$0F0F,$C0C0,$0000,$3030,$3030,$FCFC,$0000,$F0F0
	dc.w	$0000,$3030,$0000,$FFFF,$0F0F,$F0F0,$FCFC,$0F0F
	dc.w	$F3F3,$0303,$0000,$C0C0,$FCFC,$0F0F,$F0F0,$FFFF
	dc.w	$0303,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$3030,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0F0F
	dc.w	$C0C0,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$3F3F,$C0C0,$3030,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0303
	dc.w	$0000,$0000,$0000,$0000,$3030,$0000,$0303,$0303
	dc.w	$0303,$0000,$0000,$0000,$0000,$0000,$0303,$0000
	dc.w	$0000,$0000,$0000,$FCFC,$0000,$0000,$0000,$0303
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$3030,$3030,$3030,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0303
	dc.w	$0000,$0000,$0000,$0000,$3030,$0000,$0303,$CFCF
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0303,$0000
	dc.w	$0000,$0000,$0303,$0303,$0000,$0000,$0000,$0303
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$3030,$3030,$3030,$0F0F,$C0C0,$F3F3,$0F0F,$C0C0
	dc.w	$FCFC,$0000,$0000,$FFFF,$0F0F,$C3C3,$FCFC,$0F0F
	dc.w	$C0C0,$F3F3,$0F0F,$F0F0,$FCFC,$0000,$0303,$3333
	dc.w	$0303,$0303,$3C3C,$0F0F,$3030,$FFFF,$0303,$0000
	dc.w	$FCFC,$0000,$0303,$0000,$3030,$3030,$FCFC,$0F0F
	dc.w	$C0C0,$FCFC,$3C3C,$C0C0,$FCFC,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$3F3F,$C0C0,$3030,$3030,$3333,$0F0F,$3030,$0303
	dc.w	$0303,$0000,$0303,$0000,$3030,$3333,$0303,$0303
	dc.w	$0303,$0F0F,$3030,$0000,$3030,$0000,$0303,$0303
	dc.w	$0303,$0303,$C3C3,$3030,$F3F3,$0000,$0303,$0303
	dc.w	$0303,$0000,$0000,$FCFC,$3030,$3333,$0000,$0303
	dc.w	$0303,$0303,$3333,$3333,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$3030,$0000,$3030,$3F3F,$F3F3,$0303,$0F0F,$C3C3
	dc.w	$FFFF,$0000,$0303,$0000,$3030,$3333,$0303,$0303
	dc.w	$0303,$0303,$3030,$0000,$3030,$0000,$0303,$0303
	dc.w	$0303,$0303,$0000,$3030,$3333,$0000,$0303,$0303
	dc.w	$FFFF,$0000,$0000,$0303,$3030,$3030,$FCFC,$0303
	dc.w	$0303,$FFFF,$3333,$3030,$FCFC,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$3030,$0000,$3030,$3030,$0303,$0F0F,$0000,$3333
	dc.w	$0000,$0000,$0303,$0000,$3030,$3333,$0303,$0303
	dc.w	$0303,$0F0F,$3030,$0000,$3030,$0000,$0303,$0303
	dc.w	$0303,$0303,$0000,$3030,$F3F3,$0000,$0303,$0303
	dc.w	$0000,$0000,$0303,$0303,$3030,$3030,$0303,$0303
	dc.w	$0303,$0000,$3333,$3030,$0303,$0303,$C0C0,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$3030,$0000,$0C0C,$0F0F,$F0F0,$F3F3,$0F0F,$C0C0
	dc.w	$FFFF,$0000,$0000,$FFFF,$0F0F,$C3C3,$0303,$0000
	dc.w	$F0F0,$F3F3,$0F0F,$F0F0,$0F0F,$0000,$0303,$0303
	dc.w	$0000,$C3C3,$0000,$0F0F,$3030,$FFFF,$0000,$C0C0
	dc.w	$FFFF,$0000,$0000,$FCFC,$0F0F,$F0F0,$FCFC,$0000
	dc.w	$F0F0,$FFFF,$3333,$3030,$FCFC,$0303,$C0C0,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$3030,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0F0F,$C0C0,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000
	dc.w	$0000,$0000,$0000,$0000,$0000,$0000,$0000,$0000

mestext dc.w	endmtext-*-2
	dc.b	'QL ROM version is not recognised!',$a
	dc.b	'Please contact Miracle Systems.',$a
endmtext
	end
