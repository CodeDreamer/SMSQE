; base area SMSQ ABC Keyboard Driver
; This sets the address of the ABC initialisation in SYS_CKYQ and so must be
; loaded before the standard driver

	section header

	xref	smsq_end
	xref	ioq_setq
	xref	smsq_sreset

	include 'dev8_smsq_kbd_keys'
	include 'dev8_keys_err'
	include 'dev8_keys_sys'
	include 'dev8_keys_chn'
	include 'dev8_keys_iod'
	include 'dev8_keys_con'
	include 'dev8_keys_qlv'
	include 'dev8_keys_68000'
	include 'dev8_keys_qdos_sms'
	include 'dev8_smsq_gold_kbd_abc_keys'
	include 'dev8_smsq_smsq_base_keys'

	include 'dev8_keys_stella_bl'

header_base
	dc.l	kbd_base-header_base	 ; length of header
	dc.l	0			 ; module length unknown
	dc.l	smsq_end-kbd_base	 ; loaded length
	dc.l	0			 ; checksum
	dc.l	select-header_base	 ; select for ABC keyboard
	dc.b	1			 ; 1 level down
	dc.b	0
	dc.w	smsq_name-*

smsq_name
	dc.w	22,'SMSQ GOLD ABC Keyboard'
	dc.l	'    '
	dc.w	$200a


select
	cmp.b	#kb.abc>>8,sbl_mtype+2(a5)  ; ABC keybard?
	bne.s	sel_noload
	moveq	#sbl.load,d0
	rts
sel_noload
	moveq	#sbl.noload,d0
	rts

	section base

kbd_base
	moveq	#sms.xtop,d0		; set up address of initialisation
	trap	#do.smsq
	lea	kbd_init,a1
	move.l	a1,sys_ckyq(a6)
	rts



; in this version, put the address of the init in SYSVAR: will be called with
; in system mode, a5 pointing to key polling patch address
;		  a4 pointing to hdop patch address
;		  a3 pointing to linkage

kbd_init
	move.l	a3,a0
	lea	kbd_poll,a2		  ; ... to here
	bsr.s	kbdi_wba2
	move.l	a4,a5
	lea	kbd_hdop,a2
	bsr.s	kbdi_wba2

	lea	 kb_qu(a0),a2
	moveq	 #kb.qu,d1	  now create a keystroke buffer
	jsr	 ioq_setq

	move.l	a0,a3		   linkage
	move.l	a0,sys_exil(a6)    external interrupt linkage
	clr.l	(a0)+
	lea	kbd_exti,a2
	move.l	a2,(a0)
	moveq	#0,d0
kbdi_rts
	rts

kbdi_wba2
	move.w	#jsr.l,d0		  ; jsr
	bsr.s	kbdi_wbase
	move.l	a2,d0
	swap	d0
	bsr.s	kbdi_wbase
	swap	d0
kbdi_wbase
	cmp.l	#$20000,a1		 ; protected?
	bhs.s	kbd_wdrct
	jmp	sms.wbase		 ; ... yes
kbd_wdrct
	move.w	d0,(a5)+		 ; ... no write directly
	rts


kbd_hdop
	cmp.b	#9,(a3) 		 ; keyrow?
	bne.s	kbdi_rts		 ; ... no

	move.l	a3,(sp) 		 ; save A3 and kill return
	cmp.w	#$0901,(a3)+		 ; just one parameter byte?
	bne.s	khdp_bp 		 ; ... no
	moveq	#%11,d0 		 ; first byte description only
	and.l	(a3)+,d0
	bne.s	khdp_bp 		 ; not a nibble
	moveq	#0,d1
	move.b	(a3)+,d0		 ; row number

	lea	sys_poll(a6),a3 	 ; find kbd poll linkage
	lea	kbd_exti,a2
khdp_look
	move.l	(a3),d1 			; next link
	beq.s	khdp_nop			; ... all done
	move.l	d1,a3
	cmp.l	iod_xiad-iod_pllk(a3),a2	; ours?
	bne.s	khdp_look			; ... no

	moveq	#0,d1
	move.b	kb_krwem-iod_pllk(a3,d0.w),d1	; get keyrow
	bra.s	khdp_ok

