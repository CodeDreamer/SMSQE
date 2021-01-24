; AY-sound chip API and BASIC commands 3.00  2020 Jochen Merz and Marcel Kilgus
;
; Mostly QSOUND compatible where possible, but supports multiple chips.
; Actual hardware access is implemented in separate file.

	section qsound

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_keys_err'
	include 'dev8_keys_qlv'
	include 'dev8_keys_sbasic'
	include 'dev8_mac_proc'
	include 'dev8_iod_qsound_keys'

	xdef	qsound_base
	xdef	qsound.vers

	xref	qsound_drv_init
	xref	ay_hw_setup
	xref	ay_hw_wrreg
	xref	ay_hw_wrall
	xref	ay_hw_type
	xref	ay_hw_freq
	xref	ay_hw_stereo
	xref	ay_hw_volume

cmd.count equ	15
qsound.vers	equ   '3.00'

qsound_base
	lea	proc_def,a1
	move.w	sb.inipr,a2
	jsr	(a2)
	bsr.s	start
	jsr	qsound_drv_init
	moveq	#0,d0
	rts

start	movem.l a0/a3,-(sp)
	moveq	#sms.info,d0
	trap	#1
	move.l	a0,a4

; Allocate data block
	move.l	#qs_size,d1
	moveq	#-1,d2
	moveq	#sms.achp,d0
	trap	#1
	tst.l	d0
	bne.s	start_end
	move.l	a0,sys_qsound(a4)
	move.l	a0,a3			; QSound data block

; Originally there were sv_aybas and sv_ayjmp, with aybas being the data
; block and ayjmp the code entry. sv_ayjmp has been reserved as sys_qsound
; for a long time, but sv_aybas is apparently also used by Turbo! So the
; new way is to have the data block under sv_ayjmp/sys_qsound and have it start
; with a JMP to the real entry
	lea	mc_entry,a1		; Construct JMP to real mc_entry
	move.w	#$4ef9,(a3)
	move.l	a1,2(a3)

	bsr	ay_hw_setup
	bsr	ay_reset

	move.l	sys_qsound(a4),a0	; Link in poll
	lea	int_serve,a1
	move.l	a1,qs_plad(a0)
	lea	qs_pllk(a0),a0
	moveq	#sms.lpol,d0
	trap	#1

;;;	   lea	   gong_tab,a1
;;;	   bsr	   ay_wrall
start_end
	movem.l (sp)+,a0/a3
	rts

error_ok
	moveq	#0,d0
	rts

error_bp
	moveq	#err.ipar,d0
	rts

error_nc
	moveq	#err.nc,d0
	rts

error_or
	moveq	#err.orng,d0
	rts

error_om
	moveq	#err.imem,d0
	rts

error_nimp
	moveq	#err.nimp,d0
	rts

error_no
	moveq	#err.ichn,d0
	rts

; Write d1.b into register d2.b (MC entry)
ay_wrreg
	lea	qs_regs_0(a3),a5	; Chip 0
	move.b	d2,d0
	andi.b	#$f,d2
	lsr.b	#4,d0
	beq.s	ay_wrreg0
	cmp.b	qs_chip_count(a3),d0
	bcc.s	error_or
	lea	qs_regs_1(a3),a5	; Chip 1
	bra.s	ay_wrregx
ay_wrreg0
	cmpi.b	#7,d2
	bne.s	ay_wrregx		; For QSound we need to make sure the I/O
	ori.b	#$c0,d1 		; port of chip 0 is always output (for PAR port)
ay_wrregx
	cmpi.w	#13,d2
	bhi.s	error_or
; Write d1.b into register d2.b, a5 = ptr to chip registers
write_reg
	move.b	d1,(a5,d2.w)		; Save in register cache
	bra	ay_hw_wrreg		; Write to h/w

ay_rdreg
	lea	qs_regs_0(a3),a5	; Chip 0
	move.b	d2,d0
	andi.b	#$f,d2
	lsr.b	#4,d0
	beq.s	ay_rdreg0
	cmp.b	qs_chip_count(a3),d0
	bcc.s	error_or
	lea	qs_regs_1(a3),a5	; Chip 1
ay_rdreg0
	cmpi.w	#13,d2
	bhi.s	error_or
; Read the value from register d2.b to d1.b (actually just read cache)
; a5 = ptr to chip registers
read_reg
	moveq	#0,d1
	move.b	(a5,d2.w),d1
	moveq	#0,d0
	rts

; MC entry point
mc_entry
	cmpi.l	#cmd.count,d0
	bhi	error_nc

	movem.l a3-a4,-(sp)
	lsl.w	#1,d0
	lea	mc_table,a5
	adda.w	d0,a5
	move.w	(a5),a5
	lea	qsound_base,a4
	adda.l	a4,a5
	bsr.s	get_aybas		; Get data block in a3
	jsr	(a5)
	movem.l (sp)+,a3-a4
	rts

; Get QSound data block in a3
get_aybas
	movem.l d0-d2/a0,-(sp)
	moveq	#sms.info,d0
	trap	#1
	move.l	sys_qsound(a0),a3
	movem.l (sp)+,d0-d2/a0
	rts

; Free all sound queues
free_sound_queues
	movem.l d5-d7,-(sp)
	moveq	#0,d5
	moveq	#0,d6
	move.b	qs_chan_count(a3),d7
free_queue_loop
	move.l	qs_mem(a3,d6.w),d0
	beq.s	free_next_queue
	move.l	d0,a0
	sf	qs_act(a3,d5.w)
	clr.l	qs_mem(a3,d6.w)

	move.l	a3,-(sp)
	moveq	#sms.rchp,d0
	trap	#1
	move.l	(sp)+,a3
