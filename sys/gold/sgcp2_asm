; SuperGoldCard ROM initialisation and I2C code. Deciphered by Marcel Kilgus

	section sgc

	xdef	gl_ext_rom
	xdef	sgc_p4e
	xdef	sgc_i2c_entry
	xdef	sgc_i2c_raw
	xdef	gl_min_reset3

	include 'dev8_keys_qdos_sms'
	include 'dev8_keys_sys'
	include 'dev8_keys_qlv'
	include 'dev8_keys_err'
	include 'dev8_sys_gold_keys'

; Initialise ROMs in the extended I/O area (SGC)
; Also, skip FLP driver initialisation when there is only 128kb RAM (GC+SGC)
gl_ext_rom
	tst.w	glx_ptch+glk.card
	bne.s	rom_gc			; GC has no extened I/O area

	move.l	#sgk_extio,a3		; scan extended I/O area for ROMs
rom_loop
	cmpi.l	#$4afb0001,(a3) 	; ROM header?
	bne.s	rom_next		; ... no, next

	cmpa.l	#$4cc000,a3		; possible mirror?
	bne.s	rom_banner		; ... no, go ahead

	move.l	#$c000,a1		; mirror of ROM at $c000?
	move.l	a3,-(sp)
	moveq	#$b,d0
rom_cmp
	cmpm.l	(a1)+,(a3)+
	dbne	d0,rom_cmp
	move.l	(sp)+,a3
	beq.s	rom_next		; ... yes, next

rom_banner
	lea	8(a3),a1
	tst.w	(a1)			; ROM banner?
	beq.s	rom_basic
	suba.l	a0,a0
	move.w	ut.wtext,a2		; ... yes, output it
	jsr	(a2)

rom_basic
	move.w	4(a3),d0		; basic extension?
	beq.s	rom_init		; ... no
	lea	(a3,d0.w),a1
	move.w	sb.inipr,a2
	jsr	(a2)			; initialise it

rom_init
	move.w	6(a3),d0		; init code?
	beq.s	rom_next		; ... no, next ROM
	move.l	a3,-(sp)
	jsr	(a3,d0.w)		; execute init code
	move.l	(sp)+,d0
	cmpa.l	d0,a3			; a3 moved ahead?
	bge.s	rom_next		; ... yes, use it
	move.l	d0,a3			; ... no, restore original a3

rom_next
	adda.w	#$4000,a3
	cmpa.l	#sgk_end,a3
	blt.s	rom_loop

rom_gc
	moveq	#sms.info,d0
	trap	#1

	cmpi.l	#$40000,sys_ramt(a0)	; 128KB RAM ("QL mode")?
	bne.s	rom_rts 		; ... no
	addq.l	#2,(sp) 		; ... yes, skip init of FLP driver!
rom_rts rts

; Not sure where this is used?
sgc_p4e
	tst.l	d0
	move	sr,d7
	move.b	d7,$11(sp)
	btst	#5,$10(sp)	       ; supervisor flag
	rts

; Waste time macro
waste	macro	r,v
	moveq	#[v],[r]
wloop[.L]
	subq.l	#1,[r]
	bne.s	wloop[.L]
	endm

; SuperGoldCard I2C version. see i2c_asm for more comments
drv_end
	move.l	d3,a3
	movem.l (sp)+,d3-d6/a0/a2/a4-a5
	rts

sgc_i2c_entry
	movem.l d3-d6/a0/a2/a4-a5,-(sp)   
	lea	drv_end,a0

; vector II_RAW at $170 pointes to here-$4000
; entry point needing no RAM at all
sgc_i2c_raw
	nop
	nop

	move.l	a3,d3
	lea	$BFDC,a4
	lea	1(a4),a5
	lea	2(a4),a2
	lea	3(a4),a3
	bra.s	new_cmd

special
	bmi.s	grpok		; skip if G (group) bit is already set
	swap	d2		; set device group data
	clr.w	d2		; zero the parameter value
grpok
	moveq	#9,d0		; counter and msb's zero
	asl.b	#2,d6		; check V bit
	bcs.s	verok		; skip if V (validate) bit is set