khdp_bp
	moveq	#err.ipar,d0
	bra.s	khdp_exit
khdp_nop
	moveq	#0,d1
khdp_ok
	moveq	#0,d0
khdp_exit
	move.l	(sp)+,a3
	move.l	sms.rte,a5
	jmp	(a5)			 ; return from trap


kbd_exti
; not.w  $20004
; blt.s  kbp_2
; clr.w  $20004
; not.w  $20006
; blt.s  kbp_2
; clr.w  $20006
;kbp_2
	 move.b   kb.strob,d1	    check whether a key has been pressed
	 btst	  #kb..st,d1	    
	 bne.s	  kbdi_rts	    no key has been pressed

; not.w  $2000a
; blt.s  kbp_3
; clr.w  $2000a
; not.w  $2000c
; blt.s  kbp_3
; clr.w  $2000c
;kbp_3
	 move.b   kb.data,d1	    get key-code
	 move.b   kb.clstr,d0	    and clear strobe
;j1234
	 lea	  kb_qu(a3),a2	    now put code into buffer
	 move.w   ioq.pbyt,a3
	 jmp	  (a3)


key_atab
	 dc.b	  29,ctrl_key-key_jump
	 dc.b	  42,shft_key-key_jump
	 dc.b	  54,shft_key-key_jump
	 dc.b	  55,hot_key-key_jump
	 dc.b	  56,alt_key-key_jump
	 dc.b	  69,num_key-key_jump
	 dc.b	  0,0


; ABC keyboard polling routine
;
;	a1 c  p keyboard linkage
;
; must not smash d7

kbd_poll
; addq.b #5,$20000
; bcc.s  kbp_do
; not.w  $20002
; blt.s  kbp_do
; clr.w  $20002
;
; addq.b #1,$20004
; move.b $20004,d0
; ror.b  #1,d0		   lsb becomes key up
; and.b  #$9f,d0
; moveq  #7,d2
; moveq  #0,d1		   swap bits 7 to 0, 6 to 1, 5 to 2 ...
;kbx_loop
; roxr.b #1,d0
; roxl.b #1,d1
; dbra	 d2,kbx_loop
; move.l a1,a3
; bsr	 j1234
; bra.l  kbd_done

kbp_do
	 lea	  kb_qu(a1),a2
	 move.w   ioq.gbyt,a3
	 jsr	  (a3)
	 blt.l	  kbd_done	    no key

; not.w  $20010
; blt.s  kbp_1
; clr.w  $20010
; not.w  $20012
; blt.s  kbp_1
; clr.w  $20012
;kbp_1
	 move.l   a1,a3
*
	 moveq	  #7,d2
	 moveq	  #0,d0 	    swap bits 7 to 0, 6 to 1, 5 to 2 ...
rox_loop
	 roxr.b   #1,d1
	 roxl.b   #1,d0
	 dbra	  d2,rox_loop
*
	 move.b   d0,d1
	 bsr	  emul_krw	    update KEYROW-Table
*
	 move.b   d1,d0 	    d1 = key-code and key-state
	 and.b	  #$7F,d0	    d0 = key-code only
	 tst.b	  d1		    SYS REQ?
	 beq	  sys_key

	 lea	  key_atab(pc),a1
key_aloop
	 move.b   (a1)+,d2
	 beq.s	  key_up
	 addq.l   #1,a1
	 cmp.b	  d2,d0
	 bne.s	  key_aloop
	 moveq	  #0,d2
	 move.b   -(a1),d2
	 jmp	  key_jump(pc,d2.w)
key_jump

key_up
	 tst.b	  d1		    key up?
kbd_don3
	 blt.l	  kbd_done	    yes, no action	  
*
	 cmp.b	  #83,d1
	 bhi.l	  kbd_done	    not allowed!
	 cmp.b	  #70,d1	    key on number-block?
	 bls.s	  no_num_t
;	  btst	   #kb..ctrl,kb_stat(a3)
;	  beq.s    nonumct
;	  cmp.b    #74,d1	     CTRL +?
;	  beq	   speed_up
;	  cmp.b    #78,d1	     CTRL -?
;	  beq	   speed_dn
nonumct  sub.b	  #71,d1
	 lea	  tra_num1,a1
	 bra.s	  all_tra