free_next_queue
	addq.w	#4,d6
	addq.b	#1,d5
	cmp.b	d5,d7
	bne.s	free_queue_loop
	movem.l (sp)+,d5-d7
	rts

; Reset AY chip
ay_reset
	move.l	d1,-(sp)
	bsr.s	get_aybas
	bsr.s	free_sound_queues

	lea	quiet_tab,a1
	moveq	#0,d2			; Chip 0
	bsr.s	ay_wrall

	cmp.b	#1,qs_chip_count(a3)
	bne.s	ay_reset_end

	lea	quiet_tab,a1
	moveq	#1,d2			; Chip 1
	bsr.s	ay_wrall

ay_reset_end
	move.l	(sp)+,d1
	moveq	#0,d0
	rts

; Write 14 bytes block pointed to by a1 into registers 0..13 of AY
; Chip number in d2
ay_wrall
	cmp.b	qs_chip_count(a3),d2
	bcc	error_bp
	lea	qs_regs_0(a3),a5
	tst.b	d2
	beq.s	write_it_all
	lea	qs_regs_1(a3),a5

; d2 = chip (0/1), a5 = ptr to chip registers
write_it_all
	move.l	(a1)+,(a5)
	move.l	(a1)+,4(a5)
	move.l	(a1)+,8(a5)
	move.w	(a1)+,12(a5)

	tst.b	d2
	bne.s	write_all_1
	or.b	#$c0,7(a5)	; Keep I/O ports as output (chip 0 only)
write_all_1
	bra	ay_hw_wrall

; Read all 14 registers (in reality gets them from cache)
ay_rdall
	cmp.b	qs_chip_count(a3),d2
	bcc	error_bp
	lea	qs_regs_0(a3),a5
	tst.b	d2
	beq.s	read_it_all
	lea	qs_regs_1(a3),a5
read_it_all
	move.l	(a5)+,(a1)+
	move.l	(a5)+,(a1)+
	move.l	(a5)+,(a1)+
	move.w	(a5)+,(a1)+
	moveq	#0,d0
	rts

; Make some noise
ay_noise
	lea	explode_tab,a1
	andi.w	#$ff,d1
	cmpi.w	#2,d1
	bhi	error_bp
	mulu	#14,d1
	adda.w	d1,a1
	bra	ay_wrall

******************
* Poll interrupt *
******************
int_serve
	movem.l d0-d7/a0-a6,-(sp)
	bsr	out_sound_queue
	movem.l (sp)+,d0-d7/a0-a6
	rts

**********************
* Basic - extensions *
**********************

; POKE_AY (chip),r,v
;
; Set register r (0..13) to v (0..255) of chip (0..1)
poke_ay
	move.w	sb.gtint,a2
	jsr	(a2)
	bsr	get_aybas

	moveq	#0,d2		; Default to chip 0
	subq.w	#2,d3
	beq.s	poke_it
	subq.w	#1,d3
	bne	error_bp

	move.w	0(a6,a1.l),d2	; Chip number
	addq.l	#2,a1
	lsl.b	#4,d2
poke_it
	or.w	0(a6,a1.l),d2	; Add register number
	move.w	2(a6,a1.l),d1
	bra	ay_wrreg

; LIST_AY (chip),r0,r1,r2,r3..,r13
;
; Set all registers at once
list_ay
	move.w	sb.gtint,a2
	jsr	(a2)
	bsr	get_aybas

	moveq	#0,d2		; Default to chip 0
	sub.w	#14,d3
	beq.s	list_it
	subq.w	#1,d3
	bne	error_bp
	move.w	0(a6,a1.l),d2	; Chip number
list_it
	move.l	sp,a2
	suba.w	#14,sp
	move.l	sp,a0
list_ay_loop
	move.w	(a6,a1.l),d0	; Word array to byte array
	move.b	d0,(a0)+
	addq.w	#2,a1
	cmpa.l	a0,a2
	bne.s	list_ay_loop

	move.l	sp,a1

	bsr	ay_wrall	; Now write all registers
	adda.w	#14,sp
	rts

; v = PEEK_AY((chip), r)
;
; Return contents of register r (0..13)
peek_ay move.w	sb.gtint,a2
	jsr	(a2)
	bsr	get_aybas

	moveq	#0,d2		; Default to chip 0
	subq.w	#1,d3
	beq.s	peek_it
	subq.w	#1,d3
	bne	error_bp
	move.w	0(a6,a1.l),d2	; Chip number
	lsl.b	#4,d2
	addq.l	#2,a1
peek_it
	or.w	(a6,a1.l),d2
	bsr	ay_rdreg
	tst.l	d0
	bne.s	peek_rts
	move.w	d1,(a6,a1.l)
	moveq	#3,d4
	moveq	#0,d0
peek_rts
	rts

; BELL
bell	lea	bell_tab,a1
	bsr	get_aybas
	bra	ay_wrall

; EXPLODE
explode lea	explode_tab,a1
	bsr	get_aybas
	bra	ay_wrall

; SHOOT
shoot	lea	shoot_tab,a1
	bsr	get_aybas
	bra	ay_wrall


***********************************
* Indexes into int_tab jump table *
***********************************
set_period	equ	0
set_volume	equ	1
set_noise	equ	2
set_len 	equ	3
set_susp	equ	4
set_rel 	equ	5
set_n_per	equ	6
set_wave	equ	7
set_w_len	equ	8