verlp
	tst.b	(a2)		; SDA high after 1st, set SCL low
	waste	d4,$c

	tst.b	(a3)		; SDA high, set SCL high = NAK
	waste	d4,$c

	tst.b	(a5)		; SCL high, set SDA low = START
	waste	d4,$c

	tst.b	(a3)		; SCL high, set SDA high = STOP
	waste	d4,$c

	subq.b	#1,d0
	bne.s	verlp
verok
	waste	d4,$c
	move.b	d6,d0
	lsr.b	#6,d0		: put D/C bits into register
	bcc.l	err_bp		; object to spare bit zero
	tst.b	0(a4,d0.w)	; set D/C as requested

	lsl.b	#4,d6
	bcc.s	new_cmd
	moveq	#0,d0
	jmp	(a0)		; return OK

* 101xxxxx - send start sequence plus device
dostart
	tst.b	(a3)		; SDA high, assure that SCL is high
	waste	d0,$c
	tst.b	(a5)		; SCL high, set SDA low for START
	waste	d0,$18
	move.l	d2,d4		; copy device
	lsr.l	#8,d4		; shift down to bits 15-8
	move.b	d6,d4		; copy command, bit 7 = 1, bit 6 = W/R
	add.b	d4,d4		; W/R into bit 7, X=1
	tst.b	(a4)		; SDA low, set SCL low

	roxr.w	#7,d4		; bit 9 = 1, bits 7-1 device, bit 0 = W/R
	bra.s	wdev		; go use common code

parm
	lsl.w	#7,d2		; shift any existing parameter data up
	or.b	d6,d2		; put in next seven bits
new_cmd
	exg	a1,d3
	move.b	(a1)+,d6	; get next command byte
	exg	a1,d3
	bpl.s	parm		; if msb is clear, rest is 7 bits of parameter
	asl.b	#2,d6		; check next two bits, 6=special, 5=normal
	bcs.s	special 	; 11xxxxxx - go do special command sequence
	bmi.s	dostart 	; 101xxxxx - start sequence wanted
started
	moveq	#0,d4		; flag to say started (for write addr/data)
	asl.b	#2,d6		; roll out R/W and buffer to msb
	bcs.l	reader		; go do read sequence
	bmi.s	writer		; if write from control buffer, we're ready
	exg	a1,d3		; use data buffer instead of command buffer
	bra.s	writer		; go start writing from data buffer

wloop
	move.b	(a1)+,d4	; get next byte from buffer
wdev
	clr.w	d5		; clear data offset
	add.b	d4,d4		; roll out first bit
	roxl.b	#2,d5		; set 0 for SDA low, 2 for SDA high
	addq.b	#1,d4		; 1st time set marker in lsb
wbent
	waste	d0,$4
	tst.w	0(a4,d5.w)	; SCL low, set SDA data, then set SCL high
	waste	d0,$3
	move.b	1(a4,d5.w),d0	; SDA data, SCL high, get SDAin
	bchg	d5,d0		; if SDAout is 0, toggle SDAin
	lsl.b	#7,d0		; see if SDAin = SDAout (waste a bit)
	bmi.s	wmatch		; if matched, we have may have duff ack!
	waste	d0,$5
	tst.b	(a2)		; SDA high, set SCL low

	tst.w	d4		; check if device address or NOT the ack
	bne.s	sortit		; if data write and proper ack, go do next
writer
	dbra	d2,wloop	; continue with data write
	add.b	d6,d6		; did we swap control/data buffers
	bcs.s	chkstop 	; no, skip swap
	exg	a1,d3		; put back control and data pointers
chkstop
	clr.w	d2		; reset parameter
	add.b	d6,d6		; do we want to send a stop
	beq.s	chkcmd		; spare bit(s) are clear, so command was valid
err_bp
	moveq	#err.ipar,d0
	jmp	(a0)

sortit
	tst.b	d4		; check if this was the ack for the device
	beq.s	started 	; if so, get on back to decoding this command
err_ff
	moveq	#err.fmtf,d0	; say that the i2c is not working
	; as this is disastrous, don't get worked up about timing, etc!
err_com
	tst.b	(a4)		; SCL low, set SDA low
	lsr.b	#2,d4
	bra.s	stop		; common stop code will notice d0<>0