*
no_num_t move.l   kb_ktab(a3),a1
	 moveq	  #71,d0
	 btst	  #kb..shft,kb_stat(a3)
	 beq.s	  no_shft
	 add.l	  d0,a1
no_shft  btst	  #kb..ctrl,kb_stat(a3)
	 beq.s	  no_ctrl
	 add.l	  d0,a1
	 add.l	  d0,a1
	 cmp.b	  #57,d1
	 beq	  break
no_ctrl
all_tra  and.w	  #$FF,d1
	 move.b   (a1,d1.w),d1	    get QL key code
*
* normal keystroke
*
	 btst	  #kb..alt,kb_stat(a3)
	 beq.s	  key_case
	 cmp.b	  #$fd,d1		   reset?
	 bne.s	  set_alt
	 btst	  #kb..ctrl,kb_stat(a3)
	 bne	  kbd_reset		  ... yes
set_alt
	 move.b   d1,d5
	 moveq	  #-1,d1
	 bsr	  key_qin
	 move.b   d5,d1
	 bra.l	  key_send
*
ctrl_key moveq	  #kb..ctrl,d3	    handling of all shift keys
	 bra.s	  sf_keys
shft_key moveq	  #kb..shft,d3
	 bra.s	  sf_keys
alt_key  moveq	  #kb..alt,d3
sf_keys  btst	  #kb..up,d1
	 beq.s	  sfs_keys
	 bclr	  d3,kb_stat(a3)
	 bra.s	  kbd_don2
sfs_keys bset	  d3,kb_stat(a3)
	 bra.s	  kbd_don2
*
num_key
	 bra.s	  kbd_don2
*
hot_key
;	  tst.b    d1			  key up?
;	  blt.s    kbd_don2		  ... yes
;	  moveq    #-1,d1		  otherwise send ALT
;	  bsr	   key_qin
;	  move.b   kb_hotk(a3),d1	  and defined key-code
;	  bsr	   key_qin
	 bra.s	  kbd_don2

sys_key
;	  move.b   kb_stat(a3),d0    handling of SYSREQ
;	  and.b    #7,d0
;	  cmp.b    #5,d0
;	  beq	   level_7	     ALT-SHIFT-SYSREQ gives Level-7-Int
	 bra.s	  switch	    otherwise switch jobs
*
;alt_ent
;	  moveq    #-1,d1	     handling of INS
;	  bsr	   key_qin	     send ALT
;	  moveq    #10,d1
;	  bsr	   key_qin	     send ENTER
insert
	  moveq    #$fffffffc,d1
	  bsr	   key_qin	     enter code
kbd_don2
	 bra.l	  kbd_done
*
key_case tst.w	  sys_caps(a6)	     check capslock
	 beq.s	  key_send	    ... not caps
	 cmp.b	  #'a',d1	    less than 'a'?
	 blo.s	  key_send
	 cmp.b	  #'z',d1	    greater than 'z'?
	 bhi.s	  key_chi
	 bclr	  #5,d1 	    upper case it
	 bra.s	  key_send
key_chi  cmp.b	  #$80,d1	    less than '€'?
	 blo.s	  key_send
	 cmp.b	  #$9b,d1	    greater than '«'?
	 bhi.s	  key_send
	 bset	  #5,d1 	    upper case it
	 bra.s	  key_send
*
break
	st	sys_brk(a6)
kbd_don1
	 bra.s	  kbd_done
*
* switch keyboard queue
*
switch
	move.l	sys_ckyq(a6),a2    current keyboard queue
	move.l	(a2),d2 		 ; keep next queue
	move.l	d2,a2
	bra.s	hok_ckq

hok_nxq
	move.l	(a2),a2 		 ; next queue
	cmp.l	a2,d2			 ; same as saved queue?
	beq.s	hok_setq
hok_ckq
	tst.b	sd_curf-sd_keyq(a2)	 ; cursor enabled?
	bne.s	hok_setq		 ; ... yes, go to it
	tst.b	chn_stat-sd_keyq(a2)	 ; waiting?
	beq.s	hok_nxq 		 ; ... no
	cmp.b	#4,chn_actn-sd_keyq(a2)  ; input?
	bgt.s	hok_nxq 		 ; ... no