; mc_play
;
; d1.b = channel number 1..6
; a0   = pointer to string
ay_play
	move.l	a0,a1
	suba.l	a6,a1
	move.w	d1,d7
	bra.s	play_all

; PLAY channel, string
play
	suba.l	a3,a5
	cmpa.l	#16,a5		; Must be two parametrs
	bne	error_bp
	lea	8(a3),a5
	movem.l a3/a5,-(sp)
	move.w	sb.gtint,a2	; Get channel number
	jsr	(a2)
	movem.l (sp)+,a3/a5
	tst.l	d0
	bne	error_bp

	moveq	#0,d7
	move.w	0(a6,a1.l),d7
	lea	8(a3),a3
	lea	8(a3),a5
	move.w	sb.gtstr,a2	; Play string
	jsr	(a2)
	tst.w	d0
	bne	error_bp

; d7 = channel 1..3
play_all
	bsr	get_aybas
	tst.w	d7
	beq	error_bp
	andi.w	#$ff,d7
	cmp.b	qs_chan_count(a3),d7
	bhi	error_bp
	subq.w	#1,d7

; d7 = channel 0..5
	moveq	#0,d6
	move.w	(a6,a1.l),d6		; String length
	tst.w	d6
	beq	error_bp

	lea	qs_act(a3),a2
	sf	(a2,d7.w)		; Disable channel
	move.l	a1,a4			; Pointer to string

; a4 = pointer to string
	lea	qs_mem(a3),a2
	moveq	#0,d1
	move.b	d7,d1
	lsl.w	#2,d1
	move.l	(a2,d1.w),a0
	move.l	a0,d0
	bne	play_start_loop 	; already a queue? -> append

; Setup a new queue
	move.l	a3,-(sp)
	move.l	#4096,d1
	moveq	#-1,d2
	moveq	#sms.achp,d0
	trap	#1
	move.l	(sp)+,a3
	tst.l	d0
	bne	error_om

	move.l	a0,a1
	move.w	#1023,d1
play_new_loop
	clr.l	(a1)+
	dbra	d1,play_new_loop	; clear

	move.l	a3,-(sp)
	move.l	a0,a2
	move.l	#4096,d1
	move.w	ioq.setq,a1		; setup queue
	jsr	(a1)

; Put in commands to set default values
	move.w	ioq.pbyt,a1
	moveq	#set_volume,d1
	jsr	(a1)
	moveq	#0,d1			; volume = 0
	jsr	(a1)
	moveq	#set_noise,d1
	jsr	(a1)
	moveq	#0,d1			; noise = 0
	jsr	(a1)
	moveq	#set_n_per,d1
	jsr	(a1)
	moveq	#0,d1			; noise period = 0
	jsr	(a1)
	moveq	#set_wave,d1
	jsr	(a1)
	moveq	#0,d1			; wave = 0
	jsr	(a1)
	moveq	#set_w_len,d1
	jsr	(a1)
	moveq	#0,d1			; wave_length = 0
	jsr	(a1)
	moveq	#0,d1
	jsr	(a1)
	move.l	(sp)+,a3

	move.b	#4,qs_okt(a3,d7.w)	; octave = 4
	move.b	#5,qs_len(a3,d7.w)	; len = 5

	lea	qs_mem(a3),a2
	moveq	#0,d1
	move.b	d7,d1
	lsl.w	#2,d1
	move.l	a0,(a2,d1.w)		; play queue for this channel

; d6 = string size
; a0 = play queue
; a4 = pointer to string (rel a6)
play_start_loop
	lea	play_end,a2		; Jump to here on exit
	move.l	a2,-(sp)
	move.l	a0,qs_work_queue(a3)
	addq.w	#1,d6			; just so we can subtract it again
	adda.l	#2,a4			; skip size

play_loop
	moveq	#0,d0
	subq.w	#1,d6
	beq	play_rts

	moveq	#0,d1
	move.b	(a6,a4.l),d1		; get next char
	addq.l	#1,a4
	move.w	d1,d2
	sub.w	#32,d1			; table is based on ' ' char
	bcs	error_bp
	cmp.w	#96,d1			; 96 table entries
	bhi	error_bp

	lea	cmd_table,a0
	move.b	(a0,d1.w),d1
	andi.w	#$0f,d1
	lsl.w	#1,d1
	lea	jump_table,a0
	adda.w	d1,a0
	adda.w	(a0),a0
	jsr	(a0)			; jump to handler
	tst.w	d0
	bne.s	play_rts
	bra.s	play_loop

; Error in handler, skip returning to play_loop
play_error
	adda.l	#4,sp
	bra	error_bp

; Lower case note
play_note_kl
	sub.b	#32,d2			; 'a' -> 'A'

; Upper case note
play_note
	sub.b	#'A',d2
	lea	note_tab,a1
	andi.w	#$f,d2
	lsl.w	#1,d2			; note offsets
	move.w	(a1,d2.w),d1
	lea	oktave_0,a1
	moveq	#0,d2
	lea	qs_okt(a3),a2
	move.b	(a2,d7.w),d2
	mulu	#24,d2			; multiply
	adda.w	d2,a1
	moveq	#0,d2
	lea	qs_dif(a3),a2
	move.b	(a2,d7.w),d2		; get sharp/flat offset
	ext.w	d2
	clr.b	(a2,d7.w)		; ... and clear it for next note
	add.w	d2,d1			; add to note
	and.l	#$fff,d1
	move.w	(a1,d1.w),d1
play_p_1
	moveq	#set_period,d2		; load
	bra	play_word