chkcmd
	bcc.s	new_cmd 	; no stop, leave SDA high and get next command
	waste	d0,$5

	tst.b	(a4)		; send SDA low
	moveq	#0,d0		; after the stop has been sent, do next command
stop
	waste	d4,$5
	tst.b	(a5)		; SDA low, set SCL high
	waste	d4,$c
	tst.b	(a3)		; SCL high, set SDA high: STOP

	tst.l	d0		; make sure CCR is set for errors
	beq.l	new_cmd 	; go get next command
	jmp	(a0)		; go home

wbloop
	clr.w	d5		; clear data offset
	add.b	d4,d4		; roll out next bit
	roxl.b	#2,d5		; set 0 for SDA low, 2 for SDA high
	bra.s	wbent		; enter main loop

wmatch
	waste	d0,$5
	tst.b	0(a4,d5.w)	; SDA data, set SCL low

	tst.b	d4		; check if this was still a data bit
	bne.s	wbloop		; nice data bit, so keep steaming along
	moveq	#err.fdnf,d0	; if nack on a device address, just say not found
	tst.w	d4		; check if this was the device address
	bne.s	err_com 	; it was a device address, so stop all this
err_te
	moveq	#err.trns,d0	; transmission error if device nack's our data!
	bra.s	err_com

* read sequence

rloop
	waste	d0,$5
	move.b	(a3),d5 	; SDA high, set SCL high and sample SDAin

	waste	d0,$b
	tst.b	(a2)		; SDA high, set SCL low

	waste	d0,$5
	asr.b	#1,d5		; put data into X
	addx.b	d4,d4		; roll into result byte
rslow
	bcc.s	rloop		; if not at marker yet, loop
	waste	d0,$5
	tst.w	(a4)		; SCL low, set SDA low, set SCL high

	waste	d0,$5
	rol.b	#8,d6		; check if we are reading to memory (waste a bit)
	bmi.s	rmem		; if so, go put it in
	rol.l	#8,d1		; roll data return
	move.b	d4,d1		; put in byte
rrent
	waste	d0,$3
	tst.b	(a4)		; SDA low, set SCL low
	waste	d0,$3
	tst.b	(a2)		; SCL low, set SDA high
reader
	moveq	#1,d4		; set marker bit
	subq.w	#1,d2		; byte count (waste a bit)
	bcc.s	rslow		; get to rloop slower (waste a bit)
	bclr	#5,d6		; check acknowledge bit
chkstp
	bne.l	chkstop 	; final byte acknowledged, STOP not permitted
rnlp
	waste	d0,$5
	move.b	(a3),d5 	; SDA high, set SCL high and sample SDAin

	waste	d0,$b
	tst.b	(a2)		; SDA high, set SCL low

	waste	d0,$5		; balance loop
	asl.b	#8,d5		; roll data out into X (waste a bit)
	roxr.b	#8,d4		; roll SDAin into register
	bcc.s	rnlp		; if not at marker yet, loop
	waste	d0,$5
	move.b	(a3),d5 	; SDA high, set SCL high for no ack, get SDAin

	asl.b	#1,d6		; check if we are reading to memory (waste a bit)
	bcs.s	rnmem		; if so, go put it in

	waste	d0,$5
	rol.l	#8,d1		; roll data return
	move.b	d4,d1		; put in byte
rnrent
	waste	d0,$5
	tst.b	(a2)		; SDA high, set SCL low

	ror.b	#1,d5		; look at final SDAin
	bpl.l	err_te		; be annoyed if our nack was not seen!
	bra.s	chkstp		; if nack was OK, go check for stop required

rmem
	waste	d0,$3		; balance loop
	move.b	d4,(a1)+	; store this byte (slower if screen memory)
	bra.s	rrent

rnmem
	waste	d0,$3		; balance loop
	move.b	d4,(a1)+	; store last byte (slower if screen memory)
	bra.s	rnrent

; Minerva reset routine
gl_min_reset3
	move	#$2700,sr	; disable everything
	movem.l $0,a0/a1	; RESET vectors
	move.l	a0,sp
	move.l	$5c,a5		; as per the original code snipped
	jmp	(a1)

	end