hok_setq
	move.l	a2,sys_ckyq(a6) 	 ; reset keyboard queue
	bra.s	kbd_done

*
freeze	 not.b	  sys_dfrz(a6)	    ... toggle screen frozen flag
	 bra.s	  kbd_done
*
caps_loc not.b	  sys_caps(a6)
	 tst.l	  sys_csub(a6)	     capslock routine?
	 beq.s	  kbd_done
	 jmp	  sys_csub(a6)	     ... yes, do it
*
key_send cmp.b	  sys_swtc+1(a6),d1  is it switch queue character?
	 beq	  switch
	 cmp.b	  #249,d1	    is it CTRL F5?
	 beq	  freeze
	 cmp.b	  #$E0,d1	    is it CAPS LOCK?
	 beq	  caps_loc
*
key_qin
	 sf	  sys_dfrz(a6)
	 move.b   d1,sys_lchr+1(a6)
	 move.l   a3,a1 	    save linkage pointer
	 move.l   sys_ckyq(a6),a2    get current keyboard queue
	 move.w   ioq.pbyt,a3	    put character in
	 jsr	  (a3) 
	 move.l   a1,a3 	    restore linkage pointer
*
* done
*
kbd_done
	 rts

kbd_reset
	jmp	smsq_sreset

*
*
* keyboard translation table for numlock
*
tra_num0 dc.b $c1,$d0,$d4,$5f,$c0,$20,$c8,$0a,$c9,$d8,$dc,$20,$ca   * 71 normal
	 dc.b $c5,$d1,$d5,$5f,$c4,$20,$cc,$0a,$cd,$d9,$dd,$20,$ce   * 71 shift
	 dc.b $c3,$d2,$d6,$5f,$c2,$20,$ca,$0a,$cb,$da,$de,$20,$ca   * 71 ctrl
	 dc.b $c7,$d3,$d7,$5f,$c6,$20,$ce,$0a,$cf,$db,$df,$20,$ce   * 71 ctrl sh
tra_num1 dc.b '7','8','9','-','4','5','6','+','1','2','3','0','.'   * 71

*
*	 Update the KEYROW_Table
*
emul_krw moveq	  #$7f,d0	    do not modify d1, use d0
	 and.b	  d1,d0 	    we need a word index
	 lea	  krw_tab,a2	    get base of keyrow-index-table
	 move.b   (a2,d0.w),d0	    get bit/row
	 move.w   d0,d2 	    make a copy
	 and.b	  #$f,d2	    now we got the row only
	 ror.b	  #4,d0
	 and.b	  #$f,d0	    and this is the bit
	 btst	  #kb..up,d1	    Key up?
	 bne.s	  emul_kup
	 bset	  d0,kb_krwem(a3,d2.w)
	 rts
emul_kup bclr	  d0,kb_krwem(a3,d2.w)
	 rts
*
*	 Keyrow-Table	high nibble: bit, low nibble: row 
*
krw_tab  dc.b	  $08,$31,$34,$16   * 0
	 dc.b	  $14,$60,$20,$26   * 4
	 dc.b	  $70,$06,$05,$56   * 8
	 dc.b	  $55,$53,$18,$35   * 12
	 dc.b	  $36,$15,$46,$45   * 16
	 dc.b	  $66,$65,$76,$25   * 20
	 dc.b	  $75,$54,$03,$02   * 24
	 dc.b	  $01,$17,$44,$33   * 28
	 dc.b	  $64,$43,$63,$24   * 32
	 dc.b	  $74,$23,$04,$73   * 36
	 dc.b	  $72,$52,$07,$51   * 40
	 dc.b	  $12,$37,$32,$47   * 44
	 dc.b	  $42,$67,$62,$77   * 48
	 dc.b	  $22,$57,$07,$28   * 52
	 dc.b	  $27,$61,$13,$10   * 56
	 dc.b	  $30,$40,$00,$50   * 60
	 dc.b	  $38,$48,$58,$68   * 64
	 dc.b	  $78,$09,$19,$29   * 68
	 dc.b	  $21,$39,$49,$11   * 72
	 dc.b	  $59,$41,$69,$79   * 76
	 dc.b	  $71,$0a,$1a,$2a   * 80

	 end