; Play command finished, enable playing
play_end
	lea	qs_act(a3),a2		; Enable playing
	st	(a2,d7.w)
play_rts
	rts

; 'b': Next note is flat
play_lower
	lea	qs_dif(a3),a2
	move.b	#-2,(a2,d7.w)		; flats
	rts

; '#': Next note is sharp
play_higher
	lea	qs_dif(a3),a2
	move.b	#2,(a2,d7.w)		; sharps
	rts

; 'p': Pause playing (one note)
play_pause
	moveq	#0,d1			; set
	bra.s	play_p_1

; 'v': Set volume
play_volume
	bsr	get_num
	cmpi.w	#16,d1
	bhi	error_or

	moveq	#set_volume,d2
	bra	play_sub

; 'S': Synchronised wait
play_wait
	moveq	#set_susp,d1		; load
	bra	write_byte

; 'r': Activate channel
play_start
	bsr	get_num
	tst.w	d1
	beq	error_or
	andi.w	#$ff,d1
	cmp.b	qs_chan_count(a3),d1
	bhi	error_or
	subq.w	#1,d1
	moveq	#set_rel,d2		; load
	bra.s	play_sub

; 'o': Set octave
play_oktave
	bsr	get_num
	cmpi.w	#9,d1
	bhi	error_or
	lea	qs_okt(a3),a2
	move.b	d1,0(a2,d7.w)
	rts

; 'n': Noise frequency
play_noise
	bsr.s	get_num
	tst.w	d1
	beq.s	play_n_off

	andi.w	#$ff,d1
	moveq	#set_n_per,d2
	bsr.s	play_sub
	st	d1
play_n_off
	moveq	#set_noise,d2
	bra.s	play_sub

; 'l': Set note length
play_length
	bsr.s	get_num
	moveq	#set_len,d2
	bra.s	play_sub

; 'w': Set wrap curve
play_wave
	bsr.s	get_num
	cmpi.w	#15,d1
	bhi	error_bp
	moveq	#set_wave,d2
	bra.s	play_sub

; 'x': Set wrap length
play_w_length
	bsr.s	get_num
	moveq	#set_w_len,d2

; Write command and data word
play_word
	move.l	d1,-(sp)
	move.w	d2,d1
	bsr.s	write_byte
	move.l	(sp)+,d1

	tst.w	d0
	bne.s	play_rts2
	move.l	d1,-(sp)
	and.w	#$ff,d1
	bsr.s	write_byte
	move.l	(sp)+,d1
	tst.w	d0
	bne.s	play_rts2
	lsr.w	#8,d1
	and.w	#$ff,d1
	bsr.s	write_byte
play_rts2
	rts

; Write command and data byte
play_sub
	move.l	d1,-(sp)
	move.w	d2,d1
	bsr.s	write_byte
	move.l	(sp)+,d1
	tst.w	d0
	bne.s	play_rts2
;;;	   bra.s   write_byte

; Put byte d1 into queue
write_byte
	move.l	qs_work_queue(a3),a2
write_byte_1
	move.l	a3,-(sp)
	move.w	ioq.pbyt,a1
	jsr	(a1)
	move.l	(sp)+,a3
	rts

; Translate ASCII number to integer
get_num
	lea	qs_work_num(a3),a5
	moveq	#0,d1
get_loop
	move.b	(a6,a4.l),d2
	cmpi.b	#47,d2
	bls.s	get_end
	cmpi.b	#'9',d2
	bhi.s	get_end
	adda.l	#1,a4
	addq.w	#1,d1
	subq.w	#1,d6
	move.b	d2,(a5)+
	cmpi.b	#4,d1
	bne.s	get_loop
get_end
	tst.b	d1
	beq.s	get_end_all
	moveq	#0,d2
	moveq	#1,d3
get_e_1
	tst.b	d1
	beq.s	get_e_2
	moveq	#0,d4
	move.b	-(a5),d4
	sub.b	#'0',d4
	mulu	d3,d4
	add.w	d4,d2
	mulu	#10,d3
	subq.w	#1,d1
	bra.s	get_e_1
get_e_2
	move.l	d2,d1
get_end_all
	rts

; RELEASE <ch>
;
; Release all (no parameter) or a specific channel
release
	move.w	sb.gtint,a2
	jsr	(a2)
	tst.w	d3
	beq.s	ay_relse_all
	subq.w	#1,d3
	bne	error_bp
	move.w	(a6,a1.l),d1
ay_relse
	tst.b	d1
	beq.s	ay_relse_all

	bsr	get_aybas
	subq.w	#1,d1
	cmp.w	qs_chan_count(a3),d1
	bcc	error_or

	lea	qs_mem(a3),a1
	move.w	d1,d2
	lsl.w	#2,d2
	tst.l	(a1,d2.w)
	beq	error_no

	lea	qs_act(a3),a1
	st	(a1,d1.w)		; Enable playing
	moveq	#0,d0
	rts

ay_relse_all
	bsr	get_aybas
	lea	qs_act(a3),a1
	move.b	qs_chan_count(a3),d0
ay_relse_all_loop
	st	(a1)+			; Enable all channels
	subq.b	#1,d0
	bne.s	ay_relse_all_loop
	moveq	#0,d0
	rts

; val = PLAYING(ch)
;
; Check if queue of channel is currently playing
playing
	move.w	sb.gtint,a2
	jsr	(a2)
	tst.w	d0
	bne.s	playing_rts
	cmpi.w	#1,d3
	bne	error_bp

	move.w	(a6,a1.l),d1
	bsr.s	ay_tstpl
	tst.w	d0
	bne.s	playing_rts
	move.w	d1,0(a6,a1.l)
	move.l	a1,sb_arthp(a6)
	moveq	#0,d0
	moveq	#3,d4
playing_rts
	rts

ay_tstpl
	bsr	get_aybas
	andi.w	#$ff,d1
	subq.w	#1,d1
	cmp.b	qs_chan_count(a3),d1
	bcc	error_or

	move.w	d1,d2
	lea	qs_mem(a3),a2
	lsl.w	#2,d2
	tst.l	(a2,d2.w)
	beq	error_no

	lea	qs_act(a3),a2
	move.b	(a2,d1.w),d1
	andi.w	#1,d1
	moveq	#0,d0
	rts

; HOLD n
;
; Stop all (no parameter) or a specific interrupt list
hold
	move.w	sb.gtint,a2
	jsr	(a2)
	tst.w	d3
	beq.s	ay_hold_all		; No param -> hold all

	cmpi.w	#1,d3
	bne	error_bp
	move.w	(a6,a1.l),d1		; List to hold

ay_hold
	tst.b	d1
	beq.s	ay_hold_all		; List 0 -> hold all

	bsr	get_aybas
	andi.w	#$ff,d1
	subq.w	#1,d1
	cmp.b	qs_chan_count(a3),d1
	bcc	error_bp

	move.w	d1,d2
	lsl.w	#2,d2
	lea	qs_mem(a3),a1
	tst.l	(a1,d2.w)
	beq	error_no

	lea	qs_act(a3),a1
	sf	(a1,d1.w)
	moveq	#0,d0
	rts

ay_hold_all
	bsr	get_aybas
	lea	qs_act(a3),a1
	moveq	#0,d0
	move.l	d0,(a1)+		; Stop all 6 (possible) channels
	move.w	d0,(a1)+
	rts

; ENVELOPE shape, period
envelope
	move.w	sb.gtint,a2
	jsr	(a2)
	tst.w	d0
	bne.s	env_rts

	cmpi.w	#2,d3
	bne	error_bp
	move.w	0(a6,a1.l),d6		; Envelope shape
	cmpi.w	#$f,d6
	bhi	error_or
	move.w	2(a6,a1.l),d7		; Envelope period
	cmpi.w	#4095,d7
	bhi	error_or

	bsr	get_aybas
	move.w	d6,d1
	moveq	#ay_env_shape,d2
	bsr	ay_wrreg

	move.w	d7,d1
	andi.w	#$FF,d1
	moveq	#ay_env_period_l,d2
	bsr	ay_wrreg

	move.w	d7,d1
	lsr.w	#8,d1
	moveq	#ay_env_period_h,d2
	bsr	ay_wrreg

	moveq	#0,d0
env_rts
	rts


; Polled interrupt sound handler
out_sound_queue
	bsr	get_aybas
	moveq	#0,d6			; Channel within chip
	moveq	#0,d7			; Total channel
	lea	qs_regs_0(a3),a5
out_sound_loop
	cmp.b	#3,d7
	bne.s	out_no_chip_change
	lea	qs_regs_1(a3),a5
	moveq	#0,d6			; Start from channel 0 again
out_no_chip_change
	bsr.s	out_queue
	addq.w	#1,d6
	addq.w	#1,d7
	cmp.b	qs_chan_count(a3),d7
	bne.s	out_sound_loop
	rts

out_queue
	lea	qs_mem(a3),a2
	move.l	d7,d3
	lsl.w	#2,d3
	tst.l	(a2,d3.w)		; Any output queue?
	beq.s	out_rts 		; ... no, next channel

	lea	qs_act(a3),a2
	tst.b	(a2,d7.w)		; Queue enabled?
	beq.s	out_rts 		; ... no, next channel

	moveq	#0,d1
	lea	qs_count(a3),a2 	; Remaining note length
	move.b	(a2,d7.w),d1
	tst.b	d1
	beq.s	out_q_1 		; Get next queue command

	subq.w	#1,d1			; Note remains a bit longer
	move.b	d1,(a2,d7.w)
out_rts
	rts

out_q_1
	bsr	get_byte		; Next byte out of queue
	tst.w	d0
	bne.s	out_susp		; ... done, suspend output

	cmpi.b	#set_w_len,d1		; Command valdi?
	bhi.s	out_susp		; ... no, suspend output

	andi.l	#$0f,d1
	lea	int_tab,a0
	lsl.w	#1,d1
	adda.w	d1,a0
	adda.w	(a0),a0
	jmp	(a0)			; Jump to next command

out_q_end
	lea	qs_len(a3),a2
	lea	qs_count(a3),a4
	move.b	(a2,d7.w),(a4,d7.w)	; count = len
	rts

out_q_e_0
	lea	qs_count(a3),a2
	clr.b	(a2,d7.w)		; count = 0
	rts

out_period
	bsr	get_byte
	tst.w	d0
	bne.s	out_susp

	move.w	d1,d4
	bsr	get_byte
	tst.w	d0
	bne.s	out_susp

	move.w	d1,d5
	moveq	#0,d3
	move.b	d6,d3
	lsl.w	#1,d3			; Tune register low for ch <d6>
	move.w	d3,d2
	move.w	d4,d1
	bsr	write_reg

	addq.w	#1,d3			; Tune register high
	move.w	d3,d2
	move.w	d5,d1
	bsr	write_reg
	bra	out_q_end

out_len
	bsr	get_byte
	tst.w	d0
	bne.s	out_susp

	lea	qs_len(a3),a2
	move.b	d1,(a2,d7.w)
	bra	out_q_1

out_susp
	lea	qs_act(a3),a2
	sf	(a2,d7.w)		; Not playing anymore
	bra	out_q_end

out_rel
	bsr	get_byte
	tst.w	d0
	bne.s	out_susp
	andi.w	#$ff,d1

	lea	qs_act(a3),a2
	st	(a2,d1.w)		; Now playing
	bra	out_q_e_0

out_n_per
	bsr	get_byte
	tst.w	d0
	bne.s	out_susp
	moveq	#ay_noise_period,d2
	bsr	write_reg
	bra	out_q_e_0

out_volume
	bsr	get_byte
	tst.w	d0
	bne.s	out_susp
	tst.b	d1
	beq.s	out_vol_0
	moveq	#ay_amplitude_a,d2
	add.b	d6,d2			; ay_amplitude_<d6>
	bsr	write_reg

	moveq	#ay_enable,d2
	bsr	read_reg
	bclr	d6,d1			; Enable tone <d6>
	bsr	write_reg
	bra	out_q_1

out_vol_0
	moveq	#ay_amplitude_a,d2
	add.b	d6,d2			; ay_amplitude_<d6>
	bsr	write_reg

	moveq	#ay_enable,d2
	bsr	read_reg
	bset	d6,d1			; Disable tone <d6>
	bsr	write_reg
	bra	out_q_1

out_noise
	bsr.s	get_byte
	tst.w	d0
	bne.s	out_susp
	tst.b	d1
	beq.s	out_n_reset

	move.w	d6,d3			; 0..2
	addq.w	#3,d3			; 3..5
	moveq	#ay_enable,d2
	bsr	read_reg
	bclr	d3,d1			; Enable noise <d6>
	bsr	write_reg
	bra	out_q_1

out_n_reset
	move.w	d6,d3
	addq.w	#3,d3
	moveq	#ay_enable,d2
	bsr	read_reg
	bset	d3,d1			; Disable noise <d6>
	bsr	write_reg
	bra	out_q_1

out_wave
	bsr.s	get_byte
	tst.w	d0
	bne	out_susp
	moveq	#ay_env_shape,d2
	bsr	write_reg
	bra	out_q_1

out_w_len
	bsr.s	get_byte
	tst.w	d0
	bne	out_susp

	move.w	d1,d4
	bsr.s	get_byte
	tst.w	d0
	bne	out_susp

	move.w	d1,d5
	move.w	d4,d1
	moveq	#ay_env_period_l,d2
	bsr	write_reg

	move.w	d5,d1
	moveq	#ay_env_period_h,d2
	bsr	write_reg
	bra	out_q_1

get_byte
	move.l	a3,-(sp)
	lea	qs_mem(a3),a2
	moveq	#0,d5
	move.b	d7,d5
	lsl.w	#2,d5
	move.l	(a2,d5.w),a2
	move.w	ioq.gbyt,a1
	jsr	(a1)
	move.l	(sp)+,a3
	andi.l	#$ff,d1
	rts


; Machine code entry point for SOUND
;
; d1 = Channel number
; d2 = Frequency
; d3 = Volume
sound_mc
	tst.w	d1
	beq	ay_reset		; Stop all sounds

	bsr	get_aybas
	move.w	d2,d0
	or.w	d3,d0
	beq	sound_v0_mc		; Stop sound for this channel

	movem.l a0-a2/a4/d4-d7,-(sp)
	andi.l	#$fff,d2
	move.l	#93750,d4
	divu	d2,d4

	move.l	d1,d5
	move.l	d1,d2
	add.w	d2,d2			; ay_tune_a_<d5>_l
	move.b	d4,d1
	bsr	ay_wrreg

	move.w	d4,d1
	lsr.w	#8,d1
	addq.w	#1,d2			; ay_tune_a_<d5>_h
	bsr	ay_wrreg

	moveq	#ay_amplitude_a,d2
	add.w	d5,d2			; ay_amplitude_<d5>
	move.b	d3,d1
	bsr	ay_wrreg

	moveq	#ay_enable,d2
	bsr	ay_rdreg
	bclr	d5,d1			; Enable tone <d5>
	addq.w	#3,d5
	bset	d5,d1			; Disable noise <d5>
	bsr	ay_wrreg

	movem.l (sp)+,a0-a2/a4/d4-d7
	rts

; SOUND n, f, v
;
; Set sound output of channel n to frequency f with volume v
sound
	move.w	sb.gtfp,a2
	jsr	(a2)
	bsr	get_aybas
	tst.w	d3
	beq	ay_reset		; No parameter -> stop all sounds
	cmpi.w	#1,d3
	beq	sound_v0		; One parameter -> stop channel
	cmpi.w	#3,d3
	bne	error_bp
sound_all
	move.w	qa.op,a2
	moveq	#qa.int,d0
	jsr	(a2)
	tst.w	d0
	bne	sound_rts
	move.w	0(a6,a1.l),d4
	tst.w	d4
	beq	error_or
	cmpi.w	#3,d4
	bhi	error_or

	lea	qs_data(a3),a0
	subq.w	#1,d4
	move.w	d4,(a0) 		; Channel number
	subq.l	#4,a1

	move.l	#$8115B8D,0(a6,a1.l)	; 93750
	move.w	#$8000,4(a6,a1.l)
	tst.l	6(a6,a1.l)
	bne.s	sound_1
	tst.w	10(a6,a1.l)
	bne.s	sound_1
	adda.l	#12,a1
	lea	qs_data(a3),a0
	clr.w	2(a0)			; 2(a0) Frequency
	bra.s	sound_2

sound_1
	lea	qs_data(a3),a0		; Only temporary memory in this case
	move.l	0(a6,a1.l),2(a0)	; Swap TOS and NOS
	move.w	4(a6,a1.l),6(a0)
	move.l	6(a6,a1.l),0(a6,a1.l)
	move.w	10(a6,a1.l),4(a6,a1.l)
	move.l	2(a0),6(a6,a1.l)
	move.w	6(a0),10(a6,a1.l)

	move.l	(a3),-(sp)
	move.w	qa.mop,a2
	lea	operation1,a3
	jsr	(a2)
	move.l	(sp)+,a3

	tst.l	d0
	bne	sound_rts
	lea	qs_data(a3),a0
	move.w	0(a6,a1.l),2(a0)	; 2 (a0) frequency
	adda.l	#2,a1
sound_2
	move.w	qa.op,a2
	moveq	#qa.int,d0
	jsr	(a2)
	tst.l	d0
	bne.s	sound_rts

	lea	qs_data(a3),a0
	move.w	0(a6,a1.l),4(a0)	; 4(a0) volume
	cmpi.w	#4095,2(a0)
	bhi	error_or
	cmpi.w	#16,4(a0)
	bhi	error_or

	moveq	#0,d2
	move.w	(a0),d2 		; channel
	add.w	d2,d2			; ay_tune_<ch>_l
	moveq	#0,d1
	move.w	2(a0),d1		; frequency
	andi.w	#$ff,d1
	bsr	ay_wrreg

	move.w	2(a0),d1		; frequency
	lsr.w	#8,d1
	andi.w	#$F,d1
	addq.w	#1,d2			; ay_tune_<ch>_h
	bsr	ay_wrreg

	moveq	#ay_amplitude_a,d2
	add.w	(a0),d2 		; ay_amplitude_<ch>
	move.w	4(a0),d1		; volume
	bsr	ay_wrreg

	moveq	#ay_enable,d2
	bsr	ay_rdreg
	move.w	(a0),d3 		; channel
	bclr	d3,d1			; enable
	addq.w	#3,d3
	bset	d3,d1			; disable noise
	bsr	ay_wrreg

	moveq	#0,d0
sound_rts
	rts

; Stop sound for channel d1
sound_v0_mc
	move.w	d1,d4
	bra.s	sound_v0_all

sound_v0
	moveq	#qa.int,d0
	move.w	qa.op,a2
	jsr	(a2)
	tst.w	d0
	bne.s	sound_rts

	move.w	0(a6,a1.l),d4
sound_v0_all
	tst.w	d4
	beq	error_or
	cmpi.w	#3,d4
	bhi	error_or
	subq.w	#1,d4
	move.w	d4,d2
	moveq	#0,d3
	move.b	d4,d3
	add.w	d2,d2			; ay_tune_<ch>_l
	moveq	#0,d1
	bsr	ay_wrreg

	addq.w	#1,d2			; ay_tune_<ch>_h
	bsr	ay_wrreg

	move.w	d4,d2
	addq.w	#ay_amplitude_a,d2	; ay_amplitude_<ch>
	bsr	ay_wrreg

	moveq	#ay_enable,d2
	bsr	ay_rdreg
	bset	d3,d1			; silence tone
	bsr	ay_wrreg

	andi.l	#$f,d4
	sf	qs_act(a3,d4.w)

	move.w	#$4000,d1
swait
	dbra	d1,swait


	lea	qs_mem(a3),a1
	lsl.w	#2,d4
	tst.l	(a1,d4.w)
	beq	sound_rts

	move.l	(a1,d4.w),a0
	clr.l	(a1,d4.w)
	moveq	#sms.rchp,d0
	trap	#1
	moveq	#0,d0
	rts


qa_resri
	moveq	#6,d1
	move.w	qa.resri,a1
	jmp	(a1)

; version = AY_VER$
ay_ver$
	bsr.s	qa_resri
	move.l	sb_arthp(a6),a1
	subq.l	#6,a1
	move.w	#4,(a6,a1.l)
	move.l	#qsound.vers,2(a6,a1.l)
	move.l	a1,sb_arthp(a6)
	moveq	#1,d4
	moveq	#0,d0
	rts

; count = AY_CHIPS
ay_chips
	moveq	#qs_chip_count,d5
	bra.s	ay_ret_byte

; type = AY_TYPE
ay_type
	move.w	#qs_chip_type,d5
; Fall-through

; Byte index in d5
ay_ret_byte
	bsr.s	qa_resri

	bsr	get_aybas
	moveq	#0,d1
	move.w	(a3,d5.w),d1

	move.l	sb_arthp(a6),a1
	subq.l	#2,a1
	move.w	d1,(a6,a1.l)
	move.l	a1,sb_arthp(a6)
	moveq	#3,d4
	moveq	#0,d0
	rts

; Get version in d1, chip count in d2
ay_info_mc
	move.l	#qsound.vers,d1
	moveq	#0,d2
	move.b	qs_chip_count(a3),d2
	rts

; Get/set chip type
ay_type_mc
	tst.l	d1
	bmi.s	ay_type_get
	bsr	ay_hw_type
	bmi.s	ay_type_rts
	move.b	d1,qs_chip_type(a3)
ay_type_get
	moveq	#0,d1
	move.b	qs_chip_type(a3),d1
	moveq	#0,d0
ay_type_rts
	rts

; Get/set chip frequency
ay_freq_mc
	tst.l	d1
	bmi.s	ay_freq_get
	bsr	ay_hw_freq
	bmi.s	ay_freq_rts
	move.l	d1,qs_chip_freq(a3)
ay_freq_get
	move.l	qs_chip_freq(a3),d1
	moveq	#0,d0
ay_freq_rts
	rts

; Get/set stereo mode
ay_stereo_mc
	tst.l	d1
	bmi.s	ay_stereo_get
	bsr	ay_hw_stereo
	bmi.s	ay_stereo_rts
	move.b	d1,qs_stereo(a3)
ay_stereo_get
	moveq	#0,d1
	move.b	qs_stereo(a3),d1
	moveq	#0,d0
ay_stereo_rts
	rts

; Get/set volume
ay_volume_mc
	tst.l	d1
	bmi.s	ay_volume_get
	bsr	ay_hw_volume
	bmi.s	ay_volume_rts
	move.b	d1,qs_volume(a3)
ay_volume_get
	moveq	#0,d1
	move.b	qs_volume(a3),d1
	moveq	#0,d0
ay_volume_rts
	rts

; Basic definitions
proc_def
	proc_stt
	proc_ref BELL
	proc_ref EXPLODE
	proc_ref SHOOT
	proc_ref POKE_AY
	proc_ref LIST_AY
	proc_ref PLAY
	proc_ref HOLD
	proc_ref RELEASE
	proc_ref ENVELOPE
	proc_ref SOUND
	proc_end
	proc_stt
	proc_ref PEEK_AY
	proc_ref PLAYING
	proc_ref AY_CHIPS
	proc_ref AY_TYPE
	proc_end


quiet_tab	dc.b	0,0,0,0,0,0,0,$ff,0,0,0,0,0,0
explode_tab	dc.b	0,0,0,0,0,0,$1f,7,16,16,16,0,16,0
shoot_tab	dc.b	0,0,0,0,0,0,$10,7,16,16,16,0,3,0
bell_tab	dc.b	100,0,101,0,0,0,0,248,16,16,0,0,4,0
gong_tab	dc.b	60,0,61,0,63,0,0,248,16,16,16,0,9,0

; Update cmd.count when extending the table
mc_table
	dc.w	ay_reset-qsound_base	  ; 0
	dc.w	ay_wrreg-qsound_base	  ; 1
	dc.w	ay_rdreg-qsound_base	  ; 2
	dc.w	ay_wrall-qsound_base	  ; 3
	dc.w	ay_rdall-qsound_base	  ; 4
	dc.w	ay_play-qsound_base	  ; 5
	dc.w	ay_tstpl-qsound_base	  ; 6
	dc.w	ay_hold-qsound_base	  ; 7
	dc.w	ay_relse-qsound_base	  ; 8
	dc.w	ay_noise-qsound_base	  ; 9
	dc.w	sound_mc-qsound_base	  ; 10
	dc.w	ay_info_mc-qsound_base	  ; 11
	dc.w	ay_type_mc-qsound_base	  ; 12
	dc.w	ay_freq_mc-qsound_base	  ; 13
	dc.w	ay_stereo_mc-qsound_base  ; 14
	dc.w	ay_volume_mc-qsound_base  ; 15

operation1
	dc.b	qa.div,qa.int,0,0

int_tab dc.w	out_period-*
	dc.w	out_volume-*
	dc.w	out_noise-*
	dc.w	out_len-*
	dc.w	out_susp-*
	dc.w	out_rel-*
	dc.w	out_n_per-*
	dc.w	out_wave-*
	dc.w	out_w_len-*

cmd_table
	dc.b	0,0,0,1,0,0,0,0,0,0,0,0,0,0,0,0
	dc.b	0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0
	dc.b	0,12,2,12,12,12,12,12,12,0,0,0,3,0,7,8
	dc.b	9,0,6,5,0,0,4,10,11,0,0,0,0,0,0,0
	dc.b	0,13,2,13,13,13,13,13,13,0,0,0,3,0,7,8
	dc.b	9,0,6,5,0,0,4,10,11,0,0,0,0,0,0,0

jump_table
	dc.w	play_error-*
	dc.w	play_higher-*
	dc.w	play_lower-*
	dc.w	play_length-*
	dc.w	play_volume-*
	dc.w	play_wait-*
	dc.w	play_start-*
	dc.w	play_noise-*
	dc.w	play_oktave-*
	dc.w	play_pause-*
	dc.w	play_wave-*
	dc.w	play_w_length-*
	dc.w	play_note-*
	dc.w	play_note_kl-*

note_tab dc.w	18,255,0,4,8,10,14,22
okt_tab  dc.w	4000
oktave_0 dc.w	3822,3608,3405,3214,3034,2863,2703,2551,2408,2273,2145,2025
oktave_1 dc.w	1911,1804,1703,1607,1517,1432,1351,1276,1204,1136,1073,1012
oktave_2 dc.w	956,902,851,804,758,716,676,638,602,568,536,506
oktave_3 dc.w	478,451,426,402,379,358,338,319,301,284,268,253
oktave_4 dc.w	239,225,213,201,190,179,169,159,150,142,134,127
oktave_5 dc.w	119,113,106,100,95,89,84,80,75,71,67,63
oktave_6 dc.w	60,56,53,50,47,45,42,40,38,36,34,32
oktave_7 dc.w	30,28,27,25,24,22,21,20,19,18,17,16
oktave_8 dc.w	15,14,13,12,12,11,10,10,9,9,8,8
oktave_9 dc.w	7,7,6,6,6,5,5,5,4,4,4,4
	 dc.w	3

	end
